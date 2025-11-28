package legend.game.additions;

public class CharacterAdditionStats {
  public boolean unlocked;
  public int level;
  public int xp;

  public CharacterAdditionStats() {
  }

  public CharacterAdditionStats(final CharacterAdditionStats other) {
    this.unlocked = other.unlocked;
    this.level = other.level;
    this.xp = other.xp;
  }
}
