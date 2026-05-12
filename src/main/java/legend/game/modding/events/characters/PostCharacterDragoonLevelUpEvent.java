package legend.game.modding.events.characters;

import legend.game.characters.CharacterData2c;
import org.legendofdragoon.modloader.events.Event;

public class PostCharacterDragoonLevelUpEvent extends Event {
  public final CharacterData2c character;

  public PostCharacterDragoonLevelUpEvent(final CharacterData2c character) {
    this.character = character;
  }
}
