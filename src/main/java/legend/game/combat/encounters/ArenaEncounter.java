package legend.game.combat.encounters;

import legend.game.combat.Battle;
import legend.lodmod.LodPostBattleActions;

import static legend.game.Audio.loadMusicPackage;

public class ArenaEncounter extends Encounter {
  public ArenaEncounter(final int musicIndex, final int escapeChance, final int playerOpeningCamera, final int monsterOpeningCamera, final int cameraPosIndex0, final int cameraPosIndex1, final int cameraPosIndex2, final int cameraPosIndex3, final int postCombatSubmapCut, final int postCombatSubmapScene, final Monster... monsters) {
    super(musicIndex, escapeChance, playerOpeningCamera, monsterOpeningCamera, cameraPosIndex0, cameraPosIndex1, cameraPosIndex2, cameraPosIndex3, postCombatSubmapCut, postCombatSubmapScene, monsters);
  }

  @Override
  public void onBattleLost(final Battle battle) {
    loadMusicPackage(19);
    battle.postBattleAction_800bc974 = LodPostBattleActions.DIED_IN_ARENA_FIGHT.get().inst();
  }
}
