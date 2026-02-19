package legend.game.ui;

import legend.core.QueuedModelStandard;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.game.types.BackgroundType;
import legend.game.types.Textbox4c;
import legend.game.types.Translucency;
import org.joml.Vector3f;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Text.renderTextboxBackground;
import static legend.lodmod.LodConfig.UI_COLOUR;

public class UiBox {
  private final MV transforms = new MV();

  private final Textbox4c textbox = new Textbox4c();

  private int x;
  private int y;
  private int width;
  private int height;

  @Method(0x800f1268L) // buildBattleHudBackground
  public UiBox(final int x, final int y, final int width, final int height) {
    this.textbox.backgroundType_04 = BackgroundType.NORMAL;
    this.textbox.renderBorder_06 = true;
    this.textbox.flags_08 |= Textbox4c.NO_ANIMATE_OUT;

    this.setPos(x, y);
    this.setSize(width, height);
    this.setZ(124);
  }

  public UiBox() {
    this(0, 0, 0, 0);
  }

  public void setPos(final int x, final int y) {
    this.x = x;
    this.y = y;
    this.updateSize();
  }

  public void setSize(final int width, final int height) {
    this.width = width;
    this.height = height;
    this.updateSize();
  }

  public void setZ(final float z) {
    this.textbox.z_0c = z / 4.0f;
    this.updateSize();
  }

  private void updateSize() {
    this.transforms.scaling(this.width, this.height, 1.0f);
    this.transforms.transfer.set(this.x, this.y, this.textbox.z_0c * 4.0f + 1.0f);

    final int w = this.width / 2;
    final int h = this.height / 2;

    this.textbox.x_14 = this.x + w;
    this.textbox.y_16 = this.y + h + 1;
    this.textbox.width_1c = w;
    this.textbox.height_1e = h + 1;
  }

  public void render() {
    this.render(CONFIG.getConfig(UI_COLOUR.get()));
  }

  public void render(final Vector3f colour) {
    this.render(colour.x, colour.y, colour.z);
  }

  public void render(final float r, final float g, final float b) {
    this.textbox.colour.set(r, g, b);
    renderTextboxBackground(this.textbox);

    RENDERER.queueOrthoModel(RENDERER.plainQuads.get(Translucency.HALF_B_PLUS_HALF_F), this.transforms, QueuedModelStandard.class)
      .colour(0.0f, 0.0f, 0.0f);
  }
}
