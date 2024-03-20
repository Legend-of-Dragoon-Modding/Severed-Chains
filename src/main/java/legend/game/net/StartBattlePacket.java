package legend.game.net;

import io.netty.buffer.ByteBuf;

public class StartBattlePacket {
  public final int encounterId;
  public final int stageId;
  public final int charSlot;

  public StartBattlePacket(final int encounterId, final int stageId, final int charSlot) {
    this.encounterId = encounterId;
    this.stageId = stageId;
    this.charSlot = charSlot;
  }

  public static void serialize(final StartBattlePacket packet, final ByteBuf buf) {
    buf.writeInt(packet.encounterId);
    buf.writeInt(packet.stageId);
    buf.writeInt(packet.charSlot);
  }

  public static StartBattlePacket deserialize(final ByteBuf buf) {
    final int encounterId = buf.readInt();
    final int stageId = buf.readInt();
    final int charSlot = buf.readInt();
    return new StartBattlePacket(encounterId, stageId, charSlot);
  }
}
