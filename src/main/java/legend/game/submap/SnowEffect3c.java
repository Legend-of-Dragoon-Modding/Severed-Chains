package legend.game.submap;

public class SnowEffect3c {
  public int tick;

  // /** Whether particle should render, code was written so that it always rendered */
  // public short _00;

  public float angle_08;
  public float angleStep_0c;
  public float translationScaleX_10;
  public int size_14;
  public float x_16;
  public float y_18;

  public float stepX_1c;
  public float stepY_20;
  /** Not retail; randomized gaussian component of snow Y movement */
  public float randY;
  /** 16.16 */
  public float xAccumulator_24;
  // /** 16.16 - not needed now because y and yAccum are always in lockstep */
  // public float yAccumulator_28;

  public short brightness_34;

  public SnowEffect3c next_38;
}
