package legend.game.inventory.screens.controls;

import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.InputPropagation;
import legend.game.inventory.screens.TextColour;
import legend.game.types.Textbox4c;
import legend.game.types.TextboxState;

import java.util.Set;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.PLATFORM;
import static legend.game.Text.initTextbox;
import static legend.game.Text.renderText;
import static legend.game.Text.renderTextboxBackground;
import static legend.game.Text.renderTextboxSelection;
import static legend.game.Text.textZ_800bdf00;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;
import static legend.game.sound.Audio.playMenuSound;
import static legend.lodmod.LodConfig.UI_BACKGROUND_COLOUR;

public class SubmapTextboxColourPreview extends Control {
  private static final int TEXTBOX_CHARS = 27;
  private static final int TEXTBOX_LINES = 4;
  private static final int FIRST_SELECTION_LINE = 1;
  private static final int LAST_SELECTION_LINE = 2;

  private final Textbox4c textbox = new Textbox4c();
  private final FontOptions fontOptions = new FontOptions().colour(TextColour.WHITE).noShadow();

  private int selectionLine = FIRST_SELECTION_LINE;
  private boolean closing;

  public SubmapTextboxColourPreview() {
    this.textbox.clear();
    initTextbox(this.textbox, false, 0.0f, 0.0f, TEXTBOX_CHARS, TEXTBOX_LINES);
    this.textbox.state_00 = TextboxState._6;
    this.textbox.flags_08 |= Textbox4c.RENDER_BACKGROUND;
    this.textbox.width_1c = this.textbox.chars_18 * 9 / 2;
    this.textbox.height_1e = this.textbox.lines_1a * 6;
  }

  @Override
  public void setZ(final int z) {
    super.setZ(z);
    this.textbox.z_0c = z + 1;
  }

  @Override
  protected void render(final int x, final int y) {
    final float centreX = x + this.getWidth() / 2.0f;
    final float centreY = y + this.getHeight() / 2.0f;
    final float textX = centreX - TEXTBOX_CHARS * 9 / 2.0f + 4.0f;
    final float firstLineY = centreY - TEXTBOX_LINES * 6.0f;

    this.textbox.x_14 = centreX;
    this.textbox.y_16 = centreY;
    this.textbox.colour.set(CONFIG.getConfig(UI_BACKGROUND_COLOUR.get()));

    renderTextboxBackground(this.textbox);
    renderTextboxSelection(this.textbox, this.selectionLine);

    final int oldTextZ = textZ_800bdf00;
    textZ_800bdf00 = (int)this.textbox.z_0c - 1;
    renderText(I18n.translate("lod_core.ui.options.colour_preview"), textX, firstLineY, this.fontOptions);
    renderText(I18n.translate("lod_core.ui.options.colour_preview_first"), textX, firstLineY + 12.0f, this.fontOptions);
    renderText(I18n.translate("lod_core.ui.options.colour_preview_second"), textX, firstLineY + 24.0f, this.fontOptions);
    textZ_800bdf00 = oldTextZ;
  }

  @Override
  protected InputPropagation mouseClick(final double x, final double y, final int button, final Set<InputMod> mods) {
    if(button == PLATFORM.getMouseButton(0) && mods.isEmpty()) {
      playMenuSound(2);
      this.close();
    }

    return InputPropagation.HANDLED;
  }

  @Override
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(action == INPUT_ACTION_MENU_UP.get()) {
      if(this.selectionLine != FIRST_SELECTION_LINE) {
        playMenuSound(1);
        this.selectionLine = FIRST_SELECTION_LINE;
      }

      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_DOWN.get()) {
      if(this.selectionLine != LAST_SELECTION_LINE) {
        playMenuSound(1);
        this.selectionLine = LAST_SELECTION_LINE;
      }

      return InputPropagation.HANDLED;
    }

    if(!repeat && (action == INPUT_ACTION_MENU_CONFIRM.get() || action == INPUT_ACTION_MENU_BACK.get())) {
      playMenuSound(action == INPUT_ACTION_MENU_BACK.get() ? 3 : 2);
      this.close();
    }

    return InputPropagation.HANDLED;
  }

  @Override
  protected InputPropagation inputActionReleased(final InputAction action) {
    return InputPropagation.HANDLED;
  }

  private void close() {
    if(this.closing) return;

    this.closing = true;
    this.deferAction(() -> {
      this.getScreen().setFocus(null);
      this.getScreen().removeControl(this);
    });
  }
}
