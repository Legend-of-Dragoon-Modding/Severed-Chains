package legend.game.saves.serializers;

import legend.game.saves.ConfigCollection;
import legend.game.saves.SavedGame;
import legend.game.saves.types.RetailSaveDisplay;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import legend.lodmod.LodMod;

import java.util.Map;

import static legend.game.SItem.chapterNames_80114248;
import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.SItem.submapNames_8011c108;
import static legend.game.SItem.worldMapNames_8011c1ec;

public final class RetailSerializer {
  private RetailSerializer() { }

  public static final int MAGIC_RETAIL = 0x01114353; // SC__

  public static FileData fromRetailMatcher(final FileData data) {
    if(data.readInt(0) == MAGIC_RETAIL) {
      return data.slice(0x4);
    }

    return null;
  }

  public static SavedGame<RetailSaveDisplay> fromRetail(final String name, final FileData data) {
    final GameState52c state = deserializeRetailGameState(data.slice(0x1fc));
    final CharacterData2c charData = state.charData_32c[state.charIds_88[0]];

    final int locationIndex = data.readUByte(0x1a8);
    final int locationType = data.readUByte(0x1a9);
    final String[] locationNames;
    if(locationType == 1) {
      locationNames = worldMapNames_8011c1ec;
    } else if(locationType == 3) {
      locationNames = chapterNames_80114248;
    } else {
      locationNames = submapNames_8011c108;
    }

    final String locationName = locationNames[locationIndex];
    final int maxHp = levelStuff_80111cfc[state.charIds_88[0]][charData.level_12].hp_00;
    final int maxMp = magicStuff_80111d20[state.charIds_88[0]][charData.dlevel_13].mp_00;

    final RetailSaveDisplay display = new RetailSaveDisplay(locationName, maxHp, maxMp);

    return new SavedGame<>(name, name, LodMod.LEGEND_OF_DRAGOON_CAMPAIGN_TYPE, LodMod.RETAIL_SAVE_TYPE, display, state, new ConfigCollection(), Map.of());
  }

  public static GameState52c deserializeRetailGameState(final FileData data) {
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
      state.scriptFlags2_bc.setRaw(i, data.readInt(0xbc + i * 0x4));
    }

    for(int i = 0; i < 8; i++) {
      state.scriptFlags1_13c.setRaw(i, data.readInt(0x13c + i * 0x4));
    }

    for(int i = 0; i < 8; i++) {
      state.wmapFlags_15c.setRaw(i, data.readInt(0x15c + i * 0x4));
    }

    for(int i = 0; i < 8; i++) {
      state.visitedLocations_17c.setRaw(i, data.readInt(0x17c + i * 0x4));
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

      state.equipmentIds_1e8.add(id);
    }

    for(int i = 0; i < 64; i++) {
      final int id = data.readUByte(0x2e9 + i);

      if(id == 0xff) {
        break;
      }

      state.itemIds_2e9.add(id);
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
        charData.equipmentIds_14.put(EquipmentSlot.fromLegacy(i), charSlice.readUByte(0x14 + i));
      }

      charData.selectedAddition_19 = charSlice.readByte(0x19);

      for(int i = 0; i < 8; i++) {
        charData.additionLevels_1a[i] = charSlice.readUByte(0x1a + i);
        charData.additionXp_22[i] = charSlice.readUByte(0x22 + i);
      }
    }

//    for(int i = 0; i < 8; i++) {
//      state._4b8[i] = data.readInt(0x4b8 + i * 0x4);
//    }

    state.pathIndex_4d8 = data.readUShort(0x4d8);
    state.dotIndex_4da = data.readUShort(0x4da);
    state.dotOffset_4dc = data.readUByte(0x4dc);
    state.facing_4dd = data.readByte(0x4dd);
    state.directionalPathIndex_4de = data.readUShort(0x4de);

//    state.mono_4e0 = data.readUByte(0x4e0) != 0;
//    state.vibrationEnabled_4e1 = data.readUByte(0x4e1) != 0;
//    state.morphMode_4e2 = data.readUByte(0x4e2);
    state.indicatorsDisabled_4e3 = data.readUByte(0x4e3) != 0;
    state.isOnWorldMap_4e4 = data.readUByte(0x4e4) != 0;

    state.characterInitialized_4e6 = data.readUShort(0x4e6);
//    CONFIG.setConfig(CoreMod.INDICATOR_MODE_CONFIG.get(), IndicatorMode.values()[data.readInt(0x4e8)]);

    return state;
  }
}
