package legend.game.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {
  @Override
  protected void decode(final ChannelHandlerContext ctx, final ByteBuf buf, final List<Object> list) {
    final Packet<?> packet = PacketManager.getPacket(buf.readInt());
    list.add(packet.read(buf));
  }
}
