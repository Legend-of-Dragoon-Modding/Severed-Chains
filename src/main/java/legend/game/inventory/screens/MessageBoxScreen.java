package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.input.InputAction;
import legend.game.types.MessageBox20;
import legend.game.types.MessageBoxResult;

import java.util.function.Consumer;

import static legend.game.SItem.menuStack;
import static legend.game.SItem.messageBox;
import static legend.game.SItem.setMessageBoxOptions;
import static legend.game.SItem.setMessageBoxText;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;

public class MessageBoxScreen extends MenuScreen {
  private final MessageBox20 messageBox = new MessageBox20();
  private final Consumer<MessageBoxResult> onResult;
  private MessageBoxResult result;

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
      final int selectionY = this.messageBox.y_1e + 7 + this.messageBox.text_00.length * 14 + 7;

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
  protected InputPropagation mouseClick(final int x, final int y, final int button, final int mods) {
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
      final int selectionY = this.messageBox.y_1e + 7 + this.messageBox.text_00.length * 14 + 7;

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
    playMenuSound(1);
    this.messageBox.menuIndex_18 = 0;

    final int selectionY = this.messageBox.y_1e + 7 + this.messageBox.text_00.length * 14 + 7;
    if(this.messageBox.highlightRenderable_04 != null) {
      this.messageBox.highlightRenderable_04.y_44 = selectionY - 2;
    }
  }

  private void menuNavigateDown() {
    playMenuSound(1);
    this.messageBox.menuIndex_18 = 1;

    final int selectionY = this.messageBox.y_1e + 7 + this.messageBox.text_00.length * 14 + 7;
    if(this.messageBox.highlightRenderable_04 != null) {
      this.messageBox.highlightRenderable_04.y_44 = selectionY + 12;
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
    if(this.messageBox.state_0c == 5 || this.messageBox.state_0c == 4) {
      return true;
    }

    if(this.messageBox.type_15 == 0) {
      playMenuSound(2);
      this.result = MessageBoxResult.YES;
      this.messageBox.state_0c = 4;
      return true;
    }

    return this.messageBox.state_0c != 3 || this.messageBox.type_15 != 2;
  }

  @Override
  protected boolean propagateRender() {
    return true;
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.skipInput()) {
      return InputPropagation.PROPAGATE;
    }

    if(inputAction == InputAction.DPAD_UP || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_UP) {
      this.menuNavigateUp();
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.DPAD_DOWN || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_DOWN) {
      this.menuNavigateDown();
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_SOUTH) {
      this.menuSelect();
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_EAST) {
      this.menuCancel();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
