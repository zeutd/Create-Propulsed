package com.zeutd.propulsed.content.creative;

import com.zeutd.propulsed.config.PropConfig;
import com.zeutd.propulsed.content.BaseRocketEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CreativeRocketEngineBlockEntity extends BaseRocketEngine {
    public CreativeRocketEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public float getFlameOffset() {
        return 0.25f;
    }

    @Override
    public double getMaxThrust() {
        return 128. * PropConfig.server().physics.creativeRocketEngineMaxThrust.getF();
    }
}
