package com.zeutd.propulsed.content.basic;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.zeutd.propulsed.PropBlockEntityTypes;
import com.zeutd.propulsed.config.PropConfig;
import com.zeutd.propulsed.content.BaseRocketEngine;
import com.zeutd.propulsed.data.PropTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.List;

public class RocketEngineBlockEntity extends BaseRocketEngine {
    public SmartFluidTankBehaviour tank;

    protected float remainingTicks = 0;

    public RocketEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        tank = SmartFluidTankBehaviour.single(this, getFluidCapacity());
        behaviours.add(tank);
    }

    public int getFluidCapacity(){
        return 4000;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
                PropBlockEntityTypes.ROCKET_ENGINE.get(),
                (be, side) -> {
                    if (side == null || side == be.getBlockDirection())
                        return be.tank.getCapability();
                    return null;
                });
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        containedFluidTooltip(tooltip, isPlayerSneaking, tank.getCapability());
        return true;
    }

    public double getFuelBurnRate() {
        if (tank.isEmpty()) return 1;
        if (tank.getCapability().getFluidInTank(0).is(PropTags.FluidTags.GOOD_FUEL)) return 0.05;
        if (tank.getCapability().getFluidInTank(0).is(PropTags.FluidTags.FUEL)) return 0.1;
        return 1;
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
    public double getMaxThrust(){
        return 128. * PropConfig.server().physics.basicRocketEngineMaxThrust.getF();
    }

    @Override
    public boolean isActive() {
        return super.isActive() && (tank.getPrimaryHandler().getFluid().is(PropTags.FluidTags.FUEL) || tank.getPrimaryHandler().getFluid().is(PropTags.FluidTags.GOOD_FUEL));
    }

    /**
     * Called every tick this rocket engine is active
     */
    @Override
    public void onActiveTick() {
        super.onActiveTick();
        if (remainingTicks < 2) {
            remainingTicks += (float) (2 / getFuelBurnRate());
            tank.getPrimaryHandler().drain((int) getThrust(), IFluidHandler.FluidAction.EXECUTE);
        }

        if (remainingTicks >= 0) {
            remainingTicks--;
        }
    }

    @Override
    public void write(final CompoundTag compound, final HolderLookup.Provider registries, final boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putFloat("RemainingTicks", remainingTicks);
        compound.put("Tank", this.tank.getPrimaryTank().writeNBT(registries));
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        remainingTicks = tag.getFloat("RemainingTicks");
        this.tank.getPrimaryTank().readNBT(tag.getCompound("Tank"), registries, clientPacket);
    }
}
