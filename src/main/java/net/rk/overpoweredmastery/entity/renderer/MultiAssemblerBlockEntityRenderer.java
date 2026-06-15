package net.rk.overpoweredmastery.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import net.rk.overpoweredmastery.entity.blockentity.MultiAssemblerBlockEntity;
import net.rk.overpoweredmastery.entity.model.MultiAssemblerModel;
import net.rk.overpoweredmastery.entity.renderer.renderstate.MultiAssemblerRenderState;
import org.jetbrains.annotations.Nullable;

public class MultiAssemblerBlockEntityRenderer implements BlockEntityRenderer<MultiAssemblerBlockEntity,MultiAssemblerRenderState> {
    public final MultiAssemblerModel model;
    public final ItemModelResolver resolver;
    public final MaterialSet materials;

    public static final Material RL = Sheets.BLOCK_ENTITIES_MAPPER.apply(MultiAssemblerModel.MULTI_ASSEMBLER_MODEL_LAYER_LOCATION.model());

    public MultiAssemblerBlockEntityRenderer(BlockEntityRendererProvider.Context context){
        this.materials = context.materials();
        this.model = new MultiAssemblerModel(context.bakeLayer(MultiAssemblerModel.MULTI_ASSEMBLER_MODEL_LAYER_LOCATION));
        this.resolver = context.itemModelResolver();
    }

    @Override
    public void extractRenderState(MultiAssemblerBlockEntity blockEntity, MultiAssemblerRenderState renderState, float partialTick, Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity,renderState,partialTick,cameraPosition,breakProgress);
        if(blockEntity.currentInput != null){
            renderState.isAssembling = blockEntity.assembling;
            renderState.assemblyTime = blockEntity.assemblyTime;
            renderState.assemblyProgress = blockEntity.assemblyProgress;
        }
        else{
            renderState.isAssembling = false;
            renderState.assemblyTime = 0;
            renderState.assemblyProgress = 0;
        }
        renderState.ticks = blockEntity.ticksPassed + (int)partialTick;
        if(blockEntity.getResultItemReference() != null){
            ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
            this.resolver.updateForTopItem(itemStackRenderState,blockEntity.getResultItemReference(),ItemDisplayContext.FIXED,blockEntity.getLevel(),blockEntity,1996);
            renderState.resultStack[0] = itemStackRenderState;
        }
    }

    @Override
    public MultiAssemblerRenderState createRenderState() {
        return new MultiAssemblerRenderState();
    }

    @Override
    public void submit(MultiAssemblerRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        if(this.model != null){
            // model render
            poseStack.pushPose();
            try{
                poseStack.translate(0.5D,0.1D,0.5D);// 0.44 and 0.2

                MultiAssemblerModel.State modelState = new MultiAssemblerModel.State(renderState.ticks);
                RenderType multiAssemblerModelRT = RenderTypes.entitySolid(Identifier.parse(MultiAssemblerModel.MULTI_ASSEMBLER_MODEL_LAYER_LOCATION.layer()));

                if(renderState.isAssembling){
                    this.model.setupAnim(modelState);
                }
                else{
                    this.model.setupAnimOff(modelState);
                }

                nodeCollector.submitModel(this.model,modelState,poseStack,multiAssemblerModelRT,renderState.lightCoords,
                        OverlayTexture.NO_OVERLAY,-1,this.materials.get(RL),0,renderState.breakProgress);
            }
            catch (Exception e){
                LogUtils.getLogger().error(e.getMessage());
            }
            poseStack.popPose();
            // item render
            poseStack.pushPose();
            try{
                poseStack.translate(0.5,0.5,0.5);

                ItemStackRenderState state = renderState.resultStack[0];
                state.submit(poseStack,nodeCollector,renderState.lightCoords,OverlayTexture.NO_OVERLAY,0);
            }
            catch (Exception e){
                LogUtils.getLogger().error(e.getMessage());
            }
            poseStack.popPose();
        }
    }
}
