package legend.game.combat.types;

import legend.core.IoHelper;
import legend.core.gte.USCOLOUR;
import legend.game.scripting.Param;

import java.nio.ByteBuffer;

public class StageAmbiance4c {
  public final USCOLOUR ambientColour_00 = new USCOLOUR();
  public final USCOLOUR _06 = new USCOLOUR();
  public int _0c;
  public int _0e;
  public final BattleStruct14[] _10 = {new BattleStruct14(), new BattleStruct14(), new BattleStruct14()};

  public StageAmbiance4c set(final ByteBuffer buffer) {
    IoHelper.readColour(buffer, this.ambientColour_00);
    IoHelper.readColour(buffer, this._06);
    this._0c = IoHelper.readShort(buffer);
    this._0e = IoHelper.readShort(buffer);

    for(final BattleStruct14 struct : this._10) {
      IoHelper.readSvec3(buffer, struct.lightDirection_00);
      struct._06 = IoHelper.readShort(buffer);
      struct._08 = IoHelper.readShort(buffer);
      struct.lightColour_0a.r.set(IoHelper.readUByte(buffer));
      struct.lightColour_0a.g.set(IoHelper.readUByte(buffer));
      struct.lightColour_0a.b.set(IoHelper.readUByte(buffer));
      struct._0d.r.set(IoHelper.readUByte(buffer));
      struct._0d.g.set(IoHelper.readUByte(buffer));
      struct._0d.b.set(IoHelper.readUByte(buffer));
      struct._10 = IoHelper.readShort(buffer);
      struct._12 = IoHelper.readShort(buffer);
    }

    return this;
  }

  public StageAmbiance4c set(final Param param) {
    final int[] vals = new int[0x4c / 4];
    for(int i = 0; i < vals.length; i++) {
      vals[i] = param.array(i).get();
    }

    this.ambientColour_00.setX(vals[0] & 0xffff);
    this.ambientColour_00.setY(vals[0] >>> 16 & 0xffff);
    this.ambientColour_00.setZ(vals[1] & 0xffff);
    this._06.setX(vals[1] >>> 16 & 0xffff);
    this._06.setY(vals[2] & 0xffff);
    this._06.setZ(vals[2] >>> 16 & 0xffff);
    this._0c = (short)vals[3];
    this._0e = (short)(vals[3] >> 16);

    int paramIndex = 0;
    for(final BattleStruct14 struct : this._10) {
      struct.lightDirection_00.setX((short)vals[paramIndex]);
      struct.lightDirection_00.setY((short)(vals[paramIndex] >> 16));
      struct.lightDirection_00.setZ((short)vals[paramIndex + 1]);
      struct._06 = (short)(vals[paramIndex + 1] >> 16);
      struct._08 = (short)vals[paramIndex + 2];
      struct.lightColour_0a.r.set(vals[paramIndex + 2] >>> 16 & 0xff);
      struct.lightColour_0a.g.set(vals[paramIndex + 2] >>> 24 & 0xff);
      struct.lightColour_0a.b.set(vals[paramIndex + 3] & 0xff);
      struct._0d.r.set(vals[paramIndex + 3] >>  8 & 0xff);
      struct._0d.g.set(vals[paramIndex + 3] >> 16 & 0xff);
      struct._0d.b.set(vals[paramIndex + 3] >> 24 & 0xff);
      struct._10 = (short)vals[paramIndex + 4];
      struct._12 = (short)(vals[paramIndex + 4] >> 16);
      paramIndex += 5;
    }

    return this;
  }
}
