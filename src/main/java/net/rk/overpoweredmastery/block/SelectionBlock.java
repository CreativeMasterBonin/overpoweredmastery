package net.rk.overpoweredmastery.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.TriState;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.rk.overpoweredmastery.entity.blockentity.OMBlockEntities;
import net.rk.overpoweredmastery.entity.blockentity.SelectionBlockEntity;
import org.jetbrains.annotations.Nullable;

public class SelectionBlock extends BaseEntityBlock {
    public static final MapCodec<SelectionBlock> CODEC = simpleCodec(SelectionBlock::new);

    public SelectionBlock(BlockBehaviour.Properties properties){
        super(properties
                .noCollission()
                .noLootTable()
                .pushReaction(PushReaction.BLOCK)
                .mapColor(MapColor.COLOR_BLACK)
                .sound(SoundType.TRIAL_SPAWNER)
                .destroyTime(-1.000f)
                .explosionResistance(3600000.000f)
                .instrument(NoteBlockInstrument.BIT)
                .noTerrainParticles()
                .forceSolidOn()
        );
    }

    @Override
    public boolean makesOpenTrapdoorAboveClimbable(BlockState state, LevelReader level, BlockPos pos, BlockState trapdoorState) {
        return true;
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        return Shapes.block();
    }

    @Override
    public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter level, BlockPos pos, FluidState fluidState) {
        return false;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return pathComputationType == PathComputationType.AIR && !this.hasCollision || super.isPathfindable(state, pathComputationType);
    }

    @Override
    protected boolean canBeReplaced(BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return false;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier){
        if(level instanceof ServerLevel serverLevel){
            if(serverLevel.getBlockEntity(pos) instanceof SelectionBlockEntity sbe){
                if(entity instanceof Player player){
                    sbe.addTickTime(80);
                    if(!player.hasEffect(MobEffects.INVISIBILITY)){
                        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY,80,0,true,false,false));
                    }
                    try{
                        int allTheColors = ARGB.color(Mth.randomBetweenInclusive(
                                        level.getRandom(), 0, 255),
                                Mth.randomBetweenInclusive(level.getRandom(), 0, 255),
                                Mth.randomBetweenInclusive(level.getRandom(), 0, 255));
                        serverLevel.sendParticles(
                                new DustParticleOptions(allTheColors,level.getRandom().triangle(0.51f,0.97f)),
                                pos.getX() + 0.0 - level.getRandom().triangle(-0.5f,0.5f),
                                pos.getY() + 0.5,
                                pos.getZ() + 0.0 - level.getRandom().triangle(-0.5f,0.5f),
                                1,
                                0,0,0,0.25D);
                    }
                    catch (Exception e){

                    }
                }
                else{
                    sbe.setTickTime(sbe.getTicksTillRemoval());
                }
            }
        }
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return true;
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0f;
    }

    @Override
    public TriState canSustainPlant(BlockState state, BlockGetter level, BlockPos soilPosition, Direction facing, BlockState plant) {
        return TriState.TRUE;
    }

    @Override
    public boolean isPossibleToRespawnInThis(BlockState state) {
        return false;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SelectionBlockEntity(pos,state);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == OMBlockEntities.SELECTION_BLOCK_ENTITY.get() ? (BlockEntityTicker<T>) SelectionBlockEntity::serverTick : null;
    }
}
