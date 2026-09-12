package legend.game.wmap.world;

import legend.core.gte.MV;
import legend.core.renderer.QueuedModelTmd;
import legend.core.renderer.Texture;
import legend.game.tmd.TmdObjLoader;
import legend.game.tmd.UvAdjustmentMetrics14;
import legend.game.types.CContainer;
import legend.game.types.Model124;
import legend.game.types.TmdAnimationFile;
import legend.game.unpacker.FileData;
import org.joml.Vector3f;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static legend.core.GameEngine.RENDERER;
import static legend.game.Graphics.GsGetLw;
import static legend.game.Models.initModel;
import static legend.game.Models.loadModelStandardAnimation;

/**
 * Optional render-thread owner for a local-UV TMD, standalone RGBA texture and looping animations.
 * The constructor only copies CPU data. init/tick/render/delete belong to the owning map lifecycle.
 * Each texture factory call must return a new texture whose ownership transfers to this object.
 * This helper does not run native TIM/CLUT texture animations or allocate emulated VRAM.
 */
public final class WorldMapAnimatedModel {
  private final String name;
  private final FileData modelData;
  private final List<TmdAnimationFile> animations;
  private final Supplier<Texture> textureFactory;
  private final MV local = new MV();
  private final MV world = new MV();
  private final Vector3f ambient = new Vector3f(1.0f);
  private Model124 model;
  private Texture texture;
  private int animation;
  private float animationTicks;

  public WorldMapAnimatedModel(final String name, final FileData modelData, final List<TmdAnimationFile> animations, final Supplier<Texture> textureFactory) {
    this.name = Objects.requireNonNull(name, "name");
    this.modelData = new FileData(Objects.requireNonNull(modelData, "modelData").getBytes().clone());
    this.animations = List.copyOf(animations);
    if(this.animations.isEmpty()) {
      throw new IllegalArgumentException("Animated world map model requires at least one animation");
    }
    for(final TmdAnimationFile animation : this.animations) {
      if(animation.totalFrames_0e < 2) {
        throw new IllegalArgumentException("Animated world map model requires at least one complete keyframe");
      }
    }
    this.textureFactory = Objects.requireNonNull(textureFactory, "textureFactory");
  }

  public void init() {
    if(this.model != null) {
      throw new IllegalStateException("World map animated model is already initialized: " + this.name);
    }
    try {
      this.texture = Objects.requireNonNull(this.textureFactory.get(), "World map animated model texture factory returned null");
      this.model = new Model124(this.name);
      this.model.uvAdjustments_9d = UvAdjustmentMetrics14.PNG;
      // Parse per owner: model UV adjustment and GPU objects must never mutate shared TMD objects.
      final CContainer container = new CContainer(this.name, this.modelData);
      for(final TmdAnimationFile animation : this.animations) {
        if(animation.modelPartCount_0c != container.tmdPtr_00.tmd.header.nobj) {
          throw new IllegalArgumentException("Animation part count differs from world map model " + this.name);
        }
      }
      initModel(this.model, container, this.animations.get(this.animation));
      TmdObjLoader.fromModel(this.name, this.model, this.texture.width, this.texture.height);
      this.animationTicks = 0;
      this.invalidateTransforms();
    } catch(final RuntimeException | Error failure) {
      try {
        this.delete();
      } catch(final RuntimeException | Error cleanupFailure) {
        failure.addSuppressed(cleanupFailure);
      }
      throw failure;
    }
  }

  /** Changes animation only when the index changes; repeated selection does not restart its loop. */
  public void animation(final int index) {
    if(index < 0 || index >= this.animations.size()) {
      throw new IllegalArgumentException("Unknown animation " + index + " for " + this.name);
    }
    if(this.animation != index) {
      this.animation = index;
      this.animationTicks = 0;
      if(this.model != null) {
        loadModelStandardAnimation(this.model, this.animations.get(index));
        this.invalidateTransforms();
      }
    }
  }

  /** Advance authored animation ticks; context.frameScale() supplies the map's reference tick rate. */
  public void tick(final float ticks) {
    this.requireInitialized();
    if(!Float.isFinite(ticks) || ticks < 0) {
      throw new IllegalArgumentException("World map animation ticks must be finite and nonnegative");
    }
    final int duration = this.animations.get(this.animation).totalFrames_0e;
    this.animationTicks = (float)(((double)this.animationTicks + ticks) % duration);
    this.model.anim_08.apply((int)this.animationTicks);
    this.invalidateTransforms();
  }

  private void invalidateTransforms() {
    this.model.coord2_14.flg = 0;
    for(final var part : this.model.modelParts_00) {
      part.coord2_04.flg = 0;
    }
  }

  /** Parent is a complete world transform, e.g. WorldMapAvatarContext.transform(); never modified. */
  public void render(final MV parent) {
    this.requireInitialized();
    for(int i = 0; i < this.model.modelParts_00.length; i++) {
      if(i < Long.SIZE && (this.model.partInvisible_f4 & 1L << i) != 0) {
        continue;
      }
      final var part = this.model.modelParts_00[i];
      GsGetLw(part.coord2_04, this.local);
      this.local.compose(parent, this.world);
      RENDERER.queueModel(part.tmd_08.getObj(), this.world, QueuedModelTmd.class)
        .texture(this.texture)
        .backgroundColour(this.ambient);
    }
  }

  private void requireInitialized() {
    if(this.model == null) {
      throw new IllegalStateException("World map animated model is not initialized: " + this.name);
    }
  }

  /** Connect the standard avatar lifecycle and motion states without manual pose/cache handling. */
  public WorldMapAvatarVisual asAvatarVisual(final int idle, final int walk, final int run) {
    for(final int index : new int[] {idle, walk, run}) {
      if(index < 0 || index >= this.animations.size()) {
        throw new IllegalArgumentException("Unknown avatar animation " + index + " for " + this.name);
      }
    }
    return new WorldMapAvatarVisual() {
      @Override
      public void init(final WorldMapAvatarContext context) {
        WorldMapAnimatedModel.this.animation(idle);
        WorldMapAnimatedModel.this.init();
      }

      @Override
      public void tick(final WorldMapAvatarContext context) {
        WorldMapAnimatedModel.this.animation(switch(context.motion()) {
          case IDLE -> idle;
          case WALK -> walk;
          case RUN -> run;
        });
        WorldMapAnimatedModel.this.tick(context.world().frameScale());
      }

      @Override
      public void render(final WorldMapAvatarContext context) {
        WorldMapAnimatedModel.this.render(context.transform());
      }

      @Override
      public void delete() {
        WorldMapAnimatedModel.this.delete();
      }
    };
  }

  public void delete() {
    final Model124 model = this.model;
    final Texture texture = this.texture;
    this.model = null;
    this.texture = null;
    this.animationTicks = 0;
    try {
      if(model != null) {
        model.deleteModelParts();
      }
    } finally {
      if(texture != null) {
        texture.delete();
      }
    }
  }
}
