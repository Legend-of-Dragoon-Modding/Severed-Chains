package legend.game.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import legend.game.combat.ClientBattleController;
import legend.game.submap.SMap;
import legend.game.wmap.WMap;
import legend.game.wmap.WmapState;

import java.util.function.Consumer;

import static legend.core.GameEngine.BATTLE_CONTROLLER;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_800b.battleStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class NetClient {
  private final PacketManager<ClientContext> packetManager;

  public NetClient() {
    this.packetManager = new PacketManager<ClientContext>(registrar -> {
      registrar.register(GameStatePacket.class, GameStatePacket::serialize, GameStatePacket::deserialize, (packet, context) -> gameState_800babc8 = packet.state);
      registrar.register(StartBattlePacket.class, StartBattlePacket::serialize, StartBattlePacket::deserialize, (packet, context) -> {
        if(currentEngineState_8004dd04 instanceof final SMap smap) {
          smap.submap.generateEncounter();
          encounterId_800bb0f8 = packet.encounterId;
          battleStage_800bb0f4 = packet.stageId;
          smap.mapTransition(-1, 0);
        } else if(currentEngineState_8004dd04 instanceof final WMap wmap) {
          encounterId_800bb0f8 = packet.encounterId;
          gameState_800babc8.directionalPathIndex_4de = wmap.mapState_800c6798.directionalPathIndex_12;
          gameState_800babc8.pathIndex_4d8 = wmap.mapState_800c6798.pathIndex_14;
          gameState_800babc8.dotIndex_4da = wmap.mapState_800c6798.dotIndex_16;
          gameState_800babc8.dotOffset_4dc = wmap.mapState_800c6798.dotOffset_18;
          gameState_800babc8.facing_4dd = wmap.mapState_800c6798.facing_1c;
          battleStage_800bb0f4 = packet.stageId;
          wmap.wmapState_800bb10c = WmapState.TRANSITION_TO_BATTLE_8;
        }

        BATTLE_CONTROLLER = new ClientBattleController(this, packet.charSlot);
      });
      registrar.register(StartTurnPacket.class, StartTurnPacket::serialize, StartTurnPacket::deserialize, (packet, context) -> {
        ((ClientBattleController)BATTLE_CONTROLLER).startTurn(packet.bentId);
      });
    });
  }

  public void connect(final String host, final int port, final Consumer<String> logger) throws InterruptedException {
    final EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      final Bootstrap b = new Bootstrap();
      b.group(workerGroup);
      b.channel(NioSocketChannel.class);
      b.option(ChannelOption.SO_KEEPALIVE, true);
      b.handler(new ChannelInitializer<SocketChannel>() {
        @Override
        public void initChannel(final SocketChannel ch) throws Exception {
          ch.pipeline().addLast(
            new LengthFieldPrepender(2, 0), new Encoder(channel -> NetClient.this.packetManager),
            new LengthFieldBasedFrameDecoder(1024 * 8, 0, 2, 0, 2), new Decoder(channel -> NetClient.this.packetManager),
            new BattleClientHandler(NetClient.this.packetManager));
        }
      });

      // Start the client.
      final ChannelFuture f = b.connect(host, port).sync();

      logger.accept("Connected");

      // Wait until the connection is closed.
      f.channel().closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
    }
  }
}
