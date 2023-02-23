package legend.game.xa;

import legend.game.unpacker.FileData;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Transformer {
  public static void transcode(final String name, final FileData fileData) {
    final String path = System.getProperty("user.dir") + "\\files\\" + name.replace(".XA", "");

    //Make sure we have the folder created, since we're not running this back into the unpacker
    File f = new File(path);
    f.mkdirs();

    final ByteBuffer data = ByteBuffer.wrap(fileData.data()).order(ByteOrder.LITTLE_ENDIAN);

    if (name.endsWith("3.XA")) {
      creditsXa(path, data);
      return;
    }

    standardXa(path, data);
  }

  private static void standardXa(final String path, final ByteBuffer data) {
    final int sectorCount = data.capacity() / 0x930;
    final int channelSectorCount = sectorCount / 16;

    for (int subFile = 1; subFile < 16; subFile++) {
      final Track track = new Track(path + '\\' + subFile + ".ogg");

      for (int sector = 0; sector < channelSectorCount; sector++) {
        track.addAudioFrame(data.slice((sector * 16 + subFile) * 0x930, 0x930).order(ByteOrder.LITTLE_ENDIAN));

        if ((data.get((sector * 16 + subFile) * 0x930 + 18) >>> 7 & 1) == 1) {
          break;
        }
      }

      track.encode();
    }
  }

  private static void creditsXa(final String path, final ByteBuffer data) {
    final int sectorCount = data.capacity() / 0x930;
    final int channelSectorCount = sectorCount / 4;

    // The credits are on track 1.
    final Track track = new Track(path + "\\1.ogg");

    for (int sector = 0; sector < channelSectorCount; sector++) {
      track.addAudioFrame(data.slice((sector * 4 + 1) * 0x930, 0x930).order(ByteOrder.LITTLE_ENDIAN));
    }

    track.encode();
  }
}
