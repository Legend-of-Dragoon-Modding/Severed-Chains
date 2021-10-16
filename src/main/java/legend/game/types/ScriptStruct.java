package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedIntRef;

public class ScriptStruct implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef index_00;
  public final Pointer<BiggerStruct<BigStruct>> biggerStruct_04;
  public final Pointer<UnsignedIntRef> ui_08;
  public final Pointer<UnsignedIntRef> commandPtr_0c;
  public final UnsignedIntRef parentCallbackIndex_10;
  public final UnsignedIntRef childCount_14;
  public final UnsignedIntRef parentParam_18;
  public final UnsignedIntRef ui_1c;
  public final ArrayRef<Pointer<UnsignedIntRef>> params_20;

  public ScriptStruct(final Value ref) {
    this.ref = ref;

    this.index_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.biggerStruct_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, BiggerStruct.of(BigStruct::new)));
    this.ui_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, UnsignedIntRef::new));
    this.commandPtr_0c = ref.offset(4, 0x0cL).cast(Pointer.deferred(4, UnsignedIntRef::new));
    this.parentCallbackIndex_10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this.childCount_14 = ref.offset(4, 0x14L).cast(UnsignedIntRef::new);
    this.parentParam_18 = ref.offset(4, 0x18L).cast(UnsignedIntRef::new);
    this.ui_1c = ref.offset(4, 0x1cL).cast(UnsignedIntRef::new);
    this.params_20 = ref.offset(4, 0x20L).cast(ArrayRef.of(Pointer.classFor(UnsignedIntRef.class), 6, 4, Pointer.deferred(4, UnsignedIntRef::new))); //TODO unsure how many elements
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
