package legend.game.wmap.preset;

import legend.game.wmap.world.WorldMapPresentationProfile;
import legend.game.wmap.world.WorldMapRegistrySnapshot;
import legend.core.Registries;
import org.legendofdragoon.modloader.events.EventManager;
import org.junit.jupiter.api.Test;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorldMapPresentationCodecTest {
  private static final RegistryId PROFILE = new RegistryId("test", "presentation");

  private static WorldMapPreset read(final String profile) throws Exception {
    return WorldMapPresetCodec.read(("<worldMapPreset version=\"1\" id=\"test:world\" name=\"Fixture\"><presentationProfiles><presentationProfile id=\"test:presentation\">" + profile + "</presentationProfile></presentationProfiles></worldMapPreset>").getBytes(StandardCharsets.UTF_8), Path.of("."));
  }

  @Test
  void authoredProfileRoundTripsWithoutRetailTableSizes() throws Exception {
    final WorldMapPreset preset = read("""
      <capabilities retailLabels="false" retailWater="false" retailAvatars="true"/>
      <namedTextures><item id="test:icon"><adjustment index="0" clutX="0" clutY="0" tpageX="0" tpageY="0" mode="NONE"/></item></namedTextures>
      <namedElements><item id="test:objective" label="A &amp; B" texture="test:icon"><position x="1.125" y="0" z="2"/></item></namedElements>
      """);
    final byte[] encoded = WorldMapPresetCodec.write(preset);
    assertFalse(new String(encoded, StandardCharsets.UTF_8).contains("<mapPositions"));
    final WorldMapPresentationProfile profile = WorldMapPresetCodec.read(encoded, Path.of(".")).resolvePresentationProfiles().get(PROFILE);
    assertEquals(0, profile.mapPositions().size());
    assertEquals("A & B", profile.namedElements().getFirst().label());
    assertFalse(profile.supportsRetailLabels());
    assertTrue(profile.supportsRetailAvatars());
    assertTrue(profile.legacyLayout().mapPositions().size() >= 8);
    assertTrue(profile.legacyLayout().textureAdjustments().size() >= 22);
    assertThrows(UnsupportedOperationException.class, () -> profile.namedElements().clear());
  }

  @Test
  void registrySnapshotAcceptsNamedOnlyProfilesThroughItsRealValidationPath() throws Exception {
    final WorldMapPreset parsed = read("<capabilities retailLabels=\"false\" retailWater=\"false\" retailAvatars=\"true\"/><namedElements><item id=\"test:label\"><position x=\"0\" y=\"0\" z=\"0\"/></item></namedElements>");
    final WorldMapPreset.Builder builder = new WorldMapPreset.Builder(new RegistryId("test", "standalone"), "Standalone fixture").standalone(true);
    builder.presentationProfiles.putAll(parsed.presentationProfiles());
    final Registries registries = new Registries(new EventManager(access -> { }, (modId, failure) -> { throw new AssertionError(failure); }), access -> { }) { };
    final WorldMapRegistrySnapshot snapshot = WorldMapRegistrySnapshot.read(registries, builder.build());
    assertEquals(0, snapshot.presentation().mapPositions().size());
    assertEquals(new RegistryId("test", "label"), snapshot.presentation().namedElements().getFirst().id());
    assertTrue(snapshot.presentation().legacyLayout().mapPositions().size() >= 8);
  }

  @Test
  void legacyFixtureKeepsCapabilitiesAbsentAndItsTablesUnchanged() throws Exception {
    final WorldMapPresentationProfile nativeProfile = WorldMapPresentationProfile.legacy();
    final WorldMapPreset.Builder builder = new WorldMapPreset.Builder(new RegistryId("test", "world"), "Legacy fixture");
    builder.presentationProfiles.put(PROFILE, new WorldMapPreset.PresentationProfile(nativeProfile.mapPositions(), nativeProfile.regions(), nativeProfile.services(), nativeProfile.waterClutYs(), nativeProfile.playerAvatarVramSlots(), nativeProfile.textureAdjustments().stream().map(WorldMapPreset.TextureAdjustment::from).toList()));
    final byte[] before = WorldMapPresetCodec.write(builder.build());
    final WorldMapPreset restored = WorldMapPresetCodec.read(before, Path.of("."));
    assertArrayEquals(before, WorldMapPresetCodec.write(restored));
    assertFalse(new String(before, StandardCharsets.UTF_8).contains("retailLabels"));
    assertEquals(nativeProfile.mapPositions(), restored.resolvePresentationProfiles().get(PROFILE).mapPositions());
  }

  @Test
  void namedTextureReferencesAndDuplicateIdentitiesAreRejected() throws Exception {
    final String flags = "<capabilities retailLabels=\"false\" retailWater=\"false\" retailAvatars=\"false\"/>";
    final String item = "<item id=\"test:label\"><position x=\"0\" y=\"0\" z=\"0\"/></item>";
    final WorldMapPreset duplicate = read(flags + "<namedElements>" + item + item + "</namedElements>");
    assertThrows(IllegalArgumentException.class, duplicate::resolvePresentationProfiles);
    final WorldMapPreset missing = read(flags + "<namedElements>" + item.replace("id=\"test:label\"", "id=\"test:label\" texture=\"test:missing\"") + "</namedElements>");
    assertThrows(IllegalArgumentException.class, missing::resolvePresentationProfiles);
    assertThrows(java.io.IOException.class, () -> read("<namedElements/>"));
  }
}
