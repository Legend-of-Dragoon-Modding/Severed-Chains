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
import static legend.lodmod.LodGoods.VIOLET_DRAGOON_SPIRIT;
import static legend.lodmod.LodMod.THUNDER_ELEMENT;

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

    character.addAddition(LodAdditions.DOUBLE_PUNCH.getId(), new CharacterAdditionInfo(List.of()));
    character.addAddition(LodAdditions.FERRY_OF_STYX.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(14))));
    character.addAddition(LodAdditions.SUMMON_4_GODS.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(18))));
    character.addAddition(LodAdditions.FIVE_RING_SHATTERING.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(22))));
    character.addAddition(LodAdditions.HEX_HAMMER.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(26))));
    character.addAddition(LodAdditions.OMNI_SWEEP.getId(), new CharacterAdditionInfo(List.of(new AdditionMasteryUnlockCriterion())));

    character.addSpell(LodSpells.ATOMIC_MIND.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion()))).unlock(gameState.timestamp_a0);
    character.addSpell(LodSpells.THUNDER_KID.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion(), new SpellDragoonLevelUnlockCriterion(2))));
    character.addSpell(LodSpells.THUNDER_GOD.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion(), new SpellDragoonLevelUnlockCriterion(3))));
    character.addSpell(LodSpells.VIOLET_DRAGON.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion(), new SpellDragoonLevelUnlockCriterion(5))));

    character.selectedAddition_19 = LodAdditions.DOUBLE_PUNCH.getId();

    return character;
  }

  @Override
  public void loadWorldMapModel(final CharacterData2c character, final Consumer<List<FileData>> onLoad) {
    Loader.loadFiles(onLoad, "SECT/DRGN22.BIN/836/165", "SECT/DRGN22.BIN/836/textures/5", "SECT/DRGN22.BIN/836/166", "SECT/DRGN22.BIN/836/167", "SECT/DRGN22.BIN/836/168");
  }

  @Override
  public boolean canEquip(final CharacterData2c character, final EquipmentSlot slot, final Equipment equipment) {
    return (equipment.equipableFlags_03 & 0x10) != 0;
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
    return THUNDER_ELEMENT.get();
  }

  @Override
  protected Good getDragoonSpirit() {
    return VIOLET_DRAGOON_SPIRIT.get();
  }

  @Override
  public AdditionHits80 getDragoonAddition(final CharacterData2c character) {
    return new AdditionHits80(new AdditionHitProperties10(0xc0, 0, 0, 0, 100, 0, 35, 0, 0, 0, 0, 0, 6, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, new AdditionSound(8, 15), new AdditionSound(3, 13)), new AdditionHitProperties10(0xc0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, new AdditionSound(9, 19), new AdditionSound(1, 17), new AdditionSound(7, 13), new AdditionSound(7, 15), new AdditionSound(10, 4), new AdditionSound(3, 2)), new AdditionHitProperties10(0xc0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0, new AdditionSound(10, 4), new AdditionSound(3, 2)), new AdditionHitProperties10(0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 54, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 22), new AdditionSound(1, 20)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 4), new AdditionSound(2, 2)));
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
  public Path getAttackSoundsPath(final CharacterData2c character, final PlayerBattleEntity bent) {
    return Loader.resolve(Path.of("SECT/DRGN0.BIN/1302"));
  }

  @Override
  public Path getDragoonAttackSoundsPath(final CharacterData2c character, final PlayerBattleEntity bent) {
    return Loader.resolve(Path.of("SECT/DRGN0.BIN/1311"));
  }

  @Override
  public Path getDragoonTransformSoundsPath(final CharacterData2c character, final PlayerBattleEntity bent) {
    return Loader.resolve(Path.of("SECT/DRGN0.BIN/1321"));
  }

  @Override
  public void loadDragoonAttackAnimations(final CharacterData2c character, final PlayerBattleEntity bent, final Consumer<List<FileData>> onLoad) {
    loadDrgnDir(0, 4107, onLoad);
  }

  @Override
  public boolean hasWeaponTrail(final CharacterData2c character, final PlayerBattleEntity bent) {
    return false;
  }

  @Override
  public int getWeaponTrailColour(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x88d4d8;
  }

  @Override
  public int getSpellRingColour(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x6c306c;
  }

  @Override
  public int getParticleColour(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0xd888d4;
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
    if(bent.isDragoon()) {
      return 8;
    }

    return 10;
  }

  @Override
  public int getWeaponModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 5;
  }

  @Override
  public int getWeaponTrailVertexComponent(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 2;
  }

  @Override
  public int getShadowSize(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x1600;
  }

  @Override
  public int getSpecialTransformStage(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 77;
  }

  @Override
  public int getDragoonTransformDeff(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 40;
  }

  @Override
  public int getDragoonAttackDeff(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 52;
  }

  @Override
  public int getDragoonAttackSounds(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 108;
  }
}
