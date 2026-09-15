package legend.game.submap;

import legend.core.tags.Tag;
import org.legendofdragoon.modloader.registries.RegistryEntry;

/** Creates a submap; the instance owns its resources and opaque spawn/save payload. */
public abstract class SubmapProvider extends RegistryEntry {
  public abstract Submap create(final SubmapLoadingContext context, final Tag data);
}
