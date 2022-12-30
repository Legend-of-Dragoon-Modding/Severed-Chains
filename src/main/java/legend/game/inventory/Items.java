package legend.game.inventory;

import legend.core.GameEngine;
import legend.game.BaseMod;

public final class Items {
  private Items() { }

  public static final RegistryHolder<Item> CHARM_POTION = GameEngine.REGISTRIES.items.getHolder(BaseMod.id("charm_potion"));
  public static final RegistryHolder<Item> PSYCH_BOMB_X = GameEngine.REGISTRIES.items.getHolder(BaseMod.id("psych_bomb_x"));
}
