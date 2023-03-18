package legend.game.combat.types.BattleStructEF4;

import legend.core.Config;
import legend.game.combat.types.CombatantStruct1a8;

import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.combat.Bttl_800c.getCombatant;

/**
 * One for each ally and enemy
 */
public class AdditionExtra04 {
  private final int index;

  /**
   * <ul>
   *   <li>0x01 Destroyer Mace </li>
   *   <li>0x02 Wargod Calling (half damage) </li>
   *   <li>0x06 Ultimate Wargod (full damage) </li>
   * </ul>
   */
  public int flag_00;
  public int unknown_01;

  public AdditionExtra04(final int index) {
    this.index = index;
  }

  public int pack() {
    final int ultimateWargod;
    // This is a cheap way to tell if we're in a combat engine cutscene and turn it off
    if(Config.autoAddition() && _8006e398.enemyBobjIndices_ebc[0] != null && _8006e398.enemyBobjIndices_ebc[0].innerStruct_00 != null && _8006e398.enemyBobjIndices_ebc[0].innerStruct_00.hp_08 != 0) {
      final CombatantStruct1a8 combatant = getCombatant(this.index);
      ultimateWargod = combatant != null && combatant.charSlot_19c != -1 ? 0x6 : 0;
    } else {
      ultimateWargod = 0;
    }

    return (this.unknown_01 & 0xff_ffff) << 8 | this.flag_00 & 0xff | ultimateWargod;
  }

  public void unpack(final int val) {
    this.flag_00 = val & 0xff;
    this.unknown_01 = val >>> 8 & 0xff_ffff;
  }
}
