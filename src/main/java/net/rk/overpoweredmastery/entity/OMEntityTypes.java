package net.rk.overpoweredmastery.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.entity.custom.*;

import java.util.function.Supplier;

public class OMEntityTypes{
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(
            Registries.ENTITY_TYPE, OverpoweredMastery.MODID);

    public static final DeferredHolder<EntityType<?>,EntityType<RedWubEnergyBall>> RED_WUB_ENERGY_BALL = register("red_wub_energy_ball",
            () -> EntityType.Builder.<RedWubEnergyBall>of(RedWubEnergyBall::new,MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(16)
                    .noLootTable()
                    .fireImmune());

    public static final DeferredHolder<EntityType<?>,EntityType<GreenWubEnergyBall>> GREEN_WUB_ENERGY_BALL = register("green_wub_energy_ball",
            () -> EntityType.Builder.<GreenWubEnergyBall>of(GreenWubEnergyBall::new,MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(16)
                    .noLootTable()
                    .fireImmune());

    public static final DeferredHolder<EntityType<?>,EntityType<PurpleWubEnergyBall>> PURPLE_WUB_ENERGY_BALL = register("purple_wub_energy_ball",
            () -> EntityType.Builder.<PurpleWubEnergyBall>of(PurpleWubEnergyBall::new,MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(16)
                    .noLootTable()
                    .fireImmune());

    public static final DeferredHolder<EntityType<?>,EntityType<ChickenWubEnergyBall>> CHICKEN_WUB_ENERGY_BALL = register("chicken_wub_energy_ball",
            () -> EntityType.Builder.<ChickenWubEnergyBall>of(ChickenWubEnergyBall::new,MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(16)
                    .noLootTable()
                    .fireImmune());


    public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> builder) {
        return ENTITY_TYPES.register(name, () -> builder.get().build(
                ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,name))));
    }
}
