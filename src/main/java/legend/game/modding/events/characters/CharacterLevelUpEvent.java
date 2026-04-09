package legend.game.modding.events.characters;

import legend.game.types.CharacterData2c;
import org.legendofdragoon.modloader.events.Event;

public class CharacterLevelUpEvent extends Event {
  public final CharacterData2c charData;
  public final int charId;

  public CharacterLevelUpEvent(final int charId, final CharacterData2c charData) {
    this.charData = charData;
    this.charId = charId;
  }
}
