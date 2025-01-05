package legend.game.inventory.screens;

import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Brackets;
import legend.game.inventory.screens.controls.Button;
import legend.game.inventory.screens.controls.Label;
import legend.game.inventory.screens.controls.Panel;
import legend.game.inventory.screens.controls.Textbox;
import legend.game.types.MessageBoxResult;

import java.util.function.BiConsumer;

import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class InputBoxScreen extends MenuScreen {
  private final BiConsumer<MessageBoxResult, String> onResult;

  private final Brackets highlight;
  private final Textbox text;
  private final Button accept;
  private final Button cancel;
  private int selectedIndex;

  public InputBoxScreen(final String message, final String defaultText, final BiConsumer<MessageBoxResult, String> onResult) {
    final Panel panel = this.addControl(Panel.panel());
    panel.setPos(75, 75);
    panel.setSize(215, 90);
    panel.setZ(32);

    this.onResult = onResult;

    final Label label = panel.addControl(new Label(message));
    label.setAutoSize(true);
    label.getFontOptions().horizontalAlign(HorizontalAlign.CENTRE);
    label.setPos((panel.getWidth() - label.getWidth()) / 2, 12);
    label.setZ(31);

    this.text = panel.addControl(new Textbox());
    this.text.setSize(165, 16);
    this.text.setPos(25, 28);
    this.text.setZ(1);
    this.text.setText(defaultText);
    this.text.setMaxLength(15);

    this.text.onKeyPress((key, scancode, mods) -> {
      if(key == GLFW_KEY_ESCAPE || key == GLFW_KEY_ENTER) {
        this.deferAction(() -> this.setFocus(null));
        return InputPropagation.HANDLED;
      }

      return InputPropagation.PROPAGATE;
    });

    this.accept = panel.addControl(new Button("Accept"));
    this.accept.setSize(112, 14);
    this.accept.setPos((panel.getWidth() - this.accept.getWidth()) / 2, this.text.getY() + this.text.getHeight() + 4);
    this.accept.setZ(31);
    this.accept.onPressed(() -> {
      menuStack.popScreen();
      this.onResult.accept(MessageBoxResult.YES, this.text.getText().strip());
    });

    this.cancel = panel.addControl(new Button("Cancel"));
    this.cancel.setSize(112, 14);
    this.cancel.setPos((panel.getWidth() - this.cancel.getWidth()) / 2, this.accept.getY() + this.accept.getHeight() + 2);
    this.cancel.setZ(31);
    this.cancel.onPressed(this::menuCancel);

    this.highlight = panel.addControl(new Brackets());
    this.highlight.setPos(this.text.getX() - 4, this.text.getY() - 4);
    this.highlight.setSize(this.text.getWidth() + 8, this.text.getHeight() + 8);
    this.highlight.setClut(0xfc29);
    this.highlight.setZ(31);
    this.highlight.setVisibility(false);

    this.text.onHoverIn(this.highlight::show);
    this.text.onHoverOut(this.highlight::hide);

    this.selectedIndex = 1;
    this.getSelectedControl().hoverIn();
  }

  private Control getSelectedControl() {
    return switch(this.selectedIndex) {
      case 0 -> this.text;
      case 1 -> this.accept;
      case 2 -> this.cancel;
      default -> throw new IllegalStateException("Invalid control index " + this.selectedIndex);
    };
  }

  @Override
  protected void render() {

  }

  private void menuNavigateUp() {
    playMenuSound(1);

    this.getSelectedControl().hoverOut();

    this.selectedIndex--;
    this.selectedIndex = Math.floorMod(this.selectedIndex, 3);

    this.highlight.setVisibility(this.selectedIndex == 0);

    if(this.selectedIndex != 0) {
      this.getSelectedControl().hoverIn();
    }
  }

  private void menuNavigateDown() {
    playMenuSound(1);

    this.getSelectedControl().hoverOut();

    this.selectedIndex++;
    this.selectedIndex %= 3;

    this.highlight.setVisibility(this.selectedIndex == 0);

    if(this.selectedIndex != 0) {
      this.getSelectedControl().hoverIn();
    }
  }

  private void menuSelect() {
    if(this.selectedIndex == 0) {
      this.deferAction(this.text::focus);
    } else {
      this.deferAction(((Button)this.getSelectedControl())::press);
    }
  }

  private void menuCancel() {
    playMenuSound(3);

    menuStack.popScreen();
    this.onResult.accept(MessageBoxResult.CANCEL, this.text.getText());
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
