package legend.lodmod;

import legend.game.combat.ui.BattleAction;
import legend.game.combat.ui.RegisterBattleActionsEvent;
import legend.lodmod.battleactions.ChangeAdditionBattleAction;
import legend.lodmod.battleactions.SpecialBattleAction;
import legend.lodmod.battleactions.TransformAction;
import legend.lodmod.battleactions.ItemBattleAction;
import legend.lodmod.battleactions.RetailBattleAction;
import legend.lodmod.battleactions.SpellBattleAction;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import static legend.core.GameEngine.REGISTRIES;

public final class LodBattleActions {
  private LodBattleActions() { }

  private static final Registrar<BattleAction, RegisterBattleActionsEvent> REGISTRAR = new Registrar<>(REGISTRIES.battleActions, LodMod.MOD_ID);

  public static final RegistryDelegate<BattleAction> ATTACK = REGISTRAR.register("attack", () -> new RetailBattleAction(4));
  public static final RegistryDelegate<BattleAction> GUARD = REGISTRAR.register("guard", () -> new RetailBattleAction(1));
  public static final RegistryDelegate<BattleAction> ITEMS = REGISTRAR.register("items", ItemBattleAction::new);
  public static final RegistryDelegate<BattleAction> SPELLS = REGISTRAR.register("spells", SpellBattleAction::new);
  public static final RegistryDelegate<BattleAction> ESCAPE = REGISTRAR.register("escape", () -> new RetailBattleAction(6));
  public static final RegistryDelegate<BattleAction> TRANSFORM = REGISTRAR.register("transform", TransformAction::new);
  public static final RegistryDelegate<BattleAction> SPECIAL = REGISTRAR.register("special", SpecialBattleAction::new);
  public static final RegistryDelegate<BattleAction> D_ATTACK = REGISTRAR.register("d_attack", () -> new RetailBattleAction(9));
  public static final RegistryDelegate<BattleAction> ADDITIONS = REGISTRAR.register("additions", ChangeAdditionBattleAction::new);

  static void register(final RegisterBattleActionsEvent event) {
    REGISTRAR.registryEvent(event);
  }
}
