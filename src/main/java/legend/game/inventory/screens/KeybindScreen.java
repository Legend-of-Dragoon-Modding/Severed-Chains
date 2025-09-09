package legend.game.inventory.screens;

import legend.core.platform.input.AxisInputActivation;
import legend.core.platform.input.ButtonInputActivation;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputActivation;
import legend.core.platform.input.InputBindings;
import legend.core.platform.input.ScancodeInputActivation;
import legend.game.i18n.I18n;
import legend.game.types.MessageBoxResult;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.textHeight;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;

public class KeybindScreen extends InputBoxScreen {
  private static final long TIMEOUT = 4_000_000_000L;

  // Stupid hack for not being able to reference this before super completes
  private static KeybindScreen inst;

  private final FontOptions fontOptions = new FontOptions().colour(TextColour.BROWN).shadowColour(TextColour.MIDDLE_BROWN);

  private final Function<List<InputActivation>, String> actionToString;
  private final BiConsumer<MessageBoxResult, List<InputActivation>> onResult;

  private final List<InputActivation> activations;

  private long timeout;

  public KeybindScreen(final InputAction inputAction, final Function<List<InputActivation>, String> actionToString, final BiConsumer<MessageBoxResult, List<InputActivation>> onResult) {
    super(I18n.translate(inputAction), actionToString.apply(InputBindings.getActivationsForAction(inputAction)), (result, text) -> inst.onClose(result), 33);

    if(inst != null) {
      throw new IllegalStateException("Keybind screen already open");
    }

    inst = this;

    this.actionToString = actionToString;
    this.onResult = onResult;

    this.activations = InputBindings.getActivationsForAction(inputAction);

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
        final InputActivation activation = new ScancodeInputActivation(scancode);
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

  private void onClose(final MessageBoxResult result) {
    inst = null;
    this.onResult.accept(result, this.activations);
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
        renderText(str, this.text.calculateTotalX() + this.text.getWidth() - textWidth(str) - 4, this.text.calculateTotalY() + (this.text.getHeight() - textHeight(str)) / 2.0f, this.fontOptions);
        textZ_800bdf00 = oldZ;
      } else {
        this.text.unfocus();
        this.timeout = 0;
        this.menuNavigateDown();
      }
    }
  }
}
