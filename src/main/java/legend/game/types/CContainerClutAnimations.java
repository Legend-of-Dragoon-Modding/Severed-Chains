package legend.game.types;

import legend.game.unpacker.FileData;

import java.util.Arrays;

public class CContainerClutAnimations {
  public final ClutAnimation[] clutAnimation_00 = new ClutAnimation[4];

  public CContainerClutAnimations(final FileData data) {
    Arrays.setAll(this.clutAnimation_00, i -> new ClutAnimation(data.slice(data.readInt(i * 0x4))));
  }
}
