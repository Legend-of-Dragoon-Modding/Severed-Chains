package legend.game.combat.deff;

import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.effects.EffectManagerData6cInner;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptState;

public class BattleStruct24_2 {
  /**
   * Flags:
   * <ul>
   *   <li>0x100_0000 - dragoon attack/spell/transformation DEFF</li>
   *   <li>0x200_0000 - item magic DEFF</li>
   *   <li>0x300_0000 - enemy/boss DEFF</li>
   *   <li>0x500_0000 - cutscene DEFF</li>
   * </ul>
   */
  public int type_00;
  public ScriptState<BattleEntity27c> bentState_04;
  public int _08;
  public int scriptIndex_0c;
  public int scriptEntrypoint_10;
  public ScriptFile script_14;
  public ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> managerState_18;
  public boolean init_1c;
}
