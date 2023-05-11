package legend.game.tmd;

import legend.core.IoHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.VramTexture;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable1c;
import legend.game.combat.environment.BattleLightStruct64;
import legend.game.types.Translucency;

import javax.annotation.Nullable;

import static legend.core.GameEngine.CPU;
import static legend.core.GameEngine.GPU;
import static legend.game.Scus94491BpeSegment.tmdGp0CommandId_1f8003ee;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zMax_1f8003cc;
import static legend.game.Scus94491BpeSegment.zMin;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;
import static legend.game.combat.Bttl_800c._800c6930;

public final class Renderer {
  private Renderer() { }

  public static void renderDobj2(final GsDOBJ2 dobj2, final boolean useSpecialTranslucency, final int ctmdFlag) {
    renderDobj2(dobj2, useSpecialTranslucency, ctmdFlag, null, null);
  }

  /**
   * @param useSpecialTranslucency Used in battle, some TMDs have translucency info in the upper 16 bits of their ID. Also enables backside culling.
   */
  public static void renderDobj2(final GsDOBJ2 dobj2, final boolean useSpecialTranslucency, final int ctmdFlag, @Nullable final VramTexture texture, @Nullable final VramTexture[] palettes) {
    final TmdObjTable1c objTable = dobj2.tmd_08;
    final SVECTOR[] vertices = objTable.vert_top_00;
    final SVECTOR[] normals = objTable.normal_top_08;

    // CTMD flag and scripted "uniform lighting" + transparency flag for STMDs in DEFFs
    final int specialFlags = ctmdFlag | ((dobj2.attribute_00 & 0x4000_0000) != 0 ? 0x12 : 0x0);

    //LAB_800da2bc
    for(final TmdObjTable1c.Primitive primitive : objTable.primitives_10) {
      renderTmdPrimitive(primitive, vertices, normals, useSpecialTranslucency, specialFlags, texture, palettes);
    }
  }

  public static void renderTmdPrimitive(final TmdObjTable1c.Primitive primitive, final SVECTOR[] vertices, final SVECTOR[] normals, final boolean useSpecialTranslucency, final int specialFlags, @Nullable final VramTexture texture, @Nullable final VramTexture[] palettes) {
    // Read type info from command ---
    final int command = (primitive.header() | specialFlags) & 0xff04_0000;
    final int primitiveId = command >>> 24;
    if((primitiveId >>> 5 & 0b11) != 1) {
      throw new RuntimeException("Unsupported primitive type");
    }

    final boolean ctmd = (specialFlags & 0x20) != 0;
    final boolean uniformLit = (specialFlags & 0x10) != 0;
    final boolean shaded = (command & 0x4_0000) != 0;
    final boolean quad = (primitiveId & 0b1000) != 0;
    final boolean textured = (primitiveId & 0b100) != 0;
    final boolean translucent = ((primitiveId & 0b10) != 0) || ((specialFlags & 0x2) != 0);
    final boolean lit = (primitiveId & 0b1) == 0;

    if(textured && shaded) {
      throw new RuntimeException("Invalid primitive type");
    }

    if(!textured && !lit) {
      throw new RuntimeException("Invalid primitive type");
    }

    //TODO need to figure out what this was being used for
    final long specialTrans = tmdGp0CommandId_1f8003ee.get();

    final int vertexCount = quad ? 4 : 3;
    // ---

    final Polygon poly = new Polygon(vertexCount);

    outer:
    for(final byte[] data : primitive.data()) {
      // Read data from TMD ---
      int primitivesOffset = 0;

      if(textured) {
        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          poly.vertices[vertexIndex].u = IoHelper.readUByte(data, primitivesOffset++);
          poly.vertices[vertexIndex].v = IoHelper.readUByte(data, primitivesOffset++);

          if(vertexIndex == 0) {
            poly.clut = IoHelper.readUShort(data, primitivesOffset);
          } else if(vertexIndex == 1) {
            poly.tpage = IoHelper.readUShort(data, primitivesOffset);
          }

          primitivesOffset += 2;
        }
      }

      if(shaded || !lit) {
        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          poly.vertices[vertexIndex].colour = IoHelper.readInt(data, primitivesOffset);
          primitivesOffset += 4;
        }
      } else if(!textured) {
        final int colour = IoHelper.readInt(data, primitivesOffset);
        primitivesOffset += 4;

        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          poly.vertices[vertexIndex].colour = colour;
        }
      }

      for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
        if(lit) {
          poly.vertices[vertexIndex].normalIndex = IoHelper.readUShort(data, primitivesOffset);
          primitivesOffset += 2;
        }

        poly.vertices[vertexIndex].vertexIndex = IoHelper.readUShort(data, primitivesOffset);
        primitivesOffset += 2;
      }
      // ---

      final GpuCommandPoly cmd = new GpuCommandPoly(vertexCount);

      if(texture != null) {
        cmd.texture(texture, palettes);
      }

      if(textured) {
        cmd.clut((poly.clut & 0b111111) * 16, poly.clut >>> 6);
        cmd.vramPos((poly.tpage & 0b1111) * 64, (poly.tpage & 0b10000) != 0 ? 256 : 0);
        cmd.bpp(Bpp.of(poly.tpage >>> 7 & 0b11));

        if(translucent) {
          cmd.translucent(Translucency.of(poly.tpage >>> 5 & 0b11));
        }
      }

      for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
        final SVECTOR vert = vertices[poly.vertices[vertexIndex].vertexIndex];
        CPU.MTC2(vert.getXY(), 0);
        CPU.MTC2(vert.getZ(),  1);
        CPU.COP2(0x18_0001L); // Perspective transform single

        if((int)CPU.CFC2(31) < 0) { // Errors
          continue outer;
        }

        final DVECTOR xy = new DVECTOR().setXY(CPU.MFC2(14));

        cmd.pos(vertexIndex, xy.getX(), xy.getY());

        if(textured) {
          cmd.uv(vertexIndex, poly.vertices[vertexIndex].u, poly.vertices[vertexIndex].v);
        }

        /* if(useSpecialTranslucency) {
          TODO Figure out how specialTrans is used
        } */

        // Back-face culling
        if(vertexIndex == 2) {
          CPU.COP2(0x140_0006L); // Normal clipping
          final long winding = CPU.MFC2(24);

          if(!translucent && winding <= 0 || translucent && winding == 0) {
            continue outer;
          }
        }
      }

      // Average Z
      if(quad) {
        CPU.COP2(0x168_002eL);
      } else {
        CPU.COP2(0x168_002dL);
      }

      final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());

      if(z < zMin) {
        continue;
      }

      if(textured && translucent && !lit && (ctmd || uniformLit)) {
        final BattleLightStruct64 bkLight = _800c6930;
        final int rbk = bkLight.colour_00.getX();
        final int gbk = bkLight.colour_00.getY();
        final int bbk = bkLight.colour_00.getZ();

        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          int rgb = poly.vertices[vertexIndex].colour;
          final int r = (((rgb & 0xff) * rbk >> 12) & 0xff);
          final int g = ((((rgb >>> 8) & 0xff) * gbk >> 12) & 0xff);
          final int b = ((((rgb >>> 16) & 0xff) * bbk >> 12) & 0xff);
          rgb = b << 16 | g << 8 | r;
          cmd.rgb(vertexIndex, rgb);
        }
      } else if(!textured || lit) {
        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          CPU.MTC2(poly.vertices[vertexIndex].colour, 6);

          final SVECTOR norm;
          if(vertexIndex < normals.length) {
            norm = normals[poly.vertices[vertexIndex].normalIndex];
          } else {
            norm = new SVECTOR();
          }

          CPU.MTC2(norm.getXY(), 0);
          CPU.MTC2(norm.getZ(), 1);
          CPU.COP2(0x108_041bL); // Normal colour colour single vector
          cmd.rgb(vertexIndex, (int)CPU.MFC2(22));
        }
      } else {
        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          cmd.rgb(vertexIndex, poly.vertices[vertexIndex].colour);
        }
      }

      if(translucent && (!textured || uniformLit)) {
        cmd.translucent(Translucency.of(tmdGp0Tpage_1f8003ec.get() >>> 5 & 0b11));
      }

      GPU.queueCommand(z, cmd);
    }
  }
}
