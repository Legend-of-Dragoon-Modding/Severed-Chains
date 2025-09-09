package legend.game.tmd;

import legend.core.IoHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gte.ModelPart10;
import legend.core.gte.TmdObjTable1c;
import legend.game.combat.Battle;
import legend.game.combat.environment.BattleLightStruct64;
import legend.game.types.Translucency;
import org.joml.Vector3f;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.game.Scus94491BpeSegment.tmdGp0CommandId_1f8003ee;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zMax_1f8003cc;
import static legend.game.Scus94491BpeSegment.zMin;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;

public final class Renderer {
  private Renderer() { }

  /**
   * @param useSpecialTranslucency Used in battle, some TMDs have translucency info in the upper 16 bits of their ID. Also enables backside culling.
   */
  public static void renderDobj2(final ModelPart10 dobj2, final boolean useSpecialTranslucency, final int ctmdFlag) {
    final TmdObjTable1c objTable = dobj2.tmd_08;
    final Vector3f[] vertices = objTable.vert_top_00;
    final Vector3f[] normals = objTable.normal_top_08;

    // CTMD flag and scripted "uniform lighting" + transparency flag for STMDs in DEFFs
    final int specialFlags = ctmdFlag | ((dobj2.attribute_00 & 0x4000_0000) != 0 ? 0x12 : 0x0);

    //LAB_800da2bc
    for(final TmdObjTable1c.Primitive primitive : objTable.primitives_10) {
      renderTmdPrimitive(primitive, vertices, normals, useSpecialTranslucency, specialFlags);
    }
  }

  public static void renderTmdPrimitive(final TmdObjTable1c.Primitive primitive, final Vector3f[] vertices, final Vector3f[] normals, final int attribute) {
    final int specialFlags = (attribute & 0x4000_0000) != 0 ? 0x12 : 0x0;
    renderTmdPrimitive(primitive, vertices, normals, false, specialFlags);
  }

  private static final Vector3f ZERO = new Vector3f();

  public static void renderTmdPrimitive(final TmdObjTable1c.Primitive primitive, final Vector3f[] vertices, final Vector3f[] normals, final boolean useSpecialTranslucency, final int specialFlags) {
    // Read type info from command ---
    final int command = (primitive.header() | specialFlags) & 0xff04_0000; // I can only find specialFlags getting set to the bits 0x32 so it probably does nothing here
    final int primitiveId = command >>> 24;
    if((primitiveId >>> 5 & 0b11) != 1) {
      throw new RuntimeException("Unsupported primitive type");
    }

    final boolean ctmd = (specialFlags & 0x20) != 0;
    final boolean uniformLit = (specialFlags & 0x10) != 0;
    final boolean shaded = (command & 0x4_0000) != 0;
    final boolean gourad = (primitiveId & 0b1_0000) != 0;
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
    final long specialTrans = tmdGp0CommandId_1f8003ee;

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
          if(gourad || vertexIndex == 0) {
            poly.vertices[vertexIndex].normalIndex = IoHelper.readUShort(data, primitivesOffset);
            primitivesOffset += 2;
          } else {
            poly.vertices[vertexIndex].normalIndex = poly.vertices[0].normalIndex;
          }
        }

        poly.vertices[vertexIndex].vertexIndex = IoHelper.readUShort(data, primitivesOffset);
        primitivesOffset += 2;
      }
      // ---

      final GpuCommandPoly cmd = new GpuCommandPoly(vertexCount);

      if(textured) {
        cmd.clut((poly.clut & 0b111111) * 16, poly.clut >>> 6);
        cmd.vramPos((poly.tpage & 0b1111) * 64, (poly.tpage & 0b10000) != 0 ? 256 : 0);
        cmd.bpp(Bpp.of(poly.tpage >>> 7 & 0b11));

        if(translucent) {
          cmd.translucent(Translucency.of(poly.tpage >>> 5 & 0b11));
        }
      }

      for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
        GTE.perspectiveTransform(vertices[poly.vertices[vertexIndex].vertexIndex]);

        if(GTE.hasError()) {
          continue outer;
        }

        cmd.pos(vertexIndex, GTE.getScreenX(2), GTE.getScreenY(2));

        if(textured) {
          cmd.uv(vertexIndex, poly.vertices[vertexIndex].u, poly.vertices[vertexIndex].v);
        }

        /* if(useSpecialTranslucency) {
          TODO Figure out how specialTrans is used
        } */

        // Back-face culling
        if(vertexIndex == 2) {
          final int winding = GTE.normalClipping();

          if(!translucent && winding <= 0 || translucent && winding == 0) {
            continue outer;
          }
        }
      }

      final float screenZ = quad ? GTE.averageZ4() : GTE.averageZ3();
      final float z = Math.min((screenZ + zOffset_1f8003e8) / (1 << zShift_1f8003c4), zMax_1f8003cc);

      if(z < zMin) {
        continue;
      }

      if(textured && translucent && !lit && (ctmd || uniformLit)) {
        final BattleLightStruct64 bkLight = ((Battle)currentEngineState_8004dd04)._800c6930;

        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          int rgb = poly.vertices[vertexIndex].colour;
          final int r = (int)((rgb        & 0xff) * bkLight.colour_00.x) & 0xff;
          final int g = (int)((rgb >>>  8 & 0xff) * bkLight.colour_00.y) & 0xff;
          final int b = (int)((rgb >>> 16 & 0xff) * bkLight.colour_00.z) & 0xff;
          rgb = b << 16 | g << 8 | r;
          cmd.rgb(vertexIndex, rgb);
        }
      } else if(!textured || lit) {
        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          if(poly.vertices[vertexIndex].normalIndex < normals.length) {
            cmd.rgb(vertexIndex, GTE.normalColour(normals[poly.vertices[vertexIndex].normalIndex], poly.vertices[vertexIndex].colour));
          } else {
            cmd.rgb(vertexIndex, GTE.normalColour(ZERO, poly.vertices[vertexIndex].colour));
          }
        }
      } else {
        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          cmd.rgb(vertexIndex, poly.vertices[vertexIndex].colour);
        }
      }

      if(translucent && (!textured || uniformLit)) {
        cmd.translucent(Translucency.of(tmdGp0Tpage_1f8003ec >>> 5 & 0b11));
      }

      GPU.queueCommand(z, cmd);
    }
  }
}
