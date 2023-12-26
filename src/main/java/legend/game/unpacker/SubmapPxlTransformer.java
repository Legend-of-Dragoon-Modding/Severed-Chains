package legend.game.unpacker;

import legend.core.MathHelper;
import legend.core.gpu.Rect4i;
import legend.game.tim.Tim;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public final class SubmapPxlTransformer {
  private SubmapPxlTransformer() { }

  private static final Pattern pattern = Pattern.compile("^SECT/DRGN2\\d\\.BIN/\\d+/\\d+$");

  public static boolean discriminator(final String name, final FileData data, final Set<String> flags) {
    return
      pattern.matcher(name).matches() &&
      Integer.parseInt(name.substring(name.lastIndexOf('/', name.lastIndexOf('/') - 1) + 1, name.lastIndexOf('/'))) % 3 == 2 &&
      data.size() > 4 &&
      MathHelper.get(data.data(), data.offset(), 4) == 0x11;
  }

  public static Map<String, FileData> transform(final String name, final FileData data, final Set<String> flags) {
    final Map<String, FileData> files = new HashMap<>();

    final Tim pxl = new Tim(data);
    final Rect4i imageRect = pxl.getImageRect();
    final byte[] imageData = pxl.getImageData().getBytes();

    for(int tileY = 0; tileY < imageRect.h / 128; tileY++) {
      for(int tileX = 0; tileX < imageRect.w / 16; tileX++) {
        final FileData newData = new FileData(new byte[
          4 + // 0 ID
          4 + // 4 Flags
          4 + // 8 Image data offset
          8 + // c CLUT rect
          16 * 16 * 2 + // 14 CLUT data
          4 + // 214 Image segment size
          8 + // 218 Image rect
          16 * 128 * 2 // 220 Image data
        ]);

        final Tim newTim = new Tim(newData);

        newData.writeInt(0x0, 0x10);
        newData.writeInt(0x4, 0x8); // 4 BPP, CLUT
        newData.writeInt(0x8, 0x20c); // Image data offset
        newData.writeShort(0xc, imageRect.x - 576 + tileX * 16); // CLUT rect X
        newData.writeShort(0xe, imageRect.y - 256 + tileY * 128 + 112); // CLUT rect Y
        newData.writeShort(0x10, 16); // CLUT rect W
        newData.writeShort(0x12, 16); // CLUT rect H
        newData.writeInt(0x214, 0x100c); // Image segment size
        newData.writeShort(0x218, imageRect.x - 576 + tileX * 16); // Image rect X
        newData.writeShort(0x21a, imageRect.y - 256 + tileY * 128); // Image rect Y
        newData.writeShort(0x21c, 16); // Image rect W
        newData.writeShort(0x21e, 112); // Image rect H

        final Rect4i newImageRect = newTim.getImageRect();
        final Rect4i newClutRect = newTim.getClutRect();

        final FileData newImageData = newTim.getImageData();
        final FileData newClutData = newTim.getClutData();

        for(int imageRow = 0; imageRow < newImageRect.h; imageRow++) {
          newImageData.copyTo((tileY * 128 + imageRow) * imageRect.w * 2 + tileX * 16 * 2, imageData, imageRow * newImageRect.w * 2, newImageRect.w * 2);
        }

        for(int clutRow = 0; clutRow < newClutRect.h; clutRow++) {
          newClutData.copyTo((tileY * 128 + clutRow + newImageRect.h) * imageRect.w * 2 + tileX * 16 * 2, imageData, clutRow * newClutRect.w * 2, newClutRect.w * 2);
        }

        files.put(name.substring(0, name.lastIndexOf('/')) + "/textures/" + name.substring(name.lastIndexOf('/') + 1) + '.' + tileY + '.' + tileX, newData);
      }
    }

    return files;
  }
}
