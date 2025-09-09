package legend.game.debugger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Debugger extends Application {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Debugger.class);

  private static Stage stage;

  public static boolean isRunning() {
    return stage != null;
  }

  public static void show() {
    stage.show();
    stage.toFront();
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

  public static Stage getStage() {
    return Debugger.stage;
  }
}
