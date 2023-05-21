package legend.game.inventory.screens;

import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Checkbox;
import legend.game.inventory.screens.controls.Label;
import legend.game.modding.coremod.CoreMod;

import java.util.Set;

import static legend.core.GameEngine.MODS;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playSound;

public class ModsScreen extends VerticalLayoutScreen {
  private final Runnable unload;

  public ModsScreen(final Set<String> enabledMods, final Runnable unload) {
    deallocateRenderables(0xff);
    scriptStartEffect(2, 10);

    this.unload = unload;

    this.addControl(new Background());

    for(final String modId : MODS.getAllModIds()) {
      final Checkbox checkbox = new Checkbox();
      checkbox.setChecked(enabledMods.contains(modId));
      checkbox.setHorizontalAlign(Label.HorizontalAlign.RIGHT);
      checkbox.setDisabled(this.isRequired(modId));

      checkbox.onChecked(() -> enabledMods.add(modId));
      checkbox.onUnchecked(() -> enabledMods.remove(modId));

      this.addRow(modId, checkbox);
    }
  }

  private boolean isRequired(final String modId) {
    return CoreMod.MOD_ID.equals(modId);
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_EAST) {
      playSound(3);
      this.unload.run();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
