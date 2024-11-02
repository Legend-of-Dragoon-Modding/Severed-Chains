package legend.game.combat;

import legend.game.combat.types.AdditionHitProperties10;
import legend.game.combat.types.AdditionHits80;

public class AdditionConfigs {

  public static AdditionHits80[] additionHits_8010e658;
  public static final String[] additionNames_800fa8d4 = {
    "Double Slash", "Volcano", "Burning Rush", "Crush Dance", "Madness Hero", "Moon Strike", "Blazing Dynamo", "", // Dart
    "Harpoon", "Spinning Cane", "Rod Typhoon", "Gust of Wind Dance", "Flower Storm", "", // Lavitz
    "Whip Smack", "More & More", "Hard Blade", "Demon's Dance", "", // Rose
    "Pursuit", "Inferno", "Bone Crush", "", // Kongol
    "Double Smack", "Hammer Spin", "Cool Boogie", "Cat's Cradle", "Perky Step", "", // Meru
    "Double Punch", "Ferry of Styx", "Summon 4 Gods", "5-Ring Shattering", "Hex Hammer", "Omni-Sweep", // Haschel
    "Harpoon", "Spinning Cane", "Rod Typhoon", "Gust of Wind Dance", "Flower Storm" // Albert
  };

  private static boolean dirty = true; //Starts at true so the first load call actually loads the additions
  private static boolean fc = true; //Frame Correction (shorthanded to keep the ternaries in the createAdditionHits calls lean)

  // Call when you want additions to be reloaded at the start of the next combat (useful for a change in the settings to be applied)
  public static void setDirty() {
    dirty = true;
  }

  public static void setFrameCorrection(final boolean value) {
    fc = value;
  }

  public static boolean getFrameCorrection() {
    return fc;
  }

  public static void reload() {
    dirty = true;
    load();
  }

  public static void load() {

    if (!dirty) {
      return;
    }

    dirty = false;

    additionHits_8010e658 = new AdditionHits80[43];
    int additionIndex = 0;

    // !!! ORDER OF THESE IS SUPER IMPORTANT !!!

    // Dart
    additionHits_8010e658[additionIndex++] = createAdditionHits_Dart_DoubleSlash();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Dart_Volcano();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Dart_BurningRush();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Dart_CrushDance();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Dart_MadnessHero();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Dart_MoonStrike();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Dart_BlazingDynamo();

    // Unknown1
    additionHits_8010e658[additionIndex++] = createAdditionHits_Unknown1();

    // Lavitz
    additionHits_8010e658[additionIndex++] = createAdditionHits_Lavitz_Harpoon();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Lavitz_SpinningCane();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Lavitz_RodTyphoon();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Lavitz_GustOfWindDance();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Lavitz_FlowerStorm();

    // Unknown2
    additionHits_8010e658[additionIndex++] = createAdditionHits_Unknown2();

    // Rose
    additionHits_8010e658[additionIndex++] = createAdditionHits_Rose_WhipSmack();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Rose_MoreAndMore();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Rose_HardBlade();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Rose_DemonsDance();

    // Unknown3
    additionHits_8010e658[additionIndex++] = createAdditionHits_Unknown3();

    // Kongol
    additionHits_8010e658[additionIndex++] = createAdditionHits_Kongol_Pursuit();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Kongol_Inferno();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Kongol_BoneCrush();

    // Unknown4
    additionHits_8010e658[additionIndex++] = createAdditionHits_Unknown4();

    // Meru
    additionHits_8010e658[additionIndex++] = createAdditionHits_Meru_DoubleSmack();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Meru_HammerSpin();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Meru_CoolBoogie();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Meru_CatsCradle();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Meru_PerkyStep();

    // Unknown5
    additionHits_8010e658[additionIndex++] = createAdditionHits_Unknown5();

    // Haschel
    additionHits_8010e658[additionIndex++] = createAdditionHits_Haschel_DoublePunch();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Haschel_FlurryOfStyx();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Haschel_Summon4Gods();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Haschel_5RingShattering();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Haschel_HexHammer();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Haschel_OmniSweep();

    // Unknown6
    additionHits_8010e658[additionIndex++] = createAdditionHits_Unknown6();

    // Albert
    additionHits_8010e658[additionIndex++] = createAdditionHits_Albert_Harpoon();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Albert_SpinningCane();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Albert_RodTyphoon();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Albert_GustOfWindDance();
    additionHits_8010e658[additionIndex++] = createAdditionHits_Albert_FlowerStorm();

    // Unknown7
    additionHits_8010e658[additionIndex++] = createAdditionHits_Unknown7();

    // Unknown8
    additionHits_8010e658[additionIndex++] = createAdditionHits_Unknown8();
  }

  private static AdditionHitProperties10[] createAdditionHitPropertiesArray() {
    //There needs to be 8 elements in the array, so fill with empty elements
    final AdditionHitProperties10[] additionHitProperties = new AdditionHitProperties10[8];
    for (int i = 0; i < 8; i++) {
      additionHitProperties[i] = new AdditionHitProperties10(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }
    return additionHitProperties;
  }

  // totalFrames: length of the animation, when done, the next animation and overlay starts
  // overlayHitFrameOffset: the speed at which the overlay closes in
  // overlayStartingFrameOffset: frames before the overlay shows
  private static AdditionHitProperties10 createAdditionHitProperties(final int flags, final int totalFrames, int overlayHitFrameOffset, final int totalSuccessFrames, final int damageMultiplier, final int spValue, final int audioFile, final int isFinalHit, final int _08, final int _09, final int _0a, final int hitDistanceFromTarget, final int framesToHitPosition, final int _0d, final int _0e, final int overlayStartingFrameOffset) {
    if (false) { // -1 or +1 correction for people who would prefer it. There to potentially implement a setting that lets you adjust it or not?
      overlayHitFrameOffset = Math.max(0, overlayHitFrameOffset - 1);
    }
    else if (false) {
      overlayHitFrameOffset += 1;
    }
    return new AdditionHitProperties10(flags, totalFrames, overlayHitFrameOffset, totalSuccessFrames, damageMultiplier, spValue, audioFile, isFinalHit, _08, _09, _0a, hitDistanceFromTarget, framesToHitPosition, _0d, _0e, overlayStartingFrameOffset);
  }

  private static AdditionHits80 createAdditionHits_Dart_DoubleSlash() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 15, fc ? 8 : 9, 3, 100, 30, 0, 0, 0, 0, 8, 5, 12, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 19, 11, 2, 50, 5, 0, 4, 0, 8, 3, 3, 10, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Dart_Volcano() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 15, fc ? 8 : 9, 3, 50, 5, 1, 0, 0, 0, 8, 5, 12, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 33, fc ? 25 : 27, 3, 50, 5, 0, 0, 0, 25, 2, 1, 12, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 30, fc ? 13 : 15, 3, 50, 5, 0, 0, 0, 0, 0, 0, 13, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 23, 5, 2, 50, 5, 0, 4, -5, 0, 12, 5, 12, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Dart_BurningRush() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 15, fc ? 8 : 9, 3, 50, 10, 2, 0, 0, 0, 8, 5, 12, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 27, fc ? 11 : 12, 3, 50, 10, 0, 0, 0, 0, 27, 8, 10, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 22, 8, 2, 50, 10, 0, 4, -8, 5, 17, 8, 8, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Dart_CrushDance() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 15, fc ? 8 : 9, 3, 30, 10, 3, 0, 0, 0, 8, 5, 12, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 28, fc ? 20 : 22, 3, 30, 10, 0, 0, 0, 0, 12, 6, 11, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 26, fc ? 12 : 14, 3, 30, 10, 0, 0, 0, 0, 0, 0, 12, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 21, fc ? 12 : 14, 3, 30, 10, 0, 0, 0, 11, 4, 0, 15, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 24, 8, 2, 30, 10, 0, 4, -5, 5, 15, 10, 11, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Dart_MadnessHero() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 15, fc ? 8 : 9, 3, 20, 10, 4, 0, 0, 0, 8, 5, 12, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 15, fc ? 7 : 9, 3, 20, 10, 0, 0, 0, 0, 0, 0, 11, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 8, 2, 3, 20, 10, 0, 0, 0, 0, 0, 0, 10, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 8, 2, 3, 20, 10, 0, 0, 0, 0, 0, 0, 7, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 8, 2, 3, 10, 10, 0, 0, 0, 0, 0, 0, 12, 32, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 10, 2, 2, 10, 10, 0, 4, 0, 0, 0, 0, 11, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Dart_MoonStrike() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 15, fc ? 8 : 9, 3, 30, 8, 5, 0, 0, 0, 8, 5, 12, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 25, fc ? 17 : 19, 3, 30, 2, 0, 0, 0, 16, 3, 7, 9, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 14, fc ? 7 : 8, 3, 30, 2, 0, 0, 0, 0, 0, 0, 10, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 15, 10, 2, 30, 2, 0, 0, 0, 0, 0, 0, 9, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 12, fc ? 5 : 6, 3, 30, 2, 0, 0, 0, 4, 1, 1, 14, 32, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 12, fc ? 5 : 6, 3, 30, 2, 0, 0, 0, 0, 0, 0, 14, 32, 0, 0);
    additionHitProperties[6] = createAdditionHitProperties(0xc0, 59, 12, 2, 20, 2, 0, 4, -5, 0, 11, 3, 13, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Dart_BlazingDynamo() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xe0, 14, fc ? 8 : 9, 3, 40, 20, 6, 0, 0, 0, 8, 5, 12, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 23, 17, 3, 30, 20, 0, 0, 0, 0, 0, 0, 9, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 10, fc ? 6 : 5, 2, 30, 10, 0, 0, 0, 3, 2, 1, 7, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 12, fc ? 7 : 6, 3, 30, 10, 0, 0, 0, 0, 0, 0, 9, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 20, fc ? 15 : 14, 3, 30, 10, 0, 0, 0, 0, 13, 4, 13, 32, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 12, fc ? 11 : 7, 2, 30, 10, 0, 0, 0, 0, 0, 0, 11, 32, 18, 0);
    additionHitProperties[6] = createAdditionHitProperties(0xc0, 29, 9, 3, 30, 10, 0, 0, 3, 7, 11, 5, 10, 32, 0, 0);
    additionHitProperties[7] = createAdditionHitProperties(0xc0, 23, 17, 2, 30, 10, 0, 4, -8, 0, 16, 3, 13, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Unknown1() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 5, 0, 0, 100, 0, 7, 0, 0, 0, 4, 1, 8, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 15, 4, 0, 10, 0, 0, 0, 0, 0, 14, 0, 7, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 16, 10, 0, 20, 0, 0, 0, 0, 6, 8, 1, 10, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 11, 5, 0, 30, 0, 0, 0, 0, 4, 6, 0, 9, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 19, 5, 0, 40, 0, 0, 0, 0, 0, 18, 0, 9, 32, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 68, 39, 0, 0, 0, 0, 0, 0, 0, 67, 0, 11, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Lavitz_Harpoon() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 17, 11, 3, 75, 30, 8, 0, 0, 2, 5, 7, 16, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 25, 13, 2, 25, 5, 0, 4, -8, 0, 8, 12, 14, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Lavitz_SpinningCane() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 17, 11, 3, 50, 15, 9, 0, 2, 2, 5, 7, 16, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 22, 16, 3, 25, 10, 0, 0, 0, 0, 17, 5, 16, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 25, 20, 2, 25, 10, 0, 4, -4, 10, 8, 5, 17, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Lavitz_RodTyphoon() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 17, 11, 3, 30, 6, 10, 0, 2, 2, 5, 7, 16, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 19, 13, 3, 30, 6, 0, 0, -3, 11, 7, 3, 15, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 13, 7, 3, 30, 6, 0, 0, -2, 0, 9, 11, 12, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 16, 10, 3, 30, 6, 0, 0, -2, 0, 12, 11, 14, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 17, 12, 2, 30, 6, 0, 4, -9, 0, 17, 13, 18, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Lavitz_GustOfWindDance() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 17, 11, 3, 30, 5, 11, 0, 0, 2, 5, 7, 16, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 29, 14, 3, 30, 5, 0, 0, 0, 0, 29, 6, 15, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 22, 15, 3, 30, 5, 0, 0, 0, 4, 11, 23, 9, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 10, 5, 2, 30, 5, 0, 0, 0, 4, 1, 1, 18, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 22, 16, 3, 30, 5, 0, 0, 0, 11, 5, 3, 22, 32, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 13, 7, 3, 30, 5, 0, 0, -4, 3, 4, 7, 18, 32, 0, 0);
    additionHitProperties[6] = createAdditionHitProperties(0xc0, 40, 19, 2, 20, 5, 0, 4, -8, 0, 40, 6, 24, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Lavitz_FlowerStorm() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xe0, 17, 11, 3, 30, 8, 12, 0, 0, 2, 5, 7, 16, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 19, 13, 3, 30, 8, 0, 0, 0, 0, 0, 0, 18, 32, 18, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 27, 20, 2, 30, 8, 0, 0, 0, 14, 12, 12, 12, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 16, 10, 3, 40, 8, 0, 0, 0, 4, 5, 1, 22, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 22, 16, 3, 40, 8, 0, 0, 0, 10, 5, 3, 18, 32, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 17, 12, 2, 40, 8, 0, 0, 0, 0, 14, 6, 22, 32, 0, 0);
    additionHitProperties[6] = createAdditionHitProperties(0xc0, 20, 14, 3, 40, 6, 0, 0, 0, 11, 7, 14, 20, 32, 19, 0);
    additionHitProperties[7] = createAdditionHitProperties(0xc0, 26, 7, 2, 50, 6, 0, 4, 0, 0, 26, 10, 10, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Unknown2() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 2, 0, 0, 100, 0, 13, 0, 0, 0, 1, 7, 10, 32, 18, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 14, 5, 0, 10, 0, 0, 0, 0, 0, 0, 0, 7, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 32, 25, 0, 20, 0, 0, 0, 0, 4, 21, 2, 7, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 10, 1, 0, 30, 0, 0, 0, 0, 1, 8, 29, 7, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 11, 10, 0, 40, 0, 0, 0, 0, 0, 10, 29, 5, 32, 19, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 61, 29, 0, 0, 0, 0, 0, 0, 0, 60, 5, 7, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Rose_WhipSmack() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 11, 5, 3, 75, 30, 14, 0, 3, 2, 2, 9, 12, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 16, 11, 2, 25, 5, 0, 4, 0, 0, 0, 0, 8, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Rose_MoreAndMore() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 11, 5, 3, 50, 10, 15, 0, 3, 2, 2, 5, 12, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 15, 9, 3, 50, 10, 0, 0, -4, 8, 2, 7, 8, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 33, 8, 2, 50, 10, 0, 4, 0, 6, 2, 11, 6, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Rose_HardBlade() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 11, 5, 3, 20, 10, 16, 0, 0, 2, 2, 9, 12, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 17, 11, 3, 20, 5, 0, 0, 0, 10, 2, 8, 8, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 22, 16, 2, 20, 5, 0, 0, 0, 0, 2, 0, 12, 32, 18, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 11, 5, 3, 20, 5, 0, 0, 0, 0, 2, 0, 7, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 22, 15, 3, 10, 5, 0, 0, 0, 13, 2, 13, 12, 32, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 17, 12, 2, 10, 5, 0, 4, -8, 10, 3, 8, 12, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Rose_DemonsDance() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xe0, 11, 5, 3, 30, 20, 17, 0, 0, 2, 2, 9, 12, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 12, 6, 3, 30, 20, 0, 0, 0, 0, 0, 0, 10, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 14, 9, 2, 30, 10, 0, 0, 0, 6, 3, 8, 10, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 19, 13, 3, 30, 10, 0, 0, 0, 16, 2, 8, 10, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 23, 14, 3, 20, 10, 0, 0, 3, 12, 3, 10, 10, 32, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 30, 10, 2, 20, 10, 0, 0, -4, 10, 11, 3, 7, 32, 0, 0);
    additionHitProperties[6] = createAdditionHitProperties(0xc0, 20, 11, 3, 20, 10, 0, 0, 5, 8, 4, 12, 9, 32, 0, 0);
    additionHitProperties[7] = createAdditionHitProperties(0xc0, 26, 4, 2, 20, 10, 0, 4, 12, 0, 0, 0, 7, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Unknown3() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 0, 0, 0, 100, 0, 18, 0, 0, 0, 0, 0, 12, 0, 0, 0);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 12, 0, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 12, 0, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 9, 32, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 0, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Kongol_Pursuit() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 8, 2, 3, 75, 30, 19, 0, 10, 0, 0, 0, 16, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 30, 21, 2, 25, 5, 0, 4, -10, 0, 0, 0, 2, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Kongol_Inferno() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 8, 2, 3, 40, 5, 20, 0, 15, 0, 0, 0, 20, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 56, 46, 3, 20, 5, 0, 0, 15, 0, 0, 0, 15, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 9, 3, 3, 20, 5, 0, 0, -8, 0, 1, 12, 18, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 42, 13, 2, 20, 5, 0, 4, -5, 0, 11, 12, 18, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Kongol_BoneCrush() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xe0, 8, 2, 3, 50, 20, 21, 0, 10, 0, 0, 0, 20, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 19, 13, 3, 30, 20, 0, 0, 0, 11, 2, 7, 15, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 10, 5, 2, 30, 20, 0, 0, -3, 3, 2, 3, 15, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 16, 8, 3, 30, 20, 0, 0, -3, 5, 1, 3, 15, 32, 19, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 10, 3, 3, 30, 10, 0, 0, -8, 0, 2, 7, 13, 32, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 21, 12, 2, 30, 10, 0, 4, -13, 0, 11, 20, 15, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Unknown4() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 0, 0, 0, 100, 0, 22, 0, 0, 0, 0, 0, 7, 0, 0, 0);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Meru_DoubleSmack() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 7, 1, 3, 75, 10, 23, 0, 0, 0, 0, 0, 9, 32, 18, 13);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 21, 6, 2, 25, 10, 0, 4, -6, 0, 6, 5, 10, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Meru_HammerSpin() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 7, 1, 3, 50, 20, 24, 0, 0, 0, 0, 0, 9, 32, 18, 13);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 26, 8, 3, 50, 5, 0, 0, 0, 6, 12, 8, 10, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 11, 3, 3, 25, 5, 0, 0, 0, 0, 3, 3, 9, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 71, 16, 3, 25, 5, 0, 4, -6, 0, 6, 10, 6, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0, 0, 0, 0, 0, 0, 0, 0, -8, 0, 0, 0, 0, 0, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Meru_CoolBoogie() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 7, 1, 3, 20, 12, 25, 0, 0, 0, 0, 0, 9, 32, 18, 13);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 19, 8, 3, 20, 12, 0, 0, -7, 0, 9, 6, 9, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 34, 17, 3, 20, 12, 0, 0, 3, 6, 11, 20, 10, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 16, 2, 3, 20, 12, 0, 0, 0, 0, 0, 0, 15, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 23, 11, 2, 20, 12, 0, 4, -2, 9, 4, 6, 9, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Meru_CatsCradle() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 7, 1, 3, 30, 3, 26, 0, 0, 0, 0, 0, 9, 32, 18, 13);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 12, 6, 3, 20, 3, 0, 0, 0, 5, 3, 1, 12, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 16, 9, 3, 20, 3, 0, 0, 0, 0, 11, 6, 15, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 16, 8, 2, 20, 3, 0, 0, 0, 0, 12, 14, 14, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 18, 1, 3, 20, 3, 0, 0, 10, 0, 2, 3, 12, 32, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 27, 18, 3, 20, 3, 0, 0, -5, 0, 22, 5, 15, 32, 19, 0);
    additionHitProperties[6] = createAdditionHitProperties(0xc0, 29, 1, 2, 20, 2, 0, 4, -9, 0, 3, 4, 15, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Meru_PerkyStep() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xe0, 7, 1, 3, 30, 20, 27, 0, 0, 0, 0, 0, 9, 32, 18, 13);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 24, 16, 3, 30, 20, 0, 0, 0, 6, 14, 10, 14, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 21, 1, 2, 30, 10, 0, 0, 0, 0, 4, 7, 14, 32, 19, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 19, 5, 3, 30, 10, 0, 0, -12, 0, 6, 15, 12, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 8, 1, 3, 20, 10, 0, 0, 15, 0, 6, 10, 22, 32, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 14, 4, 2, 20, 10, 0, 0, 0, 6, 3, 4, 16, 32, 0, 0);
    additionHitProperties[6] = createAdditionHitProperties(0xc0, 19, 7, 3, 20, 10, 0, 0, -3, 0, 6, 1, 6, 32, 0, 0);
    additionHitProperties[7] = createAdditionHitProperties(0xc0, 10, 1, 2, 20, 10, 0, 4, -2, 0, 2, 4, 18, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Unknown5() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 0, 0, 0, 100, 0, 28, 0, 0, 0, 0, 0, 6, 0, 0, 0);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Haschel_DoublePunch() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 15, 9, 3, 75, 30, 29, 0, 0, 0, 0, 0, 8, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 23, 3, 2, 25, 5, 0, 4, 0, 0, 0, 0, 8, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Haschel_FlurryOfStyx() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 15, 9, 3, 100, 10, 30, 0, 0, 0, 0, 0, 8, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 19, 3, 3, 25, 5, 0, 0, -8, 0, 0, 0, 10, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 51, 42, 2, 25, 5, 0, 4, -8, 3, 3, 0, 13, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Haschel_Summon4Gods() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 15, 9, 3, 25, 20, 31, 0, 0, 0, 0, 0, 8, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 15, 9, 3, 25, 10, 0, 0, -3, 0, 0, 0, 5, 32, 19, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 19, 11, 3, 25, 10, 0, 0, 0, 0, 9, 8, 5, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 24, 10, 2, 25, 10, 0, 4, 5, 0, 0, 0, 8, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Haschel_5RingShattering() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 15, 9, 3, 30, 7, 32, 0, 0, 0, 0, 0, 8, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 15, 9, 3, 30, 7, 0, 0, 0, 6, 3, 5, 4, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 12, 6, 2, 30, 7, 0, 0, -3, 4, 2, 3, 5, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 10, 4, 3, 30, 7, 0, 0, 0, 0, 10, 1, 6, 32, 19, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 33, 7, 2, 30, 7, 0, 4, -5, 0, 0, 0, 10, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Haschel_HexHammer() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 15, 9, 3, 30, 3, 33, 0, 0, 0, 0, 0, 8, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 13, 7, 3, 30, 2, 0, 0, 0, 4, 2, 5, 8, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 13, 4, 2, 30, 2, 0, 0, 0, 3, 1, 3, 8, 0, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 11, 5, 3, 30, 2, 0, 0, 0, 3, 2, 5, 6, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 12, 5, 3, 30, 2, 0, 0, 0, 2, 2, 3, 5, 32, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 13, 8, 2, 30, 2, 0, 0, 0, 0, 9, 5, 10, 32, 0, 0);
    additionHitProperties[6] = createAdditionHitProperties(0xc0, 23, 10, 2, 20, 2, 0, 4, -10, 9, 1, 8, 11, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Haschel_OmniSweep() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xe0, 15, 9, 3, 30, 8, 34, 0, 0, 0, 0, 0, 8, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 18, 8, 2, 30, 6, 0, 0, 0, 5, 3, 4, 5, 0, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 12, 3, 3, 30, 6, 0, 0, 0, 0, 2, 1, 10, 0, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 11, 6, 2, 40, 6, 0, 0, 0, 5, 2, 3, 10, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 16, 6, 3, 40, 6, 0, 0, 0, 0, 0, 0, 8, 32, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 8, 3, 2, 40, 6, 0, 0, -3, 0, 3, 4, 6, 32, 0, 0);
    additionHitProperties[6] = createAdditionHitProperties(0xc0, 19, 11, 3, 40, 6, 0, 0, 0, 4, 6, 7, 12, 32, 0, 0);
    additionHitProperties[7] = createAdditionHitProperties(0xc0, 37, 3, 2, 50, 6, 0, 4, -7, 0, 2, 15, 10, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Unknown6() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 0, 0, 0, 100, 0, 35, 0, 0, 0, 0, 0, 6, 0, 0, 0);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 54, 0, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Albert_Harpoon() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 8, 2, 3, 75, 30, 36, 0, 5, 0, 1, 0, 19, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 44, 15, 2, 25, 5, 0, 4, 0, 3, 10, 3, 10, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Albert_SpinningCane() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 8, 2, 3, 50, 15, 37, 0, 5, 0, 1, 0, 19, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 24, 19, 3, 25, 10, 0, 0, 4, 0, 4, 0, 8, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 20, 4, 2, 25, 10, 0, 4, 0, 0, 0, 0, 6, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Albert_RodTyphoon() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 8, 2, 3, 30, 6, 38, 0, 0, 0, 1, 3, 19, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 31, 22, 3, 30, 6, 0, 0, 5, 0, 20, 0, 16, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 11, 4, 3, 30, 6, 0, 0, 0, 0, 0, 0, 16, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 8, 2, 3, 30, 6, 0, 0, 0, 0, 1, 3, 19, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 36, 3, 2, 30, 6, 0, 4, 0, 0, 0, 0, 18, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Albert_GustOfWindDance() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 8, 2, 3, 30, 5, 39, 0, 5, 0, 1, 0, 19, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 33, 19, 3, 30, 5, 0, 0, -3, 17, 2, 4, 11, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 32, 15, 3, 30, 5, 0, 0, 3, 10, 3, 15, 17, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 6, 1, 2, 30, 5, 0, 0, 0, 0, 1, 0, 16, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 11, 5, 3, 30, 5, 0, 0, 0, 0, 0, 0, 16, 32, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 12, 6, 3, 30, 5, 0, 0, 0, 4, 2, 3, 15, 32, 0, 0);
    additionHitProperties[6] = createAdditionHitProperties(0xc0, 34, 12, 2, 20, 5, 0, 4, 0, 4, 9, 13, 16, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Albert_FlowerStorm() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xe0, 8, 2, 3, 30, 8, 40, 0, 0, 0, 1, 0, 19, 32, 0, 11);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 26, 12, 3, 30, 8, 0, 0, -12, 10, 6, 8, 10, 32, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 17, 3, 2, 30, 8, 0, 0, 12, 0, 3, 22, 13, 32, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 9, 3, 3, 40, 8, 0, 0, 0, 0, 3, 3, 10, 32, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 16, 10, 3, 40, 8, 0, 0, 6, 0, 9, 0, 0, 32, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 10, 2, 2, 40, 8, 0, 0, 0, 0, 3, 0, 0, 32, 0, 0);
    additionHitProperties[6] = createAdditionHitProperties(0xc0, 21, 14, 3, 40, 6, 0, 0, 0, 19, 2, 0, 0, 32, 19, 0);
    additionHitProperties[7] = createAdditionHitProperties(0xc0, 16, 7, 2, 50, 6, 0, 4, -5, 0, 8, 16, 18, 32, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Unknown7() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 0, 0, 0, 100, 0, 41, 0, 0, 0, 0, 0, 12, 0, 18, 0);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }

  private static AdditionHits80 createAdditionHits_Unknown8() {
    final AdditionHitProperties10[] additionHitProperties = createAdditionHitPropertiesArray();
    additionHitProperties[0] = createAdditionHitProperties(0xc0, 0, 0, 0, 200, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    additionHitProperties[1] = createAdditionHitProperties(0xc0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    additionHitProperties[2] = createAdditionHitProperties(0xc0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    additionHitProperties[3] = createAdditionHitProperties(0xc0, 0, 0, 0, 60, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    additionHitProperties[4] = createAdditionHitProperties(0xc0, 0, 0, 0, 80, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    additionHitProperties[5] = createAdditionHitProperties(0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    return new AdditionHits80(additionHitProperties);
  }
}
