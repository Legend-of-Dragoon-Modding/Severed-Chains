package legend.game.combat.effects;

import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.memory.types.TriConsumer;
import legend.core.opengl.Obj;
import legend.core.opengl.PolyBuilder;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.function.Consumer;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.combat.Battle.seed_800fa754;
import static legend.game.combat.SEffe.FUN_800cfb94;
import static legend.game.combat.SEffe.renderSegmentGradient;
import static legend.game.combat.SEffe.scriptGetScriptedObjectPos;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;

public class ElectricityEffect38 implements Effect<EffectManagerParams.ElectricityType> {
  private final int renderIndex;

  /** ushort */
  public int boltCount_00;

  public int currentColourFadeStep_04;
  public int scriptIndex_08;
  public int numColourFadeSteps_0c;
  public float boltAngleStep_10;
  /** ushort; If true, add origin translation of current segment to that of previous segment */
  public boolean addSuccessiveSegmentOriginTranslations_14;
  /** int; Related to which rendering branch to use in 80103db0 */
  public boolean type1RendererType_18;
  /** short; The lower the value, the wider the angle in which the bolt can be drawn */
  public int boltAngleRangeCutoff_1c;
  /** short; Length of hypotenuse of translation, most often added to segment origin */
  public int segmentOriginTranslationMagnitude_1e;
  /** short */
  public int callbackIndex_20;
  /** ubyte */
  public boolean colourShouldFade_22;
  /** ubyte; If true, colour will be progressively faded for each successive segment. */
  public boolean fadeSuccessiveSegments_23;
  /** byte; If true, re-call initializeRadialElectricityNodes in renderer */
  public boolean reinitializeNodes_24;

  /** ushort */
  public int segmentOriginTranslationModifier_26;
  /** ubyte */
  public int boltSegmentCount_28;
  /** ubyte; If true, render monochrome base triangles */
  public boolean hasMonochromeBase_29;
  /** ubyte; Effect is only meant to send new render commands every other frame if manager._10._24 != 0 */
  public int frameNum_2a;

  public TriConsumer<EffectManagerData6c<EffectManagerParams.ElectricityType>, LightningBoltEffect14, Integer> callback_2c;

  public LightningBoltEffect14[] bolts_34;

  public final MV transforms = new MV();

  public ElectricityEffect38(final int renderIndex, final int scriptIndex, final int boltAngleRangeCutoff, final int boltCount, final int segmentOriginTranslationMagnitude, final float boltAngleStep, final int segmentOriginTranslationModifier, final int boltSegmentCount, final int numColourFadeSteps, final boolean colourShouldFade, final boolean fadeSuccessiveSegments, final boolean reinitializeNodes, final boolean addSuccessiveSegmentOriginTranslations, final boolean type1RendererType, final boolean hasMonochromeBase) {
    this.renderIndex = renderIndex;
    this.boltCount_00 = boltCount;
    this.boltSegmentCount_28 = boltSegmentCount;
    this.bolts_34 = new LightningBoltEffect14[boltCount];
    Arrays.setAll(this.bolts_34, i -> new LightningBoltEffect14(i, boltSegmentCount));

    this.currentColourFadeStep_04 = 0;
    this.scriptIndex_08 = scriptIndex;
    this.numColourFadeSteps_0c = numColourFadeSteps;
    this.boltAngleStep_10 = boltAngleStep;
    this.addSuccessiveSegmentOriginTranslations_14 = addSuccessiveSegmentOriginTranslations;
    this.type1RendererType_18 = type1RendererType;
    this.boltAngleRangeCutoff_1c = boltAngleRangeCutoff;
    this.segmentOriginTranslationMagnitude_1e = segmentOriginTranslationMagnitude;
    this.callbackIndex_20 = renderIndex;
    this.colourShouldFade_22 = colourShouldFade;
    this.fadeSuccessiveSegments_23 = fadeSuccessiveSegments;
    this.reinitializeNodes_24 = reinitializeNodes;
    this.segmentOriginTranslationModifier_26 = segmentOriginTranslationModifier;
    this.hasMonochromeBase_29 = hasMonochromeBase;
    this.frameNum_2a = 0;
    this.callback_2c = this.electricityEffectCallbacks_80119ee8[renderIndex];

    if(this.numColourFadeSteps_0c == 0) {
      this.numColourFadeSteps_0c = -1;
    }

    //LAB_8010549c
    //LAB_801054b4
    for(int i = 0; i < this.boltCount_00; i++) {
      final LightningBoltEffect14 boltEffect = this.bolts_34[i];
      boltEffect.unused_00 = 1;
      boltEffect.angle_02 = seed_800fa754.nextFloat(MathHelper.TWO_PI);
      boltEffect.rotation_04.zero();
    }
  }

  @Override
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.ElectricityType>> state) {

  }

  @Override
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.ElectricityType>> state) {
    this.electricityEffectRenderers_80119f14[this.renderIndex].accept(state);
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.ElectricityType>> state) {

  }

  @Method(0x80102618L)
  private void modifyLightningSegmentOriginsBySecondaryScriptTranslation(final Vector3f translation, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 boltEffect) {
    if(electricEffect.scriptIndex_08 != -1) {
      final Vector3f secondaryScriptTranslation = new Vector3f();
      final Vector3f newOrigin = new Vector3f();
      scriptGetScriptedObjectPos(electricEffect.scriptIndex_08, secondaryScriptTranslation);
      secondaryScriptTranslation.sub(translation).div(electricEffect.boltSegmentCount_28);

      //LAB_801026f0
      for(int i = 0; i < electricEffect.boltSegmentCount_28; i++) {
        final LightningBoltEffectSegment30 boltSegment = boltEffect.boltSegments_10[i];
        boltSegment.origin_00.set(newOrigin);

        newOrigin.x += (seed_800fa754.nextInt(257) - 128) * electricEffect.segmentOriginTranslationModifier_26 >> 7;
        newOrigin.z += (seed_800fa754.nextInt(257) - 128) * electricEffect.segmentOriginTranslationModifier_26 >> 7;
        newOrigin.add(secondaryScriptTranslation);
      }
    }
    //LAB_80102848
  }

  @Method(0x80102860L)
  private void initializeRadialElectricityBoltColour(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final ElectricityEffect38 electricEffect) {
    int innerColourR;
    int innerColourG;
    int innerColourB;
    int outerColourR;
    int outerColourG;
    int outerColourB;
    int innerColourFadeStepR = 0;
    int innerColourFadeStepG = 0;
    int innerColourFadeStepB = 0;
    int outerColourFadeStepR = 0;
    int outerColourFadeStepG = 0;
    int outerColourFadeStepB = 0;

    //LAB_801028a4
    for(int boltNum = 0; boltNum < electricEffect.boltCount_00; boltNum++) {
      final LightningBoltEffect14 bolt = electricEffect.bolts_34[boltNum];

      outerColourR = manager.params_10.colour_1c.x << 8;
      outerColourG = manager.params_10.colour_1c.y << 8;
      outerColourB = manager.params_10.colour_1c.z << 8;

      final int managerR = manager.params_10.colour_1c.x;
      int managerG = manager.params_10.colour_1c.y;
      int managerB = manager.params_10.colour_1c.z;

      if(managerG < managerR) {
        managerG = managerR;
      }

      //LAB_8010290c
      if(managerB < managerG) {
        managerB = managerG;
      }

      //LAB_8010292c
      innerColourR = managerB << 8;
      innerColourG = managerB << 8;
      innerColourB = managerB << 8;

      if(electricEffect.fadeSuccessiveSegments_23) {
        innerColourFadeStepR = innerColourR / electricEffect.boltSegmentCount_28;
        innerColourFadeStepG = innerColourG / electricEffect.boltSegmentCount_28;
        innerColourFadeStepB = innerColourB / electricEffect.boltSegmentCount_28;
        outerColourFadeStepR = outerColourR / electricEffect.boltSegmentCount_28;
        outerColourFadeStepG = outerColourG / electricEffect.boltSegmentCount_28;
        outerColourFadeStepB = outerColourB / electricEffect.boltSegmentCount_28;
      }

      //LAB_801029f0
      //LAB_80102a04
      for(int segmentNum = 0; segmentNum < electricEffect.boltSegmentCount_28; segmentNum++) {
        final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[segmentNum];
        segment.innerColour_10.set(innerColourR, innerColourG, innerColourB);
        segment.outerColour_16.set(outerColourR, outerColourG, outerColourB);

        if(electricEffect.colourShouldFade_22 && electricEffect.numColourFadeSteps_0c != -1) {
          segment.innerColourFadeStep_1c.set(segment.innerColour_10).div(electricEffect.numColourFadeSteps_0c);
          segment.outerColourFadeStep_22.set(segment.outerColour_16).div(electricEffect.numColourFadeSteps_0c);
        } else {
          //LAB_80102b10
          segment.innerColourFadeStep_1c.set(0, 0, 0);
          segment.outerColourFadeStep_22.set(0, 0, 0);
        }

        //LAB_80102b28
        innerColourR = innerColourR - innerColourFadeStepR;
        innerColourG = innerColourG - innerColourFadeStepG;
        innerColourB = innerColourB - innerColourFadeStepB;
        outerColourR = outerColourR - outerColourFadeStepR;
        outerColourG = outerColourG - outerColourFadeStepG;
        outerColourB = outerColourB - outerColourFadeStepB;
      }

      //LAB_80102bb8
      this.modifyLightningSegmentOriginsBySecondaryScriptTranslation(manager.params_10.trans_04, electricEffect, bolt);
    }

    //LAB_80102be0
  }

  @Method(0x80102bfcL)
  public void initializeElectricityNodes(final Vector3f translation, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 bolt) {
    float segmentOriginX = 0;
    final float segmentOriginY = -(electricEffect.boltAngleRangeCutoff_1c / (float)electricEffect.boltSegmentCount_28);
    float segmentOriginZ = 0;

    //LAB_80102c58
    for(int i = 0; i < electricEffect.boltSegmentCount_28; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.set(segmentOriginX, segmentOriginY * i, segmentOriginZ);
      segment.scaleMultiplier_28 = seed_800fa754.nextInt(7) + 5;
      segment.unused_2a = 0;
      segment.originTranslationMagnitude_2c = segmentOriginX / 16.0f;
      segment.baseVertexTranslationScale_2e = (seed_800fa754.nextInt(193) + 64) / (float)0x100;

      if(electricEffect.addSuccessiveSegmentOriginTranslations_14) {
        segmentOriginX += (seed_800fa754.nextInt(257) - 128) * electricEffect.segmentOriginTranslationModifier_26 >> 7;
        segmentOriginZ += (seed_800fa754.nextInt(257) - 128) * electricEffect.segmentOriginTranslationModifier_26 >> 7;
        //LAB_80102e58
      } else if(i < electricEffect.boltSegmentCount_28 - 2) {
        segmentOriginX = (seed_800fa754.nextInt(257) - 128) * electricEffect.segmentOriginTranslationModifier_26 >> 7;
        segmentOriginZ = (seed_800fa754.nextInt(257) - 128) * electricEffect.segmentOriginTranslationModifier_26 >> 7;
      } else {
        //LAB_80102f44
        segmentOriginX = 0;
        segmentOriginZ = 0;
      }
      //LAB_80102f4c
    }

    //LAB_80102f64
    this.modifyLightningSegmentOriginsBySecondaryScriptTranslation(translation, electricEffect, bolt);
  }

  /**
   * Renders certain types of lightning effects. (Confirmed for Rose's D transformation)
   * Used by allocator 0x801052dc
   */
  @Method(0x801030d8L)
  private void renderElectricEffectType0(final ScriptState<EffectManagerData6c<EffectManagerParams.ElectricityType>> state) {
    final EffectManagerData6c<EffectManagerParams.ElectricityType> manager = state.innerStruct_00;

    if(this.currentColourFadeStep_04 + 1 == this.numColourFadeSteps_0c) {
      return;
    }

    this.currentColourFadeStep_04++;

    //LAB_80103140
    if(this.currentColourFadeStep_04 == 1) {
      this.initializeRadialElectricityBoltColour(manager, this);

      //LAB_80103174
      for(int i = 0; i < this.boltCount_00; i++) {
        this.callback_2c.accept(manager, this.bolts_34[i], i);
      }

      return;
    }

    //LAB_801031cc
    this.frameNum_2a = this.frameNum_2a + 1 & 0x1f;

    final boolean effectShouldRender = (manager.params_10.shouldRenderFrameBits_24 >> this.frameNum_2a & 0x1) == 0;

    final Vector2f[] vertexArray = new Vector2f[4];
    Arrays.setAll(vertexArray, i -> new Vector2f());

    final Vector2f refOuterOriginA = new Vector2f();
    final Vector2f lastSegmentRef = new Vector2f();
    final Vector2f refOuterOriginB = new Vector2f();

    final Translucency translucency = Translucency.of(manager.params_10.flags_00 >>> 28 & 0x3);

    final PolyBuilder builder = new PolyBuilder("Electricity effect type 0", GL_TRIANGLES)
      .translucency(translucency);

    //LAB_80103200
    //LAB_8010322c
    for(int boltNum = 0; boltNum < this.boltCount_00; boltNum++) {
      final LightningBoltEffect14 bolt = this.bolts_34[boltNum];

      if(this.reinitializeNodes_24) {
        this.initializeElectricityNodes(manager.params_10.trans_04, this, bolt);
      }

      //LAB_8010324c
      this.callback_2c.accept(manager, bolt, boltNum);
      bolt.angle_02 -= this.boltAngleStep_10 / 2.0f;
      float zMod = FUN_800cfb94(manager, bolt.rotation_04, bolt.boltSegments_10[0].origin_00, refOuterOriginA) / 4.0f;
      FUN_800cfb94(manager, bolt.rotation_04, bolt.boltSegments_10[this.boltSegmentCount_28 - 1].origin_00, lastSegmentRef);
      final float boltLengthX = lastSegmentRef.x - refOuterOriginA.x;
      final float boltLengthY = lastSegmentRef.y - refOuterOriginA.y;
      final float segmentLengthX = boltLengthX / (this.boltSegmentCount_28 - 1);
      final float segmentLengthY = boltLengthY / (this.boltSegmentCount_28 - 1);
      float previousOriginX = refOuterOriginA.x;
      float centerLineOriginX = refOuterOriginA.x;
      float previousOriginY = refOuterOriginA.y;
      float centerLineOriginY = refOuterOriginA.y;
      final float firstSegmentScaleX = bolt.boltSegments_10[0].scaleMultiplier_28 * manager.params_10.scale_16.x;
      final float angle = -MathHelper.atan2(boltLengthX, boltLengthY);
      final float sin = MathHelper.sin(angle);
      final float cos = MathHelper.cosFromSin(sin, angle);
      final float outerXOffset = cos * firstSegmentScaleX;
      final float outerYOffset = sin * firstSegmentScaleX;
      refOuterOriginB.x = refOuterOriginA.x - outerXOffset;
      refOuterOriginB.y = refOuterOriginA.y - outerYOffset;
      refOuterOriginA.x += outerXOffset;
      refOuterOriginA.y += outerYOffset;

      //LAB_80103488
      for(int segmentNum = 0; segmentNum < this.boltSegmentCount_28; segmentNum++) {
        final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[segmentNum];
        segment.innerColour_10.sub(segment.innerColourFadeStep_1c);
        segment.outerColour_16.sub(segment.outerColourFadeStep_22);
        segment.unused_2a += this.boltAngleStep_10 / 2.0f;
      }

      //LAB_80103538
      if(effectShouldRender) {
        final float z = manager.params_10.z_22 + zMod;
        if(z >= 0xa0) {
          if(z >= 0xffe) {
            zMod = 0xffe - manager.params_10.z_22;
          }

          //LAB_80103574
          //LAB_80103594
          for(int segmentNum = 0; segmentNum < this.boltSegmentCount_28 - 1; segmentNum++) {
            final LightningBoltEffectSegment30 currentSegment = bolt.boltSegments_10[segmentNum];
            final LightningBoltEffectSegment30 nextSegment = bolt.boltSegments_10[segmentNum + 1];
            float centerLineEndpointX = centerLineOriginX + segmentLengthX;
            float centerLineEndpointY = centerLineOriginY + segmentLengthY;
            final float currentSegmentEndpointX = centerLineOriginX + segmentLengthX;
            final float currentSegmentEndpointY = centerLineOriginY + segmentLengthY;
            centerLineOriginX += cos * currentSegment.originTranslationMagnitude_2c;
            centerLineOriginY += sin * currentSegment.originTranslationMagnitude_2c;
            centerLineEndpointX += cos * nextSegment.originTranslationMagnitude_2c;
            centerLineEndpointY += sin * nextSegment.originTranslationMagnitude_2c;
            final float scale = currentSegment.scaleMultiplier_28 * manager.params_10.scale_16.x;
            final float outerEndpointXa = centerLineEndpointX + cos * scale;
            final float outerEndpointYa = centerLineEndpointY + sin * scale;
            final float outerEndpointXb = centerLineEndpointX - cos * scale;
            final float outerEndpointYb = centerLineEndpointY - sin * scale;

            if(this.hasMonochromeBase_29) {
              final float baseX0 = (previousOriginX - centerLineOriginX) * currentSegment.baseVertexTranslationScale_2e + centerLineOriginX;
              final float baseY0 = (previousOriginY - centerLineOriginY) * currentSegment.baseVertexTranslationScale_2e + centerLineOriginY;
              final float baseX2 = (centerLineEndpointX - centerLineOriginX) * currentSegment.baseVertexTranslationScale_2e + centerLineOriginX;
              final float baseY2 = (centerLineEndpointY - centerLineOriginY) * currentSegment.baseVertexTranslationScale_2e + centerLineOriginY;

              //LAB_80103808
              int baseColour = (int)(currentSegment.innerColour_10.x + Math.abs(currentSegment.originTranslationMagnitude_2c - nextSegment.originTranslationMagnitude_2c) * 8);
              if((baseColour & 0xffff) > 0xff00) {
                baseColour = 0xff00;
              }

              //LAB_80103834
              builder
                .addVertex(baseX0, baseY0, manager.params_10.z_22 + zMod)
                .monochrome((baseColour >>> 9) / 255.0f)
                .addVertex(centerLineOriginX, centerLineOriginY, manager.params_10.z_22 + zMod)
                .monochrome((baseColour >>> 8) / 255.0f)
                .addVertex(baseX2, baseY2, manager.params_10.z_22 + zMod)
                .monochrome((baseColour >>> 9) / 255.0f);
            }

            //LAB_80103994
            vertexArray[1].set(centerLineEndpointX, centerLineEndpointY);
            vertexArray[3].set(centerLineOriginX, centerLineOriginY);

            vertexArray[0].set(outerEndpointXa, outerEndpointYa);
            vertexArray[2].set(refOuterOriginA);
            renderSegmentGradient(builder, currentSegment.outerColour_16, nextSegment.outerColour_16, vertexArray, zMod, manager.params_10.z_22, translucency);

            vertexArray[0].x = (vertexArray[0].x - vertexArray[1].x) / manager.params_10.sizeDivisor_30 + vertexArray[1].x;
            vertexArray[0].y = (vertexArray[0].y - vertexArray[1].y) / manager.params_10.sizeDivisor_30 + vertexArray[1].y;
            vertexArray[2].x = (vertexArray[2].x - vertexArray[3].x) / manager.params_10.sizeDivisor_30 + vertexArray[3].x;
            vertexArray[2].y = (vertexArray[2].y - vertexArray[3].y) / manager.params_10.sizeDivisor_30 + vertexArray[3].y;
            renderSegmentGradient(builder, currentSegment.innerColour_10, nextSegment.innerColour_10, vertexArray, zMod, manager.params_10.z_22, translucency);

            vertexArray[0].set(outerEndpointXb, outerEndpointYb);
            vertexArray[2].set(refOuterOriginB);
            renderSegmentGradient(builder, currentSegment.outerColour_16, nextSegment.outerColour_16, vertexArray, zMod, manager.params_10.z_22, translucency);

            vertexArray[0].x = (vertexArray[0].x - vertexArray[1].x) / manager.params_10.sizeDivisor_30 + vertexArray[1].x;
            vertexArray[0].y = (vertexArray[0].y - vertexArray[1].y) / manager.params_10.sizeDivisor_30 + vertexArray[1].y;
            vertexArray[2].x = (vertexArray[2].x - vertexArray[3].x) / manager.params_10.sizeDivisor_30 + vertexArray[3].x;
            vertexArray[2].y = (vertexArray[2].y - vertexArray[3].y) / manager.params_10.sizeDivisor_30 + vertexArray[3].y;
            renderSegmentGradient(builder, currentSegment.innerColour_10, nextSegment.innerColour_10, vertexArray, zMod, manager.params_10.z_22, translucency);

            refOuterOriginA.set(outerEndpointXa, outerEndpointYa);
            refOuterOriginB.set(outerEndpointXb, outerEndpointYb);
            previousOriginX = centerLineOriginX;
            previousOriginY = centerLineOriginY;
            centerLineOriginX = currentSegmentEndpointX;
            centerLineOriginY = currentSegmentEndpointY;
          }
          //LAB_80103ca0
        }
      }
    }

    if(builder.count() != 0) {
      final Obj obj = builder.build();
      obj.delete();

      this.transforms.transfer.set(GPU.getOffsetX(), GPU.getOffsetY(), 0.0f);
      RENDERER.queueOrthoModel(obj, this.transforms, QueuedModelStandard.class);
    }
  }

  /**
   * Renders some lightning effects (confirmed at the end of Rose's D transformation, Melbu's D Block,
   * some Haschel stuff, Doel's fancy lightning bomb attack)
   * Used by allocator 0x801052dc
   */
  @Method(0x80103db0L)
  private void renderElectricEffectType1(final ScriptState<EffectManagerData6c<EffectManagerParams.ElectricityType>> state) {
    final EffectManagerData6c<EffectManagerParams.ElectricityType> manager = state.innerStruct_00;

    if(this.currentColourFadeStep_04 + 1 == this.numColourFadeSteps_0c) {
      return;
    }

    this.currentColourFadeStep_04++;

    //LAB_80103e40
    if(this.currentColourFadeStep_04 == 1) {
      this.initializeRadialElectricityBoltColour(manager, this);

      //LAB_80103e8c
      for(int i = 0; i < this.boltCount_00; i++) {
        this.callback_2c.accept(manager, this.bolts_34[i], i);
      }
    } else {
      final LightningBoltEffectSegmentOrigin08[] segmentArray = new LightningBoltEffectSegmentOrigin08[this.boltSegmentCount_28];
      Arrays.setAll(segmentArray, LightningBoltEffectSegmentOrigin08::new);
      LightningBoltEffectSegmentOrigin08 currentSegmentOrigin;
      LightningBoltEffectSegmentOrigin08 nextSegmentOrigin;

      //LAB_80103ee4
      this.frameNum_2a = this.frameNum_2a + 1 & 0x1f;
      final boolean effectShouldRender = (manager.params_10.shouldRenderFrameBits_24 >> this.frameNum_2a & 0x1) == 0;

      final Translucency translucency = Translucency.of(manager.params_10.flags_00 >>> 28 & 0x3);

      final PolyBuilder builder = new PolyBuilder("Lightning effect type 1", GL_TRIANGLES)
        .translucency(translucency);

      //LAB_80103f18
      //LAB_80103f44
      for(int i = 0; i < this.boltCount_00; i++) {
        final LightningBoltEffect14 bolt = this.bolts_34[i];

        if(this.reinitializeNodes_24) {
          this.initializeElectricityNodes(manager.params_10.trans_04, this, bolt);
        }

        //LAB_80103f6c
        this.callback_2c.accept(manager, bolt, i);

        bolt.angle_02 -= this.boltAngleStep_10 / 2.0f;

        //LAB_80103fc4
        int segmentNum;
        for(segmentNum = 0; segmentNum < this.boltSegmentCount_28; segmentNum++) {
          final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[segmentNum];
          final LightningBoltEffectSegmentOrigin08 segmentOrigin = segmentArray[segmentNum];
          final Vector2f ref = new Vector2f();
          bolt.sz3_0c = FUN_800cfb94(manager, bolt.rotation_04, segment.origin_00, ref) / 4;
          segmentOrigin.x_00 = ref.x;
          segmentOrigin.y_04 = ref.y;
          segment.innerColour_10.sub(segment.innerColourFadeStep_1c);
          segment.outerColour_16.sub(segment.outerColourFadeStep_22);
          segment.unused_2a += this.boltAngleStep_10;
        }

        //LAB_801040d0
        if(effectShouldRender) {
          final float boltLengthX = segmentArray[segmentNum - 1].x_00 - segmentArray[0].x_00;
          final float boltLengthY = segmentArray[segmentNum - 1].y_04 - segmentArray[0].y_04;
          final float angle = -MathHelper.atan2(boltLengthX, boltLengthY);
          final float sin = MathHelper.sin(angle);
          final float cos = MathHelper.cosFromSin(sin, angle);
          float currentSegmentScale = bolt.boltSegments_10[0].scaleMultiplier_28 * manager.params_10.scale_16.x;
          float outerOriginXa = segmentArray[0].x_00 + cos * currentSegmentScale;
          float outerOriginYa = segmentArray[0].y_04 + sin * currentSegmentScale;
          float outerOriginXb = segmentArray[0].x_00 - cos * currentSegmentScale;
          float outerOriginYb = segmentArray[0].y_04 - sin * currentSegmentScale;

          final float z = manager.params_10.z_22 + bolt.sz3_0c;
          if(z >= 0xa0) {
            if(z >= 0xffe) {
              bolt.sz3_0c = 0xffe - manager.params_10.z_22;
            }

            //LAB_8010422c
            //LAB_8010424c
            for(segmentNum = 0; segmentNum < this.boltSegmentCount_28 - 1; segmentNum++) {
              final LightningBoltEffectSegment30 currentSegment = bolt.boltSegments_10[segmentNum];
              final LightningBoltEffectSegment30 nextSegment = bolt.boltSegments_10[segmentNum + 1];
              currentSegmentScale = currentSegment.scaleMultiplier_28 * manager.params_10.scale_16.x;
              final float nextSegmentScale = nextSegment.scaleMultiplier_28 * manager.params_10.scale_16.x;

              if(this.type1RendererType_18) {
                currentSegmentOrigin = segmentArray[segmentNum];
                nextSegmentOrigin = segmentArray[segmentNum + 1];
                final float outerEndpointXa = nextSegmentOrigin.x_00 + cos * currentSegmentScale;
                final float outerEndpointYa = nextSegmentOrigin.y_04 + sin * currentSegmentScale;
                final float outerEndpointXb = nextSegmentOrigin.x_00 - cos * currentSegmentScale;
                final float outerEndpointYb = nextSegmentOrigin.y_04 - sin * currentSegmentScale;

                final Vector2f[] vertexArray = new Vector2f[4];
                Arrays.setAll(vertexArray, n -> new Vector2f());

                vertexArray[1].x = nextSegmentOrigin.x_00;
                vertexArray[1].y = nextSegmentOrigin.y_04;
                vertexArray[3].x = currentSegmentOrigin.x_00;
                vertexArray[3].y = currentSegmentOrigin.y_04;

                vertexArray[0].x = outerEndpointXa;
                vertexArray[0].y = outerEndpointYa;
                vertexArray[2].x = outerOriginXa;
                vertexArray[2].y = outerOriginYa;
                renderSegmentGradient(builder, currentSegment.outerColour_16, nextSegment.outerColour_16, vertexArray, bolt.sz3_0c, manager.params_10.z_22, translucency);

                vertexArray[0].x = (vertexArray[0].x - vertexArray[1].x) / manager.params_10.sizeDivisor_30 + vertexArray[1].x;
                vertexArray[0].y = (vertexArray[0].y - vertexArray[1].y) / manager.params_10.sizeDivisor_30 + vertexArray[1].y;
                vertexArray[2].x = (vertexArray[2].x - vertexArray[3].x) / manager.params_10.sizeDivisor_30 + vertexArray[3].x;
                vertexArray[2].y = (vertexArray[2].y - vertexArray[3].y) / manager.params_10.sizeDivisor_30 + vertexArray[3].y;
                renderSegmentGradient(builder, currentSegment.innerColour_10, nextSegment.innerColour_10, vertexArray, bolt.sz3_0c, manager.params_10.z_22, translucency);

                vertexArray[0].x = outerEndpointXb;
                vertexArray[0].y = outerEndpointYb;
                vertexArray[2].x = outerOriginXb;
                vertexArray[2].y = outerOriginYb;
                renderSegmentGradient(builder, currentSegment.outerColour_16, nextSegment.outerColour_16, vertexArray, bolt.sz3_0c, manager.params_10.z_22, translucency);

                vertexArray[0].x = (vertexArray[0].x - vertexArray[1].x) / manager.params_10.sizeDivisor_30 + vertexArray[1].x;
                vertexArray[0].y = (vertexArray[0].y - vertexArray[1].y) / manager.params_10.sizeDivisor_30 + vertexArray[1].y;
                vertexArray[2].x = (vertexArray[2].x - vertexArray[3].x) / manager.params_10.sizeDivisor_30 + vertexArray[3].x;
                vertexArray[2].y = (vertexArray[2].y - vertexArray[3].y) / manager.params_10.sizeDivisor_30 + vertexArray[3].y;
                renderSegmentGradient(builder, currentSegment.innerColour_10, nextSegment.innerColour_10, vertexArray, bolt.sz3_0c, manager.params_10.z_22, translucency);

                outerOriginXa = outerEndpointXa;
                outerOriginYa = outerEndpointYa;
                outerOriginXb = outerEndpointXb;
                outerOriginYb = outerEndpointYb;
              } else {
                //LAB_801045e8
                currentSegmentOrigin = segmentArray[segmentNum];
                nextSegmentOrigin = segmentArray[segmentNum + 1];
                float centerLineEndpointX = Math.abs(currentSegmentOrigin.x_00 - nextSegmentOrigin.x_00);
                centerLineEndpointX = seed_800fa754.nextFloat(centerLineEndpointX * 2 + 1) - centerLineEndpointX + currentSegmentOrigin.x_00;
                float centerLineOriginX = currentSegmentOrigin.x_00;
                float centerLineOriginY = currentSegmentOrigin.y_04;
                float centerLineEndpointY = (nextSegmentOrigin.y_04 - currentSegmentOrigin.y_04) / 2 + currentSegmentOrigin.y_04;
                final float nextSegmentQuarterScale = nextSegmentScale / 4.0f;
                final float currentSegmentQuarterScale = currentSegmentScale / 4.0f;

                //LAB_801046c4
                for(int j = 2; j > 0; j--) {
                  final Vector2f[] vertexArray = new Vector2f[4];
                  Arrays.setAll(vertexArray, n -> new Vector2f());

                  vertexArray[0].y = centerLineEndpointY;
                  vertexArray[1].y = centerLineEndpointY;
                  vertexArray[2].y = centerLineOriginY;
                  vertexArray[3].y = centerLineOriginY;

                  vertexArray[0].x = centerLineEndpointX - nextSegmentScale;
                  vertexArray[1].x = centerLineEndpointX + 1;
                  vertexArray[2].x = centerLineOriginX - currentSegmentScale;
                  vertexArray[3].x = centerLineOriginX + 1;
                  renderSegmentGradient(builder, currentSegment.outerColour_16, nextSegment.outerColour_16, vertexArray, bolt.sz3_0c, manager.params_10.z_22, translucency);
                  vertexArray[0].x = centerLineEndpointX - nextSegmentQuarterScale;
                  vertexArray[2].x = centerLineOriginX - currentSegmentQuarterScale;
                  renderSegmentGradient(builder, currentSegment.innerColour_10, nextSegment.innerColour_10, vertexArray, bolt.sz3_0c, manager.params_10.z_22, translucency);
                  vertexArray[0].x = centerLineEndpointX + nextSegmentScale;
                  vertexArray[1].x = centerLineEndpointX;
                  vertexArray[2].x = centerLineOriginX + currentSegmentScale;
                  vertexArray[3].x = centerLineOriginX;
                  renderSegmentGradient(builder, currentSegment.outerColour_16, nextSegment.outerColour_16, vertexArray, bolt.sz3_0c, manager.params_10.z_22, translucency);
                  vertexArray[0].x = centerLineEndpointX + nextSegmentQuarterScale;
                  vertexArray[2].x = centerLineOriginX + currentSegmentQuarterScale;
                  renderSegmentGradient(builder, currentSegment.innerColour_10, nextSegment.innerColour_10, vertexArray, bolt.sz3_0c, manager.params_10.z_22, translucency);

                  centerLineOriginX = centerLineEndpointX;
                  centerLineOriginY = centerLineEndpointY;
                  centerLineEndpointX = nextSegmentOrigin.x_00;
                  centerLineEndpointY = nextSegmentOrigin.y_04;
                }
              }
            }
          }
        }
      }

      if(effectShouldRender) {
        final Obj obj = builder.build();
        obj.delete();

        this.transforms.transfer.set(GPU.getOffsetX(), GPU.getOffsetY(), 0.0f);
        RENDERER.queueOrthoModel(obj, this.transforms, QueuedModelStandard.class);
      }
    }
  }

  @Method(0x801049d4L)
  private void FUN_801049d4(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final LightningBoltEffect14 a2, final int a3) {
    // no-op
  }

  @Method(0x801049dcL)
  private void FUN_801049dc(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final LightningBoltEffect14 boltEffect, final int boltIndex) {
    boltEffect.rotation_04.y = MathHelper.TWO_PI / this.boltCount_00 * boltIndex;
    boltEffect.rotation_04.z = MathHelper.psxDegToRad(this.segmentOriginTranslationMagnitude_1e * 2);
  }

  @Method(0x80104a14L)
  private void FUN_80104a14(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final LightningBoltEffect14 bolt, final int a3) {
    bolt.rotation_04.x = seed_800fa754.nextFloat(MathHelper.TWO_PI);
    bolt.rotation_04.y = seed_800fa754.nextFloat(MathHelper.TWO_PI);
    bolt.rotation_04.z = seed_800fa754.nextFloat(MathHelper.TWO_PI);
  }

  @Method(0x80104b10L)
  private void FUN_80104b10(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final LightningBoltEffect14 bolt, final int a3) {
    final float angleStep = MathHelper.psxDegToRad(manager.params_10._28 << 8 >> 8);
    float angle = bolt.angle_02;

    //LAB_80104b58
    for(int i = 0; i < this.boltSegmentCount_28; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.x += MathHelper.cos(angle) * this.segmentOriginTranslationMagnitude_1e;
      segment.origin_00.z += MathHelper.sin(angle) * this.segmentOriginTranslationMagnitude_1e;
      angle += angleStep;
    }

    //LAB_80104bcc
  }

  @Method(0x80104becL)
  private void FUN_80104bec(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final LightningBoltEffect14 bolt, final int a3) {
    final float angleStep = MathHelper.psxDegToRad(manager.params_10._28 << 8 >> 8);
    float angle = bolt.angle_02;

    //LAB_80104c34
    for(int i = 0; i < this.boltSegmentCount_28; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.z += MathHelper.sin(angle) * this.segmentOriginTranslationMagnitude_1e;
      angle += angleStep;
    }

    //LAB_80104c7c
  }

  @Method(0x80104c9cL)
  private void FUN_80104c9c(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final LightningBoltEffect14 bolt, final int a3) {
    int translationMagnitudeModifier = 0;
    int segmentIndex = (this.boltSegmentCount_28 - 2) / 2;
    final int angle0 = (manager.params_10._28 & 0xff) << 4;
    final int angle1 = (manager.params_10._28 & 0xff00) >>> 4;
    final int angle2 = manager.params_10._28 >>> 12 & 0xff0;

    //LAB_80104d24
    for(int i = 1; i < this.boltSegmentCount_28; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      translationMagnitudeModifier = translationMagnitudeModifier + segmentIndex;
      final int translationMagnitude = translationMagnitudeModifier * this.segmentOriginTranslationMagnitude_1e;
      final int x = translationMagnitude * (rsin(angle1) + rcos(angle2)) >> 12;
      final int y = translationMagnitude * (rcos(angle0) + rcos(angle2)) >> 12;
      final int z = translationMagnitude * (rcos(angle1) + rsin(angle0)) >> 12;
      segment.origin_00.add(x, y, z);
      segmentIndex--;
    }
  }

  @Method(0x80104e40L)
  private void FUN_80104e40(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final LightningBoltEffect14 bolt, final int a3) {
    final int translationMagnitude = this.segmentOriginTranslationMagnitude_1e / this.boltCount_00 & 0xffff;
    final int angle = 0x1000 / this.boltCount_00 * a3;

    //LAB_80104ec4
    short originTranslationX = 0;
    short originTranslationY = 0;
    for(int i = 1; i < this.boltSegmentCount_28; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.x += originTranslationX;
      segment.origin_00.z += originTranslationY;
      originTranslationX += rcos(angle) * translationMagnitude >> 12;
      originTranslationY += rsin(angle) * translationMagnitude >> 12;
    }
  }

  @Method(0x80104f70L)
  private void FUN_80104f70(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final LightningBoltEffect14 bolt, final int a3) {
    final int angleStep = 0x1000 / (this.boltSegmentCount_28 - 1);
    int angle = 0;

    //LAB_80104fb8
    for(int i = 0; i < this.boltSegmentCount_28; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.x += rsin(angle) * this.segmentOriginTranslationMagnitude_1e >> 12;
      segment.origin_00.z += rcos(angle) * this.segmentOriginTranslationMagnitude_1e >> 12;
      segment.origin_00.y = 0.0f;
      angle += angleStep;
    }
  }

  @Method(0x80105050L)
  private void FUN_80105050(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final LightningBoltEffect14 bolt, final int boltAngleModifier) {
    final int segmentCount = this.boltSegmentCount_28;
    final int angleStep = 0x800 / (segmentCount - 1);
    final int boltAngleZ = 0x1000 / this.boltCount_00 * boltAngleModifier;
    int angle = 0;

    //LAB_801050c0
    for(int i = 0; i < segmentCount; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.x += rcos(angle) * this.segmentOriginTranslationMagnitude_1e >> 12;
      segment.origin_00.y = (rsin(angle) * rsin(angle) >> 12) * this.segmentOriginTranslationMagnitude_1e >> 12;
      segment.origin_00.z += (rsin(angle) * rcos(boltAngleZ) >> 12) * this.segmentOriginTranslationMagnitude_1e >> 12;
      angle += angleStep;
    }
  }

  @Method(0x801051acL)
  private void FUN_801051ac(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final LightningBoltEffect14 bolt, final int a3) {
    final int segmentCount = this.boltSegmentCount_28;
    final float angleStepXZ = MathHelper.TWO_PI / (segmentCount - 1);
    final float angleStepY = MathHelper.psxDegToRad(manager.params_10._28 << 8 >> 8);
    float angleY = bolt.angle_02;
    float angleXZ = 0;

    //LAB_80105210
    for(int i = 0; i < segmentCount; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.x += MathHelper.sin(angleXZ) * this.segmentOriginTranslationMagnitude_1e;
      segment.origin_00.y = MathHelper.sin(angleY) * this.segmentOriginTranslationMagnitude_1e / 4;
      segment.origin_00.z += MathHelper.cos(angleXZ) * this.segmentOriginTranslationMagnitude_1e;
      angleXZ = angleXZ + angleStepXZ;
      angleY = angleY + angleStepY;
    }
  }

  /**
   * <ol start="0">
   *   <li>{@link this#FUN_801049d4}</li>
   *   <li>{@link this#FUN_801049dc}</li>
   *   <li>{@link this#FUN_80104a14}</li>
   *   <li>{@link this#FUN_80104b10}</li>
   *   <li>{@link this#FUN_80104bec}</li>
   *   <li>{@link this#FUN_80104c9c}</li>
   *   <li>{@link this#FUN_80104e40}</li>
   *   <li>{@link this#FUN_80104f70}</li>
   *   <li>{@link this#FUN_80105050}</li>
   *   <li>{@link this#FUN_801051ac}</li>
   *   <li>{@link this#FUN_801049d4}</li>
   * </ol>
   */
  private final TriConsumer<EffectManagerData6c<EffectManagerParams.ElectricityType>, LightningBoltEffect14, Integer>[] electricityEffectCallbacks_80119ee8 = new TriConsumer[11];
  {
    this.electricityEffectCallbacks_80119ee8[0] = this::FUN_801049d4;
    this.electricityEffectCallbacks_80119ee8[1] = this::FUN_801049dc;
    this.electricityEffectCallbacks_80119ee8[2] = this::FUN_80104a14;
    this.electricityEffectCallbacks_80119ee8[3] = this::FUN_80104b10;
    this.electricityEffectCallbacks_80119ee8[4] = this::FUN_80104bec;
    this.electricityEffectCallbacks_80119ee8[5] = this::FUN_80104c9c;
    this.electricityEffectCallbacks_80119ee8[6] = this::FUN_80104e40;
    this.electricityEffectCallbacks_80119ee8[7] = this::FUN_80104f70;
    this.electricityEffectCallbacks_80119ee8[8] = this::FUN_80105050;
    this.electricityEffectCallbacks_80119ee8[9] = this::FUN_801051ac;
    this.electricityEffectCallbacks_80119ee8[10] = this::FUN_801049d4;
  }
  /**
   * <ol start="0">
   *   <li>{@link this#renderElectricEffectType0}</li>
   *   <li>{@link this#renderElectricEffectType0}</li>
   *   <li>{@link this#renderElectricEffectType0}</li>
   *   <li>{@link this#renderElectricEffectType1}</li>
   *   <li>{@link this#renderElectricEffectType1}</li>
   *   <li>{@link this#renderElectricEffectType1}</li>
   *   <li>{@link this#renderElectricEffectType1}</li>
   *   <li>{@link this#renderElectricEffectType1}</li>
   *   <li>{@link this#renderElectricEffectType1}</li>
   *   <li>{@link this#renderElectricEffectType1}</li>
   *   <li>{@link this#renderElectricEffectType1}</li>
   * </ol>
   */
  private final Consumer<ScriptState<EffectManagerData6c<EffectManagerParams.ElectricityType>>>[] electricityEffectRenderers_80119f14 = new Consumer[11];
  {
    this.electricityEffectRenderers_80119f14[0] = this::renderElectricEffectType0;
    this.electricityEffectRenderers_80119f14[1] = this::renderElectricEffectType0;
    this.electricityEffectRenderers_80119f14[2] = this::renderElectricEffectType0;
    this.electricityEffectRenderers_80119f14[3] = this::renderElectricEffectType1;
    this.electricityEffectRenderers_80119f14[4] = this::renderElectricEffectType1;
    this.electricityEffectRenderers_80119f14[5] = this::renderElectricEffectType1;
    this.electricityEffectRenderers_80119f14[6] = this::renderElectricEffectType1;
    this.electricityEffectRenderers_80119f14[7] = this::renderElectricEffectType1;
    this.electricityEffectRenderers_80119f14[8] = this::renderElectricEffectType1;
    this.electricityEffectRenderers_80119f14[9] = this::renderElectricEffectType1;
    this.electricityEffectRenderers_80119f14[10] = this::renderElectricEffectType1;
  }
}
