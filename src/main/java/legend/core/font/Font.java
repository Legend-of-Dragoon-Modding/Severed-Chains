package legend.core.font;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.Texture;

import java.nio.file.Path;

import static legend.core.GameEngine.RENDERER;

public class Font {
  public final Path path;
  public final String name;
  private final Char2ObjectMap<Glyph> glyphs;

  private Texture texture;
  private Obj obj;

  public Font(final Path path, final String name, final Char2ObjectMap<Glyph> glyphs) {
    this.path = path;
    this.name = name;
    this.glyphs = glyphs;
  }

  public void init() {
    if(this.texture == null) {
      final String fontName = this.path.getFileName().toString();
      this.texture = Texture.png(this.path.resolveSibling(fontName.substring(0, fontName.lastIndexOf('.')) + ".png"));
    }

    if(this.obj == null) {
      final QuadBuilder builder = new QuadBuilder("Font " + this.name);

      for(final Glyph glyph : this.glyphs.values()) {
        glyph.index = builder.currentQuadIndex();

        builder
          .add()
          .bpp(Bpp.BITS_24)
          .uv(glyph.texU / (float)this.texture.width, glyph.texV / (float)this.texture.height)
          .uvSize(glyph.texW / (float)this.texture.width, glyph.texH / (float)this.texture.height)
          .pos(glyph.x, glyph.y, 0.0f)
          .posSize(glyph.w, glyph.h)
        ;
      }

      this.obj = builder.build();
      this.obj.persistent = true;
    }
  }

  public QueuedModelStandard queueChar(final char chr, final MV transforms) {
    final Glyph glyph = this.glyphs.get(chr);

    if(glyph == null) {
      return RENDERER.queueOrthoModel(RENDERER.lineBox, transforms, QueuedModelStandard.class);
    }

    return RENDERER.queueOrthoModel(this.obj, transforms, QueuedModelStandard.class)
      .texture(this.texture)
      .vertices(this.glyphs.get(chr).index * 4, 4)
    ;
  }

  public boolean usesColour(final char chr) {
    if(!this.glyphs.containsKey(chr)) {
      return false;
    }

    return this.glyphs.get(chr).colour;
  }

  public int charWidth(final char chr) {
    if(!this.glyphs.containsKey(chr)) {
      return 8;
    }

    return this.glyphs.get(chr).w;
  }

  /** Measures the width of a single line (up to the first linebreak encountered) */
  public int lineWidth(final String text) {
    return this.lineWidth(text, 0);
  }

  /** Measures the width of a single line (up to the first linebreak encountered) */
  public int lineWidth(final String text, final int start) {
    int width = 0;
    for(int index = start; index < text.length(); index++) {
      final char c = text.charAt(index);

      if(c == '\n') {
        break;
      }

      final Glyph glyph = this.glyphs.get(c);

      if(glyph != null) {
        width += glyph.w;
      }
    }

    return width;
  }

  /** Measures the width of the whole string, taking linebreaks into account. The width will be the width of the widest line. */
  @Method(0x8002a59cL)
  public int textWidth(final String text) {
    int width = 0;
    int currentWidth = 0;
    for(int index = 0; index < text.length(); index++) {
      if(text.charAt(index) == '\n') {
        currentWidth = 0;
      }

      currentWidth += this.charWidth(text.charAt(index));

      if(currentWidth > width) {
        width = currentWidth;
      }
    }

    return width;
  }

  public int textHeight(final String text) {
    int lines = 1;
    int newlinePos = -1;
    while((newlinePos = text.indexOf('\n', newlinePos + 1)) != -1) {
      lines++;
    }

    return lines * 12;
  }
}
