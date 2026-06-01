package legend.game.types;

import legend.core.memory.Method;
import legend.core.memory.types.IntRef;
import legend.game.submap.SubmapCutInfo;
import legend.game.unpacker.FileData;

import java.util.Arrays;

import static legend.game.DrgnFiles.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

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

  @Method(0x800e6504L)
  public void getDrgnFile(final int submapCut, final IntRef drgnIndexOut, final IntRef fileIndexOut) {
    final SubmapCutInfo entry = this.submapCutInfo_0000[submapCut];

    final int drgnIndex1 = entry.earlyGameFile_00 >>> 13;
    final int drgnIndex2 = entry.lateGameFile_02 >>> 13;

    // Once you reach a certain chapter, some maps will load from a different disk (like Fletz with the docks in chapter 4)
    final boolean useLateGameMap;
    if(drgnIndex1 == drgnBinIndex_800bc058 - 1 || drgnIndex2 > gameState_800babc8.chapterIndex_98) {
      drgnIndexOut.set(drgnIndex1);
      useLateGameMap = false;
    } else {
      drgnIndexOut.set(drgnIndex2);
      useLateGameMap = true;
    }

    final int t0 = drgnIndexOut.get() >= 0 && drgnIndexOut.get() <= 3 ? 4 : 0;

    drgnIndexOut.incr();
    fileIndexOut.set(((useLateGameMap ? entry.lateGameFile_02 : entry.earlyGameFile_00) & 0x1fff) * 3 + t0);
  }
}
