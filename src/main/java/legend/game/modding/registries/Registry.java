package legend.game.modding.registries;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import legend.game.inventory.RegistryHolder;

import java.util.HashMap;
import java.util.Map;

public class Registry<Type extends RegistryEntry> {
  protected final Map<RegistryId, Type> entries = new HashMap<>();
  protected final Int2ObjectMap<RegistryId> idToName = new Int2ObjectOpenHashMap<>();
  protected final Object2IntMap<RegistryId> nameToId = new Object2IntOpenHashMap<>();

  public Type getEntry(final RegistryId id) {
    return this.entries.get(id);
  }

  public Type getEntryById(final int id) {
    return this.getEntry(this.idToName.get(id));
  }

  public int getEntryId(final Type entry) {
    return this.nameToId.getInt(entry.id);
  }

  public RegistryHolder<Type> getHolder(final RegistryId id) {
    return new RegistryHolder<>(this, id);
  }
}
