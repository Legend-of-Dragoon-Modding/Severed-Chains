package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.types.MessageBox20;
import legend.game.types.MessageBoxResult;

import java.util.Set;
import java.util.function.Consumer;

import static legend.game.SItem.menuStack;
import static legend.game.SItem.messageBox;
import static legend.game.SItem.setMessageBoxOptions;
import static legend.game.SItem.setMessageBoxText;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;

public class MessageBoxScreen extends MenuScreen {
  private final MessageBox20 messageBox = new MessageBox20();
  private final Consumer<MessageBoxResult> onResult;
  private MessageBoxResult result;
  /** Allows list wrapping, but only on new input */
  private boolean allowWrapY = true;

  public MessageBoxScreen(final String text, final int type, final Consumer<MessageBoxResult> onResult) {
    this(text, "Yes", "No", type, onResult);
  }

  public MessageBoxScreen(final String text, final String yes, final String no, final int type, final Consumer<MessageBoxResult> onResult) {
    setMessageBoxText(this.messageBox, text, type);
    setMessageBoxOptions(this.messageBox, yes, no);
    this.onResult = onResult;
  }

  @Override
  protected void render() {
    messageBox(this.messageBox);

    if(this.messageBox.state_0c == 0) {
      menuStack.popScreen();
      this.onResult.accept(this.result);
    }
  }

  @Override
  protected InputPropagation mouseMove(final int x, final int y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.messageBox.state_0c != 3) {
      return InputPropagation.PROPAGATE;
    }

    // Yes/no
    if(this.messageBox.type_15 == 2) {
      final int selectionY = this.messageBox.y_1e + 21 + this.messageBox.text_00.length * 12 / 2 - (this.messageBox.text_00.length - 1) * 3;

      if(this.messageBox.menuIndex_18 != 0 && MathHelper.inBox(x, y, this.messageBox.x_1c + 4, selectionY, 112, 14)) {
        playMenuSound(1);
        this.messageBox.menuIndex_18 = 0;

        if(this.messageBox.highlightRenderable_04 != null) {
          this.messageBox.highlightRenderable_04.y_44 = selectionY - 2;
        }
      } else if(this.messageBox.menuIndex_18 != 1 && MathHelper.inBox(x, y, this.messageBox.x_1c + 4, selectionY + 14, 112, 14)) {
        playMenuSound(1);
        this.messageBox.menuIndex_18 = 1;

        if(this.messageBox.highlightRenderable_04 != null) {
          this.messageBox.highlightRenderable_04.y_44 = selectionY + 12;
        }
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final Set<InputMod> mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.messageBox.state_0c != 3) {
      return InputPropagation.PROPAGATE;
    }

    if(this.messageBox.type_15 == 0) {
      playMenuSound(2);
      this.result = MessageBoxResult.YES;
      this.messageBox.state_0c = 4;
    } else if(this.messageBox.type_15 == 2) {
      // Yes/no
      final int selectionY = this.messageBox.y_1e + 21 + this.messageBox.text_00.length * 12 / 2 - (this.messageBox.text_00.length - 1) * 3;

      if(MathHelper.inBox(x, y, this.messageBox.x_1c + 4, selectionY, 112, 14)) {
        playMenuSound(2);
        this.messageBox.menuIndex_18 = 0;

        if(this.messageBox.highlightRenderable_04 != null) {
          this.messageBox.highlightRenderable_04.y_44 = selectionY - 2;
        }

        this.result = MessageBoxResult.YES;
        this.messageBox.state_0c = 4;
      } else if(MathHelper.inBox(x, y, this.messageBox.x_1c + 4, selectionY + 14, 112, 14)) {
        playMenuSound(2);
        this.messageBox.menuIndex_18 = 1;

        if(this.messageBox.highlightRenderable_04 != null) {
          this.messageBox.highlightRenderable_04.y_44 = selectionY + 12;
        }

        this.result = MessageBoxResult.NO;
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
      this.messageBox.highlightRenderable_04.y_44 = this.messageBox.menuIndex_18 == 0 ? selectionY - 2 : selectionY + 12;
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
      this.messageBox.highlightRenderable_04.y_44 = this.messageBox.menuIndex_18 == 0 ? selectionY - 2 : selectionY + 12;
    }
  }

  private void menuSelect() {
    playMenuSound(2);

    if(this.messageBox.menuIndex_18 == 0) {
      this.result = MessageBoxResult.YES;
    } else {
      this.result = MessageBoxResult.NO;
    }

    this.messageBox.state_0c = 4;
  }

  private void menuCancel() {
    playMenuSound(3);

    this.result = MessageBoxResult.CANCEL;

    this.messageBox.state_0c = 4;
  }

  private boolean skipInput() {
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

    if(this.messageBox.type_15 == 0) {
      playMenuSound(2);
      this.result = MessageBoxResult.YES;
      this.messageBox.state_0c = 4;
      return InputPropagation.HANDLED;
    }

    if(this.messageBox.type_15 == 2) {
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
