package net.rk.overpoweredmastery.util;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.Random;

public class OPUtil {
    public static final int ULTRA_COLOR = 13162472;
    public static final int ULTIMATE_COLOR = 8737009;
    public static final int ULTIMATE_SHARED_DURABILITY = 9999;
    public static final int ULTRA_SHARED_DURABILITY = 99999;
    public static final int PRIMITIVE_STAFF_DURABILITY = 250;
    public static final int ENDERMARINE_STAFF_DURABILITY = 325;

    public static final String MAKES_ULTRA_TOOLS = "item.catalyst_template.overpoweredmastery.makes_type.ultra_tools";
    public static final String MAKES_ULTIMATE_TOOLS = "item.catalyst_template.overpoweredmastery.makes_type.ultimate_tools";

    public static Holder<Enchantment> getEnchantmentHolderFromKeyStatic(Level lvl, ResourceKey<Enchantment> enchantmentResourceKey){
        return lvl.registryAccess().getOrThrow(enchantmentResourceKey);
    }

    public static float nextFloatBetweenInclusive(float min, float max) {
        Random random = new Random();
        return random.nextFloat(max - min + 1) + min;
    }

    public static double nextDoubleBetweenInclusive(double min, double max) {
        Random random = new Random();
        return random.nextDouble(max - min + 1) + min;
    }

    public static int nextIntBetweenInclusive(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    /*
    TEMPLATE - for desc key logic usage

    if(ClientActionHandler.keyMappingPressed(OverpoweredMasteryClient.DESCRIPTION_KEY_MAPPING)){
            tooltipAdder.accept(Component.translatable("item.overpoweredmastery.???.desc.detail")
                    .withStyle(ChatFormatting.GRAY));
        }
        else{
            tooltipAdder.accept(Component.translatable("item.overpoweredmastery.press_desc_key",Component.translatable(OverpoweredMasteryClient.DESCRIPTION_KEY_MAPPING.getKey().getName()))
                    .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
     */

    // multi assembler recipes (disabled)
        /*multiAssembler(
                Ingredient.of(OMItems.INERT_BLUE_ESSENCE),
                Ingredient.of(OMItems.INERT_YELLOW_ESSENCE),
                Ingredient.of(OMItems.INERT_GREEN_ESSENCE),
                Ingredient.of(OMItems.INERT_RED_ESSENCE),
                Ingredient.of(OMItems.AURORAN_PROCESSOR),
                Ingredient.of(Items.IRON_INGOT),
                Ingredient.of(Items.IRON_INGOT),
                2,
                new ItemStack(OMItems.ESSENCE_ELECTRONIC_CORE.asItem(),2),
                OMItems.AURORAN_PROCESSOR.asItem()
        );

        multiAssembler(
                Ingredient.of(items.getOrThrow(OMTags.INERT_ESSENCES)),
                Ingredient.of(items.getOrThrow(OMTags.INERT_ESSENCES)),
                Ingredient.of(items.getOrThrow(OMTags.INERT_ESSENCES)),
                Ingredient.of(items.getOrThrow(OMTags.INERT_ESSENCES)),
                Ingredient.of(items.getOrThrow(OMTags.INERT_ESSENCES)),
                Ingredient.of(items.getOrThrow(Tags.Items.DYES)),
                Ingredient.of(Items.ENDER_PEARL),
                80,
                new ItemStack(OMItems.CONCENTRATED_MULTI_ESSENCE.asItem(),1),
                Items.ENDER_PEARL.asItem()
        );

        multiAssembler(
                Ingredient.of(OMItems.CONCENTRATED_MULTI_ESSENCE),
                Ingredient.of(OMItems.CONCENTRATED_MULTI_ESSENCE),
                Ingredient.of(OMItems.INERT_DARK_ESSENCE),
                Ingredient.of(OMItems.INERT_LIGHT_ESSENCE),
                Ingredient.of(OMItems.INERT_AURORAN_ESSENCE),
                Ingredient.of(Items.NETHERITE_SWORD),
                Ingredient.of(Items.IRON_INGOT),
                320,
                new ItemStack(OMItems.PENULTIMATE_SWORD_CATALYST.asItem(),1),
                OMItems.CONCENTRATED_MULTI_ESSENCE.asItem()
        );

        multiAssembler(
                Ingredient.of(Items.GLOWSTONE),
                Ingredient.of(Items.GLOWSTONE),
                Ingredient.of(Items.GLOWSTONE),
                Ingredient.of(OMItems.INERT_LIGHT_ESSENCE),
                Ingredient.of(OMItems.INERT_LIGHT_ESSENCE),
                Ingredient.of(OMItems.PENULTIMATE_SWORD_CATALYST),
                Ingredient.of(Items.TORCHFLOWER),
                720,
                new ItemStack(OMItems.PENULTIMATE_SWORD_LIGHT.asItem(),1),
                OMItems.PENULTIMATE_SWORD_CATALYST.asItem()
        );

        multiAssembler(
                Ingredient.of(Items.GILDED_BLACKSTONE),
                Ingredient.of(Items.GILDED_BLACKSTONE),
                Ingredient.of(Items.GILDED_BLACKSTONE),
                Ingredient.of(OMItems.INERT_DARK_ESSENCE),
                Ingredient.of(OMItems.INERT_DARK_ESSENCE),
                Ingredient.of(OMItems.PENULTIMATE_SWORD_CATALYST),
                Ingredient.of(Items.NETHER_STAR),
                720,
                new ItemStack(OMItems.PENULTIMATE_SWORD_DARK.asItem(),1),
                OMItems.PENULTIMATE_SWORD_CATALYST.asItem()
        );

        multiAssembler(
                Ingredient.of(Items.NETHERITE_BLOCK),
                Ingredient.of(Items.SHULKER_BOX),
                Ingredient.of(Items.OMINOUS_BOTTLE),
                Ingredient.of(OMItems.CONCENTRATED_MULTI_ESSENCE),
                Ingredient.of(OMItems.PENULTIMATE_SWORD_LIGHT),
                Ingredient.of(OMItems.PENULTIMATE_SWORD_DARK),
                Ingredient.of(OMItems.ULTIMATE_INGOT),
                1200,
                new ItemStack(OMItems.ULTIMATE_SWORD.asItem(),1),
                OMItems.CONCENTRATED_MULTI_ESSENCE.asItem()
        );

        multiAssembler(
                Ingredient.of(OMItems.STRANGE_STONE),
                Ingredient.of(OMItems.INFUSED_CONCENTRATED_MULTI_ESSENCE),
                Ingredient.of(OMItems.INFUSED_CONCENTRATED_MULTI_ESSENCE),
                Ingredient.of(OMItems.INFUSED_CONCENTRATED_MULTI_ESSENCE),
                Ingredient.of(OMItems.INFUSED_CONCENTRATED_MULTI_ESSENCE),
                Ingredient.of(Items.NETHERITE_INGOT),
                Ingredient.of(Items.NETHERITE_INGOT),
                900,
                new ItemStack(OMItems.ULTIMATE_INGOT.asItem(),1),
                OMItems.INFUSED_CONCENTRATED_MULTI_ESSENCE.asItem()
        );*/


    /*public final class WubExtension{
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

    public final class StaffExtension{
        public static final EnumProxy<HumanoidModel.ArmPose> STAFF = new EnumProxy<>(
                HumanoidModel.ArmPose.class,true,(IArmPoseTransformer) StaffExtension::transformer
        );

        public static void transformer(HumanoidModel<?> humanoidModel, HumanoidRenderState humanoidRenderState, HumanoidArm humanoidArm) {
            if(humanoidRenderState.isUsingItem && humanoidRenderState.useItemHand == InteractionHand.MAIN_HAND){
                humanoidModel.rightArm.xRot = -2;
                humanoidModel.rightArm.yRot = Mth.clamp(0.1f + (Mth.cos(1) * humanoidRenderState.ticksUsingItem),0f,0.25f);
                if(!humanoidRenderState.isCrouching && !humanoidRenderState.isVisuallySwimming){
                    humanoidModel.rightLeg.yRot = Mth.clamp(0.5f + (Mth.cos(1) * humanoidRenderState.ticksUsingItem),0f,0.25f);
                    humanoidModel.leftLeg.yRot = Mth.clamp(0.5f - (Mth.cos(1) * humanoidRenderState.ticksUsingItem),-0.25f,0.0f);
                    humanoidModel.leftLeg.zRot = Mth.clamp(0.5f - (Mth.cos(1) * humanoidRenderState.ticksUsingItem),-0.25f,0.0f);
                }
            }
            else if(!humanoidRenderState.isUsingItem && humanoidRenderState.useItemHand == InteractionHand.MAIN_HAND){
                humanoidModel.rightArm.xRot = humanoidModel.leftArm.xRot * -1;
            }

            if(humanoidRenderState.isUsingItem && humanoidRenderState.useItemHand == InteractionHand.OFF_HAND){
                humanoidModel.leftArm.xRot = -2;
                humanoidModel.leftArm.yRot = Mth.clamp(0.1f - (Mth.cos(1) * humanoidRenderState.ticksUsingItem),-0.25f,0.0f);
                if(!humanoidRenderState.isCrouching && !humanoidRenderState.isVisuallySwimming){
                    humanoidModel.leftLeg.yRot = Mth.clamp(0.5f - (Mth.cos(1) * humanoidRenderState.ticksUsingItem),-0.25f,0.0f);
                    humanoidModel.rightLeg.yRot = Mth.clamp(0.5f + (Mth.cos(1) * humanoidRenderState.ticksUsingItem),0f,0.25f);
                    humanoidModel.rightLeg.zRot = Mth.clamp(0.5f + (Mth.cos(1) * humanoidRenderState.ticksUsingItem),0f,0.25f);
                }
            }
            else if(!humanoidRenderState.isUsingItem && humanoidRenderState.useItemHand == InteractionHand.OFF_HAND){
                humanoidModel.leftArm.xRot = humanoidModel.rightArm.xRot * -1;
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
        },OMItems.CHICKEN_WUBS,
                OMItems.GREEN_WUBS,OMItems.RED_WUBS,
                OMItems.PURPLE_WUBS,OMItems.NETHER_WUBS,
                OMItems.TRIAL_WUBS,OMItems.OXIDIZED_TRIAl_WUBS);

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
        },OMItems.WOODEN_SPEAR,OMItems.STONE_SPEAR,
                OMItems.GOLD_SPEAR,OMItems.IRON_SPEAR,
                OMItems.DIAMOND_SPEAR,OMItems.NETHERITE_SPEAR);


        // staff
        event.registerItem(new IClientItemExtensions() {
                               @Nullable
                               @Override
                               public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                                   return StaffExtension.STAFF.getValue();
                               }

                               @Override
                               public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
                                   if(player.getMainHandItem().getItem() instanceof AbstractStaff | player.getOffhandItem().getItem() instanceof AbstractStaff){
                                       float f6 = itemInHand.getUseDuration(player) - (player.getUseItemRemainingTicks() - partialTick + 2.0F);
                                       if(arm == HumanoidArm.RIGHT && !player.isUsingItem()){
                                           poseStack.mulPose(Axis.XN.rotationDegrees(2.74f));
                                           poseStack.translate(0.9,-0.5,-1);
                                       }
                                       else if(arm == HumanoidArm.RIGHT && player.isUsingItem()){
                                           poseStack.mulPose(Axis.XN.rotationDegrees(55.74f));
                                           poseStack.translate(0.85,0.6,-0.9);
                                           poseStack.mulPose(Axis.ZN.rotationDegrees((-1.75f / (1 + itemInHand.getUseDuration(player) - player.getUseItemRemainingTicks())) + Mth.sin(f6) * 3.95f));
                                       }

                                       if(arm == HumanoidArm.LEFT && !player.isUsingItem()){
                                           poseStack.mulPose(Axis.XN.rotationDegrees(2.74f));
                                           poseStack.translate(-0.9,-0.5,-1);
                                       }
                                       else if(arm == HumanoidArm.LEFT && player.isUsingItem()){
                                           poseStack.mulPose(Axis.XN.rotationDegrees(55.74f));
                                           poseStack.translate(-0.85,0.6,-0.9);
                                           poseStack.mulPose(Axis.ZN.rotationDegrees((-1.75f / (1 + itemInHand.getUseDuration(player) - player.getUseItemRemainingTicks())) + Mth.sin(f6) * 3.95f));
                                       }
                                       return true;
                                   }
                                   return IClientItemExtensions.super.applyForgeHandTransform(poseStack,player,arm,itemInHand,partialTick,equipProcess,swingProcess);
                               }
                           },OMItems.ULTIMATE_STAFF);
    }*/
}
