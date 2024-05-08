package legend.game.wmap;

import legend.game.models.CContainer;
import legend.game.models.TmdAnimationFile;

public class PlayerModelTmdFileData {
  public CContainer extendedTmd_00;
//  public long unknownFile_04; // This was a TOD, but unused by the game. One for each wmap model, files 5714.1, 5715.1, 5716.1, 5717.1
  public final TmdAnimationFile[] tmdAnim_08 = new TmdAnimationFile[14];
}
