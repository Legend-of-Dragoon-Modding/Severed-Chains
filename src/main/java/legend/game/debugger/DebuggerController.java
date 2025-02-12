package legend.game.debugger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
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
import legend.game.wmap.DirectionalPathSegmentData08;
import legend.game.wmap.WMap;
import legend.game.wmap.WmapState;
import org.legendofdragoon.modloader.events.EventListener;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.battleStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
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
  public CheckBox battleUiColour;
  @FXML
  public Spinner<Integer> battleUiColourR;
  @FXML
  public Spinner<Integer> battleUiColourG;
  @FXML
  public Spinner<Integer> battleUiColourB;
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
  public CheckBox textBoxColour;
  @FXML
  public RadioButton textBoxBase;
  @FXML
  public RadioButton textBoxHorizontal;
  @FXML
  public RadioButton textBoxVertical;
  @FXML
  public Spinner<Integer> textBox1R;
  @FXML
  public Spinner<Integer> textBox1G;
  @FXML
  public Spinner<Integer> textBox1B;
  @FXML
  public Spinner<Integer> textBox2R;
  @FXML
  public Spinner<Integer> textBox2G;
  @FXML
  public Spinner<Integer> textBox2B;
  @FXML
  public Spinner<Integer> textBox3R;
  @FXML
  public Spinner<Integer> textBox3G;
  @FXML
  public Spinner<Integer> textBox3B;
  @FXML
  public Spinner<Integer> textBox4R;
  @FXML
  public Spinner<Integer> textBox4G;
  @FXML
  public Spinner<Integer> textBox4B;
  @FXML
  public Spinner<Integer> textBox5R;
  @FXML
  public Spinner<Integer> textBox5G;
  @FXML
  public Spinner<Integer> textBox5B;
  @FXML
  public Spinner<Integer> textBox6R;
  @FXML
  public Spinner<Integer> textBox6G;
  @FXML
  public Spinner<Integer> textBox6B;
  @FXML
  public Spinner<Integer> textBox7R;
  @FXML
  public Spinner<Integer> textBox7G;
  @FXML
  public Spinner<Integer> textBox7B;
  @FXML
  public Spinner<Integer> textBox8R;
  @FXML
  public Spinner<Integer> textBox8G;
  @FXML
  public Spinner<Integer> textBox8B;
  @FXML
  public Spinner<Integer> textBoxTransparencyMode;

  public void initialize() {
    this.encounterId.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
    this.mapId.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
    this.vsyncMode.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
    this.gameSpeedMultiplier.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 16, Config.getGameSpeedMultiplier()));
    this.battleUiColour.setSelected(Config.changeBattleRgb());
    this.saveAnywhere.setSelected(CONFIG.getConfig(CoreMod.SAVE_ANYWHERE_CONFIG.get()));
    this.battleUiColourR.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, (int)(Config.getBattleRgb().x * 255)));
    this.battleUiColourG.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, (int)(Config.getBattleRgb().y * 255)));
    this.battleUiColourB.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, (int)(Config.getBattleRgb().z * 255)));
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
    this.textBoxColour.setSelected(Config.textBoxColour());
    this.textBox1R.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, (Config.getTextBoxRgb(0) & 0xff)));
    this.textBox1G.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getTextBoxRgb(0) >> 8) & 0xff)));
    this.textBox1B.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getTextBoxRgb(0) >> 16) & 0xff)));
    this.textBox2R.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, (Config.getTextBoxRgb(1) & 0xff)));
    this.textBox2G.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getTextBoxRgb(1) >> 8) & 0xff)));
    this.textBox2B.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getTextBoxRgb(1) >> 16) & 0xff)));
    this.textBox3R.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, (Config.getTextBoxRgb(2) & 0xff)));
    this.textBox3G.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getTextBoxRgb(2) >> 8) & 0xff)));
    this.textBox3B.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getTextBoxRgb(2) >> 16) & 0xff)));
    this.textBox4R.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, (Config.getTextBoxRgb(3) & 0xff)));
    this.textBox4G.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getTextBoxRgb(3) >> 8) & 0xff)));
    this.textBox4B.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getTextBoxRgb(3) >> 16) & 0xff)));
    this.textBox5R.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, (Config.getTextBoxRgb(4) & 0xff)));
    this.textBox5G.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getTextBoxRgb(4) >> 8) & 0xff)));
    this.textBox5B.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getTextBoxRgb(4) >> 16) & 0xff)));
    this.textBox6R.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, (Config.getTextBoxRgb(5) & 0xff)));
    this.textBox6G.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getTextBoxRgb(5) >> 8) & 0xff)));
    this.textBox6B.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getTextBoxRgb(5) >> 16) & 0xff)));
    this.textBox7R.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, (Config.getTextBoxRgb(6) & 0xff)));
    this.textBox7G.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getTextBoxRgb(6) >> 8) & 0xff)));
    this.textBox7B.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getTextBoxRgb(6) >> 16) & 0xff)));
    this.textBox8R.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, (Config.getTextBoxRgb(7) & 0xff)));
    this.textBox8G.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getTextBoxRgb(7) >> 8) & 0xff)));
    this.textBox8B.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getTextBoxRgb(7) >> 16) & 0xff)));
    this.textBoxTransparencyMode.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, Config.getTextBoxTransparencyMode()));

    if(Config.getTextBoxColourMode() == 0) {
      this.textBoxBase.setSelected(true);
      this.textBoxHorizontal.setSelected(false);
      this.textBoxVertical.setSelected(false);
    } else if(Config.getTextBoxColourMode() == 1) {
      this.textBoxBase.setSelected(false);
      this.textBoxHorizontal.setSelected(true);
      this.textBoxVertical.setSelected(false);
    } else if(Config.getTextBoxColourMode() == 2) {
      this.textBoxBase.setSelected(false);
      this.textBoxHorizontal.setSelected(false);
      this.textBoxVertical.setSelected(true);
    }

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
      encounterId_800bb0f8 = this.encounterId.getValue();
      final DirectionalPathSegmentData08 directionalPathSegment = directionalPathSegmentData_800f2248[wmap.mapState_800c6798.directionalPathIndex_12];

      if(Config.combatStage()) {
        battleStage_800bb0f4 = Config.getCombatStage();
      } else {
        if(directionalPathSegment.battleStage_04 == -1) {
          battleStage_800bb0f4 = 1;
        } else {
          battleStage_800bb0f4 = directionalPathSegment.battleStage_04;
        }
      }

      gameState_800babc8.directionalPathIndex_4de = wmap.mapState_800c6798.directionalPathIndex_12;
      gameState_800babc8.pathIndex_4d8 = wmap.mapState_800c6798.pathIndex_14;
      gameState_800babc8.dotIndex_4da = wmap.mapState_800c6798.dotIndex_16;
      gameState_800babc8.dotOffset_4dc = wmap.mapState_800c6798.dotOffset_18;
      gameState_800babc8.facing_4dd = wmap.mapState_800c6798.facing_1c;
      wmap.wmapState_800bb10c = WmapState.TRANSITION_TO_BATTLE_8;
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
  private void toggleBattleUiColour(final ActionEvent event) {
    Config.toggleBattleUiColour();
  }

  @FXML
  private void setBattleUiRgb(final ActionEvent event) {
    final int rgb = ((this.battleUiColourR.getValueFactory().getValue().byteValue() & 0xff) << 16) |
      ((this.battleUiColourG.getValueFactory().getValue().byteValue() & 0xff) << 8) |
      ((this.battleUiColourB.getValueFactory().getValue().byteValue() & 0xff));

    Config.setBattleRgb(rgb);
    this.battleUiColour.setSelected(true);
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
  private void toggleTextBoxColour(final ActionEvent event) {
    Config.toggleTextBoxColour();
  }

  @FXML
  private void setTextBoxRadio(final ActionEvent event) {
    if((event.toString().contains("textBoxBase"))) {
      this.textBoxBase.setSelected(true);
      this.textBoxHorizontal.setSelected(false);
      this.textBoxVertical.setSelected(false);
      Config.setTextBoxColourMode(0);
    } else if (event.toString().contains("textBoxHorizontal")) {
      this.textBoxBase.setSelected(false);
      this.textBoxHorizontal.setSelected(true);
      this.textBoxVertical.setSelected(false);
      Config.setTextBoxColourMode(1);
    } else if (event.toString().contains("textBoxVertical")) {
      this.textBoxBase.setSelected(false);
      this.textBoxHorizontal.setSelected(false);
      this.textBoxVertical.setSelected(true);
      Config.setTextBoxColourMode(2);
    }
  }

  @FXML
  private void setTextBox1() {
    final byte[] rgbArray = {
      this.textBox1R.getValueFactory().getValue().byteValue(),
      this.textBox1G.getValueFactory().getValue().byteValue(),
      this.textBox1B.getValueFactory().getValue().byteValue(),
      (byte)0x00,
    };

    final int rgb =
      (0xff & rgbArray[3]) << 24 |
        (0xff & rgbArray[2]) << 16 |
        (0xff & rgbArray[1]) << 8 |
        0xff & rgbArray[0];

    Config.setTextBoxRgb(0, rgb);
  }

  @FXML
  private void setTextBox2() {
    final byte[] rgbArray = {
      this.textBox2R.getValueFactory().getValue().byteValue(),
      this.textBox2G.getValueFactory().getValue().byteValue(),
      this.textBox2B.getValueFactory().getValue().byteValue(),
      (byte)0x00,
    };

    final int rgb =
      (0xff & rgbArray[3]) << 24 |
        (0xff & rgbArray[2]) << 16 |
        (0xff & rgbArray[1]) << 8 |
        0xff & rgbArray[0];

    Config.setTextBoxRgb(1, rgb);
  }

  @FXML
  private void setTextBox3() {
    final byte[] rgbArray = {
      this.textBox3R.getValueFactory().getValue().byteValue(),
      this.textBox3G.getValueFactory().getValue().byteValue(),
      this.textBox3B.getValueFactory().getValue().byteValue(),
      (byte)0x00,
    };

    final int rgb =
      (0xff & rgbArray[3]) << 24 |
        (0xff & rgbArray[2]) << 16 |
        (0xff & rgbArray[1]) << 8 |
        0xff & rgbArray[0];

    Config.setTextBoxRgb(2, rgb);
  }

  @FXML
  private void setTextBox4() {
    final byte[] rgbArray = {
      this.textBox4R.getValueFactory().getValue().byteValue(),
      this.textBox4G.getValueFactory().getValue().byteValue(),
      this.textBox4B.getValueFactory().getValue().byteValue(),
      (byte)0x00,
    };

    final int rgb =
      (0xff & rgbArray[3]) << 24 |
        (0xff & rgbArray[2]) << 16 |
        (0xff & rgbArray[1]) << 8 |
        0xff & rgbArray[0];

    Config.setTextBoxRgb(3, rgb);
  }

  @FXML
  private void setTextBox5() {
    final byte[] rgbArray = {
      this.textBox5R.getValueFactory().getValue().byteValue(),
      this.textBox5G.getValueFactory().getValue().byteValue(),
      this.textBox5B.getValueFactory().getValue().byteValue(),
      (byte)0x00,
    };

    final int rgb =
      (0xff & rgbArray[3]) << 24 |
        (0xff & rgbArray[2]) << 16 |
        (0xff & rgbArray[1]) << 8 |
        0xff & rgbArray[0];

    Config.setTextBoxRgb(4, rgb);
  }

  @FXML
  private void setTextBox6() {
    final byte[] rgbArray = {
      this.textBox6R.getValueFactory().getValue().byteValue(),
      this.textBox6G.getValueFactory().getValue().byteValue(),
      this.textBox6B.getValueFactory().getValue().byteValue(),
      (byte)0x00,
    };

    final int rgb =
      (0xff & rgbArray[3]) << 24 |
        (0xff & rgbArray[2]) << 16 |
        (0xff & rgbArray[1]) << 8 |
        0xff & rgbArray[0];

    Config.setTextBoxRgb(5, rgb);
  }

  @FXML
  private void setTextBox7() {
    final byte[] rgbArray = {
      this.textBox7R.getValueFactory().getValue().byteValue(),
      this.textBox7G.getValueFactory().getValue().byteValue(),
      this.textBox7B.getValueFactory().getValue().byteValue(),
      (byte)0x00,
    };

    final int rgb =
      (0xff & rgbArray[3]) << 24 |
        (0xff & rgbArray[2]) << 16 |
        (0xff & rgbArray[1]) << 8 |
        0xff & rgbArray[0];

    Config.setTextBoxRgb(6, rgb);
  }

  @FXML
  private void setTextBox8() {
    final byte[] rgbArray = {
      this.textBox8R.getValueFactory().getValue().byteValue(),
      this.textBox8G.getValueFactory().getValue().byteValue(),
      this.textBox8B.getValueFactory().getValue().byteValue(),
      (byte)0x00,
    };

    final int rgb =
      (0xff & rgbArray[3]) << 24 |
        (0xff & rgbArray[2]) << 16 |
        (0xff & rgbArray[1]) << 8 |
        0xff & rgbArray[0];

    Config.setTextBoxRgb(7, rgb);
  }

  @FXML
  private void setTextBoxTransparencyMode() {
    Config.setTextBoxTransparencyMode(this.textBoxTransparencyMode.getValue());
  }
}
