package legend.game.debugger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import legend.game.types.CharacterData2c;

public class CharacterEditor extends Application {
  private final int charId;
  private final CharacterData2c charData;

  public CharacterEditor(final int id, final CharacterData2c data) {
    this.charId = id;
    this.charData = data;
  }

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("character_editor.fxml"));
    final Parent root = loader.load();
    final Scene scene = new Scene(root);

    final CharacterEditorController controller = loader.getController();
    controller.setChar(this.charId, this.charData);

    stage.setTitle("Character Editor");
    stage.setScene(scene);
    stage.setX(Debugger.getStage().getX() + ((Debugger.getStage().getWidth() - root.prefWidth(-1)) / 2));
    stage.setY(Debugger.getStage().getY() + ((Debugger.getStage().getHeight() - root.prefHeight(-1)) / 2));
    stage.show();
  }
}
