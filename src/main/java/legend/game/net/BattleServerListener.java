package legend.game.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface BattleServerListener {
  void clientConnected(final ChannelHandlerContext ctx);
  void clientDisconnected(final ChannelHandlerContext ctx);
  void packetReceived(final ChannelHandlerContext ctx, final ByteBuf buf);
}
