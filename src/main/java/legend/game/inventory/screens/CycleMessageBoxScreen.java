package legend.game.inventory.screens;

import legend.core.lang.I18nText;
import legend.core.lang.TextComponent;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.types.MessageBoxType;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Consumer;

import static legend.game.SItem.setMessageBoxFontOptions;
import static legend.game.SItem.setMessageBoxText;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_LEFT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_RIGHT;
import static legend.game.sound.Audio.playMenuSound;

public class CycleMessageBoxScreen extends MessageBoxScreen {
  private static final int ARROWS_MAX_TICKS = 45;

  private final TextComponent[] texts;
  private final FontOptions[] fonts;
  private int currentTextIndex;
  private int arrowsTick;

  private final FontOptions titleFont = new FontOptions().colour(TextColour.GOLD).shadowColour(TextColour.GREY_40).horizontalAlign(HorizontalAlign.CENTRE).size(0.9f);

  public CycleMessageBoxScreen(final TextComponent[] texts, final MessageBoxType type, final Consumer<MessageBoxResults> onResult) {
    this(texts, null, new I18nText("lod_core.ui.message_box.yes"), new I18nText("lod_core.ui.message_box.no"), type, onResult);
  }

  public CycleMessageBoxScreen(final TextComponent[] texts, final TextComponent yes, final TextComponent no, final MessageBoxType type, final Consumer<MessageBoxResults> onResult) {
    this(texts, null, yes, no, type, onResult);
  }

  public CycleMessageBoxScreen(final TextComponent[] texts, final FontOptions[] fonts, final MessageBoxType type, final Consumer<MessageBoxResults> onResult) {
    this(texts, fonts, new I18nText("lod_core.ui.message_box.yes"), new I18nText("lod_core.ui.message_box.no"), type, onResult);
  }

  public CycleMessageBoxScreen(final TextComponent[] texts, @Nullable final FontOptions[] fonts, final TextComponent yes, final TextComponent no, final MessageBoxType type, final Consumer<MessageBoxResults> onResult) {
    super("", yes, no, type, onResult);
    this.texts = texts;
    this.fonts = fonts;
    this.currentTextIndex = 0;
    this.arrowsTick = 0;
    this.setText(true);
  }

  private void menuNavigateLeft() {
    playMenuSound(1);
    this.currentTextIndex = this.currentTextIndex - 2 < 0 ? this.texts.length - 2 : this.currentTextIndex - 2;
    this.arrowsTick = 0;
    this.setText(false);
  }

  private void menuNavigateRight() {
    playMenuSound(1);
    this.currentTextIndex = this.currentTextIndex + 2 > this.texts.length - 2 ? 0 : this.currentTextIndex + 2;
    this.arrowsTick = 0;
    this.setText(false);
  }

  private void setText(final boolean isFirstSet) {
    setMessageBoxText(this.messageBox, this.getText(this.currentTextIndex), this.messageBox.type_15, isFirstSet);
    if(this.fonts != null) {
      setMessageBoxFontOptions(this.messageBox, 0, this.titleFont);
      for(int i = 1; i < this.messageBox.text_00.length; i++) {
        setMessageBoxFontOptions(this.messageBox, i, this.fonts[this.currentTextIndex / 2]);
      }
    }
  }

  private String getText(final int textIndex) {
    final boolean b = this.arrowsTick < (ARROWS_MAX_TICKS / 2);
    return (b ? "< " : "<< ") + this.texts[textIndex] + (b ? " >" : " >>")  + '\n' + this.texts[textIndex + 1];
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
    super.menuSelect(this.currentTextIndex / 2);
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
    return super.mouseClick(x, y, button, mods, this.currentTextIndex / 2);
  }
}