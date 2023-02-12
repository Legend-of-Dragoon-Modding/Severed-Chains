package legend.game.unpacker;

import legend.core.MathHelper;
import legend.core.gte.BVEC4;
import legend.core.gte.SVECTOR;
import legend.core.memory.Method;

import java.util.Map;
import java.util.Set;

public final class CtmdTransformer {
  private CtmdTransformer() { }

  /** Example file: 4146/0/0 */
  public static boolean ctmdDiscriminator(final String name, final FileData data, final Set<Unpacker.Flags> flags) {
    // Check if in DEFF and if file is large enough
    if(!flags.contains(Unpacker.Flags.DEFF) || data.size() < 0x14) {
      return false;
    }

    // Check DEFF file type
    final int type = data.readByte(3);
    if(type != 0 && type != 1 && type != 2 && type != 3 && type != 4) {
      return false;
    }

    final int containerOffset = data.readInt(0xc);

    // Validate container offset
    if(containerOffset < 0x10 || containerOffset >= data.size()) {
      return false;
    }

    final int nextOffset = data.readInt(0x10);

    // Invalid next file offset, probably not the right kind of file
    if(nextOffset != 0 && nextOffset < containerOffset || nextOffset >= data.size()) {
      return false;
    }

    // Make sure we get the 0xc container format header, and the TMD header
    if(data.readInt(containerOffset) != 0xc || data.readByte(containerOffset + 0xc) != 0x41) {
      return false;
    }

    // Check CTMD flag
    return (data.readInt(containerOffset + 0x10) & 0x2) != 0;
  }

  public static Map<String, FileData> ctmdTransformer(final String name, final FileData data, final Set<Unpacker.Flags> flags) {
    final int containerOffset = data.readInt(0xc);
    final int nextOffset = data.readInt(0x10);

    final FileData containerData = data.slice(containerOffset, (nextOffset == containerOffset ? data.size() : nextOffset) - containerOffset);
    int ctmdEnd = containerData.size();
    for(int i = 0; i < 2; i++) {
      final int subfile = containerData.readInt(4 + i * 4);
      if(subfile > 0xc && subfile < ctmdEnd) {
        ctmdEnd = subfile;
      }
    }

    final FileData ctmdData = containerData.slice(0xc, ctmdEnd - 0xc);
    final Ctmd ctmd = new Ctmd(ctmdData);

    for(int objTableIndex = 0; objTableIndex < ctmd.objCount; objTableIndex++) {
      final CtmdUnpackingData50 unpackingData = new CtmdUnpackingData50();
      final Ctmd.ObjTable objTable = ctmd.objTables[objTableIndex];

      int primitiveOffset = 0xc + objTable.primitives_10;

      for(int primitiveIndex = 0; primitiveIndex < objTable.primitiveCount_14; ) {
        unpackingData._0c = 0;
        unpackingData._08 = 0;
        unpackingData._04 = unpackingData._00;

        final int header = ctmdData.readInt(primitiveOffset);
        final int words = header & 0xffff;

        // Packed primitive
        if((header & 0x8_0000) != 0) {
          final int packetSize = primitivePacketSize(header);
          final int tmdHeader = header & ~0x8_0000 | packetSize / 4 << 8;
          final Ctmd.PrimitiveGroup group = new Ctmd.PrimitiveGroup(tmdHeader, new byte[words][]);
          primitiveOffset += 4;

          for(int i = 0; i < words; i++) {
            final byte[] unpacked = new byte[packetSize];
            primitiveOffset += unpackCtmdData(unpackingData, unpacked, ctmdData.slice(primitiveOffset));
            group.data()[i] = unpacked;
          }

          objTable.unpackedPrimitives.add(group);
        } else {
//          primitiveOffset += words * 4;
          throw new RuntimeException("Need to support mixed files");
        }

        primitiveOffset = MathHelper.roundUp(primitiveOffset, 4);
        primitiveIndex += words;
      }
    }

    for(int objTableIndex = 0; objTableIndex < ctmd.objCount; objTableIndex++) {
      final Ctmd.ObjTable objTable = ctmd.objTables[objTableIndex];

      final int vertexCount = getVertexCount(objTable);
      objTable.unpackedVertices = new SVECTOR[vertexCount];

      final FileData vertices = ctmdData.slice(0xc + objTable.vertices_00);
      for(int i = 0; i < vertexCount; i++) {
        final BVEC4 lo = new BVEC4().set(vertices.readByte(i * 4), vertices.readByte(i * 4 + 1), vertices.readByte(i * 4 + 2), vertices.readByte(i * 4 + 3));
        final BVEC4 hi = new BVEC4().set(vertices.readByte(lo.getW() * 4), vertices.readByte(lo.getW() * 4 + 1), vertices.readByte(lo.getW() * 4 + 2), vertices.readByte(lo.getW() * 4 + 3));
        final SVECTOR vert = new SVECTOR();
        vert.setX((short)(lo.getX() + ((hi.getX() & 0xff) << 8)));
        vert.setY((short)(lo.getY() + ((hi.getY() & 0xff) << 8)));
        vert.setZ((short)(lo.getZ() + ((hi.getZ() & 0xff) << 8)));
        objTable.unpackedVertices[i] = vert;
      }

      final FileData normals = ctmdData.slice(0xc + objTable.normals_08);
      for(int i = 0; i < objTable.normalCount_0c; i++) {
        final int packed = normals.readInt(i * 4);
        final SVECTOR norm = new SVECTOR();
        norm.setX((short)(packed << 20 >> 19 & 0xffff_fff8));
        norm.setY((short)(packed << 10 >> 19 & 0xffff_fff8));
        norm.setZ((short)(packed >> 19 & 0xffff_fff8));
        objTable.unpackedNormals[i] = norm;
      }
    }

    int tmdSize = 0xc + ctmd.objCount * 0x1c;
    for(final Ctmd.ObjTable objTable : ctmd.objTables) {
      tmdSize += objTable.unpackedVertices.length * 8 + objTable.normalCount_0c * 8;

      for(final Ctmd.PrimitiveGroup group : objTable.unpackedPrimitives) {
//        tmdSize += 4; // Header

        for(final byte[] groupData : group.data()) {
          tmdSize += 4; // Header
          tmdSize += groupData.length;
        }
      }
    }

    final byte[] tmdData = new byte[tmdSize];
    MathHelper.set(tmdData, 0, 4, ctmdData.readInt(0));
    MathHelper.set(tmdData, 4, 4, ctmd.flags & ~0x2);
    MathHelper.set(tmdData, 8, 4, ctmd.objCount);

    int primitivesOffset = 0xc + ctmd.objCount * 0x1c;
    for(int i = 0; i < ctmd.objTables.length; i++) {
      final Ctmd.ObjTable objTable = ctmd.objTables[i];

      MathHelper.set(tmdData, 0xc + i * 0x1c + 0x10, 4, primitivesOffset - 0xc);
      MathHelper.set(tmdData, 0xc + i * 0x1c + 0x14, 4, objTable.primitiveCount_14);

      for(int groupIndex = 0; groupIndex < objTable.unpackedPrimitives.size(); groupIndex++) {
        final Ctmd.PrimitiveGroup group = objTable.unpackedPrimitives.get(groupIndex);

//        MathHelper.set(tmdData, primitivesOffset, 4, group.header());
//        primitivesOffset += 0x4;

        for(final byte[] prims : group.data()) {
          MathHelper.set(tmdData, primitivesOffset, 4, group.header());
          primitivesOffset += 0x4;
          System.arraycopy(prims, 0, tmdData, primitivesOffset, prims.length);
          primitivesOffset += prims.length;
        }
      }
    }

    int verticesOffset = primitivesOffset;
    for(int i = 0; i < ctmd.objTables.length; i++) {
      final Ctmd.ObjTable objTable = ctmd.objTables[i];

      MathHelper.set(tmdData, 0xc + i * 0x1c, 4, verticesOffset - 0xc);
      MathHelper.set(tmdData, 0xc + i * 0x1c + 0x4, 4, objTable.unpackedVertices.length);

      for(int vertIndex = 0; vertIndex < objTable.unpackedVertices.length; vertIndex++) {
        final SVECTOR vertex = objTable.unpackedVertices[vertIndex];
        MathHelper.set(tmdData, verticesOffset, 2, vertex.getX());
        MathHelper.set(tmdData, verticesOffset + 0x2, 2, vertex.getY());
        MathHelper.set(tmdData, verticesOffset + 0x4, 2, vertex.getZ());
        verticesOffset += 0x8;
      }
    }

    int normalsOffset = verticesOffset;
    for(int i = 0; i < ctmd.objTables.length; i++) {
      final Ctmd.ObjTable objTable = ctmd.objTables[i];

      MathHelper.set(tmdData, 0xc + i * 0x1c + 0x8, 4, normalsOffset - 0xc);
      MathHelper.set(tmdData, 0xc + i * 0x1c + 0xc, 4, objTable.normalCount_0c);

      for(int normIndex = 0; normIndex  < objTable.normalCount_0c; normIndex++) {
        final SVECTOR norm = objTable.unpackedNormals[normIndex];
        MathHelper.set(tmdData, normalsOffset, 2, norm.getX());
        MathHelper.set(tmdData, normalsOffset + 0x2, 2, norm.getY());
        MathHelper.set(tmdData, normalsOffset + 0x4, 2, norm.getZ());
        normalsOffset += 0x8;
      }
    }

    final int tmdSizeDifference = tmdData.length - ctmdData.size();
    final int newContainerSize = containerData.size() + tmdSizeDifference;

    final int containerSizeDifference = newContainerSize - containerData.size();
    final int newSize = data.size() + containerSizeDifference;
    final byte[] newData = new byte[newSize];

    // Copy data before what we modified
    data.copyTo(0, newData, 0, containerOffset);
    containerData.copyTo(0, newData, containerOffset, 0xc);

    // Adjust DEFF container pointers
    final int deffContainerPtr10 = (int)MathHelper.get(newData, 0x10, 2);
    final int deffContainerPtr14 = (int)MathHelper.get(newData, 0x14, 2); // NOTE: for type 4 DEFF containers, this may be the 0xc container header, but that should be fine because 0xc will be < the offset

    if(deffContainerPtr10 > containerOffset) {
      MathHelper.set(newData, 0x10, 2, deffContainerPtr10 + containerSizeDifference);
    }

    if(deffContainerPtr14 > containerOffset) {
      MathHelper.set(newData, 0x14, 2, deffContainerPtr14 + containerSizeDifference);
    }

    // Adjust 0xc container pointers
    final int cContainerPtr4 = (int)MathHelper.get(newData, containerOffset + 0x4, 2);
    final int cContainerPtr8 = (int)MathHelper.get(newData, containerOffset + 0x8, 2);

    if(cContainerPtr4 > 0xc) {
      MathHelper.set(newData, containerOffset + 0x4, 2, cContainerPtr4 + tmdSizeDifference);
    }

    if(cContainerPtr8 > 0xc) {
      MathHelper.set(newData, containerOffset + 0x8, 2, cContainerPtr8 + tmdSizeDifference);
    }

    // Copy new TMD data
    System.arraycopy(tmdData, 0, newData, containerOffset + 0xc, tmdData.length);

    // Copy unmodified data at the end of the 0xc container
    containerData.copyTo(ctmdEnd, newData, containerOffset + 0xc + tmdData.length, containerData.size() - ctmdEnd);

    // Copy unmodified data at the end of the DEFF container
    data.copyTo(containerOffset + containerData.size(), newData, containerOffset + newContainerSize, data.size() - containerOffset - containerData.size());

    return Map.of(name, new FileData(newData));
  }

  private static int primitivePacketSize(final int command) {
    final int primitiveId = command >>> 24;
    final boolean gradated = (command & 0x4_0000) != 0;
    final boolean normals = (command & 0x1_0000) == 0;
    final boolean shaded = (primitiveId & 0b1_0000) != 0;
    final boolean quad = (primitiveId & 0b1000) != 0;
    final boolean textured = (primitiveId & 0b100) != 0;
    final boolean lit = (primitiveId & 0b1) == 0;

    if(textured && gradated) {
      throw new RuntimeException("Invalid primitive type");
    }

    if(!textured && !lit) {
      throw new RuntimeException("Invalid primitive type");
    }

    final int vertexCount = quad ? 4 : 3;

    int bytes = vertexCount * 2;

    if(normals) {
      bytes += (shaded ? vertexCount : 1) * 2;
    }

    if(gradated || !lit) {
      bytes += vertexCount * 4;
    } else if(!textured) {
      bytes += 4;
    }

    if(textured) {
      bytes += vertexCount * 4;
    }

    return MathHelper.roundUp(bytes, 4);
  }

  @Method(0x800de840L)
  private static int unpackCtmdData(final CtmdUnpackingData50 unpackingData, final byte[] unpackedData, final FileData packedData) {
    int offset = 0;

    //LAB_800de878
    final int unpackedCount = unpackedData.length / 2;
    while(unpackingData._0c < unpackedCount) {
      if((unpackingData._08 & 0x100) == 0) {
        unpackingData._08 = packedData.readUByte(offset) | 0xff00;
        offset++;
      }

      //LAB_800de89c
      if((unpackingData._08 & 0x1) != 0) {
        unpackingData._10[unpackingData._00] = packedData.readUShort(offset);
        unpackingData._00++;
        unpackingData._00 &= 0x1f;
        unpackingData._0c++;
        offset += 2;
      } else {
        //LAB_800de8ec
        int a1 = packedData.readUByte(offset);
        final int length = (a1 >>> 5) + 1;

        //LAB_800de904
        for(int i = 0; i < length; i++) {
          a1 &= 0x1f;
          unpackingData._10[unpackingData._00] = unpackingData._10[a1];
          unpackingData._00++;
          unpackingData._00 &= 0x1f;
          a1++;
        }

        //LAB_800de940
        unpackingData._0c += length;
        offset++;
      }

      //LAB_800de94c
      unpackingData._08 >>= 1;
    }

    //LAB_800de968
    //LAB_800de970
    for(int i = 0; i < unpackedCount; i++) {
      MathHelper.set(unpackedData, i * 2, 2, unpackingData._10[unpackingData._04]);
      unpackingData._04++;
      unpackingData._04 &= 0x1f;
      unpackingData._0c--;
    }

    //LAB_800de9b4
    return offset;
  }

  private static int getVertexCount(final Ctmd.ObjTable table) {
    int highestIndex = -1;

    for(int groupIndex = 0; groupIndex < table.unpackedPrimitives.size(); groupIndex++) {
      final Ctmd.PrimitiveGroup group = table.unpackedPrimitives.get(groupIndex);

      final int primitiveId = group.header() >>> 24;

      if((primitiveId >>> 5 & 0b11) != 1) {
        throw new RuntimeException("Unsupported primitive type");
      }

      final boolean gradated = (group.header() & 0x4_0000) != 0;
      final boolean quad = (primitiveId & 0b1000) != 0;
      final boolean textured = (primitiveId & 0b100) != 0;
      final boolean lit = (primitiveId & 0b1) == 0;

      if(textured && gradated) {
        throw new RuntimeException("Invalid primitive type");
      }

      if(!textured && !lit) {
        throw new RuntimeException("Invalid primitive type");
      }

      final int vertexCount = quad ? 4 : 3;

      for(int primIndex = 0; primIndex < group.data().length; primIndex++) {
        final byte[] primitives = group.data()[primIndex];
        int readIndex = 0;

        if(textured) {
          for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
            readIndex += 2; // UV
            readIndex += 2; // CLUT/TPAGE/nothing
          }
        }

        if(gradated || !lit) {
          for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
            readIndex += 4; // colour
          }
        } else if(!textured) {
          readIndex += 4; // colour
        }

        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          if(lit) {
            final int normalIndex = (int)MathHelper.get(primitives, readIndex, 2);
            if(normalIndex >= table.normalCount_0c) {
              throw new RuntimeException("Invalid normal index " + normalIndex);
            }

            readIndex += 2; // normal
          }

          final int vi = (int)MathHelper.get(primitives, readIndex, 2);
          if(vi > highestIndex) {
            highestIndex = vi;
          }

          readIndex += 2; // vertex
        }
      }
    }

    return highestIndex + 1;
  }
}
