package legend.lodmod;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import legend.game.modding.events.submap.SubmapEnvironmentTextureEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import org.json.JSONObject;

import static org.junit.jupiter.api.Assertions.*;

class SelesBackgroundsTest {
  @Test
  void quarantinedOrRepairedPacksMatchTheirRecordedState() throws IOException {
    final var report = new JSONObject(Files.readString(Path.of("docs/disc1-backgrounds/resolution/quarantined-installed.json")));
    final var repaired = report.optJSONArray("repairedCuts") == null ? new org.json.JSONArray() : report.getJSONArray("repairedCuts");
    assertEquals(8, report.getJSONArray("cuts").length() + repaired.length());
    for(final Object value : report.getJSONArray("cuts")) {
      final int cut = ((Number)value).intValue();
      assertTrue(Files.isRegularFile(Path.of("gfx/backgrounds/disc1/cut-" + cut + "/background.png")));
      final var manifest = new JSONObject(Files.readString(Path.of("build/disc1/original/cut-" + cut + "/manifest.json")));
      final var event = new SubmapEnvironmentTextureEvent(null, null, null, 1, cut, manifest.getInt("foreground_layers"));
      SelesBackgrounds.replace(event);
      assertNull(event.background, "Quarantined cut " + cut);
      for(final var foreground : event.foregrounds) assertNull(foreground);
    }
    for(final Object value : repaired) {
      final int cut = ((Number)value).intValue();
      final var manifest = new JSONObject(Files.readString(Path.of("build/disc1/original/cut-" + cut + "/manifest.json")));
      final var bounds = manifest.getJSONArray("bounds");
      assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-" + cut),
        bounds.getInt(2) * 4, bounds.getInt(3) * 4, manifest.getInt("foreground_layers")), "Repaired cut " + cut);
    }
  }

  @Test
  void installedBatchesMatchRetailMetadataAndBlockedScenesStayOriginal() throws IOException {
    final Path active = Path.of("docs/disc1-backgrounds/installation/active-packs.csv");
    assertTrue(Files.isRegularFile(active));
    for(final String row : Files.readAllLines(active)) {
      final String[] fields = row.split(",");
      final int cut = Integer.parseInt(fields[0]);
      final var original = new JSONObject(Files.readString(Path.of("build/disc1/original/cut-" + cut + "/manifest.json")));
      final var bounds = original.getJSONArray("bounds");
      assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-" + cut),
        bounds.getInt(2) * 4, bounds.getInt(3) * 4, original.getInt("foreground_layers")), "Cut " + cut);
    }
    final var plan = new JSONObject(Files.readString(Path.of("docs/disc1-backgrounds/installation/plan.json")));
    final var newlyInstalled = new java.util.HashSet<Integer>();
    final Path resolution = Path.of("docs/disc1-backgrounds/resolution/runtime-integration.json");
    if(Files.isRegularFile(resolution)) {
      final var batch = new JSONObject(Files.readString(resolution));
      if(batch.getBoolean("registered")) {
        assertTrue(batch.getBoolean("complete"));
        for(final Object value : batch.getJSONArray("packs")) {
          final var pack = (JSONObject)value;
          assertTrue(pack.getBoolean("copied"));
          newlyInstalled.add(pack.getInt("cut"));
        }
      }
    }
    for(final Object value : plan.getJSONArray("blockedCuts")) {
      final int cut = ((Number)value).intValue();
      if(newlyInstalled.contains(cut)) continue;
      assertFalse(Files.exists(Path.of("gfx/backgrounds/disc1/cut-" + cut + "/background.png")));
      final var original = new JSONObject(Files.readString(Path.of("build/disc1/original/cut-" + cut + "/manifest.json")));
      final var event = new SubmapEnvironmentTextureEvent(null, null, null, 1, cut, original.getInt("foreground_layers"));
      SelesBackgrounds.replace(event);
      assertNull(event.background, "Blocked cut " + cut);
      for(final var foreground : event.foregrounds) assertNull(foreground);
    }
  }

  @Test
  void openingPacksUseTheirOwnCanvasAndLayerCounts() throws IOException {
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-5"), 1536, 1024, 3));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-6"), 1536, 2048, 2));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-7"), 1536, 1536, 6));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-13"), 1536, 2048, 4));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-14"), 1536, 1024, 3));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-15"), 2560, 1024, 13));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-16"), 2048, 1536, 9));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-17"), 2560, 1024, 10));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-19"), 1536, 2048, 13));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-20"), 1536, 2048, 16));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-21"), 2560, 1536, 21));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-22"), 1536, 1024, 17));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-23"), 1536, 2048, 11));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-24"), 1536, 1024, 15));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-25"), 1536, 1024, 13));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-26"), 1536, 1024, 16));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-27"), 1536, 1024, 16));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-28"), 1536, 1024, 13));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-29"), 1536, 1024, 16));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-31"), 2048, 1536, 17));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-37"), 2560, 1024, 13));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-641"), 1536, 1024, 8));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-640"), 1536, 1024, 2));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-642"), 1540, 1024, 5));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-643"), 1536, 1024, 2));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-644"), 1536, 1024, 3));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-645"), 1536, 1024, 2));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-675"), 2048, 1024, 7));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-676"), 1536, 1536, 3));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-677"), 2048, 1024, 3));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-703"), 3072, 1024, 5));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-704"), 3072, 1024, 8));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-10"), 1536, 2048, 5));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-725"), 2048, 1028, 9));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-12"), 2560, 1536, 14));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-742"), 1536, 2048, 4));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-694"), 1536, 1024, 2));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-695"), 1536, 1024, 17));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-624"), 2048, 1024, 5));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-625"), 2048, 1536, 10));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-56"), 1536, 1024, 3));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-38"), 2560, 1536, 7));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-39"), 2560, 1536, 11));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-40"), 1536, 1024, 8));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-41"), 1536, 1024, 9));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-42"), 1536, 1024, 1));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-43"), 1536, 1024, 4));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-44"), 2560, 1024, 7));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-45"), 1536, 2048, 5));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-46"), 1536, 2048, 17));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-47"), 1536, 2048, 2));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-48"), 2560, 1536, 2));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-49"), 1536, 2048, 5));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-50"), 1536, 1024, 5));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-51"), 1536, 1024, 1));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-52"), 2048, 1028, 10));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-53"), 1536, 1024, 7));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-54"), 1536, 2048, 7));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-66"), 2048, 1536, 17));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-69"), 2560, 1536, 16));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-86"), 1536, 1536, 12));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-91"), 2560, 1536, 20));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-92"), 2560, 1536, 19));
    assertTrue(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-90"), 2560, 1536, 19));
    assertThrows(IOException.class, () -> SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-675"), 2048, 1424, 7));
    assertFalse(SelesBackgrounds.validPack(Path.of("gfx/backgrounds/disc1/cut-676"), 1536, 1536, 4));
  }

  @Test
  void otherDiscsUnfinishedScenesAndUnexpectedLayerCountsRemainOriginal() {
    for(final var event : new SubmapEnvironmentTextureEvent[] {
      new SubmapEnvironmentTextureEvent(null, null, null, 2, 675, 7),
      new SubmapEnvironmentTextureEvent(null, null, null, 1, 11, 4),
      new SubmapEnvironmentTextureEvent(null, null, null, 1, 55, 0),
      new SubmapEnvironmentTextureEvent(null, null, null, 1, 675, 4)
    }) {
      SelesBackgrounds.replace(event);
      assertNull(event.background);
      for(final var foreground : event.foregrounds) assertNull(foreground);
    }
  }

  @TempDir
  Path directory;

  @Test
  void onlyAcceptsACompleteAlignedTransparentPack() throws IOException {
    assertFalse(SelesBackgrounds.validPack(this.directory));
    final String[] names = {"background.png", "foreground-0.png", "foreground-1.png", "foreground-2.png", "foreground-3.png"};
    for(final String name : names) {
      ImageIO.write(new BufferedImage(2048, 1424, BufferedImage.TYPE_INT_ARGB), "png", this.directory.resolve(name).toFile());
    }
    assertTrue(SelesBackgrounds.validPack(this.directory));
    ImageIO.write(new BufferedImage(512, 356, BufferedImage.TYPE_INT_ARGB), "png", this.directory.resolve(names[4]).toFile());
    assertThrows(IOException.class, () -> SelesBackgrounds.validPack(this.directory));
    ImageIO.write(new BufferedImage(2048, 1424, BufferedImage.TYPE_INT_RGB), "png", this.directory.resolve(names[4]).toFile());
    assertThrows(IOException.class, () -> SelesBackgrounds.validPack(this.directory));
  }
}
