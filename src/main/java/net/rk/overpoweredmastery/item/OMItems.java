package net.rk.overpoweredmastery.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.block.OMBlocks;
import net.rk.overpoweredmastery.datagen.OMTags;
import net.rk.overpoweredmastery.item.custom.*;
import net.rk.overpoweredmastery.util.OPUtil;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class OMItems{
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(OverpoweredMastery.MODID);

    public static final DeferredItem<Item> ENDARKENED_CROSSBOW = ITEMS.registerItem("endarkened_crossbow",
            EndarkenedCrossbow::new,new Item.Properties().setId(makeResourceKey("endarkened_crossbow"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("om_epic")));

    public static final DeferredItem<Item> RED_WUBS = ITEMS.registerItem("red_wubs",
            RedWubs::new,new Item.Properties().setId(makeResourceKey("red_wubs"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("red_wub")));

    public static final DeferredItem<Item> GREEN_WUBS = ITEMS.registerItem("green_wubs",
            GreenWubs::new,new Item.Properties().setId(makeResourceKey("green_wubs"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("green_wub")));

    public static final DeferredItem<Item> PURPLE_WUBS = ITEMS.registerItem("purple_wubs",
            PurpleWubs::new,new Item.Properties().setId(makeResourceKey("purple_wubs"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("purple_wub")));

    public static final DeferredItem<Item> CHICKEN_WUBS = ITEMS.registerItem("chicken_wubs",
            ChickenWubs::new,new Item.Properties().setId(makeResourceKey("chicken_wubs"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("red_wub")));

    public static final DeferredItem<Item> NETHER_WUBS = ITEMS.registerItem("nether_wubs",
            NetherWubs::new,new Item.Properties().setId(makeResourceKey("nether_wubs"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("orange_wub")));

    public static final DeferredItem<Item> TRIAL_WUBS = ITEMS.registerItem("trial_wubs",
            TrialWubs::new,new Item.Properties().setId(makeResourceKey("trial_wubs"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("orange_wub")));

    public static final DeferredItem<Item> OXIDIZED_TRIAl_WUBS = ITEMS.registerItem("oxidized_trial_wubs",
            OxidizedTrialWubs::new,new Item.Properties().setId(makeResourceKey("oxidized_trial_wubs"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("orange_wub")));


    public static final DeferredItem<Item> MOVING_PROBABLE_BLOCK_ITEM = ITEMS.registerItem("moving_probable_block",
            MovingProbableBlockItem::new,
                    new Item.Properties().setId(makeResourceKey("moving_probable_block"))
                            .component(DataComponents.TOOLTIP_STYLE,
                                    makeTooltipReference("om_epic_blue")));

    // spears
    public static final DeferredItem<Item> TEST_SPEAR = ITEMS.registerItem("test_spear",
            properties -> new SpearItem(new Item.Properties(),makeResourceKey("test_spear"),3,2.5f,
                    new Weapon(1,4),Holder.direct(SoundEvents.STONE_BREAK),
                    new Enchantable(12),
                    spearTool(BlockTags.INCORRECT_FOR_STONE_TOOL,OMTags.CORRECT_FOR_SPEAR,
                            55.0f,1.75f,
                            1,false),
                    750,Items.COBBLESTONE){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
                    super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
                    tooltipAdder.accept(Component.literal("TESTING ONLY").withStyle(ChatFormatting.RED));
                }
            });


    public static final DeferredItem<Item> WOODEN_SPEAR = ITEMS.registerItem("wooden_spear",
            properties -> new SpearItem(new Item.Properties(),makeResourceKey("wooden_spear"),1,1.1f,
                    new Weapon(2,0),Holder.direct(SoundEvents.WOOD_BREAK),
                    new Enchantable(15),
                    spearTool(BlockTags.INCORRECT_FOR_WOODEN_TOOL,OMTags.CORRECT_FOR_SPEAR,
                            10.0f,1.25f,
                            2,false),
                    59, ItemTags.WOODEN_TOOL_MATERIALS));

    public static final DeferredItem<Item> STONE_SPEAR = ITEMS.registerItem("stone_spear",
            properties -> new SpearItem(new Item.Properties(),makeResourceKey("stone_spear"),2,1.25f,
                    new Weapon(1,0),Holder.direct(SoundEvents.STONE_BREAK),
                    new Enchantable(5),
                    spearTool(BlockTags.INCORRECT_FOR_STONE_TOOL,OMTags.CORRECT_FOR_SPEAR,
                            12.0f,1.35f,
                            2,false),
                    131, ItemTags.STONE_TOOL_MATERIALS));

    public static final DeferredItem<Item> GOLD_SPEAR = ITEMS.registerItem("gold_spear",
            properties -> new SpearItem(new Item.Properties(),makeResourceKey("gold_spear"),1,2.25f,
                    new Weapon(1,0),Holder.direct(SoundEvents.STONE_BREAK),
                    new Enchantable(22),
                    spearTool(BlockTags.INCORRECT_FOR_GOLD_TOOL,OMTags.CORRECT_FOR_SPEAR,
                            20.0f,2.0f,
                            4,false),
                    32, ItemTags.GOLD_TOOL_MATERIALS));

    public static final DeferredItem<Item> IRON_SPEAR = ITEMS.registerItem("iron_spear",
            properties -> new SpearItem(new Item.Properties(),makeResourceKey("iron_spear"),2,1.1f,
                    new Weapon(1,0),Holder.direct(SoundEvents.IRON_BREAK),
                    new Enchantable(14),
                    spearTool(BlockTags.INCORRECT_FOR_IRON_TOOL,OMTags.CORRECT_FOR_SPEAR,
                            14.0f,1.4f,
                            1,false),
                    250, ItemTags.IRON_TOOL_MATERIALS));

    public static final DeferredItem<Item> DIAMOND_SPEAR = ITEMS.registerItem("diamond_spear",
            properties -> new SpearItem(new Item.Properties(),makeResourceKey("diamond_spear"),3,1.1f,
                    new Weapon(3,2),Holder.direct(SoundEvents.METAL_BREAK),
                    new Enchantable(10),
                    spearTool(BlockTags.INCORRECT_FOR_DIAMOND_TOOL,OMTags.CORRECT_FOR_SPEAR,
                            20.0f,1.7f,
                            1,false),
                    1561, ItemTags.DIAMOND_TOOL_MATERIALS));

    public static final DeferredItem<Item> NETHERITE_SPEAR = ITEMS.registerItem("netherite_spear",
            properties -> new SpearItem(new Item.Properties(),makeResourceKey("netherite_spear"),4,1.35f,
                    new Weapon(4,5),Holder.direct(SoundEvents.NETHERITE_BLOCK_BREAK),
                    new Enchantable(15),
                    spearTool(BlockTags.INCORRECT_FOR_NETHERITE_TOOL,OMTags.CORRECT_FOR_SPEAR,
                            30.0f,2.0f,
                            1,false),
                    2031,ItemTags.NETHERITE_TOOL_MATERIALS));

    public static final DeferredItem<Item> WOODEN_TOOL_BINDING = ITEMS.registerItem("wooden_tool_binding",
            Item::new, new Item.Properties().setId(makeResourceKey("wooden_tool_binding")));
    public static final DeferredItem<Item> METAL_TOOL_BINDING = ITEMS.registerItem("metal_tool_binding",
            Item::new, new Item.Properties().setId(makeResourceKey("metal_tool_binding")));
    public static final DeferredItem<Item> DIAMOND_TOOL_BINDING = ITEMS.registerItem("diamond_tool_binding",
            Item::new, new Item.Properties().setId(makeResourceKey("diamond_tool_binding")));
    public static final DeferredItem<Item> NETHERITE_TOOL_BINDING = ITEMS.registerItem("netherite_tool_binding",
            Item::new, new Item.Properties().setId(makeResourceKey("netherite_tool_binding")));



    public static final DeferredItem<Item> BONE_SWORD = ITEMS.registerItem("bone_sword",
            Item::new,
            new Item.Properties().setId(makeResourceKey("bone_sword"))
                    .stacksTo(1)
                    .durability(200)
                    .repairable(Items.BONE)
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.ATTACK_DAMAGE,
                                    new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID,2.0f,AttributeModifier.Operation.ADD_VALUE),
                                    EquipmentSlotGroup.HAND)
                            .add(Attributes.ATTACK_SPEED,
                                    new AttributeModifier(Item.BASE_ATTACK_SPEED_ID,2.5f,AttributeModifier.Operation.ADD_VALUE),
                                    EquipmentSlotGroup.HAND)
                            .build())
                    .component(DataComponents.WEAPON,new Weapon(2,1))
                    .component(DataComponents.BREAK_SOUND, Holder.direct(SoundEvents.BONE_BLOCK_BREAK))
                    .component(DataComponents.ENCHANTABLE,new Enchantable(7))
                    .component(DataComponents.BLOCKS_ATTACKS,
                            new BlocksAttacks(
                                    0.05F,
                                    0.21F,
                                    List.of(new BlocksAttacks.DamageReduction(15.0F, Optional.empty(), 0.5F, 1.0F)),
                                    new BlocksAttacks.ItemDamageFunction(4.0F, 2.0F, 2.0F),
                                    Optional.empty(),
                                    Optional.of(Holder.direct(SoundEvents.BONE_BLOCK_HIT)),
                                    Optional.of(Holder.direct(SoundEvents.BONE_BLOCK_BREAK))
                            ))
                    .component(DataComponents.TOOL,BoneSword.createToolProperties())
    );

    public static final DeferredItem<Item> PENULTIMATE_SWORD_DARK = ITEMS.registerItem("penultimate_sword_dark",
            PenultimateSwordDark::new,
            new Item.Properties().setId(makeResourceKey("penultimate_sword_dark"))
                    .stacksTo(1)
                    .durability(9999)
                    .repairable(Items.NETHERITE_BLOCK)
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.ATTACK_DAMAGE,
                                    new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID,100.0f,AttributeModifier.Operation.ADD_VALUE),
                                    EquipmentSlotGroup.HAND)
                            .add(Attributes.ATTACK_SPEED,
                                    new AttributeModifier(Item.BASE_ATTACK_SPEED_ID,15.0f,AttributeModifier.Operation.ADD_VALUE),
                                    EquipmentSlotGroup.HAND)
                            .build())
                    .component(DataComponents.WEAPON,new Weapon(1,30))
                    .component(DataComponents.BREAK_SOUND, Holder.direct(SoundEvents.ENDER_EYE_DEATH))
                    .component(DataComponents.ENCHANTABLE,new Enchantable(30))
                    .component(DataComponents.BLOCKS_ATTACKS,
                            new BlocksAttacks(
                                    0.01F,
                                    0.01F,
                                    List.of(new BlocksAttacks.DamageReduction(360.0F, Optional.empty(), 1000.0f, 1.0f)),
                                    new BlocksAttacks.ItemDamageFunction(1.0F, 1.0F, 1.0F),
                                    Optional.empty(),
                                    Optional.of(Holder.direct(SoundEvents.AMETHYST_BLOCK_RESONATE)),
                                    Optional.of(Holder.direct(SoundEvents.AMETHYST_BLOCK_BREAK))
                            ))
                    .component(DataComponents.TOOL,new Tool(
                            List.of(
                                    Tool.Rule.minesAndDrops(HolderSet.direct(Blocks.COBWEB.builtInRegistryHolder()), 45.0f)),
                            2.0f,
                            1,
                            false
                    ))
    );

    public static final DeferredItem<Item> PENULTIMATE_SWORD_LIGHT = ITEMS.registerItem("penultimate_sword_light",
            PenultimateSwordLight::new,
            new Item.Properties().setId(makeResourceKey("penultimate_sword_light"))
                    .stacksTo(1)
                    .durability(9999)
                    .repairable(Items.NETHERITE_BLOCK)
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.ATTACK_DAMAGE,
                                    new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID,200.0f,AttributeModifier.Operation.ADD_VALUE),
                                    EquipmentSlotGroup.HAND)
                            .add(Attributes.ATTACK_SPEED,
                                    new AttributeModifier(Item.BASE_ATTACK_SPEED_ID,20.0f,AttributeModifier.Operation.ADD_VALUE),
                                    EquipmentSlotGroup.HAND)
                            .build())
                    .component(DataComponents.WEAPON,new Weapon(1,35))
                    .component(DataComponents.BREAK_SOUND, Holder.direct(SoundEvents.VILLAGER_WORK_WEAPONSMITH))
                    .component(DataComponents.ENCHANTABLE,new Enchantable(32))
                    .component(DataComponents.BLOCKS_ATTACKS,
                            new BlocksAttacks(
                                    0.01F,
                                    0.01F,
                                    List.of(new BlocksAttacks.DamageReduction(360.0F, Optional.empty(), 1000.0f, 1.0f)),
                                    new BlocksAttacks.ItemDamageFunction(1.0F, 1.0F, 1.0F),
                                    Optional.empty(),
                                    Optional.of(Holder.direct(SoundEvents.AMETHYST_BLOCK_RESONATE)),
                                    Optional.of(Holder.direct(SoundEvents.AMETHYST_BLOCK_BREAK))
                            ))
                    .component(DataComponents.TOOL,new Tool(
                            List.of(
                                    Tool.Rule.minesAndDrops(HolderSet.direct(Blocks.COBWEB.builtInRegistryHolder()), 50.0f)),
                            2.25f,
                            1,
                            false
                    ))
    );

    public static final DeferredItem<Item> ULTIMATE_STAFF = ITEMS.registerItem("ultimate_staff",
            UltimateStaff::new,
            new Item.Properties().setId(makeResourceKey("ultimate_staff"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("om_ultimate"))
                    .component(DataComponents.BREAK_SOUND, Holder.direct(SoundEvents.TRIAL_SPAWNER_BREAK))
                    .component(DataComponents.TOOL,new Tool(
                            List.of(),
                            0.1f,
                            1,
                            false
                    )));

    // ore items
    // blue
    public static final DeferredItem<Item> INERT_BLUE_ESSENCE_ORE = ITEMS.registerItem("inert_blue_essence_ore",
            properties -> new BlockItem(OMBlocks.INERT_BLUE_ESSENCE_ORE.get(),properties
                    .setId(makeResourceKey("inert_blue_essence_ore"))));
    public static final DeferredItem<Item> INERT_BLUE_ESSENCE = ITEMS.registerItem("inert_blue_essence",
            InertEssence::new,new Item.Properties()
                    .setId(makeResourceKey("inert_blue_essence")));
    // green
    public static final DeferredItem<Item> INERT_GREEN_ESSENCE_ORE = ITEMS.registerItem("inert_green_essence_ore",
            properties -> new BlockItem(OMBlocks.INERT_GREEN_ESSENCE_ORE.get(),properties
                    .setId(makeResourceKey("inert_green_essence_ore"))));
    public static final DeferredItem<Item> INERT_GREEN_ESSENCE = ITEMS.registerItem("inert_green_essence",
            InertEssence::new,new Item.Properties()
                    .setId(makeResourceKey("inert_green_essence")));
    // yellow
    public static final DeferredItem<Item> INERT_YELLOW_ESSENCE_ORE = ITEMS.registerItem("inert_yellow_essence_ore",
            properties -> new BlockItem(OMBlocks.INERT_YELLOW_ESSENCE_ORE.get(),properties
                    .setId(makeResourceKey("inert_yellow_essence_ore"))));
    public static final DeferredItem<Item> INERT_YELLOW_ESSENCE = ITEMS.registerItem("inert_yellow_essence",
            InertEssence::new,new Item.Properties()
                    .setId(makeResourceKey("inert_yellow_essence")));
    // orange
    public static final DeferredItem<Item> INERT_ORANGE_ESSENCE_ORE = ITEMS.registerItem("inert_orange_essence_ore",
            properties -> new BlockItem(OMBlocks.INERT_ORANGE_ESSENCE_ORE.get(),properties
                    .setId(makeResourceKey("inert_orange_essence_ore"))));
    public static final DeferredItem<Item> INERT_ORANGE_ESSENCE = ITEMS.registerItem("inert_orange_essence",
            InertEssence::new,new Item.Properties()
                    .setId(makeResourceKey("inert_orange_essence")));
    // red
    public static final DeferredItem<Item> INERT_RED_ESSENCE_ORE = ITEMS.registerItem("inert_red_essence_ore",
            properties -> new BlockItem(OMBlocks.INERT_RED_ESSENCE_ORE.get(),properties
                    .setId(makeResourceKey("inert_red_essence_ore"))));
    public static final DeferredItem<Item> INERT_RED_ESSENCE = ITEMS.registerItem("inert_red_essence",
            InertEssence::new,new Item.Properties()
                    .setId(makeResourceKey("inert_red_essence")));
    // special ore items
    // light
    public static final DeferredItem<Item> INERT_LIGHT_ESSENCE_ORE = ITEMS.registerItem("inert_light_essence_ore",
            properties -> new BlockItem(OMBlocks.INERT_LIGHT_ESSENCE_ORE.get(),properties
                    .setId(makeResourceKey("inert_light_essence_ore"))));
    public static final DeferredItem<Item> INERT_LIGHT_ESSENCE = ITEMS.registerItem("inert_light_essence",
            InertEssence::new,new Item.Properties()
                    .setId(makeResourceKey("inert_light_essence")));
    // auroran
    public static final DeferredItem<Item> INERT_AURORAN_ESSENCE_ORE = ITEMS.registerItem("inert_auroran_essence_ore",
            properties -> new BlockItem(OMBlocks.INERT_AURORAN_ESSENCE_ORE.get(),properties
                    .setId(makeResourceKey("inert_auroran_essence_ore"))));
    public static final DeferredItem<Item> INERT_AURORAN_ESSENCE = ITEMS.registerItem("inert_auroran_essence",
            InertEssence::new,new Item.Properties()
                    .setId(makeResourceKey("inert_auroran_essence")));
    // dark
    public static final DeferredItem<Item> INERT_DARK_ESSENCE_ORE = ITEMS.registerItem("inert_dark_essence_ore",
            properties -> new BlockItem(OMBlocks.INERT_DARK_ESSENCE_ORE.get(),properties
                    .setId(makeResourceKey("inert_dark_essence_ore"))));
    public static final DeferredItem<Item> INERT_DARK_ESSENCE = ITEMS.registerItem("inert_dark_essence",
            InertEssence::new,new Item.Properties()
                    .setId(makeResourceKey("inert_dark_essence")));
    // deepslate ore items
    public static final DeferredItem<Item> DEEPSLATE_INERT_BLUE_ESSENCE_ORE = ITEMS.registerItem("deepslate_inert_blue_essence_ore",
            properties -> new BlockItem(OMBlocks.DEEPSLATE_INERT_BLUE_ESSENCE_ORE.get(),properties
                    .setId(makeResourceKey("deepslate_inert_blue_essence_ore"))));

    public static final DeferredItem<Item> DEEPSLATE_INERT_GREEN_ESSENCE_ORE = ITEMS.registerItem("deepslate_inert_green_essence_ore",
            properties -> new BlockItem(OMBlocks.DEEPSLATE_INERT_GREEN_ESSENCE_ORE.get(),properties
                    .setId(makeResourceKey("deepslate_inert_green_essence_ore"))));

    public static final DeferredItem<Item> DEEPSLATE_INERT_YELLOW_ESSENCE_ORE = ITEMS.registerItem("deepslate_inert_yellow_essence_ore",
            properties -> new BlockItem(OMBlocks.DEEPSLATE_INERT_YELLOW_ESSENCE_ORE.get(),properties
                    .setId(makeResourceKey("deepslate_inert_yellow_essence_ore"))));

    public static final DeferredItem<Item> DEEPSLATE_INERT_ORANGE_ESSENCE_ORE = ITEMS.registerItem("deepslate_inert_orange_essence_ore",
            properties -> new BlockItem(OMBlocks.DEEPSLATE_INERT_ORANGE_ESSENCE_ORE.get(),properties
                    .setId(makeResourceKey("deepslate_inert_orange_essence_ore"))));

    public static final DeferredItem<Item> DEEPSLATE_INERT_RED_ESSENCE_ORE = ITEMS.registerItem("deepslate_inert_red_essence_ore",
            properties -> new BlockItem(OMBlocks.DEEPSLATE_INERT_RED_ESSENCE_ORE.get(),properties
                    .setId(makeResourceKey("deepslate_inert_red_essence_ore"))));

    public static final DeferredItem<Item> DEEPSLATE_INERT_LIGHT_ESSENCE_ORE = ITEMS.registerItem("deepslate_inert_light_essence_ore",
            properties -> new BlockItem(OMBlocks.DEEPSLATE_INERT_LIGHT_ESSENCE_ORE.get(),properties
                    .setId(makeResourceKey("deepslate_inert_light_essence_ore"))));

    public static final DeferredItem<Item> DEEPSLATE_INERT_AURORAN_ESSENCE_ORE = ITEMS.registerItem("deepslate_inert_auroran_essence_ore",
            properties -> new BlockItem(OMBlocks.DEEPSLATE_INERT_AURORAN_ESSENCE_ORE.get(),properties
                    .setId(makeResourceKey("deepslate_inert_auroran_essence_ore"))));

    public static final DeferredItem<Item> DEEPSLATE_INERT_DARK_ESSENCE_ORE = ITEMS.registerItem("deepslate_inert_dark_essence_ore",
            properties -> new BlockItem(OMBlocks.DEEPSLATE_INERT_DARK_ESSENCE_ORE.get(),properties
                    .setId(makeResourceKey("deepslate_inert_dark_essence_ore"))));


    // electronic components
    public static final DeferredItem<Item> ESSENCE_ELECTRONIC_CORE = ITEMS.registerItem("essence_electronic_core",
            properties -> new Item(properties){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
                    tooltipAdder.accept(Component.translatable("item.overpoweredmastery.essence_electronic_core.desc").withStyle(ChatFormatting.GRAY));
                }
            },new Item.Properties()
                    .setId(makeResourceKey("essence_electronic_core")));
    public static final DeferredItem<Item> AURORAN_PROCESSOR = ITEMS.registerItem("auroran_processor",
            properties -> new Item(properties){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
                    tooltipAdder.accept(Component.translatable("item.overpoweredmastery.auroran_processor.desc").withStyle(ChatFormatting.GRAY));
                }
            },new Item.Properties()
                    .setId(makeResourceKey("auroran_processor")));
    public static final DeferredItem<Item> REDSTONE_BASE_COMPONENT = ITEMS.registerItem("redstone_base_component",
            properties -> new Item(properties){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
                    tooltipAdder.accept(Component.translatable("item.overpoweredmastery.redstone_base_component.desc").withStyle(ChatFormatting.GRAY));
                }
            },new Item.Properties()
                    .setId(makeResourceKey("redstone_base_component")));
    public static final DeferredItem<Item> STRANGE_STONE = ITEMS.registerItem("strange_stone",
            properties -> new Item(properties){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
                    tooltipAdder.accept(Component.translatable("item.overpoweredmastery.strange_stone.desc").withStyle(ChatFormatting.GRAY));
                }
            },new Item.Properties()
                    .setId(makeResourceKey("strange_stone")));

    // ultimate items
    public static final DeferredItem<Item> ULTIMATE_INGOT = ITEMS.registerItem("ultimate_ingot",
            Item::new,new Item.Properties()
                    .setId(makeResourceKey("ultimate_ingot")).fireResistant()
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("om_ultimate")).rarity(OMRarity.ULTIMATE.getValue()));

    public static final DeferredItem<Item> ULTIMATE_BOW = ITEMS.registerItem("ultimate_bow",
            UltimateBow::new,
            new Item.Properties().setId(makeResourceKey("ultimate_bow"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("om_ultimate")));

    public static final DeferredItem<Item> ULTIMATE_SWORD = ITEMS.registerItem("ultimate_sword",
            UltimateSword::new,
            new Item.Properties().setId(makeResourceKey("ultimate_sword"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("om_ultimate")));

    public static final DeferredItem<Item> ULTIMATE_HOE = ITEMS.registerItem("ultimate_hoe",
            UltimateHoe::new,
            new Item.Properties().setId(makeResourceKey("ultimate_hoe"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("om_ultimate")));

    public static final DeferredItem<Item> ULTIMATE_FISHING_ROD = ITEMS.registerItem("ultimate_fishing_rod",
            UltimateFishingRod::new,
            new Item.Properties().setId(makeResourceKey("ultimate_fishing_rod"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("om_ultimate")));


    // ultra items
    public static final DeferredItem<Item> ULTRA_INGOT = ITEMS.registerItem("ultra_ingot",
            Item::new,new Item.Properties()
                    .setId(makeResourceKey("ultra_ingot")).fireResistant()
                    .rarity(OMRarity.ULTRA.getValue()));

    public static final DeferredItem<Item> ULTRA_SWORD = ITEMS.registerItem("ultra_sword",
            UltraSword::new,
            new Item.Properties().setId(makeResourceKey("ultra_sword"))
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.ATTACK_DAMAGE,
                                    new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID,99999.0f,AttributeModifier.Operation.ADD_VALUE),
                                    EquipmentSlotGroup.HAND)
                            .add(Attributes.ATTACK_SPEED,
                                    new AttributeModifier(Item.BASE_ATTACK_SPEED_ID,99.0f,AttributeModifier.Operation.ADD_VALUE),
                                    EquipmentSlotGroup.HAND)
                            .build())
                    .component(DataComponents.WEAPON,new Weapon(1,60))
                    .component(DataComponents.BREAK_SOUND, Holder.direct(SoundEvents.TRIAL_SPAWNER_AMBIENT_OMINOUS))
                    .component(DataComponents.ENCHANTABLE,new Enchantable(120))
                    .component(DataComponents.BLOCKS_ATTACKS,
                            new BlocksAttacks(
                                    0.01f,
                                    0.95f,
                                    List.of(new BlocksAttacks.DamageReduction(360.0F, Optional.empty(), 900.5F, 1.0F)),
                                    new BlocksAttacks.ItemDamageFunction(45.0F, 90.0F, 90.0F),
                                    Optional.empty(),
                                    Optional.of(Holder.direct(SoundEvents.TRIAL_SPAWNER_HIT)),
                                    Optional.of(Holder.direct(SoundEvents.TRIAL_SPAWNER_CLOSE_SHUTTER))
                            ))
                    .component(DataComponents.TOOL,new Tool(
                            List.of(),
                            3.0f,
                            1,
                            false
                    ))
    );

    public static final DeferredItem<Item> PLACEHOLDER_ITEM = ITEMS.registerItem("placeholder_item",
            PlaceholderItem::new,
            new Item.Properties().setId(makeResourceKey("placeholder_item")));

    public static final DeferredItem<Item> CONCENTRATED_MULTI_ESSENCE = ITEMS.registerItem("concentrated_multi_essence",
            Item::new,
            new Item.Properties().setId(makeResourceKey("concentrated_multi_essence"))
                    .fireResistant());



    // machines and devices
    public static final DeferredItem<Item> MULTI_ASSEMBLER = ITEMS.registerItem("multi_assembler",
            properties -> new BlockItem(OMBlocks.MULTI_ASSEMBLER.get(),
                    properties.setId(makeResourceKey("multi_assembler"))
                            .rarity(Rarity.EPIC).fireResistant()));





    public static Tool spearTool(TagKey<Block> incorrectDrops, TagKey<Block> mineBlocks, float mineSpeed, float defaultMineSpeed, int usesUsedPerBlock, boolean destroyBlocksInCreative){
        HolderGetter<Block> holdergetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
        return new Tool(
                List.of(
                        Tool.Rule.minesAndDrops(holdergetter.getOrThrow(mineBlocks),mineSpeed),
                        Tool.Rule.overrideSpeed(holdergetter.getOrThrow(BlockTags.SWORD_INSTANTLY_MINES),Float.MAX_VALUE),
                        Tool.Rule.overrideSpeed(holdergetter.getOrThrow(BlockTags.SWORD_EFFICIENT),1.15f)
                ),
                defaultMineSpeed,
                usesUsedPerBlock,
                destroyBlocksInCreative
        );
    }

    public static ResourceKey<Item> makeResourceKey(String name){
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,name));
    }

    public static ResourceLocation makeTooltipReference(String tooltipName){
        return ResourceLocation.parse(OverpoweredMastery.MODID + ":" + tooltipName);
    }
}
