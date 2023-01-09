package legend.game.unpacker;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Paths;
import java.util.stream.IntStream;

import legend.game.unpacker.audio.XaSector;
import legend.game.unpacker.video.Frame;

public class IkiFile {
    private static final int threads = 16;

    public IkiFile(final String name, final FileData file) {
        byte[] dataArr = file.data();
        ByteBuffer data = ByteBuffer.wrap(dataArr);
        final int frameCount = (int)Math.ceil(dataArr.length / 10.0);
        final int audioBlockCount = dataArr.length / 8;

        final BufferedImage[] frames = new BufferedImage[frameCount];

        int jobBucket = (frameCount / threads) + 1;

        IntStream.range(0, threads).boxed().toList().stream().parallel().forEach(s -> {
            for (int i = 0, j = s * jobBucket; i < jobBucket && j < frameCount; i++, j++) {
                frames[i] = Frame.generate(data, j * 10);
            }
        });

        final int[][] audioBlocks = new int[audioBlockCount][];

        final short[] old = new short[] {0, 0};
        final short[] older = new short[] {0, 0};

        for (int i = 0; i < audioBlockCount; i++) {
            audioBlocks[i] = XaSector.generate(ByteBuffer.wrap(dataArr, (i * 8 + 7) * 0x930, 0x930).order(ByteOrder.LITTLE_ENDIAN), old, older);
        }

        FFmpeg.createVideo(frames, audioBlocks, Paths.get(""), threads); //TODO specify path

    }
}
