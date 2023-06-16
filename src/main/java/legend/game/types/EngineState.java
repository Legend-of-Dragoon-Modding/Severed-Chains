package legend.game.types;

public enum EngineState {
  PRELOAD_00,
  UNUSED_01,
  TITLE_02,
  TRANSITION_TO_NEW_GAME_03,
  THE_END_04,
  SUBMAP_05,
  COMBAT_06,
  GAME_OVER_07,
  WORLD_MAP_08,
  FMV_09,
  DISK_SWAP_10,
  CREDITS_11,
  ;

  public boolean isInGame() {
    return this.ordinal() >= SUBMAP_05.ordinal();
  }
}
