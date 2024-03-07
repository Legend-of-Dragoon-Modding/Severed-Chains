package legend.game.net;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import legend.game.types.GameState52c;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PacketManager {
  private static final Map<Class<?>, Supplier<Packet<?>>> classToPacket = new HashMap<>();
  private static final Int2ObjectMap<Supplier<Packet<?>>> idToPacket = new Int2ObjectOpenHashMap<>();

  static {
    registerPacket(GameState52c.class, GameStatePacket::new, GameStatePacket.ID);
  }

  private static <T> void registerPacket(final Class<T> cls, final Supplier<Packet<T>> constructor, final int id) {
    ((Map)classToPacket).put(cls, constructor);
    ((Map)idToPacket).put(id, constructor);
  }

  public static <T> Packet<T> getPacket(final T obj) {
    return (Packet<T>)classToPacket.get(obj.getClass()).get();
  }

  public static Packet<?> getPacket(final int id) {
    return idToPacket.get(id).get();
  }
}
