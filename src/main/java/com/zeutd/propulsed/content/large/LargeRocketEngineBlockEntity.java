package com.zeutd.propulsed.content.large;

import com.zeutd.propulsed.config.PropConfig;
import com.zeutd.propulsed.content.Surrounding;
import com.zeutd.propulsed.content.basic.RocketEngineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LargeRocketEngineBlockEntity extends RocketEngineBlockEntity {
    public LargeRocketEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public int getSize() {
        return 3;
    }

    @Override
    public float getFlameOffset() {
        return 0;
    }

    @Override
    public double getMaxThrust() {
        return 8192. * PropConfig.server().physics.creativeRocketEngineMaxThrust.getF();
    }

    @Override
    public void updateSignal() {
        signal = 0;
        for (Surrounding surrounding : Surrounding.values()){
            signal = Math.max(signal, ((LargeRocketEngineStructuralBlockEntity) getLevel().getBlockEntity(getBlockPos().offset(surrounding.getRelativePos()))).getSignal());
        }
    }
}
