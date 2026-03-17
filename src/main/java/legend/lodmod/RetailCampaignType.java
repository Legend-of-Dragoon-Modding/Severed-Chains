package legend.lodmod;

import legend.game.fmv.Fmv;
import legend.game.saves.CampaignType;
import legend.game.characters.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;

import static legend.lodmod.LodMod.HP_STAT;
import static legend.lodmod.LodMod.MP_STAT;

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

    this.levelUp(lavitz, 3);
    this.levelUp(shana, 4);
    this.levelUp(rose, 8);
    this.levelUp(haschel, 13);
    this.levelUp(albert, 15);
    this.levelUp(meru, 17);
    this.levelUp(kongol, 19);
    this.levelUp(miranda, 23);

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

  private void levelUp(final CharacterData2c character, final int level) {
    while(character.level_12 < level - 1) {
      character.applyLevelUp(null);
    }

    character.xp_00 = character.getXpToNextLevel();
    character.applyLevelUp(null);

    character.stats.getStat(HP_STAT.get()).restore();
    character.stats.getStat(MP_STAT.get()).restore();
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
