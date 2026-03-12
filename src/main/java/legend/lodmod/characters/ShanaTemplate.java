package legend.lodmod.characters;

import legend.game.additions.AdditionHits80;
import legend.game.characters.CharacterData2c;
import legend.game.characters.CharacterSpellInfo;
import legend.game.characters.Element;
import legend.game.characters.SpellDragoonLevelUnlockCriterion;
import legend.game.characters.SpellDragoonSpiritUnlockCriterion;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.inventory.Equipment;
import legend.game.inventory.Good;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;
import legend.lodmod.LodSpells;

import java.util.List;
import java.util.function.Consumer;

import static legend.game.DrgnFiles.loadDrgnDir;
import static legend.lodmod.LodGoods.SILVER_DRAGOON_SPIRIT;
import static legend.lodmod.LodMod.LIGHT_ELEMENT;

public class ShanaTemplate extends RetailCharacterTemplate {
  private static final int[] XP = {30, 50, 100, 204, 352, 559, 835, 1189, 1632, 2172, 2820, 3585, 4478, 5508, 6684, 8018, 9517, 11193, 13056, 15113, 17377, 19856, 22560, 25500, 28684, 32122, 35825, 39802, 44064, 48618, 53477, 58649, 64144, 69972, 76142, 82665, 89551, 96808, 104448, 112479, 120911, 129755, 139020, 148716, 158852, 169439, 180486, 192003, 204000, 219608, 235840, 252709, 270226, 288405, 307256, 326793, 347028, 367973, 389640};
  private static final int[] HP = {24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 66, 66, 67, 66, 67, 66, 66, 67, 66, 67, 89, 90, 89, 90, 90, 89, 90, 89, 90, 90, 95, 95, 95, 95, 96, 95, 95, 95, 95, 96, 207, 207, 207, 207, 208, 207, 207, 207, 207, 208, 128, 128, 128, 128, 128, 128, 128, 128, 128};
  private static final int[] ATTACK = {2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 3, 2, 2};
  private static final int[] DEFENSE = {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 3, 4, 3, 4, 4, 3, 4, 3, 4, 4, 5, 5, 6, 5, 5, 6, 5, 5, 6};
  private static final int[] MAGIC_ATTACK = {3, 3, 3, 3, 4, 3, 3, 4, 3, 3, 4, 5, 6, 6, 5, 6, 6, 5, 6, 6, 6, 5, 6, 5, 6, 5, 6, 5, 6, 6, 5, 5, 5, 6, 5, 5, 5, 5, 5, 5, 6, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3};
  private static final int[] MAGIC_DEFENSE = {3, 3, 3, 3, 3, 4, 3, 3, 3, 3, 4, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5, 5, 6, 5, 6, 5, 6, 5, 6, 5, 5, 5, 5, 6, 5, 5, 5, 5, 6, 2, 3, 2, 3, 3, 2, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2};

  @Override
  public CharacterData2c make(final GameState52c gameState) {
    final CharacterData2c character = super.make(gameState);

    character.addSpell(LodSpells.STAR_CHILDREN.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion()))).unlock(gameState.timestamp_a0);
    character.addSpell(LodSpells.MOON_LIGHT.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion(), new SpellDragoonLevelUnlockCriterion(2))));
    character.addSpell(LodSpells.GATES_OF_HEAVEN.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion(), new SpellDragoonLevelUnlockCriterion(3))));
    character.addSpell(LodSpells.WHITE_SILVER_DRAGON.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion(), new SpellDragoonLevelUnlockCriterion(5))));

    return character;
  }

  @Override
  public void loadWorldMapModel(final CharacterData2c character, final Consumer<List<FileData>> onLoad) {
    Loader.loadFiles(onLoad, "SECT/DRGN22.BIN/836/33", "SECT/DRGN22.BIN/836/textures/1", "SECT/DRGN22.BIN/836/34", "SECT/DRGN22.BIN/836/35", "SECT/DRGN22.BIN/836/36");
  }

  @Override
  public boolean canEquip(final CharacterData2c character, final EquipmentSlot slot, final Equipment equipment) {
    return (equipment.equipableFlags_03 & 0x2) != 0;
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
      case 1 -> 1000;
      case 2 -> 6000;
      case 3 -> 12000;
      case 4 -> 20000;
      default -> 20000 + (character.dlevel_13 - 4) * 10000;
    };
  }

  @Override
  public Element getElement(final CharacterData2c character) {
    return LIGHT_ELEMENT.get();
  }

  @Override
  protected Good getDragoonSpirit() {
    return SILVER_DRAGOON_SPIRIT.get();
  }

  @Override
  public AdditionHits80 getDragoonAddition(final CharacterData2c character) {
    return null;
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
      return 65;
    }

    return 0;
  }

  @Override
  protected int getAttackToAdd(final int level) {
    if(level < ATTACK.length) {
      return ATTACK[level];
    }

    return (int)(Math.floor(level * 2.08f) - Math.floor((level - 1) * 2.08f));
  }

  @Override
  protected int getDefenseToAdd(final int level) {
    if(level < DEFENSE.length) {
      return DEFENSE[level];
    }

    return (int)(Math.floor(level * 2.98f) - Math.floor((level - 1) * 2.98f));
  }

  @Override
  protected int getMagicAttackToAdd(final int level) {
    if(level < MAGIC_ATTACK.length) {
      return MAGIC_ATTACK[level];
    }

    return (int)(Math.floor(level * 5.1f) - Math.floor((level - 1) * 5.1f));
  }

  @Override
  protected int getMagicDefenseToAdd(final int level) {
    if(level < MAGIC_DEFENSE.length) {
      return MAGIC_DEFENSE[level];
    }

    return (int)(Math.floor(level * 5.08f) - Math.floor((level - 1) * 5.08f));
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
  public boolean isArcher(final CharacterData2c character) {
    return true;
  }

  @Override
  public void loadHumanAttackAnimations(final CharacterData2c character, final PlayerBattleEntity bent, final Consumer<List<FileData>> onLoad) {
    loadDrgnDir(0, 4047, onLoad);
  }

  @Override
  public void loadDragoonAttackAnimations(final CharacterData2c character, final PlayerBattleEntity bent, final Consumer<List<FileData>> onLoad) {
    loadDrgnDir(0, 4105, onLoad);
  }

  @Override
  public int getWeaponTrailColour(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x808080;
  }

  @Override
  public int getSpellRingColour(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x6c8283;
  }

  @Override
  public int getParticleColour(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x747044;
  }

  @Override
  public int getLeftHandModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 11;
  }

  @Override
  public int getRightHandModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 12;
  }

  @Override
  public int getFootModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    if(bent.isDragoon()) {
      return 11;
    }

    return 13;
  }

  @Override
  public int getWeaponModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0;
  }

  @Override
  public int getWeaponTrailVertexComponent(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0;
  }

  @Override
  public int getShadowSize(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x1000;
  }

  @Override
  public int getDragoonTransformDeff(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 36;
  }

  @Override
  public int getDragoonAttackDeff(final CharacterData2c character, final PlayerBattleEntity bent) {
    return -1;
  }

  @Override
  public int getDragoonAttackSounds(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 106;
  }
}
