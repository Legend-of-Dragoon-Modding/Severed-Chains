package legend.game.characters;

@FunctionalInterface
public interface AdditionUnlockCriterion {
  boolean isUnlocked(final CharacterData2c character, final CharacterAdditionInfo additionInfo);
}
