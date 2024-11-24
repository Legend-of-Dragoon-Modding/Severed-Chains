package legend.game;

import legend.core.MathHelper;
import legend.core.audio.sequencer.assets.BackgroundMusic;
import legend.core.gpu.Bpp;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.i18n.I18n;
import legend.game.input.Input;
import legend.game.input.InputAction;
import legend.game.inventory.Addition04;
import legend.game.inventory.EquipItemResult;
import legend.game.inventory.Equipment;
import legend.game.inventory.Item;
import legend.game.inventory.screens.MenuStack;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.characters.AdditionHitMultiplierEvent;
import legend.game.modding.events.characters.AdditionUnlockEvent;
import legend.game.modding.events.characters.CharacterStatsEvent;
import legend.game.modding.events.characters.XpToLevelEvent;
import legend.game.modding.events.inventory.EquipmentStatsEvent;
import legend.game.modding.events.inventory.GatherAttackItemsEvent;
import legend.game.modding.events.inventory.GatherRecoveryItemsEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptParam;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.LevelStuff08;
import legend.game.types.MagicStuff08;
import legend.game.types.MenuAdditionInfo;
import legend.game.types.MenuEntries;
import legend.game.types.MenuEntryStruct04;
import legend.game.types.MenuGlyph06;
import legend.game.types.MenuStatus08;
import legend.game.types.MessageBox20;
import legend.game.types.MessageBoxResult;
import legend.game.types.Renderable58;
import legend.game.types.RenderableMetrics14;
import legend.game.types.UiFile;
import legend.game.types.UiPart;
import legend.game.types.UiType;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.REGISTRIES;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadDrgnFileSync;
import static legend.game.Scus94491BpeSegment.musicPackageLoadedCallback;
import static legend.game.Scus94491BpeSegment.playMusicPackage;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment.stopAndResetSoundsAndSequences;
import static legend.game.Scus94491BpeSegment.unloadSoundFile;
import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_8002.clearCharacterStats;
import static legend.game.Scus94491BpeSegment_8002.clearEquipmentStats;
import static legend.game.Scus94491BpeSegment_8002.copyPlayingSounds;
import static legend.game.Scus94491BpeSegment_8002.giveEquipment;
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.loadMenuTexture;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.sssqResetStuff;
import static legend.game.Scus94491BpeSegment_8002.takeEquipmentId;
import static legend.game.Scus94491BpeSegment_8002.takeItemId;
import static legend.game.Scus94491BpeSegment_8002.textHeight;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_8004.additionCounts_8004f5c0;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_8004.engineState_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.stopMusicSequence;
import static legend.game.Scus94491BpeSegment_8005.additionData_80052884;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.loadedDrgnFiles_800bcf78;
import static legend.game.Scus94491BpeSegment_800b.loadingNewGameState_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.playingSoundsBackup_800bca78;
import static legend.game.Scus94491BpeSegment_800b.queuedSounds_800bd110;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba4;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba8;
import static legend.game.Scus94491BpeSegment_800b.secondaryCharIds_800bdbf8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;
import static legend.game.combat.Battle.seed_800fa754;

public final class SItem {
  private SItem() { }

  public static final MenuStack menuStack = new MenuStack();
  private static BackgroundMusic menuMusic;

  public static final int[] charDragoonSpiritIndices_800fba58 = {0, 2, 5, 6, 4, 2, 1, 3, 5};
  public static final MenuStatus08[] menuStatus_800fba7c = {
    new MenuStatus08("Petrify", TextColour.MIDDLE_BROWN),
    new MenuStatus08("Charmed", TextColour.MIDDLE_BROWN),
    new MenuStatus08("Confused", TextColour.MIDDLE_BROWN),
    new MenuStatus08("Fear", TextColour.PURPLE),
    new MenuStatus08("Stunned", TextColour.MIDDLE_BROWN),
    new MenuStatus08("", TextColour.MIDDLE_BROWN),
    new MenuStatus08("Dspirit", TextColour.CYAN),
    new MenuStatus08("Poison", TextColour.LIME),
  };

  /** Note: arrays run into the next array's first element */
  public static final int[][] dragoonXpRequirements_800fbbf0 = {
    {0, 0, 1200, 6000, 12000, 20000, 0},
    {0, 0, 1000, 6000, 12000, 20000, 0},
    {0, 0, 1000, 6000, 12000, 20000, 0},
    {0, 0, 1200, 6000, 12000, 20000, 0},
    {0, 0, 1000, 6000, 12000, 20000, 0},
    {0, 0, 1000, 6000, 12000, 20000, 0},
    {0, 0, 1000, 2000, 12000, 20000, 0},
    {0, 0, 1000, 2000, 12000, 20000, 0},
    {0, 0, 1000, 6000, 12000, 20000, 0},
  };

  public static final int[] dragoonGoodsBits_800fbd08 = {0, 2, 5, 6, 4, 2, 1, 3, 5, 7};

  public static final LevelStuff08[][] levelStuff_80111cfc = {
    {new LevelStuff08(0, -1, 0, 0, 0, 0, 0), new LevelStuff08(30, -1, 50, 2, 3, 4, 4), new LevelStuff08(60, -1, 50, 4, 5, 5, 5), new LevelStuff08(90, -1, 50, 6, 7, 7, 7), new LevelStuff08(120, -1, 50, 8, 9, 10, 9), new LevelStuff08(150, -1, 50, 11, 11, 12, 11), new LevelStuff08(180, -1, 50, 13, 13, 14, 13), new LevelStuff08(210, -1, 50, 15, 15, 16, 15), new LevelStuff08(240, -1, 50, 18, 17, 19, 17), new LevelStuff08(270, -1, 50, 20, 19, 21, 19), new LevelStuff08(300, -1, 50, 22, 21, 23, 21), new LevelStuff08(330, -1, 50, 25, 24, 26, 24), new LevelStuff08(413, -1, 50, 27, 26, 28, 26), new LevelStuff08(496, -1, 50, 30, 29, 31, 29), new LevelStuff08(579, -1, 50, 32, 31, 33, 32), new LevelStuff08(662, -1, 50, 35, 34, 36, 34), new LevelStuff08(745, -1, 50, 37, 36, 38, 37), new LevelStuff08(828, -1, 50, 40, 39, 41, 40), new LevelStuff08(911, -1, 50, 42, 41, 43, 42), new LevelStuff08(994, -1, 50, 45, 44, 46, 45), new LevelStuff08(1077, -1, 50, 47, 46, 48, 48), new LevelStuff08(1160, -1, 50, 50, 49, 51, 51), new LevelStuff08(1272, -1, 50, 52, 51, 53, 53), new LevelStuff08(1384, -1, 50, 55, 54, 56, 55), new LevelStuff08(1496, -1, 50, 57, 56, 58, 57), new LevelStuff08(1608, -1, 50, 60, 59, 61, 60), new LevelStuff08(1720, -1, 50, 62, 61, 63, 62), new LevelStuff08(1832, -1, 50, 65, 64, 66, 64), new LevelStuff08(1944, -1, 50, 67, 66, 68, 67), new LevelStuff08(2056, -1, 50, 70, 69, 71, 69), new LevelStuff08(2168, -1, 50, 72, 71, 73, 71), new LevelStuff08(2280, -1, 50, 75, 74, 76, 74), new LevelStuff08(2399, -1, 50, 77, 76, 78, 76), new LevelStuff08(2518, -1, 50, 80, 79, 81, 79), new LevelStuff08(2637, -1, 50, 82, 81, 83, 82), new LevelStuff08(2756, -1, 50, 85, 84, 86, 84), new LevelStuff08(2875, -1, 50, 87, 86, 88, 87), new LevelStuff08(2994, -1, 50, 90, 89, 91, 90), new LevelStuff08(3113, -1, 50, 92, 91, 93, 92), new LevelStuff08(3232, -1, 50, 95, 94, 96, 95), new LevelStuff08(3351, -1, 50, 97, 96, 98, 98), new LevelStuff08(3470, -1, 50, 100, 99, 101, 101), new LevelStuff08(3729, -1, 50, 102, 101, 103, 103), new LevelStuff08(3988, -1, 50, 105, 104, 105, 105), new LevelStuff08(4247, -1, 50, 107, 106, 108, 107), new LevelStuff08(4506, -1, 50, 110, 109, 110, 110), new LevelStuff08(4765, -1, 50, 112, 111, 113, 112), new LevelStuff08(5024, -1, 50, 115, 114, 115, 114), new LevelStuff08(5283, -1, 50, 117, 116, 118, 117), new LevelStuff08(5542, -1, 50, 120, 119, 120, 119), new LevelStuff08(5801, -1, 50, 122, 121, 123, 121), new LevelStuff08(6060, -1, 50, 125, 124, 125, 124), new LevelStuff08(6220, -1, 50, 127, 126, 128, 126), new LevelStuff08(6380, -1, 50, 130, 129, 131, 129), new LevelStuff08(6540, -1, 50, 133, 132, 133, 132), new LevelStuff08(6700, -1, 50, 136, 135, 136, 135), new LevelStuff08(6860, -1, 50, 138, 138, 139, 138), new LevelStuff08(7020, -1, 50, 141, 141, 141, 141), new LevelStuff08(7180, -1, 50, 144, 144, 144, 144), new LevelStuff08(7340, -1, 50, 147, 147, 147, 147), new LevelStuff08(7500, -1, 50, 150, 150, 150, 150), },
    {new LevelStuff08(0, -1, 0, 0, 0, 0, 0), new LevelStuff08(35, 8, 40, 3, 2, 4, 2), new LevelStuff08(67, -1, 40, 6, 3, 7, 3), new LevelStuff08(100, -1, 40, 13, 4, 10, 4), new LevelStuff08(133, -1, 40, 15, 6, 13, 5), new LevelStuff08(166, 9, 40, 16, 7, 16, 6), new LevelStuff08(199, -1, 40, 20, 8, 19, 7), new LevelStuff08(231, 10, 40, 23, 10, 22, 7), new LevelStuff08(264, -1, 40, 27, 11, 25, 8), new LevelStuff08(297, -1, 40, 30, 13, 28, 9), new LevelStuff08(330, -1, 40, 34, 14, 31, 10), new LevelStuff08(363, 11, 40, 37, 15, 34, 11), new LevelStuff08(454, -1, 40, 41, 17, 37, 12), new LevelStuff08(545, 12, 40, 45, 18, 41, 13), new LevelStuff08(636, -1, 40, 48, 20, 44, 14), new LevelStuff08(728, -1, 40, 52, 22, 47, 15), new LevelStuff08(819, -1, 40, 56, 23, 51, 16), new LevelStuff08(910, -1, 40, 60, 25, 54, 17), new LevelStuff08(1002, -1, 40, 63, 27, 57, 18), new LevelStuff08(1093, -1, 40, 67, 28, 61, 19), new LevelStuff08(1184, -1, 40, 71, 30, 64, 20), new LevelStuff08(1276, -1, 40, 75, 31, 67, 21), new LevelStuff08(1399, -1, 40, 78, 33, 71, 24), new LevelStuff08(1522, -1, 40, 82, 35, 74, 27), new LevelStuff08(1645, -1, 40, 86, 37, 77, 29), new LevelStuff08(1768, -1, 40, 90, 39, 81, 32), new LevelStuff08(1892, -1, 40, 93, 41, 84, 35), new LevelStuff08(2015, -1, 40, 97, 43, 87, 37), new LevelStuff08(2138, -1, 40, 101, 45, 91, 40), new LevelStuff08(2261, -1, 40, 105, 47, 94, 43), new LevelStuff08(2384, -1, 40, 108, 48, 97, 45), new LevelStuff08(2508, -1, 40, 112, 50, 101, 48), new LevelStuff08(2638, -1, 40, 116, 52, 104, 49), new LevelStuff08(2769, -1, 40, 120, 54, 107, 50), new LevelStuff08(2900, -1, 40, 123, 55, 111, 51), new LevelStuff08(3031, -1, 40, 127, 57, 114, 52), new LevelStuff08(3162, -1, 40, 131, 59, 117, 53), new LevelStuff08(3293, -1, 40, 135, 61, 121, 54), new LevelStuff08(3424, -1, 40, 138, 62, 124, 55), new LevelStuff08(3555, -1, 40, 142, 64, 127, 57), new LevelStuff08(3686, -1, 40, 146, 66, 131, 58), new LevelStuff08(3817, -1, 40, 150, 68, 134, 59), new LevelStuff08(4101, -1, 40, 153, 70, 137, 61), new LevelStuff08(4386, -1, 40, 157, 72, 141, 64), new LevelStuff08(4671, -1, 40, 161, 74, 144, 67), new LevelStuff08(4956, -1, 40, 165, 76, 147, 70), new LevelStuff08(5241, -1, 40, 168, 78, 151, 73), new LevelStuff08(5526, -1, 40, 172, 80, 154, 76), new LevelStuff08(5811, -1, 40, 176, 82, 157, 79), new LevelStuff08(6096, -1, 40, 180, 84, 161, 82), new LevelStuff08(6381, -1, 40, 183, 86, 164, 85), new LevelStuff08(6666, -1, 40, 187, 88, 167, 87), new LevelStuff08(6842, -1, 40, 191, 90, 171, 88), new LevelStuff08(7018, -1, 40, 195, 92, 174, 89), new LevelStuff08(7194, -1, 40, 200, 94, 178, 90), new LevelStuff08(7370, -1, 40, 204, 97, 182, 91), new LevelStuff08(7546, -1, 40, 208, 99, 185, 93), new LevelStuff08(7722, -1, 40, 212, 101, 189, 94), new LevelStuff08(7898, -1, 40, 216, 104, 192, 95), new LevelStuff08(8074, -1, 40, 220, 106, 196, 96), new LevelStuff08(8250, -1, 40, 225, 108, 199, 97), },
    {new LevelStuff08(0, 0, 0, 0, 0, 0, 0), new LevelStuff08(24, -1, 65, 2, 3, 2, 3), new LevelStuff08(48, -1, 65, 3, 6, 3, 6), new LevelStuff08(72, -1, 65, 5, 9, 5, 9), new LevelStuff08(96, -1, 65, 7, 12, 6, 12), new LevelStuff08(120, -1, 65, 8, 16, 8, 15), new LevelStuff08(144, -1, 65, 10, 19, 9, 19), new LevelStuff08(168, -1, 65, 12, 22, 11, 22), new LevelStuff08(192, -1, 65, 13, 26, 12, 25), new LevelStuff08(216, -1, 65, 15, 29, 14, 28), new LevelStuff08(240, -1, 65, 17, 32, 15, 31), new LevelStuff08(264, -1, 65, 19, 36, 17, 35), new LevelStuff08(330, -1, 65, 20, 41, 18, 41), new LevelStuff08(396, -1, 65, 21, 47, 20, 47), new LevelStuff08(463, -1, 65, 22, 53, 22, 53), new LevelStuff08(529, -1, 65, 23, 58, 24, 59), new LevelStuff08(596, -1, 65, 24, 64, 25, 65), new LevelStuff08(662, -1, 65, 26, 70, 27, 71), new LevelStuff08(728, -1, 65, 27, 75, 29, 77), new LevelStuff08(795, -1, 65, 28, 81, 31, 83), new LevelStuff08(861, -1, 65, 29, 87, 33, 89), new LevelStuff08(928, -1, 65, 30, 93, 34, 95), new LevelStuff08(1017, -1, 65, 32, 98, 36, 101), new LevelStuff08(1107, -1, 65, 33, 104, 37, 106), new LevelStuff08(1196, -1, 65, 35, 109, 39, 111), new LevelStuff08(1286, -1, 65, 36, 115, 40, 117), new LevelStuff08(1376, -1, 65, 38, 120, 42, 122), new LevelStuff08(1465, -1, 65, 39, 126, 43, 128), new LevelStuff08(1555, -1, 65, 40, 131, 45, 133), new LevelStuff08(1644, -1, 65, 42, 137, 46, 139), new LevelStuff08(1734, -1, 65, 43, 143, 48, 144), new LevelStuff08(1824, -1, 65, 45, 148, 50, 150), new LevelStuff08(1919, -1, 65, 47, 153, 51, 155), new LevelStuff08(2014, -1, 65, 48, 158, 53, 160), new LevelStuff08(2109, -1, 65, 50, 164, 54, 165), new LevelStuff08(2204, -1, 65, 52, 169, 56, 170), new LevelStuff08(2300, -1, 65, 54, 174, 57, 176), new LevelStuff08(2395, -1, 65, 56, 179, 59, 181), new LevelStuff08(2490, -1, 65, 58, 184, 60, 186), new LevelStuff08(2585, -1, 65, 60, 189, 62, 191), new LevelStuff08(2680, -1, 65, 61, 194, 63, 196), new LevelStuff08(2776, -1, 65, 63, 200, 65, 202), new LevelStuff08(2983, -1, 65, 65, 202, 68, 204), new LevelStuff08(3190, -1, 65, 67, 205, 72, 207), new LevelStuff08(3397, -1, 65, 69, 208, 75, 209), new LevelStuff08(3604, -1, 65, 71, 211, 79, 212), new LevelStuff08(3812, -1, 65, 73, 214, 83, 215), new LevelStuff08(4019, -1, 65, 76, 217, 86, 217), new LevelStuff08(4226, -1, 65, 78, 220, 90, 220), new LevelStuff08(4433, -1, 65, 80, 223, 93, 222), new LevelStuff08(4640, -1, 65, 82, 226, 97, 225), new LevelStuff08(4848, -1, 65, 84, 229, 101, 228), new LevelStuff08(4976, -1, 65, 86, 232, 106, 231), new LevelStuff08(5104, -1, 65, 88, 235, 111, 234), new LevelStuff08(5232, -1, 65, 91, 238, 117, 237), new LevelStuff08(5360, -1, 65, 93, 240, 122, 240), new LevelStuff08(5488, -1, 65, 95, 243, 127, 243), new LevelStuff08(5616, -1, 65, 97, 246, 133, 246), new LevelStuff08(5744, -1, 65, 100, 249, 138, 249), new LevelStuff08(5872, -1, 65, 102, 252, 143, 252), new LevelStuff08(6000, -1, 65, 104, 255, 149, 254), },
    {new LevelStuff08(0, 0, 0, 0, 0, 0, 0), new LevelStuff08(21, 14, 55, 3, 6, 5, 5), new LevelStuff08(42, -1, 55, 6, 10, 8, 7), new LevelStuff08(63, -1, 55, 9, 14, 11, 10), new LevelStuff08(84, -1, 55, 13, 18, 14, 12), new LevelStuff08(105, -1, 55, 16, 22, 17, 15), new LevelStuff08(126, -1, 55, 20, 27, 19, 17), new LevelStuff08(147, -1, 55, 23, 31, 22, 20), new LevelStuff08(168, -1, 55, 27, 35, 25, 22), new LevelStuff08(189, -1, 55, 30, 39, 28, 25), new LevelStuff08(210, -1, 55, 34, 43, 31, 27), new LevelStuff08(231, -1, 55, 37, 48, 34, 30), new LevelStuff08(289, -1, 55, 39, 49, 35, 32), new LevelStuff08(347, -1, 55, 40, 50, 37, 34), new LevelStuff08(405, 15, 55, 42, 51, 38, 37), new LevelStuff08(463, -1, 55, 44, 53, 39, 39), new LevelStuff08(521, -1, 55, 46, 54, 40, 41), new LevelStuff08(579, -1, 55, 47, 55, 41, 43), new LevelStuff08(637, -1, 55, 49, 57, 43, 45), new LevelStuff08(695, 16, 55, 51, 58, 44, 47), new LevelStuff08(753, -1, 55, 52, 59, 45, 50), new LevelStuff08(812, -1, 55, 54, 61, 46, 52), new LevelStuff08(890, -1, 55, 56, 62, 49, 55), new LevelStuff08(968, -1, 55, 57, 63, 53, 58), new LevelStuff08(1047, -1, 55, 59, 64, 56, 61), new LevelStuff08(1125, -1, 55, 61, 66, 59, 64), new LevelStuff08(1204, -1, 55, 63, 67, 63, 67), new LevelStuff08(1282, -1, 55, 64, 68, 66, 70), new LevelStuff08(1360, -1, 55, 66, 70, 69, 73), new LevelStuff08(1439, -1, 55, 68, 71, 72, 76), new LevelStuff08(1517, 17, 55, 70, 72, 76, 79), new LevelStuff08(1596, -1, 55, 71, 74, 79, 82), new LevelStuff08(1679, -1, 55, 73, 75, 80, 83), new LevelStuff08(1762, -1, 55, 75, 77, 81, 84), new LevelStuff08(1845, -1, 55, 77, 79, 83, 85), new LevelStuff08(1929, -1, 55, 79, 80, 84, 86), new LevelStuff08(2012, -1, 55, 81, 82, 85, 87), new LevelStuff08(2095, -1, 55, 83, 84, 86, 88), new LevelStuff08(2179, -1, 55, 84, 86, 87, 90), new LevelStuff08(2262, -1, 55, 86, 87, 88, 91), new LevelStuff08(2345, -1, 55, 88, 89, 90, 92), new LevelStuff08(2429, -1, 55, 90, 91, 91, 93), new LevelStuff08(2610, -1, 55, 94, 95, 95, 97), new LevelStuff08(2791, -1, 55, 98, 99, 99, 101), new LevelStuff08(2972, -1, 55, 103, 103, 103, 105), new LevelStuff08(3154, -1, 55, 107, 107, 108, 109), new LevelStuff08(3335, -1, 55, 111, 111, 112, 113), new LevelStuff08(3516, -1, 55, 115, 115, 116, 117), new LevelStuff08(3698, -1, 55, 119, 119, 120, 121), new LevelStuff08(3879, -1, 55, 124, 123, 124, 125), new LevelStuff08(4060, -1, 55, 128, 127, 129, 129), new LevelStuff08(4242, -1, 55, 132, 131, 133, 133), new LevelStuff08(4354, -1, 55, 133, 133, 134, 135), new LevelStuff08(4466, -1, 55, 134, 135, 135, 136), new LevelStuff08(4578, -1, 55, 135, 137, 136, 138), new LevelStuff08(4690, -1, 55, 136, 139, 137, 140), new LevelStuff08(4802, -1, 55, 137, 141, 138, 142), new LevelStuff08(4914, -1, 55, 139, 143, 139, 144), new LevelStuff08(5026, -1, 55, 140, 145, 140, 146), new LevelStuff08(5138, -1, 55, 141, 147, 141, 148), new LevelStuff08(5250, -1, 55, 142, 150, 142, 150), },
    {new LevelStuff08(0, 0, 0, 0, 0, 0, 0), new LevelStuff08(27, 29, 60, 3, 2, 4, 2), new LevelStuff08(54, -1, 60, 5, 3, 6, 3), new LevelStuff08(81, -1, 60, 8, 5, 8, 5), new LevelStuff08(108, -1, 60, 11, 6, 10, 6), new LevelStuff08(135, -1, 60, 14, 8, 12, 7), new LevelStuff08(162, -1, 60, 17, 10, 15, 9), new LevelStuff08(189, -1, 60, 19, 11, 17, 10), new LevelStuff08(216, -1, 60, 22, 13, 19, 11), new LevelStuff08(243, -1, 60, 25, 14, 21, 13), new LevelStuff08(270, -1, 60, 28, 16, 23, 14), new LevelStuff08(297, -1, 60, 31, 18, 26, 15), new LevelStuff08(371, -1, 60, 34, 20, 28, 19), new LevelStuff08(446, -1, 60, 37, 23, 31, 22), new LevelStuff08(521, 30, 60, 40, 26, 33, 25), new LevelStuff08(595, -1, 60, 43, 29, 36, 28), new LevelStuff08(670, -1, 60, 47, 32, 38, 32), new LevelStuff08(745, -1, 60, 50, 35, 41, 35), new LevelStuff08(819, 31, 60, 53, 38, 43, 38), new LevelStuff08(894, -1, 60, 56, 41, 46, 41), new LevelStuff08(969, -1, 60, 59, 44, 48, 45), new LevelStuff08(1044, -1, 60, 63, 47, 51, 48), new LevelStuff08(1144, 32, 60, 66, 48, 53, 49), new LevelStuff08(1245, -1, 60, 69, 49, 56, 50), new LevelStuff08(1346, -1, 60, 72, 49, 58, 51), new LevelStuff08(1447, -1, 60, 75, 50, 61, 52), new LevelStuff08(1548, 33, 60, 78, 51, 63, 53), new LevelStuff08(1648, -1, 60, 81, 52, 66, 54), new LevelStuff08(1749, -1, 60, 84, 53, 68, 55), new LevelStuff08(1850, -1, 60, 87, 53, 71, 56), new LevelStuff08(1951, 34, 60, 90, 54, 73, 56), new LevelStuff08(2052, -1, 60, 94, 55, 76, 57), new LevelStuff08(2159, -1, 60, 97, 59, 78, 62), new LevelStuff08(2266, -1, 60, 100, 64, 81, 66), new LevelStuff08(2373, -1, 60, 103, 68, 83, 70), new LevelStuff08(2480, -1, 60, 106, 73, 86, 74), new LevelStuff08(2587, -1, 60, 109, 77, 88, 78), new LevelStuff08(2694, -1, 60, 112, 81, 91, 82), new LevelStuff08(2801, -1, 60, 115, 86, 93, 87), new LevelStuff08(2908, -1, 60, 118, 90, 96, 91), new LevelStuff08(3015, -1, 60, 121, 95, 98, 95), new LevelStuff08(3123, -1, 60, 125, 99, 101, 99), new LevelStuff08(3356, -1, 60, 128, 100, 103, 100), new LevelStuff08(3589, -1, 60, 131, 101, 105, 101), new LevelStuff08(3822, -1, 60, 134, 101, 108, 102), new LevelStuff08(4055, -1, 60, 137, 102, 110, 103), new LevelStuff08(4288, -1, 60, 140, 103, 113, 103), new LevelStuff08(4521, -1, 60, 143, 104, 115, 104), new LevelStuff08(4754, -1, 60, 146, 104, 118, 105), new LevelStuff08(4987, -1, 60, 149, 105, 120, 106), new LevelStuff08(5220, -1, 60, 152, 106, 123, 107), new LevelStuff08(5454, -1, 60, 156, 107, 125, 108), new LevelStuff08(5598, -1, 60, 159, 111, 128, 112), new LevelStuff08(5742, -1, 60, 163, 116, 131, 117), new LevelStuff08(5886, -1, 60, 166, 120, 133, 121), new LevelStuff08(6030, -1, 60, 170, 125, 136, 126), new LevelStuff08(6174, -1, 60, 173, 130, 139, 130), new LevelStuff08(6318, -1, 60, 177, 134, 141, 135), new LevelStuff08(6462, -1, 60, 180, 139, 144, 139), new LevelStuff08(6606, -1, 60, 184, 143, 147, 144), new LevelStuff08(6750, -1, 60, 188, 148, 150, 148), },
    {new LevelStuff08(0, -1, 0, 0, 0, 0, 0), new LevelStuff08(35, 8, 40, 3, 2, 4, 2), new LevelStuff08(67, -1, 40, 6, 3, 7, 3), new LevelStuff08(100, -1, 40, 13, 4, 10, 4), new LevelStuff08(133, -1, 40, 15, 6, 13, 5), new LevelStuff08(166, 9, 40, 16, 7, 16, 6), new LevelStuff08(199, -1, 40, 20, 8, 19, 7), new LevelStuff08(231, 10, 40, 23, 10, 22, 7), new LevelStuff08(264, -1, 40, 27, 11, 25, 8), new LevelStuff08(297, -1, 40, 30, 13, 28, 9), new LevelStuff08(330, -1, 40, 34, 14, 31, 10), new LevelStuff08(363, 11, 40, 37, 15, 34, 11), new LevelStuff08(454, -1, 40, 41, 17, 37, 12), new LevelStuff08(545, 12, 40, 45, 18, 41, 13), new LevelStuff08(636, -1, 40, 48, 20, 44, 14), new LevelStuff08(728, -1, 40, 52, 22, 47, 15), new LevelStuff08(819, -1, 40, 56, 23, 51, 16), new LevelStuff08(910, -1, 40, 60, 25, 54, 17), new LevelStuff08(1002, -1, 40, 63, 27, 57, 18), new LevelStuff08(1093, -1, 40, 67, 28, 61, 19), new LevelStuff08(1184, -1, 40, 71, 30, 64, 20), new LevelStuff08(1276, -1, 40, 75, 31, 67, 21), new LevelStuff08(1399, -1, 40, 78, 33, 71, 24), new LevelStuff08(1522, -1, 40, 82, 35, 74, 27), new LevelStuff08(1645, -1, 40, 86, 37, 77, 29), new LevelStuff08(1768, -1, 40, 90, 39, 81, 32), new LevelStuff08(1892, -1, 40, 93, 41, 84, 35), new LevelStuff08(2015, -1, 40, 97, 43, 87, 37), new LevelStuff08(2138, -1, 40, 101, 45, 91, 40), new LevelStuff08(2261, -1, 40, 105, 47, 94, 43), new LevelStuff08(2384, -1, 40, 108, 48, 97, 45), new LevelStuff08(2508, -1, 40, 112, 50, 101, 48), new LevelStuff08(2638, -1, 40, 116, 52, 104, 49), new LevelStuff08(2769, -1, 40, 120, 54, 107, 50), new LevelStuff08(2900, -1, 40, 123, 55, 111, 51), new LevelStuff08(3031, -1, 40, 127, 57, 114, 52), new LevelStuff08(3162, -1, 40, 131, 59, 117, 53), new LevelStuff08(3293, -1, 40, 135, 61, 121, 54), new LevelStuff08(3424, -1, 40, 138, 62, 124, 55), new LevelStuff08(3555, -1, 40, 142, 64, 127, 57), new LevelStuff08(3686, -1, 40, 146, 66, 131, 58), new LevelStuff08(3817, -1, 40, 150, 68, 134, 59), new LevelStuff08(4101, -1, 40, 153, 70, 137, 61), new LevelStuff08(4386, -1, 40, 157, 72, 141, 64), new LevelStuff08(4671, -1, 40, 161, 74, 144, 67), new LevelStuff08(4956, -1, 40, 165, 76, 147, 70), new LevelStuff08(5241, -1, 40, 168, 78, 151, 73), new LevelStuff08(5526, -1, 40, 172, 80, 154, 76), new LevelStuff08(5811, -1, 40, 176, 82, 157, 79), new LevelStuff08(6096, -1, 40, 180, 84, 161, 82), new LevelStuff08(6381, -1, 40, 183, 86, 164, 85), new LevelStuff08(6666, -1, 40, 187, 88, 167, 87), new LevelStuff08(6842, -1, 40, 191, 90, 171, 88), new LevelStuff08(7018, -1, 40, 195, 92, 174, 89), new LevelStuff08(7194, -1, 40, 200, 94, 178, 90), new LevelStuff08(7370, -1, 40, 204, 97, 182, 91), new LevelStuff08(7546, -1, 40, 208, 99, 185, 93), new LevelStuff08(7722, -1, 40, 212, 101, 189, 94), new LevelStuff08(7898, -1, 40, 216, 104, 192, 95), new LevelStuff08(8074, -1, 40, 220, 106, 196, 96), new LevelStuff08(8250, -1, 40, 225, 108, 199, 97), },
    {new LevelStuff08(0, 0, 0, 0, 0, 0, 0), new LevelStuff08(18, 23, 70, 2, 3, 2, 4), new LevelStuff08(36, -1, 70, 3, 6, 3, 7), new LevelStuff08(54, -1, 70, 5, 9, 4, 10), new LevelStuff08(72, -1, 70, 7, 12, 6, 13), new LevelStuff08(90, -1, 70, 8, 15, 7, 16), new LevelStuff08(108, -1, 70, 10, 19, 9, 20), new LevelStuff08(126, -1, 70, 12, 22, 10, 23), new LevelStuff08(144, -1, 70, 13, 25, 11, 26), new LevelStuff08(162, -1, 70, 15, 28, 13, 29), new LevelStuff08(180, -1, 70, 17, 31, 14, 32), new LevelStuff08(198, -1, 70, 19, 34, 16, 36), new LevelStuff08(247, -1, 70, 21, 38, 17, 39), new LevelStuff08(297, -1, 70, 23, 42, 19, 42), new LevelStuff08(347, -1, 70, 25, 46, 21, 45), new LevelStuff08(397, -1, 70, 27, 50, 23, 48), new LevelStuff08(447, -1, 70, 29, 54, 25, 51), new LevelStuff08(496, -1, 70, 31, 58, 27, 54), new LevelStuff08(546, -1, 70, 33, 62, 29, 57), new LevelStuff08(596, -1, 70, 35, 66, 31, 60), new LevelStuff08(646, -1, 70, 37, 70, 33, 63), new LevelStuff08(696, 24, 70, 40, 74, 34, 66), new LevelStuff08(763, -1, 70, 41, 77, 37, 69), new LevelStuff08(830, -1, 70, 42, 81, 39, 73), new LevelStuff08(897, -1, 70, 43, 85, 41, 76), new LevelStuff08(964, -1, 70, 44, 88, 43, 80), new LevelStuff08(1032, 25, 70, 45, 92, 45, 84), new LevelStuff08(1099, -1, 70, 46, 96, 47, 87), new LevelStuff08(1166, -1, 70, 47, 99, 49, 91), new LevelStuff08(1233, -1, 70, 48, 103, 52, 94), new LevelStuff08(1300, 26, 70, 49, 107, 54, 98), new LevelStuff08(1368, -1, 70, 50, 111, 56, 102), new LevelStuff08(1439, -1, 70, 52, 114, 58, 105), new LevelStuff08(1510, -1, 70, 53, 118, 60, 108), new LevelStuff08(1582, 27, 70, 54, 122, 62, 111), new LevelStuff08(1653, -1, 70, 56, 126, 65, 114), new LevelStuff08(1725, -1, 70, 57, 130, 67, 117), new LevelStuff08(1796, -1, 70, 58, 133, 69, 120), new LevelStuff08(1867, -1, 70, 60, 137, 71, 123), new LevelStuff08(1939, -1, 70, 61, 141, 73, 126), new LevelStuff08(2010, -1, 70, 62, 145, 75, 129), new LevelStuff08(2082, -1, 70, 64, 149, 77, 133), new LevelStuff08(2237, -1, 70, 68, 152, 80, 136), new LevelStuff08(2392, -1, 70, 72, 156, 82, 140), new LevelStuff08(2548, -1, 70, 76, 160, 85, 143), new LevelStuff08(2703, -1, 70, 80, 163, 87, 147), new LevelStuff08(2859, -1, 70, 85, 167, 90, 150), new LevelStuff08(3014, -1, 70, 89, 171, 92, 154), new LevelStuff08(3169, -1, 70, 93, 174, 94, 157), new LevelStuff08(3325, -1, 70, 97, 178, 97, 161), new LevelStuff08(3480, -1, 70, 101, 182, 99, 164), new LevelStuff08(3636, -1, 70, 105, 186, 102, 168), new LevelStuff08(3732, -1, 70, 110, 190, 104, 171), new LevelStuff08(3828, -1, 70, 115, 194, 106, 174), new LevelStuff08(3924, -1, 70, 119, 199, 108, 178), new LevelStuff08(4020, -1, 70, 124, 203, 110, 181), new LevelStuff08(4116, -1, 70, 129, 207, 112, 184), new LevelStuff08(4212, -1, 70, 133, 212, 114, 188), new LevelStuff08(4308, -1, 70, 138, 216, 116, 191), new LevelStuff08(4404, -1, 70, 143, 220, 118, 195), new LevelStuff08(4500, -1, 70, 147, 225, 120, 198), },
    {new LevelStuff08(0, 0, 0, 0, 0, 0, 0), new LevelStuff08(45, 19, 30, 4, 2, 4, 3), new LevelStuff08(83, -1, 30, 9, 3, 8, 4), new LevelStuff08(121, -1, 30, 14, 4, 13, 5), new LevelStuff08(160, -1, 30, 18, 5, 17, 6), new LevelStuff08(198, -1, 30, 23, 6, 22, 7), new LevelStuff08(237, -1, 30, 28, 7, 27, 8), new LevelStuff08(275, -1, 30, 33, 8, 31, 9), new LevelStuff08(313, -1, 30, 38, 9, 36, 10), new LevelStuff08(352, -1, 30, 43, 10, 40, 11), new LevelStuff08(390, -1, 30, 48, 11, 45, 12), new LevelStuff08(429, -1, 30, 53, 12, 50, 13), new LevelStuff08(536, -1, 30, 57, 13, 55, 14), new LevelStuff08(644, -1, 30, 62, 14, 60, 15), new LevelStuff08(752, -1, 30, 66, 15, 65, 16), new LevelStuff08(860, -1, 30, 71, 17, 70, 17), new LevelStuff08(968, -1, 30, 76, 18, 76, 18), new LevelStuff08(1076, -1, 30, 80, 19, 81, 19), new LevelStuff08(1184, -1, 30, 85, 21, 86, 20), new LevelStuff08(1292, -1, 30, 89, 22, 91, 21), new LevelStuff08(1400, -1, 30, 94, 23, 96, 22), new LevelStuff08(1508, -1, 30, 99, 25, 102, 23), new LevelStuff08(1653, -1, 30, 103, 26, 107, 24), new LevelStuff08(1799, 20, 30, 108, 27, 112, 25), new LevelStuff08(1944, -1, 30, 113, 28, 117, 26), new LevelStuff08(2090, -1, 30, 118, 29, 122, 27), new LevelStuff08(2236, -1, 30, 123, 31, 128, 28), new LevelStuff08(2381, -1, 30, 128, 32, 133, 29), new LevelStuff08(2527, -1, 30, 133, 33, 138, 30), new LevelStuff08(2672, -1, 30, 138, 34, 143, 31), new LevelStuff08(2818, 21, 30, 143, 35, 148, 32), new LevelStuff08(2964, -1, 30, 148, 37, 154, 34), new LevelStuff08(3118, -1, 30, 153, 38, 157, 35), new LevelStuff08(3273, -1, 30, 159, 39, 161, 36), new LevelStuff08(3428, -1, 30, 164, 40, 165, 38), new LevelStuff08(3582, -1, 30, 170, 42, 168, 39), new LevelStuff08(3737, -1, 30, 176, 43, 172, 41), new LevelStuff08(3892, -1, 30, 181, 44, 176, 42), new LevelStuff08(4046, -1, 30, 187, 46, 179, 44), new LevelStuff08(4201, -1, 30, 192, 47, 183, 45), new LevelStuff08(4356, -1, 30, 198, 48, 187, 47), new LevelStuff08(4511, -1, 30, 204, 50, 190, 48), new LevelStuff08(4847, -1, 30, 206, 51, 194, 50), new LevelStuff08(5184, -1, 30, 209, 52, 197, 51), new LevelStuff08(5521, -1, 30, 211, 53, 201, 53), new LevelStuff08(5857, -1, 30, 214, 54, 205, 54), new LevelStuff08(6194, -1, 30, 217, 56, 208, 56), new LevelStuff08(6531, -1, 30, 219, 57, 212, 58), new LevelStuff08(6867, -1, 30, 222, 58, 215, 59), new LevelStuff08(7204, -1, 30, 224, 59, 219, 61), new LevelStuff08(7541, -1, 30, 227, 60, 223, 63), new LevelStuff08(7878, -1, 30, 230, 62, 226, 64), new LevelStuff08(8086, -1, 30, 232, 63, 229, 66), new LevelStuff08(8294, -1, 30, 235, 64, 232, 68), new LevelStuff08(8502, -1, 30, 238, 66, 236, 69), new LevelStuff08(8710, -1, 30, 241, 67, 239, 71), new LevelStuff08(8918, -1, 30, 243, 69, 242, 73), new LevelStuff08(9126, -1, 30, 246, 70, 245, 74), new LevelStuff08(9334, -1, 30, 249, 72, 248, 76), new LevelStuff08(9542, -1, 30, 252, 73, 251, 78), new LevelStuff08(9750, -1, 30, 254, 75, 254, 80), },
    {new LevelStuff08(0, 0, 0, 0, 0, 0, 0), new LevelStuff08(24, -1, 65, 2, 3, 2, 3), new LevelStuff08(48, -1, 65, 3, 6, 3, 6), new LevelStuff08(72, -1, 65, 5, 9, 5, 9), new LevelStuff08(96, -1, 65, 7, 12, 6, 12), new LevelStuff08(120, -1, 65, 8, 16, 8, 15), new LevelStuff08(144, -1, 65, 10, 19, 9, 19), new LevelStuff08(168, -1, 65, 12, 22, 11, 22), new LevelStuff08(192, -1, 65, 13, 26, 12, 25), new LevelStuff08(216, -1, 65, 15, 29, 14, 28), new LevelStuff08(240, -1, 65, 17, 32, 15, 31), new LevelStuff08(264, -1, 65, 19, 36, 17, 35), new LevelStuff08(330, -1, 65, 20, 41, 18, 41), new LevelStuff08(396, -1, 65, 21, 47, 20, 47), new LevelStuff08(463, -1, 65, 22, 53, 22, 53), new LevelStuff08(529, -1, 65, 23, 58, 24, 59), new LevelStuff08(596, -1, 65, 24, 64, 25, 65), new LevelStuff08(662, -1, 65, 26, 70, 27, 71), new LevelStuff08(728, -1, 65, 27, 75, 29, 77), new LevelStuff08(795, -1, 65, 28, 81, 31, 83), new LevelStuff08(861, -1, 65, 29, 87, 33, 89), new LevelStuff08(928, -1, 65, 30, 93, 34, 95), new LevelStuff08(1017, -1, 65, 32, 98, 36, 101), new LevelStuff08(1107, -1, 65, 33, 104, 37, 106), new LevelStuff08(1196, -1, 65, 35, 109, 39, 111), new LevelStuff08(1286, -1, 65, 36, 115, 40, 117), new LevelStuff08(1376, -1, 65, 38, 120, 42, 122), new LevelStuff08(1465, -1, 65, 39, 126, 43, 128), new LevelStuff08(1555, -1, 65, 40, 131, 45, 133), new LevelStuff08(1644, -1, 65, 42, 137, 46, 139), new LevelStuff08(1734, -1, 65, 43, 143, 48, 144), new LevelStuff08(1824, -1, 65, 45, 148, 50, 150), new LevelStuff08(1919, -1, 65, 47, 153, 51, 155), new LevelStuff08(2014, -1, 65, 48, 158, 53, 160), new LevelStuff08(2109, -1, 65, 50, 164, 54, 165), new LevelStuff08(2204, -1, 65, 52, 169, 56, 170), new LevelStuff08(2300, -1, 65, 54, 174, 57, 176), new LevelStuff08(2395, -1, 65, 56, 179, 59, 181), new LevelStuff08(2490, -1, 65, 58, 184, 60, 186), new LevelStuff08(2585, -1, 65, 60, 189, 62, 191), new LevelStuff08(2680, -1, 65, 61, 194, 63, 196), new LevelStuff08(2776, -1, 65, 63, 200, 65, 202), new LevelStuff08(2983, -1, 65, 65, 202, 68, 204), new LevelStuff08(3190, -1, 65, 67, 205, 72, 207), new LevelStuff08(3397, -1, 65, 69, 208, 75, 209), new LevelStuff08(3604, -1, 65, 71, 211, 79, 212), new LevelStuff08(3812, -1, 65, 73, 214, 83, 215), new LevelStuff08(4019, -1, 65, 76, 217, 86, 217), new LevelStuff08(4226, -1, 65, 78, 220, 90, 220), new LevelStuff08(4433, -1, 65, 80, 223, 93, 222), new LevelStuff08(4640, -1, 65, 82, 226, 97, 225), new LevelStuff08(4848, -1, 65, 84, 229, 101, 228), new LevelStuff08(4976, -1, 65, 86, 232, 106, 231), new LevelStuff08(5104, -1, 65, 88, 235, 111, 234), new LevelStuff08(5232, -1, 65, 91, 238, 117, 237), new LevelStuff08(5360, -1, 65, 93, 240, 122, 240), new LevelStuff08(5488, -1, 65, 95, 243, 127, 243), new LevelStuff08(5616, -1, 65, 97, 246, 133, 246), new LevelStuff08(5744, -1, 65, 100, 249, 138, 249), new LevelStuff08(5872, -1, 65, 102, 252, 143, 252), new LevelStuff08(6000, -1, 65, 104, 255, 149, 254), },
  };
  public static final MagicStuff08[][] magicStuff_80111d20 = {
    {new MagicStuff08(0, -1, 255, 255, 255, 255, 255), new MagicStuff08(20, 0, 255, 150, 150, 200, 200), new MagicStuff08(40, 1, 255, 155, 155, 210, 210), new MagicStuff08(60, 2, 255, 160, 160, 220, 220), new MagicStuff08(80, -1, 255, 165, 165, 230, 230), new MagicStuff08(100, 3, 255, 170, 170, 250, 250), },
    {new MagicStuff08(0, -1, 255, 255, 255, 255, 255), new MagicStuff08(20, 5, 255, 150, 200, 200, 200), new MagicStuff08(40, 7, 255, 155, 205, 210, 210), new MagicStuff08(60, 6, 255, 160, 210, 220, 220), new MagicStuff08(80, -1, 255, 165, 215, 230, 230), new MagicStuff08(100, 8, 255, 170, 220, 250, 250), },
    {new MagicStuff08(0, -1, 255, 255, 255, 255, 255), new MagicStuff08(20, 11, 255, 200, 150, 200, 200), new MagicStuff08(40, 10, 255, 205, 155, 210, 210), new MagicStuff08(60, 12, 255, 210, 160, 220, 220), new MagicStuff08(80, -1, 255, 215, 165, 230, 230), new MagicStuff08(100, 13, 255, 220, 170, 250, 250), },
    {new MagicStuff08(0, -1, 255, 255, 255, 255, 255), new MagicStuff08(20, 15, 255, 150, 150, 200, 200), new MagicStuff08(40, 16, 255, 155, 155, 210, 210), new MagicStuff08(60, 18, 255, 160, 160, 220, 220), new MagicStuff08(80, -1, 255, 165, 165, 230, 230), new MagicStuff08(100, 19, 255, 170, 170, 250, 250), },
    {new MagicStuff08(0, -1, 255, 255, 255, 255, 255), new MagicStuff08(20, 20, 255, 150, 200, 200, 200), new MagicStuff08(40, 21, 255, 155, 205, 210, 210), new MagicStuff08(60, 22, 255, 160, 210, 220, 220), new MagicStuff08(80, -1, 255, 165, 215, 230, 230), new MagicStuff08(100, 23, 255, 170, 220, 250, 250), },
    {new MagicStuff08(0, -1, 255, 255, 255, 255, 255), new MagicStuff08(20, 14, 255, 150, 200, 200, 200), new MagicStuff08(40, 26, 255, 155, 205, 210, 210), new MagicStuff08(60, 17, 255, 160, 210, 220, 220), new MagicStuff08(80, -1, 255, 165, 215, 230, 230), new MagicStuff08(100, 8, 255, 170, 220, 250, 250), },
    {new MagicStuff08(0, -1, 255, 255, 255, 255, 255), new MagicStuff08(20, 24, 255, 200, 150, 200, 200), new MagicStuff08(40, 25, 255, 205, 155, 210, 210), new MagicStuff08(60, 27, 255, 210, 160, 220, 220), new MagicStuff08(80, -1, 255, 215, 165, 230, 230), new MagicStuff08(100, 28, 255, 220, 170, 250, 250), },
    {new MagicStuff08(0, -1, 255, 255, 255, 255, 255), new MagicStuff08(20, 29, 255, 150, 200, 200, 200), new MagicStuff08(40, -1, 255, 155, 205, 210, 210), new MagicStuff08(60, 30, 255, 160, 210, 220, 220), new MagicStuff08(80, -1, 255, 165, 215, 230, 230), new MagicStuff08(100, 31, 255, 170, 220, 250, 250), },
    {new MagicStuff08(0, -1, 255, 255, 255, 255, 255), new MagicStuff08(20, 66, 255, 200, 150, 200, 200), new MagicStuff08(40, 65, 255, 205, 155, 210, 210), new MagicStuff08(60, 67, 255, 210, 160, 220, 220), new MagicStuff08(80, -1, 255, 215, 165, 230, 230), new MagicStuff08(100, 13, 255, 220, 170, 250, 250), },
  };

  public static final int[] kongolXpTable_801134f0 = new int[61];
  public static final int[] dartXpTable_801135e4 = new int[61];
  public static final int[] haschelXpTable_801136d8 = new int[61];
  public static final int[] meruXpTable_801137cc = new int[61];
  public static final int[] lavitzXpTable_801138c0 = new int[61];
  public static final int[] albertXpTable_801138c0 = new int[61];
  public static final int[] roseXpTable_801139b4 = new int[61];
  public static final int[] shanaXpTable_80113aa8 = new int[61];
  public static final int[] mirandaXpTable_80113aa8 = new int[61];
  public static final int[][] xpTables = {dartXpTable_801135e4, lavitzXpTable_801138c0, shanaXpTable_80113aa8, roseXpTable_801139b4, haschelXpTable_801136d8, albertXpTable_801138c0, meruXpTable_801137cc, kongolXpTable_801134f0, mirandaXpTable_80113aa8};

  public static final Addition04[][] additions_80114070 = {
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 5), new Addition04(50, 0, 10), new Addition04(75, 0, 20), new Addition04(100, 0, 35)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 20, 5), new Addition04(50, 40, 10), new Addition04(75, 60, 15), new Addition04(100, 80, 25)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 50, 0), new Addition04(50, 100, 0), new Addition04(75, 150, 0), new Addition04(100, 240, 0)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 25, 15), new Addition04(50, 50, 30), new Addition04(75, 75, 45), new Addition04(100, 100, 67)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 50, 0), new Addition04(50, 100, 0), new Addition04(75, 150, 0), new Addition04(100, 240, 0)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 20), new Addition04(50, 0, 40), new Addition04(75, 0, 60), new Addition04(100, 0, 75)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 10, 20), new Addition04(50, 20, 40), new Addition04(75, 30, 60), new Addition04(100, 50, 80)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 10, 10), new Addition04(50, 20, 20), new Addition04(75, 30, 30), new Addition04(100, 45, 50)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 25), new Addition04(50, 0, 50), new Addition04(75, 0, 75), new Addition04(100, 0, 100)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 50, 8), new Addition04(50, 100, 16), new Addition04(75, 150, 24), new Addition04(100, 240, 35)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 20), new Addition04(50, 0, 40), new Addition04(75, 0, 60), new Addition04(100, 0, 75)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 50, 8), new Addition04(50, 100, 16), new Addition04(75, 150, 24), new Addition04(100, 240, 35)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 25), new Addition04(50, 0, 50), new Addition04(75, 0, 75), new Addition04(100, 0, 100)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 50, 0), new Addition04(50, 100, 0), new Addition04(75, 150, 0), new Addition04(100, 240, 0)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 50), new Addition04(50, 0, 100), new Addition04(75, 0, 150), new Addition04(100, 0, 200)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 40), new Addition04(50, 0, 80), new Addition04(75, 0, 120), new Addition04(100, 0, 150)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 10, 10), new Addition04(50, 20, 20), new Addition04(75, 30, 30), new Addition04(100, 45, 50)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 25), new Addition04(50, 0, 50), new Addition04(75, 0, 75), new Addition04(100, 0, 100)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 10), new Addition04(50, 0, 20), new Addition04(75, 0, 30), new Addition04(100, 0, 50)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 20, 10), new Addition04(50, 40, 20), new Addition04(75, 60, 30), new Addition04(100, 75, 50)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 25, 8), new Addition04(50, 50, 16), new Addition04(75, 75, 24), new Addition04(100, 100, 35)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 50, 0), new Addition04(50, 100, 0), new Addition04(75, 150, 0), new Addition04(100, 240, 0)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 30), new Addition04(50, 0, 60), new Addition04(75, 0, 90), new Addition04(100, 0, 134)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 50), new Addition04(50, 0, 100), new Addition04(75, 0, 150), new Addition04(100, 0, 200)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(0, 0, 0)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 10, 10), new Addition04(50, 20, 20), new Addition04(75, 30, 30), new Addition04(100, 45, 50)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 8), new Addition04(50, 0, 16), new Addition04(75, 0, 24), new Addition04(100, 0, 35)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 25, 0), new Addition04(50, 50, 0), new Addition04(75, 75, 0), new Addition04(100, 102, 0)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 10, 25), new Addition04(50, 20, 50), new Addition04(75, 30, 75), new Addition04(99, 45, 100)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 25), new Addition04(50, 0, 50), new Addition04(75, 0, 75), new Addition04(99, 0, 100)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 50, 15), new Addition04(50, 100, 30), new Addition04(75, 150, 45), new Addition04(99, 200, 67)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 0), new Addition04(50, 0, 0), new Addition04(75, 0, 0), new Addition04(99, 0, 0)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 10, 10), new Addition04(50, 20, 20), new Addition04(75, 30, 30), new Addition04(99, 45, 50)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 25), new Addition04(50, 0, 50), new Addition04(75, 0, 75), new Addition04(99, 0, 100)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 50, 8), new Addition04(50, 100, 16), new Addition04(75, 150, 24), new Addition04(99, 240, 35)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 20), new Addition04(50, 0, 40), new Addition04(75, 0, 60), new Addition04(99, 0, 75)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 50, 8), new Addition04(50, 100, 16), new Addition04(75, 150, 24), new Addition04(99, 240, 35)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 0), new Addition04(50, 0, 0), new Addition04(75, 0, 0), new Addition04(99, 0, 0)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 0), new Addition04(50, 0, 0), new Addition04(75, 0, 0), new Addition04(99, 0, 0)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 0), new Addition04(50, 0, 0), new Addition04(75, 0, 0), new Addition04(99, 0, 0)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 0), new Addition04(50, 0, 0), new Addition04(75, 0, 0), new Addition04(99, 0, 0)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 0), new Addition04(50, 0, 0), new Addition04(75, 0, 0), new Addition04(99, 0, 0)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 0), new Addition04(50, 0, 0), new Addition04(75, 0, 0), new Addition04(99, 0, 0)},
    {new Addition04(0, 0, 0), new Addition04(0, 0, 0), new Addition04(25, 0, 0), new Addition04(50, 0, 0), new Addition04(75, 0, 0), new Addition04(99, 0, 0)},
  };

  public static final MenuGlyph06[] charSwapGlyphs_80114160 = {
    new MenuGlyph06(69, 0, 0),
    new MenuGlyph06(70, 192, 0),
    new MenuGlyph06(75, 16, 83),
    new MenuGlyph06(75, 16, 155),
  };
  public static final MenuGlyph06[] equipmentGlyphs_80114180 = {
    new MenuGlyph06(69, 0, 0),
    new MenuGlyph06(70, 192, 0),
    new MenuGlyph06(75, 16, 88),
    new MenuGlyph06(75, 194, 92),
    new MenuGlyph06(75, 194, 173),
  };
  public static final MenuGlyph06[] characterStatusGlyphs_801141a4 = {
    new MenuGlyph06(69, 0, 0),
    new MenuGlyph06(70, 192, 0),
    new MenuGlyph06(75, 16, 88),
    new MenuGlyph06(75, 194, 94),
  };
  public static final MenuGlyph06[] additionGlyphs_801141e4 = {
    new MenuGlyph06(75, 16, 88),
    new MenuGlyph06(92, 16, 94),
    new MenuGlyph06(93, 184, 94),
  };
  public static final MenuGlyph06[] useItemGlyphs_801141fc = {
    new MenuGlyph06(69, 0, 0),
    new MenuGlyph06(70, 192, 0),
    new MenuGlyph06(81, 16, 108),
    new MenuGlyph06(82, 192, 108),
    new MenuGlyph06(157, 16, 108),
    new MenuGlyph06(158, 192, 108),
  };
  public static final MenuGlyph06[] dabasMenuGlyphs_80114228 = {
    new MenuGlyph06(69, 0, 0),
    new MenuGlyph06(70, 192, 0),
    new MenuGlyph06(160, 16, 16),
    new MenuGlyph06(161, 192, 16),
  };

  public static final int[] characterValidEquipment_80114284 = {0x80, 0x40, 0x2, 0x4, 0x10, 0x40, 0x1, 0x20, 0x2};

  public static final int[] spellMp_80114290 = {
    10, 20, 30, 80, 50, 20, 30, 20,
    80, 50, 20, 10, 30, 80, 20, 10,
    20, 30, 30, 80, 10, 20, 30, 80,
    10, 20, 20, 30, 80, 20, 30, 80,
    0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0,
    0, 20, 10, 30,
  };
  public static final MenuGlyph06 glyph_801142d4 = new MenuGlyph06(0, 202, 24);

  public static final String[] chapterNames_80114248 = {
    "Ch.1 Serdian War",
    "Ch.2 Platinum Shadow",
    "Ch.3 Fate & Soul",
    "Ch.4 Moon & Fate",
  };

  public static final String[] characterNames_801142dc = {
    "Dart", "Lavitz", "Shana", "Rose", "Haschel",
    "Albert", "Meru", "Kongol", "Miranda",
  };

  public static final int[] itemPrices_80114310 = {
    10, 30, 75, 125, 175, 200, 250, 225,
    100, 150, 175, 200, 250, 30, 100, 150,
    175, 200, 250, 80, 10, 50, 125, 150,
    200, 250, 70, 10, 25, 75, 125, 175,
    200, 250, 100, 125, 150, 200, 250, 200,
    50, 125, 150, 225, 250, 175, 10, 25,
    75, 100, 150, 400, 400, 75, 125, 200,
    400, 30, 75, 125, 150, 400, 10, 25,
    75, 100, 150, 400, 400, 400, 250, 250,
    250, 250, 5000, 1, 5, 20, 50, 75,
    100, 100, 5, 30, 75, 100, 125, 1,
    300, 5000, 250, 250, 1, 5, 50, 75,
    5, 50, 75, 150, 250, 150, 1, 100,
    100, 100, 150, 100, 150, 150, 200, 100,
    100, 300, 300, 500, 500, 500, 150, 150,
    300, 500, 500, 500, 250, 250, 250, 250,
    250, 250, 250, 250, 250, 250, 250, 250,
    250, 250, 250, 250, 250, 100, 500, 500,
    500, 1, 500, 1, 500, 5000, 2500, 2500,
    5, 50, 50, 25, 500, 5000, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1,

    2, 5, 5, 5, 1, 5, 5, 5,
    50, 5, 5, 5, 15, 10, 5, 10,
    10, 10, 10, 10, 10, 1, 10, 10,
    10, 10, 10, 1, 10, 10, 15, 2,
    200, 50, 1, 200, 200, 25, 200, 15,
    200, 60, 100, 200, 200, 200, 200, 1,
    200, 10, 10, 10, 10, 10, 10, 10,
    10, 25, 200, 0, 0, 0, 0, 0,
  };
  public static final MenuGlyph06[] glyphs_80114510 = {
    new MenuGlyph06(69, 0, 0),
    new MenuGlyph06(70, 192, 0),
    new MenuGlyph06(96, 40, 56),
    new MenuGlyph06(97, 146, 16),
    new MenuGlyph06(91, 16, 123),
    new MenuGlyph06(91, 194, 123),
    new MenuGlyph06(98, 16, 172),
    new MenuGlyph06(99, 192, 172),
  };
  public static final MenuGlyph06[] glyphs_80114548 = {
    new MenuGlyph06(69, 0, 0),
    new MenuGlyph06(70, 192, 0),
    new MenuGlyph06(101, 16, 16),
    new MenuGlyph06(152, 16, 134),
    new MenuGlyph06(85, 194, 16),
    new MenuGlyph06(91, 194, 164),
  };

  public static final String[] additions_8011a064 = {
    "Double Slash", "Volcano", "Burning Rush", "Crush Dance", "Madness Hero",
    "Moon Strike", "Blazing Dynamo", "Dragoon Attack", "Harpoon", "Spinning Cane",
    "Rod Typhoon", "Gust of Wind Dance", "Flower Storm", "Dragoon Attack", "Whip Smack",
    "More & More", "Hard Blade", "Demon's Dance", "Dragoon Attack", "Pursuit",
    "Inferno", "Bone Crush", "Dragoon Attack", "Double Smack", "Hammer Spin",
    "Cool Boogie", "Cat's Cradle", "Perky Step", "Dragoon Attack", "Double Punch",
    "Flurry of Styx", "Summon 4 Gods", "5 Ring Shattering", "Hex Hammer", "Omni-Sweep",
    "Dragoon Attack", "Harpoon", "Spinning Cane", "Rod Typhoon", "Gust of Wind Dance",
    "Flower Storm", "Dragoon Attack", "Dragoon Attack",
  };

  public static final String[] goodsDescriptions_8011b75c = {
    "Dragoon Spirit \nhis father left \nhim. Fire-based.", "Dragoon Spirit \nfrom Lenus \nWater-based.", "Dragoon Spirit \nfrom Greham\nWind-based.", "Dragoon Spirit \nfound in Lohan\nEarth-based.", "Dragoon Spirit \nfrom Doel\nThunder-based.",
    "Dragoon Spirit\nfrom Shirley\nLight-based.", "Dragoon Spirit \nof mysterious Rose\nDarkness-based.", "Dragoon Spirit \nof Divine Dragon.", "Special Edition \nshows tension among\nSandora & Serdio.", "A mysterious stone \nfrom Dart's Father\nGlows when held.",
    "Key to the second \nprison tower where\nShana is held.", "An axe left in a\nshack in a field.\nIt's well-worn.", "Good spirit that\npleases the \nman in Bale.", "Dabas forced him. \nSeems useless, but\nkept as a memento.", "A bottle acquired\nin Lohan to hold\n\"Life Water\".",
    "Life water from\na monster plant.\nRefreshes power.", "Fuel to light an\nelevator switch.", "Yellow Stone \nhidden in the \nBlack Castle.", "Blue Stone held by\nthe Spell Master \nMagi.", "Red Stone kept by\nthe janitor of the\nBlack Castle.",
    "Letter that tells \nof going alone to \nthe Gehrich Gang.", "A pass for\nZero Gravity \nValley.", "Good luck bouquet\nthrown by Kate at\nthe wedding.", "Key from the \nPhantom Ship \nCaptain.", "License to use a\nboat in Furni.\nIt's a must.",
    "A staff to confine\nDivine Dragon. \nRestrains Dragons.", "Family treasure \nof Serdio. Has\nenormous power.", "Family treasure \nof Tiberoa. Taken\naway.", "Family treasure \nof Mille Seseau.\nHidden by Flamvel.", "Attacking spell \nprepared by Savan\nof Aglis.",
    "Ultimate attack \nspell given by \nSavan for Rose.", "A certificate of\nlaw production\nin Zenebatos.", "A certificate of\nlaw enactment\nin Zenebatos.", "Dragoon Spirit \nIndora gave Kongol\nEarth-based.", "Dabas' magical\nbag. Items are \nteleported to bag.",
    "A mysterious stone \nfrom Martel for\ngetting Stardust.", "Lavitz's portrait\ndrawn in Bale. It\nlooks so real.", "Temporary event \nitem description37", "Temporary event \nitem description38", "Temporary event \nitem description39",
    "Temporary event \nitem description40", "Temporary event \nitem description41", "Temporary event \nitem description42", "Temporary event \nitem description43", "Temporary event \nitem description44",
    "Temporary event \nitem description45", "Temporary event \nitem description46", "Temporary event \nitem description47", "Temporary event \nitem description48", "Temporary event \nitem description49",
    "Temporary event \nitem description50", "Temporary event \nitem description51", "Temporary event \nitem description52", "Temporary event \nitem description53", "Temporary event \nitem description54",
    "Temporary event \nitem description55", "Temporary event \nitem description56", "Temporary event \nitem description57", "Temporary event \nitem description58", "Temporary event \nitem description59",
    "Temporary event \nitem description60", "Temporary event \nitem description61", "Temporary event \nitem description62", "Temporary event \nitem description63",
  };
  public static final String[] goodsItemNames_8011c008 = {
    "Red Dragon DS", "Blue Dragon DS", "Jade Dragon DS", "Gold Dragon DS", "Violet Dragon DS",
    "Silver Dragon DS", "Dark Dragon DS", "Divine Dragon DS", "War Bulletin", "Father's Stone",
    "Prison Key", "Axe From the Shack", "Good Spirits", "Shiny Bag", "Water Bottle",
    "Life Water", "Magic Oil", "Yellow Stone", "Blue Stone", "Red Stone",
    "Letter From Lynn", "Pass For Valley", "Kate's Bouquet", "Key to Ship", "Boat License",
    "Dragon Blocker", "Moon Gem", "Moon Dagger", "Moon Mirror", "Omega Bomb",
    "Omega Master", "Law Maker", "Law Output", "Gold Dragon DS", "Magic Shiny Bag",
    "Vanishing Stone", "Lavitz's Picture", "Temporary37", "Temporary38", "Temporary39",
    "Temporary40", "Temporary41", "Temporary42", "Temporary43", "Temporary44",
    "Temporary45", "Temporary46", "Temporary47", "Temporary48", "Temporary49",
    "Temporary50", "Temporary51", "Temporary52", "Temporary53", "Temporary54",
    "Temporary55", "Temporary56", "Temporary57", "Temporary58", "Temporary59",
    "Temporary60", "Temporary61", "Temporary62", "Temporary63",
  };
  public static final String[] submapNames_8011c108 = {
    "", "Forest", "Forest", "Seles", "Hellena Prison",
    "Prairie", "Cave", "", "Bale", "Indels Castle",
    "Town of Hoax", "Marshland", "Vol. Villude", "Nest of Dragon", "Lohan",
    "Shirley's Shrine", "", "Kazas", "Black Castle", "Fletz",
    "Twin Castle", "Barrens", "Donau", "Valley", "Giganto Home",
    "", "The Queen Fury", "Phantom Ship", "Lidiera", "Undersea Cavern",
    "Fueno", "Prison Island", "Furni", "Evergreen Frst", "Deningrad",
    "Crystal Palace", "Neet", "Wingly Forest", "Forbidden Land", "",
    "Mortal Dr Mt.", "", "Kashua Glacier", "Flanvel Tower", "Snowfield",
    "Fort Magrad", "Vellweb", "", "Death Frontier", "Ulara",
    "Zenebatos", "Mayfil", "", "Rouge", "Aglis",
    "Divine Tree", "Moon",
  };
  public static final String[] worldMapNames_8011c1ec = {
    "So. of Serdio",
    "No. of Serdio",
    "Tiberoa",
    "Illisa Bay",
    "Mille Seseau",
    "Gloriano",
    "Death Frontier",
    "Endiness",
  };

  public static int characterCount_8011d7c4;

  public static boolean canSave_8011dc88;

  @ScriptDescription("Gets the maximum number of items a player can carry")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "size")
  public static FlowControl scriptGetMaxItemCount(final RunningScript<?> script) {
    script.params_20[0].set(CONFIG.getConfig(CoreMod.INVENTORY_SIZE_CONFIG.get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the maximum number of equipment a player can carry")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "size")
  public static FlowControl scriptGetMaxEquipmentCount(final RunningScript<?> script) {
    script.params_20[0].set(255);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks whether or not an item slot is used")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "slot")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "used")
  public static FlowControl scriptIsItemSlotUsed(final RunningScript<?> script) {
    final int slot = script.params_20[0].get();
    script.params_20[1].set(slot < gameState_800babc8.items_2e9.size() ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks whether or not an equipment slot is used")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "slot")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "used")
  public static FlowControl scriptIsEquipmentSlotUsed(final RunningScript<?> script) {
    final int slot = script.params_20[0].get();
    script.params_20[1].set(slot < gameState_800babc8.equipment_1e8.size() ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Get the registry ID of an item slot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "slot")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.REG, name = "id")
  public static FlowControl scriptGetItemSlot(final RunningScript<?> script) {
    final int slot = script.params_20[0].get();
    script.params_20[1].set(gameState_800babc8.items_2e9.get(slot).getRegistryId());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Get the registry ID of an equipment slot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "slot")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.REG, name = "id")
  public static FlowControl scriptGetEquipmentSlot(final RunningScript<?> script) {
    final int slot = script.params_20[0].get();
    script.params_20[1].set(gameState_800babc8.equipment_1e8.get(slot).getRegistryId());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Set the registry ID of an item slot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "slot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id")
  public static FlowControl scriptSetItemSlot(final RunningScript<?> script) {
    final int slot = script.params_20[0].get();
    final RegistryId id = script.params_20[1].getRegistryId();
    gameState_800babc8.items_2e9.set(slot, REGISTRIES.items.getEntry(id).get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Set the registry ID of an equipment slot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "slot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id")
  public static FlowControl scriptSetEquipmentSlot(final RunningScript<?> script) {
    final int slot = script.params_20[0].get();
    final RegistryId id = script.params_20[1].getRegistryId();
    gameState_800babc8.equipment_1e8.set(slot, REGISTRIES.equipment.getEntry(id).get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gives an item to the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "given", description = "True if given successfully, false otherwise (e.g. no space)")
  public static FlowControl scriptGiveItem(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    final boolean given = giveItem(REGISTRIES.items.getEntry(id).get());
    script.params_20[1].set(given ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gives equipment to the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "given", description = "True if given successfully, false otherwise (e.g. no space)")
  public static FlowControl scriptGiveEquipment(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    final boolean given = giveEquipment(REGISTRIES.equipment.getEntry(id).get());
    script.params_20[1].set(given ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Takes an item from the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "taken", description = "True if given successfully, false otherwise (e.g. no space)")
  public static FlowControl scriptTakeItem(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    final boolean taken = takeItemId(REGISTRIES.items.getEntry(id).get());
    script.params_20[1].set(taken ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Takes equipment from the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "taken", description = "True if given successfully, false otherwise (e.g. no space)")
  public static FlowControl scriptTakeEquipment(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    final boolean taken = takeEquipmentId(REGISTRIES.equipment.getEntry(id).get());
    script.params_20[1].set(taken ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Picks a random attack item")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.REG, name = "id")
  public static FlowControl scriptGenerateAttackItem(final RunningScript<?> script) {
    final Item[] items = EVENTS.postEvent(new GatherAttackItemsEvent()).getItems();
    final Item item = items[seed_800fa754.nextInt(items.length)];
    script.params_20[0].set(item.getRegistryId());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Picks a random recovery item")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.REG, name = "id")
  public static FlowControl scriptGenerateRecoveryItem(final RunningScript<?> script) {
    final Item[] items = EVENTS.postEvent(new GatherRecoveryItemsEvent()).getItems();
    final Item item = items[seed_800fa754.nextInt(items.length)];
    script.params_20[0].set(item.getRegistryId());
    return FlowControl.CONTINUE;
  }

  @Method(0x800fc698L)
  public static int getXpToNextLevel(final int charIndex) {
    if(charIndex == -1 || charIndex > 8) {
      //LAB_800fc6a4
      throw new RuntimeException("Character index " + charIndex + " out of bounds");
    }

    //LAB_800fc6ac
    final int level = gameState_800babc8.charData_32c[charIndex].level_12;

    if(level >= 60) {
      return 0; // Max level
    }

    final XpToLevelEvent event = EVENTS.postEvent(new XpToLevelEvent(charIndex, level, xpTables[charIndex][level + 1]));

    //LAB_800fc70c
    return event.xp;
  }

  @Method(0x800fc814L)
  public static int FUN_800fc814(final int a0) {
    return 9 + a0 * 17;
  }

  public static void loadMenuAssets() {
    loadDrgnFileSync(0, 6665, data -> menuAssetsLoaded(data, 0));
    loadDrgnFileSync(0, 6666, data -> menuAssetsLoaded(data, 1));
    loadDrgnDir(0, 5815, SItem::menuMusicLoaded);
  }

  @Method(0x800fc944L)
  private static void menuAssetsLoaded(final FileData data, final int whichFile) {
    if(whichFile == 0) {
      //LAB_800fc98c
      loadMenuTexture(data.slice(0x83e0)); // Character textures
      loadMenuTexture(data); // Menu textures
      loadMenuTexture(data.slice(0x6200)); // Item textures
      loadMenuTexture(data.slice(0x1_0460));
      loadMenuTexture(data.slice(0x1_0580));
    } else if(whichFile == 1) {
      //LAB_800fc9e4
      uiFile_800bdc3c = UiFile.fromFile(data);
      uiFile_800bdc3c.uiElements_0000().obj = buildUiRenderable(uiFile_800bdc3c.uiElements_0000(), "Elements");
      uiFile_800bdc3c.itemIcons_c6a4().obj = buildUiRenderable(uiFile_800bdc3c.itemIcons_c6a4(), "Item Icons");
      uiFile_800bdc3c.portraits_cfac().obj = buildUiRenderable(uiFile_800bdc3c.portraits_cfac(), "Portraits");
      uiFile_800bdc3c._d2d8().obj = buildUiRenderable(uiFile_800bdc3c._d2d8(), "d2d8");
    }

    //LAB_800fc9fc
  }

  private static void menuMusicLoaded(final List<FileData> files) {
    menuMusic = new BackgroundMusic(files, 5815);
  }

  /** FUN_8001e010 with param 0 */
  @Method(0x8001e010L)
  public static void startMenuMusic() {
    //LAB_8001e054
    copyPlayingSounds(queuedSounds_800bd110, playingSoundsBackup_800bca78);
    stopAndResetSoundsAndSequences();
    unloadSoundFile(8);
    unloadSoundFile(8);

    menuMusic.reset();
    playMusicPackage(menuMusic, true);
  }

  /** FUN_8001e010 with param -1 */
  @Method(0x8001e010L)
  public static void stopMenuMusic() {
    //LAB_8001e044
    //LAB_8001e0f8
    if(loadingNewGameState_800bdc34) {
      if(engineState_8004dd20 == EngineStateEnum.WORLD_MAP_08 && gameState_800babc8.isOnWorldMap_4e4) {
        sssqResetStuff();
        unloadSoundFile(8);
        loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x80);
        loadDrgnDir(0, 5850, files -> musicPackageLoadedCallback(files, 5850, true));
      }
    } else {
      //LAB_8001e160
      stopMusicSequence();
      unloadSoundFile(8);

      currentEngineState_8004dd04.restoreMusicAfterMenu();
    }

    //LAB_8001e26c
    stopAndResetSoundsAndSequences();
    copyPlayingSounds(playingSoundsBackup_800bca78, queuedSounds_800bd110);
    playingSoundsBackup_800bca78.clear();
  }

  @Method(0x801033ccL)
  public static void FUN_801033cc(final Renderable58 a0) {
    a0.deallocationGroup_28 = 0x1;
    a0.heightScale_38 = 0;
    a0.widthScale = 0;
    a0.z_3c = 31;
  }

  @Method(0x801033e8L)
  public static void fadeOutArrow(final Renderable58 renderable) {
    unloadRenderable(renderable);

    final Renderable58 newRenderable = allocateUiElement(108, 111, renderable.x_40, renderable.y_44);
    newRenderable.flags_00 |= Renderable58.FLAG_DELETE_AFTER_ANIMATION;
    FUN_801033cc(newRenderable);
  }

  @Method(0x80103444L)
  public static void setRandomRepeatGlyph(@Nullable final Renderable58 renderable, final int repeatStartGlyph1, final int repeatEndGlyph1, final int repeatStartGlyph2, final int repeatEndGlyph2) {
    if(renderable != null) {
      if(renderable.repeatStartGlyph_18 == 0) {
        if((simpleRand() & 0x3000) != 0) {
          renderable.repeatStartGlyph_18 = repeatStartGlyph1;
          renderable.repeatEndGlyph_1c = repeatEndGlyph1;
        } else {
          //LAB_801034a0
          renderable.repeatStartGlyph_18 = repeatStartGlyph2;
          renderable.repeatEndGlyph_1c = repeatEndGlyph2;
        }
      }
    }

    //LAB_801034b0
  }

  @Method(0x801034ccL)
  public static void FUN_801034cc(final int charSlot, final int charCount) {
    setRandomRepeatGlyph(renderablePtr_800bdba4, 0x2d, 0x34, 0xaa, 0xb1);
    setRandomRepeatGlyph(renderablePtr_800bdba8, 0x25, 0x2c, 0xa2, 0xa9);

    if(charSlot != 0) {
      if(renderablePtr_800bdba4 == null) {
        final Renderable58 renderable = allocateUiElement(0x6f, 0x6c, 18, 16);
        renderable.repeatStartGlyph_18 = 0x2d;
        renderable.repeatEndGlyph_1c = 0x34;
        renderablePtr_800bdba4 = renderable;
        FUN_801033cc(renderable);
      }
    } else {
      //LAB_80103578
      if(renderablePtr_800bdba4 != null) {
        fadeOutArrow(renderablePtr_800bdba4);
        renderablePtr_800bdba4 = null;
      }
    }

    //LAB_80103598
    if(charSlot < charCount - 1) {
      if(renderablePtr_800bdba8 == null) {
        final Renderable58 renderable = allocateUiElement(0x6f, 0x6c, 350, 16);
        renderable.repeatStartGlyph_18 = 0x25;
        renderable.repeatEndGlyph_1c = 0x2c;
        renderablePtr_800bdba8 = renderable;
        FUN_801033cc(renderable);
      }
      //LAB_801035e8
    } else if(renderablePtr_800bdba8 != null) {
      fadeOutArrow(renderablePtr_800bdba8);
      renderablePtr_800bdba8 = null;
    }

    //LAB_80103604
  }

  @Method(0x8010376cL)
  public static void renderGlyphs(final MenuGlyph06[] glyphs, final int x, final int y) {
    //LAB_801037ac
    for(int i = 0; i < glyphs.length; i++) {
      final Renderable58 s0 = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);

      initGlyph(s0, glyphs[i]);

      s0.x_40 += x;
      s0.y_44 += y;
    }

    //LAB_801037f4
  }

  public static Renderable58 allocateUiElement(final Renderable58 renderable, final int startGlyph, final int endGlyph, final int x, final int y) {
    if(endGlyph >= startGlyph) {
      renderable.glyph_04 = startGlyph;
      renderable.startGlyph_10 = startGlyph;
      renderable.endGlyph_14 = endGlyph;
    } else {
      //LAB_80103870
      renderable.glyph_04 = startGlyph;
      renderable.startGlyph_10 = endGlyph;
      renderable.endGlyph_14 = startGlyph;
      renderable.flags_00 |= Renderable58.FLAG_BACKWARDS_ANIMATION;
    }

    //LAB_80103888
    if(startGlyph == endGlyph) {
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
    }

    //LAB_801038a4
    renderable.tpage_2c = 0x19;
    renderable.clut_30 = 0;
    renderable.x_40 = x;
    renderable.y_44 = y;

    return renderable;
  }

  @Method(0x80103818L)
  public static Renderable58 allocateUiElement(final int startGlyph, final int endGlyph, final int x, final int y) {
    final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
    return allocateUiElement(renderable, startGlyph, endGlyph, x, y);
  }

  @Method(0x80103910L)
  public static Renderable58 renderItemIcon(final int glyph, final int x, final int y, final int flags) {
    final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.itemIcons_c6a4(), null);
    renderable.flags_00 |= flags | Renderable58.FLAG_NO_ANIMATION;
    renderable.glyph_04 = glyph;
    renderable.startGlyph_10 = glyph;
    renderable.endGlyph_14 = glyph;
    renderable.tpage_2c = 0x19;
    renderable.clut_30 = 0;
    renderable.x_40 = x;
    renderable.y_44 = y;
    return renderable;
  }

  @Method(0x801039a0L)
  public static boolean canEquip(final Equipment equipment, final int charIndex) {
    return (characterValidEquipment_80114284[charIndex] & equipment.equipableFlags_03) != 0;
  }

  /**
   * @return Item ID of previously-equipped item, 0xff if invalid, 0x100 if no item was equipped
   */
  @Method(0x80103a5cL)
  public static EquipItemResult equipItem(final Equipment equipment, final int charIndex) {
    if((!canEquip(equipment, charIndex))) {
      return EquipItemResult.failure();
    }

    //LAB_80103ac0
    final CharacterData2c charData = gameState_800babc8.charData_32c[charIndex];
    final Equipment previousEquipment = charData.equipment_14.get(equipment.slot);
    charData.equipment_14.put(equipment.slot, equipment);

    //LAB_80103af4
    //LAB_80103af8
    return EquipItemResult.success(previousEquipment);
  }

  @Method(0x80103b10L)
  public static void cacheCharacterSlots() {
    characterCount_8011d7c4 = 0;

    //LAB_80103b48
    int usedCharacterSlots = 0;
    for(int slot = 0; slot < 9; slot++) {
      secondaryCharIds_800bdbf8[slot] = -1;
      characterIndices_800bdbb8[slot] = -1;

      if((gameState_800babc8.charData_32c[slot].partyFlags_04 & 0x1) != 0) {
        characterIndices_800bdbb8[characterCount_8011d7c4] = slot;
        characterCount_8011d7c4++;

        if(gameState_800babc8.charIds_88[0] != slot && gameState_800babc8.charIds_88[1] != slot && gameState_800babc8.charIds_88[2] != slot) {
          secondaryCharIds_800bdbf8[usedCharacterSlots] = slot;
          usedCharacterSlots++;
        }
      }

      //LAB_80103bb4
    }
  }

  @Method(0x80103cc4L)
  public static void renderText(final String text, final int x, final int y, final TextColour colour) {
    final TextColour shadowColour;
    if(colour == TextColour.WHITE) {
      shadowColour = TextColour.BLACK;
    } else if(colour == TextColour.LIME) {
      //LAB_80103d18
      shadowColour = TextColour.GREEN;
    } else if(colour == TextColour.MIDDLE_BROWN) {
      //LAB_80103d20
      shadowColour = TextColour.LIGHT_BROWN;
    } else {
      shadowColour = TextColour.MIDDLE_BROWN;
    }

    //LAB_80103d24
    //LAB_80103d28
    Scus94491BpeSegment_8002.renderText(text, x    , y    , colour, 0);
    Scus94491BpeSegment_8002.renderText(text, x    , y + 1, shadowColour, 0);
    Scus94491BpeSegment_8002.renderText(text, x + 1, y    , shadowColour, 0);
    Scus94491BpeSegment_8002.renderText(text, x + 1, y + 1, shadowColour, 0);
  }

  @Method(0x80103e90L)
  public static void renderCentredText(final String text, final int x, final int y, final TextColour colour) {
    renderText(text, x - textWidth(text) / 2, y, colour);
  }

  @Method(0x80103e90L)
  public static void renderCentredText(final String text, final int x, int y, final TextColour colour, final int maxWidth) {
    final String[] split;
    if(textWidth(text) <= maxWidth) {
      split = new String[] {text};
    } else {
      final List<String> temp = new ArrayList<>();
      int currentWidth = 0;
      int startIndex = 0;
      for(int i = 0; i < text.length(); i++) {
        final char current = text.charAt(i);
        final int charWidth = Scus94491BpeSegment_8002.charWidth(current);

        if(currentWidth + charWidth > maxWidth) {
          boolean advanceOverSpace = false;
          for(int backtrack = 0; backtrack < 10; backtrack++) {
            if(text.charAt(i - backtrack) == ' ') {
              i -= backtrack;
              advanceOverSpace = true;
              break;
            }
          }

          temp.add(text.substring(startIndex, i));
          currentWidth = charWidth;
          startIndex = i;

          if(advanceOverSpace) {
            startIndex++;
          }
        } else {
          currentWidth += charWidth;
        }
      }

      temp.add(text.substring(startIndex));
      split = temp.toArray(String[]::new);
    }

    for(int i = 0; i < split.length; i++) {
      final String str = split[i];
      renderText(str, x - textWidth(str) / 2, y, colour);
      y += textHeight(str);
    }
  }

  @Method(0x801038d4L)
  public static Renderable58 allocateOneFrameGlyph(final int glyph, final int x, final int y) {
    final Renderable58 renderable = allocateUiElement(glyph, glyph, x, y);
    renderable.flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
    return renderable;
  }

  @Method(0x80104738L)
  public static void loadItemsAndEquipmentForDisplay(@Nullable final MenuEntries<Equipment> equipments, @Nullable final MenuEntries<Item> items, final long a0) {
    if(items != null) {
      items.clear();

      for(int i = 0; i < gameState_800babc8.items_2e9.size(); i++) {
        final Item item = gameState_800babc8.items_2e9.get(i);
        final MenuEntryStruct04<Item> menuEntry = MenuEntryStruct04.make(item);
        items.add(menuEntry);
      }
    }

    if(equipments != null) {
      equipments.clear();

      int equipmentIndex;
      for(equipmentIndex = 0; equipmentIndex < gameState_800babc8.equipment_1e8.size(); equipmentIndex++) {
        final Equipment equipment = gameState_800babc8.equipment_1e8.get(equipmentIndex);
        final MenuEntryStruct04<Equipment> menuEntry = MenuEntryStruct04.make(equipment);

        if(a0 != 0 && !gameState_800babc8.equipment_1e8.get(equipmentIndex).canBeDiscarded()) {
          menuEntry.flags_02 = 0x2000;
        }

        equipments.add(menuEntry);
      }

      if(a0 == 0) {
        for(int i = 0; i < characterCount_8011d7c4; i++) {
          for(final EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            if(gameState_800babc8.charData_32c[characterIndices_800bdbb8[i]].equipment_14.get(equipmentSlot) != null) {
              final Equipment equipment = gameState_800babc8.charData_32c[characterIndices_800bdbb8[i]].equipment_14.get(equipmentSlot);
              final MenuEntryStruct04<Equipment> menuEntry = MenuEntryStruct04.make(equipment);
              menuEntry.flags_02 = 0x3000 | characterIndices_800bdbb8[i];
              equipments.add(menuEntry);

              equipmentIndex++;
            }
          }
        }
      }
    }
  }

  public static int loadAdditions(final int charIndex, @Nullable final MenuAdditionInfo[] additions) {
    if(additions != null) {
      for(int i = 0; i < 9; i++) {
        additions[i].offset_00 = -1;
        additions[i].index_01 = -1;
      }
    }

    if(charIndex == -1) {
      return 0;
    }

    if(additionOffsets_8004f5ac[charIndex] == -1) { // No additions (Shiranda)
      return 0;
    }

    int t5 = 0;
    int t0 = 0;
    for(int additionIndex = 0; additionIndex < additionCounts_8004f5c0[charIndex]; additionIndex++) {
      final AdditionUnlockEvent event = EVENTS.postEvent(new AdditionUnlockEvent(additionOffsets_8004f5ac[charIndex] + additionIndex, additionData_80052884[additionOffsets_8004f5ac[charIndex] + additionIndex].level_00));
      additionData_80052884[additionOffsets_8004f5ac[charIndex] + additionIndex].level_00 = event.additionLevel;

      final int level = additionData_80052884[additionOffsets_8004f5ac[charIndex] + additionIndex].level_00;

      if(level == -1 && (gameState_800babc8.charData_32c[charIndex].partyFlags_04 & 0x40) != 0) {
        if(additions != null) {
          additions[t0].offset_00 = additionOffsets_8004f5ac[charIndex] + additionIndex;
          additions[t0].index_01 = additionIndex;
        }

        t0++;
      } else if(level > 0 && level <= gameState_800babc8.charData_32c[charIndex].level_12) {
        if(additions != null) {
          additions[t0].offset_00 = additionOffsets_8004f5ac[charIndex] + additionIndex;
          additions[t0].index_01 = additionIndex;
        }

        if(gameState_800babc8.charData_32c[charIndex].additionLevels_1a[additionIndex] == 0) {
          gameState_800babc8.charData_32c[charIndex].additionLevels_1a[additionIndex] = 1;
        }

        if(level == gameState_800babc8.charData_32c[charIndex].level_12) {
          t5 = additionOffsets_8004f5ac[charIndex] + additionIndex + 1;
        }

        t0++;
      }
    }

    return t5;
  }

  @Method(0x80104b1cL)
  public static void initGlyph(final Renderable58 a0, final MenuGlyph06 glyph) {
    a0.glyph_04 = glyph.glyph_00;
    a0.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
    a0.tpage_2c = 0x19;
    a0.clut_30 = 0;
    a0.x_40 = glyph.x_02;
    a0.y_44 = glyph.y_04;
  }

  @Method(0x80104b60L)
  public static void FUN_80104b60(final Renderable58 a0) {
    a0.deallocationGroup_28 = 0x1;
    a0.widthScale = 0;
    a0.heightScale_38 = 0;
    a0.z_3c = 35;
  }

  @Method(0x80104b7cL)
  public static boolean hasDragoon(final int dragoons, final int charIndex) {
    //LAB_80104b94
    if(charIndex == -1) {
      return false;
    }

    //LAB_80104be0
    if(charIndex == 0 && (dragoons & 0xff) >>> 7 != 0) { // Divine
      return true;
    }

    //LAB_80104c24
    //LAB_80104c28
    return (dragoons & 0x1 << charDragoonSpiritIndices_800fba58[charIndex]) != 0;
  }

  @Method(0x80104c30L)
  public static void renderTwoDigitNumber(final int x, final int y, final int value) {
    renderNumber(x, y, value, 0, 2);
  }

  @Method(0x80104dd4L)
  public static void renderThreeDigitNumber(final int x, final int y, final int value) {
    renderNumber(x, y, value, 0, 3);
  }

  @Method(0x80105048L)
  public static int renderThreeDigitNumberComparison(final int x, final int y, final int currentVal, int newVal) {
    long flags = 0;
    final int clut;
    if(currentVal < newVal) {
      clut = 0x7c6b;
      //LAB_80105090
    } else if(currentVal > newVal) {
      clut = 0x7c2b;
    } else {
      clut = 0;
    }

    //LAB_801050a0
    //LAB_801050a4
    if(newVal > 999) {
      newVal = 999;
    }

    //LAB_801050b0
    int s0 = newVal / 100 % 10;
    if(s0 != 0) {
      //LAB_80105108
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      //LAB_80105138
      //LAB_8010513c
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
      renderable.glyph_04 = s0;
      renderable.tpage_2c = 0x19;
      renderable.clut_30 = clut;
      renderable.z_3c = 0x21;
      renderable.x_40 = x;
      renderable.y_44 = y;
      flags |= 0x1L;
    }

    //LAB_80105190
    s0 = newVal / 10 % 10;
    if(s0 != 0 || (flags & 0x1L) != 0) {
      //LAB_801051ec
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      //LAB_8010521c
      //LAB_80105220
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
      renderable.glyph_04 = s0;
      renderable.tpage_2c = 0x19;
      renderable.clut_30 = clut;
      renderable.z_3c = 0x21;
      renderable.x_40 = x + 6;
      renderable.y_44 = y;
    }

    //LAB_80105274
    s0 = newVal % 10;
    final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
    //LAB_801052d8
    //LAB_801052dc
    renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
    renderable.glyph_04 = s0;
    renderable.tpage_2c = 0x19;
    renderable.clut_30 = clut;
    renderable.z_3c = 0x21;
    renderable.x_40 = x + 12;
    renderable.y_44 = y;
    return clut;
  }

  public static void renderFraction(final int x, final int y, final int numerator, final int denominator) {
    final int width = renderRightAlignedNumber(x, y, denominator, 0);
    allocateUiElement(0xb, 0xb, x - width - 5, y).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
    renderRightAlignedNumber(x - width - 5, y, numerator, 0);
  }

  public static void renderHp(final int x, final int y, final int numerator, final int denominator) {
    final int clut;
    if(numerator <= denominator / 4) {
      clut = 0x7c2b;
      //LAB_80105090
    } else if(numerator <= denominator / 2) {
      clut = 0x7cab;
    } else {
      clut = 0;
    }

    final int width = renderRightAlignedNumber(x, y, denominator, 0);
    allocateUiElement(0xb, 0xb, x - width - 5, y).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
    renderRightAlignedNumber(x - width - 5, y, numerator, clut);
  }

  @Method(0x80105350L)
  public static void renderFourDigitNumber(final int x, final int y, final int value) {
    renderNumber(x, y, value, 0, 4);
  }

  @Method(0x8010568cL)
  public static void renderFourDigitHp(final int x, final int y, final int value, final int max) {
    renderFourDigitHp(x, y, value, max, 0);
  }

  @Method(0x8010568cL)
  public static void renderFourDigitHp(final int x, final int y, int value, final int max, final int renderableFlags) {
    int clut = 0;
    int flags = 0;

    if(value >= 9999) {
      value = 9999;
    }

    //LAB_801056d0
    if(max > 9999) {
      value = 9999;
    }

    //LAB_801056e0
    if(value <= max / 2) {
      clut = 0x7cab;
    }

    //LAB_801056f0
    if(value <= max / 4) {
      clut = 0x7c2b;
    }

    //LAB_80105714
    int s0 = value / 1_000 % 10;
    if(s0 != 0) {
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.glyph_04 = s0;
      //LAB_80105784
      //LAB_80105788
      renderable.flags_00 |= renderableFlags | Renderable58.FLAG_NO_ANIMATION;
      renderable.tpage_2c = 0x19;
      renderable.x_40 = x;
      renderable.y_44 = y;
      renderable.clut_30 = clut;
      renderable.z_3c = 0x21;
      flags |= 0x1;
    }

    //LAB_801057c0
    //LAB_801057d0
    s0 = value / 100 % 10;
    if(s0 != 0 || (flags & 0x1) != 0) {
      //LAB_80105830
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.glyph_04 = s0;
      //LAB_80105860
      //LAB_80105864
      renderable.flags_00 |= renderableFlags | Renderable58.FLAG_NO_ANIMATION;
      renderable.tpage_2c = 0x19;
      renderable.x_40 = x + 6;
      renderable.y_44 = y;
      renderable.clut_30 = clut;
      renderable.z_3c = 0x21;
      flags |= 0x1;
    }

    //LAB_801058a0
    //LAB_801058ac
    s0 = value / 10 % 10;
    if(s0 != 0 || (flags & 0x1) != 0) {
      //LAB_80105908
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.glyph_04 = s0;
      //LAB_80105938
      //LAB_8010593c
      renderable.flags_00 |= renderableFlags | Renderable58.FLAG_NO_ANIMATION;
      renderable.tpage_2c = 0x19;
      renderable.x_40 = x + 12;
      renderable.y_44 = y;
      renderable.clut_30 = clut;
      renderable.z_3c = 0x21;
    }

    //LAB_80105978
    //LAB_80105984
    s0 = value % 10;
    final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
    renderable.glyph_04 = s0;
    //LAB_801059e8
    //LAB_801059ec
    renderable.flags_00 |= renderableFlags | Renderable58.FLAG_NO_ANIMATION;
    renderable.tpage_2c = 0x19;
    renderable.x_40 = x + 18;
    renderable.y_44 = y;
    renderable.clut_30 = clut;
    renderable.z_3c = 0x21;
  }

  @Method(0x80105a50L)
  public static void renderSixDigitNumber(final int x, final int y, int value) {
    long flags = 0;

    if(value > 999999) {
      value = 999999;
    }

    //LAB_80105a98
    int s0 = value / 100_000 % 10;
    if(s0 != 0) {
      final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      struct.glyph_04 = s0;
      //LAB_80105b10
      //LAB_80105b14
      struct.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
      struct.tpage_2c = 0x19;
      struct.clut_30 = 0;
      struct.z_3c = 0x21;
      struct.x_40 = x;
      struct.y_44 = y;
      flags |= 0x1L;
    }

    //LAB_80105b4c
    s0 = value / 10_000 % 10;
    if(s0 != 0 || (flags & 0x1) != 0) {
      //LAB_80105ba8
      final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      struct.glyph_04 = s0;
      //LAB_80105bd8
      //LAB_80105bdc
      struct.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
      struct.tpage_2c = 0x19;
      struct.clut_30 = 0;
      struct.z_3c = 0x21;
      struct.x_40 = x + 6;
      struct.y_44 = y;
      flags |= 0x1L;
    }

    //LAB_80105c18
    s0 = value / 1_000 % 10;
    if(s0 != 0 || (flags & 0x1) != 0) {
      //LAB_80105c70
      final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      struct.glyph_04 = s0;
      //LAB_80105ca0
      //LAB_80105ca4
      struct.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
      struct.tpage_2c = 0x19;
      struct.clut_30 = 0;
      struct.z_3c = 0x21;
      struct.x_40 = x + 12;
      struct.y_44 = y;
      flags |= 0x1L;
    }

    //LAB_80105ce0
    s0 = value / 100 % 10;
    if(s0 != 0 || (flags & 0x1) != 0) {
      //LAB_80105d38
      final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      struct.glyph_04 = s0;
      //LAB_80105d68
      //LAB_80105d6c
      struct.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
      struct.tpage_2c = 0x19;
      struct.clut_30 = 0;
      struct.z_3c = 0x21;
      struct.x_40 = x + 18;
      struct.y_44 = y;
      flags |= 0x1L;
    }

    //LAB_80105da4
    s0 = value / 10 % 10;
    if(s0 != 0 || (flags & 0x1) != 0) {
      //LAB_80105dfc
      final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      struct.glyph_04 = s0;
      //LAB_80105e2c
      //LAB_80105e30
      struct.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
      struct.tpage_2c = 0x19;
      struct.clut_30 = 0;
      struct.z_3c = 0x21;
      struct.x_40 = x + 24;
      struct.y_44 = y;
    }

    //LAB_80105e68
    s0 = value % 10;
    final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
    struct.glyph_04 = s0;
    //LAB_80105ecc
    //LAB_80105ed0
    struct.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
    struct.tpage_2c = 0x19;
    struct.clut_30 = 0;
    struct.z_3c = 0x21;
    struct.x_40 = x + 30;
    struct.y_44 = y;
  }

  @Method(0x80105f2cL)
  public static void renderEightDigitNumber(final int x, final int y, final int value, final int flags) {
    renderNumber(x, y, value, flags, 8);
  }

  @Method(0x801065bcL)
  public static void renderFiveDigitNumber(final int x, final int y, final int value) {
    renderNumber(x, y, value, 0x2, 5);
  }

  public static int renderRightAlignedNumber(final int x, final int y, final int value, final int clut) {
    final int digitCount = MathHelper.digitCount(value);

    int totalWidth = 0;
    for(int i = 0; i < digitCount; i++) {
      final int digit = value / (int)Math.pow(10, i) % 10;

      final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      struct.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
      struct.glyph_04 = digit;
      struct.tpage_2c = 0x19;
      struct.clut_30 = clut;
      struct.z_3c = 0x21;
      struct.x_40 = x - (i + 1) * 6;
      totalWidth += 6;

      struct.y_44 = y;
    }

    return totalWidth;
  }

  /**
   * @param flags Bitset - 0x1: render leading zeros, 0x2: unload at end of frame
   */
  public static void renderNumber(final int x, final int y, int value, int flags, final int digitCount) {
    if(value >= Math.pow(10, digitCount)) {
      value = (int)Math.pow(10, digitCount) - 1;
    }

    for(int i = 0; i < digitCount; i++) {
      final int digit = value / (int)Math.pow(10, digitCount - (i + 1)) % 10;

      if(digit != 0 || i == digitCount - 1 || (flags & 0x1) != 0) {
        final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
        struct.flags_00 |= (flags & 0x2) != 0 ? Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER : Renderable58.FLAG_NO_ANIMATION;
        struct.glyph_04 = digit;
        struct.tpage_2c = 0x19;
        struct.clut_30 = 0;
        struct.z_3c = 33;
        struct.x_40 = x + 6 * i;
        struct.y_44 = y;
        flags |= 0x1;
      }
    }
  }

  @Method(0x80107764L)
  public static void renderThreeDigitNumber(final int x, final int y, final int value, final int flags) {
    renderNumber(x, y, value, flags, 3);
  }

  @Method(0x801079fcL)
  public static void renderTwoDigitNumber(final int x, final int y, final int value, final int flags) {
    renderNumber(x, y, value, flags, 2);
  }

  @Method(0x80107cb4L)
  public static void renderCharacter(final int x, final int y, final int character) {
    final Renderable58 v0 = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
    v0.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
    v0.glyph_04 = character;
    v0.tpage_2c = 0x19;
    v0.clut_30 = 0x7ca9;
    v0.z_3c = 0x21;
    v0.x_40 = x;
    v0.y_44 = y;
  }

  @Method(0x80107d34L)
  public static void renderThreeDigitNumberComparisonWithPercent(final int x, final int y, final int currentVal, final int newVal) {
    final int clut = renderThreeDigitNumberComparison(x, y, currentVal, newVal);
    final Renderable58 v0 = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
    v0.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
    v0.glyph_04 = 0xc;
    v0.tpage_2c = 0x19;
    v0.clut_30 = clut;
    v0.z_3c = 0x21;
    v0.x_40 = x + 20;
    v0.y_44 = y;
  }

  @Method(0x80107dd4L)
  public static void renderXp(final int x, final int y, final int xp) {
    if(xp != 0) {
      renderSixDigitNumber(x, y, xp);
    } else {
      //LAB_80107e08
      final Renderable58 v0 = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      v0.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
      v0.glyph_04 = 218;
      v0.tpage_2c = 0x19;
      v0.clut_30 = 0x7ca9;
      v0.z_3c = 0x21;
      v0.x_40 = x + 30;
      v0.y_44 = y;
    }

    //LAB_80107e58
  }

  @Method(0x80107e70L)
  public static boolean renderCharacterStatusEffect(final int x, final int y, final int charIndex) {
    //LAB_80107e90
    final int status = gameState_800babc8.charData_32c[charIndex].status_10;

    if((tickCount_800bb0fc & 0x10) == 0) {
      return false;
    }

    int statusIndex = status & 0x1;

    if((status & 0x2) != 0) {
      statusIndex = 2;
    }

    //LAB_80107f00
    if((status & 0x4) != 0) {
      statusIndex = 3;
    }

    //LAB_80107f10
    if((status & 0x8) != 0) {
      statusIndex = 4;
    }

    //LAB_80107f1c
    if((status & 0x10) != 0) {
      statusIndex = 5;
    }

    //LAB_80107f28
    if((status & 0x20) != 0) {
      statusIndex = 6;
    }

    //LAB_80107f34
    if((status & 0x40) != 0) {
      statusIndex = 7;
    }

    //LAB_80107f40
    if((status & 0x80) != 0) {
      statusIndex = 8;
    }

    //LAB_80107f50
    if(statusIndex == 0) {
      //LAB_80107f88
      return false;
    }

    final MenuStatus08 menuStatus = menuStatus_800fba7c[statusIndex - 1];
    renderCentredText(menuStatus.text_00, x + 24, y, menuStatus.colour_04);

    //LAB_80107f8c
    return true;
  }

  @Method(0x80107f9cL)
  public static void renderCharacterSlot(final int x, final int y, final int charId, final boolean allocate, final boolean dontSelect) {
    if(charId != -1) {
      if(allocate) {
        allocateUiElement( 74,  74, x, y).z_3c = 33;
        allocateUiElement(153, 153, x, y);

        if(charId < 9) {
          final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.portraits_cfac(), null);
          initGlyph(struct, glyph_801142d4);
          struct.glyph_04 = charId;
          struct.tpage_2c++;
          struct.z_3c = 33;
          struct.x_40 = x + 8;
          struct.y_44 = y + 8;
        }

        //LAB_80108098
        final ActiveStatsa0 stats = stats_800be5f8[charId];
        renderTwoDigitNumber(x + 154, y + 6, stats.level_0e);
        renderTwoDigitNumber(x + 112, y + 17, stats.dlevel_0f);
        renderThreeDigitNumber(x + 148, y + 17, stats.sp_08);
        renderFourDigitHp(x + 100, y + 28, stats.hp_04, stats.maxHp_66);
        renderCharacter(x + 124, y + 28, 11);
        renderFourDigitNumber(x + 142, y + 28, stats.maxHp_66);
        renderThreeDigitNumber(x + 106, y + 39, stats.mp_06);
        renderCharacter(x + 124, y + 39, 11);
        renderThreeDigitNumber(x + 148, y + 39, stats.maxMp_6e);
        renderSixDigitNumber(x + 88, y + 50, gameState_800babc8.charData_32c[charId].xp_00);
        renderCharacter(x + 124, y + 50, 11);
        renderXp(x + 130, y + 50, getXpToNextLevel(charId));

        // Render "don't select" overlay
        if(dontSelect) {
          final Renderable58 struct = allocateUiElement(113, 113, x + 56, y + 24);
          struct.z_3c = 33;
        }
      }

      //LAB_80108218
      if(!renderCharacterStatusEffect(x + 46, y + 3, charId)) {
        renderText(characterNames_801142dc[charId], x + 49, y + 3, TextColour.BROWN);
      }
    }

    //LAB_80108270
  }

  @Method(0x801085e0L)
  public static void renderCharacterStats(final int charIndex, @Nullable final Equipment equipment, final boolean allocate) {
    if(charIndex != -1) {
      final ActiveStatsa0 statsTmp;

      if(equipment != null) {
        final Map<EquipmentSlot, Equipment> oldEquipment = new EnumMap<>(gameState_800babc8.charData_32c[charIndex].equipment_14);

        //LAB_80108638
        equipItem(equipment, charIndex);
        loadCharacterStats();

        //LAB_80108694
        statsTmp = new ActiveStatsa0(stats_800be5f8[charIndex]);

        //LAB_801086e8
        gameState_800babc8.charData_32c[charIndex].equipment_14.clear();
        gameState_800babc8.charData_32c[charIndex].equipment_14.putAll(oldEquipment);

        loadCharacterStats();
      } else {
        //LAB_80108720
        //LAB_80108740
        statsTmp = new ActiveStatsa0(stats_800be5f8[charIndex]);
      }

      //LAB_80108770
      final ActiveStatsa0 stats = stats_800be5f8[charIndex];
      renderThreeDigitNumberComparison( 58, 116, stats.bodyAttack_6a, statsTmp.bodyAttack_6a);
      renderThreeDigitNumberComparison( 90, 116, stats.equipmentAttack_88, statsTmp.equipmentAttack_88);
      renderThreeDigitNumberComparison(122, 116, stats.bodyAttack_6a + stats.equipmentAttack_88, statsTmp.bodyAttack_6a + statsTmp.equipmentAttack_88);

      if(hasDragoon(gameState_800babc8.goods_19c[0], charIndex)) {
        renderThreeDigitNumberComparisonWithPercent(159, 116, stats.dragoonAttack_72, statsTmp.dragoonAttack_72);
      }

      //LAB_801087fc
      renderThreeDigitNumberComparison( 58, 128, stats.bodyDefence_6c, statsTmp.bodyDefence_6c);
      renderThreeDigitNumberComparison( 90, 128, stats.equipmentDefence_8c, statsTmp.equipmentDefence_8c);
      renderThreeDigitNumberComparison(122, 128, stats.bodyDefence_6c + stats.equipmentDefence_8c, statsTmp.bodyDefence_6c + statsTmp.equipmentDefence_8c);

      if(hasDragoon(gameState_800babc8.goods_19c[0], charIndex)) {
        renderThreeDigitNumberComparisonWithPercent(159, 128, stats.dragoonDefence_74, statsTmp.dragoonDefence_74);
      }

      //LAB_8010886c
      renderThreeDigitNumberComparison( 58, 140, stats.bodyMagicAttack_6b, statsTmp.bodyMagicAttack_6b);
      renderThreeDigitNumberComparison( 90, 140, stats.equipmentMagicAttack_8a, statsTmp.equipmentMagicAttack_8a);
      renderThreeDigitNumberComparison(122, 140, stats.bodyMagicAttack_6b + stats.equipmentMagicAttack_8a, statsTmp.bodyMagicAttack_6b + statsTmp.equipmentMagicAttack_8a);

      if(hasDragoon(gameState_800babc8.goods_19c[0], charIndex)) {
        renderThreeDigitNumberComparisonWithPercent(159, 140, stats.dragoonMagicAttack_73, statsTmp.dragoonMagicAttack_73);
      }

      //LAB_801088dc
      renderThreeDigitNumberComparison( 58, 152, stats.bodyMagicDefence_6d, statsTmp.bodyMagicDefence_6d);
      renderThreeDigitNumberComparison( 90, 152, stats.equipmentMagicDefence_8e, statsTmp.equipmentMagicDefence_8e);
      renderThreeDigitNumberComparison(122, 152, stats.bodyMagicDefence_6d + stats.equipmentMagicDefence_8e, statsTmp.bodyMagicDefence_6d + statsTmp.equipmentMagicDefence_8e);

      if(hasDragoon(gameState_800babc8.goods_19c[0], charIndex)) {
        renderThreeDigitNumberComparisonWithPercent(159, 152, stats.dragoonMagicDefence_75, statsTmp.dragoonMagicDefence_75);
      }

      //LAB_8010894c
      renderThreeDigitNumberComparison( 58, 164, stats.bodySpeed_69, statsTmp.bodySpeed_69);
      renderThreeDigitNumberComparison( 90, 164, stats.equipmentSpeed_86, statsTmp.equipmentSpeed_86);
      renderThreeDigitNumberComparison(122, 164, stats.bodySpeed_69 + stats.equipmentSpeed_86, statsTmp.bodySpeed_69 + statsTmp.equipmentSpeed_86);

      renderThreeDigitNumberComparisonWithPercent( 90, 176, stats.equipmentAttackHit_90, statsTmp.equipmentAttackHit_90);
      renderThreeDigitNumberComparisonWithPercent(122, 176, stats.equipmentAttackHit_90, statsTmp.equipmentAttackHit_90);
      renderThreeDigitNumberComparisonWithPercent( 90, 188, stats.equipmentMagicHit_92, statsTmp.equipmentMagicHit_92);
      renderThreeDigitNumberComparisonWithPercent(122, 188, stats.equipmentMagicHit_92, statsTmp.equipmentMagicHit_92);
      renderThreeDigitNumberComparisonWithPercent( 90, 200, stats.equipmentAttackAvoid_94, statsTmp.equipmentAttackAvoid_94);
      renderThreeDigitNumberComparisonWithPercent(122, 200, stats.equipmentAttackAvoid_94, statsTmp.equipmentAttackAvoid_94);
      renderThreeDigitNumberComparisonWithPercent( 90, 212, stats.equipmentMagicAvoid_96, statsTmp.equipmentMagicAvoid_96);
      renderThreeDigitNumberComparisonWithPercent(122, 212, stats.equipmentMagicAvoid_96, statsTmp.equipmentMagicAvoid_96);

      if(allocate) {
        allocateUiElement(0x56, 0x56, 16, 94);
      }
    }

    //LAB_80108a50
  }

  @Method(0x80108e60L)
  public static void renderCharacterEquipment(final int charIndex, final boolean allocate) {
    if(charIndex == -1) {
      return;
    }

    final CharacterData2c charData = gameState_800babc8.charData_32c[charIndex];

    if(allocate) {
      allocateUiElement(0x59, 0x59, 194, 16);

      for(final EquipmentSlot slot : EquipmentSlot.values()) {
        if(charData.equipment_14.get(slot) != null) {
          renderItemIcon(charData.equipment_14.get(slot).icon_0e, 202, 17 + 14 * slot.ordinal(), 0);
        }
      }
    }

    //LAB_80108f94
    //LAB_80108f98
    for(final EquipmentSlot slot : EquipmentSlot.values()) {
      if(charData.equipment_14.get(slot) != null) {
        renderText(I18n.translate(charData.equipment_14.get(slot)), 220, 19 + slot.ordinal() * 14, TextColour.BROWN);
      }
    }

    //LAB_8010905c
  }

  @Method(0x80109074L)
  public static void renderString(final int x, final int y, final String string, final boolean allocate) {
    if(allocate) {
      allocateUiElement(0x5b, 0x5b, x, y);
    }

//    for(int i = 0, pos = 0; i < 4 && pos < string.length(); i++) {
//      int nextNewLine = string.indexOf('\n', pos + 1);
//      if(nextNewLine == -1) {
//        nextNewLine = string.length();
//      }

      renderText(string, x + 2, y + 4, TextColour.BROWN);
//      pos = nextNewLine;
//    }
  }

  @Method(0x80109410L)
  public static void renderMenuItems(final int x, final int y, final MenuEntries<?> menuItems, final int slotScroll, final int itemCount, @Nullable final Renderable58 a5, @Nullable final Renderable58 a6) {
    int s3 = slotScroll;

    //LAB_8010947c
    int i;
    for(i = 0; i < itemCount && s3 < menuItems.size(); i++) {
      final MenuEntryStruct04<?> menuItem = menuItems.get(s3);

      //LAB_801094ac
      renderText(I18n.translate(menuItem.getNameTranslationKey()), x + 21, y + FUN_800fc814(i) + 2, (menuItem.flags_02 & 0x6000) == 0 ? TextColour.BROWN : TextColour.MIDDLE_BROWN);
      renderItemIcon(menuItem.getIcon(), x + 4, y + FUN_800fc814(i), 0x8);

      final int s0 = menuItem.flags_02;
      if((s0 & 0x1000) != 0) {
        renderItemIcon(48 | s0 & 0xf, x + 148, y + FUN_800fc814(i) - 1, 0x8).clut_30 = (500 + (s0 & 0xf) & 0x1ff) << 6 | 0x2b;
        //LAB_80109574
      } else if((s0 & 0x2000) != 0) {
        renderItemIcon(58, x + 148, y + FUN_800fc814(i) - 1, 0x8).clut_30 = 0x7eaa;
      }

      //LAB_801095a4
      s3++;
    }

    //LAB_801095c0
    //LAB_801095d4
    //LAB_801095e0
    if(a5 != null) { // There was an NPE here when fading out item list
      if(slotScroll != 0) {
        a5.flags_00 &= ~Renderable58.FLAG_INVISIBLE;
      } else {
        a5.flags_00 |= Renderable58.FLAG_INVISIBLE;
      }
    }

    //LAB_80109614
    //LAB_80109628
    if(a6 != null) { // There was an NPE here when fading out item list
      if(i + slotScroll < menuItems.size()) {
        a6.flags_00 &= ~Renderable58.FLAG_INVISIBLE;
      } else {
        a6.flags_00 |= Renderable58.FLAG_INVISIBLE;
      }
    }
  }

  public static Obj buildUiRenderable(final UiType type, final String name) {
    final QuadBuilder builder = new QuadBuilder("UI " + name);
    int vertices = 0;

    for(final UiPart part : type.entries_08) {
      for(final RenderableMetrics14 metrics : part.metrics_00()) {
        builder
          .add()
          .uv(metrics.u_00, metrics.v_01)
          .clut((metrics.clut_04 & 0x3f) * 16, (metrics.clut_04 & 0x7fff) >>> 6)
          .vramPos((metrics.tpage_06 & 0b1111) * 64, (metrics.tpage_06 & 0b10000) != 0 ? 256 : 0)
          .bpp(Bpp.of(metrics.tpage_06 >>> 7 & 0b11))
          .posSize(1.0f, 1.0f)
          .uvSize(metrics.textureWidth, metrics.textureHeight)
        ;

        metrics.vertexStart = vertices;
        vertices += 4;
      }
    }

    return builder.build();
  }

  @Method(0x8010ececL)
  public static MessageBoxResult messageBox(final MessageBox20 messageBox) {
    final Renderable58 renderable;

    switch(messageBox.state_0c) {
      case 0:
        return MessageBoxResult.YES;

      case 1: // Allocate
        messageBox.state_0c = 2;
        messageBox.highlightRenderable_04 = null;
        messageBox.backgroundRenderable_08 = allocateUiElement(149, 142, messageBox.x_1c - 50, messageBox.y_1e - 10);
        messageBox.backgroundRenderable_08.z_3c = 32;
        messageBox.backgroundRenderable_08.repeatStartGlyph_18 = 142;
        messageBox.result = MessageBoxResult.AWAITING_INPUT;

      case 2:
        if(messageBox.backgroundRenderable_08.animationLoopsCompletedCount_0c != 0) {
          messageBox.state_0c = 3;
        }

        break;

      case 3:
        textZ_800bdf00 = 31;
        final int x = messageBox.x_1c + 60;
        int y = messageBox.y_1e + 7;

        messageBox.ticks_10++;

        if(messageBox.text_00 != null) {
          for(final String line : messageBox.text_00) {
            renderCentredText(line, x, y, TextColour.BROWN);
            y += 14;
          }
        }

        //LAB_8010eeac
        textZ_800bdf00 = 33;

        if(messageBox.type_15 == 0) {
          //LAB_8010eed8
          if(!messageBox.ignoreInput && Input.pressedThisFrame(InputAction.BUTTON_SOUTH) || Input.pressedThisFrame(InputAction.BUTTON_EAST)) {
            playMenuSound(2);
            messageBox.state_0c = 4;
            messageBox.result = MessageBoxResult.YES;
          }

          break;
        }

        if(messageBox.type_15 == 2) {
          //LAB_8010ef10
          if(messageBox.highlightRenderable_04 == null) {
            renderable = allocateUiElement(125, 125, messageBox.x_1c + 45, messageBox.menuIndex_18 * 14 + y + 5);
            messageBox.highlightRenderable_04 = renderable;
            renderable.heightScale_38 = 0;
            renderable.widthScale = 0;
            messageBox.highlightRenderable_04.z_3c = 31;
          }

          //LAB_8010ef64
          textZ_800bdf00 = 30;

          renderCentredText(messageBox.yes, messageBox.x_1c + 60, y + 7, messageBox.menuIndex_18 == 0 ? TextColour.RED : TextColour.BROWN);
          renderCentredText(messageBox.no, messageBox.x_1c + 60, y + 21, messageBox.menuIndex_18 == 0 ? TextColour.BROWN : TextColour.RED);

          textZ_800bdf00 = 33;
        }

        break;

      case 4:
        messageBox.state_0c = 5;

        if(messageBox.highlightRenderable_04 != null) {
          unloadRenderable(messageBox.highlightRenderable_04);
        }

        //LAB_8010f084
        unloadRenderable(messageBox.backgroundRenderable_08);
        renderable = allocateUiElement(0x8e, 0x95, messageBox.x_1c - 50, messageBox.y_1e - 10);
        renderable.z_3c = 32;
        messageBox.backgroundRenderable_08 = renderable;
        messageBox.backgroundRenderable_08.flags_00 |= Renderable58.FLAG_DELETE_AFTER_ANIMATION;
        break;

      case 5:
        if(messageBox.backgroundRenderable_08.animationLoopsCompletedCount_0c != 0) {
          messageBox.state_0c = 6;
        }

        break;

      case 6:
        messageBox.state_0c = 0;
        return messageBox.result;
    }

    //LAB_8010f108
    //LAB_8010f10c
    return MessageBoxResult.AWAITING_INPUT;
  }

  public static void setMessageBoxOptions(final MessageBox20 messageBox, final String yes, final String no) {
    messageBox.yes = yes;
    messageBox.no = no;
  }

  @Method(0x8010f130L)
  public static void setMessageBoxText(final MessageBox20 messageBox, @Nullable final String text, final int type) {
    setMessageBoxOptions(messageBox, "Yes", "No");

    if(text != null) {
      messageBox.text_00 = text.split("\n");
    } else {
      messageBox.text_00 = null;
    }

    messageBox.x_1c = 120;
    messageBox.y_1e = 100;
    messageBox.type_15 = type;
    messageBox.menuIndex_18 = 0;
    messageBox.ticks_10 = 0;
    messageBox.state_0c = 1;
  }

  @Method(0x80110030L)
  public static void loadCharacterStats() {
    clearCharacterStats();

    //LAB_80110174
    for(int charId = 0; charId < 9; charId++) {
      final ActiveStatsa0 stats = stats_800be5f8[charId];

      final CharacterData2c charData = gameState_800babc8.charData_32c[charId];

      final CharacterStatsEvent statsEvent = EVENTS.postEvent(new CharacterStatsEvent(charId));

      stats.xp_00 = statsEvent.xp;
      stats.hp_04 = statsEvent.hp;
      stats.mp_06 = statsEvent.mp;
      stats.sp_08 = statsEvent.sp;
      stats.dxp_0a = statsEvent.dxp;
      stats.flags_0c = statsEvent.flags;
      stats.level_0e = statsEvent.level;
      stats.dlevel_0f = statsEvent.dlevel;

      //LAB_801101e4
      stats.equipment_30.clear();
      stats.equipment_30.putAll(charData.equipment_14);

      stats.selectedAddition_35 = charData.selectedAddition_19;

      //LAB_80110220
      for(int i = 0; i < 8; i++) {
        stats.additionLevels_36[i] = charData.additionLevels_1a[i];
        stats.additionXp_3e[i] = charData.additionXp_22[i];
      }

      stats.maxHp_66 = statsEvent.maxHp;
      stats.addition_68 = statsEvent.addition;
      stats.bodySpeed_69 = statsEvent.bodySpeed;
      stats.bodyAttack_6a = statsEvent.bodyAttack;
      stats.bodyMagicAttack_6b = statsEvent.bodyMagicAttack;
      stats.bodyDefence_6c = statsEvent.bodyDefence;
      stats.bodyMagicDefence_6d = statsEvent.bodyMagicDefence;

      final MagicStuff08 magicStuff = magicStuff_80111d20[charId][stats.dlevel_0f];
      stats.maxMp_6e = statsEvent.maxMp;
      stats.spellId_70 = statsEvent.spellId;
      stats._71 = magicStuff._03;
      stats.dragoonAttack_72 = statsEvent.dragoonAttack;
      stats.dragoonMagicAttack_73 = statsEvent.dragoonMagicAttack;
      stats.dragoonDefence_74 = statsEvent.dragoonDefence;
      stats.dragoonMagicDefence_75 = statsEvent.dragoonMagicDefence;

      final int additionIndex = stats.selectedAddition_35;
      if(additionIndex != -1) {
        final Addition04 addition = additions_80114070[additionIndex][stats.additionLevels_36[additionIndex - additionOffsets_8004f5ac[charId]]];

        stats.addition_00_9c = addition._00;
        stats.additionSpMultiplier_9e = addition.spMultiplier_02;
        stats.additionDamageMultiplier_9f = addition.damageMultiplier_03;

        final AdditionHitMultiplierEvent event = EVENTS.postEvent(new AdditionHitMultiplierEvent(additionIndex, stats.additionLevels_36[additionIndex - additionOffsets_8004f5ac[charId]], stats.additionSpMultiplier_9e, stats.additionDamageMultiplier_9f));
        stats.additionSpMultiplier_9e = event.additionSpMulti;
        stats.additionDamageMultiplier_9f = event.additionDmgMulti;
      } else {
        stats.additionDamageMultiplier_9f = 0;
      }

      //LAB_8011042c
      applyEquipmentStats(charId);

      final int v0 = dragoonGoodsBits_800fbd08[charId];
      if((gameState_800babc8.goods_19c[0] & 0x1 << v0) != 0) {
        stats.flags_0c |= 0x2000;

        if((gameState_800babc8.characterInitialized_4e6 & 0x1 << v0) == 0) {
          gameState_800babc8.characterInitialized_4e6 |= 0x1 << v0;

          stats.mp_06 = statsEvent.maxMp;
          stats.maxMp_6e = statsEvent.maxMp;
        }
      } else {
        //LAB_801104ec
        stats.mp_06 = 0;
        stats.maxMp_6e = 0;
        stats.dlevel_0f = 0;
      }

      //LAB_801104f8
      if(charId == 0 && (gameState_800babc8.goods_19c[0] & 0x1 << dragoonGoodsBits_800fbd08[9]) != 0) {
        stats.flags_0c |= 0x6000;

        stats.dlevel_0f = gameState_800babc8.charData_32c[0].dlevel_13;

        final int a1 = dragoonGoodsBits_800fbd08[0];

        if((gameState_800babc8.characterInitialized_4e6 & 0x1 << a1) == 0) {
          gameState_800babc8.characterInitialized_4e6 |= 0x1 << a1;
          stats.mp_06 = statsEvent.maxMp;
          stats.maxMp_6e = statsEvent.maxMp;
        } else {
          //LAB_80110590
          stats.mp_06 = charData.mp_0a;
          stats.maxMp_6e = magicStuff.mp_00;
        }
      }

      //LAB_801105b0
      final int maxHp = (int)(stats.maxHp_66 * (stats.equipmentHpMulti_62 / 100.0 + 1));

      //LAB_801105f0
      stats.maxHp_66 = maxHp;

      if(stats.hp_04 > maxHp) {
        stats.hp_04 = maxHp;
      }

      //LAB_80110608
      final int maxMp = (int)(stats.maxMp_6e * (stats.equipmentMpMulti_64 / 100.0 + 1));

      stats.maxMp_6e = maxMp;

      if(stats.mp_06 > maxMp) {
        stats.mp_06 = maxMp;
      }

      //LAB_80110654
    }

    //LAB_8011069c
  }

  @Method(0x8011085cL)
  public static void applyEquipmentStats(final int charId) {
    clearEquipmentStats(charId);

    final ActiveStatsa0 characterStats = stats_800be5f8[charId];

    //LAB_801108b0
    for(final EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
      final Equipment equipment = stats_800be5f8[charId].equipment_30.get(equipmentSlot);

      if(equipment != null) {
        final EquipmentStatsEvent event = EVENTS.postEvent(new EquipmentStatsEvent(charId, equipment));

        characterStats.specialEffectFlag_76 |= event.flags_00;
        characterStats.equipment_02_78 |= event._02;
        characterStats.equipmentEquipableFlags_79 |= event.equipableFlags_03;
        characterStats.equipmentAttackElements_7a.addAll(event.attackElement_04);
        characterStats.equipment_05_7b |= event._05;
        characterStats.equipmentElementalResistance_7c.addAll(event.elementalResistance_06);
        characterStats.equipmentElementalImmunity_7d.addAll(event.elementalImmunity_07);
        characterStats.equipmentStatusResist_7e |= event.statusResist_08;
        characterStats.equipment_09_7f |= event._09;
        characterStats.equipmentAttack1_80 += event.attack1_0a;
        characterStats.equipmentIcon_84 += event.icon_0e;
        characterStats.equipmentSpeed_86 += event.speed_0f;
        characterStats.equipmentAttack_88 += event.attack2_10 + event.attack1_0a;
        characterStats.equipmentMagicAttack_8a += event.magicAttack_11;
        characterStats.equipmentDefence_8c += event.defence_12;
        characterStats.equipmentMagicDefence_8e += event.magicDefence_13;
        characterStats.equipmentAttackHit_90 += event.attackHit_14;
        characterStats.equipmentMagicHit_92 += event.magicHit_15;
        characterStats.equipmentAttackAvoid_94 += event.attackAvoid_16;
        characterStats.equipmentMagicAvoid_96 += event.magicAvoid_17;
        characterStats.equipmentOnHitStatusChance_98 += event.onHitStatusChance_18;
        characterStats.equipment_19_99 += event._19;
        characterStats.equipment_1a_9a += event._1a;
        characterStats.equipmentOnHitStatus_9b |= event.onHitStatus_1b;
        characterStats.equipmentMpPerMagicalHit_54 += event.mpPerMagicalHit;
        characterStats.equipmentSpPerMagicalHit_52 += event.spPerMagicalHit;
        characterStats.equipmentMpPerPhysicalHit_50 += event.mpPerPhysicalHit;
        characterStats.equipmentSpPerPhysicalHit_4e += event.spPerPhysicalHit;
        characterStats.equipmentHpMulti_62 += event.hpMultiplier;
        characterStats.equipmentMpMulti_64 += event.mpMultiplier;
        characterStats.equipmentSpMultiplier_4c += event.spMultiplier;
        characterStats.equipmentMagicalResistance_60 |= event.magicalResistance;
        characterStats.equipmentPhysicalResistance_4a |= event.physicalResistance;
        characterStats.equipmentMagicalImmunity_48 |= event.magicalImmunity;
        characterStats.equipmentPhysicalImmunity_46 |= event.physicalImmunity;
        characterStats.equipmentRevive_5e += event.revive;
        characterStats.equipmentHpRegen_58 += event.hpRegen;
        characterStats.equipmentMpRegen_5a += event.mpRegen;
        characterStats.equipmentSpRegen_5c += event.spRegen;
        characterStats.equipmentEscapeBonus_56 += event.escapeBonus;
      }
    }
  }
}
