package legend.game.modding.coremod.character;

import legend.game.combat.environment.BattlePreloadedEntities_18cb0;
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
  public ArrayList<SingleAddition> additions = new ArrayList<>();
  public ArrayList<AdditionMultiplier> additionsMultiplier = new ArrayList<>();
  public ArrayList<SingleAddition> dragoonAddition = new ArrayList<>();
  public int[][] spBarColour = {{0x1057F0,0x1057F0,0x00B58E,0xCECC11,0xE68B00,0xB50000,0x000000},{0x09328A,0x09328A,0x006650,0x76750A,0x845000,0x680000,0x000000}};

  public int getAdditionHitCount(final int index) {
    final SingleAddition addition = this.additions.get(index);
    int hitCount = 0;
    for(int i = 0; i < 8; i++) {
      hitCount += addition.hits[i].totalFrames_02 > 0 ? 1 : 0;
    }
    return hitCount - 1;
  }

  public int getAdditionLevelSP(final int index, final int level) {
    final SingleAddition addition = this.additions.get(index);
    final AdditionMultiplierLevel multi = this.additionsMultiplier.get(index).levelMultiplier.get(level);
    int SP = 0;
    for(int i = 0; i < 8; i++) {
      SP += addition.hits[i].spValue_0a > 0 ? (addition.hits[i].spValue_0a * (100 + multi.spMultiplier_02) / 100) : 0;
    }
    return SP;
  }

  public int getAdditionDamage(final int index, final int level) {
    final SingleAddition addition = this.additions.get(index);
    final AdditionMultiplierLevel multi = this.additionsMultiplier.get(index).levelMultiplier.get(level);
    int dmg = 0;
    for(int i = 0; i < 8; i++) {
      dmg += addition.hits[i].damageMultiplier_08 > 0 ? addition.hits[i].damageMultiplier_08 : 0;
    }
    dmg = dmg * (100 + multi.dmgMultiplier_03) / 100;
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

  public int[] getSpBarColour(final int dlv) {
    final int[] spBarColour = new int[6];
    spBarColour[0] = this.spBarColour[0][dlv] >> 16 & 0xff;
    spBarColour[1] = this.spBarColour[0][dlv] >> 8 & 0xff;
    spBarColour[2] = this.spBarColour[0][dlv] & 0xff;
    spBarColour[3] = this.spBarColour[1][dlv] >> 16 & 0xff;
    spBarColour[4] = this.spBarColour[1][dlv] >> 8 & 0xff;
    spBarColour[5] = this.spBarColour[1][dlv] & 0xff;
    return spBarColour;
  }

  public static class SingleAddition {
    public BattlePreloadedEntities_18cb0.AdditionHitProperties20[] hits = new BattlePreloadedEntities_18cb0.AdditionHitProperties20[8];
  }

  public static class AdditionMultiplier {
    public ArrayList<AdditionMultiplierLevel> levelMultiplier = new ArrayList<>();
  }

  public static class AdditionMultiplierLevel {
    public int _00;
    public int _01;
    public int spMultiplier_02;
    public int dmgMultiplier_03;
  }
}
