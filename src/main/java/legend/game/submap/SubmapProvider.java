package legend.game.submap;

import legend.core.tags.Tag;
import org.legendofdragoon.modloader.registries.RegistryEntry;

/** Creates a submap; the instance owns its resources and opaque spawn/save payload. */
public abstract class SubmapProvider extends RegistryEntry {
  /** Custom providers can own all bootstrap assets; retail remains the compatibility default. */
  public boolean retailBootstrap() {
    return true;
  }

  public abstract Submap create(final SubmapLoadingContext context, final Tag data);
}
