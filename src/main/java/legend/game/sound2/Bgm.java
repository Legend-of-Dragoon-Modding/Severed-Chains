package legend.game.sound2;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import legend.game.unpacker.FileData;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public final class Bgm {
  private static final byte[] SSSQ = {0x53, 0x53, 0x73, 0x71};
  private final Int2ObjectMap<Channel> channels = new Int2ObjectArrayMap<>();
  private final ByteBuffer sequence;

  public Bgm(final FileData sssqData, final FileData sshdData, final FileData soundBankData) {
    final SoundBank soundBank = new SoundBank(soundBankData);
    final Sshd sshd = new Sshd(sshdData, soundBank);

    final ByteBuffer data = ByteBuffer.wrap(sssqData.data()).order(ByteOrder.LITTLE_ENDIAN).asReadOnlyBuffer();

    data.position(4);
    //Read as int_24
    final int tempo = data.getInt() << 8 >> 8;

    data.position(12);
    final byte[] sshdFlag = new byte[4];
    data.get(sshdFlag);

    assert Arrays.equals(sshdFlag, SSSQ) : "Not a SSsq file.";

    final byte[] channel = new byte[16];
    for (int i = 0; i < 16; i++) {
      data.get(channel);

      if((channel[2] & 0xff) != 0xff) {
        this.channels.put(i, new Channel(channel, sshd));
      }
    }

    this.sequence = data.slice(data.position(), data.remaining()).order(ByteOrder.LITTLE_ENDIAN);
  }

  public void tick() {
    //TODO send commands from sequence

    final int deltaTime = 180; //TODO actual delta time

    for (final Channel channel : this.channels.values()) {
      channel.tick(deltaTime);
    }
  }


  public void gracefulStop() {

  }

  public void stop() {
    for (final Channel channel : this.channels.values()) {
      channel.stop();
    }
  }

  public void pause() {

  }

  public void destroy() {
    for (final Channel channel : this.channels.values()) {
      channel.destroy();
    }
  }
}
