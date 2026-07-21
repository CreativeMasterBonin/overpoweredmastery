package net.rk.overpoweredmastery.util;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
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
}
