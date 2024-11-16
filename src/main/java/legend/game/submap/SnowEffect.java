package legend.game.submap;

import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.Random;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.opengl.MeshObj;
import legend.core.opengl.QuadBuilder;
import legend.game.types.Translucency;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.core.MathHelper.sin;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;

public class SnowEffect {
  private final Random rand = new Random();

  private int snowEffectTick;
  private int snowOffsetXTick;
  private final SnowParticleData18 particleData;
  private final SnowParticleInstance3c[] particles = new SnowParticleInstance3c[256];

  public MeshObj particle;
  public final MV transforms = new MV();

  private static class SnowParticleData18 {
    // /** Unused */
    // public short _02;
    public float angle_04;
    public float stepAngleMax_08;
    public int translationScaleX_0c;
    public int stepXMax_10;
    public int stepYDivisor_14;

    private SnowParticleData18(final float stepAngleMax, final int translationScaleX, final int stepXMax, final int stepYDivisor) {
      this.angle_04 = 0.0f;
      this.stepAngleMax_08 = stepAngleMax;
      this.translationScaleX_0c = translationScaleX;
      this.stepXMax_10 = stepXMax;
      this.stepYDivisor_14 = stepYDivisor;
    }
  }

  private static class SnowParticleInstance3c {
    // /** Whether particle should render, code was written so that it always rendered */
    // public short _00;

    public float angle_08;
    public float angleStep_0c;
    public float translationScaleX_10;
    public float size_14;
    public float x_16;
    public float y_18;

    public float stepX_1c;
    public float stepY_20;
    /** 16.16 */
    public float xAccumulator_24;
    // /** 16.16 - not needed now because y and yAccum are always in lockstep */
    // public float yAccumulator_28;

    public float brightness_34;

    // public SnowParticleInstance3c next_38;
  }

  public SnowEffect(final float stepAngleMax, final int translationScaleX, final int stepXMax, final int stepYDivisor) {
    this.particleData = new SnowParticleData18(stepAngleMax, translationScaleX, stepXMax, stepYDivisor);
  }

  public void initSnowEffect() {
    for(int i = 0; i < this.particles.length; i++) {
      final SnowParticleInstance3c inst = new SnowParticleInstance3c();

      inst.x_16 = this.rand.nextFloat(400.0f) - 200.0f + this.snowOffsetXTick;
      inst.y_18 = this.rand.nextFloat(256.0f) - 128.0f;

      final int stepXMax = this.particleData.stepXMax_10;
      if(stepXMax == 0) {
        inst.stepX_1c = 0.0f;
      } else {
        //LAB_800ee62c
        inst.stepX_1c = 32.0f / (stepXMax - this.rand.nextFloat(stepXMax) / 2.0f);
      }

      //LAB_800ee644
      inst.xAccumulator_24 = inst.x_16;

      float stepYDivisorModifier = 0.0f;
      if(this.snowEffectTick < 35) {
        inst.size_14 = 3.0f;
        //LAB_800ee66c
      } else if(this.snowEffectTick < 150) {
        inst.size_14 = 2.0f;
        stepYDivisorModifier = this.particleData.stepYDivisor_14 / 3.0f;
        //LAB_800ee6ac
      } else if(this.snowEffectTick < 256) {
        inst.size_14 = 1.0f;
        stepYDivisorModifier = this.particleData.stepYDivisor_14 * 2.0f / 3.0f;
      }

      //LAB_800ee6e8
      inst.brightness_34 = 2.0f;
      inst.stepY_20 = 32.0f / (this.particleData.stepYDivisor_14 + stepYDivisorModifier);
      inst.translationScaleX_10 = this.particleData.translationScaleX_0c;
      final float angle = this.rand.nextFloat(MathHelper.PI);
      this.particleData.angle_04 = angle;
      inst.angle_08 = angle;

      if(this.particleData.stepAngleMax_08 == 0) {
        inst.angleStep_0c = 0.0f;
      } else {
        //LAB_800ee750
        inst.angleStep_0c = this.rand.nextFloat(this.particleData.stepAngleMax_08);
      }

      this.particles[i] = inst;

      //LAB_800ee770
      this.snowEffectTick = this.snowEffectTick + 1 & 0xff;
      this.snowOffsetXTick = this.snowOffsetXTick + 1 & 0xf;
    }

    this.snowEffectTick = 0;
    this.snowOffsetXTick = 0;

    this.particle = new QuadBuilder("Snowflake")
      .bpp(Bpp.BITS_4)
      .clut(960, 464)
      .monochrome(1.0f)
      .translucency(Translucency.B_PLUS_F)
      .vramPos(960, 320)
      .size(1.0f, 1.0f)
      .uvSize(24, 24)
      .build();
  }

  public void render() {
    //LAB_800ee38c
    for(int i = 0; i < this.particles.length; i++) {
      final SnowParticleInstance3c inst = this.particles[i];

      //LAB_800ee3a0
      if(inst.y_18 + 128.0f <= 256.0f) {
        inst.xAccumulator_24 += inst.stepX_1c / (2.0f / vsyncMode_8007a3b8);
        inst.x_16 = inst.xAccumulator_24 + ((inst.translationScaleX_10 * sin(inst.angle_08) / (MathHelper.TWO_PI)) / (2.0f / vsyncMode_8007a3b8));

        if(inst.x_16 < -200.0f) {
          inst.x_16 = 200.0f;
          inst.xAccumulator_24 = 200.0f;
          inst.angle_08 = 0.0f;
          //LAB_800ee42c
        } else if(inst.x_16 > 200.0f) {
          inst.x_16 = -200.0f;
          inst.xAccumulator_24 = -200.0f;
          inst.angle_08 = 0.0f;
        }

        //LAB_800ee448
        inst.y_18 += inst.stepY_20 / (2.0f / vsyncMode_8007a3b8);

        this.transforms.scaling(inst.size_14);
        this.transforms.transfer.set(GPU.getOffsetX() + inst.x_16, GPU.getOffsetY() + inst.y_18, 160.0f);
        RENDERER.queueOrthoModel(this.particle, this.transforms, QueuedModelStandard.class)
          .monochrome(inst.brightness_34);

        inst.angle_08 = (inst.angle_08 + inst.angleStep_0c / (2.0f / vsyncMode_8007a3b8)) % MathHelper.TWO_PI;
      } else {
        //LAB_800ee52c
        this.wrapAroundSnowEffect(inst);
      }
      //LAB_800ee534
    }
    //LAB_800ee544
  }

  /** Reuse snow effect when it reaches the bottom of the screen */
  private void wrapAroundSnowEffect(final SnowParticleInstance3c inst) {
    inst.x_16 = this.rand.nextFloat(400.0f) - 200.0f + this.snowOffsetXTick;
    inst.y_18 = -128.0f;

    final int stepXMax = this.particleData.stepXMax_10;
    if(stepXMax == 0) {
      inst.stepX_1c = 0.0f;
    } else {
      //LAB_800ee84c
      inst.stepX_1c = 32.0f / (stepXMax - this.rand.nextFloat(stepXMax) / 2.0f);
    }

    //LAB_800ee864
    inst.xAccumulator_24 = inst.x_16;
    inst.brightness_34 = 432.0f / 255.0f;

    final int tick = this.snowEffectTick;
    float stepYDivisorModifier = 0;
    if(tick == 0 || tick == 2 || tick == 4) {
      //LAB_800ee890
      inst.size_14 = 1.0f;
      stepYDivisorModifier = this.particleData.stepYDivisor_14 * 2.0f / 3.0f;
      //LAB_800ee8c0
    } else if(tick == 1) {
      inst.size_14 = 2.0f;
      stepYDivisorModifier = this.particleData.stepYDivisor_14 / 3.0f;
      //LAB_800ee8f4
    } else if(tick == 3) {
      inst.size_14 = 3.0f;
    }

    //LAB_800ee900
    //LAB_800ee904
    inst.stepY_20 = 32.0f / (this.particleData.stepYDivisor_14 + stepYDivisorModifier);
    inst.translationScaleX_10 = this.particleData.translationScaleX_0c;
    final float angle = this.rand.nextFloat(MathHelper.PI) / 32.0f;
    this.particleData.angle_04 = angle;
    inst.angle_08 = angle;

    if(this.particleData.stepAngleMax_08 == 0) {
      inst.angleStep_0c = 0.0f;
    } else {
      //LAB_800ee968
      inst.angleStep_0c = this.rand.nextFloat(this.particleData.stepAngleMax_08);
    }

    //LAB_800ee988
    this.snowEffectTick++;
    if(this.snowEffectTick >= 6) {
      this.snowEffectTick = 0;
    }

    //LAB_800ee9b4
    this.snowOffsetXTick = this.snowOffsetXTick + 1 & 0xf;
  }

  public void deallocate() {
    if(this.particle != null) {
      this.particle.delete();
    }
  }
}
