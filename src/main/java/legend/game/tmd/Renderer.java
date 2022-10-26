package legend.game.tmd;

import legend.core.gte.GsDOBJ2;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable;
import legend.core.memory.types.UnboundedArrayRef;
import legend.game.Scus94491BpeSegment;
import legend.game.types.GsOT_TAG;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static legend.core.Hardware.CPU;
import static legend.core.Hardware.MEMORY;
import static legend.game.Scus94491BpeSegment._1f8003ec;
import static legend.game.Scus94491BpeSegment.gpuPacketAddr_1f8003d8;
import static legend.game.Scus94491BpeSegment.tags_1f8003d0;
import static legend.game.Scus94491BpeSegment.zMax_1f8003cc;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;

public class Renderer {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Renderer.class);

  /**
   * @param useSpecialTranslucency Used in battle, some TMDs have translucency info in the upper 16 bits of their ID. Also enables backside culling.
   */
  public static void renderDobj2(final GsDOBJ2 dobj2, final boolean useSpecialTranslucency) {
    final TmdObjTable objTable = dobj2.tmd_08.deref();
    final UnboundedArrayRef<SVECTOR> vertices = objTable.vert_top_00.deref();
    final long normals = objTable.normal_top_08.get();
    long primitives = objTable.primitives_10.getPointer();
    long count = objTable.n_primitive_14.get();

    //LAB_800da2bc
    while(count > 0) {
      final long length = MEMORY.ref(2, primitives).get();
      count -= length;

      if(count < 0) {
        LOGGER.warn("DOBJ2 count less than 0! %d", count);
      }

      if(!Scus94491BpeSegment.OLD_RENDERER) {
        primitives = renderTmdPrimitive(primitives, vertices, normals, (int)length, useSpecialTranslucency);
      } else {
        final long command = MEMORY.ref(4, primitives).get(0xff04_0000L);

        if(command == 0x3000_0000L) {
          // Tri, shading, solid
          // 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13
          //             r  g  b     n  n  v  v  n  n  v  v  n  n  v  v
          primitives = renderPrimitive3000(primitives, vertices, normals, length);
        } else if(command == 0x3004_0000L) {
          // Tri, shading, gradation
          // 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17 18 19 1a 1b
          //             r  g  b     r  g  b     r  g  b     n  n  v  v  n  n  v  v  n  n  v  v
          primitives = renderPrimitive3004(primitives, vertices, normals, length);
        } else if(command == 0x3200_0000L) {
          // Tri, shading, solid, alpha
          primitives = renderPrimitive3200(primitives, vertices, normals);
        } else if(command == 0x3204_0000L) {
          // Tri, shading, gradation, alpha
          // 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17 18 19 1a 1b
          //             r  g  b  c  r  g  b  c  r  g  b  c  n  n  v  v  n  n  v  v  n  n  v  v
          primitives = renderPrimitive3204(primitives, vertices, normals, length);
        } else if(command == 0x3400_0000L) {
          // Tri, shading, textured
          // 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17 18 19 1a 1b
          //             u  v  cl ut u  v  tp ge u  v        n  n  v  v  n  n  v  v  n  n  v  v
          primitives = renderPrimitive34(primitives, vertices, normals, length);
        } else if(command == 0x3500_0000L) {
          // Tri, shading, textured, no lighting
          primitives = renderPrimitive35(primitives, vertices, length);
        } else if(command == 0x3600_0000L) {
          // Tri, shading, textured, alpha
          // 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17 18 19 1a 1b
          //             u  v  cl ut u  v  tp ge u  v        n  n  v  v  n  n  v  v  n  n  v  v
          primitives = renderPrimitive36(primitives, vertices, normals, length);
        } else if(command == 0x3700_0000L) {
          // Tri, shading, textured, alpha, no lighting
          // 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17 18 19 1a 1b 1c 1d 1e 1f 20 21 22 23
          //             u  v  cl ut u  v  tp ge u  v        r  g  b     r  g  b     r  g  b     v  v  v  v  v  v
          primitives = renderPrimitive37(primitives, vertices, length);
        } else if(command == 0x3800_0000L) {
          // Quad, shading, solid
          // 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17
          //             r  g  b     n  n  v  v  n  n  v  v  n  n  v  v  n  n  v  v
          primitives = renderPrimitive3800(primitives, vertices, normals, length);
        } else if(command == 0x3804_0000L) {
          // Quad, shading, gradation
          // 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17 18 19 1a 1b 1c 1d 1e 1f 20 21 22 23
          //             r  g  b     r  g  b     r  g  b     r  g  b     n  n  v  v  n  n  v  v  n  n  v  v  n  n  v  v
          primitives = renderPrimitive3804(primitives, vertices, normals, length);
        } else if(command == 0x3a00_0000L) {
          // Quad, shading, solid, alpha
          // 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17
          //             r  g  b  c  n  n  v  v  n  n  v  v  n  n  v  v  n  n  v  v
          primitives = renderPrimitive3a00(primitives, vertices, normals, length);
        } else if(command == 0x3a04_0000L) {
          // Quad, shading, gradation, alpha
          // 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17 18 19 1a 1b 1c 1d 1e 1f 20 21 22 23
          //             r  g  b  c  r  g  b  c  r  g  b  c  r  g  b  c  n  n  v  v  n  n  v  v  n  n  v  v  n  n  v  v
          primitives = renderPrimitive3a04(primitives, vertices, normals, length);
        } else if(command == 0x3c00_0000L) {
          // Quad, shading, textured
          // 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17 18 19 1a 1b 1c 1d 1e 1f 20 21 22 23
          //             u  v  cl ut u  v  tp ge u  v        u  v        n  n  v  v  n  n  v  v  n  n  v  v  n  n  v  v
          primitives = renderPrimitive3c(primitives, vertices, normals, length);
        } else if(command == 0x3d00_0000L) {
          // Quad, shading, textured, no lighting
          // 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17 18 19 1a 1b 1c 1d 1e 1f 20 21 22 23 24 25 26 27 28 29 2a 2b
          //             u  v  cl ut u  v  tp ge u  v        u  v        r  g  b     r  g  b     r  g  b     r  g  b     v  v  v  v  v  v  v  v
          primitives = renderPrimitive3d(primitives, vertices, length);
        } else if(command == 0x3e00_0000L) {
          // Quad, shading, textured, alpha
          // 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17 18 19 1a 1b 1c 1d 1e 1f 20 21 22 23
          //             u  v  cl ut u  v  tp ge u  v        u  v        n  n  v  v  n  n  v  v  n  n  v  v  n  n  v  v
          primitives = renderPrimitive3e(primitives, vertices, normals, length);
        } else if(command == 0x3f00_0000L) {
          // Quad, shading, textured, alpha, no lighting
          // 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17 18 19 1a 1b 1c 1d 1e 1f 20 21 22 23 24 25 26 27 28 29 2a 2b
          //             u  v  cl ut u  v  tp ge u  v        u  v        r  g  b  c  r  g  b  c  r  g  b  c  r  g  b  c  v  v  v  v  v  v  v  v
          primitives = renderPrimitive3f(primitives, vertices, length);
        }
      }
    }
  }

  public static long renderTmdPrimitive(final long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final int count, final boolean useSpecialTranslucency) {
    // Read type info from command ---
    final long command = MEMORY.ref(4, primitives).get(0xff04_0000L);
    final int primitiveId = (int)(command >>> 24);

    if((primitiveId >>> 5 & 0b11) != 1) {
      throw new RuntimeException("Unsupported primitive type");
    }

    final boolean gradated = (command & 0x4_0000) != 0;
    final boolean shaded = (primitiveId & 0b1_0000) != 0;
    final boolean quad = (primitiveId & 0b1000) != 0;
    final boolean textured = (primitiveId & 0b100) != 0;
    final boolean translucent = (primitiveId & 0b10) != 0;
    final boolean lit = (primitiveId & 0b1) == 0;

    if(textured && gradated) {
      throw new RuntimeException("Invalid primitive type");
    }

    if(!textured && !lit) {
      throw new RuntimeException("Invalid primitive type");
    }

    final long specialTrans = (int)_1f8003ec.get() >> 16;

    final int vertexCount = quad ? 4 : 3;
    // ---

    // The number of words per vertex in the GP0 command
    int components = 1;
    if(shaded) { // Has colour info
      components++;
    }

    if(textured) { // Has texture info
      components++;
    }

    // The length in bytes of each vertex in the GP0 packet
    final int packetPitch = components * 4;

    // The number of words total in the GP0 command
    int gp0CommandCount = components * vertexCount;

    int gp0Command = 0x20;
    if(shaded) {
      gp0Command |= 0x10;
    } else {
      gp0CommandCount++; // Unshaded commands have an extra word at the start
    }

    if(quad) {
      gp0Command |= 0x8;
    }

    if(textured) {
      gp0Command |= 0x4;
    }

    if(translucent) {
      gp0Command |= 0x2;
    }

    if(useSpecialTranslucency) {
      gp0Command |= specialTrans << 1;
    }

    final int packetLength = (gp0CommandCount + 1) * 4;

    long packet = gpuPacketAddr_1f8003d8.get();

    final Polygon poly = new Polygon(vertexCount);
    long readIndex = primitives;

    outer:
    for(int i = 0; i < count; i++) {
      // Read data from TMD ---
      readIndex += 4;

      if(textured) {
        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          poly.vertices[vertexIndex].u = MEMORY.get(readIndex++);
          poly.vertices[vertexIndex].v = MEMORY.get(readIndex++);

          if(vertexIndex == 0) {
            poly.clut = (int)MEMORY.get(readIndex, 2);
          } else if(vertexIndex == 1) {
            poly.tpage = (int)MEMORY.get(readIndex, 2);
          }

          readIndex += 2;
        }
      }

      if(gradated || !lit) {
        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          poly.vertices[vertexIndex].colour = (int)MEMORY.get(readIndex, 4);
          readIndex += 4;
        }
      } else if(!textured) {
        final int colour = (int)MEMORY.get(readIndex, 4);
        readIndex += 4;

        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          poly.vertices[vertexIndex].colour = colour;
        }
      }

      for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
        if(lit) {
          poly.vertices[vertexIndex].normalIndex = (int)MEMORY.get(readIndex, 2);
          readIndex += 2;
        }

        poly.vertices[vertexIndex].vertexIndex = (int)MEMORY.get(readIndex, 2);
        readIndex += 2;
      }

      readIndex = readIndex + 3 & 0xffff_fffcL; // 4-byte-align
      // ---

      for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
        final SVECTOR vert = vertices.get(poly.vertices[vertexIndex].vertexIndex);
        CPU.MTC2(vert.getXY(), 0);
        CPU.MTC2(vert.getZ(),  1);
        CPU.COP2(0x18_0001L); // Perspective transform single

        if((int)CPU.CFC2(31) < 0) { // Errors
          continue outer;
        }

        MEMORY.set(packet + 0x8L + vertexIndex * packetPitch, 4, CPU.MFC2(14));

        if(textured) {
          final long index = packet + 0xcL + vertexIndex * packetPitch;

          MEMORY.set(index,     (byte)poly.vertices[vertexIndex].u);
          MEMORY.set(index + 1, (byte)poly.vertices[vertexIndex].v);

          if(vertexIndex == 0) {
            MEMORY.set(index + 2, 2, poly.clut);
          } else if(vertexIndex == 1) {
            MEMORY.set(index + 2, 2, poly.tpage);
          }
        }

        // Back-face culling
        if(useSpecialTranslucency) {
          if(vertexIndex == 2) {
            CPU.COP2(0x140_0006L); // Normal clipping
            final long winding = CPU.MFC2(24);

            if(!translucent && winding <= 0 || translucent && winding == 0) {
              continue outer;
            }
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

      if(textured && !lit) {
        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          MEMORY.set(packet + 0x4L + vertexIndex * packetPitch, 4, poly.vertices[vertexIndex].colour);
        }
      } else {
        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          CPU.MTC2(poly.vertices[vertexIndex].colour, 6);
          final SVECTOR norm = MEMORY.ref(2, normals + poly.vertices[vertexIndex].normalIndex * 0x8L, SVECTOR::new);
          CPU.MTC2(norm.getXY(), 0);
          CPU.MTC2(norm.getZ(),  1);
          CPU.COP2(0x108_041bL); // Normal colour colour single vector
          MEMORY.set(packet + 0x4L + vertexIndex * packetPitch, 4, CPU.MFC2(22));
        }
      }

      MEMORY.set(packet + 0x7L, (byte)gp0Command);

      final GsOT_TAG tag = tags_1f8003d0.deref().get(z);
      MEMORY.set(packet, 4, gp0CommandCount << 24 | tag.p.get());
      tag.set(packet & 0xff_ffffL);
      packet += packetLength;

      if(translucent && !textured) {
        MEMORY.set(packet, 4, 0x100_0000L | tag.p.get());
        MEMORY.set(packet + 4, 4, 0xe100_021fL | _1f8003ec.get() & 0x9ffL);
        tag.set(packet & 0xff_ffffL);
        packet += 0x8L;
      }
    }

    gpuPacketAddr_1f8003d8.setu(packet);
    return readIndex;
  }

  private static long renderPrimitive3000(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    long packet = gpuPacketAddr_1f8003d8.get();

    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x0aL).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x0eL).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x12L).get());
      CPU.MTC2(vert0.getXY(), 0); // VXY0
      CPU.MTC2(vert0.getZ(),  1); // VZ0
      CPU.MTC2(vert1.getXY(), 2); // VXY1
      CPU.MTC2(vert1.getZ(),  3); // VZ1
      CPU.MTC2(vert2.getXY(), 4); // VXY2
      CPU.MTC2(vert2.getZ(),  5); // VZ2
      CPU.COP2(0x28_0030L); // Perspective transform triple

      if((int)CPU.CFC2(31) >= 0) { // No errors
        CPU.COP2(0x140_0006L); // Normal clipping

        if((int)CPU.MFC2(24) > 0) { // Is visible
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12)); // Screen XY0
          MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(13)); // Screen XY1
          MEMORY.ref(4, packet).offset(0x18L).setu(CPU.MFC2(14)); // Screen XY2

          if(MEMORY.ref(2, packet).offset(0x08L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x10L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x18L).getSigned() >= -0xc0) {
            //LAB_800dafb8
            if(MEMORY.ref(2, packet).offset(0x0aL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x12L).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x1aL).getSigned() >= -0x80) {
              //LAB_800daff4
              if(MEMORY.ref(2, packet).offset(0x08L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x10L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x18L).getSigned() <= 0xc0) {
                //LAB_800db030
                if(MEMORY.ref(2, packet).offset(0x0aL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x12L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x1aL).getSigned() <= 0x80) {
                  //LAB_800db06c
                  CPU.COP2(0x158_002dL); // Average Z

                  final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());

                  //LAB_800db09c
                  final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x08L).get() * 0x8L;
                  final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x0cL).get() * 0x8L;
                  final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x10L).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1); // VZ0
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 2); // VXY1
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 3); // VZ1
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 4); // VXY2
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 5); // VZ2
                  CPU.MTC2(MEMORY.ref(4, primitives).offset(0x4L).get(), 6); // RGBC
                  CPU.COP2(0x118_043fL); // Normal colour colour triple vector
                  MEMORY.ref(4, packet).offset(0x04L).setu(CPU.MFC2(20)); // RGB0
                  MEMORY.ref(4, packet).offset(0x0cL).setu(CPU.MFC2(21)); // RGB1
                  MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(22)); // RGB2

                  MEMORY.ref(1, packet).offset(0x03L).setu(0x6L);
                  MEMORY.ref(1, packet).offset(0x07L).setu(0x30L);

                  final GsOT_TAG tag = tags_1f8003d0.deref().get(z);
                  MEMORY.ref(4, packet).setu(0x600_0000L | tag.p.get());
                  tag.set(packet & 0xff_ffffL);

                  packet += 0x1cL;
                }
              }
            }
          }
        }
      }

      //LAB_800db120
      primitives += 0x14L;
    }

    //LAB_800db128
    gpuPacketAddr_1f8003d8.setu(packet);
    return primitives;
  }

  private static long renderPrimitive3004(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    long packet = gpuPacketAddr_1f8003d8.get();

    //LAB_800dbb60
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x12L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x16L).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1aL).get());
      CPU.MTC2(vert0.getXY(), 0); // VXY0
      CPU.MTC2(vert0.getZ(),  1); // VZ0
      CPU.MTC2(vert1.getXY(), 2); // VXY1
      CPU.MTC2(vert1.getZ(),  3); // VZ1
      CPU.MTC2(vert2.getXY(), 4); // VXY2
      CPU.MTC2(vert2.getZ(),  5); // VZ2
      CPU.COP2(0x28_0030L); // Perspective transform triple

      if((int)CPU.CFC2(31) >= 0) { // No errors
        CPU.COP2(0x140_0006L); // Normal clipping

        if((int)CPU.MFC2(24) > 0) { // Is visible
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12)); // Screen XY0
          MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(13)); // Screen XY1
          MEMORY.ref(4, packet).offset(0x18L).setu(CPU.MFC2(14)); // Screen XY2

          if(MEMORY.ref(2, packet).offset(0x08L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x10L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x18L).getSigned() >= -0xc0) {
            //LAB_800dbc18
            if(MEMORY.ref(2, packet).offset(0x0aL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x12L).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x1aL).getSigned() >= -0x80) {
              //LAB_800dbc54
              if(MEMORY.ref(2, packet).offset(0x08L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x10L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x18L).getSigned() <= 0xc0) {
                //LAB_800dbc90
                if(MEMORY.ref(2, packet).offset(0x0aL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x12L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x1aL).getSigned() <= 0x80) {
                  //LAB_800dbccc
                  CPU.COP2(0x158_002dL); // Average Z

                  final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());

                  //LAB_800dbcfc
                  final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x10L).get() * 0x8L;
                  final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x14L).get() * 0x8L;
                  final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x18L).get() * 0x8L;

                  CPU.MTC2(MEMORY.ref(4, primitives).offset(0x4L).get(), 6); // RGBC
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1); // VZ0
                  CPU.COP2(0x108_041bL); // Normal colour colour single vector
                  MEMORY.ref(4, packet).offset(0x4L).setu(CPU.MFC2(22)); // RGB2

                  CPU.MTC2(MEMORY.ref(4, primitives).offset(0x8L).get(), 6); // RGBC
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 1); // VZ0
                  CPU.COP2(0x108_041bL); // Normal colour colour single vector
                  MEMORY.ref(4, packet).offset(0xcL).setu(CPU.MFC2(22)); // RGB2

                  CPU.MTC2(MEMORY.ref(4, primitives).offset(0xcL).get(), 6); // RGBC
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 1); // VZ0
                  CPU.COP2(0x108_041bL); // Normal colour colour single vector
                  MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(22)); // RGB2

                  MEMORY.ref(1, packet).offset(0x03L).setu(0x6L);
                  MEMORY.ref(1, packet).offset(0x07L).setu(0x30L);

                  final GsOT_TAG tag = tags_1f8003d0.deref().get(z);
                  MEMORY.ref(4, packet).setu(0x600_0000L | tag.p.get());
                  tag.set(packet & 0xff_ffffL);

                  packet += 0x1cL;
                }
              }
            }
          }
        }
      }

      //LAB_800dbdbc
      primitives += 0x1cL;
    }

    //LAB_800dbdc8
    gpuPacketAddr_1f8003d8.setu(packet);
    return primitives;
  }

  private static long renderPrimitive3200(final long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals) {
    assert false;
    return 0;
  }

  private static long renderPrimitive3204(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    long packet = gpuPacketAddr_1f8003d8.get();

    //LAB_800dd530
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x12L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x16L).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1aL).get());
      CPU.MTC2(vert0.getXY(), 0); // VXY0
      CPU.MTC2(vert0.getZ(),  1); // VZ0
      CPU.MTC2(vert1.getXY(), 2); // VXY1
      CPU.MTC2(vert1.getZ(),  3); // VZ1
      CPU.MTC2(vert2.getXY(), 4); // VXY2
      CPU.MTC2(vert2.getZ(),  5); // VZ2
      CPU.COP2(0x28_0030L); // Perspective transform triple

      if((int)CPU.CFC2(31) >= 0) { // No errors
        CPU.COP2(0x140_0006L); // Normal clipping

        if(CPU.MFC2(24) != 0) { // Is visible
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12)); // Screen XY0
          MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(13)); // Screen XY1
          MEMORY.ref(4, packet).offset(0x18L).setu(CPU.MFC2(14)); // Screen XY2

          if(MEMORY.ref(2, packet).offset(0x8L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x10L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x18L).getSigned() >= -0xc0) {
            //LAB_800dd5e8
            if(MEMORY.ref(2, packet).offset(0xaL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x12L).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x1aL).getSigned() >= -0x80) {
              //LAB_800dd624
              if(MEMORY.ref(2, packet).offset(0x8L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x10L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x18L).getSigned() <= 0xc0) {
                //LAB_800dd660
                if(MEMORY.ref(2, packet).offset(0xaL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x12L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x1aL).getSigned() <= 0x80) {
                  //LAB_800dd69c
                  CPU.COP2(0x158_002dL); // Average Z

                  final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());

                  //LAB_800dd6cc
                  final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x10L).get() * 0x8L;
                  final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x14L).get() * 0x8L;
                  final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x18L).get() * 0x8L;

                  CPU.MTC2(MEMORY.ref(4, primitives).offset(0x4L).get(), 6); // RGBC
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1); // VZ0
                  CPU.COP2(0x108_041bL); // Normal colour colour triple vector
                  MEMORY.ref(4, packet).offset(0x4L).setu(CPU.MFC2(22)); // RGB2

                  CPU.MTC2(MEMORY.ref(4, primitives).offset(0x8L).get(), 6); // RGBC
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 1); // VZ0
                  CPU.COP2(0x108_041bL); // Normal colour colour triple vector
                  MEMORY.ref(4, packet).offset(0xcL).setu(CPU.MFC2(22)); // RGB2

                  CPU.MTC2(MEMORY.ref(4, primitives).offset(0xcL).get(), 6); // RGBC
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 1); // VZ0
                  CPU.COP2(0x108_041bL); // Normal colour colour triple vector
                  MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(22)); // RGB2

                  MEMORY.ref(1, packet).offset(0x03L).setu(0x6L);
                  MEMORY.ref(1, packet).offset(0x07L).setu(0x32L);

                  final GsOT_TAG tag = tags_1f8003d0.deref().get(z);
                  MEMORY.ref(4, packet).offset(0x0L).setu(0x600_0000L | tag.p.get());
                  tag.set(packet & 0xff_ffffL);
                  packet = packet + 0x1cL;

                  MEMORY.ref(4, packet).offset(0x0L).setu(0x100_0000L | tag.p.get());
                  MEMORY.ref(1, packet).offset(0x3L).setu(0x1L);
                  MEMORY.ref(4, packet).offset(0x4L).setu(0xe100_021fL | _1f8003ec.get() & 0x9ffL);
                  tag.set(packet & 0xff_ffffL);
                  packet = packet + 0x8L;
                }
              }
            }
          }
        }
      }

      //LAB_800dd7d8
      primitives = primitives + 0x1cL;
    }

    //LAB_800dd7e4
    gpuPacketAddr_1f8003d8.setu(packet);
    return primitives;
  }

  private static long renderPrimitive34(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    long packet = gpuPacketAddr_1f8003d8.get();

    CPU.MTC2(0x80_8080L, 6); // RGBC

    //LAB_800db52c
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x12L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x16L).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1aL).get());
      CPU.MTC2(vert0.getXY(), 0);
      CPU.MTC2(vert0.getZ(),  1);
      CPU.MTC2(vert1.getXY(), 2);
      CPU.MTC2(vert1.getZ(),  3);
      CPU.MTC2(vert2.getXY(), 4);
      CPU.MTC2(vert2.getZ(),  5);
      CPU.COP2(0x28_0030L); // Perspective transform triple
      MEMORY.ref(4, packet).offset(0x0cL).setu(MEMORY.ref(4, primitives).offset(0x4L).get());
      MEMORY.ref(4, packet).offset(0x18L).setu(MEMORY.ref(4, primitives).offset(0x8L).get());
      MEMORY.ref(4, packet).offset(0x24L).setu(MEMORY.ref(4, primitives).offset(0xcL).get());

      if((int)CPU.CFC2(31) >= 0) { // No errors
        CPU.COP2(0x140_0006L); // Normal clipping

        if((int)CPU.MFC2(24) > 0) { // Is visible
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12)); // Screen XY0
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13)); // Screen XY1
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14)); // Screen XY2

          if(MEMORY.ref(2, packet).offset(0x8L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x14L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() >= -0xc0) {
            //LAB_800db604
            if(MEMORY.ref(2, packet).offset(0xaL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x16L).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() >= -0x80) {
              //LAB_800db640
              if(MEMORY.ref(2, packet).offset(0x8L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x14L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() <= 0xc0) {
                //LAB_800db67c
                if(MEMORY.ref(2, packet).offset(0xaL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x16L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() <= 0x80) {
                  //LAB_800db6b8
                  CPU.COP2(0x158_002dL); // Average Z

                  final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());

                  //LAB_800db6e8
                  final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x10L).get() * 0x8L;
                  final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x14L).get() * 0x8L;
                  final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x18L).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1); // VZ0
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 2); // VXY1
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 3); // VZ1
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 4); // VXY2
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 5); // VZ2
                  CPU.COP2(0x118_043fL); // Normal colour colour triple vector
                  MEMORY.ref(4, packet).offset(0x04L).setu(CPU.MFC2(20)); // RGB0
                  MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(21)); // RGB1
                  MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(22)); // RGB2

                  MEMORY.ref(1, packet).offset(0x3L).setu(0x9L);
                  MEMORY.ref(1, packet).offset(0x7L).setu(0x34L);

                  final GsOT_TAG tag = tags_1f8003d0.deref().get(z);
                  MEMORY.ref(4, packet).setu(0x900_0000L | tag.p.get());
                  tag.set(packet & 0xff_ffffL);
                  packet += 0x28L;
                }
              }
            }
          }
        }
      }

      //LAB_800db764
      primitives += 0x1cL;
    }

    //LAB_800db770
    gpuPacketAddr_1f8003d8.setu(packet);
    return primitives;
  }

  private static long renderPrimitive35(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long count) {
    long packet = gpuPacketAddr_1f8003d8.get();

    //LAB_800caa18
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1cL).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1eL).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x20L).get());
      CPU.MTC2(vert0.getXY(), 0);
      CPU.MTC2(vert0.getZ(),  1);
      CPU.MTC2(vert1.getXY(), 2);
      CPU.MTC2(vert1.getZ(),  3);
      CPU.MTC2(vert2.getXY(), 4);
      CPU.MTC2(vert2.getZ(),  5);
      CPU.COP2(0x28_0030L);

      MEMORY.ref(4, packet).offset(0x0cL).setu(MEMORY.ref(4, primitives).offset(0x4L).get());
      MEMORY.ref(4, packet).offset(0x18L).setu(MEMORY.ref(4, primitives).offset(0x8L).get());
      MEMORY.ref(4, packet).offset(0x24L).setu(MEMORY.ref(4, primitives).offset(0xcL).get());

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x140_0006L);

        if((int)CPU.MFC2(24) > 0) {
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12));
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14));
          MEMORY.ref(1, packet).offset(0x03L).setu(0x9L);
          MEMORY.ref(4, packet).offset(0x04L).setu(0x3480_8080L);
          final long t3 = CPU.CFC2(31);

          if(MEMORY.ref(2, packet).offset(0x8L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x14L).getSigned() >= -0xcL || MEMORY.ref(2, packet).offset(0x20L).getSigned() >= -0xc0) {
            //LAB_800cab10
            if(MEMORY.ref(2, packet).offset(0xaL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x16L).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() >= -0x80) {
              //LAB_800cab4c
              if(MEMORY.ref(2, packet).offset(0x8L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x14L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() <= 0xc0) {
                //LAB_800cab88
                if(MEMORY.ref(2, packet).offset(0xaL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x16L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() <= 0x80) {
                  //LAB_800cabc4
                  CPU.COP2(0x158_002dL);

                  final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());

                  MEMORY.ref(1, packet).offset(0x04L).setu(MEMORY.ref(1, primitives).offset(0x10L).get());
                  MEMORY.ref(1, packet).offset(0x05L).setu(MEMORY.ref(1, primitives).offset(0x11L).get());
                  MEMORY.ref(1, packet).offset(0x06L).setu(MEMORY.ref(1, primitives).offset(0x12L).get());
                  MEMORY.ref(1, packet).offset(0x10L).setu(MEMORY.ref(1, primitives).offset(0x14L).get());
                  MEMORY.ref(1, packet).offset(0x11L).setu(MEMORY.ref(1, primitives).offset(0x15L).get());
                  MEMORY.ref(1, packet).offset(0x12L).setu(MEMORY.ref(1, primitives).offset(0x16L).get());
                  MEMORY.ref(1, packet).offset(0x1cL).setu(MEMORY.ref(1, primitives).offset(0x18L).get());
                  MEMORY.ref(1, packet).offset(0x1dL).setu(MEMORY.ref(1, primitives).offset(0x19L).get());
                  MEMORY.ref(1, packet).offset(0x1eL).setu(MEMORY.ref(1, primitives).offset(0x1aL).get());

                  final GsOT_TAG tag = tags_1f8003d0.deref().get(z);
                  MEMORY.ref(4, packet).offset(0x0L).setu(0x900_0000L | tag.p.get());
                  tag.set(packet & 0xff_ffffL);
                  packet += 0x28L;
                }
              }
            }
          }
        }
      }

      //LAB_800cac88
      primitives += 0x24L;
    }

    //LAB_800cac94
    gpuPacketAddr_1f8003d8.setu(packet);
    return primitives;
  }

  private static long renderPrimitive36(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    long packet = gpuPacketAddr_1f8003d8.get();

    CPU.MTC2(0x80_8080L, 6); // RGBC

    //LAB_800dc82c
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x12L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x16L).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1aL).get());
      CPU.MTC2(vert0.getXY(), 0); // VXY0
      CPU.MTC2(vert0.getZ(),  1); // VZ0
      CPU.MTC2(vert1.getXY(), 2); // VXY1
      CPU.MTC2(vert1.getZ(),  3); // VZ1
      CPU.MTC2(vert2.getXY(), 4); // VXY2
      CPU.MTC2(vert2.getZ(),  5); // VZ2
      CPU.COP2(0x28_0030L); // Perspective transform triple

      MEMORY.ref(4, packet).offset(0x0cL).setu(MEMORY.ref(4, primitives).offset(0x4L).get());
      MEMORY.ref(4, packet).offset(0x18L).setu(MEMORY.ref(4, primitives).offset(0x8L).get());
      MEMORY.ref(4, packet).offset(0x24L).setu(MEMORY.ref(4, primitives).offset(0xcL).get());

      if((int)CPU.CFC2(31) >= 0) { // No errors
        CPU.COP2(0x140_0006L); // Normal clipping

        if(CPU.MFC2(24) != 0) { // Is visible
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12)); // Screen XY0
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13)); // Screen XY1
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14)); // Screen XY2

          if(MEMORY.ref(2, packet).offset(0x8L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x14L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() >= -0xc0) {
            //LAB_800dc904
            if(MEMORY.ref(2, packet).offset(0xaL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x16L).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() >= -0x80) {
              //LAB_800dc940
              if(MEMORY.ref(2, packet).offset(0x8L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x14L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() <= 0xc0) {
                //LAB_800dc97c
                if(MEMORY.ref(2, packet).offset(0xaL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x16L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() <= 0x80) {
                  //LAB_800dc9b8
                  CPU.COP2(0x158_002dL); // Average Z

                  final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());

                  //LAB_800dc9e8
                  final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x10L).get() * 0x8L;
                  final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x14L).get() * 0x8L;
                  final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x18L).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1); // VZ0
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 2); // VXY1
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 3); // VZ1
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 4); // VXY2
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 5); // VZ2
                  CPU.COP2(0x118_043fL); // Normal colour colour triple vector
                  MEMORY.ref(4, packet).offset(0x04L).setu(CPU.MFC2(20)); // RGB0
                  MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(21)); // RGB1
                  MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(22)); // RGB2

                  MEMORY.ref(1, packet).offset(0x3L).setu(0x9L);
                  MEMORY.ref(1, packet).offset(0x7L).setu(0x36L);

                  final GsOT_TAG tag = tags_1f8003d0.deref().get(z);
                  MEMORY.ref(4, packet).offset(0x0L).setu(0x900_0000L | tag.p.get());
                  tag.set(packet & 0xff_ffffL);

                  packet += 0x28L;
                }
              }
            }
          }
        }
      }

      //LAB_800dca64
      primitives += 0x1cL;
    }

    //LAB_800dca70
    gpuPacketAddr_1f8003d8.setu(packet);
    return primitives;
  }

  private static long renderPrimitive37(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long count) {
    long packet = gpuPacketAddr_1f8003d8.get();

    //LAB_800ddbd4
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1cL).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1eL).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x20L).get());
      CPU.MTC2(vert0.getXY(), 0); // VXY0
      CPU.MTC2(vert0.getZ(),  1); // VZ0
      CPU.MTC2(vert1.getXY(), 2); // VXY1
      CPU.MTC2(vert1.getZ(),  3); // VZ1
      CPU.MTC2(vert2.getXY(), 4); // VXY2
      CPU.MTC2(vert2.getZ(),  5); // VZ2
      CPU.COP2(0x28_0030L); // Perspective transform triple
      MEMORY.ref(4, packet).offset(0x0cL).setu(MEMORY.ref(4, primitives).offset(0x4L).get());
      MEMORY.ref(4, packet).offset(0x18L).setu(MEMORY.ref(4, primitives).offset(0x8L).get());
      MEMORY.ref(4, packet).offset(0x24L).setu(MEMORY.ref(4, primitives).offset(0xcL).get());

      if((int)CPU.CFC2(31) >= 0) { // No errors
        CPU.COP2(0x140_0006L); // Normal clip

        if(CPU.MFC2(24) != 0) { // Is visible
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12)); // Screen XY0
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13)); // Screen XY1
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14)); // Screen XY2

          if((int)CPU.CFC2(31) >= 0) { // No errors
            if(MEMORY.ref(2, packet).offset(0x8L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x14L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() >= -0xc0) {
              //LAB_800ddcd0
              if(MEMORY.ref(2, packet).offset(0xaL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x16L).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() >= -0x80) {
                //LAB_800ddd0c
                if(MEMORY.ref(2, packet).offset(0x8L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x14L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() <= 0xc0) {
                  //LAB_800ddd48
                  if(MEMORY.ref(2, packet).offset(0xaL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x16L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() <= 0x80) {
                    //LAB_800ddd84
                    MEMORY.ref(1, packet).offset(0x04L).setu(MEMORY.ref(1, primitives).offset(0x10L).get());
                    MEMORY.ref(1, packet).offset(0x05L).setu(MEMORY.ref(1, primitives).offset(0x11L).get());
                    MEMORY.ref(1, packet).offset(0x06L).setu(MEMORY.ref(1, primitives).offset(0x12L).get());
                    MEMORY.ref(1, packet).offset(0x10L).setu(MEMORY.ref(1, primitives).offset(0x14L).get());
                    MEMORY.ref(1, packet).offset(0x11L).setu(MEMORY.ref(1, primitives).offset(0x15L).get());
                    MEMORY.ref(1, packet).offset(0x12L).setu(MEMORY.ref(1, primitives).offset(0x16L).get());
                    MEMORY.ref(1, packet).offset(0x1cL).setu(MEMORY.ref(1, primitives).offset(0x18L).get());
                    MEMORY.ref(1, packet).offset(0x1dL).setu(MEMORY.ref(1, primitives).offset(0x19L).get());
                    MEMORY.ref(1, packet).offset(0x1eL).setu(MEMORY.ref(1, primitives).offset(0x1aL).get());

                    CPU.COP2(0x158_002dL); // Average Z

                    MEMORY.ref(1, packet).offset(0x03L).setu(0x9L);
                    MEMORY.ref(1, packet).offset(0x07L).setu(0x36L);

                    final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());

                    //LAB_800dde20
                    final GsOT_TAG tag = tags_1f8003d0.deref().get(z);
                    MEMORY.ref(4, packet).offset(0x0L).setu(0x900_0000L | tag.p.get());
                    tag.set(packet & 0xff_ffffL);
                    packet += 0x28L;
                  }
                }
              }
            }
          }
        }
      }

      //LAB_800dde48
      primitives += 0x24L;
    }

    //LAB_800dde54
    gpuPacketAddr_1f8003d8.setu(packet);
    return primitives;
  }

  private static long renderPrimitive3800(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    long packet = gpuPacketAddr_1f8003d8.get();

    //LAB_800dabc4
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x0aL).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x0eL).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x12L).get());
      CPU.MTC2(vert0.getXY(), 0); // VXY0
      CPU.MTC2(vert0.getZ(),  1); // VZ0
      CPU.MTC2(vert1.getXY(), 2); // VXY1
      CPU.MTC2(vert1.getZ(),  3); // VZ1
      CPU.MTC2(vert2.getXY(), 4); // VXY2
      CPU.MTC2(vert2.getZ(),  5); // VZ2
      CPU.COP2(0x28_0030L); // Perspective transform triple

      if((int)CPU.CFC2(31) >= 0) { // No errors
        CPU.COP2(0x140_0006L); // Normal clip

        if((int)CPU.MFC2(24) > 0) { // Is visible
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12)); // Screen XY0
          MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(13)); // Screen XY1
          MEMORY.ref(4, packet).offset(0x18L).setu(CPU.MFC2(14)); // Screen XY2

          final SVECTOR vert3 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x16L).get());
          CPU.MTC2(vert3.getXY(), 0); // VXY0
          CPU.MTC2(vert3.getZ(),  1); // VZ0
          CPU.COP2(0x18_0001L); // Perspective transform single
          final long t4 = CPU.CFC2(31); // Flags
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14)); // Screen XY0

          if(MEMORY.ref(2, packet).offset(0x8L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x10L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x18L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() >= -0xc0) {
            //LAB_800dacc0
            if(MEMORY.ref(2, packet).offset(0xaL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x12L).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x1aL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() >= -0x80) {
              //LAB_800dad10
              if(MEMORY.ref(2, packet).offset(0x8L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x10L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x18L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() <= 0xc0) {
                //LAB_800dad60
                if(MEMORY.ref(2, packet).offset(0xaL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x12L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x1aL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() <= 0x80) {
                  //LAB_800dadb0
                  CPU.COP2(0x168_002eL); // Average Z

                  final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());

                  //LAB_800dade0
                  final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x08L).get() * 0x8L;
                  final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x0cL).get() * 0x8L;
                  final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x10L).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1); // VZ0
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 2); // VXY1
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 3); // VZ1
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 4); // VXY2
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 5); // VZ2
                  CPU.MTC2(MEMORY.ref(4, primitives).offset(0x4L).get(), 6);
                  CPU.COP2(0x118_043fL); // Normal colour colour triple vector
                  MEMORY.ref(4, packet).offset(0x04L).setu(CPU.MFC2(20)); // RGB0
                  MEMORY.ref(4, packet).offset(0x0cL).setu(CPU.MFC2(21)); // RGB1
                  MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(22)); // RGB2

                  final long norm3 = normals + MEMORY.ref(2, primitives).offset(0x14L).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, norm3).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm3).offset(0x4L).get(), 1); // VZ0
                  CPU.COP2(0x108_041bL); // Normal colour colour single vector
                  MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(22)); // RGB2

                  MEMORY.ref(1, packet).offset(0x03L).setu(0x8L);
                  MEMORY.ref(1, packet).offset(0x07L).setu(0x38L);

                  final GsOT_TAG tag = tags_1f8003d0.deref().get(z);
                  MEMORY.ref(4, packet).setu(0x800_0000L | tag.p.get());
                  tag.set(packet & 0xff_ffffL);

                  packet += 0x24L;
                }
              }
            }
          }
        }
      }

      //LAB_800dae8c
      primitives += 0x18L;
    }

    //LAB_800dae98
    gpuPacketAddr_1f8003d8.setu(packet);
    return primitives;
  }

  private static long renderPrimitive3804(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    long packet = gpuPacketAddr_1f8003d8.get();

    //LAB_800db7d8
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x16L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1aL).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1eL).get());
      CPU.MTC2(vert0.getXY(), 0); // VXY0
      CPU.MTC2(vert0.getZ(),  1); // VZ0
      CPU.MTC2(vert1.getXY(), 2); // VXY1
      CPU.MTC2(vert1.getZ(),  3); // VZ1
      CPU.MTC2(vert2.getXY(), 4); // VXY2
      CPU.MTC2(vert2.getZ(),  5); // VZ2
      CPU.COP2(0x28_0030L); // Perspective transform triple

      if((int)CPU.CFC2(31) >= 0) { // No errors
        CPU.COP2(0x140_0006L); // Normal clip

        if((int)CPU.MFC2(24) > 0) { // Is visible
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12)); // Screen XY0
          MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(13)); // Screen XY1
          MEMORY.ref(4, packet).offset(0x18L).setu(CPU.MFC2(14)); // Screen XY2
          final SVECTOR vert3 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x22L).get());
          CPU.MTC2(vert3.getXY(), 0);
          CPU.MTC2(vert3.getZ(),  1);
          CPU.COP2(0x18_0001L); // Perspective transform single
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14)); // Screen XY2

          if(MEMORY.ref(2, packet).offset(0x8L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x10L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x18L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() >= -0xc0) {
            //LAB_800db8d4
            if(MEMORY.ref(2, packet).offset(0xaL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x12L).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x1aL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() >= -0x80) {
              //LAB_800db924
              if(MEMORY.ref(2, packet).offset(0x8L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x10L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x18L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() <= 0xc0) {
                //LAB_800db974
                if(MEMORY.ref(2, packet).offset(0xaL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x12L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x1aL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() <= 0x80) {
                  //LAB_800db9c4
                  CPU.COP2(0x168_002eL); // Average Z

                  final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());
                  final GsOT_TAG tag = tags_1f8003d0.deref().get(z);

                  CPU.MTC2(MEMORY.ref(4, primitives).offset(0x4L).get(), 6); // RGBC
                  final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x14L).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1); // VZ0
                  CPU.COP2(0x108_041bL); // Normal colour colour single vector
                  MEMORY.ref(4, packet).offset(0x4L).setu(CPU.MFC2(22)); // RGB2

                  CPU.MTC2(MEMORY.ref(4, primitives).offset(0x8).get(), 6); // RGBC
                  final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x18L).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 1); // VZ0
                  CPU.COP2(0x108_041bL); // Normal colour colour single vector
                  MEMORY.ref(4, packet).offset(0xcL).setu(CPU.MFC2(22)); // RGB2

                  CPU.MTC2(MEMORY.ref(4, primitives).offset(0xcL).get(), 6); // RGBC
                  final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x1cL).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 1); // VZ0
                  CPU.COP2(0x108_041bL); // Normal colour colour single vector
                  MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(22)); // RGB2

                  CPU.MTC2(MEMORY.ref(4, primitives).offset(0x10L).get(), 6); // RGBC
                  final long norm3 = normals + MEMORY.ref(2, primitives).offset(0x20L).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, norm3).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm3).offset(0x4L).get(), 1); // VZ0
                  CPU.COP2(0x108_041bL); // Normal colour colour single vector
                  MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(22)); // RGB2

                  MEMORY.ref(1, packet).offset(0x03L).setu(0x8L);
                  MEMORY.ref(1, packet).offset(0x07L).setu(0x38L);

                  MEMORY.ref(4, packet).offset(0x0L).setu(0x800_0000L | tag.p.get());
                  tag.set(packet & 0xff_ffffL);
                  packet += 0x24L;
                }
              }
            }
          }
        }
      }

      //LAB_800dbae8
      primitives += 0x24L;
    }

    //LAB_800dbaf4
    gpuPacketAddr_1f8003d8.setu(packet);
    return primitives;
  }

  private static long renderPrimitive3a00(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    long packet = gpuPacketAddr_1f8003d8.get();

    //LAB_800dcad8
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x0aL).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x0eL).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x12L).get());
      CPU.MTC2(vert0.getXY(), 0); // VXY0
      CPU.MTC2(vert0.getZ(),  1); // VZ0
      CPU.MTC2(vert1.getXY(), 2); // VXY1
      CPU.MTC2(vert1.getZ(),  3); // VZ1
      CPU.MTC2(vert2.getXY(), 4); // VXY2
      CPU.MTC2(vert2.getZ(),  5); // VZ2
      CPU.COP2(0x28_0030L); // Perspective transform triple

      if((int)CPU.CFC2(31) >= 0) { // No errors
        CPU.COP2(0x140_0006L); // Normal clip

        if(CPU.MFC2(24) != 0) { // Is visible
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12)); // Screen XY0
          MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(13)); // Screen XY1
          MEMORY.ref(4, packet).offset(0x18L).setu(CPU.MFC2(14)); // Screen XY2

          final SVECTOR vert3 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x16L).get());
          CPU.MTC2(vert3.getXY(), 0); // VXY0
          CPU.MTC2(vert3.getZ(),  1); // VZ0
          CPU.COP2(0x18_0001L); // Perspective transform single

          if((int)CPU.CFC2(31) >= 0) { // No errors
            MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14)); // Screen XY2

            if(MEMORY.ref(2, packet).offset(0x8L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x10L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x18L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() >= -0xc0) {
              //LAB_800dcbdc
              if(MEMORY.ref(2, packet).offset(0xaL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x12L).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x1aL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() >= -0x80) {
                //LAB_800dcc2c
                if(MEMORY.ref(2, packet).offset(0x8L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x10L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x18L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() <= 0xc0) {
                  //LAB_800dcc7c
                  if(MEMORY.ref(2, packet).offset(0xaL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x12L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x1aL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() <= 0x80) {
                    //LAB_800dcccc
                    CPU.COP2(0x168_002eL); // Average Z

                    final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());

                    CPU.MTC2(MEMORY.ref(4, primitives).offset(0x4L).get(), 6); // RGBC
                    final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x08L).get() * 0x8L;
                    final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x0cL).get() * 0x8L;
                    final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x10L).get() * 0x8L;
                    CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0); // VXY0
                    CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1); // VZ0
                    CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 2); // VXY1
                    CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 3); // VZ1
                    CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 4); // VXY2
                    CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 5); // VZ2
                    CPU.COP2(0x118_043fL); // Normal colour colour triple vector
                    MEMORY.ref(4, packet).offset(0x04L).setu(CPU.MFC2(20)); // RGB0
                    MEMORY.ref(4, packet).offset(0x0cL).setu(CPU.MFC2(21)); // RGB1
                    MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(22)); // RGB2

                    final long norm3 = normals + MEMORY.ref(2, primitives).offset(0x14L).get() * 0x8L;
                    CPU.MTC2(MEMORY.ref(4, norm3).offset(0x0L).get(), 0); // VXY0
                    CPU.MTC2(MEMORY.ref(4, norm3).offset(0x4L).get(), 1); // VZ0
                    CPU.COP2(0x108_041bL); // Normal colour colour single vector
                    MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(22)); // RGB2

                    MEMORY.ref(1, packet).offset(0x03L).setu(0x8L);
                    MEMORY.ref(1, packet).offset(0x07L).setu(0x3aL);

                    final GsOT_TAG tag = tags_1f8003d0.deref().get(z);
                    MEMORY.ref(4, packet).offset(0x00L).setu(0x800_0000L | tag.p.get());
                    tag.set(packet & 0xff_ffffL);
                    packet += 0x24L;

                    MEMORY.ref(4, packet).offset(0x0L).setu(0x100_0000L | tag.p.get());
                    MEMORY.ref(1, packet).offset(0x3L).setu(0x1L);
                    MEMORY.ref(4, packet).offset(0x4L).setu(0xe100_021fL | _1f8003ec.get() & 0x9ffL);
                    tag.set(packet & 0xff_ffffL);
                    packet += 0x8L;
                  }
                }
              }
            }
          }
        }
      }

      //LAB_800dcdf8
      primitives += 0x18L;
    }

    //LAB_800dce04
    gpuPacketAddr_1f8003d8.setu(packet);
    return primitives;
  }

  private static long renderPrimitive3a04(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    long packet = gpuPacketAddr_1f8003d8.get();

    //LAB_800dd14c
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x16L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1aL).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1eL).get());
      CPU.MTC2(vert0.getXY(), 0); // VXY0
      CPU.MTC2(vert0.getZ(),  1); // VZ0
      CPU.MTC2(vert1.getXY(), 2); // VXY1
      CPU.MTC2(vert1.getZ(),  3); // VZ1
      CPU.MTC2(vert2.getXY(), 4); // VXY2
      CPU.MTC2(vert2.getZ(),  5); // VZ2
      CPU.COP2(0x28_0030L); // Perspective transform triple

      if((int)CPU.CFC2(31) >= 0) { // No errors
        CPU.COP2(0x140_0006L); // Normal clip

        if(CPU.MFC2(24) != 0) { // Is visible
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12)); // Screen XY0
          MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(13)); // Screen XY1
          MEMORY.ref(4, packet).offset(0x18L).setu(CPU.MFC2(14)); // Screen XY2
          final SVECTOR vert3 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x22L).get());
          CPU.MTC2(vert3.getXY(), 0);
          CPU.MTC2(vert3.getZ(),  1);
          CPU.COP2(0x18_0001L); // Perspective transform single

          if((int)CPU.CFC2(31) >= 0) { // No errors
            MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14)); // Screen XY2

            if(MEMORY.ref(2, packet).offset(0x8L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x10L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x18L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() >= -0xc0) {
              //LAB_800dd250
              if(MEMORY.ref(2, packet).offset(0xaL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x12L).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x1aL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() >= 0x80) {
                //LAB_800dd2a0
                if(MEMORY.ref(2, packet).offset(0x8L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x10L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x18L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() <= 0xc0) {
                  //LAB_800dd2f0
                  if(MEMORY.ref(2, packet).offset(0xaL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x12L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x1aL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() <= 0x80) {
                    //LAB_800dd340
                    CPU.COP2(0x168_002eL); // Average Z

                    final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());

                    CPU.MTC2(MEMORY.ref(4, primitives).offset(0x4L).get(), 6); // RGBC
                    final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x14L).get() * 0x8L;
                    CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0); // VXY0
                    CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1); // VZ0
                    CPU.COP2(0x108_041bL); // Normal colour colour single vector
                    MEMORY.ref(4, packet).offset(0x4L).setu(CPU.MFC2(22)); // RGB2
                    CPU.MTC2(MEMORY.ref(4, primitives).offset(0x8L).get(), 6); // RGBC
                    final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x18L).get() * 0x8L;
                    CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 0); // VXY0
                    CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 1); // VZ0
                    CPU.COP2(0x108_041bL); // Normal colour colour single vector
                    MEMORY.ref(4, packet).offset(0xcL).setu(CPU.MFC2(22)); // RGB2
                    CPU.MTC2(MEMORY.ref(4, primitives).offset(0xcL).get(), 6); // RGBC
                    final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x1cL).get() * 0x8L;
                    CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 0); // VXY0
                    CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 1); // VZ0
                    CPU.COP2(0x108_041bL); // Normal colour colour single vector
                    MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(22)); // RGB2
                    CPU.MTC2(MEMORY.ref(4, primitives).offset(0x10L).get(), 6); // RGBC
                    final long norm3 = normals + MEMORY.ref(2, primitives).offset(0x20L).get() * 0x8L;
                    CPU.MTC2(MEMORY.ref(4, norm3).offset(0x0L).get(), 0); // VXY0
                    CPU.MTC2(MEMORY.ref(4, norm3).offset(0x4L).get(), 1); // VZ0
                    CPU.COP2(0x108_041bL); // Normal colour colour single vector
                    MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(22)); // RGB2

                    MEMORY.ref(1, packet).offset(0x03L).setu(0x8L);
                    MEMORY.ref(1, packet).offset(0x07L).setu(0x3aL);

                    final GsOT_TAG tag = tags_1f8003d0.deref().get(z);
                    MEMORY.ref(4, packet).offset(0x0L).setu(0x800_0000L | tag.p.get());
                    tag.set(packet & 0xff_ffffL);
                    packet += 0x24L;

                    MEMORY.ref(4, packet).offset(0x0L).setu(0x100_0000L | tag.p.get());
                    MEMORY.ref(1, packet).offset(0x3L).setu(0x1L);
                    MEMORY.ref(4, packet).offset(0x4L).setu(0xe100_021fL | _1f8003ec.get() & 0x9ffL);
                    tag.set(packet & 0xff_ffffL);
                    packet += 0x8L;
                  }
                }
              }
            }
          }
        }
      }

      //LAB_800dd4b0
      primitives += 0x24L;
    }

    //LAB_800dd4bc
    gpuPacketAddr_1f8003d8.setu(packet);
    return primitives;
  }

  private static long renderPrimitive3c(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    long packet = gpuPacketAddr_1f8003d8.get();

    CPU.MTC2(0x80_8080L, 6); // RGBC

    //LAB_800db1a8
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x16L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1aL).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1eL).get());
      CPU.MTC2(vert0.getXY(), 0); // VXY0
      CPU.MTC2(vert0.getZ(),  1); // VZ0
      CPU.MTC2(vert1.getXY(), 2); // VXY1
      CPU.MTC2(vert1.getZ(),  3); // VZ1
      CPU.MTC2(vert2.getXY(), 4); // VXY2
      CPU.MTC2(vert2.getZ(),  5); // VZ2
      CPU.COP2(0x28_0030L); // Perspective transform triple

      MEMORY.ref(4, packet).offset(0x0cL).setu(MEMORY.ref(4, primitives).offset(0x04L));
      MEMORY.ref(4, packet).offset(0x18L).setu(MEMORY.ref(4, primitives).offset(0x08L));
      MEMORY.ref(4, packet).offset(0x24L).setu(MEMORY.ref(4, primitives).offset(0x0cL));

      if((int)CPU.CFC2(31) >= 0) { // No errors
        CPU.COP2(0x140_0006L); // Normal clipping

        if((int)CPU.MFC2(24) > 0) { // Is visible
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12)); // Screen XY0
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13)); // Screen XY1
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14)); // Screen XY2

          final SVECTOR vert3 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x22L).get());
          CPU.MTC2(vert3.getXY(), 0); // VXY0
          CPU.MTC2(vert3.getZ(),  1); // VZ0
          CPU.COP2(0x18_0001L); // Perspective transformation single
          final long t4 = CPU.CFC2(31); // Flag
          MEMORY.ref(4, packet).offset(0x2cL).setu(CPU.MFC2(14)); // Screen XY2

          if(MEMORY.ref(2, packet).offset(0x08L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x14L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x2cL).getSigned() >= -0xc0) {
            //LAB_800db2c4
            if(MEMORY.ref(2, packet).offset(0x0aL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x16L).getSigned() >= 0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x2eL).getSigned() >= -0x80) {
              //LAB_800db314
              if(MEMORY.ref(2, packet).offset(0x08L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x14L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x2cL).getSigned() <= 0xc0) {
                //LAB_800db364
                if(MEMORY.ref(2, packet).offset(0x0aL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x16L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x2eL).getSigned() <= 0x80) {
                  //LAB_800db3b4
                  CPU.COP2(0x168_002eL); // Average Z
                  MEMORY.ref(4, packet).offset(0x30L).setu(MEMORY.ref(4, primitives).offset(0x10L));

                  final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());

                  final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x14L).get() * 0x8L;
                  final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x18L).get() * 0x8L;
                  final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x1cL).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1); // VZ0
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 2); // VXY1
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 3); // VZ1
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 4); // VXY2
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 5); // VZ2
                  CPU.COP2(0x118_043fL); // Normal colour triple vector

                  MEMORY.ref(4, packet).offset(0x04L).setu(CPU.MFC2(20)); // RGB0
                  MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(21)); // RGB1
                  MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(22)); // RGB2

                  final long norm3 = normals + MEMORY.ref(2, primitives).offset(0x20L).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, norm3).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm3).offset(0x4L).get(), 1); // VZ0
                  CPU.COP2(0x108_041bL); // Normal colour single vector
                  MEMORY.ref(4, packet).offset(0x28L).setu(CPU.MFC2(22)); // RGB2

                  MEMORY.ref(1, packet).offset(0x3L).setu(0xcL); // 12 words
                  MEMORY.ref(1, packet).offset(0x7L).setu(0x3cL); // Shaded textured quad, opaque, texture-blending

                  final GsOT_TAG tag = tags_1f8003d0.deref().get(z);
                  MEMORY.ref(4, packet).setu(0xc00_0000L | tag.p.get());
                  tag.set(packet & 0xff_ffffL);

                  packet += 0x34L;
                }
              }
            }
          }
        }
      }

      //LAB_800db494
      primitives += 0x24L;
    }

    //LAB_800db4a0
    gpuPacketAddr_1f8003d8.setu(packet);
    return primitives;
  }

  private static long renderPrimitive3d(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long length) {
    long packet = gpuPacketAddr_1f8003d8.get();

    //LAB_800dbe2c
    for(int i = 0; i < length; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x24L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x26L).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x28L).get());
      CPU.MTC2(vert0.getXY(), 0);
      CPU.MTC2(vert0.getZ(),  1);
      CPU.MTC2(vert1.getXY(), 2);
      CPU.MTC2(vert1.getZ(),  3);
      CPU.MTC2(vert2.getXY(), 4);
      CPU.MTC2(vert2.getZ(),  5);
      CPU.COP2(0x280030L);
      MEMORY.ref(4, packet).offset(0x0cL).setu(MEMORY.ref(4, primitives).offset(0x4L).get());
      MEMORY.ref(4, packet).offset(0x18L).setu(MEMORY.ref(4, primitives).offset(0x8L).get());
      MEMORY.ref(4, packet).offset(0x24L).setu(MEMORY.ref(4, primitives).offset(0xcL).get());

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        if((int)CPU.MFC2(24) > 0) {
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12));
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14));
          final SVECTOR vert3 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x2aL).get());
          CPU.MTC2(vert3.getXY(), 0);
          CPU.MTC2(vert3.getZ(),  1);
          CPU.COP2(0x180001L);
          MEMORY.ref(1, packet).offset(0x03L).setu(0xcL);
          MEMORY.ref(4, packet).offset(0x04L).setu(0x3c80_8080L);
          MEMORY.ref(4, packet).offset(0x30L).setu(MEMORY.ref(4, primitives).offset(0x10L).get());
          final long t3 = CPU.CFC2(31);
          MEMORY.ref(4, packet).offset(0x2cL).setu(CPU.MFC2(14));

          if(MEMORY.ref(2, packet).offset(0x8L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x14L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x2cL).getSigned() >= -0xc0) {
            //LAB_800dbf64
            if(MEMORY.ref(2, packet).offset(0xaL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x16L).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x2eL).getSigned() >= -0x80) {
              //LAB_800dbfb4
              if(MEMORY.ref(2, packet).offset(0x8L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x14L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x2cL).getSigned() <= 0xc0) {
                //LAB_800dc004
                if(MEMORY.ref(2, packet).offset(0xaL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x16L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x2eL).getSigned() <= 0x80) {
                  //LAB_800dc054
                  CPU.COP2(0x168002eL);

                  final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());
                  final GsOT_TAG tag = tags_1f8003d0.deref().get(z);

                  MEMORY.ref(1, packet).offset(0x04L).setu(MEMORY.ref(1, primitives).offset(0x14L).get());
                  MEMORY.ref(1, packet).offset(0x05L).setu(MEMORY.ref(1, primitives).offset(0x15L).get());
                  MEMORY.ref(1, packet).offset(0x06L).setu(MEMORY.ref(1, primitives).offset(0x16L).get());
                  MEMORY.ref(1, packet).offset(0x10L).setu(MEMORY.ref(1, primitives).offset(0x18L).get());
                  MEMORY.ref(1, packet).offset(0x11L).setu(MEMORY.ref(1, primitives).offset(0x19L).get());
                  MEMORY.ref(1, packet).offset(0x12L).setu(MEMORY.ref(1, primitives).offset(0x1aL).get());
                  MEMORY.ref(1, packet).offset(0x1cL).setu(MEMORY.ref(1, primitives).offset(0x1cL).get());
                  MEMORY.ref(1, packet).offset(0x1dL).setu(MEMORY.ref(1, primitives).offset(0x1dL).get());
                  MEMORY.ref(1, packet).offset(0x1eL).setu(MEMORY.ref(1, primitives).offset(0x1eL).get());
                  MEMORY.ref(1, packet).offset(0x28L).setu(MEMORY.ref(1, primitives).offset(0x20L).get());
                  MEMORY.ref(1, packet).offset(0x29L).setu(MEMORY.ref(1, primitives).offset(0x21L).get());
                  MEMORY.ref(1, packet).offset(0x2aL).setu(MEMORY.ref(1, primitives).offset(0x22L).get());

                  MEMORY.ref(1, packet).offset(0x03L).setu(0xcL);
                  MEMORY.ref(1, packet).offset(0x07L).setu(0x3cL);

                  MEMORY.ref(4, packet).offset(0x0L).setu(0xc00_0000L | tag.p.get());
                  tag.set(packet & 0xff_ffffL);
                  packet += 0x34L;
                }
              }
            }
          }
        }
      }

      //LAB_800dc13c
      primitives += 0x2cL;
    }

    //LAB_800dc148
    gpuPacketAddr_1f8003d8.setu(packet);
    return primitives;
  }

  private static long renderPrimitive3e(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    long packet = gpuPacketAddr_1f8003d8.get();

    CPU.MTC2(0x80_8080L, 6); // RGBC

    //LAB_800dc4a0
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x16L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1aL).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1eL).get());
      CPU.MTC2(vert0.getXY(), 0); // VXY0
      CPU.MTC2(vert0.getZ(),  1); // VZ0
      CPU.MTC2(vert1.getXY(), 2); // VXY1
      CPU.MTC2(vert1.getZ(),  3); // VZ1
      CPU.MTC2(vert2.getXY(), 4); // VXY2
      CPU.MTC2(vert2.getZ(),  5); // VZ2

      CPU.COP2(0x28_0030L); // Perspective transform triple

      MEMORY.ref(4, packet).offset(0x0cL).setu(MEMORY.ref(4, primitives).offset(0x4L).get());
      MEMORY.ref(4, packet).offset(0x18L).setu(MEMORY.ref(4, primitives).offset(0x8L).get());
      MEMORY.ref(4, packet).offset(0x24L).setu(MEMORY.ref(4, primitives).offset(0xcL).get());

      if((int)CPU.CFC2(31) >= 0) { // No errors
        CPU.COP2(0x140_0006L); // Normal clip

        if(CPU.MFC2(24) != 0) { // Is visible
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12)); // Screen XY0
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13)); // Screen XY1
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14)); // Screen XY2
          final SVECTOR vert3 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x22L).get());
          CPU.MTC2(vert3.getXY(), 0);
          CPU.MTC2(vert3.getZ(),  1);

          CPU.COP2(0x18_0001L); // Perspective transform single

          if((int)CPU.CFC2(31) >= 0) { // No errors
            MEMORY.ref(4, packet).offset(0x2cL).setu(CPU.MFC2(14)); // Screen XY2

            if(MEMORY.ref(2, packet).offset(0x8L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x14L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x2cL).getSigned() >= -0xc0) {
              //LAB_800dc5c4
              if(MEMORY.ref(2, packet).offset(0xaL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x16L).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x2eL).getSigned() >= -0x80) {
                //LAB_800dc614
                if(MEMORY.ref(2, packet).offset(0x8L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x14L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x2cL).getSigned() <= 0xc0) {
                  //LAB_800dc664
                  if(MEMORY.ref(2, packet).offset(0xaL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x16L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x2eL).getSigned() <= 0x80) {
                    //LAB_800dc6b4
                    MEMORY.ref(4, packet).offset(0x30L).setu(MEMORY.ref(4, primitives).offset(0x10L).get());

                    CPU.COP2(0x168_002eL); // Average Z

                    final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());
                    final GsOT_TAG tag = tags_1f8003d0.deref().get(z);

                    final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x14L).get() * 0x8L;
                    final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x18L).get() * 0x8L;
                    final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x1cL).get() * 0x8L;
                    CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0); // VXY0
                    CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1); // VZ0
                    CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 2); // VXY1
                    CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 3); // VZ1
                    CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 4); // VXY2
                    CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 5); // VZ2
                    CPU.COP2(0x118_043fL); // Normal colour colour triple vector
                    MEMORY.ref(4, packet).offset(0x04L).setu(CPU.MFC2(20)); // RGB0
                    MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(21)); // RGB1
                    MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(22)); // RGB2
                    final long norm3 = normals + MEMORY.ref(2, primitives).offset(0x20L).get() * 0x8L;
                    CPU.MTC2(MEMORY.ref(4, norm3).offset(0x0L).get(), 0);
                    CPU.MTC2(MEMORY.ref(4, norm3).offset(0x4L).get(), 1);
                    CPU.COP2(0x108_041bL); // Normal colour colour single vector
                    MEMORY.ref(4, packet).offset(0x28L).setu(CPU.MFC2(22)); // RGB2

                    MEMORY.ref(1, packet).offset(0x3L).setu(0xcL);
                    MEMORY.ref(1, packet).offset(0x7L).setu(0x3eL);

                    MEMORY.ref(4, packet).offset(0x0L).setu(0xc00_0000L | tag.p.get());
                    tag.set(packet & 0xff_ffffL);
                    packet += 0x34L;
                  }
                }
              }
            }
          }
        }
      }

      //LAB_800dc794
      primitives += 0x24L;
    }

    //LAB_800dc7a0
    gpuPacketAddr_1f8003d8.setu(packet);
    return primitives;
  }

  private static long renderPrimitive3f(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long count) {
    long packet = gpuPacketAddr_1f8003d8.get();

    //LAB_800dd84c
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x24L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x26L).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x28L).get());
      CPU.MTC2(vert0.getXY(), 0); // VXY0
      CPU.MTC2(vert0.getZ(),  1); // VZ0
      CPU.MTC2(vert1.getXY(), 2); // VXY1
      CPU.MTC2(vert1.getZ(),  3); // VZ1
      CPU.MTC2(vert2.getXY(), 4); // VXY2
      CPU.MTC2(vert2.getZ(),  5); // VZ2
      CPU.COP2(0x28_0030L); // Perspective transform triple

      MEMORY.ref(4, packet).offset(0x0cL).setu(MEMORY.ref(4, primitives).offset(0x04L));
      MEMORY.ref(4, packet).offset(0x18L).setu(MEMORY.ref(4, primitives).offset(0x08L));
      MEMORY.ref(4, packet).offset(0x24L).setu(MEMORY.ref(4, primitives).offset(0x0cL));

      if((int)CPU.CFC2(31) >= 0) { // No errors
        CPU.COP2(0x140_0006L); // Normal clip

        if(CPU.MFC2(24) != 0) { // Is visible
          MEMORY.ref(1, packet).offset(0x03L).setu(0xcL); // 12 words
          MEMORY.ref(4, packet).offset(0x04L).setu(0x3e80_8080L); // Shaded textured four-point polygon, semi-transparent, tex-blend
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12)); // Screen XY0
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13)); // Screen XY1
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14)); // Screen XY2

          final SVECTOR vert3 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x2aL).get());
          CPU.MTC2(vert3.getXY(), 0); // VXY0
          CPU.MTC2(vert3.getZ(),  1); // VZ0
          CPU.COP2(0x18_0001L); // Perspective transform single

          MEMORY.ref(4, packet).offset(0x30L).setu(MEMORY.ref(4, primitives).offset(0x10L));

          if((int)CPU.CFC2(31) >= 0) { // No errors
            MEMORY.ref(4, packet).offset(0x2cL).setu(CPU.MFC2(14)); // Screen XY2

            if(MEMORY.ref(2, packet).offset(0x08L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x14L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() >= -0xc0 || MEMORY.ref(2, packet).offset(0x2cL).getSigned() >= -0xc0) {
              //LAB_800dd98c
              if(MEMORY.ref(2, packet).offset(0x0aL).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x16L).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() >= -0x80 || MEMORY.ref(2, packet).offset(0x2eL).getSigned() >= -0x80) {
                //LAB_800dd9dc
                if(MEMORY.ref(2, packet).offset(0x08L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x14L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x20L).getSigned() <= 0xc0 || MEMORY.ref(2, packet).offset(0x2cL).getSigned() <= 0xc0) {
                  //LAB_800dda2c
                  if(MEMORY.ref(2, packet).offset(0x0aL).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x16L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x22L).getSigned() <= 0x80 || MEMORY.ref(2, packet).offset(0x2eL).getSigned() <= 0x80) {
                    //LAB_800dda7c
                    CPU.COP2(0x168_002eL); // Average Z

                    final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());
                    final GsOT_TAG tag = tags_1f8003d0.deref().get(z);

                    MEMORY.ref(1, packet).offset(0x04L).setu(MEMORY.ref(1, primitives).offset(0x14L));
                    MEMORY.ref(1, packet).offset(0x05L).setu(MEMORY.ref(1, primitives).offset(0x15L));
                    MEMORY.ref(1, packet).offset(0x06L).setu(MEMORY.ref(1, primitives).offset(0x16L));
                    MEMORY.ref(1, packet).offset(0x10L).setu(MEMORY.ref(1, primitives).offset(0x18L));
                    MEMORY.ref(1, packet).offset(0x11L).setu(MEMORY.ref(1, primitives).offset(0x19L));
                    MEMORY.ref(1, packet).offset(0x12L).setu(MEMORY.ref(1, primitives).offset(0x1aL));
                    MEMORY.ref(1, packet).offset(0x1cL).setu(MEMORY.ref(1, primitives).offset(0x1cL));
                    MEMORY.ref(1, packet).offset(0x1dL).setu(MEMORY.ref(1, primitives).offset(0x1dL));
                    MEMORY.ref(1, packet).offset(0x1eL).setu(MEMORY.ref(1, primitives).offset(0x1eL));
                    MEMORY.ref(1, packet).offset(0x28L).setu(MEMORY.ref(1, primitives).offset(0x20L));
                    MEMORY.ref(1, packet).offset(0x29L).setu(MEMORY.ref(1, primitives).offset(0x21L));
                    MEMORY.ref(1, packet).offset(0x2aL).setu(MEMORY.ref(1, primitives).offset(0x22L));

                    MEMORY.ref(1, packet).offset(0x03L).setu(0xcL);
                    MEMORY.ref(1, packet).offset(0x07L).setu(0x3eL);

                    //LAB_800ddb3c
                    MEMORY.ref(4, packet).setu(0xc00_0000L | tag.p.get());
                    tag.set(packet & 0xff_ffffL);
                    packet += 0x34L;
                  }
                }
              }
            }
          }
        }
      }

      //LAB_800ddb64
      primitives += 0x2cL;
    }

    //LAB_800ddb70
    gpuPacketAddr_1f8003d8.setu(packet);
    return primitives;
  }
}
