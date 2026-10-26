package legend.game.wmap;

import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.renderer.QuadBuilder;
import legend.core.renderer.QueuedModelStandard;
import legend.core.renderer.Translucency;
import legend.game.wmap.WMapModelAndAnimData258.FadeAnimationType;
import org.joml.Vector3f;

import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.core.MathHelper.flEq;
import static legend.game.Graphics.GsGetLs;
import static legend.game.Graphics.GsInitCoordinate2;
import static legend.game.Graphics.orderingTableSize_1f8003c8;
import static legend.game.Graphics.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment.rand;

/** Retail cloud, snow, and smoke resources/rendering; the engine retains effect lifecycle ordering. */
final class WMapAtmosphere {
  private WMapAtmosphere() { }

  static void allocateClouds(final WMapModelAndAnimData258 modelAndAnimData) {
    modelAndAnimData.atmosphericEffectSprites = WMapAtmosphericEffectInstance60.buildCloudSprites();

    modelAndAnimData.atmosphericEffectInstances_24 = new WMapAtmosphericEffectInstance60[24];

    //LAB_800ebbb4
    final Vector3f translation = new Vector3f();
    for(int i = 0; i < 12; i++) {
      final WMapAtmosphericEffectInstance60 cloud = new WMapAtmosphericEffectInstance60();
      modelAndAnimData.atmosphericEffectInstances_24[i] = cloud;

      //LAB_800ebbd0
      GsInitCoordinate2(null, cloud.coord2_00);

      if((i & 0x1) == 0) {
        translation.set(
          700 - rand() % 1400,
          -70 - rand() %   40,
          700 - rand() % 1400
        );

        cloud.coord2_00.coord.transfer.set(translation);
      } else {
        //LAB_800ebd18
        cloud.coord2_00.coord.transfer.set(translation).sub(
          rand() % 200 - 100,
          rand() %  80 -  40,
          rand() %  50 -  25
        );
      }

      //LAB_800ebe24
      cloud.snowTick_50 = 0;
      cloud.translation_58.set((288 - rand() % 64) / 2.0f, (80 - rand() % 32) / 2.0f, 0.0f);
      cloud.brightness_5c = 0.0f;
    }

    //LAB_800ebf2c
    //LAB_800ebf30
    for(int i = 0; i < 12; i++) {
      final WMapAtmosphericEffectInstance60 cloud = new WMapAtmosphericEffectInstance60();
      modelAndAnimData.atmosphericEffectInstances_24[i + 12] = cloud;
      cloud.set(modelAndAnimData.atmosphericEffectInstances_24[i]);
      cloud.coord2_00.coord.transfer.y = 0.0f;
    }
  }

  static void renderClouds(final WMapModelAndAnimData258 modelAndAnimData, final WMapCameraAndLights19c0 cameraAndLights) {
    final WMapAtmosphericEffectInstance60 cloud0 = modelAndAnimData.atmosphericEffectInstances_24[0];
    cloud0.coord2_00.flg = 0;

    //LAB_800ec028
    for(int i = 0; i < 24; i++) {
      final WMapAtmosphericEffectInstance60 cloud = modelAndAnimData.atmosphericEffectInstances_24[i];

      //LAB_800ec044
      cloud.translation_58.z += 1.0f / (3.0f / vsyncMode_8007a3b8);
      if((int)cloud.translation_58.z >> i % 3 + 4 != 0) {
        cloud.coord2_00.coord.transfer.x += 1.0f / (3.0f / vsyncMode_8007a3b8);
        cloud.translation_58.z = 0.0f;
      }

      //LAB_800ec288
      if(cloud.coord2_00.coord.transfer.x > 700) {
        cloud.coord2_00.coord.transfer.x = -700;
      }

      //LAB_800ec2b0
      if(!cameraAndLights.zoomStateIsLocal_c4) {
        cloud.brightness_5c -= 0.125f / (3.0f / vsyncMode_8007a3b8);

        if(cloud.brightness_5c < 0.0f) {
          cloud.brightness_5c = 0.0f;
        }

        //LAB_800ec30c
      } else {
        //LAB_800ec314
        if(cloud.brightness_5c < 0.375f) {
          cloud.brightness_5c += 0.0625f / (3.0f / vsyncMode_8007a3b8);
        }

        //LAB_800ec34c
        if(modelAndAnimData.fadeAnimationType_05 == FadeAnimationType.FADE_OUT_2) {
          cloud.brightness_5c -= 0.125f / (3.0f / vsyncMode_8007a3b8);

          if(cloud.brightness_5c < 0.0f) {
            cloud.brightness_5c = 0.0f;
          }
        }
      }

      //LAB_800ec3a8
      if(!flEq(cloud.brightness_5c, 0.0f)) {
        //LAB_800ec3c8
        GsGetLs(cloud.coord2_00, cloud.transforms);
        cloud.transforms.identity(); // NOTE: does not clear translation
        GTE.setTransforms(cloud.transforms);
        GTE.perspectiveTransform(-cloud.translation_58.x, -cloud.translation_58.y, 0.0f);
        final float sx0 = GTE.getScreenX(2);
        final float sy0 = GTE.getScreenY(2);
        float z = GTE.getScreenZ(3) / 4.0f;

        if(z >= 5 && z < orderingTableSize_1f8003c8 - 3) {
          //LAB_800ec534
          GTE.perspectiveTransform(cloud.translation_58.x, -cloud.translation_58.y, 0.0f);
          final float sx1 = GTE.getScreenX(2);
          final float sy1 = GTE.getScreenY(2);
          z = GTE.getScreenZ(3) / 4.0f;

          if(z >= 5 && z < orderingTableSize_1f8003c8 - 3 && sx1 - sx0 <= 0x400) {
            //LAB_800ec5ec
            GTE.perspectiveTransform(-cloud.translation_58.x, cloud.translation_58.y, 0.0f);
            final float sx2 = GTE.getScreenX(2);
            final float sy2 = GTE.getScreenY(2);
            z = GTE.getScreenZ(3) / 4.0f;

            if(z >= 5 && z < orderingTableSize_1f8003c8 - 3 && sy2 - sy0 <= 0x200) {
              //LAB_800ec670
              //LAB_800ec6a4
              if(sy2 > 0) {
                cloud.brightness_5c -= 0.125f / (3.0f / vsyncMode_8007a3b8);

                if(cloud.brightness_5c < 0.0f) {
                  cloud.brightness_5c = 0.0f;
                }
                //LAB_800ec6fc
              } else {
                //LAB_800ec704
                if(cloud.brightness_5c < 0.375f) {
                  cloud.brightness_5c += 0.0625f / (3.0f / vsyncMode_8007a3b8);
                }

                //LAB_800ec73c
                if(modelAndAnimData.fadeAnimationType_05 == FadeAnimationType.FADE_OUT_2) {
                  cloud.brightness_5c -= 0.125f / (3.0f / vsyncMode_8007a3b8);

                  if(cloud.brightness_5c < 0.0f) {
                    cloud.brightness_5c = 0.0f;
                  }
                }
              }

              //LAB_800ec798
              if(!flEq(cloud.brightness_5c, 0.0f)) {
                //LAB_800ec7b8
                GTE.perspectiveTransform(cloud.translation_58.x, cloud.translation_58.y, 0.0f);
                final float sx3 = GTE.getScreenX(2);
                final float sy3 = GTE.getScreenY(2);
                z = GTE.getScreenZ(3) / 4.0f;

                if(z >= 5 && z < orderingTableSize_1f8003c8 - 3 && sx3 - sx2 <= 0x400 && sy3 - sy1 <= 0x200) {
                  //LAB_800ec83c
                  //LAB_800ec870
                  //LAB_800ec8a4
                  cloud.queueZ = i < 12 ? 556.0f : (orderingTableSize_1f8003c8 - 4.0f) * 4.0f;
                  cloud.transforms.scaling(sx1 - sx0, sy2 - sy0, 1.0f);
                  cloud.transforms.transfer.set(GPU.getOffsetX() + sx0, GPU.getOffsetY() + sy0, cloud.queueZ);
                  RENDERER.queueOrthoModel(modelAndAnimData.atmosphericEffectSprites[i % 3], cloud.transforms, QueuedModelStandard.class)
                    .monochrome(i < 12 ? cloud.brightness_5c : cloud.brightness_5c / 3.0f);
                }
              }
            }
          }
        }
      }
    }
    //LAB_800eca1c
  }

  static void allocateSnow(final WMapModelAndAnimData258 modelAndAnimData) {
    modelAndAnimData.atmosphericEffectSprites = WMapAtmosphericEffectInstance60.buildSnowSprites();

    modelAndAnimData.atmosphericEffectInstances_24 = new WMapAtmosphericEffectInstance60[64];

    //LAB_800eca94
    for(int i = 0; i < 64; i++) {
      final WMapAtmosphericEffectInstance60 snowflake = new WMapAtmosphericEffectInstance60();
      modelAndAnimData.atmosphericEffectInstances_24[i] = snowflake;

      //LAB_800ecab0
      GsInitCoordinate2(null, snowflake.coord2_00);
      snowflake.coord2_00.coord.transfer.x = 500 - rand() % 1000;
      snowflake.coord2_00.coord.transfer.y =     - rand() %  200;
      snowflake.coord2_00.coord.transfer.z = 500 - rand() % 1000;
      snowflake.snowTick_50 = rand() % 12;
      snowflake.translation_58.set(rand() % 2 - 1, rand() % 2 + 1, rand() % 2 - 1);
      snowflake.brightness_5c = 0.0f;
    }
    //LAB_800eccfc
  }

  static void renderSnow(final WMapModelAndAnimData258 modelAndAnimData, final WMapCameraAndLights19c0 cameraAndLights) {
    //LAB_800ecdb4
    for(int i = 0; i < 64; i++) {
      final WMapAtmosphericEffectInstance60 snowflake = modelAndAnimData.atmosphericEffectInstances_24[i];

      //LAB_800ecdd0
      if(!cameraAndLights.zoomStateIsLocal_c4) {
        snowflake.brightness_5c -= 0.125f / (3.0f / vsyncMode_8007a3b8);

        if(snowflake.brightness_5c < 0.0f) {
          snowflake.brightness_5c = 0.0f;
        }

        //LAB_800ed0c8
      } else {
        //LAB_800ed0d0
        if(snowflake.brightness_5c < 0.375f) {
          snowflake.brightness_5c += 0.0625f / (3.0f / vsyncMode_8007a3b8);
        }

        //LAB_800ed108
        if(modelAndAnimData.fadeAnimationType_05 == FadeAnimationType.FADE_OUT_2) {
          snowflake.brightness_5c -= 0.125f / (3.0f / vsyncMode_8007a3b8);

          if(snowflake.brightness_5c < 0.0f) {
            snowflake.brightness_5c = 0.0f;
          }
        }
      }

      //LAB_800ed164
      if(!flEq(snowflake.brightness_5c, 0.0f)) {
        //LAB_800ed184
        snowflake.coord2_00.coord.transfer.x += snowflake.translation_58.x / (3.0f / vsyncMode_8007a3b8);
        snowflake.coord2_00.coord.transfer.y += snowflake.translation_58.y / (3.0f / vsyncMode_8007a3b8);
        snowflake.coord2_00.coord.transfer.z += snowflake.translation_58.z / (3.0f / vsyncMode_8007a3b8);

        if(snowflake.coord2_00.coord.transfer.y > 0.0f) {
          snowflake.coord2_00.coord.transfer.x =  500 - rand() % 1000;
          snowflake.coord2_00.coord.transfer.y = -200;
          snowflake.coord2_00.coord.transfer.z =  500 - rand() % 1000;
        }

        //LAB_800ed2bc
        snowflake.coord2_00.flg = 0;
        GsGetLs(snowflake.coord2_00, snowflake.transforms);
        snowflake.transforms.identity(); // NOTE: does not clear translation
        GTE.setTransforms(snowflake.transforms);
        GTE.perspectiveTransform(-2, -2, 0);

        final float sx0 = GTE.getScreenX(2);
        final float sy0 = GTE.getScreenY(2);
        float z = GTE.getScreenZ(3) / 4.0f;

        if(z >= 5 && z < orderingTableSize_1f8003c8 - 3) {
          //LAB_800ed37c
          GTE.perspectiveTransform(2, -2, 0);

          final float sx1 = GTE.getScreenX(2);
          final float sy1 = GTE.getScreenY(2);
          z = GTE.getScreenZ(3) / 4.0f;

          if(z >= 5 && z < orderingTableSize_1f8003c8 - 3 && sx1 - sx0 <= 0x400) {
            //LAB_800ed400
            //LAB_800ed434
            GTE.perspectiveTransform(-2, 2, 0);

            final float sx2 = GTE.getScreenX(2);
            final float sy2 = GTE.getScreenY(2);
            z = GTE.getScreenZ(3) / 4.0f;

            if(z >= 5 && z < orderingTableSize_1f8003c8 - 3 && sy2 - sy0 <= 0x200) {
              //LAB_800ed4b8
              //LAB_800ed4ec
              GTE.perspectiveTransform(2, 2, 0);

              final float sx3 = GTE.getScreenX(2);
              final float sy3 = GTE.getScreenY(2);
              z = GTE.getScreenZ(3) / 4.0f;

              if(z >= 5 && z < orderingTableSize_1f8003c8 - 3 && sx3 - sx2 <= 0x400 && sy3 - sy1 <= 0x200) {
                //LAB_800ed570
                //LAB_800ed5a4
                //LAB_800ed5d8
                snowflake.snowTick_50 = (snowflake.snowTick_50 + 1.0f / (3.0f / vsyncMode_8007a3b8)) % 12;
                final int index = (int)(snowflake.snowTick_50 / 2.0f);
                snowflake.transforms.scaling(sx1 - sx0, sy2 - sy0, 1.0f);
                snowflake.transforms.transfer.set(GPU.getOffsetX() + sx0, GPU.getOffsetY() + sy0, 556.0f);
                RENDERER.queueOrthoModel(modelAndAnimData.atmosphericEffectSprites[index], snowflake.transforms, QueuedModelStandard.class)
                  .monochrome(snowflake.brightness_5c);
              }
            }
          }
        }
      }
    }
    //LAB_800ed93c
  }

  static boolean renderSmoke(final WmapSmokeInstance60 smoke, final float size, final int mode, final int smokeIndex) {
    smoke.coord2_00.flg = 0;
    GsGetLs(smoke.coord2_00, smoke.transforms);
    smoke.transforms.identity(); // NOTE: does not clear translation
    GTE.setTransforms(smoke.transforms);

    GTE.perspectiveTransform(-size, -size, 0);
    final float sx0 = GTE.getScreenX(2);
    final float sy0 = GTE.getScreenY(2);
    float z = GTE.getScreenZ(3) / 4.0f;

    //LAB_800ee6cc
    if(z >= 5 || z < orderingTableSize_1f8003c8 - 3) {
      //LAB_800ee6d4
      GTE.perspectiveTransform(size, -size, 0);
      final float sx1 = GTE.getScreenX(2);
      final float sy1 = GTE.getScreenY(2);
      z = GTE.getScreenZ(3) / 4.0f;

      final float transformedSize = sx1 - sx0;

      //LAB_800ee750
      if(z >= 5 || z < orderingTableSize_1f8003c8 - 3 && sx1 - sx0 <= 0x400) {
        //LAB_800ee758
        //LAB_800ee78c
        GTE.perspectiveTransform(-size, size, 0);
        final float sx2 = GTE.getScreenX(2);
        final float sy2 = GTE.getScreenY(2);
        z = GTE.getScreenZ(3) / 4.0f;

        //LAB_800ee808
        if(z >= 5 && z < orderingTableSize_1f8003c8 - 3 && sy2 - sy0 <= 0x200) {
          //LAB_800ee810
          //LAB_800ee844
          GTE.perspectiveTransform(size, size, 0);
          final float sx3 = GTE.getScreenX(2);
          final float sy3 = GTE.getScreenY(2);
          z = GTE.getScreenZ(3) / 4.0f;

          //LAB_800ee8c0
          if(z >= 6 && z < orderingTableSize_1f8003c8 - 3 && sx3 - sx2 <= 0x400 && sy3 - sy1 <= 0x200) {
            //LAB_800ee8c8
            //LAB_800ee8fc
            //LAB_800ee930
            final Translucency translucency = mode == 8 ? Translucency.B_MINUS_F : Translucency.B_PLUS_F;

            //LAB_800ee9b0
            //LAB_800eea34
            final int index = (int)(smoke.scaleAndColourFade_50 / 0x40);

            if(smoke.objs[index] == null) {
              smoke.objs[index] = new QuadBuilder("Smoke sprite " + index + " (index " + smokeIndex + ')')
                .bpp(Bpp.BITS_4)
                .vramPos(640, 256)
                .pos(0.0f, 0.0f, 0.0f)
                .size(1.0f, 1.0f)
                .clut(640, 505)
                .uv(96, index == 0 ? 48 : 80)
                .uvSize(32, 32)
                .translucency(translucency)
                .build();
            }

            smoke.transforms.scaling(transformedSize);
            smoke.transforms.transfer.set(GPU.getOffsetX() + sx0, GPU.getOffsetY() + sy0, z * 4.0f);
            RENDERER.queueOrthoModel(smoke.objs[index], smoke.transforms, QueuedModelStandard.class)
              .monochrome((0x80 - smoke.scaleAndColourFade_50) / 255.0f);

            smoke.scaleAndColourFade_50 += 1.0f / (3.0f / vsyncMode_8007a3b8);

            if(smoke.scaleAndColourFade_50 >= 0x80) {
              smoke.scaleAndColourFade_50 = 0;
            }
            //LAB_800eeccc
            return true;
          }
        }
      }
    }
    return false;
  }

  static WmapSmokeInstance60[] allocateSmoke() {
    final WmapSmokeInstance60[] instances = new WmapSmokeInstance60[48];

    Arrays.setAll(instances, i -> new WmapSmokeInstance60());

    //LAB_800eb9b8
    for(int i = 0; i < 48; i++) {
      final WmapSmokeInstance60 smoke = instances[i];

      //LAB_800eb9d4
      GsInitCoordinate2(null, smoke.coord2_00);

      //LAB_800eba0c
      //LAB_800ebaa0
      smoke.translationOffset_54.x =  rand() % 8 - 4;
      smoke.translationOffset_54.y = -rand() % 3 - 2;
      smoke.translationOffset_54.z =  rand() % 8 - 4;

      //LAB_800ebadc
      smoke.scaleAndColourFade_50 = rand() % 0x80;
    }
    return instances;
  }

  static void deleteSmoke(final WmapSmokeInstance60[] instances) {
    for(final WmapSmokeInstance60 smoke : instances) {
      if(smoke.objs[0] != null) {
        smoke.objs[0].delete();
      }

      if(smoke.objs[1] != null) {
        smoke.objs[1].delete();
      }
    }

  }
}
