package net.rk.overpoweredmastery.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.rk.overpoweredmastery.entity.model.MultiAssemblerModel;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Set;

public record MultiAssemblerSpecialModelRenderer(Model model) implements NoDataSpecialModelRenderer {
    @Override
    public void render(ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
        //poseStack.pushPose();
        VertexConsumer vc = bufferSource.getBuffer(RenderType.entityCutout(MultiAssemblerModel.MULTI_ASSEMBLER_MODEL_LAYER_LOCATION.model()));
        /*if(displayContext.firstPerson() && !displayContext.leftHand()){
            poseStack.translate(1.5D,1.5D,0D);
        }
        else if(displayContext.firstPerson() && displayContext.leftHand()){
            poseStack.translate(-0.5D,1.5D,0D);
        }
        else if(!displayContext.firstPerson() && !displayContext.leftHand()){
            poseStack.translate(0.5D,1.75D,0.5D);
        }
        else if(!displayContext.firstPerson() && displayContext.leftHand()){
            poseStack.translate(0.5D,1.75D,0.5D);
        }
        poseStack.mulPose(Axis.YP.rotationDegrees(45.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-7.0f));*/
        this.model.renderToBuffer(poseStack,vc,packedLight,OverlayTexture.NO_OVERLAY);
        //poseStack.popPose();
    }

    @Override
    public void getExtents(Set<Vector3f> output) {
        PoseStack posestack = new PoseStack();
        posestack.translate(0.5F, 0.0F, 0.5F);
        posestack.scale(-1.0F, -1.0F, 1.0F);
        this.model.root().getExtentsForGui(posestack,output);
    }

    public record Unbaked(ResourceLocation texture) implements SpecialModelRenderer.Unbaked{
        public static final MapCodec<MultiAssemblerSpecialModelRenderer.Unbaked> MAP_CODEC = ResourceLocation.CODEC.fieldOf("texture")
                .xmap(MultiAssemblerSpecialModelRenderer.Unbaked::new, MultiAssemblerSpecialModelRenderer.Unbaked::texture);

        @Nullable
        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            return new MultiAssemblerSpecialModelRenderer(
                    new MultiAssemblerModel(modelSet.bakeLayer(MultiAssemblerModel.MULTI_ASSEMBLER_MODEL_LAYER_LOCATION)));
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
