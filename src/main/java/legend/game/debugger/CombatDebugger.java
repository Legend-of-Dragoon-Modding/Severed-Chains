package legend.game.debugger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CombatDebugger extends Application {
  @Override
  public void start(final Stage stage) throws Exception {
    final Parent root = FXMLLoader.load(this.getClass().getResource("combat_debugger.fxml"));
    final Scene scene = new Scene(root);
    scene.getStylesheets().add(this.getClass().getResource("combat_debugger.css").toExternalForm());

    stage.setTitle("Combat Debugger");
    stage.setScene(scene);
    stage.show();
  }
}
