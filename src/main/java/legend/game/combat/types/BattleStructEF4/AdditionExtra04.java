package legend.game.combat.types.BattleStructEF4;

import legend.game.combat.AutoAdditionMode;
import legend.game.combat.bobj.BattleObject27c;
import legend.game.modding.coremod.CoreMod;
import legend.game.scripting.ScriptState;

import static legend.core.GameEngine.CONFIG;
import static legend.game.Scus94491BpeSegment_8006._8006e398;

/** One for each ally and enemy */
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
    int ultimateWargod = 0;
    // This is a cheap way to tell if we're in a combat engine cutscene and turn it off
    if(CONFIG.getConfig(CoreMod.AUTO_ADDITION_CONFIG.get()) == AutoAdditionMode.ON) {
      final ScriptState<? extends BattleObject27c> combatant = _8006e398.allBobjs_e0c[this.index];

      if(combatant != null && (combatant.storage_44[7] & 0x4) == 0) {
        boolean enemyAlive = false;
        for(int i = 0; i < _8006e398.aliveMonsterBobjs_ebc.length; i++) {
          if(_8006e398.aliveMonsterBobjs_ebc[i] != null && _8006e398.aliveMonsterBobjs_ebc[i].innerStruct_00 != null && _8006e398.aliveMonsterBobjs_ebc[i].innerStruct_00.hp_08 != 0) {
            enemyAlive = true;
            break;
          }
        }
        if(enemyAlive) {
          ultimateWargod = 0x6;
        }
      }
    }
    return (this.unknown_01 & 0xff_ffff) << 8 | this.flag_00 & 0xff | ultimateWargod;
  }

  public void unpack(final int val) {
    this.flag_00 = val & 0xff;
    this.unknown_01 = val >>> 8 & 0xff_ffff;
  }
}
