package legend.game.saves;

import legend.core.memory.types.IntRef;
import legend.core.tags.IntTag;
import legend.core.tags.ListTag;
import legend.core.tags.MapTag;
import legend.core.tags.RegistryIdTag;
import legend.core.tags.StringTag;
import legend.core.tags.Tag;
import legend.game.unpacker.ExpandableFileData;
import legend.game.unpacker.FileData;
import org.junit.jupiter.api.Test;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SaveSchemaMigrationTest {
  private static final RegistryId OLD = id("old");
  private static final RegistryId CURRENT = id("current");
  private static final RegistryId PAYLOAD = id("campaign_data");

  @Test
  void versionNumbersAreUnboundedWhileOneMigrationAttemptIsBounded() {
    final SaveSchemaCatalog large = catalog(new SaveSchema.Builder().payload(PAYLOAD, 10001, Map.of(10000, value -> integerData("count", 7))).build());
    final MapTag migrated = large.migratePayload(payload(10000, integerData("count", 1)));
    assertEquals(10001, migrated.get("version").asInt().get());
    assertEquals(7, migrated.get("data").asMap().get("count").asInt().get());
    final MapTag historical = payload(null, integerData("count", 1));
    final MapTag retained = large.migratePayload(historical);
    assertFalse(retained.has("version"));
    assertFalse(large.readable(retained));
  }

  @Test
  void characterStatModifierTypesRenameWithoutChangingModifierInstanceIds() {
    final SaveSchemaCatalog schemas = catalog(new SaveSchema.Builder().alias(SaveSchema.Domain.STAT_MOD, OLD, CURRENT).build());
    final MapTag modifier = SaveRegistryData.inventoryEntry("typeId", OLD);
    modifier.set("modId", new RegistryIdTag(OLD));
    final MapTag stat = SaveRegistryData.inventoryEntry("statTypeId", OLD);
    stat.set("mods", list(modifier));
    final MapTag character = SaveRegistryData.inventoryEntry("templateId", OLD);
    character.set("stats", list(stat));
    final MapTag root = new MapTag();
    root.set("characters", list(character));
    final MapTag migrated = schemas.read(roundTrip(root)).get("characters").asList().get(0).asMap().get("stats").asList().get(0).asMap();
    assertEquals(OLD, migrated.get("statTypeId").asRegistryId().get());
    assertEquals(CURRENT, migrated.get("mods").asList().get(0).asMap().get("typeId").asRegistryId().get());
    assertEquals(OLD, migrated.get("mods").asList().get(0).asMap().get("modId").asRegistryId().get());
  }

  @Test
  void customRegistryDomainsAndPayloadRenamesRemainIndependent() {
    final SaveSchema.Domain custom = new SaveSchema.Domain(id("quests"));
    final SaveSchemaCatalog schemas = catalog(new SaveSchema.Builder()
      .alias(custom, OLD, CURRENT)
      .alias(SaveSchema.Domain.MOD_DATA, OLD, PAYLOAD)
      .payload(PAYLOAD, 0, Map.of()).build());
    assertEquals(CURRENT, schemas.canonical(new SaveSchema.Domain(id("quests")), OLD));
    assertEquals(OLD, schemas.canonical(SaveSchema.Domain.ITEM, OLD));
    final MapTag historical = payload(null, integerData("count", 4));
    historical.set("id", new RegistryIdTag(OLD));
    final MapTag migrated = schemas.migratePayload(historical);
    final ListTag written = new ListTag();
    new WriteSaveDataEvent(written, schemas).add(OLD, integerData("count", 8));
    final ListTag merged = schemas.mergePayloads(written, list(migrated), Set.of());
    assertEquals(1, merged.size());
    assertEquals(PAYLOAD, merged.get(0).asMap().get("id").asRegistryId().get());
    assertEquals(8, new ReadSaveDataEvent(merged, schemas).get(OLD).asMap().get("count").asInt().get());
  }

  @Test
  void historicalRenameIsDomainScopedAndDoesNotRewriteOpaqueData() {
    final SaveSchemaCatalog schemas = catalog(new SaveSchema.Builder().alias(SaveSchema.Domain.ITEM, OLD, CURRENT).build());
    final MapTag old = new MapTag();
    final MapTag item = SaveRegistryData.inventoryEntry("itemId", OLD);
    final MapTag opaque = SaveRegistryData.inventoryEntry("itemId", OLD);
    item.set("extraData", opaque);
    old.set("items", list(item));
    old.set("equipment", list(SaveRegistryData.inventoryEntry("equipmentId", OLD)));
    final MapTag migrated = schemas.read(roundTrip(old));
    assertEquals(CURRENT, migrated.get("items").asList().get(0).asMap().get("itemId").asRegistryId().get());
    assertEquals(OLD, migrated.get("equipment").asList().get(0).asMap().get("equipmentId").asRegistryId().get());
    assertEquals(OLD, migrated.get("items").asList().get(0).asMap().get("extraData").asMap().get("itemId").asRegistryId().get());
    assertEquals(OLD, item.get("itemId").asRegistryId().get());
  }

  @Test
  void aliasesRejectCyclesConflictsAndExcessiveChainsBeforeReading() {
    assertThrows(IllegalArgumentException.class, () -> catalog(new SaveSchema.Builder().alias(SaveSchema.Domain.ITEM, OLD, CURRENT).alias(SaveSchema.Domain.ITEM, CURRENT, OLD).build()));
    assertThrows(IllegalArgumentException.class, () -> catalog(new SaveSchema.Builder().alias(SaveSchema.Domain.ITEM, OLD, CURRENT).build(), new SaveSchema.Builder().alias(SaveSchema.Domain.ITEM, OLD, id("other")).build()));
    final SaveSchema.Builder longChain = new SaveSchema.Builder();
    for(int i = 0; i < 257; i++) longChain.alias(SaveSchema.Domain.ITEM, id("entry_" + i), id("entry_" + (i + 1)));
    assertThrows(IllegalArgumentException.class, () -> catalog(longChain.build()));
  }

  @Test
  void unversionedHistoricalPayloadMigratesSequentiallyAndIsIdempotent() {
    final SaveSchemaCatalog schemas = catalog(versionTwo());
    final MapTag original = payload(null, integerData("legacyCount", 5));
    final MapTag saved = new MapTag();
    saved.set("modData", list(original));
    final MapTag migrated = schemas.read(roundTrip(saved)).get("modData").asList().get(0).asMap();
    assertEquals(2, migrated.get("version").asInt().get());
    assertEquals(10, migrated.get("data").asMap().get("count").asInt().get());
    assertFalse(migrated.get("data").asMap().has("legacyCount"));
    assertFalse(original.has("version"));
    assertEquals(5, original.get("data").asMap().get("legacyCount").asInt().get());
    final ReadSaveDataEvent read = new ReadSaveDataEvent(list(migrated), schemas);
    read.get(PAYLOAD).asMap().set("count", new IntTag(99));
    assertEquals(10, read.get(PAYLOAD).asMap().get("count").asInt().get());
    assertEquals(10, schemas.migratePayload(migrated).get("data").asMap().get("count").asInt().get());
  }

  @Test
  void missingMigrationStepLeavesOriginalOpaqueAndCannotBeOverwritten() {
    final SaveSchema schema = new SaveSchema.Builder().payload(PAYLOAD, 2, Map.of(0, value -> integerData("partial", 1))).build();
    final SaveSchemaCatalog schemas = catalog(schema);
    final MapTag original = payload(null, integerData("legacyCount", 5));
    final MapTag migrated = schemas.migratePayload(original);
    assertNull(new ReadSaveDataEvent(list(migrated), schemas).get(PAYLOAD));
    final ListTag written = new ListTag();
    new WriteSaveDataEvent(written, schemas).add(PAYLOAD, integerData("count", 99));
    final MapTag retained = schemas.mergePayloads(written, list(migrated), Set.of()).get(0).asMap();
    assertFalse(retained.has("version"));
    assertEquals(5, retained.get("data").asMap().get("legacyCount").asInt().get());
  }

  @Test
  void throwingMigrationCannotPublishItsPartialMutation() {
    final SaveSchemaCatalog schemas = catalog(new SaveSchema.Builder().payload(PAYLOAD, 1, Map.of(0, value -> {
      value.asMap().remove("legacyCount");
      throw new IllegalStateException("provider failed");
    })).build());
    final MapTag original = payload(null, integerData("legacyCount", 5));
    final MapTag retained = schemas.migratePayload(original);
    assertEquals(5, retained.get("data").asMap().get("legacyCount").asInt().get());
    assertEquals(5, original.get("data").asMap().get("legacyCount").asInt().get());
    assertFalse(schemas.readable(retained));
  }

  @Test
  void newerPayloadSurvivesOlderProviderWriteAndTaggedRoundTrip() {
    final SaveSchemaCatalog schemas = catalog(versionTwo());
    final MapTag future = payload(12, integerData("futureCount", 81));
    final ListTag written = new ListTag();
    new WriteSaveDataEvent(written, schemas).add(PAYLOAD, integerData("count", 0));
    final MapTag root = new MapTag();
    root.set("modData", schemas.mergePayloads(written, list(future), Set.of()));
    final MapTag restored = roundTrip(root).get("modData").asList().get(0).asMap();
    assertEquals(12, restored.get("version").asInt().get());
    assertEquals(81, restored.get("data").asMap().get("futureCount").asInt().get());
    assertNull(new ReadSaveDataEvent(list(restored), schemas).get(PAYLOAD));
  }

  @Test
  void removedProviderPayloadRehydratesWhenCompatibleSchemaReturns() {
    final SaveSchemaCatalog missing = catalog();
    final MapTag original = payload(1, integerData("count", 9));
    assertNull(new ReadSaveDataEvent(list(original), missing).get(PAYLOAD));
    final MapTag root = new MapTag();
    root.set("modData", missing.mergePayloads(new ListTag(), list(original), Set.of()));
    final MapTag rehydrated = catalog(versionTwo()).read(roundTrip(root)).get("modData").asList().get(0).asMap();
    assertEquals(2, rehydrated.get("version").asInt().get());
    assertEquals(18, rehydrated.get("data").asMap().get("count").asInt().get());
  }

  @Test
  void explicitPayloadDeletionSurvivesRepeatedSavesWhileOmissionPreserves() {
    final SaveSchemaCatalog schemas = catalog(versionTwo());
    final ListTag original = list(payload(2, integerData("count", 8)));
    assertEquals(1, schemas.mergePayloads(new ListTag(), original, Set.of()).size());
    final ListTag written = new ListTag();
    final WriteSaveDataEvent event = new WriteSaveDataEvent(written, schemas);
    event.remove(PAYLOAD);
    final ListTag cleared = schemas.mergePayloads(written, original, event.removedIds());
    assertTrue(cleared.isEmpty());
    assertTrue(schemas.mergePayloads(new ListTag(), cleared, Set.of()).isEmpty());
  }

  @Test
  void contentPolicyClearsOwnedOptionalFieldWithoutLosingUnknownFields() {
    final SaveSchemaCatalog schemas = catalog(new SaveSchema.Builder().missingField(SaveSchema.Domain.ITEM, CURRENT, "charges", SaveSchema.MissingField.REMOVE).build());
    final MapTag original = SaveRegistryData.inventoryEntry("itemId", CURRENT);
    original.set("charges", new IntTag(7));
    original.set("futureField", new StringTag("retain"));
    final MapTag current = SaveRegistryData.inventoryEntry("itemId", CURRENT);
    SaveRegistryData.merge(current, original, schemas);
    assertFalse(current.has("charges"));
    assertEquals("retain", current.get("futureField").asString().get());
  }

  @Test
  void renamedRecordsMatchOnceAndMissingRecordsSurviveWithoutResurrectingConsumedItems() {
    final SaveSchemaCatalog schemas = catalog(new SaveSchema.Builder().alias(SaveSchema.Domain.ITEM, OLD, CURRENT).build());
    final MapTag old = new MapTag();
    old.set("items", list(SaveRegistryData.inventoryEntry("itemId", OLD), SaveRegistryData.inventoryEntry("itemId", id("missing")), SaveRegistryData.inventoryEntry("itemId", id("consumed"))));
    final MapTag current = new MapTag();
    current.set("items", list(SaveRegistryData.inventoryEntry("itemId", CURRENT)));
    SaveRegistryData.merge(current, schemas.read(roundTrip(old)), schemas, entry -> !entry.get("itemId").asRegistryId().get().equals(id("missing")));
    assertEquals(2, current.get("items").asList().size());
    assertEquals(CURRENT, current.get("items").asList().get(0).asMap().get("itemId").asRegistryId().get());
    assertEquals(id("missing"), current.get("items").asList().get(1).asMap().get("itemId").asRegistryId().get());
  }

  private static SaveSchema versionTwo() {
    return new SaveSchema.Builder().payload(PAYLOAD, 2, Map.of(
      0, value -> {
        final MapTag data = value.asMap();
        data.set("count", data.get("legacyCount"));
        data.remove("legacyCount");
        return data;
      },
      1, value -> {
        final MapTag data = value.asMap();
        data.set("count", new IntTag(data.get("count").asInt().get() * 2));
        return data;
      }
    )).build();
  }

  private static SaveSchemaCatalog catalog(final SaveSchema... schemas) {
    return new SaveSchemaCatalog(List.of(schemas));
  }

  private static RegistryId id(final String entry) {
    return new RegistryId("test", entry);
  }

  private static MapTag integerData(final String field, final int value) {
    final MapTag data = new MapTag();
    data.set(field, new IntTag(value));
    return data;
  }

  private static MapTag payload(final Integer version, final Tag data) {
    final MapTag entry = new MapTag();
    entry.set("id", new RegistryIdTag(PAYLOAD));
    if(version != null) entry.set("version", new IntTag(version));
    entry.set("data", data);
    return entry;
  }

  private static ListTag list(final Tag... entries) {
    final ListTag list = new ListTag();
    for(final Tag entry : entries) list.add(entry);
    return list;
  }

  private static MapTag roundTrip(final MapTag original) {
    final FileData bytes = new ExpandableFileData(16);
    original.serialize(bytes, new IntRef());
    final MapTag restored = new MapTag();
    restored.deserialize(bytes, new IntRef());
    return restored;
  }
}
