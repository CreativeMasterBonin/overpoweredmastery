package net.rk.overpoweredmastery.entity.renderer.renderstate;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

// for the future
public class MultiAssemblerRenderState extends BlockEntityRenderState {
    public int ticks;
    public boolean isAssembling;
    public int assemblyTime;
    public int assemblyProgress;
    public ItemStackRenderState[] resultStack = new ItemStackRenderState[1];
}
