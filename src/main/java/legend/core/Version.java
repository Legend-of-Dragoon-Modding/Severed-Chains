package legend.core;

public final class Version {
  private Version() { }

  public static final String MAJOR = "3";
  public static final String MINOR = "0";
  public static final String REVISION = "0";
  public static final String BUILD = "SNAPSHOT";
  public static final String HASH = "COMMIT";
  public static final String VERSION = MAJOR + '.' + MINOR + '.' + REVISION + '-' + BUILD;
}
