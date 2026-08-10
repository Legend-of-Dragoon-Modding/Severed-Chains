package legend.game.fmv;

import legend.core.Config;
import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.audio.GenericSource;
import legend.core.gpu.Bpp;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.Texture;
import legend.core.platform.WindowEvents;
import legend.game.EngineState;
import legend.game.modding.coremod.CoreMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3i;
import org.lwjgl.system.MemoryUtil;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Path;

import static legend.core.GameEngine.AUDIO_THREAD;
import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.DISCORD;
import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Graphics.clearBlue_800babc0;
import static legend.game.Graphics.clearGreen_800bb104;
import static legend.game.Graphics.clearRed_8007a3a8;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.opengl.GL11C.GL_LINEAR;
import static org.lwjgl.opengl.GL11C.GL_RGB;

public final class VideoPlayer {
  private VideoPlayer() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(VideoPlayer.class);

  private static Runnable oldRenderer;
  private static int oldFps;

  private static FFmpegFrameGrabber grabber;
  private static Frame currentFrame;

  private static int videoWidth;
  private static int videoHeight;

  private static GenericSource source;
  private static ByteBuffer pcmBuffer;

  private static WindowEvents.KeyPressed keyPress;
  private static WindowEvents.ButtonPressed buttonPressed;
  private static WindowEvents.Click click;
  private static boolean shouldStop;

  private static Obj texturedObj;
  private static Texture displayTexture;
  private static final Vector2i oldProjectionSize = new Vector2i();
  private static EngineState.RenderMode oldRenderMode;
  private static final Vector3i oldClearColour = new Vector3i();
  private static final Matrix4f transforms = new Matrix4f();

  private static Runnable onRender;
  private static Runnable onFinish;

  public static void play(final Path video, @Nullable final Runnable onRender, @Nullable final Runnable onFinish) throws IOException {
    LOGGER.info("Playing FMV %s", video);

    VideoPlayer.onRender = onRender;
    VideoPlayer.onFinish = onFinish;

    shouldStop = false;

    grabber = new FFmpegFrameGrabber(video.toFile());

    // Tell FFmpeg to do the YUV -> RGB conversion for us
    grabber.setPixelFormat(avutil.AV_PIX_FMT_RGB24);
    grabber.start();

    videoWidth = grabber.getImageWidth();
    videoHeight = grabber.getImageHeight();

    LOGGER.info("Video size %dx%d", videoWidth, videoHeight);

    displayTexture = Texture.create("Video", builder -> {
      builder.size(videoWidth, videoHeight);
      builder.internalFormat(GL_RGB);
      builder.dataFormat(GL_RGB);
      builder.minFilter(GL_LINEAR);
      builder.magFilter(GL_LINEAR);
    });

    oldFps = RENDERER.window().getFpsLimit();
    oldProjectionSize.set(RENDERER.getNativeWidth(), RENDERER.getNativeHeight());
    oldRenderMode = RENDERER.getRenderMode();
    oldClearColour.set(clearRed_8007a3a8, clearGreen_800bb104, clearBlue_800babc0);
    RENDERER.setRenderMode(EngineState.RenderMode.PERSPECTIVE);
    RENDERER.setProjectionSize(320, 240);
    RENDERER.setClearColour(0.0f, 0.0f, 0.0f);

    keyPress = RENDERER.events().onKeyPress((window, key, scancode, mods, repeat) -> shouldStop = true);
    buttonPressed = RENDERER.events().onButtonPress((window, action, repeat) -> shouldStop = true);
    click = RENDERER.events().onMouseRelease((window, x, y, button, mods) -> shouldStop = true);

    source = AUDIO_THREAD.addSource(new GenericSource(AL_FORMAT_STEREO16, 48_000));
    final float volume = CONFIG.getConfig(CoreMod.FMV_VOLUME_CONFIG.get()) * CONFIG.getConfig(CoreMod.MASTER_VOLUME_CONFIG.get());

    // Buffer audio
    grabber.setCloseInputStream(false);

    final int sampleRate = grabber.getSampleRate();
    final int channels = grabber.getAudioChannels();

    final double durationSec = grabber.getLengthInTime() / 1_000_000.0;
    final int maxBytes = (int)(durationSec * sampleRate * channels * 2) + 4096; // 4kb padding

    pcmBuffer = MemoryUtil.memAlloc(maxBytes);

    while((currentFrame = grabber.grabFrame()) != null) {
      if(currentFrame.samples != null) {
        final ShortBuffer sb = (ShortBuffer)currentFrame.samples[0];
        for(int i = 0; i < sb.limit(); i++) {
          pcmBuffer.putShort((short)(sb.get(i) * volume));
        }
      }
    }

    pcmBuffer.flip();
    source.bufferOutput(pcmBuffer);

    grabber.setFrameNumber(0);
    grabber.setCloseInputStream(true);

    currentFrame = grabber.grabImage();

    oldRenderer = RENDERER.setRenderCallback(() -> {
      if(onRender != null) {
        onRender.run();
      }

      if(shouldStop) {
        stop();
        return;
      }

      RENDERER.window().setFpsLimit(60 * Config.getGameSpeedMultiplier());
      PLATFORM.setInputTickRate(60 * Config.getGameSpeedMultiplier());

      // We pin video playback to audio playback
      final long audioTimeMicro = (long)(source.getPosition() * 1_000_000L);
      final long toleranceMicro = 15_000L; // 15ms tolerance

      try {
        while(true) {
          final long videoTimeMicro = currentFrame.timestamp;

          if(videoTimeMicro < audioTimeMicro - toleranceMicro) {
            // BEHIND: skip frames until we catch up
            currentFrame = grabber.grabImage();
            if(currentFrame == null) {
              stop();
              return;
            }

            continue;
          }

          if(videoTimeMicro > audioTimeMicro + toleranceMicro) {
            // AHEAD: wait for the next loop tick, render the existing rawRgbData
            break;
          }

          // IN SYNC: decode and process the frame
          final ByteBuffer buffer = (ByteBuffer)currentFrame.image[0];
          displayTexture.data(0, 0, videoWidth, videoHeight, buffer);
          break;
        }
      } catch(final Exception e) {
        LOGGER.warn("Error while playing video", e);
      }

      if(texturedObj == null) {
        texturedObj = new QuadBuilder("FMV")
          .bpp(Bpp.BITS_24)
          .size(1.0f, 1.0f)
          .build();
      }

      displayTexture.use();

      final float windowHeight = RENDERER.getNativeHeight();
      final float windowWidth = windowHeight * RENDERER.getRenderAspectRatio();

      final float scaleW = windowWidth / 320.0f;
      final float scaleH = windowHeight / videoHeight;
      final float scale = Math.min(scaleW, scaleH);

      final float w = videoWidth * scale;
      final float h = videoHeight * scale;

      final float l = (windowWidth - w) / 2.0f;
      final float t = (windowHeight - h) / 2.0f;

      transforms
        .translation(l, t, 100.0f)
        .scale(w, h, 1.0f)
      ;

      RENDERER.queueOrthoModel(texturedObj, transforms, QueuedModelStandard.class)
        .texture(displayTexture)
      ;

      DISCORD.tick();

      if(!source.isActive() || MathHelper.flEq(source.getPosition(), 0.0f)) {
        stop();
      }
    });
  }

  public static void stop() {
    RENDERER.setRenderCallback(() -> {
      if(texturedObj != null) {
        texturedObj.delete();
        texturedObj = null;
      }

      if(displayTexture != null) {
        displayTexture.delete();
        displayTexture = null;
      }

      if(keyPress != null) {
        RENDERER.events().removeKeyPress(keyPress);
        keyPress = null;
      }

      if(click != null) {
        RENDERER.events().removeMouseRelease(click);
        click = null;
      }

      if(buttonPressed != null) {
        RENDERER.events().removeButtonPress(buttonPressed);
        buttonPressed = null;
      }

      RENDERER.setRenderCallback(oldRenderer);
      RENDERER.window().setFpsLimit(oldFps);
      PLATFORM.setInputTickRate(oldFps);
      RENDERER.setRenderMode(oldRenderMode);
      RENDERER.setProjectionSize(oldProjectionSize.x, oldProjectionSize.y);
      clearRed_8007a3a8 = oldClearColour.x;
      clearGreen_800bb104 = oldClearColour.y;
      clearBlue_800babc0 = oldClearColour.z;

      oldRenderer = null;

      if(grabber != null) {
        try {
          grabber.stop();
          grabber.release();
        } catch(final FFmpegFrameGrabber.Exception e) {
          LOGGER.warn("Failed to clean up ffmpeg", e);
        }
      }

      if(onRender != null) {
        onRender.run();
      }

      if(onFinish != null) {
        onFinish.run();
      }

      onRender = null;
      onFinish = null;

      if(pcmBuffer != null) {
        MemoryUtil.memFree(pcmBuffer);
        pcmBuffer = null;
      }

      AUDIO_THREAD.removeSource(source);
      source = null;
    });
  }
}
