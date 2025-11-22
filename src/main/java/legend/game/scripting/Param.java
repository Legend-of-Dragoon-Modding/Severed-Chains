package legend.game.scripting;

import org.legendofdragoon.modloader.registries.RegistryId;

public abstract class Param {
  public abstract int get();
  public abstract Param set(final int val);
  public abstract Param array(final int index);

  public float getFloat() {
    return this.get();
  }

  public Param set(final float val) {
    return this.set(Math.round(val));
  }

  public boolean isFloat() {
    return false;
  }

  public void jump(final RunningScript<?> script) {
    throw new IllegalStateException("Can't jump to non-script param");
  }

  public void jump(final ScriptState<?> script) {
    throw new IllegalStateException("Can't jump to non-script param");
  }

  public Param set(final Param other) {
    if(other.isFloat()) {
      return this.set(other.getFloat());
    }

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
    if(this.isFloat()) {
      return this.set(this.getFloat() + amount);
    }

    return this.set(this.get() + amount);
  }

  public Param add(final float amount) {
    return this.set(this.getFloat() + amount);
  }

  public Param add(final Param amount) {
    if(this.isFloat() || amount.isFloat()) {
      return this.add(amount.getFloat());
    }

    return this.add(amount.get());
  }

  public Param sub(final int amount) {
    if(this.isFloat()) {
      return this.set(this.getFloat() - amount);
    }

    return this.set(this.get() - amount);
  }

  public Param sub(final float amount) {
    return this.set(this.getFloat() - amount);
  }

  public Param sub(final Param amount) {
    if(this.isFloat() || amount.isFloat()) {
      return this.sub(amount.getFloat());
    }

    return this.sub(amount.get());
  }

  public Param mul(final int amount) {
    if(this.isFloat()) {
      return this.set(this.getFloat() * amount);
    }

    return this.set(this.get() * amount);
  }

  public Param mul(final float amount) {
    return this.set(this.getFloat() * amount);
  }

  public Param mul(final Param amount) {
    if(this.isFloat() || amount.isFloat()) {
      return this.mul(amount.getFloat());
    }

    return this.mul(amount.get());
  }

  public Param div(final int amount) {
    if(this.isFloat()) {
      return this.set(this.getFloat() / amount);
    }

    return this.set(this.get() / amount);
  }

  public Param div(final float amount) {
    return this.set(this.getFloat() / amount);
  }

  public Param div(final Param amount) {
    if(this.isFloat() || amount.isFloat()) {
      return this.div(amount.getFloat());
    }

    return this.div(amount.get());
  }

  public Param mod(final int amount) {
    if(this.isFloat()) {
      return this.set(this.getFloat() % amount);
    }

    return this.set(this.get() % amount);
  }

  public Param mod(final float amount) {
    return this.set(this.getFloat() % amount);
  }

  public Param mod(final Param amount) {
    if(this.isFloat() || amount.isFloat()) {
      return this.mod(amount.getFloat());
    }

    return this.mod(amount.get());
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
    if(this.isFloat()) {
      return this.set(-this.getFloat());
    }

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
    if(this.isFloat()) {
      return this.set(Math.abs(this.getFloat()));
    }

    return this.set(Math.abs(this.get()));
  }
}
