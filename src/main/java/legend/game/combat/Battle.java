package legend.game.combat;

import legend.core.memory.Method;
import legend.game.EngineState;

import static legend.game.Scus94491BpeSegment.checkIfCharacterAndMonsterModelsAreLoadedAndCacheLivingBents;
import static legend.game.Scus94491BpeSegment.renderBattleEnvironment;
import static legend.game.Scus94491BpeSegment_8004.battleLoadingStage_8004f5d4;
import static legend.game.Scus94491BpeSegment_800b.battleLoaded_800bc94c;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;

public class Battle extends EngineState {
  @Override
  @Method(0x800186a0L)
  public void tick() {
    if(battleLoaded_800bc94c) {
      checkIfCharacterAndMonsterModelsAreLoadedAndCacheLivingBents();
      battleLoadingStage_8004f5d4[pregameLoadingStage_800bb10c.get()].run();

      if(battleLoaded_800bc94c) {
        renderBattleEnvironment();
      }
    } else {
      //LAB_8001870c
      battleLoadingStage_8004f5d4[pregameLoadingStage_800bb10c.get()].run();
    }

    //LAB_80018734
  }
}
