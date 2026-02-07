package legend.game.saves.serializers;

import legend.game.additions.Addition;
import legend.game.additions.CharacterAdditionStats;
import legend.game.saves.Campaign;
import legend.game.saves.ConfigCollection;
import legend.game.saves.InventoryEntry;
import legend.game.saves.MemcardSavedGame;
import legend.game.saves.RetailSavedGame;
import legend.game.saves.SavedGame;
import legend.game.types.EquipmentSlot;
import legend.game.unpacker.FileData;
import legend.lodmod.LodEngineStateTypes;
import legend.lodmod.LodMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.Scus94491BpeSegment_8004.CHARACTER_ADDITIONS;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.lodmod.LodMod.getLocationName;

public final class RetailSerializer {
  private RetailSerializer() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(RetailSerializer.class);

  public static final int MAGIC_RETAIL = 0x01114353; // SC__

  public static FileData fromRetailMatcher(final FileData data) {
    if(data.readInt(0) == MAGIC_RETAIL) {
      return data.slice(0x4);
    }

    return null;
  }

  public static SavedGame fromRetail(final Campaign campaign, final String name, final FileData data) {
    final MemcardSavedGame savedGame = new MemcardSavedGame(campaign, name, name, LodMod.RETAIL_CAMPAIGN_TYPE.getId(), new ConfigCollection());

    deserializeRetailGameState(savedGame, data.slice(0x1fc));

    savedGame.locationType = data.readUByte(0x1a9);
    savedGame.locationIndex = data.readUByte(0x1a8);
    savedGame.locationName = getLocationName(savedGame.locationType, savedGame.locationIndex);

    return savedGame;
  }

  public static void deserializeRetailGameState(final RetailSavedGame savedGame, final FileData data) {
    for(int i = 0; i < 0x20; i++) {
      savedGame.scriptData[i] = data.readInt(0x8 + i * 0x4);
    }

    for(int i = 0; i < 3; i++) {
      final int charId = data.readInt(0x88 + i * 0x4);

      if(charId != -1) {
        savedGame.charIds.add(charId);
      }
    }

    savedGame.gold = data.readInt(0x94);
    savedGame.chapterIndex = data.readInt(0x98);
    savedGame.stardust = data.readInt(0x9c);
    savedGame.timestamp = data.readInt(0xa0);
    savedGame.submapScene = data.readInt(0xa4);
    savedGame.submapCut = data.readInt(0xa8);

    savedGame._b0 = data.readInt(0xb0);
    savedGame.battleCount = data.readInt(0xb4);
    savedGame.turnCount = data.readInt(0xb8);

    for(int i = 0; i < 0x20; i++) {
      savedGame.scriptFlags2.setRaw(i, data.readInt(0xbc + i * 0x4));
    }

    for(int i = 0; i < 8; i++) {
      savedGame.scriptFlags1.setRaw(i, data.readInt(0x13c + i * 0x4));
    }

    for(int i = 0; i < 8; i++) {
      savedGame.wmapFlags.setRaw(i, data.readInt(0x15c + i * 0x4));
    }

    for(int i = 0; i < 8; i++) {
      savedGame.visitedLocations.setRaw(i, data.readInt(0x17c + i * 0x4));
    }

    for(int i = 0; i < 2; i++) {
      final int packed = data.readInt(0x19c + i * 0x4);

      for(int bit = 0; bit < 32; bit++) {
        if((packed & (1 << bit)) != 0) {
          savedGame.goodsIds.add(LodMod.id(LodMod.GOODS_IDS[i * 32 + bit]));
        }
      }
    }

    for(int i = 0; i < 8; i++) {
      savedGame._1a4[i] = data.readInt(0x1a4 + i * 0x4);
    }

    for(int i = 0; i < 8; i++) {
      savedGame.chestFlags[i] = data.readInt(0x1c4 + i * 0x4);
    }

    for(int i = 0; i < 255; i++) {
      final int id = data.readUByte(0x1e8 + i);

      if(id == 0xff) {
        break;
      }

      final String idStr = LodMod.EQUIPMENT_IDS[id];

      if(idStr.isBlank()) {
        LOGGER.warn("Skipping unknown equipment ID %#x", id);
        continue;
      }

      savedGame.equipmentIds.add(LodMod.id(idStr));
    }

    for(int i = 0; i < 64; i++) {
      final int id = data.readUByte(0x2e9 + i);

      if(id == 0xff) {
        break;
      }

      final String idStr = LodMod.ITEM_IDS[id - 192];

      if(idStr.isBlank()) {
        LOGGER.warn("Skipping unknown item ID %#x", id);
        continue;
      }

      savedGame.itemIds.add(new InventoryEntry(LodMod.id(idStr), 1, 1));
    }

    for(int charSlot = 0; charSlot < 9; charSlot++) {
      final RetailSavedGame.CharStats charData = savedGame.charStats[charSlot];
      final FileData charSlice = data.slice(0x32c + charSlot * 0x2c, 0x2c);

      charData.xp = charSlice.readInt(0x0);
      charData.flags = charSlice.readInt(0x4);
      charData.hp = charSlice.readUShort(0x8);
      charData.mp = charSlice.readUShort(0xa);
      charData.sp = charSlice.readUShort(0xc);
      charData.dlevelXp = charSlice.readUShort(0xe);
      charData.status = charSlice.readUShort(0x10);
      charData.level = charSlice.readUByte(0x12);
      charData.dlevel = charSlice.readUByte(0x13);

      for(int i = 0; i < 5; i++) {
        charData.equipmentIds.put(EquipmentSlot.fromLegacy(i), LodMod.id(LodMod.EQUIPMENT_IDS[charSlice.readUByte(0x14 + i)]));
      }

      final int oldAdditionIndex = data.readByte(0x19) - additionOffsets_8004f5ac[charSlot];

      if(CHARACTER_ADDITIONS[charSlot].length != 0) {
        if(oldAdditionIndex >= 0) {
          charData.selectedAddition = CHARACTER_ADDITIONS[charSlot][oldAdditionIndex].getId();
        } else {
          charData.selectedAddition = CHARACTER_ADDITIONS[charSlot][0].getId();
        }
      }

      for(int i = 0; i < 8; i++) {
        final int level = charSlice.readUByte(0x1a + i);
        final int xp = charSlice.readUByte(0x22 + i);

        if(i < CHARACTER_ADDITIONS[charSlot].length) {
          final RegistryDelegate<Addition> addition = CHARACTER_ADDITIONS[charSlot][i];
          final CharacterAdditionStats stats = new CharacterAdditionStats();
          stats.level = Math.max(0, level - 1);
          stats.xp = xp;
          charData.additionStats.put(addition.getId(), stats);
        }
      }
    }

    savedGame.pathIndex = data.readUShort(0x4d8);
    savedGame.dotIndex = data.readUShort(0x4da);
    savedGame.dotOffset = data.readUByte(0x4dc);
    savedGame.facing = data.readByte(0x4dd);
    savedGame.directionalPathIndex = data.readUShort(0x4de);

    savedGame.indicatorsDisabled = data.readUByte(0x4e3) != 0;
    final boolean isOnWorldMap = data.readUByte(0x4e4) != 0;

    savedGame.characterInitialized = data.readUShort(0x4e6);

    savedGame.engineState = isOnWorldMap ? LodEngineStateTypes.WORLD_MAP.getId() : LodEngineStateTypes.SUBMAP.getId();

    final RetailSavedGame.CharStats charData = savedGame.charStats[savedGame.charIds.getInt(0)];
    savedGame.maxHp = levelStuff_80111cfc[savedGame.charIds.getInt(0)][charData.level].hp_00;
    savedGame.maxMp = magicStuff_80111d20[savedGame.charIds.getInt(0)][charData.dlevel].mp_00;
  }
}
