package net.rk.overpoweredmastery.enchantment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public record EvocationMasterEffect(Optional<Holder<DamageType>> damageType) implements EnchantmentEntityEffect {
    public static final MapCodec<EvocationMasterEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    DamageType.CODEC.optionalFieldOf("damage_type").forGetter(EvocationMasterEffect::damageType)
            ).apply(instance,EvocationMasterEffect::new)
    );

    @Override
    public void apply(ServerLevel serverLevel, int i, EnchantedItemInUse enchantedItemInUse, Entity entity, Vec3 vec3) {
        if(entity != enchantedItemInUse.owner() && enchantedItemInUse.owner() != null){
            serverLevel.addFreshEntity(new EvokerFangs(serverLevel,vec3.x,vec3.y,vec3.z,180.0f,i,enchantedItemInUse.owner()));
            serverLevel.playSound(entity,new BlockPos((int)vec3.x(),(int)vec3.y(),(int)vec3.z()), SoundEvents.EVOKER_FANGS_ATTACK,SoundSource.PLAYERS);
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }

    @Override
    public Optional<Holder<DamageType>> damageType() {
        return damageType;
    }
}
