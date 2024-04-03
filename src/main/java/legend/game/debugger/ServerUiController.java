package legend.game.debugger;

import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;
import legend.game.combat.ServerBattleController;
import legend.game.net.ActionPacket;
import legend.game.net.BattleServerListener;
import legend.game.net.GameStatePacket;
import legend.game.net.NetServer;
import legend.game.net.ServerContext;
import legend.game.net.StartBattlePacket;
import legend.game.submap.SMap;
import legend.game.wmap.DirectionalPathSegmentData08;
import legend.game.wmap.WMap;
import legend.game.wmap.WmapState;

import static legend.core.GameEngine.BATTLE_CONTROLLER;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_800b.battleStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.wmap.WmapStatics.directionalPathSegmentData_800f2248;

public class ServerUiController implements BattleServerListener {
  @FXML
  private ListView<ListItem> lstConnections;
  private final ObservableList<ListItem> connections = FXCollections.observableArrayList(e -> new Observable[] {e.prop});

  @FXML
  private Button btnHost;

  @FXML
  private TextField txtPort;

  @FXML
  public Spinner<Integer> encounterId;

  public void initialize() {
    this.lstConnections.setItems(this.connections);
    this.lstConnections.setCellFactory(param -> {
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

    this.encounterId.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
  }

  private NetServer server;

  public void hostClicked(final ActionEvent event) {
    try {
      this.server = new NetServer();
      final int port = Integer.parseInt(this.txtPort.getText());

      System.out.println("Hosting server on port " + port);

      new Thread(() -> {
        try {
          this.server.listen(port, this);
        } catch(final InterruptedException e) {
          throw new RuntimeException(e);
        }
      }).start();
    } catch(final Throwable e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void startEncounter(final ActionEvent event) {
    if(currentEngineState_8004dd04 instanceof final SMap smap) {
      smap.submap.generateEncounter();
      encounterId_800bb0f8 = this.encounterId.getValue();
      smap.mapTransition(-1, 0);
    } else if(currentEngineState_8004dd04 instanceof final WMap wmap) {
      encounterId_800bb0f8 = this.encounterId.getValue();
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

    final ServerBattleController controller = new ServerBattleController(this.server);
    BATTLE_CONTROLLER = controller;

    int i = 1;
    for(final ListItem connection : this.connections) {
      controller.addPlayer(i, connection.ctx);
      connection.ctx.writeAndFlush(new GameStatePacket(gameState_800babc8));
      connection.ctx.writeAndFlush(new StartBattlePacket(encounterId_800bb0f8, battleStage_800bb0f4, i));
      i++;
    }
  }

  @Override
  public void clientConnected(final ChannelHandlerContext ctx) {
    Platform.runLater(() -> this.connections.add(new ListItem(ctx)));
  }

  @Override
  public void clientDisconnected(final ChannelHandlerContext ctx) {
    Platform.runLater(() -> this.connections.removeIf(e -> e.ctx == ctx));
  }

  @Override
  public void handleAction(final ServerContext ctx, final ActionPacket packet) {
    BATTLE_CONTROLLER.handleAction(packet.action);
    for(final var connection : this.connections) {
      if(connection.ctx != ctx.ctx) {
        connection.ctx.writeAndFlush(connection);
      }
    }
  }

  private static class ListItem {
    private final StringProperty prop = new SimpleStringProperty(this, "name");
    private final ChannelHandlerContext ctx;

    public ListItem(final ChannelHandlerContext ctx) {
      this.ctx = ctx;
      this.prop.set(ctx.name());
    }

    public String getName() {
      return this.prop.get();
    }
  }
}
