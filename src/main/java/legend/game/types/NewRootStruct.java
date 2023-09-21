package legend.game.types;

import legend.game.submap.SubmapCutInfo;
import legend.game.unpacker.FileData;

import java.util.Arrays;

public class NewRootStruct {
  public final SubmapCutInfo[] submapCutInfo_0000 = new SubmapCutInfo[0x400];
  /**
   * A bunch of packed values, related to map transitions/collision
   * <ul>
   *   <li>0 (0x0): ?</li>
   *   <li>1 (0x2): ?</li>
   *   <li>2 (0x4): ?</li>
   *   <li>3 (0x8): ?</li>
   *   <li>4 (0x10): Has map transition (see bits 16-21, 22-32)</li>
   *   <li>5 (0x20): Might disable a kind of collision</li>
   *   <li>6 (0x40): ?</li>
   *   <li>7 (0x80): ?</li>
   *   <li>8-15: Used as index into arr_800cb460 (divided by 4)</li>
   *   <li>16-21: Map transition scene</li>
   *   <li>22-32: Map transition cut</li>
   * </ul>
   */
  public final int[] collisionAndTransitions_2000 = new int[0x63c];

  public NewRootStruct(final FileData data) {
    Arrays.setAll(this.submapCutInfo_0000, i -> new SubmapCutInfo(data.slice(i * 0x8, 0x8)));
    Arrays.setAll(this.collisionAndTransitions_2000, i -> data.readInt(0x2000 + i * 0x4));
  }
}
