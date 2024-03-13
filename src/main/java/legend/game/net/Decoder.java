package legend.game.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
import java.util.function.Function;

public class Decoder extends MessageToMessageDecoder<ByteBuf> {
  private final Function<Channel, PacketManager> packetManagerGetter;

  public Decoder(final Function<Channel, PacketManager> packetManagerGetter) {
    this.packetManagerGetter = packetManagerGetter;
  }

  @Override
  protected void decode(final ChannelHandlerContext ctx, final ByteBuf msg, final List<Object> out) {
    final byte index = msg.readByte();

    System.out.println("Received " + msg.readableBytes());

    try {
      final Object packet = this.packetManagerGetter.apply(ctx.channel()).deserialize(index, msg);
      out.add(packet);
    } catch(final IndexOutOfBoundsException e) {
      //TODO: disconnect or do something here
      e.printStackTrace();
    }
  }
}
