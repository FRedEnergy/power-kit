package ru.redenergy.kit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds information about last activation of item kit
 *
 * Structure of nbt tag looks like this:
 *  {
 *      "kits-timestamps" : [
 *          {"kit-name": "premium", "kit-timestamp": 100000000L},
 *          {"kit-name": "start", "kit-timestamp": 9999999999L}
 *      ]
 *  }
 */
public class PlayerKitData implements IExtendedEntityProperties {

    public static final String IDENTIFIER = "POWERKITDATA";

    private Map<String, Long> kitTimestamps = new HashMap<>();

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagList values = new NBTTagList();
        for(Map.Entry<String, Long> kit : kitTimestamps.entrySet()){
            NBTTagCompound  kitRecord = new NBTTagCompound();
            kitRecord.setString("kit-name", kit.getKey());
            kitRecord.setLong("kit-timestamp", kit.getValue());
            values.appendTag(kitRecord);
        }
        compound.setTag("kits-timestamps", values);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagList values = compound.getTagList("kits-timestamps", 10); //10 - nbttagcompound
        for(int i = 0; i < values.tagCount(); i++){
            NBTTagCompound kitRecord = values.getCompoundTagAt(i);
            String kitName = kitRecord.getString("kit-name");
            long timestamp = kitRecord.getLong("kit-timestamp");
            kitTimestamps.put(kitName, timestamp);
        }
    }

    public Map<String, Long> getKitTimestamps(){
        return kitTimestamps;
    }

    @Override
    public void init(Entity entity, World world) {}

    public static PlayerKitData ofPlayer(EntityPlayer player){
        return (PlayerKitData) player.getExtendedProperties(IDENTIFIER);
    }
}
