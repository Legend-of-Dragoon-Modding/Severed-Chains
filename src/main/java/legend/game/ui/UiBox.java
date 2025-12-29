package legend.game.ui;

import legend.core.Config;
import legend.core.QueuedModelStandard;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.types.BackgroundType;
import legend.game.types.Textbox4c;
import legend.game.types.Translucency;
import org.joml.Vector3f;

import static legend.core.GameEngine.RENDERER;
import static legend.game.Text.renderTextboxBackground;

public class UiBox {
  private final Obj hudBackgroundButDarkerObj;
  private final MV transforms = new MV();

  private final Textbox4c textbox = new Textbox4c();

  @Method(0x800f1268L) // buildBattleHudBackground
  public UiBox(final String name, final int x, final int y, final int width, final int height) {
    // Darkening underlay
    this.hudBackgroundButDarkerObj = new QuadBuilder(name + " Background Darkening Underlay")
      .translucency(Translucency.HALF_B_PLUS_HALF_F)
      .monochrome(0, 0.0f)
      .pos(x, y, 0.0f)
      .size(width, height)
      .build();

    this.textbox.backgroundType_04 = BackgroundType.NORMAL;
    this.textbox.renderBorder_06 = true;
    this.textbox.flags_08 |= Textbox4c.NO_ANIMATE_OUT;

    final int w = width / 2;
    final int h = height / 2;

    this.textbox.x_14 = x + w;
    this.textbox.y_16 = y + h + 1;
    this.textbox.width_1c = w;
    this.textbox.height_1e = h + 1;
    this.textbox.z_0c = 31;
  }

  public void setZ(final float z) {
    this.textbox.z_0c = z / 4.0f;
  }

  public void render() {
    this.render(Config.getUiRgb());
  }

  public void render(final Vector3f colour) {
    this.render(colour.x, colour.y, colour.z);
  }

  public void render(final float r, final float g, final float b) {
    this.textbox.colour.set(r, g, b);
    renderTextboxBackground(this.textbox);

    this.transforms.transfer.set(0.0f, 0.0f, this.textbox.z_0c * 4.0f + 1.0f);
    RENDERER.queueOrthoModel(this.hudBackgroundButDarkerObj, this.transforms, QueuedModelStandard.class);
  }

  public void delete() {
    this.hudBackgroundButDarkerObj.delete();
  }
}
