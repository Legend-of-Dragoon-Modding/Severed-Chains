package legend.game;

import legend.game.submap.SubmapEnvState;
import legend.game.types.AdditionData0e;

public final class Scus94491BpeSegment_8005 {
  private Scus94491BpeSegment_8005() { }

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

  public static boolean standingInSavePoint_8005a368;
}
