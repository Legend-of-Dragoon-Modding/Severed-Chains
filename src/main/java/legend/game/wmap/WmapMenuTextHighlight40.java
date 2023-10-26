package legend.game.wmap;

import legend.core.gpu.RECT;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.game.types.Translucency;

public class WmapMenuTextHighlight40 {
  public WMapTextHighlightSubRectVertexColours10[] subRectVertexColoursArray_00;
  public Translucency[] tpagePacket_04;

  public RECT[] rects_1c;
  // public final long[] _20 = new long[2]; // Unused

  public int columnCount_28;
  public int rowCount_2c;
  public int subRectCount_30;
  public float currentBrightness_34;
  public float previousBrightness_36;
  public int x_38;
  public int y_3a;
  public boolean transparency_3c;
  public int z_3e;

  public Obj[] objs;
  public final MV transforms = new MV();

  public int type_3f;

  public void delete() {
    if(this.objs != null) {
      for(int i = 0; i < this.objs.length; i++) {
        if(this.objs[i] != null) {
          this.objs[i].delete();
          this.objs[i] = null;
        }
      }
    }
  }
}
