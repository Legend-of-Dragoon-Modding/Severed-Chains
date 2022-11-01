package legend.core.gpu;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class DRAWENV implements MemoryRef {
  private final Value ref;

  /**
   * 0x0 Drawing area<br>
   * 0x0 x<br>
   * 0x2 y<br>
   * 0x4 w<br>
   * 0x6 h
   */
  public final legend.core.gpu.RECT clip;
  /**
   * 0x8 Drawing offset<br>
   * 0x8 offset 1<br>
   * 0xa offset 2<br>
   */
  public final ArrayRef<ShortRef> ofs;
  /**
   * 0xc Texture window<br>
   * 0x0c x<br>
   * 0x0e y<br>
   * 0x10 w<br>
   * 0x12 h
   */
  public final legend.core.gpu.RECT tw;
  /**
   * 0x14 Texture page (tp, abr, tx, ty)
   */
  public final ShortRef tpage;

  /**
   * 0x17 Permission flag for drawing (1 = drawing on display area is inhibited)
   */
  public final ByteRef dfe;
  /**
   * 0x18 Draw area clear flag
   */
  public final ByteRef isbg;
  /**
   * 0x19
   */
  public final ByteRef r0;
  /**
   * 0x1a
   */
  public final ByteRef g0;
  /**
   * 0x1b
   */
  public final ByteRef b0;
  /**
   * 0x1c
   */
  public final legend.core.gpu.DR_ENV dr_env;

  public DRAWENV(final Value ref) {
    this.ref = ref;

    this.clip = new legend.core.gpu.RECT(ref.offset(2, 0x0L));
    this.ofs = ref.offset(2, 0x8L).cast(ArrayRef.of(ShortRef.class, 2, 2, ShortRef::new));
    this.tw = new RECT(ref.offset(2, 0xcL));
    this.tpage = new ShortRef(ref.offset(2, 0x14L));
    this.dfe = new ByteRef(ref.offset(1, 0x17L));
    this.isbg = new ByteRef(ref.offset(1, 0x18L));
    this.r0 = new ByteRef(ref.offset(1, 0x19L));
    this.g0 = new ByteRef(ref.offset(1, 0x1aL));
    this.b0 = new ByteRef(ref.offset(1, 0x1bL));
    this.dr_env = new DR_ENV(ref.offset(4, 0x1cL));
  }

  public DRAWENV set(final DRAWENV other) {
    this.clip.set(other.clip);
    this.ofs.get(0).set(other.ofs.get(0));
    this.ofs.get(1).set(other.ofs.get(1));
    this.tw.set(other.tw);
    this.tpage.set(other.tpage);
    this.dfe.set(other.dfe);
    this.isbg.set(other.isbg);
    this.r0.set(other.r0);
    this.g0.set(other.g0);
    this.b0.set(other.b0);
    this.dr_env.set(other.dr_env);
    return this;
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
