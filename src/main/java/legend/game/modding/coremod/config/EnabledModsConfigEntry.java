package legend.game.modding.coremod.config;

import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;
import legend.game.unpacker.FileData;

public class EnabledModsConfigEntry extends ConfigEntry<String[]> {
  public EnabledModsConfigEntry() {
    super(new String[0], ConfigStorageLocation.CAMPAIGN, ConfigCategory.OTHER, EnabledModsConfigEntry::serializer, EnabledModsConfigEntry::deserializer);
  }

  private static byte[] serializer(final String[] ids) {
    int size = 2; // length header

    for(final String id : ids) {
      size += id.length() + 1; // strlen + length header
    }

    final FileData data = new FileData(new byte[size]);
    data.writeShort(0, ids.length);

    int offset = 2;
    for(final String id : ids) {
      data.writeAscii(offset, id, 1);
      offset += id.length() + 1;
    }

    return data.getBytes();
  }

  private static String[] deserializer(final byte[] bytes) {
    if(bytes.length >= 2) {
      final FileData data = new FileData(bytes);

      final int length = data.readUShort(0);
      final String[] ids = new String[length];
      int offset = 2;

      for(int i = 0; i < length; i++) {
        ids[i] = data.readAscii(offset, 1).replace("lod-core", "lod_core");
        offset += ids[i].length() + 1;
      }

      return ids;
    }

    return new String[0];
  }
}
