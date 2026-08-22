package legend.game.modding.events.characters;

import legend.game.characters.CharacterData2c;
import legend.game.characters.LevelUpSource;
import org.legendofdragoon.modloader.events.Event;

/**
 * Fired after a character level-up is applied.
 *
 * <p>The {@link LevelUpSource} stored in {@link #source} is one of:</p>
 * <ul>
 *   <li>{@link LevelUpSource#INITIALIZATION} when building initial or restored character state</li>
 *   <li>{@link LevelUpSource#GAMEPLAY} when progression is earned during gameplay</li>
 *   <li>{@link LevelUpSource#DEBUGGER} when applied through the character debugger</li>
 * </ul>
 */
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
