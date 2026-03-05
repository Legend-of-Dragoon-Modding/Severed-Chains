package legend.game.characters;

import legend.core.memory.types.IntRef;
import legend.game.additions.AdditionHits80;
import legend.game.textures.Image;
import legend.game.inventory.Equipment;
import legend.game.saves.SavedCharacter;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryEntry;

import javax.annotation.Nullable;

public abstract class CharacterTemplate extends RegistryEntry {
  public abstract CharacterData2c make(final GameState52c gameState);

  public abstract void serialize(final CharacterData2c character, final FileData data, final IntRef offset);
  public abstract SavedCharacter deserialize(final FileData data, final IntRef offset);

  public abstract Image loadPortrait();

  public abstract void applyLevelUp(final GameState52c gameState, final CharacterData2c character, @Nullable final LevelUpActions actions);
  public abstract void applyDragoonLevelUp(final GameState52c gameState, final CharacterData2c character, @Nullable final LevelUpActions actions);
  public abstract void checkUnlocks(final CharacterData2c character, final LevelUpActions actions);
  public abstract int getXpToNextLevel(final GameState52c gameState, final CharacterData2c character);
  public abstract int getDxpToNextLevel(final GameState52c gameState, final CharacterData2c character);

  public abstract Element getElement(final GameState52c gameState, final CharacterData2c character);
  public abstract boolean hasDragoon(final GameState52c gameState, final CharacterData2c character);
  public abstract AdditionHits80 getDragoonAddition(final GameState52c gameState, final CharacterData2c character);

  public abstract boolean canEquip(final GameState52c gameState, final CharacterData2c character, final EquipmentSlot slot, final Equipment equipment);

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
