package legend.game.soundSfx;


import legend.core.DebugHelper;
import legend.core.audio.AudioThread;

public final class SoundTest {
  public static void main(final String[] args) {

    final AudioThread audioThread = new AudioThread(100, true, 24);

    final BackgroundMusic bgm = SoundFactory.backgroundMusic("SECT\\DRGN0.BIN\\5820");

    final Thread spuThread = new Thread(audioThread);
    spuThread.setName("SPU");

    audioThread.LoadBackgroundMusic(bgm);

    spuThread.start();

    DebugHelper.sleep(100000);

    audioThread.stop();
  }
}
