package legend.game;

import legend.core.tags.FloatTag;
import legend.core.tags.IntTag;
import legend.core.tags.MapTag;
import legend.core.tags.RegistryIdTag;
import legend.core.tags.StringTag;
import legend.core.tags.Tag;
import legend.game.wmap.world.SubmapEndpoint;
import legend.game.wmap.world.WorldMapTravelTarget;
import legend.lodmod.LodEngineStateTypes;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Objects;

/** A destination and its spawn payload, independent of legacy engine-switch sentinel values. */
public record EngineDestination(RegistryId engineState, Tag data) {
  public EngineDestination {
    Objects.requireNonNull(engineState, "engineState");
    data = Objects.requireNonNull(data, "data").clone();
  }

  @Override public Tag data() { return this.data.clone(); }

  public static EngineDestination submap(final SubmapEndpoint endpoint) {
    final MapTag data = new MapTag();
    data.set("cut", new IntTag(endpoint.cut()));
    data.set("scene", new IntTag(endpoint.scene()));
    return new EngineDestination(LodEngineStateTypes.SUBMAP.getId(), data);
  }

  public static EngineDestination worldMap(final WorldMapTravelTarget target) {
    final MapTag data = new MapTag();
    final MapTag destination = new MapTag();
    switch(target) {
      case WorldMapTravelTarget.Portal portal -> {
        destination.set("kind", new StringTag("portal"));
        destination.set("id", new RegistryIdTag(portal.id()));
      }
      case WorldMapTravelTarget.Node node -> {
        destination.set("kind", new StringTag("node"));
        destination.set("id", new RegistryIdTag(node.id()));
      }
      case WorldMapTravelTarget.Route route -> {
        destination.set("kind", new StringTag("route"));
        destination.set("id", new RegistryIdTag(route.id()));
        destination.set("distance", new FloatTag(route.progress()));
      }
    }
    data.set("worldMapTarget", destination);
    return new EngineDestination(LodEngineStateTypes.WORLD_MAP.getId(), data);
  }

  public static WorldMapTravelTarget worldMapTarget(final Tag tag) {
    final MapTag target = tag.asMap().get("worldMapTarget").asMap();
    final RegistryId id = target.get("id").asRegistryId().get();
    return switch(target.get("kind").asString().get()) {
      case "portal" -> new WorldMapTravelTarget.Portal(id);
      case "node" -> new WorldMapTravelTarget.Node(id);
      case "route" -> WorldMapTravelTarget.atRouteDistance(id, target.get("distance").asFloat().get());
      default -> throw new IllegalArgumentException("Unknown world map travel target kind");
    };
  }
}
