package legend.game.submap;

import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.gpu.Rect4i;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.tim.Tim;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import org.joml.Vector2i;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;

public class TheEndEffectDatab0 {
  private final FileData clutData_800d4bd4;
  private Tim tim_800d4bf0;

  private final Rect4i clutRect_800d6b48 = new Rect4i(640, 368, 16, 1);

  private final Vector2i tpage_800f9e5c = new Vector2i();
  private final Vector2i clut_800f9e5e = new Vector2i();

  /** short */
  private boolean shouldRender_00;
  /** short; inverted from retail */
  private boolean shouldBrighten_02;
  /** short; inverted from retail */
  private boolean shouldAdjustBrightness_04;
  /** short */
  private boolean shouldTickClut_06;
  /** short */
  private int tick_08;

  /** .16 */
  private float brightness_0c;
  /** .16 */
  private final float[] clutStep_10 = new float[16];
  /** .16 */
  private final float[] currClut_50 = new float[16];
  private final float[] finalClut_90 = new float[16];

  private final MV transforms = new MV();
  private Obj text;

  public TheEndEffectDatab0() {
    this.shouldBrighten_02 = true;
    this.shouldAdjustBrightness_04 = true;
    this.clutData_800d4bd4 = new FileData(new byte[0x20]);
  }

  public void setTim(final Tim tim) {
    this.tim_800d4bf0 = tim;
  }

  public FileData getClutData() {
    return this.clutData_800d4bd4;
  }

  public Rect4i getClutRect() {
    return this.clutRect_800d6b48;
  }

  @Method(0x800eef6cL)
  public void initFlameClutAnimation() {
    //LAB_800eef94
    for(int i = 0; i < 16; i++) {
      //LAB_800eefac
      this.finalClut_90[i] = this.clutData_800d4bd4.readUShort(i * 0x2) & 0x1f;
      this.clutStep_10[i] = this.finalClut_90[i] / 60.0f;
      this.currClut_50[i] = 0.0f;
      this.clutData_800d4bd4.writeShort(i * 0x2, 0x8000);
    }

    GPU.uploadData15(this.clutRect_800d6b48, this.clutData_800d4bd4);
  }

  @Method(0x800f4244L)
  public void uploadTheEndTim() {
    //LAB_800f427c
    if(this.tim_800d4bf0.hasClut()) {
      final Rect4i clutRect = this.tim_800d4bf0.getClutRect();
      this.clut_800f9e5e.set(clutRect.x, clutRect.y);
      GPU.uploadData15(clutRect, this.tim_800d4bf0.getClutData());
    }

    //LAB_800f42d0
    final Rect4i imageRect = this.tim_800d4bf0.getImageRect();
    this.tpage_800f9e5c.set(imageRect.x, imageRect.y);
    GPU.uploadData15(imageRect, this.tim_800d4bf0.getImageData());
  }

  @Method(0x800eec10L)
  public void tickFlameClutAnimation() {
    //LAB_800eec1c
    for(int i = 0; i < 16; i++) {
      this.currClut_50[i] += this.clutStep_10[i] / (3 - vsyncMode_8007a3b8);

      final float maxColour = this.finalClut_90[i];
      if(maxColour < this.currClut_50[i]) {
        this.currClut_50[i] = maxColour;
      }

      //LAB_800eec5c
      final int b = (int)(this.currClut_50[i]) << 10;
      final int g = (int)(this.currClut_50[i]) << 5;
      final int r = (int)(this.currClut_50[i]);
      this.clutData_800d4bd4.writeShort(i * 0x2, 0x8000 | b | g | r);
    }
  }

  @Method(0x800ee9e0L)
  public void render() {
    if(this.tick_08 == 500 * (3 - vsyncMode_8007a3b8)) {
      this.shouldRender_00 = true;
      this.shouldBrighten_02 = true;
      this.shouldTickClut_06 = true;

      this.text = new QuadBuilder("TheEnd")
        .bpp(Bpp.BITS_4)
        .vramPos(this.tpage_800f9e5c.x, this.tpage_800f9e5c.y >= 256 ? 256 : 0)
        .clut(this.clut_800f9e5e.x, this.clut_800f9e5e.y)
        .translucency(Translucency.B_PLUS_F)
        .monochrome(1.0f)
        .uv(0, 128)
        .pos(-188.0f, 18.0f, 160.0f)
        .size(192.0f, 72.0f)
        .build();
    }

    //LAB_800eea24
    if(this.shouldRender_00) {
      if(this.shouldAdjustBrightness_04) {
        if(this.shouldBrighten_02) {
          this.brightness_0c += (1.0f / 96.0f) / (3 - vsyncMode_8007a3b8);

          if(this.brightness_0c > 1.0f) {
            this.brightness_0c = 1.0f;
            this.shouldBrighten_02 = false;
          }
        } else {
          //LAB_800eead8
          this.brightness_0c -= (1.0f / 96.0f) / (3 - vsyncMode_8007a3b8);

          if(this.brightness_0c < 0.5f) {
            this.brightness_0c = 0.5f;
            this.shouldAdjustBrightness_04 = false;
          }
        }
      } else {
        //LAB_800eeb08
        this.brightness_0c = 0.5f;
      }

      //LAB_800eeb0c
      this.transforms.transfer.set(GPU.getOffsetX(), GPU.getOffsetY(), 0.0f);
      RENDERER.queueOrthoModel(this.text, this.transforms, QueuedModelStandard.class)
        .monochrome(this.brightness_0c);
    }

    //LAB_800eeb78
    if(this.shouldTickClut_06) {
      this.tickFlameClutAnimation();

      if(this.tick_08 == 561 * (3 - vsyncMode_8007a3b8)) {
        this.shouldTickClut_06 = false;
      }
    }

    //LAB_800eeba8
    //LAB_800eebac
    this.tick_08++;
  }

  public void deallocate() {
    if(this.text != null) {
      this.text.delete();
    }
  }
}
