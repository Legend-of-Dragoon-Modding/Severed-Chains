package legend.game.combat.types.BattleStructEF4;

/**
 * One for each ally and enemy
 */
public class AdditionExtra04 {
  /**
   * <ul
   *   <li>0x01 Destroyer Mace </li>
   *   <li>0x02 Wargod Sash (half damage) </li>
   *   <li>0x06 Ultimate Wargod (full damage) </li>
   * <ul>
   */
  public int flag_00;
  public int unknown_01;

  public int pack() {
    return (this.unknown_01 & 0xff_ffff) << 8 | this.flag_00 & 0xff;
  }

  public void unpack(final int val) {
    this.flag_00 = val & 0xff;
    this.unknown_01 = val >>> 8 & 0xff_ffff;
  }
}
