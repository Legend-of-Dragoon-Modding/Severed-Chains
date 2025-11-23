package legend.game.saves.serializers;

import legend.core.GameEngine;
import legend.core.memory.types.IntRef;
import legend.game.EngineStateEnum;
import legend.game.inventory.Equipment;
import legend.game.inventory.Good;
import legend.game.inventory.ItemStack;
import legend.game.inventory.WhichMenu;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.InventoryEntry;
import legend.game.saves.SavedGame;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.core.GameEngine.CONFIG;
import static legend.game.EngineStates.engineState_8004dd20;
import static legend.game.Menus.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800b.continentIndex_800bf0b0;
import static legend.game.Scus94491BpeSegment_800b.submapId_800bd808;

public final class V6Serializer {
  private V6Serializer() { }

  public static final int MAGIC_V6 = 0x36615344; // DSa6

  public static FileData fromV6Matcher(final FileData data) {
    if(data.readInt(0) == MAGIC_V6) {
      return data.slice(0x4);
    }

    return null;
  }

  public static SavedGame fromV6(final String filename, final FileData data) {
    final GameState52c state = new GameState52c();

    final IntRef offset = new IntRef();
    final String name = data.readAscii(offset);

    final int maxHp = data.readInt(offset);
    final int maxMp = data.readInt(offset);

    final int locationType = data.readUByte(offset);
    final int locationIndex = data.readUShort(offset);

    state._04 = data.readInt(offset);

    for(int i = 0; i < state.scriptData_08.length; i++) {
      state.scriptData_08[i] = data.readInt(offset);
    }

    final int charSlotCount = data.readByte(offset); // Not yet used

    for(int i = 0; i < state.charIds_88.length; i++) {
      state.charIds_88[i] = data.readShort(offset);
    }

    state.gold_94 = data.readInt(offset);
    state.chapterIndex_98 = data.readInt(offset);
    state.stardust_9c = data.readInt(offset);
    state.timestamp_a0 = data.readInt(offset);
    state.submapScene_a4 = data.readInt(offset);
    state.submapCut_a8 = data.readInt(offset);

    state._b0 = data.readInt(offset);
    state._b4 = data.readInt(offset);
    state._b8 = data.readInt(offset);

    for(int i = 0; i < state.scriptFlags2_bc.count(); i++) {
      state.scriptFlags2_bc.setRaw(i, data.readInt(offset));
    }

    for(int i = 0; i < state.scriptFlags1_13c.count(); i++) {
      state.scriptFlags1_13c.setRaw(i, data.readInt(offset));
    }

    for(int i = 0; i < state.wmapFlags_15c.count(); i++) {
      state.wmapFlags_15c.setRaw(i, data.readInt(offset));
    }

    for(int i = 0; i < state.visitedLocations_17c.count(); i++) {
      state.visitedLocations_17c.setRaw(i, data.readInt(offset));
    }

    for(int i = 0; i < state._1a4.length; i++) {
      state._1a4[i] = data.readInt(offset);
    }

    for(int i = 0; i < state.chestFlags_1c4.length; i++) {
      state.chestFlags_1c4[i] = data.readInt(offset);
    }

    final int equipmentCount = data.readUShort(offset);
    final int itemCount = data.readUShort(offset);
    final int goodsCount = data.readUShort(offset);

    for(int i = 0; i < equipmentCount; i++) {
      state.equipmentRegistryIds_1e8.add(data.readRegistryId(offset));
    }

    for(int i = 0; i < itemCount; i++) {
      final RegistryId itemId = data.readRegistryId(offset);
      final int size = data.readInt(offset);
      final int durability = data.readInt(offset);
      // Unused for now but may be used as the length header for extra item data in the future
      final int extraDataHeader = data.readInt(offset);
      state.itemRegistryIds_2e9.add(new InventoryEntry(itemId, size, durability));
    }

    for(int i = 0; i < goodsCount; i++) {
      state.goods_19c.give(GameEngine.REGISTRIES.goods.getEntry(data.readRegistryId(offset)));
    }

    final int charDataCount = data.readUShort(offset); // Not yet used

    for(final CharacterData2c charData : state.charData_32c) {
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

      charData.selectedAddition_19 = data.readShort(offset);

      final int additionCount = data.readShort(offset); // Not yet used

      for(int additionSlot = 0; additionSlot < charData.additionLevels_1a.length; additionSlot++) {
        charData.additionLevels_1a[additionSlot] = data.readShort(offset);
        charData.additionXp_22[additionSlot] = data.readInt(offset);
      }
    }

    state.pathIndex_4d8 = data.readUShort(offset);
    state.dotIndex_4da = data.readUShort(offset);
    state.dotOffset_4dc = data.readUByte(offset);
    state.facing_4dd = data.readByte(offset);
    state.directionalPathIndex_4de = data.readUShort(offset);
    state.characterInitialized_4e6 = data.readInt(offset);
    state.isOnWorldMap_4e4 = data.readUByte(offset) != 0;
    state.indicatorsDisabled_4e3 = data.readUByte(offset) != 0;

    final ConfigCollection config = new ConfigCollection();
    ConfigStorage.loadConfig(config, ConfigStorageLocation.SAVE, data.slice(offset.get()));

    return new SavedGame(filename, name, locationType, locationIndex, state, config, maxHp, maxMp);
  }

  public static void toV6(final String name, final FileData data, final IntRef offset, final GameState52c state, final ActiveStatsa0[] activeStats) {
    final int locationType;
    final int locationIndex;
    if(engineState_8004dd20 == EngineStateEnum.WORLD_MAP_08) {
      locationType = 1;
      locationIndex = continentIndex_800bf0b0;
      //LAB_80103c98
    } else if(whichMenu_800bdc38 == WhichMenu.RENDER_SAVE_GAME_MENU_19) {
      locationType = 3;
      locationIndex = state.chapterIndex_98;
    } else {
      locationType = 0;
      locationIndex = submapId_800bd808;
    }

    data.writeAscii(offset, name);

    int mainCharId = 0;
    for(int i = 0; i < state.charIds_88.length; i++) {
      if(state.charIds_88[i] != -1) {
        mainCharId = state.charIds_88[i];
        break;
      }
    }

    final ActiveStatsa0 slot0Stats = activeStats[mainCharId];
    data.writeInt(offset, slot0Stats.maxHp_66);
    data.writeInt(offset, slot0Stats.maxMp_6e);

    data.writeByte(offset, locationType);
    data.writeShort(offset, locationIndex);

    data.writeInt(offset, state._04);

    for(final int scriptData : state.scriptData_08) {
      data.writeInt(offset, scriptData);
    }

    data.writeByte(offset, state.charIds_88.length);

    for(final int charIndex : state.charIds_88) {
      data.writeShort(offset, charIndex);
    }

    data.writeInt(offset, state.gold_94);
    data.writeInt(offset, state.chapterIndex_98);
    data.writeInt(offset, state.stardust_9c);
    data.writeInt(offset, state.timestamp_a0);
    data.writeInt(offset, state.submapScene_a4);
    data.writeInt(offset, state.submapCut_a8);

    data.writeInt(offset, state._b0);
    data.writeInt(offset, state._b4);
    data.writeInt(offset, state._b8);

    for(int i = 0; i < state.scriptFlags2_bc.count(); i++) {
      data.writeInt(offset, state.scriptFlags2_bc.getRaw(i));
    }

    for(int i = 0; i < state.scriptFlags1_13c.count(); i++) {
      data.writeInt(offset, state.scriptFlags1_13c.getRaw(i));
    }

    for(int i = 0; i < state.wmapFlags_15c.count(); i++) {
      data.writeInt(offset, state.wmapFlags_15c.getRaw(i));
    }

    for(int i = 0; i < state.visitedLocations_17c.count(); i++) {
      data.writeInt(offset, state.visitedLocations_17c.getRaw(i));
    }

    for(final int _1a4 : state._1a4) {
      data.writeInt(offset, _1a4);
    }

    for(final int chestFlag : state.chestFlags_1c4) {
      data.writeInt(offset, chestFlag);
    }

    data.writeShort(offset, state.equipment_1e8.size());
    data.writeShort(offset, state.items_2e9.getSize());
    data.writeShort(offset, state.goods_19c.size());

    for(final Equipment equipment : state.equipment_1e8) {
      data.writeRegistryId(offset, equipment.getRegistryId());
    }

    for(final ItemStack stack : state.items_2e9) {
      data.writeRegistryId(offset, stack.getItem().getRegistryId());
      data.writeInt(offset, stack.getSize());
      data.writeInt(offset, stack.getCurrentDurability());
      // Unused for now but may be used as the length header for extra item data in the future
      data.writeInt(offset, 0);
    }

    for(final Good good : state.goods_19c) {
      data.writeRegistryId(offset, good.getRegistryId());
    }

    data.writeShort(offset, state.charData_32c.length);

    for(final CharacterData2c charData : state.charData_32c) {
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

    data.writeShort(offset, state.pathIndex_4d8);
    data.writeShort(offset, state.dotIndex_4da);
    data.writeByte(offset, (int)state.dotOffset_4dc);
    data.writeByte(offset, state.facing_4dd);
    data.writeShort(offset, state.directionalPathIndex_4de);
    data.writeInt(offset, state.characterInitialized_4e6);
    data.writeByte(offset, state.isOnWorldMap_4e4 ? 1 : 0);
    data.writeByte(offset, state.indicatorsDisabled_4e3 ? 1 : 0);

    ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.SAVE, data, offset);
  }
}
