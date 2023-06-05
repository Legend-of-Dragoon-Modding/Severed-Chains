package legend.game.characters;

public class VitalsStat extends FractionalStat {
  public VitalsStat(final StatType<VitalsStat> type, final StatCollection stats) {
    super(type, stats, value -> Math.max(0, value));
  }
}
