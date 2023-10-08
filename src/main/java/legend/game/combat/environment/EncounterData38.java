package legend.game.combat.environment;

import legend.core.MathHelper;
import org.joml.Vector3f;

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
      this.enemyInfo_08[i] = new EnemyInfo08(MathHelper.getShort(data, offset), new Vector3f(MathHelper.getShort(data, offset + 2), MathHelper.getShort(data, offset + 4), MathHelper.getShort(data, offset + 6)));
      offset += 8;
    }
  }

  public static class EnemyInfo08 {
    public final short index_00;
    public final Vector3f pos_02;

    private EnemyInfo08(final short index, final Vector3f pos) {
      this.index_00 = index;
      this.pos_02 = pos;
    }
  }
}
