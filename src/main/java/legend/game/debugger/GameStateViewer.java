package legend.game.debugger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameStateViewer extends Application {
  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("gamestate_viewer.fxml"));
    final Parent root = loader.load();
    final Scene scene = new Scene(root);
    scene.getStylesheets().add(this.getClass().getResource("gamestate_viewer.css").toExternalForm());

    stage.setTitle("GameState Viewer");
    stage.setScene(scene);
    stage.setX(Debugger.getStage().getX() + ((Debugger.getStage().getWidth() - root.prefWidth(-1)) / 2));
    stage.setY(Debugger.getStage().getY() + (Debugger.getStage().getHeight()  / 8));
    stage.show();

    stage.setOnCloseRequest(event -> ((GameStateViewerController)loader.getController()).uninitialize());
  }
}
