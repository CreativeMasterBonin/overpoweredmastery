package net.rk.overpoweredmastery.mix;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.Deadmau5EarsLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.rk.overpoweredmastery.ClientConfig;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.logging.Logger;

@Debug(export = true)
@Mixin(value = Deadmau5EarsLayer.class)
public class OMEarsMix {
    @Shadow @Final private HumanoidModel<PlayerRenderState> model;

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/PlayerRenderState;FF)V", at=@At("HEAD"))
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, PlayerRenderState renderState, float f1, float f2, CallbackInfo ci){
        boolean modifiedState = false;
        try{
            if(ClientConfig.MOUSE_EARS_HAX.get()) {
                renderState.name = "deadmau5"; // this is extremely dumb, but it does work
                // below is not really working code to customize the ears
                VertexConsumer vertexconsumer1;
                int i = LivingEntityRenderer.getOverlayCoords(renderState, 0.0F);
                if(ResourceLocation.tryParse(ResourceLocation.parse(ClientConfig.MOUSE_EARS_RL.get()).toString()) == null){
                    vertexconsumer1 = buffer.getBuffer(RenderType.entitySolid(renderState.skin.texture()));
                }
                else{
                    vertexconsumer1 = buffer.getBuffer(RenderType.entityCutout(ResourceLocation.parse(ClientConfig.MOUSE_EARS_RL.get())));
                    modifiedState = true;
                }
                model.head.copyFrom(this.model.getHead());
                model.body.copyFrom(this.model.body);
                model.rightArm.copyFrom(this.model.rightArm);
                model.leftArm.copyFrom(this.model.leftArm);
                model.rightLeg.copyFrom(this.model.rightLeg);
                model.leftLeg.copyFrom(this.model.leftLeg);
                this.model.setupAnim(renderState);
                this.model.renderToBuffer(poseStack,
                        vertexconsumer1,
                        packedLight,i);
            }
        }
        catch (Exception e){

        }

        if(modifiedState){
            return;
        }
    }
}
