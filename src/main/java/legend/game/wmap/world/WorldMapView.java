package legend.game.wmap.world;

import java.util.EnumMap;
import java.util.Map;

/** Immutable availability used by both rendering and traversal; version guards stale intents. */
public final class WorldMapView {
  private final long version;
  private final WorldMapProgression progression;
  private final Map<WorldMapAction, WorldMapAccess[]> access = new EnumMap<>(WorldMapAction.class);
  private final Map<WorldMapTravel.Capability, Boolean> capabilities = new EnumMap<>(WorldMapTravel.Capability.class);

  WorldMapView(final long version, final WorldMapDefinition definition, final WorldMapRules rules, final WorldMapProgression progression) {
    this.version = version;
    this.progression = progression;

    for(final WorldMapAction action : WorldMapAction.values()) {
      final WorldMapAccess[] values = new WorldMapAccess[definition.portals().size()];
      for(final WorldMapPortal portal : definition.portals()) {
        values[portal.legacyIndex()] = portal.route() == null ? WorldMapAccess.NO_PATH : rules.evaluate(portal, action, progression);
      }
      this.access.put(action, values);
    }

    for(final WorldMapTravel.Capability capability : WorldMapTravel.Capability.values()) {
      this.capabilities.put(capability, rules.hasCapability(capability, progression));
    }
  }

  public long version() {
    return this.version;
  }

  public WorldMapProgression progression() {
    return this.progression;
  }

  public WorldMapAccess access(final int portal, final WorldMapAction action) {
    return this.access.get(action)[portal];
  }

  public boolean hasCapability(final WorldMapTravel.Capability capability) {
    return this.capabilities.get(capability);
  }
}
