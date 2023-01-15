package legend.game.types;

import legend.core.gte.COLOUR;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;

import java.util.Arrays;

public class WMapStruct19c0 {
  public final GsRVIEW2 rview2_00 = new GsRVIEW2();
  public final GsCOORDINATE2 coord2_20 = new GsCOORDINATE2();
  public final SVECTOR mapRotation_70 = new SVECTOR();
  /** short */
  public int mapRotationStartAngle_78;
  /** short */
  public int mapRotationEndAngle_7a;
  /** short */
  public int mapRotationStep_7c;
  /** short */
  public int mapRotationCounter_7e;
  /** ubyte */
  public int mapRotating_80;

  public int _84;
  public int _88;
  public final COLOUR[] colour_8c = {new COLOUR(), new COLOUR(), new COLOUR()};
  /** short */
  public int _98;
  /** short */
  public int _9a;
  /** short */
  public int _9c;
  /** short */
  public int _9e;
  /** short */
  public int _a0;
  public final VECTOR vec_a4 = new VECTOR();
  public final VECTOR vec_b4 = new VECTOR();
  /** byte (bool?) */
  public int _c4;
  /** ubyte */
  public int _c5;

  public final GsRVIEW2 rview2_c8 = new GsRVIEW2();

  public int viewpointY_ec;
  public int viewpointZ_f0;

  public int refpointY_f8;
  public int refpointZ_fc;

  /** short */
  public int _108;
  /** short */
  public int _10a;
  /** short */
  public int _10c;
  /** ushort */
  public int _10e;
  /** ubyte */
  public int _110;

  public int _114;
  /** short */
  public int _118;
  /** ubyte */
  public int _11a;

  public final GsF_LIGHT[] lights_11c = {new GsF_LIGHT(), new GsF_LIGHT(), new GsF_LIGHT()};
  public final SVECTOR ambientLight_14c = new SVECTOR();
  public final WMapSubStruct18[] _154 = new WMapSubStruct18[0x101];
  public int _196c;
  public int _1970;
  public int _1974;

  public final short[] _19a8 = new short[3];
  public final short[] _19ae = new short[3];

  public WMapStruct19c0() {
    Arrays.setAll(this._154, i -> new WMapSubStruct18());
  }
}
