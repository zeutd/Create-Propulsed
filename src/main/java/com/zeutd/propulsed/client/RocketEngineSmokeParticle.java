package com.zeutd.propulsed.client;

import dev.eriksonn.aeronautics.content.particle.PropellerAirParticle;
import dev.eriksonn.aeronautics.content.particle.PropellerAirParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static com.zeutd.propulsed.Propulsed.LOGGER;

@OnlyIn(Dist.CLIENT)
public class RocketEngineSmokeParticle extends TextureSheetParticle {
    protected RocketEngineSmokeParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
        super(level, x, y, z);
        this.scale(3.0F);
        this.setSize(0.25F, 0.25F);
        this.lifetime = this.random.nextInt(50) + 20;

        this.gravity = 0f;
        this.xd = dx;
        this.yd = dy;
        this.zd = dz;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ < this.lifetime && !(this.alpha <= 0.0F)) {
            this.move(this.xd, this.yd, this.zd);
            if (this.age >= this.lifetime - 60 && this.alpha > 0.01F) {
                this.alpha -= 0.015F;
            }
        } else {
            this.remove();
        }

    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<RocketEngineSmokeParticleData> {
        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(final RocketEngineSmokeParticleData data, final ClientLevel worldIn, final double x, final double y, final double z,
                                       final double xSpeed, final double ySpeed, final double zSpeed) {
            RocketEngineSmokeParticle rocketEngineSmokeParticle =  new RocketEngineSmokeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            rocketEngineSmokeParticle.setAlpha(0.9F);
            rocketEngineSmokeParticle.pickSprite(this.sprites);
            return rocketEngineSmokeParticle;
        }
    }
}
