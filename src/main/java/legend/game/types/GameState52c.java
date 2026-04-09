package legend.game.types;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import legend.game.inventory.Equipment;
import legend.game.inventory.GoodsInventory;
import legend.game.inventory.Inventory;
import legend.game.saves.Campaign;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameState52c {
  private static final Logger LOGGER = LogManager.getFormatterLogger(GameState52c.class);

  public Campaign campaign;

  /**
   * <ul>
   *   <li>18 - stardust turned in</li>
   *   <li>20 - Zy: changed when I did the battles in the marshland. I believe this reset when I went back to get the stardust</li>
   *   <li>21 - Zy: changes when you are on the boat in the marshland</li>
   *   <li>27 - hero tickets - Zy: used as byte, displays as short, actually is an int, blocks if over 99, typical LOD code</li>
   * </ul>
   */
  public final int[] scriptData_08 = new int[0x20];
  public final IntList charIds_88 = new IntArrayList();
  public int gold_94;
  public int chapterIndex_98;
  public int stardust_9c;
  public int timestamp_a0;
  public int submapScene_a4;
  public int submapCut_a8;

  /** Used by the script engine */
  public int _b0;
  /** The total number of battles */
  public int battleCount_b4;
  /** The total number of player turns taken */
  public int turnCount_b8;
  /**
   * <ul>
   *   <li>13 flag 0x4_0000 - has psych bomb X</li>
   *   <li>29 flag 0x800_0000 - died in arena fight</li>
   * </ul>
   */
  public final Flags scriptFlags2_bc = new Flags(32);
  public final Flags scriptFlags1_13c = new Flags(8);
  public final Flags wmapFlags_15c = new Flags(8);
  public final Flags visitedLocations_17c = new Flags(8);
  public final GoodsInventory goods_19c = new GoodsInventory();
  /** Not sure if this is actually 8 elements long, has at least 3. Related to submap music. */
  public final int[] _1a4 = new int[8];
  /** Note: I'm not _100%_ sure this is only chest flags */
  public final int[] chestFlags_1c4 = new int[8];
//  public final ShortRef equipmentCount_1e4;
//  public final ShortRef itemCount_1e6;
  public final List<Equipment> equipment_1e8 = new ArrayList<>();
  public final Inventory items_2e9 = new Inventory();
  public final CharacterData2c[] charData_32c = new CharacterData2c[9];
//  public final int[] _4b8 = new int[8];

  // World map stuff
  public int pathIndex_4d8;
  public int dotIndex_4da;
  public float dotOffset_4dc;
  public int facing_4dd;
  public int directionalPathIndex_4de;

  // Config stuff
//  public boolean vibrationEnabled_4e1;
  public boolean indicatorsDisabled_4e3;

  /** A bitset used to set each char's MP to max the first time each one is loaded */
  public int characterInitialized_4e6;

  public GameState52c() {
    Arrays.setAll(this.charData_32c, i -> new CharacterData2c());
  }

  public CharacterData2c getCharBySlot(final int slot) {
    return this.charData_32c[this.charIds_88.getInt(slot)];
  }
}
