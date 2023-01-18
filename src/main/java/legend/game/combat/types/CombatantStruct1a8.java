package legend.game.combat.types;

import legend.game.types.ExtendedTmd;
import legend.game.types.MrgFile;
import legend.game.scripting.ScriptFile;

/** Data related to a combatant (player or enemy) */
public class CombatantStruct1a8 {
  public MrgFile mrg_00;
  public MrgFile mrg_04;
  public ExtendedTmd tmd_08;

  public ScriptFile scriptPtr_10;
  public final CombatantStruct1a8_c[] _14 = new CombatantStruct1a8_c[32];
  public int xp_194;
  public int gold_196;
  public int itemChance_198;
  public int itemDrop_199;
  public int _19a;
  public int charSlot_19c;
  /**
   * 0x1 - used?
   * 0x4 - player (not NPC)
   */
  public int flags_19e;
  public int colourMap_1a0;
  /**
   * Not just char index
   * <ul>
   *   <li>0x1 - dragoon</li>
   * </ul>
   */
  public int charIndex_1a2;
  public int _1a4;
  public int _1a6;
}
