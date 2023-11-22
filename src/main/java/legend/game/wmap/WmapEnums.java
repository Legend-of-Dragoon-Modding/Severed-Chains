package legend.game.wmap;

public class WmapEnums {
  public enum Continent {
    SOUTH_SERDIO,
    NORTH_SERDIO,
    TIBEROA,
    ILLISA_BAY,
    MILLE_SESEAU,
    GLORIANO,
    DEATH_FRONTIER,
    ENDINESS
  }

  public enum WmapActiveState {
    ACTIVE,
    TRANSITION_IN,
    TRANSITION_OUT
  }

  public enum PathSegmentEntering {
    CURRENT,
    PREVIOUS,
    NEXT
  }
}
