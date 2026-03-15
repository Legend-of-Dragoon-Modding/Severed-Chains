package legend.game.characters;

import legend.core.memory.types.IntRef;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.HashMap;
import java.util.Map;

import static legend.core.GameEngine.REGISTRIES;

public class StatCollection {
  private final Map<RegistryId, Stat> stats = new HashMap<>();

  public StatCollection(final StatType... stats) {
    for(final StatType stat : stats) {
      this.stats.put(stat.getRegistryId(), stat.make(this));
    }
  }

  public void set(final StatCollection other) {
    for(final var entry : other.stats.entrySet()) {
      this.stats.put(entry.getKey(), entry.getValue().copy());
    }
  }

  public <T extends Stat> T getStat(final StatType<T> type) {
    //noinspection unchecked
    return (T)this.getStat(type.getRegistryId());
  }

  public <T extends Stat> T getStat(final RegistryId id) {
    //noinspection unchecked
    return (T)this.stats.get(id);
  }

  public void turnFinished(final BattleEntity27c bent) {
    for(final Stat stat : this.stats.values()) {
      stat.turnFinished(bent);
    }
  }

  public void serialize(final FileData data, final IntRef offset) {
    data.writeInt(offset, this.stats.size());

    for(final var entry : this.stats.entrySet()) {
      final RegistryId statTypeId = entry.getKey();
      final StatType statType = REGISTRIES.statTypes.getEntry(statTypeId).get();
      final Stat stat = entry.getValue();
      data.writeRegistryId(offset, statTypeId);
      statType.serialize(stat, data, offset);
    }
  }

  public static StatCollection deserialize(final FileData data, final IntRef offset) {
    final StatCollection stats = new StatCollection();

    final int count = data.readInt(offset);

    for(int i = 0; i < count; i++) {
      final RegistryId statTypeId = data.readRegistryId(offset);
      final StatType statType = REGISTRIES.statTypes.getEntry(statTypeId).get();
      final Stat stat = statType.make(stats);
      statType.deserialize(stat, data, offset);
      stats.stats.put(statTypeId, stat);
    }

    return stats;
  }
}
