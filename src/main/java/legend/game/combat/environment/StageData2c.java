package legend.game.combat.environment;

public class StageData2c {
  public int _00;
  public int musicIndex_04;
  public int _08;
  public int postCombatSubmapStage_0c;
  public int _10;
  public int _14;
  public final int[] cameraPosIndices_18 = new int[4];
  public int postCombatSubmapCut_28;

  public StageData2c(final int _00, final int musicIndex, final int _08, final int postCombatSubmapStage, final int _10, final int _14, final int cameraPosIndex0, final int cameraPosIndex1, final int cameraPosIndex2, final int cameraPosIndex3, final int postCombatSubmapCut) {
    this._00 = _00;
    this.musicIndex_04 = musicIndex;
    this._08 = _08;
    this.postCombatSubmapStage_0c = postCombatSubmapStage;
    this._10 = _10;
    this._14 = _14;
    this.cameraPosIndices_18[0] = cameraPosIndex0;
    this.cameraPosIndices_18[1] = cameraPosIndex1;
    this.cameraPosIndices_18[2] = cameraPosIndex2;
    this.cameraPosIndices_18[3] = cameraPosIndex3;
    this.postCombatSubmapCut_28 = postCombatSubmapCut;
  }

  public int get(final int index) {
    return switch(index) {
      case 0 -> this._00;
      case 1 -> this.musicIndex_04;
      case 2 -> this._08;
      case 3 -> this.postCombatSubmapStage_0c;
      case 4 -> this._10;
      case 5 -> this._14;
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
      case 0 -> this._00 = value;
      case 1 -> this.musicIndex_04 = value;
      case 2 -> this._08 = value;
      case 3 -> this.postCombatSubmapStage_0c = value;
      case 4 -> this._10 = value;
      case 5 -> this._14 = value;
      case 6 -> this.cameraPosIndices_18[0] = value;
      case 7 -> this.cameraPosIndices_18[1] = value;
      case 8 -> this.cameraPosIndices_18[2] = value;
      case 9 -> this.cameraPosIndices_18[3] = value;
      case 10 -> this.postCombatSubmapCut_28 = value;
      default -> throw new IllegalArgumentException("Unknown stage data index " + index);
    }
  }
}
