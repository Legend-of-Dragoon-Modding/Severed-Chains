package legend.game.soundSfx;


import legend.core.DebugHelper;
import legend.core.audio.AudioThread;

public final class SoundTest {
  public static void main(final String[] args) {

    final AudioThread audioThread = new AudioThread(50, true, 24);

    final BackgroundMusic bgm = SoundFactory.backgroundMusic("SECT\\DRGN0.BIN\\5820");

    audioThread.LoadBackgroundMusic(bgm);

    audioThread.run();

    DebugHelper.sleep(10000);

    audioThread.stop();
  }
}
