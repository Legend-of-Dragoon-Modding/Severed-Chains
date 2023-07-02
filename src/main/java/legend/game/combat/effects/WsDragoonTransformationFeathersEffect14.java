package legend.game.combat.effects;

import java.util.Arrays;

public class WsDragoonTransformationFeathersEffect14 implements BttlScriptData6cSubBase1 {
  /** ushort */
  public final int count_00;
  /** ushort */
  public int unused_02;

  public short u_06;
  public short v_08;
  public short width_0a;
  public short height_0c;
  public short clut_0e;
  public WsDragoonTransformationFeatherInstance70[] featherArray_10;

  public WsDragoonTransformationFeathersEffect14(final int count) {
    this.count_00 = count;
    this.featherArray_10 = new WsDragoonTransformationFeatherInstance70[count];
    Arrays.setAll(this.featherArray_10, WsDragoonTransformationFeatherInstance70::new);
  }
}
