package legend.game.inventory.screens;

import legend.game.i18n.I18n;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Checkbox;
import legend.game.inventory.screens.controls.Label;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static legend.core.GameEngine.MODS;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_HELP;

public class ModsScreen extends VerticalLayoutScreen {
  private final Runnable unload;

  private final Map<Control, Label> helpLabels = new HashMap<>();
  private final Map<Control, String> helpText = new HashMap<>();

  private final FontOptions fontOptions = new FontOptions().size(0.66f).horizontalAlign(HorizontalAlign.RIGHT).colour(TextColour.BROWN).shadowColour(TextColour.MIDDLE_BROWN);

  public ModsScreen(final Set<String> enabledMods, final Runnable unload) {
    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.unload = unload;

    this.addControl(new Background());

    // Sort mods by their translated names
    final List<String> modIds = MODS.getAllModIds().stream().sorted(Comparator.comparing(o -> I18n.translate(o + ".name"))).toList();

    for(final String modId : modIds) {
      if(!MODS.getRequiredModIds().contains(modId)) {
        final Checkbox checkbox = new Checkbox();
        checkbox.setChecked(enabledMods.contains(modId));
        checkbox.setHorizontalAlign(HorizontalAlign.RIGHT);

        checkbox.onChecked(() -> enabledMods.add(modId));
        checkbox.onUnchecked(() -> enabledMods.remove(modId));
        this.addRow(I18n.translate(modId + ".name"), checkbox);
      } else {
        final Label label = this.addRow(I18n.translate(modId + ".name"), null);

        final String required = I18n.translate("lod_core.ui.mods.required");
        final Label help = label.addControl(new Label("?"));
        help.setScale(0.4f);
        help.setPos((int)(textWidth(label.getText()) * label.getScale()) + 2, 1);
        help.onHoverIn(() -> this.getStack().pushScreen(new TooltipScreen(I18n.translate("lod_core.ui.mods.required"), this.mouseX, this.mouseY)));
        this.helpLabels.put(label, help);
        this.helpText.put(label, required);
      }
    }

    if(!MODS.getFailedToLoad().isEmpty()) {
      for(final var entry : MODS.getFailedToLoad().entrySet()) {
        final Label label = new Label(I18n.translate("lod_core.ui.mods.error", entry.getValue()));
        label.getFontOptions().horizontalAlign(HorizontalAlign.RIGHT);
        this.addRow(entry.getKey(), label).getFontOptions().colour(0.30f, 0.0f, 0.0f).shadowColour(TextColour.LIGHT_BROWN);
      }
    }

    if(!MODS.getWrongVersions().isEmpty()) {
      for(final var entry : MODS.getWrongVersions().entrySet()) {
        final Label label = new Label(I18n.translate("lod_core.ui.mods.wrong_version", entry.getValue()));
        label.getFontOptions().horizontalAlign(HorizontalAlign.RIGHT);
        this.addRow(entry.getKey(), label).getFontOptions().colour(0.30f, 0.0f, 0.0f).shadowColour(TextColour.LIGHT_BROWN);
      }
    }

    this.addHotkey(I18n.translate("lod_core.ui.mods.help"), INPUT_ACTION_MENU_HELP, this::help);
    this.addHotkey(I18n.translate("lod_core.ui.mods.back"), INPUT_ACTION_MENU_BACK, this::back);
  }

  private void help() {
    final String text = this.helpText.get(this.getHighlightedRow());
    if(text != null) {
      playMenuSound(1);
      final Label helpLabel = this.helpLabels.get(this.getHighlightedRow());
      this.getStack().pushScreen(new TooltipScreen(text, helpLabel.calculateTotalX() + helpLabel.getWidth() / 2, helpLabel.calculateTotalY() + helpLabel.getHeight() / 2));
    }
  }

  private void back() {
    playMenuSound(3);
    this.unload.run();
  }
}
