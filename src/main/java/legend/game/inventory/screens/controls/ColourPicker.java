package legend.game.inventory.screens.controls;

import legend.core.QueuedModelStandard;
import legend.core.font.Font;
import legend.core.gte.MV;
import legend.core.platform.input.InputAction;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.InputPropagation;
import org.joml.Vector3i;

import static legend.core.GameEngine.DEFAULT_FONT;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Audio.playMenuSound;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_LEFT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_RIGHT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;

public class ColourPicker extends Control {
  private Font font = DEFAULT_FONT;
  private final NumberSpinner<Integer>[] colours = new NumberSpinner[3];
  private int selectedColour;

  private final MV transforms = new MV();
  private boolean triggerEvents = true;

  public ColourPicker() {
    for(int i = 0; i < this.colours.length; i++) {
      this.colours[i] = this.addControl(NumberSpinner.intSpinner(0, 0, 255));
      this.colours[i].setFont(this.getFont());
      this.colours[i].onChange(val -> this.triggerChangeEvent());
    }
  }

  private void triggerChangeEvent() {
    if(this.triggerEvents && this.changeHandler != null) {
      this.changeHandler.change(this.getR(), this.getG(), this.getB());
    }
  }

  public void setFont(final Font font) {
    this.font = font;

    for(int i = 0; i < this.colours.length; i++) {
      this.colours[i].setFont(font);
    }
  }

  public Font getFont() {
    return this.font;
  }

  public void setColour(final int r, final int g, final int b) {
    this.triggerEvents = false;
    this.colours[0].setNumber(r);
    this.colours[1].setNumber(g);
    this.colours[2].setNumber(b);
    this.triggerEvents = true;
    this.triggerChangeEvent();
  }

  public void setColour(final Vector3i colour) {
    this.setColour(colour.x, colour.y, colour.z);
  }

  public int getR() {
    return this.colours[0].getNumber();
  }

  public int getG() {
    return this.colours[1].getNumber();
  }

  public int getB() {
    return this.colours[2].getNumber();
  }

  private int getPreviewSize() {
    return this.getHeight() - 4;
  }

  @Override
  public void setScale(final float scale) {
    super.setScale(scale);

    for(int i = 0; i < this.colours.length; i++) {
      this.colours[i].setScale(scale);
    }
  }

  @Override
  protected void onResize() {
    super.onResize();

    final int previewSize = this.getPreviewSize();
    final int controlSize = (this.getWidth() - previewSize - 4) / this.colours.length;

    for(int i = 0; i < this.colours.length; i++) {
      this.colours[i].setSize(controlSize, this.getHeight());
    }

    this.colours[0].setX(previewSize + 4);

    for(int i = 1; i < this.colours.length; i++) {
      this.colours[i].setX(this.colours[i - 1].getX() + this.colours[i - 1].getWidth());
    }
  }

  @Override
  protected void render(final int x, final int y) {
    this.transforms.scaling(this.getPreviewSize(), this.getPreviewSize(), 1.0f);
    this.transforms.transfer.set(x + 2.0f, y + 2.0f, this.getZ());
    RENDERER.queueOrthoModel(RENDERER.opaqueQuad, this.transforms, QueuedModelStandard.class)
      .colour(this.getR() / 255.0f, this.getG() / 255.0f, this.getB() / 255.0f);
  }

  @Override
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.colours[this.selectedColour].isHighlightVisible()) {
      if(action == INPUT_ACTION_MENU_LEFT.get()) {
        playMenuSound(1);
        this.colours[this.selectedColour].hideHighlight();
        this.selectedColour = Math.floorMod(this.selectedColour - 1, this.colours.length);
        this.colours[this.selectedColour].showHighlight();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_RIGHT.get()) {
        playMenuSound(1);
        this.colours[this.selectedColour].hideHighlight();
        this.selectedColour = Math.floorMod(this.selectedColour + 1, this.colours.length);
        this.colours[this.selectedColour].showHighlight();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_UP.get() || action == INPUT_ACTION_MENU_DOWN.get()) {
        return this.colours[this.selectedColour].inputActionPressed(action, repeat);
      }
    }

    if(!repeat) {
      if(action == INPUT_ACTION_MENU_CONFIRM.get()) {
        if(this.colours[this.selectedColour].isHighlightVisible()) {
          this.colours[this.selectedColour].hideHighlight();
        } else {
          this.colours[this.selectedColour].showHighlight();
        }

        playMenuSound(2);
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected void lostFocus() {
    super.lostFocus();

    for(int i = 0; i < this.colours.length; i++) {
      this.colours[i].unfocus();
    }
  }

  public void onChange(final Change change) {
    this.changeHandler = change;
  }

  private Change changeHandler;

  @FunctionalInterface public interface Change { void change(final int r, final int g, final int b); }
}
