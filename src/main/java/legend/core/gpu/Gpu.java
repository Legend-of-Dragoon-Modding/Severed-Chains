package legend.core.gpu;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import legend.core.Config;
import legend.core.InterruptType;
import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.core.Timers;
import legend.core.memory.IllegalAddressException;
import legend.core.memory.Memory;
import legend.core.memory.MisalignedAccessException;
import legend.core.memory.Segment;
import legend.core.memory.Value;
import legend.core.opengl.Camera;
import legend.core.opengl.Context;
import legend.core.opengl.Font;
import legend.core.opengl.GuiManager;
import legend.core.opengl.Mesh;
import legend.core.opengl.Shader;
import legend.core.opengl.Texture;
import legend.core.opengl.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.function.BiFunction;

import static legend.core.Hardware.INTERRUPTS;
import static legend.core.Hardware.MEMORY;
import static legend.core.MathHelper.colour24To15;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickGUID;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickName;
import static org.lwjgl.glfw.GLFW.glfwJoystickPresent;
import static org.lwjgl.opengl.GL11C.GL_NEAREST;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;

public class Gpu implements Runnable {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Gpu.class);

  public static final Value GPU_REG0 = MEMORY.ref(4, 0x1f801810L);
  public static final Value GPU_REG1 = MEMORY.ref(4, 0x1f801814L);

  private static final int STANDARD_VRAM_WIDTH = 1024;
  private static final int STANDARD_VRAM_HEIGHT = 512;

  private int renderScale = Config.renderScale();

  private int vramWidth = STANDARD_VRAM_WIDTH * this.renderScale;
  private int vramHeight = STANDARD_VRAM_HEIGHT * this.renderScale;

  private static final int[] dotClockDiv = { 10, 8, 5, 4, 7 };

  private Camera camera;
  private Window window;
  private Context ctx;
  private GuiManager guiManager;
  private Shader.UniformBuffer transforms2;
  private final Matrix4f transforms = new Matrix4f();

  private int[] vram24 = new int[this.vramWidth * this.vramHeight];
  private int[] vram15 = new int[this.vramWidth * this.vramHeight];

  private boolean isVramViewer;

  private Shader vramShader;
  private Texture vramTexture;
  private Mesh vramMesh;

  private Texture displayTexture;
  private Mesh displayMesh;

  private int windowWidth;
  private int windowHeight;

  public final Status status = new Status();
  private int gpuInfo;

  private final Queue<Runnable> commandQueue = new LinkedList<>();
  private int displayStartX;
  private int displayStartY;
  private int displayRangeX1;
  private int displayRangeX2;
  private int displayRangeY1;
  private int displayRangeY2;
  private final legend.core.gpu.RECT drawingArea = new legend.core.gpu.RECT();
  private short offsetX;
  private short offsetY;
  private boolean texturedRectXFlip;
  private boolean texturedRectYFlip;
  private int textureWindowMaskX;
  private int textureWindowMaskY;
  private int textureWindowOffsetX;
  private int textureWindowOffsetY;
  private int preMaskX;
  private int preMaskY;
  private int postMaskX;
  private int postMaskY;

  @Nullable
  private Gp0CommandBuffer currentCommand;

  private int videoCycles;
  private final int horizontalTiming = 3413;
  private final int verticalTiming = 263;
  private int scanLine;
  private boolean isOddLine;

  private final List<VramTexture> textures = new ArrayList<>();

  public Gpu(final Memory memory) {
    memory.addSegment(new GpuSegment(0x1f80_1810L));
  }

  public Window.Events events() {
    return this.window.events;
  }

  public Window window() {
    return this.window;
  }

  public GuiManager guiManager() {
    return this.guiManager;
  }

  public void command00Nop() {
    LOGGER.trace("GPU NOP");
  }

  public void command01ClearCache() {
    // NOOP - we don't do caching
  }

  private void removeTexturesForPixel(final int x, final int y) {
    this.textures.removeIf(tex -> tex.containsPixel(x, y));
  }

  public void loadTexture(final RECT rect, final long address, final Bpp bpp) {
    assert address != 0;

    final int rectX = rect.x.get() * this.renderScale;
    final int rectY = rect.y.get() * this.renderScale;
    final int rectW = rect.w.get() * this.renderScale;
    final int rectH = rect.h.get() * this.renderScale;

    assert rectX + rectW <= this.vramWidth : "Rect right (" + (rectX + rectW) + ") overflows VRAM width (" + this.vramWidth + ')';
    assert rectY + rectH <= this.vramHeight : "Rect bottom (" + (rectY + rectH) + ") overflows VRAM height (" + this.vramHeight + ')';

    synchronized(this.commandQueue) {
      this.commandQueue.add(() -> {
        LOGGER.debug("Copying (%d, %d, %d, %d) from CPU to VRAM (address: %08x)", rectX, rectY, rectW, rectH, address);

        this.removeTexturesForPixel(rectX, rectY);

        MEMORY.waitForLock(() -> {
          final byte[] data = MEMORY.getBytes(address, rectW * rectH * 2);
          final VramTexture tex = new VramTexture(rectX, rectY, rectW, rectH, bpp, data);
          this.textures.add(tex);
        });
      });
    }
  }

  public void commandA0CopyRectFromCpuToVram(final RECT rect, final long address) {
    assert address != 0;

    final int rectX = rect.x.get() * this.renderScale;
    final int rectY = rect.y.get() * this.renderScale;
    final int rectW = rect.w.get() * this.renderScale;
    final int rectH = rect.h.get() * this.renderScale;

    assert rectX + rectW <= this.vramWidth : "Rect right (" + (rectX + rectW) + ") overflows VRAM width (" + this.vramWidth + ')';
    assert rectY + rectH <= this.vramHeight : "Rect bottom (" + (rectY + rectH) + ") overflows VRAM height (" + this.vramHeight + ')';

    synchronized(this.commandQueue) {
      this.commandQueue.add(() -> {
        LOGGER.debug("Copying (%d, %d, %d, %d) from CPU to VRAM (address: %08x)", rectX, rectY, rectW, rectH, address);

        MEMORY.waitForLock(() -> {
          int i = 0;
          for(int y = rectY; y < rectY + rectH; y += this.renderScale) {
            for(int x = rectX; x < rectX + rectW; x += this.renderScale) {
              this.removeTexturesForPixel(x, y);

              final int packed = (int)MEMORY.get(address + i * 2, 2);
              final int unpacked = MathHelper.colour15To24(packed);

              for(int y1 = y; y1 < y + this.renderScale; y1++) {
                for(int x1 = x; x1 < x + this.renderScale; x1++) {
                  final int index = y1 * this.vramWidth + x1;
                  this.vram24[index] = unpacked;
                  this.vram15[index] = packed;
                }
              }

              i++;
            }
          }
        });
      });
    }
  }

  public void commandC0CopyRectFromVramToCpu(final legend.core.gpu.RECT rect, final long address) {
    assert address != 0;

    final int rectX = rect.x.get() * this.renderScale;
    final int rectY = rect.y.get() * this.renderScale;
    final int rectW = rect.w.get() * this.renderScale;
    final int rectH = rect.h.get() * this.renderScale;

    assert rectX + rectW <= this.vramWidth : "Rect right (" + (rectX + rectW) + ") overflows VRAM width (" + this.vramWidth + ')';
    assert rectY + rectH <= this.vramHeight : "Rect bottom (" + (rectY + rectH) + ") overflows VRAM height (" + this.vramHeight + ')';

    synchronized(this.commandQueue) {
      this.commandQueue.add(() -> {
        LOGGER.debug("Copying (%d, %d, %d, %d) from VRAM to CPU (address: %08x)", rectX, rectY, rectW, rectH, address);

        MEMORY.waitForLock(() -> {
          int i = 0;
          for(int y = rectY; y < rectY + rectH; y += this.renderScale) {
            for(int x = rectX; x < rectX + rectW; x += this.renderScale) {
              final int index = y * this.vramWidth + x;
              MEMORY.set(address + i * 2, 2, this.vram15[index]);
              i++;
            }
          }
        });
      });
    }
  }

  private int tagsUploaded;
  public int uploadLinkedList(final long address) {
    this.tagsUploaded = 0;

    MEMORY.waitForLock(() -> {
      long value;
      long a = address;

      do {
        try {
          value = MEMORY.get(a, 4);
          final long words = (value & 0xff00_0000L) >>> 24;

          for(int i = 1; i <= words; i++) {
            this.queueGp0Command((int)MEMORY.get(a + i * 4L, 4));
          }

          this.tagsUploaded++;
          a = a & 0xff00_0000L | value & 0xff_ffffL;
        } catch(final legend.core.gpu.InvalidGp0CommandException e) {
          throw new RuntimeException("Invalid GP0 packet at 0x%08x".formatted(a), e);
        }
      } while((value & 0xff_ffffL) != 0xff_ffffL);
    });

    LOGGER.trace("GPU linked list uploaded");

    return this.tagsUploaded;
  }

  private void queueGp0Command(final int command) throws legend.core.gpu.InvalidGp0CommandException {
    if(this.currentCommand == null) {
      this.currentCommand = new Gp0CommandBuffer(command);
    } else {
      this.currentCommand.queueValue(command);
    }

    if(this.currentCommand.isComplete()) {
      synchronized(this.commandQueue) {
        this.commandQueue.add(this.currentCommand.command.factory.apply(this.currentCommand.buffer, this));
      }
      this.currentCommand = null;
    }
  }

  private void processGp0Command() {
    final Runnable command;

    synchronized(this.commandQueue) {
      command = this.commandQueue.remove();
    }

    command.run();
  }

  /**
   * GP1(01h) - Reset Command Buffer
   */
  private void resetCommandBuffer() {
    synchronized(this.commandQueue) {
      this.commandQueue.clear();
    }
  }

  /**
   * GP1(02h) - Acknowledge GPU Interrupt (IRQ1)
   */
  private void acknowledgeGpuInterrupt() {
    this.status.interruptRequest = false;
  }

  /**
   * GP1(03h) - Display Enable
   */
  private void enableDisplay() {
    this.status.displayEnable = true;
  }

  /**
   * GP1(03h) - Display Enable
   */
  private void disableDisplay() {
    this.status.displayEnable = false;
  }

  /**
   * GP1(05h) - Start of Display Area in VRAM
   *
   * Upper/left Display source address in VRAM. The size and target position on screen is set via Display Range registers; target=X1,Y2; size=(X2-X1/cycles_per_pix), (Y2-Y1).
   */
  private void displayStart(final int x, final int y) {
    this.displayStartX = x * this.renderScale;
    this.displayStartY = y * this.renderScale;
  }

  /**
   * GP1(06h) - Horizontal Display Range (on Screen)
   *
   * Specifies the horizontal range within which the display area is displayed. For resolutions other than 320 pixels it may be necessary to fine adjust the value to obtain an exact match (eg. X2=X1+pixels*cycles_per_pix).
   * The number of displayed pixels per line is "(((X2-X1)/cycles_per_pix)+2) AND NOT 3" (ie. the hardware is rounding the width up/down to a multiple of 4 pixels).
   * Most games are using a width equal to the horizontal resolution (ie. 256, 320, 368, 512, 640 pixels). A few games are using slightly smaller widths (probably due to programming bugs). Pandemonium 2 is using a bigger "overscan" width (ensuring an intact picture without borders even on mis-calibrated TV sets).
   * The 260h value is the first visible pixel on normal TV Sets, this value is used by MOST NTSC games, and SOME PAL games (see below notes on Mis-Centered PAL games).
   */
  private void horizontalDisplayRange(final int x1, final int x2) {
    this.displayRangeX1 = x1 * this.renderScale;
    this.displayRangeX2 = x2 * this.renderScale;
  }

  /**
   * GP1(07h) - Vertical Display Range (on Screen)
   *
   * Specifies the vertical range within which the display area is displayed. The number of lines is Y2-Y1 (unlike as for the width, there's no rounding applied to the height). If Y2 is set to a much too large value, then the hardware stops to generate vblank interrupts (IRQ0).
   * The 88h/A3h values are the middle-scanlines on normal TV Sets, these values are used by MOST NTSC games, and SOME PAL games (see below notes on Mis-Centered PAL games).
   * The 224/264 values are for fullscreen pictures. Many NTSC games display 240 lines (overscan with hidden lines). Many PAL games display only 256 lines (underscan with black borders).
   */
  private void verticalDisplayRange(final int y1, final int y2) {
    this.displayRangeY1 = y1 * this.renderScale;
    this.displayRangeY2 = y2 * this.renderScale;
  }

  /**
   * GP1(08h) - Display Mode
   *
   * Note: Interlace must be enabled to see all lines in 480-lines mode (interlace is causing ugly flickering, so a non-interlaced low resolution image is typically having better quality than a high resolution interlaced image, a pretty bad example are the intro screens shown by the BIOS). The Display Area Color Depth does NOT affect the Drawing Area (the Drawing Area is always 15bit).
   * When the "Reverseflag" is set, the display scrolls down 2 lines or so, and colored regions are getting somehow hatched/distorted, but black and white regions are still looking okay. Don't know what that's good for? Probably relates to PAL/NTSC-Color Clock vs PSX-Dot Clock mismatches: Bit7=0 causes Flimmering errors (errors at different locations in each frame), and Bit7=1 causes Static errors (errors at same locations in all frames)?
   */
  private void displayMode(final HORIZONTAL_RESOLUTION_1 hRes1, final VERTICAL_RESOLUTION vRes, final VIDEO_MODE vMode, final DISPLAY_AREA_COLOUR_DEPTH dispColourDepth, final boolean interlace, final HORIZONTAL_RESOLUTION_2 hRes2) {
    // Always run on the GPU thread
    if(glfwGetCurrentContext() == 0) {
      synchronized(this.commandQueue) {
        this.commandQueue.add(() -> this.displayMode(hRes1, vRes, vMode, dispColourDepth, interlace, hRes2));
      }
      return;
    }

    this.status.horizontalResolution1 = hRes1;
    this.status.verticalResolution = vRes;
    this.status.videoMode = vMode;
    this.status.displayAreaColourDepth = dispColourDepth;
    this.status.verticalInterlace = interlace;
    this.status.horizontalResolution2 = hRes2;
    this.status.reverse = false;

    final int horizontalRes = hRes2 == HORIZONTAL_RESOLUTION_2._368 ? 368 : hRes1.res;
    final int verticalRes = vRes.res;

    this.displaySize(horizontalRes, verticalRes);
  }

  private void displaySize(final int horizontalRes, final int verticalRes) {
    final int hr = horizontalRes * this.renderScale;
    final int vr = verticalRes * this.renderScale;

    if(this.displayTexture != null) {
      this.displayTexture.delete();
    }

    this.displayTexture = Texture.create(builder -> {
      builder.size(hr, vr);
      builder.internalFormat(GL_RGBA);
      builder.dataFormat(GL_RGBA);
      builder.minFilter(GL_NEAREST);
      builder.magFilter(GL_NEAREST);
    });
  }

  private Shader loadShader(final Path vsh, final Path fsh) {
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

  public Timers.Sync getBlanksAndDot() { //test
    final int dot = dotClockDiv[this.status.horizontalResolution2 == HORIZONTAL_RESOLUTION_2._256_320_512_640 ? this.status.horizontalResolution1.ordinal() : 4];
    final boolean hBlank = this.videoCycles < this.displayRangeX1 || this.videoCycles > this.displayRangeX2;
    final boolean vBlank = this.scanLine < this.displayRangeY1 || this.scanLine > this.displayRangeY2;

    return new Timers.Sync(dot, hBlank, vBlank);
  }

  private long lastFrame;
  public Runnable r = () -> { };

  @Override
  public void run() {
    this.camera = new Camera(0.0f, 0.0f);
    this.window = new Window("Legend of Dragoon", Config.windowWidth() * this.renderScale, Config.windowHeight() * this.renderScale);
    this.window.setFpsLimit(60);
    this.ctx = new Context(this.window, this.camera);
    this.guiManager = new GuiManager(this.window);
    this.window.setEventPoller(this.guiManager::captureInput);

    final Font font;
    try {
      font = new Font("gfx/fonts/Consolas.ttf");
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load font", e);
    }
    this.guiManager.setFont(font);

    this.window.events.onKeyPress((window, key, scancode, mods) -> {
      if(mods != 0) {
        return;
      }

      if(key == GLFW_KEY_TAB) {
        this.isVramViewer = !this.isVramViewer;

        if(this.isVramViewer) {
          this.window.resize(this.vramTexture.width, this.vramTexture.height);
        } else {
          this.window.resize(this.windowWidth, this.windowHeight);
        }
      }
    });

    this.window.events.onResize((window1, width, height) -> {
      if(!this.isVramViewer) {
        final float windowScale = this.window.getScale();
        final float unscaledWidth = width / windowScale;
        final float unscaledHeight = height / windowScale;

        this.windowWidth = (int)unscaledWidth;
        this.windowHeight = (int)unscaledHeight;

        if(this.displayMesh != null) {
          this.displayMesh.delete();
        }

        final float aspect = (float)this.displayTexture.width / this.displayTexture.height;

        float w = unscaledWidth;
        float h = w / aspect;

        if(h > unscaledHeight) {
          h = unscaledHeight;
          w = h * aspect;
        }

        final float l = (unscaledWidth - w) / 2;
        final float t = (unscaledHeight - h) / 2;
        final float r = l + w;
        final float b = t + h;

        this.displayMesh = new Mesh(GL_TRIANGLE_STRIP, new float[] {
          l, t, 0, 0,
          l, b, 0, 1,
          r, t, 1, 0,
          r, b, 1, 1,
        }, 4);
        this.displayMesh.attribute(0, 0L, 2, 4);
        this.displayMesh.attribute(1, 2L, 2, 4);
      }
    });

    final FloatBuffer transform2Buffer = BufferUtils.createFloatBuffer(4 * 4);
    this.transforms2 = new Shader.UniformBuffer((long)transform2Buffer.capacity() * Float.BYTES, Shader.UniformBuffer.TRANSFORM2);

    final int hr = this.vramWidth;
    final int vr = this.vramHeight;

    this.vramShader = this.loadShader(Paths.get("gfx", "shaders", "vram.vsh"), Paths.get("gfx", "shaders", "vram.fsh"));
    this.vramTexture = Texture.empty(hr, vr);

    this.vramMesh = new Mesh(GL_TRIANGLE_STRIP, new float[] {
       0,  0, 0, 0,
       0, vr, 0, 1,
      hr,  0, 1, 0,
      hr, vr, 1, 1,
    }, 4);
    this.vramMesh.attribute(0, 0L, 2, 4);
    this.vramMesh.attribute(1, 2L, 2, 4);

    this.displaySize(320, 240);

    this.lastFrame = System.nanoTime();

    this.ctx.onDraw(() -> {
      this.r.run();
      this.tick();
      this.guiManager.draw(this.ctx.getWidth(), this.ctx.getHeight(), this.ctx.getWidth() / this.window.getScale(), this.ctx.getHeight() / this.window.getScale());

      final float fps = 1.0f / ((System.nanoTime() - this.lastFrame) / (1_000_000_000 / 30.0f)) * 30.0f;
      this.window.setTitle("Legend of Dragoon - scale: %d - FPS: %.2f/%d".formatted(this.renderScale, fps, this.window.getFpsLimit()));
      this.lastFrame = System.nanoTime();
    });

    if(Config.controllerConfig()) {
      final Scanner scanner = new Scanner(System.in);

      System.out.println("Beginning controller configuration.");
      System.out.println("Choose a joystick:");
      for(int i = 0; i < GLFW_JOYSTICK_LAST; i++) {
        if(glfwJoystickPresent(i)) {
          System.out.println((i + 1) + ": " + glfwGetJoystickName(i) + " (" + glfwGetJoystickGUID(i) + ')');
        }
      }

      final int index = this.readInt(scanner, "# ", "Invalid index") - 1;
      final String guid = glfwGetJoystickGUID(index);

      Config.controllerConfig(false);
      Config.controllerGuid(guid);

      try {
        Config.save();
      } catch(final IOException e) {
        System.err.println("Failed to save config");;
      }
    }

    this.window.show();

    try {
      this.window.run();
    } catch(final Throwable t) {
      LOGGER.error("Shutting down due to GPU exception:", t);
      this.window.close();
    } finally {
      this.guiManager.free();
      font.free();
    }
  }

  private int readInt(final Scanner scanner, final String prompt, final String error) {
    while(true) {
      System.out.print(prompt);

      try {
        return Integer.parseInt(scanner.nextLine());
      } catch(final NumberFormatException e) {
        System.out.println(error);
      }
    }
  }

  public void tick() {
    INTERRUPTS.set(InterruptType.VBLANK);

    //Video clock is the cpu clock multiplied by 11/7.
    this.videoCycles += 100 * 11 / 7;

    if(this.videoCycles >= this.horizontalTiming) {
      this.videoCycles -= this.horizontalTiming;
      this.scanLine++;

      if(this.status.verticalResolution == VERTICAL_RESOLUTION._240) {
        this.isOddLine = (this.scanLine & 0x1) != 0;
      }

      if(this.scanLine >= this.verticalTiming) {
        this.scanLine = 0;

        if(this.status.verticalInterlace && this.status.verticalResolution == VERTICAL_RESOLUTION._480) {
          this.isOddLine = !this.isOddLine;
        }
      }
    }

    // Restore model buffer to identity
    this.transforms.identity();
    this.transforms2.set(this.transforms);

    this.status.readyToReceiveCommand = true;

    if(this.isVramViewer) {
      final int size = this.vramWidth * this.vramHeight;
      final ByteBuffer pixels = MemoryUtil.memAlloc(size * 4);
      final IntBuffer intPixels = pixels.asIntBuffer();
      intPixels.put(this.vram24);

      pixels.flip();

      this.vramTexture.data(new legend.core.gpu.RECT((short)0, (short)0, (short)this.vramWidth, (short)this.vramHeight), pixels);

      this.vramShader.use();
      this.vramTexture.use();
      this.vramMesh.draw();

      MemoryUtil.memFree(pixels);
    } else if(this.status.displayAreaColourDepth == DISPLAY_AREA_COLOUR_DEPTH.BITS_24) {
      int yRangeOffset = 240 * this.renderScale - (this.displayRangeY2 - this.displayRangeY1) >> (this.status.verticalResolution == VERTICAL_RESOLUTION._480 ? 0 : 1);
      if(yRangeOffset < 0) {
        yRangeOffset = 0;
      }

      final int size = this.displayTexture.width * this.displayTexture.height;
      final ByteBuffer pixels = MemoryUtil.memAlloc(size * 4);
      final IntBuffer pixelsInt = pixels.asIntBuffer();

      for(int y = yRangeOffset; y < this.status.verticalResolution.res * this.renderScale - yRangeOffset; y++) {
        int offset = 0;
        for(int x = 0; x < (this.status.horizontalResolution2 == HORIZONTAL_RESOLUTION_2._368 ? 368 : this.status.horizontalResolution1.res) * this.renderScale; x += 2) {
          final int p0rgb = this.vram24[offset++ + this.displayStartX + (y - yRangeOffset + this.displayStartY) * this.vramWidth];
          final int p1rgb = this.vram24[offset++ + this.displayStartX + (y - yRangeOffset + this.displayStartY) * this.vramWidth];
          final int p2rgb = this.vram24[offset++ + this.displayStartX + (y - yRangeOffset + this.displayStartY) * this.vramWidth];

          final int p0bgr555 = colour24To15(p0rgb);
          final int p1bgr555 = colour24To15(p1rgb);
          final int p2bgr555 = colour24To15(p2rgb);

          //[(G0R0][R1)(B0][B1G1)]
          //   RG    B - R   GB

          final int p0R = p0bgr555 & 0xff;
          final int p0G = p0bgr555 >>> 8 & 0xff;
          final int p0B = p1bgr555 & 0xff;
          final int p1R = p1bgr555 >>> 8 & 0xff;
          final int p1G = p2bgr555 & 0xff;
          final int p1B = p2bgr555 >>> 8 & 0xff;

          final int p0rgb24bpp = p0B << 16 | p0G << 8 | p0R;
          final int p1rgb24bpp = p1B << 16 | p1G << 8 | p1R;

          pixelsInt.put(p0rgb24bpp);
          pixelsInt.put(p1rgb24bpp);
        }
      }

      pixels.flip();

      this.displayTexture.data(new legend.core.gpu.RECT((short)0, (short)0, (short)this.displayTexture.width, (short)this.displayTexture.height), pixels);

      this.vramShader.use();
      this.displayTexture.use();
      this.displayMesh.draw();

      MemoryUtil.memFree(pixels);
    } else { // 15bpp
      int yRangeOffset = 240 * this.renderScale - (this.displayRangeY2 - this.displayRangeY1) >> (this.status.verticalResolution == VERTICAL_RESOLUTION._480 ? 0 : 1);
      if(yRangeOffset < 0) {
        yRangeOffset = 0;
      }

      final ByteBuffer vram = MemoryUtil.memAlloc(this.vram24.length * 4);
      final IntBuffer intVram = vram.asIntBuffer();
      intVram.put(this.vram24);

      final int size = this.displayTexture.width * this.displayTexture.height;
      final ByteBuffer pixels = MemoryUtil.memAlloc(size * 4);
      final byte[] from = new byte[this.displayTexture.width * 4];

      for(int y = yRangeOffset; y < this.status.verticalResolution.res * this.renderScale - yRangeOffset; y++) {
        vram.get((this.displayStartX + (y - yRangeOffset + this.displayStartY) * this.vramTexture.width) * 4, from);
        pixels.put(from, 0, from.length);
      }

      pixels.flip();

      this.displayTexture.data(new RECT((short)0, (short)0, (short)this.displayTexture.width, (short)this.displayTexture.height), pixels);

      this.vramShader.use();
      this.displayTexture.use();
      this.displayMesh.draw();

      MemoryUtil.memFree(vram);
      MemoryUtil.memFree(pixels);
    }

    synchronized(this.commandQueue) {
      while(!this.commandQueue.isEmpty()) {
        this.processGp0Command();
      }
    }

    //TODO in 240-line vertical resolution mode, this changes per scanline. We don't do scanlines. Not sure of the implications.
    this.status.drawingLine = this.status.drawingLine.flip();
  }

  public int getRenderScale() {
    return this.renderScale;
  }

  public void rescaleVram(final int newScale) {
    if(newScale == this.renderScale) {
      return;
    }

    if(this.renderScale > 1) {
      // Scale down to 1

      this.displayStartX /= this.renderScale;
      this.displayStartY /= this.renderScale;
      this.displayRangeX1 /= this.renderScale;
      this.displayRangeY1 /= this.renderScale;
      this.displayRangeX2 /= this.renderScale;
      this.displayRangeY2 /= this.renderScale;
      this.status.texturePageXBase /= this.renderScale;

      this.vramWidth = STANDARD_VRAM_WIDTH;
      this.vramHeight = STANDARD_VRAM_HEIGHT;

      final int[] vram15 = new int[this.vramWidth * this.vramHeight];
      final int[] vram24 = new int[this.vramWidth * this.vramHeight];

      int newIndex = 0;
      for(int oldY = 0; oldY < this.vramHeight * this.renderScale; oldY += this.renderScale) {
        for(int oldX = 0; oldX < this.vramWidth * this.renderScale; oldX += this.renderScale) {
          final int oldIndex = oldY * this.vramWidth * this.renderScale + oldX;
          vram15[newIndex] = this.vram15[oldIndex];
          vram24[newIndex] = this.vram24[oldIndex];
          newIndex++;
        }
      }

      this.vram15 = vram15;
      this.vram24 = vram24;
      this.renderScale = 1;
    }

    if(newScale > 1) {
      // Scale back up if necessary

      this.renderScale = newScale;

      this.displayStartX *= this.renderScale;
      this.displayStartY *= this.renderScale;
      this.displayRangeX1 *= this.renderScale;
      this.displayRangeY1 *= this.renderScale;
      this.displayRangeX2 *= this.renderScale;
      this.displayRangeY2 *= this.renderScale;
      this.status.texturePageXBase *= this.renderScale;

      this.vramWidth = STANDARD_VRAM_WIDTH * this.renderScale;
      this.vramHeight = STANDARD_VRAM_HEIGHT * this.renderScale;

      final int[] vram15 = new int[this.vramWidth * this.vramHeight];
      final int[] vram24 = new int[this.vramWidth * this.vramHeight];

      int oldIndex = 0;
      for(int y = 0; y < this.vramHeight; y += this.renderScale) {
        for(int x = 0; x < this.vramWidth; x += this.renderScale) {
          for(int y1 = y; y1 < y + this.renderScale; y1++) {
            for(int x1 = x; x1 < x + this.renderScale; x1++) {
              final int newIndex = y1 * this.vramWidth + x1;
              vram15[newIndex] = this.vram15[oldIndex];
              vram24[newIndex] = this.vram24[oldIndex];
            }
          }

          oldIndex++;
        }
      }

      this.vram15 = vram15;
      this.vram24 = vram24;
    }

    this.vramTexture.delete();
    this.vramTexture = Texture.empty(this.vramWidth, this.vramHeight);

    final int horizontalRes = this.status.horizontalResolution2 == HORIZONTAL_RESOLUTION_2._368 ? 368 : this.status.horizontalResolution1.res;
    final int verticalRes = this.status.verticalResolution.res;
    this.displaySize(horizontalRes, verticalRes);
  }

  private static short signed11bit(final int n) {
    return (short)(n << 21 >> 21);
  }

  private static Runnable polygonRenderer(final IntList buffer, final Gpu gpu) {
    int bufferIndex = 0;
    final int cmd = buffer.getInt(bufferIndex++);
    final int colour = cmd & 0xff_ffff;
    final int command = cmd >>> 24;
    final boolean isRaw = (command & 0b1) != 0;
    final boolean isTranslucent = (command & 0b10) != 0;
    final boolean isTextured = (command & 0b100) != 0;
    final boolean isQuad = (command & 0b1000) != 0;
    final boolean isShaded = (command & 0b1_0000) != 0;

    final int vertices = isQuad ? 4 : 3;

    final int[] x = new int[4];
    final int[] y = new int[4];
    final int[] tx = new int[4];
    final int[] ty = new int[4];
    final int[] c = new int[4];

    Arrays.fill(c, colour);

    final int vertex0 = buffer.getInt(bufferIndex++);
    y[0] = (short)(vertex0 >>> 16 & 0xffff);
    x[0] = (short)(vertex0        & 0xffff);

    final int clut;
    if(isTextured) {
      final int tex0 = buffer.getInt(bufferIndex++);
      clut = tex0 >>> 16 & 0xffff;
      ty[0] = tex0 >>> 8 & 0xff;
      tx[0] = tex0 & 0xff;
    } else {
      clut = 0;
    }

    if(isShaded) {
      c[1] = buffer.getInt(bufferIndex++);
    }

    final int vertex1 = buffer.getInt(bufferIndex++);
    y[1] = (short)(vertex1 >>> 16 & 0xffff);
    x[1] = (short)(vertex1        & 0xffff);

    final int page;
    if(isTextured) {
      final int tex1 = buffer.getInt(bufferIndex++);
      page = tex1 >>> 16 & 0xffff;
      ty[1] = tex1 >>> 8 & 0xff;
      tx[1] = tex1 & 0xff;
    } else {
      page = 0;
    }

    if(isShaded) {
      c[2] = buffer.getInt(bufferIndex++);
    }

    final int vertex2 = buffer.getInt(bufferIndex++);
    y[2] = (short)(vertex2 >>> 16 & 0xffff);
    x[2] = (short)(vertex2        & 0xffff);

    if(isTextured) {
      final int tex2 = buffer.getInt(bufferIndex++);
      ty[2] = tex2 >>> 8 & 0xff;
      tx[2] = tex2 & 0xff;
    }

    if(isQuad) {
      if(isShaded) {
        c[3] = buffer.getInt(bufferIndex++);
      }

      final int vertex3 = buffer.getInt(bufferIndex++);
      y[3] = (short)(vertex3 >>> 16 & 0xffff);
      x[3] = (short)(vertex3        & 0xffff);

      if(isTextured) {
        final int tex3 = buffer.getInt(bufferIndex);
        ty[3] = tex3 >>> 8 & 0xff;
        tx[3] = tex3 & 0xff;
      }
    }

    final int clutX = (short)((clut & 0x3f) * 16) * gpu.renderScale;
    final int clutY = (short)(clut >>> 6 & 0x1ff) * gpu.renderScale;

    for(int i = 0; i < vertices; i++) {
      x[i] *= gpu.renderScale;
      y[i] *= gpu.renderScale;
      tx[i] *= gpu.renderScale;
      ty[i] *= gpu.renderScale;
    }

    return () -> {
      LOGGER.trace("[GP0.%02x] Drawing textured %d-point poly offset %d %d, XYUV0 %d %d %d %d, XYUV1 %d %d %d %d, XYUV2 %d %d %d %d, XYUV3 %d %d %d %d, Clut(XY) %04x (%d %d), Page %04x, RGB %06x", command, vertices, gpu.offsetX, gpu.offsetY, x[0], y[0], tx[0], ty[0], x[1], y[1], tx[1], ty[1], x[2], y[2], tx[2], ty[2], x[3], y[3], tx[3], ty[3], clut, clutX, clutY, page, colour);

      final int texturePageXBase = (page       & 0b1111) *  64 * gpu.renderScale;
      final int texturePageYBase = (page >>> 4 & 0b0001) * 256 * gpu.renderScale;
      final SEMI_TRANSPARENCY translucency = SEMI_TRANSPARENCY.values()[page >>> 5 & 0b11];
      final legend.core.gpu.Bpp texturePageColours = legend.core.gpu.Bpp.values()[page >>> 7 & 0b11];

      for(int i = 0; i < vertices; i++) {
        x[i] += gpu.offsetX;
        y[i] += gpu.offsetY;
      }

      gpu.status.texturePageXBase = texturePageXBase;
      gpu.status.texturePageYBase = texturePageYBase / gpu.renderScale == 256 ? TEXTURE_PAGE_Y_BASE.BASE_256 : TEXTURE_PAGE_Y_BASE.BASE_0;
      gpu.status.semiTransparency = translucency;
      gpu.status.texturePageColours = texturePageColours;

      gpu.rasterizeTriangle(x[0], y[0], x[1], y[1], x[2], y[2], tx[0], ty[0], tx[1], ty[1], tx[2], ty[2], c[0], c[1], c[2], clutX, clutY, texturePageXBase, texturePageYBase, texturePageColours, isTextured, isShaded, isTranslucent, isRaw, translucency);

      if(isQuad) {
        gpu.rasterizeTriangle(x[1], y[1], x[2], y[2], x[3], y[3], tx[1], ty[1], tx[2], ty[2], tx[3], ty[3], c[1], c[2], c[3], clutX, clutY, texturePageXBase, texturePageYBase, texturePageColours, isTextured, isShaded, isTranslucent, isRaw, translucency);
      }
    };
  }

  private static Runnable lineRenderer(final IntList buffer, final Gpu gpu) {
    int bufferIndex = 0;
    final int cmd = buffer.getInt(bufferIndex++);
    final int colour1 = cmd & 0xff_ffff;
    final int command = cmd >>> 24;

    final boolean isPoly = (command & 1 << 3) != 0;
    final boolean isShaded = (command & 1 << 4) != 0;
    final boolean isTransparent = (command & 1 << 1) != 0;

    if(isPoly) {
      throw new RuntimeException("Polyline not supported");
    }

    final int v1 = buffer.getInt(bufferIndex++);

    final int colour2;
    if(isShaded) {
      colour2 = buffer.getInt(bufferIndex++);
    } else {
      colour2 = colour1;
    }

    final int v2 = buffer.getInt(bufferIndex);

    return () -> gpu.rasterizeLine(v1, v2, colour1, colour2, isTransparent);
  }

  private void rasterizeLine(final int v1, final int v2, final int colour1, final int colour2, final boolean transparent) {
    int x = signed11bit(v1 & 0xffff) * this.renderScale;
    int y = signed11bit(v1 >> 16) * this.renderScale;

    int x2 = signed11bit(v2 & 0xffff) * this.renderScale;
    int y2 = signed11bit(v2 >> 16) * this.renderScale;

    if(Math.abs(x - x2) >= this.vramWidth || Math.abs(y - y2) >= this.vramHeight) {
      return;
    }

    x += this.offsetX;
    y += this.offsetY;

    x2 += this.offsetX;
    y2 += this.offsetY;

    final int w = x2 - x;
    final int h = y2 - y;

    int dx1 = 0;
    if(w < 0) {
      dx1 = -1;
    } else if(w > 0) {
      dx1 = 1;
    }

    int dy1 = 0;
    if(h < 0) {
      dy1 = -1;
    } else if(h > 0) {
      dy1 = 1;
    }

    int dx2 = 0;
    if(w < 0) {
      dx2 = -1;
    } else if(w > 0) {
      dx2 = 1;
    }

    int longest = Math.abs(w);
    int shortest = Math.abs(h);

    int dy2 = 0;
    if(longest <= shortest) {
      longest = Math.abs(h);
      shortest = Math.abs(w);
      if(h < 0) {
        dy2 = -1;
      } else if(h > 0) {
        dy2 = 1;
      }
      dx2 = 0;
    }

    int numerator = longest >> 1;

    for(int i = 0; i <= longest; i++) {
      final float ratio = (float)i / longest;
      int color = interpolateColours(colour1, colour2, ratio);

      if(x >= this.drawingArea.x.get() && x < this.drawingArea.w.get() && y >= this.drawingArea.y.get() && y < this.drawingArea.h.get()) {
        if(transparent) {
          color = this.handleTranslucence(x, y, color, this.status.semiTransparency);
        }

        color |= (this.status.setMaskBit ? 1 : 0) << 24;

        for(int y1 = 0; y1 < this.renderScale; y1++) {
          for(int x1 = 0; x1 < this.renderScale; x1++) {
            this.setPixel(x + x1, y + y1, color);
          }
        }
      }

      numerator += shortest;
      if(numerator >= longest) {
        numerator -= longest;
        x += (short)dx1;
        y += (short)dy1;
      } else {
        x += (short)dx2;
        y += (short)dy2;
      }
    }
  }

  private Runnable untexturedRectangleBuilder(final int command, final int vertex, final int size) {
    final boolean isTranslucent = (command & 1 << 25) != 0;

    final int colour = command & 0xff_ffff;

    final int vy = (short)((vertex & 0xffff0000) >>> 16) * this.renderScale;
    final int vx = (short)(vertex & 0xffff) * this.renderScale;

    final int vh = (short)((size & 0xffff0000) >>> 16) * this.renderScale;
    final int vw = (short)(size & 0xffff) * this.renderScale;

    return () -> {
      LOGGER.trace("[GP0.%02x] Drawing variable-sized untextured quad offset %d %d, XYWH %d %d %d %d RGB %06x", command >>> 24, this.offsetX, this.offsetY, vx, vy, vw, vh, colour);

      final int x1 = Math.max(vx + this.offsetX, this.drawingArea.x.get());
      final int y1 = Math.max(vy + this.offsetY, this.drawingArea.y.get());
      final int x2 = Math.min(vx + this.offsetX + vw, this.drawingArea.w.get());
      final int y2 = Math.min(vy + this.offsetY + vh, this.drawingArea.h.get());

      for(int y = y1; y < y2; y++) {
        for(int x = x1; x < x2; x++) {
          // Check background mask
          if(this.status.drawPixels == DRAW_PIXELS.NOT_TO_MASKED_AREAS) {
            if((this.getPixel(x, y) & 0xff00_0000L) != 0) {
              continue;
            }
          }

          final int texel;
          if(isTranslucent) {
            texel = this.handleTranslucence(x, y, colour, this.status.semiTransparency);
          } else {
            texel = colour;
          }

          this.setPixel(x, y, (this.status.setMaskBit ? 1 : 0) << 24 | texel);
        }
      }
    };
  }

  private Runnable texturedRectangleBuilder(final int command, final int vertex, final int tex, final int size) {
    final boolean isTranslucent = (command & 1 << 25) != 0;
    final boolean isRaw = (command & 1 << 24) != 0;

    final int colour = command & 0xff_ffff;

    final int vy = (short)((vertex & 0xffff0000) >>> 16) * this.renderScale;
    final int vx = (short)(vertex & 0xffff) * this.renderScale;

    final int clut = (tex & 0xffff0000) >>> 16;
    final int ty = ((tex & 0xff00) >>> 8) * this.renderScale;
    final int tx = (tex & 0xff) * this.renderScale;

    final int vh = (short)((size & 0xffff0000) >>> 16) * this.renderScale;
    final int vw = (short)(size & 0xffff) * this.renderScale;

    final int clutX = (short)((clut & 0x3f) * 16) * this.renderScale;
    final int clutY = (short)(clut >>> 6 & 0x1ff) * this.renderScale;

    return () -> {
      LOGGER.trace("[GP0.%02x] Drawing variable-sized textured quad offset %d %d, texpage XY %d %d, XYWH %d %d %d %d, UV %d %d, Clut(XY) %04x (%d %d), RGB %06x", command >>> 24, this.offsetX, this.offsetY, this.status.texturePageXBase, this.status.texturePageYBase.value * this.renderScale, vx, vy, vw, vh, tx, ty, clut, clutX, clutY, colour);

      final int x1 = Math.max(vx + this.offsetX, this.drawingArea.x.get());
      final int y1 = Math.max(vy + this.offsetY, this.drawingArea.y.get());
      final int x2 = Math.min(vx + this.offsetX + vw, this.drawingArea.w.get());
      final int y2 = Math.min(vy + this.offsetY + vh, this.drawingArea.h.get());

      final int offsetX = x1 - (vx + this.offsetX);
      final int offsetY = y1 - (vy + this.offsetY);

      final int u1 = tx + offsetX;
      final int v1 = ty + offsetY;

      for(int y = y1, v = v1; y < y2; y++, v++) {
        for(int x = x1, u = u1; x < x2; x++, u++) {
          // Check background mask
          if(this.status.drawPixels == DRAW_PIXELS.NOT_TO_MASKED_AREAS) {
            if((this.getPixel(x, y) & 0xff00_0000L) != 0) {
              continue;
            }
          }

          int texel = this.getTexel(this.maskTexelAxis(u, this.preMaskX, this.postMaskX), this.maskTexelAxis(v, this.preMaskY, this.postMaskY), clutX, clutY, this.status.texturePageXBase, this.status.texturePageYBase.value * this.renderScale, this.status.texturePageColours);
          if(texel == 0) {
            continue;
          }

          if(!isRaw) {
            texel = this.applyBlending(colour, texel);
          }

          if(isTranslucent && (texel & 0xff00_0000) != 0) {
            texel = this.handleTranslucence(x, y, texel, this.status.semiTransparency);
          }

          this.setPixel(x, y, (this.status.setMaskBit ? 1 : 0) << 24 | texel);
        }
      }
    };
  }

  private void rasterizeTriangle(final int vx0, final int vy0, int vx1, int vy1, int vx2, int vy2, final int tu0, final int tv0, int tu1, int tv1, int tu2, int tv2, final int c0, int c1, int c2, final int clutX, final int clutY, final int textureBaseX, final int textureBaseY, final legend.core.gpu.Bpp bpp, final boolean isTextured, final boolean isShaded, final boolean isTranslucent, final boolean isRaw, final SEMI_TRANSPARENCY translucencyMode) {
    int area = orient2d(vx0, vy0, vx1, vy1, vx2, vy2);
    if(area == 0) {
      return;
    }

    // Reorient triangle so it has clockwise winding
    if(area < 0) {
      final int tempVX = vx1;
      final int tempVY = vy1;
      vx1 = vx2;
      vy1 = vy2;
      vx2 = tempVX;
      vy2 = tempVY;

      final int tempTU = tu1;
      final int tempTV = tv1;
      tu1 = tu2;
      tv1 = tv2;
      tu2 = tempTU;
      tv2 = tempTV;

      final int tempC = c1;
      c1 = c2;
      c2 = tempC;

      area = -area;
    }

    /*boundingBox*/
    int minX = Math.min(vx0, Math.min(vx1, vx2));
    int minY = Math.min(vy0, Math.min(vy1, vy2));
    int maxX = Math.max(vx0, Math.max(vx1, vx2));
    int maxY = Math.max(vy0, Math.max(vy1, vy2));

    if(maxX - minX > this.vramWidth || maxY - minY > this.vramHeight) {
      return;
    }

    /*clip*/
    minX = (short)Math.max(minX, this.drawingArea.x.get());
    minY = (short)Math.max(minY, this.drawingArea.y.get());
    maxX = (short)Math.min(maxX, this.drawingArea.w.get());
    maxY = (short)Math.min(maxY, this.drawingArea.h.get());

    final int A01 = vy0 - vy1;
    final int B01 = vx1 - vx0;
    final int A12 = vy1 - vy2;
    final int B12 = vx2 - vx1;
    final int A20 = vy2 - vy0;
    final int B20 = vx0 - vx2;

    final int bias0 = isTopLeft(vx1, vy1, vx2, vy2) ? 0 : -1;
    final int bias1 = isTopLeft(vx2, vy2, vx0, vy0) ? 0 : -1;
    final int bias2 = isTopLeft(vx0, vy0, vx1, vy1) ? 0 : -1;

    int w0_row = orient2d(vx1, vy1, vx2, vy2, minX, minY);
    int w1_row = orient2d(vx2, vy2, vx0, vy0, minX, minY);
    int w2_row = orient2d(vx0, vy0, vx1, vy1, minX, minY);

    // Rasterize
    for(int y = minY; y < maxY; y++) {
      // Barycentric coordinates at start of row
      int w0 = w0_row;
      int w1 = w1_row;
      int w2 = w2_row;

      for(int x = minX; x < maxX; x++) {
        // If p is on or inside all edges, render pixel
        if((w0 + bias0 | w1 + bias1 | w2 + bias2) >= 0) {
          // Adjustments per triangle instead of per pixel can be done at area level
          // but it still has small off-by-1 error appreciable on some textured quads
          // I assume it could be handled recalculating AXX and BXX offsets but the math is beyond my scope

          // Check background mask
          if(this.status.drawPixels == DRAW_PIXELS.NOT_TO_MASKED_AREAS) {
            if((this.getPixel(x, y) & 0xff00_0000L) != 0) {
              w0 += A12;
              w1 += A20;
              w2 += A01;
              continue;
            }
          }
          // Reset default colour of the triangle calculated outside the for as it gets overwritten as follows...
          int colour = c0;

          if(isShaded) {
            colour = this.getShadedColor(w0, w1, w2, c0, c1, c2, area);
          }

          if(isTextured) {
            final int texelX = interpolateCoords(w0, w1, w2, tu0, tu1, tu2, area);
            final int texelY = interpolateCoords(w0, w1, w2, tv0, tv1, tv2, area);
            int texel = this.getTexel(this.maskTexelAxis(texelX, this.preMaskX, this.postMaskX), this.maskTexelAxis(texelY, this.preMaskY, this.postMaskY), clutX, clutY, textureBaseX, textureBaseY, bpp);
            if(texel == 0) {
              w0 += A12;
              w1 += A20;
              w2 += A01;
              continue;
            }

            if(!isRaw) {
              texel = this.applyBlending(colour, texel);
            }

            colour = texel;
          }

          if(isTranslucent && (!isTextured || (colour & 0xff00_0000) != 0)) {
            colour = this.handleTranslucence(x, y, colour, translucencyMode);
          }

          colour |= (this.status.setMaskBit ? 1 : 0) << 24;

          this.setPixel(x, y, colour);
        }

        // One step right
        w0 += A12;
        w1 += A20;
        w2 += A01;
      }

      // One step down
      w0_row += B12;
      w1_row += B20;
      w2_row += B01;
    }
  }

  private int applyBlending(final int colour, final int texel) {
    return
      texel & 0xff00_0000 |
      Math.min((colour >>> 16 & 0xff) * (texel >>> 16 & 0xff) >>> 7, 0xff) << 16 |
      Math.min((colour >>>  8 & 0xff) * (texel >>>  8 & 0xff) >>> 7, 0xff) <<  8 |
      Math.min((colour        & 0xff) * (texel        & 0xff) >>> 7, 0xff);
  }

  private int getPixel(final int x, final int y) {
    return this.vram24[y * this.vramWidth + x];
  }

  private int getPixel15(final int x, final int y) {
    return this.vram15[y * this.vramWidth + x];
  }

  private void setPixel(final int x, final int y, final int pixel) {
    final int index = y * this.vramWidth + x;
    this.vram24[index] = pixel;
    this.vram15[index] = colour24To15(pixel);
  }

  private static int interpolateCoords(final int w0, final int w1, final int w2, final int t0, final int t1, final int t2, final int area) {
    //https://codeplea.com/triangular-interpolation
    return (t0 * w0 + t1 * w1 + t2 * w2) / area;
  }

  private static int interpolateColours(final int c1, final int c2, final float ratio) {
    final int c1B = c1       & 0xff;
    final int c1G = c1 >>  8 & 0xff;
    final int c1R = c1 >> 16 & 0xff;
    final int c2B = c2       & 0xff;
    final int c2G = c2 >>  8 & 0xff;
    final int c2R = c2 >> 16 & 0xff;

    final byte b = (byte)(c2B * ratio + c1B * (1 - ratio));
    final byte g = (byte)(c2G * ratio + c1G * (1 - ratio));
    final byte r = (byte)(c2R * ratio + c1R * (1 - ratio));

    return r << 16 | g << 8 | b;
  }

  private int maskTexelAxis(final int axis, final int preMaskAxis, final int postMaskAxis) {
    return axis & preMaskAxis | postMaskAxis;
  }

  private static boolean isTopLeft(final int ax, final int ay, final int bx, final int by) {
    return ay == by && bx > ax || by < ay;
  }

  private int getTexel(final int x, final int y, final int clutX, final int clutY, final int textureBaseX, final int textureBaseY, final Bpp depth) {
    if(depth == Bpp.BITS_4) {
      return this.get4bppTexel(x, y, clutX, clutY, textureBaseX, textureBaseY);
    }

    if(depth == Bpp.BITS_8) {
      return this.get8bppTexel(x, y, clutX, clutY, textureBaseX, textureBaseY);
    }

    return this.get16bppTexel(x, y, textureBaseX, textureBaseY);
  }

  private int get4bppTexel(final int x, final int y, final int clutX, final int clutY, final int textureBaseX, final int textureBaseY) {
    int p = -1;

    for(final VramTexture tex : this.textures) {
      final int adjustedX = tex.adjustX(textureBaseX) + x;
      final int adjustedY = tex.adjustY(textureBaseY) + y;

      if(tex.containsPixel(adjustedX, adjustedY)) {
        p = tex.getPixel(adjustedX, adjustedY);
        break;
      }
    }

    if(p == -1) {
      final int index = this.getPixel15(x / 4 + textureBaseX, y + textureBaseY);
      p = index >> (x / this.renderScale & 3) * 4 & 0xf;
    }

    return this.getPixel(clutX + p * this.renderScale, clutY);
  }

  private int get8bppTexel(final int x, final int y, final int clutX, final int clutY, final int textureBaseX, final int textureBaseY) {
    int p = -1;

    for(final VramTexture tex : this.textures) {
      final int adjustedX = tex.adjustX(textureBaseX) + x;
      final int adjustedY = tex.adjustY(textureBaseY) + y;

      if(tex.containsPixel(adjustedX, adjustedY)) {
        p = tex.getPixel(adjustedX, adjustedY);
        break;
      }
    }

    if(p == -1) {
      final int index = this.getPixel15(x / 2 + textureBaseX, y + textureBaseY);
      p = index >> (x / this.renderScale & 1) * 8 & 0xff;
    }

    return this.getPixel(clutX + p * this.renderScale, clutY);
  }

  private int get16bppTexel(final int x, final int y, final int textureBaseX, final int textureBaseY) {
    return this.getPixel(x + textureBaseX, y + textureBaseY);
  }

  /**
   * Returns positive value for clockwise winding, negative value for counter-clockwise. 0 if vertices are collinear. Value is roughly twice the area of the triangle.
   */
  private static int orient2d(final int ax, final int ay, final int bx, final int by, final int cx, final int cy) {
    return (bx - ax) * (cy - ay) - (by - ay) * (cx - ax);
  }

  private int handleTranslucence(final int x, final int y, final int texel, final SEMI_TRANSPARENCY mode) {
    final int pixel = this.getPixel(x, y);

    final int br = pixel        & 0xff;
    final int bg = pixel >>>  8 & 0xff;
    final int bb = pixel >>> 16 & 0xff;
    final int fr = texel        & 0xff;
    final int fg = texel >>>  8 & 0xff;
    final int fb = texel >>> 16 & 0xff;
    final int r;
    final int g;
    final int b;

    switch(mode) {
      case HALF_B_PLUS_HALF_F -> {
        r = (br + fr) / 2;
        g = (bg + fg) / 2;
        b = (bb + fb) / 2;
      }

      case B_PLUS_F -> {
        r = Math.min(0xff, br + fr);
        g = Math.min(0xff, bg + fg);
        b = Math.min(0xff, bb + fb);
      }

      case B_MINUS_F -> {
        r = Math.max(0, br - fr);
        g = Math.max(0, bg - fg);
        b = Math.max(0, bb - fb);
      }

      case B_PLUS_QUARTER_F -> {
        r = Math.min(0xff, br + fr / 4);
        g = Math.min(0xff, bg + fg / 4);
        b = Math.min(0xff, bb + fb / 4);
      }

      default -> throw new RuntimeException();
    }

    return b << 16 | g << 8 | r;
  }

  private int getShadedColor(final int w0, final int w1, final int w2, final int c0, final int c1, final int c2, final int area) {
    final int r = ((c0        & 0xff) * w0 + (c1        & 0xff) * w1 + (c2        & 0xff) * w2) / area;
    final int g = ((c0 >>>  8 & 0xff) * w0 + (c1 >>>  8 & 0xff) * w1 + (c2 >>>  8 & 0xff) * w2) / area;
    final int b = ((c0 >>> 16 & 0xff) * w0 + (c1 >>> 16 & 0xff) * w1 + (c2 >>> 16 & 0xff) * w2) / area;

    return b << 16 | g << 8 | r;
  }

  public void dump(final ByteBuffer stream) {
    IoHelper.write(stream, this.renderScale);

    for(final long pixel : this.vram24) {
      IoHelper.write(stream, pixel);
    }

    for(final long pixel : this.vram15) {
      IoHelper.write(stream, pixel);
    }

    IoHelper.write(stream, this.isVramViewer);
    IoHelper.write(stream, this.windowWidth);
    IoHelper.write(stream, this.windowHeight);

    IoHelper.write(stream, this.status.texturePageXBase);
    IoHelper.write(stream, this.status.texturePageYBase);
    IoHelper.write(stream, this.status.semiTransparency);
    IoHelper.write(stream, this.status.texturePageColours);
    IoHelper.write(stream, this.status.dither);
    IoHelper.write(stream, this.status.drawable);
    IoHelper.write(stream, this.status.setMaskBit);
    IoHelper.write(stream, this.status.drawPixels);
    IoHelper.write(stream, this.status.interlaceField);
    IoHelper.write(stream, this.status.disableTextures);
    IoHelper.write(stream, this.status.horizontalResolution2);
    IoHelper.write(stream, this.status.horizontalResolution1);
    IoHelper.write(stream, this.status.verticalResolution);
    IoHelper.write(stream, this.status.videoMode);
    IoHelper.write(stream, this.status.displayAreaColourDepth);
    IoHelper.write(stream, this.status.verticalInterlace);
    IoHelper.write(stream, this.status.displayEnable);
    IoHelper.write(stream, this.status.interruptRequest);
    IoHelper.write(stream, this.status.dmaRequest);
    IoHelper.write(stream, this.status.readyToReceiveCommand);
    IoHelper.write(stream, this.status.readyToSendVramToCpu);
    IoHelper.write(stream, this.status.readyToReceiveDmaBlock);
    IoHelper.write(stream, this.status.dmaDirection);
    IoHelper.write(stream, this.status.drawingLine);

    IoHelper.write(stream, this.gpuInfo);

    IoHelper.write(stream, this.displayStartX);
    IoHelper.write(stream, this.displayStartY);
    IoHelper.write(stream, this.displayRangeX1);
    IoHelper.write(stream, this.displayRangeX2);
    IoHelper.write(stream, this.displayRangeY1);
    IoHelper.write(stream, this.displayRangeY2);
    IoHelper.write(stream, this.drawingArea);
    IoHelper.write(stream, this.offsetX);
    IoHelper.write(stream, this.offsetY);
    IoHelper.write(stream, this.texturedRectXFlip);
    IoHelper.write(stream, this.texturedRectYFlip);
    IoHelper.write(stream, this.textureWindowMaskX);
    IoHelper.write(stream, this.textureWindowMaskY);
    IoHelper.write(stream, this.textureWindowOffsetX);
    IoHelper.write(stream, this.textureWindowOffsetY);
    IoHelper.write(stream, this.preMaskX);
    IoHelper.write(stream, this.preMaskY);
    IoHelper.write(stream, this.postMaskX);
    IoHelper.write(stream, this.postMaskY);

    IoHelper.write(stream, this.videoCycles);
    IoHelper.write(stream, this.scanLine);
    IoHelper.write(stream, this.isOddLine);
  }

  public void load(final ByteBuffer buf, final int version) {
    final int renderScale = version >= 2 ? IoHelper.readInt(buf) : 1;

    this.rescaleVram(renderScale);

    for(int i = 0; i < this.vram24.length; i++) {
      this.vram24[i] = IoHelper.readInt(buf);
    }

    for(int i = 0; i < this.vram15.length; i++) {
      this.vram15[i] = IoHelper.readInt(buf);
    }

    this.isVramViewer = IoHelper.readBool(buf);

    if(version >= 2) {
      this.windowWidth = IoHelper.readInt(buf);
      this.windowHeight = IoHelper.readInt(buf);
    }

    this.status.texturePageXBase = IoHelper.readInt(buf);
    this.status.texturePageYBase = IoHelper.readEnum(buf, TEXTURE_PAGE_Y_BASE.class);
    this.status.semiTransparency = IoHelper.readEnum(buf, SEMI_TRANSPARENCY.class);
    this.status.texturePageColours = IoHelper.readEnum(buf, legend.core.gpu.Bpp.class);
    this.status.dither = IoHelper.readBool(buf);
    this.status.drawable = IoHelper.readBool(buf);
    this.status.setMaskBit = IoHelper.readBool(buf);
    this.status.drawPixels = IoHelper.readEnum(buf, DRAW_PIXELS.class);
    this.status.interlaceField = IoHelper.readBool(buf);
    this.status.disableTextures = IoHelper.readBool(buf);
    this.status.horizontalResolution2 = IoHelper.readEnum(buf, HORIZONTAL_RESOLUTION_2.class);
    this.status.horizontalResolution1 = IoHelper.readEnum(buf, HORIZONTAL_RESOLUTION_1.class);
    this.status.verticalResolution = IoHelper.readEnum(buf, VERTICAL_RESOLUTION.class);
    this.status.videoMode = IoHelper.readEnum(buf, VIDEO_MODE.class);
    this.status.displayAreaColourDepth = IoHelper.readEnum(buf, DISPLAY_AREA_COLOUR_DEPTH.class);
    this.status.verticalInterlace = IoHelper.readBool(buf);
    this.status.displayEnable = IoHelper.readBool(buf);
    this.status.interruptRequest = IoHelper.readBool(buf);
    this.status.dmaRequest = IoHelper.readBool(buf);
    this.status.readyToReceiveCommand = IoHelper.readBool(buf);
    this.status.readyToSendVramToCpu = IoHelper.readBool(buf);
    this.status.readyToReceiveDmaBlock = IoHelper.readBool(buf);
    this.status.dmaDirection = IoHelper.readEnum(buf, DMA_DIRECTION.class);
    this.status.drawingLine = IoHelper.readEnum(buf, DRAWING_LINE.class);

    this.gpuInfo = IoHelper.readInt(buf);

    if(version < 3) {
      IoHelper.readLong(buf);
      IoHelper.readInt(buf);
    }

    this.displayStartX = IoHelper.readInt(buf);
    this.displayStartY = IoHelper.readInt(buf);
    this.displayRangeX1 = IoHelper.readInt(buf);
    this.displayRangeX2 = IoHelper.readInt(buf);
    this.displayRangeY1 = IoHelper.readInt(buf);
    this.displayRangeY2 = IoHelper.readInt(buf);
    IoHelper.readRect(buf, this.drawingArea);
    this.offsetX = IoHelper.readShort(buf);
    this.offsetY = IoHelper.readShort(buf);
    this.texturedRectXFlip = IoHelper.readBool(buf);
    this.texturedRectYFlip = IoHelper.readBool(buf);
    this.textureWindowMaskX = IoHelper.readInt(buf);
    this.textureWindowMaskY = IoHelper.readInt(buf);
    this.textureWindowOffsetX = IoHelper.readInt(buf);
    this.textureWindowOffsetY = IoHelper.readInt(buf);
    this.preMaskX = IoHelper.readInt(buf);
    this.preMaskY = IoHelper.readInt(buf);
    this.postMaskX = IoHelper.readInt(buf);
    this.postMaskY = IoHelper.readInt(buf);

    this.videoCycles = IoHelper.readInt(buf);
    this.scanLine = IoHelper.readInt(buf);
    this.isOddLine = IoHelper.readBool(buf);

    final int horizontalRes = this.status.horizontalResolution2 == HORIZONTAL_RESOLUTION_2._368 ? 368 : this.status.horizontalResolution1.res;
    final int verticalRes = this.status.verticalResolution.res;

    this.displaySize(horizontalRes, verticalRes);

    if(this.isVramViewer) {
      this.window.resize(this.vramTexture.width, this.vramTexture.height);
    } else {
      this.window.resize(this.windowWidth, this.windowHeight);
    }

    this.commandQueue.clear();
  }

  public enum GP0_COMMAND {
    NOOP(0x00, 1, (buffer, gpu) -> () -> LOGGER.trace("GPU NOOP")),
    NOOP_4(0x04, 1, (buffer, gpu) -> () -> LOGGER.trace("GPU NOOP 4")), //TODO I'm not sure if this command is actually supposed to be executing, or if it's a bug in the game code

    CLEAR_CACHE(0x01, 1, (buffer, gpu) -> () -> LOGGER.trace("GPU clear cache")),

    FILL_RECTANGLE_IN_VRAM(0x02, 3, (buffer, gpu) -> {
      final int colour = buffer.getInt(0) & 0xff_ffff;

      final int vertex = buffer.getInt(1);
      final int y = (short)((vertex & 0xffff0000) >>> 16) * gpu.renderScale;
      final int x = (short)(vertex & 0xffff) * gpu.renderScale;

      final int size = buffer.getInt(2);
      final int h = (short)((size & 0xffff0000) >>> 16) * gpu.renderScale;
      final int w = (short)(size & 0xffff) * gpu.renderScale;

      return () -> {
        LOGGER.trace("Fill rectangle in VRAM XYWH %d %d %d %d, RGB %06x", x, y, w, h, colour);

        for(int posY = y; posY < y + h; posY++) {
          for(int posX = x; posX < x + w; posX++) {
            gpu.removeTexturesForPixel(posX, posY);
            gpu.setPixel(posX, posY, colour);
          }
        }
      };
    }),

    MONO_FOUR_POINT_POLY_OPAQUE(0x28, 5, (buffer, gpu) -> {
      return polygonRenderer(buffer, gpu);
    }),
    MONO_FOUR_POINT_POLY_TRANSLUCENT(0x2a, 5, (buffer, gpu) -> {
      return polygonRenderer(buffer, gpu);
    }),

    TEXTURED_FOUR_POINT_POLYGON_OPAQUE_BLENDED(0x2c, 9, (buffer, gpu) -> {
      return polygonRenderer(buffer, gpu);
    }),
    TEXTURED_FOUR_POINT_POLYGON_TRANSLUCENT_BLENDED(0x2e, 9, (buffer, gpu) -> {
      return polygonRenderer(buffer, gpu);
    }),
    SHADED_THREE_POINT_POLYGON_OPAQUE(0x30, 6, (buffer, gpu) -> {
      return polygonRenderer(buffer, gpu);
    }),

    SHADED_THREE_POINT_POLYGON_TRANSLUCENT(0x32, 6, (buffer, gpu) -> {
      return polygonRenderer(buffer, gpu);
    }),
    SHADED_TEXTURED_THREE_POINT_POLYGON_OPAQUE_BLENDED(0x34, 9, (buffer, gpu) -> {
      return polygonRenderer(buffer, gpu);
    }),
    SHADED_TEXTURED_THREE_POINT_POLYGON_TRANSLUCENT_BLENDED(0x36, 9, (buffer, gpu) -> {
      return polygonRenderer(buffer, gpu);
    }),
    SHADED_FOUR_POINT_POLYGON_OPAQUE(0x38, 8, (buffer, gpu) -> {
      return polygonRenderer(buffer, gpu);
    }),
    SHADED_FOUR_POINT_POLYGON_TRANSLUCENT(0x3a, 8, (buffer, gpu) -> {
      return polygonRenderer(buffer, gpu);
    }),

    SHADED_TEXTURED_FOUR_POINT_POLYGON_OPAQUE_BLENDED(0x3c, 12, (buffer, gpu) -> {
      return polygonRenderer(buffer, gpu);
    }),
    SHADED_TEXTURED_FOUR_POINT_POLYGON_TRANSLUCENT_BLENDED(0x3e, 12, (buffer, gpu) -> {
      return polygonRenderer(buffer, gpu);
    }),

    MONOCHROME_LINE_OPAQUE(0x40, 3, (buffer, gpu) -> {
      return lineRenderer(buffer, gpu);
    }),
    MONOCHROME_LINE_TRANSLUCENT(0x42, 3, (buffer, gpu) -> {
      return lineRenderer(buffer, gpu);
    }),
    SHADED_LINE_OPAQUE(0x50, 4, (buffer, gpu) -> {
      return lineRenderer(buffer, gpu);
    }),
    SHADED_LINE_TRANSLUCENT(0x52, 4, (buffer, gpu) -> {
      return lineRenderer(buffer, gpu);
    }),

    MONO_RECT_VAR_SIZE_OPAQUE(0x60, 3, (buffer, gpu) -> {
      final int command = buffer.getInt(0);
      final int vertex = buffer.getInt(1);
      final int size = buffer.getInt(2);
      return gpu.untexturedRectangleBuilder(command, vertex, size);
    }),

    MONOCHROME_RECT_VAR_SIZE_TRANS(0x62, 3, (buffer, gpu) -> {
      final int command = buffer.getInt(0);
      final int vertex = buffer.getInt(1);
      final int size = buffer.getInt(2);
      return gpu.untexturedRectangleBuilder(command, vertex, size);
    }),

    TEX_RECT_VAR_SIZE_OPAQUE_BLENDED(0x64, 4, (buffer, gpu) -> {
      final int command = buffer.getInt(0);
      final int vertex = buffer.getInt(1);
      final int tex = buffer.getInt(2);
      final int size = buffer.getInt(3);
      return gpu.texturedRectangleBuilder(command, vertex, tex, size);
    }),

    TEX_RECT_VAR_SIZE_TRANSPARENT_BLENDED(0x66, 4, (buffer, gpu) -> {
      final int command = buffer.getInt(0);
      final int vertex = buffer.getInt(1);
      final int tex = buffer.getInt(2);
      final int size = buffer.getInt(3);
      return gpu.texturedRectangleBuilder(command, vertex, tex, size);
    }),

    TEX_RECT_VAR_SIZE_TRANSPARENT_RAW(0x67, 4, (buffer, gpu) -> {
      final int command = buffer.getInt(0);
      final int vertex = buffer.getInt(1);
      final int tex = buffer.getInt(2);
      final int size = buffer.getInt(3);
      return gpu.texturedRectangleBuilder(command, vertex, tex, size);
    }),

    TEX_RECT_16_OPAQUE_BLENDED(0x7c, 3, (buffer, gpu) -> {
      final int command = buffer.getInt(0);
      final int vertex = buffer.getInt(1);
      final int tex = buffer.getInt(2);
      return gpu.texturedRectangleBuilder(command, vertex, tex, 0x10_0010);
    }),

    TEX_RECT_16_TRANSPARENT_BLENDED(0x7e, 3, (buffer, gpu) -> {
      final int command = buffer.getInt(0);
      final int vertex = buffer.getInt(1);
      final int tex = buffer.getInt(2);
      return gpu.texturedRectangleBuilder(command, vertex, tex, 0x10_0010);
    }),

    COPY_RECT_VRAM_VRAM(0x80, 4, (buffer, gpu) -> {
      final int source = buffer.getInt(1);
      final int sourceY = (short)((source & 0xffff0000) >>> 16) * gpu.renderScale;
      final int sourceX = (short)(source & 0xffff) * gpu.renderScale;

      final int dest = buffer.getInt(2);
      final int destY = (short)((dest & 0xffff0000) >>> 16) * gpu.renderScale;
      final int destX = (short)(dest & 0xffff) * gpu.renderScale;

      final int size = buffer.getInt(3);
      final int height = (short)((size & 0xffff0000) >>> 16) * gpu.renderScale;
      final int width = (short)(size & 0xffff) * gpu.renderScale;

      return () -> {
        LOGGER.debug("COPY VRAM VRAM from %d %d to %d %d size %d %d", sourceX, sourceY, destX, destY, width, height);

        for(int y = 0; y < height; y++) {
          for(int x = 0; x < width; x++) {
            gpu.removeTexturesForPixel(x, y);

            int colour = gpu.getPixel(sourceX + x, sourceY + y);

            if(gpu.status.drawPixels == DRAW_PIXELS.NOT_TO_MASKED_AREAS) {
              if((gpu.getPixel(destX + x, destY + y) & 0xff00_0000L) != 0) {
                continue;
              }
            }

            colour |= (gpu.status.setMaskBit ? 1 : 0) << 24;

            gpu.setPixel(destX + x, destY + y, colour);
          }
        }
      };
    }),

    DRAW_MODE_SETTINGS(0xe1, 1, (buffer, gpu) -> {
      final int settings = buffer.getInt(0);

      return () -> {
        LOGGER.trace("[GP0.e1] Draw mode set to %08x", settings);

        gpu.status.texturePageXBase = (settings & 0b1111) * 64 * gpu.renderScale;
        gpu.status.texturePageYBase = (settings & 0b1_0000) != 0 ? TEXTURE_PAGE_Y_BASE.BASE_256 : TEXTURE_PAGE_Y_BASE.BASE_0;
        gpu.status.semiTransparency = SEMI_TRANSPARENCY.values()[(settings & 0b110_0000) >>> 5];
        gpu.status.texturePageColours = legend.core.gpu.Bpp.values()[(settings & 0b1_1000_0000) >>> 7];
        gpu.status.drawable = (settings & 0b100_0000_0000) != 0;
        gpu.status.disableTextures = (settings & 0b1000_0000_0000) != 0;
        gpu.texturedRectXFlip = (settings & 0b1_0000_0000_0000) != 0;
        gpu.texturedRectYFlip = (settings & 0b10_0000_0000_0000) != 0;
      };
    }),

    TEXTURE_WINDOW_SETTINGS(0xe2, 1, (buffer, gpu) -> {
      final int settings = buffer.getInt(0);

      return () -> {
        gpu.textureWindowMaskX   =  (settings & 0b0000_0000_0000_0001_1111)         * 8 * gpu.renderScale;
        gpu.textureWindowMaskY   = ((settings & 0b0000_0000_0011_1110_0000) >>>  5) * 8 * gpu.renderScale;
        gpu.textureWindowOffsetX = ((settings & 0b0000_0111_1100_0000_0000) >>> 10) * 8 * gpu.renderScale;
        gpu.textureWindowOffsetY = ((settings & 0b1111_1000_0000_0000_0000) >>> 15) * 8 * gpu.renderScale;

        gpu.preMaskX = ~(gpu.textureWindowMaskX * 8);
        gpu.preMaskY = ~(gpu.textureWindowMaskY * 8);
        gpu.postMaskX = (gpu.textureWindowOffsetX & gpu.textureWindowMaskX) * 8;
        gpu.postMaskY = (gpu.textureWindowOffsetY & gpu.textureWindowMaskY) * 8;
      };
    }),

    DRAWING_AREA_TOP_LEFT(0xe3, 1, (buffer, gpu) -> {
      final int area = buffer.getInt(0);

      final short x = (short)((area & 0b11_1111_1111) * gpu.renderScale);
      final short y = (short)((area >>> 10 & 0b1_1111_1111L) * gpu.renderScale);

      assert x != 16;

      return () -> {
        LOGGER.trace("GP0.e3 setting drawing area top left to %d, %d", x, y);

        gpu.drawingArea.x.set(x);
        gpu.drawingArea.y.set(y);
      };
    }),

    DRAWING_AREA_BOTTOM_RIGHT(0xe4, 1, (buffer, gpu) -> {
      final int area = buffer.getInt(0);

      final short x = (short)((area & 0b11_1111_1111) * gpu.renderScale);
      final short y = (short)((area >>> 10 & 0b1_1111_1111L) * gpu.renderScale);

      return () -> {
        LOGGER.trace("GP0.e3 setting drawing area bottom right to %d, %d", x, y);

        gpu.drawingArea.w.set(x);
        gpu.drawingArea.h.set(y);
      };
    }),

    DRAWING_OFFSET(0xe5, 1, (buffer, gpu) -> {
      final int offset = buffer.getInt(0);

      return () -> {
        gpu.offsetX = (short)(signed11bit(offset & 0x7ff) * gpu.renderScale);
        gpu.offsetY = (short)(signed11bit(offset >>> 11 & 0x7ff) * gpu.renderScale);
      };
    }),

    MASK_BIT(0xe6, 1, (buffer, gpu) -> {
      final int val = buffer.getInt(0);

      return () -> {
        gpu.status.setMaskBit = (val & 0x1) != 0;
        gpu.status.drawPixels = (val & 0x2) != 0 ? DRAW_PIXELS.NOT_TO_MASKED_AREAS : DRAW_PIXELS.ALWAYS;

        LOGGER.trace("[GP0.e6] set mask bit %b, draw pixels %s", gpu.status.setMaskBit, gpu.status.drawPixels);
      };
    }),
    ;

    public static GP0_COMMAND getCommand(final int command) throws legend.core.gpu.InvalidGp0CommandException {
      for(final GP0_COMMAND cmd : GP0_COMMAND.values()) {
        if(cmd.command == command) {
          return cmd;
        }
      }

      throw new legend.core.gpu.InvalidGp0CommandException("Invalid GP0 command " + Long.toString(command, 16));
    }

    public final int command;
    public final int params;
    private final BiFunction<IntList, Gpu, Runnable> factory;

    GP0_COMMAND(final int command, final int params, final BiFunction<IntList, Gpu, Runnable> factory) {
      this.command = command;
      this.params = params;
      this.factory = factory;
    }
  }

  private static final class Gp0CommandBuffer {
    private final GP0_COMMAND command;
    private final IntList buffer = new IntArrayList();

    private Gp0CommandBuffer(final int command) throws legend.core.gpu.InvalidGp0CommandException {
      this.command = GP0_COMMAND.getCommand((command & 0xff000000) >>> 24);
      this.queueValue(command);
    }

    private void queueValue(final int value) {
      this.buffer.add(value);
    }

    private boolean isComplete() {
      return this.buffer.size() >= this.command.params;
    }
  }

  public static class Status {
    /**
     * Bits 0-3 - Texture page X base (value * 64)
     */
    public int texturePageXBase;
    /**
     * Bit 4 - Texture page Y base (0 = 0, 1 = 256)
     */
    public TEXTURE_PAGE_Y_BASE texturePageYBase = TEXTURE_PAGE_Y_BASE.BASE_0;
    /**
     * Bits 5-6 - Semi-transparency
     */
    public SEMI_TRANSPARENCY semiTransparency = SEMI_TRANSPARENCY.HALF_B_PLUS_HALF_F;
    /**
     * Bits 7-8 - Texture page colours
     */
    public legend.core.gpu.Bpp texturePageColours = Bpp.BITS_4;
    /**
     * Bit 9 - Dither 24-bit to 15-bit (false = strip MSB, true = dither)
     */
    public boolean dither;
    /**
     * Bit 10 - Drawing to display area
     */
    public boolean drawable;
    /**
     * Bit 11 - Set mask bit when drawing pixels
     */
    public boolean setMaskBit;
    /**
     * Bit 12 - Draw pixels
     */
    public DRAW_PIXELS drawPixels = DRAW_PIXELS.ALWAYS;
    /**
     * Bit 13 - Interlace field (always set when bit 22 is set)
     */
    public boolean interlaceField;
    /**
     * Bit 14 - Reverse flag (0 = normal, 1 = distorted)
     */
    public boolean reverse;
    /**
     * Bit 15 - Texture disable
     */
    public boolean disableTextures;
    /**
     * Bit 16 - Horizontal resolution 2
     */
    public HORIZONTAL_RESOLUTION_2 horizontalResolution2 = HORIZONTAL_RESOLUTION_2._256_320_512_640;
    /**
     * Bits 17-18 - Horizontal resolution 1
     */
    public HORIZONTAL_RESOLUTION_1 horizontalResolution1 = HORIZONTAL_RESOLUTION_1._256;
    /**
     * Bit 19 - Vertical resolution
     */
    public VERTICAL_RESOLUTION verticalResolution = VERTICAL_RESOLUTION._240;
    /**
     * Bit 20 - Video mode
     */
    public VIDEO_MODE videoMode = VIDEO_MODE.NTSC;
    /**
     * Bit 21 - Display area colour depth
     */
    public DISPLAY_AREA_COLOUR_DEPTH displayAreaColourDepth = DISPLAY_AREA_COLOUR_DEPTH.BITS_15;
    /**
     * Bit 22 - Vertical interlace
     */
    public boolean verticalInterlace;
    /**
     * Bit 23 - Display enable
     */
    public boolean displayEnable = true;
    /**
     * Bit 24 - Interrupt request (IRQ1)
     */
    public boolean interruptRequest;
    /**
     * Bit 25 - DMA/data request
     *
     * When DMA direction = off -> 0
     * When DMA direction = (unknown) -> FIFO state (0 = full, 1 = not full)
     * When DMA direction = CPU to GP0 -> same as bit 28
     * When DMA direction = GPUREAD to CPU -> same as bit 27
     */
    public boolean dmaRequest;
    /**
     * Bit 26 - Ready to receive command word
     */
    public boolean readyToReceiveCommand = true;
    /**
     * Bit 27 - Ready to send VRAM to CPU
     */
    public boolean readyToSendVramToCpu;
    /**
     * Bit 28 - Ready to receive DMA block
     */
    public boolean readyToReceiveDmaBlock = true;
    /**
     * Bits 29-30 - DMA direction
     */
    public DMA_DIRECTION dmaDirection = DMA_DIRECTION.OFF;
    /**
     * Bits 31 - Drawing even/odd lines in interlace mode
     */
    public DRAWING_LINE drawingLine = DRAWING_LINE.EVEN;

    private long pack(final Gpu gpu) {
      return
        this.texturePageXBase / gpu.renderScale / 64 & 0b111 |
        (long)this.texturePageYBase.ordinal() << 4 |
        (long)this.semiTransparency.ordinal() << 5 |
        (long)this.texturePageColours.ordinal() << 7 |
        (this.dither ? 1 : 0) << 9 |
        (this.drawable ? 1 : 0) << 10 |
        (this.setMaskBit ? 1 : 0) << 11 |
        (long)this.drawPixels.ordinal() << 12 |
        (this.interlaceField ? 1 : 0) << 13 |
        (this.reverse ? 1 : 0) << 14 |
        (this.disableTextures ? 1 : 0) << 15 |
        (long)this.horizontalResolution2.ordinal() << 16 |
        (long)this.horizontalResolution1.ordinal() << 17 |
        (long)this.verticalResolution.ordinal() << 19 |
        (long)this.videoMode.ordinal() << 20 |
        (long)this.displayAreaColourDepth.ordinal() << 21 |
        (this.verticalInterlace ? 1 : 0) << 22 |
        (this.displayEnable ? 1 : 0) << 23 |
        (this.interruptRequest ? 1 : 0) << 24 |
        (this.dmaRequest ? 1 : 0) << 25 |
        (this.readyToReceiveCommand ? 1 : 0) << 26 |
        (this.readyToSendVramToCpu ? 1 : 0) << 27 |
        (this.readyToReceiveDmaBlock ? 1 : 0) << 28 |
        (long)this.dmaDirection.ordinal() << 29 |
        (long)this.drawingLine.ordinal() << 31;
    }
  }

  public enum TEXTURE_PAGE_Y_BASE {
    BASE_0(0),
    BASE_256(256),
    ;

    public final int value;

    TEXTURE_PAGE_Y_BASE(final int value) {
      this.value = value;
    }
  }

  public enum SEMI_TRANSPARENCY {
    HALF_B_PLUS_HALF_F,
    B_PLUS_F,
    B_MINUS_F,
    B_PLUS_QUARTER_F,
  }

  public enum DRAW_PIXELS {
    ALWAYS,
    NOT_TO_MASKED_AREAS,
  }

  public enum HORIZONTAL_RESOLUTION_2 {
    _256_320_512_640,
    _368,
  }

  public enum HORIZONTAL_RESOLUTION_1 {
    _256(256),
    _320(320),
    _512(512),
    _640(640),
    ;

    public final int res;

    HORIZONTAL_RESOLUTION_1(final int res) {
      this.res = res;
    }
  }

  public enum VERTICAL_RESOLUTION {
    _240(240),
    _480(480),
    ;

    public final int res;

    VERTICAL_RESOLUTION(final int res) {
      this.res = res;
    }
  }

  public enum VIDEO_MODE {
    NTSC,
    PAL,
  }

  public enum DISPLAY_AREA_COLOUR_DEPTH {
    BITS_15,
    BITS_24,
  }

  public enum DMA_DIRECTION {
    OFF,
    FIFO,
    CPU_TO_GP0,
    GPU_READ_TO_CPU,
  }

  public enum DRAWING_LINE {
    EVEN,
    ODD,
    ;

    public DRAWING_LINE flip() {
      return this == EVEN ? ODD : EVEN;
    }
  }

  public class GpuSegment extends Segment {
    public GpuSegment(final long address) {
      super(address, 8);
    }

    @Override
    public byte get(final int offset) {
      throw new MisalignedAccessException("GPU ports may only be accessed with 32-bit reads and writes");
    }

    @Override
    public long get(final int offset, final int size) {
      if(size != 4) {
        throw new MisalignedAccessException("GPU ports may only be accessed with 32-bit reads and writes");
      }

      return switch(offset & 0x4) {
        case 0x0 -> this.onReg0Read();
        case 0x4 -> this.onReg1Read();
        default -> throw new IllegalAddressException("There is no GPU port at " + Long.toHexString(this.getAddress() + offset));
      };
    }

    @Override
    public void set(final int offset, final byte value) {
      throw new MisalignedAccessException("GPU ports may only be accessed with 32-bit reads and writes");
    }

    @Override
    public void set(final int offset, final int size, final long value) {
      if(size != 4) {
        throw new MisalignedAccessException("GPU ports may only be accessed with 32-bit reads and writes");
      }

      switch(offset & 0x4) {
        case 0x0 -> this.onReg0Write((int)value);
        case 0x4 -> this.onReg1Write((int)value);
      }
    }

    private void onReg0Write(final int value) {
      Gpu.this.status.readyToReceiveCommand = false;

      synchronized(Gpu.this.commandQueue) {
        try {
          Gpu.this.queueGp0Command(value);
        } catch(final InvalidGp0CommandException e) {
          throw new RuntimeException("Invalid GP0 packet at 0x%08x".formatted(value), e);
        }
        Gpu.this.processGp0Command();
      }
    }

    /**
     * Display Control Commands
     *
     * These commands are executed immediately
     */
    private void onReg1Write(final int value) {
      final int command = (value & 0xff000000) >>> 24;

      switch(command) {
        case 0x00: // Reset GPU
          LOGGER.info("Resetting GPU");

          Gpu.this.resetCommandBuffer();
          Gpu.this.acknowledgeGpuInterrupt();
          Gpu.this.disableDisplay();
          Gpu.this.displayStart(0, 0);
          Gpu.this.horizontalDisplayRange(0x200, 0x200 + 0xa00);
          Gpu.this.verticalDisplayRange(0x10, 0x10 + 0xf0);
          Gpu.this.displayMode(HORIZONTAL_RESOLUTION_1._320, VERTICAL_RESOLUTION._240, VIDEO_MODE.NTSC, DISPLAY_AREA_COLOUR_DEPTH.BITS_15, false, HORIZONTAL_RESOLUTION_2._256_320_512_640);

          //TODO GP0 commands
          //TODO verify that this sets GPUSTAT to 0x14802000
          return;

        case 0x01: // Reset Command Buffer
          LOGGER.info("Resetting GPU command buffer");
          Gpu.this.resetCommandBuffer();
          return;

        case 0x02: // Acknowledge GPU Interrupt (IRQ1)
          LOGGER.trace("Acknowledging GPU interrupt");
          Gpu.this.acknowledgeGpuInterrupt();
          return;

        case 0x03: // Display enable
          final boolean enable = (value & 0b1) == 0;

          if(enable) {
            LOGGER.trace("Enabling display");
            Gpu.this.enableDisplay();
          } else {
            LOGGER.trace("Disabling display");
            Gpu.this.disableDisplay();
          }

          return;

        case 0x05: // Start of display area (in VRAM)
          Gpu.this.displayStartX = (value & 0x3ff) * Gpu.this.renderScale;
          Gpu.this.displayStartY = (value >> 10 & 0x3ff) * Gpu.this.renderScale;

          LOGGER.trace("Setting start of display area (in VRAM) to %d, %d", Gpu.this.displayStartX, Gpu.this.displayStartY);
          return;

        case 0x06: // Horizontal display range (on screen)
          // Both values are counted in 53.222400MHz units, relative to HSYNC
          // Note: 260h is the first visible pixel on normal CRT TV sets
          final int x1 = value & 0xfff; // (260h + 0)
          final int x2 = (int)(value >> 12 & 0xfffL); // (260h + 320 * 8)

          LOGGER.trace("Setting horizontal display range (on screen) to %d, %d", x1, x2);
          Gpu.this.horizontalDisplayRange(x1, x2);
          return;

        case 0x07: // Vertical display range (on screen)
          final int y1 = value & 0x3ff;
          final int y2 = value >> 10 & 0x3ff;

          LOGGER.trace("Setting vertical display range (on screen) to %d, %d", y1, y2);
          Gpu.this.verticalDisplayRange(y1, y2);
          return;

        case 0x08: // Display mode
          LOGGER.trace("Setting display mode %02x", value);

          final HORIZONTAL_RESOLUTION_1 hRes1 = HORIZONTAL_RESOLUTION_1.values()[value & 0b11];
          final VERTICAL_RESOLUTION vRes = (value & 0b100) == 0 ? VERTICAL_RESOLUTION._240 : VERTICAL_RESOLUTION._480;
          final VIDEO_MODE videoMode = (value & 0b1000) == 0 ? VIDEO_MODE.NTSC : VIDEO_MODE.PAL;
          final DISPLAY_AREA_COLOUR_DEPTH colourDepth = (value & 0b1_0000) == 0 ? DISPLAY_AREA_COLOUR_DEPTH.BITS_15 : DISPLAY_AREA_COLOUR_DEPTH.BITS_24;
          final boolean interlace = (value & 0b10_0000) != 0;
          final HORIZONTAL_RESOLUTION_2 hRes2 = (value & 0b100_0000) == 0 ? HORIZONTAL_RESOLUTION_2._256_320_512_640 : HORIZONTAL_RESOLUTION_2._368;

          Gpu.this.displayMode(hRes1, vRes, videoMode, colourDepth, interlace, hRes2);

          return;

        case 0x10: // Get GPU Info
          final int info = value & 0xffffff;

          switch(info) {
            case 0x03: // Draw area top left
              Gpu.this.gpuInfo = Gpu.this.drawingArea.y.get() / Gpu.this.renderScale << 10 | Gpu.this.drawingArea.x.get() / Gpu.this.renderScale;
              return;

            case 0x04: // Draw area bottom right
              Gpu.this.gpuInfo = Gpu.this.drawingArea.h.get() / Gpu.this.renderScale << 10 | Gpu.this.drawingArea.w.get() / Gpu.this.renderScale;
              return;

            case 0x05: // Draw area offset
              Gpu.this.gpuInfo = Gpu.this.offsetY / Gpu.this.renderScale << 11 | Gpu.this.offsetX / Gpu.this.renderScale;
              return;

            case 0x07: // GPU type
              Gpu.this.gpuInfo = 0x2;
              return;
          }

          throw new RuntimeException("Get GPU info " + Integer.toString(info, 16) + " not yet supported");
      }

      throw new RuntimeException("GPU command 1." + Integer.toString(command, 16) + " not yet supported (command word: " + Long.toString(value, 16) + ')');
    }

    private long onReg0Read() {
      return Gpu.this.gpuInfo;
    }

    private long onReg1Read() {
      return Gpu.this.status.pack(Gpu.this);
    }
  }
}
