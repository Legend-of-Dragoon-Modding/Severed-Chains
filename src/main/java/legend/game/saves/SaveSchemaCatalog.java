package legend.game.saves;

import legend.core.tags.IntTag;
import legend.core.tags.ListTag;
import legend.core.tags.MapTag;
import legend.core.tags.RegistryIdTag;
import legend.core.tags.Tag;
import org.legendofdragoon.modloader.registries.RegistryId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static legend.core.GameEngine.REGISTRIES;

/** Immutable, validated registry snapshot used for one save operation. */
public final class SaveSchemaCatalog {
  private static final Logger LOGGER = LogManager.getFormatterLogger(SaveSchemaCatalog.class);
  private static final List<SaveSchema.FieldPolicy> ENGINE_POLICIES = List.of(
    new SaveSchema.FieldPolicy(SaveSchema.Domain.ITEM, null, "extraData", SaveSchema.MissingField.REMOVE),
    new SaveSchema.FieldPolicy(SaveSchema.Domain.CHARACTER, null, "selectedAdditionId", SaveSchema.MissingField.REMOVE)
  );
  private final Map<SaveSchema.Domain, Map<RegistryId, RegistryId>> aliases = new LinkedHashMap<>();
  private final Map<RegistryId, SaveSchema.Payload> payloads = new LinkedHashMap<>();
  private final List<SaveSchema.FieldPolicy> policies = new ArrayList<>();

  public SaveSchemaCatalog(final List<SaveSchema> schemas) {
    final Map<String, SaveSchema.MissingField> policyConflicts = new LinkedHashMap<>();
    for(final SaveSchema schema : schemas) {
      schema.aliases.forEach((domain, entries) -> entries.forEach((oldId, id) -> {
        final RegistryId previous = this.aliases.computeIfAbsent(domain, ignored -> new LinkedHashMap<>()).putIfAbsent(oldId, id);
        if(previous != null && !previous.equals(id)) throw new IllegalArgumentException("Conflicting save alias " + domain + ':' + oldId);
      }));
    }
    this.aliases.forEach((domain, entries) -> entries.keySet().forEach(id -> this.canonical(domain, id)));
    for(final SaveSchema schema : schemas) {
      schema.payloads.forEach((id, payload) -> {
        final RegistryId canonical = this.canonical(SaveSchema.Domain.MOD_DATA, id);
        if(this.payloads.putIfAbsent(canonical, payload) != null) throw new IllegalArgumentException("Conflicting payload schemas " + canonical);
      });
      for(final SaveSchema.FieldPolicy policy : schema.policies) {
        final RegistryId content = policy.content() == null ? null : this.canonical(policy.domain(), policy.content());
        final String key = policy.domain() + ":" + content + ':' + policy.field();
        final SaveSchema.MissingField previous = policyConflicts.putIfAbsent(key, policy.policy());
        if(previous != null && previous != policy.policy()) throw new IllegalArgumentException("Conflicting save field policies " + key);
        this.policies.add(new SaveSchema.FieldPolicy(policy.domain(), content, policy.field(), policy.policy()));
      }
    }
  }

  public static SaveSchemaCatalog current() {
    final List<SaveSchema> schemas = new ArrayList<>();
    for(final RegistryId id : REGISTRIES.saveSchemas) schemas.add(REGISTRIES.saveSchemas.getEntry(id).get());
    return new SaveSchemaCatalog(schemas);
  }

  public RegistryId canonical(final SaveSchema.Domain domain, final RegistryId id) {
    RegistryId current = Objects.requireNonNull(id, "id");
    final Map<RegistryId, RegistryId> aliases = this.aliases.getOrDefault(domain, Map.of());
    final Set<RegistryId> visited = new HashSet<>();
    while(aliases.containsKey(current)) {
      if(!visited.add(current) || visited.size() > 256) throw new IllegalArgumentException("Cyclic or excessive save aliases in " + domain + " at " + current);
      current = aliases.get(current);
    }
    return current;
  }

  public boolean preserveAbsent(final SaveSchema.Domain domain, final RegistryId content, final String field) {
    SaveSchema.MissingField selected = SaveSchema.MissingField.PRESERVE;
    for(final SaveSchema.FieldPolicy policy : ENGINE_POLICIES) {
      if(policy.domain().equals(domain) && policy.field().equals(field)) selected = policy.policy();
    }
    boolean specific = false;
    for(final SaveSchema.FieldPolicy policy : this.policies) {
      if(!policy.domain().equals(domain) || !policy.field().equals(field)) continue;
      if(policy.content() != null && !policy.content().equals(this.canonical(domain, content))) continue;
      if(policy.content() != null || !specific) selected = policy.policy();
      specific |= policy.content() != null;
    }
    return selected == SaveSchema.MissingField.PRESERVE;
  }

  /** Only documented engine-owned fields are canonicalized; opaque payload lookalikes are untouched. */
  public MapTag read(final MapTag original) {
    final MapTag result = original.clone();
    this.canonicalizeKnownFields(result);
    if(result.has("modData")) {
      final ListTag migrated = new ListTag();
      for(final Tag entry : result.get("modData").asList()) migrated.add(this.migratePayload(entry.asMap()));
      result.set("modData", migrated);
    }
    return result;
  }

  public void canonicalizeKnownFields(final MapTag tag) {
    this.alias(tag, "campaignTypeId", SaveSchema.Domain.CAMPAIGN);
    this.alias(tag, "engineStateId", SaveSchema.Domain.ENGINE_STATE);
    this.aliasList(tag, "equipment", "equipmentId", SaveSchema.Domain.EQUIPMENT);
    this.aliasList(tag, "items", "itemId", SaveSchema.Domain.ITEM);
    this.aliasList(tag, "goods", "goodId", SaveSchema.Domain.GOOD);
    if(tag.has("characters")) {
      for(final Tag value : tag.get("characters").asList()) {
        final MapTag character = value.asMap();
        this.alias(character, "templateId", SaveSchema.Domain.CHARACTER);
        this.alias(character, "selectedAdditionId", SaveSchema.Domain.ADDITION);
        this.aliasList(character, "equipment", "equipmentId", SaveSchema.Domain.EQUIPMENT);
        this.aliasList(character, "additions", "additionId", SaveSchema.Domain.ADDITION);
        this.aliasList(character, "spells", "spellId", SaveSchema.Domain.SPELL);
        this.aliasList(character, "stats", "statTypeId", SaveSchema.Domain.STAT);
        if(character.has("stats")) {
          for(final Tag stat : character.get("stats").asList()) this.aliasList(stat.asMap(), "mods", "typeId", SaveSchema.Domain.STAT_MOD);
        }
      }
    }
    this.aliasList(tag, "modData", "id", SaveSchema.Domain.MOD_DATA);
  }

  private void aliasList(final MapTag map, final String list, final String field, final SaveSchema.Domain domain) {
    if(map.has(list)) for(final Tag entry : map.get(list).asList()) this.alias(entry.asMap(), field, domain);
  }

  private void alias(final MapTag entry, final String field, final SaveSchema.Domain domain) {
    if(entry.has(field)) entry.set(field, new RegistryIdTag(this.canonical(domain, entry.get(field).asRegistryId().get())));
  }

  /** A failed, missing, or newer migration returns its entire original entry, never partial work. */
  public MapTag migratePayload(final MapTag original) {
    final MapTag result = original.clone();
    final RegistryId id = this.canonical(SaveSchema.Domain.MOD_DATA, original.get("id").asRegistryId().get());
    final SaveSchema.Payload schema = this.payloads.get(id);
    if(schema == null) {
      LOGGER.debug("Save payload %s has no schema provider; retaining original data", id);
      return result;
    }
    final Tag migrated;
    int version = -1;
    try {
      version = version(original);
      if(version < 0 || version > schema.version() || (long)schema.version() - version > 256) {
        LOGGER.warn("Save payload %s version %d cannot migrate to %d within 256 steps; retaining original data", id, version, schema.version());
        return result;
      }
      Tag working = original.get("data").clone();
      for(int from = version; from < schema.version(); from++) {
        final var step = schema.steps().get(from);
        if(step == null) {
          LOGGER.warn("Save payload %s migration from version %d to %d is missing step %d; retaining original data", id, version, schema.version(), from);
          return result;
        }
        working = Objects.requireNonNull(step.apply(working.clone()), "Migration returned null").clone();
      }
      migrated = working;
    } catch(final RuntimeException failure) {
      LOGGER.warn("Save payload %s migration from version %d to %d failed; retaining original data", id, version, schema.version(), failure);
      return result;
    }
    result.set("id", new RegistryIdTag(id));
    result.set("version", new IntTag(schema.version()));
    result.set("data", migrated);
    return result;
  }

  public boolean readable(final MapTag entry) {
    final SaveSchema.Payload schema = this.payloads.get(this.canonical(SaveSchema.Domain.MOD_DATA, entry.get("id").asRegistryId().get()));
    try {
      return entry.has("data") && (schema == null ? !entry.has("version") : version(entry) == schema.version());
    } catch(final RuntimeException unsupportedShape) {
      return false;
    }
  }

  public int writeVersion(final RegistryId id) {
    final SaveSchema.Payload schema = this.payloads.get(this.canonical(SaveSchema.Domain.MOD_DATA, id));
    return schema == null ? -1 : schema.version();
  }

  public ListTag mergePayloads(final ListTag written, final ListTag retained, final Set<RegistryId> removed) {
    final Map<RegistryId, MapTag> output = new LinkedHashMap<>();
    final Set<RegistryId> deletions = new HashSet<>();
    removed.forEach(id -> deletions.add(this.canonical(SaveSchema.Domain.MOD_DATA, id)));
    for(final Tag value : written) {
      final MapTag entry = value.asMap().clone();
      final RegistryId id = this.canonical(SaveSchema.Domain.MOD_DATA, entry.get("id").asRegistryId().get());
      entry.set("id", new RegistryIdTag(id));
      if(output.putIfAbsent(id, entry) != null) throw new IllegalArgumentException("Duplicate written mod payload " + id);
    }
    final Set<RegistryId> originals = new HashSet<>();
    for(final Tag value : retained) {
      final MapTag entry = value.asMap();
      final RegistryId id = this.canonical(SaveSchema.Domain.MOD_DATA, entry.get("id").asRegistryId().get());
      if(!originals.add(id)) throw new IllegalArgumentException("Conflicting retained mod payload identities " + id);
      if(deletions.contains(id)) continue;
      if(!this.readable(entry) || !output.containsKey(id)) {
        output.put(id, entry.clone());
      } else {
        final MapTag replacement = output.get(id);
        for(final String field : entry.keys()) {
          if(!field.equals("id") && !field.equals("version") && !field.equals("data") && !replacement.has(field) && this.preserveAbsent(SaveSchema.Domain.MOD_DATA, id, field)) replacement.set(field, entry.get(field).clone());
        }
      }
    }
    deletions.forEach(output::remove);
    final ListTag result = new ListTag();
    output.values().forEach(result::add);
    return result;
  }

  private static int version(final MapTag entry) {
    return entry.has("version") ? entry.get("version").asInt().get() : 0;
  }
}
