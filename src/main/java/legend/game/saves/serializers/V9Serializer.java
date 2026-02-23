package legend.game.saves.serializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter;
import legend.core.memory.types.IntRef;
import legend.game.EngineState;
import legend.game.characters.CharacterTemplate;
import legend.game.inventory.Equipment;
import legend.game.inventory.Good;
import legend.game.inventory.ItemStack;
import legend.game.saves.Campaign;
import legend.game.saves.CampaignType;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.InvalidSaveException;
import legend.game.saves.InventoryEntry;
import legend.game.saves.SavedCharacter;
import legend.game.saves.SavedGame;
import legend.game.saves.SeveredSavedGame;
import legend.game.types.CharacterData2c;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.REGISTRIES;

public final class V9Serializer {
  private V9Serializer() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(V9Serializer.class);

  public static final int MAGIC_V9 = 0x39615344; // DSa9

  private static final Gson jsonSerializer = new GsonBuilder().addReflectionAccessFilter(rawClass -> ReflectionAccessFilter.FilterResult.BLOCK_ALL).create();

  public static FileData fromV9Matcher(final FileData data) {
    if(data.readInt(0) == MAGIC_V9) {
      return data.slice(0x4);
    }

    return null;
  }

  public static SavedGame fromV9(final Campaign campaign, final String filename, final FileData data) {
    final IntRef offset = new IntRef();
    final String name = data.readAscii(offset);
    final RegistryId campaignTypeId = data.readRegistryId(offset);
    final String locationName = data.readAscii(offset);

    final ConfigCollection config = new ConfigCollection();
    final SeveredSavedGame savedGame = new SeveredSavedGame(campaign, "V9", filename, name, campaignTypeId, config);

    for(int i = 0; i < savedGame.scriptData.length; i++) {
      savedGame.scriptData[i] = data.readInt(offset);
    }

    final int charSlotCount = data.readByte(offset);

    for(int i = 0; i < charSlotCount; i++) {
      savedGame.charIds.add(data.readShort(offset));
    }

    savedGame.gold = data.readInt(offset);
    savedGame.chapterIndex = data.readInt(offset);
    savedGame.stardust = data.readInt(offset);
    savedGame.timestamp = data.readInt(offset);

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

    final int charDataCount = data.readUShort(offset);
    final List<SavedCharacter> charStats = new ArrayList<>();

    for(int charIndex = 0; charIndex < charDataCount; charIndex++) {
      final RegistryId templateId = data.readRegistryId(offset);
      final CharacterTemplate template = REGISTRIES.characterTemplates.getEntry(templateId).get();
      charStats.add(template.deserialize(data, offset));
    }

    savedGame.characterInitialized = data.readInt(offset);

    final RegistryId engineStateId = data.readRegistryId(offset);
    final int engineStateDataLength = data.readInt(offset);

    if(engineStateDataLength < 0) {
      throw new InvalidSaveException("Engine state data length was negative");
    }

    final FileData engineStateData = data.slice(offset.get(), engineStateDataLength);
    offset.add(engineStateDataLength);

    ConfigStorage.loadConfig(config, ConfigStorageLocation.SAVE, data.slice(offset.get()));

    savedGame.locationName = locationName;
    savedGame.charStats.addAll(charStats);
    savedGame.engineState = engineStateId;
    savedGame.engineStateData = engineStateData;

    return savedGame;
  }

  public static void toV9(final String name, final FileData data, final IntRef offset, final CampaignType campaignType, final EngineState<?> engineState, final GameState52c gameState) {
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

    data.writeShort(offset, gameState.charData_32c.size());

    for(int i = 0; i < gameState.charData_32c.size(); i++) {
      final CharacterData2c character = gameState.charData_32c.get(i);
      data.writeRegistryId(offset, character.template.getRegistryId());
      character.template.serialize(character, data, offset);
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
