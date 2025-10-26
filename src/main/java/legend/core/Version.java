package legend.core;

import java.time.ZonedDateTime;

public final class Version {
  private Version() { }

  public static final String MAJOR = "3";
  public static final String MINOR = "0";
  public static final String REVISION = "0";
  public static final String BUILD = "SNAPSHOT";
  public static final String HASH = "COMMIT";
  public static final String CHANNEL = "CHANNEL";
  public static final String VERSION = MAJOR + '.' + MINOR + '.' + REVISION;
  public static final String FULL_VERSION = VERSION + '-' + BUILD + '-' + CHANNEL;
  public static final ZonedDateTime TIMESTAMP = ZonedDateTime.now();
}
