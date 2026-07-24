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
import net.rk.overpoweredmastery.OverpoweredMastery;
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
        fraudRenderState.isUnhappy = fraud.getEntityData().get(Fraud.UNHAPPY_DATA);
        fraudRenderState.skinVariant = fraud.getEntityData().get(Fraud.SKIN_DATA);
    }

    @Override
    public void submit(FraudRenderState fraudRenderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        if(fraudRenderState.isUnhappy){
            float shake = 0.3f * Mth.sin(0.45f * fraudRenderState.ageInTicks) + 0.4f;
            poseStack.rotateAround(Axis.YP.rotation(shake),0f,0f,0f);
        }
        if(fraudRenderState.hasRedOverlay){

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
        switch(fraudRenderState.skinVariant){
            case 1: {
                if(fraudRenderState.isUnhappy){
                    return Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID,"textures/entity/fraud/gray_angry.png");
                }
                else{
                    return Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID,"textures/entity/fraud/gray.png");
                }
            }
            case 2: {
                if(fraudRenderState.isUnhappy){
                    return Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID,"textures/entity/fraud/green_angry.png");
                }
                else{
                    return Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID,"textures/entity/fraud/green.png");
                }
            }
            case 3: {
                if(fraudRenderState.isUnhappy){
                    return Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID,"textures/entity/fraud/yellow.png");
                }
                else{
                    return Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID,"textures/entity/fraud/yellow.png");
                }
            }
            default: {
                if(fraudRenderState.isUnhappy){
                    return Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID,"textures/entity/fraud/brown_angry.png");
                }
                else{
                    return Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID,"textures/entity/fraud/brown.png");
                }
            }
        }
    }

    @Override
    public FraudRenderState createRenderState() {
        return new FraudRenderState();
    }
}
