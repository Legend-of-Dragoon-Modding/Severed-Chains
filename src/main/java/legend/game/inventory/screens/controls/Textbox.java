package legend.game.inventory.screens.controls;

import legend.core.MathHelper;
import legend.core.gte.MV;
import legend.game.input.InputAction;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.InputPropagation;
import legend.game.inventory.screens.TextColour;
import legend.game.types.LodString;

import static legend.core.GameEngine.RENDERER;
import static legend.game.SItem.renderText;
import static legend.game.Scus94491BpeSegment_8002.charWidth;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DELETE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

public class Textbox extends Control {
  private final Panel background;
  private final MV transforms = new MV();
  private String text;
  private int maxLength = -1;

  private int caretIndex;
  private int caretX;

  public Textbox() {
    this.background = this.addControl(Panel.subtle());
    this.background.ignoreInput();
    this.setText("");
  }

  @Override
  public void setZ(final int z) {
    super.setZ(z);
    this.background.setZ(z + 1);
  }

  public void setMaxLength(final int maxLength) {
    this.maxLength = maxLength;
  }

  public int getMaxLength() {
    return this.maxLength;
  }

  @Override
  protected void onResize() {
    super.onResize();
    this.background.setSize(this.getWidth(), this.getHeight());
  }

  public void setText(final String text) {
    this.updateText(text);
    this.setCaretIndex(text.length());
  }

  public String getText() {
    return this.text;
  }

  public void setCaretIndex(final int index) {
    this.caretIndex = MathHelper.clamp(index, 0, this.text.length());
    this.caretX = this.calculateCaretX(this.caretIndex);
  }

  private int calculateCaretX(final int index) {
    return textWidth(this.getText().substring(0, index));
  }

  private void updateText(final String text) {
    this.text = text;
  }

  @Override
  protected void render(final int x, final int y) {
    final int oldZ = textZ_800bdf00;
    textZ_800bdf00 = this.getZ() - 1;
    renderText(new LodString(this.text), x + 4, y + (this.getHeight() - 11) / 2 + 1, TextColour.BROWN);
    textZ_800bdf00 = oldZ;

    if(this.hasFocus()) {
      final int caretX = x + 4 + this.caretX;
      final int caretY = y + 3;

      this.transforms.scaling(1.0f, this.getHeight() - 5.0f, 1.0f);
      this.transforms.transfer.set(caretX, caretY, this.getZ() - 1.0f);
      RENDERER
        .queueOrthoModel(RENDERER.opaqueQuad, this.transforms)
        .colour(0xa0 / 255.0f, 0x80 / 255.0f, 0x50 / 255.0f);
    }
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final int mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    for(int i = this.text.length(); i >= 0; i--) {
      final int nudge = i < this.text.length() ? charWidth(this.text.charAt(i)) / 2 : 0;

      if(this.calculateCaretX(i) - nudge + 4 < x) {
        this.setCaretIndex(i);
        break;
      }
    }

    return InputPropagation.HANDLED;
  }

  @Override
  protected InputPropagation keyPress(final int key, final int scancode, final int mods) {
    if(super.keyPress(key, scancode, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(key == GLFW_KEY_LEFT) {
      if(mods == 0) {
        this.setCaretIndex(this.caretIndex - 1);
      }
    }

    if(key == GLFW_KEY_RIGHT) {
      if(mods == 0) {
        this.setCaretIndex(this.caretIndex + 1);
      }
    }

    if(key == GLFW_KEY_UP) {
      if(mods == 0) {
        this.setCaretIndex(0);
      }
    }

    if(key == GLFW_KEY_DOWN) {
      if(mods == 0) {
        this.setCaretIndex(this.text.length());
      }
    }

    if(key == GLFW_KEY_BACKSPACE && this.caretIndex > 0) {
      this.updateText(this.text.substring(0, this.caretIndex - 1) + this.text.substring(this.caretIndex));
      this.setCaretIndex(this.caretIndex - 1);
      this.fireChangedEvent();
    }

    if(key == GLFW_KEY_DELETE && this.caretIndex < this.text.length()) {
      this.updateText(this.text.substring(0, this.caretIndex) + this.text.substring(this.caretIndex + 1));
      this.fireChangedEvent();
    }

    if(key == GLFW_KEY_ESCAPE) {
      this.deferAction(this::unfocus);
    }

    return InputPropagation.HANDLED;
  }

  @Override
  protected InputPropagation charPress(final int codepoint) {
    if(super.charPress(codepoint) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.maxLength != -1 && this.text.length() >= this.maxLength) {
      return InputPropagation.HANDLED;
    }

    if(codepoint >= 32 && codepoint < 127) {
      this.updateText(this.text.substring(0, this.caretIndex) + (char)codepoint + this.text.substring(this.caretIndex));
      this.setCaretIndex(this.caretIndex + 1);
      this.fireChangedEvent();
    }

    return InputPropagation.HANDLED;
  }

  @Override
  protected InputPropagation pressedThisFrame(final InputAction inputAction) {
    return InputPropagation.HANDLED;
  }

  @Override
  protected InputPropagation pressedWithRepeatPulse(final InputAction inputAction) {
    return InputPropagation.HANDLED;
  }

  public void onChanged(final Changed handler) {
    this.changedHandler = handler;
  }

  private void fireChangedEvent() {
    if(this.changedHandler != null) {
      this.changedHandler.changed(this.text);
    }
  }

  private Changed changedHandler;

  @FunctionalInterface public interface Changed { void changed(final String text); }
}
