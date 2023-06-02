package legend.game.combat.effects;

import legend.core.gte.VECTOR;
import legend.core.memory.types.ArrayRef;
import legend.game.types.Model124;

public class WeaponTrailEffect3c implements BttlScriptData6cSubBase1 {
  public int _00;
  public int _04;
  public int dobjIndex_08;
  public int largestVertexIndex_0a;
  public int smallestVertexIndex_0c;
  /**
   * ubyte
   */
  public int _0e;
  public final VECTOR largestVertex_10 = new VECTOR();
  public final VECTOR smallestVertex_20 = new VECTOR();
  public Model124 parentModel_30;
  public ArrayRef<WeaponTrailEffectSegment2c> segments_34;
  public WeaponTrailEffectSegment2c _38;
}
