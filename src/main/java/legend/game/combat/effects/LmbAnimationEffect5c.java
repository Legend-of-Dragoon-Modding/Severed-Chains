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
  /** fip12 */
  public int previousTick_04;
  public int keyframeCount_08;
  public Lmb lmb_0c;
  /** May have two copies of the LMB table for some reason */
  public LmbTransforms14[] lmbTransforms_10;
  public final int[] deffFlags_14 = new int[8];
  /**
   * <ul>
   *   <li>0x4 - colour</li>
   *   <li>0x8 - scale</li>
   * </ul>
   */
  public int managerTransformFlags_34;
  /** Maximum number of secondary processing substeps */
  public int totalSubFrames_38;
  /** fip12; Decrement step size for current tick value when doing secondary processing with substeps */
  public int subTickStep_3c;
  /** fip12 */
  public int finalManagerTransformMultiplier_40;

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
      int tickFip12 = Math.max(0, manager.params_10.ticks_24) % (this.keyframeCount_08 * 2) << 12;
      tmdGp0Tpage_1f8003ec = flags >>> 23 & 0x60; // tpage
      zOffset_1f8003e8 = manager.params_10.z_22;
      if((manager.params_10.flags_00 & 0x40) == 0) {
        FUN_800e61e4(manager.params_10.colour_1c.x / 128.0f, manager.params_10.colour_1c.y / 128.0f, manager.params_10.colour_1c.z / 128.0f);
      }

      //LAB_80117ac0
      //LAB_80117acc
      final EffectManagerData6c<EffectManagerParams.AnimType> anim = new EffectManagerData6c<>("Temp 2", new EffectManagerParams.AnimType());
      anim.set(manager);

      final MV managerTransforms = new MV();
      calculateEffectTransforms(managerTransforms, manager);

      final int type = this.lmbType_00 & 0x7;
      if(type == 0) {
        //LAB_80117b50
        this.processLmbType0(manager, tickFip12, managerTransforms);
      } else if(type == 1) {
        //LAB_80117b68
        this.processLmbType1(manager, tickFip12, managerTransforms);
      } else if(type == 2) {
        //LAB_80117b80
        this.processLmbType2(manager, tickFip12, managerTransforms);
      }

      //LAB_80117b8c
      final int totalSubFrames = this.totalSubFrames_38;

      if(totalSubFrames >= 2) {
        final int subTick = this.subTickStep_3c;
        int accumulatorR;
        int accumulatorG;
        int accumulatorB;
        final int stepR;
        final int stepG;
        final int stepB;
        if((this.managerTransformFlags_34 & 0x4) != 0) {
          final int finalBrightness = this.finalManagerTransformMultiplier_40 - 0x1000;
          stepR = anim.params_10.colour_1c.x * finalBrightness / totalSubFrames;
          stepG = anim.params_10.colour_1c.y * finalBrightness / totalSubFrames;
          stepB = anim.params_10.colour_1c.z * finalBrightness / totalSubFrames;
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
        if((this.managerTransformFlags_34 & 0x8) != 0) {
          final int finalScale = anim.params_10.scale_28 * (this.finalManagerTransformMultiplier_40 - 0x1000);
          accumulatorScale = anim.params_10.scale_28 << 12;
          stepScale = finalScale / totalSubFrames;
        }

        //LAB_80117c88
        tickFip12 -= subTick;

        //LAB_80117ca0
        for(int i = 1; i < totalSubFrames && tickFip12 >= 0; i++) {
          if((this.managerTransformFlags_34 & 0x4) != 0) {
            accumulatorR += stepR;
            accumulatorG += stepG;
            accumulatorB += stepB;
            manager.params_10.colour_1c.x = accumulatorR >> 12;
            manager.params_10.colour_1c.y = accumulatorG >> 12;
            manager.params_10.colour_1c.z = accumulatorB >> 12;
          }

          //LAB_80117d1c
          if((this.managerTransformFlags_34 & 0x8) != 0) {
            accumulatorScale += stepScale;
            manager.params_10.scale_28 = accumulatorScale >> 12;
          }

          //LAB_80117d54
          if(type == 0) {
            //LAB_80117dc4
            this.processLmbType0(manager, tickFip12, managerTransforms);
          } else if(type == 1) {
            //LAB_80117ddc
            this.processLmbType1(manager, tickFip12, managerTransforms);
          } else if(type == 2) {
            //LAB_80117df4
            this.processLmbType2(manager, tickFip12, managerTransforms);
          }

          //LAB_80117e04
          tickFip12 -= subTick;
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
  private void renderLmbParticles(final EffectManagerData6c<EffectManagerParams.AnimType> manager, final int deffFlags, final MV managerTransforms) {
    final MV transforms = new MV();
    transforms.rotationZYX(manager.params_10.rot_10);
    transforms.scale(manager.params_10.scale_16);
    transforms.transfer.set(manager.params_10.trans_04);
    transforms.compose(managerTransforms, transforms);
    final float scale = manager.params_10.scale_28 / (float)0x1000;
    transforms.scaleLocal(scale, scale, scale);
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
      renderTmdSpriteEffect(tmdObjTable, this.obj, manager.params_10, transforms);
    } else if(type == 0x400_0000) {
      if(this.deffSpriteFlags_50 != deffFlags) {
        //LAB_801162e8
        final BillboardSpriteEffect0c sprite = new BillboardSpriteEffect0c();

        sprite.set(deffFlags);
        this.metrics_54.u_00 = sprite.metrics_04.u_00;
        this.metrics_54.v_02 = sprite.metrics_04.v_02;
        this.metrics_54.w_04 = sprite.metrics_04.w_04;
        this.metrics_54.h_05 = sprite.metrics_04.h_05;
        this.metrics_54.clut_06 = sprite.metrics_04.clut_06;
        this.deffSpriteFlags_50 = deffFlags;
      }

      final int u = (this.metrics_54.u_00 & 0x3f) * 4;
      final int v = this.metrics_54.v_02 & 0xff;
      final int w = this.metrics_54.w_04 & 0xff;
      final int h = this.metrics_54.h_05 & 0xff;
      final int clut = this.metrics_54.clut_06;

      //LAB_8011633c
      final Vector3f worldCoords = new Vector3f();
      final MV w2sTransform = new MV();
      final Vector2f screenCoords = new Vector2f();
      transforms.compose(worldToScreenMatrix_800c3548, w2sTransform);
      GTE.setTransforms(w2sTransform);

      final float z = perspectiveTransform(worldCoords, screenCoords);
      if(z >= 0x50) {
        //LAB_801163c4
        final float screenspaceScale = projectionPlaneDistance_1f8003f8 * 2.0f / z * manager.params_10.scale_16.z;
        final float l = -w / 2.0f * screenspaceScale;
        final float r = w / 2.0f * screenspaceScale;
        final float t = -h / 2.0f * screenspaceScale;
        final float b = h / 2.0f * screenspaceScale;
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
          .pos(0, screenCoords.x + cosL - sinT, screenCoords.y + sinL + cosT)
          .pos(1, screenCoords.x + cosR - sinT, screenCoords.y + sinR + cosT)
          .pos(2, screenCoords.x + cosL - sinB, screenCoords.y + sinL + cosB)
          .pos(3, screenCoords.x + cosR - sinB, screenCoords.y + sinR + cosB)
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
      manager.params_10.trans_04.set(transforms.transfer);
      getRotationAndScaleFromTransforms(manager.params_10.rot_10, manager.params_10.scale_16, transforms);

      final int oldParentBobjIndex = manager2.parentBobjIndex_0c;
      final int oldParentPartIndex = manager2.parentPartIndex_0d;
      manager2.parentBobjIndex_0c = manager.myScriptState_0e.index;
      manager2.parentPartIndex_0d = -1;
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
      manager2.parentBobjIndex_0c = oldParentBobjIndex;
      manager2.parentPartIndex_0d = oldParentPartIndex;
    }
    //LAB_801168b8
  }

  @Method(0x801168e8L)
  private void processLmbType0(final EffectManagerData6c<EffectManagerParams.AnimType> manager, final int tickFip12, final MV managerTransforms) {
    final LmbType0 lmb = (LmbType0)this.lmb_0c;
    final int keyframeIndex = tickFip12 / 0x2000;
    final int nextKeyframeIndex = (keyframeIndex + 1) % lmb.partAnimations_08[0].keyframeCount_04;
    final float lerpScaleB = (tickFip12 & 0x1fff) / (float)0x2000;
    final float lerpScaleA = 1.0f - lerpScaleB;

    //LAB_80116960
    for(int i = 0; i < lmb.objectCount_04; i++) {
      if(this.deffFlags_14[lmb.partAnimations_08[i].partFlagIndex_00] != 0) {
        final LmbTransforms14 currentKeyframeTransforms = lmb.partAnimations_08[i].keyframes_08[keyframeIndex];
        final LmbTransforms14 nextKeyframeTransforms = lmb.partAnimations_08[i].keyframes_08[nextKeyframeIndex];
        manager.params_10.rot_10.set(currentKeyframeTransforms.rot_0c);
        manager.params_10.trans_04.x = (currentKeyframeTransforms.trans_06.x * lerpScaleA + nextKeyframeTransforms.trans_06.x * lerpScaleB);
        manager.params_10.trans_04.y = (currentKeyframeTransforms.trans_06.y * lerpScaleA + nextKeyframeTransforms.trans_06.y * lerpScaleB);
        manager.params_10.trans_04.z = (currentKeyframeTransforms.trans_06.z * lerpScaleA + nextKeyframeTransforms.trans_06.z * lerpScaleB);
        manager.params_10.scale_16.x = (currentKeyframeTransforms.scale_00.x * lerpScaleA + nextKeyframeTransforms.scale_00.x * lerpScaleB);
        manager.params_10.scale_16.y = (currentKeyframeTransforms.scale_00.y * lerpScaleA + nextKeyframeTransforms.scale_00.y * lerpScaleB);
        manager.params_10.scale_16.z = (currentKeyframeTransforms.scale_00.z * lerpScaleA + nextKeyframeTransforms.scale_00.z * lerpScaleB);
        this.renderLmbParticles(manager, this.deffFlags_14[lmb.partAnimations_08[i].partFlagIndex_00], managerTransforms);
      }
    }
  }

  @Method(0x80116b7cL)
  private void processLmbType1(final EffectManagerData6c<EffectManagerParams.AnimType> manager, final int tickFip12, final MV managerTransforms) {
    final LmbType1 lmb = (LmbType1)this.lmb_0c;
    int keyframeIndex = tickFip12 >> 13;
    float lerpScale = (tickFip12 & 0x1fff) / (float)0x2000;
    int nextKeyframeIndex = (keyframeIndex + 1) % lmb.keyframeCount_0a;
    final LmbTransforms14[] lmbTransforms = this.lmbTransforms_10;
    if(this.previousTick_04 != tickFip12) {
      if(nextKeyframeIndex == 0) {
        if(keyframeIndex == 0) {
          return;
        }

        nextKeyframeIndex = keyframeIndex;
        keyframeIndex = 0;
        lerpScale = 1.0f - lerpScale;
      }

      //LAB_80116c20
      if(keyframeIndex == 0) {
        for(int i = 0; i < lmb.objectCount_04; i++) {
          lmbTransforms[i].set(lmb.transforms_10[i]);
        }
      } else {
        //LAB_80116c50
        int index = (keyframeIndex - 1) * lmb.transformDataCount_08 / 2;

        //LAB_80116c80
        for(int i = 0; i < lmb.objectCount_04; i++) {
          final LmbTransforms14 transforms = lmbTransforms[i];
          final LmbType1.PartInfo04 partInfo = lmb.partAnimations_0c[i];

          if((partInfo.transformFlags_00 & 0x8000) == 0) {
            transforms.scale_00.x = lmb.transformData_14[index++];
          }

          //LAB_80116ca0
          if((partInfo.transformFlags_00 & 0x4000) == 0) {
            transforms.scale_00.y = lmb.transformData_14[index++];
          }

          //LAB_80116cc0
          if((partInfo.transformFlags_00 & 0x2000) == 0) {
            transforms.scale_00.z = lmb.transformData_14[index++];
          }

          //LAB_80116ce0
          if((partInfo.transformFlags_00 & 0x1000) == 0) {
            transforms.trans_06.x = lmb.transformData_14[index++];
          }

          //LAB_80116d00
          if((partInfo.transformFlags_00 & 0x800) == 0) {
            transforms.trans_06.y = lmb.transformData_14[index++];
          }

          //LAB_80116d20
          if((partInfo.transformFlags_00 & 0x400) == 0) {
            transforms.trans_06.z = lmb.transformData_14[index++];
          }

          //LAB_80116d40
          if((partInfo.transformFlags_00 & 0x200) == 0) {
            transforms.rot_0c.x = MathHelper.psxDegToRad(lmb.transformData_14[index++]);
          }

          //LAB_80116d60
          if((partInfo.transformFlags_00 & 0x100) == 0) {
            transforms.rot_0c.y = MathHelper.psxDegToRad(lmb.transformData_14[index++]);
          }

          //LAB_80116d80
          if((partInfo.transformFlags_00 & 0x80) == 0) {
            transforms.rot_0c.z = MathHelper.psxDegToRad(lmb.transformData_14[index++]);
          }
        }
      }

      //LAB_80116db8
      int index = (nextKeyframeIndex - 1) * lmb.transformDataCount_08 / 2;

      //LAB_80116de8
      for(int i = 0; i < lmb.objectCount_04; i++) {
        final LmbTransforms14 transforms = lmbTransforms[i];
        final LmbType1.PartInfo04 partInfo = lmb.partAnimations_0c[i];

        if((partInfo.transformFlags_00 & 0x8000) == 0) {
          transforms.scale_00.x = Math.lerp(lmb.transformData_14[index++], transforms.scale_00.x, lerpScale);
        }

        //LAB_80116e34
        if((partInfo.transformFlags_00 & 0x4000) == 0) {
          transforms.scale_00.y = Math.lerp(lmb.transformData_14[index++], transforms.scale_00.y, lerpScale);
        }

        //LAB_80116e80
        if((partInfo.transformFlags_00 & 0x2000) == 0) {
          transforms.scale_00.z = Math.lerp(lmb.transformData_14[index++], transforms.scale_00.z, lerpScale);
        }

        //LAB_80116ecc
        if((partInfo.transformFlags_00 & 0x1000) == 0) {
          transforms.trans_06.x = Math.lerp(lmb.transformData_14[index++], transforms.trans_06.x, lerpScale);
        }

        //LAB_80116f18
        if((partInfo.transformFlags_00 & 0x800) == 0) {
          transforms.trans_06.y = Math.lerp(lmb.transformData_14[index++], transforms.trans_06.y, lerpScale);
        }

        //LAB_80116f64
        if((partInfo.transformFlags_00 & 0x400) == 0) {
          transforms.trans_06.z = Math.lerp(lmb.transformData_14[index++], transforms.trans_06.z, lerpScale);
        }

        //LAB_80116fb0
        if((partInfo.transformFlags_00 & 0x200) == 0) {
          index++;
        }

        //LAB_80116fc8
        if((partInfo.transformFlags_00 & 0x100) == 0) {
          index++;
        }

        //LAB_80116fd4
        if((partInfo.transformFlags_00 & 0x80) == 0) {
          index++;
        }
      }

      //LAB_80116ff8
      this.previousTick_04 = tickFip12;
    }

    //LAB_80116ffc
    //LAB_80117014
    for(int i = 0; i < lmb.objectCount_04; i++) {
      final LmbTransforms14 transforms = lmbTransforms[i];

      final int deffFlags = this.deffFlags_14[lmb.partAnimations_0c[i].partFlagIndex_03];
      if(deffFlags != 0) {
        manager.params_10.rot_10.set(transforms.rot_0c);
        manager.params_10.trans_04.set(transforms.trans_06);
        manager.params_10.scale_16.set(transforms.scale_00);

        this.renderLmbParticles(manager, deffFlags, managerTransforms);
      }
    }
    //LAB_801170d4
  }

  @Method(0x80117104L)
  private void processLmbType2(final EffectManagerData6c<EffectManagerParams.AnimType> manager, final int tickFip12, final MV managerTransforms) {
    final LmbType2 lmb = (LmbType2)this.lmb_0c;
    final LmbTransforms14[] originalTransforms = lmb.initialTransforms_10;
    final int keyframeIndex = tickFip12 / 0x2000;
    final float lerpScale = (tickFip12 & 0x1fff) / (float)0x2000; // mod(tick, 2) / 2
    final LmbTransforms14[] transformsLo = this.lmbTransforms_10;
    final LmbTransforms14[] transformsHi = Arrays.copyOfRange(transformsLo, lmb.objectCount_04, transformsLo.length);
    final int nextKeyframeIndex = (keyframeIndex + 1) % lmb.keyframeCount_0a;
    if(this.previousTick_04 != tickFip12) {
      int previousKeyframeIndex = this.previousTick_04 / 0x2000;

      if(nextKeyframeIndex == 0 && keyframeIndex == 0) {
        return;
      }

      //LAB_801171c8
      if(previousKeyframeIndex > keyframeIndex) {
        previousKeyframeIndex = 0;

        for(int i = 0; i < lmb.objectCount_04; i++) {
          transformsLo[i].scale_00.set(originalTransforms[i].scale_00);
          transformsLo[i].trans_06.set(originalTransforms[i].trans_06);
          transformsLo[i].rot_0c.set(originalTransforms[i].rot_0c);
        }
      }

      //LAB_801171f8
      //LAB_8011720c
      for(; previousKeyframeIndex < keyframeIndex; previousKeyframeIndex++) {
        int transformDataPairIndex = previousKeyframeIndex * lmb.transformDataPairCount_08;

        //LAB_80117234
        for(int i = 0; i < lmb.transformDataPairCount_08 * 2; i += 2) {
          lmbType2TransformationData_8011a048[i] = (byte)(lmb.packedTransformData_14[transformDataPairIndex] >> 4);
          lmbType2TransformationData_8011a048[i + 1] = (byte)(lmb.packedTransformData_14[transformDataPairIndex] << 28 >> 28);
          transformDataPairIndex++;
        }

        //LAB_80117270
        //LAB_8011728c
        int index = 0;
        for(int i = 0; i < lmb.objectCount_04; i++) {
          final LmbTransforms14 transformLo = transformsLo[i];

          final int flags = lmb.transformFlags_0c[i];

          if((flags & 0xe000) != 0xe000) {
            final int shift = lmbType2TransformationData_8011a048[index++] & 0xf;

            if((flags & 0x8000) == 0) {
              transformLo.scale_00.x += lmbType2TransformationData_8011a048[index++] << shift;
            }

            //LAB_801172c8
            if((flags & 0x4000) == 0) {
              transformLo.scale_00.y += lmbType2TransformationData_8011a048[index++] << shift;
            }

            //LAB_801172f0
            if((flags & 0x2000) == 0) {
              transformLo.scale_00.z += lmbType2TransformationData_8011a048[index++] << shift;
            }
          }

          //LAB_80117310
          //LAB_80117314
          if((flags & 0x1c00) != 0x1c00) {
            final int shift = lmbType2TransformationData_8011a048[index++] & 0xf;

            if((flags & 0x1000) == 0) {
              transformLo.trans_06.x += lmbType2TransformationData_8011a048[index++] << shift;
            }

            //LAB_80117348
            if((flags & 0x800) == 0) {
              transformLo.trans_06.y += lmbType2TransformationData_8011a048[index++] << shift;
            }

            //LAB_80117370
            if((flags & 0x400) == 0) {
              transformLo.trans_06.z += lmbType2TransformationData_8011a048[index++] << shift;
            }
          }

          //LAB_80117390
          //LAB_80117394
          if((flags & 0x380) != 0x380) {
            final int shift = lmbType2TransformationData_8011a048[index++] & 0xf;

            if((flags & 0x200) == 0) {
              transformLo.rot_0c.x += MathHelper.psxDegToRad(lmbType2TransformationData_8011a048[index++] << shift);
            }

            //LAB_801173c8
            if((flags & 0x100) == 0) {
              transformLo.rot_0c.y += MathHelper.psxDegToRad(lmbType2TransformationData_8011a048[index++] << shift);
            }

            //LAB_801173f0
            if((flags & 0x80) == 0) {
              transformLo.rot_0c.z += MathHelper.psxDegToRad(lmbType2TransformationData_8011a048[index++] << shift);
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

          final int flags = lmb.transformFlags_0c[i];

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
        int transformDataPairIndex = (nextKeyframeIndex - 1) * lmb.transformDataPairCount_08;

        //LAB_80117470
        for(int i = 0; i < lmb.transformDataPairCount_08 * 2; i += 2) {
          lmbType2TransformationData_8011a048[i] = (byte)(lmb.packedTransformData_14[transformDataPairIndex] >> 4);
          lmbType2TransformationData_8011a048[i + 1] = (byte)(lmb.packedTransformData_14[transformDataPairIndex] << 28 >> 28);
          transformDataPairIndex++;
        }

        //LAB_801174ac
        //LAB_801174d0
        int index = 0;
        for(int i = 0; i < lmb.objectCount_04; i++) {
          final LmbTransforms14 transformLo = transformsLo[i];
          final LmbTransforms14 transformHi = transformsHi[i];

          final int flags = lmb.transformFlags_0c[i];

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
      this.previousTick_04 = tickFip12;
    }

    //LAB_801178a4
    //LAB_801178c0
    for(int i = 0; i < lmb.objectCount_04; i++) {
      final LmbTransforms14 transformLo = transformsLo[i];
      final LmbTransforms14 transformHi = transformsHi[i];
      final int flags = lmb.transformFlags_0c[i];

      if(this.deffFlags_14[flags >>> 24] != 0) {
        if(manager.params_10._2c != 0) {
          manager.params_10.rot_10.set(transformLo.rot_0c);
        } else {
          //LAB_80117914
          manager.params_10.rot_10.set(transformHi.rot_0c);
        }

        //LAB_80117938
        manager.params_10.trans_04.set(transformHi.trans_06);
        manager.params_10.scale_16.set(transformHi.scale_00);
        this.renderLmbParticles(manager, this.deffFlags_14[flags >>> 24], managerTransforms);
      }
    }
    //LAB_801179c0
  }
}
