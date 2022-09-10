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
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import legend.game.combat.types.BattleObject27c;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.modding.events.EventManager;
import legend.game.types.ScriptState;

import static legend.game.Scus94491BpeSegment_8005.combatants_8005e398;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.combat.Bttl_800c.currentEnemyNames_800c69d0;
import static legend.game.combat.Bttl_800c.playerNames_800fb378;

public class CombatDebuggerController {
  @FXML
  private ListView<ListItem> bobjList;
  private final ObservableList<ListItem> bobjs = FXCollections.observableArrayList(e -> new Observable[] {e.prop});

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

  public void initialize() {
    for(int i = 0; i < 10; i++) {
      this.bobjs.add(new ListItem(this::getCombatantName, i));
    }

    this.bobjList.setItems(this.bobjs);
    this.bobjList.setCellFactory(param -> {
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

    this.bobjList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
      final int index = newValue.intValue();
      this.displayStats(index);
    });

    this.level.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60));
    this.dlevel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5));
    this.hp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999));
    this.maxHp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999));
    this.mp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999));
    this.maxMp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999));
    this.sp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1, 9999));

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

    this.bobjList.getSelectionModel().select(0);

    EventManager.INSTANCE.register(this);
  }

  private void displayStats(final int index) {
    final int bobjIndex = _8006e398.bobjIndices_e0c.get(index).get();

    if(bobjIndex == -1) {
      return;
    }

    final ScriptState<BattleObject27c> state = scriptStatePtrArr_800bc1c0.get(bobjIndex).derefAs(ScriptState.classFor(BattleObject27c.class));
    final BattleObject27c bobj = state.innerStruct_00.deref();

    this.scriptIndex.setText("View script %d".formatted(bobjIndex));

    this.level.getValueFactory().setValue(bobj.level_04.get());
    this.dlevel.getValueFactory().setValue(bobj.dlevel_06.get());
    this.hp.getValueFactory().setValue(bobj.hp_08.get());
    this.maxHp.getValueFactory().setValue(bobj.maxHp_10.get());
    this.mp.getValueFactory().setValue(bobj.mp_0c.get());
    this.maxMp.getValueFactory().setValue(bobj.maxMp_12.get());
    this.sp.getValueFactory().setValue((int)bobj.sp_0a.get());

    this.spd.getValueFactory().setValue(bobj.speed_32.get());
    this.turn.getValueFactory().setValue((int)bobj.turnValue_4c.get());
    this.atk.getValueFactory().setValue(bobj.attack_34.get());
    this.def.getValueFactory().setValue(bobj.defence_38.get());
    this.matk.getValueFactory().setValue(bobj.magicAttack_36.get());
    this.mdef.getValueFactory().setValue(bobj.magicDefence_3a.get());
    this.ahit.getValueFactory().setValue((int)bobj.attackHit_3c.get());
    this.mhit.getValueFactory().setValue((int)bobj.magicHit_3e.get());
    this.aavd.getValueFactory().setValue((int)bobj.attackAvoid_40.get());
    this.mavd.getValueFactory().setValue((int)bobj.magicAvoid_42.get());
  }

  private String getCombatantName(final int combatantIndex) {
    final int bobjIndex = _8006e398.bobjIndices_e0c.get(combatantIndex).get();

    if(bobjIndex == -1) {
      return "unused";
    }

    final ScriptState<BattleObject27c> state = scriptStatePtrArr_800bc1c0.get(bobjIndex).derefAs(ScriptState.classFor(BattleObject27c.class));
    final BattleObject27c bobj = state.innerStruct_00.deref();

    final CombatantStruct1a8 combatant = combatants_8005e398.get(bobj.combatantIndex_26c.get());

    if((combatant.flags_19e.get() & 0x1) == 0) {
      return "unused";
    }

    if((combatant.flags_19e.get() & 0x4) == 0) {
      return currentEnemyNames_800c69d0.get(bobj.charSlot_276.get()).get();
    }

    return bobj.charIndex_272.get() == 8 ? "Who?" : playerNames_800fb378.get(bobj.charIndex_272.get()).deref().get();
  }

  public void openScriptDebugger(final ActionEvent event) throws Exception {
    if(this.bobjList.getSelectionModel().getSelectedIndex() < 0) {
      return;
    }

    final int scriptIndex = _8006e398.bobjIndices_e0c.get(this.bobjList.getSelectionModel().getSelectedIndex()).get();

    final ScriptDebugger scriptDebugger = new ScriptDebugger();
    scriptDebugger.preselectScript(scriptIndex).start(new Stage());
  }

  public void refreshStats(final ActionEvent event) {
    this.displayStats(this.bobjList.getSelectionModel().getSelectedIndex());
  }

  public void updateStats(final ActionEvent event) {
    final int index = this.bobjList.getSelectionModel().getSelectedIndex();
    final int bobjIndex = _8006e398.bobjIndices_e0c.get(index).get();

    if(bobjIndex == -1) {
      return;
    }

    final ScriptState<BattleObject27c> state = scriptStatePtrArr_800bc1c0.get(bobjIndex).derefAs(ScriptState.classFor(BattleObject27c.class));
    final BattleObject27c bobj = state.innerStruct_00.deref();

    bobj.level_04.set(this.level.getValue());
    bobj.dlevel_06.set(this.dlevel.getValue());
    bobj.hp_08.set(this.hp.getValue());
    bobj.maxHp_10.set(this.maxHp.getValue());
    bobj.mp_0c.set(this.mp.getValue());
    bobj.maxMp_12.set(this.maxMp.getValue());
    bobj.sp_0a.set(this.sp.getValue().shortValue());

    bobj.speed_32.set(this.spd.getValue());
    bobj.turnValue_4c.set(this.turn.getValue().shortValue());
    bobj.attack_34.set(this.atk.getValue());
    bobj.defence_38.set(this.def.getValue());
    bobj.magicAttack_36.set(this.matk.getValue());
    bobj.magicDefence_3a.set(this.mdef.getValue());
    bobj.attackHit_3c.set(this.ahit.getValue().shortValue());
    bobj.magicHit_3e.set(this.mhit.getValue().shortValue());
    bobj.attackAvoid_40.set(this.aavd.getValue().shortValue());
    bobj.magicAvoid_42.set(this.mavd.getValue().shortValue());
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
