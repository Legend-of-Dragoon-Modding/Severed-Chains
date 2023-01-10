package legend.game.combat.types;

import legend.game.types.GsF_LIGHT;

public class BttlLightStruct84 {
  public final GsF_LIGHT light_00 = new GsF_LIGHT();
  public final BttlLightStruct84Sub38 _10 = new BttlLightStruct84Sub38();
  public int scriptIndex_48;
  public final BttlLightStruct84Sub38 _4c = new BttlLightStruct84Sub38();

  public void clear() {
    this.light_00.clear();
    this._10.clear();
    this.scriptIndex_48 = 0;
    this._4c.clear();
  }
}
