package legend.core.platform.input;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.FileData;

public class ButtonInputActivation extends InputActivation {
  public final InputButton button;

  public ButtonInputActivation(final InputButton button) {
    this.button = button;
  }

  public ButtonInputActivation(final FileData in, final IntRef offset) {
    this.button = InputButton.valueOf(in.readAscii(offset, 1));
  }

  @Override
  public boolean isSimilar(final InputActivation other) {
    return other instanceof final ButtonInputActivation button && this.button == button.button;
  }

  @Override
  public void serialize(final FileData out, final IntRef offset) {
    out.writeAscii(offset, "button", 1);
    out.writeAscii(offset, this.button.name(), 1);
  }
}
