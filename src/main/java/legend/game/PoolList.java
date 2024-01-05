package legend.game;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PoolList<T extends PoolList.Usable> {
  private final List<T> pool = new ArrayList<>();
  private final Supplier<T> constructor;

  public PoolList(final Supplier<T> constructor) {
    this.constructor = constructor;
  }

  public T get() {
    for(int i = 0; i < this.pool.size(); i++) {
      final T inst = this.pool.get(i);
      if(!inst.used()) {
        inst.use();
        return inst;
      }
    }

    final T inst = this.constructor.get();
    this.pool.add(inst);
    return inst;
  }

  public T get(final int index) {
    return this.pool.get(index);
  }

  public void clear() {
    this.pool.clear();
  }

  public boolean isEmpty() {
    return this.pool.isEmpty();
  }

  public int size() {
    return this.pool.size();
  }

  public interface Usable {
    boolean used();
    void use();
    void free();
  }
}
