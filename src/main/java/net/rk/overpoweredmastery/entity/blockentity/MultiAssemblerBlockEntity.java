package net.rk.overpoweredmastery.entity.blockentity;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.StacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStackResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.item.OMItems;
import net.rk.overpoweredmastery.item.custom.PlaceholderItem;
import net.rk.overpoweredmastery.menu.MultiAssemblerMenu;
import net.rk.overpoweredmastery.recipe.MultiAssemblerRecipe;
import net.rk.overpoweredmastery.recipe.MultiAssemblerRecipeInput;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class MultiAssemblerBlockEntity extends BlockEntity implements ItemOwner, MenuProvider {
    private Logger maLogger = Logger.getAnonymousLogger();
    public int assemblyProgress = 0;
    public int assemblyTime = 0;
    public boolean assembling = false;
    public int ticksPassed = 0;
    public boolean mayCraft = false;
    private boolean lockout = false;
    private ContainerData progressData;

    public NonNullList<ItemStack> itemStacks;
    public SimpleContainer container;

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
        itemStacks = NonNullList.withSize(getContainerSize(),ItemStack.EMPTY);
        container = new SimpleContainer(getContainerSize());
    }

    public int getContainerSize(){
        return 7;
    }

    @Override
    public void loadAdditional(ValueInput input) {
        this.lockout = input.getBooleanOr("lockout",false);
        this.mayCraft = input.getBooleanOr("mayCraft",false);
        this.assembling = input.getBooleanOr("assembling",false);
        this.assemblyTime = input.getIntOr("assemblyTime",0);
        this.assemblyProgress = input.getIntOr("assemblyProgress",0);
        this.resultItemReference = input.read("resultStack",ItemStack.CODEC).orElse(new ItemStack(OMItems.PLACEHOLDER_ITEM.asItem()));
        this.itemStacks = NonNullList.withSize(getContainerSize(),ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input,itemStacks);
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        output.putBoolean("lockout",this.lockout);
        output.putBoolean("mayCraft",mayCraft);
        output.putBoolean("assembling",assembling);
        output.putInt("assemblyTime",assemblyTime);
        output.putInt("assemblyProgress",assemblyProgress);
        output.store("resultStack",ItemStack.CODEC,resultItemReference);
        ContainerHelper.saveAllItems(output,itemStacks,false);
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

        Containers.dropContents(this.level,modifiedPos,itemStacks);
        this.level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
        setChanged();
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if(this.level != null){
            Containers.dropContents(this.level,pos,itemStacks);
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

                ItemStack firstEss = ma.itemStacks.get(0);
                ItemStack secEss = ma.itemStacks.get(1);
                ItemStack thirdEss = ma.itemStacks.get(2);
                ItemStack fourEss = ma.itemStacks.get(3);
                ItemStack extra = ma.itemStacks.get(4);
                ItemStack craftingItem = ma.itemStacks.get(5);
                ItemStack additive = ma.itemStacks.get(6);

                if(ma.ticksPassed % 75 == 0 && ma.level instanceof ServerLevel serverLevel){
                    ma.currentInput = new MultiAssemblerRecipeInput(
                            firstEss,secEss,thirdEss,fourEss,extra,craftingItem,additive
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

                            // try deleting all items
                            LogUtils.getLogger().debug(ma.itemStacks.toString());
                            for(int ind = 0; ind < ma.getContainerSize() - 1; ind++){
                                ma.itemStacks.set(ind,ItemStack.EMPTY);
                            }
                            LogUtils.getLogger().debug(ma.itemStacks.toString());

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
                // update container slots
                if(ma.container != null){
                    for(int slot = 0; slot < ma.itemStacks.size() - 1; slot++){
                        ma.container.setItem(slot,ma.itemStacks.get(slot));
                        level.setBlocksDirty(blockPos,blockState,blockState);
                        ma.setChanged();
                    }
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
                // try to recover
                ma.maLogger.warning("Multi Assembler BE has encountered an error: " + e.getMessage());
                ma.lockout = true;
                if(!ma.itemStacks.isEmpty()){
                    ma.container.clearContent();
                    Containers.dropContents(level,blockPos,ma.itemStacks);
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
        return new MultiAssemblerMenu(containerId,playerInventory,this,this.progressData,this.container);
    }

    @Override
    public Level level() {
        return this.getLevel();
    }

    @Override
    public Vec3 position() {
        return this.getBlockPos().getCenter();
    }

    @Override
    public float getVisualRotationYInDegrees() {
        return 0;
    }
}
