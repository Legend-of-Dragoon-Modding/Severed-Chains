package legend.game.debugger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerUi extends Application {
  @Override
  public void start(final Stage stage) throws Exception {
    final Parent root = FXMLLoader.load(this.getClass().getResource("server_ui.fxml"));
    final Scene scene = new Scene(root);
    scene.getStylesheets().add(this.getClass().getResource("server_ui.css").toExternalForm());

    stage.setTitle("Server");
    stage.setScene(scene);
    stage.setX(Debugger.getStage().getX() + ((Debugger.getStage().getWidth() - root.prefWidth(-1)) / 2));
    stage.setY(Debugger.getStage().getY() + ((Debugger.getStage().getHeight() - root.prefHeight(-1)) / 2));
    stage.show();
  }
}
