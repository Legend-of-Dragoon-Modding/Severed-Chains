package legend.game.combat.deff;

import legend.core.gte.BVEC4;
import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class Cmb extends Anim {
  public static final int MAGIC = 0x2042_4d43;

  public final UnsignedShortRef modelPartCount_0c;
  public final ShortRef _0e;
  public final UnboundedArrayRef<Transforms0c> transforms_10;

  private UnboundedArrayRef<SubTransforms08> subTransforms;

  public Cmb(final Value ref) {
    super(ref);

    this.modelPartCount_0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
    this._0e = ref.offset(2, 0x0eL).cast(ShortRef::new);
    this.transforms_10 = ref.offset(2, 0x10L).cast(UnboundedArrayRef.of(0xc, Transforms0c::new, this.modelPartCount_0c::get));
  }

  public UnboundedArrayRef<SubTransforms08> subTransforms() {
    if(this.subTransforms == null) {
      this.subTransforms = this.ref.offset(2, this.transforms_10.getAddress() - this.getAddress() + this.modelPartCount_0c.get() * 0xc).cast(UnboundedArrayRef.of(0x8, SubTransforms08::new));
    }

    return this.subTransforms;
  }

  public static class Transforms0c implements MemoryRef {
    private final Value ref;

    public final SVECTOR rot_00;
    public final SVECTOR trans_06;

    public Transforms0c() {
      this.ref = null;

      this.rot_00 = new SVECTOR();
      this.trans_06 = new SVECTOR();
    }

    public Transforms0c(final Value ref) {
      this.ref = ref;

      this.rot_00 = ref.offset(2, 0x00L).cast(SVECTOR::new);
      this.trans_06 = ref.offset(2, 0x06L).cast(SVECTOR::new);
    }

    @Override
    public long getAddress() {
      return this.ref.getAddress();
    }
  }

  public static class SubTransforms08 implements MemoryRef {
    private final Value ref;

    public final UnsignedByteRef rotScale_00;
    public final BVEC4 rot_01;
    public final UnsignedByteRef transScale_04;
    public final BVEC4 trans_05;

    public SubTransforms08() {
      this.ref = null;

      this.rotScale_00 = new UnsignedByteRef();
      this.rot_01 = new BVEC4();
      this.transScale_04 = new UnsignedByteRef();
      this.trans_05 = new BVEC4();
    }

    public SubTransforms08(final Value ref) {
      this.ref = ref;

      this.rotScale_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
      this.rot_01 = ref.offset(1, 0x01L).cast(BVEC4::new);
      this.transScale_04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
      this.trans_05 = ref.offset(1, 0x05L).cast(BVEC4::new);
    }

    @Override
    public long getAddress() {
      return this.ref.getAddress();
    }
  }
}
