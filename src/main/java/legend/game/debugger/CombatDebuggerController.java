package legend.game.debugger;

import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import legend.game.Scus94491BpeSegment_8006;
import legend.game.characters.UnaryStat;
import legend.game.characters.VitalsStat;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.MonsterBattleEntity;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.combat.ui.BattleHud;
import legend.game.scripting.ScriptState;
import legend.game.scripting.ScriptTempParam;
import legend.lodmod.LodMod;

import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;

public class CombatDebuggerController {
  @FXML
  private ListView<ListItem> bentList;
  private final ObservableList<ListItem> bents = FXCollections.observableArrayList(e -> new Observable[] {e.prop});

  @FXML
  private Button scriptIndex;

  @FXML
  public Spinner<Integer> level;
  @FXML
  public Spinner<Integer> dlevel;
  @FXML
  public Spinner<Integer> hp;
  @FXML
  public Spinner<Integer> maxHp;
  @FXML
  public Spinner<Integer> mp;
  @FXML
  public Spinner<Integer> maxMp;
  @FXML
  public Spinner<Integer> sp;

  @FXML
  public Spinner<Integer> spd;
  @FXML
  public Label spdMod;
  @FXML
  public Spinner<Integer> turn;
  @FXML
  public Spinner<Integer> atk;
  @FXML
  public Spinner<Integer> def;
  @FXML
  public Spinner<Integer> matk;
  @FXML
  public Spinner<Integer> mdef;
  @FXML
  public Spinner<Integer> ahit;
  @FXML
  public Spinner<Integer> mhit;
  @FXML
  public Spinner<Integer> aavd;
  @FXML
  public Spinner<Integer> mavd;

  @FXML
  public Button refreshStats;
  @FXML
  public Button updateStats;

  @FXML
  public ComboBox<String> statusCondition;

  @FXML
  public Button setStatus;

  @FXML
  public Button getStatus;


  public void initialize() {
    for(int i = 0; i < 10; i++) {
      this.bents.add(new ListItem(this::getCombatantName, i));
    }

    this.bentList.setItems(this.bents);
    this.bentList.setCellFactory(param -> {
      final TextFieldListCell<ListItem> cell = new TextFieldListCell<>();
      cell.setConverter(new StringConverter<>() {
        @Override
        public String toString(final ListItem object) {
          return object != null ? object.getName() : null;
        }

        @Override
        public ListItem fromString(final String string) {
          return null;
        }
      });
      return cell;
    });

    this.bentList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
      final int index = newValue.intValue();
      this.displayStats(index);
    });

    this.statusCondition.getItems().add("None");
    for(int i = BattleHud.ailments_800fb3a0.length - 1; i >= 0; i--) {
      this.statusCondition.getItems().add(BattleHud.ailments_800fb3a0[i]);
    }
    this.statusCondition.getSelectionModel().select(0);

    this.level.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999));
    this.dlevel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5));
    this.hp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
    this.maxHp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE));
    this.mp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999));
    this.maxMp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999));
    this.sp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999));

    this.spd.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999));
    this.turn.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999));
    this.atk.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999));
    this.def.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999));
    this.matk.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999));
    this.mdef.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999));
    this.ahit.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999));
    this.mhit.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999));
    this.aavd.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999));
    this.mavd.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999));

    this.bentList.getSelectionModel().select(0);
  }

  private void displayStats(final int index) {
    final ScriptState<? extends BattleEntity27c> state = battleState_8006e398.allBents_e0c[index];

    if(state == null) {
      return;
    }

    final BattleEntity27c bent = state.innerStruct_00;

    this.scriptIndex.setText("View script %d".formatted(state.index));

    if(bent instanceof final PlayerBattleEntity player) {
      final VitalsStat mp = player.stats.getStat(LodMod.MP_STAT.get());
      final VitalsStat sp = player.stats.getStat(LodMod.SP_STAT.get());

      this.level.getValueFactory().setValue(player.level_04);
      this.dlevel.getValueFactory().setValue(player.dlevel_06);
      this.mp.getValueFactory().setValue(mp.getCurrent());
      this.maxMp.getValueFactory().setValue(mp.getMaxRaw());
      this.sp.getValueFactory().setValue(sp.getCurrent());
      this.level.setVisible(true);
      this.dlevel.setVisible(true);
      this.mp.setVisible(true);
      this.maxMp.setVisible(true);
      this.sp.setVisible(true);
    } else if(bent instanceof MonsterBattleEntity) {
      this.level.setVisible(false);
      this.dlevel.setVisible(false);
      this.mp.setVisible(false);
      this.maxMp.setVisible(false);
      this.sp.setVisible(false);
    }

    final VitalsStat hp = bent.stats.getStat(LodMod.HP_STAT.get());
    this.hp.getValueFactory().setValue(hp.getCurrent());
    this.maxHp.getValueFactory().setValue(hp.getMaxRaw());

    final UnaryStat speedStat = bent.stats.getStat(LodMod.SPEED_STAT.get());
    final int speedMod = speedStat.getMods();
    this.spd.getValueFactory().setValue(speedStat.getRaw());
    this.spdMod.setText(speedMod < 0 ? Integer.toString(speedMod) : "+" + speedMod);

    this.turn.getValueFactory().setValue(bent.turnValue_4c);
    this.atk.getValueFactory().setValue(bent.attack_34);
    this.def.getValueFactory().setValue(bent.defence_38);
    this.matk.getValueFactory().setValue(bent.magicAttack_36);
    this.mdef.getValueFactory().setValue(bent.magicDefence_3a);
    this.ahit.getValueFactory().setValue(bent.attackHit_3c);
    this.mhit.getValueFactory().setValue(bent.magicHit_3e);
    this.aavd.getValueFactory().setValue(bent.attackAvoid_40);
    this.mavd.getValueFactory().setValue(bent.magicAvoid_42);

    this.statusCondition.getSelectionModel().select(this.getStatusIndexFromFlags(bent.status_0e) + 1);
  }

  private String getCombatantName(final int combatantIndex) {
    final ScriptState<? extends BattleEntity27c> state = battleState_8006e398.allBents_e0c[combatantIndex];

    if(state == null) {
      return "unused";
    }

    final BattleEntity27c bent = state.innerStruct_00;
    final CombatantStruct1a8 combatant = bent.combatant_144;

    if((combatant.flags_19e & 0x1) == 0) {
      return "unused";
    }

    return bent.getName();
  }

  public void openScriptDebugger(final ActionEvent event) throws Exception {
    if(this.bentList.getSelectionModel().getSelectedIndex() < 0) {
      return;
    }

    final ScriptState<? extends BattleEntity27c> state = battleState_8006e398.allBents_e0c[this.bentList.getSelectionModel().getSelectedIndex()];

    final ScriptDebugger scriptDebugger = new ScriptDebugger();
    scriptDebugger.preselectScript(state.index).start(new Stage());
  }

  public void refreshStats(final ActionEvent event) {
    this.displayStats(this.bentList.getSelectionModel().getSelectedIndex());
  }

  public void updateStats(final ActionEvent event) {
    final int index = this.bentList.getSelectionModel().getSelectedIndex();
    final ScriptState<? extends BattleEntity27c> state = battleState_8006e398.allBents_e0c[index];

    if(state == null) {
      return;
    }

    final BattleEntity27c bent = state.innerStruct_00;

    if(bent instanceof final PlayerBattleEntity player) {
      final VitalsStat mp = player.stats.getStat(LodMod.MP_STAT.get());
      final VitalsStat sp = player.stats.getStat(LodMod.SP_STAT.get());

      player.level_04 = this.level.getValue();
      player.dlevel_06 = this.dlevel.getValue();
      mp.setCurrent(this.mp.getValue());
      mp.setMaxRaw(this.maxMp.getValue());
      sp.setCurrent(this.sp.getValue());
    }

    final VitalsStat hp = bent.stats.getStat(LodMod.HP_STAT.get());
    hp.setCurrent(this.hp.getValue());
    hp.setMaxRaw(this.maxHp.getValue());

    bent.stats.getStat(LodMod.SPEED_STAT.get()).setRaw(this.spd.getValue());
    bent.turnValue_4c = this.turn.getValue().shortValue();
    bent.attack_34 = this.atk.getValue();
    bent.defence_38 = this.def.getValue();
    bent.magicAttack_36 = this.matk.getValue();
    bent.magicDefence_3a = this.mdef.getValue();
    bent.attackHit_3c = this.ahit.getValue().shortValue();
    bent.magicHit_3e = this.mhit.getValue().shortValue();
    bent.attackAvoid_40 = this.aavd.getValue().shortValue();
    bent.magicAvoid_42 = this.mavd.getValue().shortValue();
  }

  public void getStatusCondition(final ActionEvent event) {
    final int index = this.bentList.getSelectionModel().getSelectedIndex();
    final ScriptState<? extends BattleEntity27c> state = battleState_8006e398.allBents_e0c[index];

    final int statusIndex = this.getStatusIndexFromFlags(state.innerStruct_00.status_0e) + 1;

    this.statusCondition.getSelectionModel().select(statusIndex);
  }

  public void setStatusCondition(final ActionEvent event) {
    final int index = this.bentList.getSelectionModel().getSelectedIndex();
    final ScriptState<? extends BattleEntity27c> state = battleState_8006e398.allBents_e0c[index];

    final int statusIndex = this.getStatusIndexFromFlags(state.innerStruct_00.status_0e);
    final int selectedStatusIndex = this.statusCondition.getSelectionModel().getSelectedIndex();

    //PCS does not check if combatant is affected by the same status
    if(statusIndex == selectedStatusIndex - 1) {
      return;
    }
    if(selectedStatusIndex == 0) {
      this.cureStatusCondition(event);
      return;
    }

    state.context.params_20[0] = new ScriptTempParam(state.index);
    state.context.params_20[1] = new ScriptTempParam(10);
    state.context.params_20[2] = new ScriptTempParam(selectedStatusIndex - 1);

    state.scriptForkAndReenter();
  }

  public void cureStatusCondition(final ActionEvent event) {
    final int index = this.bentList.getSelectionModel().getSelectedIndex();
    final ScriptState<? extends BattleEntity27c> state = battleState_8006e398.allBents_e0c[index];

    final int arrIndex = state.innerStruct_00.combatantIndex_26c;
    Scus94491BpeSegment_8006.battleState_8006e398.status_384[arrIndex].unpack(0);
  }

  private int getStatusIndexFromFlags(int flags) {
    final int statusFlags = flags & 0xff;
    return statusFlags == 0 ? -1 : Integer.numberOfTrailingZeros(statusFlags);
  }

  private static class ListItem {
    private final Int2ObjectFunction<String> nameFunc;
    private final StringProperty prop = new SimpleStringProperty(this, "name");
    private final int index;

    public ListItem(final Int2ObjectFunction<String> nameFunc, final int index) {
      this.nameFunc = nameFunc;
      this.index = index;
      this.update();
    }

    public void update() {
      this.prop.set(this.index + ": " + this.nameFunc.get(this.index));
    }

    public String getName() {
      return this.prop.get();
    }
  }
}
