package legend.game.wmap.world;

import legend.core.tags.BoolTag;
import legend.core.tags.ListTag;
import legend.core.tags.MapTag;
import legend.core.tags.RegistryIdTag;
import legend.core.tags.Tag;
import legend.game.types.Flags;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

/** Stable portal identities with a live numeric flag adapter for retail scripts. */
public final class WorldMapPortalState {
  private final Map<RegistryId, State> states = new LinkedHashMap<>();
  private final Map<RegistryId, Integer> slots = new LinkedHashMap<>();
  private boolean identified;

  public boolean hasIdentities() {
    return this.identified;
  }

  public void set(final WorldMapPortalState other) {
    this.states.clear();
    this.states.putAll(other.states);
    this.slots.clear();
    this.slots.putAll(other.slots);
    this.identified = other.identified;
  }

  public void bind(final WorldMapDefinition definition, final Flags enabled, final Flags visited) {
    this.bind(definition, enabled, visited, !WorldMapLegacyAdapter.isNativeLayout(definition));
  }

  public void bind(final WorldMapDefinition definition, final Flags enabled, final Flags visited, final boolean standalone) {
    this.capture(enabled, visited);
    final int words = (definition.portals().size() + 31) / 32;
    enabled.ensureCapacity(words);
    visited.ensureCapacity(words);

    // Keep native identities too: visiting a standalone world must not overwrite retail progress.
    // Existing numeric script writes are captured above and remain authoritative while bound.
    for(final WorldMapPortal portal : definition.portals()) {
      this.states.putIfAbsent(portal.id(), new State(standalone || enabled.get(portal.legacyIndex()), !standalone && visited.get(portal.legacyIndex())));
    }
    this.slots.clear();
    for(int word = 0; word < enabled.count(); word++) enabled.setRaw(word, 0);
    for(int word = 0; word < visited.count(); word++) visited.setRaw(word, 0);
    for(final WorldMapPortal portal : definition.portals()) {
      this.slots.put(portal.id(), portal.legacyIndex());
      final State state = this.states.get(portal.id());
      enabled.set(portal.legacyIndex(), state.enabled());
      visited.set(portal.legacyIndex(), state.visited());
    }
    this.identified = true;
  }

  private void capture(final Flags enabled, final Flags visited) {
    this.slots.forEach((id, slot) -> this.states.put(id, new State(enabled.get(slot), visited.get(slot))));
  }

  public Tag write(final Flags enabled, final Flags visited) {
    this.capture(enabled, visited);
    final ListTag tag = new ListTag();
    this.states.forEach((id, state) -> {
      final MapTag entry = new MapTag();
      entry.set("portal", new RegistryIdTag(id));
      entry.set("enabled", new BoolTag(state.enabled()));
      entry.set("visited", new BoolTag(state.visited()));
      tag.add(entry);
    });
    return tag;
  }

  public void read(@Nullable final Tag tag) {
    this.states.clear();
    this.slots.clear();
    this.identified = tag != null;
    if(tag != null) {
      for(final Tag value : tag.asList()) {
        final MapTag entry = value.asMap();
        final RegistryId id = entry.get("portal").asRegistryId().get();
        final State state = new State(entry.get("enabled").asBool().get(), entry.get("visited").asBool().get());
        if(this.states.putIfAbsent(id, state) != null) {
          throw new IllegalArgumentException("Duplicate saved world map portal " + id);
        }
      }
    }
  }

  private record State(boolean enabled, boolean visited) { }
}
