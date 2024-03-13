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

import java.util.function.Consumer;

import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class NetClient {
  private final PacketManager<ClientContext> packetManager;

  public NetClient() {
    this.packetManager = new PacketManager<ClientContext>(registrar -> {
      registrar.register(GameStatePacket.class, GameStatePacket::serialize, GameStatePacket::deserialize, (packet, context) -> gameState_800babc8 = packet.state);
    });
  }

  public void connect(final String host, final int port, final BattleClientListener listener, final Consumer<String> logger) throws InterruptedException {
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
            new BattleClientHandler(listener, NetClient.this.packetManager));
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
