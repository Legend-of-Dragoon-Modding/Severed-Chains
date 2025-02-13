package legend.lodmod;

import legend.game.combat.Battle;
import legend.game.combat.deff.DeffPackage;
import legend.game.unpacker.Loader;

import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;

public class RetailDeffPackage extends DeffPackage {
  private final int drgn0Index;

  public RetailDeffPackage(final int drgn0Index) {
    this.drgn0Index = drgn0Index;
  }

  @Override
  public void load() {
    ((Battle)currentEngineState_8004dd04).loadDeff(
      Loader.resolve("SECT/DRGN0.BIN/" + this.drgn0Index),
      Loader.resolve("SECT/DRGN0.BIN/" + (this.drgn0Index + 1))
    );
  }
}
