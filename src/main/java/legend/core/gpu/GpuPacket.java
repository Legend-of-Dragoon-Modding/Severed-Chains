package legend.core.gpu;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

import java.util.function.Consumer;

public class GpuPacket implements MemoryRef {
  private final Value ref;

  public GpuPacket(final Value ref) {
    this.ref = ref.offset(4, 0x0L);
  }

  public void build(final Consumer<Builder> callback) {
    final Builder builder = new Builder();
    callback.accept(builder);

    this.ref.setu((builder.size & 0xffL) << 24 | builder.next & 0xff_ffffL);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }

  public class Builder {
    private int size;
    private int next = 0xff_ffff;

    public Builder add(final long command) {
      GpuPacket.this.ref.offset(4, (this.size + 1) * 0x4L).setu(command);
      this.size++;
      return this;
    }

    public Builder next(final GpuPacket next) {
      this.next = (int)(next.getAddress() & 0xff_ffffL);
      return this;
    }
  }
}
