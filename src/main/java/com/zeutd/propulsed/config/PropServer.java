package com.zeutd.propulsed.config;

import net.createmod.catnip.config.ConfigBase;

public class PropServer extends ConfigBase {

    public final PropPhysics physics = this.nested(0, PropPhysics::new, Comments.physics);
    //public final AeroKinetics kinetics = this.nested(0, AeroKinetics::new, AeroServer.Comments.kinetics);


    @Override
    public String getName() {
        return "server";
    }

    private static class Comments {
        static String kinetics = "Parameters and abilities of Aeronautics's kinetic mechanisms";
        static String physics = "Parameters related to the physics of Aeronautics blocks";
        static String blockConfig = "Parameters and abilities of Aeronautics Blocks";
    }
}
