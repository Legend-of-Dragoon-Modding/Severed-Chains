package legend.game.wmap.world;

import javax.annotation.Nullable;

/** Native sound-file index used while a location prompt is open. */
public record WorldMapSound(int nativeIndex, @Nullable String label) {
  public WorldMapSound(final int nativeIndex) {
    this(nativeIndex, null);
  }
  public WorldMapSound {
    if(nativeIndex <= 0) {
      throw new IllegalArgumentException("WMAP sound native index must be positive");
    }
  }
}
