package legend.game.saves.serializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter;
import legend.core.memory.types.IntRef;
import legend.game.additions.CharacterAdditionStats;
import legend.game.saves.Campaign;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.InventoryEntry;
import legend.game.saves.RetailSavedGame;
import legend.game.saves.SavedGame;
import legend.game.types.EquipmentSlot;
import legend.game.unpacker.FileData;
import legend.lodmod.LodEngineStateTypes;
import legend.lodmod.LodMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.nio.charset.StandardCharsets;

import static legend.lodmod.LodMod.getLocationName;

public final class V7Serializer {
  private V7Serializer() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(V7Serializer.class);

  public static final int MAGIC_V7 = 0x37615344; // DSa7

  private static final Gson jsonSerializer = new GsonBuilder().addReflectionAccessFilter(rawClass -> ReflectionAccessFilter.FilterResult.BLOCK_ALL).create();

  public static FileData fromV7Matcher(final FileData data) {
    if(data.readInt(0) == MAGIC_V7) {
      return data.slice(0x4);
    }

    return null;
  }

  public static SavedGame fromV7(final Campaign campaign, final String filename, final FileData data) {
    final IntRef offset = new IntRef();
    final String name = data.readAscii(offset);

    final ConfigCollection config = new ConfigCollection();
    final RetailSavedGame savedGame = new RetailSavedGame(campaign, "V7", filename, name, LodMod.RETAIL_CAMPAIGN_TYPE.getId(), config);

    final int maxHp = data.readInt(offset);
    final int maxMp = data.readInt(offset);

    final int locationType = data.readUByte(offset);
    final int locationIndex = data.readUShort(offset);

    /*savedGame._04 = */data.readInt(offset);

    for(int i = 0; i < savedGame.scriptData.length; i++) {
      savedGame.scriptData[i] = data.readInt(offset);
    }

    final int charSlotCount = data.readByte(offset);

    for(int i = 0; i < charSlotCount; i++) {
      final int charId = data.readShort(offset);

      if(charId != -1) {
        savedGame.charIds.add(charId);
      }
    }

    savedGame.gold = data.readInt(offset);
    savedGame.chapterIndex = data.readInt(offset);
    savedGame.stardust = data.readInt(offset);
    savedGame.timestamp = data.readInt(offset);
    savedGame.submapScene = data.readInt(offset);
    savedGame.submapCut = data.readInt(offset);

    savedGame._b0 = data.readInt(offset);
    savedGame.battleCount = data.readInt(offset);
    savedGame.turnCount = data.readInt(offset);

    for(int i = 0; i < savedGame.scriptFlags2.count(); i++) {
      savedGame.scriptFlags2.setRaw(i, data.readInt(offset));
    }

    for(int i = 0; i < savedGame.scriptFlags1.count(); i++) {
      savedGame.scriptFlags1.setRaw(i, data.readInt(offset));
    }

    for(int i = 0; i < savedGame.wmapFlags.count(); i++) {
      savedGame.wmapFlags.setRaw(i, data.readInt(offset));
    }

    for(int i = 0; i < savedGame.visitedLocations.count(); i++) {
      savedGame.visitedLocations.setRaw(i, data.readInt(offset));
    }

    for(int i = 0; i < savedGame._1a4.length; i++) {
      savedGame._1a4[i] = data.readInt(offset);
    }

    for(int i = 0; i < savedGame.chestFlags.length; i++) {
      savedGame.chestFlags[i] = data.readInt(offset);
    }

    final int equipmentCount = data.readUShort(offset);
    final int itemCount = data.readUShort(offset);
    final int goodsCount = data.readUShort(offset);

    for(int i = 0; i < equipmentCount; i++) {
      savedGame.equipmentIds.add(data.readRegistryId(offset));
    }

    for(int i = 0; i < itemCount; i++) {
      final RegistryId itemId = data.readRegistryId(offset);
      final int size = data.readInt(offset);
      final int durability = data.readInt(offset);

      final int extraDataSize = data.readInt(offset);
      JsonObject extraData = null;

      if(extraDataSize != 0) {
        final byte[] serialized = new byte[extraDataSize];
        data.read(offset, serialized, 0, serialized.length);

        try {
          extraData = jsonSerializer.fromJson(new String(serialized, StandardCharsets.UTF_8), JsonObject.class);
        } catch(final JsonSyntaxException e) {
          LOGGER.error("Failed to load extra data for instance of item " + itemId, e);
        }
      }

      savedGame.itemIds.add(new InventoryEntry(itemId, size, durability, extraData));
    }

    for(int i = 0; i < goodsCount; i++) {
      savedGame.goodsIds.add(data.readRegistryId(offset));
    }

    final int charDataCount = data.readUShort(offset); // Not yet used

    for(int charIndex = 0; charIndex < savedGame.charStats.length; charIndex++) {
      final RetailSavedGame.CharStats charData = savedGame.charStats[charIndex];
      charData.xp = data.readInt(offset);
      charData.flags = data.readInt(offset);
      charData.hp = data.readInt(offset);
      charData.mp = data.readInt(offset);
      charData.sp = data.readInt(offset);
      charData.dlevelXp = data.readInt(offset);
      charData.status = data.readInt(offset);
      charData.level = data.readUShort(offset);
      charData.dlevel = data.readUShort(offset);

      final int equipmentSlotCount = data.readByte(offset);

      for(int i = 0; i < equipmentSlotCount; i++) {
        final String slotName = data.readAscii(offset);
        final RegistryId equipmentId = data.readRegistryId(offset);
        final EquipmentSlot slot = EquipmentSlot.valueOf(slotName);

        charData.equipmentIds.put(slot, equipmentId);
      }

      charData.selectedAddition = data.readRegistryId(offset);

      final int additionCount = data.readShort(offset);

      for(int additionIndex = 0; additionIndex < additionCount; additionIndex++) {
        final RegistryId id = data.readRegistryId(offset);
        final int level = data.readShort(offset);
        final int xp = data.readInt(offset);

        final CharacterAdditionStats stats = new CharacterAdditionStats();
        stats.level = level;
        stats.xp = xp;
        charData.additionStats.put(id, stats);
      }
    }

    savedGame.pathIndex = data.readUShort(offset);
    savedGame.dotIndex = data.readUShort(offset);
    savedGame.dotOffset = data.readUByte(offset);
    savedGame.facing = data.readByte(offset);
    savedGame.directionalPathIndex = data.readUShort(offset);
    savedGame.characterInitialized = data.readInt(offset);
    final boolean isOnWorldMap = data.readUByte(offset) != 0;
    savedGame.indicatorsDisabled = data.readUByte(offset) != 0;

    ConfigStorage.loadConfig(config, ConfigStorageLocation.SAVE, data.slice(offset.get()));

    savedGame.locationName = getLocationName(locationType, locationIndex);
    savedGame.engineState = isOnWorldMap ? LodEngineStateTypes.WORLD_MAP.getId() : LodEngineStateTypes.SUBMAP.getId();
    savedGame.maxHp = maxHp;
    savedGame.maxMp = maxMp;

    return savedGame;
  }
}
