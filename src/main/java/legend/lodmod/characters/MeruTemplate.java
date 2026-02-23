package legend.lodmod.characters;

import legend.game.additions.CharacterAdditionStats;
import legend.game.inventory.Equipment;
import legend.game.inventory.Good;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.lodmod.LodAdditions;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.lodmod.LodGoods.BLUE_DRAGOON_SPIRIT;

public class MeruTemplate extends RetailCharacterTemplate {
  private static final int[] XP = {20, 43, 103, 202, 349, 555, 829, 1180, 1619, 2155, 2797, 3557, 4443, 5464, 6632, 7955, 9443, 11106, 12953, 14995, 17241, 19700, 22383, 25300, 28459, 31870, 35544, 39490, 43718, 48237, 53057, 58189, 63641, 69423, 75545, 82017, 88848, 96049, 103628, 111596, 119963, 128737, 137929, 147549, 157606, 168110, 179070, 190497, 202400, 217885, 233990, 250727, 268107, 286143, 304846, 324230, 344306, 365087, 386584};
  private static final int[] HP = {18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 49, 50, 50, 50, 50, 49, 50, 50, 50, 50, 67, 67, 67, 67, 68, 67, 67, 67, 67, 68, 71, 71, 72, 71, 72, 71, 71, 72, 71, 72, 155, 155, 156, 155, 156, 155, 155, 156, 155, 156, 96, 96, 96, 96, 96, 96, 96, 96, 96};
  private static final int[] ATTACK = {2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 2, 4, 4, 4, 4, 5, 4, 4, 4, 4, 4, 5, 5, 4, 5, 5, 4, 5, 5, 4};
  private static final int[] DEFENSE = {2, 1, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 3, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 3, 2, 3, 2, 3, 2, 2, 3, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2};
  private static final int[] MAGIC_ATTACK = {3, 3, 3, 3, 3, 4, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 4, 4, 3, 4, 4, 3, 4, 4, 4, 3, 4, 4, 4, 4, 3, 4, 4, 4, 4, 3, 4, 4, 3, 4, 4, 3, 4, 4, 4, 4, 4, 5, 4, 4, 5, 4, 4, 5};
  private static final int[] MAGIC_DEFENSE = {4, 3, 3, 3, 3, 4, 3, 3, 3, 3, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 4, 4, 3, 4, 3, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 4, 3, 4, 3, 4, 3, 4, 3, 4, 3, 3, 4, 3, 3, 4, 3, 4, 3};

  @Override
  public CharacterData2c make(final GameState52c gameState) {
    final CharacterData2c character = super.make(gameState);

    character.additionStats.put(LodAdditions.DOUBLE_SMACK.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.HAMMER_SPIN.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.COOL_BOOGIE.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.CATS_CRADLE.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.PERKY_STEP.getId(), new CharacterAdditionStats());

    character.selectedAddition_19 = LodAdditions.DOUBLE_SMACK.getId();

    return character;
  }

  @Override
  public boolean canEquip(final GameState52c gameState, final CharacterData2c character, final EquipmentSlot slot, final Equipment equipment) {
    return (equipment.equipableFlags_03 & 0x1) != 0;
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
      case 2 -> 2000;
      case 3 -> 12000;
      case 4 -> 20000;
      default -> 20000 + (character.dlevel_13 - 4) * 10000;
    };
  }

  @Override
  protected Good getDragoonSpirit() {
    return BLUE_DRAGOON_SPIRIT.get();
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
      return 70;
    }

    return 0;
  }

  @Override
  protected int getAttackToAdd(final int level) {
    if(level < ATTACK.length) {
      return ATTACK[level];
    }

    return (int)(Math.floor(level * 2.94f) - Math.floor((level - 1) * 2.94f));
  }

  @Override
  protected int getDefenseToAdd(final int level) {
    if(level < DEFENSE.length) {
      return DEFENSE[level];
    }

    return (int)(Math.floor(level * 2.4f) - Math.floor((level - 1) * 2.4f));
  }

  @Override
  protected int getMagicAttackToAdd(final int level) {
    if(level < MAGIC_ATTACK.length) {
      return MAGIC_ATTACK[level];
    }

    return (int)(Math.floor(level * 4.5f) - Math.floor((level - 1) * 4.5f));
  }

  @Override
  protected int getMagicDefenseToAdd(final int level) {
    if(level < MAGIC_DEFENSE.length) {
      return MAGIC_DEFENSE[level];
    }

    return (int)(Math.floor(level * 3.96f) - Math.floor((level - 1) * 3.96f));
  }

  @Override
  protected int getDragoonAttackToAdd(final int dlevel) {
    if(dlevel == 0) {
      return 200;
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
      case 1 -> LodAdditions.DOUBLE_SMACK.getId();
      case 21 -> LodAdditions.HAMMER_SPIN.getId();
      case 26 -> LodAdditions.COOL_BOOGIE.getId();
      case 30 -> LodAdditions.CATS_CRADLE.getId();
      default -> null;
    };
  }

  @Override
  protected int getSpellUnlock(final int dlevel) {
    return switch(dlevel) {
      case 1 -> 24;
      case 2 -> 25;
      case 3 -> 27;
      case 5 -> 28;
      default -> -1;
    };
  }
}
