package legend.game.types;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.Arrays;

public class GameState52c {
  /** Maybe flags? Maybe individual bytes? */
  public int _04;
  /**
   * <ul>
   *   <li>18 - stardust turned in</li>
   *   <li>20 - Zy: changed when I did the battles in the marshland. I believe this reset when I went back to get the stardust</li>
   *   <li>21 - Zy: changes when you are on the boat in the marshland</li>
   *   <li>27 - hero tickets - Zy: used as byte, displays as short, actually is an int, blocks if over 99, typical LOD code</li>
   * </ul>
   */
  public final int[] scriptData_08 = new int[0x20];
  public final int[] charIds_88 = new int[3];
  public int gold_94;
  public int chapterIndex_98;
  public int stardust_9c;
  public int timestamp_a0;
  /** Not 100% sure on this */
  public int submapScene_a4;
  public int submapCut_a8;

  /** Used by the script engine */
  public int _b0;
  public int _b4;
  public int _b8;
  /**
   * <ul>
   *   <li>0x40000 - has psych bomb X</li>
   * </ul>
   */
  public final int[] scriptFlags2_bc = new int[0x20];
  public final int[] scriptFlags1_13c = new int[8];
  public final int[] _15c = new int[8];
  public final int[] _17c = new int[8];
  public final int[] goods_19c = new int[2];
  /** Not sure if this is actually 8 elements long, has at least 3. Related to submap music. */
  public final int[] _1a4 = new int[8];
  /** Note: I'm not _100%_ sure this is only chest flags */
  public final int[] chestFlags_1c4 = new int[8];
//  public final ShortRef equipmentCount_1e4;
//  public final ShortRef itemCount_1e6;
  public final IntList equipment_1e8 = new IntArrayList();
  public final IntList items_2e9 = new IntArrayList();

  public final CharacterData2c[] charData_32c = new CharacterData2c[9];
  public final int[] _4b8 = new int[8];

  // World map stuff
  public int pathIndex_4d8;
  public int dotIndex_4da;
  public int dotOffset_4dc;
  public int facing_4dd;
  public int areaIndex_4de;

  // Config stuff
  public boolean mono_4e0;
  public boolean vibrationEnabled_4e1;
  public int morphMode_4e2;
  public boolean indicatorsDisabled_4e3;
  public boolean isOnWorldMap_4e4;

  /** A bitset used to set each char's MP to max the first time each one is loaded */
  public int characterInitialized_4e6;
  /** Controls how the indicators (triangles) are drawn (called "Note" in options menu) */
  public int indicatorMode_4e8;

  public GameState52c() {
    Arrays.setAll(this.charData_32c, i -> new CharacterData2c());
  }
}
