package net.rk.overpoweredmastery.item.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public abstract class AbstractFishingRod extends FishingRodItem{
    public AbstractFishingRod(Properties p) {
        super(p.stacksTo(1));
    }

    public abstract void bobberRetrieve(Level level, Player player);
    public abstract void bobberThrow(Level level, Player player);

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.fishing != null) {
            if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
                hurtItem(itemstack,serverLevel,player,hand);
            }
            bobberRetrieve(level,player);
            player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        }
        else {
            bobberThrow(level,player);
            fishingHookCreate(itemstack,level,player);
            player.awardStat(Stats.ITEM_USED.get(this));
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
        }

        return InteractionResult.SUCCESS;
    }

    public abstract void hurtItem(ItemStack itemStack,ServerLevel serverLevel, Player player,InteractionHand hand);

    public abstract void fishingHookCreate(ItemStack itemStack,Level level,Player player);
}
