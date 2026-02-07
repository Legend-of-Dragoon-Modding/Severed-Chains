package legend.game.additions;

public class CharacterAdditionStats {
  public UnlockState unlockState = UnlockState.UNLOCKABLE;
  public int level;
  public int xp;

  public CharacterAdditionStats() {
  }

  public CharacterAdditionStats(final CharacterAdditionStats other) {
    this.unlockState = other.unlockState;
    this.level = other.level;
    this.xp = other.xp;
  }
}
