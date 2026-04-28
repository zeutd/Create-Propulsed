package com.zeutd.propulsed;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.zeutd.propulsed.content.RocketEngineBlockEntity;
import com.zeutd.propulsed.client.RocketEngineRenderer;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;

public class PropBlockEntityTypes {
    private static final SimulatedRegistrate REGISTRATE = Propulsed.getRegistrate();

    public static final BlockEntityEntry<RocketEngineBlockEntity> ROCKET_ENGINE = REGISTRATE
            .blockEntity("rocket_engine", RocketEngineBlockEntity::new)
            .validBlocks(PropBlocks.ROCKET_ENGINE)
            .renderer(() -> RocketEngineRenderer::new)
            .register();
    public static void init() {
    }
}
