package net.rk.overpoweredmastery.menu;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.rk.overpoweredmastery.block.OMBlocks;
import net.rk.overpoweredmastery.entity.blockentity.MultiAssemblerBlockEntity;

public class MultiAssemblerMenu extends AbstractContainerMenu {
    private final MultiAssemblerBlockEntity copiedBE;
    private final Level level;
    private final ContainerData data;
    private final SimpleContainer container;

    public MultiAssemblerMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData){
        this(containerId,playerInventory,playerInventory.player.level().getBlockEntity(extraData.readBlockPos()),new SimpleContainerData(2));
    }

    public MultiAssemblerMenu(int containerId, Inventory playerInventory, BlockEntity be,ContainerData data){
        super(OMMenus.MULTI_ASSEMBLER_MENU.get(), containerId);
        this.copiedBE = (MultiAssemblerBlockEntity)be;
        this.level = playerInventory.player.level();
        this.data = data;
        this.container = new SimpleContainer(7){

        };

        addSlot(new Slot(container,0,26,36));
        addSlot(new Slot(container,1,44,36));
        addSlot(new Slot(container,2,62,36));
        addSlot(new Slot(container,3,80,36));
        addSlot(new Slot(container,4,98,36));
        addSlot(new Slot(container,5,116,36));
        addSlot(new Slot(container,6,134,36));
        addStandardInventorySlots(playerInventory,8,84);
        addDataSlots(data);
    }

    public MultiAssemblerMenu(int containerId, Inventory playerInventory, BlockEntity be, ContainerData data, SimpleContainer container){
        super(OMMenus.MULTI_ASSEMBLER_MENU.get(), containerId);
        this.copiedBE = (MultiAssemblerBlockEntity)be;
        this.level = playerInventory.player.level();
        this.data = data;
        this.container = container;

        addSlot(new Slot(container,0,26,36));
        addSlot(new Slot(container,1,44,36));
        addSlot(new Slot(container,2,62,36));
        addSlot(new Slot(container,3,80,36));
        addSlot(new Slot(container,4,98,36));
        addSlot(new Slot(container,5,116,36));
        addSlot(new Slot(container,6,134,36));
        addStandardInventorySlots(playerInventory,8,84);
        addDataSlots(data);
    }

    @Override
    public boolean isValidSlotIndex(int slotIndex) {
        return slotIndex >= 0 && slotIndex < this.slots.size();
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
    public void removed(Player player) {
        this.container.stopOpen(player);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level,copiedBE.getBlockPos()),
                player,OMBlocks.MULTI_ASSEMBLER.get());
    }
}
