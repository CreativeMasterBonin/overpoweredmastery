package net.rk.overpoweredmastery.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.rk.overpoweredmastery.block.OMBlocks;
import net.rk.overpoweredmastery.entity.blockentity.MultiAssemblerBlockEntity;

public class MultiAssemblerMenu extends AbstractContainerMenu {
    private final MultiAssemblerBlockEntity copiedBE;
    private final Level level;
    private final ContainerData data;

    public MultiAssemblerMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData){
        this(containerId,playerInventory,playerInventory.player.level().getBlockEntity(extraData.readBlockPos()),new SimpleContainerData(2));
    }

    public MultiAssemblerMenu(int containerId, Inventory playerInventory, BlockEntity be,ContainerData data){
        super(OMMenus.MULTI_ASSEMBLER_MENU.get(), containerId);
        this.copiedBE = (MultiAssemblerBlockEntity)be;
        this.level = playerInventory.player.level();
        this.data = data;

        addSlot(new SlotItemHandler(copiedBE.itemHandler,0,26,36));
        addSlot(new SlotItemHandler(copiedBE.itemHandler,1,44,36));
        addSlot(new SlotItemHandler(copiedBE.itemHandler,2,62,36));
        addSlot(new SlotItemHandler(copiedBE.itemHandler,3,80,36));
        addSlot(new SlotItemHandler(copiedBE.itemHandler,4,98,36));
        addSlot(new SlotItemHandler(copiedBE.itemHandler,5,116,36));
        addSlot(new SlotItemHandler(copiedBE.itemHandler,6,134,36));
        addStandardInventorySlots(playerInventory,8,84);
        addDataSlots(data);
    }

    public boolean isAssembling(){
        return this.data.get(0) != 0 && this.data.get(1) != 0;
    }

    public int getAssemblyTime(){
        return this.data.get(1);
    }

    public int getAssemblyProgress(){
        return this.data.get(0);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack quickMoveStack = ItemStack.EMPTY;
        Slot quickMoveSlot = this.slots.get(index);
        // do the moves
        if(quickMoveSlot != null && quickMoveSlot.hasItem()){
            ItemStack rawStack = quickMoveSlot.getItem();
            quickMoveStack = rawStack.copy(); // safe copy

            if(index >= 0 && index < 41){
                if(!moveItemStackTo(rawStack,0,6,false)){
                    if(index < 32){
                        if(!this.moveItemStackTo(rawStack,32,41,false)){
                            return ItemStack.EMPTY;
                        }
                    }
                    else if(!this.moveItemStackTo(rawStack,5,32,false)){
                        return ItemStack.EMPTY;
                    }
                }
            }
            else if(!this.moveItemStackTo(rawStack,5,41,false)){
                return ItemStack.EMPTY;
            }

            if(rawStack.isEmpty()){
                quickMoveSlot.setByPlayer(ItemStack.EMPTY);
            }
            else{
                quickMoveSlot.setChanged();
            }

            if(rawStack.getCount() == quickMoveStack.getCount()){
                return ItemStack.EMPTY;
            }

            quickMoveSlot.onTake(player,rawStack);
        }
        return quickMoveStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level,copiedBE.getBlockPos()),
                player,OMBlocks.MULTI_ASSEMBLER.get());
    }
}
