package legend.game.wmap.world;

import legend.game.tmd.TmdWithId;
import legend.game.types.GameState52c;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** Render-thread ownership, bounded CPU loading and registry-attributed failures. */
public final class WorldMapRegionRenderer {
  private CompletableFuture<WorldMapModelAssets> pending;
  private WorldMapModelAssets assets;
  private WorldMapModelRenderer renderer;
  private WorldMapPresentationController presentation;
  private Supplier<WorldMapPresentationController> presentationFactory;
  private RegistryId regionId;
  private boolean ready;

  /** Compatibility overload; custom regions should pass their actual registry identity. */
  public void load(final WorldMapRegion region, final GameState52c state, final Supplier<CompletableFuture<TmdWithId>> legacyAnchor) {
    this.load(WorldMapRegion.legacyId(region.legacyTemplate()), region, state, legacyAnchor);
  }

  public void load(final RegistryId regionId, final WorldMapRegion region, final GameState52c state, final Supplier<CompletableFuture<TmdWithId>> legacyAnchor) {
    this.delete();
    this.regionId = Objects.requireNonNull(regionId, "regionId");
    this.presentationFactory = region.presentation();
    try {
      // thenCompose creates an owned future: timing it out does not alter a provider's shared one.
      this.pending = Objects.requireNonNull(region.model().load(state), "World map model loader returned null future").thenCompose(assets -> {
        Objects.requireNonNull(assets, "World map model loader returned null assets");
        if(assets.model() != null) {
          return CompletableFuture.completedFuture(assets);
        }
        return Objects.requireNonNull(legacyAnchor.get(), "World map anchor loader returned null future")
          .thenApply(anchor -> new WorldMapModelAssets(Objects.requireNonNull(anchor, "World map anchor loader returned null TMD"), assets.textures(), assets.renderer(), assets.retailAnimations()));
      }).orTimeout(60, TimeUnit.SECONDS);
    } catch(final RuntimeException failure) {
      throw this.failed("request assets", failure);
    }
  }

  /** Ready is published only after both owners have initialized successfully. */
  public boolean adopt(final Consumer<TmdWithId> initializeMap, final Supplier<WorldMapRenderContext> context) {
    if(this.pending == null || !this.pending.isDone()) {
      return false;
    }
    try {
      final WorldMapModelAssets loaded = this.pending.join();
      this.pending = null;
      this.assets = loaded;
      for(final var texture : loaded.textures()) {
        texture.uploadToGpu();
      }
      initializeMap.accept(loaded.model());
      if(loaded.renderer() != null) {
        this.renderer = Objects.requireNonNull(loaded.renderer().get(), "World map renderer factory returned null");
        this.renderer.init(context.get());
      }
      this.presentation = Objects.requireNonNull(this.presentationFactory.get(), "World map presentation factory returned null");
      this.presentation.init(context.get());
      this.ready = true;
      return true;
    } catch(final RuntimeException failure) {
      throw this.failed("load or initialize assets (60 second loading limit)", failure);
    } catch(final Error failure) {
      this.cleanup(failure);
      throw failure;
    }
  }

  public boolean ready() {
    return this.ready;
  }

  public boolean customModel() {
    return this.renderer != null;
  }

  public boolean retailAnimations() {
    return this.assets != null && this.assets.retailAnimations();
  }

  public boolean useVanillaAtmosphere() {
    return this.invoke("select atmosphere", () -> this.presentation == null || this.presentation.useVanillaAtmosphere());
  }

  public boolean useVanillaSmoke() {
    return this.invoke("select smoke", () -> this.presentation == null || this.presentation.useVanillaSmoke());
  }

  public boolean routeVisible(final WorldMapPortal portal, final boolean visible) {
    return this.invoke("select route visibility for " + portal.id(), () -> this.presentation == null ? visible : this.presentation.routeVisible(portal, visible));
  }

  public String regionLabel() {
    return this.invoke("select region label", () -> this.presentation == null ? "" : Objects.requireNonNull(this.presentation.regionLabel(), "World map region label is null"));
  }

  public void tick(final WorldMapRenderContext context) {
    this.invoke("tick renderer or presentation", () -> {
      if(this.renderer != null) {
        this.renderer.tick(context);
      }
      if(this.presentation != null) {
        this.presentation.tick(context);
      }
      return null;
    });
  }

  public void renderModel(final WorldMapRenderContext context) {
    this.invoke("render model", () -> {
      if(this.renderer != null) {
        this.renderer.render(context);
      }
      return null;
    });
  }

  public void renderPresentation(final WorldMapRenderContext context) {
    this.invoke("render presentation", () -> {
      if(this.presentation != null) {
        this.presentation.render(context);
      }
      return null;
    });
  }

  private <T> T invoke(final String operation, final Supplier<T> callback) {
    try {
      return callback.get();
    } catch(final RuntimeException failure) {
      throw this.failed(operation, failure);
    }
  }

  private IllegalStateException failed(final String operation, final RuntimeException cause) {
    final IllegalStateException failure = new IllegalStateException("World map region " + this.regionId + " failed to " + operation, cause);
    this.cleanup(failure);
    return failure;
  }

  private void cleanup(final Throwable failure) {
    try {
      this.delete();
    } catch(final RuntimeException | Error cleanupFailure) {
      failure.addSuppressed(cleanupFailure);
    }
  }

  public void delete() {
    this.ready = false;
    this.pending = null;
    this.assets = null;
    final WorldMapModelRenderer renderer = this.renderer;
    final WorldMapPresentationController presentation = this.presentation;
    this.renderer = null;
    this.presentation = null;
    this.presentationFactory = null;
    Throwable failure = null;
    try {
      if(renderer != null) {
        renderer.delete();
      }
    } catch(final RuntimeException | Error problem) {
      failure = problem;
    }
    try {
      if(presentation != null) {
        presentation.delete();
      }
    } catch(final RuntimeException | Error problem) {
      if(failure == null) {
        failure = problem;
      } else {
        failure.addSuppressed(problem);
      }
    }
    if(failure instanceof Error error) {
      throw error;
    }
    if(failure != null) {
      throw new IllegalStateException("World map region " + this.regionId + " failed to release resources", failure);
    }
  }
}
