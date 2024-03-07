package legend.game.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import legend.game.types.GameState52c;

import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class BattleClientHandler extends ChannelInboundHandlerAdapter {
  private final BattleClientListener listener;

  public BattleClientHandler(final BattleClientListener listener) {
    this.listener = listener;
  }

  @Override
  public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
    gameState_800babc8 = (GameState52c)msg;
//    final ByteBuf buf = (ByteBuf)msg;
//    this.listener.packetReceived(buf);
//    buf.release();
  }

  @Override
  public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
