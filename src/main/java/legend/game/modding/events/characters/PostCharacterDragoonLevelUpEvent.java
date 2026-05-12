package legend.game.modding.events.characters;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import legend.game.characters.CharacterData2c;
import legend.game.characters.StatType;
import org.legendofdragoon.modloader.events.CancelableEvent;
import org.legendofdragoon.modloader.events.Event;

public class PostCharacterDragoonLevelUpEvent extends Event {
  public final CharacterData2c character;

  public PostCharacterDragoonLevelUpEvent(final CharacterData2c character) {
    this.character = character;
  }
}
