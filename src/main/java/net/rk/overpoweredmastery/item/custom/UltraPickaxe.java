package net.rk.overpoweredmastery.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.rk.overpoweredmastery.OverpoweredMasteryClient;
import net.rk.overpoweredmastery.datagen.OMTags;
import net.rk.overpoweredmastery.datamap.OMDatamaps;
import net.rk.overpoweredmastery.datamap.Smeltable;
import net.rk.overpoweredmastery.item.OMRarity;
import net.rk.overpoweredmastery.util.ClientActionHandler;
import net.rk.overpoweredmastery.util.OPUtil;

import java.util.List;
import java.util.function.Consumer;

public class UltraPickaxe extends Item {
    public UltraPickaxe(Properties properties) {
        super(properties.fireResistant().rarity(OMRarity.ULTRA.getValue()).stacksTo(1)
                .enchantable(999));
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return enchantment.is(OMTags.ULTRA_PICKAXE_SUPPORTED);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.ultra.desc")
                .withColor(OPUtil.ULTRA_COLOR));
        if(ClientActionHandler.keyMappingPressed(OverpoweredMasteryClient.DESCRIPTION_KEY_MAPPING)){
            tooltipAdder.accept(Component.translatable("item.overpoweredmastery.ultra.desc.detail")
                    .withStyle(ChatFormatting.GRAY));
        }
        else{
            tooltipAdder.accept(Component.translatable("item.overpoweredmastery.press_desc_key",Component.translatable(OverpoweredMasteryClient.DESCRIPTION_KEY_MAPPING.getKey().getName()))
                    .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
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
        boolean ultraCanMine = lookingAtBlockState.is(OMTags.ULTRA_TIER_CAN_MINE);
        boolean isInfested = lookingAtBlock instanceof InfestedBlock;
        int fireAspectLevel = handItem.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(level, Enchantments.FIRE_ASPECT));
        int flameLevel = handItem.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(level, Enchantments.FLAME));

        Block hostOfInfested = Blocks.STONE;

        if(level.isClientSide()){
            if(itemCanDestroyBlock){
                if(smeltable != null && (fireAspectLevel > 0 || flameLevel > 0)){
                    if(smeltable.smeltInto() == Blocks.WATER && level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES,blockPos)){
                        player.playSound(SoundEvents.FIRE_EXTINGUISH,0.75f,1.0f);
                    }
                    else{
                        player.playSound(SoundEvents.FIRECHARGE_USE,0.75f,1.0f);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
            else if(ultraCanMine){
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
                // both flame and fire aspect count as 'fire' on ultra pickaxes
                if(smeltable != null && (fireAspectLevel > 0 || flameLevel > 0)){
                    Block smeltingResult = smeltable.smeltInto();
                    if(smeltable.smeltInto() == Blocks.WATER && level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES,blockPos)){
                        level.setBlock(blockPos,Blocks.AIR.defaultBlockState(),3);
                        if(level instanceof ServerLevel serverLevel){
                            serverLevel.sendParticles(ParticleTypes.POOF,
                                    blockPos.getX(),blockPos.getY(),blockPos.getZ(),
                                    9,
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
            else if(ultraCanMine){
                if(isInfested){
                    level.setBlock(blockPos,hostOfInfested.defaultBlockState(),3);
                    if(level instanceof ServerLevel serverLevel){
                        serverLevel.sendParticles(ParticleTypes.INFESTED,
                                blockPos.getX(),blockPos.getY(),blockPos.getZ(),
                                5,
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
                                5,
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
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        // start here
        List<Entity> entities = level.getEntitiesOfClass(Entity.class,new AABB(
                player.getX() - 32,player.getY() - 32,player.getZ() - 32,
                player.getX() + 32,player.getY() + 32, player.getZ() + 32));

        // client or not
        if(level.isClientSide()){
            // shifting?
            if(player.isSecondaryUseActive()){
                if(!player.hasEffect(MobEffects.HASTE) && !player.hasEffect(MobEffects.ABSORPTION) && !player.hasEffect(MobEffects.SATURATION)){
                    player.playSound(SoundEvents.BEACON_POWER_SELECT,0.5f,OPUtil.nextFloatBetweenInclusive(0.97f,1.04f));
                }
            }
            else{
                if(!entities.isEmpty()){
                    player.playSound(SoundEvents.TRIAL_SPAWNER_OMINOUS_ACTIVATE,0.5f, OPUtil.nextFloatBetweenInclusive(0.98f,1.02f));
                    return InteractionResult.SUCCESS;
                }
            }
        }
        else{
            // shifting?
            if(player.isSecondaryUseActive()){
                if(!player.hasEffect(MobEffects.NIGHT_VISION) && !player.hasEffect(MobEffects.ABSORPTION) && !player.hasEffect(MobEffects.SATURATION) && !player.hasEffect(MobEffects.LUCK)){
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION,1200,0,true,false,false));
                    player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION,1200,4,true,false,false));
                    player.addEffect(new MobEffectInstance(MobEffects.SATURATION,20,20,true,false,false));
                    player.addEffect(new MobEffectInstance(MobEffects.LUCK,1200,2,true,false,false));
                }
            }
            else{
                if(level instanceof ServerLevel serverLevel){
                    if(!entities.isEmpty()){
                        entities.forEach(entity -> {
                            if(!entity.getType().is(OMTags.ULTRA_SEEK_IMMUNE)){
                                if(entity instanceof Mob mob){
                                    if(!player.isAlliedTo(mob) && !mob.isInvulnerable() && !mob.isVehicle()){
                                        if(!mob.hasEffect(MobEffects.WEAKNESS)){
                                            mob.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,6000,4));
                                        }
                                        if(!mob.hasEffect(MobEffects.SLOWNESS)){
                                            mob.addEffect(new MobEffectInstance(MobEffects.SLOWNESS,6000,7));
                                        }
                                        if(!mob.hasEffect(MobEffects.GLOWING)){
                                            mob.addEffect(new MobEffectInstance(MobEffects.GLOWING,6000,0));
                                        }
                                    }
                                }
                            }
                        });
                        return InteractionResult.SUCCESS_SERVER;
                    }
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
        if(entity.hasEffect(MobEffects.WEAKNESS)){
            entity.removeEffect(MobEffects.WEAKNESS);
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
        if(entity.hasEffect(MobEffects.BLINDNESS)){
            entity.removeEffect(MobEffects.BLINDNESS);
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
        if(entity.hasEffect(MobEffects.DARKNESS)){
            entity.removeEffect(MobEffects.DARKNESS);
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
        if(entity.hasEffect(MobEffects.POISON)){
            entity.removeEffect(MobEffects.POISON);
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
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if(!level.isClientSide()){
            // server only code
            // if the player doesn't have haste applied already, give a boost + tool's efficiency level
            if(!livingEntity.hasEffect(MobEffects.HASTE)){
                int efficiencyLevel = stack.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(level, Enchantments.EFFICIENCY));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.HASTE,50,
                        Mth.clamp(4 + efficiencyLevel,1,127),
                        true,false,false));
            }
        }
    }
}
