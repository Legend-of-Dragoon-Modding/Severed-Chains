package legend.core.audio;

import legend.core.DebugHelper;
import legend.core.audio.sequencer.assets.BackgroundMusic;
import legend.game.unpacker.Unpacker;

public final class SoundTest {
  private static final int FILE_INDEX = 5820;
  private static final int SECONDS = 30;
  public static void main(final String[] args) {
    final AudioThread audioThread = new AudioThread(100, true, 24, 9);

    final Thread spuThread = new Thread(audioThread);
    spuThread.setName("SPU");

    final BackgroundMusic bgm = new BackgroundMusic(Unpacker.loadDirectory("SECT/DRGN0.BIN/" + FILE_INDEX), FILE_INDEX);

    spuThread.start();

    audioThread.loadBackgroundMusic(bgm);

    audioThread.startSequence();

    DebugHelper.sleep(SECONDS * 1000);

    audioThread.stop();
  }
}
