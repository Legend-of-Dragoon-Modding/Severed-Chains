package legend.game.types;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import legend.core.GameEngine;
import legend.game.inventory.Equipment;
import legend.game.inventory.Item;
import legend.lodmod.LodMod;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameState52c {
  public String campaignName;

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
   *   <li>13 flag 0x4_0000 - has psych bomb X</li>
   *   <li>29 flag 0x800_0000 - died in arena fight</li>
   * </ul>
   */
  public final Flags scriptFlags2_bc = new Flags(32);
  public final Flags scriptFlags1_13c = new Flags(8);
  public final Flags wmapFlags_15c = new Flags(8);
  public final Flags visitedLocations_17c = new Flags(8);
  public final int[] goods_19c = new int[2];
  /** Not sure if this is actually 8 elements long, has at least 3. Related to submap music. */
  public final int[] _1a4 = new int[8];
  /** Note: I'm not _100%_ sure this is only chest flags */
  public final int[] chestFlags_1c4 = new int[8];
//  public final ShortRef equipmentCount_1e4;
//  public final ShortRef itemCount_1e6;
  public final List<Equipment> equipment_1e8 = new ArrayList<>();
  public final List<Item> items_2e9 = new ArrayList<>();

  /** Only used during loading */
  public final IntList equipmentIds_1e8 = new IntArrayList();
  /** Only used during loading */
  public final List<RegistryId> equipmentRegistryIds_1e8 = new ArrayList<>();
  /** Only used during loading */
  public final IntList itemIds_2e9 = new IntArrayList();
  /** Only used during loading */
  public final List<RegistryId> itemRegistryIds_2e9 = new ArrayList<>();

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
  public boolean isOnWorldMap_4e4;

  /** A bitset used to set each char's MP to max the first time each one is loaded */
  public int characterInitialized_4e6;

  public GameState52c() {
    Arrays.setAll(this.charData_32c, i -> new CharacterData2c());
  }

  public void syncIds() {
    this.equipment_1e8.clear();
    this.items_2e9.clear();

    for(final int id : this.equipmentIds_1e8) {
      this.equipment_1e8.add(GameEngine.REGISTRIES.equipment.getEntry(LodMod.equipmentIdMap.get(id)).get());
    }

    for(final RegistryId id : this.equipmentRegistryIds_1e8) {
      this.equipment_1e8.add(GameEngine.REGISTRIES.equipment.getEntry(id).get());
    }

    for(final CharacterData2c charData : this.charData_32c) {
      charData.equipment_14.clear();

      for(final EquipmentSlot slot : EquipmentSlot.values()) {
        if(charData.equipmentIds_14.containsKey(slot)) {
          charData.equipment_14.put(slot, GameEngine.REGISTRIES.equipment.getEntry(LodMod.equipmentIdMap.get(charData.equipmentIds_14.getInt(slot))).get());
        }

        if(charData.equipmentRegistryIds_14.containsKey(slot)) {
          charData.equipment_14.put(slot, GameEngine.REGISTRIES.equipment.getEntry(charData.equipmentRegistryIds_14.get(slot)).get());
        }
      }
    }

    for(final int id : this.itemIds_2e9) {
      this.items_2e9.add(GameEngine.REGISTRIES.items.getEntry(LodMod.itemIdMap.get(id - 192)).get());
    }

    for(final RegistryId id : this.itemRegistryIds_2e9) {
      this.items_2e9.add(GameEngine.REGISTRIES.items.getEntry(id).get());
    }
  }
}
