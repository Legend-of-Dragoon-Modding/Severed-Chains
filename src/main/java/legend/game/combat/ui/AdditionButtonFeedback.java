package legend.game.combat.ui;

public enum AdditionButtonFeedback {
  WRONG(),
  COUNTER(),
  LATE(),
  EARLY(),
  NO_PRESS(),
  NONE(),
  GOOD(),
  GOOD_PLUS(),
  GOOD_MINUS(),
  PERFECT(),
  FLAWLESS();

  public boolean isBadInput() {
    return this == AdditionButtonFeedback.WRONG ||
           this == AdditionButtonFeedback.LATE ||
           this == AdditionButtonFeedback.EARLY ||
           this == AdditionButtonFeedback.COUNTER ||
           this == AdditionButtonFeedback.NO_PRESS;
  }
}
