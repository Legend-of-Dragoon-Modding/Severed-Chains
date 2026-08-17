package legend.game.modding.events.characters;

import legend.game.additions.Addition;
import legend.game.characters.CharacterAdditionInfo;
import legend.game.characters.CharacterData2c;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

public class ResolveAdditionEvent extends Event {
  public final CharacterData2c character;
  public final CharacterAdditionInfo additionInfo;
  public final RegistryId additionId;
  public final Addition baseAddition;
  public Addition addition;

  public ResolveAdditionEvent(final CharacterData2c character, final CharacterAdditionInfo additionInfo, final RegistryId additionId, final Addition baseAddition) {
    if(character == null) {
      throw new IllegalArgumentException("Addition character cannot be null");
    }
    if(additionInfo == null) {
      throw new IllegalArgumentException("Addition info cannot be null");
    }
    if(additionId == null) {
      throw new IllegalArgumentException("Addition ID cannot be null");
    }
    if(baseAddition == null) {
      throw new IllegalArgumentException("Base addition cannot be null");
    }

    this.character = character;
    this.additionInfo = additionInfo;
    this.additionId = additionId;
    this.baseAddition = baseAddition;
    this.addition = baseAddition;
  }
}
