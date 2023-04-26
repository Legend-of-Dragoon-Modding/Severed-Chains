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
import legend.game.combat.Bttl_800c;
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
  public Spinner<Integer> battleUiColourR;
  @FXML
  public Spinner<Integer> battleUiColourG;
  @FXML
  public Spinner<Integer> battleUiColourB;
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

  public void initialize() {
    this.encounterId.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
    this.mapId.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
    this.vsyncMode.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
    this.gameSpeedMultiplier.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 16, Config.getGameSpeedMultiplier()));
    this.battleUiColour.setSelected(Config.changeBattleRgb());
    this.saveAnywhere.setSelected(Config.saveAnywhere());
    this.battleUiColourR.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, (Config.getBattleRgb() & 0xff)));
    this.battleUiColourG.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getBattleRgb() >> 8)  & 0xff)));
    this.battleUiColourB.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, ((Config.getBattleRgb() >> 16)  & 0xff)));
    this.autoAddition.setSelected(Config.autoAddition());
    this.autoMeter.setSelected(Config.autoDragoonMeter());
    this.disableStatusEffects.setSelected(Config.disableStatusEffects());
    this.combatStage.setSelected(Config.combatStage());
    this.combatStageId.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 127, Config.getCombatStage()));
    this.fastTextSpeed.setSelected(Config.fastTextSpeed());
    this.autoAdvanceText.setSelected(Config.autoAdvanceText());
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
    Config.toggleBattleUiColour();
  }

  @FXML
  private void getBattleUiRgb(final ActionEvent event) {
    this.battleUiColourR.getValueFactory().setValue(Bttl_800c.textboxColours_800c6fec.get(8).get(0).get());
    this.battleUiColourG.getValueFactory().setValue(Bttl_800c.textboxColours_800c6fec.get(8).get(1).get());
    this.battleUiColourB.getValueFactory().setValue(Bttl_800c.textboxColours_800c6fec.get(8).get(2).get());
  }

  @FXML
  private void setBattleUiRgb(final ActionEvent event) {
    Bttl_800c.textboxColours_800c6fec.get(8).get(0).set(this.battleUiColourR.getValueFactory().getValue().byteValue());
    Bttl_800c.textboxColours_800c6fec.get(8).get(1).set(this.battleUiColourG.getValueFactory().getValue().byteValue());
    Bttl_800c.textboxColours_800c6fec.get(8).get(2).set(this.battleUiColourB.getValueFactory().getValue().byteValue());

    final int rgb =
      Bttl_800c.textboxColours_800c6fec.get(8).get(2).get() << 16 |
      Bttl_800c.textboxColours_800c6fec.get(8).get(1).get() << 8  |
      Bttl_800c.textboxColours_800c6fec.get(8).get(0).get();

    Config.setBattleRgb(rgb);
    this.battleUiColour.setSelected(true);
  }

  @FXML
  private void toggleAutoAddition(final ActionEvent event) {
    Config.toggleAutoAddition();
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
}
