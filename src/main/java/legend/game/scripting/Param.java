package legend.game.scripting;

import org.legendofdragoon.modloader.registries.RegistryId;

public abstract class Param {
  public abstract int get();
  public abstract Param set(final int val);
  public abstract Param array(final int index);

//  public abstract float getFloat();
//  public abstract Param set(final float val);
//  public abstract boolean isFloat();

  public void jump(final RunningScript<?> script) {
    throw new IllegalStateException("Can't jump to non-script param");
  }

  public void jump(final ScriptState<?> script) {
    throw new IllegalStateException("Can't jump to non-script param");
  }

  public Param set(final Param other) {
    return this.set(other.get());
  }

  public RegistryId getRegistryId() {
    throw new IllegalStateException("Registry IDs can't be stored in " + this.getClass().getSimpleName());
  }

  public Param set(final RegistryId id) {
    throw new IllegalStateException("Registry IDs can't be stored in " + this.getClass().getSimpleName());
  }

  public boolean isRegistryId() {
    return false;
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
