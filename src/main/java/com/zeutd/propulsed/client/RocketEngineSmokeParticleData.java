package com.zeutd.propulsed.client;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public class RocketEngineSmokeParticleData implements ParticleOptions, ICustomParticleDataWithSprite<RocketEngineSmokeParticleData> {
    public static final RocketEngineSmokeParticleData INSTANCE = new RocketEngineSmokeParticleData();

    private static final MapCodec<RocketEngineSmokeParticleData> CODEC = MapCodec.unit(INSTANCE);
    private final StreamCodec<FriendlyByteBuf, RocketEngineSmokeParticleData> streamCodec = StreamCodec.unit(this);

    @Override
    public ParticleEngine.SpriteParticleRegistration<RocketEngineSmokeParticleData> getMetaFactory() {
        return RocketEngineSmokeParticle.Factory::new;
    }

    @Override
    public MapCodec<RocketEngineSmokeParticleData> getCodec(final ParticleType<RocketEngineSmokeParticleData> type) {
        return CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, RocketEngineSmokeParticleData> getStreamCodec() {
        return this.streamCodec;
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return PropParticleTypes.ROCKET_ENGINE_SMOKE.get();
    }
}
