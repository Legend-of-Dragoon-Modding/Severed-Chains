package legend.game.modding.events.characters;

import legend.game.characters.CharacterAdditionInfo;
import legend.game.characters.CharacterData2c;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

public class PostAdditionLevelUpEvent extends Event {
  public final CharacterData2c charData;
  public final RegistryId additionId;
  public final CharacterAdditionInfo additionInfo;

  public PostAdditionLevelUpEvent(final CharacterData2c charData, final RegistryId additionId, final CharacterAdditionInfo additionInfo) {
    this.charData = charData;
    this.additionId = additionId;
    this.additionInfo = additionInfo;
  }
}