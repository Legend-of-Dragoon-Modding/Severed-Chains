package legend.game.characters;

import legend.core.memory.types.IntRef;
import legend.game.additions.AdditionHits80;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.inventory.Equipment;
import legend.game.saves.SavedCharacter;
import legend.game.textures.Image;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryEntry;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public abstract class CharacterTemplate extends RegistryEntry {
  public abstract CharacterData2c make(final GameState52c gameState);
  public void copy(final CharacterData2c from, final CharacterData2c to) {

  }

  public abstract void serialize(final CharacterData2c character, final FileData data, final IntRef offset);
  public abstract SavedCharacter deserialize(final FileData data, final IntRef offset);

  public abstract Image loadPortrait();

  public abstract void applyLevelUp(final CharacterData2c character, @Nullable final LevelUpActions actions);
  public abstract void applyDragoonLevelUp(final CharacterData2c character, @Nullable final LevelUpActions actions);
  public abstract void checkUnlocks(final CharacterData2c character, final LevelUpActions actions);
  public abstract int getXpToNextLevel(final CharacterData2c character);
  public abstract int getDxpToNextLevel(final CharacterData2c character);

  public abstract Element getElement(final CharacterData2c character);

  public Element getElement(final CharacterData2c character, final PlayerBattleEntity bent) {
    return this.getElement(character);
  }

  public abstract boolean hasDragoon(final CharacterData2c character);
  public abstract AdditionHits80 getDragoonAddition(final CharacterData2c character);

  public abstract boolean canEquip(final CharacterData2c character, final EquipmentSlot slot, final Equipment equipment);

  public abstract Path getAttackSoundsPath(final CharacterData2c character, final PlayerBattleEntity bent);
  public abstract Path getDragoonAttackSoundsPath(final CharacterData2c character, final PlayerBattleEntity bent);
  public abstract Path getDragoonTransformSoundsPath(final CharacterData2c character, final PlayerBattleEntity bent);
  public abstract Path getBattleModelPath(final CharacterData2c character, final PlayerBattleEntity bent);
  public abstract Path getBattleTexturePath(final CharacterData2c character, final PlayerBattleEntity bent);
  public abstract Path getBattleSoundsPath(final CharacterData2c character, final PlayerBattleEntity bent);
  /** Must load the following files in order: model, texture, idle animation, walking animation, running animation */
  public abstract void loadWorldMapModel(final CharacterData2c character, final Consumer<List<FileData>> onLoad);

  /** Returns the number of frames to wait before the attack starts */
  public int prepareAttack(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 5;
  }

  public boolean hasWeaponTrail(final CharacterData2c character, final PlayerBattleEntity bent) {
    return true;
  }

  public boolean isArcher(final CharacterData2c character) {
    return false;
  }

  public abstract void loadAttackAnimations(final CharacterData2c character, final PlayerBattleEntity bent, final Consumer<List<FileData>> onLoad);

  public abstract int getWeaponTrailColour(final CharacterData2c character, final PlayerBattleEntity bent);
  public abstract int getSpellRingColour(final CharacterData2c character, final PlayerBattleEntity bent);
  /** Elemental particle effects, like during quick transform */
  public abstract int getParticleColour(final CharacterData2c character, final PlayerBattleEntity bent);
  public abstract int getLeftHandModelPart(final CharacterData2c character, final PlayerBattleEntity bent);
  public abstract int getRightHandModelPart(final CharacterData2c character, final PlayerBattleEntity bent);
  public abstract int getFootModelPart(final CharacterData2c character, final PlayerBattleEntity bent);
  public abstract int getWeaponModelPart(final CharacterData2c character, final PlayerBattleEntity bent);
  public abstract int getWeaponTrailVertexComponent(final CharacterData2c character, final PlayerBattleEntity bent);
  public abstract int getShadowSize(final CharacterData2c character, final PlayerBattleEntity bent);
  public abstract int getSpecialTransformStage(final CharacterData2c character, final PlayerBattleEntity bent);
  public abstract int getDragoonTransformDeff(final CharacterData2c character, final PlayerBattleEntity bent);
  public abstract int getDragoonAttackDeff(final CharacterData2c character, final PlayerBattleEntity bent);
  public abstract int getDragoonAttackSounds(final CharacterData2c character, final PlayerBattleEntity bent);

  protected void addToStat(final CharacterData2c character, final StatType<?> statType, final int amount) {
    final Stat stat = character.stats.getStat(statType);

    if(stat instanceof final UnaryStat unary) {
      unary.setRaw(unary.getRaw() + amount);
    } else if(stat instanceof final FractionalStat frac) {
      frac.setMaxRaw(frac.getMaxRaw() + amount);
    } else {
      throw new RuntimeException("Can't add to stat " + stat.getClass().getSimpleName());
    }
  }
}
