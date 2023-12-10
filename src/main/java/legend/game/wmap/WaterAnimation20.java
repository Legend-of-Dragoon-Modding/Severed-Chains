package legend.game.wmap;

import legend.game.unpacker.FileData;

public class WaterAnimation20 {
  public int x_00;
  public int y_02;
  public int w_04;
  public int h_06;
  public FileData imageData_08;
  public FileData imageData_0c;
  // Flag to select between left/right and up/down motion, only ever set to 3 (bit 0 set means up/down)
  // public int _10;
  // Unused
  // public int _14;
  // Direction and speed of water, high values means more rows/columns of pixels copied per tick
  // public short waterVelocity_18;
  // Hardcoded the value
  // public short maxTick_1a;
  public float currTick_1c;
}
