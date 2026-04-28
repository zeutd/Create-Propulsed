package com.zeutd.propulsed.content;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.zeutd.propulsed.client.PropParticleTypes;
import com.zeutd.propulsed.client.RocketEngineSmokeParticleData;
import dev.eriksonn.aeronautics.content.particle.PropellerAirParticle;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class RocketEngineBehavior extends BlockEntityBehaviour implements IHaveGoggleInformation {
    public static final BehaviourType<RocketEngineBehavior> TYPE = new BehaviourType<>("thruster_behaviour");
    private static final Vector3d STORED_MUT_POS = new Vector3d();


    private Vector3dc thrustDirection;

    public double updatedParticleAmount;
    public int maxParticleAmount = 20;

    /**
     * An updater for particle amount called once per tick
     * The actual particle amount is smoothed and clamped using getParticleCount()
     */
    protected Supplier<Double> particleAmountUpdater;
    /**
     * An updater for random particle position called once per spawned particle
     */
    protected BiConsumer<Vector3d, RandomSource> particlePositionUpdater;
    public RocketEngineBehavior(final SmartBlockEntity be) {
        super(be);
    }

    @Override
    public void tick() {
        this.updatedParticleAmount = this.particleAmountUpdater.get();

        super.tick();
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    public void spawnParticles() {
        if (!this.getWorld().isClientSide) return;

        final double speed = this.getParticleSpeed();

        final Vector3d mutSpeed = new Vector3d();

        final RandomSource random = this.getWorld().getRandom();
        int particleCount = this.getParticleCount();
        final Vector3d origin = new Vector3d(this.getPos().getX() + 0.5, this.getPos().getY() + 0.5, this.getPos().getZ() + 0.5);

        for (int i = 0; i < particleCount; i++) {
            this.particlePositionUpdater.accept(STORED_MUT_POS, random);
            STORED_MUT_POS.add(origin);
            final double positionNudge = speed * random.nextFloat();
            STORED_MUT_POS.fma(positionNudge + 0.26, this.thrustDirection);
            this.thrustDirection.mul(speed * Math.exp(-PropellerAirParticle.frictionScale * positionNudge), mutSpeed);
            mutSpeed.add(random.nextGaussian() * 0.05, random.nextGaussian() * 0.05, random.nextGaussian() * 0.05);

            this.getWorld().addParticle(new RocketEngineSmokeParticleData(),
                    STORED_MUT_POS.x, STORED_MUT_POS.y, STORED_MUT_POS.z,
                    mutSpeed.x, mutSpeed.y, mutSpeed.z);
        }
    }

    public int getParticleCount() {
        double count = this.updatedParticleAmount;

        return Math.min((int) (count + this.getWorld().random.nextFloat()), this.maxParticleAmount);
    }

    public float getParticleSpeed() {
        return 0.5f;
    }

    public void setParticleAmountUpdater(final Supplier<Double> supp) {
        this.particleAmountUpdater = supp;
    }

    public void setParticlePositionUpdater(final BiConsumer<Vector3d, RandomSource> cons) {
        this.particlePositionUpdater = cons;
    }

    public void setParticleCountProperties(final int maxParticleAmount) {
        this.maxParticleAmount = maxParticleAmount;
    }

    public void setThrustDirection(final Vector3dc thrustDirection) {
        this.thrustDirection = thrustDirection;
    }
}
