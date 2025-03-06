package legend.game.inventory.screens;

import legend.core.platform.input.InputMod;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static legend.core.GameEngine.PLATFORM;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_HELP;

public class LinksScreen extends VerticalLayoutScreen {
  private final Runnable unload;

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

    final Label help = this.addControl(new Label(I18n.translate("lod_core.ui.links.click_on_any_link_to_open")));
    help.setWidth(this.getWidth());
    help.getFontOptions().size(0.66f).horizontalAlign(HorizontalAlign.CENTRE);
    help.setY(200);

    this.addHotkey(I18n.translate("lod_core.ui.links.view_link"), INPUT_ACTION_MENU_HELP, this::viewLink);
    this.addHotkey(I18n.translate("lod_core.ui.links.open_link"), INPUT_ACTION_MENU_CONFIRM, this::openLink);
    this.addHotkey(I18n.translate("lod_core.ui.links.back"), INPUT_ACTION_MENU_BACK, this::back);
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

  private void openLink() {
    playMenuSound(2);
    PLATFORM.openUrl(this.links.get(this.getHighlightedRow().getText()));
  }

  private void viewLink() {
    playMenuSound(1);
    final String text = this.links.get(this.getHighlightedRow().getText());
    final Label helpLabel = this.linkLabels.get(this.getHighlightedRow().getText());
    this.getStack().pushScreen(new TooltipScreen(text, helpLabel.calculateTotalX() + helpLabel.getWidth() / 2, helpLabel.calculateTotalY() + helpLabel.getHeight() / 2));
  }

  private void back() {
    playMenuSound(3);
    this.unload.run();
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final Set<InputMod> mods) {
    final int linkIndex = Math.floorDiv(y - 32, 13);

    if(linkIndex >= 0 && linkIndex < this.linkText.size()) {
      playMenuSound(2);
      PLATFORM.openUrl(this.links.get(this.linkText.get(linkIndex)));
      return InputPropagation.HANDLED;
    }

    return super.mouseClick(x, y, button, mods);
  }
}
