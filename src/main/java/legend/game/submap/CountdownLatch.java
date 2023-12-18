package legend.game.submap;

public class CountdownLatch {
  private int counter_00;

  public boolean isOpen() {
    return this.counter_00 != 0;
  }

  public void latch(final int ticks) {
    this.counter_00 = ticks;
  }

  public void tick() {
    this.counter_00--;
  }
}
