package legend.game.saves;

import legend.core.memory.types.IntRef;
import legend.core.tags.BoolTag;
import legend.core.tags.FloatTag;
import legend.core.tags.IntTag;
import legend.core.tags.ListTag;
import legend.core.tags.MapTag;
import legend.core.tags.Tag;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.lodmod.LodEngineStateTypes.SUBMAP;
import static legend.lodmod.LodEngineStateTypes.WORLD_MAP;

public final class SaveMigrator {
  private SaveMigrator() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(SaveMigrator.class);

  private static final int WMAP_SAVE_VERSION_1 = 'V' | '1' << 8;
  private static final int SMAP_SAVE_VERSION_2 = 'V' | '2' << 8;

  public static Tag upgradeEngineStateData(final RegistryId engineStateTypeId, final FileData data, final int fallbackScene, final int fallbackCut) {
    final MapTag tag = new MapTag();

    if(engineStateTypeId.equals(WORLD_MAP.getId())) {
      // no data - legacy saves
      if(data.size() == 0) {
        return tag;
      }

      if(data.size() < 2) {
        LOGGER.warn("Failed to load WMAP data for save");
        return tag;
      }

      final IntRef offset = new IntRef();
      final int version = data.readUShort(offset);

      if(version != WMAP_SAVE_VERSION_1) {
        LOGGER.warn("Unknown WMAP save data version");
        return tag;
      }

      tag.set("pathIndex", new IntTag(data.readUShort(offset)));
      tag.set("dotIndex", new IntTag(data.readUShort(offset)));
      tag.set("dotOffset", new FloatTag(data.readUByte(offset)));
      tag.set("facing", new IntTag(data.readByte(offset)));
      tag.set("directionalPathIndex", new IntTag(data.readUShort(offset)));
    } else if(engineStateTypeId.equals(SUBMAP.getId())) {
      // no data - legacy saves
      if(data.size() == 0) {
        tag.set("scene", new IntTag(fallbackScene));
        tag.set("cut", new IntTag(fallbackCut));
        return tag;
      }

      if(data.size() < 2) {
        LOGGER.warn("Failed to load SMAP data for save");
        return tag;
      }

      final IntRef offset = new IntRef();
      final int version = data.readUShort(offset);

      tag.set("scene", new IntTag(data.readInt(offset)));
      tag.set("cut", new IntTag(data.readInt(offset)));
      tag.set("indicatorsDisabled", new BoolTag(data.readBool(offset)));

      if(version == SMAP_SAVE_VERSION_2) {
        final ListTag primaryPartyBackupTag = new ListTag();
        tag.set("primaryPartyBackup", primaryPartyBackupTag);

        final int count = data.readInt(offset);
        for(int i = 0; i < count; i++) {
          primaryPartyBackupTag.add(new IntTag(data.readInt(offset)));
        }
      }
    }

    return tag;
  }
}
