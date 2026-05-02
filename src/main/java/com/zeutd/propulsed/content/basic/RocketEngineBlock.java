package com.zeutd.propulsed.content.basic;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.zeutd.propulsed.PropBlockEntityTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class RocketEngineBlock extends WrenchableDirectionalBlock implements IBE<RocketEngineBlockEntity> {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public RocketEngineBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false));
    }

    @Override
    public Class<RocketEngineBlockEntity> getBlockEntityClass() {
        return RocketEngineBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RocketEngineBlockEntity> getBlockEntityType() {
        return PropBlockEntityTypes.ROCKET_ENGINE.get();
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        super.createBlockStateDefinition(builder);
    }
}
