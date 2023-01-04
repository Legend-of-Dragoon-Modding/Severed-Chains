package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.TriConsumer;

import javax.annotation.Nullable;

/** Holds persistent data for scripts */
public class ScriptState<T extends MemoryRef> {
  public static <T extends MemoryRef> Class<ScriptState<T>> classFor(final Class<T> t) {
    //noinspection unchecked
    return (Class<ScriptState<T>>)(Class<?>)ScriptState.class;
  }

  /** This script's index */
  public final int index;
  public final T innerStruct_00;
  public TriConsumer<Integer, ScriptState<T>, T> ticker_04;
  public TriConsumer<Integer, ScriptState<T>, T> renderer_08;
  public TriConsumer<Integer, ScriptState<T>, T> destructor_0c;
  /** If the callback returns non-zero, it's set to null */
  public Value.TriFunction<Integer, ScriptState<T>, T, Long> tempTicker_10;
  /** Pointer to the script file */
  public ScriptFile scriptPtr_14;
  /** Pointer to the current script command */
  public int offset_18;
  /** Return offset for each stack frame */
  public final int[] callStack_1c = new int[10];
  /**
   * 5 - parent script index
   * 6 - child script index
   */
  public final IntRef[] storage_44;
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
   *   <li>0x40 - ?</li>
   * </ul>
   */
  public final IntRef flags_60; // Note: also contained in previous array
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
  public final IntRef _64;
  /**
   * IsDragoon
   */
  public final IntRef _c0;
  public int scriptIndex_c8;
  public int _cc;
  public int _d0;
  public int _d4;
  public int _d8;
  public int _dc;
  public int _e0;
  public int _e4;
  public int _e8;
  public int _ec;
  public int _f0;
  public int _f4;
  public String type_f8;
  public int ui_fc;

  public ScriptState(final int index, @Nullable final T innerStruct, final IntRef[] storage) {
    this.index = index;
    this.innerStruct_00 = innerStruct;

    this.storage_44 = storage;

    this.flags_60 = this.storage_44[7];
    this._64 = this.storage_44[8];
    this._c0 = this.storage_44[31];
  }
}
