package legend.game.submap;

import legend.game.types.NewRootStruct;
import org.joml.Vector2f;

/** Resources owned by SMap and shared with the active submap. */
public record SubmapLoadingContext(SMap smap, int legacyCut, NewRootStruct newRoot, Vector2f screenOffset, CollisionGeometry collisionGeometry) { }
