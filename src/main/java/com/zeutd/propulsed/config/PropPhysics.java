package com.zeutd.propulsed.config;

import net.createmod.catnip.config.ConfigBase;

public class PropPhysics extends ConfigBase {

    public final ConfigFloat basicRocketEngineMaxThrust = this.f(1f, 0, Float.MAX_VALUE, "basicRocketEngineMaxThrust", Comments.basicRocketEngineMaxThrust);


    @Override
    public String getName() {
        return "physics";
    }

    private static class Comments {
        static String basicRocketEngineMaxThrust = "Thrust scaling for Basic Rocket Engine";
    }
}
