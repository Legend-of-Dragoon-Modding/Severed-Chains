package legend.game.modding.events.characters;

import legend.game.additions.Addition;
import legend.game.characters.CharacterAdditionInfo;
import legend.game.characters.CharacterData2c;
import org.legendofdragoon.modloader.events.CancelableEvent;

public class AdditionUnlockEvent extends CancelableEvent {
  public final CharacterData2c charData;
  public final CharacterAdditionInfo additionStats;
  public final Addition addition;

  public AdditionUnlockEvent(final CharacterData2c charData, final CharacterAdditionInfo additionStats, final Addition addition) {
    this.charData = charData;
    this.additionStats = additionStats;
    this.addition = addition;
  }
}
