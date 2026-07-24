package net.rk.overpoweredmastery.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.block.OMBlocks;
import net.rk.overpoweredmastery.entity.OMEntityTypes;
import net.rk.overpoweredmastery.item.OMItems;

public class OMLang extends LanguageProvider {
    public OMLang(PackOutput output){
        super(output, OverpoweredMastery.MODID, "en_us");
    }

    public void addItemDesc(Item item, String description){
        add(item.getDescriptionId() + ".desc",description);
    }

    public void addEntityWithSpawnEgg(){

    }

    public void spawnEgg(Item item, EntityType<?> entityType){
        add(item,entityType.getDescription().getString() + " Spawn Egg");
    }

    @Override
    protected void addTranslations(){
        add("itemGroup.overpoweredmastery","Overpowered Mastery");

        // screens and menus
        add("screen.overpoweredmastery.multi_assembler","Multi Assembler");

        // keymappings
        add("key_mapping.overpoweredmastery.show_description","Show Description");
        // keymapping categories
        add("key.category.overpoweredmastery.general_keys","Overpowered Mastery");

        // technical entities
        // projectile entities
        add(OMEntityTypes.CHICKEN_WUB_ENERGY_BALL.get(),"Energy Ball (Plucking)");
        add(OMEntityTypes.GREEN_WUB_ENERGY_BALL.get(),"Energy Ball (Shooting)");
        add(OMEntityTypes.NETHER_WUB_ENERGY_BALL.get(),"Energy Ball (Melting)");
        add(OMEntityTypes.PURPLE_WUB_ENERGY_BALL.get(),"Energy Ball (Darkening)");
        add(OMEntityTypes.RED_WUB_ENERGY_BALL.get(),"Energy Ball (Blasting)");
        add(OMEntityTypes.TRIAL_WUB_ENERGY_BALL.get(),"Energy Ball (Testing)");
        // standard entities
        add(OMEntityTypes.FRAUD.get(),"Fraud");
        // spawn eggs
        add(OMItems.FRAUD_SPAWN_EGG.asItem(),"Fraud Spawn Egg");

        // blocks
        add(OMBlocks.MOVING_PROBABLE_BLOCK.get(),"Moving Probable Block");
        add(OMBlocks.SELECTION_BLOCK.get(),"Selection Block");
        add(OMBlocks.ULTIMATE_BLOCK.get(),"Ultimate Block");
        add(OMBlocks.ULTRA_BLOCK.get(),"Ultra Block");
        add(OMItems.ULTIMATE_BLOCK.get(),"Ultimate Block");
        add(OMItems.ULTRA_BLOCK.get(),"Ultra Block");
        // crossbows
        add(OMItems.ENDARKENED_CROSSBOW.asItem(),"Endarkened Crossbow");
        // music disc something with the ending 'er' in it
        add(OMItems.RED_WUBS.asItem(),"Music Disc Blaster (Red Stage)");
        add(OMItems.GREEN_WUBS.asItem(),"Music Disc Shooter (Green Stage)");
        add(OMItems.PURPLE_WUBS.asItem(),"Music Disc Darkener (Purple Stage)");
        add(OMItems.CHICKEN_WUBS.asItem(),"Music Disc Plucker (Chicken Stage)");
        add(OMItems.NETHER_WUBS.asItem(),"Music Disc Melter (Nether Stage)");
        add(OMItems.TRIAL_WUBS.asItem(),"Music Disc Tester (Trial Stage)");
        add(OMItems.OXIDIZED_TRIAl_WUBS.asItem(),"Music Disc Oxidizer (Oxidized Stage)");
        // block items
        add(OMItems.MOVING_PROBABLE_BLOCK_ITEM.asItem(),"Moving Probable Block");
        add(OMItems.MULTI_ASSEMBLER.asItem(),"Multi-Assembler");
        // swords
        add(OMItems.BONE_SWORD.asItem(),"Bone Sword");
        add(OMItems.PENULTIMATE_SWORD_DARK.asItem(),"Penultimate Sword (Dark Phase)");
        add(OMItems.PENULTIMATE_SWORD_LIGHT.asItem(),"Penultimate Sword (Light Phase)");
        add(OMItems.ULTRA_SWORD.asItem(),"Ultra Sword");
        add(OMItems.ULTIMATE_SWORD.asItem(),"Ultimate Sword");
        // axes
        // pickaxes
        add(OMItems.ULTIMATE_PICKAXE.asItem(),"Ultimate Pickaxe");
        add(OMItems.ULTRA_PICKAXE.asItem(),"Ultra Pickaxe");
        // shovels
        add(OMItems.ULTIMATE_SHOVEL.asItem(),"Ultimate Shovel");
        // hoes
        add(OMItems.ULTIMATE_HOE.asItem(),"Ultimate Hoe");
        // bows
        add(OMItems.ULTIMATE_BOW.asItem(),"Ultimate Bow");
        // fishing rods
        add(OMItems.ULTIMATE_FISHING_ROD.asItem(),"Ultimate Fishing Rod");
        // maces
        add(OMItems.ULTIMATE_MACE.asItem(),"Ultimate Mace");
        // staffs
        add(OMItems.PRIMITIVE_STAFF.asItem(),"Primitive Staff");
        add(OMItems.ENDERMARINE_STAFF.asItem(),"Endermarine Staff");
        add(OMItems.ULTIMATE_STAFF.asItem(),"Ultimate Staff");


        // item and other descs
        add("item.overpoweredmastery.press_desc_key","Press %s to see details");
        addItemDesc(OMItems.ENDARKENED_CROSSBOW.asItem(),"The more health you have, the less rate of fire you have!");
        add("item.wub.generic_desc","Hold right click to continuously fire the musical weapon. Shifting or not holding right click stops playing the weapon.");
        addItemDesc(OMItems.RED_WUBS.asItem(),"Raiders get mad and take extra damage! Blows up small areas too!");
        addItemDesc(OMItems.GREEN_WUBS.asItem(),"Phase monsters of all types, make them dumb!");
        addItemDesc(OMItems.PURPLE_WUBS.asItem(),"Deadly to most types of objects, make em' dance!");
        addItemDesc(OMItems.CHICKEN_WUBS.asItem(),"Defeat those chicken jockeys with the power of music!");
        addItemDesc(OMItems.NETHER_WUBS.asItem(),"Bring the heat and drop the beat on the pigs!");
        addItemDesc(OMItems.TRIAL_WUBS.asItem(),"Test your enemies with the power of music!");
        addItemDesc(OMItems.OXIDIZED_TRIAl_WUBS.asItem(),"Now oxidized, the power of music is slow, but hits hard!");
        addItemDesc(OMItems.PENULTIMATE_SWORD_DARK.asItem(),"Has a void-like appearance. It takes away the darkest of effects... yet introduces a hint of trouble");
        add("item.overpoweredmastery.penultimate_sword_dark.desc.detail","Animals and players are especially treated to ill effects...");
        addItemDesc(OMItems.PENULTIMATE_SWORD_LIGHT.asItem(),"Has a light-like appearance. It is strong, and provides amazing abilities");
        add("item.overpoweredmastery.penultimate_sword_light.desc.detail","Blocks can fly in the air, and interacting on mobs give you resistance, including other players");
        addItemDesc(OMItems.REDSTONE_BASE_COMPONENT.asItem(),"The power of redstone and the strange stone used to craft it unlocks usability outside of it's limits");
        addItemDesc(OMItems.AURORAN_PROCESSOR.asItem(),"Ancient Auroran technology makes this processor like no other");
        addItemDesc(OMItems.ESSENCE_ELECTRONIC_CORE.asItem(),"The essences emanate their power into the auroran processor and have become a magical core");
        addItemDesc(OMItems.STRANGE_STONE.asItem(),"This stone has an un-natural looking carving in it; perhaps it has a particular use");
        addItemDesc(OMItems.ULTIMATE_SWORD.asItem(),"This blade combines the best of both Penultimate swords, and more!");
        addItemDesc(OMItems.ULTIMATE_HOE.asItem(),"When shifting, tool tills and hydrates a 3x3 area, otherwise applies a bonemeal effect to plants!");
        addItemDesc(OMItems.ULTIMATE_FISHING_ROD.asItem(),"Fishing yields instant results. When the bobber hits the water, pull it in!");
        addItemDesc(OMItems.ULTIMATE_MACE.asItem(),"Supports many enchantments, like a RPG tree!");
        addItemDesc(OMItems.ULTIMATE_PICKAXE.asItem(),"Mining has never been easier, or more rewarding...");
        addItemDesc(OMItems.PENULTIMATE_SWORD_CATALYST.asItem(),"Used to craft the Penultimate Swords.");
        addItemDesc(OMItems.CONCENTRATED_MULTI_ESSENCE.asItem(),"Concentrated, but needs some lightning; any way to make it will do.");
        addItemDesc(OMItems.PENULTIMATE_PICKAXE_CATALYST.asItem(),"Used to craft the Penultimate Pickaxes.");
        addItemDesc(OMItems.PENULTIMATE_AXE_CATALYST.asItem(),"Used to craft the Penultimate Axes.");
        addItemDesc(OMItems.PENULTIMATE_SHOVEL_CATALYST.asItem(),"Used to craft the Penultimate Shovels.");
        addItemDesc(OMItems.PENULTIMATE_HOE_CATALYST.asItem(),"Used to craft the Penultimate Hoes.");
        addItemDesc(OMItems.ULTRA_INGOT.asItem(),"A step towards overpoweredness...");
        add("item.overpoweredmastery.ultra.desc","Completely overpowered...");
        add("item.overpoweredmastery.ultra.desc.detail","Combines effects of the ingredients used to craft it, including all tier abilities");

        // spears
        add(OMItems.ULTIMATE_SPEAR.asItem(),"Ultimate Spear");

        // long spears
        add(OMItems.TEST_SPEAR.asItem(),"Test Long Spear");
        add(OMItems.WOODEN_SPEAR.asItem(),"Wooden Long Spear");
        add(OMItems.STONE_SPEAR.asItem(),"Stone Long Spear");
        add(OMItems.GOLD_SPEAR.asItem(),"Gold Long Spear");
        add(OMItems.IRON_SPEAR.asItem(),"Iron Long Spear");
        add(OMItems.DIAMOND_SPEAR.asItem(),"Diamond Long Spear");
        add(OMItems.NETHERITE_SPEAR.asItem(),"Netherite Long Spear");
        add(OMItems.ULTIMATE_LONG_SPEAR.asItem(),"Ultimate Long Spear");
        // binding
        add(OMItems.WOODEN_TOOL_BINDING.asItem(),"Wooden Tool Binding");
        add(OMItems.METAL_TOOL_BINDING.asItem(),"Metal Tool Binding");
        add(OMItems.DIAMOND_TOOL_BINDING.asItem(),"Diamond Tool Binding");
        add(OMItems.NETHERITE_TOOL_BINDING.asItem(),"Netherite Tool Binding");

        // ores
        add(OMBlocks.INERT_BLUE_ESSENCE_ORE.get(),"Inert Blue Essence Ore");
        add(OMItems.INERT_BLUE_ESSENCE_ORE.asItem(),"Inert Blue Essence Ore");
        add(OMBlocks.INERT_GREEN_ESSENCE_ORE.get(),"Inert Green Essence Ore");
        add(OMItems.INERT_GREEN_ESSENCE_ORE.asItem(),"Inert Green Essence Ore");
        add(OMBlocks.INERT_YELLOW_ESSENCE_ORE.get(),"Inert Yellow Essence Ore");
        add(OMItems.INERT_YELLOW_ESSENCE_ORE.asItem(),"Inert Yellow Essence Ore");
        add(OMBlocks.INERT_ORANGE_ESSENCE_ORE.get(),"Inert Orange Essence Ore");
        add(OMItems.INERT_ORANGE_ESSENCE_ORE.asItem(),"Inert Orange Essence Ore");
        add(OMBlocks.INERT_RED_ESSENCE_ORE.get(),"Inert Red Essence Ore");
        add(OMItems.INERT_RED_ESSENCE_ORE.asItem(),"Inert Red Essence Ore");
        add(OMBlocks.DEEPSLATE_INERT_BLUE_ESSENCE_ORE.get(),"Deepslate Inert Blue Essence Ore");
        add(OMItems.DEEPSLATE_INERT_BLUE_ESSENCE_ORE.asItem(),"Deepslate Inert Blue Essence Ore");
        add(OMBlocks.DEEPSLATE_INERT_GREEN_ESSENCE_ORE.get(),"Deepslate Inert Green Essence Ore");
        add(OMItems.DEEPSLATE_INERT_GREEN_ESSENCE_ORE.asItem(),"Deepslate Inert Green Essence Ore");
        add(OMBlocks.DEEPSLATE_INERT_YELLOW_ESSENCE_ORE.get(),"Deepslate Inert Yellow Essence Ore");
        add(OMItems.DEEPSLATE_INERT_YELLOW_ESSENCE_ORE.asItem(),"Deepslate Inert Yellow Essence Ore");
        add(OMBlocks.DEEPSLATE_INERT_ORANGE_ESSENCE_ORE.get(),"Deepslate Inert Orange Essence Ore");
        add(OMItems.DEEPSLATE_INERT_ORANGE_ESSENCE_ORE.asItem(),"Deepslate Inert Orange Essence Ore");
        add(OMBlocks.DEEPSLATE_INERT_RED_ESSENCE_ORE.get(),"Deepslate Inert Red Essence Ore");
        add(OMItems.DEEPSLATE_INERT_RED_ESSENCE_ORE.asItem(),"Deepslate Inert Red Essence Ore");
        // special ores
        add(OMBlocks.INERT_LIGHT_ESSENCE_ORE.get(),"Inert Light Essence Ore");
        add(OMItems.INERT_LIGHT_ESSENCE_ORE.asItem(),"Inert Light Essence Ore");
        add(OMBlocks.INERT_AURORAN_ESSENCE_ORE.get(),"Inert Auroran Essence Ore");
        add(OMItems.INERT_AURORAN_ESSENCE_ORE.asItem(),"Inert Auroran Essence Ore");
        add(OMBlocks.INERT_DARK_ESSENCE_ORE.get(),"Inert Dark Essence Ore");
        add(OMItems.INERT_DARK_ESSENCE_ORE.asItem(),"Inert Dark Essence Ore");
        add(OMBlocks.DEEPSLATE_INERT_LIGHT_ESSENCE_ORE.get(),"Deepslate Inert Light Essence Ore");
        add(OMItems.DEEPSLATE_INERT_LIGHT_ESSENCE_ORE.asItem(),"Deepslate Inert Light Essence Ore");
        add(OMBlocks.DEEPSLATE_INERT_AURORAN_ESSENCE_ORE.get(),"Deepslate Inert Auroran Essence Ore");
        add(OMItems.DEEPSLATE_INERT_AURORAN_ESSENCE_ORE.asItem(),"Deepslate Inert Auroran Essence Ore");
        add(OMBlocks.DEEPSLATE_INERT_DARK_ESSENCE_ORE.get(),"Deepslate Inert Dark Essence Ore");
        add(OMItems.DEEPSLATE_INERT_DARK_ESSENCE_ORE.asItem(),"Deepslate Inert Dark Essence Ore");
        // essences
        add(OMItems.INERT_BLUE_ESSENCE.asItem(),"Inert Blue Essence");
        add(OMItems.INERT_GREEN_ESSENCE.asItem(),"Inert Green Essence");
        add(OMItems.INERT_YELLOW_ESSENCE.asItem(),"Inert Yellow Essence");
        add(OMItems.INERT_ORANGE_ESSENCE.asItem(),"Inert Orange Essence");
        add(OMItems.INERT_RED_ESSENCE.asItem(),"Inert Red Essence");
        add(OMItems.INERT_LIGHT_ESSENCE.asItem(),"Inert Light Essence");
        add(OMItems.INERT_AURORAN_ESSENCE.asItem(),"Inert Auroran Essence");
        add(OMItems.INERT_DARK_ESSENCE.asItem(),"Inert Dark Essence");
        // electronic and other components
        add(OMItems.AURORAN_PROCESSOR.asItem(),"Auroran Processor");
        add(OMItems.ESSENCE_ELECTRONIC_CORE.asItem(),"Essence Electronic Core");
        add(OMItems.REDSTONE_BASE_COMPONENT.asItem(),"Redstone Base Component");

        // catalysts for crafting
        add(OMItems.PENULTIMATE_SWORD_CATALYST.asItem(),"Penultimate Sword Catalyst");
        add(OMItems.PENULTIMATE_PICKAXE_CATALYST.asItem(),"Penultimate Pickaxe Catalyst");
        add(OMItems.PENULTIMATE_AXE_CATALYST.asItem(),"Penultimate Axe Catalyst");
        add(OMItems.PENULTIMATE_SHOVEL_CATALYST.asItem(),"Penultimate Shovel Catalyst");
        add(OMItems.PENULTIMATE_HOE_CATALYST.asItem(),"Penultimate Hoe Catalyst");

        // smithing templates
        add(OMItems.ULTRA_CATALYST_TEMPLATE.asItem(),"Ultra Catalyst Template");

        // misc items
        add(OMItems.STRANGE_STONE.asItem(),"Strange Stone");
        add(OMItems.ULTIMATE_INGOT.asItem(),"Ultimate Ingot");
        add(OMItems.ULTRA_INGOT.asItem(),"Ultra Ingot");
        add(OMItems.CONCENTRATED_MULTI_ESSENCE.asItem(),"Concentrated Multi Essence");
        add(OMItems.INFUSED_CONCENTRATED_MULTI_ESSENCE.asItem(),"Infused Concentrated Multi Essence");

        // enchantments
        add("enchantment.overpoweredmastery.instarepair","Instarepair");
        add("enchantment.overpoweredmastery.instarepair.desc","Repairs tools for free randomly... however is quite slow");
        add("enchantment.overpoweredmastery.bypass_denial","Bypass Denial");
        add("enchantment.overpoweredmastery.bypass_denial.desc","Damage that bypasses is denied!");
        add("enchantment.overpoweredmastery.evocation_master","Evocation Master");
        add("enchantment.overpoweredmastery.evocation_master.desc","Attacks create Evoker Fangs under foes");
        add("enchantment.overpoweredmastery.delicate_touch","Delicate Touch");
        add("enchantment.overpoweredmastery.delicate_touch.desc","Certain kinds of unobtainable blocks drop");


        add("item.overpoweredmastery.delicate_touched_item.warning","Place using offhand and tool you mined it with");
        add("item.catalyst_template.overpoweredmastery.can_make","Can make %s");
        add("item.catalyst_template.overpoweredmastery.makes_type.ultra_tools","Ultra Tools");
        add("item.catalyst_template.overpoweredmastery.makes_type.ultimate_tools","Ultimate Tools");
        add("item.overpoweredmastery.catalyst_template.requires_item_base","Requires %s in Smithing Table");
        add("overpoweredmastery.item.inert_essence.desc","Although useless in this form, some stimulation might make it do something...");
        // subtitles accurately called: "captions"
        add("overpoweredmastery.subtitle.red_wubs","Red Wubs Burn");
        add("overpoweredmastery.subtitle.green_wubs","Green Wubs Phase");
        add("overpoweredmastery.subtitle.purple_wubs","Purple Wubs Darkens");
        add("overpoweredmastery.subtitle.effect","Something Whooshes");
        // block descs
        add("block.thingamajigsgoodies.moving_probable_block.desc","A reward is granted based on the number of moves it does.");
        add("block.overpoweredmastery.selection_block.desc","INTERNAL USE ONLY. A collision-less block for travelling through.");
        // config options
        add("overpoweredmastery.configuration.title","Overpowered Mastery Config");
        add("overpoweredmastery.configuration.chicken_wub_damage_chickens","Chicken Wub - Chicken Damage");
        add("overpoweredmastery.configuration.chicken_wub_damage_chicken_jockeys","Chicken Wub - Baby Zombie Damage");
        add("overpoweredmastery.configuration.chicken_wub_damage_baby_zombies","Chicken Wub - 'Chicken Jockey' Damage");
        add("overpoweredmastery.configuration.chicken_wub_damage_chickens.tooltip", "The base damage the Chicken Wub does to Chickens (effects projectile)");
        add("overpoweredmastery.configuration.chicken_wub_damage_chicken_jockeys.tooltip", "The base damage the Chicken Wub does to 'Chicken Jockeys' (effects projectile)");
        add("overpoweredmastery.configuration.chicken_wub_damage_baby_zombies.tooltip", "The base damage the Chicken Wub does to Baby Zombies (effects projectile)");
        add("overpoweredmastery.configuration.phase_block_existence_time", "Phase Block Existence Time");
        add("overpoweredmastery.configuration.phase_block_existence_time.tooltip", "The time in ticks selection blocks can last for (standing in time not affected)");
        add("overpoweredmastery.configuration.strange_stone_drop_percentage", "Strange Stone Drop Percentage");
        add("overpoweredmastery.configuration.strange_stone_drop_percentage.tooltip", "The percentage chance that a strange stone will drop from blocks tagged 'can_drop_strange_stone'");

        add("overpoweredmastery.configuration.red_wub_energy_ball_lifetime","Red Wub Energy Ball - Lifetime");
        add("overpoweredmastery.configuration.red_wub_energy_ball_lifetime.tooltip", "The lifetime of the projectile");
        add("overpoweredmastery.configuration.chicken_wub_energy_ball_lifetime","Chicken Wub Energy Ball - Lifetime");
        add("overpoweredmastery.configuration.chicken_wub_energy_ball_lifetime.tooltip", "The lifetime of the projectile");
        add("overpoweredmastery.configuration.green_wub_energy_ball_lifetime","Green Wub Energy Ball - Lifetime");
        add("overpoweredmastery.configuration.green_wub_energy_ball_lifetime.tooltip", "The lifetime of the projectile");
        add("overpoweredmastery.configuration.weeping_wub_energy_ball_lifetime","Weeping Wub Energy Ball - Lifetime");
        add("overpoweredmastery.configuration.weeping_wub_energy_ball_lifetime.tooltip", "The lifetime of the projectile");
        add("overpoweredmastery.configuration.trial_wub_energy_ball_lifetime","Trial Wub Energy Ball - Lifetime");
        add("overpoweredmastery.configuration.trial_wub_energy_ball_lifetime.tooltip", "The lifetime of the projectile");
        add("overpoweredmastery.configuration.purple_wub_energy_ball_lifetime","Purple Wub Energy Ball - Lifetime");
        add("overpoweredmastery.configuration.purple_wub_energy_ball_lifetime.tooltip", "The lifetime of the projectile");


        add("overpoweredmastery.configuration.section.overpoweredmastery.common.toml", "Server & Client");
        add("overpoweredmastery.configuration.section.overpoweredmastery.common.toml.title", "Common Config");
        add("item.overpoweredmastery.spear.desc","Hold right click to attack enemies while running");
        add("item.overpoweredmastery.staff.desc","Right click to fire attacks at enemies");
        add("overpoweredmastery.configuration.section.overpoweredmastery.server.toml","Server");
        add("overpoweredmastery.configuration.section.overpoweredmastery.server.toml.title","Server Config");
        add("overpoweredmastery.configuration.section.overpoweredmastery.client.toml","Client");
        add("overpoweredmastery.configuration.section.overpoweredmastery.client.toml.title","Client Config");
        add("overpoweredmastery.configuration.client.mouse_ears_hax","Mouse Ears Hax");
        add("overpoweredmastery.configuration.client.mouse_ears_hax.tooltip", "Enable mouse ears for your skin like a certain player has (very janky and experimental)");
        add("overpoweredmastery.configuration.client.upside_down_hax","Upside Down Hax");
        add("overpoweredmastery.configuration.client.upside_down_hax.tooltip","Turn the player upside down all the time, alike to a certain player (very janky and experimental)");
        add("overpoweredmastery.configuration.client.upside_down_hax_y_offset","Upside Down Hax Y Offset");
        add("overpoweredmastery.configuration.client.upside_down_hax_y_offset.tooltip","Affects the Y Position of the upside down player model");
        add("overpoweredmastery.configuration.client.minecart_on_head_hax","Minecart On Head");
        add("overpoweredmastery.configuration.client.minecart_on_head_hax.tooltip","Show a minecart on your head (very janky and experimental)");
        add("overpoweredmastery.configuration.client.minecart_on_head_hax_x_rotation","Head Minecart X Rotation");
        add("overpoweredmastery.configuration.client.minecart_on_head_hax_z_rotation","Head Minecart Z Rotation");
        add("overpoweredmastery.configuration.client.minecart_on_head_hax_y_rotation","Head Minecart Y Rotation");
        add("overpoweredmastery.configuration.client.minecart_on_head_hax_x_rotation.tooltip","The X Rotation for the head minecart (very janky and experimental)");
        add("overpoweredmastery.configuration.client.minecart_on_head_hax_z_rotation.tooltip","The Z Rotation for the head minecart (very janky and experimental)");
        add("overpoweredmastery.configuration.client.minecart_on_head_hax_y_rotation.tooltip","The Y Rotation for the head minecart (very janky and experimental)");
        add("overpoweredmastery.configuration.client.mouse_ears_hax_texture_location","Mouse Ears Texture");
        add("overpoweredmastery.configuration.client.mouse_ears_hax_texture_location.tooltip","The custom texture location for the mouse ears (very janky and experimental)");
    }
}
