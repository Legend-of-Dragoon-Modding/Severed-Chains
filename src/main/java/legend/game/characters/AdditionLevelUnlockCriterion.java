package legend.game.characters;

public class AdditionLevelUnlockCriterion implements AdditionUnlockCriterion {
  private final int level;

  public AdditionLevelUnlockCriterion(final int level) {
    this.level = level;
  }

  @Override
  public boolean isUnlocked(final CharacterData2c character, final CharacterAdditionInfo additionInfo) {
    return character.level_12 >= this.level;
  }
}
