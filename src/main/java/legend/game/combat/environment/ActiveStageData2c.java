package legend.game.combat.environment;

public class ActiveStageData2c{
  public int _00;
  public int musicIndex_04;
  public int _08;
  public int postCombatSubmapStage_0c;
  public int _10;
  public int _14;
  public int cameraPosIndex0_18;
  public int cameraPosIndex1_1c;
  public int cameraPosIndex2_20;
  public int cameraPosIndex3_24;
  public int postCombatSubmapCut_28;

  public int get(final int index) {
    return switch(index) {
      case 0 -> this._00;
      case 1 -> this.musicIndex_04;
      case 2 -> this._08;
      case 3 -> this.postCombatSubmapStage_0c;
      case 4 -> this._10;
      case 5 -> this._14;
      case 6 -> this.cameraPosIndex0_18;
      case 7 -> this.cameraPosIndex1_1c;
      case 8 -> this.cameraPosIndex2_20;
      case 9 -> this.cameraPosIndex3_24;
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
      case 6 -> this.cameraPosIndex0_18 = value;
      case 7 -> this.cameraPosIndex1_1c = value;
      case 8 -> this.cameraPosIndex2_20 = value;
      case 9 -> this.cameraPosIndex3_24 = value;
      case 10 -> this.postCombatSubmapCut_28 = value;
      default -> throw new IllegalArgumentException("Unknown stage data index " + index);
    }
  }
}
