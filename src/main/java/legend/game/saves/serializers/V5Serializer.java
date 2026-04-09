package legend.game.saves.serializers;

import legend.core.memory.types.IntRef;
import legend.game.additions.Addition;
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
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.game.Scus94491BpeSegment_8004.CHARACTER_ADDITIONS;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.lodmod.LodMod.getLocationName;

public final class V5Serializer {
  private V5Serializer() { }

  public static final int MAGIC_V5 = 0x35615344; // DSa5

  public static FileData fromV5Matcher(final FileData data) {
    if(data.readInt(0) == MAGIC_V5) {
      return data.slice(0x4);
    }

    return null;
  }

  public static SavedGame fromV5(final Campaign campaign, final String filename, final FileData data) {
    final IntRef offset = new IntRef();
    final String name = data.readAscii(offset);

    final ConfigCollection config = new ConfigCollection();
    final RetailSavedGame savedGame = new RetailSavedGame(campaign, "V5", filename, name, LodMod.RETAIL_CAMPAIGN_TYPE.getId(), config);

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

    for(int i = 0; i < 2; i++) {
      final int packed = data.readInt(offset);

      for(int bit = 0; bit < 32; bit++) {
        if((packed & (1 << bit)) != 0) {
          savedGame.goodsIds.add(LodMod.id(LodMod.GOODS_IDS[i * 32 + bit]));
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

    for(int i = 0; i < equipmentCount; i++) {
      savedGame.equipmentIds.add(data.readRegistryId(offset));
    }

    for(int i = 0; i < itemCount; i++) {
      final RegistryId itemId = data.readRegistryId(offset);
      final int size = data.readInt(offset);
      final int durability = data.readInt(offset);
      // Unused for now but may be used as the length header for extra item data in the future
      final int extraDataHeader = data.readInt(offset);
      savedGame.itemIds.add(new InventoryEntry(itemId, size, durability));
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

      final int oldAdditionIndex = data.readShort(offset) - additionOffsets_8004f5ac[charIndex];

      if(CHARACTER_ADDITIONS[charIndex].length != 0) {
        if(oldAdditionIndex >= 0) {
          charData.selectedAddition = CHARACTER_ADDITIONS[charIndex][oldAdditionIndex].getId();
        } else {
          charData.selectedAddition = CHARACTER_ADDITIONS[charIndex][0].getId();
        }
      }

      final int additionCount = data.readShort(offset); // Not yet used

      for(int additionIndex = 0; additionIndex < 8; additionIndex++) {
        final int level = data.readShort(offset);
        final int xp = data.readInt(offset);

        if(additionIndex < CHARACTER_ADDITIONS[charIndex].length) {
          final RegistryDelegate<Addition> addition = CHARACTER_ADDITIONS[charIndex][additionIndex];
          final CharacterAdditionStats stats = new CharacterAdditionStats();
          stats.level = Math.max(0, level - 1);
          stats.xp = xp;
          charData.additionStats.put(addition.getId(), stats);
        }
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
