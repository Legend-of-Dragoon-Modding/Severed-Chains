package legend.game.wmap.world;

import legend.core.gpu.Rect4i;
import legend.game.tim.Tim;
import legend.game.tmd.TmdObjLoader;
import legend.game.tmd.UvAdjustmentMetrics14;
import legend.game.types.GameState52c;
import legend.game.types.Model124;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static legend.core.GameEngine.GPU;
import static legend.game.Models.animateModel;
import static legend.game.Models.applyModelRotationAndScale;
import static legend.game.Models.initModel;
import static legend.game.Models.loadModelStandardAnimation;

/** Render-thread owner of one optional route visual and its borrowed leader texture allocation. */
public final class WorldMapAvatarRenderer {
  private static final Logger LOGGER = LogManager.getLogger(WorldMapAvatarRenderer.class);
  private record Pending(long generation, CompletableFuture<WorldMapAvatarAssets> future) { }
  private long generation;
  private RegistryId requested;
  private Pending pending;
  private WorldMapAvatarAssets assets;
  private Model124 model;
  private WorldMapAvatarVisual visual;
  private boolean visualInitialized;
  private Tim leader;
  private UvAdjustmentMetrics14 leaderSlot;
  private boolean textureBorrowed;
  private boolean active;
  private int animation = -1;

  /** Call only on the render thread. Null selection suspends cached assets during ordinary cinematics. */
  @Nullable
  public Model124 prepare(@Nullable final RegistryId id, @Nullable final WorldMapAvatar avatar, final GameState52c state, @Nullable final Tim leader, final UvAdjustmentMetrics14[] slots) {
    this.active = false;
    if(id == null || leader == null) {
      this.restoreLeader();
      return null;
    }
    if(!id.equals(this.requested)) {
      this.delete();
      this.requested = id;
      try {
        this.pending = new Pending(this.generation, Objects.requireNonNull(avatar, "avatar").load(state));
      } catch(final RuntimeException exception) {
        LOGGER.error("Failed to request world map avatar {}", id, exception);
      }
    }
    this.leader = leader;
    this.leaderSlot = slots[0];
    if(this.pending != null && this.pending.future().isDone()) {
      final Pending pending = this.pending;
      this.pending = null;
      if(pending.generation() == this.generation) {
        try {
          final WorldMapAvatarAssets assets = Objects.requireNonNull(pending.future().join(), "Avatar loader returned null assets");
          if(assets.visual() != null) {
            this.visual = Objects.requireNonNull(assets.visual().get(), "Avatar visual factory returned null");
          } else {
            assets.validateTextureAllocation(leader);
            final Model124 model = new Model124("World map avatar " + id);
            this.model = model;
            model.uvAdjustments_9d = slots[assets.textureSlot()];
            initModel(model, assets.model(), assets.animations().get(assets.idleAnimation()));
            loadModelStandardAnimation(model, assets.animations().get(assets.idleAnimation()));
            TmdObjLoader.fromModel("World map avatar " + id, model);
          }
          this.assets = assets;
          this.animation = assets.idleAnimation();
        } catch(final RuntimeException exception) {
          if(this.model != null) this.model.deleteModelParts();
          this.model = null;
          this.deleteVisual();
          this.assets = null;
          LOGGER.error("Failed to adopt world map avatar {}; retaining vanilla model", id, exception);
        }
      }
    }
    if(this.model == null && this.visual == null) return null;
    if(this.assets.texture() != null && !this.textureBorrowed) {
      upload(this.assets.texture(), this.leaderSlot);
      this.textureBorrowed = true;
    }
    this.active = true;
    return this.model;
  }

  public void animate(final Model124 vanilla, final int movementAnimation, final int framesPerTick) {
    if(!this.active || this.model == null) return;
    final int animation = this.assets.animation(movementAnimation);
    if(animation != this.animation) {
      loadModelStandardAnimation(this.model, this.assets.animations().get(animation));
      this.animation = animation;
    }
    this.model.coord2_14.coord.transfer.set(vanilla.coord2_14.coord.transfer);
    this.model.coord2_14.transforms.rotate.set(vanilla.coord2_14.transforms.rotate);
    final WorldMapPoint scale = this.assets.scale();
    this.model.coord2_14.transforms.scale.set(scale.x(), scale.y(), scale.z());
    applyModelRotationAndScale(this.model);
    animateModel(this.model, framesPerTick);
  }

  public float shadowScale() {
    return this.active ? this.assets.shadowScale() : 1.0f;
  }

  private static void upload(final Tim texture, final UvAdjustmentMetrics14 slot) {
    final Rect4i image = texture.getImageRect();
    final Rect4i clut = texture.getClutRect();
    GPU.uploadData15(new Rect4i(slot.tpageX, slot.tpageY, image.w, image.h), texture.getImageData());
    GPU.uploadData15(new Rect4i(slot.clutX, slot.clutY, clut.w, clut.h), texture.getClutData());
  }

  public boolean hasCustomVisual() {
    return this.active && this.visual != null;
  }

  /** Returns false on provider failure so the caller can render the vanilla player this frame. */
  public boolean renderCustomVisual(final WorldMapAvatarContext context) {
    if(!this.hasCustomVisual()) return false;
    try {
      final WorldMapPoint scale = this.assets.scale();
      context.transform().scale(scale.x(), scale.y(), scale.z());
      if(!this.visualInitialized) {
        this.visual.init(context);
        this.visualInitialized = true;
      }
      this.visual.tick(context);
      this.visual.render(context);
      return true;
    } catch(final RuntimeException exception) {
      LOGGER.error("Failed to render world map avatar {}", this.requested, exception);
      this.deleteVisual();
      this.assets = null;
      this.active = false;
      return false;
    }
  }

  private void deleteVisual() {
    final WorldMapAvatarVisual visual = this.visual;
    this.visual = null;
    this.visualInitialized = false;
    if(visual != null) {
      try {
        visual.delete();
      } catch(final RuntimeException exception) {
        LOGGER.error("Failed to release world map avatar {}", this.requested, exception);
      }
    }
  }

  private void restoreLeader() {
    if(this.textureBorrowed) {
      upload(this.leader, this.leaderSlot);
      this.textureBorrowed = false;
    }
  }

  /** Invalidates pending generations before releasing only resources owned by this renderer. */
  public void delete() {
    this.generation++;
    this.restoreLeader();
    this.deleteVisual();
    if(this.model != null) this.model.deleteModelParts();
    this.model = null;
    this.assets = null;
    this.active = false;
    this.requested = null;
    this.pending = null;
    this.animation = -1;
  }
}
