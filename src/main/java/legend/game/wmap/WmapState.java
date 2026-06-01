package legend.game.wmap;

public enum WmapState {
  INIT(0),
  WAIT_FOR_MUSIC_TO_LOAD(1),
  INIT2(2),
  PLAY(3),
  TRANSITION_TO_SCREENS(4),
  RENDER_SCREENS(5),
  EXIT_SCREENS(6),
  TRANSITION_TO_ENGINE_STATE(7),
  TRANSITION_TO_WORLD_MAP(8),
  UNUSED_DEALLOCATOR(9),
  NOOP(10),
  PRE_EXIT_SCREENS(11),
  TRANSITION_TO_TITLE(12),
  LOAD_BACKGROUND_OBJ(13),
  ;

  public final int state;

  WmapState(final int state) {
    this.state = state;
  }
}
