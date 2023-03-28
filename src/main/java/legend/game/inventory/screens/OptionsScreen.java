package legend.game.inventory.screens;

import legend.core.GameEngine;
import legend.core.MathHelper;
import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Dropdown;
import legend.game.inventory.screens.controls.Highlight;
import legend.game.inventory.screens.controls.Label;
import legend.game.saves.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class OptionsScreen extends MenuScreen {
  private final Runnable unload;
  private final List<Control> options = new ArrayList<>();

  private final Highlight highlight;
  private int highlightedOption;

  public OptionsScreen(final Runnable unload) {
    deallocateRenderables(0xff);
    scriptStartEffect(2, 10);

    this.unload = unload;

    this.addControl(new Background());

    this.highlight = this.addControl(new Highlight());
    this.highlight.setPos(34, 30);
    this.highlight.setSize(320, 16);
    this.highlight.setClut(0xfc29);

    final Dropdown sound = this.addDropdown("Sound", "Stereo", "Mono");
    sound.setSelectedIndex(gameState_800babc8.mono_4e0 ? 1 : 0);
    sound.onSelection(index -> gameState_800babc8.mono_4e0 = index == 1);

    final Dropdown transforms = this.addDropdown("Dragoon Transformation", "Normal", "Short");
    transforms.setSelectedIndex(gameState_800babc8.morphMode_4e2);
    transforms.onSelection(index -> gameState_800babc8.morphMode_4e2 = index);

    final Dropdown indicators = this.addDropdown("Indicators", "Off", "Momentary", "On");
    indicators.setSelectedIndex(gameState_800babc8.indicatorMode_4e8);
    indicators.onSelection(index -> gameState_800babc8.indicatorMode_4e8 = index);

    //noinspection rawtypes
    for(final ConfigEntry configEntry : GameEngine.REGISTRIES.config) {
      if(configEntry.hasEditControl()) {
        //noinspection unchecked
        this.addConfig(configEntry.id.toString(), configEntry.makeEditControl(gameState_800babc8.getConfig(configEntry), gameState_800babc8));
      }
    }

    this.setFocus(this.options.get(0));
  }

  private Dropdown addDropdown(final String name, final String... options) {
    final Dropdown dropdown = new Dropdown();

    for(final String option : options) {
      dropdown.addOption(option);
    }

    return this.addConfig(name, dropdown);
  }

  private <T extends Control> T addConfig(final String name, final T control) {
    this.addControl(new Label(name)).setPos(32, 32 + this.options.size() * 20);

    control.setPos(240, 32 + this.options.size() * 20);
    control.setSize(100, 16);
    this.options.add(control);
    return this.addControl(control);
  }

  private void highlightOption(final int option) {
    this.highlightedOption = option;
    this.highlight.setY(30 + option * 20);
    this.setFocus(this.options.get(option));
  }

  @Override
  protected void render() {

  }

  @Override
  protected InputPropagation mouseMove(final int x, final int y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    for(int i = 0; i < this.options.size(); i++) {
      if(MathHelper.inBox(x, y, 34, 30 + i * 20, 320, 20)) {
        this.highlightOption(i);
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_EAST) {
      playSound(3);
      this.unload.run();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation pressedWithRepeatPulse(final InputAction inputAction) {
    if(super.pressedWithRepeatPulse(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.DPAD_DOWN || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_DOWN) {
      this.highlightOption((this.highlightedOption + 1) % this.options.size());
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.DPAD_UP || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_UP) {
      this.highlightOption(Math.floorMod(this.highlightedOption - 1, this.options.size()));
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
