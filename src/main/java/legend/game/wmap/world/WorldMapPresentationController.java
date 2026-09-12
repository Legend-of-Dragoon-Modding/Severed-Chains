package legend.game.wmap.world;

/** Per-map presentation lifecycle. Default methods retain the vanilla presentation. */
public interface WorldMapPresentationController {
  default void init(final WorldMapRenderContext context) { }

  default void tick(final WorldMapRenderContext context) { }

  default void render(final WorldMapRenderContext context) { }

  default void delete() { }

  default boolean useVanillaAtmosphere() {
    return true;
  }

  default boolean useVanillaSmoke() {
    return true;
  }

  default boolean routeVisible(final WorldMapPortal portal, final boolean visible) {
    return visible;
  }

  /** Empty retains the retail name sprite. A custom label is rendered as normal UI text. */
  default String regionLabel() {
    return "";
  }
}
