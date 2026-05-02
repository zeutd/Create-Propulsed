package com.zeutd.propulsed.content.large;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.zeutd.propulsed.PropBlockEntityTypes;
import com.zeutd.propulsed.PropBlocks;
import com.zeutd.propulsed.content.Surrounding;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.zeutd.propulsed.content.PropBlockStateProperties.CAN_INPUT;
import static com.zeutd.propulsed.content.PropBlockStateProperties.SURROUNDING;

@ParametersAreNonnullByDefault
public class LargeRocketEngineBlock extends WrenchableDirectionalBlock implements IBE<LargeRocketEngineBlockEntity> {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public LargeRocketEngineBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<LargeRocketEngineBlockEntity> getBlockEntityClass() {
        return LargeRocketEngineBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends LargeRocketEngineBlockEntity> getBlockEntityType() {
        return PropBlockEntityTypes.LARGE_ROCKET_ENGINE.get();
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        BlockPos pos = context.getClickedPos();

        for (Surrounding surrounding : Surrounding.values()) {
            BlockPos offset = new BlockPos(surrounding.getRelativePos());
            BlockState occupiedState = context.getLevel()
                    .getBlockState(pos.offset(offset));
            if (!occupiedState.canBeReplaced())
                return null;
        }

        return stateForPlacement;
    }


    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.getBlockTicks()
                .hasScheduledTick(pos, this))
            level.scheduleTick(pos, this, 1);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        Direction facing = pState.getValue(FACING);
        for (Surrounding surrounding : Surrounding.values()) {
            BlockPos structurePos = new BlockPos(surrounding.getRelativePos().offset(pPos));
            BlockState occupiedState = pLevel.getBlockState(structurePos);
            boolean canInput = surrounding.getRelativePos().get(facing.getAxis()) == facing.getNormal().get(facing.getAxis());
            BlockState requiredStructure = PropBlocks.LARGE_ROCKET_ENGINE_STRUCTURAL.getDefaultState()
                    .setValue(SURROUNDING, surrounding.getOpposite())
                    .setValue(CAN_INPUT, canInput);
            if (occupiedState == requiredStructure)
                continue;
            if (!occupiedState.canBeReplaced()) {
                pLevel.destroyBlock(pPos, false);
                return;
            }
            pLevel.setBlockAndUpdate(structurePos, requiredStructure);
        }
    }
}
