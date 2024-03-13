package legend.game.net;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class PacketManager<Context> {
  private static final Logger LOGGER = LogManager.getFormatterLogger(PacketManager.class);

  private final Object2IntMap<Class<?>> packetIds = new Object2IntOpenHashMap<>();
  private final Map<Class<?>, BiConsumer<?, ByteBuf>> packetSerializers = new HashMap<>();
  private final Int2ObjectMap<Function<ByteBuf, ?>> packetDeserializers = new Int2ObjectOpenHashMap<>();
  private final Map<Class<?>, BiConsumer<?, Context>> packetHandlers = new HashMap<>();

  private int id;

  public PacketManager(final Consumer<Registrar> registry) {
    registry.accept(new Registrar());
  }

  public class Registrar {
    public <Packet> void register(final Class<Packet> packetClass, final BiConsumer<Packet, ByteBuf> serializer, final Function<ByteBuf, Packet> deserializer, final BiConsumer<Packet, Context> handler) {
      final int id = PacketManager.this.id++;
      PacketManager.this.packetIds.put(packetClass, id);
      PacketManager.this.packetSerializers.put(packetClass, serializer);
      PacketManager.this.packetDeserializers.put(id, deserializer);
      PacketManager.this.packetHandlers.put(packetClass, handler);
    }

    public <Packet> void register(final Class<Packet> packetClass, final BiConsumer<Packet, ByteBuf> serializer, final Function<ByteBuf, Packet> deserializer) {
      final int id = PacketManager.this.id++;
      PacketManager.this.packetIds.put(packetClass, id);
      PacketManager.this.packetSerializers.put(packetClass, serializer);
      PacketManager.this.packetDeserializers.put(id, deserializer);
    }
  }

  public int getId(final Object packet) {
    return this.packetIds.getInt(packet.getClass());
  }

  public void serialize(final Object packet, final ByteBuf buffer) {
    final BiConsumer<Object, ByteBuf> serializer = (BiConsumer<Object, ByteBuf>)this.packetSerializers.get(packet.getClass());

    if(serializer == null) {
      LOGGER.error("No serializer for %s", packet.getClass());
      return;
    }

    serializer.accept(packet, buffer);
  }

  public Object deserialize(final int id, final ByteBuf buffer) {
    final Function<ByteBuf, Object> deserializer = (Function<ByteBuf, Object>)this.packetDeserializers.get(id);

    //TODO: what to do if no deserializer found?
    return deserializer.apply(buffer);
  }

  public void handle(final Object packet, final Context context) {
    final BiConsumer<Object, Context> handler = (BiConsumer<Object, Context>)this.packetHandlers.get(packet.getClass());

    if(handler != null) {
      handler.accept(packet, context);
    }
  }
}
