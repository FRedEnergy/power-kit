package ru.redenergy.kit.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;

import java.lang.reflect.Type;

public class NBTDeserializer implements JsonDeserializer<NBTBase> {
    @Override
    public NBTBase deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return json != null ? JsonToNBT.func_150315_a(json.toString()) : null;
        } catch (NBTException e) {
            e.printStackTrace();
        }
        return null;
    }
}
