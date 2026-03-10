package legend.game.characters;

import legend.game.additions.AdditionHits80;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.i18n.I18n;
import legend.game.inventory.Equipment;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CharacterData2c {
  public static final int IN_PARTY = 0x1;
  /** can be put in main party (without this flag a char can only appear in secondary and can't be put into main) */
  public static final int CAN_BE_IN_PARTY = 0x2;
  /** Used by char_utils.txt. Characters who are temporarily removed from the party can be assigned this flag to be restored later. */
  public static final int TEMPORARILY_REMOVED_FROM_PARTY = 0x4;
  /** don't select, can't be taken out of main party */
  public static final int CANT_REMOVE = 0x20;
  /** no longer used */
  public static final int HAS_ULTIMATE_ADDITION = 0x40;

  public final GameState52c gameState;
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
  private final Map<RegistryId, CharacterAdditionInfo> additions = new HashMap<>();
  private final Map<RegistryId, CharacterSpellInfo> spells = new HashMap<>();
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
    this.additions.clear();

    for(final var entry : other.additions.entrySet()) {
      this.additions.put(entry.getKey(), new CharacterAdditionInfo(entry.getValue()));
    }

    this.spells.clear();

    for(final var entry : other.spells.entrySet()) {
      this.spells.put(entry.getKey(), new CharacterSpellInfo(entry.getValue()));
    }

//    System.arraycopy(other.additionLevels_1a, 0, this.additionLevels_1a, 0, this.additionLevels_1a.length);
//    System.arraycopy(other.additionXp_22, 0, this.additionXp_22, 0, this.additionXp_22.length);
  }

  public boolean canEquip(final EquipmentSlot slot, final Equipment equipment) {
    return this.template.canEquip(this, slot, equipment);
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

  public Path getBattleModelPath(final PlayerBattleEntity bent) {
    return this.template.getBattleModelPath(this, bent);
  }

  public Path getBattleTexturePath(final PlayerBattleEntity bent) {
    return this.template.getBattleTexturePath(this, bent);
  }

  public Path getBattleSoundsPath(final PlayerBattleEntity bent) {
    return this.template.getBattleSoundsPath(this, bent);
  }

  public boolean isArcher() {
    return this.template.isArcher(this);
  }

  public int getStatusAndFlags() {
    int flags = this.status_10;

    if(this.hasDragoon()) {
      flags |= 0x2000;
    }

    return flags;
  }

  public Element getElement() {
    return this.template.getElement(this);
  }

  public boolean hasDragoon() {
    return this.template.hasDragoon(this);
  }

  public AdditionHits80 getDragoonAddition() {
    return this.template.getDragoonAddition(this);
  }

  public void applyLevelUp(@Nullable final LevelUpActions actions) {
    this.template.applyLevelUp(this, actions);
  }

  public void applyDragoonLevelUp(@Nullable final LevelUpActions actions) {
    this.template.applyDragoonLevelUp(this, actions);
  }

  public int getXpToNextLevel() {
    return this.template.getXpToNextLevel(this);
  }

  public int getDxpToNextLevel() {
    return this.template.getDxpToNextLevel(this);
  }

  public List<RegistryId> getUnlockedAdditions() {
    return this.additions.entrySet().stream()
      .filter(e -> e.getValue().getUnlockState().isUsable())
      .sorted(Comparator.comparingInt(e -> e.getValue().getUnlockTimestamp()))
      .map(Map.Entry::getKey)
      .toList()
    ;
  }

  public Set<RegistryId> getAllAdditions() {
    return this.additions.keySet();
  }

  public CharacterAdditionInfo getAdditionInfo(final RegistryId id) {
    return this.additions.get(id);
  }

  public CharacterAdditionInfo addAddition(final RegistryId id, final CharacterAdditionInfo info) {
    if(info.checkUnlock(this)) {
      info.unlock(this.gameState.timestamp_a0 + this.additions.size());
    }

    this.additions.put(id, info);
    return info;
  }

  public void removeAddition(final RegistryId id) {
    this.additions.remove(id);
  }

  public List<RegistryId> getUnlockedSpells() {
    return this.spells.entrySet().stream()
      .filter(e -> e.getValue().getUnlockState().isUsable())
      .sorted(Comparator.comparingInt(e -> e.getValue().getUnlockTimestamp()))
      .map(Map.Entry::getKey)
      .toList()
    ;
  }

  public Set<RegistryId> getAllSpells() {
    return this.spells.keySet();
  }

  public CharacterSpellInfo getSpellInfo(final RegistryId id) {
    return this.spells.get(id);
  }

  public CharacterSpellInfo addSpell(final RegistryId id, final CharacterSpellInfo info) {
    this.spells.put(id, info);
    return info;
  }

  public void removeSpell(final RegistryId id) {
    this.spells.remove(id);
  }
}
