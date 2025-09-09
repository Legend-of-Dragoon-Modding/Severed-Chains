package legend.game.inventory.screens;

import legend.game.SItem;
import legend.game.inventory.screens.controls.Background;
import legend.game.types.MessageBoxResult;

import java.util.function.BiConsumer;

import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;

public class FullScreenInputScreen extends MenuScreen {
  private final String prompt;

  public FullScreenInputScreen(final String prompt, final String subprompt, final String defaultText, final BiConsumer<MessageBoxResult, String> onResult) {
    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.prompt = prompt;

    this.addControl(new Background());

    // Defer so that the screen gets added after this one
    this.deferAction(() -> menuStack.pushScreen(new InputBoxScreen(subprompt, defaultText, onResult)));
  }

  @Override
  protected void render() {
    SItem.renderMenuCentredText(this.prompt, 188, 25, 240, UI_TEXT);
  }
}
