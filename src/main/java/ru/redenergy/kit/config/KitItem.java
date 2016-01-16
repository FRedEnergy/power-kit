package ru.redenergy.kit.config;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class KitItem {

    private String item;

    private int metadata;

    private int amount;

    private NBTBase nbt;

    public KitItem(String item, int metadata, int amount, NBTBase nbt) {
        this.item = item;
        this.metadata = metadata;
        this.amount = amount;
        this.nbt = nbt;
    }

    public String getItem() {
        return item;
    }

    public int getMetadata() {
        return metadata;
    }

    public int getAmount() {
        return amount;
    }

    public NBTBase getNBT() {
        return nbt;
    }

    public ItemStack createItemStack(){
        String[] identifier = item.split(":");
        Item item = GameRegistry.findItem(identifier[0], identifier[1]);
        ItemStack stack = new ItemStack(item, amount, metadata);
        stack.stackTagCompound = (NBTTagCompound) nbt;
        return stack;
    }
}
