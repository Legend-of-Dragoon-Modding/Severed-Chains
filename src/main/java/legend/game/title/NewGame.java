package legend.game.title;

import legend.core.memory.Method;
import legend.game.EngineState;
import legend.game.EngineStateEnum;
import legend.game.types.CharacterData2c;

import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.SItem.xpTables;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class NewGame extends EngineState {
  private static final int[] characterStartingLevels = {1, 3, 4, 8, 13, 15, 17, 19, 23};
  private static final int[] startingAddition_800ce758 = {0, 8, -1, 14, 29, 8, 23, 19, -1};

  @Method(0x800c7194L)
  private void setUpNewGameData() {
    gameState_800babc8.charIds_88[0] = 0;
    gameState_800babc8.charIds_88[1] = -1;
    gameState_800babc8.charIds_88[2] = -1;

    //LAB_800c723c
    for(int charIndex = 0; charIndex < 9; charIndex++) {
      final CharacterData2c charData = gameState_800babc8.charData_32c[charIndex];
      final int level = characterStartingLevels[charIndex];
      charData.xp_00 = xpTables[charIndex][level];
      charData.hp_08 = levelStuff_80111cfc[charIndex][level].hp_00;
      charData.mp_0a = magicStuff_80111d20[charIndex][1].mp_00;
      charData.sp_0c = 0;
      charData.dlevelXp_0e = 0;
      charData.status_10 = 0;
      charData.level_12 = level;
      charData.dlevel_13 = 1;

      //LAB_800c7294
      for(int additionIndex = 0; additionIndex < 8; additionIndex++) {
        charData.additionLevels_1a[additionIndex] = 0;
        charData.additionXp_22[additionIndex] = 0;
      }

      charData.additionLevels_1a[0] = 1;

      //LAB_800c72d4
      for(int i = 1; i < level; i++) {
        final int index = levelStuff_80111cfc[charIndex][i].addition_02;

        if(index != -1) {
          final int offset = additionOffsets_8004f5ac[charIndex];
          charData.additionLevels_1a[index - offset] = 1;
        }

        //LAB_800c72fc
      }

      //LAB_800c730c
      charData.selectedAddition_19 = startingAddition_800ce758[charIndex];
    }

    gameState_800babc8.charData_32c[0].partyFlags_04 = 0x3;
  }

  @Override
  @Method(0x800c7424L)
  public void tick() {
    super.tick();

    this.setUpNewGameData();
    engineStateOnceLoaded_8004dd24 = EngineStateEnum.SUBMAP_05;
  }
}
