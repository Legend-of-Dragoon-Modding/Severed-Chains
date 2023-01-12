package legend.game.fmv;

import com.github.kokorin.jaffree.ffmpeg.FFmpegResultFuture;
import legend.core.DebugHelper;
import legend.core.MathHelper;
import legend.core.opengl.Window;
import legend.core.spu.XaAdpcm;
import legend.game.types.FileEntry08;
import legend.game.unpacker.FFmpeg;
import legend.game.unpacker.Unpacker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.game.Scus94491BpeSegment.setWidthAndFlags;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndexOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8005._80052d6c;
import static legend.game.Scus94491BpeSegment_8005.diskFmvs_80052d7c;
import static legend.game.Scus94491BpeSegment_800b.afterFmvLoadingStage_800bf0ec;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.fmvIndex_800bf0dc;
import static legend.game.Scus94491BpeSegment_800b.fmvStage_800bf0d8;
import static legend.game.Scus94491BpeSegment_800b.submapIndex_800bd808;

public class Fmv {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Fmv.class);


  private static Runnable oldRenderer;
  private static int oldFps;
  private static int oldWidth;
  private static int oldHeight;
  private static int sector;

  private static SourceDataLine sound;

  private static Window.Events.Char charPress;
  private static Window.Events.Click click;
  private static boolean shouldStop;

  public static void playCurrentFmv() {
//TODO this might be necessary for the post-game cutscene or something?
//      creditsLoaded_800d1cb8.setu(0);
//      loadDrgnBinFile(0, 5721, 0, SMap::loadCreditsMrg, (int)fmvIndex_800bf0dc.get(), 0x4L);

    final int width = switch((int)fmvIndex_800bf0dc.get()) {
      case 0, 2, 3, 4, 6, 7, 8, 9, 14, 15, 16, 17 -> 320;
      case 1, 5, 10, 11, 12, 13 -> 640;
      default -> throw new RuntimeException("Bad FMV index");
    };

    setWidthAndFlags(width);

    submapIndex_800bd808.set(-1);

    final FileEntry08 file = diskFmvs_80052d7c.get(drgnBinIndex_800bc058.get()).deref().get((int)(fmvIndex_800bf0dc.get() - _80052d6c.get(drgnBinIndex_800bc058.get() - 1).get()));
    Fmv.play(file.name_04.deref().get(), true);
    fmvStage_800bf0d8.setu(0);
    mainCallbackIndexOnceLoaded_8004dd24.setu(afterFmvLoadingStage_800bf0ec);
  }

  public static void play(final String file, final boolean doubleSpeed) {
    String mp4 = System.getProperty("user.dir") + "\\files\\" + file.replace(".IKI", ".mp4");

    FFmpegResultFuture future = FFmpeg.consumeVideo(mp4, 30);

    shouldStop = false;

    while(!FFmpeg.isReady()) {
      DebugHelper.sleep(1);
    }

    while(!GPU.isReady()) {
      DebugHelper.sleep(1);
    }

    oldRenderer = GPU.mainRenderer;
    oldFps = GPU.window().getFpsLimit();
    oldWidth = GPU.window().getWidth();
    oldHeight = GPU.window().getHeight();
    GPU.window().setFpsLimit(15);

    try {
      sound = AudioSystem.getSourceDataLine(new AudioFormat(48000, 16, 1, true, false));
      sound.open();
      sound.start();
    } catch(final LineUnavailableException|IllegalArgumentException e) {
      LOGGER.error("Failed to start audio for FMV");
    }

    charPress = GPU.window().events.onCharPress((window, codepoint) -> shouldStop = true);
    click = GPU.window().events.onMouseRelease((window, x, y, button, mods) -> shouldStop = true);

    GPU.mainRenderer = () -> {
      if(shouldStop) {
        stop();
        future.graceStop();
      }


      byte[] samples = FFmpeg.getSamples();
      if (samples != null) {
        sound.write(samples, 0 , samples.length);
      }

      GPU.displaySize(320, 192);
      GPU.displayTexture(FFmpeg.getFrame());
      GPU.drawMesh();

      if (!FFmpeg.isReady()) {
        stop();
        future.graceStop();
      }
    };
  }

  public static void stop() {
    GPU.mainRenderer = () -> {
      if(charPress != null) {
        GPU.window().events.removeCharPress(charPress);
        charPress = null;
      }

      if(click != null) {
        GPU.window().events.removeMouseRelease(click);
        click = null;
      }

      GPU.mainRenderer = oldRenderer;
      GPU.window().setFpsLimit(oldFps);
      GPU.displaySize(oldWidth, oldHeight);
      oldRenderer = null;

      if(sound != null) {
        sound.close();
        sound = null;
      }
    };
  }
}
