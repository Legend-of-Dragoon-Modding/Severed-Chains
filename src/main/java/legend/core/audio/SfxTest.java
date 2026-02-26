package legend.core.audio;

import legend.core.DebugHelper;
import legend.core.audio.sequencer.assets.BackgroundMusic;
import legend.core.audio.sequencer.assets.Sfx;
import legend.game.unpacker.Loader;

public final class SfxTest {

  private SfxTest() {}

  public static void main(final String[] args) {


    final Sfx sfx = new Sfx(Loader.loadDirectory("characters/haschel/sounds/combat"));

    System.out.println("T");
  }
}