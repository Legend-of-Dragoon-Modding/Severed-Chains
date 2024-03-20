package legend.game.net;

import io.netty.channel.ChannelHandlerContext;

public class ClientContext {
  public final ChannelHandlerContext ctx;

  public ClientContext(final ChannelHandlerContext ctx) {
    this.ctx = ctx;
  }
}
