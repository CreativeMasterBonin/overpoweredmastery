package net.rk.overpoweredmastery.item;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
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
import net.rk.overpoweredmastery.datagen.OMTags;
import net.rk.overpoweredmastery.item.custom.*;

import java.util.List;
import java.util.Optional;

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

    public static final DeferredItem<Item> MOVING_PROBABLE_BLOCK_ITEM = ITEMS.registerItem("moving_probable_block",
            MovingProbableBlockItem::new,
                    new Item.Properties().setId(makeResourceKey("moving_probable_block"))
                            .component(DataComponents.TOOLTIP_STYLE,
                                    makeTooltipReference("om_epic_blue")));

    public static final DeferredItem<Item> TEST_SPEAR = ITEMS.registerItem("test_spear",
            properties -> new SpearItem(new Item.Properties(),makeResourceKey("test_spear"),3,2.5f,
                    new Weapon(1,4),Holder.direct(SoundEvents.STONE_BREAK),
                    new Enchantable(12),
                    spearTool(BlockTags.INCORRECT_FOR_STONE_TOOL,OMTags.CORRECT_FOR_SPEAR,
                            55.0f,1.75f,
                            1,false),
                    750,Items.COBBLESTONE));

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
