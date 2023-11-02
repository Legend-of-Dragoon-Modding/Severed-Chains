package legend.game.wmap;

import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.opengl.MeshObj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.TextBuilder;
import legend.core.opengl.TextObj;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.RENDERER;

public class WmapPromptPopup {
  public enum ObjFields {
    PROMPT,
    OPTIONS,
    ALT_TEXT,
    THUMBNAIL
  }

  private final MV transforms = new MV();

  private TextObj prompt;
  final private Vector3f promptTranslation = new Vector3f();

  final private List<TextObj> options = new ArrayList<>();
  private float optionSpacing = 20.0f;
  final private Vector3f optionsTranslation = new Vector3f();

  private boolean renderAltText;
  final private List<TextObj> altText = new ArrayList<>();
  private float altTextSpacing = 16.0f;
  final private Vector3f altTextTranslation = new Vector3f();

  private MeshObj thumbnail;
  final private Vector3f thumbnailTranslation = new Vector3f();
  private float currentThumbnailBrightness;
  private float previousThumbnailBrightness;

  public WmapPromptPopup() {
  }

  public WmapPromptPopup(final String prompt, final float textZ) {
    this.prompt = this.buildText(prompt);
    final int lines = (int)prompt.lines().count();
    this.promptTranslation.set(240.0f, 140.0f - lines * 7, textZ);
    this.optionsTranslation.set(240.0f, 170.0f, textZ);
    this.altTextTranslation.set(240.0f, 30.0f, textZ);
    this.thumbnailTranslation.set(0.0f, 0.0f, textZ + 4.0f);
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

  private TextObj buildText(final String text) {
    return new TextBuilder()
      .text(text)
      .centred()
      .shadowed()
      .build();
  }

  public WmapPromptPopup addOptionText(final String text) {
    this.options.add(this.buildText(text));
    return this;
  }

  public WmapPromptPopup addAltText(final String text) {
    this.altText.add(this.buildText(text));
    return this;
  }

  public WmapPromptPopup setImage(final short clutX, final short clutY, final short vramX, final short vramY, final float posX, final float posY, final float w, final float h, final int u, final int v, final float brightness) {
    if(this.thumbnail != null && this.currentThumbnailBrightness != this.previousThumbnailBrightness) {
      this.thumbnail.delete();
      this.thumbnail = null;
    }

    if(this.thumbnail == null) {
      this.thumbnail = new QuadBuilder()
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

  public void render() {
    if(this.thumbnail != null) {
      this.transforms.transfer.set(this.thumbnailTranslation);
      RENDERER.queueOrthoOverlayModel(this.thumbnail, this.transforms);
    }

    if(this.prompt != null) {
      this.transforms.transfer.set(this.promptTranslation);
      RENDERER.queueOrthoOverlayModel(this.prompt, this.transforms);
    }

    if(!this.options.isEmpty()) {
      TextObj option;
      for(int i = 0; i < this.options.size(); i++) {
        option = this.options.get(i);
        if(option != null) {
          this.transforms.transfer.set(this.optionsTranslation);
          this.transforms.transfer.y += i * this.optionSpacing;
          RENDERER.queueOrthoOverlayModel(option, this.transforms);
        }
      }
    }

    if(this.renderAltText && !this.altText.isEmpty()) {
      TextObj altText;
      for(int i = 0; i < this.altText.size(); i++) {
        altText = this.altText.get(i);
        if(altText != null) {
          this.transforms.transfer.set(this.altTextTranslation);
          this.transforms.transfer.y += i * this.altTextSpacing;
          RENDERER.queueOrthoOverlayModel(altText, this.transforms);
        }
      }
    }
  }

  public void deallocatePlaceText() {
    if(this.prompt != null) {
      this.prompt.delete();
      this.prompt = null;
    }

    if(!this.options.isEmpty()) {
      for(int i = 0; i < this.options.size(); i++) {
        if(this.options.get(i) != null) {
          this.options.get(i).delete();
          this.options.set(i, null);
        }
      }
    }

    if(!this.altText.isEmpty()) {
      for(int i = 0; i < this.altText.size(); i++) {
        if(this.altText.get(i) != null) {
          this.altText.get(i).delete();
          this.altText.set(i, null);
        }
      }
    }

    if(this.thumbnail != null) {
      this.thumbnail.delete();
      this.thumbnail = null;
    }
  }
}
