package legend.game.wmap;

import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.TextBuilder;
import legend.core.opengl.TextObj;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

import static legend.core.GameEngine.RENDERER;

public class MapTransitionPopup {
  public float previousThumbnailBrightness;
  public final MV transforms = new MV();
  public TextObj placeName;
  public TextObj dontEnter;
  public TextObj enter;
  public TextObj dest1PlaceName;
  public TextObj dest2PlaceName;
  public TextObj noFacilities;
  public final ArrayList<TextObj> services = new ArrayList<>(Collections.nCopies(5, null));
  public Obj placeImage;

  public MapTransitionPopup setImage(final short clutX, final short clutY, final short vramX, final short vramY, final float posX, final float posY, final float brightness) {
    if(this.placeImage != null) {
      this.placeImage.delete();
      this.placeImage = null;
    }

    this.placeImage = new QuadBuilder()
      .bpp(Bpp.BITS_8)
      .clut(clutX, clutY)
      .vramPos(vramX, vramY)
      .pos(posX, posY, 0)
      .uv(0, 0)
      .size(120, 90)
      .monochrome(brightness)
      .build();

    return this;
  }

  private TextObj buildText(final String text) {
    return new TextBuilder()
      .text(text)
      .centred()
      .shadowed()
      .build();
  }

  public MapTransitionPopup addText(final String textField, final String text, @Nullable final Integer index) {
    try {
      final Field field = this.getClass().getDeclaredField(textField);
      if(index == null) {
        if(field.get(this) == null) {
          field.set(this, this.buildText(text));
        }
      } else {
        if(((ArrayList<TextObj>)field.get(this)).get(index) == null) {
          ((ArrayList<TextObj>)field.get(this)).set(index, this.buildText(text));
        }
      }
    } catch(final NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    return this;
  }

  public void render(final String field, @Nullable final Integer index, final float x, final float y, final float z) {
    this.transforms.transfer.set(x, y, z);
    try {
      if(index == null) {
        RENDERER.queueOrthoOverlayModel((Obj)this.getClass().getDeclaredField(field).get(this), this.transforms);
      } else {
        RENDERER.queueOrthoOverlayModel(((ArrayList<TextObj>)this.getClass().getDeclaredField(field).get(this)).get(index), this.transforms);
      }
    } catch(final IllegalAccessException | NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  public void renderServices(final float z) {
    int servicesCount = 0;
    for(int i = 0; i < 5; i++) {
      if(this.services.get(i) != null) {
        this.render("services", i, 240, servicesCount * 16 + 30, z);
        servicesCount++;
      }
    }

    if(servicesCount == 0) {
      this.render("noFacilities", null, 240, 62, z);
    }
  }

  public void deleteField(final String fieldName, @Nullable final Integer index) {
    try {
      final Field field = this.getClass().getDeclaredField(fieldName);
      if(field.get(this) != null) {
        ((Obj)field.get(this)).delete();
        field.set(this, null);
      }
    } catch(final NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public void deallocatePlaceText(final boolean isBorderStation) {
    if(this.placeName != null) {
      this.placeName.delete();
      this.placeName = null;
    }

    if(this.dontEnter != null) {
      this.dontEnter.delete();
      this.dontEnter = null;
    }

    if(this.placeImage != null) {
      this.placeImage.delete();
      this.placeImage = null;
    }

    if(isBorderStation) { // Going to a different region
      if(this.dest1PlaceName != null) {
        this.dest1PlaceName.delete();
        this.dest1PlaceName = null;
      }

      if(this.dest2PlaceName != null) {
        this.dest2PlaceName.delete();
        this.dest2PlaceName = null;
      }
    } else {
      if(this.enter != null) {
        this.enter.delete();
        this.enter = null;
      }
    }

    if(this.noFacilities != null) {
      this.noFacilities.delete();
      this.noFacilities = null;
    }

    for(int i = 0; i < this.services.size(); i++) {
      if(this.services.get(i) != null) {
        this.services.get(i).delete();
        this.services.set(i, null);
      }
    }
  }
}
