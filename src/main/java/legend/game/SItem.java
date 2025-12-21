package legend.game;

import legend.core.MathHelper;
import legend.core.audio.sequencer.assets.BackgroundMusic;
import legend.core.font.Font;
import legend.core.gpu.Bpp;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.additions.Addition;
import legend.game.additions.CharacterAdditionStats;
import legend.game.combat.types.EnemyDrop;
import legend.game.i18n.I18n;
import legend.game.inventory.EquipItemResult;
import legend.game.inventory.Equipment;
import legend.game.inventory.Good;
import legend.game.inventory.GoodsInventory;
import legend.game.inventory.Inventory;
import legend.game.inventory.InventoryEntry;
import legend.game.inventory.Item;
import legend.game.inventory.ItemGroupSortMode;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.ItemStack;
import legend.game.inventory.OverflowMode;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.MenuStack;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.characters.AdditionHitMultiplierEvent;
import legend.game.modding.events.characters.AdditionUnlockEvent;
import legend.game.modding.events.characters.CharacterStatsEvent;
import legend.game.modding.events.characters.XpToLevelEvent;
import legend.game.modding.events.inventory.EquipmentCanEquipEvent;
import legend.game.modding.events.inventory.EquipmentStatsEvent;
import legend.game.modding.events.inventory.GatherAttackItemsEvent;
import legend.game.modding.events.inventory.GatherRecoveryItemsEvent;
import legend.game.modding.events.inventory.IconDisplayEvent;
import legend.game.modding.events.inventory.GiveEquipmentEvent;
import legend.game.modding.events.inventory.TakeEquipmentEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptParam;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.LevelStuff08;
import legend.game.types.MagicStuff08;
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
import legend.lodmod.LodMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.REGISTRIES;
import static legend.game.Audio.copyPlayingSounds;
import static legend.game.Audio.loadedAudioFiles_800bcf78;
import static legend.game.Audio.musicPackageLoadedCallback;
import static legend.game.Audio.playMenuSound;
import static legend.game.Audio.playMusicPackage;
import static legend.game.Audio.playingSoundsBackup_800bca78;
import static legend.game.Audio.queuedSounds_800bd110;
import static legend.game.Audio.sssqResetStuff;
import static legend.game.Audio.stopAndResetSoundsAndSequences;
import static legend.game.Audio.stopMusicSequence;
import static legend.game.Audio.unloadSoundFile;
import static legend.game.DrgnFiles.loadDrgnDir;
import static legend.game.DrgnFiles.loadDrgnFileSync;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.EngineStates.engineState_8004dd20;
import static legend.game.Menus.allocateRenderable;
import static legend.game.Menus.loadMenuTexture;
import static legend.game.Menus.renderablePtr_800bdba4;
import static legend.game.Menus.renderablePtr_800bdba8;
import static legend.game.Menus.uiFile_800bdc3c;
import static legend.game.Menus.unloadRenderable;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_8004.CHARACTER_ADDITIONS;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.characterStatsLoaded_800be5d0;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.loadingNewGameState_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.secondaryCharIds_800bdbf8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Text.renderText;
import static legend.game.Text.textZ_800bdf00;
import static legend.game.combat.Battle.seed_800fa754;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.ITEM_GROUP_SORT_MODE;
import static legend.lodmod.LodGoods.BLUE_DRAGOON_SPIRIT;
import static legend.lodmod.LodGoods.DARK_DRAGOON_SPIRIT;
import static legend.lodmod.LodGoods.DIVINE_DRAGOON_SPIRIT;
import static legend.lodmod.LodGoods.GOLD_DRAGOON_SPIRIT;
import static legend.lodmod.LodGoods.JADE_DRAGOON_SPIRIT;
import static legend.lodmod.LodGoods.RED_DRAGOON_SPIRIT;
import static legend.lodmod.LodGoods.SILVER_DRAGOON_SPIRIT;
import static legend.lodmod.LodGoods.VIOLET_DRAGOON_SPIRIT;

public final class SItem {
  private SItem() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(SItem.class);

  public static final MenuStack menuStack = new MenuStack();
  private static BackgroundMusic menuMusic;

  public static final FontOptions UI_TEXT = new FontOptions().colour(TextColour.BROWN).shadowColour(TextColour.MIDDLE_BROWN);
  public static final FontOptions UI_TEXT_DISABLED = new FontOptions().colour(TextColour.MIDDLE_BROWN).shadowColour(TextColour.LIGHT_BROWN);
  public static final FontOptions UI_TEXT_SELECTED = new FontOptions().colour(TextColour.RED).shadowColour(TextColour.MIDDLE_BROWN);
  public static final FontOptions UI_TEXT_CENTERED = new FontOptions().colour(TextColour.BROWN).shadowColour(TextColour.MIDDLE_BROWN).horizontalAlign(HorizontalAlign.CENTRE);
  public static final FontOptions UI_TEXT_DISABLED_CENTERED = new FontOptions().colour(TextColour.MIDDLE_BROWN).shadowColour(TextColour.LIGHT_BROWN).horizontalAlign(HorizontalAlign.CENTRE);
  public static final FontOptions UI_TEXT_SELECTED_CENTERED = new FontOptions().colour(TextColour.RED).shadowColour(TextColour.MIDDLE_BROWN).horizontalAlign(HorizontalAlign.CENTRE);
  public static final FontOptions UI_WHITE = new FontOptions().colour(TextColour.WHITE);
  public static final FontOptions UI_WHITE_CENTERED = new FontOptions().colour(TextColour.WHITE).horizontalAlign(HorizontalAlign.CENTRE);
  public static final FontOptions UI_WHITE_SMALL = new FontOptions().colour(TextColour.WHITE).size(0.67f);

  public static int shopId_8007a3b4;

  public static final RegistryDelegate<Good>[] characterDragoonIndices_800c6e68 = new RegistryDelegate[10];
  static {
    characterDragoonIndices_800c6e68[0] = RED_DRAGOON_SPIRIT;
    characterDragoonIndices_800c6e68[1] = JADE_DRAGOON_SPIRIT;
    characterDragoonIndices_800c6e68[2] = SILVER_DRAGOON_SPIRIT;
    characterDragoonIndices_800c6e68[3] = DARK_DRAGOON_SPIRIT;
    characterDragoonIndices_800c6e68[4] = VIOLET_DRAGOON_SPIRIT;
    characterDragoonIndices_800c6e68[5] = JADE_DRAGOON_SPIRIT;
    characterDragoonIndices_800c6e68[6] = BLUE_DRAGOON_SPIRIT;
    characterDragoonIndices_800c6e68[7] = GOLD_DRAGOON_SPIRIT;
    characterDragoonIndices_800c6e68[8] = SILVER_DRAGOON_SPIRIT;
    characterDragoonIndices_800c6e68[9] = DIVINE_DRAGOON_SPIRIT;
  }

  public static final MenuStatus08[] menuStatus_800fba7c = {
    new MenuStatus08("Petrify", new FontOptions().colour(TextColour.MIDDLE_BROWN).shadowColour(TextColour.LIGHT_BROWN).horizontalAlign(HorizontalAlign.CENTRE)),
    new MenuStatus08("Charmed", new FontOptions().colour(TextColour.MIDDLE_BROWN).shadowColour(TextColour.LIGHT_BROWN).horizontalAlign(HorizontalAlign.CENTRE)),
    new MenuStatus08("Confused", new FontOptions().colour(TextColour.MIDDLE_BROWN).shadowColour(TextColour.LIGHT_BROWN).horizontalAlign(HorizontalAlign.CENTRE)),
    new MenuStatus08("Fear", new FontOptions().colour(TextColour.PURPLE).shadowColour(TextColour.LIGHT_BROWN).horizontalAlign(HorizontalAlign.CENTRE)),
    new MenuStatus08("Stunned", new FontOptions().colour(TextColour.MIDDLE_BROWN).shadowColour(TextColour.LIGHT_BROWN).horizontalAlign(HorizontalAlign.CENTRE)),
    new MenuStatus08("", new FontOptions().colour(TextColour.MIDDLE_BROWN).shadowColour(TextColour.LIGHT_BROWN).horizontalAlign(HorizontalAlign.CENTRE)),
    new MenuStatus08("Dspirit", new FontOptions().colour(TextColour.CYAN).shadowColour(TextColour.MIDDLE_BROWN).horizontalAlign(HorizontalAlign.CENTRE)),
    new MenuStatus08("Poison", new FontOptions().colour(TextColour.LIME).shadowColour(TextColour.GREEN).horizontalAlign(HorizontalAlign.CENTRE)),
  };

   /** Note: arrays run into the next array's first element */
  public static final int[][] dragoonXpRequirements_800fbbf0 = {
    {0, 1200, 6000, 12000, 20000, 0},
    {0, 1000, 6000, 12000, 20000, 0},
    {0, 1000, 6000, 12000, 20000, 0},
    {0, 1200, 6000, 12000, 20000, 0},
    {0, 1000, 6000, 12000, 20000, 0},
    {0, 1000, 6000, 12000, 20000, 0},
    {0, 1000, 2000, 12000, 20000, 0},
    {0, 1000, 2000, 12000, 20000, 0},
    {0, 1000, 6000, 12000, 20000, 0},
  };

  public static final int[] dragoonGoodsBits_800fbd08 = {0, 2, 5, 6, 4, 2, 1, 3, 5, 7};

  public static final LevelStuff08[][] levelStuff_80111cfc = {
    {new LevelStuff08(0, 0, 0, 0, 0, 0), new LevelStuff08(30, 50, 2, 3, 4, 4), new LevelStuff08(60, 50, 4, 5, 5, 5), new LevelStuff08(90, 50, 6, 7, 7, 7), new LevelStuff08(120, 50, 8, 9, 10, 9), new LevelStuff08(150, 50, 11, 11, 12, 11), new LevelStuff08(180, 50, 13, 13, 14, 13), new LevelStuff08(210, 50, 15, 15, 16, 15), new LevelStuff08(240, 50, 18, 17, 19, 17), new LevelStuff08(270, 50, 20, 19, 21, 19), new LevelStuff08(300, 50, 22, 21, 23, 21), new LevelStuff08(330, 50, 25, 24, 26, 24), new LevelStuff08(413, 50, 27, 26, 28, 26), new LevelStuff08(496, 50, 30, 29, 31, 29), new LevelStuff08(579, 50, 32, 31, 33, 32), new LevelStuff08(662, 50, 35, 34, 36, 34), new LevelStuff08(745, 50, 37, 36, 38, 37), new LevelStuff08(828, 50, 40, 39, 41, 40), new LevelStuff08(911, 50, 42, 41, 43, 42), new LevelStuff08(994, 50, 45, 44, 46, 45), new LevelStuff08(1077, 50, 47, 46, 48, 48), new LevelStuff08(1160, 50, 50, 49, 51, 51), new LevelStuff08(1272, 50, 52, 51, 53, 53), new LevelStuff08(1384, 50, 55, 54, 56, 55), new LevelStuff08(1496, 50, 57, 56, 58, 57), new LevelStuff08(1608, 50, 60, 59, 61, 60), new LevelStuff08(1720, 50, 62, 61, 63, 62), new LevelStuff08(1832, 50, 65, 64, 66, 64), new LevelStuff08(1944, 50, 67, 66, 68, 67), new LevelStuff08(2056, 50, 70, 69, 71, 69), new LevelStuff08(2168, 50, 72, 71, 73, 71), new LevelStuff08(2280, 50, 75, 74, 76, 74), new LevelStuff08(2399, 50, 77, 76, 78, 76), new LevelStuff08(2518, 50, 80, 79, 81, 79), new LevelStuff08(2637, 50, 82, 81, 83, 82), new LevelStuff08(2756, 50, 85, 84, 86, 84), new LevelStuff08(2875, 50, 87, 86, 88, 87), new LevelStuff08(2994, 50, 90, 89, 91, 90), new LevelStuff08(3113, 50, 92, 91, 93, 92), new LevelStuff08(3232, 50, 95, 94, 96, 95), new LevelStuff08(3351, 50, 97, 96, 98, 98), new LevelStuff08(3470, 50, 100, 99, 101, 101), new LevelStuff08(3729, 50, 102, 101, 103, 103), new LevelStuff08(3988, 50, 105, 104, 105, 105), new LevelStuff08(4247, 50, 107, 106, 108, 107), new LevelStuff08(4506, 50, 110, 109, 110, 110), new LevelStuff08(4765, 50, 112, 111, 113, 112), new LevelStuff08(5024, 50, 115, 114, 115, 114), new LevelStuff08(5283, 50, 117, 116, 118, 117), new LevelStuff08(5542, 50, 120, 119, 120, 119), new LevelStuff08(5801, 50, 122, 121, 123, 121), new LevelStuff08(6060, 50, 125, 124, 125, 124), new LevelStuff08(6220, 50, 127, 126, 128, 126), new LevelStuff08(6380, 50, 130, 129, 131, 129), new LevelStuff08(6540, 50, 133, 132, 133, 132), new LevelStuff08(6700, 50, 136, 135, 136, 135), new LevelStuff08(6860, 50, 138, 138, 139, 138), new LevelStuff08(7020, 50, 141, 141, 141, 141), new LevelStuff08(7180, 50, 144, 144, 144, 144), new LevelStuff08(7340, 50, 147, 147, 147, 147), new LevelStuff08(7500, 50, 150, 150, 150, 150), },
    {new LevelStuff08(0, 0, 0, 0, 0, 0), new LevelStuff08(35, 40, 3, 2, 4, 2), new LevelStuff08(67, 40, 6, 3, 7, 3), new LevelStuff08(100, 40, 13, 4, 10, 4), new LevelStuff08(133, 40, 15, 6, 13, 5), new LevelStuff08(166, 40, 16, 7, 16, 6), new LevelStuff08(199, 40, 20, 8, 19, 7), new LevelStuff08(231, 40, 23, 10, 22, 7), new LevelStuff08(264, 40, 27, 11, 25, 8), new LevelStuff08(297, 40, 30, 13, 28, 9), new LevelStuff08(330, 40, 34, 14, 31, 10), new LevelStuff08(363, 40, 37, 15, 34, 11), new LevelStuff08(454, 40, 41, 17, 37, 12), new LevelStuff08(545, 40, 45, 18, 41, 13), new LevelStuff08(636, 40, 48, 20, 44, 14), new LevelStuff08(728, 40, 52, 22, 47, 15), new LevelStuff08(819, 40, 56, 23, 51, 16), new LevelStuff08(910, 40, 60, 25, 54, 17), new LevelStuff08(1002, 40, 63, 27, 57, 18), new LevelStuff08(1093, 40, 67, 28, 61, 19), new LevelStuff08(1184, 40, 71, 30, 64, 20), new LevelStuff08(1276, 40, 75, 31, 67, 21), new LevelStuff08(1399, 40, 78, 33, 71, 24), new LevelStuff08(1522, 40, 82, 35, 74, 27), new LevelStuff08(1645, 40, 86, 37, 77, 29), new LevelStuff08(1768, 40, 90, 39, 81, 32), new LevelStuff08(1892, 40, 93, 41, 84, 35), new LevelStuff08(2015, 40, 97, 43, 87, 37), new LevelStuff08(2138, 40, 101, 45, 91, 40), new LevelStuff08(2261, 40, 105, 47, 94, 43), new LevelStuff08(2384, 40, 108, 48, 97, 45), new LevelStuff08(2508, 40, 112, 50, 101, 48), new LevelStuff08(2638, 40, 116, 52, 104, 49), new LevelStuff08(2769, 40, 120, 54, 107, 50), new LevelStuff08(2900, 40, 123, 55, 111, 51), new LevelStuff08(3031, 40, 127, 57, 114, 52), new LevelStuff08(3162, 40, 131, 59, 117, 53), new LevelStuff08(3293, 40, 135, 61, 121, 54), new LevelStuff08(3424, 40, 138, 62, 124, 55), new LevelStuff08(3555, 40, 142, 64, 127, 57), new LevelStuff08(3686, 40, 146, 66, 131, 58), new LevelStuff08(3817, 40, 150, 68, 134, 59), new LevelStuff08(4101, 40, 153, 70, 137, 61), new LevelStuff08(4386, 40, 157, 72, 141, 64), new LevelStuff08(4671, 40, 161, 74, 144, 67), new LevelStuff08(4956, 40, 165, 76, 147, 70), new LevelStuff08(5241, 40, 168, 78, 151, 73), new LevelStuff08(5526, 40, 172, 80, 154, 76), new LevelStuff08(5811, 40, 176, 82, 157, 79), new LevelStuff08(6096, 40, 180, 84, 161, 82), new LevelStuff08(6381, 40, 183, 86, 164, 85), new LevelStuff08(6666, 40, 187, 88, 167, 87), new LevelStuff08(6842, 40, 191, 90, 171, 88), new LevelStuff08(7018, 40, 195, 92, 174, 89), new LevelStuff08(7194, 40, 200, 94, 178, 90), new LevelStuff08(7370, 40, 204, 97, 182, 91), new LevelStuff08(7546, 40, 208, 99, 185, 93), new LevelStuff08(7722, 40, 212, 101, 189, 94), new LevelStuff08(7898, 40, 216, 104, 192, 95), new LevelStuff08(8074, 40, 220, 106, 196, 96), new LevelStuff08(8250, 40, 225, 108, 199, 97), },
    {new LevelStuff08(0, 0, 0, 0, 0, 0), new LevelStuff08(24, 65, 2, 3, 2, 3), new LevelStuff08(48, 65, 3, 6, 3, 6), new LevelStuff08(72, 65, 5, 9, 5, 9), new LevelStuff08(96, 65, 7, 12, 6, 12), new LevelStuff08(120, 65, 8, 16, 8, 15), new LevelStuff08(144, 65, 10, 19, 9, 19), new LevelStuff08(168, 65, 12, 22, 11, 22), new LevelStuff08(192, 65, 13, 26, 12, 25), new LevelStuff08(216, 65, 15, 29, 14, 28), new LevelStuff08(240, 65, 17, 32, 15, 31), new LevelStuff08(264, 65, 19, 36, 17, 35), new LevelStuff08(330, 65, 20, 41, 18, 41), new LevelStuff08(396, 65, 21, 47, 20, 47), new LevelStuff08(463, 65, 22, 53, 22, 53), new LevelStuff08(529, 65, 23, 58, 24, 59), new LevelStuff08(596, 65, 24, 64, 25, 65), new LevelStuff08(662, 65, 26, 70, 27, 71), new LevelStuff08(728, 65, 27, 75, 29, 77), new LevelStuff08(795, 65, 28, 81, 31, 83), new LevelStuff08(861, 65, 29, 87, 33, 89), new LevelStuff08(928, 65, 30, 93, 34, 95), new LevelStuff08(1017, 65, 32, 98, 36, 101), new LevelStuff08(1107, 65, 33, 104, 37, 106), new LevelStuff08(1196, 65, 35, 109, 39, 111), new LevelStuff08(1286, 65, 36, 115, 40, 117), new LevelStuff08(1376, 65, 38, 120, 42, 122), new LevelStuff08(1465, 65, 39, 126, 43, 128), new LevelStuff08(1555, 65, 40, 131, 45, 133), new LevelStuff08(1644, 65, 42, 137, 46, 139), new LevelStuff08(1734, 65, 43, 143, 48, 144), new LevelStuff08(1824, 65, 45, 148, 50, 150), new LevelStuff08(1919, 65, 47, 153, 51, 155), new LevelStuff08(2014, 65, 48, 158, 53, 160), new LevelStuff08(2109, 65, 50, 164, 54, 165), new LevelStuff08(2204, 65, 52, 169, 56, 170), new LevelStuff08(2300, 65, 54, 174, 57, 176), new LevelStuff08(2395, 65, 56, 179, 59, 181), new LevelStuff08(2490, 65, 58, 184, 60, 186), new LevelStuff08(2585, 65, 60, 189, 62, 191), new LevelStuff08(2680, 65, 61, 194, 63, 196), new LevelStuff08(2776, 65, 63, 200, 65, 202), new LevelStuff08(2983, 65, 65, 202, 68, 204), new LevelStuff08(3190, 65, 67, 205, 72, 207), new LevelStuff08(3397, 65, 69, 208, 75, 209), new LevelStuff08(3604, 65, 71, 211, 79, 212), new LevelStuff08(3812, 65, 73, 214, 83, 215), new LevelStuff08(4019, 65, 76, 217, 86, 217), new LevelStuff08(4226, 65, 78, 220, 90, 220), new LevelStuff08(4433, 65, 80, 223, 93, 222), new LevelStuff08(4640, 65, 82, 226, 97, 225), new LevelStuff08(4848, 65, 84, 229, 101, 228), new LevelStuff08(4976, 65, 86, 232, 106, 231), new LevelStuff08(5104, 65, 88, 235, 111, 234), new LevelStuff08(5232, 65, 91, 238, 117, 237), new LevelStuff08(5360, 65, 93, 240, 122, 240), new LevelStuff08(5488, 65, 95, 243, 127, 243), new LevelStuff08(5616, 65, 97, 246, 133, 246), new LevelStuff08(5744, 65, 100, 249, 138, 249), new LevelStuff08(5872, 65, 102, 252, 143, 252), new LevelStuff08(6000, 65, 104, 255, 149, 254), },
    {new LevelStuff08(0, 0, 0, 0, 0, 0), new LevelStuff08(21, 55, 3, 6, 5, 5), new LevelStuff08(42, 55, 6, 10, 8, 7), new LevelStuff08(63, 55, 9, 14, 11, 10), new LevelStuff08(84, 55, 13, 18, 14, 12), new LevelStuff08(105, 55, 16, 22, 17, 15), new LevelStuff08(126, 55, 20, 27, 19, 17), new LevelStuff08(147, 55, 23, 31, 22, 20), new LevelStuff08(168, 55, 27, 35, 25, 22), new LevelStuff08(189, 55, 30, 39, 28, 25), new LevelStuff08(210, 55, 34, 43, 31, 27), new LevelStuff08(231, 55, 37, 48, 34, 30), new LevelStuff08(289, 55, 39, 49, 35, 32), new LevelStuff08(347, 55, 40, 50, 37, 34), new LevelStuff08(405, 55, 42, 51, 38, 37), new LevelStuff08(463, 55, 44, 53, 39, 39), new LevelStuff08(521, 55, 46, 54, 40, 41), new LevelStuff08(579, 55, 47, 55, 41, 43), new LevelStuff08(637, 55, 49, 57, 43, 45), new LevelStuff08(695, 55, 51, 58, 44, 47), new LevelStuff08(753, 55, 52, 59, 45, 50), new LevelStuff08(812, 55, 54, 61, 46, 52), new LevelStuff08(890, 55, 56, 62, 49, 55), new LevelStuff08(968, 55, 57, 63, 53, 58), new LevelStuff08(1047, 55, 59, 64, 56, 61), new LevelStuff08(1125, 55, 61, 66, 59, 64), new LevelStuff08(1204, 55, 63, 67, 63, 67), new LevelStuff08(1282, 55, 64, 68, 66, 70), new LevelStuff08(1360, 55, 66, 70, 69, 73), new LevelStuff08(1439, 55, 68, 71, 72, 76), new LevelStuff08(1517, 55, 70, 72, 76, 79), new LevelStuff08(1596, 55, 71, 74, 79, 82), new LevelStuff08(1679, 55, 73, 75, 80, 83), new LevelStuff08(1762, 55, 75, 77, 81, 84), new LevelStuff08(1845, 55, 77, 79, 83, 85), new LevelStuff08(1929, 55, 79, 80, 84, 86), new LevelStuff08(2012, 55, 81, 82, 85, 87), new LevelStuff08(2095, 55, 83, 84, 86, 88), new LevelStuff08(2179, 55, 84, 86, 87, 90), new LevelStuff08(2262, 55, 86, 87, 88, 91), new LevelStuff08(2345, 55, 88, 89, 90, 92), new LevelStuff08(2429, 55, 90, 91, 91, 93), new LevelStuff08(2610, 55, 94, 95, 95, 97), new LevelStuff08(2791, 55, 98, 99, 99, 101), new LevelStuff08(2972, 55, 103, 103, 103, 105), new LevelStuff08(3154, 55, 107, 107, 108, 109), new LevelStuff08(3335, 55, 111, 111, 112, 113), new LevelStuff08(3516, 55, 115, 115, 116, 117), new LevelStuff08(3698, 55, 119, 119, 120, 121), new LevelStuff08(3879, 55, 124, 123, 124, 125), new LevelStuff08(4060, 55, 128, 127, 129, 129), new LevelStuff08(4242, 55, 132, 131, 133, 133), new LevelStuff08(4354, 55, 133, 133, 134, 135), new LevelStuff08(4466, 55, 134, 135, 135, 136), new LevelStuff08(4578, 55, 135, 137, 136, 138), new LevelStuff08(4690, 55, 136, 139, 137, 140), new LevelStuff08(4802, 55, 137, 141, 138, 142), new LevelStuff08(4914, 55, 139, 143, 139, 144), new LevelStuff08(5026, 55, 140, 145, 140, 146), new LevelStuff08(5138, 55, 141, 147, 141, 148), new LevelStuff08(5250, 55, 142, 150, 142, 150), },
    {new LevelStuff08(0, 0, 0, 0, 0, 0), new LevelStuff08(27, 60, 3, 2, 4, 2), new LevelStuff08(54, 60, 5, 3, 6, 3), new LevelStuff08(81, 60, 8, 5, 8, 5), new LevelStuff08(108, 60, 11, 6, 10, 6), new LevelStuff08(135, 60, 14, 8, 12, 7), new LevelStuff08(162, 60, 17, 10, 15, 9), new LevelStuff08(189, 60, 19, 11, 17, 10), new LevelStuff08(216, 60, 22, 13, 19, 11), new LevelStuff08(243, 60, 25, 14, 21, 13), new LevelStuff08(270, 60, 28, 16, 23, 14), new LevelStuff08(297, 60, 31, 18, 26, 15), new LevelStuff08(371, 60, 34, 20, 28, 19), new LevelStuff08(446, 60, 37, 23, 31, 22), new LevelStuff08(521, 60, 40, 26, 33, 25), new LevelStuff08(595, 60, 43, 29, 36, 28), new LevelStuff08(670, 60, 47, 32, 38, 32), new LevelStuff08(745, 60, 50, 35, 41, 35), new LevelStuff08(819, 60, 53, 38, 43, 38), new LevelStuff08(894, 60, 56, 41, 46, 41), new LevelStuff08(969, 60, 59, 44, 48, 45), new LevelStuff08(1044, 60, 63, 47, 51, 48), new LevelStuff08(1144, 60, 66, 48, 53, 49), new LevelStuff08(1245, 60, 69, 49, 56, 50), new LevelStuff08(1346, 60, 72, 49, 58, 51), new LevelStuff08(1447, 60, 75, 50, 61, 52), new LevelStuff08(1548, 60, 78, 51, 63, 53), new LevelStuff08(1648, 60, 81, 52, 66, 54), new LevelStuff08(1749, 60, 84, 53, 68, 55), new LevelStuff08(1850, 60, 87, 53, 71, 56), new LevelStuff08(1951, 60, 90, 54, 73, 56), new LevelStuff08(2052, 60, 94, 55, 76, 57), new LevelStuff08(2159, 60, 97, 59, 78, 62), new LevelStuff08(2266, 60, 100, 64, 81, 66), new LevelStuff08(2373, 60, 103, 68, 83, 70), new LevelStuff08(2480, 60, 106, 73, 86, 74), new LevelStuff08(2587, 60, 109, 77, 88, 78), new LevelStuff08(2694, 60, 112, 81, 91, 82), new LevelStuff08(2801, 60, 115, 86, 93, 87), new LevelStuff08(2908, 60, 118, 90, 96, 91), new LevelStuff08(3015, 60, 121, 95, 98, 95), new LevelStuff08(3123, 60, 125, 99, 101, 99), new LevelStuff08(3356, 60, 128, 100, 103, 100), new LevelStuff08(3589, 60, 131, 101, 105, 101), new LevelStuff08(3822, 60, 134, 101, 108, 102), new LevelStuff08(4055, 60, 137, 102, 110, 103), new LevelStuff08(4288, 60, 140, 103, 113, 103), new LevelStuff08(4521, 60, 143, 104, 115, 104), new LevelStuff08(4754, 60, 146, 104, 118, 105), new LevelStuff08(4987, 60, 149, 105, 120, 106), new LevelStuff08(5220, 60, 152, 106, 123, 107), new LevelStuff08(5454, 60, 156, 107, 125, 108), new LevelStuff08(5598, 60, 159, 111, 128, 112), new LevelStuff08(5742, 60, 163, 116, 131, 117), new LevelStuff08(5886, 60, 166, 120, 133, 121), new LevelStuff08(6030, 60, 170, 125, 136, 126), new LevelStuff08(6174, 60, 173, 130, 139, 130), new LevelStuff08(6318, 60, 177, 134, 141, 135), new LevelStuff08(6462, 60, 180, 139, 144, 139), new LevelStuff08(6606, 60, 184, 143, 147, 144), new LevelStuff08(6750, 60, 188, 148, 150, 148), },
    {new LevelStuff08(0, 0, 0, 0, 0, 0), new LevelStuff08(35, 40, 3, 2, 4, 2), new LevelStuff08(67, 40, 6, 3, 7, 3), new LevelStuff08(100, 40, 13, 4, 10, 4), new LevelStuff08(133, 40, 15, 6, 13, 5), new LevelStuff08(166, 40, 16, 7, 16, 6), new LevelStuff08(199, 40, 20, 8, 19, 7), new LevelStuff08(231, 40, 23, 10, 22, 7), new LevelStuff08(264, 40, 27, 11, 25, 8), new LevelStuff08(297, 40, 30, 13, 28, 9), new LevelStuff08(330, 40, 34, 14, 31, 10), new LevelStuff08(363, 40, 37, 15, 34, 11), new LevelStuff08(454, 40, 41, 17, 37, 12), new LevelStuff08(545, 40, 45, 18, 41, 13), new LevelStuff08(636, 40, 48, 20, 44, 14), new LevelStuff08(728, 40, 52, 22, 47, 15), new LevelStuff08(819, 40, 56, 23, 51, 16), new LevelStuff08(910, 40, 60, 25, 54, 17), new LevelStuff08(1002, 40, 63, 27, 57, 18), new LevelStuff08(1093, 40, 67, 28, 61, 19), new LevelStuff08(1184, 40, 71, 30, 64, 20), new LevelStuff08(1276, 40, 75, 31, 67, 21), new LevelStuff08(1399, 40, 78, 33, 71, 24), new LevelStuff08(1522, 40, 82, 35, 74, 27), new LevelStuff08(1645, 40, 86, 37, 77, 29), new LevelStuff08(1768, 40, 90, 39, 81, 32), new LevelStuff08(1892, 40, 93, 41, 84, 35), new LevelStuff08(2015, 40, 97, 43, 87, 37), new LevelStuff08(2138, 40, 101, 45, 91, 40), new LevelStuff08(2261, 40, 105, 47, 94, 43), new LevelStuff08(2384, 40, 108, 48, 97, 45), new LevelStuff08(2508, 40, 112, 50, 101, 48), new LevelStuff08(2638, 40, 116, 52, 104, 49), new LevelStuff08(2769, 40, 120, 54, 107, 50), new LevelStuff08(2900, 40, 123, 55, 111, 51), new LevelStuff08(3031, 40, 127, 57, 114, 52), new LevelStuff08(3162, 40, 131, 59, 117, 53), new LevelStuff08(3293, 40, 135, 61, 121, 54), new LevelStuff08(3424, 40, 138, 62, 124, 55), new LevelStuff08(3555, 40, 142, 64, 127, 57), new LevelStuff08(3686, 40, 146, 66, 131, 58), new LevelStuff08(3817, 40, 150, 68, 134, 59), new LevelStuff08(4101, 40, 153, 70, 137, 61), new LevelStuff08(4386, 40, 157, 72, 141, 64), new LevelStuff08(4671, 40, 161, 74, 144, 67), new LevelStuff08(4956, 40, 165, 76, 147, 70), new LevelStuff08(5241, 40, 168, 78, 151, 73), new LevelStuff08(5526, 40, 172, 80, 154, 76), new LevelStuff08(5811, 40, 176, 82, 157, 79), new LevelStuff08(6096, 40, 180, 84, 161, 82), new LevelStuff08(6381, 40, 183, 86, 164, 85), new LevelStuff08(6666, 40, 187, 88, 167, 87), new LevelStuff08(6842, 40, 191, 90, 171, 88), new LevelStuff08(7018, 40, 195, 92, 174, 89), new LevelStuff08(7194, 40, 200, 94, 178, 90), new LevelStuff08(7370, 40, 204, 97, 182, 91), new LevelStuff08(7546, 40, 208, 99, 185, 93), new LevelStuff08(7722, 40, 212, 101, 189, 94), new LevelStuff08(7898, 40, 216, 104, 192, 95), new LevelStuff08(8074, 40, 220, 106, 196, 96), new LevelStuff08(8250, 40, 225, 108, 199, 97), },
    {new LevelStuff08(0, 0, 0, 0, 0, 0), new LevelStuff08(18, 70, 2, 3, 2, 4), new LevelStuff08(36, 70, 3, 6, 3, 7), new LevelStuff08(54, 70, 5, 9, 4, 10), new LevelStuff08(72, 70, 7, 12, 6, 13), new LevelStuff08(90, 70, 8, 15, 7, 16), new LevelStuff08(108, 70, 10, 19, 9, 20), new LevelStuff08(126, 70, 12, 22, 10, 23), new LevelStuff08(144, 70, 13, 25, 11, 26), new LevelStuff08(162, 70, 15, 28, 13, 29), new LevelStuff08(180, 70, 17, 31, 14, 32), new LevelStuff08(198, 70, 19, 34, 16, 36), new LevelStuff08(247, 70, 21, 38, 17, 39), new LevelStuff08(297, 70, 23, 42, 19, 42), new LevelStuff08(347, 70, 25, 46, 21, 45), new LevelStuff08(397, 70, 27, 50, 23, 48), new LevelStuff08(447, 70, 29, 54, 25, 51), new LevelStuff08(496, 70, 31, 58, 27, 54), new LevelStuff08(546, 70, 33, 62, 29, 57), new LevelStuff08(596, 70, 35, 66, 31, 60), new LevelStuff08(646, 70, 37, 70, 33, 63), new LevelStuff08(696, 70, 40, 74, 34, 66), new LevelStuff08(763, 70, 41, 77, 37, 69), new LevelStuff08(830, 70, 42, 81, 39, 73), new LevelStuff08(897, 70, 43, 85, 41, 76), new LevelStuff08(964, 70, 44, 88, 43, 80), new LevelStuff08(1032, 70, 45, 92, 45, 84), new LevelStuff08(1099, 70, 46, 96, 47, 87), new LevelStuff08(1166, 70, 47, 99, 49, 91), new LevelStuff08(1233, 70, 48, 103, 52, 94), new LevelStuff08(1300, 70, 49, 107, 54, 98), new LevelStuff08(1368, 70, 50, 111, 56, 102), new LevelStuff08(1439, 70, 52, 114, 58, 105), new LevelStuff08(1510, 70, 53, 118, 60, 108), new LevelStuff08(1582, 70, 54, 122, 62, 111), new LevelStuff08(1653, 70, 56, 126, 65, 114), new LevelStuff08(1725, 70, 57, 130, 67, 117), new LevelStuff08(1796, 70, 58, 133, 69, 120), new LevelStuff08(1867, 70, 60, 137, 71, 123), new LevelStuff08(1939, 70, 61, 141, 73, 126), new LevelStuff08(2010, 70, 62, 145, 75, 129), new LevelStuff08(2082, 70, 64, 149, 77, 133), new LevelStuff08(2237, 70, 68, 152, 80, 136), new LevelStuff08(2392, 70, 72, 156, 82, 140), new LevelStuff08(2548, 70, 76, 160, 85, 143), new LevelStuff08(2703, 70, 80, 163, 87, 147), new LevelStuff08(2859, 70, 85, 167, 90, 150), new LevelStuff08(3014, 70, 89, 171, 92, 154), new LevelStuff08(3169, 70, 93, 174, 94, 157), new LevelStuff08(3325, 70, 97, 178, 97, 161), new LevelStuff08(3480, 70, 101, 182, 99, 164), new LevelStuff08(3636, 70, 105, 186, 102, 168), new LevelStuff08(3732, 70, 110, 190, 104, 171), new LevelStuff08(3828, 70, 115, 194, 106, 174), new LevelStuff08(3924, 70, 119, 199, 108, 178), new LevelStuff08(4020, 70, 124, 203, 110, 181), new LevelStuff08(4116, 70, 129, 207, 112, 184), new LevelStuff08(4212, 70, 133, 212, 114, 188), new LevelStuff08(4308, 70, 138, 216, 116, 191), new LevelStuff08(4404, 70, 143, 220, 118, 195), new LevelStuff08(4500, 70, 147, 225, 120, 198), },
    {new LevelStuff08(0, 0, 0, 0, 0, 0), new LevelStuff08(45, 30, 4, 2, 4, 3), new LevelStuff08(83, 30, 9, 3, 8, 4), new LevelStuff08(121, 30, 14, 4, 13, 5), new LevelStuff08(160, 30, 18, 5, 17, 6), new LevelStuff08(198, 30, 23, 6, 22, 7), new LevelStuff08(237, 30, 28, 7, 27, 8), new LevelStuff08(275, 30, 33, 8, 31, 9), new LevelStuff08(313, 30, 38, 9, 36, 10), new LevelStuff08(352, 30, 43, 10, 40, 11), new LevelStuff08(390, 30, 48, 11, 45, 12), new LevelStuff08(429, 30, 53, 12, 50, 13), new LevelStuff08(536, 30, 57, 13, 55, 14), new LevelStuff08(644, 30, 62, 14, 60, 15), new LevelStuff08(752, 30, 66, 15, 65, 16), new LevelStuff08(860, 30, 71, 17, 70, 17), new LevelStuff08(968, 30, 76, 18, 76, 18), new LevelStuff08(1076, 30, 80, 19, 81, 19), new LevelStuff08(1184, 30, 85, 21, 86, 20), new LevelStuff08(1292, 30, 89, 22, 91, 21), new LevelStuff08(1400, 30, 94, 23, 96, 22), new LevelStuff08(1508, 30, 99, 25, 102, 23), new LevelStuff08(1653, 30, 103, 26, 107, 24), new LevelStuff08(1799, 30, 108, 27, 112, 25), new LevelStuff08(1944, 30, 113, 28, 117, 26), new LevelStuff08(2090, 30, 118, 29, 122, 27), new LevelStuff08(2236, 30, 123, 31, 128, 28), new LevelStuff08(2381, 30, 128, 32, 133, 29), new LevelStuff08(2527, 30, 133, 33, 138, 30), new LevelStuff08(2672, 30, 138, 34, 143, 31), new LevelStuff08(2818, 30, 143, 35, 148, 32), new LevelStuff08(2964, 30, 148, 37, 154, 34), new LevelStuff08(3118, 30, 153, 38, 157, 35), new LevelStuff08(3273, 30, 159, 39, 161, 36), new LevelStuff08(3428, 30, 164, 40, 165, 38), new LevelStuff08(3582, 30, 170, 42, 168, 39), new LevelStuff08(3737, 30, 176, 43, 172, 41), new LevelStuff08(3892, 30, 181, 44, 176, 42), new LevelStuff08(4046, 30, 187, 46, 179, 44), new LevelStuff08(4201, 30, 192, 47, 183, 45), new LevelStuff08(4356, 30, 198, 48, 187, 47), new LevelStuff08(4511, 30, 204, 50, 190, 48), new LevelStuff08(4847, 30, 206, 51, 194, 50), new LevelStuff08(5184, 30, 209, 52, 197, 51), new LevelStuff08(5521, 30, 211, 53, 201, 53), new LevelStuff08(5857, 30, 214, 54, 205, 54), new LevelStuff08(6194, 30, 217, 56, 208, 56), new LevelStuff08(6531, 30, 219, 57, 212, 58), new LevelStuff08(6867, 30, 222, 58, 215, 59), new LevelStuff08(7204, 30, 224, 59, 219, 61), new LevelStuff08(7541, 30, 227, 60, 223, 63), new LevelStuff08(7878, 30, 230, 62, 226, 64), new LevelStuff08(8086, 30, 232, 63, 229, 66), new LevelStuff08(8294, 30, 235, 64, 232, 68), new LevelStuff08(8502, 30, 238, 66, 236, 69), new LevelStuff08(8710, 30, 241, 67, 239, 71), new LevelStuff08(8918, 30, 243, 69, 242, 73), new LevelStuff08(9126, 30, 246, 70, 245, 74), new LevelStuff08(9334, 30, 249, 72, 248, 76), new LevelStuff08(9542, 30, 252, 73, 251, 78), new LevelStuff08(9750, 30, 254, 75, 254, 80), },
    {new LevelStuff08(0, 0, 0, 0, 0, 0), new LevelStuff08(24, 65, 2, 3, 2, 3), new LevelStuff08(48, 65, 3, 6, 3, 6), new LevelStuff08(72, 65, 5, 9, 5, 9), new LevelStuff08(96, 65, 7, 12, 6, 12), new LevelStuff08(120, 65, 8, 16, 8, 15), new LevelStuff08(144, 65, 10, 19, 9, 19), new LevelStuff08(168, 65, 12, 22, 11, 22), new LevelStuff08(192, 65, 13, 26, 12, 25), new LevelStuff08(216, 65, 15, 29, 14, 28), new LevelStuff08(240, 65, 17, 32, 15, 31), new LevelStuff08(264, 65, 19, 36, 17, 35), new LevelStuff08(330, 65, 20, 41, 18, 41), new LevelStuff08(396, 65, 21, 47, 20, 47), new LevelStuff08(463, 65, 22, 53, 22, 53), new LevelStuff08(529, 65, 23, 58, 24, 59), new LevelStuff08(596, 65, 24, 64, 25, 65), new LevelStuff08(662, 65, 26, 70, 27, 71), new LevelStuff08(728, 65, 27, 75, 29, 77), new LevelStuff08(795, 65, 28, 81, 31, 83), new LevelStuff08(861, 65, 29, 87, 33, 89), new LevelStuff08(928, 65, 30, 93, 34, 95), new LevelStuff08(1017, 65, 32, 98, 36, 101), new LevelStuff08(1107, 65, 33, 104, 37, 106), new LevelStuff08(1196, 65, 35, 109, 39, 111), new LevelStuff08(1286, 65, 36, 115, 40, 117), new LevelStuff08(1376, 65, 38, 120, 42, 122), new LevelStuff08(1465, 65, 39, 126, 43, 128), new LevelStuff08(1555, 65, 40, 131, 45, 133), new LevelStuff08(1644, 65, 42, 137, 46, 139), new LevelStuff08(1734, 65, 43, 143, 48, 144), new LevelStuff08(1824, 65, 45, 148, 50, 150), new LevelStuff08(1919, 65, 47, 153, 51, 155), new LevelStuff08(2014, 65, 48, 158, 53, 160), new LevelStuff08(2109, 65, 50, 164, 54, 165), new LevelStuff08(2204, 65, 52, 169, 56, 170), new LevelStuff08(2300, 65, 54, 174, 57, 176), new LevelStuff08(2395, 65, 56, 179, 59, 181), new LevelStuff08(2490, 65, 58, 184, 60, 186), new LevelStuff08(2585, 65, 60, 189, 62, 191), new LevelStuff08(2680, 65, 61, 194, 63, 196), new LevelStuff08(2776, 65, 63, 200, 65, 202), new LevelStuff08(2983, 65, 65, 202, 68, 204), new LevelStuff08(3190, 65, 67, 205, 72, 207), new LevelStuff08(3397, 65, 69, 208, 75, 209), new LevelStuff08(3604, 65, 71, 211, 79, 212), new LevelStuff08(3812, 65, 73, 214, 83, 215), new LevelStuff08(4019, 65, 76, 217, 86, 217), new LevelStuff08(4226, 65, 78, 220, 90, 220), new LevelStuff08(4433, 65, 80, 223, 93, 222), new LevelStuff08(4640, 65, 82, 226, 97, 225), new LevelStuff08(4848, 65, 84, 229, 101, 228), new LevelStuff08(4976, 65, 86, 232, 106, 231), new LevelStuff08(5104, 65, 88, 235, 111, 234), new LevelStuff08(5232, 65, 91, 238, 117, 237), new LevelStuff08(5360, 65, 93, 240, 122, 240), new LevelStuff08(5488, 65, 95, 243, 127, 243), new LevelStuff08(5616, 65, 97, 246, 133, 246), new LevelStuff08(5744, 65, 100, 249, 138, 249), new LevelStuff08(5872, 65, 102, 252, 143, 252), new LevelStuff08(6000, 65, 104, 255, 149, 254), },
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
    script.params_20[1].set(slot < gameState_800babc8.items_2e9.getSize() ? 1 : 0);
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
    script.params_20[1].set(gameState_800babc8.items_2e9.get(slot).getItem().getRegistryId());
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
    final boolean taken = takeItem(REGISTRIES.items.getEntry(id).get());
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
    final ItemStack[] items = EVENTS.postEvent(new GatherAttackItemsEvent()).getStacks();
    final ItemStack selected = items[seed_800fa754.nextInt(items.length)];
    script.params_20[0].set(selected.getItem().getRegistryId());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Picks a random recovery item")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.REG, name = "id")
  public static FlowControl scriptGenerateRecoveryItem(final RunningScript<?> script) {
    final ItemStack[] items = EVENTS.postEvent(new GatherRecoveryItemsEvent()).getStacks();
    final ItemStack selected = items[seed_800fa754.nextInt(items.length)];
    script.params_20[0].set(selected.getItem().getRegistryId());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Check if the player has the specified good")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "has")
  public static FlowControl scriptHasGood(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    script.params_20[1].set(gameState_800babc8.goods_19c.has(REGISTRIES.goods.getEntry(id)));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gives a good to the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id")
  public static FlowControl scriptGiveGood(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    gameState_800babc8.goods_19c.give(REGISTRIES.goods.getEntry(id));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Takes a good from the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id")
  public static FlowControl scriptTakeGood(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    gameState_800babc8.goods_19c.take(REGISTRIES.goods.getEntry(id));
    return FlowControl.CONTINUE;
  }

  @Method(0x80022928L)
  public static int getUnlockedDragoonSpells(final int[] spellIndicesOut, final int charIndex) {
    //LAB_80022940
    for(int spellIndex = 0; spellIndex < 8; spellIndex++) {
      spellIndicesOut[spellIndex] = -1;
    }

    if(charIndex == -1) {
      //LAB_80022a08
      return 0;
    }

    // Hardcoded Divine Dragoon spells
    if(charIndex == 0 && gameState_800babc8.goods_19c.has(DIVINE_DRAGOON_SPIRIT)) {
      spellIndicesOut[0] = 9;
      spellIndicesOut[1] = 4;
      return 2;
    }

    //LAB_80022994
    //LAB_80022998
    //LAB_800229d0
    int spellCount = 0;
    for(int dlevel = 0; dlevel < stats_800be5f8[charIndex].dlevel_0f + 1; dlevel++) {
      final MagicStuff08 spellStuff = CoreMod.CHARACTER_DATA[charIndex].dragoonStatsTable[dlevel];
      final int spellIndex = spellStuff.spellIndex_02;

      if(spellIndex != -1) {
        spellIndicesOut[spellCount] = spellIndex;
        spellCount++;
      }

      //LAB_800229e8
    }

    //LAB_80022a00
    return spellCount;
  }

  @Method(0x80022a10L)
  public static int getUnlockedSpellCount(final int charIndex) {
    if(charIndex == -1) {
      return 0;
    }

    //LAB_80022a24
    // Divine dragoon
    if(charIndex == 0 && gameState_800babc8.goods_19c.has(DIVINE_DRAGOON_SPIRIT)) {
      return 2;
    }

    //LAB_80022a4c
    //LAB_80022a50
    //LAB_80022a64
    int unlockedSpells = 0;
    for(int i = 0; i < CoreMod.MAX_DRAGOON_LEVEL; i++) {
      if(CoreMod.CHARACTER_DATA[charIndex].dragoonStatsTable[i].spellIndex_02 != -1) {
        unlockedSpells++;
      }

      //LAB_80022a7c
    }

    return unlockedSpells;
  }

  /**
   * @param amount Amount of HP to restore, -1 restores all hP
   * @return The amount of HP restored, -1 if all HP is restored, or -2 if HP was already full
   */
  @Method(0x80022b50L)
  public static int addHp(final int charIndex, final int amount) {
    final CharacterData2c charData = gameState_800babc8.charData_32c[charIndex];
    final ActiveStatsa0 stats = stats_800be5f8[charIndex];

    if(charData.hp_08 == stats.maxHp_66) {
      return -2;
    }

    //LAB_80022bb4
    final int ret;
    if(amount == -1) {
      charData.hp_08 = stats.maxHp_66;
      ret = -1;
    } else {
      //LAB_80022bc8
      charData.hp_08 += amount;

      if(charData.hp_08 < stats.maxHp_66) {
        ret = amount;
      } else {
        charData.hp_08 = stats.maxHp_66;
        ret = -1;
      }
    }

    //LAB_80022bec
    loadCharacterStats();

    //LAB_80022bf8
    return ret;
  }

  /**
   * @param amount Amount of MP to restore, -1 restores all MP
   * @return The amount of MP restored, -1 if all MP is restored, or -2 if MP was already full
   */
  @Method(0x80022c08L)
  public static int addMp(final int charIndex, final int amount) {
    final CharacterData2c charData = gameState_800babc8.charData_32c[charIndex];
    final ActiveStatsa0 stats = stats_800be5f8[charIndex];

    if(stats.maxMp_6e == 0 || charData.mp_0a == stats.maxMp_6e) {
      return -2;
    }

    //LAB_80022c78
    final int ret;
    if(amount == -1) {
      charData.mp_0a = stats.maxMp_6e;
      ret = -1;
    } else {
      //LAB_80022c8c
      charData.mp_0a += amount;

      if(charData.mp_0a < stats.maxMp_6e) {
        ret = amount;
      } else {
        charData.mp_0a = stats.maxMp_6e;
        ret = -1;
      }
    }

    //LAB_80022cb4
    loadCharacterStats();

    //LAB_80022cc0
    return ret;
  }

  @Method(0x80022cd0L)
  public static int addSp(final int charIndex, final int amount) {
    final CharacterData2c charData = gameState_800babc8.charData_32c[charIndex];
    final ActiveStatsa0 stats = stats_800be5f8[charIndex];

    final int maxSp = stats.dlevel_0f * 100;
    if(stats.sp_08 == maxSp) {
      return -2;
    }

    final int spToAdd = amount == -1 ? maxSp - stats.sp_08 : Math.min(amount, maxSp - stats.sp_08);
    final int responseType = amount == -1 || spToAdd < amount ? -1 : amount;

    charData.sp_0c += spToAdd;
    gameState_800babc8.charData_32c[charIndex].dlevelXp_0e += amount == -1 ? spToAdd : amount;

    if(gameState_800babc8.charData_32c[charIndex].dlevelXp_0e > 32000) {
      gameState_800babc8.charData_32c[charIndex].dlevelXp_0e = 32000;
    }

    if(gameState_800babc8.charData_32c[charIndex].dlevelXp_0e >= dragoonXpRequirements_800fbbf0[charIndex][gameState_800babc8.charData_32c[charIndex].dlevel_13 + 1] && gameState_800babc8.charData_32c[charIndex].dlevel_13 < 5) {
      gameState_800babc8.charData_32c[charIndex].dlevel_13++;
    }

    loadCharacterStats();
    return responseType;
  }

  public static boolean takeItem(final Item item) {
    return gameState_800babc8.items_2e9.take(item).isEmpty();
  }

  public static boolean takeItem(final ItemStack stack) {
    return gameState_800babc8.items_2e9.take(stack).isEmpty();
  }

  @Method(0x800232dcL)
  public static boolean takeItemFromSlot(final int itemSlot) {
    return takeItemFromSlot(itemSlot, gameState_800babc8.items_2e9.get(itemSlot).getSize());
  }

  @Method(0x800232dcL)
  public static boolean takeItemFromSlot(final int itemSlot, final int amount) {
    if(itemSlot >= gameState_800babc8.items_2e9.getSize()) {
      LOGGER.warn("Tried to take item index %d (out of bounds)", itemSlot);
      return false;
    }

    gameState_800babc8.items_2e9.takeFromSlot(itemSlot, amount);
    return true;
  }

  public static boolean takeEquipmentId(final Equipment equipment) {
    final int equipmentSlot = gameState_800babc8.equipment_1e8.indexOf(equipment);

    if(equipmentSlot != -1) {
      return takeEquipment(equipmentSlot);
    }

    return false;
  }

  @Method(0x800233d8L)
  public static boolean takeEquipment(final int equipmentIndex) {
    if(equipmentIndex >= gameState_800babc8.equipment_1e8.size()) {
      LOGGER.warn("Tried to take equipment index %d (out of bounds)", equipmentIndex);
      return false;
    }

    final Equipment equipment = gameState_800babc8.equipment_1e8.get(equipmentIndex);
    final TakeEquipmentEvent event = EVENTS.postEvent(new TakeEquipmentEvent(equipment, equipmentIndex));

    if(event.isCanceled()) {
      return false;
    }

    gameState_800babc8.equipment_1e8.remove(equipmentIndex);
    return true;
  }

  @Method(0x80023484L)
  public static boolean giveItem(final Item item) {
    return gameState_800babc8.items_2e9.give(item).isEmpty();
  }

  /**
   * Note: does NOT consume the passed in item stack
   */
  @Method(0x80023484L)
  public static boolean giveItem(final ItemStack item) {
    return gameState_800babc8.items_2e9.give(new ItemStack(item)).isEmpty();
  }

  @Method(0x80023484L)
  public static boolean giveEquipment(final Equipment equipment) {
    final GiveEquipmentEvent event = EVENTS.postEvent(new GiveEquipmentEvent(equipment, Collections.unmodifiableList(gameState_800babc8.equipment_1e8), 255));

    if(event.isCanceled() || event.givenEquipment.isEmpty()) {
      return false;
    }

    final boolean overflowed = event.currentEquipment.size() + event.givenEquipment.size() > event.maxInventorySize;

    if(event.overflowMode == OverflowMode.FAIL && overflowed) {
      return false;
    }

    if(event.overflowMode == OverflowMode.TRUNCATE && overflowed) {
      for(int i = 0; i < event.givenEquipment.size() && event.currentEquipment.size() <= event.maxInventorySize; i++) {
        gameState_800babc8.equipment_1e8.add(event.givenEquipment.get(i));
      }

      return true;
    }

    gameState_800babc8.equipment_1e8.addAll(event.givenEquipment);
    return true;
  }

  /**
   * @param items the items to give
   * @return the number of items that could not be given
   */
  @Method(0x80023544L)
  public static int giveItems(final List<EnemyDrop> items) {
    int count = 0;

    //LAB_80023580
    final Iterator<EnemyDrop> it = items.iterator();
    while(it.hasNext()) {
      final EnemyDrop drop = it.next();

      if(!drop.performDrop()) {
        //LAB_800235a4
        //LAB_800235c0
        drop.overflow();
        it.remove();
        count++;
      }

      //LAB_80023604
    }

    //LAB_80023618
    return count;
  }

  @Method(0x8002363cL)
  public static int addGold(final int amount) {
    gameState_800babc8.gold_94 += amount;

    if(gameState_800babc8.gold_94 > 99999999) {
      gameState_800babc8.gold_94 = 99999999;
    }

    //LAB_8002366c
    return 0;
  }

  /**
   * @param part 0: second, 1: minute, 2: hour
   */
  @Method(0x80023674L)
  public static int getTimestampPart(int timestamp, final long part) {
    if(timestamp >= 216000000) { // Clamp to 1000 hours
      timestamp = 215999999;
    }

    // Hours
    if(part == 0) {
      return timestamp / 216000 % 1000;
    }

    // Minutes
    if(part == 1) {
      return timestamp / 3600 % 60;
    }

    // Seconds
    if(part == 2) {
      return timestamp / 60 % 60;
    }

    return 0;
  }

  @Method(0x800239e0L)
  public static <T extends InventoryEntry> void setInventoryFromDisplay(final List<MenuEntryStruct04<T>> display, final List<T> out, final int count) {
    out.clear();

    //LAB_800239ec
    for(int i = 0; i < count; i++) {
      if((display.get(i).flags_02 & 0x1000) == 0) {
        out.add(display.get(i).item_00);
      }
    }
  }

  @Method(0x800239e0L)
  public static void setInventoryFromDisplay(final List<MenuEntryStruct04<ItemStack>> display, final Inventory out, final int count) {
    out.clear();

    //LAB_800239ec
    for(int i = 0; i < count; i++) {
      if((display.get(i).flags_02 & 0x1000) == 0) {
        out.give(display.get(i).item_00, true);
      }
    }
  }

  @Method(0x80023a2cL)
  public static <T extends InventoryEntry> void sortItems(final List<MenuEntryStruct04<T>> display, final List<T> items, final int count, final List<String> retailSorting) {
    display.sort(menuItemIconComparator(retailSorting, InventoryEntry::getRegistryId));
    setInventoryFromDisplay(display, items, count);
  }

  @Method(0x80023a2cL)
  public static void sortItems(final List<MenuEntryStruct04<ItemStack>> display, final Inventory items, final int count, final List<String> retailSorting) {
    display.sort(menuItemIconComparator(retailSorting, stack -> stack.getItem().getRegistryId()));
    setInventoryFromDisplay(display, items, count);
  }

  public static <T extends InventoryEntry> Comparator<MenuEntryStruct04<T>> menuItemIconComparator(final List<String> retailSorting, final Function<T, RegistryId> idExtractor) {
    final boolean retail = CONFIG.getConfig(ITEM_GROUP_SORT_MODE.get()) == ItemGroupSortMode.RETAIL;

    Comparator<MenuEntryStruct04<T>> comparator = Comparator.comparingInt(item -> item.item_00.getIcon().resolve().icon);

    if(retail) {
      comparator = comparator.thenComparingInt(item -> {
        final RegistryId id = idExtractor.apply(item.item_00);

        if(!LodMod.MOD_ID.equals(id.modId()) || !retailSorting.contains(id.entryId())) {
          return Integer.MAX_VALUE;
        }

        return retailSorting.indexOf(id.entryId());
      });

      comparator = comparator.thenComparing(item -> {
        final RegistryId id = idExtractor.apply(item.item_00);

        if(LodMod.MOD_ID.equals(id.modId()) && retailSorting.contains(id.entryId())) {
          return "";
        }

        return I18n.translate(item.getNameTranslationKey());
      });
    } else {
      comparator = comparator.thenComparing(item -> I18n.translate(item.getNameTranslationKey()));
    }

    return comparator;
  }

  public static Comparator<MenuEntryStruct04<Equipment>> menuEquipmentSlotComparator() {
    return Comparator
      .comparingInt((MenuEntryStruct04<Equipment> equipment) -> equipment.item_00.slot.ordinal())
      .thenComparing(equipment -> I18n.translate(equipment.getNameTranslationKey()));
  }

  @Method(0x80023a88L)
  public static void sortItems() {
    final List<MenuEntryStruct04<ItemStack>> items = new ArrayList<>();

    for(final ItemStack stack : gameState_800babc8.items_2e9) {
      items.add(new MenuEntryStruct04<>(stack));
    }

    sortItems(items, gameState_800babc8.items_2e9, gameState_800babc8.items_2e9.getSize(), List.of(LodMod.ITEM_IDS));
  }

  @Method(0x8002a6fcL)
  public static void clearCharacterStats() {
    //LAB_8002a730
    for(int charIndex = 0; charIndex < 9; charIndex++) {
      final ActiveStatsa0 stats = stats_800be5f8[charIndex];

      stats.xp_00 = 0;
      stats.hp_04 = 0;
      stats.mp_06 = 0;
      stats.sp_08 = 0;
      stats.dxp_0a = 0;
      stats.flags_0c = 0;
      stats.level_0e = 0;
      stats.dlevel_0f = 0;
      stats.equipment_30.clear();

      final RegistryDelegate<Addition>[] additions = CHARACTER_ADDITIONS[charIndex];
      stats.selectedAddition_35 = additions.length != 0 ? additions[0].getId() : null;

      stats.equipmentPhysicalImmunity_46 = false;
      stats.equipmentMagicalImmunity_48 = false;
      stats.equipmentPhysicalResistance_4a = false;
      stats.equipmentSpMultiplier_4c = 0;
      stats.equipmentSpPerPhysicalHit_4e = 0;
      stats.equipmentMpPerPhysicalHit_50 = 0;
      stats.equipmentSpPerMagicalHit_52 = 0;
      stats.equipmentMpPerMagicalHit_54 = 0;
      stats.equipmentEscapeBonus_56 = 0;
      stats.equipmentHpRegen_58 = 0;
      stats.equipmentMpRegen_5a = 0;
      stats.equipmentSpRegen_5c = 0;
      stats.equipmentRevive_5e = 0;
      stats.equipmentMagicalResistance_60 = false;
      stats.equipmentHpMulti_62 = 0;
      stats.equipmentMpMulti_64 = 0;
      stats.maxHp_66 = 0;
//      stats.addition_68 = 0;
      stats.bodySpeed_69 = 0;
      stats.bodyAttack_6a = 0;
      stats.bodyMagicAttack_6b = 0;
      stats.bodyDefence_6c = 0;
      stats.bodyMagicDefence_6d = 0;
      stats.maxMp_6e = 0;
      stats.spellId_70 = 0;
      stats._71 = 0;
      stats.dragoonAttack_72 = 0;
      stats.dragoonMagicAttack_73 = 0;
      stats.dragoonDefence_74 = 0;
      stats.dragoonMagicDefence_75 = 0;

      clearEquipmentStats(charIndex);

//      stats.addition_00_9c = 0;
      stats.additionSpMultiplier_9e = 0;
      stats.additionDamageMultiplier_9f = 0;
    }

    characterStatsLoaded_800be5d0 = false;
  }

  @Method(0x8002a86cL)
  public static void clearEquipmentStats(final int charIndex) {
    final ActiveStatsa0 stats = stats_800be5f8[charIndex];

    stats.specialEffectFlag_76 = 0;
//    stats.equipmentType_77 = 0;
    stats.equipment_02_78 = 0;
    stats.equipmentEquipableFlags_79 = 0;
    stats.equipmentAttackElements_7a.clear();
    stats.equipment_05_7b = 0;
    stats.equipmentElementalResistance_7c.clear();
    stats.equipmentElementalImmunity_7d.clear();
    stats.equipmentStatusResist_7e = 0;
    stats.equipment_09_7f = 0;
    stats.equipmentAttack1_80 = 0;
    stats._83 = 0;
    stats.equipmentIcon_84 = 0;

    stats.equipmentSpeed_86 = 0;
    stats.equipmentAttack_88 = 0;
    stats.equipmentMagicAttack_8a = 0;
    stats.equipmentDefence_8c = 0;
    stats.equipmentMagicDefence_8e = 0;
    stats.equipmentAttackHit_90 = 0;
    stats.equipmentMagicHit_92 = 0;
    stats.equipmentAttackAvoid_94 = 0;
    stats.equipmentMagicAvoid_96 = 0;
    stats.equipmentOnHitStatusChance_98 = 0;
    stats.equipment_19_99 = 0;
    stats.equipment_1a_9a = 0;
    stats.equipmentOnHitStatus_9b = 0;
  }

  @Method(0x800fc698L)
  public static int getXpToNextLevel(final int charIndex) {
    if(charIndex == -1 || charIndex > 8) {
      //LAB_800fc6a4
      throw new RuntimeException("Character index " + charIndex + " out of bounds");
    }

    //LAB_800fc6ac
    final int level = gameState_800babc8.charData_32c[charIndex].level_12;

    if(level >= CoreMod.MAX_CHARACTER_LEVEL) {
      return 0; // Max level
    }

    final XpToLevelEvent event = EVENTS.postEvent(new XpToLevelEvent(charIndex, level, CoreMod.CHARACTER_DATA[charIndex].xpTable[level + 1]));

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
        loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x80);
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
  public static Renderable58 renderCharacterPortrait(final int charId, final int x, final int y, final int flags) {
    final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.itemIcons_c6a4(), null);
    renderable.flags_00 |= flags | Renderable58.FLAG_NO_ANIMATION;
    renderable.glyph_04 = 48 + charId;
    renderable.startGlyph_10 = renderable.glyph_04;
    renderable.endGlyph_14 = renderable.glyph_04;
    renderable.tpage_2c = 0x19;
    renderable.clut_30 = 0;
    renderable.x_40 = x;
    renderable.y_44 = y;
    return renderable;
  }

  @Method(0x801039a0L)
  public static boolean canEquip(final Equipment equipment, final int charIndex) {
    final EquipmentCanEquipEvent event = EVENTS.postEvent(new EquipmentCanEquipEvent(equipment, equipment.equipableFlags_03));
    return (characterValidEquipment_80114284[charIndex] & event.equipableFlags_03) != 0;
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

  @Method(0x80103e90L)
  public static float renderMenuCentredText(final Font font, final String text, final float x, final float y, final int maxWidth, final FontOptions options) {
    return renderMenuCentredText(font, text, x, y, maxWidth, options, null);
  }

  @Method(0x80103e90L)
  public static float renderMenuCentredText(final Font font, final String text, final float x, final float y, final int maxWidth, final FontOptions options, @Nullable final Text.QueueCallback queueCallback) {
    final String[] split;
    if(font.textWidth(text) * options.getSize() <= maxWidth) {
      split = new String[] {text};
    } else {
      final List<String> temp = new ArrayList<>();
      float currentWidth = 0.0f;
      int startIndex = 0;
      for(int i = 0; i < text.length(); i++) {
        final char current = text.charAt(i);
        final float charWidth = font.charWidth(current) * options.getSize();

        if(current == '\n') {
          temp.add(text.substring(startIndex, i));
          currentWidth = 0;
          startIndex = i + 1;
        } else if(currentWidth + charWidth > maxWidth) {
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

    float height = 0.0f;

    for(int i = 0; i < split.length; i++) {
      final String str = split[i];
      renderText(str, x - font.textWidth(str) * options.getSize() / 2.0f, y + height, options, queueCallback);
      height += font.textHeight(str) * options.getSize();
    }

    return height;
  }

  @Method(0x801038d4L)
  public static Renderable58 allocateOneFrameGlyph(final int glyph, final int x, final int y) {
    final Renderable58 renderable = allocateUiElement(glyph, glyph, x, y);
    renderable.flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
    return renderable;
  }

  @Method(0x80104738L)
  public static void loadItemsAndEquipmentForDisplay(@Nullable final MenuEntries<Equipment> equipments, @Nullable final MenuEntries<ItemStack> items, final long a0) {
    if(items != null) {
      items.clear();

      for(int i = 0; i < gameState_800babc8.items_2e9.getSize(); i++) {
        final ItemStack item = gameState_800babc8.items_2e9.get(i);
        final MenuEntryStruct04<ItemStack> menuEntry = new MenuEntryStruct04<>(item);
        items.add(menuEntry);
      }
    }

    if(equipments != null) {
      equipments.clear();

      int equipmentIndex;
      for(equipmentIndex = 0; equipmentIndex < gameState_800babc8.equipment_1e8.size(); equipmentIndex++) {
        final Equipment equipment = gameState_800babc8.equipment_1e8.get(equipmentIndex);
        final MenuEntryStruct04<Equipment> menuEntry = new MenuEntryStruct04<>(equipment);

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
              final MenuEntryStruct04<Equipment> menuEntry = new MenuEntryStruct04<>(equipment);
              menuEntry.flags_02 = 0x3000 | characterIndices_800bdbb8[i];
              equipments.add(menuEntry);

              equipmentIndex++;
            }
          }
        }
      }
    }
  }

  public static void loadAdditions(final int charId, final List<Addition> additions) {
    additions.clear();

    if(charId == -1) {
      return;
    }

    checkForNewlyUnlockedAddition(charId);

    final CharacterData2c charData = gameState_800babc8.charData_32c[charId];

    for(final RegistryDelegate<Addition> additionDelegate : CHARACTER_ADDITIONS[charId]) {
      final Addition addition = additionDelegate.get();
      final CharacterAdditionStats additionStats = charData.additionStats.get(addition.getRegistryId());

      if(additionStats.unlocked) {
        additions.add(addition);
      }
    }
  }

  public static Addition checkForNewlyUnlockedAddition(final int charId) {
    if(charId == -1) {
      return null;
    }

    final CharacterData2c charData = gameState_800babc8.charData_32c[charId];
    Addition newlyUnlocked = null;

    for(final RegistryDelegate<Addition> additionDelegate : CHARACTER_ADDITIONS[charId]) {
      final Addition addition = additionDelegate.get();
      final CharacterAdditionStats additionStats = charData.additionStats.computeIfAbsent(addition.getRegistryId(), k -> new CharacterAdditionStats());
      final boolean wasUnlocked = additionStats.unlocked;

      additionStats.unlocked = additionStats.unlocked || addition.isUnlocked(charData, additionStats);

      EVENTS.postEvent(new AdditionUnlockEvent(charData, additionStats, addition));

      if(additionStats.unlocked && !wasUnlocked) {
        newlyUnlocked = addition;
      }
    }

    return newlyUnlocked;
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
  public static boolean hasDragoon(final GoodsInventory dragoons, final int charIndex) {
    //LAB_80104b94
    if(charIndex == -1) {
      return false;
    }

    //LAB_80104be0
    if(charIndex == 0 && dragoons.has(DIVINE_DRAGOON_SPIRIT)) { // Divine
      return true;
    }

    //LAB_80104c24
    //LAB_80104c28
    return dragoons.has(characterDragoonIndices_800c6e68[charIndex]);
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

    if(tickCount_800bb0fc / currentEngineState_8004dd04.tickMultiplier() % 32 < 16) {
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
    renderText(menuStatus.text_00, x + 24, y, menuStatus.colour_04);

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
        renderText(characterNames_801142dc[charId], x + 49, y + 3, UI_TEXT);
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

      if(hasDragoon(gameState_800babc8.goods_19c, charIndex)) {
        renderThreeDigitNumberComparisonWithPercent(159, 116, stats.dragoonAttack_72, statsTmp.dragoonAttack_72);
      }

      //LAB_801087fc
      renderThreeDigitNumberComparison( 58, 128, stats.bodyDefence_6c, statsTmp.bodyDefence_6c);
      renderThreeDigitNumberComparison( 90, 128, stats.equipmentDefence_8c, statsTmp.equipmentDefence_8c);
      renderThreeDigitNumberComparison(122, 128, stats.bodyDefence_6c + stats.equipmentDefence_8c, statsTmp.bodyDefence_6c + statsTmp.equipmentDefence_8c);

      if(hasDragoon(gameState_800babc8.goods_19c, charIndex)) {
        renderThreeDigitNumberComparisonWithPercent(159, 128, stats.dragoonDefence_74, statsTmp.dragoonDefence_74);
      }

      //LAB_8010886c
      renderThreeDigitNumberComparison( 58, 140, stats.bodyMagicAttack_6b, statsTmp.bodyMagicAttack_6b);
      renderThreeDigitNumberComparison( 90, 140, stats.equipmentMagicAttack_8a, statsTmp.equipmentMagicAttack_8a);
      renderThreeDigitNumberComparison(122, 140, stats.bodyMagicAttack_6b + stats.equipmentMagicAttack_8a, statsTmp.bodyMagicAttack_6b + statsTmp.equipmentMagicAttack_8a);

      if(hasDragoon(gameState_800babc8.goods_19c, charIndex)) {
        renderThreeDigitNumberComparisonWithPercent(159, 140, stats.dragoonMagicAttack_73, statsTmp.dragoonMagicAttack_73);
      }

      //LAB_801088dc
      renderThreeDigitNumberComparison( 58, 152, stats.bodyMagicDefence_6d, statsTmp.bodyMagicDefence_6d);
      renderThreeDigitNumberComparison( 90, 152, stats.equipmentMagicDefence_8e, statsTmp.equipmentMagicDefence_8e);
      renderThreeDigitNumberComparison(122, 152, stats.bodyMagicDefence_6d + stats.equipmentMagicDefence_8e, statsTmp.bodyMagicDefence_6d + statsTmp.equipmentMagicDefence_8e);

      if(hasDragoon(gameState_800babc8.goods_19c, charIndex)) {
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
          charData.equipment_14.get(slot).renderIcon(202, 17 + 14 * slot.ordinal(), 0);
        }
      }
    }

    //LAB_80108f94
    //LAB_80108f98
    for(final EquipmentSlot slot : EquipmentSlot.values()) {
      if(charData.equipment_14.get(slot) != null) {
        renderText(I18n.translate(charData.equipment_14.get(slot)), 220, 19 + slot.ordinal() * 14, UI_TEXT);
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

      renderText(string, x + 2, y + 4, UI_TEXT);
//      pos = nextNewLine;
//    }
  }

  @Method(0x80109410L)
  public static void renderMenuItems(final int x, final int y, final MenuEntries<?> menuItems, final int slotScroll, final int itemCount, @Nullable final Renderable58 upArrow, @Nullable final Renderable58 downArrow) {
    int s3 = slotScroll;

    //LAB_8010947c
    int i;
    for(i = 0; i < itemCount && s3 < menuItems.size(); i++) {
      final MenuEntryStruct04<?> menuItem = menuItems.get(s3);

      //LAB_801094ac
      renderText(I18n.translate(menuItem.getNameTranslationKey()), x + 21, y + FUN_800fc814(i) + 2, (menuItem.flags_02 & 0x6000) == 0 ? UI_TEXT : UI_TEXT_DISABLED);
      menuItem.item_00.renderIcon(x + 4, y + FUN_800fc814(i), 0x8);

      if(menuItem.getMaxSize() > 1) {
        renderNumber(x + 96, y + FUN_800fc814(i) + 3, menuItem.getSize(), 0x2, 10);
      }

      final int s0 = menuItem.flags_02;
      if((s0 & 0x1000) != 0) {
        renderCharacterPortrait(s0 & 0xf, x + 148, y + FUN_800fc814(i) - 1, 0x8).clut_30 = (500 + (s0 & 0xf) & 0x1ff) << 6 | 0x2b;
        //LAB_80109574
      } else if((s0 & 0x2000) != 0) {
        ItemIcon.WARNING.render(x + 148, y + FUN_800fc814(i) - 1, 0x8).clut_30 = 0x7eaa;
      }

      //LAB_801095a4
      s3++;
    }

    //LAB_801095c0
    //LAB_801095d4
    //LAB_801095e0
    if(upArrow != null) { // There was an NPE here when fading out item list
      if(slotScroll != 0) {
        upArrow.flags_00 &= ~Renderable58.FLAG_INVISIBLE;
      } else {
        upArrow.flags_00 |= Renderable58.FLAG_INVISIBLE;
      }
    }

    //LAB_80109614
    //LAB_80109628
    if(downArrow != null) { // There was an NPE here when fading out item list
      if(i + slotScroll < menuItems.size()) {
        downArrow.flags_00 &= ~Renderable58.FLAG_INVISIBLE;
      } else {
        downArrow.flags_00 |= Renderable58.FLAG_INVISIBLE;
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
        int y = messageBox.y_1e + 14;

        messageBox.ticks_10++;

        if(messageBox.text_00 != null) {
          y -= messageBox.text_00.length * 12 / 2;

          for(final String line : messageBox.text_00) {
            renderText(line, x, y, UI_TEXT_CENTERED);
            y += 12;
          }

          y -= (messageBox.text_00.length - 1) * 3;
        }

        //LAB_8010eeac
        textZ_800bdf00 = 33;

        if(messageBox.type_15 == 0) {
          //LAB_8010eed8
          if(!messageBox.ignoreInput && PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get()) || PLATFORM.isActionPressed(INPUT_ACTION_MENU_BACK.get())) {
            playMenuSound(2);
            messageBox.state_0c = 4;
            messageBox.result = MessageBoxResult.YES;
          }

          break;
        }

        if(messageBox.type_15 == 2) {
          //LAB_8010ef10
          if(messageBox.highlightRenderable_04 == null) {
            renderable = allocateUiElement(125, 125, messageBox.x_1c + 45, messageBox.menuIndex_18 * 12 + y + 5);
            messageBox.highlightRenderable_04 = renderable;
            renderable.heightScale_38 = 0;
            renderable.widthScale = 0;
            messageBox.highlightRenderable_04.z_3c = 31;
          }

          //LAB_8010ef64
          textZ_800bdf00 = 30;

          renderText(messageBox.yes, messageBox.x_1c + 60, y + 7, messageBox.menuIndex_18 == 0 ? UI_TEXT_SELECTED_CENTERED : UI_TEXT_CENTERED);
          renderText(messageBox.no, messageBox.x_1c + 60, y + 21, messageBox.menuIndex_18 == 0 ? UI_TEXT_CENTERED : UI_TEXT_SELECTED_CENTERED);

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

      stats.maxHp_66 = statsEvent.maxHp;
//      stats.addition_68 = statsEvent.addition;
      stats.bodySpeed_69 = statsEvent.bodySpeed;
      stats.bodyAttack_6a = statsEvent.bodyAttack;
      stats.bodyMagicAttack_6b = statsEvent.bodyMagicAttack;
      stats.bodyDefence_6c = statsEvent.bodyDefence;
      stats.bodyMagicDefence_6d = statsEvent.bodyMagicDefence;

      final MagicStuff08 magicStuff = CoreMod.CHARACTER_DATA[charId].dragoonStatsTable[stats.dlevel_0f];
      stats.maxMp_6e = statsEvent.maxMp;
      stats.spellId_70 = statsEvent.spellId;
      stats._71 = magicStuff._03;
      stats.dragoonAttack_72 = statsEvent.dragoonAttack;
      stats.dragoonMagicAttack_73 = statsEvent.dragoonMagicAttack;
      stats.dragoonDefence_74 = statsEvent.dragoonDefence;
      stats.dragoonMagicDefence_75 = statsEvent.dragoonMagicDefence;

      if(stats.selectedAddition_35 != null) {
        final Addition addition = REGISTRIES.additions.getEntry(stats.selectedAddition_35).get();
        final CharacterAdditionStats additionStats = charData.additionStats.computeIfAbsent(stats.selectedAddition_35, k -> new CharacterAdditionStats());

//        stats.addition_00_9c = addition._00;

        final AdditionHitMultiplierEvent event = EVENTS.postEvent(new AdditionHitMultiplierEvent(charData, additionStats, addition));
        stats.additionSpMultiplier_9e = Math.round(event.additionSpMulti * 100) - 100;
        stats.additionDamageMultiplier_9f = Math.round(event.additionDmgMulti * 100) - 100;
      } else {
        stats.additionDamageMultiplier_9f = 0;
      }

      //LAB_8011042c
      applyEquipmentStats(charId);

      final int v0 = dragoonGoodsBits_800fbd08[charId];
      if(hasDragoon(gameState_800babc8.goods_19c, charId)) {
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
      if(charId == 0 && gameState_800babc8.goods_19c.has(DIVINE_DRAGOON_SPIRIT)) {
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
        characterStats.equipmentGuardHeal += event.guardHealBonus;
      }
    }
  }
}
