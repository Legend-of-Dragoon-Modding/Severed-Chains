package legend.core.gpu;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import legend.core.Config;
import legend.core.InterruptType;
import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.core.opengl.Camera;
import legend.core.opengl.Context;
import legend.core.opengl.Mesh;
import legend.core.opengl.Shader;
import legend.core.opengl.Texture;
import legend.core.opengl.Window;
import legend.game.types.Translucency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.function.BiFunction;

import static legend.core.Hardware.INTERRUPTS;
import static legend.core.Hardware.MEMORY;
import static legend.core.MathHelper.colour24To15;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
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

  private static final int STANDARD_VRAM_WIDTH = 1024;
  private static final int STANDARD_VRAM_HEIGHT = 512;

  private int renderScale = Config.renderScale();

  private int vramWidth = STANDARD_VRAM_WIDTH * this.renderScale;
  private int vramHeight = STANDARD_VRAM_HEIGHT * this.renderScale;

  private Camera camera;
  private Window window;
  private Context ctx;
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

  private final Queue<Runnable> commandQueue = new LinkedList<>();
  private int displayStartX;
  private int displayStartY;
  private int displayRangeY1;
  private int displayRangeY2;
  public final RECT drawingArea = new RECT();
  private short offsetX;
  private short offsetY;

  @Nullable
  private Gp0CommandBuffer currentCommand;

  private int zMax;
  private LinkedList<GpuCommand>[] zQueues;

  public Window.Events events() {
    return this.window.events;
  }

  public Window window() {
    return this.window;
  }

  public void command02FillRect(final int x, final int y, final int w, final int h, final int colour) {
    LOGGER.trace("Fill rectangle in VRAM XYWH %d %d %d %d, RGB %06x", x, y, w, h, colour);

    for(int posY = y; posY < y + h; posY++) {
      for(int posX = x; posX < x + w; posX++) {
        this.setPixel(posX, posY, colour);
      }
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

  public void commandC0CopyRectFromVramToCpu(final RECT rect, final long address) {
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

  public void command80CopyRectFromVramToVram(final int sourceX, final int sourceY, final int destX, final int destY, final int width, final int height) {
    LOGGER.debug("COPY VRAM VRAM from %d %d to %d %d size %d %d", sourceX, sourceY, destX, destY, width, height);

    for(int y = 0; y < height; y++) {
      for(int x = 0; x < width; x++) {
        int colour = this.getPixel(sourceX + x, sourceY + y);

        if(this.status.drawPixels == DRAW_PIXELS.NOT_TO_MASKED_AREAS) {
          if((this.getPixel(destX + x, destY + y) & 0xff00_0000L) != 0) {
            continue;
          }
        }

        colour |= (this.status.setMaskBit ? 1 : 0) << 24;

        this.setPixel(destX + x, destY + y, colour);
      }
    }
  }

  private int tagsUploaded;
  public int uploadLinkedList(final long address) {
    this.tagsUploaded = 0;

    MEMORY.waitForLock(() -> {
      long value;
      long a = address;

      final byte[] data = new byte[80];
      final ByteBuffer buffer = ByteBuffer.wrap(data);
      buffer.order(ByteOrder.LITTLE_ENDIAN);
      final IntBuffer ints = buffer.asIntBuffer();

      do {
        try {
          value = MEMORY.get(a, 4);
          final int words = (int)(value >>> 24);

          MEMORY.getBytes(a + 4, data, 0, words * 4);
          for(int i = 0; i < words; i++) {
            this.queueGp0Command(ints.get(i));
          }

          this.tagsUploaded++;
          a = a & 0xff00_0000L | value & 0xff_ffffL;
        } catch(final InvalidGp0CommandException e) {
          throw new RuntimeException("Invalid GP0 packet at 0x%08x".formatted(a), e);
        }
      } while((value & 0xff_ffffL) != 0xff_ffffL);
    });

    LOGGER.trace("GPU linked list uploaded");

    return this.tagsUploaded;
  }

  public void queueCommand(final int z, final GpuCommand command) {
    this.zQueues[z].addFirst(command);
  }

  private void queueGp0Command(final int command) throws InvalidGp0CommandException {
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

  public void reset() {
    LOGGER.info("Resetting GPU");

    Gpu.this.resetCommandBuffer();
    Gpu.this.displayStart(0, 0);
    Gpu.this.verticalDisplayRange(16, 256);
    Gpu.this.displayMode(HORIZONTAL_RESOLUTION._320, VERTICAL_RESOLUTION._240, DISPLAY_AREA_COLOUR_DEPTH.BITS_15);
  }

  /**
   * GP1(01h) - Reset Command Buffer
   */
  public void resetCommandBuffer() {
    synchronized(this.commandQueue) {
      this.commandQueue.clear();
    }
  }

  /**
   * GP1(05h) - Start of Display Area in VRAM
   *
   * Upper/left Display source address in VRAM. The size and target position on screen is set via Display Range registers; target=X1,Y2; size=(X2-X1/cycles_per_pix), (Y2-Y1).
   */
  public void displayStart(final int x, final int y) {
    this.displayStartX = x * this.renderScale;
    this.displayStartY = y * this.renderScale;
  }

  /**
   * GP1(07h) - Vertical Display Range (on Screen)
   *
   * Specifies the vertical range within which the display area is displayed. The number of lines is Y2-Y1 (unlike as for the width, there's no rounding applied to the height). If Y2 is set to a much too large value, then the hardware stops to generate vblank interrupts (IRQ0).
   * The 88h/A3h values are the middle-scanlines on normal TV Sets, these values are used by MOST NTSC games, and SOME PAL games (see below notes on Mis-Centered PAL games).
   * The 224/264 values are for fullscreen pictures. Many NTSC games display 240 lines (overscan with hidden lines). Many PAL games display only 256 lines (underscan with black borders).
   */
  public void verticalDisplayRange(final int y1, final int y2) {
    this.displayRangeY1 = y1 * this.renderScale;
    this.displayRangeY2 = y2 * this.renderScale;
  }

  /**
   * GP1(08h) - Display Mode
   *
   * Note: The Display Area Color Depth does NOT affect the Drawing Area (the Drawing Area is always 15bit).
   */
  public void displayMode(final HORIZONTAL_RESOLUTION hRes, final VERTICAL_RESOLUTION vRes, final DISPLAY_AREA_COLOUR_DEPTH dispColourDepth) {
    // Always run on the GPU thread
    if(glfwGetCurrentContext() == 0) {
      synchronized(this.commandQueue) {
        this.commandQueue.add(() -> this.displayMode(hRes, vRes, dispColourDepth));
      }
      return;
    }

    this.status.horizontalResolution = hRes;
    this.status.verticalResolution = vRes;
    this.status.displayAreaColourDepth = dispColourDepth;

    this.displaySize(hRes.res, vRes.res);
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

  private long lastFrame;
  public Runnable r = () -> { };

  @Override
  public void run() {
    this.camera = new Camera(0.0f, 0.0f);
    this.window = new Window("Legend of Dragoon", Config.windowWidth() * this.renderScale, Config.windowHeight() * this.renderScale);
    this.window.setFpsLimit(60);
    this.ctx = new Context(this.window, this.camera);

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
      if(this.zMax != orderingTableSize_1f8003c8.get()) {
        this.updateOrderingTableSize(orderingTableSize_1f8003c8.get());
      }

      this.r.run();

      if(this.zQueues != null) {
        synchronized(this.commandQueue) {
          for(int z = this.zQueues.length - 1; z >= 0; z--) {
            for(final GpuCommand command : this.zQueues[z]) {
              this.commandQueue.add(() -> command.render(this));
            }

            this.zQueues[z].clear();
          }
        }
      }

      this.tick();

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
        System.err.println("Failed to save config");
      }
    }

    this.window.show();

    try {
      this.window.run();
    } catch(final Throwable t) {
      LOGGER.error("Shutting down due to GPU exception:", t);
      this.window.close();
    }
  }

  public void updateOrderingTableSize(final int size) {
    final LinkedList[] list = new LinkedList[size];
    Arrays.setAll(list, key -> new LinkedList<>());

    this.zMax = size;
    this.zQueues = list;
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

    // Restore model buffer to identity
    this.transforms.identity();
    this.transforms2.set(this.transforms);

    if(this.isVramViewer) {
      final int size = this.vramWidth * this.vramHeight;
      final ByteBuffer pixels = MemoryUtil.memAlloc(size * 4);
      final IntBuffer intPixels = pixels.asIntBuffer();
      intPixels.put(this.vram24);

      pixels.flip();

      this.vramTexture.data(new RECT((short)0, (short)0, (short)this.vramWidth, (short)this.vramHeight), pixels);

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
        for(int x = 0; x < this.status.horizontalResolution.res * this.renderScale; x += 2) {
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

      this.displayTexture.data(new RECT((short)0, (short)0, (short)this.displayTexture.width, (short)this.displayTexture.height), pixels);

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
  }

  public int getOffsetX() {
    return this.offsetX;
  }

  public int getOffsetY() {
    return this.offsetY;
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
      this.displayRangeY1 /= this.renderScale;
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
      this.displayRangeY1 *= this.renderScale;
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

    final int horizontalRes = this.status.horizontalResolution.res;
    final int verticalRes = this.status.verticalResolution.res;
    this.displaySize(horizontalRes, verticalRes);
  }

  private static short signed11bit(final int n) {
    return (short)(n << 21 >> 21);
  }

  public void rasterizeLine(int x, int y, int x2, int y2, final int colour1, final int colour2, @Nullable final Translucency translucency) {
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
        if(translucency != null) {
          color = this.handleTranslucence(x, y, color, translucency);
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

          int texel = this.getTexel(u, v, clutX, clutY, this.status.texturePageXBase, this.status.texturePageYBase.value * this.renderScale, this.status.texturePageColours);
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

  void rasterizeTriangle(final int vx0, final int vy0, int vx1, int vy1, int vx2, int vy2, final int tu0, final int tv0, int tu1, int tv1, int tu2, int tv2, final int c0, int c1, int c2, final int clutX, final int clutY, final int textureBaseX, final int textureBaseY, final Bpp bpp, final boolean isTextured, final boolean isShaded, final boolean isTranslucent, final boolean isRaw, final Translucency translucencyMode) {
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
            int texel = this.getTexel(texelX, texelY, clutX, clutY, textureBaseX, textureBaseY, bpp);
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

  public int applyBlending(final int colour, final int texel) {
    return
      texel & 0xff00_0000 |
      Math.min((colour >>> 16 & 0xff) * (texel >>> 16 & 0xff) >>> 7, 0xff) << 16 |
      Math.min((colour >>>  8 & 0xff) * (texel >>>  8 & 0xff) >>> 7, 0xff) <<  8 |
      Math.min((colour        & 0xff) * (texel        & 0xff) >>> 7, 0xff);
  }

  public int getPixel(final int x, final int y) {
    return this.vram24[y * this.vramWidth + x];
  }

  public int getPixel15(final int x, final int y) {
    return this.vram15[y * this.vramWidth + x];
  }

  public void setPixel(final int x, final int y, final int pixel) {
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

  private static boolean isTopLeft(final int ax, final int ay, final int bx, final int by) {
    return ay == by && bx > ax || by < ay;
  }

  public int getTexel(final int x, final int y, final int clutX, final int clutY, final int textureBaseX, final int textureBaseY, final legend.core.gpu.Bpp depth) {
    if(depth == Bpp.BITS_4) {
      return this.get4bppTexel(x, y, clutX, clutY, textureBaseX, textureBaseY);
    }

    if(depth == Bpp.BITS_8) {
      return this.get8bppTexel(x, y, clutX, clutY, textureBaseX, textureBaseY);
    }

    return this.get16bppTexel(x, y, textureBaseX, textureBaseY);
  }

  private int get4bppTexel(final int x, final int y, final int clutX, final int clutY, final int textureBaseX, final int textureBaseY) {
    final int index = this.getPixel15(x / 4 + textureBaseX, y + textureBaseY);
    final int p = index >> (x / this.renderScale & 3) * 4 & 0xf;
    return this.getPixel(clutX + p * this.renderScale, clutY);
  }

  private int get8bppTexel(final int x, final int y, final int clutX, final int clutY, final int textureBaseX, final int textureBaseY) {
    final int index = this.getPixel15(x / 2 + textureBaseX, y + textureBaseY);
    final int p = index >> (x / this.renderScale & 1) * 8 & 0xff;
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

  public int handleTranslucence(final int x, final int y, final int texel, final Translucency mode) {
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
    IoHelper.write(stream, this.status.drawable);
    IoHelper.write(stream, this.status.setMaskBit);
    IoHelper.write(stream, this.status.drawPixels);
    IoHelper.write(stream, this.status.interlaceField);
    IoHelper.write(stream, this.status.horizontalResolution);
    IoHelper.write(stream, this.status.verticalResolution);
    IoHelper.write(stream, this.status.displayAreaColourDepth);

    IoHelper.write(stream, this.displayStartX);
    IoHelper.write(stream, this.displayStartY);
    IoHelper.write(stream, this.displayRangeY1);
    IoHelper.write(stream, this.displayRangeY2);
    IoHelper.write(stream, this.drawingArea);
    IoHelper.write(stream, this.offsetX);
    IoHelper.write(stream, this.offsetY);
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
    this.status.semiTransparency = IoHelper.readEnum(buf, Translucency.class);
    this.status.texturePageColours = IoHelper.readEnum(buf, legend.core.gpu.Bpp.class);
    this.status.drawable = IoHelper.readBool(buf);
    this.status.setMaskBit = IoHelper.readBool(buf);
    this.status.drawPixels = IoHelper.readEnum(buf, DRAW_PIXELS.class);
    this.status.interlaceField = IoHelper.readBool(buf);
    this.status.horizontalResolution = IoHelper.readEnum(buf, HORIZONTAL_RESOLUTION.class);
    this.status.verticalResolution = IoHelper.readEnum(buf, VERTICAL_RESOLUTION.class);
    this.status.displayAreaColourDepth = IoHelper.readEnum(buf, DISPLAY_AREA_COLOUR_DEPTH.class);

    if(version < 3) {
      IoHelper.readLong(buf);
      IoHelper.readInt(buf);
    }

    this.displayStartX = IoHelper.readInt(buf);
    this.displayStartY = IoHelper.readInt(buf);
    this.displayRangeY1 = IoHelper.readInt(buf);
    this.displayRangeY2 = IoHelper.readInt(buf);
    IoHelper.readRect(buf, this.drawingArea);
    this.offsetX = IoHelper.readShort(buf);
    this.offsetY = IoHelper.readShort(buf);

    final int horizontalRes = this.status.horizontalResolution.res;
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
    FILL_RECTANGLE_IN_VRAM(0x02, 3, (buffer, gpu) -> {
      final int colour = buffer.getInt(0) & 0xff_ffff;

      final int vertex = buffer.getInt(1);
      final int y = (short)((vertex & 0xffff0000) >>> 16) * gpu.renderScale;
      final int x = (short)(vertex & 0xffff) * gpu.renderScale;

      final int size = buffer.getInt(2);
      final int h = (short)((size & 0xffff0000) >>> 16) * gpu.renderScale;
      final int w = (short)(size & 0xffff) * gpu.renderScale;

      return () -> gpu.command02FillRect(x, y, w, h, colour);
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

    MONOCHROME_RECT_1X1_TRANS(0x6a, 2, (buffer, gpu) -> {
      final int command = buffer.getInt(0);
      final int vertex = buffer.getInt(1);
      return gpu.untexturedRectangleBuilder(command, vertex, 0x1_0001);
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

      return () -> gpu.command80CopyRectFromVramToVram(sourceX, sourceY, destX, destY, width, height);
    }),

    DRAW_MODE_SETTINGS(0xe1, 1, (buffer, gpu) -> {
      final int settings = buffer.getInt(0);

      return () -> {
        LOGGER.trace("[GP0.e1] Draw mode set to %08x", settings);

        gpu.status.texturePageXBase = (settings & 0b1111) * 64 * gpu.renderScale;
        gpu.status.texturePageYBase = (settings & 0b1_0000) != 0 ? TEXTURE_PAGE_Y_BASE.BASE_256 : TEXTURE_PAGE_Y_BASE.BASE_0;
        gpu.status.semiTransparency = Translucency.values()[(settings & 0b110_0000) >>> 5];
        gpu.status.texturePageColours = Bpp.values()[(settings & 0b1_1000_0000) >>> 7];
        gpu.status.drawable = (settings & 0b100_0000_0000) != 0;
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

    public static GP0_COMMAND getCommand(final int command) throws InvalidGp0CommandException {
      for(final GP0_COMMAND cmd : GP0_COMMAND.values()) {
        if(cmd.command == command) {
          return cmd;
        }
      }

      throw new InvalidGp0CommandException("Invalid GP0 command " + Long.toString(command, 16));
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

    private Gp0CommandBuffer(final int command) throws InvalidGp0CommandException {
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
    public Translucency semiTransparency = Translucency.HALF_B_PLUS_HALF_F;
    /**
     * Bits 7-8 - Texture page colours
     */
    public Bpp texturePageColours = Bpp.BITS_4;

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
     * Bits 17-18 - Horizontal resolution 1
     */
    public HORIZONTAL_RESOLUTION horizontalResolution = HORIZONTAL_RESOLUTION._256;
    /**
     * Bit 19 - Vertical resolution
     */
    public VERTICAL_RESOLUTION verticalResolution = VERTICAL_RESOLUTION._240;

    /**
     * Bit 21 - Display area colour depth
     */
    public DISPLAY_AREA_COLOUR_DEPTH displayAreaColourDepth = DISPLAY_AREA_COLOUR_DEPTH.BITS_15;
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

  public enum DRAW_PIXELS {
    ALWAYS,
    NOT_TO_MASKED_AREAS,
  }

  public enum HORIZONTAL_RESOLUTION {
    _256(256),
    _368(368),
    _320(320),
    _512(512),
    _640(640),
    ;

    public final int res;

    HORIZONTAL_RESOLUTION(final int res) {
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

  public enum DISPLAY_AREA_COLOUR_DEPTH {
    BITS_15,
    BITS_24,
  }
}
