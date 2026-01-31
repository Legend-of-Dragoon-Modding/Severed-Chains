package legend.game.modding.events.characters;

import legend.game.additions.Addition;
import legend.game.additions.CharacterAdditionStats;
import legend.game.types.CharacterData2c;
import org.legendofdragoon.modloader.events.Event;

import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class AdditionHitMultiplierEvent extends Event {
  public final CharacterData2c charData;
  public final CharacterAdditionStats additionStats;
  public final Addition addition;

  public float additionSpMulti;
  public float additionDmgMulti;

  public AdditionHitMultiplierEvent(final CharacterData2c charData, final CharacterAdditionStats additionStats, final Addition addition) {
    this.charData = charData;
    this.additionStats = additionStats;
    this.addition = addition;

    this.additionSpMulti = addition.getSpMultiplier(gameState_800babc8, charData, additionStats);
    this.additionDmgMulti = addition.getDamageMultiplier(gameState_800babc8, charData, additionStats);
  }
}
