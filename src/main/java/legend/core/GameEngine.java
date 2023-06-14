package legend.core;

import legend.core.gpu.Gpu;
import legend.core.memory.Memory;
import legend.core.memory.Value;
import legend.core.memory.segments.RamSegment;
import legend.core.opengl.Mesh;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderManager;
import legend.core.opengl.Texture;
import legend.core.opengl.Window;
import legend.core.opengl.fonts.Font;
import legend.core.opengl.fonts.FontManager;
import legend.core.opengl.fonts.TextStream;
import legend.core.spu.Spu;
import legend.game.Scus94491BpeSegment;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.Scus94491BpeSegment_8003;
import legend.game.Scus94491BpeSegment_8004;
import legend.game.Scus94491BpeSegment_800e;
import legend.game.fmv.Fmv;
import legend.game.i18n.LangManager;
import legend.game.modding.ModManager;
import legend.game.modding.events.EventManager;
import legend.game.modding.registries.Registries;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.SaveManager;
import legend.game.saves.serializers.RetailSerializer;
import legend.game.saves.serializers.V1Serializer;
import legend.game.saves.serializers.V2Serializer;
import legend.game.saves.serializers.V3Serializer;
import legend.game.scripting.ScriptManager;
import legend.game.sound.Sequencer;
import legend.game.types.LevelStuff08;
import legend.game.types.MagicStuff08;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Unpacker;
import legend.game.unpacker.UnpackerException;
import legend.game.unpacker.UnpackerStoppedRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;

import static legend.game.SItem.albertDragoonStats;
import static legend.game.SItem.albertDxpTable;
import static legend.game.SItem.albertXpTable_801138c0;
import static legend.game.SItem.albertCharacterStats;
import static legend.game.SItem.dartCharacterStats;
import static legend.game.SItem.dartDragoonStats;
import static legend.game.SItem.dartDxpTable;
import static legend.game.SItem.dartXpTable_801135e4;
import static legend.game.SItem.haschelCharacterStats;
import static legend.game.SItem.haschelDragoonStats;
import static legend.game.SItem.haschelDxpTable;
import static legend.game.SItem.haschelXpTable_801136d8;
import static legend.game.SItem.kongolCharacterStats;
import static legend.game.SItem.kongolDragoonStats;
import static legend.game.SItem.kongolDxpTable;
import static legend.game.SItem.kongolXpTable_801134f0;
import static legend.game.SItem.lavitzCharacterStats;
import static legend.game.SItem.lavitzDragoonStats;
import static legend.game.SItem.lavitzDxpTable;
import static legend.game.SItem.lavitzXpTable_801138c0;
import static legend.game.SItem.meruCharacterStats;
import static legend.game.SItem.meruDragoonStats;
import static legend.game.SItem.meruDxpTable;
import static legend.game.SItem.meruXpTable_801137cc;
import static legend.game.SItem.mirandaCharacterStats;
import static legend.game.SItem.mirandaDragoonStats;
import static legend.game.SItem.mirandaDxpTable;
import static legend.game.SItem.mirandaXpTable_80113aa8;
import static legend.game.SItem.roseCharacterStats;
import static legend.game.SItem.roseDragoonStats;
import static legend.game.SItem.roseDxpTable;
import static legend.game.SItem.roseXpTable_801139b4;
import static legend.game.SItem.shanaCharacterStats;
import static legend.game.SItem.shanaDragoonStats;
import static legend.game.SItem.shanaDxpTable;
import static legend.game.SItem.shanaXpTable_80113aa8;
import static legend.game.Scus94491BpeSegment.gameLoop;
import static org.lwjgl.opengl.GL11C.GL_BLEND;
import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11C.glBlendFunc;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glEnable;

public final class GameEngine {
  private GameEngine() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(GameEngine.class);

  public static final Memory MEMORY = new Memory();

  private static ModManager.Access MOD_ACCESS;
  private static LangManager.Access LANG_ACCESS;
  private static EventManager.Access EVENT_ACCESS;
  private static Registries.Access REGISTRY_ACCESS;
  public static final ModManager MODS = new ModManager(access -> MOD_ACCESS = access);
  public static final LangManager LANG = new LangManager(access -> LANG_ACCESS = access);
  public static final EventManager EVENTS = new EventManager(access -> EVENT_ACCESS = access);
  public static final Registries REGISTRIES = new Registries(access -> REGISTRY_ACCESS = access);

  public static final ScriptManager SCRIPTS = new ScriptManager();
  public static final Sequencer SEQUENCER = new Sequencer();

  public static final ConfigCollection CONFIG = new ConfigCollection();
  public static final SaveManager SAVES = new SaveManager(V3Serializer.MAGIC_V3, V3Serializer::toV3);

  public static final Cpu CPU;
  public static final Gpu GPU;
  public static final Spu SPU;

  public static final Thread hardwareThread;
  public static final Thread spuThread;

  static {
    try {
      if(!Config.exists()) {
        Config.save();
      } else {
        Config.load();
      }
    } catch(final IOException e) {
      LOGGER.warn("Failed to load config", e);
    }

    // --- User memory ------------------------

    MEMORY.addSegment(new RamSegment(0x0001_0000L, 0x8f_0000));
    MEMORY.addSegment(new RamSegment(0x1f80_0000L, 0x400));

    CPU = new Cpu();
    GPU = new Gpu();
    SPU = new Spu();

    hardwareThread = Thread.currentThread();
    hardwareThread.setName("Hardware");
    spuThread = new Thread(SPU);
    spuThread.setName("SPU");
  }

  private static final Value _80010000 = MEMORY.ref(4, 0x80010000L);

  private static final Object LOCK = new Object();

  private static Window.Events.Resize onResize;
  private static Window.Events.Key onKeyPress;
  private static Window.Events.Click onMouseRelease;
  private static Runnable onShutdown;

  private static Shader shader;
  private static Shader.UniformFloat shaderAlpha;
  private static Texture title1Texture;
  private static Texture title2Texture;
  private static Mesh fullScrenMesh;

  private static Shader eyeShader;
  private static Shader.UniformFloat eyeShaderAlpha;
  private static Shader.UniformFloat eyeShaderTicks;
  private static Texture eye;
  private static Mesh eyeMesh;

  private static Texture loadingTexture;
  private static Mesh loadingMesh;

  private static Font font;

  private static final Object statusTextLock = new Object();
  private static String newStatusText;
  private static TextStream statusText;
  private static float screenWidth;

  private static Shader.UniformBuffer transforms2;
  private static final Matrix4f identity = new Matrix4f();
  private static final Matrix4f eyeTransforms = new Matrix4f();
  private static final Matrix4f loadingTransforms = new Matrix4f();

  private static boolean loading;

  public static void start() throws IOException {
    final Thread thread = new Thread(() -> {
      try {
        LOGGER.info("--- Legend start ---");

        loading = true;
        GPU.mainRenderer = GameEngine::loadGfx;

        Files.createDirectories(Path.of("saves"));
        SAVES.registerDeserializer(RetailSerializer::fromRetailMatcher, RetailSerializer::fromRetail);
        SAVES.registerDeserializer(V1Serializer::fromV1Matcher, V1Serializer::fromV1);
        SAVES.registerDeserializer(V2Serializer::fromV2Matcher, V2Serializer::fromV2);
        SAVES.registerDeserializer(V3Serializer::fromV3Matcher, V3Serializer::fromV3);

        synchronized(LOCK) {
          Unpacker.setStatusListener(status -> {
            synchronized(statusTextLock) {
              newStatusText = status;
            }
          });

          try {
            Unpacker.unpack();
          } catch(final UnpackerException e) {
            newStatusText = "Failed to unpack files: " + e.getMessage();
            LOGGER.error("Failed to unpack files", e);
            skip();
            return;
          } catch(final UnpackerStoppedRuntimeException e) {
            LOGGER.info("Unpacking stopped");
            return;
          }

          MEMORY.setBytes(_80010000.getAddress(), Unpacker.loadFile("lod_engine").getBytes());

          MEMORY.addFunctions(Scus94491BpeSegment.class);
          MEMORY.addFunctions(Scus94491BpeSegment_8002.class);
          MEMORY.addFunctions(Scus94491BpeSegment_8003.class);
          MEMORY.addFunctions(Scus94491BpeSegment_8004.class);
          MEMORY.addFunctions(Scus94491BpeSegment_800e.class);

          loadStatTables();

          // Find and load all mods so their global config can be shown in the title screen options menu
          MOD_ACCESS.findMods();
          bootMods(MODS.getAllModIds());

          ConfigStorage.loadConfig(CONFIG, ConfigStorageLocation.GLOBAL, Path.of("config.dcnf"));

          Scus94491BpeSegment_8002.start();
          loading = false;
        }
      } catch(final Exception e) {
        throw new RuntimeException(e);
      }
    });

    time = System.nanoTime();
    thread.start();
    GPU.run();
  }

  /** Returns missing mod IDs, if any */
  public static Set<String> bootMods(final Set<String> modIds) {
    LOGGER.info("Booting mods...");

    MOD_ACCESS.reset();
    LANG_ACCESS.reset();
    EVENT_ACCESS.reset();
    REGISTRY_ACCESS.reset();

    LOGGER.info("Loading mods %s...", modIds);

    final Set<String> missingMods = MOD_ACCESS.loadMods(modIds);

    // Initialize language
    LANG_ACCESS.initialize(Locale.getDefault());

    // Initialize event bus and find all event handlers
    EVENT_ACCESS.initialize();

    // Initialize config registry and fire off config registry events
    REGISTRY_ACCESS.initialize(REGISTRIES.config);

    return missingMods;
  }

  public static void bootRegistries() {
    REGISTRY_ACCESS.initializeRemaining();
  }

  private static void loadStatTables() throws IOException {
    final FileData dartXp = new FileData(Files.readAllBytes(Paths.get("./files/characters/dart/xp")));
    final FileData lavitzXp = new FileData(Files.readAllBytes(Paths.get("./files/characters/lavitz/xp")));
    final FileData albertXp = new FileData(Files.readAllBytes(Paths.get("./files/characters/albert/xp")));
    final FileData shanaXp = new FileData(Files.readAllBytes(Paths.get("./files/characters/shana/xp")));
    final FileData mirandaXp = new FileData(Files.readAllBytes(Paths.get("./files/characters/miranda/xp")));
    final FileData roseXp = new FileData(Files.readAllBytes(Paths.get("./files/characters/rose/xp")));
    final FileData haschelXp = new FileData(Files.readAllBytes(Paths.get("./files/characters/haschel/xp")));
    final FileData kongolXp = new FileData(Files.readAllBytes(Paths.get("./files/characters/kongol/xp")));
    final FileData meruXp = new FileData(Files.readAllBytes(Paths.get("./files/characters/meru/xp")));
    final FileData dartDxp = new FileData(Files.readAllBytes(Paths.get("./files/characters/dart/dxp")));
    final FileData lavitzDxp = new FileData(Files.readAllBytes(Paths.get("./files/characters/lavitz/dxp")));
    final FileData albertDxp = new FileData(Files.readAllBytes(Paths.get("./files/characters/albert/dxp")));
    final FileData shanaDxp = new FileData(Files.readAllBytes(Paths.get("./files/characters/shana/dxp")));
    final FileData mirandaDxp = new FileData(Files.readAllBytes(Paths.get("./files/characters/miranda/dxp")));
    final FileData roseDxp = new FileData(Files.readAllBytes(Paths.get("./files/characters/rose/dxp")));
    final FileData haschelDxp = new FileData(Files.readAllBytes(Paths.get("./files/characters/haschel/dxp")));
    final FileData kongolDxp = new FileData(Files.readAllBytes(Paths.get("./files/characters/kongol/dxp")));
    final FileData meruDxp = new FileData(Files.readAllBytes(Paths.get("./files/characters/meru/dxp")));
    final FileData dartStats = new FileData(Files.readAllBytes(Paths.get("./files/characters/dart/stats")));
    final FileData lavitzStats = new FileData(Files.readAllBytes(Paths.get("./files/characters/lavitz/stats")));
    final FileData albertStats = new FileData(Files.readAllBytes(Paths.get("./files/characters/albert/stats")));
    final FileData shanaStats = new FileData(Files.readAllBytes(Paths.get("./files/characters/shana/stats")));
    final FileData mirandaStats = new FileData(Files.readAllBytes(Paths.get("./files/characters/miranda/stats")));
    final FileData roseStats = new FileData(Files.readAllBytes(Paths.get("./files/characters/rose/stats")));
    final FileData haschelStats = new FileData(Files.readAllBytes(Paths.get("./files/characters/haschel/stats")));
    final FileData kongolStats = new FileData(Files.readAllBytes(Paths.get("./files/characters/kongol/stats")));
    final FileData meruStats = new FileData(Files.readAllBytes(Paths.get("./files/characters/meru/stats")));
    final FileData dartDstats = new FileData(Files.readAllBytes(Paths.get("./files/characters/dart/dstats")));
    final FileData lavitzDstats = new FileData(Files.readAllBytes(Paths.get("./files/characters/lavitz/dstats")));
    final FileData albertDstats = new FileData(Files.readAllBytes(Paths.get("./files/characters/albert/dstats")));
    final FileData shanaDstats = new FileData(Files.readAllBytes(Paths.get("./files/characters/shana/dstats")));
    final FileData mirandaDstats = new FileData(Files.readAllBytes(Paths.get("./files/characters/miranda/dstats")));
    final FileData roseDstats = new FileData(Files.readAllBytes(Paths.get("./files/characters/rose/dstats")));
    final FileData haschelDstats = new FileData(Files.readAllBytes(Paths.get("./files/characters/haschel/dstats")));
    final FileData kongolDstats = new FileData(Files.readAllBytes(Paths.get("./files/characters/kongol/dstats")));
    final FileData meruDstats = new FileData(Files.readAllBytes(Paths.get("./files/characters/meru/dstats")));

    for(int i = 0; i < dartXpTable_801135e4.length; i++) {
      dartXpTable_801135e4[i] = dartXp.readInt(i * 4);
    }

    for(int i = 0; i < lavitzXpTable_801138c0.length; i++) {
      lavitzXpTable_801138c0[i] = lavitzXp.readInt(i * 4);
    }

    for(int i = 0; i < albertXpTable_801138c0.length; i++) {
      albertXpTable_801138c0[i] = albertXp.readInt(i * 4);
    }

    for(int i = 0; i < shanaXpTable_80113aa8.length; i++) {
      shanaXpTable_80113aa8[i] = shanaXp.readInt(i * 4);
    }

    for(int i = 0; i < mirandaXpTable_80113aa8.length; i++) {
      mirandaXpTable_80113aa8[i] = mirandaXp.readInt(i * 4);
    }

    for(int i = 0; i < roseXpTable_801139b4.length; i++) {
      roseXpTable_801139b4[i] = roseXp.readInt(i * 4);
    }

    for(int i = 0; i < haschelXpTable_801136d8.length; i++) {
      haschelXpTable_801136d8[i] = haschelXp.readInt(i * 4);
    }

    for(int i = 0; i < kongolXpTable_801134f0.length; i++) {
      kongolXpTable_801134f0[i] = kongolXp.readInt(i * 4);
    }

    for(int i = 0; i < meruXpTable_801137cc.length; i++) {
      meruXpTable_801137cc[i] = meruXp.readInt(i * 4);
    }

    for(int i = 0; i < dartDxpTable.length; i++) {
      dartDxpTable[i] = dartDxp.readUShort(i * 2);
    }

    for(int i = 0; i < lavitzDxpTable.length; i++) {
      lavitzDxpTable[i] = lavitzDxp.readUShort(i * 2);
    }

    for(int i = 0; i < albertDxpTable.length; i++) {
      albertDxpTable[i] = albertDxp.readUShort(i * 2);
    }

    for(int i = 0; i < shanaDxpTable.length; i++) {
      shanaDxpTable[i] = shanaDxp.readUShort(i * 2);
    }

    for(int i = 0; i < mirandaDxpTable.length; i++) {
      mirandaDxpTable[i] = mirandaDxp.readUShort(i * 2);
    }

    for(int i = 0; i < roseDxpTable.length; i++) {
      roseDxpTable[i] = roseDxp.readUShort(i * 2);
    }

    for(int i = 0; i < haschelDxpTable.length; i++) {
      haschelDxpTable[i] = haschelDxp.readUShort(i * 2);
    }

    for(int i = 0; i < meruDxpTable.length; i++) {
      meruDxpTable[i] = meruDxp.readUShort(i * 2);
    }

    for(int i = 0; i < kongolDxpTable.length; i++) {
      kongolDxpTable[i] = kongolDxp.readUShort(i * 2);
    }

    for (int i = 0; i < dartCharacterStats.length; i++) {
      dartCharacterStats[i] = new LevelStuff08(dartStats.readUShort(i * 8), dartStats.readByte(i * 8 + 2), dartStats.readUByte(i * 8 + 3), dartStats.readUByte(i * 8 + 4), dartStats.readUByte(i * 8 + 5), dartStats.readUByte(i * 8 + 6), dartStats.readUByte(i * 8 + 7));
    }

    for (int i = 0; i < lavitzCharacterStats.length; i++) {
      lavitzCharacterStats[i] = new LevelStuff08(lavitzStats.readUShort(i * 8), lavitzStats.readByte(i * 8 + 2), lavitzStats.readUByte(i * 8 + 3), lavitzStats.readUByte(i * 8 + 4), lavitzStats.readUByte(i * 8 + 5), lavitzStats.readUByte(i * 8 + 6), lavitzStats.readUByte(i * 8 + 7));
    }

    for (int i = 0; i < albertCharacterStats.length; i++) {
      albertCharacterStats[i] = new LevelStuff08(albertStats.readUShort(i * 8), albertStats.readByte(i * 8 + 2), albertStats.readUByte(i * 8 + 3), albertStats.readUByte(i * 8 + 4), albertStats.readUByte(i * 8 + 5), albertStats.readUByte(i * 8 + 6), albertStats.readUByte(i * 8 + 7));
    }

    for (int i = 0; i < shanaCharacterStats.length; i++) {
      shanaCharacterStats[i] = new LevelStuff08(shanaStats.readUShort(i * 8), shanaStats.readByte(i * 8 + 2), shanaStats.readUByte(i * 8 + 3), shanaStats.readUByte(i * 8 + 4), shanaStats.readUByte(i * 8 + 5), shanaStats.readUByte(i * 8 + 6), shanaStats.readUByte(i * 8 + 7));
    }

    for (int i = 0; i < mirandaCharacterStats.length; i++) {
      mirandaCharacterStats[i] = new LevelStuff08(mirandaStats.readUShort(i * 8), mirandaStats.readByte(i * 8 + 2), mirandaStats.readUByte(i * 8 + 3), mirandaStats.readUByte(i * 8 + 4), mirandaStats.readUByte(i * 8 + 5), mirandaStats.readUByte(i * 8 + 6), mirandaStats.readUByte(i * 8 + 7));
    }

    for (int i = 0; i < roseCharacterStats.length; i++) {
      roseCharacterStats[i] = new LevelStuff08(roseStats.readUShort(i * 8), roseStats.readByte(i * 8 + 2), roseStats.readUByte(i * 8 + 3), roseStats.readUByte(i * 8 + 4), roseStats.readUByte(i * 8 + 5), roseStats.readUByte(i * 8 + 6), roseStats.readUByte(i * 8 + 7));
    }

    for (int i = 0; i < haschelCharacterStats.length; i++) {
      haschelCharacterStats[i] = new LevelStuff08(haschelStats.readUShort(i * 8), haschelStats.readByte(i * 8 + 2), haschelStats.readUByte(i * 8 + 3), haschelStats.readUByte(i * 8 + 4), haschelStats.readUByte(i * 8 + 5), haschelStats.readUByte(i * 8 + 6), haschelStats.readUByte(i * 8 + 7));
    }

    for (int i = 0; i < meruCharacterStats.length; i++) {
      meruCharacterStats[i] = new LevelStuff08(meruStats.readUShort(i * 8), meruStats.readByte(i * 8 + 2), meruStats.readUByte(i * 8 + 3), meruStats.readUByte(i * 8 + 4), meruStats.readUByte(i * 8 + 5), meruStats.readUByte(i * 8 + 6), meruStats.readUByte(i * 8 + 7));
    }

    for (int i = 0; i < kongolCharacterStats.length; i++) {
      kongolCharacterStats[i] = new LevelStuff08(kongolStats.readUShort(i * 8), kongolStats.readByte(i * 8 + 2), kongolStats.readUByte(i * 8 + 3), kongolStats.readUByte(i * 8 + 4), kongolStats.readUByte(i * 8 + 5), kongolStats.readUByte(i * 8 + 6), kongolStats.readUByte(i * 8 + 7));
    }

    for (int i = 0; i < dartDragoonStats.length; i++) {
      dartDragoonStats[i] = new MagicStuff08(dartDstats.readUShort(i * 8), dartDstats.readByte(i * 8 + 2), dartDstats.readUByte(i * 8 + 3), dartDstats.readUByte(i * 8 + 4), dartDstats.readUByte(i * 8 + 5), dartDstats.readUByte(i * 8 + 6), dartDstats.readUByte(i * 8 + 7));
    }

    for (int i = 0; i < lavitzDragoonStats.length; i++) {
      lavitzDragoonStats[i] = new MagicStuff08(lavitzDstats.readUShort(i * 8), lavitzDstats.readByte(i * 8 + 2), lavitzDstats.readUByte(i * 8 + 3), lavitzDstats.readUByte(i * 8 + 4), lavitzDstats.readUByte(i * 8 + 5), lavitzDstats.readUByte(i * 8 + 6), lavitzDstats.readUByte(i * 8 + 7));
    }

    for (int i = 0; i < albertDragoonStats.length; i++) {
      albertDragoonStats[i] = new MagicStuff08(albertDstats.readUShort(i * 8), albertDstats.readByte(i * 8 + 2), albertDstats.readUByte(i * 8 + 3), albertDstats.readUByte(i * 8 + 4), albertDstats.readUByte(i * 8 + 5), albertDstats.readUByte(i * 8 + 6), albertDstats.readUByte(i * 8 + 7));
    }

    for (int i = 0; i < shanaDragoonStats.length; i++) {
      shanaDragoonStats[i] = new MagicStuff08(shanaDstats.readUShort(i * 8), shanaDstats.readByte(i * 8 + 2), shanaDstats.readUByte(i * 8 + 3), shanaDstats.readUByte(i * 8 + 4), shanaDstats.readUByte(i * 8 + 5), shanaDstats.readUByte(i * 8 + 6), shanaDstats.readUByte(i * 8 + 7));
    }

    for (int i = 0; i < mirandaDragoonStats.length; i++) {
      mirandaDragoonStats[i] = new MagicStuff08(mirandaDstats.readUShort(i * 8), mirandaDstats.readByte(i * 8 + 2), mirandaDstats.readUByte(i * 8 + 3), mirandaDstats.readUByte(i * 8 + 4), mirandaDstats.readUByte(i * 8 + 5), mirandaDstats.readUByte(i * 8 + 6), mirandaDstats.readUByte(i * 8 + 7));
    }

    for (int i = 0; i < roseDragoonStats.length; i++) {
      roseDragoonStats[i] = new MagicStuff08(roseDstats.readUShort(i * 8), roseDstats.readByte(i * 8 + 2), roseDstats.readUByte(i * 8 + 3), roseDstats.readUByte(i * 8 + 4), roseDstats.readUByte(i * 8 + 5), roseDstats.readUByte(i * 8 + 6), roseDstats.readUByte(i * 8 + 7));
    }

    for (int i = 0; i < haschelDragoonStats.length; i++) {
      haschelDragoonStats[i] = new MagicStuff08(haschelDstats.readUShort(i * 8), haschelDstats.readByte(i * 8 + 2), haschelDstats.readUByte(i * 8 + 3), haschelDstats.readUByte(i * 8 + 4), haschelDstats.readUByte(i * 8 + 5), haschelDstats.readUByte(i * 8 + 6), haschelDstats.readUByte(i * 8 + 7));
    }

    for (int i = 0; i < meruDragoonStats.length; i++) {
      meruDragoonStats[i] = new MagicStuff08(meruDstats.readUShort(i * 8), meruDstats.readByte(i * 8 + 2), meruDstats.readUByte(i * 8 + 3), meruDstats.readUByte(i * 8 + 4), meruDstats.readUByte(i * 8 + 5), meruDstats.readUByte(i * 8 + 6), meruDstats.readUByte(i * 8 + 7));
    }

    for (int i = 0; i < kongolDragoonStats.length; i++) {
      kongolDragoonStats[i] = new MagicStuff08(kongolDstats.readUShort(i * 8), kongolDstats.readByte(i * 8 + 2), kongolDstats.readUByte(i * 8 + 3), kongolDstats.readUByte(i * 8 + 4), kongolDstats.readUByte(i * 8 + 5), kongolDstats.readUByte(i * 8 + 6), kongolDstats.readUByte(i * 8 + 7));
    }
  }

  private static void transitionToGame() {
    glDisable(GL_BLEND);
    transforms2.set(identity);

    shaderAlpha = null;

    if(shader != null) {
      shader.delete();
      shader = null;
    }

    if(title1Texture != null) {
      title1Texture.delete();
      title1Texture = null;
    }

    if(title2Texture != null) {
      title2Texture.delete();
      title2Texture = null;
    }

    if(fullScrenMesh != null) {
      fullScrenMesh.delete();
      fullScrenMesh = null;
    }

    eyeShaderAlpha = null;
    eyeShaderTicks = null;

    if(eyeShader != null) {
      eyeShader.delete();
      eyeShader = null;
    }

    if(eye != null) {
      eye.delete();
      eye = null;
    }

    if(eyeMesh != null) {
      eyeMesh.delete();
      eyeMesh = null;
    }

    if(loadingTexture != null) {
      loadingTexture.delete();
      loadingTexture = null;
    }

    if(loadingMesh != null) {
      loadingMesh.delete();
      loadingMesh = null;
    }

    if(font != null) {
      font = null;
    }

    if(onResize != null) {
      GPU.window().events.removeOnResize(onResize);
      onResize = null;
    }

    if(onKeyPress != null) {
      GPU.window().events.removeKeyPress(onKeyPress);
      onKeyPress = null;
    }

    if(onMouseRelease != null) {
      GPU.window().events.removeMouseRelease(onMouseRelease);
      onMouseRelease = null;
    }

    if(onShutdown != null) {
      GPU.window().events.removeShutdown(onShutdown);
      onShutdown = null;
    }

    spuThread.start();
    GPU.setStandardRenderer();

    synchronized(LOCK) {
      Fmv.playCurrentFmv();
      gameLoop();
    }
  }

  private static void loadGfx() {
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    final Path vsh = Paths.get("gfx", "shaders", "vram.vsh");
    shader = loadShader(vsh, Paths.get("gfx", "shaders", "title.fsh"));
    shader.use();
    shaderAlpha = shader.new UniformFloat("alpha");

    title1Texture = Texture.png(Path.of(".", "gfx", "textures", "intro", "title1.png"));
    title2Texture = Texture.png(Path.of(".", "gfx", "textures", "intro", "title2.png"));
    loadingTexture = Texture.png(Path.of(".", "gfx", "textures", "intro", "loading.png"));
    eye = Texture.png(Path.of(".", "gfx", "textures", "loading.png"));

    eyeShader = loadShader(vsh, Paths.get("gfx", "shaders", "loading.fsh"));
    eyeShader.use();
    eyeShaderAlpha = eyeShader.new UniformFloat("alpha");
    eyeShaderTicks = eyeShader.new UniformFloat("ticks");

    eyeMesh = new Mesh(GL_TRIANGLE_STRIP, new float[] {
       0,  0, 0, 0,
       0, 32, 0, 1,
      32,  0, 1, 0,
      32, 32, 1, 1,
    }, 4);
    eyeMesh.attribute(0, 0L, 2, 4);
    eyeMesh.attribute(1, 2L, 2, 4);

    loadingMesh = new Mesh(GL_TRIANGLE_STRIP, new float[] {
        0,  0, 0, 0,
        0, 32, 0, 1,
      130,  0, 1, 0,
      130, 32, 1, 1,
    }, 4);
    loadingMesh.attribute(0, 0L, 2, 4);
    loadingMesh.attribute(1, 2L, 2, 4);

    transforms2 = ShaderManager.getUniformBuffer("transforms2");
    identity.identity();

    font = FontManager.get("default");

    onResize = GPU.window().events.onResize(GameEngine::windowResize);
    windowResize(GPU.window(), (int)(GPU.window().getWidth() * GPU.window().getScale()), (int)(GPU.window().getHeight() * GPU.window().getScale()));
    GPU.mainRenderer = GameEngine::renderIntro;

    onKeyPress = GPU.window().events.onKeyPress((window, key, scancode, mods) -> skip());
    onMouseRelease = GPU.window().events.onMouseRelease((window, x, y, button, mods) -> skip());
    onShutdown = GPU.window().events.onShutdown(Unpacker::stop);
  }

  private static void skip() {
    time = 0;
    fade1 = 0.0f;
    fade2 = 0.0f;
    loadingFade = 1.0f;
    eyeFade = 1.0f;
  }

  private static long time;
  private static float fade1;
  private static float fade2;
  private static float loadingFade;
  private static float eyeFade;

  private static void renderIntro() {
    final long deltaMs = (System.nanoTime() - time) / 1_000_000;

    if(deltaMs < 5000) {
      fade1 += 0.005f;
      if(fade1 > 1.0f) {
        fade1 = 1.0f;
      }
      fade2 = fade1;
      eyeFade = fade1;
    } else if(deltaMs < 13000) {
      fade1 -= 0.0015f;
      if(fade1 < 0) {
        fade1 = 0;
      }
    } else if(deltaMs < 17000) {
      fade1 -= 0.01f;
      if(fade1 < 0) {
        fade1 = 0;
      }

      fade2 -= 0.02f;
      if(fade2 < 0) {
        fade2 = 0;
      }

      loadingFade += 0.02f;
      if(loadingFade > 1.0f) {
        loadingFade = 1.0f;
      }
    } else {
      if(!loading) {
        transitionToGame();
        return;
      }
    }

    shader.use();

    transforms2.set(identity);
    shaderAlpha.set(fade1 * fade1 * fade1);
    title1Texture.use();
    fullScrenMesh.draw();

    shaderAlpha.set(fade2 * fade2 * fade2);
    title2Texture.use();
    fullScrenMesh.draw();

    if(loading) {
      transforms2.set(eyeTransforms);
      eyeShader.use();
      eyeShaderAlpha.set(eyeFade);
      eyeShaderTicks.set(deltaMs / 10_000.0f);
      eye.use();
      eyeMesh.draw();

      transforms2.set(loadingTransforms);
      shader.use();
      shaderAlpha.set(loadingFade);
      loadingTexture.use();
      loadingMesh.draw();
    }

    synchronized(statusTextLock) {
      if(newStatusText != null) {
        if(statusText != null) {
          statusText.delete();
        }

        statusText = font.text(stream -> stream.text(newStatusText));
      }

      newStatusText = null;
    }

    if(statusText != null && loadingFade != 0.0f) {
      statusText.setColour(loadingFade, loadingFade, loadingFade);
      statusText.draw((screenWidth - statusText.width()) / 2, 100.0f);
    }
  }

  private static void windowResize(final Window window, final int width, final int height) {
    final float windowScale = window.getScale();
    final float unscaledWidth = width / windowScale;
    final float unscaledHeight = height / windowScale;

    if(fullScrenMesh != null) {
      fullScrenMesh.delete();
    }

    final float aspect = (float)4 / 3;

    float w = unscaledWidth;
    float h = w / aspect;

    if(h > unscaledHeight) {
      h = unscaledHeight;
      w = h * aspect;
    }

    screenWidth = w;

    final float l = (unscaledWidth - w) / 2;
    final float t = (unscaledHeight - h) / 2;
    final float r = l + w;
    final float b = t + h;

    fullScrenMesh = new Mesh(GL_TRIANGLE_STRIP, new float[] {
      l, t, 0, 0,
      l, b, 0, 1,
      r, t, 1, 0,
      r, b, 1, 1,
    }, 4);
    fullScrenMesh.attribute(0, 0L, 2, 4);
    fullScrenMesh.attribute(1, 2L, 2, 4);

    eyeTransforms.translation(10.0f, unscaledHeight - 42.0f, 0.0f);
    loadingTransforms.translation(46.0f, unscaledHeight - 42.0f, 0.0f);
  }

  private static Shader loadShader(final Path vsh, final Path fsh) {
    final Shader shader;

    try {
      shader = new Shader(vsh, fsh);
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load vram shader", e);
    }

    shader.bindUniformBlock("transforms", Shader.UniformBuffer.TRANSFORM);
    shader.bindUniformBlock("transforms2", Shader.UniformBuffer.TRANSFORM2);
    return shader;
  }
}
