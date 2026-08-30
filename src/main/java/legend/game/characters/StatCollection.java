package legend.game.characters;

import legend.core.memory.types.IntRef;
import legend.core.tags.ListTag;
import legend.core.tags.MapTag;
import legend.core.tags.RegistryIdTag;
import legend.core.tags.Tag;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.unpacker.FileData;
import org.jetbrains.annotations.NotNull;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static legend.core.GameEngine.REGISTRIES;

public class StatCollection implements Iterable<RegistryId> {
  private final Map<RegistryId, Stat> stats = new LinkedHashMap<>();

  public StatCollection(final StatType... stats) {
    for(final StatType stat : stats) {
      this.stats.put(stat.getRegistryId(), stat.make(this));
    }
  }

  public void set(final StatCollection other) {
    for(final var entry : other.stats.entrySet()) {
      this.stats.put(entry.getKey(), entry.getValue().copy(this));
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

  public void serialize(final ListTag tags) {
    for(final var entry : this.stats.entrySet()) {
      final RegistryId statTypeId = entry.getKey();
      final StatType statType = REGISTRIES.statTypes.getEntry(statTypeId).get();
      final Stat stat = entry.getValue();

      final MapTag statTag = new MapTag();
      statTag.set("statTypeId", new RegistryIdTag(statTypeId));
      statType.serialize(stat, statTag);
      tags.add(statTag);
    }
  }

  public static StatCollection deserialize(final ListTag tags) {
    final StatCollection stats = new StatCollection();

    for(final Tag tag : tags) {
      final MapTag statTag = tag.asMap();
      final RegistryId statTypeId = statTag.get("statTypeId").asRegistryId().get();
      final StatType statType = REGISTRIES.statTypes.getEntry(statTypeId).get();
      final Stat stat = statType.make(stats);
      statType.deserialize(stat, statTag);
      stats.stats.put(statTypeId, stat);
    }

    return stats;
  }

  @Override
  public @NotNull Iterator<RegistryId> iterator() {
    return this.stats.keySet().iterator();
  }
}
