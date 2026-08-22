package legend.core;

import java.time.ZonedDateTime;
import java.util.Locale;

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
  public static final ZonedDateTime TIMESTAMP = null;

  public static boolean isMac() {
    final String os = System.getProperty("os.name").toLowerCase(Locale.US);
    return os.contains("mac os x") || os.contains("darwin") || os.contains("osx");
  }
}
