package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.core.gpu.ModelLoader;
import legend.core.gpu.Renderable;
import legend.core.gpu.VramTexture;
import legend.core.gpu.VramTextureLoader;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment_8002.charWidth;

public final class TextRenderer {
  private TextRenderer() { }

  private static final int TEXT_HEIGHT = 12;

  private static final VramTexture texture = VramTextureLoader.textureFromPng(Path.of("gfx", "fonts", "ingame.png"));

  public static TextRenderable prepareText(final String text, final int x, final int y, final TextColour colour) {
    return prepareText(text, x, y, colour, 0);
  }

  public static TextRenderable prepareText(final String text, final int x, final int y, final TextColour colour, final int trim) {
    final int[] size = new int[2];
    final List<Renderable> renderables = prepareRenderables(text, x, y, colour, trim, size);
    return new TextRenderable(renderables, size[0], size[1]);
  }

  public static TextRenderable prepareShadowText(final String text, final int x, final int y, final TextColour colour) {
    final TextColour shadowColour;
    if(colour == TextColour.LIME) {
      shadowColour = TextColour.GREEN;
    } else if(colour == TextColour.MIDDLE_BROWN) {
      shadowColour = TextColour.LIGHT_BROWN;
    } else {
      shadowColour = TextColour.MIDDLE_BROWN;
    }

    final List<Renderable> renderables = new ArrayList<>();
    final int[] size = new int[2];
    renderables.addAll(prepareRenderables(text, x, y, colour, 0, size));
    renderables.addAll(prepareRenderables(text, x, y + 1, shadowColour, 0, null));
    renderables.addAll(prepareRenderables(text, x + 1, y, shadowColour, 0, null));
    renderables.addAll(prepareRenderables(text, x + 1, y + 1, shadowColour, 0, null));
    return new TextRenderable(renderables, size[0], size[1]);
  }

  private static List<Renderable> prepareRenderables(final String text, final int x, int y, final TextColour colour, int trim, @Nullable final int[] size) {
    final List<Renderable> renderables = new ArrayList<>();

    trim = MathHelper.clamp(trim, -12, 12);

    int glyphIndex = 0;
    int glyphX = 0;
    int height = TEXT_HEIGHT;

    for(int i = 0; i < text.length(); i++) {
      final char c = text.charAt(i);

      if(c == '\n') {
        glyphIndex = 0;
        glyphX = 0;
        y += TEXT_HEIGHT;
        height += TEXT_HEIGHT;
      } else {
        if(glyphIndex == 0) {
          glyphX = 0;
        }

        if(c != ' ') {
          if(c == 'm') {
            glyphX -= 1;
          } else if(c == '.') {
            glyphX -= 2;
          } else if(c == '?' || c == '!') {
            glyphX -= 3;
          }

          final int textU = (c - 33) & 0xf;
          final int textV = (c - 33) / 16;
          final int v1 = textV * 16;
          final int v = trim >= 0 ? v1 : v1 - trim;
          final int h = trim >= 0 ? TEXT_HEIGHT - trim : TEXT_HEIGHT + trim;

          renderables.add(ModelLoader.quad(
            '"' + text + "\" index " + i,
            x - centreScreenX_1f8003dc + glyphX, y - centreScreenY_1f8003de, 0,
            8, h,
            textU * 16, v, 8, h,
            0, 0, 0,
            colour.r, colour.g, colour.b,
            null
          )
            .texture(texture)
            .build());
        }

        glyphX += charWidth(c);
        glyphIndex++;

        if(size != null) {
          if(glyphX > size[0]) {
            size[0] = glyphX;
          }
        }
      }
    }

    if(size != null) {
      size[1] = height;
    }

    Collections.reverse(renderables);
    return renderables;
  }
}
