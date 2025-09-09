package legend.game.unpacker;

public class Replacement {
  public final String name;
  public final Unpacker.Transformer transformer;

  public Replacement(final String name, final Unpacker.Transformer transformer) {
    this.name = name;
    this.transformer = transformer;
  }
}
