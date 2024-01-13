package legend.game.debugger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import legend.game.submap.SubmapObject210;

public class SmapModelDebugger extends Application {
  private SmapModelDebuggerController controller;
  private SubmapObject210 sobj;

  public SmapModelDebugger setSobj(final SubmapObject210 sobj) {
    this.sobj = sobj;
    return this;
  }

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("smap_model_debugger.fxml"));
    final Parent root = loader.load();
    final Scene scene = new Scene(root);
    scene.getStylesheets().add(this.getClass().getResource("smap_model_debugger.css").toExternalForm());

    this.controller = loader.getController();
    this.controller.setSobj(this.sobj);

    stage.setOnCloseRequest(event -> {
      this.controller.uninitialize();
      this.controller = null;
    });

    stage.setTitle("Submap Model Debugger");
    stage.setScene(scene);
    stage.setX(Debugger.getStage().getX() + ((Debugger.getStage().getWidth() - root.prefWidth(-1)) / 2));
    stage.setY(Debugger.getStage().getY() + ((Debugger.getStage().getHeight() - root.prefHeight(-1)) / 2));
    stage.show();
  }
}
