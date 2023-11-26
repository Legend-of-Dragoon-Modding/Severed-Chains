package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.core.gpu.GpuCommandPoly;
import legend.core.memory.Method;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.combat.types.EnemyDrop;
import legend.game.inventory.WhichMenu;
import legend.game.types.InventoryMenuState;
import legend.game.types.LodString;
import legend.game.types.Renderable58;
import legend.game.types.Translucency;

import static legend.core.GameEngine.GPU;
import static legend.game.SItem._800fbbf0;
import static legend.game.SItem._800fbc88;
import static legend.game.SItem._800fbc9c;
import static legend.game.SItem._800fbca8;
import static legend.game.SItem.additions_8011a064;
import static legend.game.SItem.cacheCharacterSlots;
import static legend.game.SItem.getXpToNextLevel;
import static legend.game.SItem.hasDragoon;
import static legend.game.SItem.loadAdditions;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.menuAssetsLoaded;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderItemIcon;
import static legend.game.SItem.renderText;
import static legend.game.Scus94491BpeSegment.FUN_80018e84;
import static legend.game.Scus94491BpeSegment.FUN_800192d8;
import static legend.game.Scus94491BpeSegment.FUN_80019470;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.loadDrgnFileSync;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.getJoypadInputByPriority;
import static legend.game.Scus94491BpeSegment_8002.getUnlockedDragoonSpells;
import static legend.game.Scus94491BpeSegment_8002.giveItems;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderables;
import static legend.game.Scus94491BpeSegment_8004.additionCounts_8004f5c0;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_800b.confirmDest_800bdc30;
import static legend.game.Scus94491BpeSegment_800b.fullScreenEffect_800bb140;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.goldGainedFromCombat_800bc920;
import static legend.game.Scus94491BpeSegment_800b.inventoryJoypadInput_800bdc44;
import static legend.game.Scus94491BpeSegment_800b.inventoryMenuState_800bdc28;
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
import static legend.game.combat.Bttl_800c.spellStats_800fa0b8;

public class PostBattleScreen extends MenuScreen {
  private static final LodString NEW_ADDITION = new LodString("New Addition");
  private static final LodString SPELL_UNLOCKED = new LodString("Spell Unlocked");

  private int _8011e170;
  private int _8011e178;
  private int soundTick_8011e17c;
  private final int[] pendingXp_8011e180 = new int[10];
  private final int[] spellsUnlocked_8011e1a8 = new int[10];
  private final int[] additionsUnlocked_8011e1b8 = new int[10];
  private final int[] _8011e1c8 = new int[10];
  private final int[] _8011e1d8 = new int[10];

  @Method(0x8010d614L)
  @Override
  protected void render() {
    inventoryJoypadInput_800bdc44.setu(getJoypadInputByPriority());

    switch(inventoryMenuState_800bdc28.get()) {
      case INIT_0:
        if(uiFile_800bdc3c != null) {
          uiFile_800bdc3c.delete();
        }

        renderablePtr_800bdc5c = null;
        uiFile_800bdc3c = null;
        resizeDisplay(320, 240);
        loadDrgnFileSync(0, 6665, data -> menuAssetsLoaded(data, 0));
        loadDrgnFileSync(0, 6666, data -> menuAssetsLoaded(data, 1));
        textZ_800bdf00.set(33);
        inventoryMenuState_800bdc28.set(InventoryMenuState.AWAIT_INIT_1);
        break;

      case AWAIT_INIT_1:
        if(uiFile_800bdc3c != null) {
          startFadeEffect(2, 10);
          inventoryMenuState_800bdc28.set(InventoryMenuState._2);
        }
        break;

      case _2:
        if(fullScreenEffect_800bb140.currentColour_28 == 0) {
          deallocateRenderables(0xff);
          Renderable58 glyph = this.FUN_8010cfa0(0, 0, 165, 21, 720, 497);
          glyph.widthScale = 0;
          glyph.heightScale_38 = 0;
          glyph = this.FUN_8010cfa0(2, 2, 13, 21, 720, 497);
          glyph.widthScale = 0;
          glyph.heightScale_38 = 0;
          glyph = this.FUN_8010cfa0(1, 1, 13, 149, 720, 497);
          glyph.widthScale = 0;
          glyph.heightScale_38 = 0;

          this.FUN_8010cfa0(0x3e, 0x3e, 24, 28, 736, 497);
          this.FUN_8010cfa0(0x3d, 0x3d, 24, 40, 736, 497);
          this.FUN_8010cfa0(0x40, 0x40, 24, 52, 736, 497);

          cacheCharacterSlots();

          //LAB_8010d87c
          for(int i = 0; i < 10; i++) {
            this.spellsUnlocked_8011e1a8[i] = 0;
            this.additionsUnlocked_8011e1b8[i] = 0;
            this._8011e1c8[i] = 0;
            this._8011e1d8[i] = 0;
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
              this.pendingXp_8011e180[gameState_800babc8.charIds_88[charSlot]] = totalXpFromCombat_800bc95c.get() / xpDivisor;
            }
          }

          //LAB_8010d9d4
          //LAB_8010d9f8
          for(int secondaryCharSlot = 0; secondaryCharSlot < 6; secondaryCharSlot++) {
            final int secondaryCharIndex = secondaryCharIds_800bdbf8.get(secondaryCharSlot).get();

            if(secondaryCharIndex != -1) {
              this.pendingXp_8011e180[secondaryCharIndex] = MathHelper.safeDiv(totalXpFromCombat_800bc95c.get(), xpDivisor) / 2;
            }

            //LAB_8010da24
          }

          inventoryMenuState_800bdc28.set(InventoryMenuState.INIT_MAIN_MENU_3);
          this.FUN_8010e9a8(1);
        }

        break;

      case INIT_MAIN_MENU_3:
        if((press_800bee94.get() & 0x20) != 0) {
          //LAB_8010da84
          if(goldGainedFromCombat_800bc920.get() == 0) {
            inventoryMenuState_800bdc28.set(InventoryMenuState._5);
          } else {
            inventoryMenuState_800bdc28.set(InventoryMenuState.MAIN_MENU_4);
          }
        }

        this.FUN_8010e9a8(0);
        break;

      case MAIN_MENU_4:
        final int goldTick;
        if((press_800bee94.get() & 0x20) != 0) {
          goldTick = goldGainedFromCombat_800bc920.get();
        } else {
          //LAB_8010dab4
          goldTick = 10;
        }

        //LAB_8010dabc
        final int goldGained = goldGainedFromCombat_800bc920.get();

        if(goldTick >= goldGained) {
          this.soundTick_8011e17c = 0;
          goldGainedFromCombat_800bc920.set(0);
          inventoryMenuState_800bdc28.set(InventoryMenuState._5);
          gameState_800babc8.gold_94 += goldGained;
        } else {
          //LAB_8010db00
          goldGainedFromCombat_800bc920.sub(goldTick);
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
          playSound(0x1L);
        }

        this.FUN_8010e9a8(0);
        break;

      case _5:
        final boolean moreXpToGive =
          this.givePendingXp(gameState_800babc8.charIds_88[0], 0) ||
          this.givePendingXp(gameState_800babc8.charIds_88[1], 1) ||
          this.givePendingXp(gameState_800babc8.charIds_88[2], 2) ||
          this.givePendingXp(secondaryCharIds_800bdbf8.get(0).get(), 3) ||
          this.givePendingXp(secondaryCharIds_800bdbf8.get(1).get(), 4) ||
          this.givePendingXp(secondaryCharIds_800bdbf8.get(2).get(), 5) ||
          this.givePendingXp(secondaryCharIds_800bdbf8.get(3).get(), 6) ||
          this.givePendingXp(secondaryCharIds_800bdbf8.get(4).get(), 7) ||
          this.givePendingXp(secondaryCharIds_800bdbf8.get(5).get(), 8);

        if(moreXpToGive) {
          this.soundTick_8011e17c++;

          if((this.soundTick_8011e17c & 0x1) != 0) {
            playSound(0x1L);
          }
        } else {
          this._8011e170 = 3;
          totalXpFromCombat_800bc95c.set(0);

          if(this.additionsUnlocked_8011e1b8[0] + this.additionsUnlocked_8011e1b8[1] + this.additionsUnlocked_8011e1b8[2] == 0) {
            //LAB_8010dc9c
            inventoryMenuState_800bdc28.set(InventoryMenuState.REPLACE_INIT_8);
          } else if((press_800bee94.get() & 0x20) != 0) {
            playSound(0x2L);
            this._8011e178 = 0;
            inventoryMenuState_800bdc28.set(InventoryMenuState.CONFIG_6);
          }
        }

        this.FUN_8010e9a8(0);
        break;

      case CONFIG_6:
        if(this._8011e178 < 20) {
          this._8011e178 += 2;
        } else {
          //LAB_8010dcc8
          if((press_800bee94.get() & 0x20) != 0) {
            playSound(0x2L);

            //LAB_8010dcf0
            inventoryMenuState_800bdc28.set(InventoryMenuState._7);
          }
        }

        //LAB_8010dcf4
        //LAB_8010dcf8
        this.renderAdditionsUnlocked(this._8011e178);
        this.FUN_8010e9a8(0);
        break;

      case _7:
        if(this._8011e178 > 0) {
          this._8011e178 -= 2;
        } else {
          //LAB_8010dd28
          inventoryMenuState_800bdc28.set(InventoryMenuState.REPLACE_INIT_8);
        }

        this.renderAdditionsUnlocked(this._8011e178);
        this.FUN_8010e9a8(0);
        break;

      case REPLACE_INIT_8:
        if(this._8011e170 >= 9) {
          //LAB_8010dd90
          inventoryMenuState_800bdc28.set(InventoryMenuState.REPLACE_MENU_10);
        } else if(this._8011e1c8[this._8011e170] != 0) {
          FUN_800192d8(-80, 44);
          playSound(9);
          inventoryMenuState_800bdc28.set(InventoryMenuState._9);
        } else {
          //LAB_8010dd88
          this._8011e170++;
        }

        this.FUN_8010e9a8(0);
        break;

      case _9:
        this.FUN_8010e708(24, 152, secondaryCharIds_800bdbf8.get(this._8011e170 - 3).get());

        if((press_800bee94.get() & 0x60) != 0) {
          playSound(0x2L);
          this._8011e1c8[this._8011e170] = 0;
          inventoryMenuState_800bdc28.set(InventoryMenuState.REPLACE_INIT_8);
          this._8011e170++;
        }

        this.FUN_8010e9a8(0);
        break;

      case REPLACE_MENU_10:
        for(int charSlot = 0; charSlot < 3; charSlot++) {
          if(this.characterIsAlive(charSlot)) {
            this.levelUpDragoon(gameState_800babc8.charIds_88[charSlot], charSlot);
          }
        }

        //LAB_8010de6c
        if(this.spellsUnlocked_8011e1a8[0] != 0 || this.spellsUnlocked_8011e1a8[1] != 0 || this.spellsUnlocked_8011e1a8[2] != 0) {
          inventoryMenuState_800bdc28.set(InventoryMenuState._11);
        } else {
          //LAB_8010de98
          inventoryMenuState_800bdc28.set(InventoryMenuState._14);
        }

        this.FUN_8010e9a8(0);
        break;

      case _11:
        if((press_800bee94.get() & 0x20) != 0) {
          this._8011e178 = 0;
          playSound(0x2L);

          //LAB_8010decc
          inventoryMenuState_800bdc28.set(InventoryMenuState.EQUIPMENT_INIT_12);
        }

        this.FUN_8010e9a8(0);
        break;

      case EQUIPMENT_INIT_12:
        if(this._8011e178 < 20) {
          this._8011e178 += 2;
        } else {
          //LAB_8010def4
          if((press_800bee94.get() & 0x20) != 0) {
            playSound(0x2L);

            //LAB_8010df1c
            inventoryMenuState_800bdc28.set(InventoryMenuState._13);
          }
        }

        //LAB_8010df20
        //LAB_8010df24
        this.renderSpellsUnlocked(this._8011e178);
        this.FUN_8010e9a8(0);
        break;

      case _13:
        if(this._8011e178 > 0) {
          this._8011e178 -= 2;
        } else {
          //LAB_8010df54
          //LAB_8010df1c
          inventoryMenuState_800bdc28.set(InventoryMenuState._14);
        }

        //LAB_8010df20
        //LAB_8010df24
        this.renderSpellsUnlocked(this._8011e178);
        this.FUN_8010e9a8(0);
        break;

      case _14:
        if((press_800bee94.get() & 0x60) != 0) {
          playSound(3);

          if(itemsDroppedByEnemies_800bc928.isEmpty() || giveItems(itemsDroppedByEnemies_800bc928) == 0) {
            //LAB_8010dfac
            // No items remaining
            this.FUN_8010d050(InventoryMenuState._18);
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
        this.FUN_8010e9a8(0);
        break;

      case LIST_INIT_16:
        startFadeEffect(1, 10);
        inventoryMenuState_800bdc28.set(InventoryMenuState._17);

      case _17:
        this.FUN_8010e9a8(0);

        if(fullScreenEffect_800bb140.currentColour_28 >= 0xff) {
          inventoryMenuState_800bdc28.set(confirmDest_800bdc30.get());
          FUN_80019470();
        }

        break;

      case _18:
        startFadeEffect(2, 10);
        deallocateRenderables(0xff);

        if(uiFile_800bdc3c != null) {
          uiFile_800bdc3c.delete();
        }

        uiFile_800bdc3c = null;
        whichMenu_800bdc38 = WhichMenu.UNLOAD_POST_COMBAT_REPORT_30;
        textZ_800bdf00.set(13);

        menuStack.popScreen();
        break;

      case _19:
        menuStack.render();
        break;
    }

    //LAB_8010e09c
    //LAB_8010e0a0
    this.FUN_8010d078(166,  22, 136, 192, 1);
    this.FUN_8010d078( 14,  22, 144, 120, 1);
    this.FUN_8010d078( 14, 150, 144,  64, 1);
    this.FUN_8010d078( 0,    0, 240, 240, 0);
  }

  /**
   * @return True if there is remaining XP to give
   */
  @Method(0x8010cc24L)
  private boolean givePendingXp(final int charIndex, final int charSlot) {
    if(charIndex == -1) {
      return false;
    }

    final int pendingXp = this.pendingXp_8011e180[charIndex];

    if(pendingXp == 0) {
      //LAB_8010cc68
      return false;
    }

    //LAB_8010cc70
    final int cappedPendingXp;
    if((press_800bee94.get() & 0x20) != 0 || pendingXp < 10) {
      cappedPendingXp = pendingXp;
    } else {
      cappedPendingXp = 10;
    }

    //LAB_8010cc94
    //LAB_8010cc98
    int xp = gameState_800babc8.charData_32c[charIndex].xp_00;
    if(xp <= 999999) {
      xp = xp + cappedPendingXp;
    } else {
      xp = 999999;
    }

    //LAB_8010ccd4
    gameState_800babc8.charData_32c[charIndex].xp_00 = xp;
    this.pendingXp_8011e180[charIndex] -= cappedPendingXp;

    //LAB_8010cd30
    while(gameState_800babc8.charData_32c[charIndex].xp_00 >= getXpToNextLevel(charIndex) && gameState_800babc8.charData_32c[charIndex].level_12 < 60) {
      gameState_800babc8.charData_32c[charIndex].level_12++;

      this._8011e1c8[charSlot]++;
      if(this.additionsUnlocked_8011e1b8[charSlot] == 0) {
        this.additionsUnlocked_8011e1b8[charSlot] = loadAdditions(charIndex, null);
      }

      //LAB_8010cd9c
    }

    //LAB_8010cdb0
    //LAB_8010cdcc
    return this.pendingXp_8011e180[charIndex] > 0;
  }

  @Method(0x8010cde8L)
  private void levelUpDragoon(final int charIndex, final int charSlot) {
    if(charIndex != -1) {
      gameState_800babc8.charData_32c[charIndex].dlevelXp_0e += spGained_800bc950.get(charSlot).get();

      if(gameState_800babc8.charData_32c[charIndex].dlevelXp_0e > 32000) {
        gameState_800babc8.charData_32c[charIndex].dlevelXp_0e = 32000;
      }

      //LAB_8010ceb0
      //LAB_8010cecc
      while(gameState_800babc8.charData_32c[charIndex].dlevelXp_0e >= _800fbbf0.offset(charIndex * 0x4L).deref(2).offset(gameState_800babc8.charData_32c[charIndex].dlevel_13 * 0x2L).offset(0x2L).get() && gameState_800babc8.charData_32c[charIndex].dlevel_13 < 5) {
        loadCharacterStats();
        final byte[] spellIndices = new byte[8];
        final int spellCount = getUnlockedDragoonSpells(spellIndices, charIndex);

        gameState_800babc8.charData_32c[charIndex].dlevel_13++;
        this._8011e1d8[charSlot]++;

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
  private Renderable58 FUN_8010cfa0(final int startGlyph, final int endGlyph, final int x, final int y, final int u, final int v) {
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
  private void FUN_8010d050(final InventoryMenuState nextMenuState) {
    inventoryMenuState_800bdc28.set(InventoryMenuState.LIST_INIT_16);
    confirmDest_800bdc30.set(nextMenuState);
  }

  @Method(0x8010d078L)
  private void FUN_8010d078(int x, int y, final int w, final int h, final int type) {
    x -= 8 + displayWidth_1f8003e0.get() / 2;
    y -= 120;

    final GpuCommandPoly cmd = new GpuCommandPoly(4)
      .pos(0, x, y)
      .pos(1, x + w, y)
      .pos(2, x, y + h)
      .pos(3, x + w, y + h);

    final int z;
    switch(type) {
      case 0 -> {
        z = 36;

        cmd
          .rgb(0, 0, 0, 1)
          .rgb(1, 0, 0, 1)
          .rgb(2, 0, 0, 1)
          .rgb(3, 0, 0, 1);
      }

      case 1 -> {
        z = 36;

        cmd
          .translucent(Translucency.HALF_B_PLUS_HALF_F)
          .rgb(0, 0x80, 0x80, 0x80)
          .rgb(1,    0, 0x14, 0x50)
          .rgb(2,    0, 0x14, 0x50)
          .rgb(3,    0,    0,    0);
      }

      case 2 -> {
        z = 36;

        cmd
          .monochrome(0, 0x7f)
          .monochrome(1, 0x7f)
          .monochrome(2, 0)
          .monochrome(3, 0);
      }

      case 3 -> {
        z = 34;

        cmd
          .rgb(0, 0xff, 0x7a, 0)
          .rgb(1, 0xff, 0x7a, 0)
          .rgb(2, 0x49, 0x23, 0)
          .rgb(3, 0x49, 0x23, 0);
      }

      case 4 -> {
        z = 35;

        cmd
          .rgb(0, 0xff, 0x7a, 0)
          .rgb(1, 0xff, 0x7a, 0)
          .rgb(2, 0xff, 0x7a, 0)
          .rgb(3, 0xff, 0x7a, 0);
      }

      case 5 -> {
        z = 34;

        cmd
          .rgb(0, 0, 0x84, 0xfe)
          .rgb(1, 0, 0x84, 0xfe)
          .rgb(2, 0, 0x26, 0x48)
          .rgb(3, 0, 0x26, 0x48);
      }

      case 6 -> {
        z = 35;

        cmd
          .monochrome(0, 0x7f)
          .monochrome(1, 0x7f)
          .monochrome(2, 0)
          .monochrome(3, 0);
      }

      default -> z = 0;
    }

    //LAB_8010d2c4
    GPU.queueCommand(z, cmd);

    //LAB_8010d318
  }

  @Method(0x8010e114L)
  private Renderable58 FUN_8010e114(final int x, final int y, final int charSlot) {
    if(charSlot >= 9) {
      //LAB_8010e1ec
      throw new IllegalArgumentException("Invalid character index");
    }

    final int glyph = (int)_800fbc9c.offset(charSlot).getSigned();
    final Renderable58 renderable = this.FUN_8010cfa0(glyph, glyph, x, y, 704, (int)_800fbc88.offset(charSlot * 0x2L).getSigned());
    renderable.z_3c = 35;

    //LAB_8010e1f0
    return renderable;
  }

  @Method(0x8010e200L)
  private void FUN_8010e200(final int x, final int y, int val, final UnsignedIntRef a3) {
    val %= 10;
    if(val != 0 || a3.get() != 0) {
      //LAB_8010e254
      final Renderable58 renderable = this.FUN_8010cfa0(val + 3, val + 3, x, y, 736, 497);
      renderable.flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
      a3.set(1);
    }

    //LAB_8010e290
  }

  @Method(0x8010e2a0L)
  private void FUN_8010e2a0(final int x, final int y, final int dlevel) {
    final int s2 = Math.min(99, dlevel);
    final UnsignedIntRef sp0x10 = new UnsignedIntRef();
    this.FUN_8010e200(x, y, s2 / 10, sp0x10.set(0));
    this.FUN_8010e200(x + 6, y, s2, sp0x10.incr());
  }

  @Method(0x8010e340L)
  private void FUN_8010e340(final int x, final int y, final int val) {
    final int s2 = Math.min(999_999, val);
    final UnsignedIntRef sp0x10 = new UnsignedIntRef();
    this.FUN_8010e200(x, y, s2 / 100_000, sp0x10);
    this.FUN_8010e200(x +  6, y, s2 / 10_000, sp0x10);
    this.FUN_8010e200(x + 12, y, s2 /  1_000, sp0x10);
    this.FUN_8010e200(x + 18, y, s2 /    100, sp0x10);
    this.FUN_8010e200(x + 24, y, s2 /     10, sp0x10);
    this.FUN_8010e200(x + 30, y, s2, sp0x10.incr());
  }

  @Method(0x8010e490L)
  private void FUN_8010e490(final int x, final int y, final int val) {
    final int s2 = Math.min(99_999_999, val);
    final UnsignedIntRef sp0x10 = new UnsignedIntRef();
    this.FUN_8010e200(x, y, s2 / 10_000_000, sp0x10);
    this.FUN_8010e200(x +  6, y, s2 / 1_000_000, sp0x10);
    this.FUN_8010e200(x + 12, y, s2 /   100_000, sp0x10);
    this.FUN_8010e200(x + 18, y, s2 /    10_000, sp0x10);
    this.FUN_8010e200(x + 24, y, s2 /     1_000, sp0x10);
    this.FUN_8010e200(x + 30, y, s2 /       100, sp0x10);
    this.FUN_8010e200(x + 36, y, s2 /        10, sp0x10);
    this.FUN_8010e200(x + 42, y, s2, sp0x10.incr());
  }

  @Method(0x8010e630L)
  private void FUN_8010e630(final int x, final int y, final int val) {
    if(val != 0) {
      this.FUN_8010e340(x, y, val);
    } else {
      //LAB_8010e660
      final Renderable58 renderable = this.FUN_8010cfa0(0x47, 0x47, x + 30, y, 736, 497);
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
  private void FUN_8010e708(final int x, final int y, final int charIndex) {
    if(charIndex != -1) {
      this.FUN_8010d078(x + 1, y + 5, 24, 32, 2);
      final Renderable58 renderable = this.FUN_8010e114(x - 1, y + 4, charIndex);
      renderable.flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
      this.FUN_8010cfa0((int)_800fbca8.offset(charIndex).get(), (int)_800fbca8.offset(charIndex).get(), x + 32, y + 4, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
      this.FUN_8010cfa0(0x3b, 0x3b, x + 30, y + 16, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
      this.FUN_8010cfa0(0x3c, 0x3c, x + 30, y + 28, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
      this.FUN_8010cfa0(0x3d, 0x3d, x, y + 40, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
      this.FUN_8010cfa0(0x3c, 0x3c, x, y + 52, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
      this.FUN_8010cfa0(0x3d, 0x3d, x + 10, y + 52, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;

      this.FUN_8010e2a0(x + 108, y + 16, gameState_800babc8.charData_32c[charIndex].level_12);

      final int dlevel;
      if(!hasDragoon(gameState_800babc8.goods_19c[0], charIndex)) {
        dlevel = 0;
      } else {
        dlevel = gameState_800babc8.charData_32c[charIndex].dlevel_13;
      }

      //LAB_8010e8e0
      this.FUN_8010e2a0(x + 108, y + 28, dlevel);
      final int xp = getXpToNextLevel(charIndex);
      this.FUN_8010e340(x + 76 - this.getXpWidth(xp), y + 40, gameState_800babc8.charData_32c[charIndex].xp_00);
      this.FUN_8010cfa0(0x22, 0x22, x - (this.getXpWidth(xp) - 114), y + 40, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
      this.FUN_8010e630(x + 84, y + 40, xp);


      final int dxp = (int) _800fbbf0.offset(charIndex * 0x4L).deref(2).offset(gameState_800babc8.charData_32c[charIndex].dlevel_13 * 0x2L).offset(0x2L).get();
      this.FUN_8010e340(x + 76 - this.getXpWidth(dxp), y + 52, gameState_800babc8.charData_32c[charIndex].dlevelXp_0e);
      this.FUN_8010cfa0(0x22, 0x22, x - (this.getXpWidth(dxp) - 114), y + 52, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
      this.FUN_8010e630(x + 84, y + 52, dxp);
    }

    //LAB_8010e978
  }

  @Method(0x8010e9a8L)
  private void FUN_8010e9a8(final int a0) {
    int y1 = 24;
    int y2 = -82;
    int y3 = -70;

    //LAB_8010e9fc
    for(int i = 0; i < 3; i++) {
      if(gameState_800babc8.charIds_88[i] != -1) {
        this.FUN_8010e708(176, y1, gameState_800babc8.charIds_88[i]);

        if(this._8011e1c8[i] != 0) {
          this._8011e1c8[i] = 0;
          FUN_800192d8(72, y2);
          playSound(9);
        }

        //LAB_8010ea44
        if(this._8011e1d8[i] != 0) {
          this._8011e1d8[i] = 0;
          FUN_800192d8(72, y3);
          playSound(9);
        }
      }

      //LAB_8010ea70
      y1 += 64;
      y2 += 64;
      y3 += 64;
    }

    this.FUN_8010e490( 96, 28, goldGainedFromCombat_800bc920.get());
    this.FUN_8010e340(108, 40, totalXpFromCombat_800bc95c.get());

    y1 = 63;
    y2 = 64;

    //LAB_8010eae0
    for(final EnemyDrop enemyDrop : itemsDroppedByEnemies_800bc928) {
      renderItemIcon(enemyDrop.icon, 18, y1, 0x8);
      renderText(new LodString(enemyDrop.name), 28, y2, TextColour.WHITE);

      //LAB_8010eb38
      y2 += 16;
      y1 += 16;
    }

    //LAB_8010eb58
    this.FUN_8010e490(96, 156, gameState_800babc8.gold_94);

    if(a0 != 0) {
      this.FUN_8010cfa0(0x3f, 0x3f, 144,  28, 736, 497);
      this.FUN_8010cfa0(0x3f, 0x3f, 144, 156, 736, 497);
    }

    //LAB_8010ebb0
    uploadRenderables();
    FUN_80018e84();
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
      for(int i = 0; i < livingCharCount_800bc97c.get(); i++) {
        if(livingCharIds_800bc968.get(i).get() == charIndex) {
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
    this.FUN_8010d078(x, y + 20 - height, 134, (height + 1) * 2, 4);
    this.FUN_8010d078(x + 1, y + 20 - height + 1, 132, height * 2, 3);

    if(height >= 20) {
      Scus94491BpeSegment_8002.renderText(additions_8011a064.get(additionIndex).deref(), x - 4, y + 6, TextColour.WHITE, 0);
      Scus94491BpeSegment_8002.renderText(NEW_ADDITION, x - 4, y + 20, TextColour.WHITE, 0);
    }

    //LAB_8010d470
  }

  @Method(0x8010d498L)
  private void renderSpellUnlocked(final int x, final int y, final int spellIndex, final int height) {
    this.FUN_8010d078(x, y + 20 - height, 134, (height + 1) * 2, 6); // New spell border
    this.FUN_8010d078(x + 1, y + 20 - height + 1, 132, height * 2, 5); // New spell background

    if(height >= 20) {
      Scus94491BpeSegment_8002.renderText(new LodString(spellStats_800fa0b8[spellIndex].name), x - 4, y + 6, TextColour.WHITE, 0);
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
    final int additionId = additionOffsets_8004f5ac.get(charIndex).get() + additionCounts_8004f5c0.get(charIndex).get();
    if(additionId == -1) {
      return 0;
    }

    //LAB_8010d60c
    return additionId;
  }
}
