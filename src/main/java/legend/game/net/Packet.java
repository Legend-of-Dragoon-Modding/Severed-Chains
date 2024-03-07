package legend.game.net;

import io.netty.buffer.ByteBuf;

public interface Packet<T> {
  int id();
  void write(final ByteBuf buf, final T obj);
  T read(final ByteBuf buf);
}
