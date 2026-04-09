package legend.lodmod;

import legend.game.additions.Addition;
import legend.game.additions.CharacterAdditionStats;
import legend.game.fmv.Fmv;
import legend.game.inventory.Equipment;
import legend.game.saves.CampaignType;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import java.util.Map;

import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.SItem.xpTables;
import static legend.game.Scus94491BpeSegment_8004.CHARACTER_ADDITIONS;
import static legend.lodmod.LodAdditions.ALBERT_HARPOON;
import static legend.lodmod.LodAdditions.DOUBLE_PUNCH;
import static legend.lodmod.LodAdditions.DOUBLE_SLASH;
import static legend.lodmod.LodAdditions.DOUBLE_SMACK;
import static legend.lodmod.LodAdditions.HARPOON;
import static legend.lodmod.LodAdditions.PURSUIT;
import static legend.lodmod.LodAdditions.WHIP_SMACK;

public class RetailCampaignType extends CampaignType {
  private static final int[] characterStartingLevels = {1, 3, 4, 8, 13, 15, 17, 19, 23};
  @SuppressWarnings("unchecked")
  private static final RegistryDelegate<Addition>[] startingAddition_800ce758 = new RegistryDelegate[] {
    DOUBLE_SLASH,
    HARPOON,
    null,
    WHIP_SMACK,
    DOUBLE_PUNCH,
    ALBERT_HARPOON,
    DOUBLE_SMACK,
    PURSUIT,
    null,
  };

  @Override
  public void setUpNewCampaign(final GameState52c gameState) {
    gameState.charIds_88.add(0);

    //LAB_800c723c
    for(int charIndex = 0; charIndex < 9; charIndex++) {
      final CharacterData2c charData = gameState.charData_32c[charIndex];
      final int level = characterStartingLevels[charIndex];
      charData.xp_00 = xpTables[charIndex][level];
      charData.hp_08 = levelStuff_80111cfc[charIndex][level].hp_00;
      charData.mp_0a = magicStuff_80111d20[charIndex][1].mp_00;
      charData.sp_0c = 0;
      charData.dlevelXp_0e = 0;
      charData.status_10 = 0;
      charData.level_12 = level;
      charData.dlevel_13 = 1;

      //LAB_800c730c
      if(startingAddition_800ce758[charIndex] != null) {
        charData.selectedAddition_19 = startingAddition_800ce758[charIndex].getId();
      }

      for(final RegistryDelegate<Addition> additions : CHARACTER_ADDITIONS[charIndex]) {
        charData.additionStats.put(additions.getId(), new CharacterAdditionStats());
      }
    }

    gameState.charData_32c[0].partyFlags_04 = 0x3;

    gameState.items_2e9.give(LodItems.BURN_OUT.get());
    gameState.items_2e9.give(LodItems.HEALING_POTION.get());
    gameState.items_2e9.give(LodItems.HEALING_POTION.get());

    final Map<EquipmentSlot, Equipment> dart = gameState.charData_32c[0].equipment_14;
    dart.put(EquipmentSlot.WEAPON, LodEquipment.BROAD_SWORD.get());
    dart.put(EquipmentSlot.HELMET, LodEquipment.BANDANA.get());
    dart.put(EquipmentSlot.ARMOUR, LodEquipment.LEATHER_ARMOR.get());
    dart.put(EquipmentSlot.BOOTS, LodEquipment.LEATHER_BOOTS.get());
    dart.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> lavitz = gameState.charData_32c[1].equipment_14;
    lavitz.put(EquipmentSlot.WEAPON, LodEquipment.SPEAR.get());
    lavitz.put(EquipmentSlot.HELMET, LodEquipment.SALLET.get());
    lavitz.put(EquipmentSlot.ARMOUR, LodEquipment.SCALE_ARMOR.get());
    lavitz.put(EquipmentSlot.BOOTS, LodEquipment.LEATHER_BOOTS.get());
    lavitz.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> shana = gameState.charData_32c[2].equipment_14;
    shana.put(EquipmentSlot.WEAPON, LodEquipment.SHORT_BOW.get());
    shana.put(EquipmentSlot.HELMET, LodEquipment.FELT_HAT.get());
    shana.put(EquipmentSlot.ARMOUR, LodEquipment.CLOTHES.get());
    shana.put(EquipmentSlot.BOOTS, LodEquipment.LEATHER_SHOES.get());
    shana.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> rose = gameState.charData_32c[3].equipment_14;
    rose.put(EquipmentSlot.WEAPON, LodEquipment.RAPIER.get());
    rose.put(EquipmentSlot.HELMET, LodEquipment.FELT_HAT.get());
    rose.put(EquipmentSlot.ARMOUR, LodEquipment.LEATHER_JACKET.get());
    rose.put(EquipmentSlot.BOOTS, LodEquipment.LEATHER_SHOES.get());
    rose.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> haschel = gameState.charData_32c[4].equipment_14;
    haschel.put(EquipmentSlot.WEAPON, LodEquipment.IRON_KNUCKLE.get());
    haschel.put(EquipmentSlot.HELMET, LodEquipment.ARMET.get());
    haschel.put(EquipmentSlot.ARMOUR, LodEquipment.DISCIPLE_VEST.get());
    haschel.put(EquipmentSlot.BOOTS, LodEquipment.IRON_KNEEPIECE.get());
    haschel.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> albert = gameState.charData_32c[5].equipment_14;
    albert.put(EquipmentSlot.WEAPON, LodEquipment.SPEAR.get());
    albert.put(EquipmentSlot.HELMET, LodEquipment.SALLET.get());
    albert.put(EquipmentSlot.ARMOUR, LodEquipment.SCALE_ARMOR.get());
    albert.put(EquipmentSlot.BOOTS, LodEquipment.LEATHER_BOOTS.get());
    albert.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> meru = gameState.charData_32c[6].equipment_14;
    meru.put(EquipmentSlot.WEAPON, LodEquipment.MACE.get());
    meru.put(EquipmentSlot.HELMET, LodEquipment.TIARA.get());
    meru.put(EquipmentSlot.ARMOUR, LodEquipment.SILVER_VEST.get());
    meru.put(EquipmentSlot.BOOTS, LodEquipment.SOFT_BOOTS.get());
    meru.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> kongol = gameState.charData_32c[7].equipment_14;
    kongol.put(EquipmentSlot.WEAPON, LodEquipment.AXE.get());
    kongol.put(EquipmentSlot.HELMET, LodEquipment.ARMET.get());
    kongol.put(EquipmentSlot.ARMOUR, LodEquipment.LION_FUR.get());
    kongol.put(EquipmentSlot.BOOTS, LodEquipment.IRON_KNEEPIECE.get());
    kongol.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> miranda = gameState.charData_32c[8].equipment_14;
    miranda.put(EquipmentSlot.WEAPON, LodEquipment.SHORT_BOW.get());
    miranda.put(EquipmentSlot.HELMET, LodEquipment.FELT_HAT.get());
    miranda.put(EquipmentSlot.ARMOUR, LodEquipment.CLOTHES.get());
    miranda.put(EquipmentSlot.BOOTS, LodEquipment.LEATHER_SHOES.get());
    miranda.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    gameState.gold_94 = 20;
  }

  @Override
  public void transitionToNewCampaign(final GameState52c state) {
    Fmv.playCurrentFmv(2, LodEngineStateTypes.SUBMAP.get());
  }

  @Override
  public void setUpLoadedGame(final GameState52c state) {

  }

  @Override
  public void transitionToLoadedGame(final GameState52c state) {

  }
}
