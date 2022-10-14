package legend.game.debugger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import static legend.game.SMap.FUN_800e5534;
import static legend.game.SMap.encounterData_800f64c4;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.submapStage_800bb0f4;

public class DebuggerController {
  @FXML
  private MenuItem menuDebuggersScript;
  @FXML
  private MenuItem menuDebuggersCombat;
  @FXML
  private MenuItem menuDebuggersSubmap;

  @FXML
  public Spinner<Integer> encounterId;
  @FXML
  public Button getEncounterId;
  @FXML
  public Button startEncounter;

  public void initialize() {
    this.encounterId.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
  }

  @FXML
  private void showScriptDebugger(final ActionEvent event) throws Exception {
    new ScriptDebugger().start(new Stage());
  }

  @FXML
  private void showCombatDebugger(final ActionEvent event) throws Exception {
    new CombatDebugger().start(new Stage());
  }

  @FXML
  private void showSubmapDebugger(final ActionEvent event) throws Exception {
    new SmapDebugger().start(new Stage());
  }

  @FXML
  private void getEncounterId(final ActionEvent event) {
    this.encounterId.getValueFactory().setValue(encounterId_800bb0f8.get());
  }

  @FXML
  private void startEncounter(final ActionEvent event) {
    encounterId_800bb0f8.set(this.encounterId.getValue());
    submapStage_800bb0f4.set(encounterData_800f64c4.get(submapCut_80052c30.get()).stage_03.get());
    FUN_800e5534(-1, 0);
  }
}
