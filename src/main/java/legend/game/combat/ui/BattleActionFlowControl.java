package legend.game.combat.ui;

public enum BattleActionFlowControl {
  /** Keep the player combat script paused while menuing */
  PAUSE_SCRIPT,
  /** Allow the player combat script to continue once menuing is finished */
  CONTINUE_SCRIPT,
  /** Action cannot be used */
  FAIL,
}
