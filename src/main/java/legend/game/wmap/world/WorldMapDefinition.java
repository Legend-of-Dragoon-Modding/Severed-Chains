package legend.game.wmap.world;

import legend.game.wmap.DirectionalPathSegmentData08;
import legend.game.wmap.Location14;
import legend.game.wmap.Place0c;
import org.joml.Vector3f;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable, dense legacy world map definition. Builder replacement is limited to existing slots
 * because the renderer and save formats use the legacy slot indices as their ABI.
 */
public final class WorldMapDefinition {
  private final List<WorldMapPortal> portals;
  private final List<WorldMapRoute> routes;
  private final List<WorldMapPlace> places;
  private final List<WorldMapNode> nodes;
  private final List<List<WorldMapPoint>> geometry;
  private final List<Integer> portalRouteIndices;
  private final List<Integer> portalPlaceIndices;
  private final int[] pathLengths;
  private final Map<RegistryId, WorldMapPortal> portalsById;
  private final Map<RegistryId, WorldMapRoute> routesById;
  private final Map<RegistryId, WorldMapPlace> placesById;
  private final Map<RegistryId, WorldMapNode> nodesById;

  public WorldMapDefinition(final List<WorldMapPortal> portals, final List<WorldMapRoute> routes, final List<WorldMapPlace> places, final List<WorldMapNode> nodes, final List<List<WorldMapPoint>> geometry) {
    this(portals, routes, places, nodes, geometry, defaultRouteIndices(portals, routes), defaultPlaceIndices(portals, places), defaultPathLengths(geometry));
  }

  WorldMapDefinition(final List<WorldMapPortal> portals, final List<WorldMapRoute> routes, final List<WorldMapPlace> places, final List<WorldMapNode> nodes, final List<List<WorldMapPoint>> geometry, final List<Integer> portalRouteIndices, final List<Integer> portalPlaceIndices, final int[] pathLengths) {
    this.portals = List.copyOf(portals);
    this.routes = List.copyOf(routes);
    this.places = List.copyOf(places);
    this.nodes = List.copyOf(nodes);
    this.geometry = copyGeometry(geometry);
    this.portalRouteIndices = List.copyOf(portalRouteIndices);
    this.portalPlaceIndices = List.copyOf(portalPlaceIndices);
    this.pathLengths = pathLengths.clone();
    this.portalsById = index(this.portals, WorldMapPortal::id, "portal");
    this.routesById = index(this.routes, WorldMapRoute::id, "route");
    this.placesById = index(this.places, WorldMapPlace::id, "place");
    this.nodesById = index(this.nodes, WorldMapNode::id, "node");
    this.validate();
  }

  public List<WorldMapPortal> portals() {
    return this.portals;
  }

  public List<WorldMapRoute> routes() {
    return this.routes;
  }

  public List<WorldMapPlace> places() {
    return this.places;
  }

  public List<WorldMapNode> nodes() {
    return this.nodes;
  }

  public List<List<WorldMapPoint>> geometry() {
    return this.geometry;
  }

  public WorldMapPortal portal(final int legacyIndex) {
    return this.portals.get(legacyIndex);
  }

  public WorldMapPortal portal(final RegistryId id) {
    return get(this.portalsById, id, "portal");
  }

  public WorldMapRoute route(final int legacyIndex) {
    return this.routes.get(legacyIndex);
  }

  public WorldMapRoute route(final RegistryId id) {
    return get(this.routesById, id, "route");
  }

  public WorldMapPlace place(final int legacyIndex) {
    return this.places.get(legacyIndex);
  }

  public WorldMapNode node(final RegistryId id) {
    return get(this.nodesById, id, "node");
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  public Location14[] locationData() {
    final Location14[] data = new Location14[this.portals.size()];
    for(int i = 0; i < data.length; i++) {
      final WorldMapPortal portal = this.portals.get(i);
      data[i] = new Location14(this.portalRouteIndices.get(i), this.portalPlaceIndices.get(i), portal.from().cut(), portal.from().scene(), portal.to().cut(), portal.to().scene(), portal.junctionIndex(), portal.continent(), portal.fullBrightness(), portal.effectFlags());
    }
    return data;
  }

  public Place0c[] placeData() {
    final Place0c[] data = new Place0c[this.places.size()];
    for(int i = 0; i < data.length; i++) {
      final WorldMapPlace place = this.places.get(i);
      final int[] sounds = place.sounds().stream().mapToInt(Integer::intValue).toArray();
      data[i] = new Place0c(place.name(), place.thumbnail(), place.services(), sounds);
    }
    return data;
  }

  public DirectionalPathSegmentData08[] directionalPathData() {
    final DirectionalPathSegmentData08[] data = new DirectionalPathSegmentData08[this.routes.size() + 1];
    for(int i = 0; i < this.routes.size(); i++) {
      final WorldMapRoute route = this.routes.get(i);
      data[i] = new DirectionalPathSegmentData08((route.segmentIndex() + 1) * route.direction(), route.encounterRate(), route.battleStage(), route.encounterIndex(), route.modelIndex());
    }
    data[this.routes.size()] = new DirectionalPathSegmentData08(0, 0, 0, 0, 0);
    return data;
  }

  public Vector3f[][] pathData() {
    final Vector3f[][] data = new Vector3f[this.geometry.size()][];
    for(int i = 0; i < data.length; i++) {
      final List<WorldMapPoint> segment = this.geometry.get(i);
      data[i] = new Vector3f[segment.size()];
      for(int j = 0; j < segment.size(); j++) {
        final WorldMapPoint point = segment.get(j);
        data[i][j] = new Vector3f(point.x(), point.y(), point.z());
      }
    }
    return data;
  }

  public int[] pathLengths() {
    return this.pathLengths.clone();
  }

  private void validate() {
    validateDense(this.portals, WorldMapPortal::legacyIndex, "portal");
    validateDense(this.routes, WorldMapRoute::legacyIndex, "route");
    validateDense(this.places, WorldMapPlace::legacyIndex, "place");
    if(this.portalRouteIndices.size() != this.portals.size() || this.portalPlaceIndices.size() != this.portals.size()) throw new IllegalArgumentException("World map portal reference metadata does not match portal slots");
    if(this.pathLengths.length != this.geometry.size() + 1 || this.pathLengths[this.pathLengths.length - 1] != -1) throw new IllegalArgumentException("World map path lengths must end with a trailing -1 sentinel");

    for(final WorldMapNode node : this.nodes) validatePoint(node.position(), "node " + node.id());
    for(int i = 0; i < this.geometry.size(); i++) {
      final List<WorldMapPoint> segment = this.geometry.get(i);
      if(segment.size() < 2) throw new IllegalArgumentException("World map geometry segment " + i + " must contain at least two points");
      for(final WorldMapPoint point : segment) validatePoint(point, "geometry segment " + i);
      if(this.pathLengths[i] != segment.size()) throw new IllegalArgumentException("World map geometry segment " + i + " does not match its path length " + this.pathLengths[i]);
    }

    for(final WorldMapRoute route : this.routes) {
      if(route.segmentIndex() < 0 || route.segmentIndex() >= this.geometry.size()) throw new IllegalArgumentException("World map route " + route.id() + " references invalid geometry segment " + route.segmentIndex());
      if(route.direction() != -1 && route.direction() != 1) throw new IllegalArgumentException("World map route " + route.id() + " must have direction -1 or 1");
      if(!this.nodesById.containsKey(route.start()) || !this.nodesById.containsKey(route.end())) throw new IllegalArgumentException("World map route " + route.id() + " references an unknown node");
      final List<WorldMapPoint> segment = this.geometry.get(route.segmentIndex());
      final WorldMapPoint expectedStart = route.direction() > 0 ? segment.getFirst() : segment.getLast();
      final WorldMapPoint expectedEnd = route.direction() > 0 ? segment.getLast() : segment.getFirst();
      if(!this.nodesById.get(route.start()).position().equals(expectedStart) || !this.nodesById.get(route.end()).position().equals(expectedEnd)) throw new IllegalArgumentException("World map route " + route.id() + " endpoints do not match its geometry direction");
    }

    for(final WorldMapPortal portal : this.portals) {
      if(portal.route() != null && !this.routesById.containsKey(portal.route())) throw new IllegalArgumentException("World map portal " + portal.id() + " references an unknown route " + portal.route());
      if(portal.place() != null && !this.placesById.containsKey(portal.place())) throw new IllegalArgumentException("World map portal " + portal.id() + " references an unknown place " + portal.place());
      if(portal.route() != null && portal.place() == null) throw new IllegalArgumentException("World map portal " + portal.id() + " has a route but no place");
      if(portal.route() != null && this.routesById.get(portal.route()).legacyIndex() != this.portalRouteIndices.get(portal.legacyIndex())) throw new IllegalArgumentException("World map portal " + portal.id() + " has mismatched route metadata");
      if(portal.place() != null && this.placesById.get(portal.place()).legacyIndex() != this.portalPlaceIndices.get(portal.legacyIndex())) throw new IllegalArgumentException("World map portal " + portal.id() + " has mismatched place metadata");
      WorldMapPresentation.from(portal);
    }
  }

  private static List<List<WorldMapPoint>> copyGeometry(final List<List<WorldMapPoint>> geometry) {
    final List<List<WorldMapPoint>> copy = new ArrayList<>(geometry.size());
    for(final List<WorldMapPoint> segment : geometry) copy.add(List.copyOf(segment));
    return List.copyOf(copy);
  }

  private static List<Integer> defaultRouteIndices(final List<WorldMapPortal> portals, final List<WorldMapRoute> routes) {
    final Map<RegistryId, Integer> indices = new HashMap<>();
    for(final WorldMapRoute route : routes) indices.put(route.id(), route.legacyIndex());
    final List<Integer> result = new ArrayList<>(portals.size());
    for(final WorldMapPortal portal : portals) {
      final RegistryId id = portal.route();
      result.add(id == null ? -1 : indices.getOrDefault(id, -1));
    }
    return result;
  }

  private static List<Integer> defaultPlaceIndices(final List<WorldMapPortal> portals, final List<WorldMapPlace> places) {
    final Map<RegistryId, Integer> indices = new HashMap<>();
    for(final WorldMapPlace place : places) indices.put(place.id(), place.legacyIndex());
    final List<Integer> result = new ArrayList<>(portals.size());
    for(final WorldMapPortal portal : portals) {
      final RegistryId id = portal.place();
      result.add(id == null ? -1 : indices.getOrDefault(id, -1));
    }
    return result;
  }

  private static int[] defaultPathLengths(final List<List<WorldMapPoint>> geometry) {
    final int[] lengths = new int[geometry.size() + 1];
    for(int i = 0; i < geometry.size(); i++) lengths[i] = geometry.get(i).size();
    lengths[geometry.size()] = -1;
    return lengths;
  }

  private static <T> Map<RegistryId, T> index(final List<T> values, final java.util.function.Function<T, RegistryId> id, final String type) {
    final Map<RegistryId, T> result = new HashMap<>();
    for(final T value : values) {
      final RegistryId key = id.apply(value);
      if(result.put(key, value) != null) throw new IllegalArgumentException("Duplicate world map " + type + " id " + key);
    }
    return Map.copyOf(result);
  }

  private static <T> void validateDense(final List<T> values, final java.util.function.ToIntFunction<T> legacyIndex, final String type) {
    for(int i = 0; i < values.size(); i++) {
      if(legacyIndex.applyAsInt(values.get(i)) != i) throw new IllegalArgumentException("World map " + type + " at list index " + i + " has legacy index " + legacyIndex.applyAsInt(values.get(i)));
    }
  }

  private static void validatePoint(final WorldMapPoint point, final String source) {
    if(!Float.isFinite(point.x()) || !Float.isFinite(point.y()) || !Float.isFinite(point.z())) throw new IllegalArgumentException("World map " + source + " has non-finite coordinates");
  }

  private static <T> T get(final Map<RegistryId, T> values, final RegistryId id, final String type) {
    final T value = values.get(Objects.requireNonNull(id, "id"));
    if(value == null) throw new IllegalArgumentException("Unknown world map " + type + " id " + id);
    return value;
  }

  public static final class Builder {
    private final List<WorldMapPortal> portals;
    private final List<WorldMapRoute> routes;
    private final List<WorldMapPlace> places;
    private final List<WorldMapNode> nodes;
    private final List<List<WorldMapPoint>> geometry;
    private final List<Integer> portalRouteIndices;
    private final List<Integer> portalPlaceIndices;

    private Builder(final WorldMapDefinition definition) {
      this.portals = new ArrayList<>(definition.portals);
      this.routes = new ArrayList<>(definition.routes);
      this.places = new ArrayList<>(definition.places);
      this.nodes = new ArrayList<>(definition.nodes);
      this.geometry = new ArrayList<>(definition.geometry);
      this.portalRouteIndices = definition.portalRouteIndices;
      this.portalPlaceIndices = definition.portalPlaceIndices;
    }

    public Builder replacePortal(final WorldMapPortal portal) {
      this.portals.set(checkedIndex(portal.legacyIndex(), this.portals.size(), "portal"), portal);
      return this;
    }

    public Builder replaceRoute(final WorldMapRoute route) {
      this.routes.set(checkedIndex(route.legacyIndex(), this.routes.size(), "route"), route);
      return this;
    }

    public Builder replacePlace(final WorldMapPlace place) {
      this.places.set(checkedIndex(place.legacyIndex(), this.places.size(), "place"), place);
      return this;
    }

    public Builder node(final WorldMapNode node) {
      Objects.requireNonNull(node, "node");
      for(int i = 0; i < this.nodes.size(); i++) {
        if(this.nodes.get(i).id().equals(node.id())) {
          this.nodes.set(i, node);
          return this;
        }
      }
      throw new IllegalArgumentException("World map node replacement id " + node.id() + " is outside existing slots");
    }

    public Builder geometry(final int segmentIndex, final List<WorldMapPoint> geometry) {
      this.geometry.set(checkedIndex(segmentIndex, this.geometry.size(), "geometry segment"), List.copyOf(geometry));
      return this;
    }

    public WorldMapDefinition build() {
      return new WorldMapDefinition(this.portals, this.routes, this.places, this.nodes, this.geometry, this.replacementRouteIndices(), this.replacementPlaceIndices(), defaultPathLengths(this.geometry));
    }

    private List<Integer> replacementRouteIndices() {
      final List<Integer> indices = defaultRouteIndices(this.portals, this.routes);
      for(int i = 0; i < indices.size(); i++) {
        if(this.portals.get(i).route() == null && this.portalRouteIndices.get(i) < 0) indices.set(i, this.portalRouteIndices.get(i));
      }
      return indices;
    }

    private List<Integer> replacementPlaceIndices() {
      final List<Integer> indices = defaultPlaceIndices(this.portals, this.places);
      for(int i = 0; i < indices.size(); i++) {
        if(this.portals.get(i).place() == null && (this.portalPlaceIndices.get(i) < 0 || this.portalPlaceIndices.get(i) >= this.places.size())) indices.set(i, this.portalPlaceIndices.get(i));
      }
      return indices;
    }

    private static int checkedIndex(final int index, final int size, final String type) {
      if(index < 0 || index >= size) throw new IllegalArgumentException("World map " + type + " replacement index " + index + " is outside existing slots");
      return index;
    }
  }
}
