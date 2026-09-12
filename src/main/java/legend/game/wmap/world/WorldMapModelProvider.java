package legend.game.wmap.world;

import legend.game.types.GameState52c;

import java.util.concurrent.CompletableFuture;

/** Loads CPU assets only. The WMAP render thread adopts textures and creates renderers. */
@FunctionalInterface
public interface WorldMapModelProvider {
  /**
   * CPU data only; return promptly with a future. WMAP allows 60 seconds for assets and any native
   * transform anchor, then fails with the region ID. Unload discards the result without adopting
   * GPU resources; it does not promise to stop the provider's background work or shared future.
   */
  CompletableFuture<WorldMapModelAssets> load(GameState52c gameState);
}
