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
import legend.game.characters.SpellDragoonSpiritUnlockCriterion;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.inventory.Equipment;
import legend.game.inventory.Good;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;
import legend.lodmod.LodAdditions;
import legend.lodmod.LodSpells;

import java.util.List;
import java.util.function.Consumer;

import static legend.game.DrgnFiles.loadDrgnDir;
import static legend.lodmod.LodGoods.BLUE_DRAGOON_SPIRIT;
import static legend.lodmod.LodMod.WATER_ELEMENT;

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

    character.addAddition(LodAdditions.DOUBLE_SMACK.getId(), new CharacterAdditionInfo(List.of()));
    character.addAddition(LodAdditions.HAMMER_SPIN.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(21))));
    character.addAddition(LodAdditions.COOL_BOOGIE.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(26))));
    character.addAddition(LodAdditions.CATS_CRADLE.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(30))));
    character.addAddition(LodAdditions.PERKY_STEP.getId(), new CharacterAdditionInfo(List.of(new AdditionMasteryUnlockCriterion())));

    character.addSpell(LodSpells.FREEZING_RING.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion())));
    character.addSpell(LodSpells.RAINBOW_BREATH.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion(), new SpellDragoonLevelUnlockCriterion(2))));
    character.addSpell(LodSpells.DIAMOND_DUST.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion(), new SpellDragoonLevelUnlockCriterion(3))));
    character.addSpell(LodSpells.BLUE_SEA_DRAGON.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion(), new SpellDragoonLevelUnlockCriterion(5))));

    character.selectedAddition_19 = LodAdditions.DOUBLE_SMACK.getId();

    return character;
  }

  @Override
  public void loadWorldMapModel(final CharacterData2c character, final Consumer<List<FileData>> onLoad) {
    Loader.loadFiles(onLoad, "SECT/DRGN22.BIN/836/66", "SECT/DRGN22.BIN/836/textures/2", "SECT/DRGN22.BIN/836/67", "SECT/DRGN22.BIN/836/68", "SECT/DRGN22.BIN/836/69");
  }

  @Override
  public boolean canEquip(final CharacterData2c character, final EquipmentSlot slot, final Equipment equipment) {
    return (equipment.equipableFlags_03 & 0x1) != 0;
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
      case 2 -> 2000;
      case 3 -> 12000;
      case 4 -> 20000;
      default -> 20000 + (character.dlevel_13 - 4) * 10000;
    };
  }

  @Override
  public Element getElement(final CharacterData2c character) {
    return WATER_ELEMENT.get();
  }

  @Override
  protected Good getDragoonSpirit() {
    return BLUE_DRAGOON_SPIRIT.get();
  }

  @Override
  public AdditionHits80 getDragoonAddition(final CharacterData2c character) {
    return new AdditionHits80(new AdditionHitProperties10(0xc0, 0, 0, 0, 100, 0, 28, 0, 0, 0, 0, 0, 6, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, new AdditionSound(10, 21), new AdditionSound(3, 19)), new AdditionHitProperties10(0xc0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, new AdditionSound(9, 46), new AdditionSound(1, 44), new AdditionSound(7, 26), new AdditionSound(7, 39), new AdditionSound(10, 3), new AdditionSound(2, 1)), new AdditionHitProperties10(0xc0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, new AdditionSound(10, 3), new AdditionSound(2, 1)), new AdditionHitProperties10(0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, new AdditionSound(12, 13), new AdditionSound(3, 11)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 14), new AdditionSound(1, 12)));
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
  public void loadDragoonAttackAnimations(final CharacterData2c character, final PlayerBattleEntity bent, final Consumer<List<FileData>> onLoad) {
    loadDrgnDir(0, 4109, onLoad);
  }

  @Override
  public int getWeaponTrailColour(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x808080;
  }

  @Override
  public int getSpellRingColour(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x78462c;
  }

  @Override
  public int getLeftHandModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 6;
  }

  @Override
  public int getRightHandModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 7;
  }

  @Override
  public int getFootModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 12;
  }

  @Override
  public int getWeaponModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 21;
  }

  @Override
  public int getWeaponTrailVertexComponent(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0;
  }

  @Override
  public int getShadowSize(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0xe00;
  }

  @Override
  public int getDragoonTransformDeff(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x2a;
  }

  @Override
  public int getDragoonAttackDeff(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x36;
  }

  @Override
  public int getDragoonAttackSounds(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x6e;
  }
}
