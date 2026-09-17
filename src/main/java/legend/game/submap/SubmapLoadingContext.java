package legend.game.submap;

import legend.game.types.NewRootStruct;
import legend.game.EngineStateLifetime;
import javax.annotation.Nullable;
import org.joml.Vector2f;

/** Resources owned by SMap and shared with the active submap. */
public record SubmapLoadingContext(SMap smap, int legacyCut, @Nullable NewRootStruct newRoot, Vector2f screenOffset, CollisionGeometry collisionGeometry, EngineStateLifetime lifetime) {
  public SubmapLoadingContext(final SMap smap, final int legacyCut, final NewRootStruct newRoot, final Vector2f screenOffset, final CollisionGeometry collisionGeometry) {
    this(smap, legacyCut, newRoot, screenOffset, collisionGeometry, smap.lifetime());
  }
}
