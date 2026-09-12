package legend.game.wmap.world;

/** Session policy changes derived access, never saved story flags or travel capabilities. */
public enum WorldMapPolicy {
  STORY,
  OPEN;

  public WorldMapAccess apply(final WorldMapAccess access) {
    if(this == OPEN && access.code() == WorldMapAccess.Code.STORY_LOCKED) {
      return WorldMapAccess.ALLOWED;
    }

    return access;
  }
}
