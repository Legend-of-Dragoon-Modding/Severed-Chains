package legend.game.unpacker;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

import legend.game.unpacker.audio.XaSector;
import legend.game.unpacker.video.Frame;

public class IkiFile {
    public static final int threads = ManagementFactory.getThreadMXBean().getThreadCount();

    public static void IkiFile(final String name, final FileData file) {
        ByteBuffer data = ByteBuffer.wrap(file.data()).order(ByteOrder.LITTLE_ENDIAN);
        final int sectorCount = data.capacity() / 0x930;
        final int frameCount = (int)Math.ceil(sectorCount / 10.0);
        final int audioBlockCount = sectorCount / 8;

        final BufferedImage[] frames = new BufferedImage[frameCount];

        int jobBucket = (frames.length / threads) + 1;

        IntStream.range(0, threads).boxed().toList().stream().parallel().forEach(s -> {
            for (int i = 0, j = s * jobBucket; i < jobBucket && j < frameCount; i++, j++) {
                frames[j] = Frame.generate(data, j * 10);
            }
        });

        final int[][] audioBlocks = new int[audioBlockCount][];

        XaSector.resetBuffer();

        for (int i = 0; i < audioBlocks.length; i++) {
            audioBlocks[i] = XaSector.generate(data.slice((i * 8 + 7) * 0x930, 0x930).order(ByteOrder.LITTLE_ENDIAN));
        }

        data.clear();

        Path path = Paths.get(System.getProperty("user.dir") + "\\files\\" + name.replace(".IKI", ".mp4"));

        File f = new File(path.getParent().toString());

        if (!f.exists()) {
            f.mkdirs();
        }

        FFmpeg.createVideo(frames, audioBlocks, path, threads);

    }
}
