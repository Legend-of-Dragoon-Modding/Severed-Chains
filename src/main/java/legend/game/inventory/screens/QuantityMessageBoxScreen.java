package legend.game.inventory.screens;

import legend.core.lang.I18nText;
import legend.core.lang.TextComponent;
import legend.game.types.MessageBoxResult;
import legend.game.types.MessageBoxType;

import java.util.function.Consumer;

public class QuantityMessageBoxScreen extends CycleMessageBoxScreen {
  public QuantityMessageBoxScreen(final String text, final int minQuantity, final int maxQuantity, final MessageBoxType type, final Consumer<CycleMessageBoxResult> onResult) {
    final FontOptions textFont = new FontOptions().colour(TextColour.BROWN).shadowColour(TextColour.MIDDLE_BROWN).horizontalAlign(HorizontalAlign.CENTRE).size(0.95f);
    final FontOptions quantityFont = new FontOptions().colour(TextColour.GREY_54).shadowColour(TextColour.LIGHT_BROWN).horizontalAlign(HorizontalAlign.CENTRE).size(0.85f);
    this(text, new I18nText("lod_core.ui.message_box.yes"), new I18nText("lod_core.ui.message_box.no"), minQuantity, maxQuantity, type, onResult, textFont, quantityFont);
  }

  public QuantityMessageBoxScreen(final String text, final TextComponent yes, final TextComponent no, final int minQuantity, final int maxQuantity, final MessageBoxType type, final Consumer<CycleMessageBoxResult> onResult, final FontOptions textFont, final FontOptions quantityFont) {
    super(text, yes, no, minQuantity, maxQuantity, type, onResult, DEFAULT_ARROW_SPEED, textFont, quantityFont);
  }

  @Override
  protected void menuSelect() {
    super.menuSelect();

    if(this.messageBox.menuIndex_18 == 0) {
      this.result = new CycleMessageBoxResult(MessageBoxResult.YES, this.currentIndex + this.minValue);
    } else {
      this.result = new CycleMessageBoxResult(MessageBoxResult.NO, this.currentIndex + this.minValue);
    }
  }

  @Override
  protected void setText(final boolean isFirstSet) {
    final CycleMessageBoxEntry entry = this.getEntry(0);
    entry.texts[1] = Integer.toString(this.currentIndex + this.minValue);
    super.setText(isFirstSet);
  }
}