package legend.game.characters;

@FunctionalInterface
public interface StatMod {
  /** Return the amount you want added to the stat */
  int apply(final StatCollection stats, final StatType type);
}
