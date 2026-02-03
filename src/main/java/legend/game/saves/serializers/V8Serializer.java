package legend.game.saves.serializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter;
import legend.core.GameEngine;
import legend.core.memory.types.IntRef;
import legend.game.EngineState;
import legend.game.additions.CharacterAdditionStats;
import legend.game.inventory.Equipment;
import legend.game.inventory.Good;
import legend.game.inventory.ItemStack;
import legend.game.saves.CampaignType;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.InvalidSaveException;
import legend.game.saves.InventoryEntry;
import legend.game.saves.SavedGame;
import legend.game.saves.SeveredSavedGame;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;

import static legend.core.GameEngine.CONFIG;

public final class V8Serializer {
  private V8Serializer() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(V7Serializer.class);

  public static final int MAGIC_V8 = 0x38615344; // DSa8

  private static final Gson jsonSerializer = new GsonBuilder().addReflectionAccessFilter(rawClass -> ReflectionAccessFilter.FilterResult.BLOCK_ALL).create();

  public static FileData fromV8Matcher(final FileData data) {
    if(data.readInt(0) == MAGIC_V8) {
      return data.slice(0x4);
    }

    return null;
  }

  public static SavedGame fromV8(final String filename, final FileData data) {
    final GameState52c gameState = new GameState52c();

    final IntRef offset = new IntRef();
    final String name = data.readAscii(offset);
    final RegistryId campaignTypeId = data.readRegistryId(offset);
    final String locationName = data.readAscii(offset);

    for(int i = 0; i < gameState.scriptData_08.length; i++) {
      gameState.scriptData_08[i] = data.readInt(offset);
    }

    final int charSlotCount = data.readByte(offset);

    for(int i = 0; i < charSlotCount; i++) {
      gameState.charIds_88.add(data.readShort(offset));
    }

    gameState.gold_94 = data.readInt(offset);
    gameState.chapterIndex_98 = data.readInt(offset);
    gameState.stardust_9c = data.readInt(offset);
    gameState.timestamp_a0 = data.readInt(offset);

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
    final List<SeveredSavedGame.CharStats> charStats = new ArrayList<>();

    for(int charIndex = 0; charIndex < gameState.charData_32c.length; charIndex++) {
      final int maxHp = data.readInt(offset);
      final int maxMp = data.readInt(offset);
      charStats.add(new SeveredSavedGame.CharStats(maxHp, maxMp));

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

    gameState.characterInitialized_4e6 = data.readInt(offset);

    final RegistryId engineStateId = data.readRegistryId(offset);
    final int engineStateDataLength = data.readInt(offset);

    if(engineStateDataLength < 0) {
      throw new InvalidSaveException("Engine state data length was negative");
    }

    final FileData engineStateData = data.slice(offset.get(), engineStateDataLength);
    offset.add(engineStateDataLength);

    final ConfigCollection config = new ConfigCollection();
    ConfigStorage.loadConfig(config, ConfigStorageLocation.SAVE, data.slice(offset.get()));

    final SeveredSavedGame savedGame = new SeveredSavedGame(filename, name, locationName, campaignTypeId, engineStateId, engineStateData, gameState, config);
    savedGame.charStats.addAll(charStats);
    return savedGame;
  }

  public static void toV8(final String name, final FileData data, final IntRef offset, final CampaignType campaignType, final EngineState<?> engineState, final GameState52c gameState, final ActiveStatsa0[] activeStats) {
    data.writeAscii(offset, name);
    data.writeRegistryId(offset, campaignType.getRegistryId());
    data.writeAscii(offset, engineState.getLocation(gameState));

    for(final int scriptData : gameState.scriptData_08) {
      data.writeInt(offset, scriptData);
    }

    data.writeByte(offset, gameState.charIds_88.size());

    for(final int charIndex : gameState.charIds_88) {
      data.writeShort(offset, charIndex);
    }

    data.writeInt(offset, gameState.gold_94);
    data.writeInt(offset, gameState.chapterIndex_98);
    data.writeInt(offset, gameState.stardust_9c);
    data.writeInt(offset, gameState.timestamp_a0);

    data.writeInt(offset, gameState._b0);
    data.writeInt(offset, gameState.battleCount_b4);
    data.writeInt(offset, gameState.turnCount_b8);

    for(int i = 0; i < gameState.scriptFlags2_bc.count(); i++) {
      data.writeInt(offset, gameState.scriptFlags2_bc.getRaw(i));
    }

    for(int i = 0; i < gameState.scriptFlags1_13c.count(); i++) {
      data.writeInt(offset, gameState.scriptFlags1_13c.getRaw(i));
    }

    for(int i = 0; i < gameState.wmapFlags_15c.count(); i++) {
      data.writeInt(offset, gameState.wmapFlags_15c.getRaw(i));
    }

    for(int i = 0; i < gameState.visitedLocations_17c.count(); i++) {
      data.writeInt(offset, gameState.visitedLocations_17c.getRaw(i));
    }

    for(final int _1a4 : gameState._1a4) {
      data.writeInt(offset, _1a4);
    }

    for(final int chestFlag : gameState.chestFlags_1c4) {
      data.writeInt(offset, chestFlag);
    }

    data.writeShort(offset, gameState.equipment_1e8.size());
    data.writeShort(offset, gameState.items_2e9.getSize());
    data.writeShort(offset, gameState.goods_19c.size());

    for(final Equipment equipment : gameState.equipment_1e8) {
      data.writeRegistryId(offset, equipment.getRegistryId());
    }

    for(final ItemStack stack : gameState.items_2e9) {
      data.writeRegistryId(offset, stack.getItem().getRegistryId());
      data.writeInt(offset, stack.getSize());
      data.writeInt(offset, stack.getCurrentDurability());

      final JsonObject extraData = stack.getExtraData();

      if(extraData == null) {
        data.writeInt(offset, 0);
        continue;
      }

      final byte[] serialized = jsonSerializer.toJson(extraData).getBytes(StandardCharsets.UTF_8);
      data.writeInt(offset, serialized.length);
      data.write(0, serialized, offset, serialized.length);
    }

    for(final Good good : gameState.goods_19c) {
      data.writeRegistryId(offset, good.getRegistryId());
    }

    data.writeShort(offset, gameState.charData_32c.length);

    for(int i = 0; i < gameState.charData_32c.length; i++) {
      final CharacterData2c charData = gameState.charData_32c[i];
      final ActiveStatsa0 stats = activeStats[i];
      data.writeInt(offset, stats.maxHp_66);
      data.writeInt(offset, stats.maxMp_6e);

      data.writeInt(offset, charData.xp_00);
      data.writeInt(offset, charData.partyFlags_04);
      data.writeInt(offset, charData.hp_08);
      data.writeInt(offset, charData.mp_0a);
      data.writeInt(offset, charData.sp_0c);
      data.writeInt(offset, charData.dlevelXp_0e);
      data.writeInt(offset, charData.status_10);
      data.writeShort(offset, charData.level_12);
      data.writeShort(offset, charData.dlevel_13);

      data.writeByte(offset, charData.equipment_14.size());

      for(final var entry : charData.equipment_14.entrySet()) {
        final EquipmentSlot slot = entry.getKey();
        final Equipment equipment = entry.getValue();
        data.writeAscii(offset, slot.name());
        data.writeRegistryId(offset, equipment.getRegistryId());
      }

      data.writeRegistryId(offset, charData.selectedAddition_19);
      data.writeShort(offset, charData.additionStats.size());

      for(final var entry : charData.additionStats.entrySet()) {
        data.writeRegistryId(offset, entry.getKey());
        data.writeShort(offset, entry.getValue().level);
        data.writeInt(offset, entry.getValue().xp);
      }
    }

    data.writeInt(offset, gameState.characterInitialized_4e6);

    data.writeRegistryId(offset, engineState.type.getRegistryId());

    final FileData engineStateData = engineState.writeSaveData(gameState);

    if(engineStateData != null) {
      data.writeInt(offset, engineStateData.size());
      data.write(0, engineStateData, offset, engineStateData.size());
    } else {
      data.writeInt(offset, 0);
    }

    ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.SAVE, data, offset);
  }
}
