package legend.lodmod.characters;

import legend.game.additions.CharacterAdditionStats;
import legend.game.inventory.Equipment;
import legend.game.inventory.Good;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.lodmod.LodAdditions;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.lodmod.LodGoods.DIVINE_DRAGOON_SPIRIT;
import static legend.lodmod.LodGoods.RED_DRAGOON_SPIRIT;

public class DartTemplate extends RetailCharacterTemplate {
  private static final int[] XP = {20, 43, 102, 200, 345, 548, 819, 1166, 1600, 2129, 2764, 3515, 4390, 5400, 6553, 7860, 9331, 10974, 12800, 14817, 17036, 19467, 22118, 25000, 28121, 31492, 35123, 39022, 43200, 47665, 52428, 57499, 62886, 68600, 74649, 81044, 87795, 94910, 102400, 110273, 118540, 127211, 136294, 145800, 155737, 166116, 176947, 188238, 200000, 215302, 231216, 247754, 264928, 282750, 301232, 320386, 340224, 360758, 382000};
  private static final int[] ATTACK = {2, 2, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 3, 3, 2, 3, 3, 3, 3};
  private static final int[] DEFENSE = {4, 1, 2, 3, 2, 2, 2, 3, 2, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 3, 2, 3, 3, 2, 3, 3, 3};
  private static final int[] MAGIC_ATTACK = {3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3};
  private static final int[] MAGIC_DEFENSE = {4, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 3, 3, 2, 3, 3, 2, 3, 3, 3, 2, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 3, 3, 2, 3, 3, 2, 3, 3, 3, 2, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3};

  @Override
  public CharacterData2c make(final GameState52c gameState) {
    final CharacterData2c character = super.make(gameState);

    character.additionStats.put(LodAdditions.DOUBLE_SLASH.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.VOLCANO.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.BURNING_RUSH.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.CRUSH_DANCE.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.MADNESS_HERO.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.MOON_STRIKE.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.BLAZING_DYNAMO.getId(), new CharacterAdditionStats());

    character.selectedAddition_19 = LodAdditions.DOUBLE_SLASH.getId();

    return character;
  }

  @Override
  public boolean canEquip(final GameState52c gameState, final CharacterData2c character, final EquipmentSlot slot, final Equipment equipment) {
    return (equipment.equipableFlags_03 & 0x80) != 0;
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
      case 1 -> 1200;
      case 2 -> 6000;
      case 3 -> 12000;
      case 4 -> 20000;
      default -> 20000 + (character.dlevel_13 - 4) * 10000;
    };
  }

  @Override
  public boolean hasDragoon(final GameState52c gameState, final CharacterData2c character) {
    return super.hasDragoon(gameState, character) || gameState.goods_19c.has(DIVINE_DRAGOON_SPIRIT.get());
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
  protected RegistryId getAdditionUnlock(final int level) {
    return switch(level) {
      case 1 -> LodAdditions.DOUBLE_SLASH.getId();
      case 2 -> LodAdditions.VOLCANO.getId();
      case 8 -> LodAdditions.BURNING_RUSH.getId();
      case 15 -> LodAdditions.CRUSH_DANCE.getId();
      case 22 -> LodAdditions.MADNESS_HERO.getId();
      case 29 -> LodAdditions.MOON_STRIKE.getId();
      default -> null;
    };
  }

  @Override
  protected int getSpellUnlock(final int dlevel) {
    return switch(dlevel) {
      case 1 -> 0;
      case 2 -> 1;
      case 3 -> 2;
      case 5 -> 3;
      default -> -1;
    };
  }
}
