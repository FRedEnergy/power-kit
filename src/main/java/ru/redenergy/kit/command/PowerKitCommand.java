package ru.redenergy.kit.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.time.DurationFormatUtils;
import ru.redenergy.kit.PlayerKitData;
import ru.redenergy.kit.PowerKit;
import ru.redenergy.kit.config.Kit;
import ru.redenergy.kit.config.KitItem;
import ru.redenergy.vault.ForgeVault;

import java.util.List;
import java.util.stream.Collectors;

public class PowerKitCommand extends CommandBase{

    private static final String INFO_COMMAND = "info";
    private static final String RELOAD_COMMAND = "reload";

    private static final String PERMISSION_NODE = "powerkit.%s";
    private static final String RELOAD_PERMISSION = String.format(PERMISSION_NODE, "admin.reload");

    @Override
    public int getRequiredPermissionLevel() {
        return 1;
    }

    @Override
    public String getCommandName() {
        return "pkit";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/pkit <name>|info|reload";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length == 0) return;
        if(INFO_COMMAND.equalsIgnoreCase(args[0])){
            displayCurrentItemNbt(sender);
        } else if(RELOAD_COMMAND.equalsIgnoreCase(args[0])) {
            if(ForgeVault.getPermission().has((String)null, sender.getCommandSenderName(), RELOAD_PERMISSION)) {
                performConfigReload(sender);
            }
        } else {
            processKitRequest(sender, args[0]);
        }
    }

    private void performConfigReload(ICommandSender sender) {
        try {
            PowerKit.instance.loadConfig(PowerKit.instance.getConfigFile());
        } catch (Exception e){
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Error while reloading Power Kit, see console"));
            e.printStackTrace();
        }
    }

    private void displayCurrentItemNbt(ICommandSender sender){
        if(!(sender instanceof EntityPlayerMP)) return;
        ItemStack stack = ((EntityPlayerMP)sender).getCurrentEquippedItem();
        if(stack != null && stack.stackTagCompound != null){
            sender.addChatMessage(new ChatComponentText("Your item nbt: " + stack.stackTagCompound.toString()));
        }
    }

    private void processKitRequest(ICommandSender sender, String kitName){
        if(!(sender instanceof EntityPlayerMP)) return;
        Kit kit = PowerKit.instance.getConfig().findKit(kitName);
        if(kit == null){
            sender.addChatMessage(new ChatComponentText(String.format(PowerKit.instance.unknownKitMessage, kitName)));
            displayAvailableKits(sender);
        } else {
            giveKit(((EntityPlayerMP)sender), kit);
        }
    }

    private void displayAvailableKits(ICommandSender sender){
        List<String> collect = PowerKit.instance.getConfig().getKits().stream()
                .map(Kit::getName).collect(Collectors.toList());
        String availableKits = String.join(",", collect.toArray(new String[collect.size()]));
        sender.addChatMessage(new ChatComponentText(String.format(PowerKit.instance.availableKitsMessage, availableKits)));
    }

    private void giveKit(EntityPlayer player, Kit kit){
        if(!ForgeVault.getPermission().has((String)null, player.getCommandSenderName(), String.format(PERMISSION_NODE, kit.getName()))){
            player.addChatMessage(new ChatComponentText(PowerKit.instance.notEnoughPermissions));
            return;
        }

        PlayerKitData data = PlayerKitData.ofPlayer(player);
        if(data.getKitTimestamps().containsKey(kit.getName())){
            long sinceLastUse = System.currentTimeMillis() - data.getKitTimestamps().get(kit.getName());
            if(sinceLastUse / 1000L < kit.getInterval()){ //because interval is stored in seconds we must convert milliseconds to second by dividing them by 1000
                String timeLeft = DurationFormatUtils.formatDuration(kit.getInterval() * 1000L - sinceLastUse, "H:m");
                player.addChatMessage(new ChatComponentText(String.format(PowerKit.instance.waitMessage, timeLeft, kit.getName())));
                return;
            }
        }
        kit.getItems().stream().map(KitItem::createItemStack).forEach(player.inventory::addItemStackToInventory);
        data.getKitTimestamps().put(kit.getName(), System.currentTimeMillis());
    }



}
