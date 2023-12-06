package legend.game.combat.environment;

import legend.game.scripting.Param;
import org.joml.Vector3f;

import java.util.Arrays;

public class StageAmbiance4c {
  public final Vector3f ambientColour_00;
  public final Vector3f _06;
  public int _0c;
  public int _0e;
  public final BattleStruct14[] _10;

  public StageAmbiance4c(final Vector3f ambientColour, final Vector3f _06, final int _0c, final int _0e, final BattleStruct14... _10) {
    this.ambientColour_00 = ambientColour;
    this._06 = _06;
    this._0c = _0c;
    this._0e = _0e;
    this._10 = _10;
  }

  public StageAmbiance4c() {
    this.ambientColour_00 = new Vector3f();
    this._06 = new Vector3f();
    this._10 = new BattleStruct14[3];
    Arrays.setAll(this._10, i -> new BattleStruct14());
  }

  public StageAmbiance4c set(final StageAmbiance4c other) {
    this.ambientColour_00.set(other.ambientColour_00);
    this._06.set(other._06);
    this._0c = other._0c;
    this._0e = other._0e;

    for(int i = 0; i < this._10.length; i++) {
      final BattleStruct14 struct = this._10[i];
      final BattleStruct14 otherStruct = other._10[i];
      struct.lightDirection_00.set(otherStruct.lightDirection_00);
      struct.x_06 = otherStruct.x_06;
      struct.y_08 = otherStruct.y_08;
      struct.lightColour_0a.set(otherStruct.lightColour_0a);
      struct._0d.set(otherStruct._0d);
      struct.x_10 = otherStruct.x_10;
      struct.y_12 = otherStruct.y_12;
    }

    return this;
  }

  public StageAmbiance4c set(final Param param) {
    final int[] vals = new int[0x4c / 4];
    for(int i = 0; i < vals.length; i++) {
      vals[i] = param.array(i).get();
    }

    this.ambientColour_00.x = (vals[0] & 0xffff) / 4096.0f;
    this.ambientColour_00.y = (vals[0] >>> 16 & 0xffff) / 4096.0f;
    this.ambientColour_00.z = (vals[1] & 0xffff) / 4096.0f;
    this._06.x = (vals[1] >>> 16 & 0xffff) / 4096.0f;
    this._06.y = (vals[2] & 0xffff) / 4096.0f;
    this._06.z = (vals[2] >>> 16 & 0xffff) / 4096.0f;
    this._0c = (short)vals[3];
    this._0e = (short)(vals[3] >> 16);

    int paramIndex = 4;
    for(final BattleStruct14 struct : this._10) {
      struct.lightDirection_00.x = (short)vals[paramIndex] / (float)0x1000;
      struct.lightDirection_00.y = (short)(vals[paramIndex] >> 16) / (float)0x1000;
      struct.lightDirection_00.z = (short)vals[paramIndex + 1] / (float)0x1000;
      struct.x_06 = (short)(vals[paramIndex + 1] >> 16) / (float)0x1000;
      struct.y_08 = (short)vals[paramIndex + 2] / (float)0x1000;
      struct.lightColour_0a.x = vals[paramIndex + 2] >>> 16 & 0xff;
      struct.lightColour_0a.y = vals[paramIndex + 2] >>> 24 & 0xff;
      struct.lightColour_0a.z = vals[paramIndex + 3] & 0xff;
      struct._0d.x = vals[paramIndex + 3] >>  8 & 0xff;
      struct._0d.y = vals[paramIndex + 3] >> 16 & 0xff;
      struct._0d.z = vals[paramIndex + 3] >> 24 & 0xff;
      struct.x_10 = (short)vals[paramIndex + 4] / (float)0x1000;
      struct.y_12 = (short)(vals[paramIndex + 4] >> 16) / (float)0x1000;
      paramIndex += 5;
    }

    return this;
  }
}
