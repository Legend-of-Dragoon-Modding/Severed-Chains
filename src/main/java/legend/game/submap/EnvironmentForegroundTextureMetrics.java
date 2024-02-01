package legend.game.submap;

public class EnvironmentForegroundTextureMetrics {
//  public int x_00;
//  public int y_04;
  public int startX;
  public int startY;
  public int destX;
  public int destY;
  public int ticksTotal;
  public int ticks;
  public boolean positionWasSet;
  public boolean hidden_08;

  public void clear() {
    this.startX = 0;
    this.startY = 0;
    this.destX = 0;
    this.destY = 0;
    this.positionWasSet = false;
    this.hidden_08 = false;
  }
}
