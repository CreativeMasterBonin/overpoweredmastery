package net.rk.overpoweredmastery;

import net.minecraft.client.model.HumanoidModel;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

public class OMEnumExtensions {
    public static final EnumProxy<HumanoidModel.ArmPose> OM_LONG_SPEAR = new EnumProxy<>(
            HumanoidModel.ArmPose.class,true,true, OMArmPoses.LONG_SPEAR_ARM_TRANSFORMER
    );
}
