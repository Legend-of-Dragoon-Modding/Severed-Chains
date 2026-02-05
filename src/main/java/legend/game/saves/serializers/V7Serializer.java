package legend.game.saves.serializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter;
import legend.core.GameEngine;
import legend.core.memory.types.IntRef;
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

  public static SavedGame fromV7(final String filename, final FileData data) {
    final GameState52c gameState = new GameState52c();

    final IntRef offset = new IntRef();
    final String name = data.readAscii(offset);

    final int maxHp = data.readInt(offset);
    final int maxMp = data.readInt(offset);

    final int locationType = data.readUByte(offset);
    final int locationIndex = data.readUShort(offset);

    final String locationName = getLocationName(locationType, locationIndex);

    /*gameState._04 = */data.readInt(offset);

    for(int i = 0; i < gameState.scriptData_08.length; i++) {
      gameState.scriptData_08[i] = data.readInt(offset);
    }

    final int charSlotCount = data.readByte(offset);

    for(int i = 0; i < charSlotCount; i++) {
      final int charId = data.readShort(offset);

      if(charId != -1) {
        gameState.charIds_88.add(charId);
      }
    }

    gameState.gold_94 = data.readInt(offset);
    gameState.chapterIndex_98 = data.readInt(offset);
    gameState.stardust_9c = data.readInt(offset);
    gameState.timestamp_a0 = data.readInt(offset);
    gameState.submapScene_a4 = data.readInt(offset);
    gameState.submapCut_a8 = data.readInt(offset);

    gameState._b0 = data.readInt(offset);
    gameState.battleCount_b4 = data.readInt(offset);
    gameState.turnCount_b8 = data.readInt(offset);

    for(int i = 0; i < gameState.scriptFlags2_bc.count(); i++) {
      gameState.scriptFlags2_bc.setRaw(i, data.readInt(offset));
    }

    for(int i = 0; i < gameState.scriptFlags1_13c.count(); i++) {
      gameState.scriptFlags1_13c.setRaw(i, data.readInt(offset));
    }

    for(int i = 0; i < gameState.wmapFlags_15c.count(); i++) {
      gameState.wmapFlags_15c.setRaw(i, data.readInt(offset));
    }

    for(int i = 0; i < gameState.visitedLocations_17c.count(); i++) {
      gameState.visitedLocations_17c.setRaw(i, data.readInt(offset));
    }

    for(int i = 0; i < gameState._1a4.length; i++) {
      gameState._1a4[i] = data.readInt(offset);
    }

    for(int i = 0; i < gameState.chestFlags_1c4.length; i++) {
      gameState.chestFlags_1c4[i] = data.readInt(offset);
    }

    final int equipmentCount = data.readUShort(offset);
    final int itemCount = data.readUShort(offset);
    final int goodsCount = data.readUShort(offset);

    for(int i = 0; i < equipmentCount; i++) {
      gameState.equipmentRegistryIds_1e8.add(data.readRegistryId(offset));
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

      gameState.itemRegistryIds_2e9.add(new InventoryEntry(itemId, size, durability, extraData));
    }

    for(int i = 0; i < goodsCount; i++) {
      gameState.goods_19c.give(GameEngine.REGISTRIES.goods.getEntry(data.readRegistryId(offset)));
    }

    final int charDataCount = data.readUShort(offset); // Not yet used

    for(int charIndex = 0; charIndex < gameState.charData_32c.length; charIndex++) {
      final CharacterData2c charData = gameState.charData_32c[charIndex];
      charData.xp_00 = data.readInt(offset);
      charData.partyFlags_04 = data.readInt(offset);
      charData.hp_08 = data.readInt(offset);
      charData.mp_0a = data.readInt(offset);
      charData.sp_0c = data.readInt(offset);
      charData.dlevelXp_0e = data.readInt(offset);
      charData.status_10 = data.readInt(offset);
      charData.level_12 = data.readUShort(offset);
      charData.dlevel_13 = data.readUShort(offset);

      final int equipmentSlotCount = data.readByte(offset);

      for(int i = 0; i < equipmentSlotCount; i++) {
        final String slotName = data.readAscii(offset);
        final RegistryId equipmentId = data.readRegistryId(offset);
        final EquipmentSlot slot = EquipmentSlot.valueOf(slotName);

        charData.equipmentRegistryIds_14.put(slot, equipmentId);
      }

      charData.selectedAddition_19 = data.readRegistryId(offset);

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

    gameState.pathIndex_4d8 = data.readUShort(offset);
    gameState.dotIndex_4da = data.readUShort(offset);
    gameState.dotOffset_4dc = data.readUByte(offset);
    gameState.facing_4dd = data.readByte(offset);
    gameState.directionalPathIndex_4de = data.readUShort(offset);
    gameState.characterInitialized_4e6 = data.readInt(offset);
    gameState.isOnWorldMap_4e4 = data.readUByte(offset) != 0;
    gameState.indicatorsDisabled_4e3 = data.readUByte(offset) != 0;

    final ConfigCollection config = new ConfigCollection();
    ConfigStorage.loadConfig(config, ConfigStorageLocation.SAVE, data.slice(offset.get()));

    final RegistryId campaignType = LodMod.RETAIL_CAMPAIGN_TYPE.getId();
    final RegistryId engineState = gameState.isOnWorldMap_4e4 ? LodEngineStateTypes.WORLD_MAP.getId() : LodEngineStateTypes.SUBMAP.getId();
    return new RetailSavedGame(filename, name, locationName, campaignType, engineState, new FileData(new byte[0]), gameState, config, maxHp, maxMp);
  }
}
