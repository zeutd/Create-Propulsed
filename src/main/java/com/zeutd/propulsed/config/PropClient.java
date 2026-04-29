package com.zeutd.propulsed.config;

import com.simibubi.create.infrastructure.config.CClient;
import net.createmod.catnip.config.ConfigBase;

public class PropClient extends ConfigBase {

    public final ConfigFloat rocketEngineParticleDensity = f(.5f, 0, 1, "rocketEngineParticleDensity",
            Comments.rocketEngineParticleDensity);

    @Override
    public String getName() {
        return "client";
    }

    private static class Comments {
        static String rocketEngineParticleDensity = "Thrust scaling for Basic Rocket Engine";
        static String drawRocketEngineFlame = "Thrust scaling for Basic Rocket Engine";
    }
}
