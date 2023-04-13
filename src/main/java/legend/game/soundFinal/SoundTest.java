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
    final String file = "SECT/DRGN0.BIN/5850";
    final Bgm bgm = new Bgm(new Sssq(Unpacker.loadFile(file + "/1")), Unpacker.loadFile(file + "/2"), Unpacker.loadFile(file + "/3"));
    Offsets.genOffsets();

    bgm.tick(2205);
    bgm.play();

    final long soundTime = 1_000_000_000 / 10;
    long time = System.nanoTime();

    boolean running = true;
    while(running) {
      bgm.tick(4410);

      final long interval = System.nanoTime() - time;
      final int toSleep = (int)Math.max(0, 100_000_000 - interval) / 1_000_000;
      DebugHelper.sleep(toSleep);
      time += soundTime;
    }

    context.destroy();
  }
}
