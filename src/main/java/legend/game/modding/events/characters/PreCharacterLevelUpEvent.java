package legend.game.modding.events.characters;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import legend.game.characters.CharacterData2c;
import legend.game.characters.LevelUpSource;
import legend.game.characters.StatType;
import org.legendofdragoon.modloader.events.CancelableEvent;

/**
 * Fired before a character level-up is applied.
 *
 * <p>The {@link LevelUpSource} stored in {@link #source} is one of:</p>
 * <ul>
 *   <li>{@link LevelUpSource#INITIALIZATION} when building initial or restored character state</li>
 *   <li>{@link LevelUpSource#GAMEPLAY} when progression is earned during gameplay</li>
 *   <li>{@link LevelUpSource#DEBUGGER} when applied through the character debugger</li>
 * </ul>
 */
public class PreCharacterLevelUpEvent extends CancelableEvent {
  public final CharacterData2c character;
  public final LevelUpSource source;

  public final Object2IntMap<StatType<?>> statsToAdd = new Object2IntOpenHashMap<>();
  public int levelsToAdd = 1;

  public PreCharacterLevelUpEvent(final CharacterData2c character) {
    this(character, LevelUpSource.GAMEPLAY);
  }

  public PreCharacterLevelUpEvent(final CharacterData2c character, final LevelUpSource source) {
    this.character = character;
    this.source = source;
  }
}
