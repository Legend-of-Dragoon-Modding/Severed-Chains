package legend.game;

import legend.core.DebugHelper;
import legend.core.platform.input.InputKey;
import legend.core.renderer.Texture;
import legend.game.modding.coremod.CoreMod;
import legend.game.submap.RetailSubmap;
import legend.game.submap.SMap;
import legend.game.title.Ttle;
import legend.lodmod.LodEngineStateTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static legend.core.GameEngine.*;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_800b.submapFullyLoaded_800bd7b4;
import static org.junit.jupiter.api.Assertions.*;

/** Real renderer smoke test. Uses a fresh campaign, never a user's save. */
class SelesBackgroundTest {
  private static final int CUT = Integer.parseInt(System.getenv().getOrDefault("DISC1_LOAD_CUT", "9"));
  private static final int SCENE = Integer.parseInt(System.getenv().getOrDefault("DISC1_LOAD_SCENE", "0"));
  private static final int WIDTH = Integer.parseInt(System.getenv().getOrDefault("DISC1_LOAD_WIDTH", "2048"));
  private static final int HEIGHT = Integer.parseInt(System.getenv().getOrDefault("DISC1_LOAD_HEIGHT", "1424"));
  private static final int LAYERS = Integer.parseInt(System.getenv().getOrDefault("DISC1_LOAD_LAYERS", "4"));

  @Test
  @Timeout(180)
  void loadReloadAndFallback() throws Exception {
    final Path background = Path.of(CUT == 9 ? "gfx/backgrounds/seles-overlook/background.png" : "gfx/backgrounds/disc1/cut-" + CUT + "/background.png");
    final Path disabled = background.resolveSibling("background.test-disabled.png");
    assertTrue(Files.isRegularFile(background));
    assertFalse(Files.exists(disabled));
    final Thread engine = Bootstrapper.loadEngine();
    try {
      CONFIG.setConfig(CoreMod.MASTER_VOLUME_CONFIG.get(), 0.0f);
      CONFIG.setConfig(CoreMod.FMV_VOLUME_CONFIG.get(), 0.0f);
      CONFIG.setConfig(CoreMod.AUTO_TEXT_CONFIG.get(), true);
      CONFIG.setConfig(CoreMod.AUTO_TEXT_DELAY_CONFIG.get(), 0.0f);
      DebugHelper.sleep(1000);
      press();
      Wait.waitForFmvToStart();
      press();
      press();
      Wait.waitForFmvToStop();
      Wait.waitForEngineState(Ttle.class);
      Wait.waitFor(() -> {
        try {
          final var ready = Ttle.class.getDeclaredField("copyrightObj");
          ready.setAccessible(true);
          return ready.get(currentEngineState_8004dd04) != null;
        } catch(final ReflectiveOperationException error) { throw new AssertionError(error); }
      }, 30000, "Title ready for cleanup");
      onRender(() -> {
        try {
          final var deallocate = Ttle.class.getDeclaredMethod("deallocate");
          deallocate.setAccessible(true);
          deallocate.invoke(currentEngineState_8004dd04);
        } catch(final ReflectiveOperationException error) { throw new AssertionError(error); }
        Harness.createFreshGameState();
        submapCut_80052c30 = CUT;
        submapScene_80052c34 = SCENE;
        Harness.transitionToEngineState(LodEngineStateTypes.SUBMAP.get());
      });
      Wait.waitForEngineState(SMap.class);
      Wait.waitFor(() -> submapFullyLoaded_800bd7b4, 30000, "Seles loaded");
      DebugHelper.sleep(2000);
      onRender(() -> SCRIPTS.pause());
      assertTextures(WIDTH, HEIGHT);
      captureLoadingEvidence("modern-loaded.png");
      System.out.println("SELES: enhanced scene ready for visual inspection");
      DebugHelper.sleep(1000);

      Files.move(background, disabled);
      reload();
      assertTextures(WIDTH / 4, HEIGHT / 4);
      verifyOriginalExport();
      captureLoadingEvidence("original-fallback.png");
      System.out.println("SELES: original fallback ready for visual inspection");
      DebugHelper.sleep(1000);
      Files.move(disabled, background);
      reload();
      assertTextures(WIDTH, HEIGHT);
      Files.move(background, disabled);
      Files.writeString(background, "invalid PNG for fallback test");
      try {
        reload();
        assertTextures(WIDTH / 4, HEIGHT / 4);
      } finally {
        Files.delete(background);
        Files.move(disabled, background);
      }
      reload();
      assertTextures(WIDTH, HEIGHT);
      if(CUT == 9) {
        transition(10, 2);
        transition(9, 0);
        assertTextures(WIDTH, HEIGHT);
      }
      captureLoadingEvidence("modern-restored.png");
    } finally {
      if(Files.exists(disabled)) {
        Files.move(disabled, background, StandardCopyOption.REPLACE_EXISTING);
      }
      if(engine.isAlive()) {
        onRender(() -> RENDERER.window().close());
        engine.join(10000);
        assertFalse(engine.isAlive(), "Engine must exit before another smoke session starts");
      }
    }
  }

  private static void reload() throws Exception {
    transition(CUT, SCENE);
  }

  private static void transition(final int cut, final int sceneIndex) throws Exception {
    final List<Texture> previous = new ArrayList<>();
    onRender(() -> {
      try {
        final var scene = ((SMap)currentEngineState_8004dd04).submap;
        final var background = RetailSubmap.class.getDeclaredField("backgroundTexture");
        final var foregrounds = RetailSubmap.class.getDeclaredField("foregroundTextures");
        background.setAccessible(true);
        foregrounds.setAccessible(true);
        previous.add((Texture)background.get(scene));
        previous.addAll(Arrays.asList((Texture[])foregrounds.get(scene)));
      } catch(final ReflectiveOperationException e) {
        throw new AssertionError(e);
      }
      SCRIPTS.resume();
      ((SMap)currentEngineState_8004dd04).mapTransition(cut, sceneIndex);
    });
    Wait.waitFor(() -> !submapFullyLoaded_800bd7b4, 5000, "Seles unloading");
    Wait.waitFor(() -> submapFullyLoaded_800bd7b4, 30000, "Seles reloaded");
    DebugHelper.sleep(2000);
    onRender(() -> {
      SCRIPTS.pause();
      try {
        final var liveField = Texture.class.getDeclaredField("texList");
        liveField.setAccessible(true);
        final var live = (List<?>)liveField.get(null);
        for(final Texture texture : previous) {
          if(texture == null) continue; // An unused retail slot never allocated a texture to release.
          assertFalse(live.contains(texture), "Old scene texture must be released: " + texture.name);
        }
      } catch(final ReflectiveOperationException e) {
        throw new AssertionError(e);
      }
    });
  }

  private static void assertTextures(final int width, final int height) throws Exception {
    onRender(() -> {
      try {
        final var scene = ((SMap)currentEngineState_8004dd04).submap;
        final var background = RetailSubmap.class.getDeclaredField("backgroundTexture");
        final var foregrounds = RetailSubmap.class.getDeclaredField("foregroundTextures");
        background.setAccessible(true);
        foregrounds.setAccessible(true);
        final Texture bg = (Texture)background.get(scene);
        assertEquals(width, bg.width);
        assertEquals(height, bg.height);
        final Texture[] layers = (Texture[])foregrounds.get(scene);
        assertEquals(LAYERS, layers.length);
        for(final Texture layer : layers) {
          if(width == WIDTH / 4 && layer == null) continue; // Retail unused slots have no GPU texture; verifyOriginalExport checks exact emptiness.
          assertNotNull(layer);
          assertEquals(width, layer.width);
          assertEquals(height, layer.height);
        }
      } catch(final ReflectiveOperationException e) {
        throw new AssertionError(e);
      }
    });
  }

  private static void press() {
    Input.sendKeyPress(InputKey.RETURN);
    DebugHelper.sleep(250);
  }

  /** Read texture assets back from the GPU, not a screenshot, to check the exporter. */
  private static void verifyOriginalExport() throws Exception {
    onRender(() -> {
      try {
        SelesSequenceTest.verifyTextures((SMap)currentEngineState_8004dd04, CUT, true);
      } catch(final Exception e) {
        throw new AssertionError(e);
      }
    });
  }

  private static void captureLoadingEvidence(final String name) throws Exception {
    if(CUT == 9) return;
    final Path directory = Path.of("build/disc1/installation/smoke-cut-" + CUT);
    Files.createDirectories(directory);
    onRender(() -> {
      try { SelesSequenceTest.capture(directory.resolve(name)); }
      catch(final Exception error) { throw new AssertionError(error); }
    });
    Files.writeString(directory.resolve("scope.txt"), "Loading/fallback/cleanup smoke test only. Full gameplay verification deferred.");
  }

  private static void onRender(final Runnable task) throws Exception {
    final CompletableFuture<Void> done = new CompletableFuture<>();
    RENDERER.addTask(() -> {
      try {
        task.run();
        done.complete(null);
      } catch(final Throwable e) {
        done.completeExceptionally(e);
      }
    });
    done.get(30, TimeUnit.SECONDS);
  }
}
