package legend.game.wmap;

import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.opengl.MeshObj;
import legend.core.opengl.QuadBuilder;
import legend.game.types.Translucency;
import org.joml.Vector3f;

import static legend.core.GameEngine.RENDERER;

public class MapMarker {
  public static final Vector3f[] colours = {
    new Vector3f(85.0f / 128.0f, 0.0f, 0.0f),
    new Vector3f(0.0f, 0.0f, 85.0f / 128.0f),
    new Vector3f(1.0f, 1.0f, 2.0f)
  };

  public MeshObj[] sprites;
  public final MV transforms = new MV();

  public MapMarker(final String name, final int objArrayLength, final float uvSize, final int v, final boolean isTranslucent) {
    this.sprites = new MeshObj[objArrayLength];

    for(int i = 0; i < objArrayLength; i++) {
      final QuadBuilder builder = new QuadBuilder(name + " (index " + i +')')
        .bpp(Bpp.BITS_4)
        .clut(640, 496)
        .vramPos(640, 256)
        .size(1.0f, 1.0f)
        .uv(i * uvSize, v)
        .uvSize(uvSize, uvSize);

      if(isTranslucent) {
        builder.translucency(Translucency.B_PLUS_F);
      }

      this.sprites[i] = builder.build();
    }
  }

  public void render(final int spriteIndex, final float x, final float y, final float z, final float size, final int colourIndex) {
    this.transforms.scaling(size, size, 1.0f);
    this.transforms.transfer.set(x, y, z);
    RENDERER.queueOrthoOverlayModel(this.sprites[spriteIndex], this.transforms)
      .colour(colours[colourIndex]);
  }

  public void delete() {
    for(final MeshObj sprite : this.sprites) {
      if(sprite != null) {
        sprite.delete();
      }
    }
  }
}
