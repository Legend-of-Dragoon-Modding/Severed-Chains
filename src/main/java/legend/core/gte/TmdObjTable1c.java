package legend.core.gte;

import legend.core.MathHelper;
import legend.game.Scus94491BpeSegment_8003;
import legend.game.unpacker.CtmdTransformer;
import legend.game.unpacker.FileData;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** 0x1c bytes long */
public class TmdObjTable1c {
  public final String name;

  public final Vector3f[] vert_top_00;
  public final int n_vert_04;
  public final Vector3f[] normal_top_08;
  public final int n_normal_0c;
  public final Primitive[] primitives_10;
  public final int n_primitive_14;
  public final int scale_18;

  public Vector3f[] vdf;

  public TmdObjTable1c(final String name, final FileData data, final FileData baseOffset) {
    this.name = name;

    final FileData verts = baseOffset.slice(data.readInt(0x0));
    final FileData normals = baseOffset.slice(data.readInt(0x8));
    final FileData primitives = baseOffset.slice(data.readInt(0x10));
    int primitivesOffset = 0;

    this.n_vert_04 = data.readInt(0x4);
    this.n_normal_0c = data.readInt(0xc);
    this.n_primitive_14 = data.readInt(0x14);

    this.vert_top_00 = new Vector3f[this.n_vert_04];
    Arrays.setAll(this.vert_top_00, i -> verts.readSvec3_0(i * 0x8, new Vector3f()));

    for(int i = 0; i < this.vert_top_00.length; i++) {
      this.vert_top_00[i] = verts.readSvec3_0(i * 0x8, new Vector3f());
    }

    this.normal_top_08 = new Vector3f[this.n_normal_0c];
    Arrays.setAll(this.normal_top_08, i -> normals.readSvec3_12(i * 0x8, new Vector3f()));

    final List<Primitive> primitivesList = new ArrayList<>();
    Scus94491BpeSegment_8003.updateTmdPacketIlen(primitives, this.n_primitive_14);

    for(int primitiveIndex = 0; primitiveIndex < this.n_primitive_14; ) {
      final int startOffset = primitivesOffset;
      final int header = primitives.readInt(primitivesOffset);
      final int count = header & 0xffff;

      final int packetSize = CtmdTransformer.primitivePacketSize(header);
      final byte[][] packetData = new byte[count][];

      for(int i = 0; i < count; i++) {
        primitivesOffset += 4;
        packetData[i] = new byte[packetSize];
        primitives.read(primitivesOffset, packetData[i], 0, packetSize);
        primitivesOffset += packetSize;
      }

      primitivesOffset = MathHelper.roundUp(primitivesOffset, 4);

      primitivesList.add(new Primitive(startOffset, packetSize, header, packetData));
      primitiveIndex += count;
    }

    this.primitives_10 = primitivesList.toArray(Primitive[]::new);

    this.scale_18 = data.readInt(0x18);
  }

  @Override
  public String toString() {
    return this.name + ' ' + super.toString();
  }

  public record Primitive(int offset, int width, int header, byte[][] data) { }
}
