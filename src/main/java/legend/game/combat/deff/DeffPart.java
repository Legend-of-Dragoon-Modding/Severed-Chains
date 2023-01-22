package legend.game.combat.deff;

import legend.core.gpu.RECT;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.RelativePointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.types.ExtendedTmd;
import legend.game.types.TmdAnimationFile;

public class DeffPart implements MemoryRef {
  protected final Value ref;

  /**
   * MSB is type, LSB is index, middle bytes are some kind of ID?
   * <ul>
   *   <li>Type 0 - LMB</li>
   *   <li>Type 1 - TMD (animated, optimised)</li>
   *   <li>Type 2 - TMD (animated, unoptimised)</li>
   *   <li>Type 3 - TMD (single object, optimised)</li>
   *   <li>Type 4 - Sprite</li>
   *   <li>Type 5 - CMB</li>
   *   <li>Apparently there is a type 7</li>
   * </ul>
   */
  public final IntRef flags_00;

  public DeffPart(final Value ref) {
    this.ref = ref;

    this.flags_00 = ref.offset(4, 0x00L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }

  public static class LmbType extends DeffPart {
    public final IntRef type_04;
    public final RelativePointer<Lmb> lmb_08;

    public LmbType(final Value ref) {
      super(ref);

      this.type_04 = ref.offset(4, 0x04L).cast(IntRef::new);
      this.lmb_08 = ref.offset(4, 0x08L).cast(RelativePointer.deferred(4, ref.getAddress(), value ->
        switch(this.type_04.get()) {
          case 0 -> new LmbType0(value);
          case 1 -> new LmbType1(value);
          case 2 -> new LmbType2(value);
          default -> throw new RuntimeException("Unsupported LMB type");
        }
      ));
    }
  }

  public static class TmdType extends DeffPart {
    public final RelativePointer<UnboundedArrayRef<TextureInfo>> textureInfo_08;
    public final RelativePointer<ExtendedTmd> tmd_0c;

    public TmdType(final Value ref) {
      super(ref);

      this.textureInfo_08 = ref.offset(4, 0x08L).cast(RelativePointer.deferred(4, ref.getAddress(), UnboundedArrayRef.of(0x10, TextureInfo::new)));
      this.tmd_0c = ref.offset(4, 0x0cL).cast(RelativePointer.deferred(4, ref.getAddress(), ExtendedTmd::new));
    }
  }

  public static class AnimatedTmdType extends TmdType {
    public final RelativePointer<Anim> anim_14;

    public AnimatedTmdType(final Value ref) {
      super(ref);

      this.anim_14 = ref.offset(4, 0x14L).cast(RelativePointer.deferred(4, ref.getAddress(), this::makeAnimType));
    }

    private Anim makeAnimType(final Value value) {
      final int magic = (int)this.ref.offset(4, this.anim_14.getPointer()).get();

      if(magic == Lmb.MAGIC) {
        return new LmbType0(value);
      }

      if(magic == Cmb.MAGIC) {
        return new Cmb(value);
      }

      return new TmdAnimationFile(value);
    }
  }

  public static class TextureInfo implements MemoryRef {
    private final Value ref;

    public final RECT vramPos_00;

    public TextureInfo(final Value ref) {
      this.ref = ref;

      this.vramPos_00 = ref.offset(2, 0x00L).cast(RECT::new);
    }

    @Override
    public long getAddress() {
      return this.ref.getAddress();
    }
  }

  public static class SpriteType extends DeffPart {
    public final RelativePointer<SpriteMetrics> metrics_08;

    public SpriteType(final Value ref) {
      super(ref);

      this.metrics_08 = ref.offset(4, 0x08L).cast(RelativePointer.deferred(4, ref.getAddress(), SpriteMetrics::new));
    }
  }

  public static class SpriteMetrics implements MemoryRef {
    private final Value ref;

    public final UnsignedShortRef u_00;
    public final UnsignedShortRef v_02;
    public final UnsignedShortRef w_04;
    public final UnsignedShortRef h_06;
    public final UnsignedShortRef clutX_08;
    public final UnsignedShortRef clutY_0a;

    public SpriteMetrics(final Value ref) {
      this.ref = ref;

      this.u_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
      this.v_02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
      this.w_04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
      this.h_06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
      this.clutX_08 = ref.offset(2, 0x08L).cast(UnsignedShortRef::new);
      this.clutY_0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);
    }

    @Override
    public long getAddress() {
      return this.ref.getAddress();
    }
  }

  public static class CmbType extends DeffPart {
    public final RelativePointer<Cmb> cmb_14;

    public CmbType(final Value ref) {
      super(ref);

      this.cmb_14 = ref.offset(4, 0x14L).cast(RelativePointer.deferred(4, ref.getAddress(), Cmb::new));
    }
  }
}
