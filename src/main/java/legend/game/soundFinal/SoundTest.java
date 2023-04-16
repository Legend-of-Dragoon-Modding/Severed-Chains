package legend.game.soundFinal;

import legend.core.DebugHelper;
import legend.core.openal.Context;
import legend.game.sound.Sssq;
import legend.game.unpacker.Unpacker;

public final class SoundTest {
  public static void main(final String[] args) {
    final Context context = new Context();

    // Title: 5820
    // World map 1: 5850 (pitch issues)
    final String file = "SECT/DRGN0.BIN/5820";
    final Bgm bgm = new Bgm(new Sssq(Unpacker.loadFile(file + "/1")), Unpacker.loadFile(file + "/2"), Unpacker.loadFile(file + "/3"));
    Offsets.genOffsets();



    final int frequency = 50;
    final long soundTime = 1_000_000_000 / frequency;
    final int ticks = 44100 / frequency;

    bgm.tick(ticks * 2);
    bgm.play();

    long time = System.nanoTime();

    boolean running = true;
    while(running) {
      running = bgm.tick(ticks);

      final long interval = System.nanoTime() - time;
      final int toSleep = (int)Math.max(0, soundTime - interval) / 1_000_000;
      DebugHelper.sleep(toSleep);
      time += soundTime;
    }

    bgm.stop();
    bgm.destroy();

    context.destroy();
  }
}
