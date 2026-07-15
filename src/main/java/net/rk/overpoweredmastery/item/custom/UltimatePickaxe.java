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
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.rk.overpoweredmastery.datagen.OMEnchantments;
import net.rk.overpoweredmastery.datagen.OMTags;
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
            // server only code
            // if the player doesn't have haste applied already, give a slight boost + tool's efficiency level
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
        boolean ultimateCanMine = lookingAtBlockState.is(OMTags.ULTIMATE_TIER_CAN_MINE);
        boolean isInfested = lookingAtBlock instanceof InfestedBlock;
        int fireAspectLevel = handItem.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(level,Enchantments.FIRE_ASPECT));

        Block hostOfInfested = Blocks.STONE;

        if(level.isClientSide()){
            if(itemCanDestroyBlock){
                if(smeltable != null && fireAspectLevel > 0){
                    if(smeltable.smeltInto() == Blocks.WATER && level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES,blockPos)){
                        player.playSound(SoundEvents.FIRE_EXTINGUISH,0.75f,1.0f);
                    }
                    else{
                        player.playSound(SoundEvents.FIRECHARGE_USE,0.75f,1.0f);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
            else if(ultimateCanMine){
                if(isInfested){
                    player.playSound(SoundEvents.SILVERFISH_DEATH);
                }
                else{
                    player.playSound(SoundEvents.TRIAL_SPAWNER_AMBIENT_OMINOUS,0.75f,OPUtil.nextFloatBetweenInclusive(0.75f,0.85f));
                }
                return InteractionResult.SUCCESS;
            }
            else{
                if(isInfested){
                    player.playSound(SoundEvents.SILVERFISH_DEATH);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        else{
            if(itemCanDestroyBlock){
                if(smeltable != null && fireAspectLevel > 0){
                    Block smeltingResult = smeltable.smeltInto();
                    if(smeltable.smeltInto() == Blocks.WATER && level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES,blockPos)){
                        level.setBlock(blockPos,Blocks.AIR.defaultBlockState(),3);
                        if(level instanceof ServerLevel serverLevel){
                            serverLevel.sendParticles(ParticleTypes.POOF,
                                    blockPos.getX(),blockPos.getY(),blockPos.getZ(),
                                    12,
                                    0D,blockPos.getY() + 0.5D,0D,
                                    0.1D);
                        }
                    }
                    else{
                        level.setBlock(blockPos,smeltingResult.defaultBlockState(),3);
                    }
                    return InteractionResult.SUCCESS_SERVER;
                }
            }
            else if(ultimateCanMine){
                if(isInfested){
                    level.setBlock(blockPos,hostOfInfested.defaultBlockState(),3);
                    if(level instanceof ServerLevel serverLevel){
                        serverLevel.sendParticles(ParticleTypes.INFESTED,
                                blockPos.getX(),blockPos.getY(),blockPos.getZ(),
                                7,
                                0D,blockPos.getY() + 0.5D,0D,
                                0.1D
                        );
                    }
                }
                else{
                    level.destroyBlock(blockPos,true,player);
                    if(level instanceof ServerLevel serverLevel){
                        serverLevel.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER,
                                blockPos.getX(),blockPos.getY(),blockPos.getZ(),
                                7,
                                0D,blockPos.getY() + 0.5D,0D,
                                0.1D
                        );
                    }
                }
                return InteractionResult.SUCCESS_SERVER;
            }
            else{
                if(isInfested){
                    level.setBlock(blockPos,hostOfInfested.defaultBlockState(),3);
                    if(level instanceof ServerLevel serverLevel){
                        serverLevel.sendParticles(ParticleTypes.INFESTED,
                                blockPos.getX(),blockPos.getY(),blockPos.getZ(),
                                7,
                                0D,blockPos.getY() + 0.5D,0D,
                                0.1D
                        );
                    }
                    return InteractionResult.SUCCESS_SERVER;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        Level level = entity.level();
        // disable any hint of mining fatigue (such as from elder guardians or other sources of trouble)
        if(entity.hasEffect(MobEffects.MINING_FATIGUE)){
            entity.removeEffect(MobEffects.MINING_FATIGUE);
            entity.playSound(SoundEvents.ALLAY_AMBIENT_WITH_ITEM,0.5f,0.75f);
            // server only code
            if(!level.isClientSide() && level instanceof ServerLevel serverLevel){
                serverLevel.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS,
                        entity.getX(),entity.getY(),entity.getZ(),
                        7,
                        0D,entity.getOnPos().getY() + 0.15D,0D,
                        0.1D);
            }
            return true;
        }
        return false;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        // apply an extra bonus if the player does not have it at all
        if(level.isClientSide()){
            if(player.isSecondaryUseActive()){
                if(!player.hasEffect(MobEffects.HASTE) && !player.hasEffect(MobEffects.ABSORPTION) && !player.hasEffect(MobEffects.SATURATION)){
                    player.playSound(SoundEvents.BEACON_POWER_SELECT,0.5f,OPUtil.nextFloatBetweenInclusive(0.97f,1.04f));
                }
                return InteractionResult.SUCCESS;
            }
        }
        else{
            if(player.isSecondaryUseActive()){
                if(!player.hasEffect(MobEffects.NIGHT_VISION) && !player.hasEffect(MobEffects.ABSORPTION) && !player.hasEffect(MobEffects.SATURATION)){
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION,300,0,true,false,false));
                    player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION,300,2,true,false,false));
                    player.addEffect(new MobEffectInstance(MobEffects.SATURATION,20,10,true,false,false));
                }
                return InteractionResult.SUCCESS_SERVER;
            }
        }
        return InteractionResult.PASS;
    }

    // sneak should in fact bypass all use-cases (e.g., mining actions)
    @Override
    public boolean doesSneakBypassUse(ItemStack stack, LevelReader level, BlockPos pos, Player player) {
        return true;
    }
}
