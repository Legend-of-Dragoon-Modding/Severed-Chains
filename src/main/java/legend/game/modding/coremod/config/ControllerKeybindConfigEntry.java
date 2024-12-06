package legend.game.modding.coremod.config;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

public class ControllerKeybindConfigEntry extends ConfigEntry<IntSet> {
  public final boolean required;

  public ControllerKeybindConfigEntry(final boolean required, final int... defaultValue) {
    super(new IntOpenHashSet(defaultValue), ConfigStorageLocation.GLOBAL, ConfigCategory.CONTROLS, ControllerKeybindConfigEntry::serializer, data -> deserializer(data, new IntOpenHashSet(defaultValue)));
    this.required = required;
  }

  private static byte[] serializer(final IntSet val) {
    final byte[] data = new byte[1 + val.size() * 2];
    MathHelper.set(data, 0, 1, val.size());

    int i = 0;
    for(final int keybind : val) {
      MathHelper.set(data, 1 + i * 2, 2, keybind);
      i++;
    }

    return data;
  }

  private static IntSet deserializer(final byte[] data, final IntSet defaultValue) {
    if(data.length >= 1) {
      final int length = IoHelper.readUByte(data, 0);

      if(data.length == 1 + length * 2) {
        final IntSet vals = new IntOpenHashSet();

        for(int i = 0; i < length; i++) {
          vals.add(IoHelper.readUShort(data, 1 + i * 2));
        }

        return vals;
      }
    }

    return defaultValue;
  }
}
