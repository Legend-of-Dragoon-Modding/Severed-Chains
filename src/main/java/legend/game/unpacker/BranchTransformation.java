package legend.game.unpacker;

public class BranchTransformation {
  public final String name;
  public final Unpacker.Transformer transformer;

  public BranchTransformation(final String name, final Unpacker.Transformer transformer) {
    this.name = name;
    this.transformer = transformer;
  }
}
