package legend.game.additions;

import legend.game.types.CharacterData2c;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryEntry;

import java.util.List;
import java.util.function.Consumer;

public abstract class Addition extends RegistryEntry {
  public abstract boolean isUnlocked(final GameState52c state, final CharacterData2c charData, final CharacterAdditionStats additionStats);
  public abstract int getDamage(final GameState52c state, final CharacterData2c charData, final CharacterAdditionStats additionStats);
  public abstract int getSp(final GameState52c state, final CharacterData2c charData, final CharacterAdditionStats additionStats);
  public abstract float getDamageMultiplier(final GameState52c state, final CharacterData2c charData, final CharacterAdditionStats additionStats);
  public abstract float getSpMultiplier(final GameState52c state, final CharacterData2c charData, final CharacterAdditionStats additionStats);

  public abstract int getHitCount(final GameState52c state, final CharacterData2c charData, final CharacterAdditionStats additionStats);
  public abstract AdditionHitProperties10 getHit(final GameState52c state, final CharacterData2c charData, final CharacterAdditionStats additionStats, final int index);

  public abstract void loadAnimations(final GameState52c state, final CharacterData2c charData, final CharacterAdditionStats additionStats, Consumer<List<FileData>> onLoad);
}
