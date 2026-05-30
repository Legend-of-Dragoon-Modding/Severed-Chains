package legend.game.modding.events.characters;

import legend.game.characters.CharacterData2c;
import org.legendofdragoon.modloader.events.Event;

public class PostCharacterLevelUpEvent extends Event {
  public final CharacterData2c character;

  public PostCharacterLevelUpEvent(final CharacterData2c character) {
    this.character = character;
  }
}
