package ru.redenergy.kit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraftforge.common.MinecraftForge;
import ru.redenergy.kit.command.PowerKitCommand;
import ru.redenergy.kit.config.KitConfig;
import ru.redenergy.kit.handler.ConstructionHandler;
import ru.redenergy.kit.json.NBTDeserializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@Mod(modid = "power-kit", acceptableRemoteVersions = "*")
public class PowerKit {

    private KitConfig config;

    @Mod.Instance("power-kit")
    public static PowerKit instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws FileNotFoundException {
        ConstructionHandler handler = new ConstructionHandler();
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance().bus().register(handler);

        Gson gson = new GsonBuilder().registerTypeAdapter(NBTBase.class, new NBTDeserializer()).create();
        config = gson.fromJson(new JsonParser().parse(new JsonReader(new FileReader(new File(event.getModConfigurationDirectory(), "kit.json")))), KitConfig.class);
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event){
        event.registerServerCommand(new PowerKitCommand());
    }

    public KitConfig getConfig() {
        return config;
    }
}
