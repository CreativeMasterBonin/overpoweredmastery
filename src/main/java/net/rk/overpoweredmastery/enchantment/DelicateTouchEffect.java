package net.rk.overpoweredmastery.enchantment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.phys.Vec3;
import net.rk.overpoweredmastery.datagen.OMTags;

import java.util.Optional;

public record DelicateTouchEffect(Vec3i offset, ItemPredicate itemsAllowingUse, BlockPredicate allowedBlocksPredicate, Optional<Holder<GameEvent>> triggerGameEvent) implements EnchantmentEntityEffect {
    public static final MapCodec<DelicateTouchEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Vec3i.CODEC.optionalFieldOf("offset", Vec3i.ZERO).forGetter(DelicateTouchEffect::offset),
                    ItemPredicate.CODEC.fieldOf("items_allowing_use").forGetter(DelicateTouchEffect::itemsAllowingUse),
                    BlockPredicate.CODEC.fieldOf("delicate_touch_affected").forGetter(DelicateTouchEffect::allowedBlocksPredicate),
                    GameEvent.CODEC.optionalFieldOf("trigger_game_event").forGetter(DelicateTouchEffect::triggerGameEvent)
            ).apply(instance,DelicateTouchEffect::new)
    );


    @Override
    public void onChangedBlock(ServerLevel serverLevel, int i, EnchantedItemInUse itemInUse, Entity user, Vec3 pos, boolean idk) {
        /*if(itemsAllowingUse.test(itemInUse.itemStack())){
            BlockState state = serverLevel.getBlockState(new BlockPos((int) pos.x(), (int) pos.y(), (int) pos.z()));
            if(state != null){
                Block block = state.getBlock();
                if(state.is(OMTags.SUPPORTS_DELICATE_TOUCH)){
                    if(block.asItem() == Items.AIR || block.asItem() == null){
                        return;
                    }
                    else{
                        ItemStack stackToDrop = new ItemStack(block.asItem());
                        serverLevel.addFreshEntity(new ItemEntity(serverLevel,pos.x,pos.y,pos.z,stackToDrop));
                        serverLevel.sendParticles(ParticleTypes.POOF,
                                pos.x,pos.y,pos.z,
                                11,
                                0D,0.1D,0D,
                                0.01D);
                    }
                }
            }
        }*/
    }

    @Override
    public void apply(ServerLevel serverLevel, int i, EnchantedItemInUse enchantedItemInUse, Entity entity, Vec3 vec3) {

    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
