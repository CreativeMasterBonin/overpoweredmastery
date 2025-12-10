package net.rk.overpoweredmastery.entity.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.rk.overpoweredmastery.Config;
import net.rk.overpoweredmastery.block.OMBlocks;

public class SelectionBlockEntity extends BlockEntity{
    public BlockState currentHoldingBlockState = Blocks.COBBLESTONE.defaultBlockState();
    public CompoundTag currentHoldingBlockData;
    public int ticksPassed = 0;
    public int ticksTillRemoval = Config.PHASE_BLOCK_EXISTENCE_TIME.getAsInt();

    public SelectionBlockEntity(BlockPos pos, BlockState blockState) {
        super(OMBlockEntities.SELECTION_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void setState(BlockState newState){
        this.currentHoldingBlockState = newState;
        setChanged();
    }

    public void addTickTime(int ticksTime){
        if(this.ticksTillRemoval - this.ticksPassed < 40){
            int checkedTicksPassed = this.ticksTillRemoval - this.ticksPassed;
            if(checkedTicksPassed <= 0){
                checkedTicksPassed = 0;
                this.ticksPassed = checkedTicksPassed;
            }
            else{
                this.ticksPassed -= ticksTime;
            }
            setChanged();
        }
        return;
    }

    public void setTickTime(int absoluteTicksTime){
        this.ticksPassed = absoluteTicksTime;
        setChanged();
    }

    public int getTickTime(){
        return this.ticksPassed;
    }

    public int getTicksTillRemoval(){
        return this.ticksTillRemoval;
    }

    // broken don't use
    public SelectionBlockEntity(Level level, BlockPos blockPos, BlockState blockState){
        super(OMBlockEntities.SELECTION_BLOCK_ENTITY.get(), blockPos, OMBlocks.SELECTION_BLOCK.get().defaultBlockState());
        currentHoldingBlockState = blockState;
    }

    @Override
    public void handleUpdateTag(ValueInput input) {
        super.handleUpdateTag(input);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        this.currentHoldingBlockState = input.read("BlockState",BlockState.CODEC).orElse(Blocks.COBBLESTONE.defaultBlockState());
        this.ticksPassed = input.getIntOr("ticksPassed",0);
        this.ticksTillRemoval = input.getIntOr("ticksTillRemoval",0);
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        output.store("BlockState",BlockState.CODEC,this.currentHoldingBlockState);
        output.putInt("ticksPassed",this.ticksPassed);
        if(this.ticksTillRemoval >= 32000){ // hard limit as this is a long time
            this.ticksTillRemoval = 32000;
        }
        output.putInt("ticksTillRemoval",this.ticksTillRemoval);
    }

    public static <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, T t){
        SelectionBlockEntity sbe = (SelectionBlockEntity)t;
        if(sbe instanceof SelectionBlockEntity){
            sbe.ticksPassed++;
            if(level instanceof ServerLevel serverLevel){
                if(!sbe.currentHoldingBlockState.isAir()){
                    if(sbe.ticksPassed >= sbe.ticksTillRemoval){
                        serverLevel.setBlock(blockPos,sbe.currentHoldingBlockState,3);
                        serverLevel.sendBlockUpdated(blockPos,blockState,sbe.currentHoldingBlockState,3);
                        if(serverLevel.getRandom().nextIntBetweenInclusive(0,100) <= 10){
                            serverLevel.playSound(null,blockPos,
                                    SoundEvents.TRIAL_SPAWNER_SPAWN_MOB, SoundSource.BLOCKS,
                                    0.25f,level.getRandom().triangle(0.97f,1.1f));
                        }
                    }
                }
                else{
                    serverLevel.setBlock(blockPos,Blocks.AIR.defaultBlockState(),3);
                    serverLevel.sendBlockUpdated(blockPos,blockState,Blocks.AIR.defaultBlockState(),3);
                }
            }
            if(sbe.ticksPassed > 32767){
                sbe.ticksPassed = 0;
            }
        }
    }
}
