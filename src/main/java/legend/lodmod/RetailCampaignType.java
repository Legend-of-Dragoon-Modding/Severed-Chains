package legend.lodmod;

import legend.game.fmv.Fmv;
import legend.game.saves.CampaignType;
import legend.game.characters.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;

public class RetailCampaignType extends CampaignType {
  @Override
  public void setUpNewCampaign(final GameState52c gameState) {
    gameState.charIds_88.add(0);

    final CharacterData2c dart = gameState.addCharacter(LodCharacterTemplates.DART.get().make(gameState));
    final CharacterData2c lavitz = gameState.addCharacter(LodCharacterTemplates.LAVITZ.get().make(gameState));
    final CharacterData2c shana = gameState.addCharacter(LodCharacterTemplates.SHANA.get().make(gameState));
    final CharacterData2c rose = gameState.addCharacter(LodCharacterTemplates.ROSE.get().make(gameState));
    final CharacterData2c haschel = gameState.addCharacter(LodCharacterTemplates.HASCHEL.get().make(gameState));
    final CharacterData2c albert = gameState.addCharacter(LodCharacterTemplates.ALBERT.get().make(gameState));
    final CharacterData2c meru = gameState.addCharacter(LodCharacterTemplates.MERU.get().make(gameState));
    final CharacterData2c kongol = gameState.addCharacter(LodCharacterTemplates.KONGOL.get().make(gameState));
    final CharacterData2c miranda = gameState.addCharacter(LodCharacterTemplates.MIRANDA.get().make(gameState));

    for(int i = 1; i < 3; i++) {
      lavitz.template.applyLevelUp(lavitz, null);
    }

    for(int i = 1; i < 4; i++) {
      shana.template.applyLevelUp(shana, null);
    }

    for(int i = 1; i < 8; i++) {
      rose.template.applyLevelUp(rose, null);
    }

    for(int i = 1; i < 13; i++) {
      haschel.template.applyLevelUp(haschel, null);
    }

    for(int i = 1; i < 15; i++) {
      albert.template.applyLevelUp(albert, null);
    }

    for(int i = 1; i < 17; i++) {
      meru.template.applyLevelUp(meru, null);
    }

    for(int i = 1; i < 19; i++) {
      kongol.template.applyLevelUp(kongol, null);
    }

    for(int i = 1; i < 23; i++) {
      miranda.template.applyLevelUp(miranda, null);
    }

    gameState.charData_32c.get(0).partyFlags_04 = 0x3;

    gameState.items_2e9.give(LodItems.BURN_OUT.get());
    gameState.items_2e9.give(LodItems.HEALING_POTION.get());
    gameState.items_2e9.give(LodItems.HEALING_POTION.get());

    dart.equip(EquipmentSlot.WEAPON, LodEquipment.BROAD_SWORD.get());
    dart.equip(EquipmentSlot.HELMET, LodEquipment.BANDANA.get());
    dart.equip(EquipmentSlot.ARMOUR, LodEquipment.LEATHER_ARMOR.get());
    dart.equip(EquipmentSlot.BOOTS, LodEquipment.LEATHER_BOOTS.get());
    dart.equip(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    lavitz.equip(EquipmentSlot.WEAPON, LodEquipment.SPEAR.get());
    lavitz.equip(EquipmentSlot.HELMET, LodEquipment.SALLET.get());
    lavitz.equip(EquipmentSlot.ARMOUR, LodEquipment.SCALE_ARMOR.get());
    lavitz.equip(EquipmentSlot.BOOTS, LodEquipment.LEATHER_BOOTS.get());
    lavitz.equip(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    shana.equip(EquipmentSlot.WEAPON, LodEquipment.SHORT_BOW.get());
    shana.equip(EquipmentSlot.HELMET, LodEquipment.FELT_HAT.get());
    shana.equip(EquipmentSlot.ARMOUR, LodEquipment.CLOTHES.get());
    shana.equip(EquipmentSlot.BOOTS, LodEquipment.LEATHER_SHOES.get());
    shana.equip(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    rose.equip(EquipmentSlot.WEAPON, LodEquipment.RAPIER.get());
    rose.equip(EquipmentSlot.HELMET, LodEquipment.FELT_HAT.get());
    rose.equip(EquipmentSlot.ARMOUR, LodEquipment.LEATHER_JACKET.get());
    rose.equip(EquipmentSlot.BOOTS, LodEquipment.LEATHER_SHOES.get());
    rose.equip(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    haschel.equip(EquipmentSlot.WEAPON, LodEquipment.IRON_KNUCKLE.get());
    haschel.equip(EquipmentSlot.HELMET, LodEquipment.ARMET.get());
    haschel.equip(EquipmentSlot.ARMOUR, LodEquipment.DISCIPLE_VEST.get());
    haschel.equip(EquipmentSlot.BOOTS, LodEquipment.IRON_KNEEPIECE.get());
    haschel.equip(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    albert.equip(EquipmentSlot.WEAPON, LodEquipment.SPEAR.get());
    albert.equip(EquipmentSlot.HELMET, LodEquipment.SALLET.get());
    albert.equip(EquipmentSlot.ARMOUR, LodEquipment.SCALE_ARMOR.get());
    albert.equip(EquipmentSlot.BOOTS, LodEquipment.LEATHER_BOOTS.get());
    albert.equip(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    meru.equip(EquipmentSlot.WEAPON, LodEquipment.MACE.get());
    meru.equip(EquipmentSlot.HELMET, LodEquipment.TIARA.get());
    meru.equip(EquipmentSlot.ARMOUR, LodEquipment.SILVER_VEST.get());
    meru.equip(EquipmentSlot.BOOTS, LodEquipment.SOFT_BOOTS.get());
    meru.equip(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    kongol.equip(EquipmentSlot.WEAPON, LodEquipment.AXE.get());
    kongol.equip(EquipmentSlot.HELMET, LodEquipment.ARMET.get());
    kongol.equip(EquipmentSlot.ARMOUR, LodEquipment.LION_FUR.get());
    kongol.equip(EquipmentSlot.BOOTS, LodEquipment.IRON_KNEEPIECE.get());
    kongol.equip(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    miranda.equip(EquipmentSlot.WEAPON, LodEquipment.SHORT_BOW.get());
    miranda.equip(EquipmentSlot.HELMET, LodEquipment.FELT_HAT.get());
    miranda.equip(EquipmentSlot.ARMOUR, LodEquipment.CLOTHES.get());
    miranda.equip(EquipmentSlot.BOOTS, LodEquipment.LEATHER_SHOES.get());
    miranda.equip(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

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
