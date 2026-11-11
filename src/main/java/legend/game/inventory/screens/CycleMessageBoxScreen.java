package legend.game.inventory.screens;

import legend.core.lang.I18nText;
import legend.core.lang.TextComponent;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.types.MessageBoxType;

import java.util.Set;
import java.util.function.Consumer;

import static legend.game.SItem.setMessageBoxFontOptions;
import static legend.game.SItem.setMessageBoxText;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_LEFT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_RIGHT;
import static legend.game.sound.Audio.playMenuSound;

public class CycleMessageBoxScreen extends MessageBoxScreen {
  private static final int ARROWS_MAX_TICKS = 90;

  private final String messageText;
  private final int minValue;
  private final int maxValue;
  private int currentValue;
  private int arrowsTick;

  private final FontOptions firstLineFont = new FontOptions().colour(TextColour.BROWN).shadowColour(TextColour.MIDDLE_BROWN).horizontalAlign(HorizontalAlign.CENTRE).size(0.95f);
  private final FontOptions secondLineFont = new FontOptions().colour(TextColour.DARK_GREY_54).shadowColour(TextColour.LIGHT_BROWN).horizontalAlign(HorizontalAlign.CENTRE).size(0.85f);

  public CycleMessageBoxScreen(final String text, final int minValue, final int maxValue, final MessageBoxType type, final Consumer<MessageBoxResults> onResult) {
    this(text, new I18nText("lod_core.ui.message_box.yes"), new I18nText("lod_core.ui.message_box.no"), minValue, maxValue, type, onResult);
  }

  public CycleMessageBoxScreen(final String text, final TextComponent yes, final TextComponent no, final int minValue, final int maxValue, final MessageBoxType type, final Consumer<MessageBoxResults> onResult) {
    super(text, yes, no, type, onResult);
    this.messageText = text;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.currentValue = 1;
    this.arrowsTick = 0;
    this.setText(true);
  }

  private void setText(final boolean isFirstSet) {
    setMessageBoxText(this.messageBox, this.getText(), this.messageBox.type_15, isFirstSet);
    setMessageBoxFontOptions(this.messageBox, 0, this.firstLineFont);
    setMessageBoxFontOptions(this.messageBox, 1, this.secondLineFont);
  }

  private String getText() {
    final boolean b = this.arrowsTick < (ARROWS_MAX_TICKS / 2);
    return this.messageText + '\n' + (b ? "< " : "<< ") + '×' + this.currentValue + (b ? " >" : " >>");
  }

  @Override
  protected void render() {
    super.render();

    this.arrowsTick++;
    if(this.arrowsTick > ARROWS_MAX_TICKS) {
      this.arrowsTick = 0;
      this.setText(false);
    } else if(this.arrowsTick == ARROWS_MAX_TICKS / 2) {
      this.setText(false);
    }
  }

  @Override
  protected void menuSelect() {
    super.menuSelect(this.currentValue);
  }

  private void menuNavigateLeft() {
    playMenuSound(1);
    this.currentValue = this.currentValue - 1 < this.minValue ? this.maxValue : this.currentValue - 1;
    this.arrowsTick = 0;
    this.setText(false);
  }

  private void menuNavigateRight() {
    playMenuSound(1);
    this.currentValue = this.currentValue + 1 > this.maxValue ? this.minValue : this.currentValue + 1;
    this.arrowsTick = 0;
    this.setText(false);
  }

  @Override
  public InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.skipInput()) {
      return InputPropagation.PROPAGATE;
    }

    if(this.messageBox.type_15 == MessageBoxType.CONFIRMATION) {
      if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
        this.menuSelect();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_LEFT.get()) {
        this.menuNavigateLeft();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_RIGHT.get()) {
        this.menuNavigateRight();
        return InputPropagation.HANDLED;
      }
    }

    return super.inputActionPressed(action, repeat);
  }

  @Override
  protected InputPropagation mouseClick(final double x, final double y, final int button, final Set<InputMod> mods) {
    return super.mouseClick(x, y, button, mods, this.currentValue);
  }
}