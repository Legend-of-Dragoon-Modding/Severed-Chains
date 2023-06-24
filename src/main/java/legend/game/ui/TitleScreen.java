package legend.game.ui;

import legend.core.gpu.VramTextureSingle;
import legend.core.opengl.MatrixStack;
import legend.core.opengl.Texture;
import legend.core.ui.Image;
import legend.core.ui.Screen;
import legend.core.ui.VerticalAlign;
import legend.core.ui.layouts.GridLayout;
import legend.game.tim.Tim;

import static legend.core.gpu.VramTextureLoader.palettesFromTim;
import static legend.core.gpu.VramTextureLoader.stitchVertical;
import static legend.core.gpu.VramTextureLoader.textureFromTim;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL12C.GL_UNSIGNED_INT_8_8_8_8_REV;

public class TitleScreen extends Screen {
  private Image background;
  private float backgroundScroll;
  private float backgroundAlpha;

  private Image logo;
  private float logoAlpha;

  private int loadingStage;

  //TODO this is dumb
  private Texture loadTexture(final VramTextureSingle texture, final VramTextureSingle palette) {
    final int[] backgroundData = palette.getData();

    final int w = texture.rect.w();
    final int h = texture.rect.h();
    final int[] data = texture.getData();

    for(int i = 0; i < data.length; i++) {
      data[i] = backgroundData[data[i]];
    }

    return Texture.create(builder -> {
      builder.data(data, w, h);
      builder.internalFormat(GL_RGBA);
      builder.dataFormat(GL_RGBA);
      builder.dataType(GL_UNSIGNED_INT_8_8_8_8_REV);
    });
  }

  @Override
  protected void initialize() {
    this.setAspectRatio(368.0f / 240.0f);

    this.backgroundScroll = 196.0f;
    this.backgroundAlpha = 0.0f;
    this.logoAlpha = 0.0f;

    loadDrgnDir(0, 5718, files -> {
      final VramTextureSingle backgroundTim = (VramTextureSingle)stitchVertical(
        textureFromTim(new Tim(files.get(0))),
        textureFromTim(new Tim(files.get(1)))
      );

      final VramTextureSingle backgroundPalette = (VramTextureSingle)palettesFromTim(new Tim(files.get(0)))[0];
      final Texture backgroundTexture = this.loadTexture(backgroundTim, backgroundPalette);
      this.background = this.addControl(new Image(backgroundTexture));
      this.background.setScaleMode(Image.ScaleMode.SCALE_TO_WIDTH);
      this.background.setVerticalAlign(VerticalAlign.TOP);
      final GridLayout layout = this.background.setLayout(new GridLayout());
      layout.setRowCount(2);
      layout.setColumnCount(1);

      final VramTextureSingle logoTim = (VramTextureSingle)textureFromTim(new Tim(files.get(2)));
      final VramTextureSingle logoPalettes = (VramTextureSingle)palettesFromTim(new Tim(files.get(2)))[0];
      final Texture logoTexture = this.loadTexture(logoTim, logoPalettes);
      this.logo = this.background.addControl(new Image(logoTexture));
      this.logo.setScaleMode(Image.ScaleMode.SCALE);
      this.logo.setVerticalAlign(VerticalAlign.BOTTOM);
      this.logo.setColour(1.0f, 1.0f, 1.0f, 0.0f);
    });
  }

  @Override
  protected void renderBackground(final MatrixStack matrixStack) {
    if(this.loadingStage == 0) {
      if(this.backgroundScroll < 20) {
        this.loadingStage = 1;
      }
    } else if(this.loadingStage == 1) {
      this.handleLogo();

      if(this.logoAlpha >= 0.5f) {
        this.loadingStage = 2;
      }
    } else if(this.loadingStage == 2) {
      this.handleLogo();
//      renderLogoFlash();
//
//      if(logoFlashStage == 2) {
//        this.loadingStage = 3;
//      }
    } else if(this.loadingStage == 3) {
      this.handleLogo();
//      renderMenuOptions();
//      renderMenuLogoFire();
//      renderCopyright();
    }

    this.handleBackground();
  }

  @Override
  protected void renderForeground(final MatrixStack matrixStack) {

  }

  public void handleBackground() {
    this.backgroundAlpha += 0.0125f;
    if(this.backgroundAlpha > 1.0f) {
      this.backgroundAlpha = 1.0f;
    }

    this.background.setUv(0.0f, this.backgroundScroll);
    this.background.setColour(1.0f, 1.0f, 1.0f, this.backgroundAlpha);

    this.backgroundScroll--;
    if(this.backgroundScroll < 0) {
      this.backgroundScroll = 0;
    }
  }

  public void handleLogo() {
    this.logoAlpha += 0.025f;
    if(this.logoAlpha > 1.0f) {
      this.logoAlpha = 1.0f;
    }

    this.logo.setColour(1.0f, 1.0f, 1.0f, this.logoAlpha);
    //TODO this.tm.setColour(1.0f, 1.0f, 1.0f, this.tmAlpha);
  }
}
