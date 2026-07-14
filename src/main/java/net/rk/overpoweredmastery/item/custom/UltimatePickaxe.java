package net.rk.overpoweredmastery.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.rk.overpoweredmastery.datagen.OMEnchantments;
import net.rk.overpoweredmastery.datamap.OMDatamaps;
import net.rk.overpoweredmastery.datamap.Smeltable;
import net.rk.overpoweredmastery.util.OPUtil;

public class UltimatePickaxe extends Item {
    public UltimatePickaxe(Properties properties) {
        super(properties.stacksTo(1).fireResistant());
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return enchantment.is(EnchantmentTags.MINING_EXCLUSIVE) ||
                enchantment.is(OMEnchantments.INSTAREPAIR) ||
                enchantment.is(Enchantments.MENDING) ||
                enchantment.is(Enchantments.EFFICIENCY) ||
                enchantment.is(Enchantments.FIRE_ASPECT) ||
                enchantment.is(Enchantments.UNBREAKING);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if(!level.isClientSide()){
            if(!livingEntity.hasEffect(MobEffects.HASTE)){
                int efficiencyLevel = stack.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(level, Enchantments.EFFICIENCY));

                livingEntity.addEffect(new MobEffectInstance(MobEffects.HASTE,50,
                        Mth.clamp(2 + efficiencyLevel,1,127),
                        true,false,false));
            }
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack handItem = context.getItemInHand();
        BlockState lookingAtBlockState = level.getBlockState(blockPos);
        Block lookingAtBlock = lookingAtBlockState.getBlock();

        Holder<Block> holder = lookingAtBlock.builtInRegistryHolder();

        Smeltable smeltable = holder.getData(OMDatamaps.SMELTABLES);
        boolean itemCanDestroyBlock = handItem.canDestroyBlock(level.getBlockState(blockPos),level,blockPos,player) && lookingAtBlockState.is(BlockTags.MINEABLE_WITH_PICKAXE);

        if(level.isClientSide()){
            if(itemCanDestroyBlock){
                if(smeltable != null){
                    if(smeltable.smeltInto() == Blocks.WATER && level.environmentAttributes().getDimensionValue(EnvironmentAttributes.WATER_EVAPORATES)){
                        player.playSound(SoundEvents.FIRE_EXTINGUISH,0.75f,1.0f);
                        return InteractionResult.FAIL;
                    }
                    else{
                        player.playSound(SoundEvents.FIRECHARGE_USE,0.75f,1.0f);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        else{
            if(itemCanDestroyBlock){
                if(smeltable != null){
                    Block smeltingResult = smeltable.smeltInto();
                    if(smeltable.smeltInto() == Blocks.WATER && level.environmentAttributes().getDimensionValue(EnvironmentAttributes.WATER_EVAPORATES)){
                        if(level instanceof ServerLevel serverLevel){
                            serverLevel.sendParticles(ParticleTypes.POOF,
                                    blockPos.getX(),blockPos.getY(),blockPos.getZ(),
                                    12,
                                    0D,0D,0D,
                                    0.1D);
                        }
                        return InteractionResult.FAIL;
                    }
                    else{
                        level.setBlock(blockPos,smeltingResult.defaultBlockState(),3);
                    }

                    return InteractionResult.SUCCESS_SERVER;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        if(entity.hasEffect(MobEffects.MINING_FATIGUE)){
            entity.removeEffect(MobEffects.MINING_FATIGUE);
            entity.playSound(SoundEvents.ALLAY_AMBIENT_WITH_ITEM,0.5f,0.75f);
            return true;
        }
        return false;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if(level.isClientSide()){
            if(player.isSecondaryUseActive()){

                return InteractionResult.SUCCESS;
            }
        }
        else{
            if(player.isSecondaryUseActive()){

                return InteractionResult.SUCCESS_SERVER;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, LevelReader level, BlockPos pos, Player player) {
        return true;
    }
}
