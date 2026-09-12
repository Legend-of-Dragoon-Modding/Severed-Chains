package legend.game.wmap.world;

import legend.game.types.GameState52c;

import java.util.concurrent.CompletableFuture;

/** Loads CPU assets only. The WMAP render thread adopts textures and creates renderers. */
@FunctionalInterface
public interface WorldMapModelProvider {
  CompletableFuture<WorldMapModelAssets> load(GameState52c gameState);
}
