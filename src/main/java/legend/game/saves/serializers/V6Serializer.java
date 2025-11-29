package legend.game.saves.serializers;

import legend.core.GameEngine;
import legend.core.memory.types.IntRef;
import legend.game.additions.Addition;
import legend.game.additions.CharacterAdditionStats;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.InventoryEntry;
import legend.game.saves.SavedGame;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.game.Scus94491BpeSegment_8004.CHARACTER_ADDITIONS;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;

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

    for(int charIndex = 0; charIndex < state.charData_32c.length; charIndex++) {
      final CharacterData2c charData = state.charData_32c[charIndex];
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

      final int oldAdditionIndex = data.readShort(offset) - additionOffsets_8004f5ac[charIndex];

      if(CHARACTER_ADDITIONS[charIndex].length != 0) {
        if(oldAdditionIndex >= 0) {
          charData.selectedAddition_19 = CHARACTER_ADDITIONS[charIndex][oldAdditionIndex].getId();
        } else {
          charData.selectedAddition_19 = CHARACTER_ADDITIONS[charIndex][0].getId();
        }
      }

      final int additionCount = data.readShort(offset); // Not yet used

      for(int additionIndex = 0; additionIndex < 8; additionIndex++) {
        final int level = data.readShort(offset);
        final int xp = data.readInt(offset);

        if(additionIndex < CHARACTER_ADDITIONS[charIndex].length) {
          final RegistryDelegate<Addition> addition = CHARACTER_ADDITIONS[charIndex][additionIndex];
          final CharacterAdditionStats stats = new CharacterAdditionStats();
          stats.level = Math.max(0, level - 1);
          stats.xp = xp;
          charData.additionStats.put(addition.getId(), stats);
        }
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
}
