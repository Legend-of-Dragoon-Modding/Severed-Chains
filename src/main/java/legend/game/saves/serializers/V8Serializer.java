package legend.game.saves.serializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter;
import legend.core.gpu.Rect4i;
import legend.core.memory.types.IntRef;
import legend.game.saves.Campaign;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.InvalidSaveException;
import legend.game.saves.InventoryEntry;
import legend.game.saves.SaveVersion;
import legend.game.saves.SavedGame;
import legend.game.saves.SeveredSavedCharacterV1;
import legend.game.saves.SeveredSavedGame;
import legend.game.types.EquipmentSlot;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.nio.charset.StandardCharsets;

import static legend.core.GameEngine.SAVES;
import static legend.lodmod.Legacy.CHAR_IDS;

public final class V8Serializer {
  private V8Serializer() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(V8Serializer.class);

  private static final Gson jsonSerializer = new GsonBuilder().addReflectionAccessFilter(rawClass -> ReflectionAccessFilter.FilterResult.BLOCK_ALL).create();

  public static SavedGame fromV8(final SaveVersion version, final Campaign campaign, final String filename, final FileData data) {
    final IntRef offset = new IntRef();
    final String name = data.readAscii(offset);
    final RegistryId campaignTypeId = data.readRegistryId(offset);
    final String locationName = data.readAscii(offset);

    final ConfigCollection config = new ConfigCollection();
    final SeveredSavedGame savedGame = new SeveredSavedGame(campaign, version.name, filename, name, campaignTypeId, config, SAVES.getRetailAtlas(), 512, 64);

    for(int i = 0; i < savedGame.scriptData.length; i++) {
      savedGame.scriptData[i] = data.readInt(offset);
    }

    final int charSlotCount = data.readByte(offset);

    for(int i = 0; i < charSlotCount; i++) {
      savedGame.activeParty.add(data.readShort(offset));
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

    for(int charIndex = 0; charIndex < charDataCount; charIndex++) {
      final RegistryId templateId = CHAR_IDS[charIndex];
      final int maxHp = data.readInt(offset);
      final int maxMp = data.readInt(offset);
      final SeveredSavedCharacterV1 charData = new SeveredSavedCharacterV1(templateId, charIndex, maxHp, maxMp);
      savedGame.characters.add(charData);
      savedGame.charPortraits.add(new Rect4i(charIndex * 48, 0, 48, 48));

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
        final int level = data.readShort(offset) + 1;
        final int xp = data.readInt(offset);
        charData.additionInfo.put(id, new SeveredSavedCharacterV1.AdditionInfo(level, xp));
      }
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
    savedGame.engineState = engineStateId;
    savedGame.engineStateData = engineStateData;

    return savedGame;
  }
}
