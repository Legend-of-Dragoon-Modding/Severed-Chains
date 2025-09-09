package legend.game.inventory.screens;

import legend.core.platform.input.AxisInputActivation;
import legend.core.platform.input.ButtonInputActivation;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputActivation;
import legend.core.platform.input.InputBindings;
import legend.core.platform.input.InputMod;
import legend.core.platform.input.KeyInputActivation;
import legend.core.platform.input.ScancodeInputActivation;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Button;
import legend.game.inventory.screens.controls.Label;
import legend.game.saves.ConfigCollection;
import legend.game.types.MessageBoxResult;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.REGISTRIES;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;

public class KeybindsScreen extends VerticalLayoutScreen {
  private final Runnable unload;
  private final ConfigCollection config;

  public KeybindsScreen(final ConfigCollection config, final Runnable unload) {
    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.config = config;
    this.unload = unload;

    this.addControl(new Background());

    final List<InputAction> actions = new ArrayList<>();

    for(final RegistryId actionId : REGISTRIES.inputActions) {
      actions.add(REGISTRIES.inputActions.getEntry(actionId).get());
    }

    actions.sort(Comparator.comparing(action -> I18n.translate(action.getTranslationKey())));

    for(final InputAction action : actions) {
      if(action.visible) {
        final Control control;

        if(action.canBeEdited) {
          final Button button = new Button(this.actionToString(action));
          button.onPressed(() -> menuStack.pushScreen(new KeybindScreen(action, this::actionToString, (result, activations) -> {
            if(result == MessageBoxResult.YES) {
              InputBindings.overwriteBindings(action, activations);
              button.setText(this.actionToString(action));
            }
          })));

          control = button;
        } else {
          final Label label = new Label(this.actionToString(action));
          label.getFontOptions().horizontalAlign(HorizontalAlign.CENTRE);
          control = label;
        }

        this.addRow(I18n.translate(action.getTranslationKey()), control);
      }
    }
  }

  private String actionToString(final InputAction action) {
    return this.actionToString(InputBindings.getActivationsForAction(action));
  }

  private String actionToString(final List<InputActivation> activations) {
    final List<String> text = new ArrayList<>();

    activations.sort(Comparator.comparing(activation -> activation.getClass().getSimpleName()));

    for(final InputActivation activation : activations) {
      if(activation instanceof final KeyInputActivation key) {
        text.add(this.modsToString(key.mods) + PLATFORM.getKeyName(key.key));
      } else if(activation instanceof final ScancodeInputActivation scancode) {
        text.add(this.modsToString(scancode.mods) + PLATFORM.getScancodeName(scancode.key));
      } else if(activation instanceof final ButtonInputActivation button) {
        text.add(String.valueOf(button.button.codepoint));
      } else if(activation instanceof final AxisInputActivation axis) {
        text.add(String.valueOf(axis.axis.codepoint));
      }
    }

    return String.join(", ", text);
  }

  private String modsToString(final Set<InputMod> mods) {
    String str = mods.stream().map(mod -> mod.toString().toLowerCase()).collect(Collectors.joining("+"));

    if(!str.isEmpty()) {
      str += '+';
    }

    return str;
  }

  @Override
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_BACK.get()) {
      playMenuSound(3);
      InputBindings.saveBindings(this.config);
      this.unload.run();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
