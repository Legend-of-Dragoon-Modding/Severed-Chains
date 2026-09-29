package legend.game.wmap.world;

import legend.core.gte.MV;

import java.util.Objects;

/** Region-owned scene anchor for regions without a retail TMD coordinate tree. */
public record WorldMapScene(MV transform) {
  public WorldMapScene {
    transform = new MV(Objects.requireNonNull(transform, "transform"));
  }

  public static WorldMapScene identity() {
    return new WorldMapScene(new MV());
  }

  @Override
  public MV transform() {
    return new MV(this.transform);
  }
}
