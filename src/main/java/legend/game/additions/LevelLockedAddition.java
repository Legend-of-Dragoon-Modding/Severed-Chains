package legend.game.additions;

import legend.game.types.CharacterData2c;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;

import java.util.List;
import java.util.function.Consumer;

import static legend.game.DrgnFiles.loadDrgnDir;

public class LevelLockedAddition extends SimpleAddition {
  public final int additionFile;
  public final int unlockLevel;

  public LevelLockedAddition(final int additionFile, final int unlockLevel, final int baseDamage, final LevelMultipliers[] levelMultipliers, final AdditionHitProperties10[] hits) {
    super(baseDamage, levelMultipliers, hits);
    this.additionFile = additionFile;
    this.unlockLevel = unlockLevel;
  }

  @Override
  public boolean isUnlocked(final GameState52c state, final CharacterData2c charData, final CharacterAdditionStats additionStats) {
    return charData.level_12 >= this.unlockLevel;
  }

  @Override
  public void loadAnimations(final GameState52c state, final CharacterData2c charData, final CharacterAdditionStats additionStats, final Consumer<List<FileData>> onLoad) {
    loadDrgnDir(0, this.additionFile, onLoad);
  }
}
