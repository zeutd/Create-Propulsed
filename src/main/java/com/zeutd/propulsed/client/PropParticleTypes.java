package com.zeutd.propulsed.client;

import com.simibubi.create.foundation.particle.ICustomParticleData;
import com.simibubi.create.foundation.utility.CreateLang;
import com.zeutd.propulsed.Propulsed;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;
import java.util.function.Supplier;

public enum PropParticleTypes {

    ROCKET_ENGINE_SMOKE(() -> RocketEngineSmokeParticleData.INSTANCE);

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Propulsed.MOD_ID);

    public final ParticleEntry<?> entry;

    <D extends ParticleOptions> PropParticleTypes(final Supplier<? extends ICustomParticleData<D>> typeFactory){
        this.entry = new ParticleEntry<>(typeFactory);
    }

    public ParticleType<?> get() {
        return this.entry.object;
    }

    public static void registerClientParticles(final Consumer<ParticleEntry<?>> consume) {
        for (final PropParticleTypes value : values()) {
            consume.accept(value.entry);
        }
    }

    public static void register(final IEventBus modEventBus) {
        for (final PropParticleTypes type : PropParticleTypes.values()) {
            final String name = CreateLang.asId(type.name());
            PARTICLE_TYPES.register(name, type::get);
        }

        modEventBus.addListener(PropParticleTypes::registerParticleProviders);
        PARTICLE_TYPES.register(modEventBus);
    }

    public static void registerParticleProviders(final RegisterParticleProvidersEvent event) {
        PropParticleTypes.registerClientParticles((x) -> {
            //noinspection rawtypes,unchecked
            x.getTypeFactory().get().register((ParticleType) x.getObject(), event);
        });;
    }

    public static class ParticleEntry<D extends ParticleOptions> {
        private final Supplier<? extends ICustomParticleData<D>> typeFactory;
        private final ParticleType<D> object;

        public ParticleEntry(final Supplier<? extends ICustomParticleData<D>> typeFactory) {
            this.typeFactory = typeFactory;

            this.object = this.typeFactory.get().createType();
        }

        public Supplier<? extends ICustomParticleData<D>> getTypeFactory() {
            return this.typeFactory;
        }

        public ParticleType<D> getObject() {
            return this.object;
        }

    }
}
