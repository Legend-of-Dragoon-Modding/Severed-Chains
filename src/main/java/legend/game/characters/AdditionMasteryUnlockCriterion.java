package legend.game.characters;

import legend.game.additions.Addition;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.core.GameEngine.REGISTRIES;

public class AdditionMasteryUnlockCriterion implements AdditionUnlockCriterion {
  @Override
  public boolean isUnlocked(final CharacterData2c character, final CharacterAdditionInfo additionInfo) {
    for(final RegistryId id : character.getAllAdditions()) {
      final CharacterAdditionInfo info = character.getAdditionInfo(id);
      final Addition addition = REGISTRIES.additions.getEntry(id).get();

      if(addition.countsTowardsMastery(character, info) && !addition.isComplete(character, info)) {
        return false;
      }
    }

    return true;
  }
}
