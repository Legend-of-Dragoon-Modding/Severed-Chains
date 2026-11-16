package legend.game.inventory.screens;

import legend.core.lang.I18nText;
import legend.core.lang.TextComponent;
import legend.game.types.MessageBoxResult;
import legend.game.types.MessageBoxType;

import java.util.function.Consumer;

public class ChoiceMessageBoxScreen extends CycleMessageBoxScreen {
  public ChoiceMessageBoxScreen(final CycleMessageBoxEntry[] entries, final MessageBoxType type, final Consumer<CycleMessageBoxResult> onResult) {
    super(entries, new I18nText("lod_core.ui.message_box.yes"), new I18nText("lod_core.ui.message_box.no"), type, onResult, DEFAULT_ARROW_SPEED);
  }

  public ChoiceMessageBoxScreen(final CycleMessageBoxEntry[] entries, final MessageBoxType type, final Consumer<CycleMessageBoxResult> onResult, final int arrowSpeed) {
    super(entries, new I18nText("lod_core.ui.message_box.yes"), new I18nText("lod_core.ui.message_box.no"), type, onResult, arrowSpeed);
  }

  public ChoiceMessageBoxScreen(final CycleMessageBoxEntry[] entries, final TextComponent yes, final TextComponent no, final MessageBoxType type, final Consumer<CycleMessageBoxResult> onResult) {
    super(entries, yes, no, type, onResult, DEFAULT_ARROW_SPEED);
  }

  public ChoiceMessageBoxScreen(final CycleMessageBoxEntry[] entries, final TextComponent yes, final TextComponent no, final MessageBoxType type, final Consumer<CycleMessageBoxResult> onResult, final int arrowSpeed) {
    super(entries, yes, no, type, onResult, arrowSpeed);
  }

  @Override
  protected void menuSelect() {
    super.menuSelect();

    if(this.messageBox.menuIndex_18 == 0) {
      this.result = new CycleMessageBoxResult(MessageBoxResult.YES, this.currentIndex);
    } else {
      this.result = new CycleMessageBoxResult(MessageBoxResult.NO, this.currentIndex);
    }
  }
}