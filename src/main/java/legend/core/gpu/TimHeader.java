package legend.core.gpu;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

import javax.annotation.Nullable;

public class TimHeader implements MemoryRef {
  @Nullable
  private final Value ref;

  /** 0x0 */
  public final UnsignedIntRef flags;
  /** 0x4 */
  public final RECT imageRect;
  /** 0xc */
  public final UnsignedIntRef imageAddress;
  /** 0x10 */
  public final RECT clutRect;
  /** 0x18 */
  public final UnsignedIntRef clutAddress;

  public TimHeader() {
    this.ref = null;

    this.flags = new UnsignedIntRef();
    this.imageRect = new RECT();
    this.imageAddress = new UnsignedIntRef();
    this.clutRect = new RECT();
    this.clutAddress = new UnsignedIntRef();
  }

  public TimHeader(final Value ref) {
    this.ref = ref;

    this.flags = new UnsignedIntRef(ref.offset(4, 0x0L));
    this.imageRect = new RECT(ref.offset(8, 0x4L));
    this.imageAddress = new UnsignedIntRef(ref.offset(4, 0xcL));
    this.clutRect = new RECT(ref.offset(8, 0x10L));
    this.clutAddress = new UnsignedIntRef(ref.offset(4, 0x18L));
  }

  public legend.core.gpu.Bpp getBpp() {
    return switch((int)(this.flags.get() & 0b111)) {
      case 0 -> legend.core.gpu.Bpp.BITS_4;
      case 1 -> legend.core.gpu.Bpp.BITS_8;
      case 2 -> legend.core.gpu.Bpp.BITS_15;
      case 3 -> Bpp.BITS_24;
      default -> throw new RuntimeException("Unsupported mixed bpp");
    };
  }

  public void set(final TimHeader other) {
    this.flags.set(other.flags);
    this.imageRect.set(other.imageRect);
    this.imageAddress.set(other.imageAddress);
    this.clutRect.set(other.clutRect);
    this.clutAddress.set(other.clutAddress);
  }

  public void setImage(final RECT rect, final long address) {
    this.imageRect.set(rect);
    this.imageAddress.set(address);
  }

  public void setClut(final RECT rect, final long address) {
    this.clutRect.set(rect);
    this.clutAddress.set(address);
  }

  public boolean hasClut() {
    return this.clutAddress.get() != 0;
  }

  public RECT getImageRect() {
    return this.imageRect;
  }

  public long getImageAddress() {
    return this.imageAddress.get();
  }

  public RECT getClutRect() {
    return this.clutRect;
  }

  public long getClutAddress() {
    return this.clutAddress.get();
  }

  @Override
  public long getAddress() {
    if(this.ref != null) {
      return this.ref.getAddress();
    }

    return 0;
  }
}
