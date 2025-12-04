package net.rk.overpoweredmastery.mix;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.EntityType;
import net.rk.overpoweredmastery.ClientConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.logging.Logger;

@Mixin(value = LivingEntityRenderer.class)
public class OMLERenderer{
    private static boolean failedOnce = false;
    private static float zeroF = 0.0f;
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
        }
        catch (Exception e){
            if(!failedOnce){
                failedOnce = true;
                Logger.getAnonymousLogger().warning("OMLERenderer - inject method has encountered an error: " + e.getMessage());
            }
        }
    }
}
