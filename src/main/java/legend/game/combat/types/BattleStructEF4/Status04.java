package legend.game.combat.types.BattleStructEF4;

/**
 * One for each ally and enemy
 */
public class Status04 {
  /**
   * Bunch of flags. Somehow monster/ally turn matters.
   */
  public int statusEffect_00;
  /**
   * Does not decrease on all statuses
   */
  public int statusTurns_01;

  /** Possibly bool has status */
  public int unknown_02;

  public int pack() {
    return (this.unknown_02 & 0xffff) << 16 | (this.statusTurns_01 & 0xff) << 8 | this.statusEffect_00 & 0xff;
  }

  public void unpack(final int val) {
    this.statusEffect_00 = val & 0xff;
    this.statusTurns_01 = val >>> 8 & 0xff;
    this.unknown_02 = val >>> 16 & 0xffff;
  }
}
