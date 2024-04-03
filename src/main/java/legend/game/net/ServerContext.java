package legend.game.net;

import io.netty.channel.ChannelHandlerContext;

public class ServerContext {
  public final ChannelHandlerContext ctx;

  public ServerContext(final ChannelHandlerContext ctx) {
    this.ctx = ctx;
  }
}
