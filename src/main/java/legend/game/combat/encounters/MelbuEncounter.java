package legend.game.combat.encounters;

import legend.game.combat.Battle;
import legend.game.modding.coremod.CorePostBattleActions;
import org.joml.Vector3f;

public class MelbuEncounter extends PhasedEncounter {
  public MelbuEncounter() {
    super("melbu", 243, 0, 31, 31, 53, 90, 71, 75, 672, 0, new Encounter.Monster(388, new Vector3f(-5376.000000f, 0.000000f, 0.000000f)), new Encounter.Monster(389, new Vector3f(-6016.000000f, 4096.000000f, -2176.000000f)), new Encounter.Monster(389, new Vector3f(-4480.000000f, 4096.000000f, -2176.000000f)), new Encounter.Monster(389, new Vector3f(-6016.000000f, 4096.000000f, 2432.000000f)), new Encounter.Monster(389, new Vector3f(-4480.000000f, 4096.000000f, 2432.000000f)));
  }

  @Override
  public void onBattleWon(final Battle battle) {
    battle.postBattleAction_800bc974 = CorePostBattleActions.PLAY_FMV.get().inst(16);
  }
}
