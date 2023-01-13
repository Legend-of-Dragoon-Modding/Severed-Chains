package legend.game.unpacker;

import legend.game.unpacker.audio.XaSector;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class XaFile {

    public static void create(final String name, final FileData file) {
        ByteBuffer data = ByteBuffer.wrap(file.data()).order(ByteOrder.LITTLE_ENDIAN);
        final int sectorCount = data.capacity() / 0x930;
        final int channelSectorCount = sectorCount / 16;


        for (int subFile = 1; subFile < 16; subFile++) {
            List<int[]> audioFile = new ArrayList<>();

            XaSector.resetBuffer();

            for (int sector = 0; sector < channelSectorCount; sector++) {
                audioFile.add(XaSector.generate(data.slice((sector * 16 + subFile) * 0x930, 0x930).order(ByteOrder.LITTLE_ENDIAN)));
                if ((data.get((sector * 16 + subFile) * 0x930 + 18) >>> 7 & 1) == 1) {
                    break;
                }

            }
            Path path = Paths.get(System.getProperty("user.dir") + "\\files\\" + name.replace(".XA", "[") + (subFile - 1) + "].ogg");

            File f = new File(path.getParent().toString());

            if (!f.exists()) {
                f.mkdirs();
            }

            FFmpeg.createAudio(audioFile.toArray(new int[0][]), path, IkiFile.threads);
        }

    }
}
