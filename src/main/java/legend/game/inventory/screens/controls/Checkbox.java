package legend.game.inventory.screens.controls;

import legend.core.gpu.ModelLoader;
import legend.core.gpu.Renderable;
import legend.core.gpu.VramTextureLoader;
import legend.game.input.InputAction;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.InputPropagation;

import java.nio.file.Path;

import static legend.game.Scus94491BpeSegment.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Checkbox extends Control {
  private final Renderable uncheckedRenderable;
  private final Renderable checkedRenderable;

  private Label.HorizontalAlign horizontalAlign = Label.HorizontalAlign.CENTRE;
  private Label.VerticalAlign verticalAlign = Label.VerticalAlign.CENTRE;
  private boolean checked;

  public Checkbox() {
    this.uncheckedRenderable = ModelLoader.quad(
      "unchecked",
      -displayWidth_1f8003e0.get() / 2, -displayHeight_1f8003e4.get() / 2, 0, 14, 14,
      0, 0, 14, 14,
      0, 0, 0,
      0x80, 0x80, 0x80,
      null
    )
      .texture(VramTextureLoader.textureFromPng(Path.of("gfx", "ui", "checkbox.png")))
      .build();

    this.checkedRenderable = ModelLoader.quad(
      "checked",
      -displayWidth_1f8003e0.get() / 2, -displayHeight_1f8003e4.get() / 2, 0, 14, 14,
      0, 0, 14, 14,
      0, 0, 0,
      0x80, 0x80, 0x80,
      null
    )
      .texture(VramTextureLoader.textureFromPng(Path.of("gfx", "ui", "checkbox_checked.png")))
      .build();

    this.setSize(14, 14);
  }

  public void setHorizontalAlign(final Label.HorizontalAlign horizontalAlign) {
    this.horizontalAlign = horizontalAlign;
  }

  public Label.HorizontalAlign getHorizontalAlign() {
    return this.horizontalAlign;
  }

  public void setVerticalAlign(final Label.VerticalAlign verticalAlign) {
    this.verticalAlign = verticalAlign;
  }

  public Label.VerticalAlign getVerticalAlign() {
    return this.verticalAlign;
  }

  public void setChecked(final boolean checked) {
    this.checked = checked;

    if(checked) {
      if(this.checkedHandler != null) {
        this.checkedHandler.run();
      }
    } else {
      if(this.uncheckedHandler != null) {
        this.uncheckedHandler.run();
      }
    }
  }

  public boolean isChecked() {
    return this.checked;
  }

  @Override
  protected void render(final int controlX, final int controlY) {
    final int x = switch(this.horizontalAlign) {
      case LEFT -> controlX;
      case CENTRE -> controlX + (this.getWidth() - 14) / 2;
      case RIGHT -> controlX + this.getWidth() - 14;
    };

    final int y = switch(this.verticalAlign) {
      case TOP -> controlY;
      case CENTRE -> controlY + (this.getHeight() - 14) / 2;
      case BOTTOM -> controlY + this.getHeight() - 14;
    };

    if(this.checked) {
      this.checkedRenderable.render(x, y, this.getZ());
    } else {
      this.uncheckedRenderable.render(x, y, this.getZ());
    }
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final int mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(button == GLFW_MOUSE_BUTTON_LEFT) {
      this.setChecked(!this.isChecked());
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
      this.setChecked(!this.isChecked());
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  public void onChecked(final Runnable handler) {
    this.checkedHandler = handler;
  }

  public void onUnchecked(final Runnable handler) {
    this.uncheckedHandler = handler;
  }

  private Runnable checkedHandler;
  private Runnable uncheckedHandler;
}
