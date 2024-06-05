package legend.game.unpacker;

import legend.core.gpu.Rect4i;
import legend.game.tim.Tim;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public final class SubmapPxlTransformer {
  private SubmapPxlTransformer() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(SubmapPxlTransformer.class);

  public static void transform(final PathNode root, final Transformations transformations, final Set<String> flags) {
    for(int drgnIndex = 1; drgnIndex <= 4; drgnIndex++) {
      final PathNode drgn = root.children.get("SECT").children.get("DRGN2" + drgnIndex + ".BIN");

      if(drgn != null) { // If we're doing a partial unpack, we might not have all DRGN2x
        final FileData mrg = drgn.children.get("mrg").data;
        final String mrgContents = mrg.readFixedLengthAscii(0x0, mrg.size());
        final int mrgEntries = (int)mrgContents.lines().count();

        for(int submapCutIndex = 1; submapCutIndex < mrgEntries; submapCutIndex += 3) {
          final PathNode submapCutAssets = drgn.children.get(Integer.toString(submapCutIndex + 1));

          if(submapCutAssets != null) { // DRGN21/180 has no assets?
            final PathNode submapCutScripts = drgn.children.get(Integer.toString(submapCutIndex + 2));

            if(submapCutAssets.data == null && submapCutScripts.data == null) { // Both are directories
              final int[] highResTextureIndices = getHighResTextureIndices(submapCutScripts);
              process(submapCutAssets, highResTextureIndices, transformations);
            }
          }
        }
      }
    }
  }

  private static String[] loadMrg(final PathNode directory) {
    final PathNode mrgFile = directory.children.get("mrg");
    return mrgFile.data.readFixedLengthAscii(0x0, mrgFile.data.size()).split("\n");
  }

  private static int[] getHighResTextureIndices(final PathNode submapCutScripts) {
    final String[] mrg = loadMrg(submapCutScripts);
    final String finalMrgEntry = mrg[mrg.length - 1];
    final String finalFileIndex = finalMrgEntry.substring(finalMrgEntry.indexOf('=') + 1, finalMrgEntry.indexOf(';'));
    final PathNode finalFile = submapCutScripts.children.get(finalFileIndex);

    if(finalFile.data.size() != 0x4) {
      final int index0 = finalFile.data.readInt(0x0) - 1;
      final int index1 = finalFile.data.readInt(0x4) - 1;
      return new int[] {index0, index1};
    }

    return new int[] {-1, -1};
  }

  private static void process(final PathNode submapCutAssets, final int[] highResTextureIndices, final Transformations transformations) {
    final PathNode textures = transformations.addChild(submapCutAssets, "textures", null);

    final String[] mrg = loadMrg(submapCutAssets);
    final Tim[] pxls = new Tim[3];

    for(int pxlIndex = 0; pxlIndex < 3; pxlIndex++) {
      final String pxlEntry = mrg[mrg.length - 3 + pxlIndex];
      final String pxlFileIndex = pxlEntry.substring(pxlEntry.indexOf('=') + 1, pxlEntry.indexOf(';'));
      pxls[pxlIndex] = new Tim(submapCutAssets.children.get(pxlFileIndex).data);
    }

    final FileMap textureMap = new FileMap();

    final int sobjCount = mrg.length / 33;
    for(int sobjIndex = 0; sobjIndex < sobjCount; sobjIndex++) {
      final String mrgEntry = mrg[sobjIndex * 33];
      final String modelFileIndex = mrgEntry.substring(mrgEntry.indexOf('=') + 1, mrgEntry.indexOf(';'));

      if(modelFileIndex.equals(Integer.toString(sobjIndex * 33))) {
        final int textureSlot;
        if(sobjIndex == highResTextureIndices[0]) {
          textureSlot = 17;
        } else if(sobjIndex == highResTextureIndices[1]) {
          textureSlot = 18;
        } else {
          textureSlot = sobjIndex;
        }

        final Rect4i textureRect = textureSlotRects[textureSlot];

        boolean found = false;
        for(final Tim pxl : pxls) {
          if(pxl.getImageRect().contains(textureRect.x, textureRect.y)) {
            final FileData tim = convertPxlSegment(pxl, textureRect);
            final String filename = Integer.toString(sobjIndex);
            transformations.addChild(textures, filename, tim);
            textureMap.addFile(filename, tim.size());
            found = true;
            break;
          }
        }

        // Sobj 16 will actually point to the submap animated overlay vram slot (1008, 256), unsure if there's anything else that would get here
        if(!found) {
          LOGGER.warn("Failed to find texture for %s sobj %d", submapCutAssets.fullPath, sobjIndex);
          textureMap.addFile(Integer.toString(sobjIndex), "", 0); // Add null file to map
        }
      } else {
        textureMap.addFile(Integer.toString(sobjIndex), Integer.toString(Integer.parseInt(modelFileIndex) / 33), 0);
      }
    }

    transformations.addChild(textures, "mrg", textureMap.build());
  }

  public static FileData convertPxlSegment(final Tim pxl, final Rect4i tileRect) {
    final Rect4i imageRect = pxl.getImageRect();
    final byte[] imageData = pxl.getImageData().getBytes();

    final int tileX = tileRect.x - imageRect.x;
    final int tileY = tileRect.y - imageRect.y;

    final FileData newData = new FileData(new byte[
      4 + // 0 ID
      4 + // 4 Flags
      4 + // 8 Image data offset
      8 + // c CLUT rect
      16 * 16 * 2 + // 14 CLUT data
      4 + // 214 Image segment size
      8 + // 218 Image rect
      tileRect.w * tileRect.h * 2 // 220 Image data
    ]);

    final Tim newTim = new Tim(newData);

    newData.writeInt(0x0, 0x10);
    newData.writeInt(0x4, 0x8); // 4 BPP, CLUT
    newData.writeInt(0x8, 0xc + 16 * 16 * 2); // Image data offset
    newData.writeShort(0xc, 0); // CLUT rect X
    newData.writeShort(0xe, tileRect.h - 16); // CLUT rect Y
    newData.writeShort(0x10, 16); // CLUT rect W
    newData.writeShort(0x12, 16); // CLUT rect H
    newData.writeInt(0x214, tileRect.w * tileRect.h * 2); // Image segment size
    newData.writeShort(0x218, 0); // Image rect X
    newData.writeShort(0x21a, 0); // Image rect Y
    newData.writeShort(0x21c, tileRect.w); // Image rect W
    newData.writeShort(0x21e, tileRect.h - 16); // Image rect H

    final Rect4i newImageRect = newTim.getImageRect();
    final Rect4i newClutRect = newTim.getClutRect();

    final FileData newImageData = newTim.getImageData();
    final FileData newClutData = newTim.getClutData();

    for(int imageRow = 0; imageRow < newImageRect.h; imageRow++) {
      newImageData.write((tileY + imageRow) * imageRect.w * 2 + tileX * 2, imageData, imageRow * newImageRect.w * 2, newImageRect.w * 2);
    }

    for(int clutRow = 0; clutRow < newClutRect.h; clutRow++) {
      newClutData.write((tileY + clutRow + newImageRect.h) * imageRect.w * 2 + tileX * 2, imageData, clutRow * newClutRect.w * 2, newClutRect.w * 2);
    }

    return newData;
  }

  private static final Rect4i[] textureSlotRects = {
    new Rect4i(608, 256, 16, 128),
    new Rect4i(624, 256, 16, 128),
    new Rect4i(576, 384, 16, 128),
    new Rect4i(592, 384, 16, 128),
    new Rect4i(608, 384, 16, 128),
    new Rect4i(624, 384, 16, 128),
    new Rect4i(672, 256, 16, 128),
    new Rect4i(688, 256, 16, 128),
    new Rect4i(640, 384, 16, 128),
    new Rect4i(656, 384, 16, 128),
    new Rect4i(672, 384, 16, 128),
    new Rect4i(688, 384, 16, 128),
    new Rect4i(736, 256, 16, 128),
    new Rect4i(752, 256, 16, 128),
    new Rect4i(704, 256, 16, 128),
    new Rect4i(720, 256, 16, 128),
    new Rect4i(1008, 256, 16, 128), // Submap cut texture (not used here)
    new Rect4i(576, 256, 32, 128), // Double-width slot 0
    new Rect4i(640, 256, 32, 128), // Double-width slot 1
  };
}
