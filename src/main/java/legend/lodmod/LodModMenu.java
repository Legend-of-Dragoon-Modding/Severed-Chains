package legend.lodmod;

import legend.game.combat.ui.ModMenu;
import legend.game.combat.ui.ModMenuAdditionSwap;
import legend.game.combat.ui.RegisterModMenuEvent;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import static legend.core.GameEngine.REGISTRIES;

public final class LodModMenu {
  private LodModMenu() { }

  private static final Registrar<ModMenu, RegisterModMenuEvent> MODMENU_REGISTRAR = new Registrar<>(REGISTRIES.modMenu, LodMod.MOD_ID);

  public static final RegistryDelegate<ModMenu> ADDITION_SWAP = MODMENU_REGISTRAR.register("addition_swap", () -> new ModMenuAdditionSwap("Addition Swap"));

  static void register(final RegisterModMenuEvent event) {
    MODMENU_REGISTRAR.registryEvent(event);
  }
}
