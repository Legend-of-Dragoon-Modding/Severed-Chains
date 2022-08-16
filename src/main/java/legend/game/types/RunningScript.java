package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedIntRef;

public class RunningScript implements MemoryRef {
  private final Value ref;

  public final IntRef scriptStateIndex_00;
  public final Pointer<ScriptState<BigStruct>> scriptState_04;
  /** Pointer to the start of the current command (i.e. the op) */
  public final Pointer<IntRef> opPtr_08;
  /** Pointer to the current element in the packet (may be op or param) */
  public final Pointer<IntRef> commandPtr_0c;
  public final UnsignedIntRef opIndex_10;
  public final UnsignedIntRef paramCount_14;
  public final UnsignedIntRef opParam_18;
  public final UnsignedIntRef ui_1c;
  public final ArrayRef<Pointer<IntRef>> params_20;

  public RunningScript(final Value ref) {
    this.ref = ref;

    this.scriptStateIndex_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.scriptState_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, ScriptState.of(BigStruct::new)));
    this.opPtr_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, IntRef::new));
    this.commandPtr_0c = ref.offset(4, 0x0cL).cast(Pointer.deferred(4, IntRef::new));
    this.opIndex_10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this.paramCount_14 = ref.offset(4, 0x14L).cast(UnsignedIntRef::new);
    this.opParam_18 = ref.offset(4, 0x18L).cast(UnsignedIntRef::new);
    this.ui_1c = ref.offset(4, 0x1cL).cast(UnsignedIntRef::new);
    this.params_20 = ref.offset(4, 0x20L).cast(ArrayRef.of(Pointer.classFor(IntRef.class), 10, 4, Pointer.deferred(4, IntRef::new))); //TODO unsure how many elements
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
