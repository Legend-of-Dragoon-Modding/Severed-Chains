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
import legend.game.combat.types.StageDeffThing08;
import legend.game.combat.ui.BattleDissolveDarkeningMetrics10;
import legend.game.types.BattleReportOverlay0e;
import legend.game.types.BattleReportOverlayList10;
import legend.game.characters.CharacterData2c;
import legend.game.types.Translucency;
import legend.lodmod.LodEncounters;
import legend.lodmod.LodMod;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.RENDERER;
import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Graphics.clearBlue_800babc0;
import static legend.game.Graphics.clearGreen_800bb104;
import static legend.game.Graphics.clearRed_8007a3a8;
import static legend.game.Graphics.displayHeight_1f8003e4;
import static legend.game.Graphics.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment.battlePreloadedEntities_1f8003f4;
import static legend.game.Scus94491BpeSegment.battleUiParts;
import static legend.game.Scus94491BpeSegment.rand;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_8004.battleReportOverlayLists_8004f658;
import static legend.game.Scus94491BpeSegment_800b.battleFlags_800bc960;
import static legend.game.Scus94491BpeSegment_800b.battleStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.modding.coremod.CoreMod.ALLOW_WIDESCREEN_CONFIG;
import static legend.game.modding.coremod.CoreMod.BATTLE_TRANSITION_MODE_CONFIG;
import static legend.game.modding.coremod.CoreMod.REDUCE_MOTION_FLASHING_CONFIG;
import static legend.game.sound.Audio.playMenuSound;

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
    playMenuSound(16); // play battle transition sound
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
    battlePreloadedEntities_1f8003f4.dragoonAdditionHits_38.clear();

    for(int charSlot = 0; charSlot < gameState_800babc8.charIds_88.size(); charSlot++) {
      final CharacterData2c character = gameState_800babc8.getCharacterBySlot(charSlot);
      battlePreloadedEntities_1f8003f4.dragoonAdditionHits_38.add(character.getDragoonAddition());
    }
  }

  public static final int[] levelUpOffsets_80010334 = {8, 8, 8, 8, 15, 8, 8, 0};

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
