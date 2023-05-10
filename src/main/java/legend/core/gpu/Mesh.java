package legend.core.gpu;

import legend.core.gte.SVECTOR;
import legend.game.types.Translucency;

import javax.annotation.Nullable;

import static legend.core.GameEngine.CPU;
import static legend.core.GameEngine.GPU;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zMax_1f8003cc;
import static legend.game.Scus94491BpeSegment.zMin;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;

public record Mesh(Segment[] segments) {
  public interface Segment {
    int vertexCount();
    boolean textured();
    boolean shaded();
    boolean translucent();
    boolean raw();

    void render(final Renderable renderable, final int offsetZ);
  }

  public static final class Segment3d implements Segment {
    public final String name;
    private final Poly3d[] polys;
    public final int vertexCount;
    public final boolean textured;
    public final boolean shaded;
    public final boolean translucent;
    public final boolean lit;
    public final boolean raw;

    public Segment3d(final String name, final Poly3d[] polys, final int vertexCount, final boolean textured, final boolean shaded, final boolean translucent, final boolean lit, final boolean raw) {
      this.name = name;
      this.polys = polys;
      this.vertexCount = vertexCount;
      this.textured = textured;
      this.shaded = shaded;
      this.translucent = translucent;
      this.lit = lit;
      this.raw = raw;
    }

    @Override
    public String toString() {
      return this.name + " (" + super.toString() + ')';
    }

    @Override
    public int vertexCount() {
      return this.vertexCount;
    }

    @Override
    public boolean textured() {
      return this.textured;
    }

    @Override
    public boolean shaded() {
      return this.shaded;
    }

    @Override
    public boolean translucent() {
      return this.translucent;
    }

    @Override
    public boolean raw() {
      return this.raw;
    }

    @Override
    public void render(final Renderable renderable, final int offsetZ) {
      outer:
      for(final Mesh.Poly3d poly : this.polys) {
        final Vec2i[] vertices = new Vec2i[this.vertexCount];
        final Vec2i[] uvs = new Vec2i[this.vertexCount];
        final int[] colours = new int[this.vertexCount];
        Translucency translucency = null;

        if(this.textured && this.translucent) {
          translucency = poly.translucency();
        }

        for(int vertexIndex = 0; vertexIndex < this.vertexCount; vertexIndex++) {
          final SVECTOR vert = poly.vertices()[vertexIndex].pos();
          CPU.MTC2(vert.getXY(), 0);
          CPU.MTC2(vert.getZ(), 1);
          CPU.COP2(0x18_0001L); // Perspective transform single

          if((int)CPU.CFC2(31) < 0) { // Errors
            continue outer;
          }

          vertices[vertexIndex] = Vec2i.unpack((int)CPU.MFC2(14));

          if(this.textured) {
            uvs[vertexIndex] = new Vec2i(poly.vertices()[vertexIndex].u(), poly.vertices()[vertexIndex].v());
          }

          // Back-face culling
          if(vertexIndex == 2) {
            CPU.COP2(0x140_0006L); // Normal clipping
            final long winding = CPU.MFC2(24);

            if(!this.translucent && winding <= 0 || this.translucent && winding == 0) {
              continue outer;
            }
          }
        }

        // Average Z
        if(this.vertexCount == 4) {
          CPU.COP2(0x168_002eL);
        } else {
          CPU.COP2(0x168_002dL);
        }

        final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());

        if(z < zMin) {
          continue;
        }

        if(this.textured && !this.lit) {
          for(int vertexIndex = 0; vertexIndex < this.vertexCount; vertexIndex++) {
            colours[vertexIndex] = poly.vertices()[vertexIndex].colour();
          }
        } else {
          for(int vertexIndex = 0; vertexIndex < this.vertexCount; vertexIndex++) {
            CPU.MTC2(poly.vertices()[vertexIndex].colour(), 6);

            final SVECTOR norm = poly.vertices()[vertexIndex].normals();
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(), 1);
            CPU.COP2(0x108_041bL); // Normal colour colour single vector
            colours[vertexIndex] = (int)CPU.MFC2(22);
          }
        }

        if(this.translucent && !this.textured) {
          translucency = Translucency.of(tmdGp0Tpage_1f8003ec.get() >>> 5 & 0b11);
        }

        GPU.queueCommand(z + offsetZ, renderable.new Command(this.name, this, vertices, uvs, colours, translucency, poly.paletteBase, poly.pageX, poly.pageY));
      }
    }
  }

  public record Poly3d(Vertex3d[] vertices, int paletteBase, int pageX, int pageY, @Nullable Translucency translucency) { }
  public record Vertex3d(SVECTOR pos, SVECTOR normals, int u, int v, int colour) { }

  public static final class Segment2d implements Segment {
    public final String name;
    private final Poly2d[] polys;
    public final int vertexCount;
    public final int z;
    public final boolean textured;
    public final boolean translucent;

    private final Vec2i[][] positions;
    private final Vec2i[][] uvs;
    private final int[][] colours;

    public Segment2d(final String name, final Poly2d[] polys, final int vertexCount, final int z, final boolean textured, final boolean translucent) {
      this.name = name;
      this.polys = polys;
      this.vertexCount = vertexCount;
      this.z = z;
      this.textured = textured;
      this.translucent = translucent;

      this.positions = new Vec2i[polys.length][this.vertexCount];
      this.uvs = new Vec2i[polys.length][this.vertexCount];
      this.colours = new int[polys.length][this.vertexCount];

      for(int polyIndex = 0; polyIndex < this.polys.length; polyIndex++) {
        final Poly2d poly = this.polys[polyIndex];

        for(int vertexIndex = 0; vertexIndex < poly.vertices.length; vertexIndex++) {
          final Vertex2d vertex = poly.vertices[vertexIndex];

          this.positions[polyIndex][vertexIndex] = vertex.pos;
          this.uvs[polyIndex][vertexIndex] = vertex.uv;
          this.colours[polyIndex][vertexIndex] = vertex.colour;
        }
      }
    }

    @Override
    public String toString() {
      return this.name + " (" + super.toString() + ')';
    }

    @Override
    public int vertexCount() {
      return this.vertexCount;
    }

    @Override
    public boolean textured() {
      return this.textured;
    }

    @Override
    public boolean shaded() {
      return false;
    }

    @Override
    public boolean translucent() {
      return this.translucent;
    }

    @Override
    public boolean raw() {
      return false;
    }

    @Override
    public void render(final Renderable renderable, final int offsetZ) {
      for(int polyIndex = 0; polyIndex < this.polys.length; polyIndex++) {
        final Poly2d poly = this.polys[polyIndex];
        GPU.queueCommand(this.z + offsetZ, renderable.new Command(this.name, this, this.positions[polyIndex], this.uvs[polyIndex], this.colours[polyIndex], poly.translucency, poly.paletteBase, poly.pageX, poly.pageY));
      }
    }
  }

  public record Poly2d(Vertex2d[] vertices, int paletteBase, int pageX, int pageY, @Nullable Translucency translucency) { }
  public record Vertex2d(Vec2i pos, Vec2i uv, int colour) { }
}
