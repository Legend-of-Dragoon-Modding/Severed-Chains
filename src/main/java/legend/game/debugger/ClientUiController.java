package legend.game.debugger;

import io.netty.buffer.ByteBuf;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import legend.game.net.BattleClientListener;
import legend.game.net.NetClient;
import legend.game.submap.SMap;
import legend.game.wmap.DirectionalPathSegmentData08;
import legend.game.wmap.WMap;
import legend.game.wmap.WmapState;

import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_800b.battleStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.wmap.WmapStatics.directionalPathSegmentData_800f2248;

public class ClientUiController implements BattleClientListener {
  @FXML
  private Button btnConnect;

  @FXML
  private TextField txtIp;

  @FXML
  private TextField txtPort;

  @FXML
  private TextArea txtLog;

  public void connectClicked(final ActionEvent event) {
    try {
      final String ip = this.txtIp.getText();
      final int port = Integer.parseInt(this.txtPort.getText());
      new Thread(() -> {
        try {
          this.addText("Connecting...");
          new NetClient().connect(ip, port, this, this::addText);
        } catch(final InterruptedException e) {
          throw new RuntimeException(e);
        }
      }).start();
    } catch(final Throwable e) {
      e.printStackTrace();
    }
  }

  private void addText(final String text) {
    this.txtLog.setText(this.txtLog.getText() + text + '\n');
  }

  @Override
  public void packetReceived(final ByteBuf buf) {
    final int packet = buf.readInt();

    switch(packet) {
      case 1 -> {
        final int encounterId = buf.readInt();
        if(currentEngineState_8004dd04 instanceof final SMap smap) {
          smap.submap.generateEncounter();
          encounterId_800bb0f8 = encounterId;
          smap.mapTransition(-1, 0);
        } else if(currentEngineState_8004dd04 instanceof final WMap wmap) {
          encounterId_800bb0f8 = encounterId;
          final DirectionalPathSegmentData08 directionalPathSegment = directionalPathSegmentData_800f2248[wmap.mapState_800c6798.directionalPathIndex_12];

          if(directionalPathSegment.battleStage_04 == -1) {
            battleStage_800bb0f4 = 1;
          } else {
            battleStage_800bb0f4 = directionalPathSegment.battleStage_04;
          }

          gameState_800babc8.directionalPathIndex_4de = wmap.mapState_800c6798.directionalPathIndex_12;
          gameState_800babc8.pathIndex_4d8 = wmap.mapState_800c6798.pathIndex_14;
          gameState_800babc8.dotIndex_4da = wmap.mapState_800c6798.dotIndex_16;
          gameState_800babc8.dotOffset_4dc = wmap.mapState_800c6798.dotOffset_18;
          gameState_800babc8.facing_4dd = wmap.mapState_800c6798.facing_1c;
          wmap.wmapState_800bb10c = WmapState.TRANSITION_TO_BATTLE_8;
        }
      }

      default -> System.out.println("Unknown packet " + packet);
    }
  }
}
