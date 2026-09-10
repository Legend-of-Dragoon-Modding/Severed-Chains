package legend.game.wmap.world;

import java.util.Objects;

/** An explainable decision. Structural validation is never bypassed by an access policy. */
public record WorldMapAccess(Code code, String reason) {
  public enum Code {
    ALLOWED,
    STORY_LOCKED,
    RULE_LOCKED,
    NO_PATH,
    WRONG_CONTINENT,
  }

  public static final WorldMapAccess ALLOWED = new WorldMapAccess(Code.ALLOWED, "");
  public static final WorldMapAccess STORY_LOCKED = new WorldMapAccess(Code.STORY_LOCKED, "Location is unavailable in the current story");
  public static final WorldMapAccess NO_PATH = new WorldMapAccess(Code.NO_PATH, "Location has no route");
  public static final WorldMapAccess WRONG_CONTINENT = new WorldMapAccess(Code.WRONG_CONTINENT, "Location is on another continent");

  public WorldMapAccess {
    Objects.requireNonNull(code, "code");
    Objects.requireNonNull(reason, "reason");
  }

  public boolean allowed() {
    return this.code == Code.ALLOWED;
  }

  public static WorldMapAccess denied(final String reason) {
    return new WorldMapAccess(Code.RULE_LOCKED, reason);
  }
}
