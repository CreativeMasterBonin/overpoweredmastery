package net.rk.overpoweredmastery.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.rk.overpoweredmastery.entity.custom.Fraud;
import net.rk.overpoweredmastery.entity.renderer.renderstate.FraudRenderState;

public class FraudRenderer extends HumanoidMobRenderer<Fraud, FraudRenderState, HumanoidModel<FraudRenderState>> {
    public FraudRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);

        ArmorModelSet<ModelLayerLocation> armorModelSet = ModelLayers.PLAYER_ARMOR;
        this.addLayer(new HumanoidArmorLayer<>(this, ArmorModelSet.bake(armorModelSet,
                context.getModelSet(), HumanoidModel::new), context.getEquipmentRenderer()));
    }

    @Override
    public void extractRenderState(Fraud fraud, FraudRenderState fraudRenderState, float partialTick) {
        super.extractRenderState(fraud, fraudRenderState, partialTick);
        fraudRenderState.isUnhappy = fraud.isUnhappy;
    }

    @Override
    public void submit(FraudRenderState fraudRenderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        if(fraudRenderState.isUnhappy){
            float shake = 0.3f * Mth.sin(0.45f * fraudRenderState.ageInTicks);
            poseStack.rotateAround(Axis.YP.rotation(shake),0f,0f,0f);
        }
        poseStack.popPose();
        super.submit(fraudRenderState, poseStack, nodeCollector, cameraRenderState);
    }

    @Override
    public boolean shouldShowName(Fraud fraud, double d1) {
        return false;
    }

    @Override
    public Identifier getTextureLocation(FraudRenderState fraudRenderState) {
        return Identifier.withDefaultNamespace("textures/entity/player/wide/steve.png");
    }

    @Override
    public FraudRenderState createRenderState() {
        return new FraudRenderState();
    }
}
