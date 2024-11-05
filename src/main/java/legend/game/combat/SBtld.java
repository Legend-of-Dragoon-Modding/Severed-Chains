package legend.game.combat;

import legend.core.memory.Method;
import legend.game.combat.environment.EncounterData38;
import legend.game.combat.types.StageDeffThing08;

import static legend.game.Scus94491BpeSegment.battlePreloadedEntities_1f8003f4;
import static legend.game.Scus94491BpeSegment.loadFile;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class SBtld {
  @Method(0x80109250L)
  public static void loadAdditions() {
    //LAB_801092a0
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final int charIndex = gameState_800babc8.charIds_88[charSlot];

      if(charIndex >= 0) {
        int activeAdditionIndex = gameState_800babc8.charData_32c[charIndex].selectedAddition_19;
        if(charIndex == 5) { // Albert
          activeAdditionIndex += 28;
        }

        //LAB_801092dc
        final int activeDragoonAdditionIndex;
        if(charIndex != 0 || (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 == 0) {
          //LAB_80109308
          activeDragoonAdditionIndex = dragoonAdditionIndices_801134e8[charIndex];
        } else {
          activeDragoonAdditionIndex = dragoonAdditionIndices_801134e8[9];
        }

        AdditionConfigs.load();

        //LAB_80109310
        if(activeAdditionIndex >= 0) {
          //LAB_80109320
          battlePreloadedEntities_1f8003f4.additionHits_38[charSlot] = AdditionConfigs.additionHits_8010e658[activeAdditionIndex];
          battlePreloadedEntities_1f8003f4.additionHits_38[charSlot + 3] = AdditionConfigs.additionHits_8010e658[activeDragoonAdditionIndex];
        }
      }

      //LAB_80109340
    }

    loadFile("encounters", file -> battlePreloadedEntities_1f8003f4.encounterData_00 = new EncounterData38(file.getBytes(), encounterId_800bb0f8 * 0x38));
  }

  public static final int[] dragoonAdditionIndices_801134e8 = {7, 13, -1, 18, 35, 41, 28, 22, -1, 42};

  public static final StageDeffThing08[] _8011517c = {
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 2, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(2, 4, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 1, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(4, 2, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(2, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(1, 1, 1),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(4, 0, 0),
    new StageDeffThing08(0, 32768, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(1, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 32768, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 4, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
  };
}
