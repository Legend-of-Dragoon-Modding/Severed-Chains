package legend.game.submap;

import legend.core.gpu.Rect4i;
import legend.game.tim.Tim;
import legend.game.scripting.ScriptFile;
import legend.game.tmd.UvAdjustmentMetrics14;

import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.GPU;

public class SubmapAssets {
  public ScriptFile script;
  public final List<SubmapObject> objects = new ArrayList<>();
  public final List<Tim> pxls = new ArrayList<>();
  public final List<UvAdjustmentMetrics14> uvAdjustments = new ArrayList<>();
  /** TODO Two object indices that get special flags, needs research */
  public byte[] lastEntry;

  public void uploadToGpu() {
    int x = 576;
    int y = 256;
    for(int pxlIndex = 0; pxlIndex < this.pxls.size(); pxlIndex++) {
      final Tim tim = this.pxls.get(pxlIndex);

      if(tim != null) {
        final Rect4i imageRect = tim.getImageRect();
        final Rect4i clutRect = tim.getClutRect();

        imageRect.x = x;
        imageRect.y = y;
        clutRect.x = x;
        clutRect.y = y + imageRect.h;

        GPU.uploadData15(imageRect, tim.getImageData());
        GPU.uploadData15(clutRect, tim.getClutData());

        this.uvAdjustments.add(this.createUvAdjustments(pxlIndex, x, y));

        x += tim.getImageRect().w;

        if(x >= 768) {
          x = 576;
          y += 128;
        }
      } else {
        this.uvAdjustments.add(UvAdjustmentMetrics14.NONE);
      }
    }
  }

  private UvAdjustmentMetrics14 createUvAdjustments(final int index, final int x, final int y) {
    final int clutX = x / 16;
    final int clutY = y + 112;
    final int tpageX = x / 64;
    final int tpageY = y / 256;
    final int u = x % 64 * 4;
    final int v = y % 256;
    final int clut = clutX | clutY << 6;
    final int tpage = tpageX | tpageY << 4;
    final int uv = u | v << 8;

    return new UvAdjustmentMetrics14(index + 1, clut << 16, 0x3c0ffff, tpage << 16, 0xffe0ffff, uv);
  }
}
