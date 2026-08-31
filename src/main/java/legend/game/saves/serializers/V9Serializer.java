package legend.game.saves.serializers;

import legend.core.gpu.Rect4i;
import legend.core.memory.types.IntRef;
import legend.game.characters.CharacterTemplate;
import legend.game.saves.Campaign;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.InvalidSaveException;
import legend.game.saves.InventoryEntry;
import legend.game.saves.SaveMigrator;
import legend.game.saves.SaveVersion;
import legend.game.saves.SavedGame;
import legend.game.saves.SeveredSavedGame;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.core.GameEngine.REGISTRIES;

public final class V9Serializer {
  private V9Serializer() { }

  public static SavedGame fromV9(final SaveVersion version, final Campaign campaign, final String filename, final FileData data) {
    final IntRef offset = new IntRef();
    final String name = data.readAscii(offset);
    final RegistryId campaignTypeId = data.readRegistryId(offset);
    final String locationName = data.readAscii(offset);

    final int atlasWidth = data.readInt(offset);
    final int atlasHeight = data.readInt(offset);
    final int atlasSize = data.readInt(offset);
    final FileData atlasData = data.slice(offset.get(), atlasSize);
    offset.add(atlasSize);

    final ConfigCollection config = new ConfigCollection();
    final SeveredSavedGame savedGame = new SeveredSavedGame(campaign, version.name, filename, name, campaignTypeId, config, atlasData, atlasWidth, atlasHeight);

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
      offset.add(extraDataSize); // no one was using extraData at this point, just gonna remove the reader instead of converting json to tags

      savedGame.itemIds.add(new InventoryEntry(itemId, size, durability, null));
    }

    for(int i = 0; i < goodsCount; i++) {
      savedGame.goodsIds.add(data.readRegistryId(offset));
    }

    final int charDataCount = data.readUShort(offset);

    for(int charIndex = 0; charIndex < charDataCount; charIndex++) {
      final RegistryId templateId = data.readRegistryId(offset);
      final CharacterTemplate template = REGISTRIES.characterTemplates.getEntry(templateId).get();
      savedGame.characters.add(template.deserialize(data, offset));
      savedGame.charPortraits.add(new Rect4i(data.readShort(offset), data.readShort(offset), data.readShort(offset), data.readShort(offset)));
    }

    /*savedGame.characterInitialized = */data.readInt(offset);

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
    savedGame.engineStateData = SaveMigrator.upgradeEngineStateData(engineStateId, engineStateData, 4, 675);

    return savedGame;
  }
}
