package legend.game.modding.events.characters;

import legend.game.additions.Addition;
import legend.game.characters.CharacterData2c;
import org.legendofdragoon.modloader.events.Event;

public class AdditionLevelUpEvent extends Event {
  public final CharacterData2c charData;
  public final Addition addition;
  public final int additionLevel;

  public AdditionLevelUpEvent(final CharacterData2c charData, final Addition addition, final int additionLevel) {
    this.charData = charData;
    this.addition = addition;
    this.additionLevel = additionLevel;
  }
}