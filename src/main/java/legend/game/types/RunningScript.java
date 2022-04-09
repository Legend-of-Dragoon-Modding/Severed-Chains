package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedIntRef;

public class RunningScript implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef scriptStateIndex_00;
  public final Pointer<ScriptState<BigStruct>> scriptState_04;
  /** Pointer to the start of the current command (i.e. the parent) */
  public final Pointer<UnsignedIntRef> parentPtr_08;
  /** Pointer to the current element in the packet (may be parent or child command) */
  public final Pointer<UnsignedIntRef> commandPtr_0c;
  public final UnsignedIntRef parentCallbackIndex_10;
  public final UnsignedIntRef childCount_14;
  public final UnsignedIntRef parentParam_18;
  public final UnsignedIntRef ui_1c;
  public final ArrayRef<Pointer<UnsignedIntRef>> params_20;

  public RunningScript(final Value ref) {
    this.ref = ref;

    this.scriptStateIndex_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.scriptState_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, ScriptState.of(BigStruct::new)));
    this.parentPtr_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, UnsignedIntRef::new));
    this.commandPtr_0c = ref.offset(4, 0x0cL).cast(Pointer.deferred(4, UnsignedIntRef::new));
    this.parentCallbackIndex_10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this.childCount_14 = ref.offset(4, 0x14L).cast(UnsignedIntRef::new);
    this.parentParam_18 = ref.offset(4, 0x18L).cast(UnsignedIntRef::new);
    this.ui_1c = ref.offset(4, 0x1cL).cast(UnsignedIntRef::new);
    this.params_20 = ref.offset(4, 0x20L).cast(ArrayRef.of(Pointer.classFor(UnsignedIntRef.class), 8, 4, Pointer.deferred(4, UnsignedIntRef::new))); //TODO unsure how many elements
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
