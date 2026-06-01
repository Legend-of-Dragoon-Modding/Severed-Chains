package legend.game.submap;

/** Controls whether the in-game save option is enabled or not */
public enum SubmapSavable {
  /** Always savable */
  ALWAYS,
  /** Savable if save anywhere is enabled */
  SAVE_ANYWHERE,
  /** Never savable */
  NEVER,
}
