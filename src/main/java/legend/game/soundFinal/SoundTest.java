package legend.game.soundFinal;

import legend.core.DebugHelper;
import legend.core.openal.Context;
import legend.game.sound.Sssq;
import legend.game.unpacker.Unpacker;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class SoundTest {
  public static void main(final String[] args) {
    final Context context = new Context();

    final Bgm bgm = new Bgm(new Sssq(Unpacker.loadFile("SECT/DRGN0.BIN/5820/1")), Unpacker.loadFile("SECT/DRGN0.BIN/5820/2"), Unpacker.loadFile("SECT/DRGN0.BIN/5820/3"));
    Offsets.genOffsets();

    bgm.tick(2205);
    bgm.play();

    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        bgm.tick(2205);
      }
    };

    ScheduledExecutorService executor  = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(runnable, 0, 50, TimeUnit.MILLISECONDS);

    DebugHelper.sleep(300000);

    context.destroy();
  }

}
