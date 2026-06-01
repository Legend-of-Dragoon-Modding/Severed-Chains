package legend.core.platform.input;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.FileData;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public class KeyInputActivation extends InputActivation {
  public final InputKey key;
  public final Set<InputMod> mods = EnumSet.noneOf(InputMod.class);

  public KeyInputActivation(final InputKey key, final InputMod... mods) {
    this.key = key;
    this.mods.addAll(Arrays.asList(mods));
  }

  public KeyInputActivation(final FileData in, final IntRef offset) {
    this.key = InputKey.valueOf(in.readAscii(offset, 1));

    final int count = in.readUByte(offset);

    for(int i = 0; i < count; i++) {
      this.mods.add(InputMod.valueOf(in.readAscii(offset, 1)));
    }
  }

  @Override
  public boolean isSimilar(final InputActivation other) {
    return
      other instanceof final KeyInputActivation key && this.key == key.key && this.mods.equals(key.mods) ||
      other instanceof final ScancodeInputActivation scancode && this.key == scancode.key && this.mods.equals(scancode.mods);
  }

  @Override
  public void serialize(final FileData out, final IntRef offset) {
    out.writeAscii(offset, "key", 1);
    out.writeAscii(offset, this.key.name(), 1);
    out.writeByte(offset, this.mods.size());

    for(final InputMod mod : this.mods) {
      out.writeAscii(offset, mod.name(), 1);
    }
  }
}
