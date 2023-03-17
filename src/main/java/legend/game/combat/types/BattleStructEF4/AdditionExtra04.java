package legend.game.combat.types.BattleStructEF4;

import legend.core.Config;

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
    return (this.unknown_01 & 0xff_ffff) << 8 | this.flag_00 & 0xff | (Config.autoAddition() && this.index < 3 ? 0x6 : 0x0);
  }

  public void unpack(final int val) {
    this.flag_00 = val & 0xff;
    this.unknown_01 = val >>> 8 & 0xff_ffff;
  }
}
