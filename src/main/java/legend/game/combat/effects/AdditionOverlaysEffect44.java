package legend.game.combat.effects;

import legend.core.Config;
import legend.core.MathHelper;
import legend.core.QueuedModel;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.types.AdditionHitProperties10;
import legend.game.combat.ui.AdditionOverlayMode;
import legend.game.modding.coremod.CoreMod;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Math;
import org.joml.Vector3f;

import java.util.Arrays;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.battlePreloadedEntities_1f8003f4;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_800b.press_800bee94;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.combat.SEffe.additionBorderColours_800fb7f0;
import static legend.game.combat.SEffe.additionHitCompletionState_8011a014;
import static legend.game.combat.SEffe.additionOverlayActive_80119f41;
import static legend.game.combat.SEffe.renderButtonPressHudElement1;

public class AdditionOverlaysEffect44 implements Effect<EffectManagerParams.VoidType> {
  public int attackerScriptIndex_00;
  public int targetScriptIndex_04;

  public final Vector3f attackerStartingPosition_10 = new Vector3f();
  public final Vector3f distancePerFrame_20 = new Vector3f();

  /** ubyte */
  public int count_30;
  /** ubyte; 0 = renders and ticks, 1 = skips render and tick, 2 = renders only; set by scriptAlterAdditionContinuationState */
  public int pauseTickerAndRenderer_31;
  /** byte */
  public int additionComplete_32;

  /** short */
  public int currentFrame_34;
  /** ushort */
  public int unused_36;
  /** ubyte */
  public int numFramesToRenderCenterSquare_38;
  /** ubyte */
  public int lastCompletedHit_39;
  /** ubyte; 0 = no auto complete, 2 = WC and UW auto-complete */
  public int autoCompleteType_3a;

  // Not needed anymore, just reference hit overlay array at index lastCompletedHit_39
  // public final Pointer<AdditionOverlaysHit20> lastCompletedHitOverlay_3c;
  public AdditionOverlaysHit20[] hitOverlays_40;

  public Obj reticleBorderShadow;
  public final MV transforms = new MV();

  @Method(0x801062a8L)
  public AdditionOverlaysEffect44(final int attackerScriptIndex, final int targetScriptIndex, final int autoCompleteType) {
    final BattleEntity27c s5 = (BattleEntity27c)scriptStatePtrArr_800bc1c0[attackerScriptIndex].innerStruct_00;

    this.reticleBorderShadow = new QuadBuilder("Reticle background")
      .translucency(Translucency.B_MINUS_F)
      .monochrome(0, 0.0f)
      .monochrome(1, 0.0f)
      .monochrome(2, 1.0f)
      .monochrome(3, 1.0f)
      .pos(-1.0f, -0.5f, 0.0f)
      .size(1.0f, 1.0f)
      .build();

    //LAB_8010633c
    int hitNum;
    for(hitNum = 0; hitNum < 8; hitNum++) {
      // Number of hits calculated by counting to first hit with 0 total frames
      if((this.getHitProperty(s5.charSlot_276, hitNum, 1, autoCompleteType) & 0xff) == 0) {
        break;
      }
    }

    //LAB_80106374
    final int hitCount = hitNum - 1;
    this.count_30 = hitCount;
    this.attackerScriptIndex_00 = attackerScriptIndex;
    this.targetScriptIndex_04 = targetScriptIndex;
    this.currentFrame_34 = 0;
    this.pauseTickerAndRenderer_31 = 0;
    this.additionComplete_32 = 0;
    this.numFramesToRenderCenterSquare_38 = 0;
    this.lastCompletedHit_39 = 0;
    this.autoCompleteType_3a = autoCompleteType;
    final AdditionOverlaysHit20[] hitArray = new AdditionOverlaysHit20[hitCount];
    Arrays.setAll(hitArray, AdditionOverlaysHit20::new);
    this.hitOverlays_40 = hitArray;
    int overlayDisplayDelay = this.getHitProperty(s5.charSlot_276, 0, 15, autoCompleteType) & 0xff;
    this.unused_36 = overlayDisplayDelay;

    //LAB_801063f0
    for(hitNum = 0; hitNum < this.count_30; hitNum++) {
      final AdditionOverlaysHit20 hitOverlay = hitArray[hitNum];
      hitOverlay.unused_00 = 1;
      hitOverlay.hitSuccessful_01 = false;
      hitOverlay.shadowColour_08 = (short)0;
      hitOverlay.frameSuccessLowerBound_10 = (short)(overlayDisplayDelay + 2);
      hitOverlay.borderColoursArrayIndex_02 = 3;
      hitOverlay.isCounter_1c = false;
      additionHitCompletionState_8011a014[hitNum] = 0;
      int hitProperty = this.getHitProperty(s5.charSlot_276, hitNum, 1, autoCompleteType) & 0xff;
      overlayDisplayDelay += hitProperty; // Display delay for each hit
      hitOverlay.totalHitFrames_0a = (short)hitProperty;
      hitProperty = this.getHitProperty(s5.charSlot_276, hitNum, 2, autoCompleteType) & 0xff;
      hitOverlay.frameBeginDisplay_0c = (short)hitProperty;
      hitProperty = this.getHitProperty(s5.charSlot_276, hitNum, 3, autoCompleteType) & 0xff;
      hitOverlay.numSuccessFrames_0e = (short)hitProperty;
      final int successFrameTarget = hitOverlay.frameSuccessLowerBound_10 + hitOverlay.frameBeginDisplay_0c;
      hitOverlay.frameSuccessLowerBound_10 = (short)(successFrameTarget - hitOverlay.numSuccessFrames_0e / 2 + 1);
      hitOverlay.frameSuccessUpperBound_12 = (short)(successFrameTarget + hitOverlay.numSuccessFrames_0e - hitOverlay.numSuccessFrames_0e / 2);

      final AdditionOverlaysBorder0e[] borderArray = hitOverlay.borderArray_18;

      //LAB_8010652c
      if(Config.changeAdditionOverlayRgb()) {
        final int counterRgb = Config.getCounterOverlayRgb();
        final int additionRgb = Config.getAdditionOverlayRgb();
        additionBorderColours_800fb7f0[6] = counterRgb & 0xff;
        additionBorderColours_800fb7f0[7] = counterRgb >> 8 & 0xff;
        additionBorderColours_800fb7f0[8] = counterRgb >> 16 & 0xff;
        additionBorderColours_800fb7f0[9] = additionRgb & 0xff;
        additionBorderColours_800fb7f0[10] = additionRgb >> 8 & 0xff;
        additionBorderColours_800fb7f0[11] = additionRgb >> 16 & 0xff;
      }

      int val = 16;
      for(int borderNum = 0; borderNum < 17; borderNum++) {
        final AdditionOverlaysBorder0e borderOverlay = borderArray[borderNum];
        borderOverlay.size_08 = (18 - val) * 10;
        borderOverlay.isVisible_00 = true;
        //LAB_8010656c
        //LAB_80106574
        borderOverlay.angleModifier_02 = Math.toRadians((16 - val) * 11.25f);
        borderOverlay.countFramesVisible_0c = 5;
        borderOverlay.sideEffects_0d = 0;
        borderOverlay.framesUntilRender_0a = (short)(hitOverlay.frameSuccessLowerBound_10 + (hitOverlay.numSuccessFrames_0e - 1) / 2 + val - 17);
        borderOverlay.r_04 = additionBorderColours_800fb7f0[hitOverlay.borderColoursArrayIndex_02 * 3];
        borderOverlay.g_05 = additionBorderColours_800fb7f0[hitOverlay.borderColoursArrayIndex_02 * 3 + 1];
        borderOverlay.b_06 = additionBorderColours_800fb7f0[hitOverlay.borderColoursArrayIndex_02 * 3 + 2];

        val--;
      }

      //LAB_80106634
      val = 0;
      for(int borderNum = 16; borderNum >= 14; borderNum--) {
        final AdditionOverlaysBorder0e borderOverlay = borderArray[borderNum];
        borderOverlay.size_08 = 20 - val * 2;
        borderOverlay.angleModifier_02 = 0.0f;
        borderOverlay.countFramesVisible_0c = 0x11;
        borderOverlay.framesUntilRender_0a = hitOverlay.frameSuccessLowerBound_10 - 17;

        if(val != 0x1L) {
          borderOverlay.r_04 = 0x30;
          borderOverlay.g_05 = 0x30;
          borderOverlay.b_06 = 0x30;
          borderOverlay.sideEffects_0d = 1;
        } else {
          //LAB_80106680
          borderOverlay.sideEffects_0d = -1;
        }
        //LAB_80106684
        val++;
      }
    }

    // These fields are not used for anything
    //LAB_801066c8
    this.getBentTranslation(this.attackerScriptIndex_00, this.attackerStartingPosition_10, 0);

    final Vector3f targetStartingPosition = new Vector3f();
    this.getBentTranslation(this.targetScriptIndex_04, targetStartingPosition, 1);

    final int firstHitSuccessLowerBound = this.hitOverlays_40[0].frameSuccessLowerBound_10;
    this.distancePerFrame_20.x = (targetStartingPosition.x - this.attackerStartingPosition_10.x) / firstHitSuccessLowerBound;
    this.distancePerFrame_20.y = (targetStartingPosition.y - this.attackerStartingPosition_10.y) / firstHitSuccessLowerBound;
    this.distancePerFrame_20.z = (targetStartingPosition.z - this.attackerStartingPosition_10.z) / firstHitSuccessLowerBound;
  }

  public void setContinuationState(final int continuationState) {
    this.additionComplete_32 = continuationState;

    //LAB_80107954
    final AdditionOverlaysHit20[] hitArray = this.hitOverlays_40;
    for(int hitNum = 0; hitNum < this.count_30; hitNum++) {
      hitArray[hitNum].frameSuccessLowerBound_10 = 0;
      hitArray[hitNum].frameSuccessUpperBound_12 = 0;
      additionHitCompletionState_8011a014[hitNum] = -1;
    }
  }

  /**
   * Used to calculate unused vectors on AdditionOverlaysEffect. Gets translation of attacker or target at start of
   * addition, and for some reason does a meaningless 0 rotation.
   */
  @Method(0x80105f98L)
  private void getBentTranslation(final int scriptIndex, final Vector3f out, final long coordType) {
    final MV transformationMatrix = new MV();

    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;

    final GsCOORDINATE2 coord2;
    if(coordType == 0) {
      coord2 = bent.model_148.modelParts_00[1].coord2_04;
    } else {
      //LAB_80105fe4
      coord2 = bent.model_148.coord2_14;
    }

    //LAB_80105fec
    GsGetLw(coord2, transformationMatrix);
    // Does nothing? Changed line below to set //ApplyMatrixLV(transformationMatrix, zeroVec, out);
    out.set(transformationMatrix.transfer);
  }

  /** Runs callbacks to render correct button icon effects during addition */
  @Method(0x80106050L)
  private void renderAdditionButton(final int frames, final boolean isCounter) {
    final int offset = isCounter ? 1 : 0;
    if(Math.abs(frames) >= 2) {  // Button up position
      renderButtonPressHudElement1(0x24, 119, 43, Translucency.B_PLUS_F, 0x80);
      renderButtonPressHudElement1(additionButtonRenderCallbackIndices_800fb7bc[offset], 115, 48, Translucency.B_PLUS_F, 0x80);
    } else {  // Button down position
      //LAB_80106114
      renderButtonPressHudElement1(0x24, 119, 51, Translucency.B_PLUS_F, 0x80);
      renderButtonPressHudElement1(additionButtonRenderCallbackIndices_800fb7bc[offset + 2], 115, 48, Translucency.B_PLUS_F, 0x80);
      renderButtonPressHudElement1(0x25, 115, 50, Translucency.B_PLUS_F, 0x80);
    }
  }

  /**
   * Selects where to get hit property from based on auto-complete type. 1 and 3 use a mysterious global 2-hit array,
   * which may be unused testing code.
   */
  @Method(0x801061bcL)
  private int getHitProperty(final int charSlot, final int hitNum, final int hitPropertyIndex, final int autoCompleteType) {
    //LAB_80106264
    final int hitPropertyValue;
    if(autoCompleteType == 1 || autoCompleteType == 3) {
      //LAB_80106274
      hitPropertyValue = staticTestAdditionHitProperties_800fb7c0[hitNum].get(hitPropertyIndex);
    } else {
      //LAB_8010628c
      hitPropertyValue = battlePreloadedEntities_1f8003f4.getHitProperty(charSlot, hitNum, hitPropertyIndex) & 0xff;
    }

    //LAB_80106298
    return hitPropertyValue;
  }

  @Method(0x80106774L)
  private int fadeAdditionBorders(final AdditionOverlaysBorder0e square, final int fadeStep) {
    int numberOfNegativeComponents = 0;
    int newColour = square.r_04 - fadeStep;
    final int newR;
    if(newColour > 0) {
      newR = newColour;
    } else {
      newR = 0;
      numberOfNegativeComponents++;
    }

    //LAB_801067b0
    newColour = square.g_05 - fadeStep;
    final int newG;
    if(newColour > 0) {
      newG = newColour;
    } else {
      newG = 0;
      numberOfNegativeComponents++;
    }

    //LAB_801067c4
    newColour = square.b_06 - fadeStep;
    final int newB;
    if(newColour > 0) {
      newB = newColour;
    } else {
      newB = 0;
      numberOfNegativeComponents++;
    }

    //LAB_801067d8
    square.r_04 = newR;
    square.g_05 = newG;
    square.b_06 = newB;
    return numberOfNegativeComponents;
  }

  @Method(0x80106808L)
  private void renderAdditionCentreSolidSquare(final AdditionOverlaysEffect44 effect, final AdditionOverlaysHit20 hitOverlay, final int completionState, final EffectManagerData6c<?> manager) {
    if(manager.params_10.flags_00 >= 0) {
      final AdditionOverlaysBorder0e[] targetBorderArray = hitOverlay.borderArray_18;

      //LAB_8010685c
      for(int targetBorderNum = 0; targetBorderNum < 2; targetBorderNum++) {
        final int squareSize = targetBorderArray[16].size_08 - targetBorderNum * 8;

        effect.transforms.scaling(squareSize * 2.0f, squareSize * 2.0f, 1.0f);
        effect.transforms.transfer.set(GPU.getOffsetX() + squareSize, GPU.getOffsetY() + squareSize + 30.0f, 120.0f);
        final QueuedModel<?> model = RENDERER.queueOrthoModel(RENDERER.centredQuadBPlusF, effect.transforms);

        if(completionState == 1) {  // Success
          model.monochrome(1.0f);
        } else if(completionState != -2) {  // Too early
          model.monochrome(0x30 / 255.0f);
        } else if(hitOverlay.isCounter_1c) {  // Counter-attack too late
          if(Config.changeAdditionOverlayRgb()) {
            model.colour(additionBorderColours_800fb7f0[6] / 255.0f, additionBorderColours_800fb7f0[7] / 255.0f, (additionBorderColours_800fb7f0[8] * 8 - 2) * 8 / 255.0f);
          } else {
            model.colour(targetBorderArray[15].r_04 * 3 / 255.0f, targetBorderArray[15].g_05 / 255.0f, (targetBorderArray[15].b_06 - 1) * 8 / 255.0f);
          }
        } else {  // Too late
          model.colour(targetBorderArray[15].r_04 / 255.0f, targetBorderArray[15].g_05 / 255.0f, targetBorderArray[15].b_06 / 255.0f);
        }
      }
    }

    //LAB_80106a4c
  }

  /** Renders the shadow on the inside of the innermost rotating border. */
  @Method(0x80106ac4L)
  private void renderAdditionBorderShadow(final AdditionOverlaysHit20 hitOverlay, final float angle, final int borderSize) {
    // Would you believe me if I said I knew what I was doing when I wrote any of this?
    final int offset = borderSize - 1;
    final float sin0 = MathHelper.sin(angle);
    final float cos0 = MathHelper.cosFromSin(sin0, angle);
    final float x0 = cos0 * offset / 2.0f;
    final float y0 = sin0 * offset / 2.0f;
    final int colour = hitOverlay.shadowColour_08 * 4;

    this.transforms.transfer.set(x0 + GPU.getOffsetX(), y0 + GPU.getOffsetY() + 30.0f, 124.0f);
    this.transforms
      .scaling(10.0f, borderSize, 1.0f)
      .rotateLocalZ(angle);

    RENDERER.queueOrthoModel(this.reticleBorderShadow, this.transforms)
      .monochrome(colour / 255.0f);
  }

  @Method(0x80106cccL)
  private void renderAdditionBorders(final int hitNum, final AdditionOverlaysHit20[] hitArray) {
    final AdditionOverlaysBorder0e[] borderArray = hitArray[hitNum].borderArray_18;
    final byte currentHitCompletionState = additionHitCompletionState_8011a014[hitNum];

    //LAB_80106d18
    for(int borderNum = 0; borderNum < 17; borderNum++) {
      final AdditionOverlaysBorder0e borderOverlay = borderArray[borderNum];

      if(borderOverlay.isVisible_00 && borderOverlay.framesUntilRender_0a <= 0) {
        this.transforms
          .scaling(borderOverlay.size_08, borderOverlay.size_08, 1.0f)
          .rotateZ(borderOverlay.angleModifier_02);
        this.transforms.transfer.set(GPU.getOffsetX(), GPU.getOffsetY() + 30.0f, 120.0f);

        final QueuedModel<?> model;

        // Set translucent if button press is failure and border sideEffects_0d not innermost rotating border or target (15)
        if((borderOverlay.sideEffects_0d == 0 || borderOverlay.sideEffects_0d == -1) && currentHitCompletionState >= 0) {
          model = RENDERER.queueOrthoModel(RENDERER.lineBox, this.transforms);
        } else {
          model = RENDERER.queueOrthoModel(RENDERER.lineBoxBPlusF, this.transforms);
        }

        if(hitArray[hitNum].isCounter_1c && borderNum != 16) {
          if(Config.changeAdditionOverlayRgb()) {
            final int rgb = Config.getCounterOverlayRgb();

            // Hack to get around lack of separate counterattack color field until full dememulation
            final float rFactor = borderArray[borderNum].r_04 / (float)additionBorderColours_800fb7f0[9];
            final float gFactor = borderArray[borderNum].g_05 / (float)additionBorderColours_800fb7f0[10];
            final float bFactor = borderArray[borderNum].b_06 / (float)additionBorderColours_800fb7f0[11];

            model.colour((rgb & 0xff) * rFactor / 255.0f, (rgb >> 8 & 0xff) * gFactor / 255.0f, (rgb >> 16 & 0xff) * bFactor / 255.0f);
          } else {
            model.colour(borderOverlay.r_04 * 3 / 255.0f, borderOverlay.g_05 / 255.0f, (borderOverlay.b_06 + 1) / 8.0f / 255.0f);
          }
        } else {
          //LAB_80106e58
          model.colour(borderOverlay.r_04 / 255.0f, borderOverlay.g_05 / 255.0f, borderOverlay.b_06 / 255.0f);
        }

        // Renders rotating shadow on innermost rotating border
        if(borderOverlay.sideEffects_0d == 0) {
          this.renderAdditionBorderShadow(hitArray[hitNum], borderOverlay.angleModifier_02, borderOverlay.size_08 * 2);
          this.renderAdditionBorderShadow(hitArray[hitNum], borderOverlay.angleModifier_02 + MathHelper.HALF_PI, borderOverlay.size_08 * 2);
          this.renderAdditionBorderShadow(hitArray[hitNum], borderOverlay.angleModifier_02 + MathHelper.PI, borderOverlay.size_08 * 2);
          this.renderAdditionBorderShadow(hitArray[hitNum], borderOverlay.angleModifier_02 + MathHelper.PI + MathHelper.HALF_PI, borderOverlay.size_08 * 2);
        }
      }
      //LAB_80106fac
    }
  }

  @Method(0x80107088L)
  private int tickBorderDisplay(final int a0, final int hitNum, final AdditionOverlaysHit20[] hitArray) {
    // Darken shadow color of innermost border of current hit
    final AdditionOverlaysHit20 hitOverlay = hitArray[hitNum];
    if(this.currentFrame_34 >= hitOverlay.frameSuccessLowerBound_10 - 0x11) {
      hitOverlay.shadowColour_08 += 1;

      if(hitOverlay.shadowColour_08 >= 0xe) {
        hitOverlay.shadowColour_08 = 0xd;
      }
    }

    //LAB_801070ec
    final byte currentHitCompletionState = additionHitCompletionState_8011a014[hitNum];
    final AdditionOverlaysBorder0e[] borderArray = hitOverlay.borderArray_18;
    int isRendered = 0;

    //LAB_80107104
    for(int borderNum = 0; borderNum < 17; borderNum++) {
      final AdditionOverlaysBorder0e borderOverlay = borderArray[borderNum];

      // Fade shadow if hit failed, set invisible if border is within 0x20 of fully faded
      if(currentHitCompletionState < 0) {
        hitOverlay.shadowColour_08 -= 3;

        if(hitOverlay.shadowColour_08 < 0) {
          hitOverlay.shadowColour_08 = 0;
        }

        //LAB_80107134
        if(this.fadeAdditionBorders(borderOverlay, 0x20) == 3) {
          borderOverlay.isVisible_00 = false;
        }
      }

      //LAB_80107150
      if(borderOverlay.isVisible_00) {
        if(borderOverlay.framesUntilRender_0a > 0) {
          borderOverlay.framesUntilRender_0a--;
        } else {
          //LAB_80107178
          if(borderOverlay.sideEffects_0d != -1) {
            borderOverlay.sideEffects_0d++;
          }

          //LAB_80107190
          borderOverlay.countFramesVisible_0c--;
          if(borderOverlay.countFramesVisible_0c == 0) {
            borderOverlay.isVisible_00 = false;
          }

          //LAB_801071b0
          // Fade rotating borders only
          if(borderNum < 14) {
            this.fadeAdditionBorders(borderOverlay, 0x4e);
          }

          isRendered = 1;
        }
      }
      //LAB_801071c0
    }

    return isRendered;
  }

  /** If a hit is failed, flag all subsequent hits as failed as well */
  @Method(0x801071fcL)
  private void propagateFailedAdditionHitFlag(final AdditionOverlaysHit20[] hitArray, int hitNum) {
    final byte currentHitCompletionState = additionHitCompletionState_8011a014[hitNum];
    this.additionComplete_32 = 1;

    //LAB_80107234
    hitNum += 1;
    for(; hitNum < this.count_30; hitNum++) {
      final AdditionOverlaysHit20 hitOverlay = hitArray[hitNum];
      hitOverlay.frameSuccessUpperBound_12 = -1;
      hitOverlay.frameSuccessLowerBound_10 = -1;
      hitOverlay.numSuccessFrames_0e = 0;
      hitOverlay.frameBeginDisplay_0c = 0;
      additionHitCompletionState_8011a014[hitNum] = currentHitCompletionState;
    }
    //LAB_80107264
  }

  @Override
  @Method(0x801073d4L)
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    if(this.pauseTickerAndRenderer_31 == 0) {
      final AdditionOverlaysHit20[] hitArray = this.hitOverlays_40;
      this.currentFrame_34++;

      //LAB_80107440
      int hitNum;
      int hitFailed = 1;
      for(hitNum = 0; hitNum < this.count_30; hitNum++) {
        // Catch too late failure when no button pressed
        if(this.currentFrame_34 == hitArray[hitNum].frameSuccessUpperBound_12 + 1) {
          if(additionHitCompletionState_8011a014[hitNum] == 0) {
            additionHitCompletionState_8011a014[hitNum] = -2;
            this.propagateFailedAdditionHitFlag(hitArray, hitNum);

            //LAB_80107478
          }
        } else if(additionHitCompletionState_8011a014[hitNum] == 0) {
          hitFailed = 0;
        }
        //LAB_8010748c
      }

      //LAB_801074a8
      if(hitFailed != 0) {
        this.propagateFailedAdditionHitFlag(hitArray, hitNum);
      }

      //LAB_801074bc
      int numberBordersRendering = 0;

      //LAB_801074d0
      for(hitNum = 0; hitNum < this.count_30; hitNum++) {
        numberBordersRendering += this.tickBorderDisplay(hitArray[hitNum].borderColoursArrayIndex_02, hitNum, hitArray);
      }

      //LAB_80107500
      // If addition is complete and there are no more visible overlays to render, deallocate effect
      if(numberBordersRendering == 0 && this.additionComplete_32 != 0) {
        additionOverlayActive_80119f41 = 0;

        //LAB_8010752c
        for(hitNum = 0; hitNum < this.count_30; hitNum++) {
          hitArray[hitNum].borderArray_18 = null;
        }

        //LAB_80107554
        state.deallocateWithChildren();
      } else {
        //LAB_8010756c
        if(this.currentFrame_34 >= 9) {
          //LAB_80107598
          for(hitNum = 0; hitNum < this.count_30; hitNum++) {
            if(additionHitCompletionState_8011a014[hitNum] == 0) {
              break;
            }
          }

          //LAB_801075bc
          if(hitNum < this.count_30) {
            final AdditionOverlaysHit20 hitOverlay = hitArray[hitNum];

            if(state.storage_44[8] != 0) {
              hitOverlay.isCounter_1c = true;
              state.storage_44[8] = 0;
            }

            //LAB_801075e8
            if(this.autoCompleteType_3a < 1 || this.autoCompleteType_3a > 2) {
              //LAB_8010763c
              if(this.autoCompleteType_3a != 3) {
                final int buttonType;
                if(!hitOverlay.isCounter_1c) {
                  buttonType = 0x20;
                } else {
                  buttonType = 0x40;
                }

                //LAB_80107664
                if((press_800bee94 & 0x60) != 0) {
                  additionHitCompletionState_8011a014[hitNum] = -1;

                  if((press_800bee94 & buttonType) == 0 || (press_800bee94 & ~buttonType) != 0) {
                    //LAB_801076d8
                    //LAB_801076dc
                    additionHitCompletionState_8011a014[hitNum] = -3;
                  } else if(this.currentFrame_34 >= hitOverlay.frameSuccessLowerBound_10 && this.currentFrame_34 <= hitOverlay.frameSuccessUpperBound_12) {
                    additionHitCompletionState_8011a014[hitNum] = 1;
                    hitOverlay.hitSuccessful_01 = true;
                  }

                  //LAB_801076f0
                  if(additionHitCompletionState_8011a014[hitNum] < 0) {
                    this.propagateFailedAdditionHitFlag(hitArray, hitNum);
                  }

                  //LAB_80107718
                  //LAB_8010771c
                  this.numFramesToRenderCenterSquare_38 = 2;
                  this.lastCompletedHit_39 = hitNum;
                }
              }
            } else {  // Auto-complete
              if(this.currentFrame_34 >= hitOverlay.frameSuccessLowerBound_10 && this.currentFrame_34 <= hitOverlay.frameSuccessUpperBound_12) {
                additionHitCompletionState_8011a014[hitNum] = 1;
                hitOverlay.hitSuccessful_01 = true;

                //LAB_8010771c
                this.numFramesToRenderCenterSquare_38 = 2;
                this.lastCompletedHit_39 = hitNum;
              }
            }
          }

          //LAB_80107728
          if(this.numFramesToRenderCenterSquare_38 != 0) {
            this.numFramesToRenderCenterSquare_38--;
            if(CONFIG.getConfig(CoreMod.ADDITION_OVERLAY_CONFIG.get()) != AdditionOverlayMode.OFF) {
              this.renderAdditionCentreSolidSquare(this, this.hitOverlays_40[this.lastCompletedHit_39], additionHitCompletionState_8011a014[this.lastCompletedHit_39], manager);
            }
          }
        }
      }
    }
    //LAB_80107764
  }

  @Override
  @Method(0x8010726cL)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    if(this.pauseTickerAndRenderer_31 != 1) {
      if(manager.params_10.flags_00 >= 0) {
        final AdditionOverlaysHit20[] hitArray = this.hitOverlays_40;

        //LAB_801072c4
        int hitNum;
        for(hitNum = 0; hitNum < this.count_30; hitNum++) {
          if(CONFIG.getConfig(CoreMod.ADDITION_OVERLAY_CONFIG.get()) == AdditionOverlayMode.FULL) {
            this.renderAdditionBorders(hitNum, hitArray);
          }
        }

        //LAB_801072f4
        //LAB_8010730c
        for(hitNum = 0; hitNum < this.count_30; hitNum++) {
          if(additionHitCompletionState_8011a014[hitNum] == 0) {
            break;
          }
        }

        //LAB_80107330
        if(hitNum < this.count_30) {
          final AdditionOverlaysHit20 hitOverlay = hitArray[hitNum];
          if(CONFIG.getConfig(CoreMod.ADDITION_OVERLAY_CONFIG.get()) == AdditionOverlayMode.FULL) {
            this.renderAdditionButton(hitOverlay.frameSuccessLowerBound_10 + (hitOverlay.frameSuccessUpperBound_12 - hitOverlay.frameSuccessLowerBound_10) / 2 - this.currentFrame_34 - 1, hitOverlay.isCounter_1c);
          }

          final byte currentFrame = (byte)this.currentFrame_34;
          if(currentFrame >= hitOverlay.frameSuccessLowerBound_10 && currentFrame <= hitOverlay.frameSuccessUpperBound_12) {
            if(CONFIG.getConfig(CoreMod.ADDITION_OVERLAY_CONFIG.get()) != AdditionOverlayMode.OFF) {
              this.renderAdditionCentreSolidSquare(this, hitOverlay, -2, manager);
            }
          }
        }
      }
    }

    //LAB_801073b4
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    this.reticleBorderShadow.delete();
  }

  private static final byte[] additionButtonRenderCallbackIndices_800fb7bc = {35, 40, 33, 38};

  /** Some kind of mysterious global 2-hit addition array. Should probably be yeeted, but need to be sure. */
  private static final AdditionHitProperties10[] staticTestAdditionHitProperties_800fb7c0 = {
    new AdditionHitProperties10(0xc0, 13, 9, 2, 50, 20, 2, 0, 0, 0, 8, 5, 8, 32, 0, 11),
    new AdditionHitProperties10(0xc0, 33, 27, 1, 30, 10, 0, 0, 0, 25, 2, 1, 8, 32, 0, 0),
    new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
  };
}
