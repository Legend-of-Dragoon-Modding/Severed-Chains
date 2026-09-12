package legend.game.wmap.world;

import legend.core.gte.MV;

/** Frame-local scene and vanilla model root transform, including scale and route visual offsets. The transform is a copy. */
public record WorldMapAvatarContext(WorldMapRenderContext world, MV transform, Motion motion) {
  public enum Motion {
    IDLE,
    WALK,
    RUN,
  }
}
