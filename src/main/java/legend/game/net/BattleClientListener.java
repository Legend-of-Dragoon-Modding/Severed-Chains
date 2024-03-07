package legend.game.net;

import io.netty.buffer.ByteBuf;

public interface BattleClientListener {
  void packetReceived(final ByteBuf buf);
}
