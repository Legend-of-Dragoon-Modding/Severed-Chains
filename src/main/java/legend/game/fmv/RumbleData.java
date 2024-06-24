package legend.game.fmv;

import legend.game.unpacker.FileData;

import java.util.ArrayList;
import java.util.List;

public class RumbleData {
  public static RumbleData[] load(final FileData data) {
    final List<RumbleData> rumbleData = new ArrayList<>();

    for(int i = 0; i < data.size() / 0xc; i++) {
      if(data.readInt(i * 0xc) == -1) {
        break;
      }

      rumbleData.add(new RumbleData(data.slice(i * 0xc)));
    }

    return rumbleData.toArray(RumbleData[]::new);
  }

  public final int frame;
  public final int initialIntensity;
  public final int endingIntensity;
  public final int duration;

  public RumbleData(final FileData data) {
    this.frame = data.readInt(0x0);
    this.initialIntensity = data.readUShort(0x4);
    this.endingIntensity = data.readUShort(0x6);
    this.duration = data.readInt(0x8);
  }
}
