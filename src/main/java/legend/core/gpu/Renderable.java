package legend.core.gpu;

import legend.game.types.Translucency;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector2i;

import javax.annotation.Nullable;
import java.util.Arrays;

public class Renderable {
  private final Mesh mesh;
  private final VramTexture texture;
  private final VramTexture[] palettes;

  private int translateX;
  private int translateY;
  private int palette;
  private int recolour = -1;
  private int colourMultiplier = 0xff;

  public Renderable(final Mesh mesh, final VramTexture texture, final VramTexture[] palettes) {
    this.mesh = mesh;
    this.texture = texture;
    this.palettes = palettes;
  }

  public Renderable palette(final int palette) {
    this.palette = palette;
    return this;
  }

  public Renderable recolour(final int colour) {
    this.recolour = colour;
    return this;
  }

  public Renderable recolourMono(final int colour) {
    return this.recolour(colour, colour, colour);
  }

  public Renderable recolour(final int r, final int g, final int b) {
    this.recolour = (b & 0xff) << 16 | (g & 0xff) << 8 | r & 0xff;
    return this;
  }

  public Renderable disableRecolour() {
    this.recolour = -1;
    return this;
  }

  public Renderable colourMultiplier(final int multiplier) {
    this.colourMultiplier = multiplier;
    return this;
  }

  public void render(final int x, final int y, final int z) {
    this.translateX = x;
    this.translateY = y;

    for(final Mesh.Segment segment : this.mesh.segments()) {
      segment.render(this, z);
    }
  }

  public void render(final int x, final int y) {
    this.render(x, y, 0);
  }

  public void render() {
    this.render(0, 0);
  }

  public class Command extends GpuCommand {
    public final String name;
    private final Mesh.Segment segment;
    private final Vector2f[] vertices;
    private final Vector2i[] uvs;
    private final int[] colours;
    private final Translucency translucency;
    private final int paletteBase;
    private final int pageX;
    private final int pageY;

    public Command(final String name, final Mesh.Segment segment, final Vector2f[] vertices, final Vector2i[] uvs, final int[] colours, @Nullable final Translucency translucency, final int paletteBase, final int pageX, final int pageY) {
      this.name = name;
      this.segment = segment;
      this.vertices = vertices;
      this.uvs = uvs;
      this.colours = colours;
      this.translucency = translucency;
      this.paletteBase = paletteBase;
      this.pageX = pageX;
      this.pageY = pageY;
    }

    @Override
    public void render(final Gpu gpu) {
      final int[] colours;

      if(Renderable.this.recolour == -1 && Renderable.this.colourMultiplier == 0xff) {
        colours = this.colours;
      } else {
        colours = Arrays.copyOf(this.colours, this.colours.length);

        if(Renderable.this.recolour != -1) {
          for(int i = 0; i < this.colours.length; i++) {
            colours[i] = Renderable.this.recolour | colours[i] & 0xff00_0000;
          }
        }

        if(Renderable.this.colourMultiplier != 0xff) {
          for(int i = 0; i < this.colours.length; i++) {
            colours[i] = Gpu.applyBlending(Renderable.this.colourMultiplier, colours[i]) >> 1;
          }
        }
      }

      gpu.rasterizeTriangle(
        Math.round(this.vertices[0].x + gpu.getOffsetX() + Renderable.this.translateX), Math.round(this.vertices[0].y + gpu.getOffsetY() + Renderable.this.translateY),
        Math.round(this.vertices[1].x + gpu.getOffsetX() + Renderable.this.translateX), Math.round(this.vertices[1].y + gpu.getOffsetY() + Renderable.this.translateY),
        Math.round(this.vertices[2].x + gpu.getOffsetX() + Renderable.this.translateX), Math.round(this.vertices[2].y + gpu.getOffsetY() + Renderable.this.translateY),
        this.uvs[0].x(), this.uvs[0].y(),
        this.uvs[1].x(), this.uvs[1].y(),
        this.uvs[2].x(), this.uvs[2].y(),
        colours[0], colours[1], colours[2],
        0, this.paletteBase + Renderable.this.palette,
        this.pageX, 0, Bpp.BITS_4,
        this.segment.textured(), this.segment.shaded(), this.segment.translucent(), this.segment.raw(), this.translucency,
        Renderable.this.texture, Renderable.this.palettes
      );

      if(this.segment.vertexCount() == 4) {
        gpu.rasterizeTriangle(
          Math.round(this.vertices[1].x + gpu.getOffsetX() + Renderable.this.translateX), Math.round(this.vertices[1].y + gpu.getOffsetY() + Renderable.this.translateY),
          Math.round(this.vertices[2].x + gpu.getOffsetX() + Renderable.this.translateX), Math.round(this.vertices[2].y + gpu.getOffsetY() + Renderable.this.translateY),
          Math.round(this.vertices[3].x + gpu.getOffsetX() + Renderable.this.translateX), Math.round(this.vertices[3].y + gpu.getOffsetY() + Renderable.this.translateY),
          this.uvs[1].x(), this.uvs[1].y(),
          this.uvs[2].x(), this.uvs[2].y(),
          this.uvs[3].x(), this.uvs[3].y(),
          colours[1], colours[2], colours[3],
          0, this.paletteBase + Renderable.this.palette,
          this.pageX, 0, Bpp.BITS_4,
          this.segment.textured(), this.segment.shaded(), this.segment.translucent(), this.segment.raw(), this.translucency,
          Renderable.this.texture, Renderable.this.palettes
        );
      }
    }

    @Override
    public String toString() {
      return this.name + " (" + super.toString() + ')';
    }
  }
}
