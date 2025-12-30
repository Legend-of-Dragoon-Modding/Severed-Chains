package legend.core.font;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMaps;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FontManager {
  private static final Logger LOGGER = LogManager.getFormatterLogger(FontManager.class);

  private final Font INVALID = new Font(null, "INVALID", Char2ObjectMaps.emptyMap());

  private final Map<Path, Font> fonts = new HashMap<>();

  public Font get(Path path) {
    path = path.normalize();

    if(this.fonts.containsKey(path)) {
      return this.fonts.get(path);
    }

    LOGGER.info("Loading font %s", path);

    try {
      final JSONObject file = new JSONObject(Files.readString(path));

      final String name = file.getString("name");
      final JSONObject jsonGlyphs = file.getJSONObject("glyphs");

      final Char2ObjectMap<Glyph> glyphs = new Char2ObjectOpenHashMap<>();
      final Font font = new Font(path, name, glyphs);

      for(final String key : jsonGlyphs.keySet()) {
        final char chr;
        if(key.length() == 1) {
          chr = key.charAt(0);
        } else {
          try {
            chr = (char)Integer.parseUnsignedInt(key.substring(2), 16);
          } catch(final Throwable e) {
            LOGGER.error("Failed to parse glyph code %s in font %s", key, path);
            continue;
          }
        }

        final JSONObject glyph = jsonGlyphs.getJSONObject(key);
        final int texU = glyph.getInt("texU");
        final int texV = glyph.getInt("texV");
        final int texW = glyph.getInt("texW");
        final int texH = glyph.getInt("texH");
        final int x = glyph.getInt("x");
        final int y = glyph.getInt("y");
        final int w = glyph.getInt("w");
        final int h = glyph.getInt("h");
        final boolean colour = glyph.getBoolean("colour");

        glyphs.put(chr, new Glyph(chr, texU, texV, texW, texH, x, y, w, h, colour));
      }

      this.fonts.put(path, font);
      return font;
    } catch(final Throwable e) {
      LOGGER.error("Failed to load font %s", path);
      LOGGER.error("", e);
    }

    return this.INVALID;
  }
}
