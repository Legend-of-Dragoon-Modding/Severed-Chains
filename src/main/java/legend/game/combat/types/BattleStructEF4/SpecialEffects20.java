package legend.game.combat.types.BattleStructEF4;

/**
 * One for each ally and enemy
 */
public class SpecialEffects20 {
  public int _00;
  public int _04;
  public int _08;
  public int _0c;
  public int _10;
  public int _14;
  /**
   * <ul>
   *   <li>0x01 Attack</li>
   *   <li>0x02 Guard</li>
   *   <li>0x04 Items</li>
   *   <li>0x08 Escape</li>
   *   <li>0x10 Dragoon</li>
   *   <li>0x20 D-Attack</li>
   *   <li>0x40 Magic</li>
   *   <li>0x80 Special</li>
   * <ul>
   */
  public int menuBlockFlag_18;
  /**
   * Each effect has two bits for up to 3 turns
   * <ul>
   *   <li>0 - 1 Material Shield</li>
   *   <li>2 - 3 Magical Shield</li>
   *   <li>4 - 5 Magic Sig Stone</li>
   *   <li>6 - 7 Charm Potion</li>
   * <ul>
   */
  public int shieldsSigStoneCharmTurns_1c;
  public int pandemoniumTurns_1d;
  public int chargingSpirit_1e;
  public int unknown_1f;
}
