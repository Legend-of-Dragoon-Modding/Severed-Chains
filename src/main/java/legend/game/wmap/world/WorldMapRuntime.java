package legend.game.wmap.world;

import legend.game.types.Flags;
import legend.game.wmap.Continent;
import org.joml.Vector3f;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;

/** Per-engine-state resolver. Legacy numeric values are confined to the renderer/save adapter. */
public final class WorldMapRuntime {
  private final WorldMapDefinition definition;
  private final WorldMapRules rules;
  private final WorldMapTraversal traversal;
  private WorldMapView view;
  private long version;
  private boolean invalidated = true;

  public WorldMapRuntime(final WorldMapDefinition definition, final WorldMapRules rules) {
    rules.validate(definition);
    this.definition = definition;
    this.rules = rules;
    this.traversal = new WorldMapTraversal(definition);
  }

  public WorldMapDefinition definition() {
    return this.definition;
  }

  public WorldMapTraversal traversal() {
    return this.traversal;
  }

  public WorldMapTravel.Departure departure(final SubmapEndpoint origin) {
    return this.rules.departure(origin, this.view().progression());
  }

  public WorldMapTravel.Arrival arrival(final SubmapEndpoint origin) {
    return this.rules.arrival(origin, this.view().progression(), this.definition);
  }

  public WorldMapView view() {
    if(this.view == null) {
      throw new IllegalStateException("World-map progression has not been resolved");
    }
    return this.view;
  }

  public boolean needsRefresh(final Flags story, final Flags locations) {
    return this.invalidated || this.view == null || !this.view.progression().matches(story, locations);
  }

  public void invalidate() {
    this.invalidated = true;
  }

  public void resolve(final WorldMapProgression progression) {
    this.view = new WorldMapView(++this.version, this.definition, this.rules, progression);
    this.invalidated = false;
  }

  public WorldMapAccess access(final RegistryId portalId, final WorldMapAction action, final Continent continent) {
    final WorldMapPortal portal = this.definition.portal(portalId);
    if(portal.route() == null) {
      return WorldMapAccess.NO_PATH;
    }
    if(portal.continent() != continent) {
      return WorldMapAccess.WRONG_CONTINENT;
    }
    return this.view().access(portal.legacyIndex(), action);
  }

  public WorldMapAccess access(final RegistryId portalId, final WorldMapAction action, final RegistryId region) {
    final WorldMapPortal portal = this.definition.portal(portalId);
    if(portal.route() == null) {
      return WorldMapAccess.NO_PATH;
    }
    if(!WorldMapRegion.idFor(portal).equals(region)) {
      return WorldMapAccess.WRONG_CONTINENT;
    }
    return this.view().access(portal.legacyIndex(), action);
  }

  /** A stale command must be submitted against the newly resolved view. */
  public WorldMapAccess validateIntent(final RegistryId portal, final WorldMapAction action, final Continent continent, final long expectedVersion) {
    if(this.view().version() != expectedVersion) {
      return WorldMapAccess.denied("World-map state changed; resolve the action again");
    }
    return this.access(portal, action, continent);
  }

  public WorldMapAccess validateIntent(final RegistryId portal, final WorldMapAction action, final RegistryId region, final long expectedVersion) {
    if(this.view().version() != expectedVersion) {
      return WorldMapAccess.denied("World-map state changed; resolve the action again");
    }
    return this.access(portal, action, region);
  }

  public int legacyValidity(final int index, final int mode, final RegistryId region, @Nullable final Vector3f position, final WorldMapAction action) {
    final WorldMapPortal portal = this.definition.portal(index);
    if(mode != -1 && portal.route() != null && !WorldMapRegion.idFor(portal).equals(region)) {
      return -2;
    }
    return this.legacyValidity(index, mode, portal.continent(), position, action);
  }

  public int legacyValidity(final int index, final int mode, final Continent continent, @Nullable final Vector3f position, final WorldMapAction action) {
    final WorldMapPortal portal = this.definition.portal(index);
    if(portal.route() == null) {
      return -1;
    }
    if(mode != -1 && portal.continent() != continent) {
      return -2;
    }
    if(!this.view().access(index, action).allowed()) {
      return 1;
    }
    if(mode == 0 || mode == -1) {
      return 0;
    }

    final WorldMapRoute route = this.definition.route(portal.route());
    final var points = this.definition.geometry().get(route.segmentIndex());
    final WorldMapPoint point = points.get(route.direction() > 0 ? 0 : points.size() - 1);
    if(position == null) {
      throw new IllegalArgumentException("A position is required in world-map positional query mode");
    }
    position.set(point.x(), point.y(), point.z());
    return 0;
  }

  /** Preserve first matching From pair, before availability validation. */
  public int findArrival(final SubmapEndpoint from) {
    for(final WorldMapPortal portal : this.definition.portals()) {
      if(portal.from().equals(from)) {
        return portal.legacyIndex();
      }
    }
    return -1;
  }

  /** Arrival precedes path restoration; Queen Fury's slot 93 intentionally has no route. */
  public boolean arrivalAllowed(final int index) {
    return this.rules.evaluate(this.definition.portal(index), WorldMapAction.TRAVERSE, this.view().progression()).allowed();
  }

  /** Preserve first valid table row for a directed route, including overlapping variants. */
  public int findRoutePortal(final int directionalPath, final Continent continent) {
    for(final WorldMapPortal portal : this.definition.portals()) {
      if(portal.route() != null && this.definition.route(portal.route()).legacyIndex() == directionalPath && this.access(portal.id(), WorldMapAction.TRAVERSE, continent).allowed()) {
        return portal.legacyIndex();
      }
    }
    return -1;
  }

  public int findRoutePortal(final int directionalPath, final RegistryId region) {
    for(final WorldMapPortal portal : this.definition.portals()) {
      if(portal.route() != null && this.definition.route(portal.route()).legacyIndex() == directionalPath && this.access(portal.id(), WorldMapAction.TRAVERSE, region).allowed()) {
        return portal.legacyIndex();
      }
    }
    return -1;
  }
}
