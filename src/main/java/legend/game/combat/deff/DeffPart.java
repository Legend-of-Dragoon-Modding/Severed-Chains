package legend.game.combat.deff;

import legend.core.gpu.RECT;
import legend.game.types.CContainer;
import legend.game.types.TmdAnimationFile;
import legend.game.unpacker.FileData;

public class DeffPart {
  /**
   * MSB is type, LSB is index, middle bytes are some kind of ID?
   * <ul>
   *   <li>Type 0 - LMB</li>
   *   <li>Type 1 - TMD (animated, optimised)</li>
   *   <li>Type 2 - TMD (animated, unoptimised)</li>
   *   <li>Type 3 - TMD (single object, optimised)</li>
   *   <li>Type 4 - Sprite</li>
   *   <li>Type 5 - CMB</li>
   *   <li>Apparently there is a type 7</li>
   * </ul>
   */
  public final int flags_00;

  public DeffPart(final FileData data) {
    this.flags_00 = data.readInt(0);
  }

  public static class LmbType extends DeffPart {
    public final int type_04;
    public final Lmb lmb_08;

    public LmbType(final FileData data) {
      super(data);

      this.type_04 = data.readInt(0x4);

      final int lmbOffset = data.readInt(0x8);
      this.lmb_08 = switch(this.type_04) {
        case 0 -> new LmbType0(data.slice(lmbOffset));
        case 1 -> new LmbType1(data.slice(lmbOffset));
        case 2 -> new LmbType2(data.slice(lmbOffset));
        default -> throw new RuntimeException("Unsupported LMB type");
      };
    }
  }

  public static class TmdType extends DeffPart {
    public final TextureInfo[] textureInfo_08;
    public final CContainer tmd_0c;

    public TmdType(final String name, final FileData data) {
      super(data);

      final int textureOffset = data.readInt(0x8);
      final int tmdOffset = data.readInt(0xc);
      final int offset14 = data.readInt(0x14);

      if(textureOffset != tmdOffset) {
        this.textureInfo_08 = new TextureInfo[(tmdOffset - textureOffset) / 0x8];
        for(int i = 0; i < this.textureInfo_08.length; i++) {
          this.textureInfo_08[i] = new TextureInfo(data.slice(textureOffset + i * 0x8, 0x8));
        }
      } else {
        this.textureInfo_08 = null;
      }

      if(tmdOffset != offset14) {
        this.tmd_0c = new CContainer(name, data.slice(tmdOffset));
      } else {
        this.tmd_0c = null;
      }
    }
  }

  public static class AnimatedTmdType extends TmdType {
    public final Anim anim_14;

    public AnimatedTmdType(final String name, final FileData data) {
      super(name, data);

      final int animOffset = data.readInt(0x14);
      final int magic = data.readInt(animOffset);

      if(magic == Lmb.MAGIC) {
        this.anim_14 = new LmbType0(data.slice(animOffset));
      } else if(magic == Cmb.MAGIC) {
        this.anim_14 = new Cmb(data.slice(animOffset));
      } else {
        this.anim_14 = new TmdAnimationFile(data.slice(animOffset));
      }
    }
  }

  public static class TextureInfo {
    public final RECT vramPos_00 = new RECT();

    public TextureInfo(final FileData data) {
      data.readRect(0, this.vramPos_00);
    }
  }

  public static class SpriteType extends DeffPart {
    public final SpriteMetrics metrics_08;

    public SpriteType(final FileData data) {
      super(data);
      this.metrics_08 = new SpriteMetrics(data.slice(data.readInt(0xc), 0xc));
    }
  }

  public static class SpriteMetrics {
    public int u_00;
    public int v_02;
    public int w_04;
    public int h_06;
    public int clutX_08;
    public int clutY_0a;

    public SpriteMetrics(final FileData data) {
      this.u_00 = data.readUShort(0x0);
      this.v_02 = data.readUShort(0x2);
      this.w_04 = data.readUShort(0x4);
      this.h_06 = data.readUShort(0x6);
      this.clutX_08 = data.readUShort(0x8);
      this.clutY_0a = data.readUShort(0xa);
    }
  }
}
