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

  public int getAdditionHitCount(int index) {
    SingleAddition addition = additions.get(index);
    int hitCount = 0;
    for(int i = 0; i < 8; i++) {
      hitCount += addition.hits[i].totalFrames_02 > 0 ? 1 : 0;
    }
    return hitCount - 1;
  }

  public int getAdditionLevelSP(int index, int level) {
    SingleAddition addition = additions.get(index);
    AdditionMultiplierLevel multi = additionsMultiplier.get(index).levelMultiplier.get(level);
    int SP = 0;
    for(int i = 0; i < 8; i++) {
      SP += addition.hits[i].spValue_0a > 0 ? (addition.hits[i].spValue_0a * (100 + multi.spMultiplier_02) / 100) : 0;
    }
    return SP;
  }

  public int getAdditionDamage(int index, int level) {
    SingleAddition addition = additions.get(index);
    AdditionMultiplierLevel multi = additionsMultiplier.get(index).levelMultiplier.get(level);
    int dmg = 0;
    for(int i = 0; i < 8; i++) {
      dmg += addition.hits[i].damageMultiplier_08 > 0 ? addition.hits[i].damageMultiplier_08 : 0;
    }
    dmg = Math.round(dmg * (100 + multi.dmgMultiplier_03) / 100);
    return dmg;
  }

  public int getNonMaxedAdditions(int index) {
    int notMaxed = additions.size();
    final CharacterData2c charData = gameState_800babc8.charData_32c[index];

    for (int i = 0; i < additions.size(); i++) {
      if (charData.additionLevels_1a[i] == CoreMod.MAX_ADDITION_LEVEL) {
        notMaxed--;
      }
    }

    return notMaxed;
  }

  public static class SingleAddition {
    public BattlePreloadedEntities_18cb0.AdditionHitProperties20[] hits = new BattlePreloadedEntities_18cb0.AdditionHitProperties20[8];
  }

  public static class AdditionMultiplier {
    public ArrayList<AdditionMultiplierLevel> levelMultiplier = new ArrayList<>();
  }

  public static class AdditionMultiplierLevel {
    public int _00 = 0;
    public int _01 = 0;
    public int spMultiplier_02 = 0;
    public int dmgMultiplier_03 = 0;
  }
}
