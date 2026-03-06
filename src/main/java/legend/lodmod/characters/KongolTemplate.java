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

import static legend.lodmod.LodGoods.GOLD_DRAGOON_SPIRIT;
import static legend.lodmod.LodMod.EARTH_ELEMENT;

public class KongolTemplate extends RetailCharacterTemplate {
  private static final int[] XP = {20, 43, 104, 203, 351, 558, 833, 1186, 1627, 2165, 2811, 3574, 4465, 5491, 6665, 7994, 9489, 11160, 13017, 15069, 17326, 19798, 22494, 25425, 28599, 32028, 35720, 39685, 43934, 48475, 53320, 58476, 63955, 69766, 75918, 82422, 89287, 96523, 104140, 112148, 120555, 129373, 138611, 148278, 158385, 168940, 179955, 191438, 203400, 218962, 235146, 251965, 269431, 287556, 306352, 325832, 346007, 366890, 388494};
  private static final int[] HP = {45, 38, 38, 39, 38, 39, 38, 38, 39, 38, 39, 107, 108, 108, 108, 108, 108, 108, 108, 108, 108, 145, 146, 145, 146, 146, 145, 146, 145, 146, 146, 154, 155, 155, 154, 155, 155, 154, 155, 155, 155, 336, 337, 337, 336, 337, 337, 336, 337, 337, 337, 208, 208, 208, 208, 208, 208, 208, 208, 208};
  private static final int[] ATTACK = {4, 5, 5, 4, 5, 5, 5, 5, 5, 5, 5, 4, 5, 4, 5, 5, 4, 5, 4, 5, 5, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 5, 6, 6, 5, 6, 5, 6, 6, 2, 3, 2, 3, 3, 2, 3, 2, 3, 3, 2, 3, 3, 3, 2, 3, 3, 3, 2};
  private static final int[] DEFENSE = {4, 4, 5, 4, 5, 5, 4, 5, 4, 5, 5, 5, 5, 5, 5, 6, 5, 5, 5, 5, 6, 5, 5, 5, 5, 6, 5, 5, 5, 5, 6, 3, 4, 4, 3, 4, 4, 3, 4, 4, 3, 4, 3, 4, 4, 3, 4, 3, 4, 4, 3, 3, 3, 4, 3, 3, 3, 3, 3, 3};
  private static final int[] MAGIC_ATTACK = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 2, 1, 2, 1, 2, 1, 2};
  private static final int[] MAGIC_DEFENSE = {3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 2};

  @Override
  public CharacterData2c make(final GameState52c gameState) {
    final CharacterData2c character = super.make(gameState);

    character.addAddition(LodAdditions.PURSUIT.getId(), new CharacterAdditionInfo(List.of()));
    character.addAddition(LodAdditions.INFERNO.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(23))));
    character.addAddition(LodAdditions.BONE_CRUSH.getId(), new CharacterAdditionInfo(List.of(new AdditionMasteryUnlockCriterion())));

    character.addSpell(LodSpells.GRAND_STREAM.getId(), new CharacterSpellInfo(List.of()));
    character.addSpell(LodSpells.METEOR_STRIKE.getId(), new CharacterSpellInfo(List.of(new SpellDragoonLevelUnlockCriterion(3))));
    character.addSpell(LodSpells.GOLDEN_DRAGON.getId(), new CharacterSpellInfo(List.of(new SpellDragoonLevelUnlockCriterion(5))));

    character.selectedAddition_19 = LodAdditions.PURSUIT.getId();

    return character;
  }

  @Override
  public boolean canEquip(final GameState52c gameState, final CharacterData2c character, final EquipmentSlot slot, final Equipment equipment) {
    return (equipment.equipableFlags_03 & 0x20) != 0;
  }

  @Override
  public int getXpToNextLevel(final GameState52c gameState, final CharacterData2c character) {
    if(character.level_12 - 1 < XP.length) {
      return XP[character.level_12 - 1];
    }

    return XP[XP.length - 1] + 23000 + (character.level_12 - 1 - XP.length) * 1000;
  }

  @Override
  public int getDxpToNextLevel(final GameState52c gameState, final CharacterData2c character) {
    return switch(character.dlevel_13) {
      case 1 -> 1000;
      case 2 -> 6000;
      case 3 -> 12000;
      case 4 -> 20000;
      default -> 20000 + (character.dlevel_13 - 4) * 10000;
    };
  }

  @Override
  public Element getElement(final GameState52c gameState, final CharacterData2c character) {
    return EARTH_ELEMENT.get();
  }

  @Override
  protected Good getDragoonSpirit() {
    return GOLD_DRAGOON_SPIRIT.get();
  }

  @Override
  public AdditionHits80 getDragoonAddition(final GameState52c gameState, final CharacterData2c character) {
    return new AdditionHits80(new AdditionHitProperties10(0xc0, 0, 0, 0, 100, 0, 22, 0, 0, 0, 0, 0, 7, 0, 0, 0, new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0, new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0), new AdditionHitProperties10(0xc0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0), new AdditionHitProperties10(0xc0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), new AdditionHitProperties10(0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
  }

  @Override
  protected int getHpToAdd(final int level) {
    if(level < HP.length) {
      return HP[level];
    }

    return 385;
  }

  @Override
  protected int getSpeedToAdd(final int level) {
    if(level == 0) {
      return 30;
    }

    return 0;
  }

  @Override
  protected int getAttackToAdd(final int level) {
    if(level < ATTACK.length) {
      return ATTACK[level];
    }

    return (int)(Math.floor(level * 5.08f) - Math.floor((level - 1) * 5.08f));
  }

  @Override
  protected int getDefenseToAdd(final int level) {
    if(level < DEFENSE.length) {
      return DEFENSE[level];
    }

    return (int)(Math.floor(level * 5.08f) - Math.floor((level - 1) * 5.08f));
  }

  @Override
  protected int getMagicAttackToAdd(final int level) {
    if(level < MAGIC_ATTACK.length) {
      return MAGIC_ATTACK[level];
    }

    return (int)(Math.floor(level * 1.5f) - Math.floor((level - 1) * 1.5f));
  }

  @Override
  protected int getMagicDefenseToAdd(final int level) {
    if(level < MAGIC_DEFENSE.length) {
      return MAGIC_DEFENSE[level];
    }

    return (int)(Math.floor(level * 1.6f) - Math.floor((level - 1) * 1.6f));
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
      return 200;
    }

    return 5;
  }

  @Override
  public int getWeaponTrailColour(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x90d0f0;
  }

  @Override
  public int getSpellRingColour(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x286499;
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
    return 8;
  }

  @Override
  public int getWeaponModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 9;
  }

  @Override
  public int getWeaponTrailVertexComponent(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 2;
  }

  @Override
  public int getShadowSize(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x2000;
  }

  @Override
  public int getDragoonTransformDeff(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x2c;
  }

  @Override
  public int getDragoonAttackDeff(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x37;
  }

  @Override
  public int getDragoonAttackSounds(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x6f;
  }
}
