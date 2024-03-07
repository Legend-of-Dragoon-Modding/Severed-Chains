package legend.game.net;

import io.netty.buffer.ByteBuf;
import legend.game.inventory.Equipment;
import legend.game.inventory.Item;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;

import java.nio.charset.StandardCharsets;

import static legend.core.GameEngine.REGISTRIES;

public class GameStatePacket implements Packet<GameState52c> {
  public static int ID = 1;

  @Override
  public int id() {
    return ID;
  }

  @Override
  public void write(final ByteBuf buf, final GameState52c state) {
    buf.writeInt(state._04);

    buf.writeInt(state.scriptData_08.length);
    for(final int scriptData : state.scriptData_08) {
      buf.writeInt(scriptData);
    }

    buf.writeInt(state.charIds_88.length);
    for(final int charId : state.charIds_88) {
      buf.writeInt(charId);
    }

    buf.writeInt(state.gold_94);
    buf.writeInt(state.chapterIndex_98);
    buf.writeInt(state.stardust_9c);
    buf.writeInt(state.timestamp_a0);
    buf.writeInt(state.submapScene_a4);
    buf.writeInt(state.submapCut_a8);
    buf.writeInt(state._b0);
    buf.writeInt(state._b4);
    buf.writeInt(state._b8);

    buf.writeInt(state.scriptFlags2_bc.count());
    for(int i = 0; i < state.scriptFlags2_bc.count(); i++) {
      buf.writeInt(state.scriptFlags2_bc.getRaw(i));
    }

    buf.writeInt(state.scriptFlags1_13c.count());
    for(int i = 0; i < state.scriptFlags1_13c.count(); i++) {
      buf.writeInt(state.scriptFlags1_13c.getRaw(i));
    }

    buf.writeInt(state.wmapFlags_15c.count());
    for(int i = 0; i < state.wmapFlags_15c.count(); i++) {
      buf.writeInt(state.wmapFlags_15c.getRaw(i));
    }

    buf.writeInt(state.visitedLocations_17c.count());
    for(int i = 0; i < state.visitedLocations_17c.count(); i++) {
      buf.writeInt(state.visitedLocations_17c.getRaw(i));
    }

    buf.writeInt(state.goods_19c.length);
    for(final int goods : state.goods_19c) {
      buf.writeInt(goods);
    }

    buf.writeInt(state._1a4.length);
    for(final int _1a4 : state._1a4) {
      buf.writeInt(_1a4);
    }

    buf.writeInt(state.chestFlags_1c4.length);
    for(final int chestFlags : state.chestFlags_1c4) {
      buf.writeInt(chestFlags);
    }

    buf.writeInt(state.equipment_1e8.size());
    for(final Equipment equipment : state.equipment_1e8) {
      buf.writeInt(equipment.getRegistryId().toString().length());
      buf.writeCharSequence(equipment.getRegistryId().toString(), StandardCharsets.US_ASCII);
    }

    buf.writeInt(state.items_2e9.size());
    for(final Item item : state.items_2e9) {
      buf.writeInt(item.getRegistryId().toString().length());
      buf.writeCharSequence(item.getRegistryId().toString(), StandardCharsets.US_ASCII);
    }

    buf.writeInt(state.charData_32c.length);
    for(final CharacterData2c charData : state.charData_32c) {
      buf.writeInt(charData.xp_00);
      buf.writeInt(charData.partyFlags_04);
      buf.writeInt(charData.hp_08);
      buf.writeInt(charData.mp_0a);
      buf.writeInt(charData.sp_0c);
      buf.writeInt(charData.dlevelXp_0e);
      buf.writeInt(charData.status_10);
      buf.writeInt(charData.level_12);
      buf.writeInt(charData.dlevel_13);

      buf.writeInt(charData.equipment_14.size());
      for(final Equipment equipment : charData.equipment_14.values()) {
        buf.writeInt(equipment.getRegistryId().toString().length());
        buf.writeCharSequence(equipment.getRegistryId().toString(), StandardCharsets.US_ASCII);
      }

      buf.writeInt(charData.selectedAddition_19);

      buf.writeInt(charData.additionLevels_1a.length);
      for(final int additionLevel : charData.additionLevels_1a) {
        buf.writeInt(additionLevel);
      }

      buf.writeInt(charData.additionXp_22.length);
      for(final int additionXp : charData.additionXp_22) {
        buf.writeInt(additionXp);
      }
    }

    buf.writeInt(state.pathIndex_4d8);
    buf.writeInt(state.dotIndex_4da);
    buf.writeFloat(state.dotOffset_4dc);
    buf.writeInt(state.facing_4dd);
    buf.writeInt(state.directionalPathIndex_4de);
    buf.writeBoolean(state.vibrationEnabled_4e1);
    buf.writeBoolean(state.indicatorsDisabled_4e3);
    buf.writeBoolean(state.isOnWorldMap_4e4);
  }

  @Override
  public GameState52c read(final ByteBuf buf) {
    final GameState52c state = new GameState52c();

    state._04 = buf.readInt();

    final int scriptDataLen = buf.readInt();
    for(int i = 0; i < scriptDataLen; i++) {
      state.scriptData_08[i] = buf.readInt();
    }

    final int charIdsLen = buf.readInt();
    for(int i = 0; i < charIdsLen; i++) {
      state.charIds_88[i] = buf.readInt();
    }

    state.gold_94 = buf.readInt();
    state.chapterIndex_98 = buf.readInt();
    state.stardust_9c = buf.readInt();
    state.timestamp_a0 = buf.readInt();
    state.submapScene_a4 = buf.readInt();
    state.submapCut_a8 = buf.readInt();
    state._b0 = buf.readInt();
    state._b4 = buf.readInt();
    state._b8 = buf.readInt();

    final int scriptFlags2Len = buf.readInt();
    for(int i = 0; i < scriptFlags2Len; i++) {
      state.scriptFlags2_bc.setRaw(i, buf.readInt());
    }

    final int scriptFlags1Len = buf.readInt();
    for(int i = 0; i < scriptFlags1Len; i++) {
      state.scriptFlags1_13c.setRaw(i, buf.readInt());
    }

    final int wmapFlagsLen = buf.readInt();
    for(int i = 0; i < wmapFlagsLen; i++) {
      state.wmapFlags_15c.setRaw(i, buf.readInt());
    }

    final int visitedLocationsLen = buf.readInt();
    for(int i = 0; i < visitedLocationsLen; i++) {
      state.visitedLocations_17c.setRaw(i, buf.readInt());
    }

    final int goodsLen = buf.readInt();
    for(int i = 0; i < goodsLen; i++) {
      state.goods_19c[i] = buf.readInt();
    }

    final int _1a4Len = buf.readInt();
    for(int i = 0; i < _1a4Len; i++) {
      state._1a4[i] = buf.readInt();
    }

    final int chestLen = buf.readInt();
    for(int i = 0; i < chestLen; i++) {
      state.chestFlags_1c4[i] = buf.readInt();
    }

    final int equipmentLen = buf.readInt();
    state.equipment_1e8.clear();
    for(int i = 0; i < equipmentLen; i++) {
      final int idLen = buf.readInt();
      final Equipment equipment = REGISTRIES.equipment.getEntry(buf.readCharSequence(idLen, StandardCharsets.US_ASCII).toString()).get();
      state.equipment_1e8.add(equipment);
    }

    final int itemsLen = buf.readInt();
    state.items_2e9.clear();
    for(int i = 0; i < itemsLen; i++) {
      final int idLen = buf.readInt();
      final Item item = REGISTRIES.items.getEntry(buf.readCharSequence(idLen, StandardCharsets.US_ASCII).toString()).get();
      state.items_2e9.add(item);
    }

    final int charLen = buf.readInt();
    for(int charIndex = 0; charIndex < charLen; charIndex++) {
      final CharacterData2c charData = state.charData_32c[charIndex];
      charData.xp_00 = buf.readInt();
      charData.partyFlags_04 = buf.readInt();
      charData.hp_08 = buf.readInt();
      charData.mp_0a = buf.readInt();
      charData.sp_0c = buf.readInt();
      charData.dlevelXp_0e = buf.readInt();
      charData.status_10 = buf.readInt();
      charData.level_12 = buf.readInt();
      charData.dlevel_13 = buf.readInt();

      final int charEquipmentLen = buf.readInt();
      charData.equipment_14.clear();
      for(int i = 0; i < charEquipmentLen; i++) {
        final int idLen = buf.readInt();
        final Equipment equipment = REGISTRIES.equipment.getEntry(buf.readCharSequence(idLen, StandardCharsets.US_ASCII).toString()).get();
        charData.equipment_14.put(EquipmentSlot.fromLegacy(i), equipment);
      }

      charData.selectedAddition_19 = buf.readInt();

      final int additionLevelsLen = buf.readInt();
      for(int i = 0; i < additionLevelsLen; i++) {
        charData.additionLevels_1a[i] = buf.readInt();
      }

      final int additionXpLen = buf.readInt();
      for(int i = 0; i < additionXpLen; i++) {
        charData.additionXp_22[i] = buf.readInt();
      }
    }

    state.pathIndex_4d8 = buf.readInt();
    state.dotIndex_4da = buf.readInt();
    state.dotOffset_4dc = buf.readFloat();
    state.facing_4dd = buf.readInt();
    state.directionalPathIndex_4de = buf.readInt();
    state.vibrationEnabled_4e1 = buf.readBoolean();
    state.indicatorsDisabled_4e3 = buf.readBoolean();
    state.isOnWorldMap_4e4 = buf.readBoolean();

    return state;
  }
}
