package legend.game.additions;

public enum UnlockState {
  /** This addition is unlockable and will be unlocked automatically when its unlock criteria is met */
  UNLOCKABLE,
  /** This addition is unlocked and usable */
  UNLOCKED,
  /** This addition is locked and will not be available even if its unlock criteria is met */
  LOCKED,
  ;

  public boolean isUnlockable() {
    return this == UNLOCKABLE;
  }

  public boolean isUsable() {
    return this == UNLOCKED;
  }
}
