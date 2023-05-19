package legend.game.saves;

import legend.game.inventory.WhichMenu;
import legend.game.types.CharacterData2c;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;

import static legend.core.GameEngine.CONFIG;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_800b.continentIndex_800bf0b0;
import static legend.game.Scus94491BpeSegment_800b.submapIndex_800bd808;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;

public final class SaveSerialization {
  private SaveSerialization() { }

  public static final int MAGIC_RETAIL = 0x01114353; // SC__
  public static final int MAGIC_V1 = 0x76615344; // DSav
  public static final int MAGIC_V2 = 0x32615344; // DSa2

  public static FileData fromRetailMatcher(final FileData data) {
    if(data.readInt(0) == MAGIC_RETAIL) {
      return data.slice(0x4);
    }

    return null;
  }

  public static SavedGame fromRetail(final String name, final FileData data) {
    return new SavedGame(name, data.readUByte(0x1a9), data.readUByte(0x1a8), deserializeRetailGameState(data.slice(0x1fc)), new ConfigCollection());
  }

  public static FileData fromV1Matcher(final FileData data) {
    if(data.readInt(0) == MAGIC_V1) {
      return data.slice(0x4);
    }

    return null;
  }

  public static SavedGame fromV1(final String name, final FileData data) {
    return new SavedGame(name, data.readUByte(0x2d), data.readUByte(0x2c), deserializeRetailGameState(data.slice(0x30)), new ConfigCollection());
  }

  public static FileData fromV2Matcher(final FileData data) {
    if(data.readInt(0) == MAGIC_V2) {
      return data.slice(0x4);
    }

    return null;
  }

  public static SavedGame fromV2(final String name, final FileData data) {
    final GameState52c state = new GameState52c();

    int offset = 0;
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

    for(int i = 0; i < state.scriptFlags2_bc.length; i++) {
      state.scriptFlags2_bc[i] = data.readInt(offset);
      offset += 4;
    }

    for(int i = 0; i < state.scriptFlags1_13c.length; i++) {
      state.scriptFlags1_13c[i] = data.readInt(offset);
      offset += 4;
    }

    for(int i = 0; i < state._15c.length; i++) {
      state._15c[i] = data.readInt(offset);
      offset += 4;
    }

    for(int i = 0; i < state._17c.length; i++) {
      state._17c[i] = data.readInt(i);
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

    for(int i = 0; i < state._4b8.length; i++) {
      state._4b8[i] = data.readInt(offset);
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

    return new SavedGame(name, locationType, locationIndex, state, config);
  }

  public static int toV2(final FileData data, final GameState52c state) {
    final int locationType;
    final int locationIndex;
    if(mainCallbackIndex_8004dd20.get() == 8) {
      locationType = 1;
      locationIndex = continentIndex_800bf0b0.get();
      //LAB_80103c98
    } else if(whichMenu_800bdc38 == WhichMenu.RENDER_SAVE_GAME_MENU_19) {
      locationType = 3;
      locationIndex = state.chapterIndex_98;
    } else {
      locationType = 0;
      locationIndex = submapIndex_800bd808.get();
    }

    int offset = 0;
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

    for(final int scriptFlags2 : state.scriptFlags2_bc) {
      data.writeInt(offset, scriptFlags2);
      offset += 4;
    }

    for(final int scriptFlags1 : state.scriptFlags1_13c) {
      data.writeInt(offset, scriptFlags1);
      offset += 4;
    }

    for(final int _15c : state._15c) {
      data.writeInt(offset, _15c);
      offset += 4;
    }

    for(final int _17c :  state._17c) {
      data.writeInt(offset, _17c);
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

    for(final int _4b8 : state._4b8) {
      data.writeInt(offset, _4b8);
      offset += 4;
    }

    data.writeShort(offset, state.pathIndex_4d8);
    offset += 2;
    data.writeShort(offset, state.dotIndex_4da);
    offset += 2;
    data.writeByte(offset, state.dotOffset_4dc);
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

  private static GameState52c deserializeRetailGameState(final FileData data) {
    final GameState52c state = new GameState52c();

    state._04 = data.readInt(0x0);

    for(int i = 0; i < 0x20; i++) {
      state.scriptData_08[i] = data.readInt(0x8 + i * 0x4);
    }

    for(int i = 0; i < 3; i++) {
      state.charIds_88[i] = data.readInt(0x88 + i * 0x4);
    }

    state.gold_94 = data.readInt(0x94);
    state.chapterIndex_98 = data.readInt(0x98);
    state.stardust_9c = data.readInt(0x9c);
    state.timestamp_a0 = data.readInt(0xa0);
    state.submapScene_a4 = data.readInt(0xa4);
    state.submapCut_a8 = data.readInt(0xa8);

    state._b0 = data.readInt(0xb0);
    state._b4 = data.readInt(0xb4);
    state._b8 = data.readInt(0xb8);

    for(int i = 0; i < 0x20; i++) {
      state.scriptFlags2_bc[i] = data.readInt(0xbc + i * 0x4);
    }

    for(int i = 0; i < 8; i++) {
      state.scriptFlags1_13c[i] = data.readInt(0x13c + i * 0x4);
    }

    for(int i = 0; i < 8; i++) {
      state._15c[i] = data.readInt(0x15c + i * 0x4);
    }

    for(int i = 0; i < 8; i++) {
      state._17c[i] = data.readInt(0x17c + i * 0x4);
    }

    for(int i = 0; i < 2; i++) {
      state.goods_19c[i] = data.readInt(0x19c + i * 0x4);
    }

    for(int i = 0; i < 8; i++) {
      state._1a4[i] = data.readInt(0x1a4 + i * 0x4);
    }

    for(int i = 0; i < 8; i++) {
      state.chestFlags_1c4[i] = data.readInt(0x1c4 + i * 0x4);
    }

    for(int i = 0; i < 255; i++) {
      final int id = data.readUByte(0x1e8 + i);

      if(id == 0xff) {
        break;
      }

      state.equipment_1e8.add(id);
    }

    for(int i = 0; i < 64; i++) {
      final int id = data.readUByte(0x2e9 + i);

      if(id == 0xff) {
        break;
      }

      state.items_2e9.add(id);
    }

    for(int charSlot = 0; charSlot < 9; charSlot++) {
      final CharacterData2c charData = state.charData_32c[charSlot];
      final FileData charSlice = data.slice(0x32c + charSlot * 0x2c, 0x2c);

      charData.xp_00 = charSlice.readInt(0x0);
      charData.partyFlags_04 = charSlice.readInt(0x4);
      charData.hp_08 = charSlice.readUShort(0x8);
      charData.mp_0a = charSlice.readUShort(0xa);
      charData.sp_0c = charSlice.readUShort(0xc);
      charData.dlevelXp_0e = charSlice.readUShort(0xe);
      charData.status_10 = charSlice.readUShort(0x10);
      charData.level_12 = charSlice.readUByte(0x12);
      charData.dlevel_13 = charSlice.readUByte(0x13);

      for(int i = 0; i < 5; i++) {
        charData.equipment_14[i] = charSlice.readUByte(0x14 + i);
      }

      charData.selectedAddition_19 = charSlice.readByte(0x19);

      for(int i = 0; i < 8; i++) {
        charData.additionLevels_1a[i] = charSlice.readUByte(0x1a + i);
        charData.additionXp_22[i] = charSlice.readUByte(0x22 + i);
      }
    }

    for(int i = 0; i < 8; i++) {
      state._4b8[i] = data.readInt(0x4b8 + i * 0x4);
    }

    state.pathIndex_4d8 = data.readUShort(0x4d8);
    state.dotIndex_4da = data.readUShort(0x4da);
    state.dotOffset_4dc = data.readUByte(0x4dc);
    state.facing_4dd = data.readByte(0x4dd);
    state.areaIndex_4de = data.readUShort(0x4de);

//    state.mono_4e0 = data.readUByte(0x4e0) != 0;
    state.vibrationEnabled_4e1 = data.readUByte(0x4e1) != 0;
//    state.morphMode_4e2 = data.readUByte(0x4e2);
    state.indicatorsDisabled_4e3 = data.readUByte(0x4e3) != 0;
    state.isOnWorldMap_4e4 = data.readUByte(0x4e4) != 0;

    state.characterInitialized_4e6 = data.readUShort(0x4e6);
//    CONFIG.setConfig(CoreMod.INDICATOR_MODE_CONFIG.get(), IndicatorMode.values()[data.readInt(0x4e8)]);

    return state;
  }
}
