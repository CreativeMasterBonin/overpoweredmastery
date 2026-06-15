package net.rk.overpoweredmastery.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.entity.model.MultiAssemblerModel;
import net.rk.overpoweredmastery.entity.renderer.renderstate.MultiAssemblerRenderState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;

import java.util.function.Consumer;

public record MultiAssemblerSpecialModelRenderer(MaterialSet materials, MultiAssemblerModel model) implements NoDataSpecialModelRenderer {
    public MultiAssemblerSpecialModelRenderer(MaterialSet materials, MultiAssemblerModel model) {
        this.materials = materials;
        this.model = model;
    }

    @Nullable
    @Override
    public Void extractArgument(ItemStack stack) {
        return NoDataSpecialModelRenderer.super.extractArgument(stack);
    }

    @Override
    public void submit(@Nullable Void argument, ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, int packedOverlay, boolean hasFoil, int outlineColor) {
        NoDataSpecialModelRenderer.super.submit(argument, displayContext, poseStack, nodeCollector, packedLight, packedOverlay, hasFoil, outlineColor);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {
        PoseStack posestack = new PoseStack();
        posestack.translate(0.5F, 0.0F, 0.5F);
        posestack.scale(-1.0F, -1.0F, 1.0F);

    }

    public static Material multiAssembler = new Material(Sheets.BLOCK_ENTITIES_MAPPER.sheet(),
            Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID,"entity/multi_assembler"));

    @Override
    public void submit(ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, int packedOverlay, boolean hasFoil, int outlineColor) {
        poseStack.pushPose();
        MultiAssemblerRenderState renderState = new MultiAssemblerRenderState();
        renderState.isAssembling = true;
        renderState.ticks = 0;
        renderState.assemblyTime = 0;
        renderState.assemblyProgress = 0;
        renderState.resultStack = new ItemStackRenderState[1];
        Material RL = multiAssembler;
        RenderType multiAssemblerModelRT = RL.renderType(RenderTypes::entityCutout);

        if(displayContext.firstPerson() && !displayContext.leftHand()){
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
        poseStack.mulPose(Axis.ZP.rotationDegrees(-7.0f));

        MultiAssemblerModel.State modelState = new MultiAssemblerModel.State(renderState.ticks);

        TextureAtlasSprite textureatlassprite = this.materials.get(RL);
        nodeCollector.submitModelPart(model.root(),poseStack,multiAssemblerModelRT,renderState.lightCoords,
                OverlayTexture.NO_OVERLAY,textureatlassprite);
        poseStack.popPose();
    }

    public record Unbaked(Identifier texture) implements SpecialModelRenderer.Unbaked{
        public static final MapCodec<MultiAssemblerSpecialModelRenderer.Unbaked> MAP_CODEC = Identifier.CODEC.fieldOf("texture")
                .xmap(MultiAssemblerSpecialModelRenderer.Unbaked::new, MultiAssemblerSpecialModelRenderer.Unbaked::texture);

        @Override
        public @NotNull SpecialModelRenderer<?> bake(BakingContext context) {
            EntityModelSet set = context.entityModelSet();
            return new MultiAssemblerSpecialModelRenderer(context.materials(),new MultiAssemblerModel(set.bakeLayer(MultiAssemblerModel.MULTI_ASSEMBLER_MODEL_LAYER_LOCATION)));
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
