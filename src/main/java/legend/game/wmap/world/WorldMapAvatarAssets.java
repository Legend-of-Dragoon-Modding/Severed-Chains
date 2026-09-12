package legend.game.wmap.world;

import legend.game.tim.Tim;
import legend.game.types.CContainer;
import legend.game.types.TmdAnimationFile;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Owned CPU assets for an optional route visual. Slot zero borrows the leader's bounded texture
 * allocation and requires a TIM; slots one through three use the existing ship/Coolon/teleport textures.
 * Custom visuals own their rendering resources and do not use these texture slots.
 */
public record WorldMapAvatarAssets(@Nullable CContainer model, @Nullable Tim texture, List<TmdAnimationFile> animations, WorldMapPoint scale, float shadowScale, int idleAnimation, int walkAnimation, int runAnimation, int textureSlot, @Nullable Supplier<WorldMapAvatarVisual> visual) {
  public WorldMapAvatarAssets(final CContainer model, @Nullable final Tim texture, final List<TmdAnimationFile> animations, final WorldMapPoint scale, final float shadowScale, final int idleAnimation, final int walkAnimation, final int runAnimation, final int textureSlot) {
    this(model, texture, animations, scale, shadowScale, idleAnimation, walkAnimation, runAnimation, textureSlot, null);
  }

  /** The factory creates GPU resources only when adopted on the render thread. */
  public static WorldMapAvatarAssets custom(final Supplier<WorldMapAvatarVisual> visual, final float shadowScale) {
    return new WorldMapAvatarAssets(null, null, List.of(), new WorldMapPoint(1.0f, 1.0f, 1.0f), shadowScale, 0, 0, 0, -1, Objects.requireNonNull(visual, "visual"));
  }

  public WorldMapAvatarAssets {
    if(visual == null) {
      Objects.requireNonNull(model, "model");
    } else if(model != null || texture != null || !animations.isEmpty() || textureSlot != -1) {
      throw new IllegalArgumentException("Custom world map avatar visuals own their model and textures");
    }
    animations = List.copyOf(animations);
    Objects.requireNonNull(scale, "scale");
    if(visual == null && (textureSlot < 0 || textureSlot > 3)) throw new IllegalArgumentException("World map avatar texture slot must be 0..3");
    if(visual == null && (textureSlot == 0 && texture == null || textureSlot != 0 && texture != null)) throw new IllegalArgumentException("Only leader-slot avatars provide an uploadable TIM");
    if(!Float.isFinite(scale.x()) || !Float.isFinite(scale.y()) || !Float.isFinite(scale.z()) || scale.x() <= 0 || scale.y() <= 0 || scale.z() <= 0) throw new IllegalArgumentException("World map avatar scale must be finite and positive");
    if(!Float.isFinite(shadowScale) || shadowScale < 0) throw new IllegalArgumentException("World map avatar shadow scale must be finite and nonnegative");
    for(final int animation : new int[]{idleAnimation, walkAnimation, runAnimation}) {
      if(visual == null && (animation < 0 || animation >= animations.size())) throw new IllegalArgumentException("World map avatar animation index outside loaded animations: " + animation);
    }
  }

  public int animation(final int movementAnimation) {
    return switch(movementAnimation) {
      case 3 -> this.walkAnimation;
      case 4 -> this.runAnimation;
      default -> this.idleAnimation;
    };
  }

  public void validateTextureAllocation(final Tim leader) {
    if(this.texture == null) return;
    if(!this.texture.hasClut() || !leader.hasClut() || this.texture.getBpp() != leader.getBpp()) throw new IllegalArgumentException("World map avatar TIM must match the leader's indexed texture format");
    final var image = this.texture.getImageRect();
    final var clut = this.texture.getClutRect();
    final var allocatedImage = leader.getImageRect();
    final var allocatedClut = leader.getClutRect();
    if(image.w <= 0 || image.h <= 0 || clut.w <= 0 || clut.h <= 0 || image.w > allocatedImage.w || image.h > allocatedImage.h || clut.w > allocatedClut.w || clut.h > allocatedClut.h) throw new IllegalArgumentException("World map avatar TIM exceeds the leader texture or CLUT allocation");
  }
}
