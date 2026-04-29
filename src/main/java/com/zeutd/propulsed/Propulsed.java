package com.zeutd.propulsed;

import com.mojang.logging.LogUtils;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.zeutd.propulsed.client.PropParticleTypes;
import com.zeutd.propulsed.config.PropConfig;
import com.zeutd.propulsed.content.RocketEngineBlockEntity;
import com.zeutd.propulsed.data.PropLang;
import com.zeutd.propulsed.data.PropTags;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@EventBusSubscriber(modid = Propulsed.MOD_ID)
@Mod(Propulsed.MOD_ID)
public class Propulsed {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "propulsed";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final NonNullSupplier<SimulatedRegistrate> REGISTRATE = NonNullSupplier.lazy(() ->
            (SimulatedRegistrate) new SimulatedRegistrate(path("propulsed"), MOD_ID).defaultCreativeTab((ResourceKey<CreativeModeTab>) null));


    public Propulsed(final IEventBus modBus, final ModContainer modContainer){
        getRegistrate().addDataGenerator(ProviderType.LANG, PropLang::registrateLang);
        PropParticleTypes.register(modBus);
        getRegistrate().registerEventListeners(modBus);
        PropBlocks.init();
        PropBlockEntityTypes.init();

        LOGGER.info("Propulsed Initialized!");
        PropConfig.register(modContainer);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void gatherDataHighPriority(GatherDataEvent event) {
        if(event.getMods().contains(Propulsed.MOD_ID)) {
            PropTags.addGenerators();
        }
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        RocketEngineBlockEntity.registerCapabilities(event);
    }

    public static SimulatedRegistrate getRegistrate() {
        return REGISTRATE.get();
    }

    public static ResourceLocation path(final String path) {
        return ResourceLocation.tryBuild(MOD_ID, path);
    }

}
