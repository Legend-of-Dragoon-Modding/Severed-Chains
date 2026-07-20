package legend.game.inventory.screens;

import legend.core.lang.I18nText;
import legend.core.lang.RawText;
import legend.core.lang.TextComponent;
import legend.core.platform.input.InputMod;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Label;
import legend.game.modding.coremod.CoreEngineStateTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static legend.core.GameEngine.PLATFORM;
import static legend.game.EngineStates.engineStateOnceLoaded_8004dd24;
import static legend.game.EngineStates.postCreditsEngineState;
import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Menus.deallocateRenderables;
import static legend.game.SItem.menuStack;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_HELP;
import static legend.game.sound.Audio.playMenuSound;

public class AboutScreen extends VerticalLayoutScreen {
  private final Runnable unload;
  private int loadingStage;

  private final List<Label> linkLabels = new ArrayList<>();
  private final Map<Label, String> links = new HashMap<>();

  private static final String LOD_CREDITS = "[LOD_CREDITS]";
  private static final String SC_CREDITS = "[SC_CREDITS]";

  public AboutScreen(final Runnable unload) {
    this.unload = unload;

    this.addControl(new Background());
    this.addLink(new I18nText("lod_core.ui.about.discord"), "https://discord.gg/legendofdragoon", true);
    this.addLink(new I18nText("lod_core.ui.about.youtube"), "https://www.youtube.com/@legend-of-dragoon", true);
    this.addLink(new I18nText("lod_core.ui.about.fandom"), "https://legendofdragoon.org", true);
    this.addLink(new I18nText("lod_core.ui.about.wiki"), "https://legendofdragoon.org/wiki/Main_Page", true);
    this.addLink(new I18nText("lod_core.ui.about.project"), "https://legendofdragoon.org/projects/severed-chains", true);
    this.addLink(new I18nText("lod_core.ui.about.github"), "https://github.com/Legend-of-Dragoon-Modding/Severed-Chains", true);
    this.addLink(new I18nText("lod_core.ui.about.issue"), "https://github.com/Legend-of-Dragoon-Modding/Severed-Chains/issues", true);
    this.addLink(new I18nText("lod_core.ui.about.lod_credits"), LOD_CREDITS, false);
    this.addLink(new I18nText("lod_core.ui.about.sc_credits"), SC_CREDITS, false);

    final Label help = this.addControl(new Label(new I18nText("lod_core.ui.about.click_on_any_link_to_open")));
    help.setWidth(this.getWidth());
    help.getFontOptions().size(0.66f).horizontalAlign(HorizontalAlign.CENTRE);
    help.setY(200);

    this.addHotkey(new I18nText("lod_core.ui.about.view_link"), INPUT_ACTION_MENU_HELP, this::viewLink);
    this.addHotkey(new I18nText("lod_core.ui.about.open_link"), INPUT_ACTION_MENU_CONFIRM, this::openLink);
    this.addHotkey(new I18nText("lod_core.ui.about.back"), INPUT_ACTION_MENU_BACK, this::back);
  }

  private void addLink(final TextComponent text, final String url, final boolean help) {
    final Label label = this.addRow(text, null);

    this.linkLabels.add(label);
    this.links.put(label, url);

    if(help) {
      final Label tooltip = label.addControl(new Label(new I18nText("lod_core.ui.about.tooltip")));
      tooltip.setScale(0.4f);
      tooltip.setPos((int)(tooltip.getFont().textWidth(label.getText().get()) * label.getScale()) + 2, 1);
      tooltip.onHoverIn(() -> this.getStack().pushScreen(new TooltipScreen(new RawText(url), (int)this.mouseX, (int)this.mouseY)));
    }
  }

  private void openLink() {
    playMenuSound(2);
    this.execute(this.links.get(this.getHighlightedRow()));
  }

  private void viewLink() {
    playMenuSound(1);
    final String text = this.links.get(this.getHighlightedRow());
    final Label label = this.getHighlightedRow();
    this.getStack().pushScreen(new TooltipScreen(new RawText(text), label.calculateTotalX() + label.getWidth() / 2, label.calculateTotalY() + label.getHeight() / 2));
  }

  private void back() {
    playMenuSound(3);
    this.unload.run();
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        deallocateRenderables(0xff);
        startFadeEffect(2, 10);

        this.loadingStage++;
      }

      case 1 -> super.render();
    }
  }

  @Override
  protected InputPropagation mouseClick(final double x, final double y, final int button, final Set<InputMod> mods) {
    final int linkIndex = Math.floorDiv((int)(y - 32), 13);

    if(linkIndex >= 0 && linkIndex < this.linkLabels.size()) {
      playMenuSound(2);
      this.execute(this.links.get(this.linkLabels.get(linkIndex)));
      return InputPropagation.HANDLED;
    }

    return super.mouseClick(x, y, button, mods);
  }

  private void execute(final String url) {
    if(url.equals(LOD_CREDITS)) {
      postCreditsEngineState = CoreEngineStateTypes.TITLE.get();
      engineStateOnceLoaded_8004dd24 = CoreEngineStateTypes.CREDITS.get();
    } else if(url.equals(SC_CREDITS)) {
      this.showScreen(CreditsScreen::new);
    } else {
      PLATFORM.openUrl(url);
    }
  }

  private void showScreen(final Function<Runnable, MenuScreen> screen) {
    menuStack.pushScreen(screen.apply(menuStack::popScreen));
    this.loadingStage = 0;
  }
}
