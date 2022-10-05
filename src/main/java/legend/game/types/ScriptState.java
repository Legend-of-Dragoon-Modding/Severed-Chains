package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.*;

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
  public final Pointer<TriConsumerRef<Integer, ScriptState<T>, T>> ticker_04;
  public final Pointer<TriConsumerRef<Integer, ScriptState<T>, T>> renderer_08;
  public final Pointer<TriConsumerRef<Integer, ScriptState<T>, T>> destructor_0c;
  /** If the callback returns non-zero, it's set to null */
  public final Pointer<TriFunctionRef<Integer, ScriptState<T>, T, Long>> tempTicker_10;
  /** Pointer to the script file */
  public final Pointer<ScriptFile> scriptPtr_14;
  /** Pointer to the current script command */
  public final Pointer<IntRef> commandPtr_18;
  public final ArrayRef<Pointer<IntRef>> commandStack_1c;
  /**
   * 5 - parent script index
   * 6 - child script index
   */
  public final ArrayRef<IntRef> storage_44;
  /**
   * <strong>NOTE: also contained in previous array</strong>
   *
   * <p>Bit set - which of the pointers at the start of the struct are set</p>
   *
   * <ul>
   *   <li>Bit 17 - {@link ScriptState#scriptPtr_14} is unset</li>
   *   <li>Bit 18 - {@link ScriptState#ticker_04} is unset</li>
   *   <li>Bit 19 - {@link ScriptState#renderer_08} is unset</li>
   *   <li>Bit 20 - Child script</li>
   *   <li>Bit 21 - Parent script</li>
   *   <li>Bit 26 - {@link ScriptState#tempTicker_10} is set (note: not sure why this is backwards from the others)</li>
   *   <li>Bit 27 - {@link ScriptState#destructor_0c} is unset</li>
   * </ul>
   *
   * <ul>
   *   <li>If bits 17 and 20 are not set, the script will be executed</li>
   *   <li>If bits 18 and 20 are not set, {@link ScriptState#ticker_04} will be executed</li>
   *   <li>If bits 19 and 20 are not set, {@link ScriptState#renderer_08} will be executed</li>
   *   <li>If bit 26 is set and bit 20 is not set, {@link ScriptState#tempTicker_10} will be executed</li>
   *   <li>If bits 27 and 20 are not set, {@link ScriptState#destructor_0c} will be executed</li>
   * </ul>
   *
   * <p>In combat this variable is used for a few different things:</p>
   * <ul>
   *   <li>0x2 - dragoon</li>
   *   <li>0x4 - is enemy</li>
   *   <li>0x8 - it is this character's turn</li>
   *   <li>0x20 - ?</li>
   * </ul>
   */
  public final UnsignedIntRef ui_60; // Note: also contained in previous array
  /**
   * <strong>NOTE: also contained in previous array</strong>
   *
   * <p>Battle Menu flag - controls which options you can choose from.</p>

   * <ul>
   *   <li>0x01 Attack</li>
   *   <li>0x02 Guard</li>
   *   <li>0x04 Items</li>
   *   <li>0x08 Escape</li>
   *   <li>0x10 Dragoon</li>
   *   <li>0x20 D-Attack</li>
   *   <li>0x40 Magic</li>
   *   <li>0x80 Special</li>
   * <ul>
   */
  public final UnsignedByteRef _64;
  public final IntRef _c4;
  public final IntRef scriptIndex_c8;
  /**
   * IsDragoon
   */
  public final BoolRef _c0;
  public final IntRef _cc;
  public final IntRef _d0;
  public final IntRef _d4;
  public final IntRef _d8;
  public final IntRef _dc;
  public final IntRef _e0;
  public final IntRef _e4;
  public final IntRef _e8;
  public final IntRef _ec;
  public final IntRef _f0;
  public final IntRef _f4;
  public final Pointer<CString> typePtr_f8;
  public final UnsignedIntRef ui_fc;

  public ScriptState(final Value ref, final Function<Value, T> innerStructConstructor) {
    this.ref = ref;

    this.innerStruct_00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, innerStructConstructor));
    this.ticker_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, TriConsumerRef::new));
    this.renderer_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, TriConsumerRef::new));
    this.destructor_0c = ref.offset(4, 0x0cL).cast(Pointer.deferred(4, TriConsumerRef::new));
    this.tempTicker_10 = ref.offset(4, 0x10L).cast(Pointer.deferred(4, TriFunctionRef::new));
    this.scriptPtr_14 = ref.offset(4, 0x14L).cast(Pointer.deferred(4, ScriptFile::new));
    this.commandPtr_18 = ref.offset(4, 0x18L).cast(Pointer.deferred(4, IntRef::new));
    this.commandStack_1c = ref.offset(4, 0x1cL).cast(ArrayRef.of(Pointer.classFor(IntRef.class), 10, 4, Pointer.deferred(4, IntRef::new)));
    this.storage_44 = ref.offset(4, 0x44L).cast(ArrayRef.of(IntRef.class, 33, 4, IntRef::new)); // Dunno how long this should be
    this.ui_60 = ref.offset(4, 0x60L).cast(UnsignedIntRef::new);
    this._64 = ref.offset(1, 0x64L).cast(UnsignedByteRef::new);
    this._c0 = ref.offset(1, 0x40L).cast(BoolRef::new);
    this._c4 = ref.offset(4, 0xc4L).cast(IntRef::new);
    this.scriptIndex_c8 = ref.offset(4, 0xc8L).cast(IntRef::new);
    this._cc = ref.offset(4, 0xccL).cast(IntRef::new);
    this._d0 = ref.offset(4, 0xd0L).cast(IntRef::new);
    this._d4 = ref.offset(4, 0xd4L).cast(IntRef::new);
    this._d8 = ref.offset(4, 0xd8L).cast(IntRef::new);
    this._dc = ref.offset(4, 0xdcL).cast(IntRef::new);
    this._e0 = ref.offset(4, 0xe0L).cast(IntRef::new);
    this._e4 = ref.offset(4, 0xe4L).cast(IntRef::new);
    this._e8 = ref.offset(4, 0xe8L).cast(IntRef::new);
    this._ec = ref.offset(4, 0xecL).cast(IntRef::new);
    this._f0 = ref.offset(4, 0xf0L).cast(IntRef::new);
    this._f4 = ref.offset(4, 0xf4L).cast(IntRef::new);
    this.typePtr_f8 = ref.offset(4, 0xf8L).cast(Pointer.deferred(4, CString::new));
    this.ui_fc = ref.offset(4, 0xfcL).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
