package legend.game.wmap;

public enum Continent {
  SOUTH_SERDIO_0(0),
  NORTH_SERDIO_1(1),
  TIBEROA_2(2),
  ILLISA_BAY_3(3),
  MILLE_SESEAU_4(4),
  GLORIANO_5(5),
  DEATH_FRONTIER_6(6),
  ENDINESS_7(7),
  NONE_8(8),
  ;

  public final int continentNum;

  Continent(final int continentNum) {
    this.continentNum = continentNum;
  }
}
