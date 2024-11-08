package legend.game;

import legend.core.gte.MV;
import legend.game.sound.ReverbConfig;
import legend.game.sound.ReverbConfigAndLocation;
import legend.game.submap.SubmapEnvState;
import legend.game.tmd.UvAdjustmentMetrics14;
import legend.game.types.AdditionData0e;
import legend.game.types.LodString;

import java.util.Arrays;

public final class Scus94491BpeSegment_8005 {
  private Scus94491BpeSegment_8005() { }

  public static final int[] submapMusic_80050068 = {
    -1, -1, 23, 28, 44, 20, 22, -1,
    29, 40, 30, 22, 24, 22, 31, 42,
    -1, 32, 45, 29, 40, 27, 33, 21,
    21, -1, 48, 46, -1, 22, 38, -1,
    33, 23, 36, 49, 28, 39, 50, -1,
    47, -1, 26, 27, 26, 42, 45, -1,
    27, 39, 27, 52, -1, 38, 53, 54,
    55, -1, -1, -1, -1, -1, -1, -1,
  };
  public static final int[] monsterSoundFileIndices_800500e8 = {4, 5, 6, 7};
  public static final int[] characterSoundFileIndices_800500f8 = {1, 2, 3};

  public static final int[] charSlotSpuOffsets_80050190 = {0x44250, 0x4b780, 0x52cb0};

  public static final int[] combatSoundEffectsTypes_8005019c = {
    12, 13, 86, 12, 12, 12, 12, 12,
    12, 12, 12, 12, 12, 12, 12, 12,
    14, 15, 88, 90, 14, 14, 14, 14,
    14, 14, 14, 14, 14, 14, 14, 14,
  };
  public static final int[] combatMusicFileIndices_800501bc = {
    702, 707, 722, 702, 702, 702, 702, 702,
    702, 702, 702, 702, 702, 702, 702, 702,
    712, 717, 727, 732, 712, 712, 712, 712,
    712, 712, 712, 712, 712, 712, 712, 712,
  };

  public static final UvAdjustmentMetrics14[] vramSlots_8005027c = {
    UvAdjustmentMetrics14.NONE,
    new UvAdjustmentMetrics14( 1, 320, 496, 320, 256, false),
    new UvAdjustmentMetrics14( 2, 384, 496, 384, 256, false),
    new UvAdjustmentMetrics14( 3, 448, 496, 448, 256, false),
    new UvAdjustmentMetrics14( 4, 512, 496, 512, 256, false),
    new UvAdjustmentMetrics14( 5, 576, 496, 576, 256, false),
    new UvAdjustmentMetrics14( 6, 640, 496, 640, 256, false),
    new UvAdjustmentMetrics14( 7, 576, 496, 576, 256, false),
    new UvAdjustmentMetrics14( 8, 640, 496, 640, 256, false),
    new UvAdjustmentMetrics14( 9, 704, 496, 704, 256, false),
    new UvAdjustmentMetrics14(10, 768, 240, 768,   0, false),
    new UvAdjustmentMetrics14(11, 832, 240, 832,   0, false),
    new UvAdjustmentMetrics14(12, 896, 240, 896,   0, false),
    new UvAdjustmentMetrics14(13, 960, 240, 960,   0, false),
    new UvAdjustmentMetrics14(14, 512, 240, 512,   0, false),
    new UvAdjustmentMetrics14(15, 576, 240, 576,   0, false),
    new UvAdjustmentMetrics14(16, 640, 240, 640,   0, false),
    new UvAdjustmentMetrics14(17, 704, 240, 704,   0, false),
  };

  public static final int[] shadowScale_8005039c = {0x1800, 0x1800, 0x1000, 0xe00, 0x1600, 0x1300, 0xe00, 0x2000, 0x1300, 0x1500};

  public static final String[] spellCombatDescriptions_80052018 = {
    "Fire STR 50% Single", "Fire STR 25% All", "Fire STR 75% Single", "Fire STR 75% All", "STR 150% Single",
    "Wind STR 25% All", "Wind STR 75% Single", "Damage Resist 50% 3 Turns", "Wind STR 75% All", "STR 100% All",
    "Light STR 25% All", "Ally 50% Rev, 100% Rec, Cure", "Ally All 50% Rev & Rec, Cure", "Light STR 75% & Allies 100% Rec", "Wind STR 25% All",
    "Dark STR 50% Single & HP", "Dark STR 25% All & Fear", "Wind STR 75% Single", "Drags minor enemies to Hell", "Dark STR 100% Single",
    "Thunder STR 25% Single", "Thunder STR 50% Single & Stun", "Thunder STR 75% Single", "Thunder STR 100% Single", "Water STR 50% Single",
    "Ally All 50% Rec, Cure", "Damage Resist 50% 3 Turns", "Water STR 50% All", "Water STR 100% Single", "Earth STR 37.5% All",
    "Earth STR 50% All", "Earth STR 75% All", "", "", "",
    "", "", "", "", "",
    "", "", "", "", "",
    "", "", "", "", "",
    "", "", "", "", "",
    "", "", "", "", "",
    "", "", "", "", "",
    "Light STR 25% All", "Ally 50% Rev, 100% Rec, Cure", "Ally All 50% Rev & Rec, Cure", "", "",
    "", "", "", "", "",
    "", "", "", "", "",
    "", "", "", "",
  };

  public static final String[] spells_80052734 = {
    "Flameshot", "Explosion", "Final Burst", "Red-Eyed Dragon", "Divine DG Cannon",
    "Wing Blaster", "Gaspless", "Blossom Storm", "Jade Dragon", "Divine DG Ball",
    "Star Children", "Moon Light", "Gates of Heaven", "W Silver Dragon", "Wing Blaster",
    "Astral Drain", "Death Dimension", "Gaspless", "Demon's Gate", "Dark Dragon",
    "Atomic Mind", "Thunder Kid", "Thunder God", "Violet Dragon", "Freezing Ring",
    "Rainbow Breath", "Rose Storm", "Diamond Dust", "Blue Sea Dragon", "Grand Stream",
    "Meteor Strike", "Golden Dragon", "", "", "",
    "", "", "", "", "",
    "", "", "", "", "",
    "", "", "", "", "",
    "", "", "", "", "",
    "", "", "", "", "",
    "", "", "", "", "",
    "Star Children", "Moon Light", "Gates of Heaven", "", "",
    "", "", "", "", "",
    "", "", "", "", "",
    "", "", "", "",
  };
  public static final AdditionData0e[] additionData_80052884 = {
    new AdditionData0e(1, 1, new int[] {35, 35, 35, 35, 35}, 150),
    new AdditionData0e(2, 3, new int[] {20, 24, 28, 32, 36}, 200),
    new AdditionData0e(8, 2, new int[] {30, 45, 60, 75, 102}, 150),
    new AdditionData0e(15, 4, new int[] {50, 60, 75, 85, 100}, 150),
    new AdditionData0e(22, 5, new int[] {60, 90, 120, 150, 204}, 100),
    new AdditionData0e(29, 6, new int[] {20, 20, 20, 20, 20}, 200),
    new AdditionData0e(-1, 7, new int[] {100, 110, 120, 130, 150}, 250),
    new AdditionData0e(-2, 5, new int[] {0, 0, 0, 0, 0}, 0),
    new AdditionData0e(1, 1, new int[] {35, 38, 42, 45, 50}, 100),
    new AdditionData0e(5, 2, new int[] {35, 35, 35, 35, 35}, 100),
    new AdditionData0e(7, 4, new int[] {30, 45, 60, 75, 100}, 150),
    new AdditionData0e(11, 6, new int[] {35, 35, 35, 35, 35}, 200),
    new AdditionData0e(-1, 7, new int[] {60, 90, 120, 150, 202}, 300),
    new AdditionData0e(-2, 5, new int[] {0, 0, 0, 0, 0}, 0),
    new AdditionData0e(1, 1, new int[] {35, 35, 35, 35, 35}, 100),
    new AdditionData0e(14, 2, new int[] {30, 45, 60, 75, 102}, 150),
    new AdditionData0e(19, 5, new int[] {35, 35, 35, 35, 35}, 100),
    new AdditionData0e(-1, 7, new int[] {100, 100, 100, 100, 100}, 200),
    new AdditionData0e(-2, 5, new int[] {0, 0, 0, 0, 0}, 0),
    new AdditionData0e(1, 1, new int[] {35, 38, 42, 45, 50}, 100),
    new AdditionData0e(23, 3, new int[] {20, 20, 20, 20, 20}, 100),
    new AdditionData0e(-1, 5, new int[] {100, 100, 100, 100, 100}, 200),
    new AdditionData0e(-2, 5, new int[] {0, 0, 0, 0, 0}, 0),
    new AdditionData0e(1, 1, new int[] {20, 24, 28, 32, 34}, 100),
    new AdditionData0e(21, 3, new int[] {35, 43, 51, 59, 70}, 150),
    new AdditionData0e(26, 4, new int[] {60, 90, 120, 150, 200}, 100),
    new AdditionData0e(30, 6, new int[] {20, 20, 20, 20, 20}, 150),
    new AdditionData0e(-1, 7, new int[] {100, 100, 100, 100, 100}, 200),
    new AdditionData0e(-2, 5, new int[] {0, 0, 0, 0, 0}, 0),
    new AdditionData0e(1, 1, new int[] {35, 38, 42, 45, 50}, 100),
    new AdditionData0e(14, 2, new int[] {20, 20, 20, 20, 20}, 150),
    new AdditionData0e(18, 3, new int[] {50, 61, 75, 86, 100}, 100),
    new AdditionData0e(22, 4, new int[] {35, 35, 40, 45, 50}, 150),
    new AdditionData0e(26, 6, new int[] {15, 15, 15, 15, 15}, 200),
    new AdditionData0e(-1, 7, new int[] {50, 75, 100, 125, 150}, 300),
    new AdditionData0e(-2, 5, new int[] {0, 0, 0, 0, 0}, 0),
    new AdditionData0e(1, 1, new int[] {35, 38, 42, 45, 50}, 100),
    new AdditionData0e(5, 2, new int[] {35, 35, 35, 35, 35}, 100),
    new AdditionData0e(7, 4, new int[] {30, 45, 60, 75, 100}, 150),
    new AdditionData0e(11, 6, new int[] {35, 35, 35, 35, 35}, 200),
    new AdditionData0e(-1, 7, new int[] {60, 90, 120, 150, 202}, 300),
    new AdditionData0e(-2, 5, new int[] {0, 0, 0, 0, 0}, 0),
    new AdditionData0e(-2, 5, new int[] {0, 0, 0, 0, 0}, 0),
  };

  public static final LodString[] digits_80052b40 = { new LodString("0"), new LodString("1"), new LodString("2"), new LodString("3"), new LodString("4"), new LodString("5"), new LodString("6"), new LodString("7"), new LodString("8"), new LodString("9") };
  public static final boolean[] renderBorder_80052b68 = {false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
  public static final int[] textboxMode_80052b88 = {0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
  public static final int[] textboxTextType_80052ba8 = {0, 1, 2, 3, 4, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};

  public static int submapCut_80052c30 = 675;
  public static int submapScene_80052c34 = 4;
  /** The collision primitive index the player is currently collided with */
  public static int collidedPrimitiveIndex_80052c38;
  public static int submapCutBeforeBattle_80052c3c = -1;
  /** Moved from SMAP since it's referenced unconditionally when saving the game */
  public static int submapCutForSave_800cb450;
  /** Something related to submap camera and map transitioning */
  public static boolean shouldRestoreCameraPosition_80052c40;
  public static SubmapEnvState submapEnvState_80052c44 = SubmapEnvState.CHECK_TRANSITIONS_1_2;

  public static int matrixStackIndex_80054a08;
  public static final MV[] matrixStack_80054a0c = new MV[20];
  static {
    Arrays.setAll(matrixStack_80054a0c, i -> new MV());
  }

  public static final ReverbConfigAndLocation[] reverbConfigs_80059f7c = {
    new ReverbConfigAndLocation(0xfb28, new ReverbConfig(125, 91, 28032, 21688, -16688, 0, 0, -17792, 22528, 21248, 1238, 819, 1008, 551, 884, 495, 820, 437, 0, 0, 0, 0, 0, 0, 0, 0, 436, 310, 184, 92, -32768, -32768)),
    new ReverbConfigAndLocation(0xfc18, new ReverbConfig(51, 37, 28912, 20392, -17184, 17424, -16144, -25600, 21120, 20160, 996, 795, 932, 687, 882, 614, 796, 605, 604, 398, 559, 309, 466, 183, 399, 181, 180, 128, 76, 38, -32768, -32768)),
    new ReverbConfigAndLocation(0xf6f8, new ReverbConfig(177, 127, 28912, 20392, -17184, 17680, -16656, -19264, 21120, 20160, 2308, 1899, 2084, 1631, 1954, 1558, 1900, 1517, 1516, 1070, 1295, 773, 1122, 695, 1071, 613, 612, 434, 256, 128, -32768, -32768)),
    new ReverbConfigAndLocation(0xf204, new ReverbConfig(227, 169, 28512, 20392, -17184, 17680, -16656, -22912, 22144, 21184, 3579, 2904, 3337, 2620, 3033, 2419, 2905, 2266, 2265, 1513, 2028, 1200, 1775, 978, 1514, 797, 796, 568, 340, 170, -32768, -32768)),
    new ReverbConfigAndLocation(0xea44, new ReverbConfig(421, 313, 24576, 20480, 19456, -18432, -17408, -16384, 24576, 23552, 5562, 4539, 5314, 4285, 4540, 3521, 4544, 3523, 3520, 2497, 3012, 1985, 2560, 1741, 2498, 1473, 1472, 1050, 628, 314, -32768, -32768)),
    new ReverbConfigAndLocation(0xe128, new ReverbConfig(829, 561, 32256, 20480, -19456, -20480, 19456, -20480, 24576, 21504, 7894, 6705, 7444, 6203, 7106, 5810, 6706, 5615, 5614, 4181, 4916, 3885, 4598, 3165, 4182, 2785, 2784, 1954, 1124, 562, -32768, -32768)),
    new ReverbConfigAndLocation(0xe000, new ReverbConfig(1, 1, 32767, 32767, 0, 0, 0, -32512, 0, 0, 8191, 4095, 4101, 5, 0, 0, 4101, 5, 0, 0, 0, 0, 0, 0, 0, 0, 4100, 4098, 4, 2, -32768, -32768)),
    new ReverbConfigAndLocation(0xe000, new ReverbConfig(1, 1, 32767, 32767, 0, 0, 0, 0, 0, 0, 8191, 4095, 4101, 5, 0, 0, 4101, 5, 0, 0, 0, 0, 0, 0, 0, 0, 4100, 4098, 4, 2, -32768, -32768)),
    new ReverbConfigAndLocation(0xfc8c, new ReverbConfig(23, 19, 28912, 20392, -17184, 17680, -16656, -31488, 24448, 21696, 881, 687, 741, 479, 688, 471, 856, 618, 470, 286, 301, 177, 287, 89, 416, 227, 88, 64, 40, 20, -32768, -32768)),
  };

  /** short */
  public static int sssqFadeCurrent_8005a1ce;

  public static boolean standingInSavePoint_8005a368;
}
