package legend.lodmod.characters;

import legend.game.additions.CharacterAdditionStats;
import legend.game.inventory.Equipment;
import legend.game.inventory.Good;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.lodmod.LodAdditions;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.lodmod.LodGoods.DARK_DRAGOON_SPIRIT;

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

    character.additionStats.put(LodAdditions.WHIP_SMACK.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.MORE_MORE.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.HARD_BLADE.getId(), new CharacterAdditionStats());
    character.additionStats.put(LodAdditions.DEMONS_DANCE.getId(), new CharacterAdditionStats());

    character.selectedAddition_19 = LodAdditions.WHIP_SMACK.getId();

    return character;
  }

  @Override
  public boolean canEquip(final GameState52c gameState, final CharacterData2c character, final EquipmentSlot slot, final Equipment equipment) {
    return (equipment.equipableFlags_03 & 0x4) != 0;
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
  protected Good getDragoonSpirit() {
    return DARK_DRAGOON_SPIRIT.get();
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
  protected RegistryId getAdditionUnlock(final int level) {
    return switch(level) {
      case 1 -> LodAdditions.WHIP_SMACK.getId();
      case 14 -> LodAdditions.MORE_MORE.getId();
      case 19 -> LodAdditions.HARD_BLADE.getId();
      default -> null;
    };
  }

  @Override
  protected int getSpellUnlock(final int dlevel) {
    return switch(dlevel) {
      case 1 -> 15;
      case 2 -> 16;
      case 3 -> 18;
      case 5 -> 19;
      default -> -1;
    };
  }
}
