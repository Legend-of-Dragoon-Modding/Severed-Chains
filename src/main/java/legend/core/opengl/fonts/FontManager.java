package legend.core.opengl.fonts;

import java.util.HashMap;
import java.util.Map;

public final class FontManager {
  private FontManager() { }

  private static final Map<String, Font> fonts = new HashMap<>();

  public static Font get(final String name) {
    return fonts.get(name);
  }

  public static void add(final String name, final Font font) {
    fonts.put(name, font);
  }
}
