package legend.game.net;

import io.netty.buffer.ByteBuf;

public class StartTurnPacket {
  public final int bentId;

  public StartTurnPacket(final int bentId) {
    this.bentId = bentId;
  }

  public static void serialize(final StartTurnPacket packet, final ByteBuf buf) {
    buf.writeInt(packet.bentId);
  }

  public static StartTurnPacket deserialize(final ByteBuf buf) {
    final int bentId = buf.readInt();
    return new StartTurnPacket(bentId);
  }
}
