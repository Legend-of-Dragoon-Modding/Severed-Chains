package legend.lodmod;

import legend.core.memory.types.IntRef;
import legend.game.fmv.Fmv;
import legend.game.inventory.Equipment;
import legend.game.inventory.Item;
import legend.game.saves.campaigns.CampaignType;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Map;

import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.SItem.xpTables;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8005.collidedPrimitiveIndex_80052c38;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class LegendOfDragoonCampaign extends CampaignType {
  private static final int[] characterStartingLevels = {1, 3, 4, 8, 13, 15, 17, 19, 23};
  private static final int[] startingAddition_800ce758 = {0, 8, -1, 14, 29, 8, 23, 19, -1};

  @Override
  public void setUpNewCampaign(final GameState52c state) {
    state.charIds_88[0] = 0;
    state.charIds_88[1] = -1;
    state.charIds_88[2] = -1;

    //LAB_800c723c
    for(int charIndex = 0; charIndex < 9; charIndex++) {
      final CharacterData2c charData = state.charData_32c[charIndex];
      final int level = characterStartingLevels[charIndex];
      charData.xp_00 = xpTables[charIndex][level];
      charData.hp_08 = levelStuff_80111cfc[charIndex][level].hp_00;
      charData.mp_0a = magicStuff_80111d20[charIndex][1].mp_00;
      charData.sp_0c = 0;
      charData.dlevelXp_0e = 0;
      charData.status_10 = 0;
      charData.level_12 = level;
      charData.dlevel_13 = 1;

      //LAB_800c7294
      for(int additionIndex = 0; additionIndex < 8; additionIndex++) {
        charData.additionLevels_1a[additionIndex] = 0;
        charData.additionXp_22[additionIndex] = 0;
      }

      charData.additionLevels_1a[0] = 1;

      //LAB_800c72d4
      for(int i = 1; i < level; i++) {
        final int index = levelStuff_80111cfc[charIndex][i].addition_02;

        if(index != -1) {
          final int offset = additionOffsets_8004f5ac[charIndex];
          charData.additionLevels_1a[index - offset] = 1;
        }

        //LAB_800c72fc
      }

      //LAB_800c730c
      charData.selectedAddition_19 = startingAddition_800ce758[charIndex];
    }

    state.charData_32c[0].partyFlags_04 = 0x3;

    state.items_2e9.add(LodItems.BURN_OUT.get());
    state.items_2e9.add(LodItems.HEALING_POTION.get());
    state.items_2e9.add(LodItems.HEALING_POTION.get());

    final Map<EquipmentSlot, Equipment> dart = state.charData_32c[0].equipment_14;
    dart.put(EquipmentSlot.WEAPON, LodEquipment.BROAD_SWORD.get());
    dart.put(EquipmentSlot.HELMET, LodEquipment.BANDANA.get());
    dart.put(EquipmentSlot.ARMOUR, LodEquipment.LEATHER_ARMOR.get());
    dart.put(EquipmentSlot.BOOTS, LodEquipment.LEATHER_BOOTS.get());
    dart.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> lavitz = state.charData_32c[1].equipment_14;
    lavitz.put(EquipmentSlot.WEAPON, LodEquipment.SPEAR.get());
    lavitz.put(EquipmentSlot.HELMET, LodEquipment.SALLET.get());
    lavitz.put(EquipmentSlot.ARMOUR, LodEquipment.SCALE_ARMOR.get());
    lavitz.put(EquipmentSlot.BOOTS, LodEquipment.LEATHER_BOOTS.get());
    lavitz.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> shana = state.charData_32c[2].equipment_14;
    shana.put(EquipmentSlot.WEAPON, LodEquipment.SHORT_BOW.get());
    shana.put(EquipmentSlot.HELMET, LodEquipment.FELT_HAT.get());
    shana.put(EquipmentSlot.ARMOUR, LodEquipment.CLOTHES.get());
    shana.put(EquipmentSlot.BOOTS, LodEquipment.LEATHER_SHOES.get());
    shana.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> rose = state.charData_32c[3].equipment_14;
    rose.put(EquipmentSlot.WEAPON, LodEquipment.RAPIER.get());
    rose.put(EquipmentSlot.HELMET, LodEquipment.FELT_HAT.get());
    rose.put(EquipmentSlot.ARMOUR, LodEquipment.LEATHER_JACKET.get());
    rose.put(EquipmentSlot.BOOTS, LodEquipment.LEATHER_SHOES.get());
    rose.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> haschel = state.charData_32c[4].equipment_14;
    haschel.put(EquipmentSlot.WEAPON, LodEquipment.IRON_KNUCKLE.get());
    haschel.put(EquipmentSlot.HELMET, LodEquipment.ARMET.get());
    haschel.put(EquipmentSlot.ARMOUR, LodEquipment.DISCIPLE_VEST.get());
    haschel.put(EquipmentSlot.BOOTS, LodEquipment.IRON_KNEEPIECE.get());
    haschel.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> albert = state.charData_32c[5].equipment_14;
    albert.put(EquipmentSlot.WEAPON, LodEquipment.SPEAR.get());
    albert.put(EquipmentSlot.HELMET, LodEquipment.SALLET.get());
    albert.put(EquipmentSlot.ARMOUR, LodEquipment.SCALE_ARMOR.get());
    albert.put(EquipmentSlot.BOOTS, LodEquipment.LEATHER_BOOTS.get());
    albert.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> meru = state.charData_32c[6].equipment_14;
    meru.put(EquipmentSlot.WEAPON, LodEquipment.MACE.get());
    meru.put(EquipmentSlot.HELMET, LodEquipment.TIARA.get());
    meru.put(EquipmentSlot.ARMOUR, LodEquipment.SILVER_VEST.get());
    meru.put(EquipmentSlot.BOOTS, LodEquipment.SOFT_BOOTS.get());
    meru.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> kongol = state.charData_32c[7].equipment_14;
    kongol.put(EquipmentSlot.WEAPON, LodEquipment.AXE.get());
    kongol.put(EquipmentSlot.HELMET, LodEquipment.ARMET.get());
    kongol.put(EquipmentSlot.ARMOUR, LodEquipment.LION_FUR.get());
    kongol.put(EquipmentSlot.BOOTS, LodEquipment.IRON_KNEEPIECE.get());
    kongol.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> miranda = state.charData_32c[8].equipment_14;
    miranda.put(EquipmentSlot.WEAPON, LodEquipment.SHORT_BOW.get());
    miranda.put(EquipmentSlot.HELMET, LodEquipment.FELT_HAT.get());
    miranda.put(EquipmentSlot.ARMOUR, LodEquipment.CLOTHES.get());
    miranda.put(EquipmentSlot.BOOTS, LodEquipment.LEATHER_SHOES.get());
    miranda.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    state.gold_94 = 20;
  }

  @Override
  public void transitionToNewCampaign(final GameState52c state) {
    Fmv.playCurrentFmv(2, LodMod.SUBMAP_STATE_TYPE.get());
  }

  @Override
  public void setUpLoadedGame(final GameState52c state) {
    submapScene_80052c34 = gameState_800babc8.submapScene_a4;
    submapCut_80052c30 = gameState_800babc8.submapCut_a8;
    collidedPrimitiveIndex_80052c38 = gameState_800babc8.submapCut_a8;

    if(gameState_800babc8.submapCut_a8 == 264) { // Somewhere in Home of Giganto
      submapScene_80052c34 = 53;
    }
  }

  @Override
  public void transitionToLoadedGame(final GameState52c state) {
    if(state.isOnWorldMap_4e4) {
      engineStateOnceLoaded_8004dd24 = LodMod.WORLD_MAP_STATE_TYPE.get();
    } else {
      engineStateOnceLoaded_8004dd24 = LodMod.SUBMAP_STATE_TYPE.get();
    }
  }

  @Override
  public void saveGameState(final FileData data, final IntRef offset, final GameState52c gameState) {
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
    data.writeByte(offset, gameState.indicatorsDisabled_4e3 ? 1 : 0);
  }

  @Override
  public GameState52c loadGameState(final FileData data, final IntRef offset) {
    final GameState52c state = new GameState52c();
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

    for(int i = 0; i < state.goods_19c.length; i++) {
      state.goods_19c[i] = data.readInt(offset);
    }

    for(int i = 0; i < state._1a4.length; i++) {
      state._1a4[i] = data.readInt(offset);
    }

    for(int i = 0; i < state.chestFlags_1c4.length; i++) {
      state.chestFlags_1c4[i] = data.readInt(offset);
    }

    final int equipmentCount = data.readUShort(offset);
    final int itemCount = data.readUShort(offset);

    for(int i = 0; i < equipmentCount; i++) {
      final RegistryId equipmentId = data.readRegistryId(offset);
      state.equipmentRegistryIds_1e8.add(equipmentId);
    }

    for(int i = 0; i < itemCount; i++) {
      final RegistryId itemId = data.readRegistryId(offset);
      state.itemRegistryIds_2e9.add(itemId);
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

    return state;
  }
}
