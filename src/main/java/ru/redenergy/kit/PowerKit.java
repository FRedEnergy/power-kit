package ru.redenergy.kit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.nbt.NBTBase;
import ru.redenergy.kit.config.KitConfig;
import ru.redenergy.kit.json.NBTDeserializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@Mod(modid = "power-kit", acceptableRemoteVersions = "*")
public class PowerKit {

    private KitConfig config;

    @Mod.Instance
    public static PowerKit instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws FileNotFoundException {
        Gson gson = new GsonBuilder().registerTypeAdapter(NBTBase.class, new NBTDeserializer()).create();
        config = gson.fromJson(new JsonParser().parse(new JsonReader(new FileReader(new File(event.getModConfigurationDirectory(), "kit.json")))), KitConfig.class);
    }
}
