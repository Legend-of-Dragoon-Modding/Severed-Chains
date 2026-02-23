package legend.lodmod.characters;

import legend.game.additions.CharacterAdditionStats;
import legend.game.inventory.Equipment;
import legend.game.inventory.Good;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.lodmod.LodAdditions;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.lodmod.LodGoods.VIOLET_DRAGOON_SPIRIT;

public class HaschelTemplate extends RetailCharacterTemplate {
  private static final int[] XP = {20, 43, 103, 202, 349, 554, 827, 1178, 1616, 2150, 2792, 3550, 4434, 5454, 6619, 7939, 9424, 11084, 12928, 14965, 17207, 19661, 22339, 25250, 28402, 31807, 35474, 39412, 43632, 48142, 52953, 58074, 63515, 69286, 75396, 81855, 88673, 95859, 103424, 111376, 119726, 128483, 137657, 147258, 157294, 167777, 178716, 190120, 202000, 217455, 233528, 250231, 267577, 285577, 304244, 323589, 343626, 364365, 385820};
  private static final int[] HP = {27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 74, 75, 75, 74, 75, 75, 74, 75, 75, 75, 100, 101, 101, 101, 101, 100, 101, 101, 101, 101, 107, 107, 107, 107, 107, 107, 107, 107, 107, 108, 233, 233, 233, 233, 233, 233, 233, 233, 233, 234, 144, 144, 144, 144, 144, 144, 144, 144, 144};
  private static final int[] ATTACK = {3, 2, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 3, 3, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 4, 3, 4, 3, 4, 3, 4, 4};
  private static final int[] DEFENSE = {4, 2, 2, 2, 2, 3, 2, 2, 2, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 3, 2, 3, 3, 2, 3, 3, 3};
  private static final int[] MAGIC_ATTACK = {2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 4, 5, 4, 5, 4, 4, 5, 4, 5, 4, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 4, 5, 4, 5, 5, 4, 5, 4, 5};
  private static final int[] MAGIC_DEFENSE = {2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 4, 3, 3, 3, 4, 3, 3, 3, 4, 3, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 5, 4, 4, 4, 4, 4, 5, 4, 4, 4, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 4, 5, 4, 5, 4, 5, 4, 5, 4};

  @Override
  public CharacterData2c make(final GameState52c gameState) {
    final CharacterData2c character = super.make(gameState);

    character.additionStats.put(LodAdditions.DOUBLE_PUNCH.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.FERRY_OF_STYX.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.SUMMON_4_GODS.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.FIVE_RING_SHATTERING.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.HEX_HAMMER.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.OMNI_SWEEP.getId(), new CharacterAdditionStats());

    character.selectedAddition_19 = LodAdditions.DOUBLE_PUNCH.getId();

    return character;
  }

  @Override
  public boolean canEquip(final GameState52c gameState, final CharacterData2c character, final EquipmentSlot slot, final Equipment equipment) {
    return (equipment.equipableFlags_03 & 0x10) != 0;
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
  protected Good getDragoonSpirit() {
    return VIOLET_DRAGOON_SPIRIT.get();
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
      return 60;
    }

    return 0;
  }

  @Override
  protected int getAttackToAdd(final int level) {
    if(level < ATTACK.length) {
      return ATTACK[level];
    }

    return (int)(Math.floor(level * 3.76f) - Math.floor((level - 1) * 3.76f));
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

    return (int)(Math.floor(level * 2.96f) - Math.floor((level - 1) * 2.96f));
  }

  @Override
  protected int getMagicDefenseToAdd(final int level) {
    if(level < MAGIC_DEFENSE.length) {
      return MAGIC_DEFENSE[level];
    }

    return (int)(Math.floor(level * 2.96f) - Math.floor((level - 1) * 2.96f));
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
  protected RegistryId getAdditionUnlock(final int level) {
    return switch(level) {
      case 1 -> LodAdditions.DOUBLE_PUNCH.getId();
      case 14 -> LodAdditions.FERRY_OF_STYX.getId();
      case 18 -> LodAdditions.SUMMON_4_GODS.getId();
      case 22 -> LodAdditions.FIVE_RING_SHATTERING.getId();
      case 27 -> LodAdditions.HEX_HAMMER.getId();
      default -> null;
    };
  }

  @Override
  protected int getSpellUnlock(final int dlevel) {
    return switch(dlevel) {
      case 1 -> 20;
      case 2 -> 21;
      case 3 -> 22;
      case 5 -> 23;
      default -> -1;
    };
  }
}
