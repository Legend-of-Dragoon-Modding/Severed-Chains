package legend.game.types;

import legend.game.additions.CharacterAdditionStats;
import legend.game.characters.CharacterTemplate;
import legend.game.characters.StatCollection;
import legend.game.i18n.I18n;
import legend.game.inventory.Equipment;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
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

  private final GameState52c gameState;
  public final CharacterTemplate template;

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
//  public int hp_08;
//  public int mp_0a;
//  public int sp_0c;
  public int dlevelXp_0e;
  /**
   * <ul>
   *   <li>0x1 - Petrified</li>
   *   <li>0x2 - Bewitched</li>
   *   <li>0x4 - Confused</li>
   *   <li>0x8 - Fearful</li>
   *   <li>0x10 - Stunned</li>
   *   <li>0x20 - Weapon blocked</li>
   *   <li>0x40 - Dispirited</li>
   *   <li>0x80 - Poison</li>
   * </ul>
   */
  public int status_10;
  public int level_12;
  public int dlevel_13;
  public final StatCollection stats;
  private final Map<EquipmentSlot, Equipment> equipment_14 = new EnumMap<>(EquipmentSlot.class);
  public RegistryId selectedAddition_19;
  public final Map<RegistryId, CharacterAdditionStats> additionStats = new HashMap<>();
//  public final int[] additionLevels_1a = new int[8];
//  public final int[] additionXp_22 = new int[8];

  public CharacterData2c(final GameState52c gameState, final CharacterTemplate template, final StatCollection stats) {
    this.gameState = gameState;
    this.template = template;
    this.stats = stats;
  }

  public String getName() {
    return I18n.translate(this.template);
  }

  public void set(final CharacterData2c other) {
    this.xp_00 = other.xp_00;
    this.partyFlags_04 = other.partyFlags_04;
    this.stats.set(other.stats);
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

  public boolean canEquip(final EquipmentSlot slot, final Equipment equipment) {
    return this.template.canEquip(this.gameState, this, slot, equipment);
  }

  public void equip(final EquipmentSlot slot, @Nullable final Equipment equipment) {
    if(this.equipment_14.containsKey(slot)) {
      this.equipment_14.get(slot).onUnequip(this);
    }

    if(equipment != null) {
      this.equipment_14.put(slot, equipment);
      equipment.onEquip(this);
    } else {
      this.equipment_14.remove(slot);
    }
  }

  public Equipment getEquipment(final EquipmentSlot slot) {
    return this.equipment_14.get(slot);
  }

  public int getXpToNextLevel() {
    return this.template.getXpToNextLevel(this.gameState, this);
  }
}
