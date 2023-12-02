package legend.game.wmap;

import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.opengl.MeshObj;
import legend.core.opengl.QuadBuilder;
import legend.game.types.Translucency;

public class PathDots {

  public MeshObj dots;
  public final MV transforms = new MV();

  public PathDots() {
    final QuadBuilder builder = new QuadBuilder("PathBigDot");
    for(int i = 0; i < 4; i++) {
      builder
        .add()
        .bpp(Bpp.BITS_4)
        .translucency(Translucency.B_PLUS_F)
        .clut(640, 496)
        .vramPos(640, 256)
        .pos(-8.0f, -8.0f, 0.0f)
        .size(16.0f, 16.0f)
        .uv(i * 16, 0);
    }

    this.dots = builder.build();
  }

  public void delete() {
    if(this.dots != null) {
      this.dots.delete();
      this.dots = null;
    }
  }
}
