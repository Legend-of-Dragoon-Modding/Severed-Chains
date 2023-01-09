package legend.game.unpacker;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import com.github.kokorin.jaffree.ffmpeg.FrameProducer;
import com.github.kokorin.jaffree.ffmpeg.Stream;
import com.github.kokorin.jaffree.ffmpeg.Frame;
import com.github.kokorin.jaffree.ffmpeg.FrameInput;
import com.github.kokorin.jaffree.ffmpeg.ChannelOutput;

public class FFmpeg {
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
                if (frameCounter > frames.length - 1 || audioCounter > audio.length) {
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
                    .addArguments("-preset:v", "slow") // Compression preset
                    .addArguments("-qp", "0") // Lossless video
                    .addArguments("-c:a", "aac") // Audio codec
                    .addArguments("-af", "volume=0.5") // halve the volume
                    .addArguments("-aq", "0") // Losless audio
                    .addArguments("-ac", "2") // Stereo
                    .addArguments("-threads", Integer.toString(threads))
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
