package net.rk.overpoweredmastery.datagen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.ChangeItemDamage;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.rk.overpoweredmastery.OverpoweredMastery;

public class OMEnchantments{
    public static final ResourceKey<Enchantment> INSTAREPAIR = key("instarepair");

    public static final ResourceLocation INSTAREPAIR_ARMOR_TOUGHNESS_MODIFIER = ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,"instarepair_modifier");
    public static final ResourceLocation INSTAREPAIR_NOTICEABLE_MODIFIER = ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,"instarepair_noticeable_modifier");
    public static final ResourceLocation INSTAREPAIR_MOVEMENT_EFFECTIVENESS_MODIFIER = ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,"instarepair_movement_effectiveness_modifier");
    public static final RegistrySetBuilder enchantmentBuilder = new RegistrySetBuilder();

    public static void bootstrap(BootstrapContext<Enchantment> context){
        //HolderGetter<DamageType> holdergetter = context.lookup(Registries.DAMAGE_TYPE);
        //HolderGetter<Enchantment> holdergetter1 = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> holdergetter2 = context.lookup(Registries.ITEM);
        //HolderGetter<Block> holdergetter3 = context.lookup(Registries.BLOCK);
        //HolderGetter<EntityType<?>> holdergetter4 = context.lookup(Registries.ENTITY_TYPE);

        registerEnchantment(
                context,
                INSTAREPAIR,
                Enchantment.enchantment(
                                Enchantment.definition(
                                        holdergetter2.getOrThrow(OMTags.CAN_HAVE_INSTAREPAIR_ENCHANTMENT),
                                        1,
                                        1,
                                        Enchantment.dynamicCost(30, 10),
                                        Enchantment.dynamicCost(40, 12),
                                        50,
                                        EquipmentSlotGroup.ANY
                                )
                        )
                        .withEffect(
                                EnchantmentEffectComponents.TICK,
                                new ChangeItemDamage(LevelBasedValue.constant(-1)),
                                AllOfCondition.allOf(
                                        LootItemRandomChanceCondition.randomChance(0.02f)
                                )
                        )
        );
    }

    /*
    .withSpecialEffect(DataComponents.ATTRIBUTE_MODIFIERS,
                                ItemAttributeModifiers.builder()
                                        .add(Attributes.ARMOR_TOUGHNESS,
                                                new AttributeModifier(INSTAREPAIR_ARMOR_TOUGHNESS_MODIFIER,
                                                        2.0D,
                                                        AttributeModifier.Operation.ADD_VALUE),
                                                EquipmentSlotGroup.ARMOR)
                                        .build())
                        .withSpecialEffect(DataComponents.ATTRIBUTE_MODIFIERS,
                                ItemAttributeModifiers.builder()
                                        .add(Attributes.WAYPOINT_TRANSMIT_RANGE,
                                                new AttributeModifier(INSTAREPAIR_NOTICEABLE_MODIFIER,
                                                        16.0D,
                                                        AttributeModifier.Operation.ADD_VALUE),
                                                EquipmentSlotGroup.ANY)
                                        .build())
                        .withSpecialEffect(DataComponents.ATTRIBUTE_MODIFIERS,
                                ItemAttributeModifiers.builder()
                                        .add(Attributes.MOVEMENT_EFFICIENCY,
                                                new AttributeModifier(INSTAREPAIR_MOVEMENT_EFFECTIVENESS_MODIFIER,
                                                        4.0D,
                                                        AttributeModifier.Operation.ADD_VALUE),
                                                EquipmentSlotGroup.ANY)
                                        .build())
     */

    public static void registerEnchantment(BootstrapContext<Enchantment> context,ResourceKey<Enchantment> key,Enchantment.Builder builder) {
        context.register(key, builder.build(key.location()));
    }

    public static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT,ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,name));
    }
}
