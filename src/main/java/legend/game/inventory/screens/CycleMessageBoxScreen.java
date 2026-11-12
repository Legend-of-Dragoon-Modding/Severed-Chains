package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.core.lang.TextComponent;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.types.MessageBox20;
import legend.game.types.MessageBoxResult;
import legend.game.types.MessageBoxType;

import java.util.Set;
import java.util.function.Consumer;

import static legend.game.SItem.menuStack;
import static legend.game.SItem.messageBox;
import static legend.game.SItem.setMessageBoxOptions;
import static legend.game.SItem.setMessageBoxText;
import static legend.game.Text.renderText;
import static legend.game.Text.textZ_800bdf00;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_LEFT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_RIGHT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;
import static legend.game.sound.Audio.playMenuSound;

public class CycleMessageBoxScreen extends MenuScreen {
  protected final MessageBox20 messageBox = new MessageBox20();
  private final Consumer<CycleMessageBoxResult> onResult;
  protected CycleMessageBoxResult result;
  /** Allows list wrapping, but only on new input */
  private boolean allowWrapY = true;

  protected final CycleMessageBoxEntry[] entries;
  protected int currentIndex;
  protected final int minValue;
  protected final int maxValue;
  private final int arrowLineIndex;
  private int arrowsTick;
  private final int arrowsMaxTicks;

  protected static int DEFAULT_ARROW_SPEED = 90;

  public CycleMessageBoxScreen(final String text, final TextComponent yes, final TextComponent no, final int minValue, final int maxValue, final MessageBoxType type, final Consumer<CycleMessageBoxResult> onResult, final int arrowSpeed, final FontOptions titleFont, final FontOptions textFont) {
    this.entries = new CycleMessageBoxEntry[1];
    this.entries[0] = new CycleMessageBoxEntry(new String[] { text, Integer.toString(minValue) }, new FontOptions[] { titleFont, textFont });
    this.arrowLineIndex = 1;
    this.arrowsTick = 0;
    this.arrowsMaxTicks = arrowSpeed;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.currentIndex = 0;
    this.messageBox.type_15 = type;
    this.onResult = onResult;

    this.setText(true);
    setMessageBoxOptions(this.messageBox, yes, no);
  }

  public CycleMessageBoxScreen(final CycleMessageBoxEntry[] entries, final TextComponent yes, final TextComponent no, final MessageBoxType type, final Consumer<CycleMessageBoxResult> onResult, final int arrowSpeed) {
    this.entries = entries;
    this.arrowLineIndex = 0;
    this.arrowsTick = 0;
    this.arrowsMaxTicks = arrowSpeed;
    this.minValue = 0;
    this.maxValue = entries.length - 1;
    this.currentIndex = 0;
    this.messageBox.type_15 = type;
    this.onResult = onResult;

    this.setText(true);
    setMessageBoxOptions(this.messageBox, yes, no);
  }

  @Override
  protected void delete() {
    super.delete();
    this.messageBox.delete();
  }

  protected CycleMessageBoxEntry getEntry(final int index) {
    return this.entries[index];
  }

  protected void setText(final boolean isFirstSet) {
    final StringBuilder text = new StringBuilder();
    text.repeat(" \n", this.entries[Math.min(this.currentIndex, this.entries.length - 1)].texts.length);
    if(isFirstSet) {
      setMessageBoxText(this.messageBox, text.toString(), this.messageBox.type_15);
    } else {
      this.messageBox.text_00 = text.toString().split("\n");
    }
  }

  @Override
  protected void render() {
    messageBox(this.messageBox);

    if (this.messageBox.state_0c == 3) {
      final CycleMessageBoxEntry entry = this.entries[Math.min(this.currentIndex, this.entries.length - 1)];
      final int leftPadding = 60;
      final int topPadding = this.messageBox.type_15 == MessageBoxType.CONFIRMATION ? 14 : 29;
      final int x = this.messageBox.x_1c + leftPadding;
      int y = this.messageBox.y_1e + topPadding;
      final int textHeight = 13;
      final int textOffset = this.messageBox.text_00.length * textHeight / 2;
      y -= textOffset;

      textZ_800bdf00 = (int)this.messageBox.backgroundRenderable_08.z_3c - 1;

      for(int i = 0; i < entry.texts.length; i++) {
        if(i == this.arrowLineIndex) {
          final boolean b = this.arrowsTick < (this.arrowsMaxTicks / 2);
          final String text = (b ? "< " : "<< ") + entry.texts[i] + (b ? " >" : " >>");
          renderText(text, x, y, entry.fonts[Math.min(i, entry.fonts.length - 1)]);
        } else {
          renderText(entry.texts[i], x, y, entry.fonts[Math.min(i, entry.fonts.length - 1)]);
        }
        y += textHeight;
      }
    }

    if(this.messageBox.state_0c == 0) {
      menuStack.popScreen();
      this.onResult.accept(this.result);
    }

    this.arrowsTick++;
    if(this.arrowsTick > this.arrowsMaxTicks) {
      this.arrowsTick = 0;
    }
  }

  @Override
  protected InputPropagation mouseMove(final double x, final double y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.messageBox.state_0c != 3) {
      return InputPropagation.PROPAGATE;
    }

    // Yes/no
    if(this.messageBox.type_15 == MessageBoxType.CONFIRMATION) {
      final int selectionY = this.messageBox.y_1e + 21 + this.messageBox.text_00.length * 12 / 2 - (this.messageBox.text_00.length - 1) * 3;

      if(this.messageBox.menuIndex_18 != 0 && MathHelper.inBox((int)x, (int)y, this.messageBox.x_1c + 4, selectionY, 112, 14)) {
        playMenuSound(1);
        this.messageBox.menuIndex_18 = 0;

        if(this.messageBox.highlightRenderable_04 != null) {
          this.messageBox.highlightRenderable_04.setY(selectionY - 2);
        }
      } else if(this.messageBox.menuIndex_18 != 1 && MathHelper.inBox((int)x, (int)y, this.messageBox.x_1c + 4, selectionY + 14, 112, 14)) {
        playMenuSound(1);
        this.messageBox.menuIndex_18 = 1;

        if(this.messageBox.highlightRenderable_04 != null) {
          this.messageBox.highlightRenderable_04.setY(selectionY + 12);
        }
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseClick(final double x, final double y, final int button, final Set<InputMod> mods)
  {
    return this.mouseClick(x, y, button, mods, 0);
  }

  protected InputPropagation mouseClick(final double x, final double y, final int button, final Set<InputMod> mods, final int intValue) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.messageBox.state_0c != 3) {
      return InputPropagation.PROPAGATE;
    }

    if(this.messageBox.type_15 == MessageBoxType.ALERT) {
      playMenuSound(2);
      this.result = new CycleMessageBoxResult(MessageBoxResult.YES, intValue);
      this.messageBox.state_0c = 4;
    } else if(this.messageBox.type_15 == MessageBoxType.CONFIRMATION) {
      // Yes/no
      final int selectionY = this.messageBox.y_1e + 21 + this.messageBox.text_00.length * 12 / 2 - (this.messageBox.text_00.length - 1) * 3;

      if(MathHelper.inBox((int)x, (int)y, this.messageBox.x_1c + 4, selectionY, 112, 14)) {
        playMenuSound(2);
        this.messageBox.menuIndex_18 = 0;

        if(this.messageBox.highlightRenderable_04 != null) {
          this.messageBox.highlightRenderable_04.setY(selectionY - 2);
        }

        this.result = new CycleMessageBoxResult(MessageBoxResult.YES, intValue);
        this.messageBox.state_0c = 4;
      } else if(MathHelper.inBox((int)x, (int)y, this.messageBox.x_1c + 4, selectionY + 14, 112, 14)) {
        playMenuSound(2);
        this.messageBox.menuIndex_18 = 1;

        if(this.messageBox.highlightRenderable_04 != null) {
          this.messageBox.highlightRenderable_04.setY(selectionY + 12);
        }

        this.result = new CycleMessageBoxResult(MessageBoxResult.NO, intValue);
        this.messageBox.state_0c = 4;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  private void menuNavigateUp() {
    if(this.messageBox.menuIndex_18 != 0) {
      playMenuSound(1);
      this.messageBox.menuIndex_18 = 0;
    } else if(this.allowWrapY) {
      playMenuSound(1);
      this.messageBox.menuIndex_18 = 1;
    }

    final int selectionY = this.messageBox.y_1e + 21 + this.messageBox.text_00.length * 12 / 2 - (this.messageBox.text_00.length - 1) * 3;
    if(this.messageBox.highlightRenderable_04 != null) {
      this.messageBox.highlightRenderable_04.setY(this.messageBox.menuIndex_18 == 0 ? selectionY - 2 : selectionY + 12);
    }
  }

  private void menuNavigateDown() {
    if(this.messageBox.menuIndex_18 != 1) {
      playMenuSound(1);
      this.messageBox.menuIndex_18 = 1;
    } else if(this.allowWrapY) {
      playMenuSound(1);
      this.messageBox.menuIndex_18 = 0;
    }

    final int selectionY = this.messageBox.y_1e + 21 + this.messageBox.text_00.length * 12 / 2 - (this.messageBox.text_00.length - 1) * 3;
    if(this.messageBox.highlightRenderable_04 != null) {
      this.messageBox.highlightRenderable_04.setY(this.messageBox.menuIndex_18 == 0 ? selectionY - 2 : selectionY + 12);
    }
  }

  protected void menuNavigateLeft() {
    playMenuSound(1);
    this.currentIndex = this.currentIndex - 1 < 0 ? this.maxValue - this.minValue : this.currentIndex - 1;
    this.arrowsTick = 0;
    this.setText(false);
  }

  protected void menuNavigateRight() {
    playMenuSound(1);
    this.currentIndex = this.currentIndex + 1 > this.maxValue - this.minValue ? 0 : this.currentIndex + 1;
    this.arrowsTick = 0;
    this.setText(false);
  }

  protected void menuSelect() {
    playMenuSound(2);
    this.messageBox.state_0c = 4;
  }

  private void menuCancel() {
    playMenuSound(3);
    this.result = new CycleMessageBoxResult(MessageBoxResult.CANCEL);
    this.messageBox.state_0c = 4;
  }

  protected boolean skipInput() {
    return this.messageBox.state_0c != 3;
  }

  @Override
  protected boolean propagateRender() {
    return true;
  }

  @Override
  public InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.skipInput()) {
      return InputPropagation.PROPAGATE;
    }

    if(this.messageBox.type_15 == MessageBoxType.ALERT) {
      playMenuSound(2);
      this.result = new CycleMessageBoxResult(MessageBoxResult.YES);
      this.messageBox.state_0c = 4;
      return InputPropagation.HANDLED;
    }

    if(this.messageBox.type_15 == MessageBoxType.CONFIRMATION) {
      if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
        this.menuSelect();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
        this.menuCancel();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_UP.get()) {
        this.menuNavigateUp();
        this.allowWrapY = false;
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_DOWN.get()) {
        this.menuNavigateDown();
        this.allowWrapY = false;
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

    return InputPropagation.PROPAGATE;
  }

  @Override
  public InputPropagation inputActionReleased(final InputAction action) {
    if(super.inputActionReleased(action) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_UP.get() || action == INPUT_ACTION_MENU_DOWN.get()) {
      this.allowWrapY = true;
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}