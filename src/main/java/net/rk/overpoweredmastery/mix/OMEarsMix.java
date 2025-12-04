package net.rk.overpoweredmastery.mix;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.Deadmau5EarsLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.rk.overpoweredmastery.ClientConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Deadmau5EarsLayer.class)
public class OMEarsMix {
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/PlayerRenderState;FF)V", at=@At("HEAD"))
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, PlayerRenderState renderState, float f1, float f2, CallbackInfo ci){
        try{
            if(ClientConfig.MOUSE_EARS_HAX.get()) {
                renderState.name = "deadmau5"; // this is extremely dumb, but it does work
            }
        }
        catch (Exception e){

        }
    }
}
