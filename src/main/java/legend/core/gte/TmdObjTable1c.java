package legend.core.gte;

import legend.core.IoHelper;

import java.util.Arrays;

/** 0x1c bytes long */
public class TmdObjTable1c {
  public final SVECTOR[] vert_top_00;
  public final int n_vert_04;
  public final SVECTOR[] normal_top_08;
  public final int n_normal_0c;
  public final int[] primitives_10;
  public final int n_primitive_14;
  public final int scale_18;

  public TmdObjTable1c(final byte[] data, final int offset) {
    final int vertOffset = offset + IoHelper.readInt(data, offset);
    final int normalOffset = offset + IoHelper.readInt(data, offset + 0x8);
    final int primitivesOffset = offset + IoHelper.readInt(data, offset + 0x10);

    this.n_vert_04 = IoHelper.readInt(data, offset + 0x4);
    this.n_normal_0c = IoHelper.readInt(data, offset + 0xc);
    this.n_primitive_14 = IoHelper.readInt(data, offset + 0x14);

    this.vert_top_00 = new SVECTOR[this.n_vert_04];
    Arrays.setAll(this.vert_top_00, i -> IoHelper.readSvec3(data, vertOffset + i * 0x8, new SVECTOR()));

    this.normal_top_08 = new SVECTOR[this.n_normal_0c];
    Arrays.setAll(this.normal_top_08, i -> IoHelper.readSvec3(data, normalOffset + i * 0x8, new SVECTOR()));

    this.primitives_10 = new int[this.n_primitive_14];
    Arrays.setAll(this.primitives_10, i -> IoHelper.readInt(data, primitivesOffset + i * 0x4));

    this.scale_18 = IoHelper.readInt(data, offset + 0x18);
  }
}
