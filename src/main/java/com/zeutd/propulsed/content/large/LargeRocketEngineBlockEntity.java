package com.zeutd.propulsed.content.large;

import com.zeutd.propulsed.config.PropConfig;
import com.zeutd.propulsed.content.RocketEngineBehavior;
import com.zeutd.propulsed.content.Surrounding;
import com.zeutd.propulsed.content.basic.RocketEngineBlockEntity;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

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
            BlockPos pos = getBlockPos().offset(surrounding.getRelativePos());
            if (Objects.requireNonNull(getLevel()).getBlockEntity(pos) != null && getLevel().getBlockEntity(pos) instanceof LargeRocketEngineStructuralBlockEntity lresbe) {
                signal = Math.max(signal, lresbe.getSignal());
            }
        }
    }

    @Override
    public RocketEngineBehavior createBehavior() {
        final RocketEngineBehavior behavior = new RocketEngineBehavior(this);
        behavior.setThrustDirection(JOMLConversion.toJOML(Vec3.atLowerCornerOf(this.getBlockDirection().getOpposite().getNormal())));

        behavior.setParticleAmountUpdater(() -> 15. * PropConfig.client().rocketEngineParticleDensity.getF() * getRedstoneThrottle());
        behavior.setParticleCountProperties(12);
        behavior.setParticlePositionUpdater((v, random) -> {
            final double R = Math.sqrt(Mth.lerp(random.nextFloat(), 0, 20f * 20f / 16f / 16f));
            final double angle = Math.PI * 2.0 * random.nextFloat();
            v.set(Math.cos(angle) * R, 1.26, Math.sin(angle) * R);
            this.getBlockDirection().getOpposite().getRotation().transform(v);
        });

        return behavior;
    }
}
