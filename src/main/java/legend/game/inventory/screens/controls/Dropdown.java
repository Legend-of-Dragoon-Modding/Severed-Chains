package legend.game.inventory.screens.controls;

import legend.core.MathHelper;
import legend.game.input.InputAction;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.InputPropagation;
import legend.game.inventory.screens.MenuScreen;
import legend.game.inventory.screens.TextColour;
import legend.game.inventory.screens.TextRenderable;
import legend.game.inventory.screens.TextRenderer;

import java.util.ArrayList;
import java.util.List;

public class Dropdown extends Control {
  private final Panel background;
  private final Panel panel;
  private final Glyph downArrow;
  private final Brackets highlight;

  private final List<String> options = new ArrayList<>();
  private int hoverIndex;
  private int selectedIndex = -1;
  private TextRenderable selectedTextRenderable;

  public Dropdown() {
    this.background = this.addControl(Panel.subtle());

    this.panel = Panel.panel();
    this.panel.setZ(10);

    this.highlight = this.panel.addControl(new Brackets());
    this.highlight.setPos(6, 8);
    this.highlight.setZ(this.panel.getZ() - 2);
    this.highlight.setHeight(16);

    this.panel.onMouseMove((x, y) -> {
      for(int i = 0; i < this.options.size(); i++) {
        if(MathHelper.inBox(x, y, 0, 10 + i * 16, this.getWidth(), 16)) {
          this.hover(i);
          return InputPropagation.HANDLED;
        }
      }

      return InputPropagation.PROPAGATE;
    });

    this.panel.onMouseClick((x, y, button, mods) -> {
      for(int i = 0; i < this.options.size(); i++) {
        if(MathHelper.inBox(x, y, 0, 10 + i * 16, this.getWidth(), 16)) {
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
    this.panel.setHeight(18);
    this.selectedIndex = -1;
  }

  public void addOption(final String option) {
    this.options.add(option);
    this.panel.setHeight(18 + this.options.size() * 16);

    if(this.selectedIndex == -1) {
      this.setSelectedIndex(0);
    }
  }

  public void setSelectedIndex(final int index) {
    this.selectedIndex = index;
    this.selectedTextRenderable = TextRenderer.prepareShadowText(this.options.get(index), 0, 0, TextColour.BROWN);
  }

  public int getSelectedIndex() {
    return this.selectedIndex;
  }

  public int size() {
    return this.options.size();
  }

  @Override
  protected void onResize() {
    super.onResize();
    this.background.setSize(this.getWidth(), this.getHeight());
    this.highlight.setWidth(this.getWidth() + 7);
    this.downArrow.setPos(this.getWidth() - 1, -1);
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final int mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    this.showDropdown();
    return InputPropagation.HANDLED;
  }

  private void showDropdown() {
    this.getScreen().getStack().pushScreen(new DropdownScreen());
    this.hover(this.selectedIndex);
  }

  private void hover(final int index) {
    this.hoverIndex = index;
    this.highlight.setY(8 + index * 16);
  }

  private void select(final int index) {
    this.setSelectedIndex(index);

    if(this.selectionHandler != null) {
      this.selectionHandler.selection(index);
    }
  }

  @Override
  protected InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_SOUTH) {
      this.showDropdown();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected void render(final int x, final int y) {
    if(!this.options.isEmpty()) {
      this.selectedTextRenderable.render(x + 4, y + (this.getHeight() - 11) / 2 + 1, this.background.getZ() - 1);
    }
  }

  public void onSelection(final Selection handler) {
    this.selectionHandler = handler;
  }

  private Selection selectionHandler;

  @FunctionalInterface public interface Selection { void selection(final int index); }

  private class DropdownScreen extends MenuScreen {
    private final TextRenderable[] textRenderables = new TextRenderable[Dropdown.this.options.size()];

    private DropdownScreen() {
      this.addControl(Dropdown.this.panel);
      Dropdown.this.panel.setPos(Dropdown.this.getX() - 9, Dropdown.this.getY() + Dropdown.this.getHeight());
      Dropdown.this.panel.setWidth(Dropdown.this.getWidth() + 18);

      for(int i = 0; i < this.textRenderables.length; i++) {
        this.textRenderables[i] = TextRenderer.prepareShadowText(Dropdown.this.options.get(i), 0, 0, TextColour.BROWN);
      }
    }

    @Override
    protected void render() {
      for(int i = 0; i < Dropdown.this.options.size(); i++) {
        this.textRenderables[i].render(Dropdown.this.panel.getX() + 10, Dropdown.this.panel.getY() + 10 + i * 16, Dropdown.this.panel.getZ() - 1);
      }
    }

    @Override
    protected InputPropagation mouseClick(final int x, final int y, final int button, final int mods) {
      if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
        return InputPropagation.HANDLED;
      }

      this.getStack().popScreen();
      return InputPropagation.HANDLED;
    }

    @Override
    protected InputPropagation pressedWithRepeatPulse(final InputAction inputAction) {
      if(super.pressedWithRepeatPulse(inputAction) == InputPropagation.HANDLED) {
        return InputPropagation.HANDLED;
      }

      if(inputAction == InputAction.DPAD_DOWN || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_DOWN) {
        Dropdown.this.hover((Dropdown.this.hoverIndex + 1) % Dropdown.this.options.size());
        return InputPropagation.HANDLED;
      } else if(inputAction == InputAction.DPAD_UP || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_UP) {
        Dropdown.this.hover(Math.floorMod(Dropdown.this.hoverIndex - 1, Dropdown.this.options.size()));
        return InputPropagation.HANDLED;
      }

      return InputPropagation.PROPAGATE;
    }

    @Override
    protected InputPropagation pressedThisFrame(final InputAction inputAction) {
      if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
        return InputPropagation.HANDLED;
      }

      if(inputAction == InputAction.BUTTON_SOUTH) {
        Dropdown.this.select(Dropdown.this.hoverIndex);
        this.getStack().popScreen();
        return InputPropagation.HANDLED;
      } else if(inputAction == InputAction.BUTTON_EAST) {
        this.getStack().popScreen();
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
