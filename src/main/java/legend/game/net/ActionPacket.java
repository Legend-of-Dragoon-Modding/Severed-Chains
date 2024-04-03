package legend.game.net;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class ActionPacket {
  public final BattleAction action;

  public ActionPacket(final BattleAction action) {
    this.action = action;
  }

  public static void serialize(final ActionPacket packet, final ByteBuf buf) {
    buf.writeInt(packet.action.name().length());
    buf.writeCharSequence(packet.action.name(), StandardCharsets.UTF_8);
  }

  public static ActionPacket deserialize(final ByteBuf buf) {
    final int actionLen = buf.readInt();
    final BattleAction action = BattleAction.valueOf(buf.readCharSequence(actionLen, StandardCharsets.UTF_8).toString());
    return new ActionPacket(action);
  }
}
