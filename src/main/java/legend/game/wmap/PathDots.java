package legend.game.wmap;

import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.opengl.MeshObj;
import legend.core.opengl.QuadBuilder;
import legend.game.types.Translucency;

public class PathDots {

  public MeshObj bigDots;
  public MeshObj smallDots;
  public final MV transforms = new MV();

  public PathDots() {
    final QuadBuilder bigDotsBuilder = new QuadBuilder("PathBigDot");
    for(int i = 0; i < 3; i++) {
      bigDotsBuilder
        .add()
        .bpp(Bpp.BITS_4)
        .translucency(Translucency.B_PLUS_F)
        .clut(640, 496)
        .vramPos(640, 256)
        .pos(-8.0f, -8.0f, 0.0f)
        .size(16.0f, 16.0f)
        .uv(i * 16, 0);
    }
    this.bigDots = bigDotsBuilder.build();

    this.smallDots = new QuadBuilder("PathSmallDot")
      .bpp(Bpp.BITS_4)
      .translucency(Translucency.B_PLUS_F)
      .clut(640, 496)
      .vramPos(640, 256)
      .pos(-8.0f, -8.0f, 0.0f)
      .size(16.0f, 16.0f)
      .uv(48, 0)
      .build();
  }

  public void delete() {
    if(this.bigDots != null) {
      this.bigDots.delete();
      this.bigDots = null;
    }

    if(this.smallDots != null) {
      this.smallDots.delete();
      this.smallDots = null;
    }
  }
}
