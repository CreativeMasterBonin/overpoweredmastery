package net.rk.overpoweredmastery.util;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.Vec3;
import net.rk.overpoweredmastery.item.OMItems;

import java.util.List;

public class OPServerUtil {
    /**
     * Multipurpose use method to return a block entity data applied stack
     * @param serverLevel The ServerLevel being accessed
     * @param blockPos The BlockPos to check at
     * @param player The Player performing the action
     * @return The finalized ItemStack
     */
    public static ItemStack serverSideCheckGetAndApplyData(ServerLevel serverLevel, BlockPos blockPos, Player player){
        BlockState blockstate = serverLevel.getBlockState(blockPos);
        ItemStack itemstack = blockstate.getCloneItemStack(blockPos,serverLevel,true,player);
        if (!itemstack.isEmpty()) {
            BlockEntity blockentity = blockstate.hasBlockEntity() ? serverLevel.getBlockEntity(blockPos) : null;
            if (blockentity != null) {
                // Need the reporter as we are serializing things server-side
                try (ProblemReporter.ScopedCollector scopedProblemer =
                             new ProblemReporter.ScopedCollector(blockentity.problemPath(), LogUtils.getLogger())){

                    TagValueOutput tagvalueoutput = TagValueOutput.createWithContext(scopedProblemer, serverLevel.registryAccess());
                    blockentity.saveCustomOnly(tagvalueoutput);
                    blockentity.removeComponentsFromTag(tagvalueoutput);
                    BlockItem.setBlockEntityData(itemstack, blockentity.getType(), tagvalueoutput);
                    itemstack.applyComponents(blockentity.collectComponents());
                }
            }
            else{
                return new ItemStack(OMItems.PLACEHOLDER_ITEM.asItem());
            }
        }
        return itemstack;
    }

    /**
     * Multipurpose use method to return a block entity data applied stack
     * @param serverLevel The ServerLevel being accessed
     * @param blockPos The BlockPos to check at
     * @param player The Player performing the action
     * @param extraComponents The extra Components that will be applied to the lore text of the result ItemStack
     * @return The finalized ItemStack
     */
    public static ItemStack serverSideCheckGetAndApplyData(ServerLevel serverLevel, BlockPos blockPos, Player player, List<Component> extraComponents){
        BlockState blockstate = serverLevel.getBlockState(blockPos);
        ItemStack itemstack = blockstate.getCloneItemStack(blockPos,serverLevel,true,player);
        if (!itemstack.isEmpty()) {
            BlockEntity blockentity = blockstate.hasBlockEntity() ? serverLevel.getBlockEntity(blockPos) : null;
            if (blockentity != null) {
                // Need the reporter as we are serializing things server-side
                try (ProblemReporter.ScopedCollector scopedProblemer =
                             new ProblemReporter.ScopedCollector(blockentity.problemPath(), LogUtils.getLogger())){

                    TagValueOutput tagvalueoutput = TagValueOutput.createWithContext(scopedProblemer, serverLevel.registryAccess());
                    blockentity.saveCustomOnly(tagvalueoutput);
                    blockentity.removeComponentsFromTag(tagvalueoutput);
                    BlockItem.setBlockEntityData(itemstack, blockentity.getType(), tagvalueoutput);
                    itemstack.applyComponents(blockentity.collectComponents());

                    if(!extraComponents.isEmpty()){
                        MutableComponent packedComponent = Component.literal("");

                        for(Component component : extraComponents){
                            packedComponent.append(component);
                        }

                        itemstack.set(DataComponents.LORE,itemstack.get(DataComponents.LORE)
                                .withLineAdded(packedComponent));
                    }
                }
            }
            else{
                return new ItemStack(OMItems.PLACEHOLDER_ITEM.asItem());
            }
        }
        return itemstack;
    }

    public static void fireArrowForStaffUse(ServerLevel serverLevel, LivingEntity entity, Vec3 multipliedMovementOffset, float eyeOffset, int remainingDuration, float durationOffset, boolean shouldBeOnFire, int fireTicksLeft){
        Arrow arrow = new Arrow(serverLevel,entity,new ItemStack(Items.ARROW),new ItemStack(Items.BOW));
        arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        arrow.setDeltaMovement(entity.getViewVector((float)remainingDuration + durationOffset));
        arrow.setDeltaMovement(arrow.getDeltaMovement().multiply(multipliedMovementOffset.x,multipliedMovementOffset.y,multipliedMovementOffset.z));
        arrow.setPos(arrow.getX(),entity.getEyeY() + eyeOffset,arrow.getZ());

        if(shouldBeOnFire){
            arrow.setRemainingFireTicks(fireTicksLeft);
        }
        serverLevel.addFreshEntity(arrow);
    }

    /**
     * Used to fire an arrow server-side with offsets and whether to be on fire
     * @param serverLevel The ServerLevel instance
     * @param entity The LivingEntity that is performing the action
     * @param multipliedMovementOffset The multiplied movement offset of the arrow
     * @param eyeOffset The position relative to the eye pos offset
     * @param remainingDuration The remaining duration on the item being used
     * @param durationOffset The offset for the remainder of the duration of the item being used
     * @param shouldBeOnFire Whether the arrow should be on fire
     * @param fireTicksLeft The ticks left until the arrow is extinguished
     */
    public static void fireArrowForStaffUseUltimate(ServerLevel serverLevel, LivingEntity entity, Vec3 multipliedMovementOffset, float eyeOffset, int remainingDuration, float durationOffset, boolean shouldBeOnFire, int fireTicksLeft){
        Arrow arrow = new Arrow(serverLevel,entity,new ItemStack(Items.ARROW),new ItemStack(OMItems.ULTIMATE_BOW.asItem()));
        arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        arrow.setDeltaMovement(entity.getViewVector((float)remainingDuration + durationOffset));
        arrow.setDeltaMovement(arrow.getDeltaMovement().multiply(multipliedMovementOffset.x,multipliedMovementOffset.y,multipliedMovementOffset.z));
        arrow.setPos(arrow.getX(),entity.getEyeY() + eyeOffset,arrow.getZ());

        if(shouldBeOnFire){
            arrow.setRemainingFireTicks(fireTicksLeft);
        }
        serverLevel.addFreshEntity(arrow);
    }

    /**
     * Fire a generally weak fire arrow
     * @param serverLevel The ServerLevel that the arrow will be added to
     * @param entity The LivingEntity shooter of the arrow
     * @param remainingDuration The remainder of the duration on the item being used by the LivingEntity
     */
    public static void doDefaultArrowSpawn(ServerLevel serverLevel, LivingEntity entity, int remainingDuration){
        OPServerUtil.fireArrowForStaffUse(serverLevel,entity,
                new Vec3(2D,3D,2D),0.25f,
                remainingDuration,5.0f,
                true,75);
    }
}
