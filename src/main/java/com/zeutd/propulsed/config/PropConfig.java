package com.zeutd.propulsed.config;

import net.createmod.catnip.config.ConfigBase;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class PropConfig {
    public static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);

    static PropConfig INSTANCE = new PropConfig();

    public static PropServer server() {
        return INSTANCE.getServerConfig();
    }

    public static PropClient client() {
        return INSTANCE.getClientConfig();
    }

    private static PropServer server;
    private static PropClient client;

    public PropServer getServerConfig() {
        return server;
    }

    public PropClient getClientConfig() {
        return client;
    }

    private static <T extends ConfigBase> T register(final Supplier<T> factory, final ModConfig.Type side) {
        final Pair<T, ModConfigSpec> specPair = (new ModConfigSpec.Builder()).configure((builder) -> {
            final T config = factory.get();
            config.registerAll(builder);
            return config;
        });

        final T config = specPair.getLeft();
        config.specification = specPair.getRight();
        CONFIGS.put(side, config);
        return config;
    }
    
    public static void register(final ModContainer container) {
        server = register(PropServer::new, ModConfig.Type.SERVER);
        client = register(PropClient::new, ModConfig.Type.CLIENT);

        for (final Map.Entry<ModConfig.Type, ConfigBase> typeConfigBaseEntry : CONFIGS.entrySet()) {
            container.registerConfig(typeConfigBaseEntry.getKey(), typeConfigBaseEntry.getValue().specification);
        }
    }
}
