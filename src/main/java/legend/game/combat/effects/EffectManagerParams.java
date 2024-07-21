package legend.game.combat.effects;

import org.joml.Vector3f;
import org.joml.Vector3i;

public abstract class EffectManagerParams<T extends EffectManagerParams<T>> {
  /**
   * <ul>
   *   <li>0x8 - normalize light matrix before use</li>
   *   <li>0x40 - disable custom lighting</li>
   *   <li>0x400_0000 - apply rotation and scale</li>
   *   <li>0x3000_0000 - texpage translucency mode (bits 28-29)</li>
   *   <li>0x4000_0000 - use effect manager's translucency instead of effect's</li>
   *   <li>0x8000_0000 - error</li>
   * </ul>
   */
  public int flags_00;
  public final Vector3f trans_04 = new Vector3f();
  public final Vector3f rot_10 = new Vector3f();
  public final Vector3f scale_16 = new Vector3f();
  public final Vector3i colour_1c = new Vector3i();
  public int z_22 = 1;

  public void set(final T other) {
    this.flags_00 = other.flags_00;
    this.trans_04.set(other.trans_04);
    this.rot_10.set(other.rot_10);
    this.scale_16.set(other.scale_16);
    this.colour_1c.set(other.colour_1c);
    this.z_22 = other.z_22;
  }

  public abstract int get24(final int index);
  public abstract void set24(final int index, final int val);

  public static class AnimType extends EffectManagerParams<AnimType> {
    public int ticks_24;
    public int scale_28;
    public int _2c;
    public int _30;

    @Override
    public void set(final AnimType other) {
      super.set(other);
      this.ticks_24 = other.ticks_24;
      this.scale_28 = other.scale_28;
      this._2c = other._2c;
      this._30 = other._30;
    }

    @Override
    public int get24(final int index) {
      return switch(index) {
        case 0 -> this.ticks_24;
        case 1 -> this.scale_28;
        case 2 -> this._2c;
        case 3 -> this._30;
        default -> throw new IllegalArgumentException("Invalid index " + index);
      };
    }

    @Override
    public void set24(final int index, final int val) {
      switch(index) {
        case 0 -> this.ticks_24 = val;
        case 1 -> this.scale_28 = val;
        case 2 -> this._2c = val;
        case 3 -> this._30 = val;
        default -> throw new IllegalArgumentException("Invalid index " + index);
      }
    }
  }

  public static class ShirleyType extends EffectManagerParams<ShirleyType> {
    public int width_24;
    public int height_28;
    public int depth_2c;
    public int _30;

    @Override
    public void set(final ShirleyType other) {
      super.set(other);
      this.width_24 = other.width_24;
      this.height_28 = other.height_28;
      this.depth_2c = other.depth_2c;
      this._30 = other._30;
    }

    @Override
    public int get24(final int index) {
      return switch(index) {
        case 0 -> this.width_24;
        case 1 -> this.height_28;
        case 2 -> this.depth_2c;
        case 3 -> this._30;
        default -> throw new IllegalArgumentException("Invalid index " + index);
      };
    }

    @Override
    public void set24(final int index, final int val) {
      switch(index) {
        case 0 -> this.width_24 = val;
        case 1 -> this.height_28 = val;
        case 2 -> this.depth_2c = val;
        case 3 -> this._30 = val;
        default -> throw new IllegalArgumentException("Invalid index " + index);
      }
    }
  }

  public static class ColourType extends EffectManagerParams<ColourType> {
    public int _24;
    /** 12-bit fixed-point */
    public int r_28;
    /** 12-bit fixed-point */
    public int g_2c;
    /** 12-bit fixed-point */
    public int b_30;

    @Override
    public void set(final ColourType other) {
      super.set(other);
      this._24 = other._24;
      this.r_28 = other.r_28;
      this.g_2c = other.g_2c;
      this.b_30 = other.b_30;
    }

    @Override
    public int get24(final int index) {
      return switch(index) {
        case 0 -> this._24;
        case 1 -> this.r_28;
        case 2 -> this.g_2c;
        case 3 -> this.b_30;
        default -> throw new IllegalArgumentException("Invalid index " + index);
      };
    }

    @Override
    public void set24(final int index, final int val) {
      switch(index) {
        case 0 -> this._24 = val;
        case 1 -> this.r_28 = val;
        case 2 -> this.g_2c = val;
        case 3 -> this.b_30 = val;
        default -> throw new IllegalArgumentException("Invalid index " + index);
      }
    }
  }

  public static class RadialGradientType extends EffectManagerParams<RadialGradientType> {
    public int colour_24;
    public int size_28;
    public int z_2c;
    public int _30;

    @Override
    public void set(final RadialGradientType other) {
      super.set(other);
      this.colour_24 = other.colour_24;
      this.size_28 = other.size_28;
      this.z_2c = other.z_2c;
      this._30 = other._30;
    }

    @Override
    public int get24(final int index) {
      return switch(index) {
        case 0 -> this.colour_24;
        case 1 -> this.size_28;
        case 2 -> this.z_2c;
        case 3 -> this._30;
        default -> throw new IllegalArgumentException("Invalid index " + index);
      };
    }

    @Override
    public void set24(final int index, final int val) {
      switch(index) {
        case 0 -> this.colour_24 = val;
        case 1 -> this.size_28 = val;
        case 2 -> this.z_2c = val;
        case 3 -> this._30 = val;
        default -> throw new IllegalArgumentException("Invalid index " + index);
      }
    }
  }

  public static class WeaponTrailType extends EffectManagerParams<WeaponTrailType> {
    public int vertexComponent_24;
    public int _28;
    public int _2c;
    public int _30;

    @Override
    public void set(final WeaponTrailType other) {
      super.set(other);
      this.vertexComponent_24 = other.vertexComponent_24;
      this._28 = other._28;
      this._2c = other._2c;
      this._30 = other._30;
    }

    @Override
    public int get24(final int index) {
      return switch(index) {
        case 0 -> this.vertexComponent_24;
        case 1 -> this._28;
        case 2 -> this._2c;
        case 3 -> this._30;
        default -> throw new IllegalArgumentException("Invalid index " + index);
      };
    }

    @Override
    public void set24(final int index, final int val) {
      switch(index) {
        case 0 -> this.vertexComponent_24 = val;
        case 1 -> this._28 = val;
        case 2 -> this._2c = val;
        case 3 -> this._30 = val;
        default -> throw new IllegalArgumentException("Invalid index " + index);
      }
    }
  }

  public static class ParticleType extends EffectManagerParams<ParticleType> {
    public int flags_24;
    public int _28;
    public int _2c;
    public int y_30;

    @Override
    public void set(final ParticleType other) {
      super.set(other);
      this.flags_24 = other.flags_24;
      this._28 = other._28;
      this._2c = other._2c;
      this.y_30 = other.y_30;
    }

    @Override
    public int get24(final int index) {
      return switch(index) {
        case 0 -> this.flags_24;
        case 1 -> this._28;
        case 2 -> this._2c;
        case 3 -> this.y_30;
        default -> throw new IllegalArgumentException("Invalid index " + index);
      };
    }

    @Override
    public void set24(final int index, final int val) {
      switch(index) {
        case 0 -> this.flags_24 = val;
        case 1 -> this._28 = val;
        case 2 -> this._2c = val;
        case 3 -> this.y_30 = val;
        default -> throw new IllegalArgumentException("Invalid index " + index);
      }
    }
  }

  public static class ElectricityType extends EffectManagerParams<ElectricityType> {
    /** Used for flickering, each bit corresponds to a frame and if set that frame will not render */
    public int shouldRenderFrameBits_24;
    /** Multiple packed values */
    public int _28;
    public int _2c;
    public int sizeDivisor_30;

    @Override
    public void set(final ElectricityType other) {
      super.set(other);
      this.shouldRenderFrameBits_24 = other.shouldRenderFrameBits_24;
      this._28 = other._28;
      this._2c = other._2c;
      this.sizeDivisor_30 = other.sizeDivisor_30;
    }

    @Override
    public int get24(final int index) {
      return switch(index) {
        case 0 -> this.shouldRenderFrameBits_24;
        case 1 -> this._28;
        case 2 -> this._2c;
        case 3 -> this.sizeDivisor_30;
        default -> throw new IllegalArgumentException("Invalid index " + index);
      };
    }

    @Override
    public void set24(final int index, final int val) {
      switch(index) {
        case 0 -> this.shouldRenderFrameBits_24 = val;
        case 1 -> this._28 = val;
        case 2 -> this._2c = val;
        case 3 -> this.sizeDivisor_30 = val;
        default -> throw new IllegalArgumentException("Invalid index " + index);
      }
    }
  }

  public static class FrozenJetType extends EffectManagerParams<FrozenJetType> {
    public int _24;
    public int _28;
    public int _2c;
    public int _30;

    @Override
    public void set(final FrozenJetType other) {
      super.set(other);
      this._24 = other._24;
      this._28 = other._28;
      this._2c = other._2c;
      this._30 = other._30;
    }

    @Override
    public int get24(final int index) {
      return switch(index) {
        case 0 -> this._24;
        case 1 -> this._28;
        case 2 -> this._2c;
        case 3 -> this._30;
        default -> throw new IllegalArgumentException("Invalid index " + index);
      };
    }

    @Override
    public void set24(final int index, final int val) {
      switch(index) {
        case 0 -> this._24 = val;
        case 1 -> this._28 = val;
        case 2 -> this._2c = val;
        case 3 -> this._30 = val;
        default -> throw new IllegalArgumentException("Invalid index " + index);
      }
    }
  }

  /** This probably shouldn't have values, but certain generic attacks like Michael's opener set values on a generic effect manager */
  public static class VoidType extends EffectManagerParams<VoidType> {
    public int _24;
    public int _28;
    public int _2c;
    public int _30;

    @Override
    public int get24(final int index) {
      return switch(index) {
        case 0 -> this._24;
        case 1 -> this._28;
        case 2 -> this._2c;
        case 3 -> this._30;
        default -> throw new IllegalArgumentException("Invalid index " + index);
      };
    }

    @Override
    public void set24(final int index, final int val) {
      switch(index) {
        case 0 -> this._24 = val;
        case 1 -> this._28 = val;
        case 2 -> this._2c = val;
        case 3 -> this._30 = val;
        default -> throw new IllegalArgumentException("Invalid index " + index);
      }
    }
  }
}
