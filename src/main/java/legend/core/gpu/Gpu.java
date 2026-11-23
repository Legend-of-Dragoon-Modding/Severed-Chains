package legend.core.gpu;

import legend.core.MathHelper;
import legend.core.renderer.Mesh;
import legend.core.renderer.ProjectionMode;
import legend.core.renderer.RenderEngine;
import legend.core.renderer.Shader;
import legend.core.renderer.ShaderManager;
import legend.core.renderer.ShaderUniformBuffer;
import legend.core.renderer.SimpleShaderOptions;
import legend.core.renderer.Texture;
import legend.core.renderer.TextureDataFormat;
import legend.core.renderer.TextureDataType;
import legend.core.renderer.TextureInternalFormat;
import legend.core.renderer.VertexOrder;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.RENDERER;
import static legend.core.MathHelper.colour15To24;

public class Gpu {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Gpu.class);

  private static final int STANDARD_VRAM_WIDTH = 1024;
  private static final int STANDARD_VRAM_HEIGHT = 512;

  public final int vramWidth = STANDARD_VRAM_WIDTH;
  public final int vramHeight = STANDARD_VRAM_HEIGHT;

  private final Object vramLock = new Object();
  private final int[] vram24 = new int[this.vramWidth * this.vramHeight];
  private final int[] vram15 = new int[this.vramWidth * this.vramHeight];

  public Texture vramTexture15;
  private Texture vramTexture24;
  private boolean vramDirty;

  private Shader<SimpleShaderOptions> vramShader;
  private SimpleShaderOptions vramShaderOptions;
  private ShaderUniformBuffer transforms2Uniform;
  private final FloatBuffer transforms2Buffer = BufferUtils.createFloatBuffer(4 * 4 + 4);
  private final Matrix4f transforms = new Matrix4f();

  private Mesh displayMesh;

  private short offsetX;
  private short offsetY;

  private final List<GpuCommandCopyVramToVram> vramCopyQueue = new ArrayList<>();

  public void init() {
    this.vramShader = ShaderManager.getShader(RenderEngine.SIMPLE_SHADER);
    this.vramShaderOptions = this.vramShader.makeOptions();

    this.transforms2Uniform = ShaderManager.getUniformBuffer("transforms2");

    this.initVram();

    this.vramTexture24 = Texture.create("VRAM 24", builder -> {
      builder.size(1024, 512);
      builder.internalFormat(TextureInternalFormat.RGBA_8);
      builder.dataFormat(TextureDataFormat.RGBA);
      builder.dataType(TextureDataType.UBYTE);
    });
    this.vramTexture24.persistent = true;

    this.displayMesh = RENDERER.api().makeMesh("GPU display mesh", VertexOrder.TRIANGLE_STRIP, new float[] {
      0.0f, 0.0f, 1.0f, 0, 0,
      0.0f, 1.0f, 1.0f, 0, 1,
      1.0f, 0.0f, 1.0f, 1, 0,
      1.0f, 1.0f, 1.0f, 1, 1,
    }, 4);
    this.displayMesh.attribute(0, 0L, 3, 5);
    this.displayMesh.attribute(1, 3L, 2, 5);
  }

  public void initVram() {
    this.vramTexture15 = Texture.create("VRAM 15", builder -> {
      builder.size(1024, 512);
      builder.internalFormat(TextureInternalFormat.R_32_UINT);
      builder.dataFormat(TextureDataFormat.RED_INT);
      builder.dataType(TextureDataType.UINT);
    });
    this.vramTexture15.persistent = true;
  }

  public void updateVramTexture() {
    synchronized(this.vramLock) {
      if(this.vramDirty) {
        this.vramTexture15.data(0, 0, 1024, 512, TextureDataType.UINT, this.vram15);
        this.vramDirty = false;
      }
    }
  }

  public void startFrame() {
    this.updateVramTexture();
  }

  public void endFrame() {
    if(RenderEngine.vramViewer) {
      this.drawVram();
    }

    for(int i = this.vramCopyQueue.size() - 1; i >= 0; i--) {
      final GpuCommandCopyVramToVram copy = this.vramCopyQueue.get(i);
      this.copyVramToVram(copy.sourceX, copy.sourceY, copy.destX, copy.destY, copy.width, copy.height);
    }

    this.vramCopyQueue.clear();
  }

  public void useVramTexture() {
    this.vramTexture15.use(1);
  }

  public void uploadData15(final Rect4i rect, final FileData data) {
    final int rectX = rect.x;
    final int rectY = rect.y;
    final int rectW = rect.w;
    final int rectH = rect.h;

    assert rectX + rectW <= this.vramWidth : "Rect right (" + (rectX + rectW) + ") overflows VRAM width (" + this.vramWidth + ')';
    assert rectY + rectH <= this.vramHeight : "Rect bottom (" + (rectY + rectH) + ") overflows VRAM height (" + this.vramHeight + ')';

    if(LOGGER.isDebugEnabled()) {
      LOGGER.debug("Copying (%d, %d, %d, %d) from CPU to VRAM", rectX, rectY, rectW, rectH);
    }

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

    if(LOGGER.isDebugEnabled()) {
      LOGGER.debug("Copying (%d, %d, %d, %d) from CPU to VRAM", rectX, rectY, rectW, rectH);
    }

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

  public void downloadData15(final Rect4i rect, final FileData out) {
    final int rectX = rect.x;
    final int rectY = rect.y;
    final int rectW = rect.w;
    final int rectH = rect.h;

    assert rectX + rectW <= this.vramWidth : "Rect right (" + (rectX + rectW) + ") overflows VRAM width (" + this.vramWidth + ')';
    assert rectY + rectH <= this.vramHeight : "Rect bottom (" + (rectY + rectH) + ") overflows VRAM height (" + this.vramHeight + ')';

    if(LOGGER.isDebugEnabled()) {
      LOGGER.debug("Copying (%d, %d, %d, %d) from VRAM to byte array", rectX, rectY, rectW, rectH);
    }

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

    if(LOGGER.isDebugEnabled()) {
      LOGGER.debug("Copying (%d, %d, %d, %d) from VRAM to byte array", rectX, rectY, rectW, rectH);
    }

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
    if(LOGGER.isDebugEnabled()) {
      LOGGER.debug("COPY VRAM VRAM from %d %d to %d %d size %d %d", sourceX, sourceY, destX, destY, width, height);
    }

    synchronized(this.vramLock) {
      for(int y = 0; y < height; y++) {
        for(int x = 0; x < width; x++) {
          final int colour15 = this.getPixel15(sourceX + x, sourceY + y);
          final int colour24 = this.getPixel(sourceX + x, sourceY + y);
          this.setVramPixel(destX + x, destY + y, colour24, colour15);
        }
      }

      this.vramDirty = true;
    }
  }

  public void queueVramCopy(final GpuCommandCopyVramToVram command) {
    this.vramCopyQueue.add(command);
  }

  /**
   * GP1(01h) - Reset Command Buffer
   */
  public void resetCommandBuffer() {
    this.vramCopyQueue.clear();
  }

  public void drawingOffset(final int x, final int y) {
    this.offsetX = (short)x;
    this.offsetY = (short)y;
  }

  public void drawVram() {
    RENDERER.setProjectionMode(ProjectionMode._2D);
    RENDERER.api().translucency(null);

    this.transforms.scaling(1024.0f, 512.0f, 1.0f);
    this.transforms.get(this.transforms2Buffer);
    this.transforms2Uniform.set(this.transforms2Buffer);

    this.vramShader.use();
    this.vramShaderOptions.recolour(1.0f, 1.0f, 1.0f, 1.0f);
    this.vramShaderOptions.apply();
    this.vramTexture24.use();
    this.vramTexture24.data(0, 0, 1024, 512, TextureDataType.UBYTE, this.vram24);
    this.displayMesh.draw();
  }

  public int getOffsetX() {
    return this.offsetX;
  }

  public int getOffsetY() {
    return this.offsetY;
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
}
