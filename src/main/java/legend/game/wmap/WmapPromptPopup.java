package legend.game.wmap;

import legend.core.gpu.Bpp;
import legend.core.gpu.Rect4i;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.MeshObj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.TextBuilder;
import legend.core.opengl.TextObj;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;

public class WmapPromptPopup {
  public enum ObjFields {
    PROMPT,
    OPTIONS,
    ALT_TEXT,
    THUMBNAIL,
    SHADOW,
    SELECTOR
  }

  public enum HighlightMode {
    SHADOW,
    SELECTOR
  }

  private final MV transforms = new MV();

  private TextObj prompt;
  private final Vector3f promptTranslation = new Vector3f();

  private final List<TextObj> options = new ArrayList<>();
  private int menuSelectorOptionIndex;
  private float optionSpacing = 20.0f;
  private final Vector3f optionsTranslation = new Vector3f();

  private boolean renderAltText;
  private final List<TextObj> altText = new ArrayList<>();
  private float altTextSpacing = 16.0f;
  private final Vector3f altTextTranslation = new Vector3f();

  private MeshObj thumbnail;
  private final Vector3f thumbnailTranslation = new Vector3f();
  private float currentThumbnailBrightness;
  private float previousThumbnailBrightness;

  private WmapMenuTextHighlight40 shadow;
  private WmapMenuTextHighlight40 selector;

  public WmapPromptPopup() {
  }

  public WmapPromptPopup(final String prompt, final float textZ) {
    this.prompt = this.buildText(prompt);
    final int lines = (int)prompt.lines().count();
    this.promptTranslation.set(240.0f, 140.0f - lines * 7, textZ);
    this.optionsTranslation.set(240.0f, 170.0f, textZ - 2.0f);
    this.altTextTranslation.set(240.0f, 31.0f, textZ - 2.0f);
    this.thumbnailTranslation.set(0.0f, 0.0f, textZ);
    this.currentThumbnailBrightness = 1.0f;
    this.previousThumbnailBrightness = 0.0f;
    this.transforms.identity();
  }

  public void setTranslation(final ObjFields field, final float x, final float y, final float z) {
    switch(field) {
      case PROMPT -> this.promptTranslation.set(x, y, z);
      case OPTIONS -> this.optionsTranslation.set(x, y, z);
      case ALT_TEXT -> this.altTextTranslation.set(x, y, z);
      case THUMBNAIL -> this.thumbnailTranslation.set(x, y, z);
    }
  }

  public int getMenuSelectorOptionIndex() {
    return this.menuSelectorOptionIndex;
  }

  public void setMenuSelectorOptionIndex(final int menuSelectorOptionIndex) {
    this.menuSelectorOptionIndex = menuSelectorOptionIndex;
  }

  public void incrMenuSelectorOptionIndex() {
    this.menuSelectorOptionIndex++;
    if(this.menuSelectorOptionIndex >= this.options.size()) {
      this.setMenuSelectorOptionIndex(0);
    }
  }

  public void decrMenuSelectorOptionIndex() {
    this.menuSelectorOptionIndex--;
    if(this.menuSelectorOptionIndex < 0) {
      this.setMenuSelectorOptionIndex(this.options.size() - 1);
    }
  }

  public float getOptionSpacing() {
    return this.optionSpacing;
  }

  public void setOptionSpacing(final float spacing) {
    this.optionSpacing = spacing;
  }

  public void setAltTextSpacing(final float spacing) {
    this.altTextSpacing = spacing;
  }

  public void setShowAltText(final boolean renderAltText) {
    this.renderAltText = renderAltText;
  }

  public float getThumbnailBrightness() {
    return this.currentThumbnailBrightness;
  }

  public void setThumbnailBrightness(final float brightness) {
    this.previousThumbnailBrightness = this.currentThumbnailBrightness;
    this.currentThumbnailBrightness = brightness;
  }

  public WmapMenuTextHighlight40 getShadow() {
    return this.shadow;
  }

  public WmapMenuTextHighlight40 getSelector() {
    return this.selector;
  }

  private TextObj buildText(final String text) {
    return new TextBuilder(text)
      .text(text)
      .centred()
      .shadowed()
      .build();
  }

  public WmapPromptPopup setPrompt(final String text) {
    this.prompt = new TextBuilder(text)
      .text(text)
      .centred()
      .shadowed()
      .build();
    return this;
  }

  public WmapPromptPopup addOptionText(final String text) {
    this.options.add(this.buildText(text));
    return this;
  }

  public WmapPromptPopup addAltText(final String text) {
    this.altText.add(this.buildText(text));
    return this;
  }

  public WmapPromptPopup setImage(final int clutX, final int clutY, final int vramX, final int vramY, final float posX, final float posY, final float w, final float h, final int u, final int v, final float brightness) {
    if(this.thumbnail != null && this.currentThumbnailBrightness != this.previousThumbnailBrightness) {
      this.thumbnail.delete();
      this.thumbnail = null;
    }

    if(this.thumbnail == null) {
      this.thumbnail = new QuadBuilder("PopupThumbnail")
        .bpp(Bpp.BITS_8)
        .clut(clutX, clutY)
        .vramPos(vramX, vramY)
        .pos(posX, posY, 0)
        .uv(u, v)
        .size(w, h)
        .monochrome(brightness)
        .build();
    }

    return this;
  }

  public WmapPromptPopup setHighlight(final HighlightMode mode, final WmapMenuTextHighlight40 highlight) {
    switch(mode) {
      case SHADOW -> this.shadow = highlight;
      case SELECTOR -> this.selector = highlight;
    }

    this.setRenderColours(mode, highlight);

    return this;
  }

  @Method(0x800cea1cL)
  private void setRenderColours(final HighlightMode mode, final WmapMenuTextHighlight40 highlight) {
    final QuadBuilder builder = new QuadBuilder("MenuPrompt" + mode);

    //LAB_800ceaa0
    //LAB_800ceacc
    //LAB_800ceb38
    int n = 0;
    for(int i = 0; i < highlight.subRectCount_30; i++) {
      final WMapTextHighlightSubRectVertexColours10 colours = highlight.subRectVertexColoursArray_00[n];

      final float r0 = colours.topLeft_00.x;
      final float g0 = colours.topLeft_00.y;
      final float b0 = colours.topLeft_00.z;
      final float r1 = colours.topRight_04.x;
      final float g1 = colours.topRight_04.y;
      final float b1 = colours.topRight_04.z;
      final float r2 = colours.bottomLeft_08.x;
      final float g2 = colours.bottomLeft_08.y;
      final float b2 = colours.bottomLeft_08.z;
      final float r3 = colours.bottomRight_0c.x;
      final float g3 = colours.bottomRight_0c.y;
      final float b3 = colours.bottomRight_0c.z;
      if(highlight.type_3f != 0) {
        n++;
      }

      final Rect4i rect = highlight.rects_1c[i];

      builder
        .add()
        .rgb(0, r0, g0, b0)
        .rgb(1, r2, g2, b2)
        .rgb(2, r1, g1, b1)
        .rgb(3, r3, g3, b3)
        .pos(rect.x, rect.y, 0.0f)
        .size(rect.w, rect.h);

      if(highlight.transparency_3c) {
        builder.translucency(highlight.tpagePacket_04[i]);
      }
    }

    highlight.highlight = builder.build();

    //LAB_800cf1dc
    //LAB_800cf1e4
    //LAB_800cf1fc
  }

  public void renderHighlight(final HighlightMode mode) {
    final WmapMenuTextHighlight40 highlight = mode == HighlightMode.SELECTOR ? this.selector : this.shadow;

    if(highlight.currentBrightness_34 < 0.0f) {
      highlight.currentBrightness_34 = 0.0f;
      //LAB_800cea54
    } else if(highlight.currentBrightness_34 > 1.0f) {
      highlight.currentBrightness_34 = 1.0f;
    }

    final float x = highlight.x_38 + GPU.getOffsetX();
    final float y = highlight.y_3a + GPU.getOffsetY();

    //LAB_800ce538
    //LAB_800ce5a0
    //LAB_800ce5a4
    for(int i = 0; i < highlight.subRectCount_30; i++) {
      //LAB_800ce5c8
      highlight.transforms.identity();
      highlight.transforms.transfer.set(x, y, highlight.z_3e);
      RENDERER.queueOrthoModel(highlight.highlight, highlight.transforms)
        .vertices(i * 4, 4)
        .monochrome(highlight.currentBrightness_34);
    }
  }

  public void render() {
    if(this.shadow != null) {
      this.renderHighlight(HighlightMode.SHADOW);
    }

    if(this.selector != null) {
      this.renderHighlight(HighlightMode.SELECTOR);
    }

    if(this.thumbnail != null) {
      this.transforms.transfer.set(this.thumbnailTranslation);
      RENDERER.queueOrthoModel(this.thumbnail, this.transforms);
    }

    if(this.prompt != null) {
      this.transforms.transfer.set(this.promptTranslation);
      RENDERER.queueOrthoModel(this.prompt, this.transforms);
    }

    if(!this.options.isEmpty()) {
      this.transforms.transfer.set(this.optionsTranslation);
      for(int i = 0; i < this.options.size(); i++) {
        final TextObj option = this.options.get(i);
        if(option != null) {
          RENDERER.queueOrthoModel(option, this.transforms);
          this.transforms.transfer.y += this.optionSpacing;
        }
      }
    }

    if(this.renderAltText && !this.altText.isEmpty()) {
      this.transforms.transfer.set(this.altTextTranslation);
      for(int i = 0; i < this.altText.size(); i++) {
        final TextObj altText = this.altText.get(i);
        if(altText != null) {
          RENDERER.queueOrthoModel(altText, this.transforms);
          this.transforms.transfer.y += this.altTextSpacing;
        }
      }
    }
  }

  public void deallocate() {
    if(this.prompt != null) {
      this.prompt.delete();
      this.prompt = null;
    }

    if(!this.options.isEmpty()) {
      for(int i = 0; i < this.options.size(); i++) {
        if(this.options.get(i) != null) {
          this.options.get(i).delete();
        }
      }

      this.options.clear();
    }

    if(!this.altText.isEmpty()) {
      for(int i = 0; i < this.altText.size(); i++) {
        if(this.altText.get(i) != null) {
          this.altText.get(i).delete();
        }
      }

      this.altText.clear();
    }

    if(this.thumbnail != null) {
      this.thumbnail.delete();
      this.thumbnail = null;
    }

    if(this.shadow != null) {
      this.shadow.delete();
      this.shadow = null;
    }

    if(this.selector != null) {
      this.selector.delete();
      this.selector = null;
    }
  }
}
