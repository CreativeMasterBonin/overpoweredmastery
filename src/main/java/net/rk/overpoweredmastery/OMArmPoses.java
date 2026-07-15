package net.rk.overpoweredmastery;

import net.minecraft.client.model.HumanoidModel;
import net.neoforged.neoforge.client.IArmPoseTransformer;

/**
 * Contains all client data for arm poses and objects for use with client extensions
 */
public class OMArmPoses {
    public static final HumanoidModel.ArmPose LONG_SPEAR_ARM_POSE = HumanoidModel.ArmPose.valueOf("OM_LONG_SPEAR");
    public static final IArmPoseTransformer LONG_SPEAR_ARM_TRANSFORMER = (humanoidModel, livingEntity, humanoidArm) -> {

    };
}
