package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Textbox;
import legend.game.types.LodString;
import legend.game.types.MessageBox20;
import legend.game.types.MessageBoxResult;

import java.util.function.BiConsumer;

import static legend.game.SItem.menuStack;
import static legend.game.SItem.messageBox;
import static legend.game.SItem.setMessageBoxOptions;
import static legend.game.SItem.setMessageBoxText;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class InputBoxScreen extends MenuScreen {
  private final MessageBox20 messageBox = new MessageBox20();
  private final BiConsumer<MessageBoxResult, String> onResult;
  private MessageBoxResult result;

  private final Textbox text;

  public InputBoxScreen(final String message, final String defaultText, final int type, final BiConsumer<MessageBoxResult, String> onResult) {
    setMessageBoxText(this.messageBox, new LodString(message + '\n'), type);
    setMessageBoxOptions(this.messageBox, new LodString("Accept"), new LodString("Cancel"));
    this.onResult = onResult;

    this.text = this.addControl(new Textbox());
    this.text.setSize(165, 16);
    this.text.setPos(100, 124);
    this.text.setZ(1);
    this.text.setText(defaultText);
    this.text.setMaxLength(30);

    this.text.onGotFocus(() -> this.messageBox.ignoreInput = true);
    this.text.onLostFocus(() -> this.messageBox.ignoreInput = false);
    this.text.onKeyPress((key, scancode, mods) -> {
      if(key == GLFW_KEY_ESCAPE) {
        this.setFocus(null);
        return InputPropagation.HANDLED;
      }

      return InputPropagation.PROPAGATE;
    });

    this.setFocus(this.text);
  }

  @Override
  protected void render() {
    messageBox(this.messageBox);

    if(this.messageBox.state_0c == 0) {
      menuStack.popScreen();
      this.onResult.accept(this.result, this.text.getText());
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
        playSound(1);
        this.messageBox.menuIndex_18 = 0;

        if(this.messageBox.renderable_04 != null) {
          this.messageBox.renderable_04.y_44 = selectionY - 2;
        }
      } else if(this.messageBox.menuIndex_18 != 1 && MathHelper.inBox(x, y, this.messageBox.x_1c + 4, selectionY + 14, 112, 14)) {
        playSound(1);
        this.messageBox.menuIndex_18 = 1;

        if(this.messageBox.renderable_04 != null) {
          this.messageBox.renderable_04.y_44 = selectionY + 12;
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
      playSound(2);
      this.result = MessageBoxResult.YES;
      this.messageBox.state_0c = 4;
    } else if(this.messageBox.type_15 == 2) {
      // Yes/no
      final int selectionY = this.messageBox.y_1e + 7 + this.messageBox.text_00.length * 14 + 7;

      if(MathHelper.inBox(x, y, this.messageBox.x_1c + 4, selectionY, 112, 14)) {
        playSound(2);
        this.messageBox.menuIndex_18 = 0;

        if(this.messageBox.renderable_04 != null) {
          this.messageBox.renderable_04.y_44 = selectionY - 2;
        }

        this.result = MessageBoxResult.YES;
        this.messageBox.state_0c = 4;
      } else if(MathHelper.inBox(x, y, this.messageBox.x_1c + 4, selectionY + 14, 112, 14)) {
        playSound(2);
        this.messageBox.menuIndex_18 = 1;

        if(this.messageBox.renderable_04 != null) {
          this.messageBox.renderable_04.y_44 = selectionY + 12;
        }

        this.result = MessageBoxResult.NO;
        this.messageBox.state_0c = 4;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  private void menuNavigateUp() {
    playSound(1);
    this.messageBox.menuIndex_18 = 0;

    final int selectionY = this.messageBox.y_1e + 7 + this.messageBox.text_00.length * 14 + 7;
    if(this.messageBox.renderable_04 != null) {
      this.messageBox.renderable_04.y_44 = selectionY - 2;
    }
  }

  private void menuNavigateDown() {
    playSound(1);
    this.messageBox.menuIndex_18 = 1;

    final int selectionY = this.messageBox.y_1e + 7 + this.messageBox.text_00.length * 14 + 7;
    if(this.messageBox.renderable_04 != null) {
      this.messageBox.renderable_04.y_44 = selectionY + 12;
    }
  }

  private void menuSelect() {
    playSound(2);

    if(this.messageBox.menuIndex_18 == 0) {
      this.result = MessageBoxResult.YES;
    } else {
      this.result = MessageBoxResult.NO;
    }

    this.messageBox.state_0c = 4;
  }

  private void menuCancel() {
    playSound(3);

    this.result = MessageBoxResult.CANCEL;

    this.messageBox.state_0c = 4;
  }

  private boolean skipInput() {
    if(this.messageBox.type_15 == 0) {
      playSound(2);
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
