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

public class WmapPopup {
  public enum ObjFields {
    PROMPT,
    OPTIONS,
    ALT_TEXT,
    THUMBNAIL
  }

  public float currentThumbnailBrightness;
  public float previousThumbnailBrightness;
  private final MV transforms = new MV();
  private TextObj prompt;
  final private Vector3f promptTranslation = new Vector3f();
  final private List<TextObj> options = new ArrayList<>();
  final private Vector3f optionsTranslation = new Vector3f();
  private boolean renderAltText;
  final private List<TextObj> altText = new ArrayList<>();
  final private Vector3f altTextTranslation = new Vector3f();
  private MeshObj thumbnail;
  final private Vector3f thumbnailTranslation = new Vector3f();

  public WmapPopup() {
  }

  public WmapPopup(final String prompt, final float z) {
    this.prompt = this.buildText(prompt);
    final int lines = (int)prompt.lines().count();
    this.promptTranslation.set(240.0f, 140.0f - lines * 7, z);
    this.optionsTranslation.set(240.0f, 170.0f, z);
    this.altTextTranslation.set(240.0f, 30.0f, z);
    this.thumbnailTranslation.set(0.0f, 0.0f, 56.0f);
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

  public void setShowAltText(final boolean renderAltText) {
    this.renderAltText = renderAltText;
  }

  private TextObj buildText(final String text) {
    return new TextBuilder()
      .text(text)
      .centred()
      .shadowed()
      .build();
  }

  public WmapPopup addOptionText(final String text) {
    this.options.add(this.buildText(text));
    return this;
  }

  public WmapPopup addAltText(final String text) {
    this.altText.add(this.buildText(text));
    return this;
  }

  public WmapPopup setImage(final short clutX, final short clutY, final short vramX, final short vramY, final float posX, final float posY, final float w, final float h, final int u, final int v, final float brightness) {
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
          this.transforms.transfer.y += i * 18.0f;
          RENDERER.queueOrthoOverlayModel(option, this.transforms);
        }
      }
    }

    if(this.thumbnail != null) {
      this.transforms.transfer.set(this.thumbnailTranslation);
      RENDERER.queueOrthoOverlayModel(this.thumbnail, this.transforms);
    }

    if(this.renderAltText && !this.altText.isEmpty()) {
      TextObj altText;
      for(int i = 0; i < this.altText.size(); i++) {
        altText = this.altText.get(i);
        if(altText != null) {
          this.transforms.transfer.set(this.altTextTranslation);
          this.transforms.transfer.y += i * 16.0f;
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
