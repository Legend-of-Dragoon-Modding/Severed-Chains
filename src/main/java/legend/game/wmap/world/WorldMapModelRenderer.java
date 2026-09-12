package legend.game.wmap.world;

/** Per-map renderer instance. Every callback, including cleanup, runs on the render thread. */
public interface WorldMapModelRenderer {
  default void init(final WorldMapRenderContext context) { }

  default void tick(final WorldMapRenderContext context) { }

  void render(WorldMapRenderContext context);

  default void delete() { }
}
