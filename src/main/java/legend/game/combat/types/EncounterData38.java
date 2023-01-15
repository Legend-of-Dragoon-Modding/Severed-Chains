package legend.game.combat.types;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedShortRef;

public class EncounterData38 implements MemoryRef {
  private final Value ref;

  public final ArrayRef<UnsignedShortRef> enemyIndices_00;

  public final ArrayRef<EnemyInfo08> enemyInfo_08;

  public EncounterData38(final Value ref) {
    this.ref = ref;

    this.enemyIndices_00 = ref.offset(2, 0x00L).cast(ArrayRef.of(UnsignedShortRef.class, 3, 2, UnsignedShortRef::new));

    this.enemyInfo_08 = ref.offset(2, 0x08L).cast(ArrayRef.of(EnemyInfo08.class, 6, 8, EnemyInfo08::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }

  public static class EnemyInfo08 implements MemoryRef {
    private final Value ref;

    public final ShortRef index_00;
    public final SVECTOR pos_02;

    public EnemyInfo08(final Value ref) {
      this.ref = ref;

      this.index_00 = ref.offset(2, 0x00L).cast(ShortRef::new);
      this.pos_02 = ref.offset(2, 0x02L).cast(SVECTOR::new);
    }

    @Override
    public long getAddress() {
      return this.ref.getAddress();
    }
  }
}
