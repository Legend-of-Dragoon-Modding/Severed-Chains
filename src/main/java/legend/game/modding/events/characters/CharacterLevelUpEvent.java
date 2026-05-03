package legend.game.modding.events.characters;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import legend.game.characters.CharacterData2c;
import legend.game.characters.StatType;
import org.legendofdragoon.modloader.events.CancelableEvent;

public class CharacterLevelUpEvent extends CancelableEvent {
  public final CharacterData2c character;

  public final Object2IntMap<StatType<?>> statsToAdd = new Object2IntOpenHashMap<>();
  public int levelsToAdd = 1;

  public CharacterLevelUpEvent(final CharacterData2c character) {
    this.character = character;
  }
}
