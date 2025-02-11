package legend.game.inventory.screens;

import legend.core.GameEngine;
import legend.game.i18n.I18n;
import legend.game.input.InputAction;
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
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.textWidth;

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
          //noinspection unchecked
          Control editControl;
          boolean error = false;

          try {
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

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    try {
      if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
        return InputPropagation.HANDLED;
      }

      if(inputAction == InputAction.BUTTON_EAST) {
        playMenuSound(3);
        this.unload.run();
        return InputPropagation.HANDLED;
      }

      if(inputAction == InputAction.BUTTON_NORTH) {
        final ConfigEntry<?> configEntry = this.helpEntries.get(this.getHighlightedRow());
        if(configEntry != null) {
          playMenuSound(1);
          final Label helpLabel = this.helpLabels.get(this.getHighlightedRow());
          this.getStack().pushScreen(new TooltipScreen(I18n.translate(configEntry.getHelpTranslationKey()), helpLabel.calculateTotalX() + helpLabel.getWidth() / 2, helpLabel.calculateTotalY() + helpLabel.getHeight() / 2));
        }

        return InputPropagation.HANDLED;
      }
    } catch(final Throwable ex) {
      this.replaceControlWithErrorLabel("Error on pressedThisFrame", ex);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected void render() {
    super.render();
    renderText(I18n.translate("lod_core.ui.options.help_hotkey", "\u0120"), 334, 226, this.fontOptions);
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
  protected InputPropagation pressedWithRepeatPulse(final InputAction inputAction) {
    try {
      return super.pressedWithRepeatPulse(inputAction);
    } catch(final Throwable ex) {
      this.replaceControlWithErrorLabel("Error on pressedWithRepeatPulse", ex);
    }
    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation keyPress(final int key, final int scancode, final int mods) {
    try {
      return super.keyPress(key, scancode, mods);
    } catch(final Throwable ex) {
      this.replaceControlWithErrorLabel("Error on keyPress", ex);
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
  protected InputPropagation releasedThisFrame(final InputAction inputAction) {
    try {
      return super.releasedThisFrame(inputAction);
    } catch(final Throwable ex) {
      this.replaceControlWithErrorLabel("Error on releasedThisFrame", ex);
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
