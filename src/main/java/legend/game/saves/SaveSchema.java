package legend.game.saves;

import legend.core.tags.Tag;
import org.legendofdragoon.modloader.registries.RegistryEntry;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;

/** Registered save compatibility contracts; independent of rendering and live engine states. */
public final class SaveSchema extends RegistryEntry {
  /** Mods may define domains for their own registries; names never alias across domains. */
  public record Domain(RegistryId id) {
    public static final Domain ITEM = nativeDomain("items");
    public static final Domain EQUIPMENT = nativeDomain("equipment");
    public static final Domain GOOD = nativeDomain("goods");
    public static final Domain CHARACTER = nativeDomain("characters");
    public static final Domain STAT = nativeDomain("stats");
    public static final Domain STAT_MOD = nativeDomain("stat_modifiers");
    public static final Domain ADDITION = nativeDomain("additions");
    public static final Domain SPELL = nativeDomain("spells");
    public static final Domain CAMPAIGN = nativeDomain("campaigns");
    public static final Domain ENGINE_STATE = nativeDomain("engine_states");
    public static final Domain MOD_DATA = nativeDomain("mod_data");

    public Domain {
      Objects.requireNonNull(id, "id");
    }

    private static Domain nativeDomain(final String name) {
      return new Domain(new RegistryId("lod_core", name));
    }
  }
  public enum MissingField { PRESERVE, REMOVE }
  public record FieldPolicy(Domain domain, @Nullable RegistryId content, String field, MissingField policy) {
    public FieldPolicy {
      Objects.requireNonNull(domain, "domain");
      Objects.requireNonNull(field, "field");
      Objects.requireNonNull(policy, "policy");
    }
  }
  public record Payload(int version, Map<Integer, UnaryOperator<Tag>> steps) {
    public Payload {
      if(version < 0) throw new IllegalArgumentException("Payload version must be nonnegative");
      steps = Map.copyOf(steps);
      for(final int from : steps.keySet()) {
        if(from < 0 || from >= version) throw new IllegalArgumentException("Migration step must advance an older version by one");
      }
    }
  }

  final Map<Domain, Map<RegistryId, RegistryId>> aliases;
  final Map<RegistryId, Payload> payloads;
  final List<FieldPolicy> policies;

  private SaveSchema(final Builder builder) {
    final Map<Domain, Map<RegistryId, RegistryId>> aliases = new LinkedHashMap<>();
    builder.aliases.forEach((domain, values) -> aliases.put(domain, Map.copyOf(values)));
    this.aliases = Map.copyOf(aliases);
    this.payloads = Map.copyOf(builder.payloads);
    this.policies = List.copyOf(builder.policies);
  }

  public static final class Builder {
    private final Map<Domain, Map<RegistryId, RegistryId>> aliases = new LinkedHashMap<>();
    private final Map<RegistryId, Payload> payloads = new LinkedHashMap<>();
    private final List<FieldPolicy> policies = new ArrayList<>();

    public Builder alias(final Domain domain, final RegistryId oldId, final RegistryId currentId) {
      Objects.requireNonNull(oldId, "oldId");
      Objects.requireNonNull(currentId, "currentId");
      final RegistryId previous = this.aliases.computeIfAbsent(Objects.requireNonNull(domain, "domain"), ignored -> new LinkedHashMap<>()).putIfAbsent(oldId, currentId);
      if(previous != null && !previous.equals(currentId)) throw new IllegalArgumentException("Conflicting save alias " + domain + ':' + oldId);
      return this;
    }

    /** Absent version tags mean zero; each step maps its key to key+1. Missing steps retain data opaque. */
    public Builder payload(final RegistryId id, final int currentVersion, final Map<Integer, UnaryOperator<Tag>> steps) {
      if(this.payloads.putIfAbsent(Objects.requireNonNull(id, "id"), new Payload(currentVersion, steps)) != null) throw new IllegalArgumentException("Duplicate payload schema " + id);
      return this;
    }

    /** Null content applies to every record in the domain; specific content rules override domain defaults. */
    public Builder missingField(final Domain domain, @Nullable final RegistryId content, final String field, final MissingField policy) {
      this.policies.add(new FieldPolicy(domain, content, field, policy));
      return this;
    }

    public SaveSchema build() {
      return new SaveSchema(this);
    }
  }
}
