package legend.game.saves.serializers;

import legend.core.memory.types.IntRef;
import legend.game.EngineState;
import legend.game.inventory.Equipment;
import legend.game.inventory.Item;
import legend.game.modding.events.saves.SaveTypeEvent;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.SavedGame;
import legend.game.saves.types.RetailSaveDisplay;
import legend.game.saves.types.SaveType;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.game.SItem.chapterNames_80114248;
import static legend.game.SItem.submapNames_8011c108;
import static legend.game.SItem.worldMapNames_8011c1ec;

public final class V5Serializer {
  private V5Serializer() { }

  public static final int MAGIC_V5 = 0x35615344; // DSa4

  public static FileData fromV5Matcher(final FileData data) {
    if(data.readInt(0) == MAGIC_V5) {
      return data.slice(0x4);
    }

    return null;
  }

  public static SavedGame<RetailSaveDisplay> fromV5(final String filename, final FileData data) {
    final GameState52c state = new GameState52c();

    int offset = 0;
    final String name = data.readAscii(offset);
    offset += 3 + name.length();

    final int maxHp = data.readInt(offset);
    offset += 4;
    final int maxMp = data.readInt(offset);
    offset += 4;

    final int locationType = data.readUByte(offset);
    offset++;
    final int locationIndex = data.readUShort(offset);
    offset += 2;

    state._04 = data.readInt(offset);
    offset += 4;

    for(int i = 0; i < state.scriptData_08.length; i++) {
      state.scriptData_08[i] = data.readInt(offset);
      offset += 4;
    }

    final int charSlotCount = data.readByte(offset); // Not yet used
    offset++;

    for(int i = 0; i < state.charIds_88.length; i++) {
      state.charIds_88[i] = data.readShort(offset);
      offset += 2;
    }

    state.gold_94 = data.readInt(offset);
    offset += 4;
    state.chapterIndex_98 = data.readInt(offset);
    offset += 4;
    state.stardust_9c = data.readInt(offset);
    offset += 4;
    state.timestamp_a0 = data.readInt(offset);
    offset += 4;
    state.submapScene_a4 = data.readInt(offset);
    offset += 4;
    state.submapCut_a8 = data.readInt(offset);
    offset += 4;

    state._b0 = data.readInt(offset);
    offset += 4;
    state._b4 = data.readInt(offset);
    offset += 4;
    state._b8 = data.readInt(offset);
    offset += 4;

    for(int i = 0; i < state.scriptFlags2_bc.count(); i++) {
      state.scriptFlags2_bc.setRaw(i, data.readInt(offset));
      offset += 4;
    }

    for(int i = 0; i < state.scriptFlags1_13c.count(); i++) {
      state.scriptFlags1_13c.setRaw(i, data.readInt(offset));
      offset += 4;
    }

    for(int i = 0; i < state.wmapFlags_15c.count(); i++) {
      state.wmapFlags_15c.setRaw(i, data.readInt(offset));
      offset += 4;
    }

    for(int i = 0; i < state.visitedLocations_17c.count(); i++) {
      state.visitedLocations_17c.setRaw(i, data.readInt(i));
      offset += 4;
    }

    for(int i = 0; i < state.goods_19c.length; i++) {
      state.goods_19c[i] = data.readInt(offset);
      offset += 4;
    }

    for(int i = 0; i < state._1a4.length; i++) {
      state._1a4[i] = data.readInt(offset);
      offset += 4;
    }

    for(int i = 0; i < state.chestFlags_1c4.length; i++) {
      state.chestFlags_1c4[i] = data.readInt(offset);
      offset += 4;
    }

    final int equipmentCount = data.readUShort(offset);
    offset += 2;
    final int itemCount = data.readUShort(offset);
    offset += 2;

    for(int i = 0; i < equipmentCount; i++) {
      final RegistryId equipmentId = data.readRegistryId(offset);
      state.equipmentRegistryIds_1e8.add(equipmentId);
      offset += equipmentId.toString().length() + 3;
    }

    for(int i = 0; i < itemCount; i++) {
      final RegistryId itemId = data.readRegistryId(offset);
      state.itemRegistryIds_2e9.add(itemId);
      offset += itemId.toString().length() + 3;
    }

    final int charDataCount = data.readUShort(offset); // Not yet used
    offset += 2;

    for(final CharacterData2c charData : state.charData_32c) {
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

      final int equipmentSlotCount = data.readByte(offset);
      offset++;

      for(int i = 0; i < equipmentSlotCount; i++) {
        final String slotName = data.readAscii(offset);
        offset += slotName.length() + 3;

        final RegistryId equipmentId = data.readRegistryId(offset);
        offset += equipmentId.toString().length() + 3;

        final EquipmentSlot slot = EquipmentSlot.valueOf(slotName);

        charData.equipmentRegistryIds_14.put(slot, equipmentId);
      }

      charData.selectedAddition_19 = data.readShort(offset);
      offset += 2;

      final int additionCount = data.readShort(offset); // Not yet used
      offset += 2;

      for(int additionSlot = 0; additionSlot < charData.additionLevels_1a.length; additionSlot++) {
        charData.additionLevels_1a[additionSlot] = data.readShort(offset);
        offset += 2;
        charData.additionXp_22[additionSlot] = data.readInt(offset);
        offset += 4;
      }
    }

    for(int i = 0; i < 8; i++) {
//      state._4b8[i] = data.readInt(offset);
      offset += 4;
    }

    state.pathIndex_4d8 = data.readUShort(offset);
    offset += 2;
    state.dotIndex_4da = data.readUShort(offset);
    offset += 2;
    state.dotOffset_4dc = data.readUByte(offset);
    offset++;
    state.facing_4dd = data.readByte(offset);
    offset++;
    state.directionalPathIndex_4de = data.readUShort(offset);
    offset += 2;

    state.characterInitialized_4e6 = data.readInt(offset);
    offset += 4;

    state.isOnWorldMap_4e4 = data.readUByte(offset) != 0;
    offset++;

//    state.mono_4e0 = data.readUByte(offset) != 0;
    offset++;
    state.vibrationEnabled_4e1 = data.readUByte(offset) != 0;
    offset++;
//    state.morphMode_4e2 = data.readUByte(offset);
    offset++;
    state.indicatorsDisabled_4e3 = data.readUByte(offset) != 0;
    offset++;

    final String[] locationNames;
    if(locationType == 1) {
      locationNames = worldMapNames_8011c1ec;
    } else if(locationType == 3) {
      locationNames = chapterNames_80114248;
    } else {
      locationNames = submapNames_8011c108;
    }

    final String locationName = locationNames[locationIndex];
    final RetailSaveDisplay display = new RetailSaveDisplay(locationName, maxHp, maxMp);

    final ConfigCollection config = new ConfigCollection();
    ConfigStorage.loadConfig(config, ConfigStorageLocation.SAVE, data.slice(offset));

    return new SavedGame<>(filename, name, display, state, config);
  }

  public static int toV5(final String name, final FileData data, final GameState52c gameState, final ActiveStatsa0[] activeStats, final EngineState engineState) {
    final SaveType saveType = EVENTS.postEvent(new SaveTypeEvent(gameState, engineState)).saveType;

    final IntRef offset = new IntRef();
    data.writeRegistryId(offset, saveType.getRegistryId());

    offset.add(saveType.serialize(data.slice(offset.get()), saveType.createDisplayData(gameState, activeStats, engineState)));

    data.writeInt(offset, gameState._04);

    for(final int scriptData : gameState.scriptData_08) {
      data.writeInt(offset, scriptData);
    }

    data.writeByte(offset, gameState.charIds_88.length);

    for(final int charIndex : gameState.charIds_88) {
      data.writeShort(offset, charIndex);
    }

    data.writeInt(offset, gameState.gold_94);
    data.writeInt(offset, gameState.chapterIndex_98);
    data.writeInt(offset, gameState.stardust_9c);
    data.writeInt(offset, gameState.timestamp_a0);
    data.writeInt(offset, gameState.submapScene_a4);
    data.writeInt(offset, gameState.submapCut_a8);

    data.writeInt(offset, gameState._b0);
    data.writeInt(offset, gameState._b4);
    data.writeInt(offset, gameState._b8);

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

    for(final int good : gameState.goods_19c) {
      data.writeInt(offset, good);
    }

    for(final int _1a4 : gameState._1a4) {
      data.writeInt(offset, _1a4);
    }

    for(final int chestFlag : gameState.chestFlags_1c4) {
      data.writeInt(offset, chestFlag);
    }

    data.writeShort(offset, gameState.equipment_1e8.size());
    data.writeShort(offset, gameState.items_2e9.size());

    for(final Equipment equipment : gameState.equipment_1e8) {
      data.writeRegistryId(offset, equipment.getRegistryId());
    }

    for(final Item item : gameState.items_2e9) {
      data.writeRegistryId(offset, item.getRegistryId());
    }

    data.writeShort(offset, gameState.charData_32c.length);

    for(final CharacterData2c charData : gameState.charData_32c) {
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

      data.writeShort(offset, charData.selectedAddition_19);

      data.writeShort(offset, charData.additionLevels_1a.length);
      for(int additionSlot = 0; additionSlot < charData.additionLevels_1a.length; additionSlot++) {
        data.writeShort(offset, charData.additionLevels_1a[additionSlot]);
        data.writeInt(offset, charData.additionXp_22[additionSlot]);
      }
    }

    data.writeShort(offset, gameState.pathIndex_4d8);
    data.writeShort(offset, gameState.dotIndex_4da);
    data.writeByte(offset, (int)gameState.dotOffset_4dc);
    data.writeByte(offset, gameState.facing_4dd);
    data.writeShort(offset, gameState.directionalPathIndex_4de);

    data.writeInt(offset, gameState.characterInitialized_4e6);
    data.writeByte(offset, gameState.isOnWorldMap_4e4 ? 1 : 0);
    data.writeByte(offset, gameState.vibrationEnabled_4e1 ? 1 : 0);
    data.writeByte(offset, gameState.indicatorsDisabled_4e3 ? 1 : 0);

    offset.add(ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.SAVE, data.slice(offset.get())));

    return offset.get();
  }
}
