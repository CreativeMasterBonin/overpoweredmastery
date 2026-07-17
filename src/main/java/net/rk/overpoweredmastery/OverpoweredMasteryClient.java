package net.rk.overpoweredmastery;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MaterialMapper;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.AtlasManager;
import net.minecraft.resources.Identifier;
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
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.rk.overpoweredmastery.entity.OMEntityTypes;
import net.rk.overpoweredmastery.entity.blockentity.OMBlockEntities;
import net.rk.overpoweredmastery.entity.model.MultiAssemblerModel;
import net.rk.overpoweredmastery.entity.renderer.*;
import net.rk.overpoweredmastery.item.OMItems;
import net.rk.overpoweredmastery.item.custom.AbstractSpear;
import net.rk.overpoweredmastery.menu.OMMenus;
import net.rk.overpoweredmastery.screen.MultiAssemblerScreen;

@Mod(value = OverpoweredMastery.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = OverpoweredMastery.MODID, value = Dist.CLIENT)
public class OverpoweredMasteryClient{
    public OverpoweredMasteryClient(IEventBus eventBus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        //eventBus.addListener(this::setupClientExtensions);
    }
    /*
    {
      "enum": "net/minecraft/client/model/HumanoidModel$ArmPose",
      "name": "overpoweredmastery_long_spear_armpose",
      "constructor": "(ZZLnet/neoforged/neoforge/client/IArmPoseTransformer;)V",
      "parameters": {
        "class": "net/rk/overpoweredmastery/OMEnumExtensions",
        "field": "OM_LONG_SPEAR"
      }
    }
     */

    public static final Identifier MULTI_ASSEMBLER_ATLAS_ID = Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID,"multi_assembler");
    public static final Identifier MULTI_ASSEMBLER_SHEET = Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID,"textures/block_entity/multi_assembler.png");
    public static final MaterialMapper MULTI_ASSEMBLER_MAPPER = new MaterialMapper(MULTI_ASSEMBLER_SHEET,"block_entity/multi_assembler");

    public static KeyMapping.Category OM_KEYMAPPING_CATEGORY = new KeyMapping.Category(
            Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID,"general_keys")
    );

    public static KeyMapping DESCRIPTION_KEY_MAPPING = new KeyMapping("key_mapping.overpoweredmastery.show_description",
            KeyConflictContext.GUI,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_RBRACKET,
            OM_KEYMAPPING_CATEGORY);

    @SubscribeEvent
    public static void registerAtlases(RegisterTextureAtlasesEvent event){
        event.register(new AtlasManager.AtlasConfig(MULTI_ASSEMBLER_SHEET,MULTI_ASSEMBLER_ATLAS_ID,false));
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event){
        event.register(DESCRIPTION_KEY_MAPPING);
    }

    @SubscribeEvent
    public static void setupSpecialRenderers(RegisterSpecialModelRendererEvent event){
        event.register(Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID,"multi_assembler"),
                MultiAssemblerSpecialModelRenderer.Unbaked.MAP_CODEC);
    }

    @SubscribeEvent
    public static void setupSpecialBlockRenderers(RegisterSpecialBlockModelRendererEvent event){}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        BlockEntityRenderers.register(OMBlockEntities.MULTI_ASSEMBLER_BLOCK_ENTITY.get(),MultiAssemblerBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void setupModelLayer(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(MultiAssemblerModel.MULTI_ASSEMBLER_MODEL_LAYER_LOCATION,MultiAssemblerModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void setupEntityRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(OMEntityTypes.RED_WUB_ENERGY_BALL.get(),RedWubEnergyBallRenderer::new);
        event.registerEntityRenderer(OMEntityTypes.GREEN_WUB_ENERGY_BALL.get(),GreenWubEnergyBallRenderer::new);
        event.registerEntityRenderer(OMEntityTypes.PURPLE_WUB_ENERGY_BALL.get(),PurpleWubEnergyBallRenderer::new);
        event.registerEntityRenderer(OMEntityTypes.CHICKEN_WUB_ENERGY_BALL.get(),ChickenWubEnergyBallRenderer::new);
        event.registerEntityRenderer(OMEntityTypes.NETHER_WUB_ENERGY_BALL.get(),NetherWubEnergyBallRenderer::new);
        event.registerEntityRenderer(OMEntityTypes.TRIAL_WUB_ENERGY_BALL.get(),TrialWubEnergyBallRenderer::new);
        event.registerEntityRenderer(OMEntityTypes.FRAUD.get(),FraudRenderer::new);
    }

    @SubscribeEvent
    public static void setupScreen(RegisterMenuScreensEvent event){
        event.register(OMMenus.MULTI_ASSEMBLER_MENU.get(),MultiAssemblerScreen::new);
    }

    public void setupClientExtensions(RegisterClientExtensionsEvent event){
        event.registerItem(LONG_SPEAR_EXTENSION,
                OMItems.TEST_SPEAR,
                OMItems.WOODEN_SPEAR,
                OMItems.STONE_SPEAR,
                OMItems.GOLD_SPEAR,
                OMItems.IRON_SPEAR,
                OMItems.DIAMOND_SPEAR,
                OMItems.NETHERITE_SPEAR,
                OMItems.ULTIMATE_LONG_SPEAR);
    }

    public static final IClientItemExtensions LONG_SPEAR_EXTENSION = new IClientItemExtensions() {
        @Override
        public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
            if(entityLiving.getUsedItemHand() == hand && entityLiving.getItemInHand(hand).is(itemStack.getItem()) && entityLiving.isUsingItem()){
                return OMArmPoses.LONG_SPEAR_ARM_POSE;
            }
            return IClientItemExtensions.super.getArmPose(entityLiving, hand, itemStack);
        }

        @Override
        public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
            if(player.getMainHandItem().getItem() instanceof AbstractSpear || player.getOffhandItem().getItem() instanceof AbstractSpear){
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
            return IClientItemExtensions.super.applyForgeHandTransform(poseStack, player, arm, itemInHand, partialTick, equipProcess, swingProcess);
        }
    };
}
