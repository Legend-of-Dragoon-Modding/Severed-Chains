package legend.core.gpu;

import legend.core.IoHelper;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable1c;
import legend.game.tim.Tim;
import org.joml.Math;

import java.nio.file.Path;
import java.util.Arrays;

import static legend.core.gpu.Gpu.isTopLeft;
import static legend.core.gpu.Gpu.orient2d;

public class ModelTextureDumper {
  public static void dumpTmd(final Tmd tmd, final Tim tim, final Path path) {
    final VramTextureSingle tex = (VramTextureSingle)VramTextureLoader.textureFromTim(tim);
    final VramTextureSingle[] palettes = Arrays.stream(VramTextureLoader.palettesFromTim(tim)).map(VramTextureSingle.class::cast).toArray(VramTextureSingle[]::new);
    final int[] clutMap = new int[tex.rect.w * tex.rect.h];
    Arrays.fill(clutMap, -1);

    for(final TmdObjTable1c table : tmd.objTable) {
      for(final TmdObjTable1c.Primitive primitive : table.primitives_10) {
        final int command = primitive.header() & 0xff04_0000;
        final int primitiveId = command >>> 24;

        final boolean textured = (primitiveId & 0b100) != 0;

        if(textured) {
          final boolean shaded = (command & 0x4_0000) != 0;
          final boolean gourad = (primitiveId & 0b1_0000) != 0;
          final boolean quad = (primitiveId & 0b1000) != 0;
          final boolean lit = (primitiveId & 0b1) == 0;

          final int vertexCount = quad ? 4 : 3;

          for(final byte[] data : primitive.data()) {
            int primitivesOffset = 0;

            final int[] u = new int[vertexCount];
            final int[] v = new int[vertexCount];
            int clut = 0;
            for(int tmdVertexIndex = 0; tmdVertexIndex < vertexCount; tmdVertexIndex++) {
              u[tmdVertexIndex] = IoHelper.readUByte(data, primitivesOffset++);
              v[tmdVertexIndex] = IoHelper.readUByte(data, primitivesOffset++);

              if(tmdVertexIndex == 0) {
                clut = IoHelper.readUShort(data, primitivesOffset);
              }

              primitivesOffset += 2;
            }

            final int clutY = (clut & 0x3c0) >>> 6;

            //            final int colour = clutY * 16;
            //            out[v * tex.rect.w + u] = 0xff << 24 | colour << 16 | colour << 8 | colour;

            rasterizeTriangle(u[0], v[0], u[1], v[1], u[2], v[2], clutY, clutMap, tex.rect.w);

            if(quad) {
              rasterizeTriangle(u[1], v[1], u[2], v[2], u[3], v[3], clutY, clutMap, tex.rect.w);
            }

            if(shaded || !lit) {
              for(int tmdVertexIndex = 0; tmdVertexIndex < vertexCount; tmdVertexIndex++) {
                primitivesOffset += 4;
              }
            }

            for(int tmdVertexIndex = 0; tmdVertexIndex < vertexCount; tmdVertexIndex++) {
              if(lit && (gourad || tmdVertexIndex == 0)) {
                primitivesOffset += 2;
              }

              primitivesOffset += 2;
            }
          }
        }
      }
    }

    final int[] out = new int[tex.rect.w * tex.rect.h];
    final int[] newClutMap = new int[tex.rect.w * tex.rect.h];
    for(int i = 0; i < clutMap.length; i++) {
      final int x = i % tex.rect.w;
      final int y = i / tex.rect.w;

      newClutMap[i] = clutMap[i];

      // Fill untouched pixels with the CLUT from the nearest-ish pixel using the worst algorithm human fingers have ever typed
      int mult = 1;
      outer:
      while(clutMap[i] == -1 && mult < 30) {
        for(int nudgeX = -1 * mult; nudgeX < mult; nudgeX++) {
          for(int nudgeY = -1 * mult; nudgeY < mult; nudgeY++) {
            // If we're inside the bounds of the texture
            if(x + nudgeX >= 0 && y + nudgeY >= 0 && x + nudgeX < tex.rect.w && y + nudgeY < tex.rect.h) {
              // If we're on the border of the search square
              if(nudgeX == -1 * mult || nudgeY == -1 * mult || nudgeX == mult - 1 || nudgeY == mult - 1) {
                final int index = (y + nudgeY) * tex.rect.w + x + nudgeX;
                if(clutMap[index] != -1) {
                  newClutMap[i] = clutMap[index];
                  break outer;
                }
              }
            }
          }
        }

        mult++;
      }

      if(newClutMap[i] != -1) {
        out[i] = palettes[newClutMap[i]].getPixel(tex.getPixel(x, y), 0);
      }
    }

    VramTextureSingle.dumpToFile(tex.rect, out, path, true);
  }

  private static void rasterizeTriangle(final int vx0, final int vy0, int vx1, int vy1, int vx2, int vy2, final int c0, final int[] out, final int width) {
    final int area = orient2d(vx0, vy0, vx1, vy1, vx2, vy2);
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
    }

    /*boundingBox*/
    final int minX = Math.min(vx0, Math.min(vx1, vx2));
    final int minY = Math.min(vy0, Math.min(vy1, vy2));
    final int maxX = Math.max(vx0, Math.max(vx1, vx2));
    final int maxY = Math.max(vy0, Math.max(vy1, vy2));

    final int A01 = vy0 - vy1;
    final int B01 = vx1 - vx0;
    final int A12 = vy1 - vy2;
    final int B12 = vx2 - vx1;
    final int A20 = vy2 - vy0;
    final int B20 = vx0 - vx2;

    final int bias0 = isTopLeft(vx1, vy1, vx2, vy2) ? 0 : -1;
    final int bias1 = isTopLeft(vx2, vy2, vx0, vy0) ? 0 : -1;
    final int bias2 = isTopLeft(vx0, vy0, vx1, vy1) ? 0 : -1;

    int w0_row = orient2d(vx1, vy1, vx2, vy2, minX, minY);
    int w1_row = orient2d(vx2, vy2, vx0, vy0, minX, minY);
    int w2_row = orient2d(vx0, vy0, vx1, vy1, minX, minY);

    // Rasterize
    for(int y = minY; y < maxY; y++) {
      // Barycentric coordinates at start of row
      int w0 = w0_row;
      int w1 = w1_row;
      int w2 = w2_row;

      for(int x = minX; x < maxX; x++) {
        // If p is on or inside all edges, render pixel
        if((w0 + bias0 | w1 + bias1 | w2 + bias2) >= 0) {
          out[y * width + x] = c0;
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
