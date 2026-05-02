package com.zeutd.propulsed;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.builders.Builder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.zeutd.propulsed.content.PropBlockStateProperties;
import com.zeutd.propulsed.content.creative.CreativeRocketEngineBlock;
import com.zeutd.propulsed.content.large.LargeRocketEngineBlock;
import com.zeutd.propulsed.content.basic.RocketEngineBlock;
import com.zeutd.propulsed.content.large.LargeRocketEngineBlockItem;
import com.zeutd.propulsed.content.large.LargeRocketEngineStructuralBlock;
import dev.simulated_team.simulated.index.SimItems;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

@SuppressWarnings("removal")
public class PropBlocks {

    private static final SimulatedRegistrate REGISTRATE = Propulsed.getRegistrate();

    public static final BlockEntry<RocketEngineBlock> ROCKET_ENGINE =
            REGISTRATE.block("rocket_engine", RocketEngineBlock::new)
                    .initialProperties(SharedProperties::netheriteMetal)
                    .lang("Basic Rocket Engine")
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .blockstate((ctx, prov) ->
                            prov.directionalBlock(ctx.getEntry(), blockState -> {
                                boolean isPowered = blockState.getValue(BlockStateProperties.POWERED);
                                if (!isPowered) return prov.models().getExistingFile(prov.modLoc("block/" +  ctx.getName()));
                                return prov.models().withExistingParent("block/" + ctx.getName() + "_powered", prov.modLoc("block/" +  ctx.getName())).texture("0", prov.modLoc("block/" + ctx.getName() + "_powered"));
                            }))
                    .item()
                    .transform(Builder::build)
                    .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                            .pattern("EPE")
                            .pattern("ASA")
                            .pattern("FIF")
                            .define('P', AllBlocks.MECHANICAL_PUMP.asItem())
                            .define('E', AllBlocks.FLUID_PIPE.asItem())
                            .define('A', SimItems.ENGINE_ASSEMBLY)
                            .define('S', AllBlocks.INDUSTRIAL_IRON_BLOCK)
                            .define('I', AllItems.STURDY_SHEET)
                            .define('F', AllItems.PROPELLER)
                            .unlockedBy("has_ingredient", RegistrateRecipeProvider.has(SimItems.ENGINE_ASSEMBLY.get()))
                            .save(p))
                    .register();

    public static final BlockEntry<LargeRocketEngineBlock> LARGE_ROCKET_ENGINE =
            REGISTRATE.block("large_rocket_engine", LargeRocketEngineBlock::new)
                    .initialProperties(SharedProperties::netheriteMetal)
                    .lang("Large Rocket Engine")
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .blockstate((ctx, prov) ->
                            prov.directionalBlock(ctx.getEntry(), blockState -> {
                                boolean isPowered = blockState.getValue(BlockStateProperties.POWERED);
                                if (!isPowered) return prov.models().getExistingFile(prov.modLoc("block/" +  ctx.getName()));
                                return prov.models().withExistingParent("block/" + ctx.getName() + "_powered", prov.modLoc("block/" +  ctx.getName())).texture("0", prov.modLoc("block/" + ctx.getName() + "_powered"));
                            }))
                    .item(LargeRocketEngineBlockItem::new)
                    .transform(Builder::build)
                    .register();
    public static final BlockEntry<LargeRocketEngineStructuralBlock> LARGE_ROCKET_ENGINE_STRUCTURAL =
            REGISTRATE.block("large_rocket_engine_structure", LargeRocketEngineStructuralBlock::new)
                    .initialProperties(SharedProperties::netheriteMetal)
                    .clientExtension(() -> LargeRocketEngineStructuralBlock.RenderProperties::new)
                    .blockstate((c, p) -> p.getVariantBuilder(c.get())
                            .forAllStatesExcept(BlockStateGen.mapToAir(p), PropBlockStateProperties.SURROUNDING))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .lang("Large Rocket Engine")
                    .register();
    public static final BlockEntry<CreativeRocketEngineBlock> CREATIVE_ROCKET_ENGINE =
            REGISTRATE.block("creative_rocket_engine", CreativeRocketEngineBlock::new)
                    .initialProperties(SharedProperties::netheriteMetal)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .blockstate((ctx, prov) ->
                            prov.directionalBlock(ctx.getEntry(), blockState -> {
                                boolean isPowered = blockState.getValue(BlockStateProperties.POWERED);
                                if (!isPowered) return prov.models().withExistingParent("block/" + ctx.getName(), prov.modLoc("block/rocket_engine")).texture("0", prov.modLoc("block/" + ctx.getName()));
                                return prov.models().withExistingParent("block/" + ctx.getName() + "_powered", prov.modLoc("block/rocket_engine")).texture("0", prov.modLoc("block/" + ctx.getName() + "_powered"));
                            }))
                    .item()
                    .transform(Builder::build)
                    .register();
    public static void init() {
    }
}
