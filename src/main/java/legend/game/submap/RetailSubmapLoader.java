package legend.game.submap;

import legend.game.types.NewRootStruct;
import org.joml.Vector2f;

import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;

public class RetailSubmapLoader implements SubmapLoader<RetailSubmap> {
  @Override
  public RetailSubmap make(final SMap smap, final NewRootStruct newRoot, final Vector2f screenOffset, final CollisionGeometry collisionGeometry) {
    return new RetailSubmap(smap, submapCut_80052c30, newRoot, screenOffset, collisionGeometry);
  }
}
