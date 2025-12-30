package legend.game;

import legend.game.submap.SubmapEnvState;

public final class Scus94491BpeSegment_8005 {
  private Scus94491BpeSegment_8005() { }

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
