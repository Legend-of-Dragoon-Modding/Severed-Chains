package legend.game.combat.effects;

import legend.core.MathHelper;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gte.MV;
import legend.core.gte.TmdObjTable1c;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.TmdObjLoader;
import legend.game.combat.deff.DeffPart;
import legend.game.combat.deff.Lmb;
import legend.game.combat.deff.LmbTransforms14;
import legend.game.combat.deff.LmbType0;
import legend.game.combat.deff.LmbType1;
import legend.game.combat.deff.LmbType2;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.game.Scus94491BpeSegment.projectionPlaneDistance_1f8003f8;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8003.perspectiveTransform;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static legend.game.combat.Battle.deffManager_800c693c;
import static legend.game.combat.SEffe.FUN_800e61e4;
import static legend.game.combat.SEffe.FUN_800e62a8;
import static legend.game.combat.SEffe.calculateEffectTransforms;
import static legend.game.combat.SEffe.getRotationAndScaleFromTransforms;
import static legend.game.combat.SEffe.renderTmdSpriteEffect;

public class LmbAnimationEffect5c implements Effect<EffectManagerParams.AnimType> {
  /** Related to processing type 2 LMBs */
  private static final byte[] lmbType2TransformationData_8011a048 = new byte[0x300];

  public int lmbType_00;
  public int _04;
  public int keyframeCount_08;
  public Lmb lmb_0c;
  /** May have two copies of the LMB table for some reason */
  public LmbTransforms14[] lmbTransforms_10;
  public final int[] _14 = new int[8];
  /**
   * <ul>
   *   <li>0x4 - colour</li>
   *   <li>0x8 - scale</li>
   * </ul>
   */
  public int flags_34;
  public int _38;
  public int _3c;
  public int _40;

  public int deffTmdFlags_48;
  public TmdObjTable1c deffTmdObjTable_4c;
  public Obj obj;
  public int deffSpriteFlags_50;
  public final SpriteMetrics08 metrics_54 = new SpriteMetrics08();

  @Override
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state) {

  }

  @Override
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state) {
    final EffectManagerData6c<EffectManagerParams.AnimType> manager = state.innerStruct_00;
    final int flags = manager.params_10.flags_00;
    if(flags >= 0) { // No errors
      int s1 = Math.max(0, manager.params_10.ticks_24) % (this.keyframeCount_08 * 2) << 12;
      tmdGp0Tpage_1f8003ec = flags >>> 23 & 0x60; // tpage
      zOffset_1f8003e8 = manager.params_10.z_22;
      if((manager.params_10.flags_00 & 0x40) == 0) {
        FUN_800e61e4(manager.params_10.colour_1c.x / 128.0f, manager.params_10.colour_1c.y / 128.0f, manager.params_10.colour_1c.z / 128.0f);
      }

      //LAB_80117ac0
      //LAB_80117acc
      final EffectManagerData6c<EffectManagerParams.AnimType> anim = new EffectManagerData6c<>("Temp 2", new EffectManagerParams.AnimType());
      anim.set(manager);

      final MV sp0x80 = new MV();
      calculateEffectTransforms(sp0x80, manager);

      final int type = this.lmbType_00 & 0x7;
      if(type == 0) {
        //LAB_80117b50
        this.processLmbType0(manager, s1, sp0x80);
      } else if(type == 1) {
        //LAB_80117b68
        this.processLmbType1(manager, s1, sp0x80);
      } else if(type == 2) {
        //LAB_80117b80
        this.processLmbType2(manager, s1, sp0x80);
      }

      //LAB_80117b8c
      final int steps = this._38;

      if(steps >= 2) {
        final int spe4 = this._3c;
        int accumulatorR;
        int accumulatorG;
        int accumulatorB;
        final int stepR;
        final int stepG;
        final int stepB;
        if((this.flags_34 & 0x4) != 0) {
          final int v0 = this._40 - 0x1000;
          stepR = anim.params_10.colour_1c.x * v0 / steps;
          stepG = anim.params_10.colour_1c.y * v0 / steps;
          stepB = anim.params_10.colour_1c.z * v0 / steps;
          accumulatorR = anim.params_10.colour_1c.x << 12;
          accumulatorG = anim.params_10.colour_1c.y << 12;
          accumulatorB = anim.params_10.colour_1c.z << 12;
        } else {
          //LAB_80117c30
          accumulatorR = 0;
          accumulatorG = 0;
          accumulatorB = 0;
          stepR = 0;
          stepG = 0;
          stepB = 0;
        }

        //LAB_80117c48
        int accumulatorScale = 0;
        int stepScale = 0;
        if((this.flags_34 & 0x8) != 0) {
          final int t4 = anim.params_10.scale_28 * (this._40 - 0x1000);
          accumulatorScale = anim.params_10.scale_28 << 12;
          stepScale = t4 / steps;
        }

        //LAB_80117c88
        s1 -= spe4;

        //LAB_80117ca0
        for(int i = 1; i < steps && s1 >= 0; i++) {
          if((this.flags_34 & 0x4) != 0) {
            accumulatorR += stepR;
            accumulatorG += stepG;
            accumulatorB += stepB;
            manager.params_10.colour_1c.x = accumulatorR >> 12;
            manager.params_10.colour_1c.y = accumulatorG >> 12;
            manager.params_10.colour_1c.z = accumulatorB >> 12;
          }

          //LAB_80117d1c
          if((this.flags_34 & 0x8) != 0) {
            accumulatorScale += stepScale;
            manager.params_10.scale_28 = accumulatorScale >> 12;
          }

          //LAB_80117d54
          if(type == 0) {
            //LAB_80117dc4
            this.processLmbType0(manager, s1, sp0x80);
          } else if(type == 1) {
            //LAB_80117ddc
            this.processLmbType1(manager, s1, sp0x80);
          } else if(type == 2) {
            //LAB_80117df4
            this.processLmbType2(manager, s1, sp0x80);
          }

          //LAB_80117e04
          s1 -= spe4;
        }
      }

      //LAB_80117e14
      //LAB_80117e20
      manager.set(anim);

      if((manager.params_10.flags_00 & 0x40) == 0) {
        FUN_800e62a8();
      }
    }

    //LAB_80117e80
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state) {
    if(this.obj != null) {
      this.obj.delete();
      this.obj = null;
    }
  }

  /**
   * TODO used for rendering deffs of some kind, possibly sprite deffs?
   *   Uses CTMD render pipeline if type == 0x300_0000
   */
  @Method(0x8011619cL)
  private void FUN_8011619c(final EffectManagerData6c<EffectManagerParams.AnimType> manager, final int deffFlags, final MV matrix) {
    final MV sp0x10 = new MV();
    sp0x10.scaling(manager.params_10.scale_16);
    sp0x10.rotateZYX(manager.params_10.rot_10);
    sp0x10.transfer.set(manager.params_10.trans_04);
    sp0x10.compose(matrix, sp0x10);
    final float scale = manager.params_10.scale_28 / (float)0x1000;
    sp0x10.scaleLocal(scale, scale, scale);
    manager.params_10.scale_16.mul(scale);

    final int type = deffFlags & 0xff00_0000;
    if(type == 0x300_0000) {
      //LAB_80116708
      final TmdObjTable1c tmdObjTable;
      // If we already have it cached
      if(this.deffTmdFlags_48 == deffFlags) {
        tmdObjTable = this.deffTmdObjTable_4c;
      } else {
        //LAB_80116724
        if((deffFlags & 0xf_ff00) == 0xf_ff00) {
          tmdObjTable = deffManager_800c693c.tmds_2f8[deffFlags & 0xff];
        } else {
          //LAB_80116750
          tmdObjTable = ((DeffPart.TmdType)deffManager_800c693c.getDeffPart(deffFlags | 0x300_0000)).tmd_0c.tmdPtr_00.tmd.objTable[0];
        }

        //LAB_8011676c
        // Cache it
        this.deffTmdFlags_48 = deffFlags;
        this.deffTmdObjTable_4c = tmdObjTable;

        if(this.obj != null) {
          this.obj.delete();
        }

        this.obj = TmdObjLoader.fromObjTable(manager.name, this.deffTmdObjTable_4c);
      }

      //LAB_80116778
      renderTmdSpriteEffect(tmdObjTable, this.obj, manager.params_10, sp0x10);
    } else if(type == 0x400_0000) {
      if(this.deffSpriteFlags_50 != deffFlags) {
        //LAB_801162e8
        final BillboardSpriteEffect0c sp0x48 = new BillboardSpriteEffect0c();

        sp0x48.set(deffFlags);
        this.metrics_54.u_00 = sp0x48.metrics_04.u_00;
        this.metrics_54.v_02 = sp0x48.metrics_04.v_02;
        this.metrics_54.w_04 = sp0x48.metrics_04.w_04;
        this.metrics_54.h_05 = sp0x48.metrics_04.h_05;
        this.metrics_54.clut_06 = sp0x48.metrics_04.clut_06;
        this.deffSpriteFlags_50 = deffFlags;
      }

      final int u = (this.metrics_54.u_00 & 0x3f) * 4;
      final int v = this.metrics_54.v_02 & 0xff;
      final int w = this.metrics_54.w_04 & 0xff;
      final int h = this.metrics_54.h_05 & 0xff;
      final int clut = this.metrics_54.clut_06;

      //LAB_8011633c
      final Vector3f sp0x58 = new Vector3f();
      final MV sp0x60 = new MV();
      final Vector2f sp0x80 = new Vector2f();
      sp0x10.compose(worldToScreenMatrix_800c3548, sp0x60);
      GTE.setTransforms(sp0x60);

      final float z = perspectiveTransform(sp0x58, sp0x80);
      if(z >= 0x50) {
        //LAB_801163c4
        final float a1 = projectionPlaneDistance_1f8003f8 * 2.0f / z * manager.params_10.scale_16.z;
        final float l = -w / 2.0f * a1;
        final float r = w / 2.0f * a1;
        final float t = -h / 2.0f * a1;
        final float b = h / 2.0f * a1;
        final float sin = MathHelper.sin(manager.params_10.rot_10.z);
        final float cos = MathHelper.cos(manager.params_10.rot_10.z);
        final float sinL = l * sin;
        final float cosL = l * cos;
        final float sinR = r * sin;
        final float cosR = r * cos;
        final float sinT = t * sin;
        final float cosT = t * cos;
        final float sinB = b * sin;
        final float cosB = b * cos;

        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .clut((clut & 0b111111) * 16, clut >>> 6)
          .vramPos(this.metrics_54.u_00 & 0x3c0, (this.metrics_54.v_02 & 0x100) != 0 ? 256 : 0)
          .rgb(manager.params_10.colour_1c)
          .pos(0, sp0x80.x + cosL - sinT, sp0x80.y + sinL + cosT)
          .pos(1, sp0x80.x + cosR - sinT, sp0x80.y + sinR + cosT)
          .pos(2, sp0x80.x + cosL - sinB, sp0x80.y + sinL + cosB)
          .pos(3, sp0x80.x + cosR - sinB, sp0x80.y + sinR + cosB)
          .uv(0, u, v)
          .uv(1, u + w - 1, v)
          .uv(2, u, v + h - 1)
          .uv(3, u + w - 1, v + h - 1);

        if((manager.params_10.flags_00 >>> 30 & 1) != 0) {
          cmd.translucent(Translucency.of(manager.params_10.flags_00 >>> 28 & 0b11));
        }

        GPU.queueCommand(z / 4.0f, cmd);
      }
    } else {
      //LAB_80116790
      final ScriptState<EffectManagerData6c<?>> state = (ScriptState<EffectManagerData6c<?>>)scriptStatePtrArr_800bc1c0[deffFlags];
      final EffectManagerData6c<?> manager2 = state.innerStruct_00;
      manager.params_10.trans_04.set(sp0x10.transfer);
      getRotationAndScaleFromTransforms(manager.params_10.rot_10, manager.params_10.scale_16, sp0x10);

      final int oldScriptIndex = manager2.scriptIndex_0c;
      final int oldCoord2Index = manager2.coord2Index_0d;
      manager2.scriptIndex_0c = manager.myScriptState_0e.index;
      manager2.coord2Index_0d = -1;
      final int r = manager2.params_10.colour_1c.x;
      final int g = manager2.params_10.colour_1c.y;
      final int b = manager2.params_10.colour_1c.z;
      // As far as I can tell, using R for each of these is right...
      manager2.params_10.colour_1c.x = manager.params_10.colour_1c.x * manager2.params_10.colour_1c.x / 128;
      manager2.params_10.colour_1c.y = manager.params_10.colour_1c.x * manager2.params_10.colour_1c.y / 128;
      manager2.params_10.colour_1c.z = manager.params_10.colour_1c.x * manager2.params_10.colour_1c.z / 128;
      state.renderer_08.accept(state, manager2);
      manager2.params_10.colour_1c.x = r;
      manager2.params_10.colour_1c.y = g;
      manager2.params_10.colour_1c.z = b;
      manager2.scriptIndex_0c = oldScriptIndex;
      manager2.coord2Index_0d = oldCoord2Index;
    }

    //LAB_801168b8
  }

  @Method(0x801168e8L)
  private void processLmbType0(final EffectManagerData6c<EffectManagerParams.AnimType> manager, final int a2, final MV matrix) {
    final LmbType0 lmb = (LmbType0)this.lmb_0c;
    final int s6 = a2 / 0x2000;
    final int v1 = s6 + 1;
    final int s0 = a2 & 0x1fff;
    final int s1 = 0x2000 - s0;
    final int fp = v1 % lmb.partAnimations_08[0].count_04;

    //LAB_80116960
    for(int i = 0; i < lmb.objectCount_04; i++) {
      if(this._14[lmb.partAnimations_08[i]._00] != 0) {
        final LmbTransforms14 a1 = lmb.partAnimations_08[i].keyframes_08[s6];
        final LmbTransforms14 a0 = lmb.partAnimations_08[i].keyframes_08[fp];
        manager.params_10.rot_10.set(a1.rot_0c);
        manager.params_10.trans_04.x = (a1.trans_06.x * s1 + a0.trans_06.x * s0) / 0x2000;
        manager.params_10.trans_04.y = (a1.trans_06.y * s1 + a0.trans_06.y * s0) / 0x2000;
        manager.params_10.trans_04.z = (a1.trans_06.z * s1 + a0.trans_06.z * s0) / 0x2000;
        manager.params_10.scale_16.x = (a1.scale_00.x * s1 + a0.scale_00.x * s0) / 0x2000;
        manager.params_10.scale_16.y = (a1.scale_00.y * s1 + a0.scale_00.y * s0) / 0x2000;
        manager.params_10.scale_16.z = (a1.scale_00.z * s1 + a0.scale_00.z * s0) / 0x2000;
        this.FUN_8011619c(manager, this._14[lmb.partAnimations_08[i]._00], matrix);
      }
    }
  }

  @Method(0x80116b7cL)
  private void processLmbType1(final EffectManagerData6c<EffectManagerParams.AnimType> manager, final int t0, final MV matrix) {
    final LmbType1 lmb = (LmbType1)this.lmb_0c;
    int a0 = t0 >> 13;
    float lerpScale = (t0 & 0x1fff) / (float)0x2000;
    int s5 = (a0 + 1) % lmb.keyframeCount_0a;
    final LmbTransforms14[] s7 = this.lmbTransforms_10;
    if(this._04 != t0) {
      if(s5 == 0) {
        if(a0 == 0) {
          return;
        }

        s5 = a0;
        a0 = 0;
        lerpScale = 1.0f - lerpScale;
      }

      //LAB_80116c20
      if(a0 == 0) {
        for(int i = 0; i < lmb.objectCount_04; i++) {
          s7[i].set(lmb._10[i]);
        }
      } else {
        //LAB_80116c50
        int a0_0 = (a0 - 1) * lmb._08 / 2;

        //LAB_80116c80
        for(int i = 0; i < lmb.objectCount_04; i++) {
          final LmbTransforms14 transforms = s7[i];
          final LmbType1.Sub04 v1 = lmb._0c[i];

          if((v1.transformsType_00 & 0x8000) == 0) {
            transforms.scale_00.x = lmb._14[a0_0];
            a0_0++;
          }

          //LAB_80116ca0
          if((v1.transformsType_00 & 0x4000) == 0) {
            transforms.scale_00.y = lmb._14[a0_0];
            a0_0++;
          }

          //LAB_80116cc0
          if((v1.transformsType_00 & 0x2000) == 0) {
            transforms.scale_00.z = lmb._14[a0_0];
            a0_0++;
          }

          //LAB_80116ce0
          if((v1.transformsType_00 & 0x1000) == 0) {
            transforms.trans_06.x = lmb._14[a0_0];
            a0_0++;
          }

          //LAB_80116d00
          if((v1.transformsType_00 & 0x800) == 0) {
            transforms.trans_06.y = lmb._14[a0_0];
            a0_0++;
          }

          //LAB_80116d20
          if((v1.transformsType_00 & 0x400) == 0) {
            transforms.trans_06.z = lmb._14[a0_0];
            a0_0++;
          }

          //LAB_80116d40
          if((v1.transformsType_00 & 0x200) == 0) {
            transforms.rot_0c.x = MathHelper.psxDegToRad(lmb._14[a0_0]);
            a0_0++;
          }

          //LAB_80116d60
          if((v1.transformsType_00 & 0x100) == 0) {
            transforms.rot_0c.y = MathHelper.psxDegToRad(lmb._14[a0_0]);
            a0_0++;
          }

          //LAB_80116d80
          if((v1.transformsType_00 & 0x80) == 0) {
            transforms.rot_0c.z = MathHelper.psxDegToRad(lmb._14[a0_0]);
            a0_0++;
          }
        }
      }

      //LAB_80116db8
      int a0_0 = (s5 - 1) * lmb._08 / 2;

      //LAB_80116de8
      for(int i = 0; i < lmb.objectCount_04; i++) {
        final LmbTransforms14 transforms = s7[i];
        final LmbType1.Sub04 a2 = lmb._0c[i];

        if((a2.transformsType_00 & 0x8000) == 0) {
          transforms.scale_00.x = Math.lerp(lmb._14[a0_0], transforms.scale_00.x, lerpScale);
          a0_0++;
        }

        //LAB_80116e34
        if((a2.transformsType_00 & 0x4000) == 0) {
          transforms.scale_00.y = Math.lerp(lmb._14[a0_0], transforms.scale_00.y, lerpScale);
          a0_0++;
        }

        //LAB_80116e80
        if((a2.transformsType_00 & 0x2000) == 0) {
          transforms.scale_00.z = Math.lerp(lmb._14[a0_0], transforms.scale_00.z, lerpScale);
          a0_0++;
        }

        //LAB_80116ecc
        if((a2.transformsType_00 & 0x1000) == 0) {
          transforms.trans_06.x = Math.lerp(lmb._14[a0_0], transforms.trans_06.x, lerpScale);
          a0_0++;
        }

        //LAB_80116f18
        if((a2.transformsType_00 & 0x800) == 0) {
          transforms.trans_06.y = Math.lerp(lmb._14[a0_0], transforms.trans_06.y, lerpScale);
          a0_0++;
        }

        //LAB_80116f64
        if((a2.transformsType_00 & 0x400) == 0) {
          transforms.trans_06.z = Math.lerp(lmb._14[a0_0], transforms.trans_06.z, lerpScale);
          a0_0++;
        }

        //LAB_80116fb0
        if((a2.transformsType_00 & 0x200) == 0) {
          a0_0++;
        }

        //LAB_80116fc8
        if((a2.transformsType_00 & 0x100) == 0) {
          a0_0++;
        }

        //LAB_80116fd4
        if((a2.transformsType_00 & 0x80) == 0) {
          a0_0++;
        }
      }

      //LAB_80116ff8
      this._04 = t0;
    }

    //LAB_80116ffc
    //LAB_80117014
    for(int i = 0; i < lmb.objectCount_04; i++) {
      final LmbTransforms14 transforms = s7[i];

      final int deffFlags = this._14[lmb._0c[i]._03];
      if(deffFlags != 0) {
        manager.params_10.rot_10.set(transforms.rot_0c);
        manager.params_10.trans_04.set(transforms.trans_06);
        manager.params_10.scale_16.set(transforms.scale_00);

        this.FUN_8011619c(manager, deffFlags, matrix);
      }
    }

    //LAB_801170d4
  }

  @Method(0x80117104L)
  private void processLmbType2(final EffectManagerData6c<EffectManagerParams.AnimType> manager, final int t5, final MV matrix) {
    final LmbType2 lmb = (LmbType2)this.lmb_0c;
    final LmbTransforms14[] originalTransforms = lmb.initialTransforms_10;
    final int keyframeIndex = t5 / 0x2000;
    final float lerpScale = (t5 & 0x1fff) / (float)0x2000;
    final LmbTransforms14[] transformsLo = this.lmbTransforms_10;
    final LmbTransforms14[] transformsHi = Arrays.copyOfRange(transformsLo, lmb.objectCount_04, transformsLo.length);
    final int nextKeyframeIndex = (keyframeIndex + 1) % lmb.keyframeCount_0a;
    if(this._04 != t5) {
      int s1 = this._04 / 0x2000;

      if(nextKeyframeIndex == 0 && keyframeIndex == 0) {
        return;
      }

      //LAB_801171c8
      if(s1 > keyframeIndex) {
        s1 = 0;

        for(int i = 0; i < lmb.objectCount_04; i++) {
          transformsLo[i].scale_00.set(originalTransforms[i].scale_00);
          transformsLo[i].trans_06.set(originalTransforms[i].trans_06);
          transformsLo[i].rot_0c.set(originalTransforms[i].rot_0c);
        }
      }

      //LAB_801171f8
      //LAB_8011720c
      for(; s1 < keyframeIndex; s1++) {
        int a0 = s1 * lmb.transformDataPairCount_08;

        //LAB_80117234
        for(int i = 0; i < lmb.transformDataPairCount_08 * 2; i += 2) {
          lmbType2TransformationData_8011a048[i] = (byte)(lmb._14[a0] >> 4);
          lmbType2TransformationData_8011a048[i + 1] = (byte)(lmb._14[a0] << 28 >> 28);
          a0++;
        }

        //LAB_80117270
        //LAB_8011728c
        int index = 0;
        for(int i = 0; i < lmb.objectCount_04; i++) {
          final LmbTransforms14 transform = transformsLo[i];

          final int flags = lmb.flags_0c[i];

          if((flags & 0xe000) != 0xe000) {
            final int shift = lmbType2TransformationData_8011a048[index++] & 0xf;

            if((flags & 0x8000) == 0) {
              transform.scale_00.x += lmbType2TransformationData_8011a048[index++] << shift;
            }

            //LAB_801172c8
            if((flags & 0x4000) == 0) {
              transform.scale_00.y += lmbType2TransformationData_8011a048[index++] << shift;
            }

            //LAB_801172f0
            if((flags & 0x2000) == 0) {
              transform.scale_00.z += lmbType2TransformationData_8011a048[index++] << shift;
            }
          }

          //LAB_80117310
          //LAB_80117314
          if((flags & 0x1c00) != 0x1c00) {
            final int shift = lmbType2TransformationData_8011a048[index++] & 0xf;

            if((flags & 0x1000) == 0) {
              transform.trans_06.x += lmbType2TransformationData_8011a048[index++] << shift;
            }

            //LAB_80117348
            if((flags & 0x800) == 0) {
              transform.trans_06.y += lmbType2TransformationData_8011a048[index++] << shift;
            }

            //LAB_80117370
            if((flags & 0x400) == 0) {
              transform.trans_06.z += lmbType2TransformationData_8011a048[index++] << shift;
            }
          }

          //LAB_80117390
          //LAB_80117394
          if((flags & 0x380) != 0x380) {
            final int shift = lmbType2TransformationData_8011a048[index++] & 0xf;

            if((flags & 0x200) == 0) {
              transform.rot_0c.x += lmbType2TransformationData_8011a048[index++] << shift;
            }

            //LAB_801173c8
            if((flags & 0x100) == 0) {
              transform.rot_0c.y += lmbType2TransformationData_8011a048[index++] << shift;
            }

            //LAB_801173f0
            if((flags & 0x80) == 0) {
              transform.rot_0c.z += lmbType2TransformationData_8011a048[index++] << shift;
            }
          }
        }
      }

      //LAB_80117438
      if(nextKeyframeIndex == 0) {
        //LAB_801176c0
        //LAB_801176e0
        for(int i = 0; i < lmb.objectCount_04; i++) {
          final LmbTransforms14 originalTransform = originalTransforms[i];
          final LmbTransforms14 transformLo = transformsLo[i];
          final LmbTransforms14 transformHi = transformsHi[i];

          final int flags = lmb.flags_0c[i];

          if((flags & 0x8000) == 0) {
            transformHi.scale_00.x = Math.lerp(originalTransform.scale_00.x, transformLo.scale_00.x, lerpScale);
          }

          //LAB_80117730
          if((flags & 0x4000) == 0) {
            transformHi.scale_00.y = Math.lerp(originalTransform.scale_00.y, transformLo.scale_00.y, lerpScale);
          }

          //LAB_80117774
          if((flags & 0x2000) == 0) {
            transformHi.scale_00.z = Math.lerp(originalTransform.scale_00.z, transformLo.scale_00.z, lerpScale);
          }

          //LAB_801177b8
          if((flags & 0x1000) == 0) {
            transformHi.trans_06.x = Math.lerp(originalTransform.trans_06.x, transformLo.trans_06.x, lerpScale);
          }

          //LAB_801177fc
          if((flags & 0x800) == 0) {
            transformHi.trans_06.y = Math.lerp(originalTransform.trans_06.y, transformLo.trans_06.y, lerpScale);
          }

          //LAB_80117840
          if((flags & 0x400) == 0) {
            transformHi.trans_06.z = Math.lerp(originalTransform.trans_06.z, transformLo.trans_06.z, lerpScale);
          }
        }
      } else {
        int a0 = (nextKeyframeIndex - 1) * lmb.transformDataPairCount_08;

        //LAB_80117470
        for(int i = 0; i < lmb.transformDataPairCount_08 * 2; i += 2) {
          lmbType2TransformationData_8011a048[i] = (byte)(lmb._14[a0] >> 4);
          lmbType2TransformationData_8011a048[i + 1] = (byte)(lmb._14[a0] << 28 >> 28);
          a0++;
        }

        //LAB_801174ac
        //LAB_801174d0
        int index = 0;
        for(int i = 0; i < lmb.objectCount_04; i++) {
          final LmbTransforms14 transformLo = transformsLo[i];
          final LmbTransforms14 transformHi = transformsHi[i];

          final int flags = lmb.flags_0c[i];

          if((flags & 0xe000) != 0xe000) {
            final int shift = lmbType2TransformationData_8011a048[index++] & 0xf;

            if((flags & 0x8000) == 0) {
              transformHi.scale_00.x = transformLo.scale_00.x + (lmbType2TransformationData_8011a048[index++] << shift) * lerpScale;
            }

            //LAB_80117524
            if((flags & 0x4000) == 0) {
              transformHi.scale_00.y = transformLo.scale_00.y + (lmbType2TransformationData_8011a048[index++] << shift) * lerpScale;
            }

            //LAB_80117564
            if((flags & 0x2000) == 0) {
              transformHi.scale_00.z = transformLo.scale_00.z + (lmbType2TransformationData_8011a048[index++] << shift) * lerpScale;
            }
          }

          //LAB_8011759c
          //LAB_801175a0
          if((flags & 0x1c00) != 0x1c00) {
            final int shift = lmbType2TransformationData_8011a048[index++] & 0xf;

            if((flags & 0x1000) == 0) {
              transformHi.trans_06.x = transformLo.trans_06.x + (lmbType2TransformationData_8011a048[index++] << shift) * lerpScale;
            }

            //LAB_801175ec
            if((flags & 0x800) == 0) {
              transformHi.trans_06.y = transformLo.trans_06.y + (lmbType2TransformationData_8011a048[index++] << shift) * lerpScale;
            }

            //LAB_8011762c
            if((flags & 0x400) == 0) {
              transformHi.trans_06.z = transformLo.trans_06.z + (lmbType2TransformationData_8011a048[index++] << shift) * lerpScale;
            }
          }

          //LAB_80117664
          //LAB_80117668
          if((flags & 0x380) != 0x380) {
            index++;

            if((flags & 0x200) == 0) {
              index++;
            }

            //LAB_80117680
            if((flags & 0x100) == 0) {
              index++;
            }

            //LAB_80117690
            if((flags & 0x80) == 0) {
              index++;
            }
          }
        }
      }

      //LAB_801178a0
      this._04 = t5;
    }

    //LAB_801178a4
    //LAB_801178c0
    for(int i = 0; i < lmb.objectCount_04; i++) {
      final LmbTransforms14 transformLo = transformsLo[i];
      final LmbTransforms14 transformHi = transformsHi[i];
      final int flags = lmb.flags_0c[i];

      if(this._14[flags >>> 24] != 0) {
        if(manager.params_10._2c != 0) {
          manager.params_10.rot_10.set(transformLo.rot_0c);
        } else {
          //LAB_80117914
          manager.params_10.rot_10.set(transformHi.rot_0c);
        }

        //LAB_80117938
        manager.params_10.trans_04.set(transformHi.trans_06);
        manager.params_10.scale_16.set(transformHi.scale_00);
        this.FUN_8011619c(manager, this._14[flags >>> 24], matrix);
      }
    }

    //LAB_801179c0
  }
}
