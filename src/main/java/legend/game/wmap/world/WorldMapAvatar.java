package legend.game.wmap.world;

import legend.game.types.GameState52c;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/** CPU asset provider. Completion is adopted by the map render thread, never by the loader callback. */
public record WorldMapAvatar(Function<GameState52c, CompletableFuture<WorldMapAvatarAssets>> loader) {
  public WorldMapAvatar {
    Objects.requireNonNull(loader, "loader");
  }

  public CompletableFuture<WorldMapAvatarAssets> load(final GameState52c gameState) {
    return Objects.requireNonNull(this.loader.apply(gameState), "World map avatar loader returned null future");
  }
}

