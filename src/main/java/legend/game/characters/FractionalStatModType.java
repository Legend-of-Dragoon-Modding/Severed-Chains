package legend.game.characters;

import legend.core.memory.types.IntRef;
import legend.core.tags.BoolTag;
import legend.core.tags.IntTag;
import legend.core.tags.MapTag;
import legend.game.scripting.Param;
import legend.game.unpacker.FileData;

public class FractionalStatModType extends StatModType<FractionalStat, FractionalStatMod, FractionalStatModConfig> {
  @Override
  public FractionalStatMod make(final FractionalStatModConfig config) {
    return new FractionalStatMod(config.amount, config.percentile, config.turns, config.contributesToOtherMods);
  }

  @Override
  public FractionalStatModConfig makeConfig() {
    return new FractionalStatModConfig();
  }

  @Override
  public FractionalStatMod deserialize(final FileData data, final IntRef offset) {
    final int amount = data.readInt(offset);
    final boolean percentile = data.readBool(offset);
    final int turns = data.readInt(offset);
    return new FractionalStatMod(amount, percentile, turns, false);
  }

  @Override
  public void serialize(final FractionalStatMod mod, final MapTag tag) {
    tag.set("amount", new IntTag(mod.amount));
    tag.set("percentile", new BoolTag(mod.percentile));
    tag.set("turns", new IntTag(mod.turns));
    tag.set("contributesToOtherMods", new BoolTag(mod.contributesToOtherMods));
  }

  @Override
  public FractionalStatMod deserialize(final MapTag tag) {
    final int amount = tag.get("amount").asInt().get();
    final boolean percentile = tag.get("percentile").asBool().get();
    final int turns = tag.get("turns").asInt().get();
    final boolean contributesToOtherMods = tag.get("contributesToOtherMods").asBool().get();
    return new FractionalStatMod(amount, percentile, turns, contributesToOtherMods);
  }

  @Override
  public void update(final FractionalStatMod mod, final FractionalStatModConfig config) {
    mod.amount = config.amount;
    mod.percentile = config.percentile;
    mod.turns = config.turns;
    mod.contributesToOtherMods = config.contributesToOtherMods;
  }

  @Override
  public void readConfigFromScript(final FractionalStatModConfig config, final Param params) {
    config.amount = params.array(0).get();
    config.percentile = params.array(1).get() == 1;
    config.turns = params.array(2).get();
    config.contributesToOtherMods = params.array(3).get() == 1;
  }

  @Override
  public void writeConfigToScript(final FractionalStatModConfig config, final Param params) {
    params.array(0).set(config.amount);
    params.array(1).set(config.percentile ? 1 : 0);
    params.array(2).set(config.turns);
    params.array(3).set(config.contributesToOtherMods ? 1 : 0);
  }
}
