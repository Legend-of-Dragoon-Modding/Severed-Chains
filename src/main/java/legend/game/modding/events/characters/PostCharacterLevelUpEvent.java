package legend.game.modding.events.characters;

import legend.game.characters.CharacterData2c;
import legend.game.characters.LevelUpSource;
import org.legendofdragoon.modloader.events.Event;

public class PostCharacterLevelUpEvent extends Event {
  public final CharacterData2c character;
  public final LevelUpSource source;

  public PostCharacterLevelUpEvent(final CharacterData2c character) {
    this(character, LevelUpSource.GAMEPLAY);
  }

  public PostCharacterLevelUpEvent(final CharacterData2c character, final LevelUpSource source) {
    this.character = character;
    this.source = source;
  }
}
