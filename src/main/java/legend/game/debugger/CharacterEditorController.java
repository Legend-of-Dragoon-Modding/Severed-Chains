package legend.game.debugger;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import legend.game.additions.UnlockState;
import legend.game.characters.CharacterAdditionInfo;
import legend.game.characters.CharacterData2c;
import legend.game.characters.VitalsStat;
import legend.game.i18n.I18n;
import legend.game.inventory.Equipment;
import legend.game.types.EquipmentSlot;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static legend.core.GameEngine.REGISTRIES;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.characters.CharacterData2c.CANT_REMOVE;
import static legend.game.characters.CharacterData2c.CAN_BE_IN_PARTY;
import static legend.game.characters.CharacterData2c.IN_PARTY;
import static legend.lodmod.LodMod.ATTACK_AVOID_STAT;
import static legend.lodmod.LodMod.ATTACK_HIT_STAT;
import static legend.lodmod.LodMod.ATTACK_STAT;
import static legend.lodmod.LodMod.DEFENSE_STAT;
import static legend.lodmod.LodMod.DRAGOON_ATTACK_STAT;
import static legend.lodmod.LodMod.DRAGOON_DEFENSE_STAT;
import static legend.lodmod.LodMod.DRAGOON_MAGIC_ATTACK_STAT;
import static legend.lodmod.LodMod.DRAGOON_MAGIC_DEFENSE_STAT;
import static legend.lodmod.LodMod.GUARD_HEAL_STAT;
import static legend.lodmod.LodMod.HP_STAT;
import static legend.lodmod.LodMod.MAGIC_ATTACK_STAT;
import static legend.lodmod.LodMod.MAGIC_AVOID_STAT;
import static legend.lodmod.LodMod.MAGIC_DEFENSE_STAT;
import static legend.lodmod.LodMod.MAGIC_HIT_STAT;
import static legend.lodmod.LodMod.MP_STAT;
import static legend.lodmod.LodMod.SPEED_STAT;
import static legend.lodmod.LodMod.SP_STAT;

public class CharacterEditorController {
  public Label name;
  public CheckBox inParty;
  public CheckBox canBeInParty;
  public CheckBox cantRemove;
  public Spinner<Integer> hp;
  public Spinner<Integer> mp;
  public Spinner<Integer> sp;
  public Spinner<Integer> maxHp;
  public Spinner<Integer> maxMp;
  public Spinner<Integer> maxSp;
  public Label hpMods;
  public Label mpMods;
  public Label spMods;
  public Spinner<Integer> level;
  public Spinner<Integer> xp;
  public Spinner<Integer> dlevel;
  public Spinner<Integer> dxp;
  public Spinner<Integer> atk;
  public Spinner<Integer> matk;
  public Spinner<Integer> datk;
  public Spinner<Integer> dmatk;
  public Spinner<Integer> def;
  public Spinner<Integer> mdef;
  public Spinner<Integer> ddef;
  public Spinner<Integer> dmdef;
  public Spinner<Integer> ahit;
  public Spinner<Integer> mhit;
  public Spinner<Integer> aav;
  public Spinner<Integer> mav;
  public Spinner<Integer> spd;
  public Spinner<Integer> heal;
  public CheckBox petrified;
  public CheckBox bewitched;
  public CheckBox confused;
  public CheckBox fearful;
  public CheckBox stunned;
  public CheckBox weaponBlocked;
  public CheckBox dispirited;
  public CheckBox poisoned;
  public ComboBox<EquipmentSlot> equipmentSlots;
  public Label equipmentName;
  public ComboBox<RegistryId> selectedAddition;
  public ComboBox<RegistryId> additionList;
  public ComboBox<UnlockState> additionUnlockState;
  public TextField additionLevel;
  public TextField additionXp;

  private CharacterData2c charData;

  private final Map<EquipmentSlot, Equipment> equipped = new EnumMap<>(EquipmentSlot.class);
  private final Map<RegistryId, CharacterAdditionInfo> additionInfo = new HashMap<>();

  public void initialize() {
    this.hp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.mp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.sp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.maxHp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.maxMp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.maxSp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.level.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.xp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.dlevel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.dxp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.atk.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.matk.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.datk.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.dmatk.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.def.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.mdef.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.ddef.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.dmdef.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.ahit.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.mhit.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.aav.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.mav.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.spd.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));
    this.heal.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999_999_999));

    for(final EquipmentSlot slot : EquipmentSlot.values()) {
      this.equipmentSlots.getItems().add(slot);
    }

    for(final UnlockState state : UnlockState.values()) {
      this.additionUnlockState.getItems().add(state);
    }
  }

  public void setChar(final CharacterData2c data) {
    this.charData = data;
    this.refresh();
  }

  private void refresh() {
    this.selectedAddition.getItems().clear();
    this.additionList.getItems().clear();

    for(final RegistryId id : this.charData.getAllAdditions()) {
      this.selectedAddition.getItems().add(id);
      this.additionList.getItems().add(id);
    }

    this.name.setText(this.charData.getName() + " (" + this.charData.template.getRegistryId() + ')');

    this.inParty.setSelected((this.charData.partyFlags_04 & IN_PARTY) != 0);
    this.canBeInParty.setSelected((this.charData.partyFlags_04 & CAN_BE_IN_PARTY) != 0);
    this.cantRemove.setSelected((this.charData.partyFlags_04 & CANT_REMOVE) != 0);

    this.hp.getValueFactory().setValue(this.charData.stats.getStat(HP_STAT.get()).getCurrent());
    this.mp.getValueFactory().setValue(this.charData.stats.getStat(MP_STAT.get()).getCurrent());
    this.sp.getValueFactory().setValue(this.charData.stats.getStat(SP_STAT.get()).getCurrent());
    this.maxHp.getValueFactory().setValue(this.charData.stats.getStat(HP_STAT.get()).getMaxRaw());
    this.maxMp.getValueFactory().setValue(this.charData.stats.getStat(MP_STAT.get()).getMaxRaw());
    this.maxSp.getValueFactory().setValue(this.charData.stats.getStat(SP_STAT.get()).getMaxRaw());
    this.hpMods.setText("+ " + (this.charData.stats.getStat(HP_STAT.get()).getMax() - this.charData.stats.getStat(HP_STAT.get()).getMaxRaw()));
    this.mpMods.setText("+ " + (this.charData.stats.getStat(MP_STAT.get()).getMax() - this.charData.stats.getStat(MP_STAT.get()).getMaxRaw()));
    this.spMods.setText("+ " + (this.charData.stats.getStat(SP_STAT.get()).getMax() - this.charData.stats.getStat(SP_STAT.get()).getMaxRaw()));

    this.level.getValueFactory().setValue(this.charData.level_12);
    this.xp.getValueFactory().setValue(this.charData.xp_00);
    this.dlevel.getValueFactory().setValue(this.charData.dlevel_13);
    this.dxp.getValueFactory().setValue(this.charData.dlevelXp_0e);

    this.atk.getValueFactory().setValue(this.charData.stats.getStat(ATTACK_STAT.get()).getRaw());
    this.matk.getValueFactory().setValue(this.charData.stats.getStat(MAGIC_ATTACK_STAT.get()).getRaw());
    this.datk.getValueFactory().setValue(this.charData.stats.getStat(DRAGOON_ATTACK_STAT.get()).getRaw());
    this.dmatk.getValueFactory().setValue(this.charData.stats.getStat(DRAGOON_MAGIC_ATTACK_STAT.get()).getRaw());
    this.def.getValueFactory().setValue(this.charData.stats.getStat(DEFENSE_STAT.get()).getRaw());
    this.mdef.getValueFactory().setValue(this.charData.stats.getStat(MAGIC_DEFENSE_STAT.get()).getRaw());
    this.ddef.getValueFactory().setValue(this.charData.stats.getStat(DRAGOON_DEFENSE_STAT.get()).getRaw());
    this.dmdef.getValueFactory().setValue(this.charData.stats.getStat(DRAGOON_MAGIC_DEFENSE_STAT.get()).getRaw());
    this.ahit.getValueFactory().setValue(this.charData.stats.getStat(ATTACK_HIT_STAT.get()).getRaw());
    this.mhit.getValueFactory().setValue(this.charData.stats.getStat(MAGIC_HIT_STAT.get()).getRaw());
    this.aav.getValueFactory().setValue(this.charData.stats.getStat(ATTACK_AVOID_STAT.get()).getRaw());
    this.mav.getValueFactory().setValue(this.charData.stats.getStat(MAGIC_AVOID_STAT.get()).getRaw());
    this.spd.getValueFactory().setValue(this.charData.stats.getStat(SPEED_STAT.get()).getRaw());
    this.heal.getValueFactory().setValue(this.charData.stats.getStat(GUARD_HEAL_STAT.get()).getRaw());

    this.petrified.setSelected((this.charData.status_10 & 0x1) != 0);
    this.bewitched.setSelected((this.charData.status_10 & 0x2) != 0);
    this.confused.setSelected((this.charData.status_10 & 0x4) != 0);
    this.fearful.setSelected((this.charData.status_10 & 0x8) != 0);
    this.stunned.setSelected((this.charData.status_10 & 0x10) != 0);
    this.weaponBlocked.setSelected((this.charData.status_10 & 0x20) != 0);
    this.dispirited.setSelected((this.charData.status_10 & 0x40) != 0);
    this.poisoned.setSelected((this.charData.status_10 & 0x80) != 0);

    this.equipmentSlots.getSelectionModel().select(0);
    this.selectedAddition.getSelectionModel().select(this.charData.selectedAddition_19);
    this.additionList.getSelectionModel().select(0);

    this.equipped.clear();

    for(final EquipmentSlot slot : EquipmentSlot.values()) {
      final Equipment equipment = this.charData.getEquipment(slot);

      if(equipment != null) {
        this.equipped.put(slot, equipment);
      }
    }

    this.additionInfo.clear();

    for(final RegistryId additionId : this.charData.getAllAdditions()) {
      this.additionInfo.put(additionId, this.charData.getAdditionInfo(additionId));
    }

    this.refreshEquipment();
    this.refreshAddition();
  }

  private void refreshEquipment() {
    final Equipment equipped = this.equipped.get(this.equipmentSlots.getValue());

    if(equipped != null) {
      final RegistryId id = equipped.getRegistryId();
      this.equipmentName.setText(id + " - " + I18n.translate(equipped));
    } else {
      this.equipmentName.setText("<none>");
    }
  }

  private void refreshAddition() {
    final RegistryId id = this.additionList.getValue();

    if(id != null) {
      final CharacterAdditionInfo info = this.additionInfo.get(id);
      this.additionUnlockState.getSelectionModel().select(info.getUnlockState());
      this.additionLevel.setText(String.valueOf(info.level));
      this.additionXp.setText(String.valueOf(info.xp));
    } else {
      this.additionUnlockState.getSelectionModel().clearSelection();
      this.additionLevel.setText("");
      this.additionXp.setText("");
    }
  }

  public void onRefreshClick() {
    this.refresh();
  }

  public void onSaveClick() {
    this.charData.partyFlags_04 =
      (this.inParty.isSelected() ? IN_PARTY : 0) |
      (this.canBeInParty.isSelected() ? CAN_BE_IN_PARTY : 0) |
      (this.cantRemove.isSelected() ? CANT_REMOVE : 0)
    ;

    this.charData.stats.getStat(HP_STAT.get()).setCurrent(this.hp.getValue());
    this.charData.stats.getStat(MP_STAT.get()).setCurrent(this.mp.getValue());
    this.charData.stats.getStat(SP_STAT.get()).setCurrent(this.sp.getValue());
    this.charData.stats.getStat(HP_STAT.get()).setMaxRaw(this.maxHp.getValue());
    this.charData.stats.getStat(MP_STAT.get()).setMaxRaw(this.maxMp.getValue());
    this.charData.stats.getStat(SP_STAT.get()).setMaxRaw(this.maxSp.getValue());

    this.charData.level_12 = this.level.getValue();
    this.charData.xp_00 = this.xp.getValue();
    this.charData.dlevel_13 = this.dlevel.getValue();
    this.charData.dlevelXp_0e = this.dxp.getValue();

    this.charData.stats.getStat(ATTACK_STAT.get()).setRaw(this.atk.getValue());
    this.charData.stats.getStat(MAGIC_ATTACK_STAT.get()).setRaw(this.matk.getValue());
    this.charData.stats.getStat(DRAGOON_ATTACK_STAT.get()).setRaw(this.datk.getValue());
    this.charData.stats.getStat(DRAGOON_MAGIC_ATTACK_STAT.get()).setRaw(this.dmatk.getValue());
    this.charData.stats.getStat(DEFENSE_STAT.get()).setRaw(this.def.getValue());
    this.charData.stats.getStat(MAGIC_DEFENSE_STAT.get()).setRaw(this.mdef.getValue());
    this.charData.stats.getStat(DRAGOON_DEFENSE_STAT.get()).setRaw(this.ddef.getValue());
    this.charData.stats.getStat(DRAGOON_MAGIC_DEFENSE_STAT.get()).setRaw(this.dmdef.getValue());
    this.charData.stats.getStat(ATTACK_HIT_STAT.get()).setRaw(this.ahit.getValue());
    this.charData.stats.getStat(MAGIC_HIT_STAT.get()).setRaw(this.mhit.getValue());
    this.charData.stats.getStat(ATTACK_AVOID_STAT.get()).setRaw(this.aav.getValue());
    this.charData.stats.getStat(MAGIC_AVOID_STAT.get()).setRaw(this.mav.getValue());
    this.charData.stats.getStat(SPEED_STAT.get()).setRaw(this.spd.getValue());
    this.charData.stats.getStat(GUARD_HEAL_STAT.get()).setRaw(this.heal.getValue());

    this.charData.status_10 =
      (this.petrified.isSelected() ? 0x1 : 0) |
      (this.bewitched.isSelected() ? 0x2 : 0) |
      (this.confused.isSelected() ? 0x4 : 0) |
      (this.fearful.isSelected() ? 0x8 : 0) |
      (this.stunned.isSelected() ? 0x10 : 0) |
      (this.weaponBlocked.isSelected() ? 0x20 : 0) |
      (this.dispirited.isSelected() ? 0x40 : 0) |
      (this.poisoned.isSelected() ? 0x80 : 0)
    ;

    if(this.additionList.getValue() != null) {
      final CharacterAdditionInfo info = this.additionInfo.get(this.additionList.getValue());

      if(info.getUnlockState() != this.additionUnlockState.getValue()) {
        info.setUnlockState(this.additionUnlockState.getValue(), gameState_800babc8.timestamp_a0);
      }

      info.level = Integer.parseInt(this.additionLevel.getText());
      info.xp = Integer.parseInt(this.additionXp.getText());
    }

    for(final EquipmentSlot slot : EquipmentSlot.values()) {
      final Equipment current = this.charData.getEquipment(slot);
      final Equipment equip = this.equipped.get(slot);

      if(current != equip) {
        this.charData.equip(slot, equip);
      }
    }

    this.charData.selectedAddition_19 = this.selectedAddition.getValue();
  }

  public void onHpRestoreClick() {
    final VitalsStat stat = this.charData.stats.getStat(HP_STAT.get());
    stat.restore();
    this.hp.getValueFactory().setValue(stat.getCurrent());
  }

  public void onMpRestoreClick() {
    final VitalsStat stat = this.charData.stats.getStat(MP_STAT.get());
    stat.restore();
    this.mp.getValueFactory().setValue(stat.getCurrent());
  }

  public void onSpRestoreClick() {
    final VitalsStat stat = this.charData.stats.getStat(SP_STAT.get());
    stat.restore();
    this.sp.getValueFactory().setValue(stat.getCurrent());
  }

  public void onLevelUpClick() {
    this.charData.applyLevelUp(null);
    this.refresh();
  }

  public void onDlevelUpClick() {
    this.charData.applyDragoonLevelUp(null);
    this.refresh();
  }

  public void onEquipmentSlotSelect() {
    this.refreshEquipment();
  }

  public void onEquipmentSelectClick() throws Exception {
    final RegistrySelector selector = new RegistrySelector(REGISTRIES.equipment, this::equipmentSelectorOnSelect, () -> {});
    selector.start(new Stage());
  }

  private void equipmentSelectorOnSelect(final Equipment equipment) {
    this.equipped.put(this.equipmentSlots.getValue(), equipment);
    this.refreshEquipment();
  }

  public void onEquipmentClearClick() {
    this.equipped.put(this.equipmentSlots.getValue(), null);
    this.refreshEquipment();
  }

  public void onAdditionSelect() {
    this.refreshAddition();
  }
}
