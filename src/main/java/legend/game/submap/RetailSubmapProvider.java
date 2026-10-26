package legend.game.submap;

import legend.core.tags.Tag;
import org.legendofdragoon.modloader.registries.RegistryId;

/** Adapter for the original cut/scene destination protocol. */
public class RetailSubmapProvider extends SubmapProvider {
  public static final RegistryId ID = new RegistryId("lod", "retail_submap");

  @Override
  public Submap create(final SubmapLoadingContext context, final Tag data) {
    return new RetailSubmap(context.smap(), context.legacyCut(), context.newRoot(), context.screenOffset(), context.collisionGeometry());
  }
}
