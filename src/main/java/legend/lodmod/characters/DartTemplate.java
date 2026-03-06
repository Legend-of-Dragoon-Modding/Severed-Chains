package legend.lodmod.characters;

import legend.game.additions.AdditionHitProperties10;
import legend.game.additions.AdditionHits80;
import legend.game.additions.AdditionSound;
import legend.game.characters.AdditionLevelUnlockCriterion;
import legend.game.characters.AdditionMasteryUnlockCriterion;
import legend.game.characters.CharacterAdditionInfo;
import legend.game.characters.CharacterData2c;
import legend.game.characters.CharacterSpellInfo;
import legend.game.characters.Element;
import legend.game.characters.SpellDragoonLevelUnlockCriterion;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.inventory.Equipment;
import legend.game.inventory.Good;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.lodmod.LodAdditions;
import legend.lodmod.LodSpells;

import java.util.List;

import static legend.lodmod.LodGoods.DIVINE_DRAGOON_SPIRIT;
import static legend.lodmod.LodGoods.RED_DRAGOON_SPIRIT;
import static legend.lodmod.LodMod.DIVINE_ELEMENT;
import static legend.lodmod.LodMod.FIRE_ELEMENT;

public class DartTemplate extends RetailCharacterTemplate {
  private static final int[] XP = {20, 43, 102, 200, 345, 548, 819, 1166, 1600, 2129, 2764, 3515, 4390, 5400, 6553, 7860, 9331, 10974, 12800, 14817, 17036, 19467, 22118, 25000, 28121, 31492, 35123, 39022, 43200, 47665, 52428, 57499, 62886, 68600, 74649, 81044, 87795, 94910, 102400, 110273, 118540, 127211, 136294, 145800, 155737, 166116, 176947, 188238, 200000, 215302, 231216, 247754, 264928, 282750, 301232, 320386, 340224, 360758, 382000};
  private static final int[] ATTACK = {2, 2, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 3, 3, 2, 3, 3, 3, 3};
  private static final int[] DEFENSE = {4, 1, 2, 3, 2, 2, 2, 3, 2, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 3, 2, 3, 3, 2, 3, 3, 3};
  private static final int[] MAGIC_ATTACK = {3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3};
  private static final int[] MAGIC_DEFENSE = {4, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 3, 3, 2, 3, 3, 2, 3, 3, 3, 2, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 3, 3, 2, 3, 3, 2, 3, 3, 3, 2, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3};

  @Override
  public CharacterData2c make(final GameState52c gameState) {
    final CharacterData2c original = super.make(gameState);
    final DartCharacterData character = new DartCharacterData(gameState, this, original.stats);
    character.set(original);

    character.addAddition(LodAdditions.DOUBLE_SLASH.getId(), new CharacterAdditionInfo(List.of()));
    character.addAddition(LodAdditions.VOLCANO.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(2))));
    character.addAddition(LodAdditions.BURNING_RUSH.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(8))));
    character.addAddition(LodAdditions.CRUSH_DANCE.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(15))));
    character.addAddition(LodAdditions.MADNESS_HERO.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(22))));
    character.addAddition(LodAdditions.MOON_STRIKE.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(29))));
    character.addAddition(LodAdditions.BLAZING_DYNAMO.getId(), new CharacterAdditionInfo(List.of(new AdditionMasteryUnlockCriterion())));

    character.addSpell(LodSpells.FLAMESHOT.getId(), new CharacterSpellInfo(List.of()));
    character.addSpell(LodSpells.EXPLOSION.getId(), new CharacterSpellInfo(List.of(new SpellDragoonLevelUnlockCriterion(2))));
    character.addSpell(LodSpells.FINAL_BURST.getId(), new CharacterSpellInfo(List.of(new SpellDragoonLevelUnlockCriterion(3))));
    character.addSpell(LodSpells.RED_EYED_DRAGON.getId(), new CharacterSpellInfo(List.of(new SpellDragoonLevelUnlockCriterion(5))));

    character.selectedAddition_19 = LodAdditions.DOUBLE_SLASH.getId();

    return character;
  }

  @Override
  public boolean canEquip(final CharacterData2c character, final EquipmentSlot slot, final Equipment equipment) {
    return (equipment.equipableFlags_03 & 0x80) != 0;
  }

  @Override
  public int getXpToNextLevel(final CharacterData2c character) {
    if(character.level_12 - 1 < XP.length) {
      return XP[character.level_12 - 1];
    }

    return XP[XP.length - 1] + 23000 + (character.level_12 - 1 - XP.length) * 1000;
  }

  @Override
  public int getDxpToNextLevel(final CharacterData2c character) {
    return switch(character.dlevel_13) {
      case 1 -> 1200;
      case 2 -> 6000;
      case 3 -> 12000;
      case 4 -> 20000;
      default -> 20000 + (character.dlevel_13 - 4) * 10000;
    };
  }

  @Override
  public AdditionHits80 getDragoonAddition(final CharacterData2c character) {
    if(character.gameState.goods_19c.has(DIVINE_DRAGOON_SPIRIT.get())) {
      return new AdditionHits80(new AdditionHitProperties10(0xc0, 0, 0, 0, 200, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 11), new AdditionSound(3, 9)), new AdditionHitProperties10(0xc0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 0, 0, 0, 60, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(13, 12), new AdditionSound(1, 10)), new AdditionHitProperties10(0xc0, 0, 0, 0, 80, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 8), new AdditionSound(3, 6)), new AdditionHitProperties10(0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 28), new AdditionSound(1, 26)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 15), new AdditionSound(2, 13)));
    }

    return new AdditionHits80(new AdditionHitProperties10(0xc0, 5, 0, 0, 100, 0, 7, 0, 0, 0, 4, 1, 8, 32, 0, 11, new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0xc0, 15, 4, 0, 10, 0, 0, 0, 0, 0, 14, 0, 7, 32, 0, 0, new AdditionSound(8, 13), new AdditionSound(3, 11)), new AdditionHitProperties10(0xc0, 16, 10, 0, 20, 0, 0, 0, 0, 6, 8, 1, 10, 32, 0, 0, new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0xc0, 11, 5, 0, 30, 0, 0, 0, 0, 4, 6, 0, 9, 32, 0, 0, new AdditionSound(9, 16), new AdditionSound(1, 14)), new AdditionHitProperties10(0xc0, 19, 5, 0, 40, 0, 0, 0, 0, 0, 18, 0, 9, 32, 0, 0, new AdditionSound(10, 20), new AdditionSound(3, 18), new AdditionSound(7, 4), new AdditionSound(7, 10), new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0xc0, 68, 39, 0, 0, 0, 0, 0, 0, 0, 67, 0, 11, 32, 0, 0, new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 14), new AdditionSound(1, 12)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 7), new AdditionSound(2, 5)));
  }

  @Override
  public Element getElement(final CharacterData2c character) {
    return character.gameState.goods_19c.has(DIVINE_DRAGOON_SPIRIT.get()) ? DIVINE_ELEMENT.get() : FIRE_ELEMENT.get();
  }

  @Override
  public boolean hasDragoon(final CharacterData2c character) {
    return super.hasDragoon(character) || character.gameState.goods_19c.has(DIVINE_DRAGOON_SPIRIT.get());
  }

  @Override
  protected Good getDragoonSpirit() {
    return RED_DRAGOON_SPIRIT.get();
  }

  @Override
  protected int getHpToAdd(final int level) {
    if(level <= 10) {
      return 30;
    }

    if(level <= 20) {
      return 83;
    }

    if(level <= 30) {
      return 112;
    }

    if(level <= 40) {
      return 119;
    }

    if(level <= 50) {
      return 259;
    }

    if(level <= 60) {
      return 160;
    }

    return 385;
  }

  @Override
  protected int getSpeedToAdd(final int level) {
    if(level == 0) {
      return 50;
    }

    return 0;
  }

  @Override
  protected int getAttackToAdd(final int level) {
    if(level < ATTACK.length) {
      return ATTACK[level];
    }

    return 3;
  }

  @Override
  protected int getDefenseToAdd(final int level) {
    if(level < DEFENSE.length) {
      return DEFENSE[level];
    }

    return 3;
  }

  @Override
  protected int getMagicAttackToAdd(final int level) {
    if(level < MAGIC_ATTACK.length) {
      return MAGIC_ATTACK[level];
    }

    return 3;
  }

  @Override
  protected int getMagicDefenseToAdd(final int level) {
    if(level < MAGIC_DEFENSE.length) {
      return MAGIC_DEFENSE[level];
    }

    return 3;
  }

  @Override
  protected int getDragoonAttackToAdd(final int dlevel) {
    if(dlevel == 0) {
      return 150;
    }

    return 5;
  }

  @Override
  protected int getDragoonMagicAttackToAdd(final int dlevel) {
    if(dlevel == 0) {
      return 150;
    }

    return 5;
  }

  @Override
  public int getWeaponTrailColour(final CharacterData2c character, final PlayerBattleEntity bent) {
    return character.gameState.goods_19c.has(DIVINE_DRAGOON_SPIRIT.get()) ? 0x808080 : 0x2068e8;
  }

  @Override
  public int getSpellRingColour(final CharacterData2c character, final PlayerBattleEntity bent) {
    return character.gameState.goods_19c.has(DIVINE_DRAGOON_SPIRIT.get()) ? 0x808080 : 0x201996;
  }

  @Override
  public int getLeftHandModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 5;
  }

  @Override
  public int getRightHandModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 6;
  }

  @Override
  public int getFootModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    if(bent.isDragoon()) {
      return character.gameState.goods_19c.has(DIVINE_DRAGOON_SPIRIT.get()) ? 7 : 8;
    }

    return 8;
  }

  @Override
  public int getWeaponModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return character.gameState.goods_19c.has(DIVINE_DRAGOON_SPIRIT.get()) ? 0 : 14;
  }

  @Override
  public int getWeaponTrailVertexComponent(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0;
  }

  @Override
  public int getShadowSize(final CharacterData2c character, final PlayerBattleEntity bent) {
    return character.gameState.goods_19c.has(DIVINE_DRAGOON_SPIRIT.get()) ? 0x1500 : 0x1800;
  }

  @Override
  public int getDragoonTransformDeff(final CharacterData2c character, final PlayerBattleEntity bent) {
    return character.gameState.goods_19c.has(DIVINE_DRAGOON_SPIRIT.get()) ? 0x2e : 0x20;
  }

  @Override
  public int getDragoonAttackDeff(final CharacterData2c character, final PlayerBattleEntity bent) {
    return character.gameState.goods_19c.has(DIVINE_DRAGOON_SPIRIT.get()) ? 0x39 : 0x30;
  }

  @Override
  public int getDragoonAttackSounds(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x68;
  }
}
