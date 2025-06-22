package legend.core;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.game.EngineState;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.GsMulCoord2;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;

public final class Transformations {
  private Transformations() { }

  private static final Vector4f toScreenTempVec = new Vector4f();
  private static final Matrix4f toScreenTempMat = new Matrix4f();
  private static final MV toScreenTempMv = new MV();

  public static void toScreenspace(final Vector3f worldspaceCoord, final MV transforms, final Vector2f out) {
    if(RENDERER.getRenderMode() == EngineState.RenderMode.LEGACY) {
      toScreenTempMv.set(transforms);
      GsMulCoord2(worldToScreenMatrix_800c3548, toScreenTempMv);
      GTE.setTransforms(toScreenTempMv);
      GTE.perspectiveTransform(worldspaceCoord.x, worldspaceCoord.y, worldspaceCoord.z);
      out.set(GTE.getScreenX(2), GTE.getScreenY(2));
      return;
    }

    toScreenTempVec.set(worldspaceCoord, 1.0f);
    toScreenTempMat.set(transforms).setTranslation(transforms.transfer);

    toScreenTempVec.mul(toScreenTempMat);
    toScreenTempVec.mul(RENDERER.camera().getView());
    toScreenTempVec.mul(RENDERER.getPerspectiveProjection());

    out.set(toScreenTempVec.x / toScreenTempVec.w / 2 * (RENDERER.getNativeWidth() * RENDERER.getRenderAspectRatio() / RENDERER.getNativeAspectRatio()), (-toScreenTempVec.y / toScreenTempVec.w / 2) * RENDERER.getNativeHeight());
  }

  private static final MV toScreenTempLw = new MV();

  public static void toScreenspace(final Vector3f worldspaceCoord, final GsCOORDINATE2 transforms, final Vector2f out) {
    GsGetLw(transforms, toScreenTempLw);
    toScreenspace(worldspaceCoord, toScreenTempLw, out);
  }
}
