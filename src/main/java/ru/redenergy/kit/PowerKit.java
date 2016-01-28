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
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
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
    private File configFile;

    //because it is server side only mod we can't use Forge localization API and must load messages from config
    public String unknownKitMessage = EnumChatFormatting.RED + "Unknown kit `%s`";
    public String availableKitsMessage = EnumChatFormatting.GOLD + "Available kits: %s";
    public String notEnoughPermissions = EnumChatFormatting.RED + "Not enough permissions";
    public String waitMessage = EnumChatFormatting.RED + "You have to wait %s hours before using `%s` kit again";

    @Mod.Instance("power-kit")
    public static PowerKit instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws FileNotFoundException {
        ConstructionHandler handler = new ConstructionHandler();
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance().bus().register(handler);

        configFile = new File(event.getModConfigurationDirectory(), "kit.json");
        loadConfig(configFile);
        File modConfigFile = event.getSuggestedConfigurationFile();
        loadLocalization(modConfigFile);
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event){
        event.registerServerCommand(new PowerKitCommand());
    }

    public void loadLocalization(File file){
        Configuration configuration = new Configuration(file);
        this.unknownKitMessage = configuration.getString("unknownKitMessage", "PowerKit", unknownKitMessage, "Unknown kit message");
        this.availableKitsMessage = configuration.getString("availableKitsMessage", "PowerKit", availableKitsMessage, "List of available kits");
        this.notEnoughPermissions = configuration.getString("notEnoughPermissionsMessage", "PowerKit", notEnoughPermissions, "Not enough permissions message");
        this.waitMessage = configuration.getString("waitMessage", "PowerKit", waitMessage, "Wait message");
        configuration.save();
    }


    /**
     * Loads kit config from given json file <br>
     * Take a look at README.md for more information about config structure
     */
    public void loadConfig(File file) throws FileNotFoundException {
        Gson gson = new GsonBuilder().registerTypeAdapter(NBTBase.class, new NBTDeserializer()).create();
        config = gson.fromJson(new FileReader(file), KitConfig.class);
    }

    public KitConfig getConfig() {
        return config;
    }

    public File getConfigFile() {
        return configFile;
    }
}
