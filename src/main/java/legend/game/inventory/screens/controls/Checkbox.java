package legend.game.inventory.screens.controls;

import legend.core.gpu.ModelLoader;
import legend.core.gpu.Renderable;
import legend.core.gpu.VramTextureLoader;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.InputPropagation;

import java.nio.file.Path;

import static legend.game.Scus94491BpeSegment.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Checkbox extends Control {
  private final Renderable uncheckedRenderable;
  private final Renderable checkedRenderable;

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

  public void setChecked(final boolean checked) {
    this.checked = checked;
  }

  public boolean isChecked() {
    return this.checked;
  }

  @Override
  protected void render(final int x, final int y) {
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
}
