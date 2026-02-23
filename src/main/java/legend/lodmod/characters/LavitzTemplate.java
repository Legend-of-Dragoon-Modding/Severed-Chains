package legend.lodmod.characters;

import legend.game.additions.CharacterAdditionStats;
import legend.game.inventory.Equipment;
import legend.game.inventory.Good;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.lodmod.LodAdditions;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.lodmod.LodGoods.JADE_DRAGOON_SPIRIT;

public class LavitzTemplate extends RetailCharacterTemplate {
  private static final int[] XP = {35, 60, 110, 203, 350, 557, 831, 1183, 1624, 2161, 2806, 3567, 4456, 5481, 6651, 7978, 9471, 11139, 12992, 15039, 17292, 19759, 22450, 25375, 28543, 31965, 35650, 39607, 43848, 48380, 53215, 58361, 63829, 69629, 75769, 82260, 89112, 96334, 103936, 111927, 120318, 129119, 138338, 147987, 158073, 168608, 179601, 191061, 203000, 218531, 234684, 251470, 268901, 286991, 305750, 325191, 345327, 366169, 387730};
  private static final int[] HP = {35, 32, 33, 33, 33, 33, 32, 33, 33, 33, 33, 91, 91, 91, 92, 91, 91, 92, 91, 91, 92, 123, 123, 123, 123, 124, 123, 123, 123, 123, 124, 130, 131, 131, 131, 131, 131, 131, 131, 131, 131, 284, 285, 285, 285, 285, 285, 285, 285, 285, 285, 176, 176, 176, 176, 176, 176, 176, 176, 176};
  private static final int[] ATTACK = {3, 3, 7, 2, 1, 4, 3, 4, 3, 4, 3, 4, 4, 3, 4, 4, 4, 3, 4, 4, 4, 3, 4, 4, 4, 3, 4, 4, 4, 3, 4, 4, 4, 3, 4, 4, 4, 3, 4, 4, 4, 3, 4, 4, 4, 3, 4, 4, 4, 3, 4, 4, 4, 5, 4, 4, 4, 4, 4, 5};
  private static final int[] DEFENSE = {4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 4, 3, 3, 4, 3, 3, 4, 3, 3, 4, 3, 3, 4, 3, 3, 4, 3, 3, 4, 3, 3, 4, 3, 3, 4, 3, 3, 4, 3, 3, 4, 3, 3, 4, 3, 3, 4, 3, 4, 4, 3, 4, 3, 4, 3};
  private static final int[] MAGIC_ATTACK = {2, 1, 1, 2, 1, 1, 2, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 2, 3, 2, 2};
  private static final int[] MAGIC_DEFENSE = {2, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 2, 3, 3, 2, 3, 3, 2, 3, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1};

  @Override
  public CharacterData2c make(final GameState52c gameState) {
    final CharacterData2c character = super.make(gameState);

    character.additionStats.put(LodAdditions.HARPOON.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.SPINNING_CANE.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.ROD_TYPHOON.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.GUST_OF_WIND_DANCE.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.FLOWER_STORM.getId(), new CharacterAdditionStats());

    character.selectedAddition_19 = LodAdditions.HARPOON.getId();

    return character;
  }

  @Override
  public boolean canEquip(final GameState52c gameState, final CharacterData2c character, final EquipmentSlot slot, final Equipment equipment) {
    return (equipment.equipableFlags_03 & 0x40) != 0;
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
    return JADE_DRAGOON_SPIRIT.get();
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
      return 40;
    }

    return 0;
  }

  @Override
  protected int getAttackToAdd(final int level) {
    if(level < ATTACK.length) {
      return ATTACK[level];
    }

    return (int)(Math.floor(level * 4.5f) - Math.floor((level - 1) * 4.5f));
  }

  @Override
  protected int getDefenseToAdd(final int level) {
    if(level < DEFENSE.length) {
      return DEFENSE[level];
    }

    return (int)(Math.floor(level * 3.98f) - Math.floor((level - 1) * 3.98f));
  }

  @Override
  protected int getMagicAttackToAdd(final int level) {
    if(level < MAGIC_ATTACK.length) {
      return MAGIC_ATTACK[level];
    }

    return (int)(Math.floor(level * 2.16f) - Math.floor((level - 1) * 2.16f));
  }

  @Override
  protected int getMagicDefenseToAdd(final int level) {
    if(level < MAGIC_DEFENSE.length) {
      return MAGIC_DEFENSE[level];
    }

    return (int)(Math.floor(level * 1.94f) - Math.floor((level - 1) * 1.94f));
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
      case 1 -> LodAdditions.HARPOON.getId();
      case 5 -> LodAdditions.SPINNING_CANE.getId();
      case 7 -> LodAdditions.ROD_TYPHOON.getId();
      case 11 -> LodAdditions.GUST_OF_WIND_DANCE.getId();
      default -> null;
    };
  }

  @Override
  protected int getSpellUnlock(final int dlevel) {
    return switch(dlevel) {
      case 1 -> 5;
      case 2 -> 7;
      case 3 -> 6;
      case 5 -> 8;
      default -> -1;
    };
  }
}
