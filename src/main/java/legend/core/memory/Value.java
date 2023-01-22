package legend.core.memory;

import legend.core.MathHelper;
import legend.core.memory.types.QuadConsumer;
import legend.core.memory.types.QuintConsumer;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Value {
  private final int size;
  private long value;

  public Value(final int byteSize) {
    this.size = byteSize;
  }

  public Value offset(final long offset) {
    throw new UnsupportedOperationException("Can't offset register");
  }

  public Value offset(final Value offset) {
    return this.offset(offset.get());
  }

  public Value offset(final int size, final long offset) {
    throw new UnsupportedOperationException("Can't offset register");
  }

  public Value offset(final int size, final Value offset) {
    return this.offset(size, offset.get());
  }

  public int getSize() {
    return this.size;
  }

  public <T> T cast(final Function<Value, T> constructor) {
    return constructor.apply(this);
  }

  public long getAddress() {
    throw new UnsupportedOperationException("Can't get address of register");
  }

  public Value deref(final int size) {
    throw new UnsupportedOperationException("Can't dereference registers");
  }

  public long get() {
    return MathHelper.unsign(this.value, this.size);
  }

  public long get(final long mask) {
    return this.get() & mask;
  }

  public long get(final Value mask) {
    return this.get(mask.get());
  }

  public long getSigned() {
    return MathHelper.sign(this.value, this.size);
  }

  public long getSigned(final long mask) {
    return this.getSigned() & mask;
  }

  public String getString() {
    if(this.size != 1) {
      throw new RuntimeException("Byte size for string values must be set to 1");
    }

    final StringBuilder sb = new StringBuilder();

    for(int offset = 0; ; offset++) {
      final char ascii = (char)this.offset(1, offset).get();

      if(ascii == 0) {
        break;
      }

      sb.append(ascii);
    }

    return sb.toString();
  }

  public String getString(final int length) {
    if(this.size != 1) {
      throw new RuntimeException("Byte size for string values must be set to 1");
    }

    final StringBuilder sb = new StringBuilder();

    for(int offset = 0; offset < length; offset++) {
      sb.append((char)this.offset(1, offset).get());
    }

    return sb.toString();
  }

  public Object call(final Object... params) {
    throw new RuntimeException("Can't call a register");
  }

  protected void validateUnsigned(final long value) {
    if(value < 0 || value >= 0b1L << this.size * 8) {
      throw new RuntimeException("Can't set " + this.size + "-byte value to " + Long.toString(value, 16));
    }
  }

  protected void validateSigned(final long value) {
    if(value < ~(0b1L << this.size * 8L - 1) + 1 || value >= 0b1L << this.size * 8L - 1) {
      throw new RuntimeException("Can't set " + this.size + "-byte value to " + Long.toString(value, 16));
    }
  }

  public Value set(long value) {
    if((value & 0b1L << this.size * 8 - 1) != 0) {
      value |= -(0b1L << this.size * 8);
    }

    this.validateSigned(value);
    this.value = value;
    return this;
  }

  public Value set(final Value value) {
    return this.set(value.getSigned());
  }

  public Value setu(long value) {
    value &= (0b1L << this.size * 8) - 1;
    this.validateUnsigned(value);
    this.value = value;
    return this;
  }

  public Value setu(final Value value) {
    return this.setu(value.get());
  }

  public Value setu(final long mask, final boolean value) {
    final long previous = this.get();
    final long keptBits = previous & ~mask;
    final long newBits = (value ? 0xffffffffffffffffL : 0) & mask;
    return this.setu(keptBits | newBits);
  }

  public Value set(final String string) {
    if(this.size != 1) {
      throw new RuntimeException("Byte size for string values must be set to 1");
    }

    for(int offset = 0; offset < string.length(); offset++) {
      this.offset(offset).set((byte)string.charAt(offset));
    }

    this.offset(string.length()).set(0x0L);
    return this;
  }

  public Value set(final Runnable function) {
    throw new RuntimeException("Can't store functions in registers");
  }

  public <T> Value set(final Consumer<T> function) {
    throw new RuntimeException("Can't store functions in registers");
  }

  public <T, U> Value set(final BiConsumer<T, U> function) {
    throw new RuntimeException("Can't store functions in registers");
  }

  public <T, U, V, W> Value set(final QuadConsumer<T, U, V, W> function) {
    throw new RuntimeException("Can't store functions in registers");
  }

  public <T, U, V, W, X> Value set(final QuintConsumer<T, U, V, W, X> function) {
    throw new RuntimeException("Can't store functions in registers");
  }

  public Value set(final Function<?, ?> function) {
    throw new RuntimeException("Can't store functions in registers");
  }

  public Value set(final BiFunction<?, ?, ?> function) {
    throw new RuntimeException("Can't store functions in registers");
  }

  public Value set(final TriFunction<?, ?, ?, ?> function) {
    throw new RuntimeException("Can't store functions in registers");
  }

  public Value set(final QuadFunction<?, ?, ?, ?, ?> function) {
    throw new RuntimeException("Can't store functions in registers");
  }

  public <T> Value set(final Supplier<T> function) {
    throw new RuntimeException("Can't store functions in registers");
  }

  public Value add(final long amount) {
    return this.set(this.getSigned() + amount);
  }

  public Value add(final Value amount) {
    return this.add(amount.getSigned());
  }

  public Value addu(final long amount) {
    return this.setu(this.get() + amount);
  }

  public Value addu(final Value amount) {
    return this.addu(amount.get());
  }

  public Value sub(final long amount) {
    return this.add(-amount);
  }

  public Value sub(final Value amount) {
    return this.sub(amount.get());
  }

  public Value subu(final long amount) {
    return this.addu(-amount);
  }

  public Value subu(final Value amount) {
    return this.subu(amount.get());
  }

  public Value mul(final long amount) {
    return this.set(this.getSigned() * amount);
  }

  public Value mul(final Value amount) {
    return this.mul(amount.get());
  }

  public Value mulu(final long amount) {
    return this.setu(this.get() * amount);
  }

  public Value mulu(final Value amount) {
    return this.mulu(amount.get());
  }

  public Value div(final long amount) {
    return this.set(this.getSigned() / amount);
  }

  public Value div(final Value amount) {
    return this.div(amount.get());
  }

  public Value divu(final long amount) {
    return this.setu(this.get() / amount);
  }

  public Value divu(final Value amount) {
    return this.divu(amount.get());
  }

  public Value mod(final long amount) {
    return this.set(this.getSigned() % amount);
  }

  public Value mod(final Value amount) {
    return this.mod(amount.get());
  }

  public Value modu(final long amount) {
    return this.setu(this.get() % amount);
  }

  public Value modu(final Value amount) {
    return this.modu(amount.get());
  }

  public Value and(final long amount) {
    return this.setu(this.get() & amount);
  }

  public Value and(final Value amount) {
    return this.and(amount.get());
  }

  public Value or(final long amount) {
    return this.set(this.get() | amount);
  }

  public Value or(final Value amount) {
    return this.or(amount.get());
  }

  public Value oru(final long amount) {
    return this.setu(this.get() | amount);
  }

  public Value oru(final Value amount) {
    return this.oru(amount.get());
  }


  public Value xor(final long amount) {
    return this.set(this.get() ^ amount);
  }

  public Value xor(final Value amount) {
    return this.xor(amount.get());
  }

  public Value xoru(final long amount) {
    return this.setu(this.get() ^ amount);
  }

  public Value xoru(final Value amount) {
    return this.xoru(amount.get());
  }

  public Value not() {
    return this.set(~this.get());
  }

  public Value notu() {
    return this.setu(~this.get());
  }

  public Value neg() {
    return this.set(-this.getSigned());
  }

  public Value abs() {
    return this.set(Math.abs(this.getSigned()));
  }

  public Value shl(final long bits) {
    return this.setu(this.get() << bits & (0b1L << this.size * 8) - 1);
  }

  public Value shl(final Value bits) {
    return this.shl(bits.get());
  }

  public Value shr(final long bits) {
    return this.setu(this.get() >>> bits);
  }

  public Value shr(final Value bits) {
    return this.shr(bits.get());
  }

  public Value shra(final long bits) {
    return this.setu(this.get() >> bits);
  }

  public Value shra(final Value bits) {
    return this.shra(bits.get());
  }

  @Override
  public String toString() {
    return '[' + Long.toString(this.get(), 16) + "] (" + this.size + "b)";
  }

  @FunctionalInterface
  public interface TriFunction<T, U, V, R>  {
    R apply(T t, U u, V v);
  }

  @FunctionalInterface
  public interface QuadFunction<T, U, V, W, R>  {
    R apply(T t, U u, V v, W w);
  }
}
