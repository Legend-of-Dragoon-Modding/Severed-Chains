package legend.game.submap;

import legend.game.types.NewRootStruct;
import org.joml.Vector2f;

@FunctionalInterface
public interface SubmapLoader<T extends Submap> {
  T make(final SMap smap, final NewRootStruct newRoot, final Vector2f screenOffset, final CollisionGeometry collisionGeometry);
}
