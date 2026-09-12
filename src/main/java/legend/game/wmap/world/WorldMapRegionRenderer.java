package legend.game.wmap.world;

import legend.game.tmd.TmdWithId;
import legend.game.types.GameState52c;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** Owns one map asset request and its render-thread lifecycle; abandoned loads never allocate GPU resources. */
public final class WorldMapRegionRenderer {
  private CompletableFuture<WorldMapModelAssets> pending;
  private WorldMapModelAssets assets;
  private WorldMapModelRenderer renderer;
  private WorldMapPresentationController presentation;
  private Supplier<WorldMapPresentationController> presentationFactory;

  public void load(final WorldMapRegion region, final GameState52c state, final Supplier<CompletableFuture<TmdWithId>> legacyAnchor) {
    this.delete();
    this.presentationFactory = region.presentation();
    this.pending = Objects.requireNonNull(region.model().load(state), "World map model loader returned null future").thenCompose(assets -> {
      Objects.requireNonNull(assets, "World map model loader returned null assets");
      if(assets.model() != null) {
        return CompletableFuture.completedFuture(assets);
      }
      return legacyAnchor.get().thenApply(anchor -> new WorldMapModelAssets(anchor, assets.textures(), assets.renderer(), assets.retailAnimations()));
    });
  }

  /** Returns true once, after every CPU asset is ready and the map has been initialized on this thread. */
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
      return true;
    } catch(final RuntimeException | Error failure) {
      try {
        this.delete();
      } catch(final RuntimeException | Error cleanupFailure) {
        failure.addSuppressed(cleanupFailure);
      }
      throw failure;
    }
  }

  public boolean ready() {
    return this.assets != null;
  }

  public boolean customModel() {
    return this.renderer != null;
  }

  public boolean retailAnimations() {
    return this.assets != null && this.assets.retailAnimations();
  }

  public boolean useVanillaAtmosphere() {
    return this.presentation == null || this.presentation.useVanillaAtmosphere();
  }

  public boolean useVanillaSmoke() {
    return this.presentation == null || this.presentation.useVanillaSmoke();
  }

  public boolean routeVisible(final WorldMapPortal portal, final boolean visible) {
    return this.presentation == null ? visible : this.presentation.routeVisible(portal, visible);
  }

  public String regionLabel() {
    return this.presentation == null ? "" : Objects.requireNonNull(this.presentation.regionLabel(), "World map region label is null");
  }

  public void tick(final WorldMapRenderContext context) {
    if(this.renderer != null) {
      this.renderer.tick(context);
    }
    if(this.presentation != null) {
      this.presentation.tick(context);
    }
  }

  public void renderModel(final WorldMapRenderContext context) {
    if(this.renderer != null) {
      this.renderer.render(context);
    }
  }

  public void renderPresentation(final WorldMapRenderContext context) {
    if(this.presentation != null) {
      this.presentation.render(context);
    }
  }

  public void delete() {
    this.pending = null;
    this.assets = null;
    final WorldMapModelRenderer renderer = this.renderer;
    final WorldMapPresentationController presentation = this.presentation;
    this.renderer = null;
    this.presentation = null;
    this.presentationFactory = null;
    try {
      if(renderer != null) {
        renderer.delete();
      }
    } finally {
      if(presentation != null) {
        presentation.delete();
      }
    }
  }
}
