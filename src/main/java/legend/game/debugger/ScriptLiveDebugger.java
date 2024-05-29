package legend.game.debugger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import legend.game.scripting.ScriptState;

public class ScriptLiveDebugger extends Application {
  private ScriptLiveDebuggerController controller;
  private int index;

  public ScriptLiveDebugger setState(final int index) {
    this.index = index;
    return this;
  }

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("script_live_debugger.fxml"));
    final Parent root = loader.load();
    final Scene scene = new Scene(root);
    scene.getStylesheets().add(this.getClass().getResource("script_live_debugger.css").toExternalForm());

    this.controller = loader.getController();
    this.controller.setState(this.index);

    stage.setOnCloseRequest(event -> {
      this.controller.uninitialize();
      this.controller = null;
    });

    stage.setTitle("Script %d Live Debugger".formatted(this.index));
    stage.setScene(scene);
    stage.setX(Debugger.getStage().getX() + ((Debugger.getStage().getWidth() - root.prefWidth(-1)) / 2));
    stage.setY(Debugger.getStage().getY() + ((Debugger.getStage().getHeight() - root.prefHeight(-1)) / 2));
    stage.show();
  }
}
