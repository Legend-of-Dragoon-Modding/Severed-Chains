package legend.core.audio;

import legend.core.DebugHelper;
import legend.core.audio.sequencer.assets.BackgroundMusic;
import legend.game.unpacker.Loader;

public final class SoundTest {
  private static final int FILE_INDEX = 5820;
  private static final int SECONDS = 30;

  private SoundTest() {}

  public static void main(final String[] args) {
    final AudioThread audioThread = new AudioThread(true, 24, InterpolationPrecision.Double, PitchResolution.Quadruple, SampleRate._48000, EffectsOverTimeGranularity.Finer);

    final Thread spuThread = new Thread(audioThread);
    spuThread.setName("SPU");

    final BackgroundMusic bgm = new BackgroundMusic(Loader.loadDirectory("SECT/DRGN0.BIN/" + FILE_INDEX), FILE_INDEX, audioThread.getSequencer().getSampleRate());

    spuThread.start();

    audioThread.loadBackgroundMusic(bgm);

    audioThread.startSequence();

    DebugHelper.sleep(SECONDS * 1000);

    audioThread.stop();
  }
}
