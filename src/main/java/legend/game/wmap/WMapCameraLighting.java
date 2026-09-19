package legend.game.wmap;

import legend.game.types.GsF_LIGHT;
import legend.game.wmap.WMapCameraAndLights19c0.LightsUpdateState;
import legend.game.wmap.WMapModelAndAnimData258.ZoomState;
import legend.game.wmap.world.WorldMapCameraSettings;
import legend.game.wmap.world.WorldMapLightingSettings;
import org.joml.Vector3f;

import static legend.core.GameEngine.GTE;
import static legend.game.Graphics.GsSetFlatLight;
import static legend.game.Graphics.vsyncMode_8007a3b8;

/** Retail-compatible world-map light setup and zoom-transition dimming. */
final class WMapCameraLighting {
  private WMapCameraLighting() {
  }

  static void initialize(final WMapCameraAndLights19c0 cameraAndLights, final WorldMapCameraSettings camera) {
    final WorldMapLightingSettings lighting = camera.lighting() == null ? WorldMapLightingSettings.DEFAULT : camera.lighting();

    for(int i = 0; i < cameraAndLights.lights_11c.length; i++) {
      final WorldMapLightingSettings.Light setting = lighting.lights().get(i);
      final GsF_LIGHT light = cameraAndLights.lights_11c[i];
      light.r_0c = setting.colour().x();
      light.g_0d = setting.colour().y();
      light.b_0e = setting.colour().z();
      light.direction_00.set(setting.direction().x(), setting.direction().y(), setting.direction().z());
      GsSetFlatLight(i, light);
    }

    cameraAndLights.ambientLight_14c.set(lighting.ambient().x(), lighting.ambient().y(), lighting.ambient().z());
    GTE.setBackgroundColour(cameraAndLights.ambientLight_14c.x, cameraAndLights.ambientLight_14c.y, cameraAndLights.ambientLight_14c.z);
    cameraAndLights.lightsUpdateState_88 = LightsUpdateState.INIT_DIMMING_0;
  }

  static void update(final WMapCameraAndLights19c0 cameraAndLights, final WMapModelAndAnimData258 modelAndAnimData, final WorldMapCameraSettings camera) {
    if(modelAndAnimData.zoomState_1f8 == ZoomState.LOCAL_0) {
      return;
    }

    final WorldMapLightingSettings lighting = camera.lighting() == null ? WorldMapLightingSettings.DEFAULT : camera.lighting();

    //LAB_800d21cc
    if(modelAndAnimData.zoomState_1f8 == ZoomState.TRANSITION_MODEL_OUT_2 || modelAndAnimData.zoomState_1f8 == ZoomState.WORLD_3) {
      //LAB_800d2228
      switch(cameraAndLights.lightsUpdateState_88) {
        case INIT_DIMMING_0:
          //LAB_800d2258
          //LAB_800d225c
          for(int i = 0; i < 3; i++) {
            //LAB_800d2278
            cameraAndLights.lightsColours_8c[i].x = (int)(cameraAndLights.lights_11c[i].r_0c * 0x100);
            cameraAndLights.lightsColours_8c[i].y = (int)(cameraAndLights.lights_11c[i].g_0d * 0x100);
            cameraAndLights.lightsColours_8c[i].z = (int)(cameraAndLights.lights_11c[i].b_0e * 0x100);
          }

          //LAB_800d235c
          cameraAndLights.lightsBrightness_84 = 1.0f;
          cameraAndLights.lightsUpdateState_88 = LightsUpdateState.DIM_1;

        case DIM_1:
          //LAB_800d237c
          cameraAndLights.lightsBrightness_84 -= lighting.transitionStep() / (3.0f / vsyncMode_8007a3b8);

          if(cameraAndLights.lightsBrightness_84 < lighting.transitionBrightness()) {
            cameraAndLights.lightsBrightness_84 = lighting.overviewBrightness();
            cameraAndLights.lightsUpdateState_88 = LightsUpdateState.INIT_BRIGHTENING_2;
          }

          //LAB_800d23e0
          //LAB_800d23e4
          for(int i = 0; i < 3; i++) {
            final GsF_LIGHT light = cameraAndLights.lights_11c[i];

            //LAB_800d2400
            //LAB_800d2464
            //LAB_800d24d0
            //LAB_800d253c
            light.r_0c = camera.lighting() == null ? cameraAndLights.lightsColours_8c[i].x * cameraAndLights.lightsBrightness_84 / 0x100 : lighting.lights().get(i).colour().x() * cameraAndLights.lightsBrightness_84;
            light.g_0d = camera.lighting() == null ? cameraAndLights.lightsColours_8c[i].y * cameraAndLights.lightsBrightness_84 / 0x100 : lighting.lights().get(i).colour().y() * cameraAndLights.lightsBrightness_84;
            light.b_0e = camera.lighting() == null ? cameraAndLights.lightsColours_8c[i].z * cameraAndLights.lightsBrightness_84 / 0x100 : lighting.lights().get(i).colour().z() * cameraAndLights.lightsBrightness_84;
            GsSetFlatLight(i, cameraAndLights.lights_11c[i]);
          }

          break;
      }
    }

    //LAB_800d2590
    //LAB_800d2598
    if(modelAndAnimData.zoomState_1f8 == ZoomState.TRANSITION_MODEL_IN_4) {
      //LAB_800d25d8
      switch(cameraAndLights.lightsUpdateState_88) {
        case INIT_BRIGHTENING_2:
          //LAB_800d2608
          cameraAndLights.lightsBrightness_84 = lighting.transitionBrightness();
          cameraAndLights.lightsUpdateState_88 = LightsUpdateState.BRIGHTEN_3;

        case BRIGHTEN_3:
          //LAB_800d2628
          cameraAndLights.lightsBrightness_84 += lighting.transitionStep() / (3.0f / vsyncMode_8007a3b8);

          if(cameraAndLights.lightsBrightness_84 > 1.0f) {
            cameraAndLights.lightsBrightness_84 = 1.0f;
            cameraAndLights.lightsUpdateState_88 = LightsUpdateState.INIT_DIMMING_0;
          }

          //LAB_800d268c
          //LAB_800d2690
          for(int i = 0; i < 3; i++) {
            final GsF_LIGHT light = cameraAndLights.lights_11c[i];

            //LAB_800d26ac
            //LAB_800d2710
            //LAB_800d277c
            //LAB_800d27e8
            light.r_0c = camera.lighting() == null ? cameraAndLights.lightsColours_8c[i].x * cameraAndLights.lightsBrightness_84 / 0x100 : lighting.lights().get(i).colour().x() * cameraAndLights.lightsBrightness_84;
            light.g_0d = camera.lighting() == null ? cameraAndLights.lightsColours_8c[i].y * cameraAndLights.lightsBrightness_84 / 0x100 : lighting.lights().get(i).colour().y() * cameraAndLights.lightsBrightness_84;
            light.b_0e = camera.lighting() == null ? cameraAndLights.lightsColours_8c[i].z * cameraAndLights.lightsBrightness_84 / 0x100 : lighting.lights().get(i).colour().z() * cameraAndLights.lightsBrightness_84;
            GsSetFlatLight(i, cameraAndLights.lights_11c[i]);
          }

          break;
      }
    }
    //LAB_800d283c
    //LAB_800d2844
  }

  static Vector3f target(final WMapCameraAndLights19c0 camera, final ZoomState zoomState, final WorldMapCameraSettings settings) {
    final Vector3f target = new Vector3f(camera.coord2_20.coord.transfer);
    if(settings.minimum() != null && camera.zoomStateIsLocal_c4 && zoomState == ZoomState.LOCAL_0) {
      target.set(Math.max(settings.minimum().x(), Math.min(settings.maximum().x(), target.x)), Math.max(settings.minimum().y(), Math.min(settings.maximum().y(), target.y)), Math.max(settings.minimum().z(), Math.min(settings.maximum().z(), target.z)));
    }
    return target;
  }
}
