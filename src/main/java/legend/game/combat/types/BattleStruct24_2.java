package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.types.ScriptFile;

public class BattleStruct24_2 implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _00;
  public final IntRef scriptIndex_04;
  public final IntRef _08;
  public final IntRef _0c;
  public final IntRef scriptOffsetIndex_10;
  public final Pointer<ScriptFile> script_14;
  public final IntRef scriptIndex_18;
  public final IntRef _1c;
  public final IntRef frameCount_20;

  public BattleStruct24_2(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.scriptIndex_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this.scriptOffsetIndex_10 = ref.offset(4, 0x10L).cast(IntRef::new);
    this.script_14 = ref.offset(4, 0x14L).cast(Pointer.deferred(4, ScriptFile::new));
    this.scriptIndex_18 = ref.offset(4, 0x18L).cast(IntRef::new);
    this._1c = ref.offset(4, 0x1cL).cast(IntRef::new);
    this.frameCount_20 = ref.offset(4, 0x20L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
