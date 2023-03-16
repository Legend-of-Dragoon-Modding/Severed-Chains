package legend.game.combat.types;

import legend.game.combat.deff.Anim;
import legend.game.combat.deff.DeffPart;
import legend.game.types.CContainer;
import legend.game.types.Model124;

public class BttlScriptData6cSub13c implements BttlScriptData6cSubBase1 {
  public int _00;
  /** Can be LMB or CMB subtype */
  public DeffPart.TmdType tmdType_04;
  public CContainer extTmd_08;
  public Anim anim_0c;
  public final Model124 model_10;
  public Model124 model_134;

  public BttlScriptData6cSub13c(final String name) {
    this.model_10 = new Model124(name);
  }
}
