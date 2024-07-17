package legend.game.characters;

import legend.game.scripting.Param;

public class UnaryStatModType extends StatModType<UnaryStat, UnaryStatMod, UnaryStatModConfig> {
  @Override
  public UnaryStatMod make(final UnaryStatModConfig config) {
    return new UnaryStatMod(config.amount, config.percentile, config.turns);
  }

  @Override
  public UnaryStatModConfig makeConfig() {
    return new UnaryStatModConfig();
  }

  @Override
  public void update(final UnaryStatMod mod, final UnaryStatModConfig config) {
    mod.amount = config.amount;
    mod.percentile = config.percentile;
    mod.turns = config.turns;
  }

  @Override
  public void readConfigFromScript(final UnaryStatModConfig config, final Param params) {
    config.amount = params.array(0).get();
    config.percentile = params.array(1).get() == 1;
    config.turns = params.array(2).get();
  }

  @Override
  public void writeConfigToScript(final UnaryStatModConfig config, final Param params) {
    params.array(0).set(config.amount);
    params.array(1).set(config.percentile ? 1 : 0);
    params.array(2).set(config.turns);
  }
}
