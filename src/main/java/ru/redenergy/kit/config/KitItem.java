package ru.redenergy.kit.config;

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
}
