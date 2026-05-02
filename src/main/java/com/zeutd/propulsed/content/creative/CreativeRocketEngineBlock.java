package com.zeutd.propulsed.content.creative;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.zeutd.propulsed.PropBlockEntityTypes;
import com.zeutd.propulsed.content.basic.RocketEngineBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class CreativeRocketEngineBlock extends WrenchableDirectionalBlock implements IBE<CreativeRocketEngineBlockEntity> {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public CreativeRocketEngineBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false));
    }

    @Override
    public Class<CreativeRocketEngineBlockEntity> getBlockEntityClass() {
        return CreativeRocketEngineBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CreativeRocketEngineBlockEntity> getBlockEntityType() {
        return PropBlockEntityTypes.CREATIVE_ROCKET_ENGINE.get();
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        super.createBlockStateDefinition(builder);
    }
}
