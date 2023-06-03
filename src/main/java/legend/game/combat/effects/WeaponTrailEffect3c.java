package legend.game.combat.effects;

import legend.core.gte.VECTOR;
import legend.core.memory.types.ArrayRef;
import legend.game.types.Model124;

public class WeaponTrailEffect3c implements BttlScriptData6cSubBase1 {
  public int currentSegmentIndex_00;
  public int unused_04;
  public int dobjIndex_08;
  public int largestVertexIndex_0a;
  public int smallestVertexIndex_0c;
  /**
   * ubyte
   */
  public int segmentCount_0e;
  public final VECTOR largestVertex_10 = new VECTOR();
  public final VECTOR smallestVertex_20 = new VECTOR();
  public Model124 parentModel_30;
  public ArrayRef<WeaponTrailEffectSegment2c> segments_34;
  public WeaponTrailEffectSegment2c currentSegment_38;
}
