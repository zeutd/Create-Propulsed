package com.zeutd.propulsed.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.zeutd.propulsed.Propulsed;
import com.zeutd.propulsed.content.BaseRocketEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.NeoForgeRenderTypes;

public class RocketEngineRenderer extends SmartBlockEntityRenderer<BaseRocketEngine> {

    private static final ResourceLocation TEXTURE = Propulsed.path("block/thruster_flame");

    public RocketEngineRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(BaseRocketEngine blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(blockEntity, partialTicks, ms, buffer, light, overlay);
        //if (!blockEntity.isActive()) return;
        VertexConsumer vc = buffer.getBuffer(NeoForgeRenderTypes.ITEM_UNLIT_TRANSLUCENT.get());
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(TEXTURE);
        ms.pushPose();
        ms.translate(-1, -1, -1);
        ms.scale(blockEntity.getSize(), blockEntity.getSize(), blockEntity.getSize());
        part(vc, ms, sprite, blockEntity.getBlockState().getValue(WrenchableDirectionalBlock.FACING).getOpposite(), overlay, blockEntity.getFlameOffset());
        ms.translate(0, 0, 0);
        ms.popPose();
    }

    private static void part(VertexConsumer vc, PoseStack poseStack, TextureAtlasSprite sprite, Direction dir, int packedOverlay, float offset){
        float u0 = sprite.getU0(), u1 = sprite.getU1(), v0 = sprite.getV0(), v1 = sprite.getV1();
        for (int i = 0; i < 4; i++) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(dir.getRotation());
            poseStack.translate(-0.5, 0.5, 0.5);
            poseStack.rotateAround(Axis.YP.rotationDegrees(90f * i), 0.5f, 0f, -0.5f);
            vc
                    .addVertex(poseStack.last().pose(),
                            0f, offset, 0f
                    )
                    .setColor(0xffffffff)
                    .setUv(u0, v1)
                    .setOverlay(packedOverlay)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setNormal(0, 0, 0)
                    ;
            vc
                    .addVertex(poseStack.last().pose(),
                            1f, offset, 0f
                    )
                    .setColor(0xffffffff)
                    .setUv(u1, v1)
                    .setOverlay(packedOverlay)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setNormal(0, 0, 0)
                    ;
            vc
                    .addVertex(poseStack.last().pose(),
                            1f, offset + 3, -0.15f
                    )
                    .setColor(0xffffffff)
                    .setUv(u1, v0)
                    .setOverlay(packedOverlay)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setNormal(0, 0, 0)
                    ;
            vc
                    .addVertex(poseStack.last().pose(),
                            0f, offset + 3, -0.15f
                    )
                    .setColor(0xffffffff)
                    .setUv(u0, v0)
                    .setOverlay(packedOverlay)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setNormal(0, 0, 0)
                    ;
            vc
                    .addVertex(poseStack.last().pose(),
                            0f, offset, 0f
                    )
                    .setColor(0xffffffff)
                    .setUv(u0, v1)
                    .setOverlay(packedOverlay)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setNormal(0, 0, 0)
                    ;
            vc
                    .addVertex(poseStack.last().pose(),
                            1f, offset, 0f
                    )
                    .setColor(0xffffffff)
                    .setUv(u1, v1)
                    .setOverlay(packedOverlay)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setNormal(0, 0, 0)
                    ;
            vc
                    .addVertex(poseStack.last().pose(),
                            1f, offset + 2.75f, -0.6f
                    )
                    .setColor(0xffffffff)
                    .setUv(u1, v0)
                    .setOverlay(packedOverlay)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setNormal(0, 0, 0)
                    ;
            vc
                    .addVertex(poseStack.last().pose(),
                            0f, offset + 2.75f, -0.6f
                    )
                    .setColor(0xffffffff)
                    .setUv(u0, v0)
                    .setOverlay(packedOverlay)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setNormal(0, 0, 0)
                    ;
            poseStack.popPose();
        }
    }
}
