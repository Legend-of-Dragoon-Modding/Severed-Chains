package legend.game.characters;

import legend.game.additions.UnlockState;

import java.util.ArrayList;
import java.util.List;

public class CharacterAdditionInfo {
  private final List<AdditionUnlockCriterion> unlockCriteria = new ArrayList<>();

  private UnlockState unlockState = UnlockState.UNLOCKABLE;
  private int unlockTimestamp = -1;
  public int level;
  public int xp;

  public CharacterAdditionInfo(final List<AdditionUnlockCriterion> unlockCriteria) {
    this.unlockCriteria.addAll(unlockCriteria);
  }

  public CharacterAdditionInfo(final List<AdditionUnlockCriterion> unlockCriteria, final UnlockState unlockState, final int unlockTimestamp, final int level, final int xp) {
    this.unlockCriteria.addAll(unlockCriteria);
    this.unlockState = unlockState;
    this.unlockTimestamp = unlockTimestamp;
    this.level = level;
    this.xp = xp;
  }

  public CharacterAdditionInfo(final CharacterAdditionInfo other) {
    this.unlockCriteria.addAll(other.unlockCriteria);
    this.unlockState = other.unlockState;
    this.unlockTimestamp = other.unlockTimestamp;
    this.level = other.level;
    this.xp = other.xp;
  }

  public void setUnlockState(final UnlockState unlockState, final int timestamp) {
    this.unlockState = unlockState;

    if(unlockState.isUsable()) {
      this.unlockTimestamp = timestamp;
    } else {
      this.unlockTimestamp = -1;
    }
  }

  public UnlockState getUnlockState() {
    return this.unlockState;
  }

  public int getUnlockTimestamp() {
    return this.unlockTimestamp;
  }

  public void unlock(final int timestamp) {
    this.unlockState = UnlockState.UNLOCKED;
    this.unlockTimestamp = timestamp;
  }

  /**
   * @return True if unlockable and meets the unlock criteria. False if already unlocked, is not unlockable, or does not meet unlock criteria.
   */
  public boolean checkUnlock(final CharacterData2c character) {
    if(!this.unlockState.isUnlockable()) {
      return false;
    }

    for(int i = 0; i < this.unlockCriteria.size(); i++) {
      if(!this.unlockCriteria.get(i).isUnlocked(character, this)) {
        return false;
      }
    }

    return true;
  }
}
