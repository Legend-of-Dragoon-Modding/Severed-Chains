package legend.game;

import legend.core.DebugHelper;
import legend.core.platform.input.InputKey;
import legend.game.modding.coremod.CoreMod;
import legend.game.submap.EncounterRateMode;
import legend.game.submap.RetailSubmap;
import legend.game.submap.SMap;
import legend.game.title.Ttle;
import legend.lodmod.LodEngineStateTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.RENDERER;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.SelesSequenceTest.onRender;
import static org.junit.jupiter.api.Assertions.*;

/** Batched texture/state inspections. These do not establish gameplay traversal. */
class Disc1LayerPreparationTest {
  private static final boolean MODERN = "modern".equals(System.getenv("DISC1_PREPARATION_TEXTURES"));
  private volatile Probe active;

  private static final class Probe {
    final int cut, scene;
    final Path output;
    final List<String> trace = new ArrayList<>();
    final CompletableFuture<Void> done = new CompletableFuture<>();
    final List<Integer> dynamicLayers = new ArrayList<>();
    final List<org.json.JSONObject> focusedStates = new ArrayList<>();
    int frames;
    float probeCameraX, probeCameraY;
    float[] cameraCenter;

    Probe(final int cut, final int scene) throws Exception {
      this.cut = cut;
      this.scene = scene;
      final String variant = System.getenv().getOrDefault("DISC1_PREPARATION_VARIANT", "");
      assertTrue(variant.matches("[a-z0-9-]*"));
      assertFalse(MODERN && variant.isEmpty(), "Modern focused inspections need a distinct capture variant");
      this.output = Path.of("build/disc1/captures/cut-" + cut + (variant.isEmpty() ? "" : "-" + variant) + (MODERN ? "/modern" : "/original"));
      final Path config = Path.of("gfx/backgrounds/disc1/cut-" + cut + "/source/layer-resolution/plates.json");
      if(MODERN && Files.isRegularFile(config)) {
        final var layers = new org.json.JSONObject(Files.readString(config)).getJSONArray("dynamicLayers");
        for(final Object value : layers) this.dynamicLayers.add(((Number)value).intValue());
      }
      final Path profiles = Path.of("build/disc1/focused-layer-states.json");
      if(MODERN && Files.isRegularFile(profiles)) {
        final var states = new org.json.JSONObject(Files.readString(profiles)).optJSONArray(Integer.toString(cut));
        if(states != null) for(final Object value : states) {
          final var state = (org.json.JSONObject)value;
          assertTrue(state.getString("name").matches("[a-z0-9-]+"));
          this.focusedStates.add(state);
        }
      }
      final String cameraProfiles = System.getenv().getOrDefault("DISC1_CAMERA_PROFILES", "");
      if(MODERN && !cameraProfiles.isEmpty()) {
        final var center = new org.json.JSONObject(Files.readString(Path.of(cameraProfiles))).optJSONArray(Integer.toString(cut));
        if(center != null) this.cameraCenter = new float[] {center.getFloat(0), center.getFloat(1)};
      }
      this.trace.add("frame,cut,camera,player,layer,startX,startY,destX,destY,hidden,z,zFlags");
    }
  }

  private static Object field(final Object object, final String name) throws Exception {
    final var field = object.getClass().getDeclaredField(name);
    field.setAccessible(true);
    return field.get(object);
  }

  private void inspectFrame() {
    final Probe probe = this.active;
    if(probe == null || probe.done.isDone()) return;
    try {
      if(!(currentEngineState_8004dd04 instanceof final SMap map)) {
        if(probe.frames > 0) {
          Files.writeString(probe.output.resolve("original-inspection-end.txt"), "Original engine transition after " + probe.frames + " recorded frames to " + currentEngineState_8004dd04.getClass().getSimpleName());
          probe.done.complete(null);
        }
        return;
      }
      if(!(map.submap instanceof final RetailSubmap retail) || retail.cut != probe.cut) {
        if(probe.frames > 0) {
          Files.writeString(probe.output.resolve("original-inspection-end.txt"), "Original scene transitioned after " + probe.frames + " recorded frames to cut " + submapCut_80052c30);
          probe.done.complete(null);
          return;
        }
        map.mapTransition(probe.cut, probe.scene);
        return;
      }
      if(map.sobjs_800c6880[0] == null || field(retail, "backgroundTexture") == null) return;
      if(probe.frames == 0) {
        SelesSequenceTest.verifyTextures(map, probe.cut, !MODERN);
        Files.writeString(probe.output.resolve(MODERN ? "modern-gpu-pack.txt" : "original-gpu-export.txt"), MODERN
          ? "Modern GPU textures exactly match installed PNGs for cut " + probe.cut
          : "Original GPU textures exactly match exported PNGs for cut " + probe.cut);
        if(MODERN) Files.writeString(probe.output.resolve("planned-state-probes.json"), new org.json.JSONArray(probe.focusedStates).toString(2));
      }
      final boolean lowerRoom = probe.cut == 148 && System.getenv().getOrDefault("DISC1_PREPARATION_VARIANT", "").startsWith("lower-room");
      if(lowerRoom || probe.cameraCenter != null) {
        if(probe.frames == 60) {
          legend.core.GameEngine.SCRIPTS.pause();
          for(final var state : map.sobjs_800c6880) {
            if(state != null) state.innerStruct_00.cameraAttached_178 = false;
          }
          final var rect = (legend.core.gpu.Rect4i)field(retail, "backgroundRect");
          final float centerX = probe.cameraCenter == null ? 223 : probe.cameraCenter[0];
          final float centerY = probe.cameraCenter == null ? 292 : probe.cameraCenter[1];
          probe.probeCameraX = -centerX - (Integer)field(retail, "submapOffsetX_800cb560") - rect.x;
          probe.probeCameraY = -centerY - (Integer)field(retail, "submapOffsetY_800cb564") - rect.y;
          for(final String name : new String[] {"screenOffset_800cb568", "screenOffsetStart", "screenOffsetDest", "geomOffset", "geomOffsetStart", "geomOffsetDest"}) {
            ((org.joml.Vector2f)field(map, name)).set(probe.probeCameraX, probe.probeCameraY);
          }
          legend.core.GameEngine.GTE.setScreenOffset(probe.probeCameraX, probe.probeCameraY);
          Files.writeString(probe.output.resolve("target-view.txt"), "Exploratory camera centers native canvas point " + centerX + "," + centerY + " using actual background rectangle and renderer submap offsets. Camera=" + probe.probeCameraX + "," + probe.probeCameraY + "; no story/traversal claim.");
        }
        if(probe.frames == 90) {
          assertEquals(probe.probeCameraY, ((org.joml.Vector2f)field(map, "screenOffset_800cb568")).y, "Lower-camera probe must actually reach its target");
          SelesSequenceTest.capture(probe.output.resolve(lowerRoom ? "lower-layer13-visible.png" : "camera-centered.png"));
        }
        if(lowerRoom && probe.frames == 91) retail.setEnvForegroundPosition(1024, 1024, 13);
        if(lowerRoom && probe.frames == 110) SelesSequenceTest.capture(probe.output.resolve("lower-layer13-hidden.png"));
      }
      SelesSequenceTest.record(probe.trace, map, probe.frames, probe.cut);
      if(probe.frames == 0 || probe.frames == 120) {
        SelesSequenceTest.capture(probe.output.resolve(String.format("frame-%04d.png", probe.frames)));
      }
      if(MODERN) {
        if(probe.frames == 121) {
          legend.core.GameEngine.SCRIPTS.pause();
          for(final int layer : probe.dynamicLayers) retail.setEnvForegroundPosition(1024, 1024, layer);
        }
        if(probe.frames == 123) SelesSequenceTest.capture(probe.output.resolve("synthetic-dynamic-hidden.png"));
        if(probe.frames == 124) {
          for(final int layer : probe.dynamicLayers) retail.setEnvForegroundPosition(0, 0, layer);
        }
        if(probe.frames == 126) SelesSequenceTest.capture(probe.output.resolve("synthetic-all-visible.png"));
        final int extra = probe.frames - 127;
        if(extra >= 0 && extra / 3 < probe.focusedStates.size()) {
          final var state = probe.focusedStates.get(extra / 3);
          if(extra % 3 == 0) {
            for(final int layer : probe.dynamicLayers) retail.setEnvForegroundPosition(1024, 1024, layer);
            for(final Object value : state.getJSONArray("visible")) {
              final int layer = ((Number)value).intValue();
              assertTrue(probe.dynamicLayers.contains(layer));
              retail.setEnvForegroundPosition(0, 0, layer);
            }
            final var positions = state.optJSONObject("positions");
            if(positions != null) for(final String key : positions.keySet()) {
              final int layer = Integer.parseInt(key);
              assertTrue(probe.dynamicLayers.contains(layer));
              final var position = positions.getJSONArray(key);
              retail.setEnvForegroundPosition(position.getInt(0), position.getInt(1), layer);
            }
          }
          if(extra % 3 == 2) SelesSequenceTest.capture(probe.output.resolve("synthetic-" + state.getString("name") + ".png"));
        }
      }
      if(probe.frames++ == (MODERN ? 126 + probe.focusedStates.size() * 3 : 120)) probe.done.complete(null);
    } catch(final Throwable error) {
      probe.done.completeExceptionally(error);
    }
  }

  @Test
  @Timeout(1800)
  void inspectLayersSequentially() throws Exception {
    final Path jobs = Path.of(System.getenv().getOrDefault("DISC1_PREPARATION_JOBS", "build/disc1/preparation-jobs.csv"));
    final List<String> entries = Files.readAllLines(jobs);
    assertFalse(entries.isEmpty());
    Thread engine = null;
    final List<String> failures = new ArrayList<>();
    try {
      engine = new Thread(() -> Main.main(new String[0]));
      engine.start();
      Wait.waitFor(() -> !legend.core.GameEngine.isLoading(), 60000, "engine loading");
      // SDL's last-active window stays null when another app has keyboard focus.
      Wait.waitFor(() -> RENDERER.window() != null, 30000, "renderer window created");
      final var focus = org.lwjgl.sdl.SDL_Event.create();
      focus.type(org.lwjgl.sdl.SDLEvents.SDL_EVENT_WINDOW_FOCUS_GAINED);
      focus.window().windowID(org.lwjgl.sdl.SDLVideo.SDL_GetWindowID(
        ((legend.core.platform.SdlWindow)RENDERER.window()).getWindowPtr()));
      org.lwjgl.sdl.SDLEvents.SDL_PushEvent(focus);
      Wait.waitFor(() -> legend.core.GameEngine.PLATFORM.getLastWindow() != null,
        10000, "test input window registered");
      CONFIG.setConfig(CoreMod.MASTER_VOLUME_CONFIG.get(), 0.0f);
      CONFIG.setConfig(CoreMod.FMV_VOLUME_CONFIG.get(), 0.0f);
      CONFIG.setConfig(CoreMod.AUTO_TEXT_CONFIG.get(), true);
      CONFIG.setConfig(CoreMod.AUTO_TEXT_DELAY_CONFIG.get(), 0.0f);
      CONFIG.setConfig(CoreMod.ENCOUNTER_RATE_CONFIG.get(), EncounterRateMode.NONE);
      DebugHelper.sleep(1000);
      Input.sendKeyPress(InputKey.RETURN);
      Wait.waitForFmvToStart();
      Input.sendKeyPress(InputKey.RETURN);
      DebugHelper.sleep(250);
      Input.sendKeyPress(InputKey.RETURN);
      Wait.waitForFmvToStop();
      Wait.waitForEngineState(Ttle.class);
      Wait.waitFor(() -> {
        try { return field(currentEngineState_8004dd04, "copyrightObj") != null; }
        catch(final Exception error) { throw new AssertionError(error); }
      }, 30000, "Title ready");
      onRender(() -> {
        try {
          final var deallocate = Ttle.class.getDeclaredMethod("deallocate");
          deallocate.setAccessible(true);
          deallocate.invoke(currentEngineState_8004dd04);
        } catch(final Exception error) { throw new AssertionError(error); }
        Harness.createFreshGameState();
        Harness.setSimpleRandSeed(12345);
        Harness.setMersenneTwisterSeed(12345);
        RENDERER.window().events().onDraw(this::inspectFrame);
        // Leave the title in the same frame that its render objects are freed.
        final String[] first = entries.getFirst().split(",");
        submapCut_80052c30 = Integer.parseInt(first[0]);
        submapScene_80052c34 = Integer.parseInt(first[1]);
        Harness.transitionToEngineState(LodEngineStateTypes.SUBMAP.get());
      });
      int completed = 0;
      for(final String entry : entries) {
        final String[] parts = entry.split(",");
        final Probe probe = new Probe(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        assertEquals(MODERN, Files.exists(Path.of("gfx/backgrounds/disc1/cut-" + probe.cut + "/background.png")), "Inspection texture mode must match installation state");
        Files.createDirectories(probe.output);
        assertFalse(Files.exists(probe.output.resolve("alignment.csv")), "Reuse existing recordings; do not overwrite them");
        try {
          onRender(() -> {
            legend.core.GameEngine.SCRIPTS.resume();
            this.active = probe;
            if(!(currentEngineState_8004dd04 instanceof SMap)) {
              submapCut_80052c30 = probe.cut;
              submapScene_80052c34 = probe.scene;
              Harness.transitionToEngineState(LodEngineStateTypes.SUBMAP.get());
            }
          });
          probe.done.get(45, TimeUnit.SECONDS);
          Files.writeString(probe.output.resolve("completion.txt"), "Inspection only; " + (MODERN ? "GPU loading and synthetic layer endpoints; " : "original layer evidence; ") + "full story/traversal verification pending for cut " + probe.cut);
          System.out.println("DISC1 " + (MODERN ? "MODERN" : "ORIGINAL") + " INSPECTION " + (++completed) + "/" + entries.size() + " cut=" + probe.cut);
        } catch(final Exception error) {
          failures.add(probe.cut + ": " + error);
          Files.writeString(probe.output.resolve("completion.txt"), "Inspection failed: " + error);
        } finally {
          probe.done.complete(null);
          this.active = null;
          Files.write(probe.output.resolve("alignment.csv"), probe.trace);
        }
      }
      assertTrue(failures.isEmpty(), failures.toString());
    } finally {
      this.active = null;
      if(engine != null && engine.isAlive()) {
        onRender(() -> RENDERER.window().close());
        engine.join(10000);
        assertFalse(engine.isAlive(), "Engine must exit before another session starts");
      }
    }
  }
}
