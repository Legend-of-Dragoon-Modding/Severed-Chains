package legend.game.wmap;

public class Location14 {
  public int directionalPathIndex_00;
  public int placeIndex_02;
  public int submapCutFrom_04;
  public int submapSceneFrom_06;
  public int submapCutTo_08;
  public int submapSceneTo_0a;
  public int unknownIndex_0c;
  /** 1-based, because why not */
  public Continent continent_0e;
  /** ubyte */
  public boolean thumbnailShouldUseFullBrightness_10;
  /**
   * <ul>
   *   <li>0x0 = atmospheric effect 0 (no-op)</li>
   *   <li>0x4 = smoke mode 1</li>
   *   <li>0x8 = smoke mode 2</li>
   *   <li>0x10 = atmospheric effect 1 (clouds)</li>
   *   <li>0x20 = atmospheric effect 2 (snow)</li>
   * </ul>
   */
  public int effectFlags_12;

  public Location14(final int directionalPathIndex, final int placeIndex, final int submapCut0, final int submapScene0, final int submapCut1, final int submapScene1, final int unknownIndex, final Continent continent, final boolean thumbnailShouldUseFullBrightness, final int effectFlags) {
    this.directionalPathIndex_00 = directionalPathIndex;
    this.placeIndex_02 = placeIndex;
    this.submapCutFrom_04 = submapCut0;
    this.submapSceneFrom_06 = submapScene0;
    this.submapCutTo_08 = submapCut1;
    this.submapSceneTo_0a = submapScene1;
    this.unknownIndex_0c = unknownIndex;
    this.continent_0e = continent;
    this.thumbnailShouldUseFullBrightness_10 = thumbnailShouldUseFullBrightness;
    this.effectFlags_12 = effectFlags;
  }
}
