package net.rk.overpoweredmastery.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.rk.overpoweredmastery.entity.blockentity.MultiAssemblerBlockEntity;
import net.rk.overpoweredmastery.entity.model.MultiAssemblerModel;

public class MultiAssemblerBlockEntityRenderer implements BlockEntityRenderer<MultiAssemblerBlockEntity> {
    public final MultiAssemblerModel model;
    public ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    public MultiAssemblerBlockEntityRenderer(BlockEntityRendererProvider.Context context){
        this.model = new MultiAssemblerModel(context.bakeLayer(MultiAssemblerModel.MULTI_ASSEMBLER_MODEL_LAYER_LOCATION));
    }

    @Override
    public void render(MultiAssemblerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, Vec3 cameraPos) {
        if(this.model != null){
            itemRenderer = Minecraft.getInstance().getItemRenderer();
            poseStack.pushPose();
            poseStack.translate(0.5D,0.1D,0.5D);// 0.44 and 0.2
            VertexConsumer vc = bufferSource.getBuffer(RenderType.entityCutout(MultiAssemblerModel.MULTI_ASSEMBLER_MODEL_LAYER_LOCATION.model()));
            if(blockEntity.assembling){
                this.model.setupAnim(blockEntity);
            }
            else{
                this.model.setupAnimOff(blockEntity);
            }
            //15728880
            //LightTexture.pack(blockEntity.getLevel().getBrightness(LightLayer.BLOCK,blockEntity.getBlockPos()),blockEntity.getLevel().getBrightness(LightLayer.SKY,blockEntity.getBlockPos()))
            this.model.renderToBuffer(poseStack,vc,
                    15728880,
                    OverlayTexture.NO_OVERLAY);
            poseStack.popPose();

            poseStack.pushPose();
            if(blockEntity.getResultItemReference() != null){
                if(blockEntity.getResultItemReference() != ItemStack.EMPTY){
                    poseStack.translate(0.5D,0.63D,0.5D);
                    itemRenderer.renderStatic(blockEntity.getResultItemReference(),
                            ItemDisplayContext.GROUND,packedLight,packedOverlay,poseStack,bufferSource,blockEntity.getLevel(),1991);
                }
            }
            poseStack.popPose();
        }
    }
}
