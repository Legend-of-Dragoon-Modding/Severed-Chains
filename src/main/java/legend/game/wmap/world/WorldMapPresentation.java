package legend.game.wmap.world;

import java.util.Objects;

/** Typed presentation settings encoded in a portal's legacy effect flags. */
public record WorldMapPresentation(Atmosphere atmosphere, Smoke smoke, boolean fullBrightness) {
  private static final int ATMOSPHERE_MASK = 0x30;
  private static final int SMOKE_MASK = 0x0c;

  public WorldMapPresentation {
    Objects.requireNonNull(atmosphere, "atmosphere");
    Objects.requireNonNull(smoke, "smoke");
  }

  public static WorldMapPresentation from(final WorldMapPortal portal) {
    Objects.requireNonNull(portal, "portal");
    final int atmosphere = portal.effectFlags() & ATMOSPHERE_MASK;
    final int smoke = portal.effectFlags() & SMOKE_MASK;
    return new WorldMapPresentation(atmosphere(atmosphere), Smoke.fromLegacyMode(smoke), portal.fullBrightness());
  }

  private static Atmosphere atmosphere(final int legacyMode) {
    return switch(legacyMode) {
      case 0 -> Atmosphere.NONE;
      case 0x10 -> Atmosphere.CLOUDS;
      case 0x20 -> Atmosphere.SNOW;
      case 0x30 -> throw new IllegalArgumentException("World map presentation has reserved atmosphere mode 3");
      default -> throw new IllegalArgumentException("World map presentation has invalid atmosphere mode " + legacyMode);
    };
  }

  int atmosphereLegacyMode() {
    return switch(this.atmosphere) {
      case NONE -> 0;
      case CLOUDS -> 0x10;
      case SNOW -> 0x20;
    };
  }

  public enum Atmosphere {
    NONE,
    CLOUDS,
    SNOW,
  }

  public enum Smoke {
    NONE(0),
    MODE_1(4),
    MODE_2(8),
    ;

    private final int legacyMode;

    Smoke(final int legacyMode) {
      this.legacyMode = legacyMode;
    }

    public int legacyMode() {
      return this.legacyMode;
    }

    private static Smoke fromLegacyMode(final int legacyMode) {
      return switch(legacyMode) {
        case 0 -> NONE;
        case 4 -> MODE_1;
        case 8 -> MODE_2;
        case 0x0c -> throw new IllegalArgumentException("World map presentation has reserved smoke mode 3");
        default -> throw new IllegalArgumentException("World map presentation has invalid smoke mode " + legacyMode);
      };
    }
  }
}
