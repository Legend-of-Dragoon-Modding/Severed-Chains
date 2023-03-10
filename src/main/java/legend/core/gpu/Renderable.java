package legend.core.gpu;

import legend.game.types.Translucency;

import javax.annotation.Nullable;

public class Renderable {
  private final Mesh mesh;
  private final VramTexture2 texture;
  private final VramTexture2[] palettes;

  private int translateX;
  private int translateY;
  private int palette;
  private int recolour = -1;

  public Renderable(final Mesh mesh, final VramTexture2 texture, final VramTexture2[] palettes) {
    this.mesh = mesh;
    this.texture = texture;
    this.palettes = palettes;
  }

  public Renderable translate(final int x, final int y) {
    this.translateX = x;
    this.translateY = y;
    return this;
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

  public void render() {
    for(final Mesh.Segment segment : this.mesh.segments()) {
      segment.render(this);
    }
  }

  public class Command extends GpuCommand {
    private final Mesh.Segment segment;
    private final Vec2i[] vertices;
    private final Vec2i[] uvs;
    private final int[] colours;
    private final Translucency translucency;
    private final int paletteBase;

    public Command(final Mesh.Segment segment, final Vec2i[] vertices, final Vec2i[] uvs, final int[] colours, @Nullable final Translucency translucency, final int paletteBase) {
      this.segment = segment;
      this.vertices = vertices;
      this.uvs = uvs;
      this.colours = colours;
      this.translucency = translucency;
      this.paletteBase = paletteBase;
    }

    @Override
    public void render(final Gpu gpu) {
      this.rasterizeTriangle(
        gpu,
        this.vertices[0].x() + gpu.getOffsetX(), this.vertices[0].y() + gpu.getOffsetY(),
        this.vertices[1].x() + gpu.getOffsetX(), this.vertices[1].y() + gpu.getOffsetY(),
        this.vertices[2].x() + gpu.getOffsetX(), this.vertices[2].y() + gpu.getOffsetY(),
        this.uvs[0].x(), this.uvs[0].y(),
        this.uvs[1].x(), this.uvs[1].y(),
        this.uvs[2].x(), this.uvs[2].y(),
        this.colours[0], this.colours[1], this.colours[2]
      );

      if(this.segment.vertexCount() == 4) {
        this.rasterizeTriangle(
          gpu,
          this.vertices[1].x() + gpu.getOffsetX(), this.vertices[1].y() + gpu.getOffsetY(),
          this.vertices[2].x() + gpu.getOffsetX(), this.vertices[2].y() + gpu.getOffsetY(),
          this.vertices[3].x() + gpu.getOffsetX(), this.vertices[3].y() + gpu.getOffsetY(),
          this.uvs[1].x(), this.uvs[1].y(),
          this.uvs[2].x(), this.uvs[2].y(),
          this.uvs[3].x(), this.uvs[3].y(),
          this.colours[1], this.colours[2], this.colours[3]
        );
      }
    }

    private void rasterizeTriangle(final Gpu gpu, int vx0, int vy0, int vx1, int vy1, int vx2, int vy2, final int tu0, final int tv0, int tu1, int tv1, int tu2, int tv2, int c0, int c1, int c2) {
      vx0 += Renderable.this.translateX;
      vy0 += Renderable.this.translateY;
      vx1 += Renderable.this.translateX;
      vy1 += Renderable.this.translateY;
      vx2 += Renderable.this.translateX;
      vy2 += Renderable.this.translateY;

      int area = Gpu.orient2d(vx0, vy0, vx1, vy1, vx2, vy2);
      if(area == 0) {
        return;
      }

      // Reorient triangle so it has clockwise winding
      if(area < 0) {
        final int tempVX = vx1;
        final int tempVY = vy1;
        vx1 = vx2;
        vy1 = vy2;
        vx2 = tempVX;
        vy2 = tempVY;

        final int tempTU = tu1;
        final int tempTV = tv1;
        tu1 = tu2;
        tv1 = tv2;
        tu2 = tempTU;
        tv2 = tempTV;

        final int tempC = c1;
        c1 = c2;
        c2 = tempC;

        area = -area;
      }

      /*boundingBox*/
      int minX = Math.min(vx0, Math.min(vx1, vx2));
      int minY = Math.min(vy0, Math.min(vy1, vy2));
      int maxX = Math.max(vx0, Math.max(vx1, vx2));
      int maxY = Math.max(vy0, Math.max(vy1, vy2));

      if(maxX - minX > gpu.vramWidth || maxY - minY > gpu.vramHeight) {
        return;
      }

      /*clip*/
      minX = (short)Math.max(minX, gpu.drawingArea.x.get());
      minY = (short)Math.max(minY, gpu.drawingArea.y.get());
      maxX = (short)Math.min(maxX, gpu.drawingArea.w.get());
      maxY = (short)Math.min(maxY, gpu.drawingArea.h.get());

      final int A01 = vy0 - vy1;
      final int B01 = vx1 - vx0;
      final int A12 = vy1 - vy2;
      final int B12 = vx2 - vx1;
      final int A20 = vy2 - vy0;
      final int B20 = vx0 - vx2;

      final int bias0 = Gpu.isTopLeft(vx1, vy1, vx2, vy2) ? 0 : -1;
      final int bias1 = Gpu.isTopLeft(vx2, vy2, vx0, vy0) ? 0 : -1;
      final int bias2 = Gpu.isTopLeft(vx0, vy0, vx1, vy1) ? 0 : -1;

      int w0_row = Gpu.orient2d(vx1, vy1, vx2, vy2, minX, minY);
      int w1_row = Gpu.orient2d(vx2, vy2, vx0, vy0, minX, minY);
      int w2_row = Gpu.orient2d(vx0, vy0, vx1, vy1, minX, minY);

      if(Renderable.this.recolour != -1) {
        c0 = Renderable.this.recolour | c0 & 0xff00_0000;
        c1 = Renderable.this.recolour | c1 & 0xff00_0000;
        c2 = Renderable.this.recolour | c2 & 0xff00_0000;
      }

      // Rasterize
      for(int y = minY; y < maxY; y++) {
        // Barycentric coordinates at start of row
        int w0 = w0_row;
        int w1 = w1_row;
        int w2 = w2_row;

        for(int x = minX; x < maxX; x++) {
          // If p is on or inside all edges, render pixel
          if((w0 + bias0 | w1 + bias1 | w2 + bias2) >= 0) {
            // Adjustments per triangle instead of per pixel can be done at area level
            // but it still has small off-by-1 error appreciable on some textured quads
            // I assume it could be handled recalculating AXX and BXX offsets but the math is beyond my scope

            // Check background mask
            if(gpu.status.drawPixels == Gpu.DRAW_PIXELS.NOT_TO_MASKED_AREAS) {
              if((gpu.getPixel(x, y) & 0xff00_0000) != 0) {
                w0 += A12;
                w1 += A20;
                w2 += A01;
                continue;
              }
            }
            // Reset default colour of the triangle calculated outside the for as it gets overwritten as follows...
            int colour = c0;

            if(this.segment.shaded()) {
              colour = gpu.getShadedColor(w0, w1, w2, c0, c1, c2, area);
            }

            if(this.segment.textured()) {
              final int texelX = Gpu.interpolateCoords(w0, w1, w2, tu0, tu1, tu2, area);
              final int texelY = Gpu.interpolateCoords(w0, w1, w2, tv0, tv1, tv2, area);

              int texel = 0;
              boolean found = false;
              for(final VramTexture2 palette : Renderable.this.palettes) {
                if(palette.vramY - this.paletteBase - Renderable.this.palette == 0) {
                  texel = Renderable.this.texture.getTexel(palette, texelX, texelY);
                  found = true;
                  break;
                }
              }

              if(!found) {
                System.err.println("Failed to find palette");
              }

              if(texel == 0) {
                w0 += A12;
                w1 += A20;
                w2 += A01;
                continue;
              }

              if(!this.segment.raw()) {
                texel = gpu.applyBlending(colour, texel);
              }

              colour = texel;
            }

            if(this.segment.translucent() && (!this.segment.textured() || (colour & 0xff00_0000) != 0)) {
              colour = gpu.handleTranslucence(x, y, colour, this.translucency);
            }

            colour |= (gpu.status.setMaskBit ? 1 : 0) << 24;

            gpu.setPixel(x, y, colour);
          }

          // One step right
          w0 += A12;
          w1 += A20;
          w2 += A01;
        }

        // One step down
        w0_row += B12;
        w1_row += B20;
        w2_row += B01;
      }
    }
  }
}
