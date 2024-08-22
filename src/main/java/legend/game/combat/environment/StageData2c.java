package legend.game.combat.environment;

public class StageData2c {
  public int unused_00;
  public int musicIndex_04;
  public int escapeChance_08;
  public int postCombatSubmapScene_0c;
  public int playerOpeningCamera_10;
  public int monsterOpeningCamera_14;
  public final int[] cameraPosIndices_18 = new int[4];
  public int postCombatSubmapCut_28;

  public StageData2c(final int unused, final int musicIndex, final int escapeChance, final int postCombatSubmapScene, final int playerOpeningCamera, final int monsterOpeningCamera, final int cameraPosIndex0, final int cameraPosIndex1, final int cameraPosIndex2, final int cameraPosIndex3, final int postCombatSubmapCut) {
    this.unused_00 = unused;
    this.musicIndex_04 = musicIndex;
    this.escapeChance_08 = escapeChance;
    this.postCombatSubmapScene_0c = postCombatSubmapScene;
    this.playerOpeningCamera_10 = playerOpeningCamera;
    this.monsterOpeningCamera_14 = monsterOpeningCamera;
    this.cameraPosIndices_18[0] = cameraPosIndex0;
    this.cameraPosIndices_18[1] = cameraPosIndex1;
    this.cameraPosIndices_18[2] = cameraPosIndex2;
    this.cameraPosIndices_18[3] = cameraPosIndex3;
    this.postCombatSubmapCut_28 = postCombatSubmapCut;
  }

  public int get(final int index) {
    return switch(index) {
      case 0 -> this.unused_00;
      case 1 -> this.musicIndex_04;
      case 2 -> this.escapeChance_08;
      case 3 -> this.postCombatSubmapScene_0c;
      case 4 -> this.playerOpeningCamera_10;
      case 5 -> this.monsterOpeningCamera_14;
      case 6 -> this.cameraPosIndices_18[0];
      case 7 -> this.cameraPosIndices_18[1];
      case 8 -> this.cameraPosIndices_18[2];
      case 9 -> this.cameraPosIndices_18[3];
      case 10 -> this.postCombatSubmapCut_28;
      default -> throw new IllegalArgumentException("Unknown stage data index " + index);
    };
  }

  public void set(final int index, final int value) {
    switch(index) {
      case 0 -> this.unused_00 = value;
      case 1 -> this.musicIndex_04 = value;
      case 2 -> this.escapeChance_08 = value;
      case 3 -> this.postCombatSubmapScene_0c = value;
      case 4 -> this.playerOpeningCamera_10 = value;
      case 5 -> this.monsterOpeningCamera_14 = value;
      case 6 -> this.cameraPosIndices_18[0] = value;
      case 7 -> this.cameraPosIndices_18[1] = value;
      case 8 -> this.cameraPosIndices_18[2] = value;
      case 9 -> this.cameraPosIndices_18[3] = value;
      case 10 -> this.postCombatSubmapCut_28 = value;
      default -> throw new IllegalArgumentException("Unknown stage data index " + index);
    }
  }
}
