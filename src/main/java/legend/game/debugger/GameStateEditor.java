package legend.game.debugger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameStateEditor extends Application {
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
    final Parent root = FXMLLoader.load(this.getClass().getResource("gamestate_editor.fxml"));
    final Scene scene = new Scene(root);
    scene.getStylesheets().add(this.getClass().getResource("gamestate_editor.css").toExternalForm());

    stage.setTitle("GameState Editor");
    stage.setScene(scene);
    stage.setX(Debugger.getStage().getX() + ((Debugger.getStage().getWidth() - root.prefWidth(-1)) / 2));
    stage.setY(Debugger.getStage().getY() + (Debugger.getStage().getHeight() / 8));
    stage.show();
    GameStateEditor.stage = stage;
  }
}
