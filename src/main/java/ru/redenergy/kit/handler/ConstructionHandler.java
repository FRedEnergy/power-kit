package ru.redenergy.kit.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import ru.redenergy.kit.PlayerKitData;

/**
 * With class will prepare PlayerKitData for us and will prevent is from disappearing after player death
 */
public class ConstructionHandler {

    /**
     * We need to register our data (even if it's empty) when player is just created
     */
    @SubscribeEvent
    public void constriction(EntityEvent.EntityConstructing event){
        if(event.entity instanceof EntityPlayer){
            if(PlayerKitData.ofPlayer((EntityPlayer) event.entity) == null){
                ((EntityPlayer) event.entity).registerExtendedProperties(PlayerKitData.IDENTIFIER, new PlayerKitData());
            }
        }
    }

    /**
     * This event ussually fired when player dies and new player is created <br/>
     * At that moment we must transfer data from dead player to newly created
     */
    @SubscribeEvent
    public void onClone(PlayerEvent.Clone event){
        NBTTagCompound tag = new NBTTagCompound();
        PlayerKitData.ofPlayer(event.original).saveNBTData(tag);
        PlayerKitData.ofPlayer(event.entityPlayer).loadNBTData(tag);
    }
}
