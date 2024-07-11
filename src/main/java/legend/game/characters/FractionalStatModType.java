package legend.game.characters;

import legend.game.scripting.Param;

public class FractionalStatModType extends StatModType<FractionalStat, FractionalStatMod, FractionalStatModConfig> {
  @Override
  public FractionalStatMod make(final FractionalStatModConfig config) {
    return new FractionalStatMod(config.amount, config.percentile, config.turns);
  }

  @Override
  public FractionalStatModConfig makeConfig() {
    return new FractionalStatModConfig();
  }

  @Override
  public void update(final FractionalStatMod mod, final FractionalStatModConfig config) {
    mod.amount = config.amount;
    mod.percentile = config.percentile;
    mod.turns = config.turns;
  }

  @Override
  public void readConfigFromScript(final FractionalStatModConfig config, final Param params) {
    config.amount = params.array(0).get();
    config.percentile = params.array(1).get() == 1;
    config.turns = params.array(2).get();
  }

  @Override
  public void writeConfigToScript(final FractionalStatModConfig config, final Param params) {
    params.array(0).set(config.amount);
    params.array(1).set(config.percentile ? 1 : 0);
    params.array(2).set(config.turns);
  }
}
