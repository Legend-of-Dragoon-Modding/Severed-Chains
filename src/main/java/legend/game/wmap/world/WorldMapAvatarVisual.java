package legend.game.wmap.world;

/**
 * Optional avatar renderer with its own rendering resources. All callbacks run on the render thread.
 * The engine suspends tick/render during vanilla cinematics; delete releases resources on replacement
 * or map teardown. Custom textures belong to this renderer and do not borrow the leader's VRAM slot.
 */
public interface WorldMapAvatarVisual {
  default void init(final WorldMapAvatarContext context) { }

  default void tick(final WorldMapAvatarContext context) { }

  void render(WorldMapAvatarContext context);

  default void delete() { }
}
