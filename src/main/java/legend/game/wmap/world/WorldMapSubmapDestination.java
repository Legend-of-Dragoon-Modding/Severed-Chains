package legend.game.wmap.world;

import javax.annotation.Nullable;
import legend.core.tags.MapTag;
import legend.core.tags.Tag;
import legend.game.EngineDestination;
import legend.game.submap.RetailSubmapProvider;
import org.legendofdragoon.modloader.registries.RegistryId;

/** Reusable named submap endpoint for world-map arrival and departure. */
public record WorldMapSubmapDestination(int cut, int scene, @Nullable String label, @Nullable RegistryId provider, @Nullable Tag data) {
  public WorldMapSubmapDestination {
    if(provider != null && (cut < 2 || cut >= 0x800)) throw new IllegalArgumentException("Custom submap destination requires a retail fallback cut");
    data = data == null ? null : data.clone();
  }

  public WorldMapSubmapDestination(final int cut, final int scene, @Nullable final String label) {
    this(cut, scene, label, null, null);
  }

  public WorldMapSubmapDestination(final RegistryId provider, final Tag data, final SubmapEndpoint fallback) {
    this(fallback.cut(), fallback.scene(), null, provider, data);
  }

  @Override
  public Tag data() {
    return this.data == null ? null : this.data.clone();
  }

  public EngineDestination engineDestination() {
    if(this.provider == null && this.data == null) return EngineDestination.submap(this.endpoint());
    return EngineDestination.submap(this.provider == null ? RetailSubmapProvider.ID : this.provider, this.data == null ? new MapTag() : this.data, this.endpoint());
  }
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
