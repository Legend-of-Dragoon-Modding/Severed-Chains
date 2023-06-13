package legend.game.wmap;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;
import legend.game.types.Model124;
import legend.game.types.MrgFile;
import legend.game.unpacker.FileData;

public class WMapStruct258 {
  public int _00;
  /** ubyte */
  public int _04;
  /** ubyte */
  public int _05;

  public WMapTmdRenderingStruct18 tmdRendering_08;
  public final Model124[] models_0c = new Model124[4];
  public TextureAnimation20 textureAnimation_1c;
  /** short */
  public int colour_20;

  public WMapStruct258Sub60[] _24;
  public int _28;
  public FileData imageData_2c;
  public FileData imageData_30;
  public final GsCOORDINATE2 coord2_34 = new GsCOORDINATE2();
  public final VECTOR vec_84 = new VECTOR();
  public final VECTOR vec_94 = new VECTOR();
  public final SVECTOR rotation_a4 = new SVECTOR();
  public int currentAnimIndex_ac;
  public int animIndex_b0;
  public final WMapStruct258Sub40[] _b4 = {new WMapStruct258Sub40(), new WMapStruct258Sub40(), new WMapStruct258Sub40(), new WMapStruct258Sub40()};
  public final MrgFile[] _1b4 = new MrgFile[4];
  /** short */
  public final int[] _1c4 = new int[16];
  public int modelIndex_1e4;
  public final SVECTOR svec_1e8 = new SVECTOR();
  public final SVECTOR svec_1f0 = new SVECTOR();
  /** ubyte */
  public int zoomState_1f8;
  /** ubyte */
  public int _1f9;

  public WMapRender40 _1fc;
  public final SVECTOR svec_200 = new SVECTOR();
  public final SVECTOR svec_208 = new SVECTOR();

  public int _218;
  /** ushort */
  public int _21c;
  /** ushort */
  public int _21e;
  /** byte */
  public int _220;
  /** ubyte */
  public int coolonWarpIndex_221;
  /** ubyte */
  public int coolonWarpIndex_222;
  /** ubyte */
  public int _223;
  public VECTOR[] vecs_224;
  public VECTOR[] vecs_228;
  public int[] ptr_22c;
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
}
