package legend.core;

public record Tuple<T, U>(T a, U b) {
  @Override
  public boolean equals(final Object obj) {
    if(!(obj instanceof Tuple)) {
      return false;
    }

    return this.a.equals(((Tuple<?, ?>)obj).a) && this.b.equals(((Tuple<?, ?>)obj).b);
  }
}
