package legend.game.modding.coremod.character;

import legend.game.characters.Addition04;
import legend.game.combat.environment.BattlePreloadedEntities_18cb0;
import legend.game.combat.types.AdditionHits80;
import legend.game.modding.coremod.CoreMod;
import legend.game.types.CharacterData2c;
import legend.game.types.LevelStuff08;
import legend.game.types.MagicStuff08;

import java.util.ArrayList;

import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class CharacterData {
  public int[] xpTable = new int[CoreMod.MAX_CHARACTER_LEVEL + 1];
  public LevelStuff08[] statsTable = new LevelStuff08[CoreMod.MAX_CHARACTER_LEVEL + 1];
  public int[] dxpTable = new int[CoreMod.MAX_DRAGOON_LEVEL + 1];
  public MagicStuff08[] dragoonStatsTable = new MagicStuff08[CoreMod.MAX_DRAGOON_LEVEL + 1];
  public ArrayList<AdditionHits80> additions = new ArrayList<>();
  public ArrayList<Addition04[]> additionsMultiplier = new ArrayList<>();
  public ArrayList<AdditionHits80> dragoonAddition = new ArrayList<>();
  public int[][] spBarColours = {{16, 87, 240, 9, 50, 138}, {16, 87, 240, 9, 50, 138}, {0, 181, 142, 0, 102, 80}, {206, 204, 17, 118, 117, 10}, {230, 139, 0, 132, 80, 0}, {181, 0, 0, 104, 0, 0}, {16, 87, 240, 9, 50, 138}};

  public int getAdditionHitCount(final int index) {
    final AdditionHits80 addition = this.additions.get(index);
    int hitCount = 0;
    for(int i = 0; i < 8; i++) {
      hitCount += addition.hits_00[i].overlayHitFrameOffset_02 > 0 ? 1 : 0;
    }
    return hitCount - 1;
  }

  public int getAdditionLevelSP(final int index, final int level) {
    final AdditionHits80 addition = this.additions.get(index);
    final Addition04[] multi = this.additionsMultiplier.get(index);
    int SP = 0;
    for(int i = 0; i < 8; i++) {
      SP += addition.hits_00[i].spValue_05 > 0 ? (addition.hits_00[i].spValue_05 * (100 + multi[level].spMultiplier_02) / 100) : 0;
    }
    return SP;
  }

  public int getAdditionDamage(final int index, final int level) {
    final AdditionHits80 addition = this.additions.get(index);
    final Addition04[] multi = this.additionsMultiplier.get(index);
    int dmg = 0;
    for(int i = 0; i < 8; i++) {
      dmg += Math.max(addition.hits_00[i].damageMultiplier_04, 0);
    }
    dmg = dmg * (100 + multi[level].damageMultiplier_03) / 100;
    return dmg;
  }

  public int getNonMaxedAdditions(final int index) {
    int notMaxed = this.additions.size();
    final CharacterData2c charData = gameState_800babc8.charData_32c[index];

    for(int i = 0; i < this.additions.size(); i++) {
      if (charData.additionLevels_1a[i] == CoreMod.MAX_ADDITION_LEVEL) {
        notMaxed--;
      }
    }

    return notMaxed;
  }
}
