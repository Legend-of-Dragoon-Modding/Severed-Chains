package legend.game.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class BattleServerHandler extends ChannelInboundHandlerAdapter {
  private final BattleServerListener listener;
  private final PacketManager<ServerContext> packetManager;

  public BattleServerHandler(final BattleServerListener listener, final PacketManager<ServerContext> packetManager) {
    this.listener = listener;
    this.packetManager = packetManager;
  }

  @Override
  public void channelActive(final ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);
    this.listener.clientConnected(ctx);
  }

  @Override
  public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
    this.listener.clientDisconnected(ctx);
    super.channelInactive(ctx);
  }

  @Override
  public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
    this.packetManager.handle(msg, new ServerContext(ctx));
  }

  @Override
  public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
    // Close the connection when an exception is raised.
    cause.printStackTrace();
    ctx.close();
  }
}

