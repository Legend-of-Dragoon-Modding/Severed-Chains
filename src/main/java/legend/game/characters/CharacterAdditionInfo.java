package legend.game.characters;

import legend.game.additions.UnlockState;

import java.util.ArrayList;
import java.util.List;

public class CharacterAdditionInfo {
  private final List<AdditionUnlockCriterion> unlockCriteria = new ArrayList<>();

  private UnlockState unlockState = UnlockState.UNLOCKABLE;
  private int unlockTimestamp = -1;
  public int level = 1;
  public int xp;

  public CharacterAdditionInfo(final List<AdditionUnlockCriterion> unlockCriteria) {
    this.unlockCriteria.addAll(unlockCriteria);
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

    return this.checkUnlockCriteria(character);
  }

  /**
   * @param character - Character to validate unlock criteria against
   *
   * @return True if all criteria is met, false if not.
   */
  public boolean checkUnlockCriteria(final CharacterData2c character) {
    return this.unlockCriteria.stream().allMatch(uc -> uc.isUnlocked(character, this));
  }
}
