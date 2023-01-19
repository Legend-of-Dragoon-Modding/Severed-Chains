package legend.game.combat.types;

import legend.core.gte.VECTOR;
import legend.core.memory.types.ArrayRef;
import legend.game.types.Model124;

public class WeaponTrailEffect3c implements BttlScriptData6cSubBase1 {
  public int _00;
  public int _04;
  public int dobjIndex_08;
  public int smallestVertexIndex_0a;
  public int largestVertexIndex_0c;
  /** ubyte */
  public int _0e;
  public final VECTOR smallestVertex_10 = new VECTOR();
  public final VECTOR largestVertex_20 = new VECTOR();
  public Model124 parentModel_30;
  public ArrayRef<WeaponTrailEffectSegment2c> segments_34;
  public WeaponTrailEffectSegment2c _38;
}
