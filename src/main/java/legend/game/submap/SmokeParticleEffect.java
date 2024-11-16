package legend.game.submap;

import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.opengl.MeshObj;
import legend.core.opengl.QuadBuilder;
import legend.game.scripting.Param;
import legend.game.scripting.RunningScript;
import legend.game.types.Translucency;
import org.joml.Math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_8003.GsGetLs;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.PopMatrix;
import static legend.game.Scus94491BpeSegment_8003.PushMatrix;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;

public class SmokeParticleEffect {
  public enum SmokeCloudState {
    UNINITIALIZED,
    TICK,
    DONT_TICK,
  }

  /**
   * <ol>
   *   <li>Uninitialized</li>
   *   <li>Tick</li>
   *   <li>Don't tick</li>
   * </ol>
   */
  public SmokeCloudState smokeCloudState = SmokeCloudState.UNINITIALIZED;
  public boolean effectShouldRender;

  private final List<SmokeEffectData34> smokeEffectData = new ArrayList<>();

  private SmokeParticleInstance3c[] particles;
  private int firstEmptyIndex;

  private MeshObj particle;
  private final MV transforms = new MV();

  public void allocateSmokePlumeEffect(final RunningScript<?> script, final float screenOffsetX, final float screenOffsetY, final int tpage, final int clut) {
    if(!this.effectShouldRender) {
      final MV transforms = new MV();
      final GsCOORDINATE2 coord2 = new GsCOORDINATE2();

      //LAB_800f10ac
      GsInitCoordinate2(null, coord2);

      final Param ints = script.params_20[0];
      int i = 0;

      //LAB_800f10dc
      while(ints.array(i).get() != -1) {
        final SmokeEffectData34 inst = new SmokeEffectData34();

        coord2.coord.transfer.x = ints.array(i++).get();
        coord2.coord.transfer.y = ints.array(i++).get();
        coord2.coord.transfer.z = ints.array(i++).get();
        GsGetLs(coord2, transforms);

        PushMatrix();
        GTE.setTransforms(transforms);
        GTE.perspectiveTransform(0, 0, 0);
        final float sx = GTE.getScreenX(2);
        final float sy = GTE.getScreenY(2);

        inst.sz3_2c = GTE.getScreenZ(3) / 4.0f;
        PopMatrix();

        inst.tick_02 = 0;
        inst.countTicksParticleInstantiationInterval_04 = 17;
        inst.countTicksParticleLifecycle_06 = 100;
        inst.countTicksInstantiationDelay_08 = 0;
        inst.maxTicks_0a = 0;
        inst.stepOffsetY_0c = ints.array(i++).get();
        inst.size_10 = ints.array(i++).get();
        inst.stepSize_14 = ints.array(i++).get();
        inst.offsetRandomX_18 = ints.array(i++).get();
        inst.offsetBaseX_1c = sx;
        inst.offsetBaseY_20 = sy;
        inst.screenOffsetX_24 = screenOffsetX;
        inst.screenOffsetY_28 = screenOffsetY;

        this.smokeEffectData.add(inst);
      }

      //LAB_800f123c
      this.effectShouldRender = true;
      this.particles = new SmokeParticleInstance3c[24];
      Arrays.setAll(this.particles, val -> new SmokeParticleInstance3c());

      this.particle = new QuadBuilder("SmokePlumeParticle")
        .bpp(Bpp.of(tpage >>> 7 & 0b11))
        .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
        .clut((clut & 0b111111) * 16, clut >>> 6)
        .translucency(Translucency.of(tpage >>> 5 & 0b11))
        .monochrome(1.0f)
        .uv(64, 32)
        .uvSize(32, 32)
        .posSize(1.0f, 1.0f)
        .build();
    }
  }

  public void allocateSmokeCloudEffect(final RunningScript<?> script, final int tpage, final int clut) {
    this.smokeCloudState = SmokeCloudState.values()[script.params_20[0].get()];

    if(this.smokeCloudState != SmokeCloudState.UNINITIALIZED) {
      final SmokeEffectData34 inst = new SmokeEffectData34();
      inst.tick_02 = 0;
      inst.countTicksParticleInstantiationInterval_04 = script.params_20[1].get();

      if(script.params_20[2].get() == 0) {
        script.params_20[2].set(1);
      }

      //LAB_800f154c
      inst.countTicksParticleLifecycle_06 = script.params_20[2].get();
      inst.offsetBaseX_1c = script.params_20[3].get();
      inst.offsetBaseY_20 = script.params_20[4].get();
      inst.offsetRandomX_18 = script.params_20[5].get();
      inst.stepOffsetY_0c = (float)script.params_20[6].get() / (float)script.params_20[2].get();
      inst.size_10 = script.params_20[7].get();
      inst.stepSize_14 = (float)script.params_20[8].get() / (float)script.params_20[2].get();

      this.smokeEffectData.add(inst);

      this.particles = new SmokeParticleInstance3c[inst.countTicksParticleLifecycle_06 / inst.countTicksParticleInstantiationInterval_04 + 1];
      Arrays.setAll(this.particles, val -> new SmokeParticleInstance3c());

      this.particle = new QuadBuilder("SmokeCloudParticle")
        .bpp(Bpp.of(tpage >>> 7 & 0b11))
        .clut((clut & 0b111111) * 16, clut >>> 6)
        .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
        .translucency(Translucency.of(tpage >>> 5 & 0b11))
        .monochrome(1.0f)
        .uv(64, 64)
        .uvSize(32, 32)
        .posSize(1.0f, 1.0f)
        .build();
    }
  }

  public void allocateUnusedSmokeEffect(final RunningScript<?> script, final float screenOffsetX, final float screenOffsetY, final int tpage, final int clut) {
    this.effectShouldRender = true;

    final SmokeEffectData34 inst = new SmokeEffectData34();

    final GsCOORDINATE2 coord2 = new GsCOORDINATE2();
    GsInitCoordinate2(null, coord2);

    coord2.coord.transfer.set(script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get());
    final MV transforms = new MV();
    GsGetLs(coord2, transforms);

    PushMatrix();
    GTE.setTransforms(transforms);
    GTE.perspectiveTransform(0, 0, 0);

    final float sx = GTE.getScreenX(2);
    final float sy = GTE.getScreenY(2);
    final float sz = GTE.getScreenZ(3) / 4.0f;
    PopMatrix();

    // Add lifecycle ticks to delay ticks if the latter is less than the former.
    if(script.params_20[2].get() < script.params_20[1].get()) {
      script.params_20[2].add(script.params_20[1].get());
    }

    //LAB_800f13f0
    inst.tick_02 = 0;
    inst.countTicksParticleInstantiationInterval_04 = script.params_20[0].get();
    inst.countTicksParticleLifecycle_06 = script.params_20[1].get();
    inst.countTicksInstantiationDelay_08 = script.params_20[2].get();
    inst.maxTicks_0a = 0;
    inst.stepOffsetY_0c = (float)((script.params_20[6].get() << 16) / script.params_20[1].get());
    inst.size_10 = script.params_20[8].get();
    inst.stepSize_14 = (float)((script.params_20[9].get() << 16) / script.params_20[1].get());
    inst.offsetRandomX_18 = script.params_20[7].get();
    inst.offsetBaseX_1c = sx;
    inst.offsetBaseY_20 = sy;
    inst.screenOffsetX_24 = screenOffsetX;
    inst.screenOffsetY_28 = screenOffsetY;
    inst.sz3_2c = sz;

    this.smokeEffectData.add(inst);

    this.particles = new SmokeParticleInstance3c[(inst.countTicksParticleLifecycle_06 / inst.countTicksParticleInstantiationInterval_04 + 1) * 4];
    Arrays.setAll(this.particles, val -> new SmokeParticleInstance3c());

    this.particle = new QuadBuilder("UnusedSmokeParticle")
      .bpp(Bpp.of(tpage >>> 7 & 0b11))
      .clut((clut & 0b111111) * 16, clut >>> 6)
      .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
      .translucency(Translucency.of(tpage >>> 5 & 0b11))
      .monochrome(1.0f)
      .uv(64, 64)
      .uvSize(32, 32)
      .posSize(1.0f, 1.0f)
      .build();
  }

  public void reinitializeSmokePlumeForIntermittentBursts(final RunningScript<?> script) {
    final Param ints = script.params_20[0];

    //LAB_800f20a8
    //LAB_800f20c4
    //LAB_800f20e4
    int index = 0;
    for(int i = this.smokeEffectData.size() - 1; i >= 0; i--) {
      final SmokeEffectData34 inst = this.smokeEffectData.get(i);
      if(ints.array(index).get() != -2) {
        if(ints.array(index).get() == -1) {
          index++;
          inst.countTicksParticleInstantiationInterval_04 = 17;
          inst.countTicksParticleLifecycle_06 = 100;
          inst.countTicksInstantiationDelay_08 = 0;
          inst.maxTicks_0a = 0;
        } else {
          //LAB_800f2108
          inst.countTicksInstantiationDelay_08 = ints.array(index++).get();
          inst.maxTicks_0a = ints.array(index++).get();
          inst.countTicksParticleInstantiationInterval_04 = ints.array(index++).get();
          inst.countTicksParticleLifecycle_06 = ints.array(index++).get();
        }
      }

      //LAB_800f2138
      inst.unused_00 = ints.array(index++).get();
    }
  }

  public void tickAndRenderSmokePlumeEffect(final float screenOffsetX, final float screenOffsetY) {
    if(this.effectShouldRender) {
      this.tickSmokePlume();
      this.renderSmokePlume(screenOffsetX, screenOffsetY);
    }
  }

  public void tickAndRenderSmokeCloudEffect(final float screenOffsetX, final float screenOffsetY) {
    if(this.effectShouldRender || this.smokeCloudState != SmokeParticleEffect.SmokeCloudState.UNINITIALIZED) {
      this.tickSmokeCloud(screenOffsetX, screenOffsetY);
      this.renderSmokeCloud(screenOffsetX, screenOffsetY);
    }
  }

  private void tickSmokePlume() {
    //LAB_800f3ce8
    for(int i = 0; i < this.smokeEffectData.size(); i++) {
      final SmokeEffectData34 dataInst = this.smokeEffectData.get(i);
      if(dataInst.countTicksInstantiationDelay_08 == 0) {
        if(dataInst.tick_02 % (dataInst.countTicksParticleInstantiationInterval_04 * (3 - vsyncMode_8007a3b8)) == 0) {
          final SmokeParticleInstance3c effectInst = this.particles[this.firstEmptyIndex];

          effectInst.tick_02 = 0;
          effectInst.countTicksParticleLifecycle_06 = dataInst.countTicksParticleLifecycle_06;
          effectInst.initialScreenOffsetX_0c = dataInst.screenOffsetX_24;
          effectInst.initialScreenOffsetY_0e = dataInst.screenOffsetY_28;
          effectInst.offsetX_10 = dataInst.offsetBaseX_1c - (simpleRand() * dataInst.offsetRandomX_18 >> 16);
          effectInst.offsetY_14 = dataInst.offsetBaseY_20;
          effectInst.stepOffsetY_1c = 8.0f / dataInst.stepOffsetY_0c;
          effectInst.stepSize_20 = dataInst.stepSize_14 / dataInst.countTicksParticleLifecycle_06;
          effectInst.size_28 = dataInst.size_10;
          effectInst.stepBrightness_2c = 0.5f / effectInst.countTicksParticleLifecycle_06;
          effectInst.brightness_30 = 0.5f;
          effectInst.z_34 = dataInst.sz3_2c;
          this.firstEmptyIndex++;
          this.firstEmptyIndex %= this.particles.length;
        }

        //LAB_800f3df4
        dataInst.tick_02++;
      } else {
        //LAB_800f3e08
        if(dataInst.tick_02 >= dataInst.countTicksInstantiationDelay_08 * (2.0f / vsyncMode_8007a3b8)) {
          if(dataInst.tick_02 % (dataInst.countTicksParticleInstantiationInterval_04 * (3 - vsyncMode_8007a3b8)) == 0) {
            final SmokeParticleInstance3c effectInst = this.particles[this.firstEmptyIndex];

            effectInst.tick_02 = 0;
            effectInst.countTicksParticleLifecycle_06 = dataInst.countTicksParticleLifecycle_06;
            effectInst.initialScreenOffsetX_0c = dataInst.screenOffsetX_24;
            effectInst.initialScreenOffsetY_0e = dataInst.screenOffsetY_28;
            effectInst.offsetX_10 = dataInst.offsetBaseX_1c - (simpleRand() * dataInst.offsetRandomX_18 >> 16);
            effectInst.offsetY_14 = dataInst.offsetBaseY_20;
            effectInst.stepOffsetY_1c = 8.0f / dataInst.stepOffsetY_0c;
            effectInst.stepSize_20 = dataInst.stepSize_14 / dataInst.countTicksParticleLifecycle_06;
            effectInst.size_28 = dataInst.size_10;
            effectInst.stepBrightness_2c = 0.5f / effectInst.countTicksParticleLifecycle_06;
            effectInst.brightness_30 = 0.5f;
            effectInst.z_34 = dataInst.sz3_2c;
            this.firstEmptyIndex++;
            this.firstEmptyIndex %= this.particles.length;
          }
        }

        //LAB_800f3f14
        dataInst.tick_02++;

        if(dataInst.tick_02 >= dataInst.maxTicks_0a * (2.0f / vsyncMode_8007a3b8)) {
          dataInst.tick_02 = 0;
        }
      }
      //LAB_800f3f3c
    }
    //LAB_800f3f4c
  }

  private void renderSmokePlume(final float screenOffsetX, final float screenOffsetY) {
    //LAB_800f3fb0
    for(int i = this.particles.length - 1; i >= 0; i--) {
      final SmokeParticleInstance3c inst = this.particles[i];
      if(inst.tick_02 < inst.countTicksParticleLifecycle_06 * (2.0f / vsyncMode_8007a3b8)) {
        //LAB_800f3fe8
        inst.size_28 += inst.stepSize_20 / (2.0f / vsyncMode_8007a3b8);
        inst.offsetY_14 -= inst.stepOffsetY_1c / (2.0f / vsyncMode_8007a3b8);
        inst.brightness_30 -= inst.stepBrightness_2c / (2.0f / vsyncMode_8007a3b8);
        final float halfSize = inst.size_28 / 2;
        final float x = screenOffsetX - inst.initialScreenOffsetX_0c + inst.offsetX_10 - halfSize;
        final float y = screenOffsetY - inst.initialScreenOffsetY_0e + inst.offsetY_14 - halfSize;
        final float brightness = inst.brightness_30 > 0.5f ? 0.0f : inst.brightness_30;

        //LAB_800f4084
        this.transforms.scaling(inst.size_28, inst.size_28, 1.0f);
        this.transforms.transfer.set(GPU.getOffsetX() + x, GPU.getOffsetY() + y, inst.z_34 * 4);
        RENDERER.queueOrthoModel(this.particle, this.transforms, QueuedModelStandard.class)
          .monochrome(brightness);

        inst.tick_02++;
      }
      //LAB_800f41b0
    }
    //LAB_800f41bc
  }

  private void tickSmokeCloud(final float screenOffsetX, final float screenOffsetY) {
    if(this.effectShouldRender) {
      //LAB_800f0100
      for(int i = 0; i < this.smokeEffectData.size(); i++) {
        final SmokeEffectData34 dataInst = this.smokeEffectData.get(i);
        if(dataInst.countTicksInstantiationDelay_08 * (2.0f / vsyncMode_8007a3b8) >= dataInst.tick_02) {
          if(dataInst.tick_02 % (dataInst.countTicksParticleInstantiationInterval_04 * (3 - vsyncMode_8007a3b8)) == 0) {
            //LAB_800f0148
            for(int j = 0; j < 4; j++) {
              final SmokeParticleInstance3c effectInst = this.particles[this.firstEmptyIndex];
              effectInst.tick_02 = 0;
              effectInst.countTicksParticleLifecycle_06 = dataInst.countTicksParticleLifecycle_06;
              effectInst.initialScreenOffsetX_0c = screenOffsetX;
              effectInst.initialScreenOffsetY_0e = screenOffsetY;
              effectInst.offsetX_10 = dataInst.offsetBaseX_1c + (simpleRand() * dataInst.offsetRandomX_18 >> 16);
              effectInst.offsetY_14 = dataInst.offsetBaseY_20;
              effectInst.stepOffsetY_1c = -dataInst.stepOffsetY_0c;
              effectInst.stepSize_20 = dataInst.stepSize_14;
              effectInst.size_28 = dataInst.size_10;
              effectInst.stepBrightness_2c = 0.5f / effectInst.countTicksParticleLifecycle_06;
              effectInst.brightness_30 = 0.5f;
              this.firstEmptyIndex++;
              this.firstEmptyIndex %= this.particles.length;
            }
          }

          //LAB_800f01ec
          dataInst.tick_02++;
        } else {
          //LAB_800f0208
          this.smokeEffectData.remove(i);
          i--;
          if(this.smokeEffectData.isEmpty()) {
            this.effectShouldRender = false;
          }
        }
      }
    }

    //LAB_800f023c
    if(this.smokeCloudState == SmokeCloudState.TICK) {
      final SmokeEffectData34 dataInst = this.smokeEffectData.get(0);

      if(dataInst.tick_02 % (dataInst.countTicksParticleInstantiationInterval_04 * (3 - vsyncMode_8007a3b8)) == 0) {
        //LAB_800f0284
        final SmokeParticleInstance3c effectInst = this.particles[this.firstEmptyIndex];
        effectInst.tick_02 = 0;
        effectInst.countTicksParticleLifecycle_06 = dataInst.countTicksParticleLifecycle_06;
        effectInst.initialScreenOffsetX_0c = screenOffsetX;
        effectInst.initialScreenOffsetY_0e = screenOffsetY;
        effectInst.offsetX_10 = dataInst.offsetBaseX_1c + (simpleRand() * dataInst.offsetRandomX_18 >> 16);
        effectInst.offsetY_14 = dataInst.offsetBaseY_20;
        effectInst.stepOffsetY_1c = -dataInst.stepOffsetY_0c;
        effectInst.stepSize_20 = dataInst.stepSize_14;
        effectInst.size_28 = dataInst.size_10;
        effectInst.stepBrightness_2c = 0.5f / effectInst.countTicksParticleLifecycle_06;
        effectInst.brightness_30 = 0.5f;
        this.firstEmptyIndex++;
        this.firstEmptyIndex %= this.particles.length;
      }

      //LAB_800f032c
      dataInst.tick_02++;
    }
    //LAB_800f0344
  }

  private void renderSmokeCloud(final float screenOffsetX, final float screenOffsetY) {
    //LAB_800efecc
    for(int i = this.particles.length - 1; i >= 0; i--) {
      final SmokeParticleInstance3c inst = this.particles[i];
      if(inst.tick_02 <= inst.countTicksParticleLifecycle_06 * (2.0f / vsyncMode_8007a3b8) && inst.countTicksParticleLifecycle_06 != 0) {
        //LAB_800eff04
        inst.offsetY_14 += inst.stepOffsetY_1c / (2.0f / vsyncMode_8007a3b8);
        inst.size_28 += inst.stepSize_20 / (2.0f / vsyncMode_8007a3b8);
        inst.brightness_30 = Math.max(inst.brightness_30 - inst.stepBrightness_2c, 0);

        final float x = screenOffsetX - inst.initialScreenOffsetX_0c + inst.offsetX_10 % 65536;
        final float y = screenOffsetY - inst.initialScreenOffsetY_0e + inst.offsetY_14 - inst.size_28;

        //LAB_800eff7c
        this.transforms.scaling(inst.size_28, inst.size_28, 1.0f);
        this.transforms.transfer.set(GPU.getOffsetX() + x, GPU.getOffsetY() + y, 160.0f);
        RENDERER.queueOrthoModel(this.particle, this.transforms, QueuedModelStandard.class)
          .monochrome(inst.brightness_30);

        inst.tick_02++;
      }
    }
  }

  public void deallocate() {
    this.effectShouldRender = false;
    this.smokeCloudState = SmokeCloudState.UNINITIALIZED;
    this.smokeEffectData.clear();
    this.particles = null;
    this.firstEmptyIndex = 0;

    if(this.particle != null) {
      this.particle.delete();
      this.particle = null;
    }
  }
}
