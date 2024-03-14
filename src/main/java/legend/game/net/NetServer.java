package legend.game.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class NetServer {
  private final PacketManager<ServerContext> packetManager;

  public NetServer() {
    this.packetManager = new PacketManager<ServerContext>(registrar -> {
      registrar.register(GameStatePacket.class, GameStatePacket::serialize, GameStatePacket::deserialize);
      registrar.register(StartBattlePacket.class, StartBattlePacket::serialize, StartBattlePacket::deserialize);
    });
  }

  public void listen(final int port, final BattleServerListener listener) throws InterruptedException {
    final EventLoopGroup bossGroup = new NioEventLoopGroup();
    final EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      final ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          public void initChannel(final SocketChannel ch) {
            ch.pipeline().addLast(
              new LengthFieldPrepender(2, 0), new Encoder(channel -> NetServer.this.packetManager),
              new LengthFieldBasedFrameDecoder(1024 * 8, 0, 2, 0, 2), new Decoder(channel -> NetServer.this.packetManager),
              new BattleServerHandler(listener));
          }
        })
        .option(ChannelOption.SO_BACKLOG, 128)
        .childOption(ChannelOption.SO_KEEPALIVE, true);

      // Bind and start to accept incoming connections.
      final ChannelFuture f = b.bind(port).sync();

      // Wait until the server socket is closed.
      // In this example, this does not happen, but you can do that to gracefully
      // shut down your server.
      f.channel().closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
  }
}
