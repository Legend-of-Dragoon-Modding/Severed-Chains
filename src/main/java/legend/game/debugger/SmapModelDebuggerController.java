package legend.game.debugger;

import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;
import legend.core.opengl.TmdObjLoader;
import legend.game.modding.events.RenderEvent;
import legend.game.submap.SMap;
import legend.game.submap.SubmapObject;
import legend.game.submap.SubmapObject210;
import legend.game.tmd.UvAdjustmentMetrics14;
import legend.game.types.CContainer;
import legend.game.types.Model124;
import legend.game.types.TmdAnimationFile;
import org.legendofdragoon.modloader.events.EventListener;

import static legend.core.GameEngine.EVENTS;
import static legend.game.Scus94491BpeSegment_8002.animateModel;
import static legend.game.Scus94491BpeSegment_8002.applyInterpolationFrame;
import static legend.game.Scus94491BpeSegment_8002.applyKeyframe;
import static legend.game.Scus94491BpeSegment_8002.applyModelRotationAndScale;
import static legend.game.Scus94491BpeSegment_8002.initModel;
import static legend.game.Scus94491BpeSegment_8002.loadModelStandardAnimation;
import static legend.game.Scus94491BpeSegment_8004.engineState_8004dd04;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;

public class SmapModelDebuggerController {
  @FXML
  private Label sobjName;

  @FXML
  private ListView<ListItem> animList;
  private final ObservableList<ListItem> anims = FXCollections.observableArrayList(e -> new Observable[] {e.prop});

  @FXML
  public Spinner<Double> posX;
  @FXML
  public Spinner<Double> posY;
  @FXML
  public Spinner<Double> posZ;
  @FXML
  public Spinner<Double> rotX;
  @FXML
  public Spinner<Double> rotY;
  @FXML
  public Spinner<Double> rotZ;
  @FXML
  public Spinner<Double> scaleX;
  @FXML
  public Spinner<Double> scaleY;
  @FXML
  public Spinner<Double> scaleZ;

  @FXML
  private Label frames;
  @FXML
  private Slider frameSlider;

  @FXML
  private Label subframes;
  @FXML
  private Slider subframeSlider;

  @FXML
  private CheckBox paused;

  private SMap smap;
  private Model124 model;
  private boolean delete;
  private boolean destroy;
  private boolean freezeUpdates;

  public void initialize() {
    EVENTS.register(this);
  }

  public void setSobj(final SubmapObject210 sobj) {
    this.smap = ((SMap)engineState_8004dd04);

    this.model = new Model124("Animation viewer");
    this.model.coord2_14.coord.transfer.set(this.smap.sobjs_800c6880[0].innerStruct_00.model_00.coord2_14.coord.transfer);
    this.model.coord2_14.transforms.set(this.smap.sobjs_800c6880[0].innerStruct_00.model_00.coord2_14.transforms);

    this.sobjName.setText(sobj.model_00.name);
    this.anims.clear();

    final SubmapObject assets = this.smap.submap.objects.get(sobj.sobjIndex_12e);

    for(int i = 0; i < assets.animations.size(); i++) {
      this.anims.add(new ListItem(Integer::toString, i));
    }

    this.animList.setItems(this.anims);
    this.animList.setCellFactory(param -> {
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

    this.animList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
      final int index = newValue.intValue();
      this.loadAnimation(assets.model, assets.animations.get(index), sobj.model_00.uvAdjustments_9d);
    });

    this.animList.getSelectionModel().select(0);

    this.posX.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-Float.MAX_VALUE, Float.MAX_VALUE));
    this.posY.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-Float.MAX_VALUE, Float.MAX_VALUE));
    this.posZ.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-Float.MAX_VALUE, Float.MAX_VALUE));
    this.rotX.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-Float.MAX_VALUE, Float.MAX_VALUE));
    this.rotY.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-Float.MAX_VALUE, Float.MAX_VALUE));
    this.rotZ.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-Float.MAX_VALUE, Float.MAX_VALUE));
    this.scaleX.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-Float.MAX_VALUE, Float.MAX_VALUE));
    this.scaleY.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-Float.MAX_VALUE, Float.MAX_VALUE));
    this.scaleZ.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-Float.MAX_VALUE, Float.MAX_VALUE));

    this.posX.getValueFactory().setValue((double)this.model.coord2_14.coord.transfer.x);
    this.posY.getValueFactory().setValue((double)this.model.coord2_14.coord.transfer.y);
    this.posZ.getValueFactory().setValue((double)this.model.coord2_14.coord.transfer.z);
    this.rotX.getValueFactory().setValue((double)(this.model.coord2_14.transforms.rotate.x));
    this.rotY.getValueFactory().setValue((double)(this.model.coord2_14.transforms.rotate.y));
    this.rotZ.getValueFactory().setValue((double)(this.model.coord2_14.transforms.rotate.z));
    this.scaleX.getValueFactory().setValue((double)(this.model.coord2_14.transforms.scale.x));
    this.scaleY.getValueFactory().setValue((double)(this.model.coord2_14.transforms.scale.y));
    this.scaleZ.getValueFactory().setValue((double)(this.model.coord2_14.transforms.scale.z));

    this.frameSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
      if(!this.freezeUpdates) {
        this.model.currentKeyframe_94 = newValue.intValue();
        applyKeyframe(this.model);
      }
    });

    this.subframeSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
      if(!this.freezeUpdates) {
        this.model.subFrameIndex = newValue.intValue();
        applyInterpolationFrame(this.model, 4 / vsyncMode_8007a3b8);
      }
    });
  }

  public void uninitialize() {
    synchronized(this) {
      this.smap = null;

      this.delete = true;
      this.destroy = true;
    }
  }

  private void loadAnimation(final CContainer tmd, final TmdAnimationFile animation, final UvAdjustmentMetrics14 uvAdjustments) {
    synchronized(this) {
      this.freezeUpdates = true;
      this.model.uvAdjustments_9d = UvAdjustmentMetrics14.NONE;
      initModel(this.model, tmd, animation);
      loadModelStandardAnimation(this.model, animation);
      this.model.animationState_9c = this.paused.isSelected() ? 2 : 1;
      this.frameSlider.setMax(this.model.totalFrames_9a / 2 - 1);
      this.subframeSlider.setMax(4 / vsyncMode_8007a3b8 - 1);
      this.delete = true;
      this.freezeUpdates = false;
    }
  }

  @EventListener
  public void renderModel(final RenderEvent event) {
    synchronized(this) {
      if(this.delete) {
        this.model.deleteModelParts();
        this.delete = false;
      }

      if(this.destroy) {
        this.model = null;
        this.destroy = false;
      }

      if(this.model != null) {
        if(this.model.modelParts_00[0].obj == null) {
          TmdObjLoader.fromModel("Animation viewer", this.model);
        }

        animateModel(this.model, 4 / vsyncMode_8007a3b8);
        applyModelRotationAndScale(this.model);
        this.smap.renderSmapModel(this.model, null);

        Platform.runLater(() -> {
          this.freezeUpdates = true;

          this.frames.setText(this.model.currentKeyframe_94 + "/" + (this.model.totalFrames_9a / 2 - 1));
          this.subframes.setText(this.model.subFrameIndex + "/" + (4 / vsyncMode_8007a3b8 - 1));

          if(this.model.animationState_9c == 1) {
            this.frameSlider.setValue(this.model.currentKeyframe_94);
            this.subframeSlider.setValue(this.model.subFrameIndex);
          }

          this.freezeUpdates = false;
        });
      }
    }
  }

  public void updatePos(final ActionEvent event) {
    if(this.model != null) {
      this.model.coord2_14.coord.transfer.x = this.posX.getValueFactory().getValue().floatValue();
      this.model.coord2_14.coord.transfer.y = this.posY.getValueFactory().getValue().floatValue();
      this.model.coord2_14.coord.transfer.z = this.posZ.getValueFactory().getValue().floatValue();
    }
  }

  public void updateRot(final ActionEvent event) {
    if(this.model != null) {
      this.model.coord2_14.transforms.rotate.x = this.rotX.getValueFactory().getValue().floatValue();
      this.model.coord2_14.transforms.rotate.y = this.rotY.getValueFactory().getValue().floatValue();
      this.model.coord2_14.transforms.rotate.z = this.rotZ.getValueFactory().getValue().floatValue();
    }
  }

  public void updateScale(final ActionEvent event) {
    if(this.model != null) {
      this.model.coord2_14.transforms.scale.x = this.scaleX.getValueFactory().getValue().floatValue();
      this.model.coord2_14.transforms.scale.y = this.scaleY.getValueFactory().getValue().floatValue();
      this.model.coord2_14.transforms.scale.z = this.scaleZ.getValueFactory().getValue().floatValue();
    }
  }

  public void togglePause(final ActionEvent event) {
    this.model.animationState_9c = this.paused.isSelected() ? 2 : 1;
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
