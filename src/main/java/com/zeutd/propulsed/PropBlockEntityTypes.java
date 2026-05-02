package com.zeutd.propulsed;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.zeutd.propulsed.content.creative.CreativeRocketEngineBlockEntity;
import com.zeutd.propulsed.content.large.LargeRocketEngineBlockEntity;
import com.zeutd.propulsed.content.basic.RocketEngineBlockEntity;
import com.zeutd.propulsed.client.RocketEngineRenderer;
import com.zeutd.propulsed.content.large.LargeRocketEngineStructuralBlockEntity;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;

public class PropBlockEntityTypes {
    private static final SimulatedRegistrate REGISTRATE = Propulsed.getRegistrate();

    public static final BlockEntityEntry<RocketEngineBlockEntity> ROCKET_ENGINE = REGISTRATE
            .blockEntity("rocket_engine", RocketEngineBlockEntity::new)
            .validBlocks(PropBlocks.ROCKET_ENGINE)
            .renderer(() -> RocketEngineRenderer::new)
            .register();
    public static final BlockEntityEntry<CreativeRocketEngineBlockEntity> CREATIVE_ROCKET_ENGINE = REGISTRATE
            .blockEntity("creative_rocket_engine", CreativeRocketEngineBlockEntity::new)
            .validBlocks(PropBlocks.CREATIVE_ROCKET_ENGINE)
            .renderer(() -> RocketEngineRenderer::new)
            .register();
    public static final BlockEntityEntry<LargeRocketEngineBlockEntity> LARGE_ROCKET_ENGINE = REGISTRATE
            .blockEntity("large_rocket_engine", LargeRocketEngineBlockEntity::new)
            .validBlocks(PropBlocks.LARGE_ROCKET_ENGINE)
            .renderer(() -> RocketEngineRenderer::new)
            .register();
    public static final BlockEntityEntry<LargeRocketEngineStructuralBlockEntity> LARGE_ROCKET_ENGINE_STRUCTURAL = REGISTRATE
            .blockEntity("large_rocket_engine_structure", LargeRocketEngineStructuralBlockEntity::new)
            .validBlocks(PropBlocks.LARGE_ROCKET_ENGINE_STRUCTURAL)
            .register();
    public static void init() {
    }
}
