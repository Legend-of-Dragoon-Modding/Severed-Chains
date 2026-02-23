package legend.game.characters;

public class VitalsStat extends FractionalStat {
  public VitalsStat(final StatType<VitalsStat> type, final StatCollection stats) {
    super(type, stats, value -> Math.max(0, value));
  }

  @Override
  public VitalsStat copy() {
    final VitalsStat stat = new VitalsStat((StatType<VitalsStat>)this.type, this.stats);
    stat.set(this);
    return stat;
  }
}
