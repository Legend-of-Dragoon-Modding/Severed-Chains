package legend.lodmod;

import legend.core.GameEngine;
import legend.game.characters.LevelUpAction;
import legend.game.characters.RegisterLevelUpActionsEvent;
import legend.lodmod.characters.DragoonLevelUpLevelUpAction;
import legend.lodmod.characters.LevelUpLevelUpAction;
import legend.lodmod.characters.UnlockAdditionLevelUpAction;
import legend.lodmod.characters.UnlockAdditionLevelUpActionOptions;
import legend.lodmod.characters.UnlockSpellLevelUpAction;
import legend.lodmod.characters.UnlockSpellLevelUpActionOptions;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

public final class LodLevelUpActions {
  private LodLevelUpActions() { }

  private static final Registrar<LevelUpAction<?>, RegisterLevelUpActionsEvent> REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.levelUpActions, LodMod.MOD_ID);

  public static final RegistryDelegate<LevelUpAction<Void>> LEVEL_UP = REGISTRAR.register("level_up", LevelUpLevelUpAction::new);
  public static final RegistryDelegate<LevelUpAction<Void>> DRAGOON_LEVEL_UP = REGISTRAR.register("dragoon_level_up", DragoonLevelUpLevelUpAction::new);
  public static final RegistryDelegate<LevelUpAction<UnlockAdditionLevelUpActionOptions>> UNLOCK_ADDITION = REGISTRAR.register("unlock_addition", UnlockAdditionLevelUpAction::new);
  public static final RegistryDelegate<LevelUpAction<UnlockSpellLevelUpActionOptions>> UNLOCK_SPELL = REGISTRAR.register("unlock_spell", UnlockSpellLevelUpAction::new);

  static void register(final RegisterLevelUpActionsEvent event) {
    REGISTRAR.registryEvent(event);
  }
}
