package com.zeutd.propulsed.content.large;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.api.equipment.goggles.IProxyHoveringInformation;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.render.MultiPosDestructionHandler;
import com.zeutd.propulsed.PropBlockEntityTypes;
import com.zeutd.propulsed.PropBlocks;
import com.zeutd.propulsed.content.Surrounding;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Set;

import static com.zeutd.propulsed.content.PropBlockStateProperties.CAN_INPUT;
import static com.zeutd.propulsed.content.PropBlockStateProperties.SURROUNDING;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class LargeRocketEngineStructuralBlock extends Block implements IProxyHoveringInformation, IBE<LargeRocketEngineStructuralBlockEntity> {

    public static final MapCodec<LargeRocketEngineStructuralBlock> CODEC = simpleCodec(LargeRocketEngineStructuralBlock::new);
    
    public LargeRocketEngineStructuralBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(SURROUNDING).add(CAN_INPUT));
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK;
    }

    @Override
    public float getShadeBrightness(BlockState p_221552_, BlockGetter p_221553_, BlockPos p_221554_) {
        return 1F;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return PropBlocks.LARGE_ROCKET_ENGINE.asStack();
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (stillValid(pLevel, pPos, pState))
            pLevel.destroyBlock(getMaster(pPos, pState), true);
    }

    public BlockState playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (stillValid(pLevel, pPos, pState)) {
            BlockPos masterPos = getMaster(pPos, pState);
            pLevel.destroyBlockProgress(masterPos.hashCode(), masterPos, -1);
            if (!pLevel.isClientSide() && pPlayer.isCreative())
                pLevel.destroyBlock(masterPos, false);
        }
        return super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel,
                                  BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (stillValid(pLevel, pCurrentPos, pState)) {
            BlockPos masterPos = getMaster(pCurrentPos, pState);
            if (!pLevel.getBlockTicks()
                    .hasScheduledTick(masterPos, PropBlocks.LARGE_ROCKET_ENGINE.get()))
                pLevel.scheduleTick(masterPos, PropBlocks.LARGE_ROCKET_ENGINE.get(), 1);
            return pState;
        }
        if (!(pLevel instanceof Level level) || level.isClientSide())
            return pState;
        if (!level.getBlockTicks()
                .hasScheduledTick(pCurrentPos, this))
            level.scheduleTick(pCurrentPos, this, 1);
        return pState;
    }

    public static BlockPos getMaster(BlockPos pos, BlockState state) {
        Surrounding surrounding = state.getValue(SURROUNDING);
        return pos.offset(surrounding.getRelativePos());
    }

    public boolean stillValid(BlockGetter level, BlockPos pos, BlockState state) {
        if (!state.is(this))
            return false;

        Surrounding surrounding = state.getValue(SURROUNDING);
        BlockPos targetedPos = pos.offset(surrounding.getRelativePos());
        BlockState targetedState = level.getBlockState(targetedPos);
        
        return targetedState.getBlock() instanceof LargeRocketEngineBlock;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!stillValid(pLevel, pPos, pState))
            pLevel.setBlockAndUpdate(pPos, Blocks.AIR.defaultBlockState());
    }

    @Override
    public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2,
                                     LivingEntity entity, int numberOfParticles) {
        return true;
    }

    @Override
    public Class<LargeRocketEngineStructuralBlockEntity> getBlockEntityClass() {
        return LargeRocketEngineStructuralBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends LargeRocketEngineStructuralBlockEntity> getBlockEntityType() {
        return PropBlockEntityTypes.LARGE_ROCKET_ENGINE_STRUCTURAL.get();
    }

    public static class RenderProperties implements IClientBlockExtensions, MultiPosDestructionHandler {

        @Override
        public boolean addDestroyEffects(BlockState state, Level Level, BlockPos pos, ParticleEngine manager) {
            return true;
        }

        @Override
        public boolean addHitEffects(BlockState state, Level level, HitResult target, ParticleEngine manager) {
            if (target instanceof BlockHitResult bhr) {
                BlockPos targetPos = bhr.getBlockPos();
                LargeRocketEngineStructuralBlock largeRocketEngineStructuralBlock = PropBlocks.LARGE_ROCKET_ENGINE_STRUCTURAL.get();
                if (largeRocketEngineStructuralBlock.stillValid(level, targetPos, state))
                    manager.crack(LargeRocketEngineStructuralBlock.getMaster(targetPos, state), bhr.getDirection());
                return true;
            }
            return IClientBlockExtensions.super.addHitEffects(state, level, target, manager);
        }

        @Override
        @Nullable
        public Set<BlockPos> getExtraPositions(ClientLevel level, BlockPos pos, BlockState blockState, int progress) {
            LargeRocketEngineStructuralBlock largeRocketEngineStructuralBlock = PropBlocks.LARGE_ROCKET_ENGINE_STRUCTURAL.get();
            if (!largeRocketEngineStructuralBlock.stillValid(level, pos, blockState))
                return null;
            HashSet<BlockPos> set = new HashSet<>();
            set.add(LargeRocketEngineStructuralBlock.getMaster(pos, blockState));
            return set;
        }
    }

    @Override
    public BlockPos getInformationSource(Level level, BlockPos pos, BlockState state) {
        return stillValid(level, pos, state) ? getMaster(pos, state) : pos;
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }
    @Override
    protected @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
