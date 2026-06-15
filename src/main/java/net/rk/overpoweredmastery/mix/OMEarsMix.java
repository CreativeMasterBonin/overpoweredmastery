package net.rk.overpoweredmastery.mix;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.Deadmau5EarsLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.rk.overpoweredmastery.ClientConfig;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(value = Deadmau5EarsLayer.class)
public class OMEarsMix {
    @Shadow
    @Final
    private HumanoidModel<AvatarRenderState> model;

    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V", at=@At("HEAD"))
    public void render(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, AvatarRenderState renderState, float yRot, float xRot, CallbackInfo ci){
        boolean modifiedState = false;
        try{
            if(ClientConfig.MOUSE_EARS_HAX.get().booleanValue() && !renderState.isInvisible){
                renderState.showExtraEars = true;
                modifiedState = true;
                nodeCollector.submitModel(
                        this.model, renderState, poseStack, RenderTypes.entitySolid(renderState.skin.body().texturePath()), packedLight, LivingEntityRenderer.getOverlayCoords(renderState,0.0f), renderState.outlineColor, null
                );
            }
            if(modifiedState){
                return;
            }
        }
        catch (Exception e){

        }
    }
}
