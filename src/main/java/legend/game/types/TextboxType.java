package legend.game.types;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TextboxType {
  /** Normal interactive: waits for player input */
  NORMAL_INTERACTIVE(0),

  /** Simple textbox: no name, no scroll-in, no delay */
  SIMPLE(1),

  /** Textbox scrolls upward into view */
  SCROLL_IN_UP(2),

  /** Delayed start, then auto-advance / cutscene flow */
  DELAYED_AUTO_START(3),

  /** SMAP or named textbox: fast writes name / first line */
  SMAP_NAMED(4);

  public final int id;

  TextboxType(int id) {
    this.id = id;
  }

  private static final Map<Integer, TextboxType> BY_ID =
    Stream.of(values()).collect(Collectors.toMap(t -> t.id, t -> t));

  public static TextboxType from(int value) {
    return BY_ID.getOrDefault(value, SIMPLE);
  }
}
