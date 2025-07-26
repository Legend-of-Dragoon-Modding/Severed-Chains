package legend.game;

public enum EngineStateEnum {
  PRELOAD_00,
  UNUSED_01,
  TITLE_02,
  TRANSITION_TO_NEW_GAME_03,
  CREDITS_04,
  SUBMAP_05,
  COMBAT_06,
  GAME_OVER_07,
  WORLD_MAP_08,
  FMV_09,
  DISK_SWAP_10,
  FINAL_FMV_11,
  GAME_CLOSED_12
  ;

  public boolean isInGame() {
    return this.ordinal() >= SUBMAP_05.ordinal();
  }
}
