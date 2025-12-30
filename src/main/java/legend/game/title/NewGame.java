package legend.game.title;

import de.jcm.discordgamesdk.activity.Activity;
import legend.core.memory.Method;
import legend.game.EngineState;
import legend.game.EngineStateEnum;
import legend.game.additions.Addition;
import legend.game.additions.CharacterAdditionStats;
import legend.game.types.CharacterData2c;
import legend.game.types.GsRVIEW2;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import static legend.game.EngineStates.engineStateOnceLoaded_8004dd24;
import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.SItem.xpTables;
import static legend.game.Scus94491BpeSegment_8004.CHARACTER_ADDITIONS;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.lodmod.LodAdditions.ALBERT_HARPOON;
import static legend.lodmod.LodAdditions.DOUBLE_PUNCH;
import static legend.lodmod.LodAdditions.DOUBLE_SLASH;
import static legend.lodmod.LodAdditions.DOUBLE_SMACK;
import static legend.lodmod.LodAdditions.HARPOON;
import static legend.lodmod.LodAdditions.PURSUIT;
import static legend.lodmod.LodAdditions.WHIP_SMACK;

public class NewGame extends EngineState {
  public static final int[] characterStartingLevels = {1, 3, 4, 8, 13, 15, 17, 19, 23};
  @SuppressWarnings("unchecked")
  public static final RegistryDelegate<Addition>[] startingAddition_800ce758 = new RegistryDelegate[] {
    DOUBLE_SLASH,
    HARPOON,
    null,
    WHIP_SMACK,
    DOUBLE_PUNCH,
    ALBERT_HARPOON,
    DOUBLE_SMACK,
    PURSUIT,
    null,
  };

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

      //LAB_800c730c
      if(startingAddition_800ce758[charIndex] != null) {
        charData.selectedAddition_19 = startingAddition_800ce758[charIndex].getId();
      }

      for(final RegistryDelegate<Addition> additions : CHARACTER_ADDITIONS[charIndex]) {
        charData.additionStats.put(additions.getId(), new CharacterAdditionStats());
      }
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

  @Override
  public void updateDiscordRichPresence(final Activity activity) {
    activity.setDetails("Starting a New Game");
    activity.setState(null);
  }

  @Override
  public GsRVIEW2 getCamera() {
    return null;
  }
}
