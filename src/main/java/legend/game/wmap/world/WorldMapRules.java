package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/** Ordered legacy availability is the default; mods override rules by stable portal ID. */
public final class WorldMapRules {
  private final Map<RegistryId, WorldMapRule> portals;
  private final Map<WorldMapTravel.Capability, Predicate<WorldMapProgression>> capabilities;
  private final WorldMapPolicy policy;
  private final BiFunction<SubmapEndpoint, WorldMapProgression, WorldMapTravel.Departure> departure;
  private final WorldMapArrivalRule arrival;

  private WorldMapRules(final Builder builder) {
    this.portals = Map.copyOf(builder.portals);
    this.capabilities = Map.copyOf(builder.capabilities);
    this.policy = builder.policy;
    this.departure = builder.departure;
    this.arrival = builder.arrival;
  }

  public WorldMapAccess evaluate(final WorldMapPortal portal, final WorldMapAction action, final WorldMapProgression progression) {
    final WorldMapRule rule = this.portals.get(portal.id());
    final WorldMapAccess access = rule == null
      ? progression.locationEnabled(portal.legacyIndex()) ? WorldMapAccess.ALLOWED : WorldMapAccess.STORY_LOCKED
      : Objects.requireNonNull(rule.evaluate(portal, action, progression), "World-map rule returned null for " + portal.id());
    return this.policy.apply(access);
  }

  public boolean hasCapability(final WorldMapTravel.Capability capability, final WorldMapProgression progression) {
    final Predicate<WorldMapProgression> rule = this.capabilities.get(capability);
    return rule == null ? WorldMapTravel.hasCapability(capability, progression::storyFlag) : rule.test(progression);
  }

  public void validate(final WorldMapDefinition definition) {
    for(final RegistryId id : this.portals.keySet()) {
      if(definition.portals().stream().noneMatch(portal -> portal.id().equals(id))) {
        throw new IllegalArgumentException("Unknown world-map rule portal " + id);
      }
    }
  }

  public WorldMapTravel.Departure departure(final SubmapEndpoint origin, final WorldMapProgression progression) {
    return Objects.requireNonNull(this.departure.apply(origin, progression), "WMAP departure rule returned null");
  }

  public WorldMapTravel.Arrival arrival(final SubmapEndpoint origin, final WorldMapProgression progression, final WorldMapDefinition definition) {
    return Objects.requireNonNull(this.arrival.evaluate(origin, progression, definition), "WMAP arrival rule returned null");
  }

  public static final class Builder {
    private BiFunction<SubmapEndpoint, WorldMapProgression, WorldMapTravel.Departure> departure = (origin, progression) -> WorldMapTravel.Departure.NONE;
    private WorldMapArrivalRule arrival = (origin, progression, definition) -> WorldMapTravel.Arrival.NORMAL;

    public Builder departure(final BiFunction<SubmapEndpoint, WorldMapProgression, WorldMapTravel.Departure> rule) {
      this.departure = Objects.requireNonNull(rule, "rule");
      return this;
    }

    public Builder arrival(final BiFunction<SubmapEndpoint, WorldMapProgression, WorldMapTravel.Arrival> rule) {
      Objects.requireNonNull(rule, "rule");
      return this.arrival((origin, progression, definition) -> rule.apply(origin, progression));
    }

    public Builder arrival(final WorldMapArrivalRule rule) {
      this.arrival = Objects.requireNonNull(rule, "rule");
      return this;
    }
    private final Map<RegistryId, WorldMapRule> portals = new LinkedHashMap<>();
    private final Map<WorldMapTravel.Capability, Predicate<WorldMapProgression>> capabilities = new EnumMap<>(WorldMapTravel.Capability.class);
    private WorldMapPolicy policy = WorldMapPolicy.STORY;

    public Builder portal(final RegistryId id, final WorldMapRule rule) {
      this.portals.put(Objects.requireNonNull(id, "id"), Objects.requireNonNull(rule, "rule"));
      return this;
    }

    public Builder capability(final WorldMapTravel.Capability capability, final Predicate<WorldMapProgression> rule) {
      this.capabilities.put(Objects.requireNonNull(capability, "capability"), Objects.requireNonNull(rule, "rule"));
      return this;
    }

    public Builder policy(final WorldMapPolicy policy) {
      this.policy = Objects.requireNonNull(policy, "policy");
      return this;
    }

    public WorldMapRules build() {
      return new WorldMapRules(this);
    }
  }
}
