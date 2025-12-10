package net.rk.overpoweredmastery.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.rk.overpoweredmastery.item.custom.AbstractSpear;

public class UltimateSpear extends AbstractSpear {
    public UltimateSpear(Properties properties) {
        super(properties);
    }

    @Override
    public void extraOnUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int remainingDuration) {

    }
}
