package legend.game.wmap.world;

/** Opt-ins for built-in rendering; custom controllers interpret named declarations. */
public record WorldMapPresentationCapabilities(boolean retailLabels, boolean retailWater, boolean retailAvatars) {
  public static final WorldMapPresentationCapabilities NONE = new WorldMapPresentationCapabilities(false, false, false);
}
