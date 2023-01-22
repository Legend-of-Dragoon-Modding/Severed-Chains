package legend.game.scripting;

public abstract class Param {
  public abstract int get();
  public abstract Param set(final int val);
  public abstract Param array(final int index);

  public void jump(final RunningScript<?> script) {
    throw new IllegalStateException("Can't jump to non-script param");
  }

  public void jump(final ScriptState<?> script) {
    throw new IllegalStateException("Can't jump to non-script param");
  }

  public Param add(final int amount) {
    return this.set(this.get() + amount);
  }

  public Param sub(final int amount) {
    return this.set(this.get() - amount);
  }

  public Param mul(final int amount) {
    return this.set(this.get() * amount);
  }

  public Param div(final int amount) {
    return this.set(this.get() / amount);
  }

  public Param mod(final int amount) {
    return this.set(this.get() % amount);
  }

  public Param incr() {
    return this.add(1);
  }

  public Param decr() {
    return this.sub(1);
  }

  public Param not() {
    return this.set(~this.get());
  }

  public Param neg() {
    return this.set(-this.get());
  }

  public Param and(final int val) {
    return this.set(this.get() & val);
  }

  public Param or(final int val) {
    return this.set(this.get() | val);
  }

  public Param xor(final int val) {
    return this.set(this.get() ^ val);
  }

  public Param shl(final int bits) {
    return this.set(this.get() << bits);
  }

  public Param shr(final int bits) {
    return this.set(this.get() >>> bits);
  }

  public Param shra(final int bits) {
    return this.set(this.get() >> bits);
  }

  public Param abs() {
    return this.set(Math.abs(this.get()));
  }
}
