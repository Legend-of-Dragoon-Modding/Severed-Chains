package legend.game.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class BattleClientHandler extends ChannelInboundHandlerAdapter {
  private final BattleClientListener listener;

  public BattleClientHandler(final BattleClientListener listener) {
    this.listener = listener;
  }

  @Override
  public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
    final ByteBuf buf = (ByteBuf) msg;
    this.listener.packetReceived(buf);
    buf.release();
  }

  @Override
  public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
