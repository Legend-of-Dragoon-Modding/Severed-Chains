package legend.game.wmap;

public class DirectionalPathSegmentData08 {
  /**
   * Path segment index (actually path index + 1 because 0 can't have a sign).
   * The index can be positive or negative depending on Dart's direction.
   */
  public final int pathSegmentIndexAndDirection_00;

  public final int encounterRate_03;
  public final int battleStage_04;
  /** Index into a table which has the actual encounter ID */
  public final int encounterIndex_05;
  public final int modelIndex_06;

  public DirectionalPathSegmentData08(final int indexAndDirection, final int encounterRate, final int battleStage, final int encounterIndex, final int modelIndex) {
    this.pathSegmentIndexAndDirection_00 = indexAndDirection;
    this.encounterRate_03 = encounterRate;
    this.battleStage_04 = battleStage;
    this.encounterIndex_05 = encounterIndex;
    this.modelIndex_06 = modelIndex;
  }
}
