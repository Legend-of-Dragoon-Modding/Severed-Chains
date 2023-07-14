package legend.game.combat.environment;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class StageData10 implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef _00;
  public final UnsignedByteRef musicIndex_01;
  public final UnsignedByteRef _02;
  public final UnsignedByteRef postCombatSubmapStage_03;
  public final UnsignedByteRef _04;
  public final UnsignedByteRef _05;
  public final ShortRef cameraPosIndex0_06;
  public final ShortRef cameraPosIndex1_08;
  public final ShortRef cameraPosIndex2_0a;
  public final ShortRef cameraPosIndex3_0c;
  public final UnsignedShortRef postCombatSubmapCut_0e;

  public StageData10(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this.musicIndex_01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);
    this._02 = ref.offset(1, 0x02L).cast(UnsignedByteRef::new);
    this.postCombatSubmapStage_03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    this._04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this._05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);
    this.cameraPosIndex0_06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this.cameraPosIndex1_08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this.cameraPosIndex2_0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this.cameraPosIndex3_0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
    this.postCombatSubmapCut_0e = ref.offset(2, 0x0eL).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
