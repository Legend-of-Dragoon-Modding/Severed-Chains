package legend.game.combat.types;

import legend.core.IoHelper;
import legend.core.gte.SVECTOR;
import legend.game.scripting.Param;

import java.nio.ByteBuffer;

public class BattleStruct4c {
  public final SVECTOR ambientColour_00 = new SVECTOR();
  public int _06;
  public int _08;
  public int _0a;
  public int _0c;
  public int _0e;
  public final BattleStruct14[] _10 = {new BattleStruct14(), new BattleStruct14(), new BattleStruct14()};

  public BattleStruct4c set(final ByteBuffer buffer) {
    IoHelper.readSvec(buffer, this.ambientColour_00);
    this._06 = IoHelper.readShort(buffer);
    this._08 = IoHelper.readShort(buffer);
    this._0a = IoHelper.readShort(buffer);
    this._0c = IoHelper.readShort(buffer);
    this._0e = IoHelper.readShort(buffer);

    for(final BattleStruct14 struct : this._10) {
      IoHelper.readSvec(buffer, struct.lightDirection_00);
      struct._06 = IoHelper.readShort(buffer);
      struct._08 = IoHelper.readShort(buffer);
      struct.lightColour_0a.r.set(IoHelper.readByte(buffer) & 0xff);
      struct.lightColour_0a.g.set(IoHelper.readByte(buffer) & 0xff);
      struct.lightColour_0a.b.set(IoHelper.readByte(buffer) & 0xff);
      struct._0d.r.set(IoHelper.readByte(buffer) & 0xff);
      struct._0d.g.set(IoHelper.readByte(buffer) & 0xff);
      struct._0d.b.set(IoHelper.readByte(buffer) & 0xff);
      struct._10 = IoHelper.readShort(buffer);
      struct._12 = IoHelper.readShort(buffer);
    }

    return this;
  }

  public BattleStruct4c set(final Param param) {
    final int[] vals = new int[0x4c / 4];
    for(int i = 0; i < vals.length; i++) {
      vals[i] = param.array(i).get();
    }

    this.ambientColour_00.setX((short)vals[0]);
    this.ambientColour_00.setY((short)(vals[0] >> 16));
    this.ambientColour_00.setZ((short)vals[1]);
    this._06 = (short)(vals[1] >> 16);
    this._08 = (short)vals[2];
    this._0a = (short)(vals[2] >> 16);
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
