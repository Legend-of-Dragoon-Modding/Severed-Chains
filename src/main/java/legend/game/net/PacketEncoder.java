package legend.game.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Object> {
  @Override
  protected void encode(final ChannelHandlerContext ctx, final Object obj, final ByteBuf buf) {
    final Packet<Object> packet = PacketManager.getPacket(obj);
    buf.writeInt(packet.id());
    packet.write(buf, obj);
  }
}
