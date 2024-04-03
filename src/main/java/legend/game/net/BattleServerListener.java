package legend.game.net;

import io.netty.channel.ChannelHandlerContext;

public interface BattleServerListener {
  void clientConnected(final ChannelHandlerContext ctx);
  void clientDisconnected(final ChannelHandlerContext ctx);
  void handleAction(final ServerContext ctx, final ActionPacket packet);
}
