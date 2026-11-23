package legend.game.combat.ui;

public enum AdditionFeedbackValue {
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
    return this == AdditionFeedbackValue.WRONG ||
           this == AdditionFeedbackValue.LATE ||
           this == AdditionFeedbackValue.EARLY ||
           this == AdditionFeedbackValue.COUNTER ||
           this == AdditionFeedbackValue.NO_PRESS;
  }
}
