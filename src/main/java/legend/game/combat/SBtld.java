package legend.game.combat;

import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.EngineState;
import legend.game.Scus94491BpeSegment_800b;
import legend.game.combat.encounters.Encounter;
import legend.game.combat.types.AdditionHitProperties10;
import legend.game.combat.types.AdditionHits80;
import legend.game.combat.types.AdditionSound;
import legend.game.combat.types.StageDeffThing08;
import legend.game.combat.ui.BattleDissolveDarkeningMetrics10;
import legend.game.inventory.WhichMenu;
import legend.game.types.BattleReportOverlay0e;
import legend.game.types.BattleReportOverlayList10;
import legend.game.types.Translucency;
import legend.game.unpacker.Loader;
import legend.lodmod.LodEncounters;
import legend.lodmod.LodMod;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Audio.playSound;
import static legend.game.Audio.stopMusicSequence;
import static legend.game.EngineStates.engineStateOnceLoaded_8004dd24;
import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Graphics.clearBlue_800babc0;
import static legend.game.Graphics.clearGreen_800bb104;
import static legend.game.Graphics.clearRed_8007a3a8;
import static legend.game.Graphics.displayHeight_1f8003e4;
import static legend.game.Graphics.vsyncMode_8007a3b8;
import static legend.game.Menus.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment.battlePreloadedEntities_1f8003f4;
import static legend.game.Scus94491BpeSegment.battleUiParts;
import static legend.game.Scus94491BpeSegment.rand;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_8004.battleReportOverlayLists_8004f658;
import static legend.game.Scus94491BpeSegment_800b.battleFlags_800bc960;
import static legend.game.Scus94491BpeSegment_800b.battleStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.postCombatMainCallbackIndex_800bc91c;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.modding.coremod.CoreMod.ALLOW_WIDESCREEN_CONFIG;
import static legend.game.modding.coremod.CoreMod.BATTLE_TRANSITION_MODE_CONFIG;
import static legend.game.modding.coremod.CoreMod.REDUCE_MOTION_FLASHING_CONFIG;

public final class SBtld {
  private SBtld() { }

  private static int _8004f6e4 = -1;

  private static int battleStartDelayTicks_8004f6ec;

  private static final BattleDissolveDarkeningMetrics10 dissolveDarkening_800bd700 = new BattleDissolveDarkeningMetrics10();
  private static int dissolveRowCount_800bd710;
  private static int dissolveIterationsPerformed_800bd714;
  private static int battleDissolveTicks;

  private static int _800bd740;

  public static void startLegacyEncounter(final int encounterId, final int stageId) {
    startEncounter(REGISTRIES.encounters.getEntry(LodMod.MOD_ID, LodEncounters.LEGACY[encounterId]).get(), stageId);
    encounterId_800bb0f8 = encounterId;
  }

  public static void startEncounter(final Encounter encounter, final int stageId) {
    encounterId_800bb0f8 = -1;
    battleStage_800bb0f4 = stageId;
    Scus94491BpeSegment_800b.encounter = encounter;
  }

  @Method(0x80018508L)
  public static void renderPostCombatScreen() {
    // There used to be code to preload SMAP while the post-combat screen is still up. I removed it because it only takes a few milliseconds to load in SC.

    //LAB_8001852c
    if(whichMenu_800bdc38 == WhichMenu.NONE_0) {
      pregameLoadingStage_800bb10c++;
    }

    //LAB_80018644
  }

  @Method(0x800189b0L)
  public static void transitionBackFromBattle() {
    if(Loader.getLoadingFileCount() == 0) {
      //LAB_800189e4
      //LAB_800189e8
      stopMusicSequence();
      pregameLoadingStage_800bb10c = 0;
      vsyncMode_8007a3b8 = 2;
      engineStateOnceLoaded_8004dd24 = postCombatMainCallbackIndex_800bc91c;
    }

    //LAB_80018a4c
  }

  @Method(0x80018e84L)
  public static void drawBattleReportOverlays() {
    final int[] sp0x30 = {0x42, 0x43, 0x02000802, 0x000a0406}; // These last two entries must be wrong but they might be unused cause I don't see anything wrong

    //LAB_80018f04
    BattleReportOverlayList10 current = battleReportOverlayLists_8004f658;
    while(current != null) {
      do {
        current.ticksRemaining_03--;

        if(current.ticksRemaining_03 != 0) {
          break;
        }

        if(current.next_08 == null) {
          //LAB_80018f48
          battleReportOverlayLists_8004f658 = current.prev_0c;
        } else {
          //LAB_80018f50
          current.next_08.prev_0c = null;
        }

        //LAB_80018f54
        final BattleReportOverlayList10 list = current.prev_0c;

        if(list != null) {
          if(current.next_08 == null) {
            list.next_08 = null;
            battleReportOverlayLists_8004f658 = current.prev_0c;
          } else {
            //LAB_80018f84
            current.prev_0c.next_08 = current.next_08;
            current.next_08.prev_0c = current.prev_0c;
          }
        }

        //LAB_80018fa0
        if(list == null) {
          return;
        }
        current = list;
      } while(true);

      //LAB_80018fd0
      //LAB_80018fd4
      for(int overlayIndex = 0; overlayIndex < 8; overlayIndex++) {
        final BattleReportOverlay0e overlay = current.overlays_04[overlayIndex];

        overlay.negaticks_08++;

        if(overlay.negaticks_08 >= 0) {
          final int v1 = overlay.clutAndTranslucency_0c >>> 8 & 0xf;

          if(v1 == 0) {
            //LAB_80019040
            overlay._09--;

            if(overlay.widthScale_04 != 0) {
              overlay.widthScale_04 -= 0x492;
              overlay.heightScale_06 -= 0x492;
            } else if(!CONFIG.getConfig(REDUCE_MOTION_FLASHING_CONFIG.get())) {
              //LAB_80019084
              overlay.clutAndTranslucency_0c &= 0x8fff;
            }

            //LAB_80019094
            if(overlay._09 == 0) {
              overlay.negaticks_08 = 0;

              if(!CONFIG.getConfig(REDUCE_MOTION_FLASHING_CONFIG.get())) {
                overlay.clutAndTranslucency_0c &= 0x7fff;
                overlay.clutAndTranslucency_0c &= 0xf0ff;
                overlay.clutAndTranslucency_0c |= ((overlay.clutAndTranslucency_0c >>> 8 & 0xf) + 1 & 0xf) << 8;
              }
            }
          } else if(v1 == 1) {
            //LAB_800190d4
            overlay.clutAndTranslucency_0c = sp0x30[overlay.clutAndTranslucency_0c >>> 15];

            if(overlay.negaticks_08 == 10) {
              overlay._09 = (byte)(simpleRand() % 10 + 2);

              if(!CONFIG.getConfig(REDUCE_MOTION_FLASHING_CONFIG.get())) {
                overlay.clutAndTranslucency_0c &= 0xf0ff;
                overlay.clutAndTranslucency_0c |= ((overlay.clutAndTranslucency_0c >>> 8 & 0xf) + 1 & 0xf) << 8;
              }
            }
            //LAB_80019028
          } else if(v1 == 2) {
            //LAB_80019164
            overlay._09--;

            if(overlay._09 == 0) {
              //LAB_80019180
              overlay._09 = 8;

              if(!CONFIG.getConfig(REDUCE_MOTION_FLASHING_CONFIG.get())) {
                overlay.clutAndTranslucency_0c &= 0xf0ff;
                overlay.clutAndTranslucency_0c |= ((overlay.clutAndTranslucency_0c >>> 8 & 0xf) + 1 & 0xf) << 8;
              }
            }
          } else if(v1 == 3) {
            //LAB_800191b8
            overlay._09--;

            if((overlay._09 & 0x1) != 0) {
              overlay.y_02++;
            }

            //LAB_800191e4
            overlay.heightScale_06 -= 0x200;

            if(overlay._09 == 0) {
              overlay.negaticks_08 = -100;
            }
          }

          battleUiParts.queueLevelUp(overlay);
        }
      }

      current = current.prev_0c;
    }

    //LAB_800192b4
  }

  @Method(0x800192d8L)
  public static void addLevelUpOverlay(int x, final int y) {
    final BattleReportOverlayList10 s0 = new BattleReportOverlayList10();
    s0.ticksRemaining_03 = 52;
    s0.prev_0c = battleReportOverlayLists_8004f658;

    if(s0.prev_0c != null) {
      s0.prev_0c.next_08 = s0;
    }

    //LAB_800193b4
    battleReportOverlayLists_8004f658 = s0;

    //LAB_800193dc
    for(int i = 0; i < 8; i++) {
      final BattleReportOverlay0e overlay = s0.overlays_04[i];
      overlay.x_00 = x - 6;
      overlay.y_02 = y - 16;
      overlay.widthScale_04 = 0x1ffe;
      overlay.heightScale_06 = 0x1ffe;
      overlay.negaticks_08 = (byte)~i;
      overlay._09 = (byte)(20 - i);
      //      overlay.u_0b = levelUpUs_8001032c[i];
      overlay.letterIndex = i;
      overlay.clutAndTranslucency_0c = 0x204a;
      x += levelUpOffsets_80010334[i];
    }
  }

  @Method(0x80019470L)
  public static void FUN_80019470() {
    battleReportOverlayLists_8004f658 = null;
  }

  @Method(0x8001b410L)
  public static void tickAndRenderTransitionIntoBattle() {
    if(_8004f6e4 == -1) {
      return;
    }

    renderCombatDissolveEffect();

    if(_800bd740 >= 0) {
      _800bd740--;
      return;
    }

    //LAB_8001b460
    if(dissolveDarkening_800bd700.active_00) {
      tickBattleDissolveDarkening();
    }

    final BattleTransitionMode mode = CONFIG.getConfig(BATTLE_TRANSITION_MODE_CONFIG.get());
    final int speedDivisor = mode == BattleTransitionMode.NORMAL ? 1 : 2;

    //LAB_8001b480
    if((battleFlags_800bc960 & 0x2) != 0) { // Combat controller script is loaded
      if(battleStartDelayTicks_8004f6ec == 0) {
        battleStartDelayTicks_8004f6ec = 1;
        setBattleDissolveDarkeningMetrics(true, 300 / vsyncMode_8007a3b8 / speedDivisor);
        startFadeEffect(1, 1);
      }
    }

    //LAB_8001b4c0
    if(battleStartDelayTicks_8004f6ec != 0) {
      //LAB_8001b4d4
      if(battleStartDelayTicks_8004f6ec >= 150 / vsyncMode_8007a3b8 / speedDivisor || mode == BattleTransitionMode.INSTANT) {
        _8004f6e4 = -1;
        battleFlags_800bc960 |= 0x1;
      }

      //LAB_8001b518
      battleStartDelayTicks_8004f6ec++;
    }

    //LAB_8001b528
    //LAB_8001b53c
  }

  /** To determine if we need to rebuild the quad because the UVs changed */
  private static float dissolveDisplayWidth;
  private static Obj dissolveSquare;
  private static final MV dissolveTransforms = new MV();

  @Method(0x8001b54cL)
  public static void renderCombatDissolveEffect() {
    if(dissolveSquare == null) {
      dissolveSquare = new QuadBuilder("Dissolve Square")
        .bpp(Bpp.BITS_24)
        .translucency(Translucency.HALF_B_PLUS_HALF_F)
        .size(8.0f, 8.0f)
        .uv(0.0f, 8.0f / 240.0f)
        .uvSize(7.0f / dissolveDisplayWidth, -8.0f / 240.0f)
        .build();

      dissolveSquare.persistent = true;
    }

    battleDissolveTicks += vsyncMode_8007a3b8;

    if(!CONFIG.getConfig(REDUCE_MOTION_FLASHING_CONFIG.get()) && ((battleDissolveTicks & 0x1) == 0 || CONFIG.getConfig(BATTLE_TRANSITION_MODE_CONFIG.get()) == BattleTransitionMode.FAST)) {
      final float squish;
      final float width;
      final float offset;

      // Make sure effect fills the whole screen
      if(RENDERER.getRenderMode() == EngineState.RenderMode.PERSPECTIVE && !CONFIG.getConfig(ALLOW_WIDESCREEN_CONFIG.get())) {
        squish = 1.0f;
        width = dissolveDisplayWidth;
        offset = 0.0f;
      } else {
        squish = dissolveDisplayWidth / 320.0f;
        width = RENDERER.getLastFrame().width / ((float)RENDERER.getLastFrame().height / RENDERER.getNativeHeight());
        offset = width - 320.0f;
      }

      final int numberOfBlocksY = displayHeight_1f8003e4 / 8;
      final int blockHeight = 100 / numberOfBlocksY;

      if(blockHeight == dissolveIterationsPerformed_800bd714) {
        dissolveIterationsPerformed_800bd714 = 0;
        dissolveRowCount_800bd710++;

        if(dissolveRowCount_800bd710 > numberOfBlocksY) {
          dissolveRowCount_800bd710 = numberOfBlocksY;
        }
      }

      //LAB_8001b608
      int offsetY = 2;

      //LAB_8001b620
      for(int row = 0; row < dissolveRowCount_800bd710; row++) {
        int offsetX = 0;
        final int v = displayHeight_1f8003e4 - dissolveRowCount_800bd710 * 8 + row * 8;

        //LAB_8001b664
        for(int col = 0; col < width * squish / 32 * 4; col++) {
          final int u = col * 8;

          //LAB_8001b6a4
          int jitterX = rand() % 4;
          if((rand() & 1) != 0) {
            jitterX = -jitterX;
          }

          //LAB_8001b6dc
          final int jitterY = rand() % 6;
          final int left = offsetX + jitterX;
          final int top = v + offsetY + jitterY;

          //LAB_8001b734
          //LAB_8001b868
          dissolveTransforms.transfer.set(left - offset / 2.0f, top, 24.0f);
          RENDERER.queueOrthoModel(dissolveSquare, dissolveTransforms, QueuedModelStandard.class)
            .uvOffset((float)u / dissolveDisplayWidth, (displayHeight_1f8003e4 - v) / (float)displayHeight_1f8003e4)
            .texture(RENDERER.getLastFrame())
            .monochrome((dissolveDarkening_800bd700.brightnessAccumulator_08 >> 8) / 128.0f);

          offsetX += 8;
        }

        //LAB_8001b8b8
        offsetY += 2;
      }

      dissolveIterationsPerformed_800bd714++;
    }

    renderBattleStartingScreenDarkening();
  }

  private static final MV darkeningTransforms = new MV();

  /** The game doesn't continue rendering when battles are loading, this basically continues rendering the last frame that was rendered, but slightly darker each time */
  @Method(0x8001bbccL)
  public static void renderBattleStartingScreenDarkening() {
    final float squish;
    final float width;
    final float offset;

    // Make sure effect fills the whole screen
    if(RENDERER.getRenderMode() == EngineState.RenderMode.PERSPECTIVE && !CONFIG.getConfig(ALLOW_WIDESCREEN_CONFIG.get())) {
      squish = 1.0f;
      width = dissolveDisplayWidth;
      offset = 0.0f;
    } else {
      squish = dissolveDisplayWidth / 320.0f;
      width = RENDERER.getLastFrame().width / ((float)RENDERER.getLastFrame().height / RENDERER.getNativeHeight());
      offset = width - 320.0f;
    }

    darkeningTransforms.transfer.set(-offset / 2.0f, 0.0f, 25.0f);
    darkeningTransforms.scaling(width * squish, displayHeight_1f8003e4, 1.0f);
    RENDERER.queueOrthoModel(RENDERER.renderBufferQuad, darkeningTransforms, QueuedModelStandard.class)
      .texture(RENDERER.getLastFrame())
      .monochrome(Math.clamp((int)(dissolveDarkening_800bd700.brightnessAccumulator_08 * 1.1f) >> 8, 0x80 - 2 * vsyncMode_8007a3b8, 0x80) / 128.0f);
  }

  @Method(0x8001c4ecL)
  public static void clearCombatVars() {
    battleStartDelayTicks_8004f6ec = 0;
    playSound(0, 16, (short)0, (short)0); // play battle transition sound
    vsyncMode_8007a3b8 = 1;
    _800bd740 = 2;
    dissolveDarkening_800bd700.active_00 = false;
    dissolveDarkening_800bd700.framesRemaining_04 = 0;
    dissolveDarkening_800bd700.brightnessAccumulator_08 = 0x8000;
    dissolveIterationsPerformed_800bd714 = 0;
    dissolveRowCount_800bd710 = 0;
    battleDissolveTicks = 0;
    clearRed_8007a3a8 = 0;
    clearGreen_800bb104 = 0;
    clearBlue_800babc0 = 0;
    _8004f6e4 = 1;
    dissolveDisplayWidth = RENDERER.getNativeWidth();

    if(dissolveSquare != null) {
      dissolveSquare.delete();
      dissolveSquare = null;
    }
  }

  @Method(0x8001c594L)
  public static void setBattleDissolveDarkeningMetrics(final boolean active, final int frames) {
    dissolveDarkening_800bd700.active_00 = active;
    dissolveDarkening_800bd700.framesRemaining_04 = frames;
    dissolveDarkening_800bd700.brightnessAccumulator_08 = 0x8000;
    dissolveDarkening_800bd700.brightnessStep_0c = 0x8000 / frames;
  }

  @Method(0x8001c5bcL)
  public static void tickBattleDissolveDarkening() {
    if(dissolveDarkening_800bd700.active_00) {
      dissolveDarkening_800bd700.framesRemaining_04--;
      dissolveDarkening_800bd700.brightnessAccumulator_08 -= dissolveDarkening_800bd700.brightnessStep_0c;

      if(dissolveDarkening_800bd700.framesRemaining_04 == 0) {
        dissolveDarkening_800bd700.active_00 = false;
      }
    }
  }

  @Method(0x80109250L)
  public static void loadAdditions() {
    //LAB_801092a0
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final int charIndex = gameState_800babc8.charIds_88[charSlot];

      if(charIndex >= 0) {
        int activeAdditionIndex = gameState_800babc8.charData_32c[charIndex].selectedAddition_19;
        if(charIndex == 5) { // Albert
          activeAdditionIndex += 28;
        }

        //LAB_801092dc
        final int activeDragoonAdditionIndex;
        if(charIndex != 0 || (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 == 0) {
          //LAB_80109308
          activeDragoonAdditionIndex = dragoonAdditionIndices_801134e8[charIndex];
        } else {
          activeDragoonAdditionIndex = dragoonAdditionIndices_801134e8[9];
        }

        //LAB_80109310
        if(activeAdditionIndex >= 0) {
          //LAB_80109320
          battlePreloadedEntities_1f8003f4.additionHits_38[charSlot] = additionHits_8010e658[activeAdditionIndex];
          battlePreloadedEntities_1f8003f4.additionHits_38[charSlot + 3] = additionHits_8010e658[activeDragoonAdditionIndex];
        }
      }

      //LAB_80109340
    }
  }

  public static final int[] levelUpOffsets_80010334 = {8, 8, 8, 8, 15, 8, 8, 0};

  public static final AdditionHits80[] additionHits_8010e658 = {
    new AdditionHits80(new AdditionHitProperties10(0xc0, 15, 9, 3, 100, 30, 0, 0, 0, 0, 8, 5, 12, 32, 0, 11, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 19, 11, 2, 50, 5, 0, 4, 0, 8, 3, 3, 10, 32, 0, 0, new AdditionSound(5, 11), new AdditionSound(3, 9)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(13, 12), new AdditionSound(1, 10)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 8), new AdditionSound(3, 6)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 28), new AdditionSound(1, 26)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 15), new AdditionSound(2, 13))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 15, 9, 3, 50, 5, 1, 0, 0, 0, 8, 5, 12, 32, 0, 11, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 33, 27, 3, 50, 5, 0, 0, 0, 25, 2, 1, 12, 32, 0, 0, new AdditionSound(5, 28), new AdditionSound(1, 26)), new AdditionHitProperties10(0xc0, 30, 15, 3, 50, 5, 0, 0, 0, 0, 0, 0, 13, 32, 0, 0, new AdditionSound(9, 15), new AdditionSound(2, 13)), new AdditionHitProperties10(0xc0, 23, 5, 2, 50, 5, 0, 4, -5, 0, 12, 5, 12, 32, 0, 0, new AdditionSound(10, 5), new AdditionSound(3, 3)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(7, 23), new AdditionSound(1, 21)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 14), new AdditionSound(3, 12)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 14), new AdditionSound(2, 12))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 15, 9, 3, 50, 10, 2, 0, 0, 0, 8, 5, 12, 32, 0, 11, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 27, 12, 3, 50, 10, 0, 0, 0, 0, 27, 8, 10, 32, 0, 0, new AdditionSound(13, 12), new AdditionSound(1, 10)), new AdditionHitProperties10(0xc0, 22, 8, 2, 50, 10, 0, 4, -8, 5, 17, 8, 8, 32, 0, 0, new AdditionSound(9, 8), new AdditionSound(3, 6)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 28), new AdditionSound(1, 26)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 15), new AdditionSound(2, 13)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(10, 5), new AdditionSound(3, 3)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 15, 9, 3, 30, 10, 3, 0, 0, 0, 8, 5, 12, 32, 0, 11, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 28, 22, 3, 30, 10, 0, 0, 0, 0, 12, 6, 11, 32, 0, 0, new AdditionSound(7, 23), new AdditionSound(1, 21)), new AdditionHitProperties10(0xc0, 26, 14, 3, 30, 10, 0, 0, 0, 0, 0, 0, 12, 32, 0, 0, new AdditionSound(9, 14), new AdditionSound(3, 12)), new AdditionHitProperties10(0xc0, 21, 14, 3, 30, 10, 0, 0, 0, 11, 4, 0, 15, 32, 0, 0, new AdditionSound(5, 14), new AdditionSound(2, 12)), new AdditionHitProperties10(0xc0, 24, 8, 2, 30, 10, 0, 4, -5, 5, 15, 10, 11, 32, 0, 0, new AdditionSound(10, 8), new AdditionSound(3, 6)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 10), new AdditionSound(2, 8)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(7, 3), new AdditionSound(3, 1))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 15, 9, 3, 20, 10, 4, 0, 0, 0, 8, 5, 12, 32, 0, 11, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 15, 9, 3, 20, 10, 0, 0, 0, 0, 0, 0, 11, 32, 0, 0, new AdditionSound(5, 10), new AdditionSound(2, 8)), new AdditionHitProperties10(0xc0, 8, 2, 3, 20, 10, 0, 0, 0, 0, 0, 0, 10, 32, 0, 0, new AdditionSound(7, 3), new AdditionSound(3, 1)), new AdditionHitProperties10(0xc0, 8, 2, 3, 20, 10, 0, 0, 0, 0, 0, 0, 7, 32, 0, 0, new AdditionSound(8, 3), new AdditionSound(3, 1)), new AdditionHitProperties10(0xc0, 8, 2, 3, 10, 10, 0, 0, 0, 0, 0, 0, 12, 32, 0, 0, new AdditionSound(5, 3), new AdditionSound(3, 1)), new AdditionHitProperties10(0xc0, 10, 2, 2, 10, 10, 0, 4, 0, 0, 0, 0, 11, 32, 0, 0, new AdditionSound(9, 2), new AdditionSound(3, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(7, 20), new AdditionSound(2, 18))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 15, 9, 3, 30, 8, 5, 0, 0, 0, 8, 5, 12, 32, 0, 11, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 25, 19, 3, 30, 2, 0, 0, 0, 16, 3, 7, 9, 32, 0, 0, new AdditionSound(7, 20), new AdditionSound(2, 18)), new AdditionHitProperties10(0xc0, 14, 8, 3, 30, 2, 0, 0, 0, 0, 0, 0, 10, 32, 0, 0, new AdditionSound(8, 9), new AdditionSound(1, 7)), new AdditionHitProperties10(0xc0, 15, 10, 2, 30, 2, 0, 0, 0, 0, 0, 0, 9, 32, 0, 0, new AdditionSound(7, 11), new AdditionSound(3, 9)), new AdditionHitProperties10(0xc0, 12, 6, 3, 30, 2, 0, 0, 0, 4, 1, 1, 14, 32, 0, 0, new AdditionSound(4, 7), new AdditionSound(2, 5)), new AdditionHitProperties10(0xc0, 12, 6, 3, 30, 2, 0, 0, 0, 0, 0, 0, 14, 32, 0, 0, new AdditionSound(5, 7), new AdditionSound(2, 5)), new AdditionHitProperties10(0xc0, 59, 12, 2, 20, 2, 0, 4, -5, 0, 11, 3, 13, 32, 0, 0, new AdditionSound(10, 12), new AdditionSound(3, 10), new AdditionSound(12, 40)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7))),
    new AdditionHits80(new AdditionHitProperties10(0xe0, 14, 9, 3, 40, 20, 6, 0, 0, 0, 8, 5, 12, 32, 0, 11, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 23, 17, 3, 30, 20, 0, 0, 0, 0, 0, 0, 9, 32, 0, 0, new AdditionSound(8, 18), new AdditionSound(1, 16)), new AdditionHitProperties10(0xc0, 10, 5, 2, 30, 10, 0, 0, 0, 3, 2, 1, 7, 32, 0, 0, new AdditionSound(5, 6), new AdditionSound(2, 4)), new AdditionHitProperties10(0xc0, 12, 6, 3, 30, 10, 0, 0, 0, 0, 0, 0, 9, 32, 0, 0, new AdditionSound(7, 7), new AdditionSound(1, 5)), new AdditionHitProperties10(0xc0, 20, 14, 3, 30, 10, 0, 0, 0, 0, 13, 4, 13, 32, 0, 0, new AdditionSound(5, 15), new AdditionSound(3, 13)), new AdditionHitProperties10(0xc0, 12, 7, 2, 30, 10, 0, 0, 0, 0, 0, 0, 11, 32, 18, 0, new AdditionSound(9, 8), new AdditionSound(1, 6)), new AdditionHitProperties10(0xc0, 29, 9, 3, 30, 10, 0, 0, 3, 7, 11, 5, 10, 32, 0, 0, new AdditionSound(11, 9), new AdditionSound(2, 7)), new AdditionHitProperties10(0xc0, 23, 17, 2, 30, 10, 0, 4, -8, 0, 16, 3, 13, 32, 0, 0, new AdditionSound(10, 17), new AdditionSound(3, 15))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 5, 0, 0, 100, 0, 7, 0, 0, 0, 4, 1, 8, 32, 0, 11, new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0xc0, 15, 4, 0, 10, 0, 0, 0, 0, 0, 14, 0, 7, 32, 0, 0, new AdditionSound(8, 13), new AdditionSound(3, 11)), new AdditionHitProperties10(0xc0, 16, 10, 0, 20, 0, 0, 0, 0, 6, 8, 1, 10, 32, 0, 0, new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0xc0, 11, 5, 0, 30, 0, 0, 0, 0, 4, 6, 0, 9, 32, 0, 0, new AdditionSound(9, 16), new AdditionSound(1, 14)), new AdditionHitProperties10(0xc0, 19, 5, 0, 40, 0, 0, 0, 0, 0, 18, 0, 9, 32, 0, 0, new AdditionSound(10, 20), new AdditionSound(3, 18), new AdditionSound(7, 4), new AdditionSound(7, 10), new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0xc0, 68, 39, 0, 0, 0, 0, 0, 0, 0, 67, 0, 11, 32, 0, 0, new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 14), new AdditionSound(1, 12)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 7), new AdditionSound(2, 5))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 17, 11, 3, 75, 30, 8, 0, 0, 2, 5, 7, 16, 32, 0, 11, new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0xc0, 25, 13, 2, 25, 5, 0, 4, -8, 0, 8, 12, 14, 32, 0, 0, new AdditionSound(8, 13), new AdditionSound(3, 11)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 16), new AdditionSound(1, 14)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(10, 20), new AdditionSound(3, 18), new AdditionSound(7, 4), new AdditionSound(7, 10), new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 14), new AdditionSound(1, 12)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 7), new AdditionSound(2, 5))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 17, 11, 3, 50, 15, 9, 0, 2, 2, 5, 7, 16, 32, 0, 11, new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0xc0, 22, 16, 3, 25, 10, 0, 0, 0, 0, 17, 5, 16, 32, 0, 0, new AdditionSound(9, 16), new AdditionSound(1, 14)), new AdditionHitProperties10(0xc0, 25, 20, 2, 25, 10, 0, 4, -4, 10, 8, 5, 17, 32, 0, 0, new AdditionSound(10, 20), new AdditionSound(3, 18), new AdditionSound(7, 4), new AdditionSound(7, 10), new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 14), new AdditionSound(1, 12)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 7), new AdditionSound(2, 5)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 11), new AdditionSound(2, 9)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(11, 13), new AdditionSound(3, 11))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 17, 11, 3, 30, 6, 10, 0, 2, 2, 5, 7, 16, 32, 0, 11, new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0xc0, 19, 13, 3, 30, 6, 0, 0, -3, 11, 7, 3, 15, 32, 0, 0, new AdditionSound(8, 14), new AdditionSound(1, 12)), new AdditionHitProperties10(0xc0, 13, 7, 3, 30, 6, 0, 0, -2, 0, 9, 11, 12, 32, 0, 0, new AdditionSound(8, 7), new AdditionSound(2, 5)), new AdditionHitProperties10(0xc0, 16, 10, 3, 30, 6, 0, 0, -2, 0, 12, 11, 14, 32, 0, 0, new AdditionSound(9, 11), new AdditionSound(2, 9)), new AdditionHitProperties10(0xc0, 17, 12, 2, 30, 6, 0, 4, -9, 0, 17, 13, 18, 32, 0, 0, new AdditionSound(11, 13), new AdditionSound(3, 11)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 14), new AdditionSound(1, 12)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(12, 15), new AdditionSound(3, 13))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 17, 11, 3, 30, 5, 11, 0, 0, 2, 5, 7, 16, 32, 0, 11, new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0xc0, 29, 14, 3, 30, 5, 0, 0, 0, 0, 29, 6, 15, 32, 0, 0, new AdditionSound(8, 14), new AdditionSound(1, 12)), new AdditionHitProperties10(0xc0, 22, 15, 3, 30, 5, 0, 0, 0, 4, 11, 23, 9, 32, 0, 0, new AdditionSound(12, 15), new AdditionSound(3, 13)), new AdditionHitProperties10(0xc0, 10, 5, 2, 30, 5, 0, 0, 0, 4, 1, 1, 18, 32, 0, 0, new AdditionSound(8, 6), new AdditionSound(0, 4)), new AdditionHitProperties10(0xc0, 22, 16, 3, 30, 5, 0, 0, 0, 11, 5, 3, 22, 32, 0, 0, new AdditionSound(9, 17), new AdditionSound(1, 15), new AdditionSound(7, 7), new AdditionSound(7, 12), new AdditionSound(10, 8), new AdditionSound(2, 6)), new AdditionHitProperties10(0xc0, 13, 7, 3, 30, 5, 0, 0, -4, 3, 4, 7, 18, 32, 0, 0, new AdditionSound(10, 8), new AdditionSound(2, 6)), new AdditionHitProperties10(0xc0, 40, 19, 2, 20, 5, 0, 4, -8, 0, 40, 6, 24, 32, 0, 0, new AdditionSound(11, 19), new AdditionSound(3, 17), new AdditionSound(7, 12), new AdditionSound(7, 15), new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 11), new AdditionSound(0, 9))),
    new AdditionHits80(new AdditionHitProperties10(0xe0, 17, 11, 3, 30, 8, 12, 0, 0, 2, 5, 7, 16, 32, 0, 11, new AdditionSound(5, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0xc0, 19, 13, 3, 30, 8, 0, 0, 0, 0, 0, 0, 18, 32, 18, 0, new AdditionSound(10, 13), new AdditionSound(1, 11)), new AdditionHitProperties10(0xc0, 27, 20, 2, 30, 8, 0, 0, 0, 14, 12, 12, 12, 32, 0, 0, new AdditionSound(13, 20), new AdditionSound(2, 19)), new AdditionHitProperties10(0xc0, 16, 10, 3, 40, 8, 0, 0, 0, 4, 5, 1, 22, 32, 0, 0, new AdditionSound(8, 11), new AdditionSound(0, 9)), new AdditionHitProperties10(0xc0, 22, 16, 3, 40, 8, 0, 0, 0, 10, 5, 3, 18, 32, 0, 0, new AdditionSound(9, 17), new AdditionSound(2, 15)), new AdditionHitProperties10(0xc0, 17, 12, 2, 40, 8, 0, 0, 0, 0, 14, 6, 22, 32, 0, 0, new AdditionSound(5, 13), new AdditionSound(2, 11)), new AdditionHitProperties10(0xc0, 20, 14, 3, 40, 6, 0, 0, 0, 11, 7, 14, 20, 32, 19, 0, new AdditionSound(10, 15), new AdditionSound(1, 13)), new AdditionHitProperties10(0xc0, 26, 7, 2, 50, 6, 0, 4, 0, 0, 26, 10, 10, 32, 0, 0, new AdditionSound(11, 7), new AdditionSound(3, 5))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 2, 0, 0, 100, 0, 13, 0, 0, 0, 1, 7, 10, 32, 18, 11, new AdditionSound(5, 5), new AdditionSound(0, 3)), new AdditionHitProperties10(0xc0, 14, 5, 0, 10, 0, 0, 0, 0, 0, 0, 0, 7, 32, 0, 0, new AdditionSound(4, 11), new AdditionSound(2, 9)), new AdditionHitProperties10(0xc0, 32, 25, 0, 20, 0, 0, 0, 0, 4, 21, 2, 7, 32, 0, 0, new AdditionSound(5, 5), new AdditionSound(0, 3)), new AdditionHitProperties10(0xc0, 10, 1, 0, 30, 0, 0, 0, 0, 1, 8, 29, 7, 32, 0, 0, new AdditionSound(7, 9), new AdditionSound(1, 7)), new AdditionHitProperties10(0xc0, 11, 10, 0, 40, 0, 0, 0, 0, 0, 10, 29, 5, 32, 19, 0, new AdditionSound(10, 8), new AdditionSound(3, 6)), new AdditionHitProperties10(0xc0, 61, 29, 0, 0, 0, 0, 0, 0, 0, 60, 5, 7, 32, 0, 0, new AdditionSound(5, 5), new AdditionSound(0, 3)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 12), new AdditionSound(1, 10)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 16), new AdditionSound(2, 14))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 11, 5, 3, 75, 30, 14, 0, 3, 2, 2, 9, 12, 32, 0, 11, new AdditionSound(5, 5), new AdditionSound(0, 3)), new AdditionHitProperties10(0xc0, 16, 11, 2, 25, 5, 0, 4, 0, 0, 0, 0, 8, 32, 0, 0, new AdditionSound(4, 11), new AdditionSound(2, 9)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 5), new AdditionSound(0, 3)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(7, 9), new AdditionSound(1, 7)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(10, 8), new AdditionSound(3, 6)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 5), new AdditionSound(0, 3)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 12), new AdditionSound(1, 10)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 16), new AdditionSound(2, 14))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 11, 5, 3, 50, 10, 15, 0, 3, 2, 2, 5, 12, 32, 0, 11, new AdditionSound(5, 5), new AdditionSound(0, 3)), new AdditionHitProperties10(0xc0, 15, 9, 3, 50, 10, 0, 0, -4, 8, 2, 7, 8, 32, 0, 0, new AdditionSound(7, 9), new AdditionSound(1, 7)), new AdditionHitProperties10(0xc0, 33, 8, 2, 50, 10, 0, 4, 0, 6, 2, 11, 6, 32, 0, 0, new AdditionSound(10, 8), new AdditionSound(3, 6)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 5), new AdditionSound(0, 3)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 12), new AdditionSound(1, 10)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 16), new AdditionSound(2, 14)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 6), new AdditionSound(1, 4)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 15), new AdditionSound(3, 13))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 11, 5, 3, 20, 10, 16, 0, 0, 2, 2, 9, 12, 32, 0, 11, new AdditionSound(5, 5), new AdditionSound(0, 3)), new AdditionHitProperties10(0xc0, 17, 11, 3, 20, 5, 0, 0, 0, 10, 2, 8, 8, 32, 0, 0, new AdditionSound(4, 12), new AdditionSound(1, 10)), new AdditionHitProperties10(0xc0, 22, 16, 2, 20, 5, 0, 0, 0, 0, 2, 0, 12, 32, 18, 0, new AdditionSound(8, 16), new AdditionSound(2, 14)), new AdditionHitProperties10(0xc0, 11, 5, 3, 20, 5, 0, 0, 0, 0, 2, 0, 7, 32, 0, 0, new AdditionSound(5, 6), new AdditionSound(1, 4)), new AdditionHitProperties10(0xc0, 22, 15, 3, 10, 5, 0, 0, 0, 13, 2, 13, 12, 32, 0, 0, new AdditionSound(9, 15), new AdditionSound(3, 13)), new AdditionHitProperties10(0xc0, 17, 12, 2, 10, 5, 0, 4, -8, 10, 3, 8, 12, 32, 0, 0, new AdditionSound(10, 12), new AdditionSound(2, 10)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 5), new AdditionSound(0, 3)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 7), new AdditionSound(1, 5))),
    new AdditionHits80(new AdditionHitProperties10(0xe0, 11, 5, 3, 30, 20, 17, 0, 0, 2, 2, 9, 12, 32, 0, 11, new AdditionSound(5, 5), new AdditionSound(0, 3)), new AdditionHitProperties10(0xc0, 12, 6, 3, 30, 20, 0, 0, 0, 0, 0, 0, 10, 32, 0, 0, new AdditionSound(9, 7), new AdditionSound(1, 5)), new AdditionHitProperties10(0xc0, 14, 9, 2, 30, 10, 0, 0, 0, 6, 3, 8, 10, 32, 0, 0, new AdditionSound(4, 9), new AdditionSound(3, 7)), new AdditionHitProperties10(0xc0, 19, 13, 3, 30, 10, 0, 0, 0, 16, 2, 8, 10, 32, 0, 0, new AdditionSound(8, 14), new AdditionSound(1, 12)), new AdditionHitProperties10(0xc0, 23, 14, 3, 20, 10, 0, 0, 3, 12, 3, 10, 10, 32, 0, 0, new AdditionSound(7, 14), new AdditionSound(0, 12)), new AdditionHitProperties10(0xc0, 30, 10, 2, 20, 10, 0, 0, -4, 10, 11, 3, 7, 32, 0, 0, new AdditionSound(8, 10), new AdditionSound(2, 8)), new AdditionHitProperties10(0xc0, 20, 11, 3, 20, 10, 0, 0, 5, 8, 4, 12, 9, 32, 0, 0, new AdditionSound(9, 11), new AdditionSound(1, 9)), new AdditionHitProperties10(0xc0, 26, 4, 2, 20, 10, 0, 4, 12, 0, 0, 0, 7, 32, 0, 0, new AdditionSound(10, 4), new AdditionSound(2, 2))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 0, 0, 0, 100, 0, 18, 0, 0, 0, 0, 0, 12, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 12, 0, 0, 0, new AdditionSound(10, 3), new AdditionSound(1, 1)), new AdditionHitProperties10(0xc0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 12, 0, 0, 0, new AdditionSound(10, 3), new AdditionSound(0, 1)), new AdditionHitProperties10(0xc0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 9, 32, 0, 0, new AdditionSound(11, 42), new AdditionSound(3, 40)), new AdditionHitProperties10(0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 10), new AdditionSound(1, 8)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 11), new AdditionSound(2, 9))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 8, 2, 3, 75, 30, 19, 0, 10, 0, 0, 0, 16, 32, 0, 11, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 30, 21, 2, 25, 5, 0, 4, -10, 0, 0, 0, 2, 32, 0, 0, new AdditionSound(10, 21), new AdditionSound(3, 19)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 46), new AdditionSound(1, 44), new AdditionSound(7, 26), new AdditionSound(7, 39), new AdditionSound(10, 3), new AdditionSound(2, 1)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(10, 3), new AdditionSound(2, 1)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(12, 13), new AdditionSound(3, 11)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 14), new AdditionSound(1, 12))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 8, 2, 3, 40, 5, 20, 0, 15, 0, 0, 0, 20, 32, 0, 11, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 56, 46, 3, 20, 5, 0, 0, 15, 0, 0, 0, 15, 32, 0, 0, new AdditionSound(9, 46), new AdditionSound(1, 44), new AdditionSound(7, 26), new AdditionSound(7, 39), new AdditionSound(10, 3), new AdditionSound(2, 1)), new AdditionHitProperties10(0xc0, 9, 3, 3, 20, 5, 0, 0, -8, 0, 1, 12, 18, 32, 0, 0, new AdditionSound(10, 3), new AdditionSound(2, 1)), new AdditionHitProperties10(0xc0, 42, 13, 2, 20, 5, 0, 4, -5, 0, 11, 12, 18, 32, 0, 0, new AdditionSound(12, 13), new AdditionSound(3, 11)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 14), new AdditionSound(1, 12)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(11, 5), new AdditionSound(1, 3)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 8), new AdditionSound(0, 6))),
    new AdditionHits80(new AdditionHitProperties10(0xe0, 8, 2, 3, 50, 20, 21, 0, 10, 0, 0, 0, 20, 32, 0, 11, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 19, 13, 3, 30, 20, 0, 0, 0, 11, 2, 7, 15, 32, 0, 0, new AdditionSound(4, 14), new AdditionSound(1, 12)), new AdditionHitProperties10(0xc0, 10, 5, 2, 30, 20, 0, 0, -3, 3, 2, 3, 15, 32, 0, 0, new AdditionSound(11, 5), new AdditionSound(1, 3)), new AdditionHitProperties10(0xc0, 16, 8, 3, 30, 20, 0, 0, -3, 5, 1, 3, 15, 32, 19, 0, new AdditionSound(9, 8), new AdditionSound(0, 6)), new AdditionHitProperties10(0xc0, 10, 3, 3, 30, 10, 0, 0, -8, 0, 2, 7, 13, 32, 0, 0, new AdditionSound(13, 3), new AdditionSound(2, 1)), new AdditionHitProperties10(0xc0, 21, 12, 2, 30, 10, 0, 4, -13, 0, 11, 20, 15, 32, 0, 0, new AdditionSound(14, 12), new AdditionSound(3, 10)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 0, 0, 0, 100, 0, 22, 0, 0, 0, 0, 0, 7, 0, 0, 0, new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0, new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0), new AdditionHitProperties10(0xc0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0), new AdditionHitProperties10(0xc0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), new AdditionHitProperties10(0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 7, 1, 3, 75, 10, 23, 0, 0, 0, 0, 0, 9, 32, 18, 13, new AdditionSound(4, 1), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 21, 6, 2, 25, 10, 0, 4, -6, 0, 6, 5, 10, 32, 0, 0, new AdditionSound(5, 6), new AdditionSound(3, 4)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 1), new AdditionSound(0, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 8), new AdditionSound(1, 6)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(15, 3), new AdditionSound(0, 1)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(14, 16), new AdditionSound(3, 14), new AdditionSound(7, 26)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 1), new AdditionSound(0, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 8), new AdditionSound(1, 6))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 7, 1, 3, 50, 20, 24, 0, 0, 0, 0, 0, 9, 32, 18, 13, new AdditionSound(4, 1), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 26, 8, 3, 50, 5, 0, 0, 0, 6, 12, 8, 10, 32, 0, 0, new AdditionSound(8, 8), new AdditionSound(1, 6)), new AdditionHitProperties10(0xc0, 11, 3, 3, 25, 5, 0, 0, 0, 0, 3, 3, 9, 32, 0, 0, new AdditionSound(15, 3), new AdditionSound(0, 1)), new AdditionHitProperties10(0xc0, 71, 16, 3, 25, 5, 0, 4, -6, 0, 6, 10, 6, 32, 0, 0, new AdditionSound(14, 16), new AdditionSound(3, 14), new AdditionSound(7, 26)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, -8, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 1), new AdditionSound(0, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 8), new AdditionSound(1, 6)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 17), new AdditionSound(2, 15), new AdditionSound(7, 30), new AdditionSound(7, 33), new AdditionSound(10, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(10, 2), new AdditionSound(0, 0))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 7, 1, 3, 20, 12, 25, 0, 0, 0, 0, 0, 9, 32, 18, 13, new AdditionSound(4, 1), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 19, 8, 3, 20, 12, 0, 0, -7, 0, 9, 6, 9, 32, 0, 0, new AdditionSound(8, 8), new AdditionSound(1, 6)), new AdditionHitProperties10(0xc0, 34, 17, 3, 20, 12, 0, 0, 3, 6, 11, 20, 10, 32, 0, 0, new AdditionSound(9, 17), new AdditionSound(2, 15), new AdditionSound(7, 30), new AdditionSound(7, 33), new AdditionSound(10, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 16, 2, 3, 20, 12, 0, 0, 0, 0, 0, 0, 15, 32, 0, 0, new AdditionSound(10, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 23, 11, 2, 20, 12, 0, 4, -2, 9, 4, 6, 9, 32, 0, 0, new AdditionSound(11, 11), new AdditionSound(3, 9)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 1), new AdditionSound(0, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(10, 7), new AdditionSound(1, 5)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 9), new AdditionSound(0, 7))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 7, 1, 3, 30, 3, 26, 0, 0, 0, 0, 0, 9, 32, 18, 13, new AdditionSound(4, 1), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 12, 6, 3, 20, 3, 0, 0, 0, 5, 3, 1, 12, 32, 0, 0, new AdditionSound(10, 7), new AdditionSound(1, 5)), new AdditionHitProperties10(0xc0, 16, 9, 3, 20, 3, 0, 0, 0, 0, 11, 6, 15, 32, 0, 0, new AdditionSound(5, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 16, 8, 2, 20, 3, 0, 0, 0, 0, 12, 14, 14, 32, 0, 0, new AdditionSound(13, 8), new AdditionSound(2, 6)), new AdditionHitProperties10(0xc0, 18, 1, 3, 20, 3, 0, 0, 10, 0, 2, 3, 12, 32, 0, 0, new AdditionSound(14, 1), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 27, 18, 3, 20, 3, 0, 0, -5, 0, 22, 5, 15, 32, 19, 0, new AdditionSound(10, 18), new AdditionSound(2, 17)), new AdditionHitProperties10(0xc0, 29, 1, 2, 20, 2, 0, 4, -9, 0, 3, 4, 15, 32, 0, 0, new AdditionSound(11, 1), new AdditionSound(3, 0), new AdditionSound(7, 12), new AdditionSound(7, 16), new AdditionSound(4, 1), new AdditionSound(0, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 1), new AdditionSound(0, 0))),
    new AdditionHits80(new AdditionHitProperties10(0xe0, 7, 1, 3, 30, 20, 27, 0, 0, 0, 0, 0, 9, 32, 18, 13, new AdditionSound(4, 1), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 24, 16, 3, 30, 20, 0, 0, 0, 6, 14, 10, 14, 32, 0, 0, new AdditionSound(5, 16), new AdditionSound(1, 14)), new AdditionHitProperties10(0xc0, 21, 1, 2, 30, 10, 0, 0, 0, 0, 4, 7, 14, 32, 19, 0, new AdditionSound(9, 1), new AdditionSound(1, 0)), new AdditionHitProperties10(0xc0, 19, 5, 3, 30, 10, 0, 0, -12, 0, 6, 15, 12, 32, 0, 0, new AdditionSound(15, 5), new AdditionSound(3, 3)), new AdditionHitProperties10(0xc0, 8, 1, 3, 20, 10, 0, 0, 15, 0, 6, 10, 22, 32, 0, 0, new AdditionSound(8, 1), new AdditionSound(1, 0)), new AdditionHitProperties10(0xc0, 14, 4, 2, 20, 10, 0, 0, 0, 6, 3, 4, 16, 32, 0, 0, new AdditionSound(9, 4), new AdditionSound(2, 2)), new AdditionHitProperties10(0xc0, 19, 7, 3, 20, 10, 0, 0, -3, 0, 6, 1, 6, 32, 0, 0, new AdditionSound(10, 7), new AdditionSound(0, 5)), new AdditionHitProperties10(0xc0, 10, 1, 2, 20, 10, 0, 4, -2, 0, 2, 4, 18, 32, 0, 0, new AdditionSound(11, 1), new AdditionSound(3, 0))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 0, 0, 0, 100, 0, 28, 0, 0, 0, 0, 0, 6, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, new AdditionSound(10, 21), new AdditionSound(3, 19)), new AdditionHitProperties10(0xc0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, new AdditionSound(9, 46), new AdditionSound(1, 44), new AdditionSound(7, 26), new AdditionSound(7, 39), new AdditionSound(10, 3), new AdditionSound(2, 1)), new AdditionHitProperties10(0xc0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, new AdditionSound(10, 3), new AdditionSound(2, 1)), new AdditionHitProperties10(0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, new AdditionSound(12, 13), new AdditionSound(3, 11)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 14), new AdditionSound(1, 12))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 15, 9, 3, 75, 30, 29, 0, 0, 0, 0, 0, 8, 32, 0, 11, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 23, 3, 2, 25, 5, 0, 4, 0, 0, 0, 0, 8, 32, 0, 0, new AdditionSound(10, 3), new AdditionSound(1, 1)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(10, 3), new AdditionSound(0, 1)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(11, 42), new AdditionSound(3, 40)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 10), new AdditionSound(1, 8)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 11), new AdditionSound(2, 9))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 15, 9, 3, 100, 10, 30, 0, 0, 0, 0, 0, 8, 32, 0, 11, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 19, 3, 3, 25, 5, 0, 0, -8, 0, 0, 0, 10, 32, 0, 0, new AdditionSound(10, 3), new AdditionSound(0, 1)), new AdditionHitProperties10(0xc0, 51, 42, 2, 25, 5, 0, 4, -8, 3, 3, 0, 13, 32, 0, 0, new AdditionSound(11, 42), new AdditionSound(3, 40)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 10), new AdditionSound(1, 8)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 11), new AdditionSound(2, 9)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(11, 10), new AdditionSound(3, 8)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 15, 9, 3, 25, 20, 31, 0, 0, 0, 0, 0, 8, 32, 0, 11, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 15, 9, 3, 25, 10, 0, 0, -3, 0, 0, 0, 5, 32, 19, 0, new AdditionSound(9, 10), new AdditionSound(1, 8)), new AdditionHitProperties10(0xc0, 19, 11, 3, 25, 10, 0, 0, 0, 0, 9, 8, 5, 32, 0, 0, new AdditionSound(5, 11), new AdditionSound(2, 9)), new AdditionHitProperties10(0xc0, 24, 10, 2, 25, 10, 0, 4, 5, 0, 0, 0, 8, 32, 0, 0, new AdditionSound(11, 10), new AdditionSound(3, 8)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 10), new AdditionSound(1, 8)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 6), new AdditionSound(1, 4)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 6), new AdditionSound(2, 4))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 15, 9, 3, 30, 7, 32, 0, 0, 0, 0, 0, 8, 32, 0, 11, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 15, 9, 3, 30, 7, 0, 0, 0, 6, 3, 5, 4, 32, 0, 0, new AdditionSound(4, 10), new AdditionSound(1, 8)), new AdditionHitProperties10(0xc0, 12, 6, 2, 30, 7, 0, 0, -3, 4, 2, 3, 5, 32, 0, 0, new AdditionSound(8, 6), new AdditionSound(1, 4)), new AdditionHitProperties10(0xc0, 10, 4, 3, 30, 7, 0, 0, 0, 0, 10, 1, 6, 32, 19, 0, new AdditionSound(9, 6), new AdditionSound(2, 4)), new AdditionHitProperties10(0xc0, 33, 7, 2, 30, 7, 0, 4, -5, 0, 0, 0, 10, 32, 0, 0, new AdditionSound(11, 7), new AdditionSound(3, 5)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 8), new AdditionSound(1, 6)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 4), new AdditionSound(2, 2))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 15, 9, 3, 30, 3, 33, 0, 0, 0, 0, 0, 8, 32, 0, 11, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 13, 7, 3, 30, 2, 0, 0, 0, 4, 2, 5, 8, 32, 0, 0, new AdditionSound(5, 8), new AdditionSound(1, 6)), new AdditionHitProperties10(0xc0, 13, 4, 2, 30, 2, 0, 0, 0, 3, 1, 3, 8, 0, 0, 0, new AdditionSound(9, 4), new AdditionSound(2, 2)), new AdditionHitProperties10(0xc0, 11, 5, 3, 30, 2, 0, 0, 0, 3, 2, 5, 6, 32, 0, 0, new AdditionSound(8, 6), new AdditionSound(0, 4)), new AdditionHitProperties10(0xc0, 12, 5, 3, 30, 2, 0, 0, 0, 2, 2, 3, 5, 32, 0, 0, new AdditionSound(5, 5), new AdditionSound(1, 3)), new AdditionHitProperties10(0xc0, 13, 8, 2, 30, 2, 0, 0, 0, 0, 9, 5, 10, 32, 0, 0, new AdditionSound(11, 9), new AdditionSound(2, 7)), new AdditionHitProperties10(0xc0, 23, 10, 2, 20, 2, 0, 4, -10, 9, 1, 8, 11, 32, 0, 0, new AdditionSound(10, 10), new AdditionSound(3, 8)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 8), new AdditionSound(0, 6))),
    new AdditionHits80(new AdditionHitProperties10(0xe0, 15, 9, 3, 30, 8, 34, 0, 0, 0, 0, 0, 8, 32, 0, 11, new AdditionSound(4, 8), new AdditionSound(0, 6)), new AdditionHitProperties10(0xc0, 18, 8, 2, 30, 6, 0, 0, 0, 5, 3, 4, 5, 0, 0, 0, new AdditionSound(9, 8), new AdditionSound(1, 6)), new AdditionHitProperties10(0xc0, 12, 3, 3, 30, 6, 0, 0, 0, 0, 2, 1, 10, 0, 0, 0, new AdditionSound(8, 2), new AdditionSound(2, 0)), new AdditionHitProperties10(0xc0, 11, 6, 2, 40, 6, 0, 0, 0, 5, 2, 3, 10, 32, 0, 0, new AdditionSound(5, 7), new AdditionSound(0, 5)), new AdditionHitProperties10(0xc0, 16, 6, 3, 40, 6, 0, 0, 0, 0, 0, 0, 8, 32, 0, 0, new AdditionSound(9, 5), new AdditionSound(2, 3)), new AdditionHitProperties10(0xc0, 8, 3, 2, 40, 6, 0, 0, -3, 0, 3, 4, 6, 32, 0, 0, new AdditionSound(8, 3), new AdditionSound(1, 1)), new AdditionHitProperties10(0xc0, 19, 11, 3, 40, 6, 0, 0, 0, 4, 6, 7, 12, 32, 0, 0, new AdditionSound(11, 10), new AdditionSound(2, 8)), new AdditionHitProperties10(0xc0, 37, 3, 2, 50, 6, 0, 4, -7, 0, 2, 15, 10, 32, 0, 0, new AdditionSound(10, 3), new AdditionSound(3, 1))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 0, 0, 0, 100, 0, 35, 0, 0, 0, 0, 0, 6, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, new AdditionSound(8, 15), new AdditionSound(3, 13)), new AdditionHitProperties10(0xc0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, new AdditionSound(9, 19), new AdditionSound(1, 17), new AdditionSound(7, 13), new AdditionSound(7, 15), new AdditionSound(10, 4), new AdditionSound(3, 2)), new AdditionHitProperties10(0xc0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0, new AdditionSound(10, 4), new AdditionSound(3, 2)), new AdditionHitProperties10(0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 54, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 22), new AdditionSound(1, 20)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 4), new AdditionSound(2, 2))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 8, 2, 3, 75, 30, 36, 0, 5, 0, 1, 0, 19, 32, 0, 11, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 44, 15, 2, 25, 5, 0, 4, 0, 3, 10, 3, 10, 32, 0, 0, new AdditionSound(8, 15), new AdditionSound(3, 13)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 19), new AdditionSound(1, 17), new AdditionSound(7, 13), new AdditionSound(7, 15), new AdditionSound(10, 4), new AdditionSound(3, 2)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(10, 4), new AdditionSound(3, 2)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 22), new AdditionSound(1, 20)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 4), new AdditionSound(2, 2))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 8, 2, 3, 50, 15, 37, 0, 5, 0, 1, 0, 19, 32, 0, 11, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 24, 19, 3, 25, 10, 0, 0, 4, 0, 4, 0, 8, 32, 0, 0, new AdditionSound(9, 19), new AdditionSound(1, 17), new AdditionSound(7, 13), new AdditionSound(7, 15), new AdditionSound(10, 4), new AdditionSound(3, 2)), new AdditionHitProperties10(0xc0, 20, 4, 2, 25, 10, 0, 4, 0, 0, 0, 0, 6, 32, 0, 0, new AdditionSound(10, 4), new AdditionSound(3, 2)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 22), new AdditionSound(1, 20)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 4), new AdditionSound(2, 2)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 3), new AdditionSound(2, 1)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(11, 3), new AdditionSound(3, 1))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 8, 2, 3, 30, 6, 38, 0, 0, 0, 1, 3, 19, 32, 0, 11, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 31, 22, 3, 30, 6, 0, 0, 5, 0, 20, 0, 16, 32, 0, 0, new AdditionSound(8, 22), new AdditionSound(1, 20)), new AdditionHitProperties10(0xc0, 11, 4, 3, 30, 6, 0, 0, 0, 0, 0, 0, 16, 32, 0, 0, new AdditionSound(8, 4), new AdditionSound(2, 2)), new AdditionHitProperties10(0xc0, 8, 2, 3, 30, 6, 0, 0, 0, 0, 1, 3, 19, 32, 0, 0, new AdditionSound(9, 3), new AdditionSound(2, 1)), new AdditionHitProperties10(0xc0, 36, 3, 2, 30, 6, 0, 4, 0, 0, 0, 0, 18, 32, 0, 0, new AdditionSound(11, 3), new AdditionSound(3, 1)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(8, 19), new AdditionSound(2, 17)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(12, 15), new AdditionSound(3, 13), new AdditionSound(7, 3))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 8, 2, 3, 30, 5, 39, 0, 5, 0, 1, 0, 19, 32, 0, 11, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 33, 19, 3, 30, 5, 0, 0, -3, 17, 2, 4, 11, 32, 0, 0, new AdditionSound(8, 19), new AdditionSound(2, 17)), new AdditionHitProperties10(0xc0, 32, 15, 3, 30, 5, 0, 0, 3, 10, 3, 15, 17, 32, 0, 0, new AdditionSound(12, 15), new AdditionSound(3, 13), new AdditionSound(7, 3)), new AdditionHitProperties10(0xc0, 6, 1, 2, 30, 5, 0, 0, 0, 0, 1, 0, 16, 32, 0, 0, new AdditionSound(8, 1), new AdditionSound(2, 0)), new AdditionHitProperties10(0xc0, 11, 5, 3, 30, 5, 0, 0, 0, 0, 0, 0, 16, 32, 0, 0, new AdditionSound(9, 6), new AdditionSound(0, 4), new AdditionSound(7, 9), new AdditionSound(7, 14), new AdditionSound(10, 7), new AdditionSound(1, 5)), new AdditionHitProperties10(0xc0, 12, 6, 3, 30, 5, 0, 0, 0, 4, 2, 3, 15, 32, 0, 0, new AdditionSound(10, 7), new AdditionSound(1, 5)), new AdditionHitProperties10(0xc0, 34, 12, 2, 20, 5, 0, 4, 0, 4, 9, 13, 16, 32, 0, 0, new AdditionSound(11, 12), new AdditionSound(3, 10), new AdditionSound(7, 9)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 2), new AdditionSound(0, 0))),
    new AdditionHits80(new AdditionHitProperties10(0xe0, 8, 2, 3, 30, 8, 40, 0, 0, 0, 1, 0, 19, 32, 0, 11, new AdditionSound(5, 2), new AdditionSound(0, 0)), new AdditionHitProperties10(0xc0, 26, 12, 3, 30, 8, 0, 0, -12, 10, 6, 8, 10, 32, 0, 0, new AdditionSound(10, 12), new AdditionSound(3, 10)), new AdditionHitProperties10(0xc0, 17, 3, 2, 30, 8, 0, 0, 12, 0, 3, 22, 13, 32, 0, 0, new AdditionSound(13, 3), new AdditionSound(2, 1), new AdditionSound(7, 0)), new AdditionHitProperties10(0xc0, 9, 3, 3, 40, 8, 0, 0, 0, 0, 3, 3, 10, 32, 0, 0, new AdditionSound(8, 4), new AdditionSound(0, 2)), new AdditionHitProperties10(0xc0, 16, 10, 3, 40, 8, 0, 0, 6, 0, 9, 0, 0, 32, 0, 0, new AdditionSound(9, 11), new AdditionSound(1, 9)), new AdditionHitProperties10(0xc0, 10, 2, 2, 40, 8, 0, 0, 0, 0, 3, 0, 0, 32, 0, 0, new AdditionSound(5, 2), new AdditionSound(2, 0)), new AdditionHitProperties10(0xc0, 21, 14, 3, 40, 6, 0, 0, 0, 19, 2, 0, 0, 32, 19, 0, new AdditionSound(10, 14), new AdditionSound(0, 12)), new AdditionHitProperties10(0xc0, 16, 7, 2, 50, 6, 0, 4, -5, 0, 8, 16, 18, 32, 0, 0, new AdditionSound(11, 7), new AdditionSound(3, 5), new AdditionSound(7, 0))),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 0, 0, 0, 100, 0, 41, 0, 0, 0, 0, 0, 12, 0, 18, 0), new AdditionHitProperties10(0xc0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0), new AdditionHitProperties10(0xc0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0), new AdditionHitProperties10(0xc0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0), new AdditionHitProperties10(0xc0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0), new AdditionHitProperties10(0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)),
    new AdditionHits80(new AdditionHitProperties10(0xc0, 0, 0, 0, 200, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 11), new AdditionSound(3, 9)), new AdditionHitProperties10(0xc0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0xc0, 0, 0, 0, 60, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(13, 12), new AdditionSound(1, 10)), new AdditionHitProperties10(0xc0, 0, 0, 0, 80, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 8), new AdditionSound(3, 6)), new AdditionHitProperties10(0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(4, 9), new AdditionSound(0, 7)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(5, 28), new AdditionSound(1, 26)), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new AdditionSound(9, 15), new AdditionSound(2, 13))),
  };

  public static final int[] dragoonAdditionIndices_801134e8 = {7, 13, -1, 18, 35, 41, 28, 22, -1, 42};

  public static final StageDeffThing08[] _8011517c = {
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 2, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(2, 4, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 1, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(4, 2, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(2, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(1, 1, 1),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(4, 0, 0),
    new StageDeffThing08(0, 32768, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(1, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 32768, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 4, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
    new StageDeffThing08(0, 0, 0),
  };
}
