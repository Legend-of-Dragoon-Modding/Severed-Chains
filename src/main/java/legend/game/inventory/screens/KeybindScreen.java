package legend.game.inventory.screens;

import legend.core.lang.I18nText;
import legend.core.platform.input.AxisInputActivation;
import legend.core.platform.input.ButtonInputActivation;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputActivation;
import legend.core.platform.input.InputBindings;
import legend.core.platform.input.KeyInputActivation;
import legend.core.platform.input.ScancodeInputActivation;
import legend.game.types.MessageBoxResult;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static legend.game.Text.renderText;
import static legend.game.Text.textZ_800bdf00;

public class KeybindScreen extends InputBoxScreen {
  private static final long TIMEOUT = 4_000_000_000L;

  private final FontOptions fontOptions = new FontOptions().colour(TextColour.BROWN).shadowColour(TextColour.MIDDLE_BROWN);

  private final Function<List<InputActivation>, String> actionToString;

  private final List<InputActivation> activations;

  private long timeout;

  public KeybindScreen(final InputAction inputAction, final Function<List<InputActivation>, String> actionToString, final BiConsumer<MessageBoxResult, List<InputActivation>> onResult) {
    final List<InputActivation> activations = InputBindings.getActivationsForAction(inputAction);
    this.activations = activations;

    super(new I18nText(inputAction), actionToString.apply(InputBindings.getActivationsForAction(inputAction)), (result, text) -> onResult.accept(result, activations), 33);

    this.actionToString = actionToString;

    // Ignore text input in textbox
    this.text.onCharPress(codepoint -> InputPropagation.HANDLED);

    this.text.onGotFocus(() -> {
      // Clear activations when the textbox is focused
      this.activations.clear();
      this.text.setText("");

      // Start timeout countdown
      this.timeout = System.nanoTime() + TIMEOUT;
    });

    this.text.onLostFocus(() -> this.timeout = 0);

    // Handle keyboard input
    this.text.onKeyPress((key, scancode, mods, repeat) -> {
      if(!repeat) {
        final InputActivation activation = scancode != null ? new ScancodeInputActivation(scancode) : new KeyInputActivation(key);
        this.removeSimilarActivations(activation);
        this.activations.add(activation);
        this.updateText();
      }

      return InputPropagation.HANDLED;
    });

    // Handle gamepad input
    this.text.onButtonPress((button, repeat) -> {
      if(!repeat) {
        final InputActivation activation = new ButtonInputActivation(button);
        this.removeSimilarActivations(activation);
        this.activations.add(activation);
        this.updateText();
      }

      return InputPropagation.HANDLED;
    });

    // Handle gamepad axes
    this.text.onAxis((axis, direction, menuValue, movementValue) -> {
      if(menuValue > 0.0f) {
        final InputActivation activation = new AxisInputActivation(axis, direction);
        this.removeSimilarActivations(activation);
        this.activations.add(activation);
        this.updateText();
      }

      return InputPropagation.HANDLED;
    });
  }

  private void updateText() {
    this.text.setText(this.actionToString.apply(this.activations));
  }

  private void removeSimilarActivations(final InputActivation activation) {
    this.activations.removeIf(activation::isSimilar);
  }

  @Override
  protected void render() {
    if(this.timeout != 0) {
      final long time = System.nanoTime();
      final int remaining = (int)Math.max(0, Math.ceilDiv(this.timeout - time, 1_000_000_000L));

      if(remaining != 0) {
        final String str = Integer.toString(remaining);

        final int oldZ = textZ_800bdf00;
        textZ_800bdf00 = this.text.getZ() - 2;
        renderText(this.text.getFont(), str, this.text.calculateTotalX() + this.text.getWidth() - this.text.getFont().textWidth(str) - 4, this.text.calculateTotalY() + (this.text.getHeight() - this.text.getFont().textHeight(str)) / 2.0f, this.fontOptions);
        textZ_800bdf00 = oldZ;
      } else {
        this.text.unfocus();
        this.timeout = 0;

        if(!this.textboxWasClicked()) {
          this.menuNavigateDown();
        }
      }
    }
  }
}
