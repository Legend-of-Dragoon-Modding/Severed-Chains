package legend.core;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.game.EngineState;
import legend.game.modding.coremod.CoreMod;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Graphics.GsGetLw;
import static legend.game.Graphics.GsMulCoord2;
import static legend.game.Graphics.PopMatrix;
import static legend.game.Graphics.PushMatrix;
import static legend.game.Graphics.worldToScreenMatrix_800c3548;

public final class Transformations {
  private Transformations() { }

  private static final Vector4f toScreenTempVec = new Vector4f();
  private static final Matrix4f toScreenTempMat = new Matrix4f();
  private static final MV toScreenTempMv = new MV();

  public static float toScreenspace(final Vector3f worldspaceCoord, final Vector2f out) {
    toScreenTempMv.identity();
    toScreenTempMv.transfer.zero();
    return toScreenspace(worldspaceCoord, toScreenTempMv, out);
  }

  public static float toScreenspace(final Vector3f worldspaceCoord, final MV transforms, final Vector2f out) {
    return toScreenspace(worldspaceCoord.x, worldspaceCoord.y, worldspaceCoord.z, transforms, out);
  }

  public static float toScreenspace(final float worldspaceX, final float worldspaceY, final float worldspaceZ, final Vector2f out) {
    toScreenTempMv.identity();
    toScreenTempMv.transfer.zero();
    return toScreenspace(worldspaceX, worldspaceY, worldspaceZ, toScreenTempMv, out);
  }

  public static float toScreenspace(final float worldspaceX, final float worldspaceY, final float worldspaceZ, final MV transforms, final Vector2f out) {
    if(RENDERER.getRenderMode() == EngineState.RenderMode.LEGACY) {
      toScreenTempMv.set(transforms);
      GsMulCoord2(worldToScreenMatrix_800c3548, toScreenTempMv);
      PushMatrix();
      GTE.setTransforms(toScreenTempMv);
      GTE.perspectiveTransform(worldspaceX, worldspaceY, worldspaceZ);
      out.set(GTE.getScreenX(2), GTE.getScreenY(2));
      PopMatrix();
      return GTE.getScreenZ(3);
    }

    toScreenTempVec.set(worldspaceX, worldspaceY, worldspaceZ, 1.0f);
    toScreenTempMat.set(transforms).setTranslation(transforms.transfer);

    toScreenTempVec.mul(toScreenTempMat);
    toScreenTempVec.mul(RENDERER.camera().getView());
    toScreenTempVec.mul(RENDERER.getPerspectiveProjection());

    final float renderAspect;
    if(CONFIG.getConfig(CoreMod.ALLOW_WIDESCREEN_CONFIG.get())) {
      renderAspect = RENDERER.getRenderAspectRatio();
    } else {
      renderAspect = 4.0f / 3.0f;
    }

    out.set(toScreenTempVec.x / toScreenTempVec.w / 2 * (RENDERER.getNativeWidth() * renderAspect / RENDERER.getNativeAspectRatio()), (-toScreenTempVec.y / toScreenTempVec.w / 2) * RENDERER.getNativeHeight());
    return toScreenTempVec.z;
  }

  private static final MV toScreenTempLw = new MV();

  public static float toScreenspace(final Vector3f worldspaceCoord, final GsCOORDINATE2 transforms, final Vector2f out) {
    GsGetLw(transforms, toScreenTempLw);
    return toScreenspace(worldspaceCoord, toScreenTempLw, out);
  }

  public static float toScreenspace(final float worldspaceX, final float worldspaceY, final float worldspaceZ, final GsCOORDINATE2 transforms, final Vector2f out) {
    GsGetLw(transforms, toScreenTempLw);
    return toScreenspace(worldspaceX, worldspaceY, worldspaceZ, toScreenTempLw, out);
  }
}
