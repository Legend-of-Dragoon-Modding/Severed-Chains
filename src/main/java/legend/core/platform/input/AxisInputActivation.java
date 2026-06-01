package legend.core.platform.input;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.FileData;

public class AxisInputActivation extends InputActivation {
  public final InputAxis axis;
  public final InputAxisDirection direction;

  public AxisInputActivation(final InputAxis axis, final InputAxisDirection direction) {
    this.axis = axis;
    this.direction = direction;
  }

  public AxisInputActivation(final FileData in, final IntRef offset) {
    this.axis = InputAxis.valueOf(in.readAscii(offset, 1));
    this.direction = InputAxisDirection.valueOf(in.readAscii(offset, 1));
  }

  @Override
  public boolean isSimilar(final InputActivation other) {
    return other instanceof final AxisInputActivation axis && this.axis == axis.axis;
  }

  @Override
  public void serialize(final FileData out, final IntRef offset) {
    out.writeAscii(offset, "axis", 1);
    out.writeAscii(offset, this.axis.name(), 1);
    out.writeAscii(offset, this.direction.name(), 1);
  }
}
