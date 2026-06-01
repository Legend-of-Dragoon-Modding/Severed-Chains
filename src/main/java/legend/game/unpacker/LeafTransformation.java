package legend.game.unpacker;

public class LeafTransformation {
  public final String name;
  public final Unpacker.Discriminator discriminator;
  public final Unpacker.Transformer transformer;

  public LeafTransformation(final String name, final Unpacker.Discriminator discriminator, final Unpacker.Transformer transformer) {
    this.name = name;
    this.discriminator = discriminator;
    this.transformer = transformer;
  }
}
