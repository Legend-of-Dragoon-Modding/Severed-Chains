package legend.game.textures;

import legend.core.gpu.Bpp;
import legend.core.gpu.Rect4i;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.Texture;
import org.legendofdragoon.modloader.registries.RegistryId;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBRPContext;
import org.lwjgl.stb.STBRPNode;
import org.lwjgl.stb.STBRPRect;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11C.GL_NEAREST;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL12C.GL_UNSIGNED_INT_8_8_8_8_REV;
import static org.lwjgl.stb.STBRectPack.stbrp_init_target;
import static org.lwjgl.stb.STBRectPack.stbrp_pack_rects;

public class TexturePacker {
  private final Map<RegistryId, Rect4i> entryToRect = new HashMap<>();
  private final Map<RegistryId, Image> entryToImage = new HashMap<>();

  public void add(final RegistryId id, final Image image) {
    final Rect4i rect = new Rect4i();
    rect.w = image.width;
    rect.h = image.height;
    this.entryToRect.put(id, rect);
    this.entryToImage.put(id, image);
  }

  public Rect4i getRect(final RegistryId id) {
    return this.entryToRect.get(id);
  }

  public byte[] packToBytes(final int width, final int height) {
    final STBRPContext ctx = STBRPContext.create();
    final STBRPNode.Buffer nodes = STBRPNode.create(width); // documentation says that nodes should be >= width
    final STBRPRect.Buffer rectBuffer = STBRPRect.create(this.entryToRect.size());

    int i = 0;
    for(final Rect4i icon : this.entryToRect.values()) {
      final STBRPRect rect = rectBuffer.get(i++);
      rect.x(icon.x);
      rect.y(icon.y);
      rect.w(icon.w);
      rect.h(icon.h);
    }

    stbrp_init_target(ctx, width, height, nodes);

    if(stbrp_pack_rects(ctx, rectBuffer) == 0) {
      throw new RuntimeException("Failed to pack texture atlas");
    }

    i = 0;
    for(final Rect4i icon : this.entryToRect.values()) {
      final STBRPRect rect = rectBuffer.get(i++);
      icon.x = rect.x();
      icon.y = rect.y();
      icon.w = rect.w();
      icon.h = rect.h();
    }

    rectBuffer.free();
    nodes.free();
    ctx.free();

    return this.buildTexture(width, height);
  }

  public TextureAtlas pack(final int width, final int height) {
    final byte[] packedData = this.packToBytes(width, height);
    final ByteBuffer buffer = BufferUtils.createByteBuffer(packedData.length);
    buffer.put(0, packedData);

    final Texture texture = Texture.create(builder -> {
      builder.internalFormat(GL_RGBA);
      builder.dataFormat(GL_RGBA);
      builder.dataType(GL_UNSIGNED_INT_8_8_8_8_REV);
      builder.minFilter(GL_NEAREST);
      builder.magFilter(GL_NEAREST);
      builder.data(buffer, width, height);
    });

    final Map<RegistryId, TextureAtlasIcon> icons = new HashMap<>();
    final QuadBuilder builder = new QuadBuilder("Atlas");

    for(final var entry : this.entryToRect.entrySet()) {
      final Rect4i rect = entry.getValue();

      builder.add();
      builder.bpp(Bpp.BITS_24);
      builder.posSize(1.0f, 1.0f);
      builder.uv(rect.x / (float)width, rect.y / (float)height);
      builder.uvSize(rect.w / (float)width, rect.h / (float)height);
    }

    final Obj obj = builder.build();
    final TextureAtlas atlas = new TextureAtlas(texture, obj, icons);

    int i = 0;
    for(final var entry : this.entryToRect.entrySet()) {
      final RegistryId id = entry.getKey();
      final Rect4i rect = entry.getValue();

      icons.put(id, new TextureAtlasIcon(atlas, rect, i * 4));
      i++;
    }

    return atlas;
  }

  private byte[] buildTexture(final int width, final int height) {
    final byte[] out = new byte[width * height * 4];

    for(final RegistryId entry : this.entryToRect.keySet()) {
      this.insertImage(out, width, entry);
    }

    return out;
  }

  private void insertImage(final byte[] data, final int stride, final RegistryId entry) {
    final Rect4i icon = this.entryToRect.get(entry);
    final Image image = this.entryToImage.get(entry);

    for(int y = 0; y < icon.h; y++) {
      System.arraycopy(image.data, y * icon.w * 4, data, ((icon.y + y) * stride + icon.x) * 4, icon.w * 4);
    }
  }
}
