package legend.core.platform.input;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.FileData;

public abstract class InputActivation {
  public abstract boolean isSimilar(final InputActivation other);
  public abstract void serialize(final FileData out, final IntRef offset);

  public static InputActivation deserialize(final FileData in, final IntRef offset) {
    final String name = in.readAscii(offset, 1);

    return switch(name) {
      case "button" -> new ButtonInputActivation(in, offset);
      case "axis" -> new AxisInputActivation(in, offset);
      case "key" -> new KeyInputActivation(in, offset);
      case "scancode" -> new ScancodeInputActivation(in, offset);
      default -> throw new IllegalStateException("Unknown input type: " + name);
    };
  }
}
