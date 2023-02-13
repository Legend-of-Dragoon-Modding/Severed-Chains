package legend.game.combat.deff;

import legend.core.IoHelper;
import legend.core.gpu.RECT;
import legend.game.types.CContainer;
import legend.game.types.TmdAnimationFile;

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

  public DeffPart(final byte[] data, final int offset) {
    this.flags_00 = IoHelper.readInt(data, offset);
  }

  public static class LmbType extends DeffPart {
    public final int type_04;
    public final Lmb lmb_08;

    public LmbType(final byte[] data, final int offset) {
      super(data, offset);

      this.type_04 = IoHelper.readInt(data, 4);

      final int lmbOffset = offset + IoHelper.readInt(data, 8);
      this.lmb_08 = switch(this.type_04) {
        case 0 -> new LmbType0(data, lmbOffset);
        case 1 -> new LmbType1(data, lmbOffset);
        case 2 -> new LmbType2(data, lmbOffset);
        default -> throw new RuntimeException("Unsupported LMB type");
      };
    }
  }

  public static class TmdType extends DeffPart {
    public final TextureInfo[] textureInfo_08;
    public final CContainer tmd_0c;

    public TmdType(final byte[] data, final int offset) {
      super(data, offset);

      final int textureOffset = IoHelper.readInt(data, offset + 0x8);
      final int tmdOffset = IoHelper.readInt(data, offset + 0xc);

      this.textureInfo_08 = new TextureInfo[(tmdOffset - textureOffset) / 0x8];
      for(int i = 0; i < this.textureInfo_08.length; i++) {
        this.textureInfo_08[i] = new TextureInfo(data, offset + textureOffset + i * 0x8);
      }

      this.tmd_0c = new CContainer(data, offset + tmdOffset);
    }
  }

  public static class AnimatedTmdType extends TmdType {
    public final Anim anim_14;

    public AnimatedTmdType(final byte[] data, final int offset) {
      super(data, offset);

      final int animOffset = offset + IoHelper.readInt(data, offset + 0x14);
      final int magic = IoHelper.readInt(data, animOffset);

      if(magic == Lmb.MAGIC) {
        this.anim_14 = new LmbType0(data, animOffset);
      } else if(magic == Cmb.MAGIC) {
        this.anim_14 = new Cmb(data, animOffset);
      } else {
        this.anim_14 = new TmdAnimationFile(data, animOffset);
      }
    }
  }

  public static class TextureInfo {
    public final RECT vramPos_00 = new RECT();

    public TextureInfo(final byte[] data, final int offset) {
      IoHelper.readRect(data, offset, this.vramPos_00);
    }
  }

  public static class SpriteType extends DeffPart {
    public final SpriteMetrics metrics_08;

    public SpriteType(final byte[] data, final int offset) {
      super(data, offset);
      this.metrics_08 = new SpriteMetrics(data, offset + IoHelper.readInt(data, offset + 0x8));
    }
  }

  public static class SpriteMetrics {
    public int u_00;
    public int v_02;
    public int w_04;
    public int h_06;
    public int clutX_08;
    public int clutY_0a;

    public SpriteMetrics(final byte[] data, final int offset) {
      this.u_00 = IoHelper.readUShort(data, offset);
      this.v_02 = IoHelper.readUShort(data, offset + 0x2);
      this.w_04 = IoHelper.readUShort(data, offset + 0x4);
      this.h_06 = IoHelper.readUShort(data, offset + 0x6);
      this.clutX_08 = IoHelper.readUShort(data, offset + 0x8);
      this.clutY_0a = IoHelper.readUShort(data, offset + 0xa);
    }
  }

  public static class CmbType extends DeffPart {
    public final Cmb cmb_14;

    public CmbType(final byte[] data, final int offset) {
      super(data, offset);
      this.cmb_14 = new Cmb(data, offset + IoHelper.readInt(data, offset + 0x14));
    }
  }
}
