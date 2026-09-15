package legend.game.wmap.world;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/** Ordered legacy availability is the default; attributed rules can replace or compose access decisions. */
public final class WorldMapRules {
  private static final Logger LOGGER = LogManager.getFormatterLogger(WorldMapRules.class);
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
    return resolveDecision(this.departures, rule -> rule.apply(origin, progression), WorldMapTravel.Departure.NONE, "departure from " + origin);
  }

  public WorldMapTravel.Arrival arrival(final SubmapEndpoint origin, final WorldMapProgression progression, final WorldMapDefinition definition) {
    return resolveDecision(this.arrivals, rule -> rule.evaluate(origin, progression, definition), WorldMapTravel.Arrival.NORMAL, "arrival from " + origin);
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

  private static <T, D> D resolveDecision(final List<Registration<T>> rules, final Function<T, D> evaluate, final D fallback, final String context) {
    int end = rules.size();
    while(end > 0) {
      final int priority = rules.get(end - 1).priority;
      int start = end - 1;
      while(start > 0 && rules.get(start - 1).priority == priority) start--;
      boolean legacy = false;
      for(int i = start; i < end; i++) legacy |= rules.get(i).legacy;
      D selected = null;
      D compatibilityWinner = null;
      RegistryId selectedSource = null;
      for(int i = end - 1; i >= start; i--) {
        final Registration<T> registration = rules.get(i);
        if(registration.legacy && compatibilityWinner != null) continue;
        final D candidate = evaluate.apply(registration.rule);
        if(candidate == null) continue;
        if(compatibilityWinner == null) compatibilityWinner = candidate;
        // Legacy decisions retain ordering, but cannot suppress conflicts between scoped decisions.
        if(registration.legacy) continue;
        if(selected != null && !selected.equals(candidate)) {
          throw new IllegalStateException("Conflicting world-map " + context + " decisions at priority " + priority + " from " + selectedSource + " (" + selected + ") and " + registration.source + " (" + candidate + ")");
        }
        selected = candidate;
        selectedSource = registration.source;
      }
      if(legacy && compatibilityWinner != null) return compatibilityWinner;
      if(selected != null) return selected;
      end = start;
    }
    return fallback;
  }

  private record Registration<T>(T rule, WorldMapRuleComposition composition, RegistryId source, int priority, long order, boolean legacy) {
    private WorldMapRuleAttribution attribution() {
      return new WorldMapRuleAttribution(this.source, this.priority, this.composition, this.legacy);
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
    private final Set<String> legacyDiagnostics = new HashSet<>();

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
      this.register(this.policies, Objects.requireNonNull(policy, "policy"), "policy", false);
      return this;
    }

    public Builder departure(final BiFunction<SubmapEndpoint, WorldMapProgression, WorldMapTravel.Departure> rule) {
      Objects.requireNonNull(rule, "rule");
      this.register(this.departures, (origin, progression) -> Objects.requireNonNull(rule.apply(origin, progression), "WMAP departure rule returned null"), "departure", true);
      return this;
    }

    public Builder arrival(final BiFunction<SubmapEndpoint, WorldMapProgression, WorldMapTravel.Arrival> rule) {
      Objects.requireNonNull(rule, "rule");
      return this.arrival((origin, progression, definition) -> rule.apply(origin, progression));
    }

    public Builder arrival(final WorldMapArrivalRule rule) {
      Objects.requireNonNull(rule, "rule");
      this.register(this.arrivals, (origin, progression, definition) -> Objects.requireNonNull(rule.evaluate(origin, progression, definition), "WMAP arrival rule returned null"), "arrival", true);
      return this;
    }

    /** Highest applicable priority wins; null abstains, allowing lower priority decisions. */
    public Builder departureDecision(final RegistryId source, final int priority, final BiFunction<SubmapEndpoint, WorldMapProgression, WorldMapTravel.Departure> rule) {
      try(final SourceScope ignored = this.source(source, priority)) {
        this.register(this.departures, Objects.requireNonNull(rule, "rule"), "departure", true);
      }
      return this;
    }

    /** Equal-priority applicable decisions must agree; null explicitly abstains. */
    public Builder arrivalDecision(final RegistryId source, final int priority, final WorldMapArrivalRule rule) {
      try(final SourceScope ignored = this.source(source, priority)) {
        this.register(this.arrivals, Objects.requireNonNull(rule, "rule"), "arrival", true);
      }
      return this;
    }

    public WorldMapRules build() {
      return new WorldMapRules(this);
    }

    private <K, T> void register(final Map<K, List<Registration<T>>> registrations, final K key, final WorldMapRuleComposition composition, final T rule) {
      final List<Registration<T>> rules = registrations.computeIfAbsent(key, ignored -> new ArrayList<>());
      this.checkConflict(rules, composition, key.toString(), false);
      rules.add(this.registration(rule, composition));
    }

    private <T> void register(final List<Registration<T>> registrations, final T rule, final String context, final boolean decision) {
      this.checkConflict(registrations, WorldMapRuleComposition.REPLACE, context, decision);
      registrations.add(this.registration(rule, WorldMapRuleComposition.REPLACE));
    }

    private <T> Registration<T> registration(final T rule, final WorldMapRuleComposition composition) {
      return new Registration<>(rule, composition, this.source == null ? WorldMapRuleAttribution.LEGACY_SOURCE : this.source, this.source == null ? Integer.MAX_VALUE : this.priority, this.order++, this.source == null);
    }

    private void checkConflict(final List<? extends Registration<?>> registrations, final WorldMapRuleComposition composition, final String context, final boolean decision) {
      if(composition != WorldMapRuleComposition.REPLACE) return;
      for(final Registration<?> registration : registrations) {
        if(registration.composition != WorldMapRuleComposition.REPLACE) continue;
        if(this.source == null || registration.legacy) {
          if(this.legacyDiagnostics.add(context)) {
            LOGGER.warn("World-map %s overrides include compatibility source %s; legacy last-call ordering remains active alongside source %s. Migrate registrations to attributed rules", context, WorldMapRuleAttribution.LEGACY_SOURCE, this.source == null ? registration.source : this.source);
          }
          continue;
        }
        if(!decision && registration.priority == this.priority && !registration.source.equals(this.source)) {
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
