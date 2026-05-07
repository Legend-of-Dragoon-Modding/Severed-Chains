package legend.game.additions;

import legend.game.characters.AdditionMasteryUnlockCriterion;
import legend.game.characters.CharacterAdditionInfo;
import legend.game.characters.CharacterData2c;
import legend.game.i18n.I18n;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryEntry;

import java.util.List;
import java.util.function.Consumer;

public abstract class Addition extends RegistryEntry {
  public String getName() {
    return I18n.translate(this);
  }

  public abstract int getDamage(final CharacterData2c character, final CharacterAdditionInfo additionInfo);
  public abstract int getSp(final CharacterData2c character, final CharacterAdditionInfo additionInfo);
  public abstract float getDamageMultiplier(final CharacterData2c character, final CharacterAdditionInfo additionInfo);
  public abstract float getSpMultiplier(final CharacterData2c character, final CharacterAdditionInfo additionInfo);
  public abstract int getXpToNextLevel(final CharacterData2c character, final CharacterAdditionInfo additionInfo);
  public abstract int getMaxLevel(final CharacterData2c character, final CharacterAdditionInfo additionInfo);
  /** When every addition that {@link #countsTowardsMastery} returns true, unlocks {@link AdditionMasteryUnlockCriterion}s */
  public abstract boolean isComplete(final CharacterData2c character, final CharacterAdditionInfo additionInfo);
  /** If true, this addition must be unlocked before {@link AdditionMasteryUnlockCriterion}s will unlock */
  public abstract boolean countsTowardsMastery(final CharacterData2c character, final CharacterAdditionInfo additionInfo);

  public abstract int getHitCount(final CharacterData2c character, final CharacterAdditionInfo additionInfo);
  public abstract AdditionHitProperties10 getHit(final CharacterData2c character, final CharacterAdditionInfo additionInfo, final int index);

  public abstract void loadAnimations(final CharacterData2c character, final CharacterAdditionInfo additionInfo, Consumer<List<FileData>> onLoad);
}
