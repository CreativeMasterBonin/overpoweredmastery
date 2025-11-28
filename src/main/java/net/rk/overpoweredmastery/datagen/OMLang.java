package net.rk.overpoweredmastery.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.block.OMBlocks;
import net.rk.overpoweredmastery.item.OMItems;

public class OMLang extends LanguageProvider {
    public OMLang(PackOutput output){
        super(output, OverpoweredMastery.MODID, "en_us");
    }

    public void addItemDesc(Item item, String description){
        add(item.getDescriptionId() + ".desc",description);
    }

    @Override
    protected void addTranslations(){
        add("itemGroup.overpoweredmastery","Overpowered Mastery");

        add(OMBlocks.MOVING_PROBABLE_BLOCK.get(),"Moving Probable Block");

        add(OMItems.ENDARKENED_CROSSBOW.asItem(),"Endarkened Crossbow");
        add(OMItems.RED_WUBS.asItem(),"Music Disc Wub Blaster (Red Stage)");
        add(OMItems.GREEN_WUBS.asItem(),"Music Disc Wub Shooter (Green Stage)");
        add(OMItems.PURPLE_WUBS.asItem(),"Music Disc Wub Darkener (Purple Stage)");
        add(OMItems.CHICKEN_WUBS.asItem(),"Music Disc Wub Layer (Chicken Stage)");
        add(OMItems.MOVING_PROBABLE_BLOCK_ITEM.asItem(),"Moving Probable Block");

        addItemDesc(OMItems.ENDARKENED_CROSSBOW.asItem(),"The more health you have, the less rate of fire you have!");
        add("item.wub.generic_desc","Hold right click to fire the musical weapon. Shift to stop playing the weapon.");
        addItemDesc(OMItems.RED_WUBS.asItem(),"Raiders get mad and take extra damage! Blows up small areas too!");
        addItemDesc(OMItems.GREEN_WUBS.asItem(),"Phase monsters of all types, make them dumb!");
        addItemDesc(OMItems.PURPLE_WUBS.asItem(),"Deadly to most types of objects, make em' dance!");
        addItemDesc(OMItems.CHICKEN_WUBS.asItem(),"Defeat those chicken jockeys with the power of music!");

        // entity names
        add("entity.overpoweredmastery.multipurpose_vehicle","Multipurpose Vehicle");
        // subtitles accurately called: "captions"
        add("overpoweredmastery.subtitle.red_wubs","Red Wubs Burn");
        add("overpoweredmastery.subtitle.green_wubs","Green Wubs Phase");
        add("overpoweredmastery.subtitle.purple_wubs","Purple Wubs Darkens");
        add("block.thingamajigsgoodies.moving_probable_block.desc","A reward is granted based on the number of moves it does.");
    }
}
