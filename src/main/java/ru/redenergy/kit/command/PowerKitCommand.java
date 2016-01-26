package ru.redenergy.kit.command;

import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.time.DurationFormatUtils;
import ru.redenergy.kit.PlayerKitData;
import ru.redenergy.kit.PowerKit;
import ru.redenergy.kit.config.Kit;
import ru.redenergy.kit.config.KitItem;
import ru.redenergy.vault.ForgeVault;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PowerKitCommand extends CommandBase{

    private static final String PERMISSION_NODE = "powerkit.%s";

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
        if("info".equalsIgnoreCase(args[0])){
            displayCurrentItemNbt(sender);
        } else if("reload".equalsIgnoreCase(args[0])) {
            if(ForgeVault.getPermission().has((String)null, sender.getCommandSenderName(), "powerkit.admin.reload")) {
                performConfigReload(sender);
            }
        } else {
            proccessKitRequest(sender, args[0]);
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
        ItemStack stack = MinecraftServer.getServer().getConfigurationManager().func_152612_a(sender.getCommandSenderName()).getCurrentEquippedItem();
        if(stack != null && stack.stackTagCompound != null){
            sender.addChatMessage(new ChatComponentText("Your item nbt: " + stack.stackTagCompound.toString()));
        }
    }

    private void proccessKitRequest(ICommandSender sender, String kitName){
        Kit kit = PowerKit.instance.getConfig().findKit(kitName);
        if(kit == null){
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Unknown kit `" + kitName +"`"));
            displayAvailableKits(sender);
        } else {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(sender.getCommandSenderName());
            giveKit(player, kit);
        }
    }

    private void displayAvailableKits(ICommandSender sender){
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "Available kits: " +
                String.join(",", PowerKit.instance.getConfig().getKits().stream()
                        .map(it -> it.getName()).collect(Collectors.toList())
                        .toArray(new String[0]))));
    }

    private void giveKit(EntityPlayer player, Kit kit){
        if(!ForgeVault.getPermission().has((String)null, player.getCommandSenderName(), String.format(PERMISSION_NODE, kit.getName()))){
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Not enough permissions"));
            return;
        }

        PlayerKitData data = PlayerKitData.ofPlayer(player);
        if(data.getKitTimestamps().containsKey(kit.getName())){
            long sinceLastUse = System.currentTimeMillis() - data.getKitTimestamps().get(kit.getName());
            if(sinceLastUse / 1000L < kit.getInterval()){ //because interval is stored in seconds we must convert milliseconds to second by dividing them by 1000
                player.addChatMessage(new ChatComponentText
                        (EnumChatFormatting.RED + "You have to wait " +
                        DurationFormatUtils.formatDuration(kit.getInterval() * 1000L - sinceLastUse, "H:m:s") +
                        " before using `" + kit.getName() +"` kit again"));
                return;
            }
        }
        kit.getItems().stream().map(KitItem::createItemStack).forEach(player.inventory::addItemStackToInventory);
        data.getKitTimestamps().put(kit.getName(), System.currentTimeMillis());
    }



}
