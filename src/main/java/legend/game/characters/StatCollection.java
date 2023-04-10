package legend.game.characters;

import java.util.HashMap;
import java.util.Map;

public class StatCollection {
  private final Map<StatType, Stat> stats = new HashMap<>();

  public StatCollection(final StatType... stats) {
    for(final StatType stat : stats) {
      this.stats.put(stat, new Stat(this, stat));
    }
  }

  public Stat getStat(final StatType type) {
    return this.stats.get(type);
  }
}
