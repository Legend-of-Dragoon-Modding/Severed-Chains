package legend.game.debugger;

import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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

public class CombatDebugger extends Application {
  @FXML
  private ListView<ListItem> bobjList;
  private final ObservableList<ListItem> bobjs = FXCollections.observableArrayList(e -> new Observable[] {e.prop});

  @FXML
  private Button scriptIndex;

  @Override
  public void start(final Stage stage) throws Exception {
    final Parent root = FXMLLoader.load(getClass().getResource("combat_debugger.fxml"));
    final Scene scene = new Scene(root);
    scene.getStylesheets().add(getClass().getResource("combat_debugger.css").toExternalForm());

    stage.setTitle("Combat Debugger");
    stage.setScene(scene);
    stage.show();
  }

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
      final int bobjIndex = (int)_8006e398.offset(0xe0cL).offset(index * 0x4L).get();

      if(bobjIndex == -1) {
        return;
      }

      scriptIndex.setText("View script %d".formatted(bobjIndex));
    });

    this.bobjList.getSelectionModel().select(0);

    EventManager.INSTANCE.register(this);
  }

  private String getCombatantName(final int combatantIndex) {
    final int bobjIndex = (int)_8006e398.offset(0xe0cL).offset(combatantIndex * 0x4L).get();

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

    final int scriptIndex = (int)_8006e398.offset(0xe0cL).offset(this.bobjList.getSelectionModel().getSelectedIndex() * 0x4L).get();

    final ScriptDebugger scriptDebugger = new ScriptDebugger();
    scriptDebugger.preselectScript(scriptIndex).start(new Stage());
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
      this.prop.set(this.index + ": " + nameFunc.get(index));
    }

    public String getName() {
      return this.prop.get();
    }
  }
}
