package legend.game.saves.serializers;

import legend.core.GameEngine;
import legend.game.additions.Addition;
import legend.game.additions.CharacterAdditionStats;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.InventoryEntry;
import legend.game.saves.RetailSavedGame;
import legend.game.saves.SavedGame;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import legend.lodmod.LodEngineStateTypes;
import legend.lodmod.LodMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.Scus94491BpeSegment_8004.CHARACTER_ADDITIONS;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.lodmod.LodMod.getLocationName;

public final class V2Serializer {
  private V2Serializer() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(RetailSerializer.class);

  public static final int MAGIC_V2 = 0x32615344; // DSa2

  public static FileData fromV2Matcher(final FileData data) {
    if(data.readInt(0) == MAGIC_V2) {
      return data.slice(0x4);
    }

    return null;
  }

  public static SavedGame fromV2(final String name, final FileData data) {
    final GameState52c gameState = new GameState52c();

    int offset = 0;
    final int locationType = data.readUByte(offset);
    offset++;
    final int locationIndex = data.readUShort(offset);
    offset += 2;

    final String locationName = getLocationName(locationType, locationIndex);

//    gameState._04 = data.readInt(offset);
    offset += 4;

    for(int i = 0; i < gameState.scriptData_08.length; i++) {
      gameState.scriptData_08[i] = data.readInt(offset);
      offset += 4;
    }

    final int charSlotCount = data.readByte(offset);
    offset++;

    for(int i = 0; i < charSlotCount; i++) {
      final int charId = data.readShort(offset);
      offset += 2;

      if(charId != -1) {
        gameState.charIds_88.add(charId);
      }
    }

    gameState.gold_94 = data.readInt(offset);
    offset += 4;
    gameState.chapterIndex_98 = data.readInt(offset);
    offset += 4;
    gameState.stardust_9c = data.readInt(offset);
    offset += 4;
    gameState.timestamp_a0 = data.readInt(offset);
    offset += 4;
    gameState.submapScene_a4 = data.readInt(offset);
    offset += 4;
    gameState.submapCut_a8 = data.readInt(offset);
    offset += 4;

    gameState._b0 = data.readInt(offset);
    offset += 4;
    gameState.battleCount_b4 = data.readInt(offset);
    offset += 4;
    gameState.turnCount_b8 = data.readInt(offset);
    offset += 4;

    for(int i = 0; i < gameState.scriptFlags2_bc.count(); i++) {
      gameState.scriptFlags2_bc.setRaw(i, data.readInt(offset));
      offset += 4;
    }

    for(int i = 0; i < gameState.scriptFlags1_13c.count(); i++) {
      gameState.scriptFlags1_13c.setRaw(i, data.readInt(offset));
      offset += 4;
    }

    for(int i = 0; i < gameState.wmapFlags_15c.count(); i++) {
      gameState.wmapFlags_15c.setRaw(i, data.readInt(offset));
      offset += 4;
    }

    for(int i = 0; i < gameState.visitedLocations_17c.count(); i++) {
      gameState.visitedLocations_17c.setRaw(i, data.readInt(i));
      offset += 4;
    }

    for(int i = 0; i < 2; i++) {
      final int packed = data.readInt(offset);
      offset += 4;

      for(int bit = 0; bit < 32; bit++) {
        if((packed & (1 << bit)) != 0) {
          gameState.goods_19c.give(GameEngine.REGISTRIES.goods.getEntry(LodMod.id(LodMod.GOODS_IDS[i * 32 + bit])));
        }
      }
    }

    for(int i = 0; i < gameState._1a4.length; i++) {
      gameState._1a4[i] = data.readInt(offset);
      offset += 4;
    }

    for(int i = 0; i < gameState.chestFlags_1c4.length; i++) {
      gameState.chestFlags_1c4[i] = data.readInt(offset);
      offset += 4;
    }

    final int equipmentCount = data.readUShort(offset);
    offset += 2;
    final int itemCount = data.readUShort(offset);
    offset += 2;

    for(int i = 0; i < equipmentCount; i++) {
      final int id = data.readUByte(offset);
      final String idStr = LodMod.EQUIPMENT_IDS[id];

      if(idStr.isBlank()) {
        LOGGER.warn("Skipping unknown equipment ID %#x", id);
        continue;
      }

      gameState.equipmentRegistryIds_1e8.add(LodMod.id(idStr));
      offset++;
    }

    for(int i = 0; i < itemCount; i++) {
      final int id = data.readUByte(offset);
      final String idStr = LodMod.ITEM_IDS[id - 192];

      if(idStr.isBlank()) {
        LOGGER.warn("Skipping unknown item ID %#x", id);
        continue;
      }

      gameState.itemRegistryIds_2e9.add(new InventoryEntry(LodMod.id(idStr), 1, 1));
      offset++;
    }

    final int charDataCount = data.readUShort(offset); // Not yet used
    offset += 2;

    for(int charIndex = 0; charIndex < gameState.charData_32c.length; charIndex++) {
      final CharacterData2c charData = gameState.charData_32c[charIndex];
      charData.xp_00 = data.readInt(offset);
      offset += 4;
      charData.partyFlags_04 = data.readInt(offset);
      offset += 4;
      charData.hp_08 = data.readInt(offset);
      offset += 4;
      charData.mp_0a = data.readInt(offset);
      offset += 4;
      charData.sp_0c = data.readInt(offset);
      offset += 4;
      charData.dlevelXp_0e = data.readInt(offset);
      offset += 4;
      charData.status_10 = data.readInt(offset);
      offset += 4;
      charData.level_12 = data.readUShort(offset);
      offset += 2;
      charData.dlevel_13 = data.readUShort(offset);
      offset += 2;

      for(int i = 0; i < 5; i++) {
        charData.equipmentIds_14.put(EquipmentSlot.fromLegacy(i), data.readUByte(offset));
        offset++;
      }

      final int oldAdditionIndex = data.readShort(offset) - additionOffsets_8004f5ac[charIndex];
      offset += 2;

      if(CHARACTER_ADDITIONS[charIndex].length != 0) {
        if(oldAdditionIndex >= 0) {
          charData.selectedAddition_19 = CHARACTER_ADDITIONS[charIndex][oldAdditionIndex].getId();
        } else {
          charData.selectedAddition_19 = CHARACTER_ADDITIONS[charIndex][0].getId();
        }
      }

      final int additionCount = data.readShort(offset); // Not yet used
      offset += 2;

      for(int additionIndex = 0; additionIndex < 8; additionIndex++) {
        final int level = data.readShort(offset);
        offset += 2;
        final int xp = data.readInt(offset);
        offset += 4;

        if(additionIndex < CHARACTER_ADDITIONS[charIndex].length) {
          final RegistryDelegate<Addition> addition = CHARACTER_ADDITIONS[charIndex][additionIndex];
          final CharacterAdditionStats stats = new CharacterAdditionStats();
          stats.level = Math.max(0, level - 1);
          stats.xp = xp;
          charData.additionStats.put(addition.getId(), stats);
        }
      }
    }

    for(int i = 0; i < 8; i++) {
//      gameState._4b8[i] = data.readInt(offset);
      offset += 4;
    }

    gameState.pathIndex_4d8 = data.readUShort(offset);
    offset += 2;
    gameState.dotIndex_4da = data.readUShort(offset);
    offset += 2;
    gameState.dotOffset_4dc = data.readUByte(offset);
    offset++;
    gameState.facing_4dd = data.readByte(offset);
    offset++;
    gameState.directionalPathIndex_4de = data.readUShort(offset);
    offset += 2;

    gameState.characterInitialized_4e6 = data.readInt(offset);
    offset += 4;

    gameState.isOnWorldMap_4e4 = data.readUByte(offset) != 0;
    offset++;

//    gameState.mono_4e0 = data.readUByte(offset) != 0;
    offset++;
//    gameState.vibrationEnabled_4e1 = data.readUByte(offset) != 0;
    offset++;
//    gameState.morphMode_4e2 = data.readUByte(offset);
    offset++;
    gameState.indicatorsDisabled_4e3 = data.readUByte(offset) != 0;
    offset++;

    final ConfigCollection config = new ConfigCollection();
    ConfigStorage.loadConfig(config, ConfigStorageLocation.SAVE, data.slice(offset));

    final RegistryId campaignType = LodMod.RETAIL_CAMPAIGN_TYPE.getId();
    final RegistryId engineState = gameState.isOnWorldMap_4e4 ? LodEngineStateTypes.WORLD_MAP.getId() : LodEngineStateTypes.SUBMAP.getId();
    final CharacterData2c charData = gameState.charData_32c[gameState.charIds_88.getInt(0)];
    final int maxHp = levelStuff_80111cfc[gameState.charIds_88.getInt(0)][charData.level_12].hp_00;
    final int maxMp = magicStuff_80111d20[gameState.charIds_88.getInt(0)][charData.dlevel_13].mp_00;
    return new RetailSavedGame(name, name, locationName, campaignType, engineState, new FileData(new byte[0]), gameState, config, maxHp, maxMp);
  }
}
