package legend.core.audio;

public final class Constants {
  public static final int BASE_SAMPLE_RATE = 44_100;
  public static final int ENGINE_SAMPLE_RATE = 48_000;
  public static final double SAMPLE_RATE_RATIO = BASE_SAMPLE_RATE / (double)ENGINE_SAMPLE_RATE;

  private Constants() {}
}
