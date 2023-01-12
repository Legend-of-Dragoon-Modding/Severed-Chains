package legend.game.unpacker;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.github.kokorin.jaffree.StreamType;
import com.github.kokorin.jaffree.ffmpeg.*;

public class FFmpeg {
    private static final Queue<int[]> frameBuffer = new LinkedList<>();
    private static final Queue<byte[]> audioBuffer = new LinkedList<>();

    public static int[] getFrame() {
        return frameBuffer.poll();
    }
    public static byte[] getSamples() {return audioBuffer.poll();}

    public static boolean isReady() {
        return frameBuffer.size() > 0;
    }

    public static void createVideo(final BufferedImage[] frames, final int[][] audio, Path path, int threads) {
        FrameProducer producer = new FrameProducer() {
            private int frameCounter = 0;
            private int audioCounter = 0;
            private int nextVideoTimecode = 0;
            private int nextAudioTimecode = 0;

            @Override
            public List<Stream> produceStreams() {
                return Arrays.asList(
                        new Stream()
                                .setType(Stream.Type.VIDEO)
                                .setTimebase(1500L)
                                .setWidth(frames[0].getWidth())
                                .setHeight(frames[0].getHeight()),
                        new Stream().setType(Stream.Type.AUDIO)
                                .setId(1)
                                .setTimebase(1500L)
                                .setSampleRate(37800)
                                .setChannels(1)
                );
            }

            @Override
            public Frame produce() {
                if (frameCounter > frames.length - 1 && audioCounter > audio.length - 1 ) {
                    return null; // return null when End of Stream is reached
                }

                if (nextVideoTimecode <= nextAudioTimecode) {
                    Frame videoFrame = Frame.createVideoFrame(0, nextVideoTimecode, frames[frameCounter]);
                    frameCounter++;
                    nextVideoTimecode += 100;
                    return videoFrame;
                }

                Frame audioFrame = Frame.createAudioFrame(1, nextAudioTimecode, audio[audioCounter]);
                audioCounter++;
                nextAudioTimecode += 80;
                return audioFrame;
            }
        };

        try (SeekableByteChannel outputChannel =
                     Files.newByteChannel(path, StandardOpenOption.CREATE,
                             StandardOpenOption.WRITE, StandardOpenOption.READ,
                             StandardOpenOption.TRUNCATE_EXISTING)
        ) {
            com.github.kokorin.jaffree.ffmpeg.FFmpeg.atPath()
                    .addInput(FrameInput.withProducer(producer))
                    .addOutput(ChannelOutput.toChannel(path.getFileName().toString(), outputChannel))
                    .addArguments("-f", "mp4")
                    .addArguments("-c:v", "libx264") // video codec
                    .addArguments("-pix_fmt", "yuv420p")
                    .addArguments("-crf", "0")
                    .addArguments("-c:a", "libopus") // Audio codec
                    .addArguments("-af", "volume=0.5") // halve the volume
                    .addArguments("-aq", "0") // Losless audio
                    .addArguments("-threads", Integer.toString(threads))
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FFmpegResultFuture consumeVideo(String file, int frameBufferSize) {
        return com.github.kokorin.jaffree.ffmpeg.FFmpeg.atPath()
                .addInput(UrlInput.fromUrl(file))
                .addOutput(FrameOutput
                        .withConsumer(
                                new FrameConsumer() {
                                    @Override
                                    public void consumeStreams(List<Stream> streams) {
                                    }

                                    @Override
                                    public void consume(Frame frame) {
                                        if (frame == null) {
                                            return;
                                        }

                                        if (frame.getStreamId() == 0) {
                                            if (frameBuffer.size() >= frameBufferSize) {
                                                try {
                                                    Thread.sleep(10);
                                                } catch (InterruptedException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }

                                            int height = frame.getImage().getHeight();
                                            int width = frame.getImage().getWidth();
                                            int[] rgb = new int[height * width];

                                            frame.getImage().getRGB(0, 0, width, height, rgb, 0, width);
                                            for (int i = 0; i < rgb.length; i++) {
                                                rgb[i] = rgb[i] << 8;
                                            }
                                            frameBuffer.add(rgb);

                                        } else {
                                            int[] samples = frame.getSamples();
                                        }
                                    }
                                }
                        )
                        .disableStream(StreamType.SUBTITLE)
                        .disableStream(StreamType.DATA)
                ).executeAsync();
    }

}
