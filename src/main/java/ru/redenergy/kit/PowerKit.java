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

/**
 * @author RedEnergy <forwot163@gmail.com>
 *
 * Power Kit Mod
 *
 * Minecraft mod based on Forge Mod Loader which allows administrators to define item kits
 */
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

        loadConfig(new File(event.getModConfigurationDirectory(), "kit.json"));
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event){
        event.registerServerCommand(new PowerKitCommand());
    }

    /**
     * Loads kit config from given json file <br>
     * Take a look at README.md for more information about config structure
     */
    private void loadConfig(File file) throws FileNotFoundException {
        Gson gson = new GsonBuilder().registerTypeAdapter(NBTBase.class, new NBTDeserializer()).create();
        config = gson.fromJson(new FileReader(file), KitConfig.class);
    }

    public KitConfig getConfig() {
        return config;
    }
}
