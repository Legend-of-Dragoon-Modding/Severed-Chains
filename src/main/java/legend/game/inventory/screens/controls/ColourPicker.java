package legend.game.inventory.screens.controls;

import legend.core.renderer.QueuedModelStandard;
import legend.core.font.Font;
import legend.core.gte.MV;
import legend.core.lang.I18nText;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.InputPropagation;
import org.joml.Vector3i;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static legend.core.GameEngine.DEFAULT_FONT;
import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.RENDERER;
import static legend.game.sound.Audio.playMenuSound;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_LEFT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_RIGHT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;

public class ColourPicker extends Control {
  private static final int PREVIEW_CONTROL = 0;
  private static final int FIRST_COLOUR_CONTROL = 1;
  private static final int RANDOM_CONTROL = 4;
  private static final int RANDOM_BUTTON_WIDTH = 32;
  private static final int COLOUR_SPINNER_ARROW_PADDING = 12;
  private static final float UI_ACTION_CONTROL_SCALE = 0.9f;

  private Font font = DEFAULT_FONT;
  private final NumberSpinner<Integer>[] colours = new NumberSpinner[3];
  private final boolean enableUiActions;
  private final Brackets previewHighlight;
  private final Button randomButton;

  private int selectedColour;
  private int selectedControl = FIRST_COLOUR_CONTROL;
  private boolean controlSelectionActive;

  private final MV transforms = new MV();
  private boolean triggerEvents = true;

  public ColourPicker() {
    this(false);
  }

  public ColourPicker(final boolean enableUiActions) {
    this.enableUiActions = enableUiActions;

    if(enableUiActions) {
      this.previewHighlight = this.addControl(new Brackets());
      this.previewHighlight.hide();

      this.randomButton = this.addControl(new Button(new I18nText("lod_core.ui.options.colour_random")));
      this.randomButton.onPressed(this::randomize);
    } else {
      this.previewHighlight = null;
      this.randomButton = null;
    }

    for(int i = 0; i < this.colours.length; i++) {
      this.colours[i] = this.addControl(NumberSpinner.intSpinner(0, 0, 255));
      this.colours[i].setFont(this.getFont());
      if(enableUiActions) {
        this.colours[i].setLabelRightPadding(COLOUR_SPINNER_ARROW_PADDING);
      }
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

    if(this.randomButton != null) {
      this.randomButton.setFont(font);
    }

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

    final float controlScale = this.enableUiActions ? scale * UI_ACTION_CONTROL_SCALE : scale;

    if(this.randomButton != null) {
      this.randomButton.setScale(controlScale);
    }

    for(int i = 0; i < this.colours.length; i++) {
      this.colours[i].setScale(controlScale);
    }
  }

  @Override
  public void setZ(final int z) {
    super.setZ(z);

    if(this.previewHighlight != null) {
      this.previewHighlight.setZ(z - 1);
    }

    if(this.randomButton != null) {
      this.randomButton.setZ(z);
    }

    for(int i = 0; i < this.colours.length; i++) {
      this.colours[i].setZ(z);
    }
  }

  @Override
  protected void onResize() {
    super.onResize();

    final int previewSize = this.getPreviewSize();
    final int randomButtonWidth = this.enableUiActions ? RANDOM_BUTTON_WIDTH : 0;
    final int controlSize = (this.getWidth() - previewSize - 4 - randomButtonWidth) / this.colours.length;

    if(this.previewHighlight != null) {
      this.previewHighlight.setSize(previewSize + 4, this.getHeight());
    }

    for(int i = 0; i < this.colours.length; i++) {
      this.colours[i].setSize(controlSize, this.getHeight());
    }

    this.colours[0].setX(previewSize + 4);

    for(int i = 1; i < this.colours.length; i++) {
      this.colours[i].setX(this.colours[i - 1].getX() + this.colours[i - 1].getWidth());
    }

    if(this.randomButton != null) {
      final int randomButtonX = this.colours[this.colours.length - 1].getX() + this.colours[this.colours.length - 1].getWidth();
      this.randomButton.setPos(randomButtonX, 0);
      this.randomButton.setSize(this.getWidth() - randomButtonX, this.getHeight());
    }
  }

  @Override
  protected void render(final int x, final int y) {
    this.transforms.scaling(this.getPreviewSize(), this.getPreviewSize(), 1.0f);
    this.transforms.transfer.set(x + 2.0f, y + 2.0f, this.getZ() * 4.0f);
    RENDERER.queueOrthoModel(RENDERER.opaqueQuad, this.transforms, QueuedModelStandard.class)
      .colour(this.getR() / 255.0f, this.getG() / 255.0f, this.getB() / 255.0f);
  }

  @Override
  protected InputPropagation mouseClick(final double x, final double y, final int button, final Set<InputMod> mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.enableUiActions && button == PLATFORM.getMouseButton(0) && mods.isEmpty() && x < this.getPreviewSize() + 4) {
      playMenuSound(2);
      this.openPreview();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.enableUiActions) {
      return this.uiInputActionPressed(action, repeat);
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

  private InputPropagation uiInputActionPressed(final InputAction action, final boolean repeat) {
    if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
      playMenuSound(3);
      this.unfocus();
      return InputPropagation.HANDLED;
    }

    if(this.controlSelectionActive) {
      if(action == INPUT_ACTION_MENU_LEFT.get()) {
        playMenuSound(1);
        this.moveControlSelection(-1);
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_RIGHT.get()) {
        playMenuSound(1);
        this.moveControlSelection(1);
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_UP.get() || action == INPUT_ACTION_MENU_DOWN.get()) {
        if(this.isColourControlSelected()) {
          return this.colours[this.selectedControl - FIRST_COLOUR_CONTROL].inputActionPressed(action, repeat);
        }

        return InputPropagation.HANDLED;
      }
    }

    if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
      if(!this.controlSelectionActive) {
        playMenuSound(2);
        this.controlSelectionActive = true;
        this.showControlSelection();
      } else if(this.selectedControl == PREVIEW_CONTROL) {
        playMenuSound(2);
        this.openPreview();
      } else if(this.selectedControl == RANDOM_CONTROL) {
        this.randomButton.press();
      } else {
        playMenuSound(2);
        this.hideControlSelection();
        this.controlSelectionActive = false;
      }

      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  private void moveControlSelection(final int amount) {
    this.hideControlSelection();
    this.selectedControl = Math.floorMod(this.selectedControl + amount, RANDOM_CONTROL + 1);
    this.showControlSelection();
  }

  private boolean isColourControlSelected() {
    return this.selectedControl >= FIRST_COLOUR_CONTROL && this.selectedControl < RANDOM_CONTROL;
  }

  private void showControlSelection() {
    if(this.selectedControl == PREVIEW_CONTROL) {
      this.previewHighlight.show();
    } else if(this.selectedControl == RANDOM_CONTROL) {
      this.randomButton.hoverIn();
    } else {
      this.colours[this.selectedControl - FIRST_COLOUR_CONTROL].showHighlight();
    }
  }

  private void hideControlSelection() {
    if(this.previewHighlight != null) {
      this.previewHighlight.hide();
    }

    if(this.randomButton != null) {
      this.randomButton.hoverOut();
    }

    for(int i = 0; i < this.colours.length; i++) {
      this.colours[i].hideHighlight();
    }
  }

  private void randomize() {
    final ThreadLocalRandom random = ThreadLocalRandom.current();
    this.setColour(random.nextInt(256), random.nextInt(256), random.nextInt(256));
  }

  private void openPreview() {
    this.deferAction(() -> {
      if(this.getScreen().findControl(SubmapTextboxColourPreview.class, preview -> true).isPresent()) return;

      final SubmapTextboxColourPreview preview = new SubmapTextboxColourPreview();
      preview.setZ(1);
      preview.setSize(this.getScreen().getWidth(), this.getScreen().getHeight());
      this.getScreen().addControl(preview);
      this.getScreen().setFocus(preview);
    });
  }

  @Override
  protected void lostFocus() {
    super.lostFocus();

    if(this.enableUiActions) {
      this.hideControlSelection();
      this.controlSelectionActive = false;
    }

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
