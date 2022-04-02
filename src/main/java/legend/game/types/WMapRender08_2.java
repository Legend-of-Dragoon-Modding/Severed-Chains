package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

//TODO maybe a RECT?
public class WMapRender08_2 implements MemoryRef {
  private final Value ref;



  public WMapRender08_2(final Value ref) {
    this.ref = ref;
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
