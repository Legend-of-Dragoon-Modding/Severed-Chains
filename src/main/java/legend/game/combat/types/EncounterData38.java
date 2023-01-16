package legend.game.combat.types;

import legend.core.MathHelper;
import legend.core.gte.SVECTOR;

public class EncounterData38 {
  public final short[] enemyIndices_00 = new short[3];
  public final EnemyInfo08[] enemyInfo_08 = new EnemyInfo08[6];

  public EncounterData38(final byte[] data, int offset) {
    for(int i = 0; i < this.enemyIndices_00.length; i++) {
      this.enemyIndices_00[i] = (short)MathHelper.get(data, offset, 2);
      offset += 2;
    }

    offset += 2;

    for(int i = 0; i < this.enemyInfo_08.length; i++) {
      this.enemyInfo_08[i] = new EnemyInfo08((short)MathHelper.get(data, offset, 2), new SVECTOR().set((short)MathHelper.get(data, offset + 2, 2), (short)MathHelper.get(data, offset + 4, 2), (short)MathHelper.get(data, offset + 6, 2)));
      offset += 8;
    }
  }

  public static class EnemyInfo08 {
    public final short index_00;
    public final SVECTOR pos_02;

    private EnemyInfo08(final short index, final SVECTOR pos) {
      this.index_00 = index;
      this.pos_02 = pos;
    }
  }
}
