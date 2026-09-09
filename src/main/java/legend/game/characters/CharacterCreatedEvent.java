package legend.game.characters;

import legend.game.types.GameState52c;
import org.legendofdragoon.modloader.events.Event;

public class CharacterCreatedEvent extends Event {
  public final GameState52c gameState;
  public final CharacterTemplate template;
  public final CharacterData2c character;

  public CharacterCreatedEvent(final GameState52c gameState, final CharacterTemplate template, final CharacterData2c character) {
    this.gameState = gameState;
    this.character = character;
    this.template = template;
  }
}
