package legend.core.gpu;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;

public class DISPENV implements MemoryRef {
  private final Value ref;

  /**
   * 0x0 Display area within frame buffer. Width: 256, 320, 384, 512, or 640. Height: 240 or 480.
   * 0x0 x
   * 0x2 y
   * 0x4 w
   * 0x6 h
   */
  public final RECT disp;
  /**
   * 0x8 Output screen display area. It is calculated without regard to the value of disp, using the standard monitor screen upper left-hand point (0, 0) and lower right-hand point (256, 240).
   * 0x8 x
   * 0xa y
   * 0xc w
   * 0xe h
   */
  public final RECT screen;

  /**
   * 0x12 Reserved (NTSC/PAL, value of GsGetWorkBase, temp var for various things)
   */
  public final ByteRef pad0;

  public DISPENV(final Value ref) {
    this.ref = ref;

    this.disp = new legend.core.gpu.RECT(ref.offset(2, 0x0L));
    this.screen = new RECT(ref.offset(2, 0x8L));
    this.pad0 = new ByteRef(ref.offset(1, 0x12L));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
