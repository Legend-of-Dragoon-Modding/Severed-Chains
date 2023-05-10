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
  public Label dataV0;
  @FXML
  public Label dataV1;
  @FXML
  public Label dataV2;
  @FXML
  public Label dataV3;
  @FXML
  public Label dataV4;
  @FXML
  public Label dataV5;
  @FXML
  public Label dataV6;
  @FXML
  public Label dataV7;
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
    updateUI = new Timer();
    updateUI.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        Platform.runLater(() -> {
          if(gameState_800babc8 == null) {
            return;
          }
          dataI.setText(String.valueOf(gameState_800babc8._04));
          scriptData0.setText(String.valueOf(gameState_800babc8.scriptData_08[0]));
          scriptData1.setText(String.valueOf(gameState_800babc8.scriptData_08[1]));
          scriptData2.setText(String.valueOf(gameState_800babc8.scriptData_08[2]));
          scriptData3.setText(String.valueOf(gameState_800babc8.scriptData_08[3]));
          scriptData4.setText(String.valueOf(gameState_800babc8.scriptData_08[4]));
          scriptData5.setText(String.valueOf(gameState_800babc8.scriptData_08[5]));
          scriptData6.setText(String.valueOf(gameState_800babc8.scriptData_08[6]));
          scriptData7.setText(String.valueOf(gameState_800babc8.scriptData_08[7]));
          scriptData8.setText(String.valueOf(gameState_800babc8.scriptData_08[8]));
          scriptData9.setText(String.valueOf(gameState_800babc8.scriptData_08[9]));
          scriptData10.setText(String.valueOf(gameState_800babc8.scriptData_08[10]));
          scriptData11.setText(String.valueOf(gameState_800babc8.scriptData_08[11]));
          scriptData12.setText(String.valueOf(gameState_800babc8.scriptData_08[12]));
          scriptData13.setText(String.valueOf(gameState_800babc8.scriptData_08[13]));
          scriptData14.setText(String.valueOf(gameState_800babc8.scriptData_08[14]));
          scriptData15.setText(String.valueOf(gameState_800babc8.scriptData_08[15]));
          scriptData16.setText(String.valueOf(gameState_800babc8.scriptData_08[16]));
          scriptData17.setText(String.valueOf(gameState_800babc8.scriptData_08[17]));
          scriptData18.setText(String.valueOf(gameState_800babc8.scriptData_08[18]));
          scriptData19.setText(String.valueOf(gameState_800babc8.scriptData_08[19]));
          scriptData20.setText(String.valueOf(gameState_800babc8.scriptData_08[20]));
          scriptData21.setText(String.valueOf(gameState_800babc8.scriptData_08[21]));
          scriptData22.setText(String.valueOf(gameState_800babc8.scriptData_08[22]));
          scriptData23.setText(String.valueOf(gameState_800babc8.scriptData_08[23]));
          scriptData24.setText(String.valueOf(gameState_800babc8.scriptData_08[24]));
          scriptData25.setText(String.valueOf(gameState_800babc8.scriptData_08[25]));
          scriptData26.setText(String.valueOf(gameState_800babc8.scriptData_08[26]));
          scriptData27.setText(String.valueOf(gameState_800babc8.scriptData_08[27]));
          scriptData28.setText(String.valueOf(gameState_800babc8.scriptData_08[28]));
          scriptData29.setText(String.valueOf(gameState_800babc8.scriptData_08[29]));
          scriptData30.setText(String.valueOf(gameState_800babc8.scriptData_08[30]));
          scriptData31.setText(String.valueOf(gameState_800babc8.scriptData_08[31]));
          partyI.setText(String.valueOf(gameState_800babc8.charIds_88[0]));
          partyII.setText(String.valueOf(gameState_800babc8.charIds_88[1]));
          partyIII.setText(String.valueOf(gameState_800babc8.charIds_88[2]));
          gold.setText(String.valueOf(gameState_800babc8.gold_94));
          chapter.setText(String.valueOf(gameState_800babc8.chapterIndex_98));
          stardust.setText(String.valueOf(gameState_800babc8.stardust_9c));
          timestamp.setText(String.valueOf(gameState_800babc8.timestamp_a0));
          submapScene.setText(String.valueOf(gameState_800babc8.submapScene_a4));
          submapCut.setText(String.valueOf(gameState_800babc8.submapCut_a8));
          scriptEngineValueI.setText(String.valueOf(gameState_800babc8._b0));
          scriptEngineValueII.setText(String.valueOf(gameState_800babc8._b4));
          scriptEngineValueIII.setText(String.valueOf(gameState_800babc8._b8));
          scriptFlagsetI0.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[0]));
          scriptFlagsetI0.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[0]));
          scriptFlagsetI1.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[1]));
          scriptFlagsetI2.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[2]));
          scriptFlagsetI3.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[3]));
          scriptFlagsetI4.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[4]));
          scriptFlagsetI5.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[5]));
          scriptFlagsetI6.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[6]));
          scriptFlagsetI7.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[7]));
          scriptFlagsetI8.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[8]));
          scriptFlagsetI9.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[9]));
          scriptFlagsetI10.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[10]));
          scriptFlagsetI11.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[11]));
          scriptFlagsetI12.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[12]));
          scriptFlagsetI13.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[13]));
          scriptFlagsetI14.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[14]));
          scriptFlagsetI15.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[15]));
          scriptFlagsetI16.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[16]));
          scriptFlagsetI17.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[17]));
          scriptFlagsetI18.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[18]));
          scriptFlagsetI19.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[19]));
          scriptFlagsetI20.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[20]));
          scriptFlagsetI21.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[21]));
          scriptFlagsetI22.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[22]));
          scriptFlagsetI23.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[23]));
          scriptFlagsetI24.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[24]));
          scriptFlagsetI25.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[25]));
          scriptFlagsetI26.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[26]));
          scriptFlagsetI27.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[27]));
          scriptFlagsetI28.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[28]));
          scriptFlagsetI29.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[29]));
          scriptFlagsetI30.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[30]));
          scriptFlagsetI31.setText(String.valueOf(gameState_800babc8.scriptFlags2_bc[31]));
          scriptFlagsetII0.setText(String.valueOf(gameState_800babc8.scriptFlags1_13c[0]));
          scriptFlagsetII1.setText(String.valueOf(gameState_800babc8.scriptFlags1_13c[1]));
          scriptFlagsetII2.setText(String.valueOf(gameState_800babc8.scriptFlags1_13c[2]));
          scriptFlagsetII3.setText(String.valueOf(gameState_800babc8.scriptFlags1_13c[3]));
          scriptFlagsetII4.setText(String.valueOf(gameState_800babc8.scriptFlags1_13c[4]));
          scriptFlagsetII5.setText(String.valueOf(gameState_800babc8.scriptFlags1_13c[5]));
          scriptFlagsetII6.setText(String.valueOf(gameState_800babc8.scriptFlags1_13c[6]));
          scriptFlagsetII7.setText(String.valueOf(gameState_800babc8.scriptFlags1_13c[7]));
          dataII0.setText(String.valueOf(gameState_800babc8._15c[0]));
          dataII1.setText(String.valueOf(gameState_800babc8._15c[1]));
          dataII2.setText(String.valueOf(gameState_800babc8._15c[2]));
          dataII3.setText(String.valueOf(gameState_800babc8._15c[3]));
          dataII4.setText(String.valueOf(gameState_800babc8._15c[4]));
          dataII5.setText(String.valueOf(gameState_800babc8._15c[5]));
          dataII6.setText(String.valueOf(gameState_800babc8._15c[6]));
          dataII7.setText(String.valueOf(gameState_800babc8._15c[7]));
          dataIII0.setText(String.valueOf(gameState_800babc8._17c[0]));
          dataIII1.setText(String.valueOf(gameState_800babc8._17c[1]));
          dataIII2.setText(String.valueOf(gameState_800babc8._17c[2]));
          dataIII3.setText(String.valueOf(gameState_800babc8._17c[3]));
          dataIII4.setText(String.valueOf(gameState_800babc8._17c[4]));
          dataIII5.setText(String.valueOf(gameState_800babc8._17c[5]));
          dataIII6.setText(String.valueOf(gameState_800babc8._17c[6]));
          dataIII7.setText(String.valueOf(gameState_800babc8._17c[7]));
          goodsI.setText(String.valueOf(gameState_800babc8.goods_19c[0]));
          goodsII.setText(String.valueOf(gameState_800babc8.goods_19c[1]));
          dataIV0.setText(String.valueOf(gameState_800babc8._1a4[0]));
          dataIV1.setText(String.valueOf(gameState_800babc8._1a4[1]));
          dataIV2.setText(String.valueOf(gameState_800babc8._1a4[2]));
          dataIV3.setText(String.valueOf(gameState_800babc8._1a4[3]));
          dataIV4.setText(String.valueOf(gameState_800babc8._1a4[4]));
          dataIV5.setText(String.valueOf(gameState_800babc8._1a4[5]));
          dataIV6.setText(String.valueOf(gameState_800babc8._1a4[6]));
          dataIV7.setText(String.valueOf(gameState_800babc8._1a4[7]));
          chestFlags0.setText(String.valueOf(gameState_800babc8.chestFlags_1c4[0]));
          chestFlags1.setText(String.valueOf(gameState_800babc8.chestFlags_1c4[1]));
          chestFlags2.setText(String.valueOf(gameState_800babc8.chestFlags_1c4[2]));
          chestFlags3.setText(String.valueOf(gameState_800babc8.chestFlags_1c4[3]));
          chestFlags4.setText(String.valueOf(gameState_800babc8.chestFlags_1c4[4]));
          chestFlags5.setText(String.valueOf(gameState_800babc8.chestFlags_1c4[5]));
          chestFlags6.setText(String.valueOf(gameState_800babc8.chestFlags_1c4[6]));
          chestFlags7.setText(String.valueOf(gameState_800babc8.chestFlags_1c4[7]));
          dataV0.setText(String.valueOf(gameState_800babc8._4b8[0]));
          dataV1.setText(String.valueOf(gameState_800babc8._4b8[1]));
          dataV2.setText(String.valueOf(gameState_800babc8._4b8[2]));
          dataV3.setText(String.valueOf(gameState_800babc8._4b8[3]));
          dataV4.setText(String.valueOf(gameState_800babc8._4b8[4]));
          dataV5.setText(String.valueOf(gameState_800babc8._4b8[5]));
          dataV6.setText(String.valueOf(gameState_800babc8._4b8[6]));
          dataV7.setText(String.valueOf(gameState_800babc8._4b8[7]));
          pathIndex.setText(String.valueOf(gameState_800babc8.pathIndex_4d8));
          dotIndex.setText(String.valueOf(gameState_800babc8.dotIndex_4da));
          dotOffset.setText(String.valueOf(gameState_800babc8.dotOffset_4dc));
          facing.setText(String.valueOf(gameState_800babc8.facing_4dd));
          areaIndex.setText(String.valueOf(gameState_800babc8.areaIndex_4de));
        });
      }
    }, 0, 1000);
  }

  public void finalize() {
    updateUI.cancel();
  }

}
