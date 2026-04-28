package com.zeutd.propulsed.data;

import com.jesz.createdieselgenerators.CDGFluids;
import com.jesz.createdieselgenerators.CDGTags;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.zeutd.propulsed.Propulsed;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.fml.ModList;

public class PropTags {
    public static void addGenerators() {
        Propulsed.getRegistrate().addDataGenerator(ProviderType.FLUID_TAGS, FluidTags::genFluidTags);
    }

    public static class FluidTags {

        public static final TagKey<Fluid> FUEL = create("fuel");
        public static final TagKey<Fluid> GOOD_FUEL = create("good_fuel");

        private static TagKey<Fluid> create(final String path) {
            return TagKey.create(Registries.FLUID, Propulsed.path(path));
        }

        public static void genFluidTags(final RegistrateTagsProvider<Fluid> provIn) {
            final TagGen.CreateTagsProvider<Fluid> prov = new TagGen.CreateTagsProvider<>(provIn, Fluid::builtInRegistryHolder);


            prov.tag(FUEL).add(Fluids.LAVA);

            if (ModList.get().isLoaded("createdieselgenerators")) {
                prov.tag(GOOD_FUEL).add(CDGFluids.DIESEL.get());
                prov.tag(GOOD_FUEL).add(CDGFluids.ETHANOL.get());
                prov.tag(GOOD_FUEL).add(CDGFluids.BIODIESEL.get());
                prov.tag(GOOD_FUEL).add(CDGFluids.GASOLINE.get());
            }
        }
    }
}
