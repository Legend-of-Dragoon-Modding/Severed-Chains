package legend.lodmod.characters;

import legend.game.characters.CharacterData2c;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;

import java.util.List;
import java.util.function.Consumer;

public class MirandaTemplate extends ShanaTemplate {
  @Override
  public void loadWorldMapModel(final CharacterData2c character, final Consumer<List<FileData>> onLoad) {
    Loader.loadFiles(onLoad, "SECT/DRGN22.BIN/836/231", "SECT/DRGN22.BIN/836/textures/7", "SECT/DRGN22.BIN/836/232", "SECT/DRGN22.BIN/836/233", "SECT/DRGN22.BIN/836/234");
  }

  @Override
  public int getLeftHandModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 10;
  }

  @Override
  public int getRightHandModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 11;
  }

  @Override
  public int getFootModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    if(bent.isDragoon()) {
      return 11;
    }

    return 12;
  }

  @Override
  public int getShadowSize(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x1300;
  }

  @Override
  public int getDragoonTransformDeff(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x40;
  }

  @Override
  public int getDragoonAttackSounds(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x70;
  }
}
