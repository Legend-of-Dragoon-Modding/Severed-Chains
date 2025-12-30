package legend.game.debugger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.legendofdragoon.modloader.registries.Registry;
import org.legendofdragoon.modloader.registries.RegistryEntry;

import java.util.function.Consumer;

public class RegistrySelector extends Application {
  private final Registry<?> registry;

  private final Runnable onCancel;
  private final Consumer<? extends RegistryEntry> onSelect;

  public <T extends RegistryEntry> RegistrySelector(final Registry<T> registry, final Consumer<T> onSelect, final Runnable onCancel) {
    this.registry = registry;
    this.onSelect = onSelect;
    this.onCancel = onCancel;
  }

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("registry_selector.fxml"));
    final Parent root = loader.load();
    final Scene scene = new Scene(root);
    scene.getStylesheets().add(this.getClass().getResource("registry_selector.css").toExternalForm());

    final RegistrySelectorController controller = loader.getController();
    controller.setRegistry(this.registry);
    controller.onCancel(this.onCancel);
    controller.onSelect(this.onSelect);

    stage.setTitle(this.registry.id + " Selector");
    stage.setScene(scene);
    stage.setX(Debugger.getStage().getX() + ((Debugger.getStage().getWidth() - root.prefWidth(-1)) / 2));
    stage.setY(Debugger.getStage().getY() + ((Debugger.getStage().getHeight() - root.prefHeight(-1)) / 2));
    stage.show();
  }
}
