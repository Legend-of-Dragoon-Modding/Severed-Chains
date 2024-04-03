package legend.game.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BattleClientHandler extends ChannelInboundHandlerAdapter {
  private static final Logger LOGGER = LogManager.getFormatterLogger(BattleClientHandler.class);

  private final PacketManager<ClientContext> packetManager;

  public BattleClientHandler(final PacketManager<ClientContext> packetManager) {
    this.packetManager = packetManager;
  }

  @Override
  public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
    LOGGER.debug("Got client packet %s", msg.getClass());
    this.packetManager.handle(msg, new ClientContext(ctx));
  }

  @Override
  public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
