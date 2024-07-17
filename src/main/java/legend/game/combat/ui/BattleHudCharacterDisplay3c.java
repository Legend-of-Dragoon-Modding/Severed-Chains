package legend.game.combat.ui;

public class BattleHudCharacterDisplay3c {
  public int charIndex_00;
  public int charId_02;

  /**
   * 0x1 - render battle UI background
   * 0x2 - render player stats, portrait, name, etc.
   * 0x4 - max SP
   * 0x8 - max SP blinking border (turns on and off each frame)
   */
  public int flags_06;
  public int x_08;
  public int y_0a;

  /** TODO This should be another struct I think */
  public final int[] _14 = new int[10];
}
