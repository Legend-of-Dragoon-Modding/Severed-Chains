package legend.game.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class BattleClientHandler extends ChannelInboundHandlerAdapter {
  @Override
  public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
    final ByteBuf buf = (ByteBuf) msg;
    System.out.println(buf.toString(CharsetUtil.US_ASCII));
  }

  @Override
  public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
