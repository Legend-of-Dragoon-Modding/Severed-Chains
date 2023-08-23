package legend.game.saves.serializers;

import legend.game.inventory.WhichMenu;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.SavedGame;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CharacterData2c;
import legend.game.types.EngineState;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;

import static legend.core.GameEngine.CONFIG;
import static legend.game.Scus94491BpeSegment_8004.engineState_8004dd20;
import static legend.game.Scus94491BpeSegment_800b.continentIndex_800bf0b0;
import static legend.game.Scus94491BpeSegment_800b.submapId_800bd808;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;

public final class V3Serializer {
  private V3Serializer() { }

  public static final int MAGIC_V3 = 0x33615344; // DSa3

  public static FileData fromV3Matcher(final FileData data) {
    if(data.readInt(0) == MAGIC_V3) {
      return data.slice(0x4);
    }

    return null;
  }

  public static SavedGame fromV3(final String filename, final FileData data) {
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

    for(int i = 0; i < state._17c.count(); i++) {
      state._17c.setRaw(i, data.readInt(i));
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
      state.equipment_1e8.add(data.readUByte(offset));
      offset++;
    }

    for(int i = 0; i < itemCount; i++) {
      state.items_2e9.add(data.readUByte(offset));
      offset++;
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

      for(int i = 0; i < charData.equipment_14.length; i++) {
        charData.equipment_14[i] = data.readUByte(offset);
        offset++;
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
    state.areaIndex_4de = data.readUShort(offset);
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

    final ConfigCollection config = new ConfigCollection();
    ConfigStorage.loadConfig(config, ConfigStorageLocation.SAVE, data.slice(offset));

    return new SavedGame(filename, name, locationType, locationIndex, state, config, maxHp, maxMp);
  }

  public static int toV3(final String name, final FileData data, final GameState52c state, final ActiveStatsa0[] activeStats) {
    final int locationType;
    final int locationIndex;
    if(engineState_8004dd20 == EngineState.WORLD_MAP_08) {
      locationType = 1;
      locationIndex = continentIndex_800bf0b0.get();
      //LAB_80103c98
    } else if(whichMenu_800bdc38 == WhichMenu.RENDER_SAVE_GAME_MENU_19) {
      locationType = 3;
      locationIndex = state.chapterIndex_98;
    } else {
      locationType = 0;
      locationIndex = submapId_800bd808.get();
    }

    int offset = 0;
    data.writeAscii(offset, name);
    offset += 3 + name.length();

    int mainCharId = 0;
    for(int i = 0; i < state.charIds_88.length; i++) {
      if(state.charIds_88[i] != -1) {
        mainCharId = state.charIds_88[i];
        break;
      }
    }

    final ActiveStatsa0 slot0Stats = activeStats[mainCharId];
    data.writeInt(offset, slot0Stats.maxHp_66);
    offset += 4;
    data.writeInt(offset, slot0Stats.maxMp_6e);
    offset += 4;

    data.writeByte(offset, locationType);
    offset++;
    data.writeShort(offset, locationIndex);
    offset += 2;

    data.writeInt(offset, state._04);
    offset += 4;

    for(final int scriptData : state.scriptData_08) {
      data.writeInt(offset, scriptData);
      offset += 4;
    }

    data.writeByte(offset, state.charIds_88.length);
    offset++;

    for(final int charIndex : state.charIds_88) {
      data.writeShort(offset, charIndex);
      offset += 2;
    }

    data.writeInt(offset, state.gold_94);
    offset += 4;
    data.writeInt(offset, state.chapterIndex_98);
    offset += 4;
    data.writeInt(offset, state.stardust_9c);
    offset += 4;
    data.writeInt(offset, state.timestamp_a0);
    offset += 4;
    data.writeInt(offset, state.submapScene_a4);
    offset += 4;
    data.writeInt(offset, state.submapCut_a8);
    offset += 4;

    data.writeInt(offset, state._b0);
    offset += 4;
    data.writeInt(offset, state._b4);
    offset += 4;
    data.writeInt(offset, state._b8);
    offset += 4;

    for(int i = 0; i < state.scriptFlags2_bc.count(); i++) {
      data.writeInt(offset, state.scriptFlags2_bc.getRaw(i));
      offset += 4;
    }

    for(int i = 0; i < state.scriptFlags1_13c.count(); i++) {
      data.writeInt(offset, state.scriptFlags1_13c.getRaw(i));
      offset += 4;
    }

    for(int i = 0; i < state.wmapFlags_15c.count(); i++) {
      data.writeInt(offset, state.wmapFlags_15c.getRaw(i));
      offset += 4;
    }

    for(int i = 0; i < state._17c.count(); i++) {
      data.writeInt(offset, state._17c.getRaw(i));
      offset += 4;
    }

    for(final int good : state.goods_19c) {
      data.writeInt(offset, good);
      offset += 4;
    }

    for(final int _1a4 : state._1a4) {
      data.writeInt(offset, _1a4);
      offset += 4;
    }

    for(final int chestFlag : state.chestFlags_1c4) {
      data.writeInt(offset, chestFlag);
      offset += 4;
    }

    data.writeShort(offset, state.equipment_1e8.size());
    offset += 2;
    data.writeShort(offset, state.items_2e9.size());
    offset += 2;

    for(final int id : state.equipment_1e8) {
      data.writeByte(offset, id);
      offset++;
    }

    for(final int id : state.items_2e9) {
      data.writeByte(offset, id);
      offset++;
    }

    data.writeShort(offset, state.charData_32c.length);
    offset += 2;

    for(final CharacterData2c charData : state.charData_32c) {
      data.writeInt(offset, charData.xp_00);
      offset += 4;
      data.writeInt(offset, charData.partyFlags_04);
      offset += 4;
      data.writeInt(offset, charData.hp_08);
      offset += 4;
      data.writeInt(offset, charData.mp_0a);
      offset += 4;
      data.writeInt(offset, charData.sp_0c);
      offset += 4;
      data.writeInt(offset, charData.dlevelXp_0e);
      offset += 4;
      data.writeInt(offset, charData.status_10);
      offset += 4;
      data.writeShort(offset, charData.level_12);
      offset += 2;
      data.writeShort(offset, charData.dlevel_13);
      offset += 2;

      for(final int equipment : charData.equipment_14) {
        data.writeByte(offset, equipment);
        offset++;
      }

      data.writeShort(offset, charData.selectedAddition_19);
      offset += 2;

      data.writeShort(offset, charData.additionLevels_1a.length);
      offset += 2;

      for(int additionSlot = 0; additionSlot < charData.additionLevels_1a.length; additionSlot++) {
        data.writeShort(offset, charData.additionLevels_1a[additionSlot]);
        offset += 2;
        data.writeInt(offset, charData.additionXp_22[additionSlot]);
        offset += 4;
      }
    }

    for(int i = 0; i < 8; i++) {
//      data.writeInt(offset, _4b8);
      offset += 4;
    }

    data.writeShort(offset, state.pathIndex_4d8);
    offset += 2;
    data.writeShort(offset, state.dotIndex_4da);
    offset += 2;
    data.writeByte(offset, (int)state.dotOffset_4dc);
    offset++;
    data.writeByte(offset, state.facing_4dd);
    offset++;
    data.writeShort(offset, state.areaIndex_4de);
    offset += 2;

    data.writeInt(offset, state.characterInitialized_4e6);
    offset += 4;

    data.writeByte(offset, state.isOnWorldMap_4e4 ? 1 : 0);
    offset++;

//    data.writeByte(offset, state.mono_4e0 ? 1 : 0);
    offset++;
    data.writeByte(offset, state.vibrationEnabled_4e1 ? 1 : 0);
    offset++;
//    data.writeByte(offset, state.morphMode_4e2);
    offset++;
    data.writeByte(offset, state.indicatorsDisabled_4e3 ? 1 : 0);
    offset++;

    offset += ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.SAVE, data.slice(offset));

    return offset;
  }
}
