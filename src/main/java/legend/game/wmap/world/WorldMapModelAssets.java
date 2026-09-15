package legend.game.wmap.world;

import legend.game.tim.Tim;
import legend.game.tmd.TmdWithId;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

/**
 * A TMD map, or an arbitrary renderer. Renderer-only regions use their region scene anchor.
 * Texture coordinates/VRAM placement belong to the provider, as with other SC map assets.
 */
public record WorldMapModelAssets(@Nullable TmdWithId model, List<Tim> textures, @Nullable Supplier<WorldMapModelRenderer> renderer, boolean retailAnimations) {
  public WorldMapModelAssets {
    textures = List.copyOf(textures);
    if(model == null && renderer == null) {
      throw new IllegalArgumentException("World map assets require a TMD or renderer");
    }
  }

  public WorldMapModelAssets(final TmdWithId model, final List<Tim> textures) {
    this(model, textures, null, false);
  }

  public WorldMapModelAssets(@Nullable final TmdWithId model, final List<Tim> textures, @Nullable final Supplier<WorldMapModelRenderer> renderer) {
    this(model, textures, renderer, false);
  }
}
