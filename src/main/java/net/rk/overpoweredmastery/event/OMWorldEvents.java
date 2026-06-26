package net.rk.overpoweredmastery.event;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
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
import net.rk.overpoweredmastery.item.custom.UltimateFishingRod;
import net.rk.overpoweredmastery.item.custom.UltimateSword;
import net.rk.overpoweredmastery.resource.OMSoundEvents;
import net.rk.overpoweredmastery.util.OPUtil;

@EventBusSubscriber(modid = OverpoweredMastery.MODID)
public class OMWorldEvents{
    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event){
        if(!event.isCanceled()){ // we let other mods do things and cancel the event first, to prevent a conflict
            if(!event.getPlayer().isCreative()){ // creative players don't need to obtain strange stones from survival aspects of the game (just use the creative inventory)
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
        try{
            if(event.getEntity() instanceof ItemEntity itemEntity){
                if(itemEntity.getItem().is(OMItems.CONCENTRATED_MULTI_ESSENCE)){
                    event.getLightning().setVisualOnly(true);

                    itemEntity.level().playSound(null,itemEntity.getOnPos().above(1),
                            OMSoundEvents.EFFECT.get(), SoundSource.NEUTRAL,0.75f,OPUtil.nextFloatBetweenInclusive(0.95f,1.1f));

                    Level level = event.getEntity().level();
                    ItemEntity itemEntity2 = new ItemEntity(level,itemEntity.getX(),itemEntity.getY(),itemEntity.getZ(),itemEntity.getItem(),0,0,0);
                    itemEntity2.setItem(new ItemStack(OMItems.INFUSED_CONCENTRATED_MULTI_ESSENCE.asItem(),itemEntity.getItem().copy().getCount()));

                    level.addFreshEntity(itemEntity2);
                    itemEntity.remove(Entity.RemovalReason.DISCARDED);

                    event.setCanceled(true); // we do not want other mods trying to check after this
                }
            }
        }
        catch (Exception e){
            String c = "OMWorldEvents has encountered an exception within the onLightningStrikeEntity method: ";
            LogUtils.getLogger().error("{}{}", c, e.getLocalizedMessage());
        }
    }

    @SubscribeEvent
    public static void onFishedItem(ItemFishedEvent event){
        if(event.getEntity().getItemInHand(event.getEntity().getUsedItemHand()).is(OMItems.ULTIMATE_FISHING_ROD)){
            Player player = event.getEntity();
            Item itemInHand = player.getItemInHand(player.getUsedItemHand()).getItem();

        }
    }

    @SubscribeEvent
    public static void onCraftItem(PlayerEvent.ItemCraftedEvent event){

    }
}
