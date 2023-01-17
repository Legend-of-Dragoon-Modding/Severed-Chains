package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public class ScriptEffectStruct implements MemoryRef {
  private final Value ref;

  public final IntRef type_00;
  public final IntRef startTime_04;
  public final IntRef totalFrames_08;
  public final IntRef blue1_0c;
  public final IntRef green1_10;
  public final IntRef blue0_14;
  public final IntRef red1_18;
  public final IntRef green0_1c;
  public final IntRef red0_20;
  public final IntRef _24;

  public ScriptEffectStruct(final Value ref) {
    this.ref = ref;

    this.type_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.startTime_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this.totalFrames_08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this.blue1_0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this.green1_10 = ref.offset(4, 0x10L).cast(IntRef::new);
    this.blue0_14 = ref.offset(4, 0x14L).cast(IntRef::new);
    this.red1_18 = ref.offset(4, 0x18L).cast(IntRef::new);
    this.green0_1c = ref.offset(4, 0x1cL).cast(IntRef::new);
    this.red0_20 = ref.offset(4, 0x20L).cast(IntRef::new);
    this._24 = ref.offset(4, 0x24L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
