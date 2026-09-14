package legend.game.wmap;

import legend.core.MathHelper;
import legend.game.wmap.WMapModelAndAnimData258.CoolonWarpState;
import legend.game.wmap.WMapModelAndAnimData258.TeleportAnimationState;
import org.joml.Vector3f;

import static legend.game.Graphics.vsyncMode_8007a3b8;

/** Shared teleport interpolation retained from the retail world-map animation. */
final class WMapTravelAnimation {
  private WMapTravelAnimation() {
  }

  static void arcLerp(final Vector3f currentPosition, final Vector3f origin, final Vector3f target, final float ratio) {
    if(ratio == 0.0f) {
      currentPosition.set(origin);
    } else if(ratio == 1.0f) {
      currentPosition.set(target);
    } else {
      currentPosition.x = origin.x + (target.x - origin.x) * ratio;
      currentPosition.y = origin.y + (target.y - origin.y) * ratio + MathHelper.sin(MathHelper.PI * ratio) * -200;
      currentPosition.z = origin.z + (target.z - origin.z) * ratio;
    }
  }

  static void tickTeleport(final WMapModelAndAnimData258 modelAndAnimData, final WMapCameraAndLights19c0 cameraAndLights, final Vector3f origin, final Vector3f target) {
    arcLerp(modelAndAnimData.currPlayerPos_94, origin, target, modelAndAnimData.teleportAnimationTick_24c / (96.0f / vsyncMode_8007a3b8));

    modelAndAnimData.teleportAnimationTick_24c++;
    if(modelAndAnimData.teleportAnimationTick_24c > 96.0f / vsyncMode_8007a3b8) {
      modelAndAnimData.teleportAnimationState_248 = TeleportAnimationState.INIT_FADE_2;
    }

    //LAB_800e0980
    final float scale = (modelAndAnimData.teleportAnimationTick_24c * 0.015625f) / (3.0f / vsyncMode_8007a3b8) + MathHelper.sin(modelAndAnimData.teleportAnimationTick_24c * (MathHelper.PI / 4.0f / (3.0f / vsyncMode_8007a3b8))) / 16.0f;
    modelAndAnimData.models_0c[3].coord2_14.transforms.scale.set(scale, scale, scale);
    modelAndAnimData.models_0c[modelAndAnimData.modelIndex_1e4].coord2_14.transforms.rotate.y = cameraAndLights.currMapRotation_70.y;
    modelAndAnimData.playerRotation_a4.y = cameraAndLights.currMapRotation_70.y;
  }

  static void tickCoolonAscent(final WMapModelAndAnimData258 modelAndAnimData, final WMapCameraAndLights19c0 cameraAndLights) {
    modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x += 0.015625f / (3.0f / vsyncMode_8007a3b8); // 1/64

    if(modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x > 0.375f) { // 24/64
      modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x = 0.375f;
    }

    //LAB_800da9fc
    modelAndAnimData.models_0c[2].coord2_14.transforms.scale.set(modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x);
    modelAndAnimData.currPlayerPos_94.y -= 96.0f / (3.0f / vsyncMode_8007a3b8);

    cameraAndLights.coord2_20.coord.transfer.y -= 96.0f / (3.0f / vsyncMode_8007a3b8);

    if(cameraAndLights.coord2_20.coord.transfer.y < -1500) {
      cameraAndLights.coord2_20.coord.transfer.y = -1500;
    }

    //LAB_800daab8
    if(modelAndAnimData.currPlayerPos_94.y < -2500.0f) {
      modelAndAnimData.currPlayerPos_94.y = -2500.0f;
    }

    //LAB_800daaf0
    if(modelAndAnimData.currPlayerPos_94.y <= -2500.0f) {
      if(cameraAndLights.coord2_20.coord.transfer.y <= -1500) {
        modelAndAnimData.coolonWarpState_220 = CoolonWarpState.INIT_WORLD_MAP_2;
      }
    }

  }

  static void tickCoolonDescent(final WMapModelAndAnimData258 modelAndAnimData) {
    modelAndAnimData.currPlayerPos_94.y += 16.0f / (3.0f / vsyncMode_8007a3b8);

    if(modelAndAnimData.playerPos_208.y < modelAndAnimData.currPlayerPos_94.y) {
      modelAndAnimData.currPlayerPos_94.y = modelAndAnimData.playerPos_208.y;
    }

    //LAB_800dbe70
    if(modelAndAnimData.playerPos_208.y <= modelAndAnimData.currPlayerPos_94.y) {
      modelAndAnimData.coolonWarpState_220 = CoolonWarpState.RESTORE_DART_NEG_1;
    }

    //LAB_800dbeb4
    modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x -= 0.00390625f / (3.0f / vsyncMode_8007a3b8); // 1/256

    if(modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x < 0.25f) { // 64/256
      modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x = 0.25f;
    }

    //LAB_800dbf28
    modelAndAnimData.models_0c[2].coord2_14.transforms.scale.set(modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x);

  }
}
