package legend.game.scriptdebugger;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import legend.game.types.BigStruct;
import legend.game.types.ScriptState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.scriptState_800bc0c0;

public class ScriptDebugger {
  private static final Logger LOGGER = LogManager.getFormatterLogger(ScriptDebugger.class);

  private final Pattern websocketKey = Pattern.compile("Sec-WebSocket-Key: (.*)");
  private final MessageDigest sha1 = MessageDigest.getInstance("SHA-1");

  private final AsynchronousServerSocketChannel socket;

  private final Set<Socket> clients = new HashSet<>();

  private final Int2ObjectMap<Set<Socket>> scriptWatchers = new Int2ObjectOpenHashMap<>();

  public ScriptDebugger(final int port) throws IOException, NoSuchAlgorithmException {
    this.socket = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));

    this.socket.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
      public void completed(final AsynchronousSocketChannel ch, final Void att) {
        ScriptDebugger.this.socket.accept(null, this);
        ScriptDebugger.this.acceptConnection(ch);
      }

      public void failed(final Throwable exc, final Void att) {
        LOGGER.warn("Socket accept error", exc);
      }
    });

    LOGGER.info("Debugger listening on port %d", port);
  }

  private void acceptConnection(final AsynchronousSocketChannel channel) {
    try {
      LOGGER.info("Accepting connection from %s", channel.getRemoteAddress());
    } catch(IOException e) {
      return;
    }

    final Socket socket = new Socket(channel);

    synchronized(this.clients) {
      this.clients.add(socket);
    }

    socket.read(new CompletionHandler<>() {
      @Override
      public void completed(final Integer result, final ScriptDebugger.Socket attachment) {
        socket.readBuffer.flip();

        final byte[] data = new byte[result];
        socket.readBuffer.get(data);
        final String response = new String(data);

        if(response.startsWith("GET")) {
          final Matcher match = ScriptDebugger.this.websocketKey.matcher(response);
          match.find();
          final byte[] output = ("HTTP/1.1 101 Switching Protocols\r\n"
            + "Connection: Upgrade\r\n"
            + "Upgrade: websocket\r\n"
            + "Sec-WebSocket-Accept: "
            + Base64.getEncoder().encodeToString(sha1.digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes(StandardCharsets.UTF_8)))
            + "\r\n\r\n").getBytes(StandardCharsets.UTF_8);

          final ByteBuffer outBuffer = ByteBuffer.allocateDirect(output.length);
          outBuffer.put(output);
          outBuffer.flip();
          socket.socket.write(outBuffer);

          ScriptDebugger.this.sendScriptList(socket);
          socket.waitForPacket();
        }
      }

      @Override
      public void failed(final Throwable e, final ScriptDebugger.Socket attachment) {
        LOGGER.warn("Socket failed to read", e);
      }
    });
  }

  private void closeConnection(final Socket socket) {
    this.clients.remove(socket);

    for(final Set<Socket> watchers : this.scriptWatchers.values()) {
      watchers.remove(socket);
    }

    try {
      socket.close();
    } catch(final IOException ignored) { }
  }

  private void handlePacket(final Socket socket, final String packetId, final JSONObject data) {
    switch(packetId) {
      case "script-selected" -> {
        final int index = Integer.parseUnsignedInt((String)data.get("index"));
        this.scriptWatchers.computeIfAbsent(index, key -> new HashSet<>()).add(socket);
      }
    }
  }

  private void sendScriptList(final Socket socket) {
    final JSONObject scripts = new JSONObject();

    for(int index = 0; index < 0x48; index++) {
      final ScriptState<BigStruct> scriptState = scriptStatePtrArr_800bc1c0.get(index).derefAs(ScriptState.classFor(BigStruct.class));

      if(scriptState.getAddress() != scriptState_800bc0c0.getAddress() && (scriptState.ui_60.get() & 0x12_0000L) == 0) {
        scripts.put(index, Long.toHexString(scriptState.scriptPtr_14.getPointer())); //TODO better identifier for script name
      }
    }

    final JSONObject packet = new JSONObject();
    packet.put("script-list", scripts);

    socket.sendPacket(packet);
  }

  private class Socket {
    private final ByteBuffer readBuffer = ByteBuffer.allocateDirect(1500);
    private final ByteBuffer writeBuffer = ByteBuffer.allocateDirect(1500);
    private final AsynchronousSocketChannel socket;

    public Socket(final AsynchronousSocketChannel socket) {
      this.socket = socket;
    }

    public void close() throws IOException {
      this.socket.close();
    }

    public void read(final CompletionHandler<Integer, Socket> handler) {
      this.readBuffer.clear();
      this.socket.read(this.readBuffer, this, handler);
    }

    public void waitForPacket() {
      this.read(new PacketHandler());
    }

    public void sendPacket(final JSONObject packet) {
      this.writeBuffer.clear();
      final String out = packet.toJSONString();

      // Header
      this.writeBuffer.put((byte)0x81);

      // Length
      if(out.length() <= 125) {
        this.writeBuffer.put((byte)(out.length()));
      } else if(out.length() <= 0xffff) {
        this.writeBuffer.put((byte)126);
        this.writeBuffer.putShort((short)out.length());
      } else {
        this.writeBuffer.put((byte)127);
        this.writeBuffer.putLong(out.length());
      }

      // Data
      this.writeBuffer.put(out.getBytes());

      this.writeBuffer.flip();
      this.socket.write(this.writeBuffer);
    }
  }

  private class PacketHandler implements CompletionHandler<Integer, ScriptDebugger.Socket> {
    @Override
    public void completed(final Integer result, final ScriptDebugger.Socket socket) {
      socket.readBuffer.flip();

      final byte[] data = new byte[result];
      socket.readBuffer.get(data);

      try {
        final Object obj = new JSONParser().parse(this.decode(data));

        if(!(obj instanceof JSONObject)) {
          throw new ParseException(ParseException.ERROR_UNEXPECTED_EXCEPTION);
        }

        for(final Object e : ((JSONObject)obj).entrySet()) {
          final Map.Entry<String, JSONObject> entry = (Map.Entry<String, JSONObject>)e;

          handlePacket(socket, entry.getKey(), entry.getValue());
        }
      } catch(final ParseException e) {
        LOGGER.warn("Invalid packet");
      }

      socket.waitForPacket();
    }

    @Override
    public void failed(final Throwable e, final ScriptDebugger.Socket socket) {
      LOGGER.warn("Socket failed to read", e);
      ScriptDebugger.this.closeConnection(socket);
    }

    private String decode(final byte[] data) {
      if((data[0] & 0xff) != 0x81) {
        LOGGER.warn("Can only handle single packets");
        return "";
      }

      final boolean masked = (data[1] & 0x80) != 0;
      final int lenType = (data[1] & 0x7f);
      final int padStart;
      final int dataStart;
      final int length;
      if(lenType <= 125) {
        length = lenType;
        padStart = 2;
      } else if(lenType == 126) {
        length = (data[3] & 0xff) << 8 | data[2] & 0xff;
        padStart = 4;
      } else {
        length = (data[5] & 0xff) << 24 | (data[4] & 0xff) << 16 | (data[3] & 0xff) << 8 | data[2] & 0xff;
        padStart = 10;
      }

      dataStart = masked ? padStart + 4 : padStart;

      if(masked) {
        for(int i = dataStart; i < data.length; i++) {
          data[i] = (byte)(data[i] ^ data[padStart + ((i - dataStart) & 0x3)]);
        }
      }

      return new String(data, dataStart, length);
    }
  }
}
