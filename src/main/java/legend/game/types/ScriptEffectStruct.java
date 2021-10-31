package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class ScriptEffectStruct implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef type_00;
  public final UnsignedIntRef startTime_04;
  public final UnsignedIntRef totalFrames_08;
  public final UnsignedIntRef blue1_0c;
  public final UnsignedIntRef green1_10;
  public final UnsignedIntRef blue0_14;
  public final UnsignedIntRef red1_18;
  public final UnsignedIntRef green0_1c;
  public final UnsignedIntRef red0_20;
  public final UnsignedIntRef _24;

  public ScriptEffectStruct(final Value ref) {
    this.ref = ref;

    this.type_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.startTime_04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this.totalFrames_08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.blue1_0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this.green1_10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this.blue0_14 = ref.offset(4, 0x14L).cast(UnsignedIntRef::new);
    this.red1_18 = ref.offset(4, 0x18L).cast(UnsignedIntRef::new);
    this.green0_1c = ref.offset(4, 0x1cL).cast(UnsignedIntRef::new);
    this.red0_20 = ref.offset(4, 0x20L).cast(UnsignedIntRef::new);
    this._24 = ref.offset(4, 0x24L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
