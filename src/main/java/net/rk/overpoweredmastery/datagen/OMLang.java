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
        add(OMItems.CHICKEN_WUBS.asItem(),"Music Disc Wub Plucker (Chicken Stage)");
        add(OMItems.MOVING_PROBABLE_BLOCK_ITEM.asItem(),"Moving Probable Block");
        add(OMItems.BONE_SWORD.asItem(),"Bone Sword");
        add(OMItems.PENULTIMATE_SWORD_DARK.asItem(),"Penultimate Sword (Dark Phase)");
        add(OMItems.PENULTIMATE_SWORD_LIGHT.asItem(),"Penultimate Sword (Light Phase)");

        addItemDesc(OMItems.ENDARKENED_CROSSBOW.asItem(),"The more health you have, the less rate of fire you have!");
        add("item.wub.generic_desc","Hold right click to continuously fire the musical weapon. Shifting or not holding right click stops playing the weapon.");
        addItemDesc(OMItems.RED_WUBS.asItem(),"Raiders get mad and take extra damage! Blows up small areas too!");
        addItemDesc(OMItems.GREEN_WUBS.asItem(),"Phase monsters of all types, make them dumb!");
        addItemDesc(OMItems.PURPLE_WUBS.asItem(),"Deadly to most types of objects, make em' dance!");
        addItemDesc(OMItems.CHICKEN_WUBS.asItem(),"Defeat those chicken jockeys with the power of music!");
        addItemDesc(OMItems.PENULTIMATE_SWORD_DARK.asItem(),"The Penultimate Sword has taken a void-like appearance. It takes away the darkest of effects... yet introduces a hint of trouble");
        addItemDesc(OMItems.PENULTIMATE_SWORD_LIGHT.asItem(),"The Penultimate Sword has taken a light-like appearance. It is strong, and provides amazing abilities");

        // entity names
        //add("entity.overpoweredmastery.multipurpose_vehicle","Multipurpose Vehicle");
        // subtitles accurately called: "captions"
        add("overpoweredmastery.subtitle.red_wubs","Red Wubs Burn");
        add("overpoweredmastery.subtitle.green_wubs","Green Wubs Phase");
        add("overpoweredmastery.subtitle.purple_wubs","Purple Wubs Darkens");
        add("block.thingamajigsgoodies.moving_probable_block.desc","A reward is granted based on the number of moves it does.");
        // config options
        add("overpoweredmastery.configuration.title","Overpowered Mastery Config");
        add("overpoweredmastery.configuration.chicken_wub_damage_chickens","Chicken Wub - Chicken Damage");
        add("overpoweredmastery.configuration.chicken_wub_damage_chicken_jockeys","Chicken Wub - Baby Zombie Damage");
        add("overpoweredmastery.configuration.chicken_wub_damage_baby_zombies","Chicken Wub - 'Chicken Jockey' Damage");
        add("overpoweredmastery.configuration.chicken_wub_damage_chickens.tooltip", "The base damage the Chicken Wub does to Chickens (effects projectile)");
        add("overpoweredmastery.configuration.chicken_wub_damage_chicken_jockeys.tooltip", "The base damage the Chicken Wub does to 'Chicken Jockeys' (effects projectile)");
        add("overpoweredmastery.configuration.chicken_wub_damage_baby_zombies.tooltip", "The base damage the Chicken Wub does to Baby Zombies (effects projectile)");
        add("overpoweredmastery.configuration.section.overpoweredmastery.common.toml", "Server & Client");
        add("overpoweredmastery.configuration.section.overpoweredmastery.common.toml.title", "Common Config");
        add("item.overpoweredmastery.spear.desc","Hold right click to attack enemies while running");
    }
}
