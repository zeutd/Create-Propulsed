package com.zeutd.propulsed.content.large;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.zeutd.propulsed.PropBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.List;
import java.util.Objects;

import static com.zeutd.propulsed.content.PropBlockStateProperties.CAN_INPUT;
import static com.zeutd.propulsed.content.PropBlockStateProperties.SURROUNDING;

public class LargeRocketEngineStructuralBlockEntity extends SmartBlockEntity {

    public final BlockPos master;

    protected int signal = 0;

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
                PropBlockEntityTypes.LARGE_ROCKET_ENGINE_STRUCTURAL.get(),
                (be, side) -> {
                    if (!be.getBlockState().getValue(CAN_INPUT)) return null;
                    BlockEntity entity = Objects.requireNonNull(be.getLevel()).getBlockEntity(be.getMaster());
                    if (entity instanceof LargeRocketEngineBlockEntity lrebe)
                        return lrebe.tank.getCapability();
                    return null;
                });
    }

    public LargeRocketEngineStructuralBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.setLazyTickRate(20);
        master = LargeRocketEngineStructuralBlock.getMaster(pos, state);
    }

    public BlockPos getMaster(){
        return master;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        signal = getLevel().getBestNeighborSignal(getBlockPos());
    }

    public int getSignal() {
        return signal;
    }
}
