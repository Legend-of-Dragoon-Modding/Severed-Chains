package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.memory.types.IntRef;
import legend.core.opengl.MeshObj;
import legend.core.opengl.QuadBuilder;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.combat.types.EnemyDrop;
import legend.game.inventory.WhichMenu;
import legend.game.types.Renderable58;
import legend.game.types.Translucency;

import static legend.core.GameEngine.RENDERER;
import static legend.game.SItem.additions_8011a064;
import static legend.game.SItem.cacheCharacterSlots;
import static legend.game.SItem.dragoonXpRequirements_800fbbf0;
import static legend.game.SItem.getXpToNextLevel;
import static legend.game.SItem.hasDragoon;
import static legend.game.SItem.loadAdditions;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.menuAssetsLoaded;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderItemIcon;
import static legend.game.SItem.renderText;
import static legend.game.Scus94491BpeSegment.FUN_80019470;
import static legend.game.Scus94491BpeSegment.addLevelUpOverlay;
import static legend.game.Scus94491BpeSegment.drawBattleReportOverlays;
import static legend.game.Scus94491BpeSegment.loadDrgnFileSync;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.getJoypadInputByPriority;
import static legend.game.Scus94491BpeSegment_8002.getUnlockedDragoonSpells;
import static legend.game.Scus94491BpeSegment_8002.giveItems;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderables;
import static legend.game.Scus94491BpeSegment_8004.additionCounts_8004f5c0;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_800b.fullScreenEffect_800bb140;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.goldGainedFromCombat_800bc920;
import static legend.game.Scus94491BpeSegment_800b.inventoryJoypadInput_800bdc44;
import static legend.game.Scus94491BpeSegment_800b.itemsDroppedByEnemies_800bc928;
import static legend.game.Scus94491BpeSegment_800b.livingCharCount_800bc97c;
import static legend.game.Scus94491BpeSegment_800b.livingCharIds_800bc968;
import static legend.game.Scus94491BpeSegment_800b.press_800bee94;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdc5c;
import static legend.game.Scus94491BpeSegment_800b.secondaryCharIds_800bdbf8;
import static legend.game.Scus94491BpeSegment_800b.spGained_800bc950;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.Scus94491BpeSegment_800b.totalXpFromCombat_800bc95c;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.unlockedUltimateAddition_800bc910;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.combat.Battle.spellStats_800fa0b8;

public class PostBattleScreen extends MenuScreen {
  private static final String NEW_ADDITION = "New Addition";
  private static final String SPELL_UNLOCKED = "Spell Unlocked";

  private static final int[] characterPortraitVs_800fbc88 = {0x1f0, 0x1f2, 0x1f1, 0x1f3, 0x1f4, 0x1f5, 0x1f6, 0x1f7, 0x1f8};
  private static final int[] charPortraitGlyphs_800fbc9c = {0xd, 0xf, 0xe, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15};
  private static final int[] _800fbca8 = {0x26, 0x28, 0x27, 0x29, 0x2a, 0x2b, 0x2c, 0x2d, 0x2e};

  private int levelUpCharId_8011e170;
  private int unlockHeight_8011e178;
  private int soundTick_8011e17c;
  private final int[] pendingXp_8011e180 = new int[10];
  private final int[] spellsUnlocked_8011e1a8 = new int[10];
  private final int[] additionsUnlocked_8011e1b8 = new int[10];
  private final int[] levelsGained_8011e1c8 = new int[10];
  private final int[] dragoonLevelsGained_8011e1d8 = new int[10];

  private MenuState inventoryMenuState_800bdc28 = MenuState.INIT_0;
  private MenuState confirmDest_800bdc30;

  private final MeshObj[] resultsBackgroundObj = new MeshObj[6];
  private final MV resultsBackgroundTransforms = new MV();

  @Method(0x8010d614L)
  @Override
  protected void render() {
    inventoryJoypadInput_800bdc44 = getJoypadInputByPriority();

    switch(this.inventoryMenuState_800bdc28) {
      case INIT_0:
        if(uiFile_800bdc3c != null) {
          uiFile_800bdc3c.delete();
        }

        renderablePtr_800bdc5c = null;
        uiFile_800bdc3c = null;
        resizeDisplay(320, 240);
        loadDrgnFileSync(0, 6665, data -> menuAssetsLoaded(data, 0));
        loadDrgnFileSync(0, 6666, data -> menuAssetsLoaded(data, 1));
        textZ_800bdf00 = 33;
        this.inventoryMenuState_800bdc28 = MenuState.WAIT_FOR_UI_FILE_TO_LOAD_1;
        this.initResultBackgrounds();
        break;

      case WAIT_FOR_UI_FILE_TO_LOAD_1:
        if(uiFile_800bdc3c != null) {
          startFadeEffect(2, 10);
          this.inventoryMenuState_800bdc28 = MenuState.WAIT_FOR_FADE_IN_AND_INIT_CONTROLS_2;
        }
        break;

      case WAIT_FOR_FADE_IN_AND_INIT_CONTROLS_2:
        if(fullScreenEffect_800bb140.currentColour_28 == 0) {
          deallocateRenderables(0xff);
          Renderable58 glyph = this.drawGlyph(0, 0, 165, 21, 720, 497);
          glyph.widthScale = 0;
          glyph.heightScale_38 = 0;
          glyph = this.drawGlyph(2, 2, 13, 21, 720, 497);
          glyph.widthScale = 0;
          glyph.heightScale_38 = 0;
          glyph = this.drawGlyph(1, 1, 13, 149, 720, 497);
          glyph.widthScale = 0;
          glyph.heightScale_38 = 0;

          this.drawGlyph(0x3e, 0x3e, 24, 28, 736, 497);
          this.drawGlyph(0x3d, 0x3d, 24, 40, 736, 497);
          this.drawGlyph(0x40, 0x40, 24, 52, 736, 497);

          cacheCharacterSlots();

          //LAB_8010d87c
          for(int i = 0; i < 10; i++) {
            this.spellsUnlocked_8011e1a8[i] = 0;
            this.additionsUnlocked_8011e1b8[i] = 0;
            this.levelsGained_8011e1c8[i] = 0;
            this.dragoonLevelsGained_8011e1d8[i] = 0;
            this.pendingXp_8011e180[i] = 0;
          }

          this.additionsUnlocked_8011e1b8[0] = this.getUltimateAdditionIdIfUnlocked(0);
          this.additionsUnlocked_8011e1b8[1] = this.getUltimateAdditionIdIfUnlocked(1);
          this.additionsUnlocked_8011e1b8[2] = this.getUltimateAdditionIdIfUnlocked(2);

          int xpDivisor = 0;
          for(int charSlot = 0; charSlot < 3; charSlot++) {
            if(this.characterIsAlive(charSlot)) {
              xpDivisor++;
            }
          }

          for(int charSlot = 0; charSlot < 3; charSlot++) {
            if(this.characterIsAlive(charSlot)) {
              this.pendingXp_8011e180[gameState_800babc8.charIds_88[charSlot]] = totalXpFromCombat_800bc95c / xpDivisor;
            }
          }

          //LAB_8010d9d4
          //LAB_8010d9f8
          for(int secondaryCharSlot = 0; secondaryCharSlot < 6; secondaryCharSlot++) {
            final int secondaryCharIndex = secondaryCharIds_800bdbf8[secondaryCharSlot];

            if(secondaryCharIndex != -1) {
              this.pendingXp_8011e180[secondaryCharIndex] = MathHelper.safeDiv(totalXpFromCombat_800bc95c, xpDivisor) / 2;
            }

            //LAB_8010da24
          }

          this.inventoryMenuState_800bdc28 = MenuState.WAIT_FOR_FIRST_BUTTON_PRESS_3;
          this.drawGlyph(0x3f, 0x3f, 144,  28, 736, 497);
          this.drawGlyph(0x3f, 0x3f, 144, 156, 736, 497);
          this.drawReport();
        }

        break;

      case WAIT_FOR_FIRST_BUTTON_PRESS_3:
        if((press_800bee94 & 0x20) != 0) {
          //LAB_8010da84
          if(goldGainedFromCombat_800bc920 == 0) {
            this.inventoryMenuState_800bdc28 = MenuState.TICK_XP_5;
          } else {
            this.inventoryMenuState_800bdc28 = MenuState.TICK_GOLD_4;
          }
        }

        this.drawReport();
        break;

      case TICK_GOLD_4:
        final int goldTick;
        if((press_800bee94 & 0x20) != 0) {
          goldTick = goldGainedFromCombat_800bc920;
        } else {
          //LAB_8010dab4
          goldTick = 10;
        }

        //LAB_8010dabc
        final int goldGained = goldGainedFromCombat_800bc920;

        if(goldTick >= goldGained) {
          this.soundTick_8011e17c = 0;
          goldGainedFromCombat_800bc920 = 0;
          this.inventoryMenuState_800bdc28 = MenuState.TICK_XP_5;
          gameState_800babc8.gold_94 += goldGained;
        } else {
          //LAB_8010db00
          goldGainedFromCombat_800bc920 -= goldTick;
          gameState_800babc8.gold_94 += goldTick;
        }

        //LAB_8010db18
        if(gameState_800babc8.gold_94 > 99999999) {
          gameState_800babc8.gold_94 = 99999999;
        }

        //LAB_8010db3c
        //LAB_8010db40
        this.soundTick_8011e17c++;

        if((this.soundTick_8011e17c & 0x1) != 0) {
          playMenuSound(1);
        }

        this.drawReport();
        break;

      case TICK_XP_5:
        final boolean moreXpToGive = this.givePendingXp();

        if(moreXpToGive) {
          this.soundTick_8011e17c++;

          if((this.soundTick_8011e17c & 0x1) != 0) {
            playMenuSound(1);
          }
        } else {
          this.levelUpCharId_8011e170 = 3;
          totalXpFromCombat_800bc95c = 0;

          if(this.additionsUnlocked_8011e1b8[0] + this.additionsUnlocked_8011e1b8[1] + this.additionsUnlocked_8011e1b8[2] == 0) {
            //LAB_8010dc9c
            this.inventoryMenuState_800bdc28 = MenuState.MAIN_LEVEL_UPS_8;
          } else if((press_800bee94 & 0x20) != 0) {
            playMenuSound(2);
            this.unlockHeight_8011e178 = 0;
            this.inventoryMenuState_800bdc28 = MenuState.EMBIGGEN_UNLOCKED_ADDITIONS_6;
          }
        }

        this.drawReport();
        break;

      case EMBIGGEN_UNLOCKED_ADDITIONS_6:
        if(this.unlockHeight_8011e178 < 20) {
          this.unlockHeight_8011e178 += 2;
        } else {
          //LAB_8010dcc8
          if((press_800bee94 & 0x20) != 0) {
            playMenuSound(2);

            //LAB_8010dcf0
            this.inventoryMenuState_800bdc28 = MenuState.ENSMALLEN_UNLOCKED_ADDITIONS_7;
          }
        }

        //LAB_8010dcf4
        //LAB_8010dcf8
        this.renderAdditionsUnlocked(this.unlockHeight_8011e178);
        this.drawReport();
        break;

      case ENSMALLEN_UNLOCKED_ADDITIONS_7:
        if(this.unlockHeight_8011e178 > 0) {
          this.unlockHeight_8011e178 -= 2;
        } else {
          //LAB_8010dd28
          this.inventoryMenuState_800bdc28 = MenuState.MAIN_LEVEL_UPS_8;
        }

        this.renderAdditionsUnlocked(this.unlockHeight_8011e178);
        this.drawReport();
        break;

      case MAIN_LEVEL_UPS_8:
        if(this.levelUpCharId_8011e170 >= 9) {
          //LAB_8010dd90
          this.inventoryMenuState_800bdc28 = MenuState.DRAGOON_LEVEL_UPS_10;
        } else if(this.levelsGained_8011e1c8[this.levelUpCharId_8011e170] != 0) {
          addLevelUpOverlay(-80, 44);
          playMenuSound(9);
          this.inventoryMenuState_800bdc28 = MenuState.SECONDARY_LEVEL_UPS_9;
        } else {
          //LAB_8010dd88
          this.levelUpCharId_8011e170++;
        }

        this.drawReport();
        break;

      case SECONDARY_LEVEL_UPS_9:
        this.drawChar(24, 152, secondaryCharIds_800bdbf8[this.levelUpCharId_8011e170 - 3]);

        if((press_800bee94 & 0x60) != 0) {
          playMenuSound(2);
          this.levelsGained_8011e1c8[this.levelUpCharId_8011e170] = 0;
          this.inventoryMenuState_800bdc28 = MenuState.MAIN_LEVEL_UPS_8;
          this.levelUpCharId_8011e170++;
        }

        this.drawReport();
        break;

      case DRAGOON_LEVEL_UPS_10:
        for(int charSlot = 0; charSlot < 3; charSlot++) {
          if(this.characterIsAlive(charSlot)) {
            this.levelUpDragoon(gameState_800babc8.charIds_88[charSlot], charSlot);
          }
        }

        //LAB_8010de6c
        if(this.spellsUnlocked_8011e1a8[0] != 0 || this.spellsUnlocked_8011e1a8[1] != 0 || this.spellsUnlocked_8011e1a8[2] != 0) {
          this.inventoryMenuState_800bdc28 = MenuState.WAIT_FOR_DRAGOON_LEVEL_UP_INPUT_11;
        } else {
          //LAB_8010de98
          this.inventoryMenuState_800bdc28 = MenuState.WAIT_FOR_INPUT_14;
        }

        this.drawReport();
        break;

      case WAIT_FOR_DRAGOON_LEVEL_UP_INPUT_11:
        if((press_800bee94 & 0x20) != 0) {
          this.unlockHeight_8011e178 = 0;
          playMenuSound(2);

          //LAB_8010decc
          this.inventoryMenuState_800bdc28 = MenuState.EMBIGGEN_SPELL_UNLOCK_12;
        }

        this.drawReport();
        break;

      case EMBIGGEN_SPELL_UNLOCK_12:
        if(this.unlockHeight_8011e178 < 20) {
          this.unlockHeight_8011e178 += 2;
        } else {
          //LAB_8010def4
          if((press_800bee94 & 0x20) != 0) {
            playMenuSound(2);

            //LAB_8010df1c
            this.inventoryMenuState_800bdc28 = MenuState.ENSMALLEN_SPELL_UNLOCK_13;
          }
        }

        //LAB_8010df20
        //LAB_8010df24
        this.renderSpellsUnlocked(this.unlockHeight_8011e178);
        this.drawReport();
        break;

      case ENSMALLEN_SPELL_UNLOCK_13:
        if(this.unlockHeight_8011e178 > 0) {
          this.unlockHeight_8011e178 -= 2;
        } else {
          //LAB_8010df54
          //LAB_8010df1c
          this.inventoryMenuState_800bdc28 = MenuState.WAIT_FOR_INPUT_14;
        }

        //LAB_8010df20
        //LAB_8010df24
        this.renderSpellsUnlocked(this.unlockHeight_8011e178);
        this.drawReport();
        break;

      case WAIT_FOR_INPUT_14:
        if((press_800bee94 & 0x60) != 0) {
          playMenuSound(3);

          if(itemsDroppedByEnemies_800bc928.isEmpty() || giveItems(itemsDroppedByEnemies_800bc928) == 0) {
            //LAB_8010dfac
            // No items remaining
            this.fadeToMenuState(MenuState.UNLOAD_18);
          } else {
            // Some items remaining
            resizeDisplay(384, 240);
            deallocateRenderables(0xff);
            menuStack.popScreen();
            menuStack.pushScreen(new TooManyItemsScreen());
          }
        }

        //LAB_8010dfb8
        //LAB_8010dfbc
        this.drawReport();
        break;

      case FADE_OUT_16:
        startFadeEffect(1, 10);
        this.inventoryMenuState_800bdc28 = MenuState.WAIT_FOR_FADE_OUT_17;

      case WAIT_FOR_FADE_OUT_17:
        this.drawReport();

        if(fullScreenEffect_800bb140.currentColour_28 >= 0xff) {
          this.inventoryMenuState_800bdc28 = this.confirmDest_800bdc30;
          FUN_80019470();
        }

        break;

      case UNLOAD_18:
        startFadeEffect(2, 10);
        deallocateRenderables(0xff);

        if(uiFile_800bdc3c != null) {
          uiFile_800bdc3c.delete();
        }

        uiFile_800bdc3c = null;
        whichMenu_800bdc38 = WhichMenu.UNLOAD_POST_COMBAT_REPORT_30;
        textZ_800bdf00 = 13;

        this.deleteResultsScreenObjects();

        menuStack.popScreen();
        break;
    }

    //LAB_8010e09c
    //LAB_8010e0a0
    this.drawResultsBackground(166,  22, 136, 192, 1);
    this.drawResultsBackground( 14,  22, 144, 120, 1);
    this.drawResultsBackground( 14, 150, 144,  64, 1);
  }

  /**
   * @return True if there is remaining XP to give
   */
  @Method(0x8010cc24L)
  private boolean givePendingXp() {
    final int[] charIds = {gameState_800babc8.charIds_88[0], gameState_800babc8.charIds_88[1], gameState_800babc8.charIds_88[2], secondaryCharIds_800bdbf8[0], secondaryCharIds_800bdbf8[1], secondaryCharIds_800bdbf8[2], secondaryCharIds_800bdbf8[3], secondaryCharIds_800bdbf8[4], secondaryCharIds_800bdbf8[5]};
    int pendingXpCleared = 0;

    for(int i = 0; i < 9; i++) {
      if(charIds[i] >= 0) {
        final int pendingXp = this.pendingXp_8011e180[i];

        if(pendingXp == 0) {
          //LAB_8010cc68
          pendingXpCleared++;
          continue;
        }

        //LAB_8010cc70
        final int cappedPendingXp;
        if((press_800bee94 & 0x20) != 0 || pendingXp < 10) {
          cappedPendingXp = pendingXp;
        } else {
          cappedPendingXp = 10;
        }

        //LAB_8010cc94
        //LAB_8010cc98
        int xp = gameState_800babc8.charData_32c[i].xp_00;
        if(xp <= 999999) {
          xp = xp + cappedPendingXp;
        } else {
          xp = 999999;
        }

        //LAB_8010ccd4
        gameState_800babc8.charData_32c[i].xp_00 = xp;
        this.pendingXp_8011e180[i] -= cappedPendingXp;

        //LAB_8010cd30
        while(gameState_800babc8.charData_32c[i].xp_00 >= getXpToNextLevel(i) && gameState_800babc8.charData_32c[i].level_12 < 60) {
          gameState_800babc8.charData_32c[i].level_12++;

          this.levelsGained_8011e1c8[i]++;
          if(this.additionsUnlocked_8011e1b8[i] == 0) {
            this.additionsUnlocked_8011e1b8[i] = loadAdditions(i, null);
          }

          //LAB_8010cd9c
        }
      } else {
        pendingXpCleared++;
      }
    }

    //LAB_8010cdb0
    //LAB_8010cdcc
    return pendingXpCleared < 9;
  }

  @Method(0x8010cde8L)
  private void levelUpDragoon(final int charIndex, final int charSlot) {
    if(charIndex != -1) {
      gameState_800babc8.charData_32c[charIndex].dlevelXp_0e += spGained_800bc950[charSlot];

      if(gameState_800babc8.charData_32c[charIndex].dlevelXp_0e > 32000) {
        gameState_800babc8.charData_32c[charIndex].dlevelXp_0e = 32000;
      }

      //LAB_8010ceb0
      //LAB_8010cecc
      while(gameState_800babc8.charData_32c[charIndex].dlevelXp_0e >= dragoonXpRequirements_800fbbf0[charIndex][gameState_800babc8.charData_32c[charIndex].dlevel_13 + 1] && gameState_800babc8.charData_32c[charIndex].dlevel_13 < 5) {
        loadCharacterStats();
        final int[] spellIndices = new int[8];
        final int spellCount = getUnlockedDragoonSpells(spellIndices, charIndex);

        gameState_800babc8.charData_32c[charIndex].dlevel_13++;
        this.dragoonLevelsGained_8011e1d8[charSlot]++;

        loadCharacterStats();
        if(spellCount != getUnlockedDragoonSpells(spellIndices, charIndex)) {
          this.spellsUnlocked_8011e1a8[charSlot] = spellIndices[spellCount] + 1;
        }

        //LAB_8010cf70
      }
    }

    //LAB_8010cf84
  }

  @Method(0x8010cfa0L)
  private Renderable58 drawGlyph(final int startGlyph, final int endGlyph, final int x, final int y, final int u, final int v) {
    final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c._d2d8(), null);
    renderable.glyph_04 = startGlyph;
    renderable.startGlyph_10 = startGlyph;

    if(startGlyph != endGlyph) {
      renderable.endGlyph_14 = endGlyph;
    } else {
      renderable.endGlyph_14 = endGlyph;
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
    }

    //LAB_8010d004
    renderable.x_40 = x;
    renderable.y_44 = y;
    renderable.clut_30 = v << 6 | (u & 0x3f0) >> 4;
    renderable.tpage_2c = 0x1b;
    return renderable;
  }

  @Method(0x8010d050L)
  private void fadeToMenuState(final MenuState nextMenuState) {
    this.inventoryMenuState_800bdc28 = MenuState.FADE_OUT_16;
    this.confirmDest_800bdc30 = nextMenuState;
  }

  private void initResultBackgrounds() {
    this.resultsBackgroundObj[0] = new QuadBuilder("Results Screen Background")
      .size(1.0f, 1.0f)
      .translucency(Translucency.HALF_B_PLUS_HALF_F)
      .monochrome(0, 128.0f / 255.0f)
      .rgb(1, 0.0f, 20.0f / 255.0f, 80.0f / 255.0f)
      .rgb(2, 0.0f, 20.0f / 255.0f, 80.0f / 255.0f)
      .monochrome(3, 0.0f)
      .build();

    this.resultsBackgroundObj[1] = new QuadBuilder("Results Screen Portrait Shadow")
      .size(1.0f, 1.0f)
      .monochrome(0, 127.0f / 255.0f)
      .monochrome(2, 127.0f / 255.0f)
      .monochrome(1, 0.0f)
      .monochrome(3, 0.0f)
      .build();

    this.resultsBackgroundObj[2] = new QuadBuilder("Results Screen Addition Background")
      .size(1.0f, 1.0f)
      .rgb(0, 1.0f, 122.0f / 255.0f, 0.0f)
      .rgb(2, 1.0f, 122.0f / 255.0f, 0.0f)
      .rgb(1, 73.0f / 255.0f, 35.0f / 255.0f, 0.0f)
      .rgb(3, 73.0f / 255.0f, 35.0f / 255.0f, 0.0f)
      .build();

    this.resultsBackgroundObj[3] = new QuadBuilder("Results Screen Addition Border")
      .size(1.0f, 1.0f)
      .rgb(0, 1.0f, 122.0f / 255.0f, 0.0f)
      .rgb(1, 1.0f, 122.0f / 255.0f, 0.0f)
      .rgb(2, 1.0f, 122.0f / 255.0f, 0.0f)
      .rgb(3, 1.0f, 122.0f / 255.0f, 0.0f)
      .build();

    this.resultsBackgroundObj[4] = new QuadBuilder("Results Screen Spell Background")
      .size(1.0f, 1.0f)
      .rgb(0, 0.0f, 132.0f / 255.0f, 254.0f / 255.0f)
      .rgb(2, 0.0f, 132.0f / 255.0f, 254.0f / 255.0f)
      .rgb(1, 0.0f, 38.0f / 255.0f, 72.0f / 255.0f)
      .rgb(3, 0.0f, 38.0f / 255.0f, 72.0f / 255.0f)
      .build();

    this.resultsBackgroundObj[5] = new QuadBuilder("Results Screen Spell Border")
      .size(1.0f, 1.0f)
      .monochrome(0, 127.0f / 255.0f)
      .monochrome(2, 127.0f / 255.0f)
      .monochrome(1, 0.0f)
      .monochrome(3, 0.0f)
      .build();
  }

  @Method(0x8010d078L)
  private void drawResultsBackground(int x, int y, final int w, final int h, final int type) {
    if(this.resultsBackgroundObj[type - 1] != null) {
      x -= 8;
      //y += 120;

      final int z;
      switch(type) {
        case 1 -> { //Background gradient
          z = 37;
        }

        case 2 -> { //Character portrait shadow
          z = 36;
        }

        case 3 -> { //Addition background
          z = 34;
        }

        case 4 -> { //Addition border
          z = 35;
        }

        case 5 -> { //Spell background
          z = 34;
        }

        case 6 -> { //Spell border
          z = 35;
        }

        default -> z = 0;
      }

      //LAB_8010d2c4
      this.resultsBackgroundTransforms.transfer.set(x, y, z * 4);
      this.resultsBackgroundTransforms.scaling(w, h, 1);

      RENDERER.queueOrthoModel(this.resultsBackgroundObj[type - 1], this.resultsBackgroundTransforms);

      //LAB_8010d318
    }
  }

  @Method(0x8010e114L)
  private Renderable58 drawCharPortrait(final int x, final int y, final int charId) {
    if(charId >= 9) {
      //LAB_8010e1ec
      throw new IllegalArgumentException("Invalid character index");
    }

    final int glyph = charPortraitGlyphs_800fbc9c[charId];
    final Renderable58 renderable = this.drawGlyph(glyph, glyph, x, y, 704, characterPortraitVs_800fbc88[charId]);
    renderable.z_3c = 35;

    //LAB_8010e1f0
    return renderable;
  }

  @Method(0x8010e200L)
  private void drawDigit(final int x, final int y, int val, final IntRef a3) {
    val %= 10;
    if(val != 0 || a3.get() != 0) {
      //LAB_8010e254
      final Renderable58 renderable = this.drawGlyph(val + 3, val + 3, x, y, 736, 497);
      renderable.flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
      a3.set(1);
    }

    //LAB_8010e290
  }

  @Method(0x8010e2a0L)
  private void drawTwoDigitNumber(final int x, final int y, final int dlevel) {
    final int s2 = Math.min(99, dlevel);
    final IntRef sp0x10 = new IntRef();
    this.drawDigit(x, y, s2 / 10, sp0x10.set(0));
    this.drawDigit(x + 6, y, s2, sp0x10.incr());
  }

  @Method(0x8010e340L)
  private void drawSixDigitNumber(final int x, final int y, final int val) {
    final int s2 = Math.min(999_999, val);
    final IntRef sp0x10 = new IntRef();
    this.drawDigit(x, y, s2 / 100_000, sp0x10);
    this.drawDigit(x +  6, y, s2 / 10_000, sp0x10);
    this.drawDigit(x + 12, y, s2 /  1_000, sp0x10);
    this.drawDigit(x + 18, y, s2 /    100, sp0x10);
    this.drawDigit(x + 24, y, s2 /     10, sp0x10);
    this.drawDigit(x + 30, y, s2, sp0x10.incr());
  }

  @Method(0x8010e490L)
  private void drawEightDigitNumber(final int x, final int y, final int val) {
    final int s2 = Math.min(99_999_999, val);
    final IntRef sp0x10 = new IntRef();
    this.drawDigit(x, y, s2 / 10_000_000, sp0x10);
    this.drawDigit(x +  6, y, s2 / 1_000_000, sp0x10);
    this.drawDigit(x + 12, y, s2 /   100_000, sp0x10);
    this.drawDigit(x + 18, y, s2 /    10_000, sp0x10);
    this.drawDigit(x + 24, y, s2 /     1_000, sp0x10);
    this.drawDigit(x + 30, y, s2 /       100, sp0x10);
    this.drawDigit(x + 36, y, s2 /        10, sp0x10);
    this.drawDigit(x + 42, y, s2, sp0x10.incr());
  }

  @Method(0x8010e630L)
  private void drawNextLevelXp(final int x, final int y, final int val) {
    if(val != 0) {
      this.drawSixDigitNumber(x, y, val);
    } else {
      //LAB_8010e660
      final Renderable58 renderable = this.drawGlyph(0x47, 0x47, x + 30, y, 736, 497);
      renderable.flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
    }

    //LAB_8010e698
  }

  @Method(0x8010e6a8L)
  private int getXpWidth(final int xp) {
    if(xp > 99999) {
      return 36;
    }

    //LAB_8010e6c4
    if(xp > 9999) {
      return 30;
    }

    //LAB_8010e6d4
    if(xp > 999) {
      return 24;
    }

    //LAB_8010e6e4
    if(xp > 99) {
      //LAB_8010e6fc
      return 18;
    }

    if(xp > 9) {
      //LAB_8010e700
      return 12;
    }

    return 6;
  }

  @Method(0x8010e708L)
  private void drawChar(final int x, final int y, final int charId) {
    if(charId != -1) {
      this.drawResultsBackground(x + 1, y + 5, 24, 32, 2);
      this.drawCharPortrait(x - 1, y + 4, charId).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
      this.drawGlyph(_800fbca8[charId], _800fbca8[charId], x + 32, y + 4, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
      this.drawGlyph(0x3b, 0x3b, x + 30, y + 16, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
      this.drawGlyph(0x3c, 0x3c, x + 30, y + 28, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
      this.drawGlyph(0x3d, 0x3d, x, y + 40, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;

      final Renderable58 glyph = this.drawGlyph(0x3c, 0x3c, x, y + 52, 736, 497);
      glyph.flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
      glyph.widthCut = 16;
      this.drawGlyph(0x3d, 0x3d, x + 10, y + 52, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;

      this.drawTwoDigitNumber(x + 108, y + 16, gameState_800babc8.charData_32c[charId].level_12);

      final int dlevel;
      if(!hasDragoon(gameState_800babc8.goods_19c[0], charId)) {
        dlevel = 0;
      } else {
        dlevel = gameState_800babc8.charData_32c[charId].dlevel_13;
      }

      //LAB_8010e8e0
      this.drawTwoDigitNumber(x + 108, y + 28, dlevel);
      final int xp = getXpToNextLevel(charId);
      this.drawSixDigitNumber(x + 76 - this.getXpWidth(xp), y + 40, gameState_800babc8.charData_32c[charId].xp_00);
      this.drawGlyph(0x22, 0x22, x - (this.getXpWidth(xp) - 114), y + 40, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
      this.drawNextLevelXp(x + 84, y + 40, xp);

      final int dxp = dragoonXpRequirements_800fbbf0[charId][gameState_800babc8.charData_32c[charId].dlevel_13 + 1];
      this.drawSixDigitNumber(x + 76 - this.getXpWidth(dxp), y + 52, gameState_800babc8.charData_32c[charId].dlevelXp_0e);
      this.drawGlyph(0x22, 0x22, x - (this.getXpWidth(dxp) - 114), y + 52, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
      this.drawNextLevelXp(x + 84, y + 52, dxp);
    }

    //LAB_8010e978
  }

  @Method(0x8010e9a8L)
  private void drawReport() {
    int y1 = 24;
    int y2 = -82;
    int y3 = -70;

    //LAB_8010e9fc
    for(int i = 0; i < 3; i++) {
      if(gameState_800babc8.charIds_88[i] != -1) {
        this.drawChar(176, y1, gameState_800babc8.charIds_88[i]);

        if(this.levelsGained_8011e1c8[i] != 0) {
          this.levelsGained_8011e1c8[i] = 0;
          addLevelUpOverlay(72, y2);
          playMenuSound(9);
        }

        //LAB_8010ea44
        if(this.dragoonLevelsGained_8011e1d8[i] != 0) {
          this.dragoonLevelsGained_8011e1d8[i] = 0;
          addLevelUpOverlay(72, y3);
          playMenuSound(9);
        }
      }

      //LAB_8010ea70
      y1 += 64;
      y2 += 64;
      y3 += 64;
    }

    this.drawEightDigitNumber( 96, 28, goldGainedFromCombat_800bc920);
    this.drawSixDigitNumber(108, 40, totalXpFromCombat_800bc95c);

    y1 = 63;
    y2 = 64;

    //LAB_8010eae0
    for(final EnemyDrop enemyDrop : itemsDroppedByEnemies_800bc928) {
      renderItemIcon(enemyDrop.icon, 18, y1, 0x8);
      renderText(enemyDrop.name, 28, y2, TextColour.WHITE);

      //LAB_8010eb38
      y2 += 16;
      y1 += 16;
    }

    //LAB_8010eb58
    this.drawEightDigitNumber(96, 156, gameState_800babc8.gold_94);

    //LAB_8010ebb0
    uploadRenderables();
    drawBattleReportOverlays();
  }

  @Method(0x8010ebecL)
  private void renderAdditionsUnlocked(final int height) {
    for(int i = 0; i < 3; i++) {
      if(this.additionsUnlocked_8011e1b8[i] != 0) {
        this.renderAdditionUnlocked(168, 40 + i * 64, this.additionsUnlocked_8011e1b8[i] - 1, height);
      }
    }
  }

  @Method(0x8010ec6cL)
  private void renderSpellsUnlocked(final int height) {
    //LAB_8010ec98
    for(int i = 0; i < 3; i++) {
      if(this.spellsUnlocked_8011e1a8[i] != 0) {
        this.renderSpellUnlocked(168, 40 + i * 64, this.spellsUnlocked_8011e1a8[i] - 1, height);
      }

      //LAB_8010ecc0
    }
  }

  @Method(0x8010d32cL)
  private boolean characterIsAlive(final int charSlot) {
    final int charIndex = gameState_800babc8.charIds_88[charSlot];

    if(charIndex != -1) {
      //LAB_8010d36c
      for(int i = 0; i < livingCharCount_800bc97c; i++) {
        if(livingCharIds_800bc968[i] == charIndex) {
          return true;
        }

        //LAB_8010d384
      }
    }

    //LAB_8010d390
    return false;
  }

  @Method(0x8010d398L)
  private void renderAdditionUnlocked(final int x, final int y, final int additionIndex, final int height) {
    this.drawResultsBackground(x, y + 20 - height, 134, (height + 1) * 2, 4);
    this.drawResultsBackground(x + 1, y + 20 - height + 1, 132, height * 2, 3);

    if(height >= 20) {
      Scus94491BpeSegment_8002.renderText(additions_8011a064[additionIndex], x - 4, y + 6, TextColour.WHITE, 0);
      Scus94491BpeSegment_8002.renderText(NEW_ADDITION, x - 4, y + 20, TextColour.WHITE, 0);
    }

    //LAB_8010d470
  }

  @Method(0x8010d498L)
  private void renderSpellUnlocked(final int x, final int y, final int spellIndex, final int height) {
    this.drawResultsBackground(x, y + 20 - height, 134, (height + 1) * 2, 6); // New spell border
    this.drawResultsBackground(x + 1, y + 20 - height + 1, 132, height * 2, 5); // New spell background

    if(height >= 20) {
      Scus94491BpeSegment_8002.renderText(spellStats_800fa0b8[spellIndex].name, x - 4, y + 6, TextColour.WHITE, 0);
      Scus94491BpeSegment_8002.renderText(SPELL_UNLOCKED, x - 4, y + 20, TextColour.WHITE, 0);
    }

    //LAB_8010d470
  }

  @Method(0x8010d598L)
  private int getUltimateAdditionIdIfUnlocked(final int charSlot) {
    final int charIndex = gameState_800babc8.charIds_88[charSlot];

    if(charIndex == -1) {
      return 0;
    }

    if(!unlockedUltimateAddition_800bc910[charSlot]) {
      //LAB_8010d5d0
      return 0;
    }

    //LAB_8010d5d8
    final int additionId = additionOffsets_8004f5ac[charIndex] + additionCounts_8004f5c0[charIndex];
    if(additionId == -1) {
      return 0;
    }

    //LAB_8010d60c
    return additionId;
  }

  private void deleteResultsScreenObjects() {
    for(int i = 0; i < this.resultsBackgroundObj.length; i++) {
      if(this.resultsBackgroundObj[i] != null) {
        this.resultsBackgroundObj[i].delete();
        this.resultsBackgroundObj[i] = null;
      }
    }
  }

  private enum MenuState {
    INIT_0,
    WAIT_FOR_UI_FILE_TO_LOAD_1,
    WAIT_FOR_FADE_IN_AND_INIT_CONTROLS_2,
    WAIT_FOR_FIRST_BUTTON_PRESS_3,
    TICK_GOLD_4,
    TICK_XP_5,
    EMBIGGEN_UNLOCKED_ADDITIONS_6,
    ENSMALLEN_UNLOCKED_ADDITIONS_7,
    MAIN_LEVEL_UPS_8,
    SECONDARY_LEVEL_UPS_9,
    DRAGOON_LEVEL_UPS_10,
    WAIT_FOR_DRAGOON_LEVEL_UP_INPUT_11,
    EMBIGGEN_SPELL_UNLOCK_12,
    ENSMALLEN_SPELL_UNLOCK_13,
    WAIT_FOR_INPUT_14,
    _15,
    FADE_OUT_16,
    WAIT_FOR_FADE_OUT_17,
    UNLOAD_18,
  }
}
