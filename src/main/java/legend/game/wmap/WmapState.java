package legend.game.wmap;

public enum WmapState {
  INIT_0(0),
  WAIT_FOR_MUSIC_TO_LOAD_1(1),
  INIT2_2(2),
  PLAY_3(3),
  TRANSITION_TO_SCREENS_4(4),
  RENDER_SCREENS_5(5),
  EXIT_SCREENS_6(6),
  TRANSITION_TO_SUBMAP_7(7),
  TRANSITION_TO_BATTLE_8(8),
  TRANSITION_TO_WORLD_MAP_9(9),
  UNUSED_DEALLOCATOR_10(10),
  NOOP_11(11),
  PRE_EXIT_SCREENS_12(12),
  TRANSITION_TO_TITLE_13(13),
  LOAD_BACKGROUND_OBJ_14(14),
  ;
  
  public final int state;
  
  WmapState(final int state) {
    this.state = state;
  }
}
