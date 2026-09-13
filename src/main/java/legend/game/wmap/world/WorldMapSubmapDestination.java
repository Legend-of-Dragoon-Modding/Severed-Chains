package legend.game.wmap.world;

import javax.annotation.Nullable;

/** Reusable named submap endpoint for world-map arrival and departure. */
public record WorldMapSubmapDestination(int cut, int scene, @Nullable String label) {
  public WorldMapSubmapDestination(final SubmapEndpoint endpoint) {
    this(endpoint.cut(), endpoint.scene(), null);
  }

  public WorldMapSubmapDestination(final int cut, final int scene) {
    this(cut, scene, null);
  }

  public SubmapEndpoint endpoint() {
    return new SubmapEndpoint(this.cut, this.scene);
  }
}
