package legend.game.dabas;

import legend.core.QueuedModelStandard;
import legend.core.audio.GenericSource;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.Texture;
import legend.core.platform.WindowEvents;
import legend.game.EngineState;
import legend.game.unpacker.Loader;
import org.joml.Vector2i;
import org.joml.Vector3i;
import org.legendofdragoon.dabas.core.FileData;
import org.legendofdragoon.dabas.core.Hardware;
import org.legendofdragoon.dabas.core.InputType;
import org.legendofdragoon.dabas.core.memory.types.IntRef;
import org.legendofdragoon.dabas.game.DabasInterface;
import org.legendofdragoon.dabas.game.types.Save60;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static legend.core.GameEngine.AUDIO_THREAD;
import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Graphics.clearBlue_800babc0;
import static legend.game.Graphics.clearGreen_800bb104;
import static legend.game.Graphics.clearRed_8007a3a8;
import static legend.lodmod.LodConfig.DABAS_SAVE_DATA;
import static legend.lodmod.LodMod.INPUT_ACTION_GENERAL_MOVE_DOWN;
import static legend.lodmod.LodMod.INPUT_ACTION_GENERAL_MOVE_LEFT;
import static legend.lodmod.LodMod.INPUT_ACTION_GENERAL_MOVE_RIGHT;
import static legend.lodmod.LodMod.INPUT_ACTION_GENERAL_MOVE_UP;
import static legend.lodmod.LodMod.INPUT_ACTION_SMAP_INTERACT;
import static org.legendofdragoon.dabas.core.sound.Spu.SAMPLES_PER_TICK;
import static org.legendofdragoon.dabas.core.sound.Spu.SAMPLE_RATE;
import static org.lwjgl.openal.AL10.AL_FORMAT_MONO8;

public class Dabas implements DabasInterface {
  private final int[] pixels = new int[32];
  private final ByteBuffer pixelBuffer = BufferUtils.createByteBuffer(32 * 32 * 4);
  private final Hardware dabas = new Hardware(this, this.pixels);

  private final ByteBuffer audioBuffer = BufferUtils.createByteBuffer(SAMPLES_PER_TICK);
  private final GenericSource source;

  private Runnable oldRenderer;
  private int oldFps;
  private final Vector2i oldProjectionSize = new Vector2i();
  private EngineState.RenderMode oldRenderMode;
  private final Vector3i oldClearColour = new Vector3i();

  private Runnable ticker;

  private Obj texturedObj;
  private Texture displayTexture;
  private final MV transforms = new MV();

  private WindowEvents.InputActionPressed onPressed;
  private WindowEvents.InputActionReleased onReleased;
  private Runnable onClosed;

  public Dabas() {
    this.source = AUDIO_THREAD.addSource(new GenericSource(AL_FORMAT_MONO8, SAMPLE_RATE));

    this.dabas.start(Loader.resolve("OHTA/MCX/DABAS.BIN"));
  }

  @Override
  public void setTicker(final Runnable ticker) {
    if(this.oldRenderer == null) {
      this.oldRenderer = RENDERER.setRenderCallback(this::tick);
      this.oldFps = RENDERER.window().getFpsLimit();
      this.oldProjectionSize.set(RENDERER.getNativeWidth(), RENDERER.getNativeHeight());
      this.oldRenderMode = RENDERER.getRenderMode();
      this.oldClearColour.set(clearRed_8007a3a8, clearGreen_800bb104, clearBlue_800babc0);

      RENDERER.setRenderMode(EngineState.RenderMode.PERSPECTIVE);
      RENDERER.setProjectionSize(320, 320);
      RENDERER.setClearColour(0.0f, 0.0f, 0.0f);

      this.onPressed = RENDERER.window().events().onInputActionPressed((window, action, repeat) -> {
        final InputType input;
        if(action == INPUT_ACTION_GENERAL_MOVE_UP.get()) {
          input = InputType.BUTTON_UP;
        } else if(action == INPUT_ACTION_GENERAL_MOVE_DOWN.get()) {
          input = InputType.BUTTON_DOWN;
        } else if(action == INPUT_ACTION_GENERAL_MOVE_LEFT.get()) {
          input = InputType.BUTTON_LEFT;
        } else if(action == INPUT_ACTION_GENERAL_MOVE_RIGHT.get()) {
          input = InputType.BUTTON_RIGHT;
        } else if(action == INPUT_ACTION_SMAP_INTERACT.get()) {
          input = InputType.BUTTON_ACTION;
        } else {
          return;
        }

        this.dabas.buttonPressed(input);
      });

      this.onReleased = RENDERER.window().events().onInputActionReleased((window, action) -> {
        final InputType input;
        if(action == INPUT_ACTION_GENERAL_MOVE_UP.get()) {
          input = InputType.BUTTON_UP;
        } else if(action == INPUT_ACTION_GENERAL_MOVE_DOWN.get()) {
          input = InputType.BUTTON_DOWN;
        } else if(action == INPUT_ACTION_GENERAL_MOVE_LEFT.get()) {
          input = InputType.BUTTON_LEFT;
        } else if(action == INPUT_ACTION_GENERAL_MOVE_RIGHT.get()) {
          input = InputType.BUTTON_RIGHT;
        } else if(action == INPUT_ACTION_SMAP_INTERACT.get()) {
          input = InputType.BUTTON_ACTION;
        } else {
          return;
        }

        this.dabas.buttonReleased(input);
      });

      this.onClosed = RENDERER.window().events().onClose(this::shutdown);
    }

    this.ticker = ticker;
  }

  @Override
  public void setFps(final int fps) {
    RENDERER.window().setFpsLimit(fps);
  }

  private void tick() {
    if(this.displayTexture == null) {
      this.displayTexture = Texture.empty(32, 32);
    }

    if(this.texturedObj == null) {
      this.texturedObj = new QuadBuilder("Dabas")
        .bpp(Bpp.BITS_24)
        .size(1.0f, 1.0f)
        .build();
    }

    this.ticker.run();

    int pixelIndex = 0;
    for(int i = 0; i < 32 * 32; i++) {
      final int byteIndex = i / 32;
      final int bitIndex = i & 0x1f;
      final byte colour = (byte)((this.pixels[byteIndex] >>> bitIndex & 0x1 ^ 0x1) * 0xff);
      this.pixelBuffer.put(pixelIndex++, colour);
      this.pixelBuffer.put(pixelIndex++, colour);
      this.pixelBuffer.put(pixelIndex++, colour);
      this.pixelBuffer.put(pixelIndex++, (byte)0xff);
    }

    this.displayTexture.data(0, 0, 32, 32, this.pixelBuffer);

    this.transforms.scaling(320.0f, 320.0f, 1.0f);
    this.transforms.transfer.set(0.0f, 0.0f, 100.0f);
    RENDERER.queueOrthoModel(this.texturedObj, this.transforms, QueuedModelStandard.class)
      .texture(this.displayTexture);
  }

  @Override
  public void bufferAudio(final byte[] samples) {
    if(this.source.canBuffer()) {
      this.audioBuffer.put(0, samples);
      this.source.bufferOutput(this.audioBuffer);
    }
  }

  @Override
  public void loadGame(final Save60 save, final int[][] tiles) {
    if(!CONFIG.hasConfig(DABAS_SAVE_DATA.get())) {
      return;
    }

    final FileData data = new FileData(CONFIG.getConfig(DABAS_SAVE_DATA.get()));
    final IntRef offset = new IntRef();

    save.load(data, offset);

    for(int y = 0; y < tiles.length; y++) {
      for(int x = 0; x < tiles[y].length; x++) {
        tiles[y][x] = data.readUByte(offset);
      }
    }
  }

  @Override
  public void saveGame(final Save60 save, final int[][] tiles) {
    final FileData data = new FileData(new byte[0x260]);
    final IntRef offset = new IntRef();
    save.save(data, offset);

    for(final int[] row : tiles) {
      for(final int tile : row) {
        data.writeByte(offset, tile);
      }
    }

    CONFIG.setConfig(DABAS_SAVE_DATA.get(), data.getBytes());
  }

  @Override
  public void shutdown() {
    this.dabas.stop();

    RENDERER.window().events().removeInputActionPressed(this.onPressed);
    RENDERER.window().events().removeInputActionReleased(this.onReleased);
    RENDERER.window().events().removeClose(this.onClosed);

    RENDERER.setRenderCallback(this.oldRenderer);
    RENDERER.window().setFpsLimit(this.oldFps);
    PLATFORM.setInputTickRate(this.oldFps);
    RENDERER.setRenderMode(this.oldRenderMode);
    RENDERER.setProjectionSize(this.oldProjectionSize.x, this.oldProjectionSize.y);
    clearRed_8007a3a8 = this.oldClearColour.x;
    clearGreen_800bb104 = this.oldClearColour.y;
    clearBlue_800babc0 = this.oldClearColour.z;

    AUDIO_THREAD.removeSource(this.source);

    this.displayTexture.delete();
    this.texturedObj.delete();
  }
}
