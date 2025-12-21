package legend.game.debugger;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import legend.game.i18n.I18n;
import org.legendofdragoon.modloader.registries.Registry;
import org.legendofdragoon.modloader.registries.RegistryEntry;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Comparator;
import java.util.function.Consumer;

public class RegistrySelectorController {
  @FXML
  private ListView<ListItem> entryList;
  private final ObservableList<ListItem> entries = FXCollections.observableArrayList(e -> new Observable[] {e.prop});

  @FXML
  private Label registryName;

  private Runnable onCancel = () -> {};
  private Consumer<? extends RegistryEntry> onSelect = id -> {};

  private Registry<? extends RegistryEntry> registry;
  private RegistryId selected;

  public void onCancel(final Runnable callback) {
    this.onCancel = callback;
  }

  public void onSelect(final Consumer<? extends RegistryEntry> callback) {
    this.onSelect = callback;
  }

  public void setRegistry(final Registry<?> registry) {
    this.registry = registry;
    this.registryName.setText(registry.id.toString());
    this.entries.clear();

    for(final RegistryId id : registry) {
      this.entries.add(new ListItem(registry, id));
    }

    this.entries.sort(Comparator.comparing(i -> i.id.toString()));

    this.entryList.setItems(this.entries);
    this.entryList.setCellFactory(param -> {
      final TextFieldListCell<ListItem> cell = new TextFieldListCell<>();
      cell.setConverter(new StringConverter<>() {
        @Override
        public String toString(final ListItem object) {
          return object != null ? object.getName() : null;
        }

        @Override
        public ListItem fromString(final String string) {
          return null;
        }
      });
      return cell;
    });

    this.entryList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      this.selected = newValue.id;
    });

    this.entryList.getSelectionModel().select(0);
  }

  public void onCancelClick() {
    this.onCancel.run();
    ((Stage)this.registryName.getScene().getWindow()).close();
  }

  public void onSelectClick() {
    //noinspection rawtypes,unchecked
    ((Consumer)this.onSelect).accept(this.registry.getEntry(this.selected).get());
    ((Stage)this.registryName.getScene().getWindow()).close();
  }

  public void onKeyPressed(final KeyEvent keyEvent) {
    if(keyEvent.getCode() == KeyCode.ENTER) {
      this.onSelectClick();
    }
  }

  public void onListClicked(final MouseEvent mouseEvent) {
    if(mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
      this.onSelectClick();
    }
  }

  private static class ListItem {
    private final Registry<?> registry;
    private final RegistryId id;
    private final StringProperty prop = new SimpleStringProperty(this, "name");

    public ListItem(final Registry<?> registry, final RegistryId id) {
      this.registry = registry;
      this.id = id;
      this.update();
    }

    public void update() {
      this.prop.set(this.id.toString() + " - " + I18n.translate(this.registry.getEntry(this.id)));
    }

    public String getName() {
      return this.prop.get();
    }
  }
}
