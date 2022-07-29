package legend.game.debugger;

import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import legend.game.modding.events.EventListener;
import legend.game.modding.events.scripting.ScriptAllocatedEvent;
import legend.game.modding.events.scripting.ScriptDeallocatedEvent;
import legend.game.modding.events.scripting.ScriptTickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.scriptState_800bc0c0;

@EventListener
public class ScriptDebugger extends Application {
  private static final Logger LOGGER = LogManager.getFormatterLogger(ScriptDebugger.class);

  private static final Set<ScriptDebugger> INSTANCES = new HashSet<>();

  @FXML
  private ComboBox<ScriptListItem> scriptSelector;
  private final ObservableList<ScriptListItem> scripts = FXCollections.observableArrayList(e -> new Observable[] {e.prop});

  @FXML
  private ListView<ScriptListItem> scriptStorage;
  private final ObservableList<ScriptListItem> storage = FXCollections.observableArrayList(e -> new Observable[] {e.prop});

  @Override
  public void start(Stage stage) throws Exception {
    final Parent root = FXMLLoader.load(getClass().getResource("script_debugger.fxml"));
    final Scene scene = new Scene(root);
    scene.getStylesheets().add(getClass().getResource("script_debugger.css").toExternalForm());

    stage.setTitle("Script Debugger");
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void stop() throws Exception {
    INSTANCES.remove(this);

    super.stop();
  }

  public void initialize() {
    for(int i = 0; i < 0x48; i++) {
      this.scripts.add(new ScriptListItem(this::getScriptName, i));
    }

    this.scriptSelector.setItems(this.scripts);
    this.scriptSelector.setConverter(new StringConverter<>() {
      @Override
      public String toString(final ScriptListItem object) {
        return object != null ? object.getName() : null;
      }

      @Override
      public ScriptListItem fromString(final String string) {
        return null;
      }
    });
    this.scriptSelector.setValue(this.scripts.get(0));

    for(int i = 0; i < 33; i++) {
      this.storage.add(new ScriptListItem(paramIndex -> this.getScriptStorage(this.scriptSelector.getValue().index, paramIndex), i));
    }
    this.scriptStorage.setItems(this.storage);
    this.scriptStorage.setCellFactory(param -> {
      final TextFieldListCell<ScriptListItem> cell = new TextFieldListCell<>();
      cell.setConverter(this.scriptSelector.getConverter());
      return cell;
    });

    INSTANCES.add(this);
  }

  private void updateScriptName(final int scriptIndex) {
    final ScriptListItem item = this.scripts.get(scriptIndex);
    item.update();

    if(item == this.scriptSelector.getValue()) {
      this.scriptSelector.setValue(item);
    }
  }

  private String getScriptName(final int scriptIndex) {
    return scriptStatePtrArr_800bc1c0.get(scriptIndex).getPointer() != scriptState_800bc0c0.getAddress() ? Long.toHexString(scriptStatePtrArr_800bc1c0.get(scriptIndex).getPointer()) : "not allocated";
  }

  private void updateScriptStorage() {
    for(int storageIndex = 0; storageIndex < 33; storageIndex++) {
      this.storage.get(storageIndex).update();
    }
  }

  private String getScriptStorage(final int scriptIndex, final int storageIndex) {
    final int val = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().storage_44.get(storageIndex).get();
    return "0x%x (%d)".formatted(val, val);
  }

  @EventListener
  public static void onScriptAllocated(final ScriptAllocatedEvent event) {
    Platform.runLater(() -> {
      for(final ScriptDebugger instance : INSTANCES) {
        instance.updateScriptName(event.scriptIndex);
      }
    });
  }

  @EventListener
  public static void onScriptDeallocated(final ScriptDeallocatedEvent event) {
    Platform.runLater(() -> {
      for(final ScriptDebugger instance : INSTANCES) {
        instance.updateScriptName(event.scriptIndex);
      }
    });
  }

  @EventListener
  public static void onScriptTick(final ScriptTickEvent event) {
    Platform.runLater(() -> {
      for(final ScriptDebugger instance : INSTANCES) {
        if(event.scriptIndex == instance.scriptSelector.getValue().index) {
          instance.updateScriptStorage();
        }
      }
    });
  }

  private static class ScriptListItem {
    private final Int2ObjectFunction<String> nameFunc;
    private final StringProperty prop = new SimpleStringProperty(this, "name");
    private final int index;

    public ScriptListItem(final Int2ObjectFunction<String> nameFunc, final int index) {
      this.nameFunc = nameFunc;
      this.index = index;
      this.update();
    }

    public void update() {
      this.prop.set(this.index + ": " + nameFunc.get(index));
    }

    public String getName() {
      return this.prop.get();
    }
  }
}
