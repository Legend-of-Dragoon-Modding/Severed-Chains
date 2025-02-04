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
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.textWidth;

public class OptionsScreen extends VerticalLayoutScreen {
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
          final Control editControl = configEntry.makeEditControl(config.getConfig(configEntry), config);
          editControl.setZ(35);

          final Label label = this.addRow(text, editControl);

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

    if(inputAction == InputAction.BUTTON_NORTH) {
      final ConfigEntry<?> configEntry = this.helpEntries.get(this.getHighlightedRow());
      if(configEntry != null) {
        playMenuSound(1);
        final Label helpLabel = this.helpLabels.get(this.getHighlightedRow());
        this.getStack().pushScreen(new TooltipScreen(I18n.translate(configEntry.getHelpTranslationKey()), helpLabel.calculateTotalX() + helpLabel.getWidth() / 2, helpLabel.calculateTotalY() + helpLabel.getHeight() / 2));
      }

      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected void render() {
    super.render();
    renderText(I18n.translate("lod_core.ui.options.help_hotkey", "\u0120"), 334, 226, this.fontOptions);
  }
}
