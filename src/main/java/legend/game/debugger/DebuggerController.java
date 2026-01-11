package legend.game.debugger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import legend.core.Config;
import legend.game.combat.AdditionMode;
import legend.game.combat.SEffe;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.config.ConfigLoadedEvent;
import legend.game.modding.events.config.ConfigUpdatedEvent;
import legend.game.submap.SMap;
import legend.game.submap.SubmapState;
import legend.game.types.GsRVIEW2;
import legend.game.wmap.DirectionalPathSegmentData08;
import legend.game.wmap.WMap;
import legend.game.wmap.WmapState;
import org.legendofdragoon.modloader.events.EventListener;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.Graphics.GsSetRefView2L;
import static legend.game.Graphics.GsSetSmapRefView2L;
import static legend.game.Graphics.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_800b.battleStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.combat.SBtld.startLegacyEncounter;
import static legend.game.wmap.WmapStatics.directionalPathSegmentData_800f2248;

public class DebuggerController {
  @FXML
  private MenuItem menuDebuggersScript;
  @FXML
  private MenuItem menuDebuggersCombat;
  @FXML
  private MenuItem menuDebuggersSubmap;
  @FXML
  private MenuItem menuDebuggersGameStateEditor;
  @FXML
  private MenuItem menuDebuggersGameStateViewer;

  @FXML
  public Spinner<Integer> encounterId;
  @FXML
  public Button getEncounterId;
  @FXML
  public Button startEncounter;

  @FXML
  public Spinner<Integer> mapId;
  @FXML
  public Button getMapId;
  @FXML
  public Button warpToMap;

  @FXML
  public Spinner<Integer> vsyncMode;
  @FXML
  public Button getVsyncMode;
  @FXML
  public Button setVsyncMode;

  @FXML
  public Spinner<Integer> gameSpeedMultiplier;
  @FXML
  public Button getGameSpeedMultiplier;
  @FXML
  public Button setGameSpeedMultiplier;

  @FXML
  public CheckBox additionOverlayColour;
  @FXML
  public Spinner<Integer> additionOverlayR;
  @FXML
  public Spinner<Integer> additionOverlayG;
  @FXML
  public Spinner<Integer> additionOverlayB;
  @FXML
  public Spinner<Integer> counterOverlayR;
  @FXML
  public Spinner<Integer> counterOverlayG;
  @FXML
  public Spinner<Integer> counterOverlayB;
  @FXML
  public Spinner<Integer> combatStageId;
  @FXML
  public CheckBox saveAnywhere;
  @FXML
  public CheckBox autoAddition;
  @FXML
  public CheckBox autoMeter;
  @FXML
  public CheckBox disableStatusEffects;
  @FXML
  public CheckBox combatStage;
  @FXML
  public CheckBox fastTextSpeed;
  @FXML
  public CheckBox autoAdvanceText;

  @FXML
  public Spinner<Double> viewpointX;
  @FXML
  public Spinner<Double> viewpointY;
  @FXML
  public Spinner<Double> viewpointZ;
  @FXML
  public Spinner<Double> refpointX;
  @FXML
  public Spinner<Double> refpointY;
  @FXML
  public Spinner<Double> refpointZ;

  public void initialize() {
    this.encounterId.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
    this.mapId.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
    this.vsyncMode.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
    this.gameSpeedMultiplier.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 16, Config.getGameSpeedMultiplier()));
    this.saveAnywhere.setSelected(CONFIG.getConfig(CoreMod.SAVE_ANYWHERE_CONFIG.get()));
    this.additionOverlayColour.setSelected(Config.changeAdditionOverlayRgb());
    this.additionOverlayR.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, (Config.getAdditionOverlayRgb() & 0xff)));
    this.additionOverlayG.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getAdditionOverlayRgb() >> 8) & 0xff)));
    this.additionOverlayB.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getAdditionOverlayRgb() >> 16) & 0xff)));
    this.counterOverlayR.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, (Config.getCounterOverlayRgb() & 0xff)));
    this.counterOverlayG.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getCounterOverlayRgb() >> 8) & 0xff)));
    this.counterOverlayB.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getCounterOverlayRgb() >> 16) & 0xff)));
    this.autoAddition.setSelected(CONFIG.getConfig(CoreMod.ADDITION_MODE_CONFIG.get()) == AdditionMode.AUTOMATIC);
    this.autoMeter.setSelected(CONFIG.getConfig(CoreMod.AUTO_DRAGOON_ADDITION_CONFIG.get()));
    this.disableStatusEffects.setSelected(CONFIG.getConfig(CoreMod.DISABLE_STATUS_EFFECTS_CONFIG.get()));
    this.combatStage.setSelected(Config.combatStage());
    this.combatStageId.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 127, Config.getCombatStage()));
    this.fastTextSpeed.setSelected(CONFIG.getConfig(CoreMod.QUICK_TEXT_CONFIG.get()));
    this.autoAdvanceText.setSelected(CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get()));

    this.viewpointX.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-Float.MAX_VALUE, Float.MAX_VALUE, 0.0f));
    this.viewpointY.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-Float.MAX_VALUE, Float.MAX_VALUE, 0.0f));
    this.viewpointZ.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-Float.MAX_VALUE, Float.MAX_VALUE, 0.0f));
    this.refpointX.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-Float.MAX_VALUE, Float.MAX_VALUE, 0.0f));
    this.refpointY.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-Float.MAX_VALUE, Float.MAX_VALUE, 0.0f));
    this.refpointZ.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-Float.MAX_VALUE, Float.MAX_VALUE, 0.0f));

    EVENTS.register(this);
  }

  @EventListener
  public void configLoaded(final ConfigLoadedEvent event) {
    this.saveAnywhere.setSelected(CONFIG.getConfig(CoreMod.SAVE_ANYWHERE_CONFIG.get()));
    this.fastTextSpeed.setSelected(CONFIG.getConfig(CoreMod.QUICK_TEXT_CONFIG.get()));
    this.autoAdvanceText.setSelected(CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get()));
    this.autoAddition.setSelected(CONFIG.getConfig(CoreMod.ADDITION_MODE_CONFIG.get()) == AdditionMode.AUTOMATIC);
    this.autoMeter.setSelected(CONFIG.getConfig(CoreMod.AUTO_DRAGOON_ADDITION_CONFIG.get()));
    this.disableStatusEffects.setSelected(CONFIG.getConfig(CoreMod.DISABLE_STATUS_EFFECTS_CONFIG.get()));
  }

  @EventListener
  public void configUpdated(final ConfigUpdatedEvent event) {
    if(event.config == CoreMod.SAVE_ANYWHERE_CONFIG.get()) {
      this.saveAnywhere.setSelected(CONFIG.getConfig(CoreMod.SAVE_ANYWHERE_CONFIG.get()));
    }

    if(event.config == CoreMod.QUICK_TEXT_CONFIG.get()) {
      this.fastTextSpeed.setSelected(CONFIG.getConfig(CoreMod.QUICK_TEXT_CONFIG.get()));
    }

    if(event.config == CoreMod.AUTO_TEXT_CONFIG.get()) {
      this.autoAdvanceText.setSelected(CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get()));
    }

    if(event.config == CoreMod.ADDITION_MODE_CONFIG.get()) {
      this.autoAddition.setSelected(CONFIG.getConfig(CoreMod.ADDITION_MODE_CONFIG.get()) == AdditionMode.AUTOMATIC);
    }

    if(event.config == CoreMod.AUTO_DRAGOON_ADDITION_CONFIG.get()) {
      this.autoMeter.setSelected(CONFIG.getConfig(CoreMod.AUTO_DRAGOON_ADDITION_CONFIG.get()));
    }

    if(event.config == CoreMod.DISABLE_STATUS_EFFECTS_CONFIG.get()) {
      this.disableStatusEffects.setSelected(CONFIG.getConfig(CoreMod.DISABLE_STATUS_EFFECTS_CONFIG.get()));
    }
  }

  @FXML
  private void showScriptDebugger(final ActionEvent event) throws Exception {
    new ScriptDebugger().start(new Stage());
  }

  @FXML
  private void showCombatDebugger(final ActionEvent event) throws Exception {
    new CombatDebugger().start(new Stage());
  }

  @FXML
  private void showSubmapDebugger(final ActionEvent event) throws Exception {
    new SmapDebugger().start(new Stage());
  }

  @FXML
  private void showGameStateEditor(final ActionEvent event) throws Exception {
    new GameStateEditor().start(new Stage());
  }

  @FXML
  private void showGameStateViewer(final ActionEvent event) throws Exception {
    new GameStateViewer().start(new Stage());
  }

  @FXML
  private void getEncounterId(final ActionEvent event) {
    this.encounterId.getValueFactory().setValue(encounterId_800bb0f8);
  }

  @FXML
  private void startEncounter(final ActionEvent event) {
    if(currentEngineState_8004dd04 instanceof final SMap smap) {
      smap.submap.prepareEncounter(this.encounterId.getValue(), false);
      smap.mapTransition(-1, 0);
    } else if(currentEngineState_8004dd04 instanceof final WMap wmap) {
      final int encounterId = this.encounterId.getValue();
      final DirectionalPathSegmentData08 directionalPathSegment = directionalPathSegmentData_800f2248[wmap.mapState_800c6798.directionalPathIndex_12];

      final int stageId;
      if(Config.combatStage()) {
        stageId = Config.getCombatStage();
      } else if(directionalPathSegment.battleStage_04 == -1) {
        stageId = 1;
      } else {
        stageId = directionalPathSegment.battleStage_04;
      }

      startLegacyEncounter(encounterId, stageId);

      gameState_800babc8.directionalPathIndex_4de = wmap.mapState_800c6798.directionalPathIndex_12;
      gameState_800babc8.pathIndex_4d8 = wmap.mapState_800c6798.pathIndex_14;
      gameState_800babc8.dotIndex_4da = wmap.mapState_800c6798.dotIndex_16;
      gameState_800babc8.dotOffset_4dc = wmap.mapState_800c6798.dotOffset_18;
      gameState_800babc8.facing_4dd = wmap.mapState_800c6798.facing_1c;
      wmap.wmapState_800bb10c = WmapState.TRANSITION_TO_ENGINE_STATE;
    }
  }

  @FXML
  private void getMapId(final ActionEvent event) {
    this.mapId.getValueFactory().setValue(submapCut_80052c30);
  }

  @FXML
  private void warpToMap(final ActionEvent event) {
    submapCut_80052c30 = this.mapId.getValue();
    ((SMap)currentEngineState_8004dd04).smapLoadingStage_800cb430 = SubmapState.CHANGE_SUBMAP_4;
  }

  @FXML
  private void getVsyncMode(final ActionEvent event) {
    this.vsyncMode.getValueFactory().setValue(vsyncMode_8007a3b8);
  }

  @FXML
  private void setVsyncMode(final ActionEvent event) {
    vsyncMode_8007a3b8 = this.vsyncMode.getValue();
  }

  @FXML
  private void getGameSpeedMultiplier(final ActionEvent event) {
    this.gameSpeedMultiplier.getValueFactory().setValue(Config.getGameSpeedMultiplier());
  }

  @FXML
  private void setGameSpeedMultiplier(final ActionEvent event) {
    Config.setGameSpeedMultiplier(this.gameSpeedMultiplier.getValue());
    Config.setLoadedGameSpeedMultiplier(this.gameSpeedMultiplier.getValue());
  }

  @FXML
  private void toggleSaveAnywhere(final ActionEvent event) {
    CONFIG.setConfig(CoreMod.SAVE_ANYWHERE_CONFIG.get(), !CONFIG.getConfig(CoreMod.SAVE_ANYWHERE_CONFIG.get()));
  }

  @FXML
  private void toggleAdditionOverlayColour() {
    Config.toggleAdditionOverlayColour();
  }

  @FXML
  private void getAdditionOverlayRgb(final ActionEvent event) {
    this.additionOverlayR.getValueFactory().setValue(SEffe.additionBorderColours_800fb7f0[9]);
    this.additionOverlayG.getValueFactory().setValue(SEffe.additionBorderColours_800fb7f0[10]);
    this.additionOverlayB.getValueFactory().setValue(SEffe.additionBorderColours_800fb7f0[11]);
  }

  @FXML
  private void setAdditionOverlayRgb(final ActionEvent event) {
    final byte[] rgbArray = {
      this.additionOverlayR.getValueFactory().getValue().byteValue(),
      this.additionOverlayG.getValueFactory().getValue().byteValue(),
      this.additionOverlayB.getValueFactory().getValue().byteValue(),
    };

    final int rgb =
      (0xff & rgbArray[2]) << 16 |
      (0xff & rgbArray[1]) << 8 |
      0xff & rgbArray[0];

    Config.setAdditionOverlayRgb(rgb);
    SEffe.additionBorderColours_800fb7f0[9] = rgbArray[0] & 0xff;
    SEffe.additionBorderColours_800fb7f0[10] = rgbArray[1] & 0xff;
    SEffe.additionBorderColours_800fb7f0[11] = rgbArray[2] & 0xff;
    this.additionOverlayColour.setSelected(true);
  }

  @FXML
  private void getCounterOverlayRgb(final ActionEvent event) {
    this.counterOverlayR.getValueFactory().setValue(SEffe.additionBorderColours_800fb7f0[6]);
    this.counterOverlayG.getValueFactory().setValue(SEffe.additionBorderColours_800fb7f0[7]);
    this.counterOverlayB.getValueFactory().setValue(SEffe.additionBorderColours_800fb7f0[8]);
  }

  @FXML
  private void setCounterOverlayRgb(final ActionEvent event) {
    final byte[] rgbArray = {
      this.counterOverlayR.getValueFactory().getValue().byteValue(),
      this.counterOverlayG.getValueFactory().getValue().byteValue(),
      this.counterOverlayB.getValueFactory().getValue().byteValue(),
    };

    final int rgb =
      (0xff & rgbArray[2]) << 16 |
      (0xff & rgbArray[1]) << 8 |
      0xff & rgbArray[0];

    Config.setCounterOverlayRgb(rgb);
    SEffe.additionBorderColours_800fb7f0[6] = rgbArray[0] & 0xff;
    SEffe.additionBorderColours_800fb7f0[7] = rgbArray[1] & 0xff;
    SEffe.additionBorderColours_800fb7f0[8] = rgbArray[2] & 0xff;
    this.additionOverlayColour.setSelected(true);
  }

  @FXML
  private void toggleAutoAddition(final ActionEvent event) {
    final boolean autoAddition = CONFIG.getConfig(CoreMod.ADDITION_MODE_CONFIG.get()) == AdditionMode.AUTOMATIC;
    CONFIG.setConfig(CoreMod.ADDITION_MODE_CONFIG.get(), autoAddition ? AdditionMode.NORMAL : AdditionMode.AUTOMATIC);
  }

  @FXML
  private void toggleAutoDragoonMeter(final ActionEvent event) {
    CONFIG.setConfig(CoreMod.AUTO_DRAGOON_ADDITION_CONFIG.get(), !CONFIG.getConfig(CoreMod.AUTO_DRAGOON_ADDITION_CONFIG.get()));
  }

  @FXML
  private void toggleDisableStatusEffects(final ActionEvent event) {
    CONFIG.setConfig(CoreMod.DISABLE_STATUS_EFFECTS_CONFIG.get(), !CONFIG.getConfig(CoreMod.DISABLE_STATUS_EFFECTS_CONFIG.get()));
  }

  @FXML
  private void toggleCombatStage(final ActionEvent event) {
    Config.toggleCombatStage();
  }

  @FXML
  private void getCombatStageId(final ActionEvent event) {
    this.combatStageId.getValueFactory().setValue(battleStage_800bb0f4);
  }

  @FXML
  private void setCombatStageId(final ActionEvent event) {
    Config.setCombatStage(this.combatStageId.getValue());
  }

  @FXML
  private void toggleFastText(final ActionEvent event) {
    CONFIG.setConfig(CoreMod.QUICK_TEXT_CONFIG.get(), !CONFIG.getConfig(CoreMod.QUICK_TEXT_CONFIG.get()));
  }

  @FXML
  private void toggleAutoAdvanceText(final ActionEvent event) {
    CONFIG.setConfig(CoreMod.AUTO_TEXT_CONFIG.get(), !CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get()));
  }

  @FXML
  private void getViewpoint() {
    final GsRVIEW2 camera = currentEngineState_8004dd04.getCamera();

    if(camera != null) {
      this.viewpointX.getValueFactory().setValue((double)camera.viewpoint_00.x);
      this.viewpointY.getValueFactory().setValue((double)camera.viewpoint_00.y);
      this.viewpointZ.getValueFactory().setValue((double)camera.viewpoint_00.z);
    }
  }

  @FXML
  private void setViewpoint() {
    final GsRVIEW2 camera = currentEngineState_8004dd04.getCamera();

    if(camera != null) {
      camera.viewpoint_00.x = (float)(double)this.viewpointX.getValueFactory().getValue();
      camera.viewpoint_00.y = (float)(double)this.viewpointY.getValueFactory().getValue();
      camera.viewpoint_00.z = (float)(double)this.viewpointZ.getValueFactory().getValue();

      if(currentEngineState_8004dd04 instanceof SMap) {
        GsSetSmapRefView2L(camera);
      } else {
        GsSetRefView2L(camera);
      }
    }
  }

  @FXML
  private void getRefpoint() {
    final GsRVIEW2 camera = currentEngineState_8004dd04.getCamera();

    if(camera != null) {
      this.refpointX.getValueFactory().setValue((double)camera.refpoint_0c.x);
      this.refpointY.getValueFactory().setValue((double)camera.refpoint_0c.y);
      this.refpointZ.getValueFactory().setValue((double)camera.refpoint_0c.z);
    }
  }

  @FXML
  private void setRefpoint() {
    final GsRVIEW2 camera = currentEngineState_8004dd04.getCamera();

    if(camera != null) {
      camera.refpoint_0c.x = (float)(double)this.refpointX.getValueFactory().getValue();
      camera.refpoint_0c.y = (float)(double)this.refpointY.getValueFactory().getValue();
      camera.refpoint_0c.z = (float)(double)this.refpointZ.getValueFactory().getValue();

      if(currentEngineState_8004dd04 instanceof SMap) {
        GsSetSmapRefView2L(camera);
      } else {
        GsSetRefView2L(camera);
      }
    }
  }
}
