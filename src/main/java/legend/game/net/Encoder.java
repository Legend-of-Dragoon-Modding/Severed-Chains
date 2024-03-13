package legend.game.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.function.Function;

public class Encoder extends MessageToMessageEncoder<Object> {
  private final Function<Channel, PacketManager<?>> packetManagerGetter;

  public Encoder(final Function<Channel, PacketManager<?>> packetManagerGetter) {
    this.packetManagerGetter = packetManagerGetter;
  }

  @Override
  protected void encode(final ChannelHandlerContext ctx, final Object msg, final List<Object> out) {
    final PacketManager<?> packetManager = this.packetManagerGetter.apply(ctx.channel());

    //TODO use a single buffer

    final ByteBuf data = Unpooled.buffer();
    packetManager.serialize(msg, data);
    final int length = data.readableBytes();
    final ByteBuf b = Unpooled.buffer(length + 1);
    b.writeByte(packetManager.getId(msg));
    b.writeBytes(data);

    System.out.println("Sending " + length);

    out.add(b);
  }
}
