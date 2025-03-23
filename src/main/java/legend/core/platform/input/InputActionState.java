package legend.core.platform.input;

public class InputActionState {
  private static final long REPEAT_DELAY = 500_000_000L;
  private static final long REPEAT_INTERVAL = 100_000_000L;

  private State state = State.RELEASED;
  private float axis;
  private long timestamp;

  public void press() {
    this.state = State.JUST_PRESSED;
    this.axis = 0.0f;
    this.timestamp = System.nanoTime();
  }

  public void release() {
    this.axis = 0.0f;
    this.state = State.RELEASED;
  }

  public void axis(final float axis) {
    this.axis = axis;
  }

  public boolean repeat() {
    // Repeat is called after the event loop so this stops it from immediately going to HELD
    if(this.state == State.JUST_PRESSED) {
      this.state = State.PRESSED;
      return false;
    }

    final long time = System.nanoTime();

    if(this.state == State.PRESSED) {
      this.state = State.DELAY;
      this.timestamp = time;
      return false;
    }

    if(this.state == State.DELAY && time - this.timestamp >= REPEAT_DELAY) {
      this.state = State.REPEAT;
      this.timestamp = time;
      return true;
    }

    if(this.state == State.REPEAT && time - this.timestamp >= REPEAT_INTERVAL / 2) {
      this.state = State.HELD;
      this.timestamp = time;
      return false;
    }

    if(this.state == State.HELD && time - this.timestamp >= REPEAT_INTERVAL / 2) {
      this.state = State.REPEAT;
      this.timestamp = time;
      return true;
    }

    return false;
  }

  public boolean isPressed() {
    return this.state == State.JUST_PRESSED || this.state == State.PRESSED;
  }

  public boolean isRepeat() {
    return this.isPressed() || this.state == State.REPEAT;
  }

  public boolean isHeld() {
    return this.state != State.RELEASED;
  }

  public float getAxis() {
    return this.axis;
  }

  public enum State {
    RELEASED,
    JUST_PRESSED,
    PRESSED,
    DELAY,
    REPEAT,
    HELD,
  }
}
