package legend.game.debugger;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import legend.game.Scus94491BpeSegment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Debugger extends Application {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Debugger.class);

  private static Stage stage;

  @FXML
  private MenuItem menuDebuggersScript;
  @FXML
  private MenuItem menuDebuggersCombat;

  @FXML
  private CheckBox scriptLog;

  public static boolean isRunning() {
    return stage != null;
  }

  public static void show() {
    stage.show();
  }

  @Override
  public void start(final Stage stage) throws Exception {
    final Parent root = FXMLLoader.load(this.getClass().getResource("debugger.fxml"));
    final Scene scene = new Scene(root);
    scene.getStylesheets().add(this.getClass().getResource("debugger.css").toExternalForm());

    stage.setTitle("Debugger");
    stage.setScene(scene);
    stage.show();
    Debugger.stage = stage;
  }

  @FXML
  private void showScriptDebugger(final ActionEvent event) throws Exception {
    new ScriptDebugger().start(new Stage());
  }

  @FXML
  private void showCombatDebugger(final ActionEvent event) throws Exception {
    new CombatDebugger().start(new Stage());
  }

  public void scriptLogClick(final ActionEvent event) {
    Scus94491BpeSegment.scriptLog = this.scriptLog.isSelected();
  }
}
