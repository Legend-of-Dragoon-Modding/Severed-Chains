package legend.game.inventory.screens.controls;

import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.gte.MV;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputKey;
import legend.core.platform.input.InputMod;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.InputPropagation;
import legend.game.inventory.screens.TextColour;

import java.util.Set;

import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_8002.charWidth;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.textHeight;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_TEXTBOX_CONFIRM;

public class Textbox extends Control {
  private final Panel background;
  private final MV transforms = new MV();
  private String text;
  private float textHeight;
  private int maxLength = -1;
  private final FontOptions fontOptions = new FontOptions().colour(TextColour.BROWN).shadowColour(TextColour.MIDDLE_BROWN);

  private int caretIndex;
  private int caretX;

  public Textbox() {
    this.background = this.addControl(Panel.subtle());
    this.background.ignoreInput();
    this.setText("");
  }

  @Override
  protected void gotFocus() {
    super.gotFocus();
    RENDERER.window().startTextInput();
  }

  @Override
  protected void lostFocus() {
    super.lostFocus();
    RENDERER.window().stopTextInput();
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

  @Override
  public void setScale(final float scale) {
    super.setScale(scale);
    this.fontOptions.size(scale);
    this.updateTextSize();
  }

  public void setCaretIndex(final int index) {
    this.caretIndex = MathHelper.clamp(index, 0, this.text.length());
    this.caretX = this.calculateCaretX(this.caretIndex);
  }

  private int calculateCaretX(final int index) {
    return (int)(textWidth(this.getText().substring(0, index)) * this.getScale());
  }

  private void updateText(final String text) {
    this.text = text;
    this.updateTextSize();
  }

  private void updateTextSize() {
    this.textHeight = textHeight(this.text) * this.getScale();
    this.setCaretIndex(this.caretIndex);
  }

  @Override
  protected void render(final int x, final int y) {
    final int oldZ = textZ_800bdf00;
    textZ_800bdf00 = this.getZ() - 1;
    renderText(this.text, x + 4, y + (this.getHeight() - this.textHeight) / 2 + 1, this.fontOptions);
    textZ_800bdf00 = oldZ;

    if(this.hasFocus()) {
      final int caretX = x + 4 + this.caretX;
      final int caretY = y + 3;

      this.transforms.scaling(1.0f, this.getHeight() - 5.0f, 1.0f);
      this.transforms.transfer.set(caretX, caretY, this.getZ() - 2.0f);
      RENDERER
        .queueOrthoModel(RENDERER.opaqueQuad, this.transforms, QueuedModelStandard.class)
        .colour(0xa0 / 255.0f, 0x80 / 255.0f, 0x50 / 255.0f);
    }
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final Set<InputMod> mods) {
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

    this.deferAction(this::focus);

    return InputPropagation.HANDLED;
  }

  @Override
  protected InputPropagation keyPress(final InputKey key, final InputKey scancode, final Set<InputMod> mods, final boolean repeat) {
    if(super.keyPress(key, scancode, mods, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(key == InputKey.BACKSPACE && this.caretIndex > 0) {
      final int caretIndex = this.caretIndex;
      this.updateText(this.text.substring(0, this.caretIndex - 1) + this.text.substring(this.caretIndex));
      this.setCaretIndex(caretIndex - 1);
      this.fireChangedEvent();
    }

    if(key == InputKey.DELETE && this.caretIndex < this.text.length()) {
      this.updateText(this.text.substring(0, this.caretIndex) + this.text.substring(this.caretIndex + 1));
      this.fireChangedEvent();
    }

    if(key == InputKey.LEFT && mods.isEmpty()) {
      this.setCaretIndex(this.caretIndex - 1);
    }

    if(key == InputKey.RIGHT && mods.isEmpty()) {
      this.setCaretIndex(this.caretIndex + 1);
    }

    if(key == InputKey.UP && mods.isEmpty()) {
      this.setCaretIndex(0);
    }

    if(key == InputKey.DOWN && mods.isEmpty()) {
      this.setCaretIndex(this.text.length());
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
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if((action == INPUT_ACTION_MENU_TEXTBOX_CONFIRM.get() || action == INPUT_ACTION_MENU_BACK.get()) && !repeat) {
      this.deferAction(this::unfocus);
    }

    return InputPropagation.HANDLED;
  }

  @Override
  protected InputPropagation inputActionReleased(final InputAction action) {
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
