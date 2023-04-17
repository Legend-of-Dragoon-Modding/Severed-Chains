package legend.game.debugger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameStateViewer extends Application {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Debugger.class);

  private static Stage stage;

  public static boolean isRunning() {
    return stage != null;
  }

  public static void show() {
    stage.show();
  }

  @Override
  public void start(final Stage stage) throws Exception {
    final Parent root = FXMLLoader.load(this.getClass().getResource("gamestate_viewer.fxml"));
    final Scene scene = new Scene(root);
    scene.getStylesheets().add(this.getClass().getResource("gamestate_viewer.css").toExternalForm());

    stage.setTitle("GameState Viewer");
    stage.setScene(scene);
    stage.show();
    GameStateViewer.stage = stage;
  }
}
