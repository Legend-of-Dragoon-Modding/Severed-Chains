package legend.game.types;

import legend.game.additions.CharacterAdditionStats;
import legend.game.inventory.Equipment;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class CharacterData2c {
  public static final int IN_PARTY = 0x1;
  /** can be put in main party (without this flag a char can only appear in secondary and can't be put into main) */
  public static final int CAN_BE_IN_PARTY = 0x2;
  /** Used by char_utils.txt. Characters who are temporarily removed from the party can be assigned this flag to be restored later. */
  public static final int TEMPORARILY_REMOVED_FROM_PARTY = 0x4;
  /** don't select, can't be taken out of main party */
  public static final int CANT_REMOVE = 0x20;
  public static final int HAS_ULTIMATE_ADDITION = 0x40;

  public int xp_00;
  /**
   * <ul>
   *   <li>0x1 - {@link #IN_PARTY}</li>
   *   <li>0x2 - {@link #CAN_BE_IN_PARTY}</li>
   *   <li>0x4 - {@link #TEMPORARILY_REMOVED_FROM_PARTY}</li>
   *   <li>0x20 - {@link #CANT_REMOVE}</li>
   *   <li>0x40 - {@link #HAS_ULTIMATE_ADDITION}</li>
   * </ul>
   */
  public int partyFlags_04;
  public int hp_08;
  public int mp_0a;
  public int sp_0c;
  public int dlevelXp_0e;
  /** i.e. poison */
  public int status_10;
  public int level_12;
  public int dlevel_13;
  public final Map<EquipmentSlot, Equipment> equipment_14 = new EnumMap<>(EquipmentSlot.class);
  public RegistryId selectedAddition_19;
  public final Map<RegistryId, CharacterAdditionStats> additionStats = new HashMap<>();
//  public final int[] additionLevels_1a = new int[8];
//  public final int[] additionXp_22 = new int[8];

  public void set(final CharacterData2c other) {
    this.xp_00 = other.xp_00;
    this.partyFlags_04 = other.partyFlags_04;
    this.hp_08 = other.hp_08;
    this.mp_0a = other.mp_0a;
    this.sp_0c = other.sp_0c;
    this.dlevelXp_0e = other.dlevelXp_0e;
    this.status_10 = other.status_10;
    this.level_12 = other.level_12;
    this.dlevel_13 = other.dlevel_13;
    this.equipment_14.clear();
    this.equipment_14.putAll(other.equipment_14);
    this.selectedAddition_19 = other.selectedAddition_19;
    this.additionStats.clear();

    for(final var entry : other.additionStats.entrySet()) {
      this.additionStats.put(entry.getKey(), new CharacterAdditionStats(entry.getValue()));
    }

//    System.arraycopy(other.additionLevels_1a, 0, this.additionLevels_1a, 0, this.additionLevels_1a.length);
//    System.arraycopy(other.additionXp_22, 0, this.additionXp_22, 0, this.additionXp_22.length);
  }
}
