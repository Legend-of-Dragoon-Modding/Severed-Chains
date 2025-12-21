package legend.game.modding.events.characters;

import legend.game.additions.Addition;
import legend.game.additions.CharacterAdditionStats;
import legend.game.types.CharacterData2c;
import org.legendofdragoon.modloader.events.Event;

public class AdditionUnlockEvent extends Event {
  public final CharacterData2c charData;
  public final CharacterAdditionStats additionStats;
  public final Addition addition;

  public AdditionUnlockEvent(final CharacterData2c charData, final CharacterAdditionStats additionStats, final Addition addition) {
    this.charData = charData;
    this.additionStats = additionStats;
    this.addition = addition;
  }
}
