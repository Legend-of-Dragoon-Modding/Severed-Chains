package legend.game.debugger;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import legend.game.additions.Addition;
import legend.game.additions.CharacterAdditionStats;
import legend.game.i18n.I18n;
import legend.game.inventory.Equipment;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static legend.core.GameEngine.REGISTRIES;
import static legend.game.SItem.characterNames_801142dc;
import static legend.game.SItem.checkForNewlyUnlockedAddition;
import static legend.game.Scus94491BpeSegment_8004.CHARACTER_ADDITIONS;
import static legend.game.types.CharacterData2c.CANT_REMOVE;
import static legend.game.types.CharacterData2c.CAN_BE_IN_PARTY;
import static legend.game.types.CharacterData2c.HAS_ULTIMATE_ADDITION;
import static legend.game.types.CharacterData2c.IN_PARTY;

public class CharacterEditorController {
  public Label name;
  public CheckBox inParty;
  public CheckBox canBeInParty;
  public CheckBox cantRemove;
  public CheckBox hasUltimate;
  public TextField hp;
  public TextField mp;
  public TextField sp;
  public TextField level;
  public TextField xp;
  public TextField dlevel;
  public TextField dxp;
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
  public CheckBox additionUnlocked;
  public TextField additionLevel;
  public TextField additionXp;

  private int charId;
  private CharacterData2c charData;

  private final Map<EquipmentSlot, Equipment> equipped = new EnumMap<>(EquipmentSlot.class);
  private final Map<RegistryId, CharacterAdditionStats> additionStats = new HashMap<>();

  public void initialize() {
    for(final EquipmentSlot slot : EquipmentSlot.values()) {
      this.equipmentSlots.getItems().add(slot);
    }
  }

  public void setChar(final int id, final CharacterData2c data) {
    this.charId = id;
    this.charData = data;
    this.refresh();
  }

  private void refresh() {
    checkForNewlyUnlockedAddition(this.charId);

    this.selectedAddition.getItems().clear();
    this.additionList.getItems().clear();

    for(final RegistryDelegate<Addition> addition : CHARACTER_ADDITIONS[this.charId]) {
      this.selectedAddition.getItems().add(addition.getId());
      this.additionList.getItems().add(addition.getId());
    }

    this.name.setText(characterNames_801142dc[this.charId]);

    this.inParty.setSelected((this.charData.partyFlags_04 & IN_PARTY) != 0);
    this.canBeInParty.setSelected((this.charData.partyFlags_04 & CAN_BE_IN_PARTY) != 0);
    this.cantRemove.setSelected((this.charData.partyFlags_04 & CANT_REMOVE) != 0);
    this.hasUltimate.setSelected((this.charData.partyFlags_04 & HAS_ULTIMATE_ADDITION) != 0);

    this.hp.setText(String.valueOf(this.charData.hp_08));
    this.mp.setText(String.valueOf(this.charData.mp_0a));
    this.sp.setText(String.valueOf(this.charData.sp_0c));

    this.level.setText(String.valueOf(this.charData.level_12));
    this.xp.setText(String.valueOf(this.charData.xp_00));
    this.dlevel.setText(String.valueOf(this.charData.dlevel_13));
    this.dxp.setText(String.valueOf(this.charData.dlevelXp_0e));

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
    this.equipped.putAll(this.charData.equipment_14);

    this.additionStats.clear();
    this.additionStats.putAll(this.charData.additionStats);

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
      final CharacterAdditionStats stats = this.additionStats.get(id);
      this.additionUnlocked.setSelected(stats.unlocked);
      this.additionLevel.setText(String.valueOf(stats.level));
      this.additionXp.setText(String.valueOf(stats.xp));
    } else {
      this.additionUnlocked.setSelected(false);
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
      (this.cantRemove.isSelected() ? CANT_REMOVE : 0) |
      (this.hasUltimate.isSelected() ? HAS_ULTIMATE_ADDITION : 0)
    ;

    this.charData.hp_08 = Integer.parseInt(this.hp.getText());
    this.charData.mp_0a = Integer.parseInt(this.mp.getText());
    this.charData.sp_0c = Integer.parseInt(this.sp.getText());

    this.charData.level_12 = Integer.parseInt(this.level.getText());
    this.charData.xp_00 = Integer.parseInt(this.xp.getText());
    this.charData.dlevel_13 = Integer.parseInt(this.dlevel.getText());
    this.charData.dlevelXp_0e = Integer.parseInt(this.dxp.getText());

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
      final CharacterAdditionStats additionStats = this.additionStats.get(this.additionList.getValue());
      additionStats.unlocked = this.additionUnlocked.isSelected();
      additionStats.level = Integer.parseInt(this.additionLevel.getText());
      additionStats.xp = Integer.parseInt(this.additionXp.getText());
    }

    this.charData.equipment_14.clear();
    this.charData.equipment_14.putAll(this.equipped);

    this.charData.selectedAddition_19 = this.selectedAddition.getValue();

    this.charData.additionStats.clear();
    this.charData.additionStats.putAll(this.additionStats);
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
