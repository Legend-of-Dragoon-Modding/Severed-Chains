package legend.game.scripting;

import org.legendofdragoon.modloader.registries.RegistryId;

public class ScriptInlineRegistryIdParam extends Param {
  private final ScriptState<?> state;
  private final int offset;
  private final int length;

  public ScriptInlineRegistryIdParam(final ScriptState<?> state, final int offset, final int length) {
    this.state = state;
    this.offset = offset;
    this.length = length;
  }

  @Override
  public int get() {
    throw new IllegalStateException(this.getClass().getSimpleName() + " can only store registry IDs");
  }

  @Override
  public Param set(final int val) {
    throw new IllegalStateException(this.getClass().getSimpleName() + " can only store registry IDs");
  }

  @Override
  public Param array(final int index) {
    throw new IllegalStateException(this.getClass().getSimpleName() + " can only store registry IDs");
  }

  @Override
  public boolean isRegistryId() {
    return true;
  }

  @Override
  public RegistryId getRegistryId() {
    final char[] chars = new char[this.length];
    for(int i = 0; i < this.length; i++) {
      chars[i] = (char)(this.state.scriptPtr_14.getOp(this.offset + i / 4) >>> i % 4 * 8 & 0xff);
    }

    return new RegistryId(new String(chars));
  }

  @Override
  public Param set(final RegistryId id) {
    throw new IllegalStateException(this.getClass().getSimpleName() + " is immutable");
  }
}
