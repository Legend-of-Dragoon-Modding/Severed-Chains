package legend.game.net;

import io.netty.buffer.ByteBuf;

public class StartBattlePacket {
  public final int encounterId;
  public final int stageId;

  public StartBattlePacket(final int encounterId, final int stageId) {
    this.encounterId = encounterId;
    this.stageId = stageId;
  }

  public static void serialize(final StartBattlePacket packet, final ByteBuf buf) {
    buf.writeInt(packet.encounterId);
    buf.writeInt(packet.stageId);
  }

  public static StartBattlePacket deserialize(final ByteBuf buf) {
    final int encounterId = buf.readInt();
    final int stageId = buf.readInt();
    return new StartBattlePacket(encounterId, stageId);
  }
}
