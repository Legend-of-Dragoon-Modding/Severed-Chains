package legend.core.gpu;

import legend.core.MathHelper;
import legend.core.ProjectionMode;
import legend.core.RenderEngine;
import legend.core.opengl.Mesh;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderManager;
import legend.core.opengl.SimpleShaderOptions;
import legend.core.opengl.Texture;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import javax.annotation.Nullable;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.RENDERER;
import static legend.core.MathHelper.colour15To24;
import static legend.core.MathHelper.colour24To15;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static org.lwjgl.opengl.GL11C.GL_BLEND;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL12C.GL_UNSIGNED_INT_8_8_8_8_REV;
import static org.lwjgl.opengl.GL30C.GL_R32UI;
import static org.lwjgl.opengl.GL30C.GL_RED_INTEGER;

public class Gpu {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Gpu.class);

  private static final int STANDARD_VRAM_WIDTH = 1024;
  private static final int STANDARD_VRAM_HEIGHT = 512;

  public final int vramWidth = STANDARD_VRAM_WIDTH;
  public final int vramHeight = STANDARD_VRAM_HEIGHT;

  private final VramTextureSingle[] renderBuffers = new VramTextureSingle[2];
  private int drawBufferIndex;

  private final Object vramLock = new Object();
  private final int[] vram24 = new int[this.vramWidth * this.vramHeight];
  private final int[] vram15 = new int[this.vramWidth * this.vramHeight];

  private Texture vramTexture15;
  private Texture vramTexture24;
  private boolean vramDirty;

  private Shader<SimpleShaderOptions> vramShader;
  private SimpleShaderOptions vramShaderOptions;
  private Shader.UniformBuffer transforms2Uniform;
  private final FloatBuffer transforms2Buffer = BufferUtils.createFloatBuffer(4 * 4 + 4);
  private final Matrix4f identity = new Matrix4f();

  private Texture displayTexture;
  private Mesh displayMesh;

  private int windowWidth;
  private int windowHeight;

  public final Status status = new Status();

  public final Rect4i drawingArea = new Rect4i();
  public final Rect4i scaledDrawingArea = new Rect4i();
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

  public void init() {
    RENDERER.events().onResize((window1, width, height) -> this.updateDisplayTexture(width, height));

    this.vramShader = ShaderManager.getShader(RenderEngine.SIMPLE_SHADER);
    this.vramShaderOptions = this.vramShader.makeOptions();

    this.transforms2Uniform = ShaderManager.getUniformBuffer("transforms2");

    this.vramTexture15 = Texture.create(builder -> {
      builder.size(1024, 512);
      builder.internalFormat(GL_R32UI);
      builder.dataFormat(GL_RED_INTEGER);
      builder.dataType(GL_UNSIGNED_INT);
    });

    this.vramTexture24 = Texture.create(builder -> {
      builder.size(1024, 512);
      builder.internalFormat(GL_RGBA);
      builder.dataFormat(GL_RGBA);
      builder.dataType(GL_UNSIGNED_INT_8_8_8_8_REV);
    });

    this.displaySize(320, 240);
  }

  public void startFrame() {
    synchronized(this.vramLock) {
      if(this.vramDirty) {
        this.vramTexture15.dataInt(0, 0, 1024, 512, this.vram15);
        this.vramTexture24.data(0, 0, 1024, 512, this.vram24);
        this.vramDirty = false;
      }
    }

    if(this.zMax != orderingTableSize_1f8003c8) {
      this.updateOrderingTableSize(orderingTableSize_1f8003c8);
    }
  }

  public void endFrame() {
    this.tick();
  }

  public void useVramTexture() {
    this.vramTexture24.use(0);
    this.vramTexture15.use(1);
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

    if(RenderEngine.legacyMode == 1) {
      this.displayTexture.data(0, 0, this.displayTexture.width, this.displayTexture.height, this.getDisplayBuffer().getData());
      this.drawDisplay();
    } else if(RenderEngine.legacyMode == 2) {
      this.drawVram();
    }

    if(this.zQueues != null) {
      for(int z = this.zQueues.length - 1; z >= 0; z--) {
        final List<GpuCommand> queue = this.zQueues[z];

        for(int i = queue.size() - 1; i >= 0; i--) {
          queue.get(i).render(this);
        }

        queue.clear();
      }
    }

    this.drawBufferIndex ^= 1;
  }

  public void clear(final int colour) {
    LOGGER.trace("Clear display RGB %06x", colour);

    this.getDrawBuffer().fill(colour);
  }

  public void clearData(final int x, final int y, final int w, final int h) {
    assert x + w <= this.vramWidth : "Rect right (" + (x + w) + ") overflows VRAM width (" + this.vramWidth + ')';
    assert y + h <= this.vramHeight : "Rect bottom (" + (y + h) + ") overflows VRAM height (" + this.vramHeight + ')';

    synchronized(this.vramLock) {
      int offset;
      for(int i = y; i < y + h; i++) {
        offset = i * this.vramWidth + x;
        Arrays.fill(this.vram15, offset, offset + w, 0);
        Arrays.fill(this.vram24, offset, offset + w, 0);
      }

      this.vramDirty = true;
    }
  }

  public void uploadData15(final Rect4i rect, final FileData data) {
    final int rectX = rect.x;
    final int rectY = rect.y;
    final int rectW = rect.w;
    final int rectH = rect.h;

    assert rectX + rectW <= this.vramWidth : "Rect right (" + (rectX + rectW) + ") overflows VRAM width (" + this.vramWidth + ')';
    assert rectY + rectH <= this.vramHeight : "Rect bottom (" + (rectY + rectH) + ") overflows VRAM height (" + this.vramHeight + ')';

    LOGGER.debug("Copying (%d, %d, %d, %d) from CPU to VRAM", rectX, rectY, rectW, rectH);

    synchronized(this.vramLock) {
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

      this.vramDirty = true;
    }
  }

  public void uploadData15(final Rect4i rect, final int[] data) {
    final int rectX = rect.x;
    final int rectY = rect.y;
    final int rectW = rect.w;
    final int rectH = rect.h;

    assert rectX + rectW <= this.vramWidth : "Rect right (" + (rectX + rectW) + ") overflows VRAM width (" + this.vramWidth + ')';
    assert rectY + rectH <= this.vramHeight : "Rect bottom (" + (rectY + rectH) + ") overflows VRAM height (" + this.vramHeight + ')';

    LOGGER.debug("Copying (%d, %d, %d, %d) from CPU to VRAM", rectX, rectY, rectW, rectH);

    synchronized(this.vramLock) {
      int i = 0;
      for(int y = rectY; y < rectY + rectH; y++) {
        for(int x = rectX; x < rectX + rectW; x++) {
          this.setVramPixel(x, y, colour15To24(data[i]), data[i]);
          i++;
        }
      }

      this.vramDirty = true;
    }
  }

  public void uploadData24(final Rect4i rect, final int[] data) {
    final int rectX = rect.x;
    final int rectY = rect.y;
    final int rectW = rect.w;
    final int rectH = rect.h;

    assert rectX + rectW <= this.vramWidth : "Rect right (" + (rectX + rectW) + ") overflows VRAM width (" + this.vramWidth + ')';
    assert rectY + rectH <= this.vramHeight : "Rect bottom (" + (rectY + rectH) + ") overflows VRAM height (" + this.vramHeight + ')';

    LOGGER.debug("Copying (%d, %d, %d, %d) from CPU to VRAM", rectX, rectY, rectW, rectH);

    synchronized(this.vramLock) {
      int i = 0;
      for(int y = rectY; y < rectY + rectH; y++) {
        for(int x = rectX; x < rectX + rectW; x++) {
          this.setVramPixel(x, y, data[i], colour24To15(data[i]));
          i++;
        }
      }

      this.vramDirty = true;
    }
  }

  public void downloadData15(final Rect4i rect, final FileData out) {
    final int rectX = rect.x;
    final int rectY = rect.y;
    final int rectW = rect.w;
    final int rectH = rect.h;

    assert rectX + rectW <= this.vramWidth : "Rect right (" + (rectX + rectW) + ") overflows VRAM width (" + this.vramWidth + ')';
    assert rectY + rectH <= this.vramHeight : "Rect bottom (" + (rectY + rectH) + ") overflows VRAM height (" + this.vramHeight + ')';

    LOGGER.debug("Copying (%d, %d, %d, %d) from VRAM to byte array", rectX, rectY, rectW, rectH);

    synchronized(this.vramLock) {
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
  }

  public void downloadData15(final Rect4i rect, final int[] out) {
    final int rectX = rect.x;
    final int rectY = rect.y;
    final int rectW = rect.w;
    final int rectH = rect.h;

    assert rectX + rectW <= this.vramWidth : "Rect right (" + (rectX + rectW) + ") overflows VRAM width (" + this.vramWidth + ')';
    assert rectY + rectH <= this.vramHeight : "Rect bottom (" + (rectY + rectH) + ") overflows VRAM height (" + this.vramHeight + ')';

    LOGGER.debug("Copying (%d, %d, %d, %d) from VRAM to byte array", rectX, rectY, rectW, rectH);

    synchronized(this.vramLock) {
      int i = 0;
      for(int y = rectY; y < rectY + rectH; y++) {
        for(int x = rectX; x < rectX + rectW; x++) {
          out[i++] = this.getPixel15(x, y);
        }
      }
    }
  }

  public void copyVramToVram(final int sourceX, final int sourceY, final int destX, final int destY, final int width, final int height) {
    LOGGER.debug("COPY VRAM VRAM from %d %d to %d %d size %d %d", sourceX, sourceY, destX, destY, width, height);

    synchronized(this.vramLock) {
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

      this.vramDirty = true;
    }
  }

  public void queueCommand(final float z, final GpuCommand command) {
    this.queueCommand(Math.round(z), command);
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

    RENDERER.setProjectionSize(width, height);

    this.status.horizontalResolution = width;
    this.status.verticalResolution = height;

    // Always run on the GPU thread
    if(!PLATFORM.isContextCurrent()) {
      this.displayChanged = true;
      return;
    }

    this.displaySize(width, height);
  }

  public void displaySize(final int horizontalRes, final int verticalRes) {
    if(this.displayTexture != null) {
      this.displayTexture.delete();
    }

    for(int i = 0; i < this.renderBuffers.length; i++) {
      this.renderBuffers[i] = new VramTextureSingle(Bpp.BITS_24, new Rect4i(0, 0, horizontalRes, verticalRes), new int[horizontalRes * verticalRes]);
    }

    this.displayTexture = Texture.empty(horizontalRes, verticalRes);

    this.updateDisplayTexture(this.windowWidth, this.windowHeight);
  }

  public void drawingArea(final int left, final int top, int width, final int height) {
    if(width == 384) {
      width = 368;
    }

    this.drawingArea.set((short)left, (short)top, (short)width, (short)height);
    this.scaledDrawingArea.set((short)left, (short)top, (short)width, (short)height);
  }

  public void drawingOffset(final int x, final int y) {
    this.offsetX = (short)x;
    this.offsetY = (short)y;
  }

  public void updateOrderingTableSize(final int size) {
    final List<GpuCommand>[] list = new ArrayList[size];
    Arrays.setAll(list, key -> new ArrayList<>());

    this.zMax = size;
    this.zQueues = list;
  }

  public void drawDisplay() {
    RENDERER.setProjectionMode(ProjectionMode._2D);

    glDisable(GL_BLEND);

    this.identity.get(this.transforms2Buffer);
    this.transforms2Uniform.set(this.transforms2Buffer);

    this.vramShader.use();
    this.vramShaderOptions.recolour(1.0f, 1.0f, 1.0f, 1.0f);
    this.vramShaderOptions.apply();
    this.displayTexture.use();
    this.displayMesh.draw();
  }

  public void drawVram() {
    RENDERER.setProjectionMode(ProjectionMode._2D);

    glDisable(GL_BLEND);

    this.identity.get(this.transforms2Buffer);
    this.transforms2Uniform.set(this.transforms2Buffer);

    this.vramShader.use();
    this.vramShaderOptions.recolour(1.0f, 1.0f, 1.0f, 1.0f);
    this.vramShaderOptions.apply();
    this.vramTexture24.use();
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

      if(this.drawingArea.contains(x, y)) {
        if(translucency != null) {
          colour = this.handleTranslucence(x, y, colour, translucency);
        }

        colour |= (this.status.setMaskBit ? 1 : 0) << 24;

        this.getDrawBuffer().setPixel(x, y, colour);
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

  void rasterizeTriangle(final int vx0, final int vy0, int vx1, int vy1, int vx2, int vy2, final int tu0, final int tv0, int tu1, int tv1, int tu2, int tv2, final int c0, int c1, int c2, final int clutX, final int clutY, final int textureBaseX, final int textureBaseY, final Bpp bpp, final boolean isTextured, final boolean isShaded, final boolean isTranslucent, final boolean isRaw, final Translucency translucencyMode, @Nullable final VramTexture texture, @Nullable final VramTexture[] palettes) {
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
    minX = (short)Math.max(minX, this.scaledDrawingArea.x);
    minY = (short)Math.max(minY, this.scaledDrawingArea.y);
    maxX = (short)Math.min(maxX, this.scaledDrawingArea.x + this.scaledDrawingArea.w);
    maxY = (short)Math.min(maxY, this.scaledDrawingArea.y + this.scaledDrawingArea.h);

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
                  if(texelX < this.drawingArea.x + this.drawingArea.w && texelY < this.drawingArea.y + this.drawingArea.h) {
                    texel = texture.getPixel(texelX, texelY) & 0xffffff;
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

  void rasterizeQuad(final int x1, final int y1, final int x2, final int y2, final int colour, final boolean raw, final boolean textured, final int u1, final int v1, final int clutX, final int clutY, final int vramX, final int vramY, final Bpp bpp, @Nullable final Translucency translucency, @Nullable final VramTexture texture, @Nullable final VramTexture[] palettes) {
    for(int y = y1, v = v1; y < y2; y++, v++) {
      for(int x = x1, u = u1; x < x2; x++, u++) {
        // Check background mask
        if(this.status.drawPixels == DRAW_PIXELS.NOT_TO_MASKED_AREAS) {
          if((this.getPixel(x, y) & 0xff00_0000L) != 0) {
            continue;
          }
        }

        boolean handleTranslucence = false;
        int texel;
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
          this.getDrawBuffer().setPixel(x, y, (this.status.setMaskBit ? 1 : 0) << 24 | this.handleTranslucence(x, y, texel, translucency));
        } else {
          this.getDrawBuffer().setPixel(x, y, (this.status.setMaskBit ? 1 : 0) << 24 | texel);
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

    return (r & 0xff) << 16 | (g & 0xff) << 8 | b & 0xff;
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
