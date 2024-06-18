package legend.game.inventory.screens;

import legend.game.i18n.I18n;
import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Checkbox;
import legend.game.inventory.screens.controls.Label;
import legend.game.modding.coremod.CoreMod;
import legend.lodmod.LodMod;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static legend.core.GameEngine.MODS;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;

public class ModsScreen extends VerticalLayoutScreen {
  private final Runnable unload;

  public ModsScreen(final Set<String> enabledMods, final Runnable unload) {
    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.unload = unload;

    this.addControl(new Background());

    // Sort mods by their translated names
    final List<String> modIds = MODS.getAllModIds().stream().sorted(Comparator.comparing(o -> I18n.translate(o + ".name"))).toList();

    for(final String modId : modIds) {
      final Checkbox checkbox = new Checkbox();
      checkbox.setChecked(enabledMods.contains(modId));
      checkbox.setHorizontalAlign(Label.HorizontalAlign.RIGHT);
      checkbox.setDisabled(this.isRequired(modId));

      checkbox.onChecked(() -> enabledMods.add(modId));
      checkbox.onUnchecked(() -> enabledMods.remove(modId));

      this.addRow(I18n.translate(modId + ".name"), checkbox);
    }
  }

  private boolean isRequired(final String modId) {
    return CoreMod.MOD_ID.equals(modId) || LodMod.MOD_ID.equals(modId);
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_EAST) {
      playMenuSound(3);
      this.unload.run();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
