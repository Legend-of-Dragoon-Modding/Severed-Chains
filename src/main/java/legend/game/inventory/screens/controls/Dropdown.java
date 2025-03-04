package legend.game.inventory.screens.controls;

import legend.core.MathHelper;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.InputPropagation;
import legend.game.inventory.screens.MenuScreen;
import legend.game.inventory.screens.TextColour;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.textHeight;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;

public class Dropdown<T> extends Control {
  private final Panel background;
  private final Panel panel;
  private final Glyph downArrow;
  private final Brackets highlight;

  /** Allows list wrapping, but only on new input */
  private boolean allowWrapY = true;

  private final List<T> options = new ArrayList<>();
  private final Function<T, String> toString;
  private int hoverIndex = -1;
  private int selectedIndex = -1;
  private final FontOptions fontOptions = new FontOptions().colour(TextColour.BROWN).shadowColour(TextColour.MIDDLE_BROWN);

  public Dropdown() {
    this(String::valueOf);
  }

  public Dropdown(final Function<T, String> toString) {
    this.toString = toString;
    this.background = this.addControl(Panel.subtle());

    this.panel = Panel.panel();
    this.panel.setZ(10);

    this.highlight = this.panel.addControl(new Brackets());
    this.highlight.setPos(6, 8);
    this.highlight.setZ(this.panel.getZ() - 2);
    this.highlight.setHeight((int)(16 * this.getScale()));

    this.panel.onMouseMove((x, y) -> {
      for(int i = 0; i < this.options.size(); i++) {
        if(MathHelper.inBox(x, y, 6, (int)(10 + i * 16 * this.getScale()), this.getWidth(), (int)(16 * this.getScale()))) {
          this.hover(i);
          return InputPropagation.HANDLED;
        }
      }

      return InputPropagation.PROPAGATE;
    });

    this.panel.onMouseClick((x, y, button, mods) -> {
      for(int i = 0; i < this.options.size(); i++) {
        if(MathHelper.inBox(x, y, 6, (int)(10 + i * 16 * this.getScale()), this.getWidth(), (int)(16 * this.getScale()))) {
          this.select(i);
          this.panel.getScreen().getStack().popScreen();
          return InputPropagation.HANDLED;
        }
      }

      return InputPropagation.PROPAGATE;
    });

    this.downArrow = this.addControl(Glyph.uiElement(53, 60));
    this.downArrow.ignoreInput();
    this.downArrow.setZ(this.background.getZ() - 1);

    this.setSize(100, 16);
  }

  public void clearOptions() {
    this.options.clear();
    this.panel.setHeight(17);
    this.selectedIndex = -1;
  }

  public void addOption(final T option) {
    this.options.add(option);
    this.panel.setHeight((int)(17 + this.options.size() * 16 * this.getScale()));

    if(this.selectedIndex == -1) {
      this.setSelectedIndex(0);
    }
  }

  public void setSelectedIndex(final int index) {
    if(index < 0 || index >= this.options.size()) {
      this.selectedIndex = -1;
      return;
    }

    this.selectedIndex = index;
  }

  public int getSelectedIndex() {
    return this.selectedIndex;
  }

  public T getSelectedOption() {
    if(this.selectedIndex == -1) {
      return null;
    }

    return this.options.get(this.selectedIndex);
  }

  public int size() {
    return this.options.size();
  }

  @Override
  public void setScale(final float scale) {
    super.setScale(scale);
    this.fontOptions.size(scale);
    this.downArrow.setScale(scale);
    this.downArrow.setPos(this.getWidth() - 1, (this.getHeight() - 17) / 2);
    this.highlight.setHeight((int)(16 * this.getScale()));
    this.panel.setHeight((int)(17 + this.options.size() * 16 * this.getScale()));
  }

  @Override
  protected void onResize() {
    super.onResize();
    this.background.setSize(this.getWidth(), this.getHeight());
    this.downArrow.setPos(this.getWidth() - 1, (this.getHeight() - 17) / 2);
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final Set<InputMod> mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    playMenuSound(2);
    this.showDropdown();
    return InputPropagation.HANDLED;
  }

  private void showDropdown() {
    this.getScreen().getStack().pushScreen(new DropdownScreen());
    this.hover(this.selectedIndex);
  }

  private void hover(final int index) {
    if(index == -1) {
      this.highlight.hide();
    } else {
      if(this.hoverIndex != -1 && this.hoverIndex != index) {
        playMenuSound(1);
      }

      this.highlight.show();
    }

    this.hoverIndex = index;
    this.highlight.setY((int)(8 + index * 16 * this.getScale()));
  }

  private void select(final int index) {
    playMenuSound(2);
    this.setSelectedIndex(index);

    if(this.selectionHandler != null) {
      this.selectionHandler.selection(index);
    }
  }

  @Override
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
      playMenuSound(2);
      this.showDropdown();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.selectedIndex != -1) {
      final String text = this.toString.apply(this.options.get(this.selectedIndex));

      final int oldZ = textZ_800bdf00;
      textZ_800bdf00 = this.background.getZ() - 1;
      renderText(text, x + 4, y + (this.getHeight() - textHeight(text) * this.getScale()) / 2 + 0.5f, this.fontOptions);
      textZ_800bdf00 = oldZ;
    }
  }

  public void onSelection(final Selection handler) {
    this.selectionHandler = handler;
  }

  private Selection selectionHandler;

  @FunctionalInterface public interface Selection { void selection(final int index); }

  private class DropdownScreen extends MenuScreen {
    private DropdownScreen() {
      final Panel panel = Dropdown.this.panel;

      this.addControl(panel);
      panel.setPos(Dropdown.this.calculateTotalX() - 6, Dropdown.this.calculateTotalY() + Dropdown.this.getHeight());

      if(panel.getY() + panel.getHeight() > this.getHeight()) {
        panel.setY(Dropdown.this.calculateTotalY() - panel.getHeight());
      }

      if(panel.getY() < 0) {
        panel.setY(0);
      }

      panel.setWidth(Dropdown.this.getWidth() + 12);
      Dropdown.this.highlight.setWidth(Dropdown.this.getWidth());
    }

    @Override
    protected void render() {
      final int oldZ = textZ_800bdf00;
      textZ_800bdf00 = Dropdown.this.panel.getZ() - 1;

      for(int i = 0; i < Dropdown.this.options.size(); i++) {
        renderText(Dropdown.this.toString.apply(Dropdown.this.options.get(i)), Dropdown.this.panel.getX() + 10, Dropdown.this.panel.getY() + 10 + i * 16 * Dropdown.this.getScale() - 1, Dropdown.this.fontOptions);
      }

      textZ_800bdf00 = oldZ;
    }

    @Override
    protected InputPropagation mouseClick(final int x, final int y, final int button, final Set<InputMod> mods) {
      if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
        return InputPropagation.HANDLED;
      }

      playMenuSound(3);
      this.getStack().popScreen();
      return InputPropagation.HANDLED;
    }

    @Override
    protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
      if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_UP.get()) {
        final int optionCount = Dropdown.this.options.size();
        if(Dropdown.this.hoverIndex != 0) {
          Dropdown.this.hover(Dropdown.this.hoverIndex - 1);
        } else if(optionCount > 1 && Dropdown.this.allowWrapY) {
          Dropdown.this.hover(optionCount - 1);
        }
        Dropdown.this.allowWrapY = false;
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_DOWN.get()) {
        final int optionCount = Dropdown.this.options.size();
        if(Dropdown.this.hoverIndex != optionCount - 1) {
          Dropdown.this.hover(Dropdown.this.hoverIndex + 1);
        } else if(optionCount > 1 && Dropdown.this.allowWrapY) {
          Dropdown.this.hover(0);
        }
        Dropdown.this.allowWrapY = false;
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
        Dropdown.this.select(Dropdown.this.hoverIndex);
        this.getStack().popScreen();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
        playMenuSound(3);
        this.getStack().popScreen();
        return InputPropagation.HANDLED;
      }

      return InputPropagation.PROPAGATE;
    }

    @Override
    protected InputPropagation inputActionReleased(final InputAction action) {
      if(super.inputActionReleased(action) == InputPropagation.HANDLED) {
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_UP.get() || action == INPUT_ACTION_MENU_DOWN.get()) {
        Dropdown.this.allowWrapY = true;
        return InputPropagation.HANDLED;
      }

      return InputPropagation.PROPAGATE;
    }

    @Override
    protected boolean propagateRender() {
      return true;
    }
  }
}
