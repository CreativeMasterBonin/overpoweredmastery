package net.rk.overpoweredmastery.entity.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.item.OMItems;
import net.rk.overpoweredmastery.item.custom.PlaceholderItem;
import net.rk.overpoweredmastery.menu.MultiAssemblerMenu;
import net.rk.overpoweredmastery.recipe.MultiAssemblerRecipe;
import net.rk.overpoweredmastery.recipe.MultiAssemblerRecipeInput;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.logging.Logger;

public class MultiAssemblerBlockEntity extends BlockEntity implements MenuProvider {
    private Logger maLogger = Logger.getAnonymousLogger();
    public int assemblyProgress = 0;
    public int assemblyTime = 0;
    public boolean assembling = false;
    public int ticksPassed = 0;
    public boolean mayCraft = false;
    //public static RecipeManager.CachedCheck<MultiAssemblerRecipeInput,MultiAssemblerRecipe> recipeCache = RecipeManager.createCheck(OverpoweredMastery.MULTI_ASSEMBLER_RECIPE.get()); // for all instances to use
    public NonNullList<ItemStack> items = NonNullList.withSize(this.getContainerSize(),ItemStack.EMPTY);
    public IItemHandlerModifiable itemHandler = new IItemHandlerModifiable() {
        @Override
        public void setStackInSlot(int slot, ItemStack stack) {
            items.set(slot,stack);
            this.itemChanged();
        }

        @Override
        public int getSlots() {
            return items.size();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return items.get(slot);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate){
            items.set(slot,stack);
            this.itemChanged();
            return items.get(slot);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate){
            items.get(slot).shrink(amount);
            this.itemChanged();
            return items.get(slot);
        }

        @Override
        public int getSlotLimit(int slot) {
            return items.size() - 1;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return true;
        }

        public void itemChanged(){
            level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
            setChanged();
        }
    };

    private boolean lockout = false;
    private ContainerData progressData;

    public MultiAssemblerBlockEntity(BlockPos pos, BlockState blockState) {
        super(OMBlockEntities.MULTI_ASSEMBLER_BLOCK_ENTITY.get(), pos, blockState);
        this.progressData = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> MultiAssemblerBlockEntity.this.assemblyProgress;
                    case 1 -> MultiAssemblerBlockEntity.this.assemblyTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch(i){
                    case 0:
                        MultiAssemblerBlockEntity.this.assemblyProgress = value;
                        break;
                    case 1:
                        MultiAssemblerBlockEntity.this.assemblyTime = value;
                        break;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public int getContainerSize(){
        return 7;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        this.items = NonNullList.withSize(this.getContainerSize(),ItemStack.EMPTY);
        this.lockout = input.getBooleanOr("lockout",false);
        ContainerHelper.loadAllItems(input,this.items);
        this.assemblyTime = input.getIntOr("assemblyTime",0);
        this.assemblyProgress = input.getIntOr("assemblyProgress",0);
        this.resultItemReference = input.read("resultStack",ItemStack.CODEC).orElse(new ItemStack(OMItems.PLACEHOLDER_ITEM.asItem()));
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        output.putBoolean("lockout",this.lockout);
        ContainerHelper.saveAllItems(output,this.items);
        output.putInt("assemblyTime",assemblyTime);
        output.putInt("assemblyProgress",assemblyProgress);
        output.store("resultStack",ItemStack.CODEC,resultItemReference);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    @Override
    public void handleUpdateTag(ValueInput input) {
        this.loadAdditional(input);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void dropAll(){
        BlockPos modifiedPos = getBlockPos();
        modifiedPos = modifiedPos.offset(0,2,0);

        Containers.dropContents(this.level,modifiedPos,items);
        this.level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
        setChanged();
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if(this.level != null){
            Containers.dropContents(this.level,pos,this.items);
        }
    }

    public MultiAssemblerRecipeInput currentInput;
    public ItemStack resultItemReference = new ItemStack(OMItems.PLACEHOLDER_ITEM.asItem());

    // only works if saved to disk
    public ItemStack getResultItemReference() {
        if(this.level instanceof ServerLevel serverLevel){
            if(serverLevel.getBlockEntity(getBlockPos())  instanceof MultiAssemblerBlockEntity mabe){
                return mabe.resultItemReference;
            }
        }
        return resultItemReference;
    }

    public static <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, T t){
        if(t instanceof MultiAssemblerBlockEntity mabe){
            if(mabe.lockout){
                return;
            }
        }
        if(t instanceof MultiAssemblerBlockEntity ma){
            try{
                ma.ticksPassed++;

                if(ma.ticksPassed % 75 == 0 && ma.level instanceof ServerLevel serverLevel){
                    ma.currentInput = new MultiAssemblerRecipeInput(
                            ma.items.get(0),ma.items.get(1),ma.items.get(2),ma.items.get(3),ma.items.get(4),ma.items.get(5),ma.items.get(6)
                    );
                }
                if(ma.ticksPassed % 20 == 0 && ma.level instanceof ServerLevel serverLevel && ma.currentInput != null){
                    if(ma.currentInput.isEmpty()){
                        ma.assembling = false;
                        ma.assemblyTime = 0;
                        ma.assemblyProgress = 0;
                        ma.level.sendBlockUpdated(blockPos,blockState,blockState,3);
                        ma.resultItemReference = new ItemStack(OMItems.PLACEHOLDER_ITEM.asItem());
                        ma.setChanged();
                        return;
                    }

                    Optional<RecipeHolder<MultiAssemblerRecipe>> multiAssemblerRecipeHolder =
                            serverLevel.recipeAccess().getRecipeFor(OverpoweredMastery.MULTI_ASSEMBLER_RECIPE.get(),ma.currentInput,serverLevel);

                    multiAssemblerRecipeHolder.ifPresentOrElse(recipe -> {
                        if(!(ma.assemblyTime == recipe.value().getAssemblyTimeTicks())){
                            if(ma.resultItemReference != recipe.value().getResultItem()){
                                ma.resultItemReference = recipe.value().getResultItem();
                                //Logger.getAnonymousLogger().info(ma.resultItemReference.toString());
                                ma.setChanged();
                            }
                            ma.assemblyTime = recipe.value().getAssemblyTimeTicks();
                            ma.mayCraft = true;
                            level.sendBlockUpdated(blockPos,blockState,blockState,3);
                            ma.setChanged();
                        }
                        ma.assemblyProgress++;
                        if(ma.assemblyProgress >= ma.assemblyTime){
                            ItemStack itemStack = new ItemStack(recipe.value().getResultItem().getItem());
                            serverLevel.addFreshEntity(new ItemEntity(serverLevel,blockPos.getX(),blockPos.getY(),blockPos.getZ(),itemStack.copy()));
                            ma.items.clear();
                            ma.mayCraft = false;
                            ma.assemblyTime = 0;
                            ma.assemblyProgress = 0;
                            if(!(ma.resultItemReference.getItem() instanceof PlaceholderItem)){
                                ma.resultItemReference = new ItemStack(OMItems.PLACEHOLDER_ITEM.asItem());
                            }
                            level.sendBlockUpdated(blockPos,blockState,blockState,3);
                            ma.setChanged();
                            return;
                        }
                    },() -> {

                    });
                }
                if(ma.ticksPassed % 40 == 0){
                    ma.assembling = ma.assemblyProgress > 0 && ma.assemblyTime > 0;
                    ma.setChanged();
                }

                if(ma.ticksPassed > 32767){
                    ma.ticksPassed = 0;
                }
            }
            catch (Exception e){
                ma.maLogger.warning("Multi Assembler BE has encountered an error: " + e.getMessage());
                ma.lockout = true;
                if(!ma.items.isEmpty()){
                    Containers.dropContents(level,blockPos,ma.items);
                }
                ma.setChanged();
            }
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.overpoweredmastery.multi_assembler");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new MultiAssemblerMenu(containerId,playerInventory,this,this.progressData);
    }
}
