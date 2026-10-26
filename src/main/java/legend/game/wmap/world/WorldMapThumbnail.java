package legend.game.wmap.world;

import legend.game.tim.Tim;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/** Reusable location-prompt thumbnail backed by retail data, a preset asset, or a mod provider. */
public record WorldMapThumbnail(int nativeIndex, @Nullable String asset, @Nullable String label, @Nullable Supplier<Tim> provider) {
  public WorldMapThumbnail(final int nativeIndex) {
    this(nativeIndex, null, null, null);
  }

  public WorldMapThumbnail(final String asset) {
    this(-1, asset, null, null);
  }

  public WorldMapThumbnail(final Supplier<Tim> provider) {
    this(-1, null, null, provider);
  }

  public WorldMapThumbnail {
    final int sources = (nativeIndex >= 0 ? 1 : 0) + (asset != null ? 1 : 0) + (provider != null ? 1 : 0);
    if(sources != 1) {
      throw new IllegalArgumentException("WMAP thumbnail requires exactly one native index, preset asset, or provider");
    }
  }
}
