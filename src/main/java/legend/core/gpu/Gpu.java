package legend.core.gpu;

import legend.core.Config;
import legend.core.MathHelper;
import legend.core.ProjectionMode;
import legend.core.opengl.Mesh;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderManager;
import legend.core.opengl.Texture;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.coremod.config.RenderScaleConfigEntry;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.RENDERER;
import static legend.core.MathHelper.colour24To15;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_EQUAL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_MINUS;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_CONTROL;
import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.opengl.GL11C.GL_BLEND;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11C.glDisable;

public class Gpu {
  private static final Logger LOGGER = LogManager.getFormatterLogger();

  private static final int STANDARD_VRAM_WIDTH = 1024;
  private static final int STANDARD_VRAM_HEIGHT = 512;

  public final int vramWidth = STANDARD_VRAM_WIDTH;
  public final int vramHeight = STANDARD_VRAM_HEIGHT;

  private final VramTextureSingle[] renderBuffers = new VramTextureSingle[2];
  private int drawBufferIndex;

  private int scale = 1;
  private int newScale;

  private final int[] vram24 = new int[this.vramWidth * this.vramHeight];
  private final int[] vram15 = new int[this.vramWidth * this.vramHeight];

  private Shader vramShader;
  private Shader.UniformVec4 vramShaderColour;

  private Texture displayTexture;
  private Mesh displayMesh;

  private int windowWidth;
  private int windowHeight;

  public final Status status = new Status();

  public final RECT drawingArea = new RECT();
  public final RECT scaledDrawingArea = new RECT();
  private short offsetX;
  private short offsetY;

  private int zMax;
  private List<GpuCommand>[] zQueues;

  private boolean displayChanged;

  public int getDrawBufferIndex() {
    return this.drawBufferIndex;
  }

  public VramTextureSingle getDisplayBuffer() {
    return this.renderBuffers[this.drawBufferIndex ^ 1];
  }

  public VramTextureSingle getDrawBuffer() {
    return this.renderBuffers[this.drawBufferIndex];
  }

  public int getScale() {
    return this.scale;
  }

  /** Schedule a rescale when the current frame completes */
  public void rescale(final int scale) {
    this.newScale = scale;
  }

  /** Rescale immediately - may cause issues if commands are in the queue */
  public void rescaleNow(final int scale) {
    this.scale = scale;
    this.displaySize(this.status.horizontalResolution, this.status.verticalResolution);
    this.drawingArea(this.drawingArea.x.get(), this.drawingArea.y.get(), this.drawingArea.w.get(), this.drawingArea.h.get());
  }

  public void init() {
    RENDERER.events().onResize((window1, width, height) -> this.updateDisplayTexture(width, height));

    RENDERER.events().onKeyPress((window, key, scancode, mods) -> {
      if(key == GLFW_KEY_EQUAL) {
        if(mods == 0) {
          Config.setGameSpeedMultiplier(Config.getGameSpeedMultiplier() + 1);
        } else if((mods & GLFW_MOD_CONTROL) != 0 && gameState_800babc8 != null) {
          final RenderScaleConfigEntry config = CoreMod.RENDER_SCALE_CONFIG.get();
          final int scale = CONFIG.getConfig(config) + 1;

          if(scale <= RenderScaleConfigEntry.MAX) {
            CONFIG.setConfig(config, scale);
            GPU.rescale(scale);
          }
        }
      }

      if(key == GLFW_KEY_MINUS) {
        if(mods == 0) {
          Config.setGameSpeedMultiplier(Config.getGameSpeedMultiplier() - 1);
        } else if((mods & GLFW_MOD_CONTROL) != 0 && gameState_800babc8 != null) {
          final RenderScaleConfigEntry config = CoreMod.RENDER_SCALE_CONFIG.get();
          final int scale = CONFIG.getConfig(config) - 1;

          if(scale >= 1) {
            CONFIG.setConfig(config, scale);
            GPU.rescale(scale);
          }
        }
      }
    });

    this.vramShader = ShaderManager.getShader("simple");
    this.vramShaderColour = this.vramShader.new UniformVec4("recolour");

    this.displaySize(320, 240);
  }

  public void startFrame() {
    if(this.zMax != orderingTableSize_1f8003c8.get()) {
      this.updateOrderingTableSize(orderingTableSize_1f8003c8.get());
    }
  }

  public void endFrame() {
    this.tick();
    RENDERER.window().setTitle("Legend of Dragoon - FPS: %.2f/%d scale: %d res: %dx%d".formatted(RENDERER.getFps(), RENDERER.window().getFpsLimit(), this.scale, this.displayTexture.width, this.displayTexture.height));
  }

  private void updateDisplayTexture(final int width, final int height) {
    this.windowWidth = width;
    this.windowHeight = height;

    if(this.displayMesh != null) {
      this.displayMesh.delete();
    }

    final float aspect = 4.0f / 3.0f;

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
      l, t, 1.0f, 0, 0,
      l, b, 1.0f, 0, 1,
      r, t, 1.0f, 1, 0,
      r, b, 1.0f, 1, 1,
    }, 4);
    this.displayMesh.attribute(0, 0L, 3, 5);
    this.displayMesh.attribute(1, 3L, 2, 5);
  }

  private void tick() {
    if(this.displayChanged) {
      this.displaySize(this.status.horizontalResolution, this.status.verticalResolution);
      this.displayChanged = false;
    }

    this.displayTexture.data(0, 0, this.displayTexture.width, this.displayTexture.height, this.getDisplayBuffer().getData());
    this.drawMesh();

    if(this.zQueues != null) {
      for(int z = this.zQueues.length - 1; z >= 0; z--) {
        final List<GpuCommand> queue = this.zQueues[z];

        for(int i = queue.size() - 1; i >= 0; i--) {
          queue.get(i).render(this);
        }

        queue.clear();
      }
    }

    if(this.newScale != 0) {
      this.rescaleNow(this.newScale);
      this.newScale = 0;
    }

    this.drawBufferIndex ^= 1;
  }

  public void clear(final int colour) {
    LOGGER.trace("Clear display RGB %06x", colour);

    this.getDrawBuffer().fill(colour);
  }

  public void commandA0CopyRectFromCpuToVram(final RECT rect, final long address) {
    assert address != 0;

    final int rectX = rect.x.get();
    final int rectY = rect.y.get();
    final int rectW = rect.w.get();
    final int rectH = rect.h.get();

    if(rectX + rectW > this.vramWidth) {
      throw new IllegalArgumentException("Rect right (" + (rectX + rectW) + ") overflows VRAM width (" + this.vramWidth + ')');
    }

    if(rectY + rectH > this.vramHeight) {
      throw new IllegalArgumentException("Rect bottom (" + (rectY + rectH) + ") overflows VRAM height (" + this.vramHeight + ')');
    }

    LOGGER.debug("Copying (%d, %d, %d, %d) from CPU to VRAM (address: %08x)", rectX, rectY, rectW, rectH, address);

    MEMORY.waitForLock(() -> {
      int i = 0;
      for(int y = rectY; y < rectY + rectH; y++) {
        for(int x = rectX; x < rectX + rectW; x++) {
          final int packed = (int)MEMORY.get(address + i, 2);
          final int unpacked = MathHelper.colour15To24(packed);

          this.setVramPixel(x, y, unpacked, packed);

          i += 2;
        }
      }
    });
  }

  public void uploadData(final RECT rect, final FileData data) {
    final int rectX = rect.x.get();
    final int rectY = rect.y.get();
    final int rectW = rect.w.get();
    final int rectH = rect.h.get();

    assert rectX + rectW <= this.vramWidth : "Rect right (" + (rectX + rectW) + ") overflows VRAM width (" + this.vramWidth + ')';
    assert rectY + rectH <= this.vramHeight : "Rect bottom (" + (rectY + rectH) + ") overflows VRAM height (" + this.vramHeight + ')';

    LOGGER.debug("Copying (%d, %d, %d, %d) from CPU to VRAM", rectX, rectY, rectW, rectH);

    int i = 0;
    for(int y = rectY; y < rectY + rectH; y++) {
      for(int x = rectX; x < rectX + rectW; x++) {
        // Sometimes the rect is larger than the data (see: the DEFF stuff where animations are loaded into VRAM for some reason)
        if(i + 1 >= data.size()) {
          break;
        }

        final int packed = data.readUShort(i);
        final int unpacked = MathHelper.colour15To24(packed);

        this.setVramPixel(x, y, unpacked, packed);

        i += 2;
      }
    }
  }

  public void uploadData(final RECT rect, final short[] data) {
    final int rectX = rect.x.get();
    final int rectY = rect.y.get();
    final int rectW = rect.w.get();
    final int rectH = rect.h.get();

    assert rectX + rectW <= this.vramWidth : "Rect right (" + (rectX + rectW) + ") overflows VRAM width (" + this.vramWidth + ')';
    assert rectY + rectH <= this.vramHeight : "Rect bottom (" + (rectY + rectH) + ") overflows VRAM height (" + this.vramHeight + ')';

    LOGGER.debug("Copying (%d, %d, %d, %d) from CPU to VRAM", rectX, rectY, rectW, rectH);

    int i = 0;
    for(int y = rectY; y < rectY + rectH; y++) {
      for(int x = rectX; x < rectX + rectW; x++) {
        this.setVramPixel(x, y, MathHelper.colour15To24(data[i]), data[i]);
        i++;
      }
    }
  }

  public void uploadData(final Rect4i rect, final int[] data) {
    final int rectX = rect.x();
    final int rectY = rect.y();
    final int rectW = rect.w();
    final int rectH = rect.h();

    assert rectX + rectW <= this.vramWidth : "Rect right (" + (rectX + rectW) + ") overflows VRAM width (" + this.vramWidth + ')';
    assert rectY + rectH <= this.vramHeight : "Rect bottom (" + (rectY + rectH) + ") overflows VRAM height (" + this.vramHeight + ')';

    LOGGER.debug("Copying (%d, %d, %d, %d) from CPU to VRAM", rectX, rectY, rectW, rectH);

    int i = 0;
    for(int y = rectY; y < rectY + rectH; y++) {
      for(int x = rectX; x < rectX + rectW; x++) {
        this.setVramPixel(x, y, data[i], colour24To15(data[i]));
        i++;
      }
    }
  }

  public void commandC0CopyRectFromVramToCpu(final RECT rect, final FileData out) {
    final int rectX = rect.x.get();
    final int rectY = rect.y.get();
    final int rectW = rect.w.get();
    final int rectH = rect.h.get();

    assert rectX + rectW <= this.vramWidth : "Rect right (" + (rectX + rectW) + ") overflows VRAM width (" + this.vramWidth + ')';
    assert rectY + rectH <= this.vramHeight : "Rect bottom (" + (rectY + rectH) + ") overflows VRAM height (" + this.vramHeight + ')';

    LOGGER.debug("Copying (%d, %d, %d, %d) from VRAM to byte array", rectX, rectY, rectW, rectH);

    int i = 0;
    for(int y = rectY; y < rectY + rectH; y++) {
      for(int x = rectX; x < rectX + rectW; x++) {
        final int pixel = this.getPixel15(x, y);
        out.writeByte(i, (byte)(pixel & 0xff));
        out.writeByte(i + 1, (byte)(pixel >>> 8));
        i += 2;
      }
    }
  }

  public void command80CopyRectFromVramToVram(final int sourceX, final int sourceY, final int destX, final int destY, final int width, final int height) {
    LOGGER.debug("COPY VRAM VRAM from %d %d to %d %d size %d %d", sourceX, sourceY, destX, destY, width, height);

    for(int y = 0; y < height; y++) {
      for(int x = 0; x < width; x++) {
        int colour15 = this.getPixel15(sourceX + x, sourceY + y);
        int colour24 = this.getPixel(sourceX + x, sourceY + y);

        if(this.status.drawPixels == DRAW_PIXELS.NOT_TO_MASKED_AREAS) {
          if((this.getPixel(destX + x, destY + y) & 0xff00_0000L) != 0) {
            continue;
          }
        }

        colour15 |= (this.status.setMaskBit ? 1 : 0) << 15;
        colour24 |= (this.status.setMaskBit ? 1 : 0) << 24;

        this.setVramPixel(destX + x, destY + y, colour24, colour15);
      }
    }
  }

  public void queueCommand(final int z, final GpuCommand command) {
    this.zQueues[z].add(command);
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
   * GP1(08h) - Display Mode
   */
  public void displayMode(int width, final int height) {
    if(width == 384) {
      width = 368;
    }

    this.status.horizontalResolution = width;
    this.status.verticalResolution = height;

    // Always run on the GPU thread
    if(glfwGetCurrentContext() == 0) {
      this.displayChanged = true;
      return;
    }

    this.displaySize(width, height);
  }

  public void displaySize(final int horizontalRes, final int verticalRes) {
    if(this.displayTexture != null) {
      this.displayTexture.delete();
    }

    final int scaledWidth = horizontalRes * this.scale;
    final int scaledHeight = verticalRes * this.scale;

    for(int i = 0; i < this.renderBuffers.length; i++) {
      this.renderBuffers[i] = new VramTextureSingle(Bpp.BITS_24, new Rect4i(0, 0, scaledWidth, scaledHeight), new int[scaledWidth * scaledHeight]);
    }

    this.displayTexture = Texture.empty(scaledWidth, scaledHeight);

    this.updateDisplayTexture(this.windowWidth, this.windowHeight);
  }

  public void drawingArea(final int left, final int top, int width, final int height) {
    if(width == 384) {
      width = 368;
    }

    this.drawingArea.set((short)left, (short)top, (short)width, (short)height);
    this.scaledDrawingArea.set((short)(left * this.scale), (short)(top * this.scale), (short)(width * this.scale), (short)(height * this.scale));
  }

  public void drawingOffset(final int x, final int y) {
    this.offsetX = (short)x;
    this.offsetY = (short)y;
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

  public void updateOrderingTableSize(final int size) {
    final List<GpuCommand>[] list = new ArrayList[size];
    Arrays.setAll(list, key -> new ArrayList<>());

    this.zMax = size;
    this.zQueues = list;
  }

  public void drawMesh() {
    RENDERER.setProjectionMode(ProjectionMode._2D);

    glDisable(GL_BLEND);

    this.vramShader.use();
    this.vramShaderColour.set(1.0f, 1.0f, 1.0f, 1.0f);
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
    return this.displayTexture.width / this.scale;
  }

  public int getDisplayTextureHeight() {
    return this.displayTexture.height / this.scale;
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

      if(this.drawingArea.contains(x, y)) {
        if(translucency != null) {
          colour = this.handleTranslucence(x * this.scale, y * this.scale, colour, translucency);
        }

        colour |= (this.status.setMaskBit ? 1 : 0) << 24;

        for(int xx = 0; xx < this.scale; xx++) {
          for(int yy = 0; yy < this.scale; yy++) {
            this.getDrawBuffer().setPixel(x * this.scale + xx, y * this.scale + yy, colour);
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

  void rasterizeTriangle(int vx0, int vy0, int vx1, int vy1, int vx2, int vy2, final int tu0, final int tv0, int tu1, int tv1, int tu2, int tv2, final int c0, int c1, int c2, final int clutX, final int clutY, final int textureBaseX, final int textureBaseY, final Bpp bpp, final boolean isTextured, final boolean isShaded, final boolean isTranslucent, final boolean isRaw, final Translucency translucencyMode, @Nullable final VramTexture texture, @Nullable final VramTexture[] palettes) {
    vx0 *= this.scale;
    vy0 *= this.scale;
    vx1 *= this.scale;
    vy1 *= this.scale;
    vx2 *= this.scale;
    vy2 *= this.scale;

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

    /*clip*/
    minX = (short)Math.max(minX, this.scaledDrawingArea.x.get());
    minY = (short)Math.max(minY, this.scaledDrawingArea.y.get());
    maxX = (short)Math.min(maxX, this.scaledDrawingArea.x.get() + this.scaledDrawingArea.w.get());
    maxY = (short)Math.min(maxY, this.scaledDrawingArea.y.get() + this.scaledDrawingArea.h.get());

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

            int texel;
            if(texture == null) {
              texel = this.getTexel(texelX, texelY, clutX, clutY, textureBaseX, textureBaseY, bpp);
            } else {
              texel = 0;
              if(palettes == null) {
                if(texture == this.getDisplayBuffer() || texture == this.getDrawBuffer()) {
                  if(texelX < this.drawingArea.x.get() + this.drawingArea.w.get() && texelY < this.drawingArea.y.get() + this.drawingArea.h.get()) {
                    texel = texture.getPixel(texelX * this.scale, texelY * this.scale) & 0xffffff;
                  }
                } else {
                  texel = texture.getPixel(texelX, texelY) & 0xffffff;
                }
              } else {
                boolean found = false;
                for(final VramTexture palette : palettes) {
                  if(palette.rect.y() - clutY == 0) {
                    texel = texture.getTexel(palette, textureBaseX, texelX, texelY);
                    found = true;
                    break;
                  }
                }

                if(!found) {
                  throw new RuntimeException("Failed to find palette");
                }
              }
            }

            if(texel == 0) {
              w0 += A12;
              w1 += A20;
              w2 += A01;
              continue;
            }

            if(!isRaw) {
              texel = applyBlending(colour, texel);
            }

            colour = texel;
          }

          if(isTranslucent && (!isTextured || (colour & 0xff00_0000) != 0)) {
            colour = this.handleTranslucence(x, y, colour, translucencyMode);
          }

          colour |= (this.status.setMaskBit ? 1 : 0) << 24;

          this.getDrawBuffer().setPixel(x, y, colour);
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

  void rasterizeQuad(int x1, int y1, int x2, int y2, final int colour, final boolean raw, final boolean textured, int u1, int v1, final int clutX, final int clutY, final int vramX, final int vramY, final Bpp bpp, @Nullable final Translucency translucency, @Nullable final VramTexture texture, @Nullable final VramTexture[] palettes) {
    // If we're dealing with a render buffer texture we need to process the entire thing scaled. If we're doing
    // a regular render, we only need to render at 1x and then duplicate the pixels `this.scale` times.
    if(texture == this.getDrawBuffer() || texture == this.getDisplayBuffer()) {
      x1 *= this.scale;
      y1 *= this.scale;
      x2 *= this.scale;
      y2 *= this.scale;
      u1 *= this.scale;
      v1 *= this.scale;

      for(int y = y1, v = v1; y < y2; y++, v++) {
        for(int x = x1, u = u1; x < x2; x++, u++) {
          // Check background mask
          if(this.status.drawPixels == DRAW_PIXELS.NOT_TO_MASKED_AREAS) {
            if((this.getPixel(x, y) & 0xff00_0000L) != 0) {
              continue;
            }
          }

          int texel = texture.getPixel(u, v);

          if(texel == 0) {
            continue;
          }

          if(!raw) {
            texel = applyBlending(colour, texel);
          }

          if(translucency != null && (texel & 0xff00_0000) != 0) {
            texel = this.handleTranslucence(x, y, texel, translucency);
          }

          this.getDrawBuffer().setPixel(x, y, (this.status.setMaskBit ? 1 : 0) << 24 | texel);
        }
      }
    } else {
      for(int y = y1, v = v1; y < y2; y++, v++) {
        for(int x = x1, u = u1; x < x2; x++, u++) {
          // Check background mask
          if(this.status.drawPixels == DRAW_PIXELS.NOT_TO_MASKED_AREAS) {
            if((this.getPixel(x, y) & 0xff00_0000L) != 0) {
              continue;
            }
          }

          boolean handleTranslucence = false;
          int texel = 0;
          if(textured) {
            if(texture == null) {
              texel = this.getTexel(u, v, clutX, clutY, vramX, vramY, bpp);
            } else {
              texel = 0;
              if(palettes == null) {
                texel = texture.getPixel(u, v);
              } else {
                boolean found = false;
                for(final VramTexture palette : palettes) {
                  if(palette.rect.y() - clutY == 0) {
                    texel = texture.getTexel(palette, vramX, u, v);
                    found = true;
                    break;
                  }
                }

                if(!found) {
                  throw new RuntimeException("Failed to find palette");
                }
              }
            }

            if(texel == 0) {
              continue;
            }

            if(!raw) {
              texel = applyBlending(colour, texel);
            }

            if(translucency != null && (texel & 0xff00_0000) != 0) {
              handleTranslucence = true;
            }
          } else {
            texel = colour;

            if(translucency != null) {
              handleTranslucence = true;
            }
          }

          if(handleTranslucence) {
            for(int xx = 0; xx < this.scale; xx++) {
              for(int yy = 0; yy < this.scale; yy++) {
                this.getDrawBuffer().setPixel(x * this.scale + xx, y * this.scale + yy, (this.status.setMaskBit ? 1 : 0) << 24 | this.handleTranslucence(x * this.scale + xx, y * this.scale + yy, texel, translucency));
              }
            }
          } else {
            for(int xx = 0; xx < this.scale; xx++) {
              for(int yy = 0; yy < this.scale; yy++) {
                this.getDrawBuffer().setPixel(x * this.scale + xx, y * this.scale + yy, (this.status.setMaskBit ? 1 : 0) << 24 | texel);
              }
            }
          }
        }
      }
    }
  }

  public static int applyBlending(final int colour, final int texel) {
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
    return this.vram15[index];
  }

  private void setVramPixel(final int x, final int y, final int pixel24, final int pixel15) {
    final int index = y * this.vramWidth + x;
    this.vram24[index] = pixel24;
    this.vram15[index] = pixel15;
  }

  public static int interpolateCoords(final long w0, final long w1, final long w2, final int t0, final int t1, final int t2, final long area) {
    //https://codeplea.com/triangular-interpolation
    return (int)((t0 * w0 + t1 * w1 + t2 * w2) / area);
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

  public static boolean isTopLeft(final int ax, final int ay, final int bx, final int by) {
    return ay == by && bx > ax || by < ay;
  }

  public int getTexel(final int x, final int y, final int clutX, final int clutY, final int textureBaseX, final int textureBaseY, final Bpp depth) {
    final int index = this.getPixel15(x / depth.widthDivisor + textureBaseX, y + textureBaseY);
    final int p = index >> ((x & depth.widthMask) << depth.indexShift) & depth.indexMask;
    return this.getPixel(clutX + p, clutY);
  }

  /**
   * Returns positive value for clockwise winding, negative value for counter-clockwise. 0 if vertices are collinear. Value is roughly twice the area of the triangle.
   */
  public static int orient2d(final int ax, final int ay, final int bx, final int by, final int cx, final int cy) {
    return (bx - ax) * (cy - ay) - (by - ay) * (cx - ax);
  }

  public int handleTranslucence(final int x, final int y, final int texel, final Translucency mode) {
    final int pixel = this.getDrawBuffer().getPixel(x, y);

    final int br = pixel        & 0xff;
    final int bg = pixel >>>  8 & 0xff;
    final int bb = pixel >>> 16 & 0xff;
    final int fr = texel        & 0xff;
    final int fg = texel >>>  8 & 0xff;
    final int fb = texel >>> 16 & 0xff;
    final int tp = texel >>> 24 & 0x1;
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

      case FULL_BACKGROUND -> {
        r = br;
        g = bg;
        b = bb;
      }

      case TQUATER_B_FOREGROUND -> {
        r = Math.min(0xff, br * 3 / 4 + fr);
        g = Math.min(0xff, bg * 3 / 4 + fg);
        b = Math.min(0xff, bb * 3 / 4 + fb);
      }

      case HALF_B_FOREGROUND -> {
        r = Math.min(0xff, br / 2 + fr);
        g = Math.min(0xff, bg / 2 + fg);
        b = Math.min(0xff, bb / 2 + fb);
      }

      case QUARTER_B_FOREGROUND -> {
        r = Math.min(0xff, br / 4 + fr);
        g = Math.min(0xff, bg / 4 + fg);
        b = Math.min(0xff, bb / 4 + fb);
      }

      case FULL_FOREGROUND -> {
        r = fr;
        g = fg;
        b = fb;
      }

      case QUARTER_B_QUARTER_F -> {
        r = (br + fr) / 4;
        g = (bg + fg) / 4;
        b = (bb + fb) / 4;
      }

      case TQUARTER_B_TQUARTER_F -> {
        r = Math.min(0xff, br * 3 / 4 + fr * 3 / 4);
        g = Math.min(0xff, bg * 3 / 4 + fg * 3 / 4);
        b = Math.min(0xff, bb * 3 / 4 + fb * 3 / 4);
      }

      default -> throw new RuntimeException();
    }

    return tp << 24 | b << 16 | g << 8 | r;
  }

  public int getShadedColor(final int w0, final int w1, final int w2, final int c0, final int c1, final int c2, final int area) {
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

    public int horizontalResolution = 320;
    public int verticalResolution = 240;
  }

  public enum DRAW_PIXELS {
    ALWAYS,
    NOT_TO_MASKED_AREAS,
  }
}
