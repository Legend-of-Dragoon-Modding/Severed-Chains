package legend.game.characters;

import static legend.game.characters.CharacterData2c.HAS_ULTIMATE_ADDITION;

public class AdditionMasteryUnlockCriterion implements AdditionUnlockCriterion {
  @Override
  public boolean isUnlocked(final CharacterData2c character, final CharacterAdditionInfo additionInfo) {
    return (character.partyFlags_04 & HAS_ULTIMATE_ADDITION) != 0;
  }
}
