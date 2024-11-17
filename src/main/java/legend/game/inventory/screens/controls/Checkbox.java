package legend.game.inventory.screens.controls;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.Texture;
import legend.game.input.InputAction;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.InputPropagation;

import java.nio.file.Path;

import static legend.core.GameEngine.RENDERER;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Checkbox extends Control {
  private final Obj obj;
  private final MV transforms = new MV();

  private final Texture uncheckedTexture;
  private final Texture checkedTexture;

  private Label.HorizontalAlign horizontalAlign = Label.HorizontalAlign.CENTRE;
  private Label.VerticalAlign verticalAlign = Label.VerticalAlign.CENTRE;
  private boolean checked;

  public Checkbox() {
    this.uncheckedTexture = Texture.png(Path.of("gfx", "ui", "checkbox.png"));
    this.checkedTexture = Texture.png(Path.of("gfx", "ui", "checkbox_checked.png"));

    this.obj = new QuadBuilder("Checkbox")
      .bpp(Bpp.BITS_24)
      .posSize(14.0f, 14.0f)
      .uvSize(1.0f, 1.0f)
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

    if(this.toggledHandler != null) {
      this.toggledHandler.accept(checked);
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

    this.transforms.transfer.set(x, y, this.getZ() * 4.0f);
    final QueuedModelStandard model = RENDERER
      .queueOrthoModel(this.obj, this.transforms, QueuedModelStandard.class);

    if(this.checked) {
      model.texture(this.checkedTexture);
    } else {
      model.texture(this.uncheckedTexture);
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

  public void onToggled(final BooleanConsumer handler) {
    this.toggledHandler = handler;
  }

  private Runnable checkedHandler;
  private Runnable uncheckedHandler;
  private BooleanConsumer toggledHandler;
}
