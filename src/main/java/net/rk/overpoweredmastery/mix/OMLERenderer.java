package net.rk.overpoweredmastery.mix;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.MinecartModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.EntityType;
import net.rk.overpoweredmastery.ClientConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.logging.Logger;

@Mixin(value = LivingEntityRenderer.class)
public abstract class OMLERenderer{
    private static boolean failedOnce = false;
    private static float zeroF = 0.0f;
    private MultiBufferSource refBufferSource = null;

    @Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",at = @At("TAIL"))
    public void render(LivingEntityRenderState renderState, PoseStack stack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci){
        refBufferSource = bufferSource;
    }

    @Inject(method = "setupRotations",at=@At("TAIL"))
    public void setupRotations(LivingEntityRenderState renderState, PoseStack poseStack, float bodyRot, float scale, CallbackInfo ci){
        try{
            if(ClientConfig.UPSIDE_DOWN_HAX.get() && !renderState.isInvisible && renderState.entityType == EntityType.PLAYER){
                float currentBoundingBoxWithOffset = renderState.boundingBoxHeight;
                float scaleOffsetBounds = currentBoundingBoxWithOffset / scale;
                float yOffset = ClientConfig.UPSIDE_DOWN_HAX_Y_OFFSET.get().floatValue();
                if(!(scale <= zeroF)){
                    poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
                    poseStack.translate(zeroF, scaleOffsetBounds - yOffset, zeroF);
                }
            }
            if(renderState.entityType == EntityType.PLAYER && refBufferSource != null && ClientConfig.MINECART_ON_HEAD_HAX.get() && !renderState.isInvisible){
                poseStack.pushPose();
                poseStack.translate(0f,0.1f + (renderState.boundingBoxHeight / scale),0f);
                MinecartModel model = new MinecartModel(MinecartModel.createBodyLayer().bakeRoot());
                // the order is xyz rotations
                poseStack.mulPose(Axis.XP.rotationDegrees(ClientConfig.MINECART_HAX_X_ROTATION.get().floatValue()));
                poseStack.mulPose(Axis.YP.rotationDegrees(ClientConfig.MINECART_HAX_Y_ROTATION.get().floatValue()));
                poseStack.mulPose(Axis.ZP.rotationDegrees(ClientConfig.MINECART_HAX_Z_ROTATION.get().floatValue()));
                model.renderToBuffer(poseStack,
                        refBufferSource.getBuffer(RenderType.entityCutout(ResourceLocation.withDefaultNamespace("textures/entity/minecart.png"))),
                        16777215, OverlayTexture.NO_OVERLAY, ARGB.colorFromFloat(1.0f, 1.0f, 1.0f, 1.0f));
                poseStack.popPose();
            }
        }
        catch (Exception e){
            if(!failedOnce){
                failedOnce = true;
                Logger.getAnonymousLogger().warning("OMLERenderer - inject method has encountered an error: " + e.getMessage());
            }
        }
    }
}
