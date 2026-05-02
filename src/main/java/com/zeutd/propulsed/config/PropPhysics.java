package com.zeutd.propulsed.config;

import net.createmod.catnip.config.ConfigBase;

public class PropPhysics extends ConfigBase {

    public final ConfigFloat basicRocketEngineMaxThrust = this.f(1f, 0, Float.MAX_VALUE, "basicRocketEngineMaxThrust", Comments.basicRocketEngineMaxThrust);
    public final ConfigFloat creativeRocketEngineMaxThrust = this.f(1f, 0, Float.MAX_VALUE, "creativeRocketEngineMaxThrust", Comments.creativeRocketEngineMaxThrust);
    public final ConfigFloat largeRocketEngineMaxThrust = this.f(1f, 0, Float.MAX_VALUE, "largeRocketEngineMaxThrust", Comments.largeRocketEngineMaxThrust);


    @Override
    public String getName() {
        return "physics";
    }

    private static class Comments {
        static String basicRocketEngineMaxThrust = "Thrust scaling for Basic Rocket Engine";
        static String largeRocketEngineMaxThrust = "Thrust scaling for Large Rocket Engine";
        static String creativeRocketEngineMaxThrust = "Thrust scaling for Creative Rocket Engine";
    }
}
