package legend.game.saves.serializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter;
import legend.core.memory.types.IntRef;
import legend.game.saves.Campaign;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.InventoryEntry;
import legend.game.saves.RetailSavedGame;
import legend.game.saves.SaveVersion;
import legend.game.saves.SavedGame;
import legend.game.saves.SeveredSavedCharacterV1;
import legend.game.types.EquipmentSlot;
import legend.game.unpacker.FileData;
import legend.lodmod.Legacy;
import legend.lodmod.LodEngineStateTypes;
import legend.lodmod.LodMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.nio.charset.StandardCharsets;

import static legend.game.Scus94491BpeSegment_8004.CHARACTER_ADDITIONS;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.lodmod.LodMod.getLocationName;

public final class LegacySerializer {
  private LegacySerializer() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(LegacySerializer.class);

  private static final Gson jsonSerializer = new GsonBuilder().addReflectionAccessFilter(rawClass -> ReflectionAccessFilter.FilterResult.BLOCK_ALL).create();

  public static SavedGame fromV2To7(final SaveVersion version, final Campaign campaign, final String filename, final FileData data) {
    final IntRef offset = new IntRef();
    final String name;

    if(version.ordinal() >= SaveVersion.V3.ordinal()) {
      name = data.readAscii(offset);
    } else {
      name = filename;
    }

    final ConfigCollection config = new ConfigCollection();
    final RetailSavedGame savedGame = new RetailSavedGame(campaign, version.name, filename, name, LodMod.RETAIL_CAMPAIGN_TYPE.getId(), config);

    int maxHp = 0;
    int maxMp = 0;
    if(version.ordinal() >= SaveVersion.V3.ordinal()) {
      maxHp = data.readInt(offset);
      maxMp = data.readInt(offset);
    }

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
        savedGame.charIndices.add(charId);
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

    if(version.ordinal() <= SaveVersion.V5.ordinal()) {
      for(int i = 0; i < 2; i++) {
        final int packed = data.readInt(offset);

        for(int bit = 0; bit < 32; bit++) {
          if((packed & (1 << bit)) != 0) {
            savedGame.goodsIds.add(LodMod.id(LodMod.GOODS_IDS[i * 32 + bit]));
          }
        }
      }
    }

    for(int i = 0; i < savedGame._1a4.length; i++) {
      savedGame._1a4[i] = data.readInt(offset);
    }

    for(int i = 0; i < savedGame.chestFlags.length; i++) {
      savedGame.chestFlags[i] = data.readInt(offset);
    }

    final int equipmentCount = data.readUShort(offset);
    final int itemCount = data.readUShort(offset);
    final int goodsCount;

    if(version.ordinal() >= SaveVersion.V6.ordinal()) {
      goodsCount = data.readUShort(offset);
    } else {
      goodsCount = 0;
    }

    for(int i = 0; i < equipmentCount; i++) {
      final RegistryId equipmentId;
      if(version.ordinal() >= SaveVersion.V4.ordinal()) {
        equipmentId = data.readRegistryId(offset);
      } else {
        final int id = data.readUByte(offset);
        final String idStr = LodMod.EQUIPMENT_IDS[id];

        if(idStr.isBlank()) {
          LOGGER.warn("Skipping unknown equipment ID %#x", id);
          continue;
        }

        equipmentId = LodMod.id(idStr);
      }

      savedGame.equipmentIds.add(equipmentId);
    }

    for(int i = 0; i < itemCount; i++) {
      final RegistryId itemId;

      if(version.ordinal() >= SaveVersion.V4.ordinal()) {
        itemId = data.readRegistryId(offset);
      } else {
        final int id = data.readUByte(offset);
        final String idStr = LodMod.ITEM_IDS[id - 192];

        if(idStr.isBlank()) {
          LOGGER.warn("Skipping unknown item ID %#x", id);
          continue;
        }

        itemId = LodMod.id(idStr);
      }

      final int size;
      final int durability;
      JsonObject extraData = null;
      if(version.ordinal() >= SaveVersion.V5.ordinal()) {
        size = data.readInt(offset);
        durability = data.readInt(offset);
        final int extraDataSize = data.readInt(offset);

        if(extraDataSize != 0) {
          final byte[] serialized = new byte[extraDataSize];
          data.read(offset, serialized, 0, serialized.length);

          try {
            extraData = jsonSerializer.fromJson(new String(serialized, StandardCharsets.UTF_8), JsonObject.class);
          } catch(final JsonSyntaxException e) {
            LOGGER.error("Failed to load extra data for instance of item " + itemId, e);
          }
        }
      } else {
        size = 1;
        durability = 1;
      }

      savedGame.itemIds.add(new InventoryEntry(itemId, size, durability, extraData));
    }

    for(int i = 0; i < goodsCount; i++) {
      savedGame.goodsIds.add(data.readRegistryId(offset));
    }

    final int charDataCount = data.readUShort(offset); // Not yet used

    for(int charIndex = 0; charIndex < savedGame.characters.length; charIndex++) {
      final RetailSavedGame.SavedCharacter charData = savedGame.characters[charIndex];
      charData.xp = data.readInt(offset);
      charData.flags = data.readInt(offset);
      charData.hp = data.readInt(offset);
      charData.mp = data.readInt(offset);
      charData.sp = data.readInt(offset);
      charData.dlevelXp = data.readInt(offset);
      charData.status = data.readInt(offset);
      charData.level = data.readUShort(offset);
      charData.dlevel = data.readUShort(offset);

      final int equipmentSlotCount;
      if(version.ordinal() >= SaveVersion.V4.ordinal()) {
        equipmentSlotCount = data.readByte(offset);
      } else {
        equipmentSlotCount = 5;
      }

      for(int i = 0; i < equipmentSlotCount; i++) {
        final EquipmentSlot slot;
        final RegistryId equipmentId;

        if(version.ordinal() >= SaveVersion.V4.ordinal()) {
          slot = EquipmentSlot.valueOf(data.readAscii(offset));
          equipmentId = data.readRegistryId(offset);
        } else {
          slot = EquipmentSlot.fromLegacy(i);
          equipmentId = LodMod.id(LodMod.EQUIPMENT_IDS[data.readUByte(offset)]);
        }

        charData.equipmentIds.put(slot, equipmentId);
      }

      if(version.ordinal() >= SaveVersion.V7.ordinal()) {
        charData.selectedAddition = data.readRegistryId(offset);
      } else {
        final int oldAdditionIndex = data.readShort(offset) - additionOffsets_8004f5ac[charIndex];

        if(CHARACTER_ADDITIONS[charIndex].length != 0) {
          if(oldAdditionIndex >= 0) {
            charData.selectedAddition = CHARACTER_ADDITIONS[charIndex][oldAdditionIndex].getId();
          } else {
            charData.selectedAddition = CHARACTER_ADDITIONS[charIndex][0].getId();
          }
        }
      }

      final int additionCount = data.readShort(offset);

      if(version.ordinal() >= SaveVersion.V7.ordinal()) {
        for(int additionIndex = 0; additionIndex < additionCount; additionIndex++) {
          final RegistryId id = data.readRegistryId(offset);
          final int level = data.readShort(offset);
          final int xp = data.readInt(offset);
          charData.additionInfo.put(id, new SeveredSavedCharacterV1.AdditionInfo(level, xp));
        }
      } else {
        for(int additionIndex = 0; additionIndex < 8; additionIndex++) {
          final int level = data.readShort(offset);
          final int xp = data.readInt(offset);

          if(additionIndex < CHARACTER_ADDITIONS[charIndex].length) {
            final RegistryId additionId = CHARACTER_ADDITIONS[charIndex][additionIndex].getId();
            charData.additionInfo.put(additionId, new SeveredSavedCharacterV1.AdditionInfo(Math.max(0, level - 1), xp));
          }
        }
      }
    }

    if(version.ordinal() < SaveVersion.V3.ordinal()) {
      final RetailSavedGame.SavedCharacter charData = savedGame.characters[savedGame.charIndices.getInt(0)];
      maxHp = Legacy.RETAIL_HP[savedGame.charIndices.getInt(0)][charData.level - 1];
      maxMp = charData.dlevel * 20;
    }

    if(version.ordinal() <= SaveVersion.V4.ordinal()) {
      for(int i = 0; i < 8; i++) {
        /*savedGame._4b8[i] = */data.readInt(offset);
      }
    }

    savedGame.pathIndex = data.readUShort(offset);
    savedGame.dotIndex = data.readUShort(offset);
    savedGame.dotOffset = data.readUByte(offset);
    savedGame.facing = data.readByte(offset);
    savedGame.directionalPathIndex = data.readUShort(offset);
    savedGame.characterInitialized = data.readInt(offset);
    final boolean isOnWorldMap = data.readUByte(offset) != 0;

    if(version.ordinal() <= SaveVersion.V4.ordinal()) {
      /*savedGame.mono_4e0 =*/ data.readUByte(offset) /*!= 0*/;
      /*savedGame.vibrationEnabled_4e1 =*/ data.readUByte(offset) /*!= 0*/;
      /*savedGame.morphMode_4e2 =*/ data.readUByte(offset);
    }

    savedGame.indicatorsDisabled = data.readUByte(offset) != 0;

    ConfigStorage.loadConfig(config, ConfigStorageLocation.SAVE, data.slice(offset.get()));

    savedGame.locationName = getLocationName(locationType, locationIndex);
    savedGame.engineState = isOnWorldMap ? LodEngineStateTypes.WORLD_MAP.getId() : LodEngineStateTypes.SUBMAP.getId();
    savedGame.maxHp = maxHp;
    savedGame.maxMp = maxMp;

    return savedGame;
  }
}
