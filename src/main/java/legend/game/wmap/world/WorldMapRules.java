package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/** Ordered legacy availability is the default; attributed rules can replace or compose access decisions. */
public final class WorldMapRules {
  private final Map<RegistryId, List<Registration<WorldMapRule>>> portals;
  private final Map<WorldMapTravel.Capability, List<Registration<Predicate<WorldMapProgression>>>> capabilities;
  private final List<Registration<WorldMapPolicy>> policies;
  private final List<Registration<BiFunction<SubmapEndpoint, WorldMapProgression, WorldMapTravel.Departure>>> departures;
  private final List<Registration<WorldMapArrivalRule>> arrivals;

  private WorldMapRules(final Builder builder) {
    this.portals = copy(builder.portals);
    this.capabilities = copy(builder.capabilities);
    this.policies = ordered(builder.policies);
    this.departures = ordered(builder.departures);
    this.arrivals = ordered(builder.arrivals);
  }

  public WorldMapAccess evaluate(final WorldMapPortal portal, final WorldMapAction action, final WorldMapProgression progression) {
    WorldMapAccess access = progression.locationEnabled(portal.legacyIndex()) ? WorldMapAccess.ALLOWED : WorldMapAccess.STORY_LOCKED;
    for(final Registration<WorldMapRule> registration : this.portals.getOrDefault(portal.id(), List.of())) {
      if(registration.composition == WorldMapRuleComposition.VETO) continue;
      final WorldMapAccess candidate = Objects.requireNonNull(registration.rule.evaluate(portal, action, progression), "World-map rule returned null for " + portal.id());
      access = apply(access, candidate, registration.composition);
    }
    if(!this.policies.isEmpty()) access = this.policies.getLast().rule.apply(access);
    for(final Registration<WorldMapRule> registration : this.portals.getOrDefault(portal.id(), List.of())) {
      if(registration.composition != WorldMapRuleComposition.VETO) continue;
      final WorldMapAccess candidate = Objects.requireNonNull(registration.rule.evaluate(portal, action, progression), "World-map rule returned null for " + portal.id());
      if(!candidate.allowed()) access = candidate;
    }
    return access;
  }

  public boolean hasCapability(final WorldMapTravel.Capability capability, final WorldMapProgression progression) {
    boolean allowed = WorldMapTravel.hasCapability(capability, progression::storyFlag);
    for(final Registration<Predicate<WorldMapProgression>> registration : this.capabilities.getOrDefault(capability, List.of())) {
      if(registration.composition == WorldMapRuleComposition.VETO) continue;
      allowed = apply(allowed, registration.rule.test(progression), registration.composition);
    }
    for(final Registration<Predicate<WorldMapProgression>> registration : this.capabilities.getOrDefault(capability, List.of())) {
      if(registration.composition == WorldMapRuleComposition.VETO && !registration.rule.test(progression)) return false;
    }
    return allowed;
  }

  public void validate(final WorldMapDefinition definition) {
    for(final RegistryId id : this.portals.keySet()) {
      if(definition.portals().stream().noneMatch(portal -> portal.id().equals(id))) {
        throw new IllegalArgumentException("Unknown world-map portal rule target: " + id);
      }
    }
  }

  public WorldMapTravel.Departure departure(final SubmapEndpoint origin, final WorldMapProgression progression) {
    return this.departures.isEmpty() ? WorldMapTravel.Departure.NONE : this.departures.getLast().rule.apply(origin, progression);
  }

  public WorldMapTravel.Arrival arrival(final SubmapEndpoint origin, final WorldMapProgression progression, final WorldMapDefinition definition) {
    return this.arrivals.isEmpty() ? WorldMapTravel.Arrival.NORMAL : Objects.requireNonNull(this.arrivals.getLast().rule.evaluate(origin, progression, definition), "WMAP arrival rule returned null");
  }

  /** Immutable diagnostic attribution in the order each target is evaluated. */
  public Map<RegistryId, List<WorldMapRuleAttribution>> portalAttributions() {
    return attributions(this.portals);
  }

  /** Immutable diagnostic attribution in the order each target is evaluated. */
  public Map<WorldMapTravel.Capability, List<WorldMapRuleAttribution>> capabilityAttributions() {
    return attributions(this.capabilities);
  }

  public List<WorldMapRuleAttribution> policyAttributions() {
    return attributions(this.policies);
  }

  public List<WorldMapRuleAttribution> departureAttributions() {
    return attributions(this.departures);
  }

  public List<WorldMapRuleAttribution> arrivalAttributions() {
    return attributions(this.arrivals);
  }

  private static WorldMapAccess apply(final WorldMapAccess current, final WorldMapAccess candidate, final WorldMapRuleComposition composition) {
    return switch(composition) {
      case REPLACE -> candidate;
      case REQUIRE_ALL -> current.allowed() ? candidate : current;
      case ALLOW_ANY -> current.allowed() ? current : candidate.allowed() ? candidate : current;
      case VETO -> candidate.allowed() ? current : candidate;
    };
  }

  private static boolean apply(final boolean current, final boolean candidate, final WorldMapRuleComposition composition) {
    return switch(composition) {
      case REPLACE -> candidate;
      case REQUIRE_ALL, VETO -> current && candidate;
      case ALLOW_ANY -> current || candidate;
    };
  }

  private static <K, T> Map<K, List<Registration<T>>> copy(final Map<K, List<Registration<T>>> values) {
    final Map<K, List<Registration<T>>> result = new LinkedHashMap<>();
    values.forEach((key, rules) -> result.put(key, ordered(rules)));
    return Map.copyOf(result);
  }

  private static <T> List<Registration<T>> ordered(final List<Registration<T>> rules) {
    return rules.stream().sorted(Comparator.comparingInt((Registration<T> rule) -> rule.priority).thenComparingLong(rule -> rule.order)).toList();
  }

  private static <K, T> Map<K, List<WorldMapRuleAttribution>> attributions(final Map<K, List<Registration<T>>> values) {
    final Map<K, List<WorldMapRuleAttribution>> result = new LinkedHashMap<>();
    values.forEach((key, rules) -> result.put(key, attributions(rules)));
    return Map.copyOf(result);
  }

  private static <T> List<WorldMapRuleAttribution> attributions(final List<Registration<T>> rules) {
    return rules.stream().map(Registration::attribution).toList();
  }

  private record Registration<T>(T rule, WorldMapRuleComposition composition, RegistryId source, int priority, long order) {
    private WorldMapRuleAttribution attribution() {
      return new WorldMapRuleAttribution(this.source, this.priority, this.composition);
    }
  }

  public static final class Builder {
    private final Map<RegistryId, List<Registration<WorldMapRule>>> portals = new LinkedHashMap<>();
    private final Map<WorldMapTravel.Capability, List<Registration<Predicate<WorldMapProgression>>>> capabilities = new EnumMap<>(WorldMapTravel.Capability.class);
    private final List<Registration<WorldMapPolicy>> policies = new ArrayList<>();
    private final List<Registration<BiFunction<SubmapEndpoint, WorldMapProgression, WorldMapTravel.Departure>>> departures = new ArrayList<>();
    private final List<Registration<WorldMapArrivalRule>> arrivals = new ArrayList<>();
    private RegistryId source;
    private int priority;
    private long order;

    /** Attributes registrations made until this scope closes. Scopes may be nested. */
    public SourceScope source(final RegistryId source, final int priority) {
      final RegistryId previousSource = this.source;
      final int previousPriority = this.priority;
      this.source = Objects.requireNonNull(source, "source");
      this.priority = priority;
      return () -> {
        this.source = previousSource;
        this.priority = previousPriority;
      };
    }

    /** Legacy replacement API. Calls outside a source scope run after attributed rules and retain last-call-wins behavior. */
    public Builder portal(final RegistryId id, final WorldMapRule rule) {
      return this.portal(id, WorldMapRuleComposition.REPLACE, rule);
    }

    public Builder portal(final RegistryId id, final WorldMapRuleComposition composition, final WorldMapRule rule) {
      this.register(this.portals, Objects.requireNonNull(id, "id"), Objects.requireNonNull(composition, "composition"), Objects.requireNonNull(rule, "rule"));
      return this;
    }

    public Builder portal(final RegistryId id, final RegistryId source, final int priority, final WorldMapRuleComposition composition, final WorldMapRule rule) {
      try(final SourceScope ignored = this.source(source, priority)) {
        return this.portal(id, composition, rule);
      }
    }

    /** Legacy replacement API. Calls outside a source scope run after attributed rules and retain last-call-wins behavior. */
    public Builder capability(final WorldMapTravel.Capability capability, final Predicate<WorldMapProgression> rule) {
      return this.capability(capability, WorldMapRuleComposition.REPLACE, rule);
    }

    public Builder capability(final WorldMapTravel.Capability capability, final WorldMapRuleComposition composition, final Predicate<WorldMapProgression> rule) {
      this.register(this.capabilities, Objects.requireNonNull(capability, "capability"), Objects.requireNonNull(composition, "composition"), Objects.requireNonNull(rule, "rule"));
      return this;
    }

    public Builder capability(final WorldMapTravel.Capability capability, final RegistryId source, final int priority, final WorldMapRuleComposition composition, final Predicate<WorldMapProgression> rule) {
      try(final SourceScope ignored = this.source(source, priority)) {
        return this.capability(capability, composition, rule);
      }
    }

    public Builder policy(final WorldMapPolicy policy) {
      this.register(this.policies, Objects.requireNonNull(policy, "policy"));
      return this;
    }

    public Builder departure(final BiFunction<SubmapEndpoint, WorldMapProgression, WorldMapTravel.Departure> rule) {
      this.register(this.departures, Objects.requireNonNull(rule, "rule"));
      return this;
    }

    public Builder arrival(final BiFunction<SubmapEndpoint, WorldMapProgression, WorldMapTravel.Arrival> rule) {
      Objects.requireNonNull(rule, "rule");
      return this.arrival((origin, progression, definition) -> rule.apply(origin, progression));
    }

    public Builder arrival(final WorldMapArrivalRule rule) {
      this.register(this.arrivals, Objects.requireNonNull(rule, "rule"));
      return this;
    }

    public WorldMapRules build() {
      return new WorldMapRules(this);
    }

    private <K, T> void register(final Map<K, List<Registration<T>>> registrations, final K key, final WorldMapRuleComposition composition, final T rule) {
      final List<Registration<T>> rules = registrations.computeIfAbsent(key, ignored -> new ArrayList<>());
      this.checkConflict(rules, composition);
      rules.add(new Registration<>(rule, composition, this.source, this.source == null ? Integer.MAX_VALUE : this.priority, this.order++));
    }

    private <T> void register(final List<Registration<T>> registrations, final T rule) {
      this.checkConflict(registrations, WorldMapRuleComposition.REPLACE);
      registrations.add(new Registration<>(rule, WorldMapRuleComposition.REPLACE, this.source, this.source == null ? Integer.MAX_VALUE : this.priority, this.order++));
    }

    private void checkConflict(final List<? extends Registration<?>> registrations, final WorldMapRuleComposition composition) {
      if(this.source == null || composition != WorldMapRuleComposition.REPLACE) return;
      for(final Registration<?> registration : registrations) {
        if(registration.composition == WorldMapRuleComposition.REPLACE && registration.source != null && registration.priority == this.priority && !registration.source.equals(this.source)) {
          throw new IllegalArgumentException("Conflicting world-map rule replacements at priority " + this.priority + " from " + registration.source + " and " + this.source);
        }
      }
    }
  }

  @FunctionalInterface
  public interface SourceScope extends AutoCloseable {
    @Override
    void close();
  }
}
