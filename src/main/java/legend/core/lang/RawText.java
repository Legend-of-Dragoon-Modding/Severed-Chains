package legend.core.lang;

public class RawText implements TextComponent {
  public static final RawText BLANK = new RawText("");

  private final String text;

  public RawText(final String text) {
    this.text = text;
  }

  @Override
  public String get() {
    return this.text;
  }

  @Override
  public String toString() {
    return this.get();
  }
}
