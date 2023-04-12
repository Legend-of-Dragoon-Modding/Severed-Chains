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
import legend.game.combat.bobj.BattleObject27c;
import legend.game.combat.bobj.MonsterBattleObject;
import legend.game.combat.bobj.PlayerBattleObject;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.scripting.ScriptState;

import static legend.game.Scus94491BpeSegment_8005.combatants_8005e398;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
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

    this.level.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999));
    this.dlevel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5));
    this.hp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 65535));
    this.maxHp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 65535));
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

    this.bobjList.getSelectionModel().select(0);
  }

  private void displayStats(final int index) {
    final ScriptState<? extends BattleObject27c> state = _8006e398.bobjIndices_e0c[index];

    if(state == null) {
      return;
    }

    final BattleObject27c bobj = state.innerStruct_00;

    this.scriptIndex.setText("View script %d".formatted(state.index));

    if(bobj instanceof final PlayerBattleObject player) {
      this.level.getValueFactory().setValue(player.level_04);
      this.dlevel.getValueFactory().setValue(player.dlevel_06);
      this.mp.getValueFactory().setValue(player.mp_0c);
      this.maxMp.getValueFactory().setValue(player.maxMp_12);
      this.sp.getValueFactory().setValue(player.sp_0a);
      this.level.setVisible(true);
      this.dlevel.setVisible(true);
      this.mp.setVisible(true);
      this.maxMp.setVisible(true);
      this.sp.setVisible(true);
    } else if(bobj instanceof MonsterBattleObject) {
      this.level.setVisible(false);
      this.dlevel.setVisible(false);
      this.mp.setVisible(false);
      this.maxMp.setVisible(false);
      this.sp.setVisible(false);
    }

    this.hp.getValueFactory().setValue(bobj.hp_08);
    this.maxHp.getValueFactory().setValue(bobj.maxHp_10);

    this.spd.getValueFactory().setValue(bobj.speed_32);
    this.turn.getValueFactory().setValue(bobj.turnValue_4c);
    this.atk.getValueFactory().setValue(bobj.attack_34);
    this.def.getValueFactory().setValue(bobj.defence_38);
    this.matk.getValueFactory().setValue(bobj.magicAttack_36);
    this.mdef.getValueFactory().setValue(bobj.magicDefence_3a);
    this.ahit.getValueFactory().setValue(bobj.attackHit_3c);
    this.mhit.getValueFactory().setValue(bobj.magicHit_3e);
    this.aavd.getValueFactory().setValue(bobj.attackAvoid_40);
    this.mavd.getValueFactory().setValue(bobj.magicAvoid_42);
  }

  private String getCombatantName(final int combatantIndex) {
    final ScriptState<? extends BattleObject27c> state = _8006e398.bobjIndices_e0c[combatantIndex];

    if(state == null) {
      return "unused";
    }

    final BattleObject27c bobj = state.innerStruct_00;

    final CombatantStruct1a8 combatant = combatants_8005e398[bobj.combatantIndex_26c];

    if((combatant.flags_19e & 0x1) == 0) {
      return "unused";
    }

    if((combatant.flags_19e & 0x4) == 0) {
      return currentEnemyNames_800c69d0.get(bobj.charSlot_276).get();
    }

    return bobj.charIndex_272 == 8 ? "Who?" : playerNames_800fb378.get(bobj.charIndex_272).deref().get();
  }

  public void openScriptDebugger(final ActionEvent event) throws Exception {
    if(this.bobjList.getSelectionModel().getSelectedIndex() < 0) {
      return;
    }

    final ScriptState<? extends BattleObject27c> state = _8006e398.bobjIndices_e0c[this.bobjList.getSelectionModel().getSelectedIndex()];

    final ScriptDebugger scriptDebugger = new ScriptDebugger();
    scriptDebugger.preselectScript(state.index).start(new Stage());
  }

  public void refreshStats(final ActionEvent event) {
    this.displayStats(this.bobjList.getSelectionModel().getSelectedIndex());
  }

  public void updateStats(final ActionEvent event) {
    final int index = this.bobjList.getSelectionModel().getSelectedIndex();
    final ScriptState<? extends BattleObject27c> state = _8006e398.bobjIndices_e0c[index];

    if(state == null) {
      return;
    }

    final BattleObject27c bobj = state.innerStruct_00;

    if(bobj instanceof final PlayerBattleObject player) {
      player.level_04 = this.level.getValue();
      player.dlevel_06 = this.dlevel.getValue();
      player.mp_0c = this.mp.getValue();
      player.maxMp_12 = this.maxMp.getValue();
      player.sp_0a = this.sp.getValue().shortValue();
    }

    bobj.hp_08 = this.hp.getValue();
    bobj.maxHp_10 = this.maxHp.getValue();

    bobj.speed_32 = this.spd.getValue();
    bobj.turnValue_4c = this.turn.getValue().shortValue();
    bobj.attack_34 = this.atk.getValue();
    bobj.defence_38 = this.def.getValue();
    bobj.magicAttack_36 = this.matk.getValue();
    bobj.magicDefence_3a = this.mdef.getValue();
    bobj.attackHit_3c = this.ahit.getValue().shortValue();
    bobj.magicHit_3e = this.mhit.getValue().shortValue();
    bobj.attackAvoid_40 = this.aavd.getValue().shortValue();
    bobj.magicAvoid_42 = this.mavd.getValue().shortValue();
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
