package legend.game.inventory.screens;

import legend.core.GameEngine;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputButton;
import legend.core.platform.input.InputKey;
import legend.core.platform.input.InputMod;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Label;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_HELP;

public class OptionsScreen extends VerticalLayoutScreen {
  private static final Logger LOGGER = LogManager.getFormatterLogger(OptionsScreen.class);
  private final Runnable unload;

  private final Map<Control, Label> helpLabels = new HashMap<>();
  private final Map<Control, ConfigEntry<?>> helpEntries = new HashMap<>();

  private final FontOptions fontOptions = new FontOptions().size(0.66f).horizontalAlign(HorizontalAlign.RIGHT).colour(TextColour.BROWN).shadowColour(TextColour.MIDDLE_BROWN);

  public OptionsScreen(final ConfigCollection config, final Set<ConfigStorageLocation> validLocations, final ConfigCategory category, final Runnable unload) {
    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.unload = unload;

    this.addControl(new Background());

    final Map<ConfigEntry<?>, String> translations = new HashMap<>();

    for(final RegistryId configId : GameEngine.REGISTRIES.config) {
      final ConfigEntry<?> entry = GameEngine.REGISTRIES.config.getEntry(configId).get();

      if(entry.category == category) {
        translations.put(entry, I18n.translate(entry.getLabelTranslationKey()));
      }
    }

    translations.entrySet().stream()
      .sorted((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getValue(), o2.getValue()))
      .forEach(entry -> {
        //noinspection rawtypes
        final ConfigEntry configEntry = entry.getKey();
        final String text = entry.getValue();

        if(validLocations.contains(configEntry.storageLocation) && configEntry.hasEditControl()) {
          Control editControl;
          boolean error = false;

          try {
            //noinspection unchecked
            editControl = configEntry.makeEditControl(config.getConfig(configEntry), config);
          } catch(final Throwable ex) {
            editControl = this.createErrorLabel("Error creating control", ex, false);
            error = true;
          }

          editControl.setZ(35);

          final Label label = this.addRow(text, editControl);

          if(error) {
            label.getFontOptions().colour(0.30f, 0.0f, 0.0f).shadowColour(TextColour.LIGHT_BROWN);
          }

          if(configEntry.hasHelp()) {
            final Label help = label.addControl(new Label("?"));
            help.setScale(0.4f);
            help.setPos((int)(textWidth(text) * label.getScale()) + 2, 1);
            help.onHoverIn(() -> this.getStack().pushScreen(new TooltipScreen(I18n.translate(configEntry.getHelpTranslationKey()), this.mouseX, this.mouseY)));
            this.helpLabels.put(label, help);
            this.helpEntries.put(label, configEntry);
          }
        }
      });

    this.addHotkey(I18n.translate("lod_core.ui.options.help"), INPUT_ACTION_MENU_HELP, this::help);
    this.addHotkey(I18n.translate("lod_core.ui.options.back"), INPUT_ACTION_MENU_BACK, this::back);
  }

  private Label createErrorLabel(final String log, final Throwable ex, final boolean setSize) {
    LOGGER.warn(log, ex);
    final Label l = new Label(I18n.translate("lod_core.ui.options.error"));
    l.getFontOptions().colour(0.30f, 0.0f, 0.0f).shadowColour(TextColour.LIGHT_BROWN);

    if(setSize) {
      l.setSize(140, 11);
      l.setPos(this.getWidth() - 64 - l.getWidth(), 0);
      l.setScale(0.66f);
    }

    return l;
  }

  private void replaceControlWithErrorLabel(final String log, final Throwable ex) {
    final Label row = this.getHighlightedRow();
    if(row != null) {
      row.getFontOptions().colour(0.30f, 0.0f, 0.0f).shadowColour(TextColour.LIGHT_BROWN);
      for(int i = row.getControls().size() - 1; i > -1; i--) {
        row.removeControl(row.getControl(i));
      }
      row.addControl(this.createErrorLabel(log, ex, true));
    }
  }

  private void back() {
    playMenuSound(3);
    this.unload.run();
  }

  private void help() {
    final ConfigEntry<?> configEntry = this.helpEntries.get(this.getHighlightedRow());
    if(configEntry != null) {
      playMenuSound(2);
      final Label helpLabel = this.helpLabels.get(this.getHighlightedRow());
      this.getStack().pushScreen(new TooltipScreen(I18n.translate(configEntry.getHelpTranslationKey()), helpLabel.calculateTotalX() + helpLabel.getWidth() / 2, helpLabel.calculateTotalY() + helpLabel.getHeight() / 2));
    }
  }

  @Override
  public InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    try {
      return super.inputActionPressed(action, repeat);
    } catch(final Throwable ex) {
      this.replaceControlWithErrorLabel("Error on pressedThisFrame", ex);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected void renderControls(final int parentX, final int parentY) {
    try {
      super.renderControls(parentX, parentY);
    } catch(final Throwable ex) {
      this.replaceControlWithErrorLabel("Error on renderControls", ex);
    }
  }

  @Override
  protected InputPropagation mouseMove(final int x, final int y) {
    try {
      return super.mouseMove(x, y);
    } catch(final Throwable ex) {
      this.replaceControlWithErrorLabel("Error on keyPress", ex);
    }
    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseScroll(final int deltaX, final int deltaY) {
    try {
      return super.mouseScroll(deltaX, deltaY);
    } catch(final Throwable ex) {
      this.replaceControlWithErrorLabel("Error on mouseScroll", ex);
    }
    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation keyPress(final InputKey key, final InputKey scancode, final Set<InputMod> mods, final boolean repeat) {
    try {
      return super.keyPress(key, scancode, mods, repeat);
    } catch(final Throwable ex) {
      this.replaceControlWithErrorLabel("Error on keyPress", ex);
    }
    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation keyRelease(final InputKey key, final InputKey scancode, final Set<InputMod> mods) {
    try {
      return super.keyRelease(key, scancode, mods);
    } catch(final Throwable ex) {
      this.replaceControlWithErrorLabel("Error on keyRelease", ex);
    }
    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation buttonPress(final InputButton button, final boolean repeat) {
    try {
      return super.buttonPress(button, repeat);
    } catch(final Throwable ex) {
      this.replaceControlWithErrorLabel("Error on buttonPress", ex);
    }
    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation buttonRelease(final InputButton button) {
    try {
      return super.buttonRelease(button);
    } catch(final Throwable ex) {
      this.replaceControlWithErrorLabel("Error on buttonRelease", ex);
    }
    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation charPress(final int codepoint) {
    try {
      return super.charPress(codepoint);
    } catch(final Throwable ex) {
      this.replaceControlWithErrorLabel("Error on charPress", ex);
    }
    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation inputActionReleased(final InputAction action) {
    try {
      return super.inputActionReleased(action);
    } catch(final Throwable ex) {
      this.replaceControlWithErrorLabel("Error on inputActionReleased", ex);
    }
    return InputPropagation.PROPAGATE;
  }

  @Override
  public void setFocus(@Nullable final Control control) {
    try {
      super.setFocus(control);
    } catch(final Throwable ex) {
      this.replaceControlWithErrorLabel("Error on setFocus", ex);
    }
  }

  @Override
  protected InputPropagation mouseScrollHighRes(final double deltaX, final double deltaY) {
    try {
      return super.mouseScrollHighRes(deltaX, deltaY);
    } catch(final Throwable ex) {
      this.replaceControlWithErrorLabel("Error on mouseScrollHighRes", ex);
    }
    return InputPropagation.PROPAGATE;
  }
}
