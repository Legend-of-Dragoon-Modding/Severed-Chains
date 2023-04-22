package legend.game.soundSfx;


import legend.core.DebugHelper;
import legend.core.audio.AudioThread;

import java.io.IOException;

//TODO
// See sssqTempo_800bd104
// See code @ LAB_8001dda0
// Decay ((lo & 0xf) + 1) * 0x800

public final class SoundTest {
  public static void main(final String[] args) {

    final AudioThread audioThread = new AudioThread(100, true, 24);

    final BackgroundMusic bgm = SoundFactory.backgroundMusic(5815);

    final Thread spuThread = new Thread(audioThread);
    spuThread.setName("SPU");

    audioThread.loadBackgroundMusic(bgm);

    spuThread.start();

    try {
      System.in.read();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }

    audioThread.stop();
  }
}
