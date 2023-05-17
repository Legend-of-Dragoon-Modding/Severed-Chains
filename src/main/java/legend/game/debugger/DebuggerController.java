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
import legend.game.combat.AutoAdditionMode;
import legend.game.combat.Bttl_800c;
import legend.game.combat.SEffe;
import legend.game.modding.coremod.CoreMod;
import legend.game.types.WMapAreaData08;

import static legend.game.SMap.FUN_800e5534;
import static legend.game.SMap.encounterData_800f64c4;
import static legend.game.SMap.smapLoadingStage_800cb430;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.combatStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.WMap.areaData_800f2248;
import static legend.game.WMap.areaIndex_800c67aa;
import static legend.game.WMap.dotIndex_800c67ae;
import static legend.game.WMap.dotOffset_800c67b0;
import static legend.game.WMap.facing_800c67b4;
import static legend.game.WMap.pathIndex_800c67ac;

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
  public Spinner<Integer> battleUIColourR;
  @FXML
  public Spinner<Integer> battleUIColourG;
  @FXML
  public Spinner<Integer> battleUIColourB;
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
  public Button refreshAutoAddition;
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
    this.battleUiColour.setSelected(Config.changeBattleRGB());
    this.saveAnywhere.setSelected(Config.saveAnywhere());
    this.battleUIColourR.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, (Config.getBattleRgb() & 0xff)));
    this.battleUIColourG.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getBattleRgb() >> 8) & 0xff)));
    this.battleUIColourB.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getBattleRgb() >> 16) & 0xff)));
    this.additionOverlayColour.setSelected(Config.changeAdditionOverlayRgb());
    this.additionOverlayR.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, (Config.getAdditionOverlayRgb() & 0xff)));
    this.additionOverlayG.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getAdditionOverlayRgb() >> 8) & 0xff)));
    this.additionOverlayB.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getAdditionOverlayRgb() >> 16) & 0xff)));
    this.counterOverlayR.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, (Config.getCounterOverlayRgb() & 0xff)));
    this.counterOverlayG.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getCounterOverlayRgb() >> 8) & 0xff)));
    this.counterOverlayB.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getCounterOverlayRgb() >> 16) & 0xff)));
    this.autoMeter.setSelected(Config.autoDragoonMeter());
    this.disableStatusEffects.setSelected(Config.disableStatusEffects());
    this.combatStage.setSelected(Config.combatStage());
    this.combatStageId.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 127, Config.getCombatStage()));
    this.fastTextSpeed.setSelected(Config.fastTextSpeed());
    this.autoAdvanceText.setSelected(Config.autoAdvanceText());
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
      textBoxBase.setSelected(true);
      textBoxHorizontal.setSelected(false);
      textBoxVertical.setSelected(false);
    } else if(Config.getTextBoxColourMode() == 1) {
      textBoxBase.setSelected(false);
      textBoxHorizontal.setSelected(true);
      textBoxVertical.setSelected(false);
    } else if(Config.getTextBoxColourMode() == 2) {
      textBoxBase.setSelected(false);
      textBoxHorizontal.setSelected(false);
      textBoxVertical.setSelected(true);
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
    this.encounterId.getValueFactory().setValue(encounterId_800bb0f8.get());
  }

  @FXML
  private void startEncounter(final ActionEvent event) {
    encounterId_800bb0f8.set(this.encounterId.getValue());

    if(mainCallbackIndex_8004dd20.get() == 5) {
      if(Config.combatStage()) {
        combatStage_800bb0f4.set(Config.getCombatStage());
      } else {
        combatStage_800bb0f4.set(encounterData_800f64c4.get(submapCut_80052c30.get()).stage_03.get());
      }
      FUN_800e5534(-1, 0);
    } else if(mainCallbackIndex_8004dd20.get() == 8) {
      final WMapAreaData08 area = areaData_800f2248.get(areaIndex_800c67aa.get());

      if(Config.combatStage()) {
        combatStage_800bb0f4.set(Config.getCombatStage());
      } else {
        if(area.stage_04.get() == -1) {
          combatStage_800bb0f4.set(1);
        } else {
          combatStage_800bb0f4.set(area.stage_04.get());
        }
      }

      gameState_800babc8.areaIndex_4de = areaIndex_800c67aa.get();
      gameState_800babc8.pathIndex_4d8 = pathIndex_800c67ac.get();
      gameState_800babc8.dotIndex_4da = dotIndex_800c67ae.get();
      gameState_800babc8.dotOffset_4dc = dotOffset_800c67b0.get();
      gameState_800babc8.facing_4dd = facing_800c67b4.get();
      pregameLoadingStage_800bb10c.set(8);
    }
  }

  @FXML
  private void getMapId(final ActionEvent event) {
    this.mapId.getValueFactory().setValue(submapCut_80052c30.get());
  }

  @FXML
  private void warpToMap(final ActionEvent event) {
    submapCut_80052c30.set(this.mapId.getValue());
    smapLoadingStage_800cb430.set(0x4);
  }

  @FXML
  private void getVsyncMode(final ActionEvent event) {
    this.vsyncMode.getValueFactory().setValue(vsyncMode_8007a3b8.get());
  }

  @FXML
  private void setVsyncMode(final ActionEvent event) {
    vsyncMode_8007a3b8.set(this.vsyncMode.getValue());
  }

  @FXML
  private void getGameSpeedMultiplier(final ActionEvent event) {
    this.gameSpeedMultiplier.getValueFactory().setValue(Config.getGameSpeedMultiplier());
  }

  @FXML
  private void setGameSpeedMultiplier(final ActionEvent event) {
    Config.setGameSpeedMultiplier(this.gameSpeedMultiplier.getValue());
  }

  @FXML
  private void toggleSaveAnywhere(final ActionEvent event) {
    Config.toggleSaveAnywhere();
  }

  @FXML
  private void toggleBattleUiColour(final ActionEvent event) {
    Config.toggleBattleUIColour();
  }

  @FXML
  private void getBattleUiRgb(final ActionEvent event) {
    final int rgb = (int)Bttl_800c._800c7004.get();
    final int[] rgbArray = {
      rgb >> 24 & 0xff,
      rgb >> 16 & 0xff,
      rgb >> 8 & 0xff,
      rgb & 0xff
    };

    this.battleUIColourR.getValueFactory().setValue(rgbArray[3]);
    this.battleUIColourG.getValueFactory().setValue(rgbArray[2]);
    this.battleUIColourB.getValueFactory().setValue(rgbArray[1]);
  }

  @FXML
  private void setBattleUiRgb(final ActionEvent event) {
    final byte[] rgbArray = {
      this.battleUIColourR.getValueFactory().getValue().byteValue(),
      this.battleUIColourG.getValueFactory().getValue().byteValue(),
      this.battleUIColourB.getValueFactory().getValue().byteValue(),
      (byte)0x00,
    };

    final int rgb =
      (0xff & rgbArray[3]) << 24 |
        (0xff & rgbArray[2]) << 16 |
        (0xff & rgbArray[1]) << 8 |
        0xff & rgbArray[0];

    Config.setBattleRgb(rgb);
    Bttl_800c._800c7004.set(rgb);
    this.battleUiColour.setSelected(true);
  }

  @FXML
  private void toggleAdditionOverlayColour() {
    Config.toggleAdditionOverlayColour();
  }

  @FXML
  private void getAdditionOverlayRgb(final ActionEvent event) {
    this.additionOverlayR.getValueFactory().setValue(SEffe.additionBorderColours_800fb7f0.get(9).get());
    this.additionOverlayG.getValueFactory().setValue(SEffe.additionBorderColours_800fb7f0.get(10).get());
    this.additionOverlayB.getValueFactory().setValue(SEffe.additionBorderColours_800fb7f0.get(11).get());
  }

  @FXML
  private void setAdditionOverlayRgb(final ActionEvent event) {
    final byte[] rgbArray = {
      this.additionOverlayR.getValueFactory().getValue().byteValue(),
      this.additionOverlayG.getValueFactory().getValue().byteValue(),
      this.additionOverlayB.getValueFactory().getValue().byteValue(),
      (byte)0x00,
    };

    final int rgb =
      (0xff & rgbArray[3]) << 24 |
        (0xff & rgbArray[2]) << 16 |
        (0xff & rgbArray[1]) << 8 |
        0xff & rgbArray[0];

    Config.setAdditionOverlayRgb(rgb);
    SEffe.additionBorderColours_800fb7f0.get(9).set(rgbArray[0] & 0xff);
    SEffe.additionBorderColours_800fb7f0.get(10).set(rgbArray[1] & 0xff);
    SEffe.additionBorderColours_800fb7f0.get(11).set(rgbArray[2] & 0xff);
    this.additionOverlayColour.setSelected(true);
  }

  @FXML
  private void getCounterOverlayRgb(final ActionEvent event) {
    this.counterOverlayR.getValueFactory().setValue(SEffe.additionBorderColours_800fb7f0.get(6).get());
    this.counterOverlayG.getValueFactory().setValue(SEffe.additionBorderColours_800fb7f0.get(7).get());
    this.counterOverlayB.getValueFactory().setValue(SEffe.additionBorderColours_800fb7f0.get(8).get());
  }

  @FXML
  private void setCounterOverlayRgb(final ActionEvent event) {
    final byte[] rgbArray = {
      this.counterOverlayR.getValueFactory().getValue().byteValue(),
      this.counterOverlayG.getValueFactory().getValue().byteValue(),
      this.counterOverlayB.getValueFactory().getValue().byteValue(),
      (byte)0x00,
    };

    final int rgb =
      (0xff & rgbArray[3]) << 24 |
        (0xff & rgbArray[2]) << 16 |
        (0xff & rgbArray[1]) << 8 |
        0xff & rgbArray[0];

    Config.setCounterOverlayRgb(rgb);
    SEffe.additionBorderColours_800fb7f0.get(6).set(rgbArray[0] & 0xff);
    SEffe.additionBorderColours_800fb7f0.get(7).set(rgbArray[1] & 0xff);
    SEffe.additionBorderColours_800fb7f0.get(8).set(rgbArray[2] & 0xff);
    this.additionOverlayColour.setSelected(true);
  }

  @FXML
  private void toggleAutoAddition(final ActionEvent event) {
    if(gameState_800babc8 != null) {
      final boolean autoAddition = gameState_800babc8.getConfig(CoreMod.AUTO_ADDITION_CONFIG.get()) == AutoAdditionMode.ON;
      gameState_800babc8.setConfig(CoreMod.AUTO_ADDITION_CONFIG.get(), autoAddition ? AutoAdditionMode.OFF : AutoAdditionMode.ON);
    }
  }

  @FXML
  private void refreshAutoAddition(final ActionEvent event) {
    if(gameState_800babc8 != null) {
      this.autoAddition.setSelected(gameState_800babc8.getConfig(CoreMod.AUTO_ADDITION_CONFIG.get()) == AutoAdditionMode.ON);
    }
  }

  @FXML
  private void toggleAutoDragoonMeter(final ActionEvent event) {
    Config.toggleAutoDragoonMeter();
  }

  @FXML
  private void toggleDisableStatusEffects(final ActionEvent event) {
    Config.toggleDisableStatusEffects();
  }

  @FXML
  private void toggleCombatStage(final ActionEvent event) {
    Config.toggleCombatStage();
  }

  @FXML
  private void getCombatStageId(final ActionEvent event) {
    this.combatStageId.getValueFactory().setValue(combatStage_800bb0f4.get());
  }

  @FXML
  private void setCombatStageId(final ActionEvent event) {
    Config.setCombatStage(this.combatStageId.getValue());
  }

  @FXML
  private void toggleFastText(final ActionEvent event) {
    Config.toggleFastText();
  }

  @FXML
  private void toggleAutoAdvanceText(final ActionEvent event) {
    Config.toggleAutoAdvanceText();
  }

  @FXML
  private void toggleTextBoxColour(final ActionEvent event) {
    Config.toggleTextBoxColour();
  }

  @FXML
  private void setTextBoxRadio(final ActionEvent event) {
    if((event.toString().contains("textBoxBase"))) {
      textBoxBase.setSelected(true);
      textBoxHorizontal.setSelected(false);
      textBoxVertical.setSelected(false);
      Config.setTextBoxColourMode(0);
    } else if (event.toString().contains("textBoxHorizontal")) {
      textBoxBase.setSelected(false);
      textBoxHorizontal.setSelected(true);
      textBoxVertical.setSelected(false);
      Config.setTextBoxColourMode(1);
    } else if (event.toString().contains("textBoxVertical")) {
      textBoxBase.setSelected(false);
      textBoxHorizontal.setSelected(false);
      textBoxVertical.setSelected(true);
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
    Config.setTextBoxTransparencyMode(textBoxTransparencyMode.getValue());
  }
}
