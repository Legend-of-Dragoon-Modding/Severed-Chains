package legend.game.saves.serializers;

import legend.game.modding.coremod.CoreMod;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.SavedGame;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;

import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;

public final class V2Serializer {
  private V2Serializer() { }

  public static final int MAGIC_V2 = 0x32615344; // DSa2

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
      state.equipmentIds_1e8.add(data.readUByte(offset));
      offset++;
    }

    for(int i = 0; i < itemCount; i++) {
      state.itemIds_2e9.add(data.readUByte(offset));
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

      for(int i = 0; i < 5; i++) {
        charData.equipmentIds_14.put(EquipmentSlot.fromLegacy(i), data.readUByte(offset));
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

    final ConfigCollection config = new ConfigCollection();
    ConfigStorage.loadConfig(config, ConfigStorageLocation.SAVE, data.slice(offset));

    final CharacterData2c charData = state.charData_32c[state.charIds_88[0]];
    final int maxHp = CoreMod.CHARACTER_DATA[state.charIds_88[0]].statsTable[charData.level_12].hp_00;
    final int maxMp = CoreMod.CHARACTER_DATA[state.charIds_88[0]].dragoonStatsTable[charData.dlevel_13].mp_00;
    return new SavedGame(name, name, locationType, locationIndex, state, config, maxHp, maxMp);
  }
}
