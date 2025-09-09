package legend.game.combat.types.battlestate;

import legend.game.combat.AdditionMode;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.modding.coremod.CoreMod;
import legend.game.scripting.ScriptState;
import legend.lodmod.LodMod;

import static legend.core.GameEngine.CONFIG;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.combat.bent.BattleEntity27c.FLAG_MONSTER;

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
    if(CONFIG.getConfig(CoreMod.ADDITION_MODE_CONFIG.get()) == AdditionMode.AUTOMATIC) {
      final ScriptState<? extends BattleEntity27c> combatant = battleState_8006e398.allBents_e0c[this.index];

      if(combatant != null && (combatant.storage_44[7] & FLAG_MONSTER) == 0) {
        boolean enemyAlive = false;
        for(int i = 0; i < battleState_8006e398.aliveMonsterBents_ebc.length; i++) {
          if(battleState_8006e398.aliveMonsterBents_ebc[i] != null && battleState_8006e398.aliveMonsterBents_ebc[i].innerStruct_00 != null && battleState_8006e398.aliveMonsterBents_ebc[i].innerStruct_00.stats.getStat(LodMod.HP_STAT.get()).getCurrent() != 0) {
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
