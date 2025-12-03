package net.rk.overpoweredmastery;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.IArmPoseTransformer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.rk.overpoweredmastery.datagen.OMTags;
import net.rk.overpoweredmastery.entity.OMEntityTypes;
import net.rk.overpoweredmastery.entity.renderer.*;
import net.rk.overpoweredmastery.item.OMItems;
import net.rk.overpoweredmastery.item.custom.AbstractSpear;
import net.rk.overpoweredmastery.item.custom.AbstractWubs;
import org.jetbrains.annotations.Nullable;

@Mod(value = OverpoweredMastery.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = OverpoweredMastery.MODID, value = Dist.CLIENT)
public class OverpoweredMasteryClient {
    public OverpoweredMasteryClient(IEventBus eventBus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        eventBus.addListener(this::setupClientExtensions);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){

    }

    public final class WubExtension{
        public static final EnumProxy<HumanoidModel.ArmPose> WUB = new EnumProxy<>(
                HumanoidModel.ArmPose.class,true,(IArmPoseTransformer) WubExtension::transformer
        );

        public static void transformer(HumanoidModel<?> humanoidModel, HumanoidRenderState humanoidRenderState, HumanoidArm humanoidArm) {
            if(humanoidRenderState.isUsingItem && humanoidRenderState.useItemHand == InteractionHand.MAIN_HAND){
                humanoidModel.rightArm.xRot = 5;
            }
            else if(humanoidRenderState.isUsingItem && humanoidRenderState.useItemHand == InteractionHand.OFF_HAND){
                humanoidModel.leftArm.xRot = 5;
            }
        }
    }

    public final class SpearExtension{
        public static final EnumProxy<HumanoidModel.ArmPose> SPEAR = new EnumProxy<>(
                HumanoidModel.ArmPose.class,true,(IArmPoseTransformer) SpearExtension::transformer
        );

        public static void transformer(HumanoidModel<?> humanoidModel, HumanoidRenderState humanoidRenderState, HumanoidArm humanoidArm) {
            if(humanoidRenderState.isUsingItem && humanoidRenderState.useItemHand == InteractionHand.MAIN_HAND){
                humanoidModel.rightArm.xRot = 82;
                humanoidModel.rightArm.zRot = 0.5f + Mth.sin(humanoidRenderState.ticksUsingItem) * 0.15f;
            }
            else if(humanoidRenderState.isUsingItem && humanoidRenderState.useItemHand == InteractionHand.OFF_HAND){
                humanoidModel.leftArm.xRot = 82;
                humanoidModel.leftArm.zRot = -0.5f + Mth.sin(humanoidRenderState.ticksUsingItem) * 0.15f;
            }
            else if(!humanoidRenderState.isUsingItem && humanoidRenderState.useItemHand == InteractionHand.MAIN_HAND){
                humanoidModel.rightArm.xRot = 75;
                humanoidModel.rightArm.zRot = 0.23f;
            }
            else if(!humanoidRenderState.isUsingItem && humanoidRenderState.useItemHand == InteractionHand.OFF_HAND){
                humanoidModel.leftArm.xRot = 75;
                humanoidModel.leftArm.zRot = -0.23f;
            }
        }
    }

    public void setupClientExtensions(RegisterClientExtensionsEvent event){
        event.registerItem(new IClientItemExtensions() {
            @Nullable
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                return WubExtension.WUB.getValue();
            }

            @Override
            public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
                if(player.getMainHandItem().is(OMTags.MUSIC_DISC_WUBS) || player.getOffhandItem().is(OMTags.MUSIC_DISC_WUBS) || player.getMainHandItem().getItem() instanceof AbstractWubs | player.getOffhandItem().getItem() instanceof AbstractWubs){
                    if(arm == HumanoidArm.RIGHT){
                        if(player.isUsingItem()){
                            float f6 = itemInHand.getUseDuration(player) - (player.getUseItemRemainingTicks() - partialTick + 2.0F);
                            poseStack.mulPose(Axis.XN.rotationDegrees(-1.2f + Mth.sin(f6) * 0.5f));
                            poseStack.translate(0.5,-0.5,-1);
                        }
                        else{
                            poseStack.mulPose(Axis.XN.rotationDegrees(2.74f));
                            poseStack.translate(0.5,-0.5,-1);
                        }
                    }
                    else if(arm == HumanoidArm.LEFT){
                        if(player.isUsingItem()){
                            float f6 = itemInHand.getUseDuration(player) - (player.getUseItemRemainingTicks() - partialTick + 2.0F);
                            poseStack.mulPose(Axis.XN.rotationDegrees(-1.2f + Mth.sin(f6) * 0.5f));
                            poseStack.translate(-0.5,-0.5,-1);
                        }
                        else{
                            poseStack.mulPose(Axis.XN.rotationDegrees(2.74f));
                            poseStack.translate(-0.5,-0.5,-1);
                        }
                    }
                    return true;
                }
                return IClientItemExtensions.super.applyForgeHandTransform(poseStack,player,arm,itemInHand,partialTick,equipProcess,swingProcess);
            }
        },OMItems.CHICKEN_WUBS,OMItems.GREEN_WUBS,OMItems.RED_WUBS,OMItems.PURPLE_WUBS,OMItems.NETHER_WUBS,OMItems.TRIAL_WUBS);

        event.registerItem(new IClientItemExtensions() {
            @Nullable
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                return SpearExtension.SPEAR.getValue();
            }

            @Override
            public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
                if(player.getMainHandItem().getItem() instanceof AbstractSpear | player.getOffhandItem().getItem() instanceof AbstractSpear){
                    if(arm == HumanoidArm.RIGHT){
                        if(player.swinging || player.swingTime > 0){
                            poseStack.mulPose(Axis.XN.rotationDegrees(-1.35f));
                            poseStack.translate(0.5,-0.5,-1);
                            return true;
                        }

                        if(player.isUsingItem()){
                            float f6 = itemInHand.getUseDuration(player) - (player.getUseItemRemainingTicks() - partialTick + 2.0F);
                            poseStack.mulPose(Axis.XN.rotationDegrees((-1.2f / (1 + itemInHand.getUseDuration(player) - player.getUseItemRemainingTicks())) + Mth.sin(f6) * 0.5f));
                            poseStack.translate(0.5,-0.5,-1);
                        }
                        else{
                            poseStack.mulPose(Axis.XN.rotationDegrees(2.74f));
                            poseStack.translate(0.5,-0.5,-1);
                        }
                    }
                    else if(arm == HumanoidArm.LEFT){
                        if(player.swinging || player.swingTime > 0){
                            poseStack.mulPose(Axis.XN.rotationDegrees(-1.35f));
                            poseStack.translate(0.5,-0.5,-1);
                            return true;
                        }

                        if(player.isUsingItem()){
                            float f6 = itemInHand.getUseDuration(player) - (player.getUseItemRemainingTicks() - partialTick + 2.0F);
                            poseStack.mulPose(Axis.XN.rotationDegrees((-1.2f / (1 + itemInHand.getUseDuration(player) - player.getUseItemRemainingTicks())) + Mth.sin(f6) * 0.5f));
                            poseStack.translate(-0.5,-0.5,-1);
                        }
                        else{
                            poseStack.mulPose(Axis.XN.rotationDegrees(2.74f));
                            poseStack.translate(-0.5,-0.5,-1);
                        }
                    }
                    return true;
                }
                return IClientItemExtensions.super.applyForgeHandTransform(poseStack,player,arm,itemInHand,partialTick,equipProcess,swingProcess);
            }
        },OMItems.WOODEN_SPEAR,OMItems.STONE_SPEAR,OMItems.GOLD_SPEAR,OMItems.IRON_SPEAR,OMItems.DIAMOND_SPEAR,OMItems.NETHERITE_SPEAR);
    }

    @SubscribeEvent
    public static void setupModelLayer(EntityRenderersEvent.RegisterLayerDefinitions event){
        //event.registerLayerDefinition(MultipurposeVehicleModel.MULTIPURPOSE_VEHICLE_LAYER,MultipurposeVehicleModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void setupEntityRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(OMEntityTypes.RED_WUB_ENERGY_BALL.get(),RedWubEnergyBallRenderer::new);
        event.registerEntityRenderer(OMEntityTypes.GREEN_WUB_ENERGY_BALL.get(),GreenWubEnergyBallRenderer::new);
        event.registerEntityRenderer(OMEntityTypes.PURPLE_WUB_ENERGY_BALL.get(),PurpleWubEnergyBallRenderer::new);
        event.registerEntityRenderer(OMEntityTypes.CHICKEN_WUB_ENERGY_BALL.get(),ChickenWubEnergyBallRenderer::new);
        event.registerEntityRenderer(OMEntityTypes.NETHER_WUB_ENERGY_BALL.get(),NetherWubEnergyBallRenderer::new);
        event.registerEntityRenderer(OMEntityTypes.TRIAL_WUB_ENERGY_BALL.get(),TrialWubEnergyBallRenderer::new);
    }
}
