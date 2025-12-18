package net.rk.overpoweredmastery.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.rk.overpoweredmastery.Config;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.datagen.OMTags;
import net.rk.overpoweredmastery.item.OMItems;
import net.rk.overpoweredmastery.item.custom.UltimateSword;
import net.rk.overpoweredmastery.resource.OMSoundEvents;
import net.rk.overpoweredmastery.util.OPUtil;

@EventBusSubscriber(modid = OverpoweredMastery.MODID)
public class OMWorldEvents{
    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event){
        if(!event.isCanceled()){
            if(!event.getPlayer().isCreative()){
                try{
                    if(event.getLevel() instanceof ServerLevel serverLevel){
                        if(!event.getPlayer().blockActionRestricted(serverLevel, event.getPos(), event.getPlayer().gameMode())){
                            if(event.getState().is(OMTags.CAN_DROP_STRANGE_STONE)){
                                if(!Config.STRANGE_STONE_DROP_CHANCE.get().isNaN() && !Config.STRANGE_STONE_DROP_CHANCE.get().isInfinite()){

                                    if(serverLevel.getRandom().nextDouble() <= Config.STRANGE_STONE_DROP_CHANCE.getAsDouble()){
                                        int fortuneLevel = event.getPlayer().getItemInHand(event.getPlayer().getUsedItemHand()).getEnchantmentLevel(
                                                OPUtil.getEnchantmentHolderFromKeyStatic(serverLevel, Enchantments.FORTUNE));
                                        int hasSilkTouch = event.getPlayer().getItemInHand(event.getPlayer().getUsedItemHand()).getEnchantmentLevel(
                                                OPUtil.getEnchantmentHolderFromKeyStatic(serverLevel,Enchantments.SILK_TOUCH));
                                        if(hasSilkTouch > 0){
                                            return; // we will not drop strange stones if silk touching blocks
                                        }

                                        if(fortuneLevel > 0){
                                            if(fortuneLevel > 10){
                                                fortuneLevel = 10; // hard limit of 10 stones per block mined
                                            }
                                            for(int xtraItem = 0; xtraItem < fortuneLevel; ++xtraItem){
                                                serverLevel.addFreshEntity(new ItemEntity(serverLevel,event.getPos().getX(),event.getPos().getY(),event.getPos().getZ(),
                                                        new ItemStack(OMItems.STRANGE_STONE.asItem())));
                                            }
                                        }
                                        else{
                                            serverLevel.addFreshEntity(new ItemEntity(serverLevel,event.getPos().getX(),event.getPos().getY(),event.getPos().getZ(),
                                                    new ItemStack(OMItems.STRANGE_STONE.asItem())));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                catch (Exception e){

                }
            }
        }
    }

    @SubscribeEvent
    public static void onLightningStrikeEntity(EntityStruckByLightningEvent event){
        if(event.getEntity() instanceof ItemEntity itemEntity){
            if(itemEntity.getItem().is(OMItems.CONCENTRATED_MULTI_ESSENCE)){
                itemEntity.invulnerableTime = 30;
                itemEntity.setNoPickUpDelay();
                itemEntity.setItem(new ItemStack(OMItems.INFUSED_CONCENTRATED_MULTI_ESSENCE.asItem()));
                itemEntity.level().playSound(null,itemEntity.getOnPos(),
                        SoundEvents.MOOSHROOM_CONVERT, SoundSource.NEUTRAL,0.45f,OPUtil.nextFloatBetweenInclusive(0.95f,1.1f));
                itemEntity.level().playSound(null,itemEntity.getOnPos(),
                        SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.NEUTRAL,0.45f,OPUtil.nextFloatBetweenInclusive(0.95f,1.1f));
                itemEntity.level().playSound(null,itemEntity.getOnPos().above(1),
                        OMSoundEvents.EFFECT.get(), SoundSource.NEUTRAL,0.75f,OPUtil.nextFloatBetweenInclusive(0.95f,1.1f));
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onFishedItem(ItemFishedEvent event){

    }

    @SubscribeEvent
    public static void onCraftItem(PlayerEvent.ItemCraftedEvent event){

    }
}
