package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.TriFunctionRef;
import legend.core.memory.types.UnsignedIntRef;

import java.util.function.Function;

/** Holds persistent data for scripts */
public class ScriptState<T extends MemoryRef> implements MemoryRef {
  public static <T extends MemoryRef> Function<Value, ScriptState<T>> of(final Function<Value, T> constructor) {
    return ref -> new ScriptState<>(ref, constructor);
  }

  public static <T extends MemoryRef> Class<ScriptState<T>> classFor(final Class<T> t) {
    //noinspection unchecked
    return (Class<ScriptState<T>>)(Class<?>)ScriptState.class;
  }

  private final Value ref;

  public final Pointer<T> innerStruct_00;
  public final Pointer<TriConsumerRef<Integer, ScriptState<T>, T>> callback_04;
  public final Pointer<TriConsumerRef<Integer, ScriptState<T>, T>> callback_08;
  public final Pointer<TriConsumerRef<Integer, ScriptState<T>, T>> callback_0c;
  /** If the callback returns non-zero, it's set to null */
  public final Pointer<TriFunctionRef<Integer, ScriptState<T>, T, Long>> callback_10;
  /** Pointer to the script file */
  public final Pointer<ScriptFile> scriptPtr_14;
  /** Pointer to the current script command */
  public final Pointer<UnsignedIntRef> commandPtr_18;
  public final ArrayRef<Pointer<UnsignedIntRef>> commandStack_1c;
  public final ArrayRef<UnsignedIntRef> storage_44;
  /**
   * <p>Bit set - which of the pointers at the start of the struct are set</p>
   *
   * <ul>
   *   <li>Bit 17 - {@link ScriptState#scriptPtr_14} is unset</li>
   *   <li>Bit 18 - {@link ScriptState#callback_04} is unset</li>
   *   <li>Bit 19 - {@link ScriptState#callback_08} is unset</li>
   *   <li>Bit 26 - {@link ScriptState#callback_10} is set (note: not sure why this is backwards from the others)</li>
   *   <li>Bit 27 - {@link ScriptState#callback_0c} is unset</li>
   * </ul>
   *
   * <ul>
   *   <li>If bits 17 and 20 are not set, the script will be executed</li>
   *   <li>If bits 18 and 20 are not set, {@link ScriptState#callback_04} will be executed</li>
   *   <li>If bits 19 and 20 are not set, {@link ScriptState#callback_08} will be executed</li>
   *   <li>If bit 26 is set and bit 20 is not set, {@link ScriptState#callback_10} will be executed</li>
   * </ul>
   */
  public final UnsignedIntRef ui_60; // Note: also contained in previous array

  public final UnsignedIntRef ui_f8;
  public final UnsignedIntRef ui_fc;

  public ScriptState(final Value ref, final Function<Value, T> innerStructConstructor) {
    this.ref = ref;

    this.innerStruct_00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, innerStructConstructor));
    this.callback_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, TriConsumerRef::new));
    this.callback_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, TriConsumerRef::new));
    this.callback_0c = ref.offset(4, 0x0cL).cast(Pointer.deferred(4, TriConsumerRef::new));
    this.callback_10 = ref.offset(4, 0x10L).cast(Pointer.deferred(4, TriFunctionRef::new));
    this.scriptPtr_14 = ref.offset(4, 0x14L).cast(Pointer.deferred(4, ScriptFile::new));
    this.commandPtr_18 = ref.offset(4, 0x18L).cast(Pointer.deferred(4, UnsignedIntRef::new));
    this.commandStack_1c = ref.offset(4, 0x1cL).cast(ArrayRef.of(Pointer.classFor(UnsignedIntRef.class), 10, 4, Pointer.deferred(4, UnsignedIntRef::new)));
    this.storage_44 = ref.offset(4, 0x44L).cast(ArrayRef.of(UnsignedIntRef.class, 32, 4, UnsignedIntRef::new)); // Dunno how long this should be
    this.ui_60 = ref.offset(4, 0x60L).cast(UnsignedIntRef::new);

    this.ui_f8 = ref.offset(4, 0xf8L).cast(UnsignedIntRef::new);
    this.ui_fc = ref.offset(4, 0xfcL).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
