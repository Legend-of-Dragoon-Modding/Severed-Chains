package legend.game.sound2;

import legend.core.DebugHelper;
import legend.core.openal.Context;
import legend.game.sound.Sssq;
import legend.game.unpacker.Unpacker;

public class SoundTest {
  public static void main(final String[] args) {
    final Context context = new Context();

    //TODO Remove this and blame Illeprih

    final Bgm bgm = new Bgm(new Sssq(Unpacker.loadFile("SECT/DRGN0.BIN/5820/1")), Unpacker.loadFile("SECT/DRGN0.BIN/5820/2"), Unpacker.loadFile("SECT/DRGN0.BIN/5820/3"));
    bgm.tick();

    DebugHelper.sleep(5000);
    context.destroy();
  }
}
