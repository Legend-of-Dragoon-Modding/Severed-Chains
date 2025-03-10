package legend.game.debugger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;

import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class GameStateViewerController {
  Timer updateUI;
  @FXML
  public Label scriptData0;
  @FXML
  public Label scriptData1;
  @FXML
  public Label scriptData2;
  @FXML
  public Label scriptData3;
  @FXML
  public Label scriptData4;
  @FXML
  public Label scriptData5;
  @FXML
  public Label scriptData6;
  @FXML
  public Label scriptData7;
  @FXML
  public Label scriptData8;
  @FXML
  public Label scriptData9;
  @FXML
  public Label scriptData10;
  @FXML
  public Label scriptData11;
  @FXML
  public Label scriptData12;
  @FXML
  public Label scriptData13;
  @FXML
  public Label scriptData14;
  @FXML
  public Label scriptData15;
  @FXML
  public Label scriptData16;
  @FXML
  public Label scriptData17;
  @FXML
  public Label scriptData18;
  @FXML
  public Label scriptData19;
  @FXML
  public Label scriptData20;
  @FXML
  public Label scriptData21;
  @FXML
  public Label scriptData22;
  @FXML
  public Label scriptData23;
  @FXML
  public Label scriptData24;
  @FXML
  public Label scriptData25;
  @FXML
  public Label scriptData26;
  @FXML
  public Label scriptData27;
  @FXML
  public Label scriptData28;
  @FXML
  public Label scriptData29;
  @FXML
  public Label scriptData30;
  @FXML
  public Label scriptData31;
  @FXML
  public Label scriptFlagsetI0;
  @FXML
  public Label scriptFlagsetI1;
  @FXML
  public Label scriptFlagsetI2;
  @FXML
  public Label scriptFlagsetI3;
  @FXML
  public Label scriptFlagsetI4;
  @FXML
  public Label scriptFlagsetI5;
  @FXML
  public Label scriptFlagsetI6;
  @FXML
  public Label scriptFlagsetI7;
  @FXML
  public Label scriptFlagsetI8;
  @FXML
  public Label scriptFlagsetI9;
  @FXML
  public Label scriptFlagsetI10;
  @FXML
  public Label scriptFlagsetI11;
  @FXML
  public Label scriptFlagsetI12;
  @FXML
  public Label scriptFlagsetI13;
  @FXML
  public Label scriptFlagsetI14;
  @FXML
  public Label scriptFlagsetI15;
  @FXML
  public Label scriptFlagsetI16;
  @FXML
  public Label scriptFlagsetI17;
  @FXML
  public Label scriptFlagsetI18;
  @FXML
  public Label scriptFlagsetI19;
  @FXML
  public Label scriptFlagsetI20;
  @FXML
  public Label scriptFlagsetI21;
  @FXML
  public Label scriptFlagsetI22;
  @FXML
  public Label scriptFlagsetI23;
  @FXML
  public Label scriptFlagsetI24;
  @FXML
  public Label scriptFlagsetI25;
  @FXML
  public Label scriptFlagsetI26;
  @FXML
  public Label scriptFlagsetI27;
  @FXML
  public Label scriptFlagsetI28;
  @FXML
  public Label scriptFlagsetI29;
  @FXML
  public Label scriptFlagsetI30;
  @FXML
  public Label scriptFlagsetI31;
  @FXML
  public Label scriptFlagsetII0;
  @FXML
  public Label scriptFlagsetII1;
  @FXML
  public Label scriptFlagsetII2;
  @FXML
  public Label scriptFlagsetII3;
  @FXML
  public Label scriptFlagsetII4;
  @FXML
  public Label scriptFlagsetII5;
  @FXML
  public Label scriptFlagsetII6;
  @FXML
  public Label scriptFlagsetII7;
  @FXML
  public Label dataII0;
  @FXML
  public Label dataII1;
  @FXML
  public Label dataII2;
  @FXML
  public Label dataII3;
  @FXML
  public Label dataII4;
  @FXML
  public Label dataII5;
  @FXML
  public Label dataII6;
  @FXML
  public Label dataII7;
  @FXML
  public Label dataIII0;
  @FXML
  public Label dataIII1;
  @FXML
  public Label dataIII2;
  @FXML
  public Label dataIII3;
  @FXML
  public Label dataIII4;
  @FXML
  public Label dataIII5;
  @FXML
  public Label dataIII6;
  @FXML
  public Label dataIII7;
  @FXML
  public Label dataIV0;
  @FXML
  public Label dataIV1;
  @FXML
  public Label dataIV2;
  @FXML
  public Label dataIV3;
  @FXML
  public Label dataIV4;
  @FXML
  public Label dataIV5;
  @FXML
  public Label dataIV6;
  @FXML
  public Label dataIV7;
  @FXML
  public Label dataI;
  @FXML
  public Label partyI;
  @FXML
  public Label partyII;
  @FXML
  public Label partyIII;
  @FXML
  public Label gold;
  @FXML
  public Label chapter;
  @FXML
  public Label stardust;
  @FXML
  public Label timestamp;
  @FXML
  public Label goodsI;
  @FXML
  public Label goodsII;
  @FXML
  public Label chestFlags0;
  @FXML
  public Label chestFlags1;
  @FXML
  public Label chestFlags2;
  @FXML
  public Label chestFlags3;
  @FXML
  public Label chestFlags4;
  @FXML
  public Label chestFlags5;
  @FXML
  public Label chestFlags6;
  @FXML
  public Label chestFlags7;
  @FXML
  public Label submapScene;
  @FXML
  public Label submapCut;
  @FXML
  public Label scriptEngineValueI;
  @FXML
  public Label scriptEngineValueII;
  @FXML
  public Label scriptEngineValueIII;
  @FXML
  public Label pathIndex;
  @FXML
  public Label dotIndex;
  @FXML
  public Label dotOffset;
  @FXML
  public Label facing;
  @FXML
  public Label areaIndex;


  public void initialize() {
    this.updateUI = new Timer();
    this.updateUI.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        Platform.runLater(() -> {
          if(gameState_800babc8 == null) {
            return;
          }
          GameStateViewerController.this.dataI.setText(String.format("%#x", gameState_800babc8._04));
          GameStateViewerController.this.scriptData0.setText(String.format("%#x", gameState_800babc8.scriptData_08[0]));
          GameStateViewerController.this.scriptData1.setText(String.format("%#x", gameState_800babc8.scriptData_08[1]));
          GameStateViewerController.this.scriptData2.setText(String.format("%#x", gameState_800babc8.scriptData_08[2]));
          GameStateViewerController.this.scriptData3.setText(String.format("%#x", gameState_800babc8.scriptData_08[3]));
          GameStateViewerController.this.scriptData4.setText(String.format("%#x", gameState_800babc8.scriptData_08[4]));
          GameStateViewerController.this.scriptData5.setText(String.format("%#x", gameState_800babc8.scriptData_08[5]));
          GameStateViewerController.this.scriptData6.setText(String.format("%#x", gameState_800babc8.scriptData_08[6]));
          GameStateViewerController.this.scriptData7.setText(String.format("%#x", gameState_800babc8.scriptData_08[7]));
          GameStateViewerController.this.scriptData8.setText(String.format("%#x", gameState_800babc8.scriptData_08[8]));
          GameStateViewerController.this.scriptData9.setText(String.format("%#x", gameState_800babc8.scriptData_08[9]));
          GameStateViewerController.this.scriptData10.setText(String.format("%#x", gameState_800babc8.scriptData_08[10]));
          GameStateViewerController.this.scriptData11.setText(String.format("%#x", gameState_800babc8.scriptData_08[11]));
          GameStateViewerController.this.scriptData12.setText(String.format("%#x", gameState_800babc8.scriptData_08[12]));
          GameStateViewerController.this.scriptData13.setText(String.format("%#x", gameState_800babc8.scriptData_08[13]));
          GameStateViewerController.this.scriptData14.setText(String.format("%#x", gameState_800babc8.scriptData_08[14]));
          GameStateViewerController.this.scriptData15.setText(String.format("%#x", gameState_800babc8.scriptData_08[15]));
          GameStateViewerController.this.scriptData16.setText(String.format("%#x", gameState_800babc8.scriptData_08[16]));
          GameStateViewerController.this.scriptData17.setText(String.format("%#x", gameState_800babc8.scriptData_08[17]));
          GameStateViewerController.this.scriptData18.setText(String.format("%#x", gameState_800babc8.scriptData_08[18]));
          GameStateViewerController.this.scriptData19.setText(String.format("%#x", gameState_800babc8.scriptData_08[19]));
          GameStateViewerController.this.scriptData20.setText(String.format("%#x", gameState_800babc8.scriptData_08[20]));
          GameStateViewerController.this.scriptData21.setText(String.format("%#x", gameState_800babc8.scriptData_08[21]));
          GameStateViewerController.this.scriptData22.setText(String.format("%#x", gameState_800babc8.scriptData_08[22]));
          GameStateViewerController.this.scriptData23.setText(String.format("%#x", gameState_800babc8.scriptData_08[23]));
          GameStateViewerController.this.scriptData24.setText(String.format("%#x", gameState_800babc8.scriptData_08[24]));
          GameStateViewerController.this.scriptData25.setText(String.format("%#x", gameState_800babc8.scriptData_08[25]));
          GameStateViewerController.this.scriptData26.setText(String.format("%#x", gameState_800babc8.scriptData_08[26]));
          GameStateViewerController.this.scriptData27.setText(String.format("%#x", gameState_800babc8.scriptData_08[27]));
          GameStateViewerController.this.scriptData28.setText(String.format("%#x", gameState_800babc8.scriptData_08[28]));
          GameStateViewerController.this.scriptData29.setText(String.format("%#x", gameState_800babc8.scriptData_08[29]));
          GameStateViewerController.this.scriptData30.setText(String.format("%#x", gameState_800babc8.scriptData_08[30]));
          GameStateViewerController.this.scriptData31.setText(String.format("%#x", gameState_800babc8.scriptData_08[31]));
          GameStateViewerController.this.partyI.setText(String.format("%#x", gameState_800babc8.charIds_88[0]));
          GameStateViewerController.this.partyII.setText(String.format("%#x", gameState_800babc8.charIds_88[1]));
          GameStateViewerController.this.partyIII.setText(String.format("%#x", gameState_800babc8.charIds_88[2]));
          GameStateViewerController.this.gold.setText(String.valueOf(gameState_800babc8.gold_94));
          GameStateViewerController.this.chapter.setText(String.valueOf(gameState_800babc8.chapterIndex_98));
          GameStateViewerController.this.stardust.setText(String.valueOf(gameState_800babc8.stardust_9c));
          GameStateViewerController.this.timestamp.setText(String.valueOf(gameState_800babc8.timestamp_a0));
          GameStateViewerController.this.submapScene.setText(String.format("%#x", gameState_800babc8.submapScene_a4));
          GameStateViewerController.this.submapCut.setText(String.format("%#x", gameState_800babc8.submapCut_a8));
          GameStateViewerController.this.scriptEngineValueI.setText(String.format("%#x", gameState_800babc8._b0));
          GameStateViewerController.this.scriptEngineValueII.setText(String.format("%#x", gameState_800babc8._b4));
          GameStateViewerController.this.scriptEngineValueIII.setText(String.format("%#x", gameState_800babc8._b8));
          GameStateViewerController.this.scriptFlagsetI0.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(0)));
          GameStateViewerController.this.scriptFlagsetI1.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(1)));
          GameStateViewerController.this.scriptFlagsetI2.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(2)));
          GameStateViewerController.this.scriptFlagsetI3.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(3)));
          GameStateViewerController.this.scriptFlagsetI4.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(4)));
          GameStateViewerController.this.scriptFlagsetI5.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(5)));
          GameStateViewerController.this.scriptFlagsetI6.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(6)));
          GameStateViewerController.this.scriptFlagsetI7.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(7)));
          GameStateViewerController.this.scriptFlagsetI8.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(8)));
          GameStateViewerController.this.scriptFlagsetI9.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(9)));
          GameStateViewerController.this.scriptFlagsetI10.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(10)));
          GameStateViewerController.this.scriptFlagsetI11.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(11)));
          GameStateViewerController.this.scriptFlagsetI12.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(12)));
          GameStateViewerController.this.scriptFlagsetI13.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(13)));
          GameStateViewerController.this.scriptFlagsetI14.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(14)));
          GameStateViewerController.this.scriptFlagsetI15.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(15)));
          GameStateViewerController.this.scriptFlagsetI16.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(16)));
          GameStateViewerController.this.scriptFlagsetI17.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(17)));
          GameStateViewerController.this.scriptFlagsetI18.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(18)));
          GameStateViewerController.this.scriptFlagsetI19.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(19)));
          GameStateViewerController.this.scriptFlagsetI20.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(20)));
          GameStateViewerController.this.scriptFlagsetI21.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(21)));
          GameStateViewerController.this.scriptFlagsetI22.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(22)));
          GameStateViewerController.this.scriptFlagsetI23.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(23)));
          GameStateViewerController.this.scriptFlagsetI24.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(24)));
          GameStateViewerController.this.scriptFlagsetI25.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(25)));
          GameStateViewerController.this.scriptFlagsetI26.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(26)));
          GameStateViewerController.this.scriptFlagsetI27.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(27)));
          GameStateViewerController.this.scriptFlagsetI28.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(28)));
          GameStateViewerController.this.scriptFlagsetI29.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(29)));
          GameStateViewerController.this.scriptFlagsetI30.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(30)));
          GameStateViewerController.this.scriptFlagsetI31.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(31)));
          GameStateViewerController.this.scriptFlagsetII0.setText(String.format("%#x", gameState_800babc8.scriptFlags1_13c.getRaw(0)));
          GameStateViewerController.this.scriptFlagsetII1.setText(String.format("%#x", gameState_800babc8.scriptFlags1_13c.getRaw(1)));
          GameStateViewerController.this.scriptFlagsetII2.setText(String.format("%#x", gameState_800babc8.scriptFlags1_13c.getRaw(2)));
          GameStateViewerController.this.scriptFlagsetII3.setText(String.format("%#x", gameState_800babc8.scriptFlags1_13c.getRaw(3)));
          GameStateViewerController.this.scriptFlagsetII4.setText(String.format("%#x", gameState_800babc8.scriptFlags1_13c.getRaw(4)));
          GameStateViewerController.this.scriptFlagsetII5.setText(String.format("%#x", gameState_800babc8.scriptFlags1_13c.getRaw(5)));
          GameStateViewerController.this.scriptFlagsetII6.setText(String.format("%#x", gameState_800babc8.scriptFlags1_13c.getRaw(6)));
          GameStateViewerController.this.scriptFlagsetII7.setText(String.format("%#x", gameState_800babc8.scriptFlags1_13c.getRaw(7)));
          GameStateViewerController.this.dataII0.setText(String.format("%#x", gameState_800babc8.wmapFlags_15c.getRaw(0)));
          GameStateViewerController.this.dataII1.setText(String.format("%#x", gameState_800babc8.wmapFlags_15c.getRaw(1)));
          GameStateViewerController.this.dataII2.setText(String.format("%#x", gameState_800babc8.wmapFlags_15c.getRaw(2)));
          GameStateViewerController.this.dataII3.setText(String.format("%#x", gameState_800babc8.wmapFlags_15c.getRaw(3)));
          GameStateViewerController.this.dataII4.setText(String.format("%#x", gameState_800babc8.wmapFlags_15c.getRaw(4)));
          GameStateViewerController.this.dataII5.setText(String.format("%#x", gameState_800babc8.wmapFlags_15c.getRaw(5)));
          GameStateViewerController.this.dataII6.setText(String.format("%#x", gameState_800babc8.wmapFlags_15c.getRaw(6)));
          GameStateViewerController.this.dataII7.setText(String.format("%#x", gameState_800babc8.wmapFlags_15c.getRaw(7)));
          GameStateViewerController.this.dataIII0.setText(String.format("%#x", gameState_800babc8.visitedLocations_17c.getRaw(0)));
          GameStateViewerController.this.dataIII1.setText(String.format("%#x", gameState_800babc8.visitedLocations_17c.getRaw(1)));
          GameStateViewerController.this.dataIII2.setText(String.format("%#x", gameState_800babc8.visitedLocations_17c.getRaw(2)));
          GameStateViewerController.this.dataIII3.setText(String.format("%#x", gameState_800babc8.visitedLocations_17c.getRaw(3)));
          GameStateViewerController.this.dataIII4.setText(String.format("%#x", gameState_800babc8.visitedLocations_17c.getRaw(4)));
          GameStateViewerController.this.dataIII5.setText(String.format("%#x", gameState_800babc8.visitedLocations_17c.getRaw(5)));
          GameStateViewerController.this.dataIII6.setText(String.format("%#x", gameState_800babc8.visitedLocations_17c.getRaw(6)));
          GameStateViewerController.this.dataIII7.setText(String.format("%#x", gameState_800babc8.visitedLocations_17c.getRaw(7)));
          GameStateViewerController.this.goodsI.setText(String.format("%#x", gameState_800babc8.goods_19c[0]));
          GameStateViewerController.this.goodsII.setText(String.format("%#x", gameState_800babc8.goods_19c[1]));
          GameStateViewerController.this.dataIV0.setText(String.format("%#x", gameState_800babc8._1a4[0]));
          GameStateViewerController.this.dataIV1.setText(String.format("%#x", gameState_800babc8._1a4[1]));
          GameStateViewerController.this.dataIV2.setText(String.format("%#x", gameState_800babc8._1a4[2]));
          GameStateViewerController.this.dataIV3.setText(String.format("%#x", gameState_800babc8._1a4[3]));
          GameStateViewerController.this.dataIV4.setText(String.format("%#x", gameState_800babc8._1a4[4]));
          GameStateViewerController.this.dataIV5.setText(String.format("%#x", gameState_800babc8._1a4[5]));
          GameStateViewerController.this.dataIV6.setText(String.format("%#x", gameState_800babc8._1a4[6]));
          GameStateViewerController.this.dataIV7.setText(String.format("%#x", gameState_800babc8._1a4[7]));
          GameStateViewerController.this.chestFlags0.setText(String.format("%#x", gameState_800babc8.chestFlags_1c4[0]));
          GameStateViewerController.this.chestFlags1.setText(String.format("%#x", gameState_800babc8.chestFlags_1c4[1]));
          GameStateViewerController.this.chestFlags2.setText(String.format("%#x", gameState_800babc8.chestFlags_1c4[2]));
          GameStateViewerController.this.chestFlags3.setText(String.format("%#x", gameState_800babc8.chestFlags_1c4[3]));
          GameStateViewerController.this.chestFlags4.setText(String.format("%#x", gameState_800babc8.chestFlags_1c4[4]));
          GameStateViewerController.this.chestFlags5.setText(String.format("%#x", gameState_800babc8.chestFlags_1c4[5]));
          GameStateViewerController.this.chestFlags6.setText(String.format("%#x", gameState_800babc8.chestFlags_1c4[6]));
          GameStateViewerController.this.chestFlags7.setText(String.format("%#x", gameState_800babc8.chestFlags_1c4[7]));
          GameStateViewerController.this.pathIndex.setText(String.format("%#x", gameState_800babc8.pathIndex_4d8));
          GameStateViewerController.this.dotIndex.setText(String.format("%#x", gameState_800babc8.dotIndex_4da));
          GameStateViewerController.this.dotOffset.setText(String.valueOf(gameState_800babc8.dotOffset_4dc));
          GameStateViewerController.this.facing.setText(String.valueOf(gameState_800babc8.facing_4dd));
          GameStateViewerController.this.areaIndex.setText(String.format("%#x", gameState_800babc8.directionalPathIndex_4de));
        });
      }
    }, 0, 1000);
  }

  public void uninitialize() {
    this.updateUI.cancel();
  }

}
