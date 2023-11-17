package legend.game.wmap;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.opengl.MeshObj;
import legend.game.types.Model124;
import legend.game.unpacker.FileData;
import org.joml.Vector3f;

public class WMapStruct258 {
  public int _00;
  /** ubyte */
  public int _04;
  /** ubyte */
  public WmapStateEnum wmapState_05;

  public WMapTmdRenderingStruct18 tmdRendering_08;
  public final Model124[] models_0c = new Model124[4];
  public TextureAnimation20 textureAnimation_1c;
  /** short */
  public float colour_20;

  public MeshObj mapOverlayObj;
  public MeshObj[] zoomOverlayObjs = new MeshObj[7];
  public final MV mapOverlayTransforms = new MV();

  public MapMarker mapArrow;
  public MapMarker coolonPlaceMarker;

  public WMapAtmosphericEffectInstance60[] atmosphericEffectInstances_24;
  public MeshObj[] atmosphericEffectSprites;

  public float clutYIndex_28;
  public FileData imageData_2c;
  public FileData imageData_30;
  /** Used as the camera position, only translation is used */
  public final GsCOORDINATE2 coord2_34 = new GsCOORDINATE2();
  public final Vector3f vec_84 = new Vector3f();
  public final Vector3f vec_94 = new Vector3f();
  public final Vector3f rotation_a4 = new Vector3f();
  public int currentAnimIndex_ac;
  public int animIndex_b0;
  public final WMapStruct258Sub40[] _b4 = {new WMapStruct258Sub40(), new WMapStruct258Sub40(), new WMapStruct258Sub40(), new WMapStruct258Sub40()};

  /** short */
  public final int[] _1c4 = new int[16];
  public int modelIndex_1e4;
  public final Vector3f svec_1e8 = new Vector3f();
  public final Vector3f svec_1f0 = new Vector3f();
  /** ubyte */
  public int zoomState_1f8;
  /** Not in retail */
  public int previousZoomLevel;
  /** ubyte */
  public int _1f9;
  /** Highlight refactored into WmapPromptPopup */
  // public WmapMenuTextHighlight40 coolonTravelMenuSelectorHighlight_1fc;
  public final Vector3f svec_200 = new Vector3f();
  public final Vector3f svec_208 = new Vector3f();

  public int _218;
  /** ushort */
  public float angle_21c;
  /** ushort */
  public float angle_21e;
  /** byte */
  public int _220;
  /** ubyte */
  public int coolonWarpIndex_221;
  /** ubyte */
  public int coolonWarpIndex_222;
  /** ubyte */
  public int _223;
  public Vector3f[] vecs_224;
  public Vector3f[] vecs_228;
  public int[] _22c;
  public int _230;
  public int _234;
  public int _238;
  public int _23c;
  public int _240;
  /** byte */
  public int _244;

  public int _248;
  public int _24c;
  public int _250;
  public int _254;

  public void deleteAtmosphericEffectObjs() {
    for(final MeshObj obj : this.atmosphericEffectSprites) {
      if(obj != null) {
        obj.delete();
      }
    }
  }
}
