package legend.game.additions;

import legend.game.types.CharacterData2c;
import legend.game.unpacker.FileData;

import java.util.List;
import java.util.function.Consumer;

import static legend.game.DrgnFiles.loadDrgnDir;
import static legend.game.types.CharacterData2c.HAS_ULTIMATE_ADDITION;

public class MasterAddition extends SimpleAddition {
  public final int additionFile;

  public MasterAddition(final int additionFile, final int baseDamage, final LevelMultipliers[] levelMultipliers, final AdditionHitProperties10[] hits) {
    super(baseDamage, levelMultipliers, hits);
    this.additionFile = additionFile;
  }

  @Override
  public boolean isUnlocked(final CharacterData2c charData, final CharacterAdditionStats additionStats) {
    return (charData.partyFlags_04 & HAS_ULTIMATE_ADDITION) != 0;
  }

  @Override
  public void loadAnimations(final CharacterData2c charData, final CharacterAdditionStats additionStats, final Consumer<List<FileData>> onLoad) {
    loadDrgnDir(0, this.additionFile, onLoad);
  }
}
