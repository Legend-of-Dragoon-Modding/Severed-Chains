package legend.game.combat.environment;

import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;
import legend.game.types.GsRVIEW2;

public class BattleCamera {
  public final GsRVIEW2 rview2_00 = new GsRVIEW2();
  public final VECTOR vec_20 = new VECTOR();  // related to refpoint
  public int _2c;
  public int _30;

  public int _38;
  public int _3c;  // refpoint X factor?
  public int _40;
  public int _44;
  public int _48;  // refpoint Y factor?

  public int _54;  // refpoint Z factor?

  public int _5c;  // refpoint step count?
  public final VECTOR vec_60 = new VECTOR();
  public int _6c;
  public int _70;
  public final VECTOR vec_74 = new VECTOR();
  public int bobjIndex_80;  // refpoint bobj index?

  public int callbackIndex_88;  // refpoint callback?
  public final SVECTOR svec_8c = new SVECTOR();
  public final VECTOR vec_94 = new VECTOR();  // related to viewpoint
  public int _a0; // something z
  public int _a4;

  public int _ac; // something x
  public int _b0; // something x
  public int _b4;
  public int _b8; // something y
  public int _bc; // something y

  public int _c8; // something z

  public int _d0;  // viewpoint step count?
  public int _d4;  // viewpoint X factor?
  public int _d8;  // viewpoint Y factor?
  public int _dc;  // viewpoint Z factor?
  public int _e0;
  public int _e4;
  public int _e8;  // viewpoint X factor?
  public int _ec;  // viewpoint Y factor?
  public int _f0;  // viewpoint Z factor?
  public int bobjIndex_f4;  // viewpoint bobj index?

  public int callbackIndex_fc;  // viewpoint callback?
  public int _100;
  public int _104;
  public int _108;
  public int _10c;
  public int _110;
  public int _114;
  /**
   * ubyte
   */
  public int _118;

  public int _11c;
  /**
   * ubyte
   */
  public int _120;  // viewpoint callback
  /**
   * ubyte
   */
  public int _121;  // refpoint callback
  /**
   * ubyte
   */
  public int _122;
  /** ubyte */
  public int _123;
}
