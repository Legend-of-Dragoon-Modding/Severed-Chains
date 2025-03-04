package legend.game.inventory.screens;

import com.vaadin.open.Open;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_HELP;

public class LinksScreen extends VerticalLayoutScreen {
  private final Runnable unload;

  private final FontOptions fontOptions = new FontOptions().size(0.66f).horizontalAlign(HorizontalAlign.RIGHT).colour(TextColour.BROWN).shadowColour(TextColour.MIDDLE_BROWN);

  private final List<String> linkText = new ArrayList<>();
  private final Map<String, String> links = new HashMap<>();
  private final Map<String, Label> linkLabels = new HashMap<>();

  public LinksScreen(final Runnable unload) {
    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.unload = unload;

    this.addControl(new Background());
    this.addLink(I18n.translate("lod_core.ui.links.discord"), "https://discord.gg/legendofdragoon");
    this.addLink(I18n.translate("lod_core.ui.links.youtube"), "https://www.youtube.com/@legend-of-dragoon");
    this.addLink(I18n.translate("lod_core.ui.links.fandom"), "https://legendofdragoon.org");
    this.addLink(I18n.translate("lod_core.ui.links.wiki"), "https://legendofdragoon.org/wiki/Main_Page");
    this.addLink(I18n.translate("lod_core.ui.links.project"), "https://legendofdragoon.org/projects/severed-chains");
    this.addLink(I18n.translate("lod_core.ui.links.github"), "https://github.com/Legend-of-Dragoon-Modding/Severed-Chains");
    this.addLink(I18n.translate("lod_core.ui.links.issue"), "https://github.com/Legend-of-Dragoon-Modding/Severed-Chains/issues");

    final Label help = this.addControl(new Label("Click on any link to open it"));
    help.setWidth(this.getWidth());
    help.getFontOptions().size(0.66f).horizontalAlign(HorizontalAlign.CENTRE);
    help.setY(200);
  }

  private void addLink(final String text, final String url) {
    this.linkText.add(text);
    this.links.put(text, url);

    final Label label = this.addRow(text, null);
    final Label tooltip = label.addControl(new Label("?"));
    tooltip.setScale(0.4f);
    tooltip.setPos((int)(textWidth(label.getText()) * label.getScale()) + 2, 1);
    tooltip.onHoverIn(() -> this.getStack().pushScreen(new TooltipScreen(url, this.mouseX, this.mouseY)));
    this.linkLabels.put(text, label);
  }

  @Override
  protected void render() {
    renderText(I18n.translate("lod_core.ui.links.hotkeys", "\u011d", "\u0120", "\u011e"), 334, 226, this.fontOptions);
  }

  @Override
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
      playMenuSound(2);
      Open.open(this.links.get(this.getHighlightedRow().getText()));
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
      playMenuSound(3);
      this.unload.run();
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_HELP.get() && !repeat) {
      playMenuSound(1);
      final String text = this.links.get(this.getHighlightedRow().getText());
      final Label helpLabel = this.linkLabels.get(this.getHighlightedRow().getText());
      this.getStack().pushScreen(new TooltipScreen(text, helpLabel.calculateTotalX() + helpLabel.getWidth() / 2, helpLabel.calculateTotalY() + helpLabel.getHeight() / 2));
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final Set<InputMod> mods) {
    final int linkIndex = Math.floorDiv(y - 32, 13);

    if(linkIndex >= 0 && linkIndex < this.linkText.size()) {
      playMenuSound(2);
      Open.open(this.links.get(this.linkText.get(linkIndex)));
      return InputPropagation.HANDLED;
    }

    return super.mouseClick(x, y, button, mods);
  }
}
