package legend.core.audio;

import legend.core.DebugHelper;
import legend.core.audio.assets.SequencedAudio;
import legend.core.audio.assets.SoundFactory;

public final class SoundTest {
  public static void main(final String[] args) {
    final AudioThread audioThread = new AudioThread(100, false, 24);
    final SequencedAudio bgm = SoundFactory.backgroundMusic(5820);

    audioThread.loadSequence(bgm, 0);

    final Thread spuThread = new Thread(audioThread);
    spuThread.setName("SPU");

    spuThread.start();

    DebugHelper.sleep(100000);

    audioThread.stop();
  }
}
