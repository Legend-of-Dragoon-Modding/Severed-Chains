package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.types.TmdAnimationFile;

/** A union type, see type property to tell which type it is */
public class CombatantStruct1a8_c implements MemoryRef {
  private final Value ref;

  public final Type0 type0;
  public final AnimType type1_2;
  public final IndexType type3;
  public final BpeType type4_5;
  public final TimType type6;

  public final ShortRef BttlStruct08_index_04;
  public final ShortRef BattleStructEf4Sub08_index_06;
  public final ByteRef _08;
  public final ByteRef _09;
  /**
   * <ol start="0">
   *  <li>None</li>
   *  <li>Anim file</li>
   *  <li>Anim file</li>
   *  <li>Index</li>
   *  <li>BPE version of 1</li>
   *  <li>BPE version of 2</li>
   *  <li>TIM</li>
   * </ol>
   */
  public final UnsignedByteRef type_0a;
  public final UnsignedByteRef _0b;

  public CombatantStruct1a8_c(final Value ref) {
    this.ref = ref;

    this.type0 = new Type0();
    this.type1_2 = new AnimType();
    this.type3 = new IndexType();
    this.type4_5 = new BpeType();
    this.type6 = new TimType();

    this.BttlStruct08_index_04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this.BattleStructEf4Sub08_index_06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this._08 = ref.offset(1, 0x08L).cast(ByteRef::new);
    this._09 = ref.offset(1, 0x09L).cast(ByteRef::new);
    this.type_0a = ref.offset(1, 0x0aL).cast(UnsignedByteRef::new);
    this._0b = ref.offset(1, 0x0bL).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }

  public class Type0 {
    public final IntRef _00;

    private Type0() {
      this._00 = CombatantStruct1a8_c.this.ref.offset(4, 0x00L).cast(IntRef::new);
    }
  }

  public class AnimType {
    public final Pointer<TmdAnimationFile> anim_00;

    private AnimType() {
      this.anim_00 = CombatantStruct1a8_c.this.ref.offset(4, 0x00L).cast(Pointer.deferred(4, TmdAnimationFile::new));
    }
  }

  public class IndexType {
    public final IntRef index_00;

    private IndexType() {
      this.index_00 = CombatantStruct1a8_c.this.ref.offset(4, 0x00L).cast(IntRef::new);
    }
  }

  public class BpeType {
    public final UnsignedIntRef bpe_00;

    private BpeType() {
      this.bpe_00 = CombatantStruct1a8_c.this.ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    }
  }

  public class TimType {
    public final UnsignedShortRef x_00;
    public final UnsignedByteRef y_02;
    public final UnsignedByteRef h_03;

    private TimType() {
      this.x_00 = CombatantStruct1a8_c.this.ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
      this.y_02 = CombatantStruct1a8_c.this.ref.offset(1, 0x02L).cast(UnsignedByteRef::new);
      this.h_03 = CombatantStruct1a8_c.this.ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    }
  }
}
