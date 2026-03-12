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

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

import static legend.game.DrgnFiles.loadDrgnDir;
import static legend.lodmod.LodGoods.JADE_DRAGOON_SPIRIT;
import static legend.lodmod.LodMod.WIND_ELEMENT;

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

    character.addAddition(LodAdditions.HARPOON.getId(), new CharacterAdditionInfo(List.of()));
    character.addAddition(LodAdditions.SPINNING_CANE.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(5))));
    character.addAddition(LodAdditions.ROD_TYPHOON.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(7))));
    character.addAddition(LodAdditions.GUST_OF_WIND_DANCE.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(11))));
    character.addAddition(LodAdditions.FLOWER_STORM.getId(), new CharacterAdditionInfo(List.of(new AdditionMasteryUnlockCriterion())));

    character.addSpell(LodSpells.WING_BLASTER.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion()))).unlock(gameState.timestamp_a0);
    character.addSpell(LodSpells.BLOSSOM_STORM.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion(), new SpellDragoonLevelUnlockCriterion(2))));
    character.addSpell(LodSpells.GASPLESS.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion(), new SpellDragoonLevelUnlockCriterion(3))));
    character.addSpell(LodSpells.JADE_DRAGON.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion(), new SpellDragoonLevelUnlockCriterion(5))));

    character.selectedAddition_19 = LodAdditions.HARPOON.getId();

    return character;
  }

  @Override
  public void loadWorldMapModel(final CharacterData2c character, final Consumer<List<FileData>> onLoad) {
    Loader.loadFiles(onLoad, "SECT/DRGN22.BIN/290/33", "SECT/DRGN22.BIN/290/textures/1", "SECT/DRGN22.BIN/290/34", "SECT/DRGN22.BIN/290/35", "SECT/DRGN22.BIN/290/36");
  }

  @Override
  public boolean canEquip(final CharacterData2c character, final EquipmentSlot slot, final Equipment equipment) {
    return (equipment.equipableFlags_03 & 0x40) != 0;
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
    return WIND_ELEMENT.get();
  }

  @Override
  protected Good getDragoonSpirit() {
    return JADE_DRAGOON_SPIRIT.get();
  }

  @Override
  public AdditionHits80 getDragoonAddition(final CharacterData2c character) {
    return new AdditionHits80(new AdditionHitProperties10(0xc0, 2, 0, 0, 100, 0, 13, 0, 0, 0, 1, 7, 10, 32, 18, 11, new AdditionSound(5, 5), new AdditionSound(0, 3)), new AdditionHitProperties10(0xc0, 14, 5, 0, 10, 0, 0, 0, 0, 0, 0, 0, 7, 32, 0, 0, new AdditionSound(4, 11), new AdditionSound(2, 9)), new AdditionHitProperties10(0xc0, 32, 25, 0, 20, 0, 0, 0, 0, 4, 21, 2, 7, 32, 0, 0, new AdditionSound(5, 5), new AdditionSound(0, 3)), new AdditionHitProperties10(0xc0, 10, 1, 0, 30, 0, 0, 0, 0, 1, 8, 29, 7, 32, 0, 0, new AdditionSound(7, 9), new AdditionSound(1, 7)), new AdditionHitProperties10(0xc0, 11, 10, 0, 40, 0, 0, 0, 0, 0, 10, 29, 5, 32, 19, 0, new AdditionSound(10, 8), new AdditionSound(3, 6)), new AdditionHitProperties10(0xc0, 61, 29, 0, 0, 0, 0, 0, 0, 0, 60, 5, 7, 32, 0, 0, new AdditionSound(5, 5), new AdditionSound(0, 3)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 12), new AdditionSound(1, 10)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 16), new AdditionSound(2, 14)));
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
  public Path getAttackSoundsPath(final CharacterData2c character, final PlayerBattleEntity bent) {
    return Loader.resolve(Path.of("SECT/DRGN0.BIN/1299"));
  }

  @Override
  public Path getDragoonAttackSoundsPath(final CharacterData2c character, final PlayerBattleEntity bent) {
    return Loader.resolve(Path.of("SECT/DRGN0.BIN/1308"));
  }

  @Override
  public Path getDragoonTransformSoundsPath(final CharacterData2c character, final PlayerBattleEntity bent) {
    return Loader.resolve(Path.of("SECT/DRGN0.BIN/1318"));
  }

  @Override
  public void loadDragoonAttackAnimations(final CharacterData2c character, final PlayerBattleEntity bent, final Consumer<List<FileData>> onLoad) {
    loadDrgnDir(0, 4104, onLoad);
  }

  @Override
  public int getWeaponTrailColour(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x20a878;
  }

  @Override
  public int getSpellRingColour(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x297231;
  }

  @Override
  public int getParticleColour(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x55c883;
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
    return 9;
  }

  @Override
  public int getWeaponModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 3;
  }

  @Override
  public int getWeaponTrailVertexComponent(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 2;
  }

  @Override
  public int getShadowSize(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x1800;
  }

  @Override
  public int getDragoonTransformDeff(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 34;
  }

  @Override
  public int getDragoonAttackDeff(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 49;
  }

  @Override
  public int getDragoonAttackSounds(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 105;
  }
}
