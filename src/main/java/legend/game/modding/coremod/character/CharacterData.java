package legend.game.modding.coremod.character;


import legend.game.modding.coremod.CoreMod;
import legend.game.types.CharacterData2c;
import legend.game.types.LevelStuff08;
import legend.game.types.MagicStuff08;

import java.util.ArrayList;
import java.util.List;

import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class CharacterData {
  public int[] xpTable = new int[CoreMod.MAX_CHARACTER_LEVEL + 1];
  public LevelStuff08[] statsTable = new LevelStuff08[CoreMod.MAX_CHARACTER_LEVEL + 1];
  public int[] dxpTable = new int[CoreMod.MAX_DRAGOON_LEVEL + 1];
  public MagicStuff08[] dragoonStatsTable = new MagicStuff08[CoreMod.MAX_DRAGOON_LEVEL + 1];

  public int[][] spBarColours = {{16, 87, 240, 9, 50, 138}, {16, 87, 240, 9, 50, 138}, {0, 181, 142, 0, 102, 80}, {206, 204, 17, 118, 117, 10}, {230, 139, 0, 132, 80, 0}, {181, 0, 0, 104, 0, 0}, {16, 87, 240, 9, 50, 138}, {0, 0, 0, 0, 0, 0}};

}
