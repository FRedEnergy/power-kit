package ru.redenergy.kit.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import ru.redenergy.kit.PlayerKitData;
import ru.redenergy.kit.PowerKit;
import ru.redenergy.kit.config.Kit;
import ru.redenergy.kit.config.KitItem;
import ru.redenergy.vault.ForgeVault;

public class PowerKitCommand extends CommandBase{

    private static final String PERMISSION_NODE = "powerkit.%s";
    @Override
    public String getCommandName() {
        return "pkit";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/pkit <name>|info";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length == 0) return;
        if("info".equalsIgnoreCase(args[0])){
            displayCurrentItemNbt(sender);
        } else {
            proccessKitRequest(sender, args[0]);
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
        } else {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(sender.getCommandSenderName());
            giveKit(player, kit);
        }
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
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "This kit has been already used"));
                return;
            }
        }
        kit.getItems().stream().map(KitItem::createItemStack).forEach(player.inventory::addItemStackToInventory);
        data.getKitTimestamps().put(kit.getName(), System.currentTimeMillis());
    }


}
