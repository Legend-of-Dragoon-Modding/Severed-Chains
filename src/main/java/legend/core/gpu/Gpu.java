package legend.core.gpu;

import legend.core.Config;
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
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

import static legend.core.GameEngine.MEMORY;
import static legend.core.MathHelper.colour24To15;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickGUID;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickName;
import static org.lwjgl.glfw.GLFW.glfwJoystickPresent;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;

public class Gpu implements Runnable {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Gpu.class);

  private static final int STANDARD_VRAM_WIDTH = 1024;
  private static final int STANDARD_VRAM_HEIGHT = 512;

  private final int vramWidth = STANDARD_VRAM_WIDTH;
  private final int vramHeight = STANDARD_VRAM_HEIGHT;

  private Camera camera;
  private Window window;
  private Context ctx;
  private Shader.UniformBuffer transforms2;
  private final Matrix4f transforms = new Matrix4f();

  private final int[] vram24 = new int[this.vramWidth * this.vramHeight];
  private final int[] vram15 = new int[this.vramWidth * this.vramHeight];

  private boolean isVramViewer;

  private Shader vramShader;
  private Texture vramTexture;
  private Mesh vramMesh;

  private Texture displayTexture;
  private Mesh displayMesh;

  private int windowWidth;
  private int windowHeight;

  public final Status status = new Status();

  private int displayStartX;
  private int displayStartY;
  private int displayRangeY1;
  private int displayRangeY2;
  public final RECT drawingArea = new RECT();
  private short offsetX;
  private short offsetY;

  private int zMax;
  private LinkedList<GpuCommand>[] zQueues;

  private boolean displayChanged;

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

    final int rectX = rect.x.get();
    final int rectY = rect.y.get();
    final int rectW = rect.w.get();
    final int rectH = rect.h.get();

    assert rectX + rectW <= this.vramWidth : "Rect right (" + (rectX + rectW) + ") overflows VRAM width (" + this.vramWidth + ')';
    assert rectY + rectH <= this.vramHeight : "Rect bottom (" + (rectY + rectH) + ") overflows VRAM height (" + this.vramHeight + ')';

    LOGGER.debug("Copying (%d, %d, %d, %d) from CPU to VRAM (address: %08x)", rectX, rectY, rectW, rectH, address);

    MEMORY.waitForLock(() -> {
      int i = 0;
      for(int y = rectY; y < rectY + rectH; y++) {
        for(int x = rectX; x < rectX + rectW; x++) {
          final int packed = (int)MEMORY.get(address + i, 2);
          final int unpacked = MathHelper.colour15To24(packed);

          final int index = y * this.vramWidth + x;
          this.vram24[index] = unpacked;
          this.vram15[index] = packed;

          i += 2;
        }
      }
    });
  }

  public void uploadData(final RECT rect, final byte[] data, final int offset) {
    final int rectX = rect.x.get();
    final int rectY = rect.y.get();
    final int rectW = rect.w.get();
    final int rectH = rect.h.get();

    assert rectX + rectW <= this.vramWidth : "Rect right (" + (rectX + rectW) + ") overflows VRAM width (" + this.vramWidth + ')';
    assert rectY + rectH <= this.vramHeight : "Rect bottom (" + (rectY + rectH) + ") overflows VRAM height (" + this.vramHeight + ')';

    LOGGER.debug("Copying (%d, %d, %d, %d) from CPU to VRAM", rectX, rectY, rectW, rectH);

    MEMORY.waitForLock(() -> {
      int i = offset;
      for(int y = rectY; y < rectY + rectH; y++) {
        for(int x = rectX; x < rectX + rectW; x++) {
          final int packed = (int)MathHelper.get(data, i, 2);
          final int unpacked = MathHelper.colour15To24(packed);

          final int index = y * this.vramWidth + x;
          this.vram24[index] = unpacked;
          this.vram15[index] = packed;

          i += 2;
        }
      }
    });
  }

  public void commandC0CopyRectFromVramToCpu(final RECT rect, final long address) {
    assert address != 0;

    final int rectX = rect.x.get();
    final int rectY = rect.y.get();
    final int rectW = rect.w.get();
    final int rectH = rect.h.get();

    assert rectX + rectW <= this.vramWidth : "Rect right (" + (rectX + rectW) + ") overflows VRAM width (" + this.vramWidth + ')';
    assert rectY + rectH <= this.vramHeight : "Rect bottom (" + (rectY + rectH) + ") overflows VRAM height (" + this.vramHeight + ')';

    LOGGER.debug("Copying (%d, %d, %d, %d) from VRAM to CPU (address: %08x)", rectX, rectY, rectW, rectH, address);

    MEMORY.waitForLock(() -> {
      int i = 0;
      for(int y = rectY; y < rectY + rectH; y++) {
        for(int x = rectX; x < rectX + rectW; x++) {
          final int index = y * this.vramWidth + x;
          MEMORY.set(address + i, 2, this.vram15[index]);
          i += 2;
        }
      }
    });
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

  public void queueCommand(final int z, final GpuCommand command) {
    this.zQueues[z].addFirst(command);
  }

  public void reset() {
    LOGGER.info("Resetting GPU");

    this.resetCommandBuffer();
    this.displayStart(0, 0);
    this.verticalDisplayRange(16, 256);
    this.displayMode(HORIZONTAL_RESOLUTION._320, VERTICAL_RESOLUTION._240, DISPLAY_AREA_COLOUR_DEPTH.BITS_15);
  }

  /**
   * GP1(01h) - Reset Command Buffer
   */
  public void resetCommandBuffer() {
    if(this.zQueues != null) {
      for(int z = this.zQueues.length - 1; z >= 0; z--) {
        this.zQueues[z].clear();
      }
    }
  }

  /**
   * GP1(05h) - Start of Display Area in VRAM
   *
   * Upper/left Display source address in VRAM. The size and target position on screen is set via Display Range registers; target=X1,Y2; size=(X2-X1/cycles_per_pix), (Y2-Y1).
   */
  public void displayStart(final int x, final int y) {
    this.displayStartX = x;
    this.displayStartY = y;
  }

  /**
   * GP1(07h) - Vertical Display Range (on Screen)
   *
   * Specifies the vertical range within which the display area is displayed. The number of lines is Y2-Y1 (unlike as for the width, there's no rounding applied to the height). If Y2 is set to a much too large value, then the hardware stops to generate vblank interrupts (IRQ0).
   * The 88h/A3h values are the middle-scanlines on normal TV Sets, these values are used by MOST NTSC games, and SOME PAL games (see below notes on Mis-Centered PAL games).
   * The 224/264 values are for fullscreen pictures. Many NTSC games display 240 lines (overscan with hidden lines). Many PAL games display only 256 lines (underscan with black borders).
   */
  public void verticalDisplayRange(final int y1, final int y2) {
    this.displayRangeY1 = y1;
    this.displayRangeY2 = y2;
  }

  /**
   * GP1(08h) - Display Mode
   *
   * Note: The Display Area Color Depth does NOT affect the Drawing Area (the Drawing Area is always 15bit).
   */
  public void displayMode(final HORIZONTAL_RESOLUTION hRes, final VERTICAL_RESOLUTION vRes, final DISPLAY_AREA_COLOUR_DEPTH dispColourDepth) {
    this.status.horizontalResolution = hRes;
    this.status.verticalResolution = vRes;
    this.status.displayAreaColourDepth = dispColourDepth;

    // Always run on the GPU thread
    if(glfwGetCurrentContext() == 0) {
      this.displayChanged = true;
      return;
    }

    this.displaySize(hRes.res, vRes.res);
  }

  public void displaySize(final int horizontalRes, final int verticalRes) {
    if(this.displayTexture != null) {
      if(this.displayTexture.width == horizontalRes && this.displayTexture.height == verticalRes) {
        return;
      }

      this.displayTexture.delete();
    }

    this.displayTexture = Texture.empty(horizontalRes, verticalRes);

    this.updateDisplayTexture(this.window, this.windowWidth, this.windowHeight);
  }

  public void displayTexture(final int[] pixels) {
    this.displayTexture.data(0, 0, this.displayTexture.width, this.displayTexture.height, pixels);
  }

  public void drawingArea(final int left, final int top, final int right, final int bottom) {
    this.drawingArea.set((short)left, (short)top, (short)right, (short)bottom);
  }

  public void drawingOffset(final int x, final int y) {
    this.offsetX = (short)x;
    this.offsetY = (short)y;
  }

  public void maskBit(final boolean setMaskBit, final DRAW_PIXELS drawPixels) {
    this.status.setMaskBit = setMaskBit;
    this.status.drawPixels = drawPixels;
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

  private boolean ready;
  private double vsyncCount;
  private long lastFrame;
  public Runnable mainRenderer;
  public Runnable subRenderer = () -> { };

  public int getVsyncCount() {
    return (int)this.vsyncCount;
  }

  public boolean isReady() {
    return this.ready;
  }

  @Override
  public void run() {
    this.camera = new Camera(0.0f, 0.0f);
    this.window = new Window("Legend of Dragoon", Config.windowWidth(), Config.windowHeight());
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

    this.window.events.onResize((window1, width, height) -> this.updateDisplayTexture(window1, (int)(width / window1.getScale()), (int)(height / window1.getScale())));

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

    if(this.mainRenderer == null) {
      this.setStandardRenderer();
    }

    this.ctx.onDraw(() -> {
      // Restore model buffer to identity
      this.transforms.identity();
      this.transforms2.set(this.transforms);

      this.mainRenderer.run();

      final float fps = 1.0f / ((System.nanoTime() - this.lastFrame) / (1_000_000_000 / 30.0f)) * 30.0f;
      this.window.setTitle("Legend of Dragoon - FPS: %.2f/%d".formatted(fps, this.window.getFpsLimit()));
      this.lastFrame = System.nanoTime();
      this.vsyncCount += 60.0d / this.window.getFpsLimit();
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

    this.ready = true;

    try {
      this.window.run();
    } catch(final Throwable t) {
      LOGGER.error("Shutting down due to GPU exception:", t);
      this.window.close();
    }
  }

  private void updateDisplayTexture(final Window window, final int width, final int height) {
    if(!this.isVramViewer) {

      this.windowWidth = width;
      this.windowHeight = height;

      if(this.displayMesh != null) {
        this.displayMesh.delete();
      }

      final float aspect = (float)this.displayTexture.width / this.displayTexture.height;

      float w = width;
      float h = w / aspect;

      if(h > height) {
        h = height;
        w = h * aspect;
      }

      final float l = (width - w) / 2;
      final float t = (height - h) / 2;
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
  }

  public void setStandardRenderer() {
    this.mainRenderer = () -> {
      if(this.zMax != orderingTableSize_1f8003c8.get()) {
        this.updateOrderingTableSize(orderingTableSize_1f8003c8.get());
      }

      this.subRenderer.run();
      this.tick();
    };
  }

  public void updateOrderingTableSize(final int size) {
    final LinkedList<GpuCommand>[] list = new LinkedList[size];
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
    if(this.displayChanged) {
      this.displaySize(this.status.horizontalResolution.res, this.status.verticalResolution.res);
      this.displayChanged = false;
    }

    if(this.isVramViewer) {
      final int size = this.vramWidth * this.vramHeight;
      final ByteBuffer pixels = MemoryUtil.memAlloc(size * 4);
      final IntBuffer intPixels = pixels.asIntBuffer();
      intPixels.put(this.vram24);

      pixels.flip();

      this.vramTexture.data(0, 0, this.vramWidth, this.vramHeight, pixels);

      this.vramShader.use();
      this.vramTexture.use();
      this.vramMesh.draw();

      MemoryUtil.memFree(pixels);
    } else if(this.status.displayAreaColourDepth == DISPLAY_AREA_COLOUR_DEPTH.BITS_24) {
      int yRangeOffset = 240 - (this.displayRangeY2 - this.displayRangeY1) >> (this.status.verticalResolution == VERTICAL_RESOLUTION._480 ? 0 : 1);
      if(yRangeOffset < 0) {
        yRangeOffset = 0;
      }

      final int size = this.displayTexture.width * this.displayTexture.height;
      final ByteBuffer pixels = MemoryUtil.memAlloc(size * 4);
      final IntBuffer pixelsInt = pixels.asIntBuffer();

      for(int y = yRangeOffset; y < this.status.verticalResolution.res - yRangeOffset; y++) {
        int offset = 0;
        for(int x = 0; x < this.status.horizontalResolution.res; x += 2) {
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

      this.displayTexture.data(0, 0, this.displayTexture.width, this.displayTexture.height, pixels);

      this.drawMesh();

      MemoryUtil.memFree(pixels);
    } else { // 15bpp
      int yRangeOffset = 240 - (this.displayRangeY2 - this.displayRangeY1) >> (this.status.verticalResolution == VERTICAL_RESOLUTION._480 ? 0 : 1);
      if(yRangeOffset < 0) {
        yRangeOffset = 0;
      }

      final ByteBuffer vram = MemoryUtil.memAlloc(this.vram24.length * 4);
      final IntBuffer intVram = vram.asIntBuffer();
      intVram.put(this.vram24);

      final int size = this.displayTexture.width * this.displayTexture.height;
      final ByteBuffer pixels = MemoryUtil.memAlloc(size * 4);
      final byte[] from = new byte[this.displayTexture.width * 4];

      for(int y = yRangeOffset; y < this.status.verticalResolution.res - yRangeOffset; y++) {
        vram.get((this.displayStartX + (y - yRangeOffset + this.displayStartY) * this.vramTexture.width) * 4, from);
        pixels.put(from, 0, from.length);
      }

      pixels.flip();

      this.displayTexture.data(0, 0, this.displayTexture.width, this.displayTexture.height, pixels);

      this.drawMesh();

      MemoryUtil.memFree(vram);
      MemoryUtil.memFree(pixels);
    }

    if(this.zQueues != null) {
      for(int z = this.zQueues.length - 1; z >= 0; z--) {
        for(final GpuCommand command : this.zQueues[z]) {
          command.render(this);
        }

        this.zQueues[z].clear();
      }
    }
  }

  public void drawMesh() {
    this.vramShader.use();
    this.displayTexture.use();
    this.displayMesh.draw();
  }

  public int getOffsetX() {
    return this.offsetX;
  }

  public int getOffsetY() {
    return this.offsetY;
  }

  public int getDisplayTextureWidth() {
    return this.displayTexture.width;
  }

  public int getDisplayTextureHeight() {
    return this.displayTexture.height;
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
      int colour = interpolateColours(colour1, colour2, ratio);

      if(x >= this.drawingArea.x.get() && x < this.drawingArea.w.get() && y >= this.drawingArea.y.get() && y < this.drawingArea.h.get()) {
        if(translucency != null) {
          colour = this.handleTranslucence(x, y, colour, translucency);
        }

        colour |= (this.status.setMaskBit ? 1 : 0) << 24;

        this.setPixel(x, y, colour);
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
    final int index = y * this.vramWidth + x;

    if(index < 0 || index >= this.vram15.length) {
      throw new IndexOutOfBoundsException("Index %d out of bounds for length %d".formatted(index, this.vram15.length));
    }

    return this.vram15[index];
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

    return (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
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
    final int p = index >> (x & 3) * 4 & 0xf;
    return this.getPixel(clutX + p, clutY);
  }

  private int get8bppTexel(final int x, final int y, final int clutX, final int clutY, final int textureBaseX, final int textureBaseY) {
    final int index = this.getPixel15(x / 2 + textureBaseX, y + textureBaseY);
    final int p = index >> (x & 1) * 8 & 0xff;
    return this.getPixel(clutX + p, clutY);
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

  public static class Status {
    /**
     * Bit 11 - Set mask bit when drawing pixels
     */
    public boolean setMaskBit;
    /**
     * Bit 12 - Draw pixels
     */
    public DRAW_PIXELS drawPixels = DRAW_PIXELS.ALWAYS;

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
