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

import static legend.lodmod.LodGoods.DARK_DRAGOON_SPIRIT;
import static legend.lodmod.LodMod.DARK_ELEMENT;

public class RoseTemplate extends RetailCharacterTemplate {
  private static final int[] XP = {20, 44, 104, 204, 353, 561, 838, 1193, 1636, 2178, 2828, 3596, 4491, 5524, 6704, 8041, 9545, 11226, 13094, 15158, 17428, 19914, 22627, 25575, 28768, 32217, 35931, 39919, 44193, 48761, 53634, 58821, 64332, 70177, 76366, 82908, 89814, 97093, 104755, 112809, 121267, 130137, 139429, 149153, 159319, 169937, 181016, 192567, 204600, 220253, 236533, 253452, 271021, 289253, 308160, 327754, 348049, 369055, 390786};
  private static final int[] HP = {21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 58, 58, 58, 58, 58, 58, 58, 58, 58, 59, 78, 78, 79, 78, 79, 78, 78, 79, 78, 79, 83, 83, 83, 84, 83, 83, 84, 83, 83, 84, 181, 181, 181, 182, 181, 181, 182, 181, 181, 182, 112, 112, 112, 112, 112, 112, 112, 112, 112};
  private static final int[] ATTACK = {3, 3, 3, 4, 3, 4, 3, 4, 3, 4, 3, 2, 1, 2, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 4, 4, 5, 4, 4, 4, 4, 5, 4, 4, 1, 1, 1, 1, 1, 2, 1, 1, 1};
  private static final int[] DEFENSE = {5, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 3, 4, 3, 3, 4, 3, 3, 3, 4, 3, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 4, 4, 4, 5, 4, 4, 4, 4, 5, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1};
  private static final int[] MAGIC_ATTACK = {6, 4, 4, 4, 4, 5, 4, 4, 4, 4, 5, 1, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 2, 1, 2, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 2, 2, 2, 2, 2, 2, 2, 2, 3};
  private static final int[] MAGIC_DEFENSE = {5, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 2, 3, 2, 2, 2, 2, 2, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 2, 1, 2, 2, 2, 2, 2, 2, 2};

  @Override
  public CharacterData2c make(final GameState52c gameState) {
    final CharacterData2c character = super.make(gameState);

    character.addAddition(LodAdditions.WHIP_SMACK.getId(), new CharacterAdditionInfo(List.of()));
    character.addAddition(LodAdditions.MORE_MORE.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(14))));
    character.addAddition(LodAdditions.HARD_BLADE.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(19))));
    character.addAddition(LodAdditions.DEMONS_DANCE.getId(), new CharacterAdditionInfo(List.of(new AdditionMasteryUnlockCriterion())));

    character.addSpell(LodSpells.ASTRAL_DRAIN.getId(), new CharacterSpellInfo(List.of()));
    character.addSpell(LodSpells.DEATH_DIMENSION.getId(), new CharacterSpellInfo(List.of(new SpellDragoonLevelUnlockCriterion(2))));
    character.addSpell(LodSpells.DEMONS_GATE.getId(), new CharacterSpellInfo(List.of(new SpellDragoonLevelUnlockCriterion(3))));
    character.addSpell(LodSpells.DARK_DRAGON.getId(), new CharacterSpellInfo(List.of(new SpellDragoonLevelUnlockCriterion(5))));

    character.selectedAddition_19 = LodAdditions.WHIP_SMACK.getId();

    return character;
  }

  @Override
  public boolean canEquip(final CharacterData2c character, final EquipmentSlot slot, final Equipment equipment) {
    return (equipment.equipableFlags_03 & 0x4) != 0;
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
  public Element getElement(final CharacterData2c character) {
    return DARK_ELEMENT.get();
  }

  @Override
  protected Good getDragoonSpirit() {
    return DARK_DRAGOON_SPIRIT.get();
  }

  @Override
  public AdditionHits80 getDragoonAddition(final CharacterData2c character) {
    return new AdditionHits80(new AdditionHitProperties10(0xc0, 0, 0, 0, 100, 0, 18, 0, 0, 0, 0, 0, 12, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 12, 0, 0, 0, new AdditionSound(10, 3), new AdditionSound(1, 1)), new AdditionHitProperties10(0xc0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 12, 0, 0, 0, new AdditionSound(10, 3), new AdditionSound(0, 1)), new AdditionHitProperties10(0xc0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 9, 32, 0, 0, new AdditionSound(11, 42), new AdditionSound(3, 40)), new AdditionHitProperties10(0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 10), new AdditionSound(1, 8)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 11), new AdditionSound(2, 9)));
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
      return 55;
    }

    return 0;
  }

  @Override
  protected int getAttackToAdd(final int level) {
    if(level < ATTACK.length) {
      return ATTACK[level];
    }

    return (int)(Math.floor(level * 2.84f) - Math.floor((level - 1) * 2.84f));
  }

  @Override
  protected int getDefenseToAdd(final int level) {
    if(level < DEFENSE.length) {
      return DEFENSE[level];
    }

    return (int)(Math.floor(level * 2.84f) - Math.floor((level - 1) * 2.84f));
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
    return 0x1090d8;
  }

  @Override
  public int getSpellRingColour(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x5e263a;
  }

  @Override
  public int getLeftHandModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 8;
  }

  @Override
  public int getRightHandModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 9;
  }

  @Override
  public int getFootModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 11;
  }

  @Override
  public int getWeaponModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 18;
  }

  @Override
  public int getWeaponTrailVertexComponent(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 2;
  }

  @Override
  public int getShadowSize(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0xe00;
  }

  @Override
  public int getDragoonTransformDeff(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x26;
  }

  @Override
  public int getDragoonAttackDeff(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x33;
  }

  @Override
  public int getDragoonAttackSounds(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x6b;
  }
}
