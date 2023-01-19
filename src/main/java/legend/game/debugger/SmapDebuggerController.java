package legend.game.debugger;

import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import legend.game.SMap;
import legend.game.scripting.ScriptState;
import legend.game.types.SubmapObject210;

import static legend.game.SMap.sobjCount_800c6730;
import static legend.game.SMap.sobjs_800c6880;

public class SmapDebuggerController {
  @FXML
  private ListView<ListItem> sobjList;
  private final ObservableList<ListItem> sobjs = FXCollections.observableArrayList(e -> new Observable[] {e.prop});

  @FXML
  private Button scriptIndex;

  @FXML
  public CheckBox renderCollision;

  @FXML
  public Spinner<Integer> posX;
  @FXML
  public Spinner<Integer> posY;
  @FXML
  public Spinner<Integer> posZ;
  @FXML
  public Spinner<Integer> rotX;
  @FXML
  public Spinner<Integer> rotY;
  @FXML
  public Spinner<Integer> rotZ;
  @FXML
  public Spinner<Integer> scaleX;
  @FXML
  public Spinner<Integer> scaleY;
  @FXML
  public Spinner<Integer> scaleZ;

  @FXML
  public CheckBox collideByPlayer;
  @FXML
  public CheckBox collide20;
  @FXML
  public CheckBox collide40;
  @FXML
  public CheckBox collide80;
  @FXML
  public CheckBox collide100;
  @FXML
  public CheckBox collide200;
  @FXML
  public CheckBox collide400;
  @FXML
  public CheckBox collide800;
  @FXML
  public CheckBox collide1000;

  @FXML
  public CheckBox alertIcon;

  private SubmapObject210 sobj;

  public void initialize() {
    for(int i = 0; i < sobjCount_800c6730.get(); i++) {
      this.sobjs.add(new ListItem(this::getSobjName, i));
    }

    this.sobjList.setItems(this.sobjs);
    this.sobjList.setCellFactory(param -> {
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

    this.sobjList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
      final int index = newValue.intValue();
      this.displayStats(index);
    });

    this.renderCollision.setSelected(SMap.enableCollisionDebug);

    this.posX.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE));
    this.posY.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE));
    this.posZ.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE));
    this.rotX.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Short.MIN_VALUE, Short.MAX_VALUE));
    this.rotY.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Short.MIN_VALUE, Short.MAX_VALUE));
    this.rotZ.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Short.MIN_VALUE, Short.MAX_VALUE));
    this.scaleX.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE));
    this.scaleY.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE));
    this.scaleZ.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE));

    this.sobjList.getSelectionModel().select(0);
  }

  private String getSobjName(final int index) {
    final ScriptState<SubmapObject210> state = sobjs_800c6880[index];

    if(state == null) {
      return "unused";
    }

    if(index == 0) {
      return "Player";
    }

    return "Script %d".formatted(index);
  }

  private void displayStats(final int index) {
    final ScriptState<SubmapObject210> state = sobjs_800c6880[index];

    if(state == null) {
      return;
    }

    this.scriptIndex.setText("View script %d".formatted(index));

    this.sobj = state.innerStruct_00;

    this.posX.getValueFactory().setValue(this.sobj.model_00.coord2_14.coord.transfer.getX());
    this.posY.getValueFactory().setValue(this.sobj.model_00.coord2_14.coord.transfer.getY());
    this.posZ.getValueFactory().setValue(this.sobj.model_00.coord2_14.coord.transfer.getZ());
    this.rotX.getValueFactory().setValue((int)this.sobj.model_00.coord2Param_64.rotate.getX());
    this.rotY.getValueFactory().setValue((int)this.sobj.model_00.coord2Param_64.rotate.getY());
    this.rotZ.getValueFactory().setValue((int)this.sobj.model_00.coord2Param_64.rotate.getZ());
    this.scaleX.getValueFactory().setValue(this.sobj.model_00.scaleVector_fc.getX());
    this.scaleY.getValueFactory().setValue(this.sobj.model_00.scaleVector_fc.getY());
    this.scaleZ.getValueFactory().setValue(this.sobj.model_00.scaleVector_fc.getZ());

    this.collideByPlayer.setSelected((this.sobj.flags_190 & 0x10_0000) != 0);
    this.collide20.setSelected((this.sobj.flags_190 & 0x20_0000) != 0);
    this.collide40.setSelected((this.sobj.flags_190 & 0x40_0000) != 0);
    this.collide80.setSelected((this.sobj.flags_190 & 0x80_0000) != 0);
    this.collide100.setSelected((this.sobj.flags_190 & 0x100_0000) != 0);
    this.collide200.setSelected((this.sobj.flags_190 & 0x200_0000) != 0);
    this.collide400.setSelected((this.sobj.flags_190 & 0x400_0000) != 0);
    this.collide800.setSelected((this.sobj.flags_190 & 0x800_0000) != 0);
    this.collide1000.setSelected((this.sobj.flags_190 & 0x1000_0000) != 0);

    this.alertIcon.setSelected(this.sobj.showAlertIndicator_194);
  }

  public void openScriptDebugger(final ActionEvent event) throws Exception {
    if(this.sobjList.getSelectionModel().getSelectedIndex() < 0) {
      return;
    }

    final ScriptState<SubmapObject210> state = sobjs_800c6880[this.sobjList.getSelectionModel().getSelectedIndex()];

    final ScriptDebugger scriptDebugger = new ScriptDebugger();
    scriptDebugger.preselectScript(state.index).start(new Stage());
  }

  public void refreshValues(final ActionEvent event) {
    this.displayStats(this.sobjList.getSelectionModel().getSelectedIndex());
  }

  public void updatePos(final ActionEvent event) {
    if(this.sobj != null) {
      this.sobj.model_00.coord2_14.coord.transfer.setX(this.posX.getValueFactory().getValue());
      this.sobj.model_00.coord2_14.coord.transfer.setY(this.posY.getValueFactory().getValue());
      this.sobj.model_00.coord2_14.coord.transfer.setZ(this.posZ.getValueFactory().getValue());
    }
  }

  public void updateRot(final ActionEvent event) {
    if(this.sobj != null) {
      this.sobj.model_00.coord2Param_64.rotate.setX(this.rotX.getValueFactory().getValue().shortValue());
      this.sobj.model_00.coord2Param_64.rotate.setY(this.rotY.getValueFactory().getValue().shortValue());
      this.sobj.model_00.coord2Param_64.rotate.setZ(this.rotZ.getValueFactory().getValue().shortValue());
    }
  }

  public void updateScale(final ActionEvent event) {
    if(this.sobj != null) {
      this.sobj.model_00.scaleVector_fc.setX(this.scaleX.getValueFactory().getValue());
      this.sobj.model_00.scaleVector_fc.setY(this.scaleY.getValueFactory().getValue());
      this.sobj.model_00.scaleVector_fc.setZ(this.scaleZ.getValueFactory().getValue());
    }
  }

  @FXML
  public void renderCollisionClick(final ActionEvent actionEvent) {
    SMap.enableCollisionDebug = this.renderCollision.isSelected();
  }

  public void playerCollideWithClick(final ActionEvent actionEvent) {
    this.setOrClearFlag(0x10_0000L, this.collideByPlayer.isSelected());
  }

  public void collider20Click(final ActionEvent actionEvent) {
    this.setOrClearFlag(0x20_0000L, this.collide20.isSelected());
  }

  public void collider40Click(final ActionEvent actionEvent) {
    this.setOrClearFlag(0x40_0000L, this.collide40.isSelected());
  }

  public void collider80Click(final ActionEvent actionEvent) {
    this.setOrClearFlag(0x80_0000L, this.collide80.isSelected());
  }

  public void collider100Click(final ActionEvent actionEvent) {
    this.setOrClearFlag(0x100_0000L, this.collide100.isSelected());
  }

  public void collider200Click(final ActionEvent actionEvent) {
    this.setOrClearFlag(0x200_0000L, this.collide200.isSelected());
  }

  public void collider400Click(final ActionEvent actionEvent) {
    this.setOrClearFlag(0x400_0000L, this.collide400.isSelected());
  }

  public void collider800Click(final ActionEvent actionEvent) {
    this.setOrClearFlag(0x800_0000L, this.collide800.isSelected());
  }

  public void collider1000Click(final ActionEvent actionEvent) {
    this.setOrClearFlag(0x1000_0000L, this.collide1000.isSelected());
  }

  public void showAlertIconClick(final ActionEvent actionEvent) {
    if(this.sobj != null) {
      this.sobj.showAlertIndicator_194 = this.alertIcon.isSelected();
    }
  }

  private void setOrClearFlag(final long flag, final boolean selected) {
    if(this.sobj != null) {
      if(selected) {
        this.sobj.flags_190 |= flag;
      } else {
        this.sobj.flags_190 &= ~flag;
      }
    }
  }

  private static class ListItem {
    private final Int2ObjectFunction<String> nameFunc;
    private final StringProperty prop = new SimpleStringProperty(this, "name");
    private final int index;

    public ListItem(final Int2ObjectFunction<String> nameFunc, final int index) {
      this.nameFunc = nameFunc;
      this.index = index;
      this.update();
    }

    public void update() {
      this.prop.set(this.index + ": " + this.nameFunc.get(this.index));
    }

    public String getName() {
      return this.prop.get();
    }
  }
}
