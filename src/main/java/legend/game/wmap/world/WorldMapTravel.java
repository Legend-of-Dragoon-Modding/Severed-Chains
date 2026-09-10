package legend.game.wmap.world;

import java.util.Objects;
import java.util.function.IntPredicate;

/** Pure decisions for world map transport capabilities, departures, and destinations. */
public final class WorldMapTravel {
  public enum Capability {
    QUEEN_FURY_BOARDING,
    COOLON,
  }

  public enum Departure {
    NONE,
    FIRST_QUEEN_FURY,
    LATER_QUEEN_FURY,
  }

  public enum Kind {
    LOCATION,
    REGION,
    QUEEN_FURY,
    COOLON,
    TELEPORT,
    FORCED_QUEEN_FURY,
  }

  private WorldMapTravel() { }

  public static boolean hasCapability(final Capability capability, final IntPredicate storyFlag) {
    Objects.requireNonNull(storyFlag, "storyFlag");
    return storyFlag.test(switch(Objects.requireNonNull(capability, "capability")) {
      case QUEEN_FURY_BOARDING -> 0x97;
      case COOLON -> 0x15a;
    });
  }

  /** The later departure takes precedence when both Queen Fury story flags are set. */
  public static Departure departure(final SubmapEndpoint origin, final IntPredicate storyFlag) {
    Objects.requireNonNull(origin, "origin");
    Objects.requireNonNull(storyFlag, "storyFlag");
    if(origin.cut() != 242 || origin.scene() != 3) {
      return Departure.NONE;
    }

    if(storyFlag.test(0x90)) {
      return Departure.LATER_QUEEN_FURY;
    }

    return storyFlag.test(0x8f) ? Departure.FIRST_QUEEN_FURY : Departure.NONE;
  }

  public static boolean isTeleportOrigin(final SubmapEndpoint origin) {
    Objects.requireNonNull(origin, "origin");
    return origin.cut() == 528 && (origin.scene() == 13 || origin.scene() == 14 || origin.scene() == 15)
      || origin.cut() == 540 && origin.scene() == 19
      || origin.cut() == 572 && origin.scene() == 23;
  }

  public static boolean isCoolonOrigin(final SubmapEndpoint origin) {
    Objects.requireNonNull(origin, "origin");
    return origin.cut() == 529 && origin.scene() == 41;
  }

  public static SubmapEndpoint destination(final WorldMapPortal portal, final boolean worldMapArrival) {
    Objects.requireNonNull(portal, "portal");
    return worldMapArrival ? portal.from() : portal.to();
  }
}
