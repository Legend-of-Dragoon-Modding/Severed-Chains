package legend.game.combat.encounters;

import static legend.game.Audio.loadBattlePhaseSounds;

public class PhasedEncounter extends Encounter {
  public final String phaseDirectoryName;

  public PhasedEncounter(final String phaseDirectoryName, final int musicIndex, final int escapeChance, final int playerOpeningCamera, final int monsterOpeningCamera, final int cameraPosIndex0, final int cameraPosIndex1, final int cameraPosIndex2, final int cameraPosIndex3, final int postCombatSubmapCut, final int postCombatSubmapScene, final Monster... monsters) {
    super(musicIndex, escapeChance, playerOpeningCamera, monsterOpeningCamera, cameraPosIndex0, cameraPosIndex1, cameraPosIndex2, cameraPosIndex3, postCombatSubmapCut, postCombatSubmapScene, monsters);
    this.phaseDirectoryName = phaseDirectoryName;
  }

  @Override
  public void loadSounds(final int phase) {
    loadBattlePhaseSounds("doel", phase);
  }
}
