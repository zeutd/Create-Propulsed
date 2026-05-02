package com.zeutd.propulsed.content;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.CenteredSideValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.zeutd.propulsed.config.PropConfig;
import com.zeutd.propulsed.content.basic.RocketEngineBlock;
import dev.ryanhcode.sable.api.block.BlockEntitySubLevelActor;
import dev.ryanhcode.sable.api.block.propeller.BlockEntityPropeller;
import dev.ryanhcode.sable.api.physics.force.ForceGroups;
import dev.ryanhcode.sable.api.physics.force.QueuedForceGroup;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.util.List;

public abstract class BaseRocketEngine extends SmartBlockEntity implements BlockEntitySubLevelActor, BlockEntityPropeller, IHaveGoggleInformation, IRocketEngine {

    public static final Vector3d THRUST_VECTOR = new Vector3d();
    public static final Vector3d THRUST_POSITION = new Vector3d();
    public static final Component THROTTLE_TITLE = Component.translatable("aeronautics.display_source.propeller.thrust");


    public RocketEngineBehavior reb;
    protected ScrollValueBehaviour inputThrottle;

    protected int signal = 0;

    public BaseRocketEngine(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.setLazyTickRate(20);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        this.inputThrottle = new ScrollValueBehaviour(
                THROTTLE_TITLE,
                this, new CenteredSideValueBoxTransform((b, d) -> b.getValue(WrenchableDirectionalBlock.FACING).getAxis() != d.getAxis()));
        this.inputThrottle.between(1, 100);
        this.inputThrottle.setValue(100);
        this.inputThrottle.withFormatter(num -> num + "%");
        behaviours.add(inputThrottle);
        reb = createBehavior();
        behaviours.add(reb);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        IHaveGoggleInformation.super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        return true;
    }

    @Override
    public void sable$physicsTick(final ServerSubLevel subLevel, final RigidBodyHandle handle, final double timeStep) {
        if (isActive()) {
            final Vec3 thrustDirection = Vec3.atLowerCornerOf(getBlockDirection().getNormal());
            this.applyForces(subLevel, thrustDirection, timeStep);
        }
    }

    public void applyForces(final ServerSubLevel subLevel, final Vec3 thrustDirection, final double timeStep) {
        final Vec3 thrust = thrustDirection.scale(getThrust() * timeStep);

        THRUST_POSITION.set(JOMLConversion.atCenterOf(getBlockPos()));
        THRUST_VECTOR.set(thrust.x, thrust.y, thrust.z);

        final QueuedForceGroup forceGroup = subLevel.getOrCreateQueuedForceGroup(ForceGroups.PROPULSION.get());
        forceGroup.applyAndRecordPointForce(new Vector3d(THRUST_POSITION), new Vector3d(THRUST_VECTOR));
    }

    public RocketEngineBehavior createBehavior() {
        final RocketEngineBehavior behavior = new RocketEngineBehavior(this);
        behavior.setThrustDirection(JOMLConversion.toJOML(Vec3.atLowerCornerOf(this.getBlockDirection().getOpposite().getNormal())));

        behavior.setParticleAmountUpdater(() -> 10. * PropConfig.client().rocketEngineParticleDensity.getF() * getRedstoneThrottle());
        behavior.setParticleCountProperties(12);
        behavior.setParticlePositionUpdater((v, random) -> {
            final double R = Math.sqrt(Mth.lerp(random.nextFloat(), 0, 9f * 9f / 16f / 16f));
            final double angle = Math.PI * 2.0 * random.nextFloat();
            v.set(Math.cos(angle) * R, 0.26, Math.sin(angle) * R);
            this.getBlockDirection().getOpposite().getRotation().transform(v);
        });

        return behavior;
    }

    @Override
    public Direction getBlockDirection() {
        return this.getBlockState().getValue(BlockStateProperties.FACING);
    }

    @Override
    public double getAirflow() {
        return getThrust();
    }

    @Override
    public double getThrust() {
        return inputThrottle.getValue() / 100. * getRedstoneThrottle() * getMaxThrust();
    }

    @Override
    public double getRedstoneThrottle(){
        return 1 - signal / 15f;
    }

    @Override
    public boolean isActive() {
        return signal < 15;
    }

    @Override
    public void tick() {
        super.tick();
        this.getLevel().setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(RocketEngineBlock.POWERED, this.signal > 0));
        if (this.isActive() && !this.isVirtual()) {
            this.onActiveTick();
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        updateSignal();
    }

    public void updateSignal(){
        signal = getLevel().getBestNeighborSignal(getBlockPos());
    }

    /**
     * Called every tick this rocket engine is active
     */
    public void onActiveTick() {
        this.reb.spawnParticles();
    }

    @Override
    public void write(final CompoundTag compound, final HolderLookup.Provider registries, final boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putInt("Signal", signal);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        signal = tag.getInt("Signal");
    }
}
