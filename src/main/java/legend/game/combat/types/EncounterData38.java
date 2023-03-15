package legend.game.combat.types;

import legend.core.MathHelper;
import legend.core.gte.SVECTOR;

public class EncounterData38 {
  public final short[] enemyIndices_00;
  public final EnemyInfo08[] enemyInfo_08;

  public EncounterData38(final short[] enemyIndices, final EnemyInfo08[] enemyInfo) {
    this.enemyIndices_00 = enemyIndices;
    this.enemyInfo_08 = enemyInfo;
  }

  public static EncounterData38 fromOverlay(final byte[] data, int offset) {
    final short[] enemyIndices = new short[3];
    final EnemyInfo08[] enemyInfo = new EnemyInfo08[6];

    for(int i = 0; i < enemyIndices.length; i++) {
      enemyIndices[i] = (short)MathHelper.get(data, offset, 2);
      offset += 2;
    }

    offset += 2;

    for(int i = 0; i < enemyInfo.length; i++) {
      enemyInfo[i] = new EnemyInfo08((short)MathHelper.get(data, offset, 2), new SVECTOR().set((short)MathHelper.get(data, offset + 2, 2), (short)MathHelper.get(data, offset + 4, 2), (short)MathHelper.get(data, offset + 6, 2)));
      offset += 8;
    }

    return new EncounterData38(enemyIndices, enemyInfo);
  }

  public static class EnemyInfo08 {
    public final short index_00;
    public final SVECTOR pos_02;

    public EnemyInfo08(final short index, final SVECTOR pos) {
      this.index_00 = index;
      this.pos_02 = pos;
    }
  }
}
