package net.rk.overpoweredmastery.entity.renderer.renderstate;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public class FraudRenderState extends HumanoidRenderState {
    public boolean isUnhappy;

    public ItemStack getUseItemStackForArm(HumanoidArm arm) {
        return this.getMainHandItemStack();
    }
}
