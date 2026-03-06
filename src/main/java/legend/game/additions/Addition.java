package legend.game.additions;

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

  public abstract int getDamage(final CharacterData2c charData, final CharacterAdditionInfo additionStats);
  public abstract int getSp(final CharacterData2c charData, final CharacterAdditionInfo additionStats);
  public abstract float getDamageMultiplier(final CharacterData2c charData, final CharacterAdditionInfo additionStats);
  public abstract float getSpMultiplier(final CharacterData2c charData, final CharacterAdditionInfo additionStats);

  public abstract int getHitCount(final CharacterData2c charData, final CharacterAdditionInfo additionStats);
  public abstract AdditionHitProperties10 getHit(final CharacterData2c charData, final CharacterAdditionInfo additionStats, final int index);

  public abstract void loadAnimations(final CharacterData2c charData, final CharacterAdditionInfo additionStats, Consumer<List<FileData>> onLoad);
}
