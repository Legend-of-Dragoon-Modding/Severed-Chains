package legend.lodmod.additions;

import legend.game.additions.AdditionHitProperties10;
import legend.game.additions.SimpleAddition;
import legend.game.characters.CharacterAdditionInfo;
import legend.game.characters.CharacterData2c;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;

import java.util.List;
import java.util.function.Consumer;

import static legend.game.DrgnFiles.loadDrgnDir;

public class RetailAddition extends SimpleAddition {
  public final int additionFile;

  public RetailAddition(final int additionFile, final int baseDamage, final LevelMultipliers[] levelMultipliers, final AdditionHitProperties10[] hits) {
    super(baseDamage, levelMultipliers, hits);
    this.additionFile = additionFile;
  }

  @Override
  public void loadAnimations(final GameState52c state, final CharacterData2c charData, final CharacterAdditionInfo additionStats, final Consumer<List<FileData>> onLoad) {
    loadDrgnDir(0, this.additionFile, onLoad);
  }
}
