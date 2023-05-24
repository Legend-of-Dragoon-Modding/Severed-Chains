package legend.game.saves;

public enum ConfigStorageLocation {
  /** Stored per save file (different saves all have different values) */
  SAVE,
  /** Stored per campaign file (every save in the campaign has the same value) */
  CAMPAIGN,
  /** Stored globally (always has the same value) */
  GLOBAL,
}
