package legend.game.combat.ui;

import legend.core.Config;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.environment.BattleItemMenuArrowUvMetrics06;
import legend.game.combat.environment.BattleMenuBackgroundDisplayMetrics0c;
import legend.game.combat.environment.BattleMenuHighlightMetrics12;
import legend.game.combat.environment.BattleMenuIconMetrics08;
import legend.game.combat.environment.BattleMenuTextMetrics08;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.battle.BattleDescriptionEvent;
import legend.game.scripting.ScriptState;
import legend.game.types.LodString;
import legend.game.types.Translucency;

import javax.annotation.Nullable;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment_8002.intToStr;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.input_800bee90;
import static legend.game.Scus94491BpeSegment_800b.press_800bee94;
import static legend.game.combat.Bttl_800c._800c6748;
import static legend.game.combat.Bttl_800c.aliveBentCount_800c669c;
import static legend.game.combat.Bttl_800c.aliveMonsterCount_800c6758;
import static legend.game.combat.Bttl_800c.battleMenu_800c6c34;
import static legend.game.combat.Bttl_800c.cameraPositionIndicesIndices_800c6c30;
import static legend.game.combat.Bttl_800c.charCount_800c677c;
import static legend.game.combat.Bttl_800c.combatItems_800c6988;
import static legend.game.combat.Bttl_800c.countCameraPositionIndicesIndices_800c6ba0;
import static legend.game.combat.Bttl_800c.currentCameraPositionIndicesIndex_800c66b0;
import static legend.game.combat.Bttl_800c.currentCameraPositionIndicesIndicesIndex_800c6ba1;
import static legend.game.combat.Bttl_800c.currentStageData_800c6718;
import static legend.game.combat.Bttl_800c.dragoonSpells_800c6960;
import static legend.game.combat.Bttl_800c.spellAndItemMenu_800c6b60;
import static legend.game.combat.Bttl_800c.spellStats_800fa0b8;
import static legend.game.combat.Bttl_800c.targetBents_800c71f0;
import static legend.game.combat.Bttl_800f.buildBattleMenuBackground;
import static legend.game.combat.Bttl_800f.buildBattleMenuElement;
import static legend.game.combat.Bttl_800f.prepareItemList;
import static legend.game.combat.Bttl_800f.setActiveCharacterSpell;

public class BattleMenuStruct58 {
  private static final int[] iconFlags_800c7194 = {4, 1, 5, 6, 2, 9, 3, 7};

  private static final BattleMenuHighlightMetrics12 battleMenuHighlightMetrics_800c71bc = new BattleMenuHighlightMetrics12(0, 0, 24, 24, 232, 120, 23, 23, 0);
  public static final int[] dragoonSpiritIconClutOffsets_800c71d0 = {152, 153, 154, 155, 156, 153, 157, 158, 154, 159};
  public static final int[] battleMenuIconStates_800c71e4 = {0, 1, 2, 1};

  private static final BattleItemMenuArrowUvMetrics06[] battleMenuBackgroundMetrics_800fb5dc = {
    new BattleItemMenuArrowUvMetrics06(184, 72, 7, 7, 0),
    new BattleItemMenuArrowUvMetrics06(176, 64, 7, 7, 0),
    new BattleItemMenuArrowUvMetrics06(176, 64, 7, 7, 2),
    new BattleItemMenuArrowUvMetrics06(176, 64, 7, 7, 1),
    new BattleItemMenuArrowUvMetrics06(176, 64, 7, 7, 3),
    new BattleItemMenuArrowUvMetrics06(184, 64, 7, 7, 0),
    new BattleItemMenuArrowUvMetrics06(176, 72, 7, 7, 0),
    new BattleItemMenuArrowUvMetrics06(176, 72, 7, 7, 2),
    new BattleItemMenuArrowUvMetrics06(184, 64, 7, 7, 1),
  };

  private static final BattleMenuBackgroundDisplayMetrics0c[] battleMenuBackgroundDisplayMetrics_800fb614 = {
    new BattleMenuBackgroundDisplayMetrics0c(0, -8, -8, 8, 8),
    new BattleMenuBackgroundDisplayMetrics0c(1, 0, -8, 8, 8),
    new BattleMenuBackgroundDisplayMetrics0c(2, -8, 0, 8, 8),
    new BattleMenuBackgroundDisplayMetrics0c(3, 0, 0, 8, 8),
    new BattleMenuBackgroundDisplayMetrics0c(0, 0, -8, 0, 8),
    new BattleMenuBackgroundDisplayMetrics0c(0, -8, 0, 8, 0),
    new BattleMenuBackgroundDisplayMetrics0c(1, 0, 0, 8, 0),
    new BattleMenuBackgroundDisplayMetrics0c(2, 0, 0, 0, 8),
  };

  private static final BattleMenuIconMetrics08[] battleMenuIconMetrics_800fb674 = {
    new BattleMenuIconMetrics08(112, 64, 28, 0),
    new BattleMenuIconMetrics08(80, 64, 25, -1),
    new BattleMenuIconMetrics08(64, 64, 24, 1),
    new BattleMenuIconMetrics08(16, 64, 21, -1),
    new BattleMenuIconMetrics08(32, 64, 22, -1),
    new BattleMenuIconMetrics08(48, 64, 23, -1),
    new BattleMenuIconMetrics08(96, 64, 26, 0),
    new BattleMenuIconMetrics08(112, 64, 13, 0),
    new BattleMenuIconMetrics08(0, 64, 20, -1),
    new BattleMenuIconMetrics08(16, 16, 16, 16),
    new BattleMenuIconMetrics08(16, 16, 16, 24),
    new BattleMenuIconMetrics08(24, 16, 24, 24),
  };

  private static final int[][] battleMenuIconHeights_800fb6bc = {
    {16, 16, 16},
    {16, 16, 16},
    {16, 24, 24},
    {16, 24, 24},
    {16, 24, 24},
    {16, 24, 24},
    {16, 16, 16},
    {16, 16, 16},
    {16, 24, 24},
  };

  private static final int[][] battleMenuIconVOffsets_800fb6f4 = {
    {0, 16, 32},
    {0, 16, 32},
    {0, 16, 40},
    {0, 16, 40},
    {0, 16, 40},
    {0, 16, 40},
    {0, 16, 32},
    {0, 16, 32},
    {0, 16, 40},
  };

  private static final BattleMenuTextMetrics08[] battleMenuTextMetrics_800fb72c = {
    new BattleMenuTextMetrics08(48, 56, 32, 33),
    new BattleMenuTextMetrics08(144, 88, 40, 16),
    new BattleMenuTextMetrics08(144, 72, 32, 14),
    new BattleMenuTextMetrics08(16, 56, 32, 33),
    new BattleMenuTextMetrics08(144, 64, 32, 33),
    new BattleMenuTextMetrics08(144, 80, 32, 33),
    new BattleMenuTextMetrics08(144, 104, 40, 15),
    new BattleMenuTextMetrics08(0, 16, 32, 0),
    new BattleMenuTextMetrics08(144, 96, 40, 14),
    new BattleMenuTextMetrics08(0, 0, 0, 0),
    new BattleMenuTextMetrics08(0, 0, 0, 0),
    new BattleMenuTextMetrics08(0, 0, 0, 0),
  };

  private UiBox battleUiItemSpellList;
  private UiBox battleUiSpellList;
  private UiBox battleUiItemDescription;

  public short state_00;
  /** 0 = don't render, 1 = wrapping, 2 = render normally */
  public int highlightState_02;
  public short charIndex_04;
  public short x_06;
  public short y_08;
  public short xShiftOffset_0a;

  public short iconCount_0e;
  /**
   * <ul>
   *   <li>0x80 - disabled</li>
   * </ul>
   */
  public final int[] iconFlags_10 = new int[9];
  public short selectedIcon_22;
  public short currentIconStateTick_24;
  public short iconStateIndex_26;
  public short highlightX0_28;
  public short highlightY_2a;
  public short colour_2c;

  public int countHighlightMovementStep_30;
  public int highlightMovementDistance_34;
  public int currentHighlightMovementStep_38;
  public int highlightX1_3c;
  public boolean renderSelectedIconText_40;
  public int cameraPositionSwitchTicksRemaining_44;
  public int target_48;
  public boolean displayTargetArrowAndName_4c;
  public int targetType_50;
  public int combatantIndex_54;

  public final MV transforms = new MV();
  public final Obj[][] actionIconObj = new Obj[9][3];
  public final Obj[][] dragoonIconObj = new Obj[10][3];
  public final Obj[] actionIconTextObj = new Obj[9];
  public Obj actionDisabledObj;
  public final Obj[] divineSpiritOverlay = new Obj[2];
  public final Obj[] actionMenuBackground = new Obj[9];
  public Obj highlight;
  public final Obj[] targetArrows = new Obj[3];

  /** These three are related to targeting */
  public int _800c697c;
  public int _800c697e;
  public int _800c6980;

  private final BattleHud hud;

  public BattleMenuStruct58(final BattleHud hud) {
    this.hud = hud;
  }

  private void initIconObjs() {
    if(this.actionDisabledObj == null) {
      // "X" icon over attack icon if attack is disabled
      this.actionDisabledObj = buildBattleMenuElement("Action Icon Disabled", 0, 0, 96, 112, 16, 16, 0x19, null);

      // Divine dragoon spirit overlay
      for(int i = 0; i < 2; i++) {
        this.divineSpiritOverlay[i] = buildBattleMenuElement("Action Icon Divine Dragoon Spirit Overlay", 4, 0, 80 + i * 8, 112, 8, 16, 0x98, Translucency.B_PLUS_F);
      }

      for(int iconId = 0; iconId < 9; iconId++) {
        final BattleMenuIconMetrics08 iconMetrics = battleMenuIconMetrics_800fb674[iconId];

        final int iconClutOffset = iconMetrics.clutOffset_04;

        for(int iconState = 0; iconState < 3; iconState++) {
          final int vOffset = battleMenuIconVOffsets_800fb6f4[iconId][iconState];
          final int iconH = battleMenuIconHeights_800fb6bc[iconId][iconState];

          // Combat menu icons
          this.actionIconObj[iconId][iconState] = buildBattleMenuElement("Action Icon " + iconId + " state " + iconState, 0, 0, iconMetrics.u_00, iconMetrics.v_02 + vOffset & 0xff, 16, iconH, iconClutOffset, Translucency.of(iconMetrics.translucencyMode_06));
        }

        // Selected combat menu icon text
        final BattleMenuTextMetrics08 textMetrics = battleMenuTextMetrics_800fb72c[iconId];
        this.actionIconTextObj[iconId] = buildBattleMenuElement("Action Icon Text " + iconId, -textMetrics.w_04 / 2 + 8, -24, textMetrics.u_00, textMetrics.v_02, textMetrics.w_04, 8, textMetrics.clutOffset_06, null);
      }

      for(int spiritId = 0; spiritId < 10; spiritId++) {
        final BattleMenuIconMetrics08 iconMetrics = battleMenuIconMetrics_800fb674[1];

        final int iconClutOffset = dragoonSpiritIconClutOffsets_800c71d0[spiritId];

        for(int iconState = 0; iconState < 3; iconState++) {
          final int vOffset = battleMenuIconVOffsets_800fb6f4[1][iconState];
          final int iconH = battleMenuIconHeights_800fb6bc[1][iconState];

          // Combat menu icons
          this.dragoonIconObj[spiritId][iconState] = buildBattleMenuElement("Spirit Icon " + spiritId + " state " + iconState, 0, 0, iconMetrics.u_00, iconMetrics.v_02 + vOffset & 0xff, 16, iconH, iconClutOffset, Translucency.of(iconMetrics.translucencyMode_06));
        }
      }

      this.actionMenuBackground[0] = buildBattleMenuBackground("Action Background 0", battleMenuBackgroundMetrics_800fb5dc[0], 0, 0, 1, 2, 0x2b, Translucency.B_PLUS_F, battleMenuBackgroundMetrics_800fb5dc[0].uvShiftType_04);

      for(int i = 1; i < 9; i++) {
        this.actionMenuBackground[i] = buildBattleMenuBackground("Action Background " + i, battleMenuBackgroundMetrics_800fb5dc[i], 0, 0, 1, 1, 0x2b, Translucency.B_PLUS_F, battleMenuBackgroundMetrics_800fb5dc[i].uvShiftType_04);
      }

      // Red glow underneath selected menu item
      this.highlight = this.buildBattleMenuSelectionHighlight("Action UI Highlight", battleMenuHighlightMetrics_800c71bc, 0xc, Translucency.B_PLUS_F, 1.0f);

      for(int i = 0; i < this.targetArrows.length; i++) {
        final QuadBuilder builder = new QuadBuilder("Target Arrow " + i)
          .bpp(Bpp.BITS_4)
          .translucency(Translucency.HALF_B_PLUS_HALF_F)
          .vramPos(704, 256)
          .monochrome(0.5f)
          .size(16, 24)
          .uv(240, 0);

        if(i == 0) {
          builder.clut(720, 510);
        } else if(i == 1) {
          builder.clut(720, 511);
        } else if(i == 2) {
          builder.clut(736, 496);
        }

        this.targetArrows[i] = builder.build();
      }
    }
  }

  public void delete() {
    if(this.battleUiItemSpellList != null) {
      this.battleUiItemSpellList.delete();
      this.battleUiItemSpellList = null;
    }

    if(this.battleUiSpellList != null) {
      this.battleUiSpellList.delete();
      this.battleUiSpellList = null;
    }

    if(this.battleUiItemDescription != null) {
      this.battleUiItemDescription.delete();
      this.battleUiItemDescription = null;
    }

    for(int i = 0; i < this.actionIconObj.length; i++) {
      for(int state = 0; state < this.actionIconObj[i].length; state++) {
        if(this.actionIconObj[i][state] != null) {
          this.actionIconObj[i][state].delete();
          this.actionIconObj[i][state] = null;
        }
      }

      if(this.actionIconTextObj[i] != null) {
        this.actionIconTextObj[i].delete();
        this.actionIconTextObj[i] = null;
      }

      if(this.actionMenuBackground[i] != null) {
        this.actionMenuBackground[i].delete();
        this.actionMenuBackground[i] = null;
      }
    }

    for(int i = 0; i < this.dragoonIconObj.length; i++) {
      for(int state = 0; state < this.dragoonIconObj[i].length; state++) {
        if(this.dragoonIconObj[i][state] != null) {
          this.dragoonIconObj[i][state].delete();
          this.dragoonIconObj[i][state] = null;
        }
      }
    }

    if(this.actionDisabledObj != null) {
      this.actionDisabledObj.delete();
      this.actionDisabledObj = null;
    }

    for(int i = 0; i < 2; i++) {
      if(this.divineSpiritOverlay[i] != null) {
        this.divineSpiritOverlay[i].delete();
        this.divineSpiritOverlay[i] = null;
      }
    }

    if(this.highlight != null) {
      this.highlight.delete();
      this.highlight = null;
    }

    for(int i = 0; i < this.targetArrows.length; i++) {
      if(this.targetArrows[i] != null) {
        this.targetArrows[i].delete();
        this.targetArrows[i] = null;
      }
    }
  }

  /**
   * @param type <ol start="0"><li>items</li><li>spells</li></ol>
   */
  @Method(0x800f57f8L)
  private void renderList(final int type) {
    int trim;

    final SpellAndItemMenuA4 menu = spellAndItemMenu_800c6b60;

    int y1 = menu._20;
    final int y2 = menu._1a;
    final int sp68 = menu.y_06;

    //LAB_800f5860
    int charSlot;
    for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      if(dragoonSpells_800c6960.get(charSlot).charIndex_00.get() == menu.charIndex_08) {
        break;
      }
    }

    //LAB_800f58a4
    int sp7c = 0;

    final LodString sp0x18 = new LodString(18);
    final LodString sp0x40 = new LodString(5);
    final LodString itemCount = new LodString(12);

    //LAB_800f58e0
    for(int spellSlot = 0; spellSlot < menu.count_22; spellSlot++) {
      if(y1 >= sp68) {
        break;
      }

      TextColour textColour = TextColour.WHITE;
      final LodString name;
      if(type == 0) {
        //LAB_800f5918
        name = new LodString(combatItems_800c6988.get(sp7c).item.getName());
        intToStr(combatItems_800c6988.get(sp7c).count, itemCount);

        //LAB_800f5968
        int i;
        for(i = 0; ; i++) {
          sp0x18.charAt(i, name.charAt(i));
          if(name.charAt(i) == 0xa0ff) {
            break;
          }
        }

        //LAB_800f5990
        //LAB_800f59a4
        for(; i < 16; i++) {
          sp0x18.charAt(i, 0);
        }

        //LAB_800f59bc
        if(combatItems_800c6988.get(sp7c).count < 10) {
          sp0x18.charAt(i, 0);
          i++;
        }

        //LAB_800f59e8
        sp0x18.charAt(i, 0xa0ff);
        sp0x40.charAt(0, 0xe);

        //LAB_800f5a10
        int n;
        for(n = 0; n < 2; n++) {
          final int chr = itemCount.charAt(n);
          if(chr == 0xa0ff) {
            break;
          }

          sp0x40.charAt(n + 1, chr);
        }

        //LAB_800f5a38
        sp0x40.charAt(n + 1, 0xa0ff);
      } else if(type == 1) {
        //LAB_800f5a4c
        int spellId = dragoonSpells_800c6960.get(charSlot).spellIndex_01.get(spellSlot).get();
        name = new LodString(spellStats_800fa0b8[spellId].name);

        if(menu.charIndex_08 == 8) {
          if(spellId == 65) {
            spellId = 10;
          }

          //LAB_800f5ab4
          if(spellId == 66) {
            spellId = 11;
          }

          //LAB_800f5ac0
          if(spellId == 67) {
            spellId = 12;
          }
        }

        //LAB_800f5acc
        final PlayerBattleEntity bent = setActiveCharacterSpell(spellId);

        // Not enough MP for spell
        if(bent.stats.getStat(CoreMod.MP_STAT.get()).getCurrent() < bent.spell_94.mp_06) {
          textColour = TextColour.GREY;
        }
      } else {
        throw new RuntimeException("Undefined s3");
      }

      //LAB_800f5af0
      if(y1 >= y2) {
        //LAB_800f5b90
        if(sp68 >= y1 + 12) {
          trim = 0;
        } else {
          trim = y1 - (sp68 - 12);
        }

        //LAB_800f5bb4
        if((menu._02 & 0x4) != 0) {
          trim = (short)menu._8c;
        }

        //LAB_800f5bd8
        Scus94491BpeSegment_8002.renderText(name, menu.textX_18, y1, textColour, trim);

        if(type == 0) {
          Scus94491BpeSegment_8002.renderText(sp0x40, menu.textX_18 + 128, y1, textColour, trim);
        }
      } else if(y2 < y1 + 12) {
        if((menu._02 & 0x4) != 0) {
          trim = menu._8c;
        } else {
          trim = y1 - y2;
        }

        //LAB_800f5b40
        Scus94491BpeSegment_8002.renderText(name, menu.textX_18, y2, textColour, trim);

        if(type == 0) {
          Scus94491BpeSegment_8002.renderText(sp0x40, menu.textX_18 + 128, y2, textColour, trim);
        }
      }

      //LAB_800f5c38
      y1 += 14;
      sp7c++;
    }

    //LAB_800f5c64
  }

  /** Draws most elements associated with item and dragoon magic menus.
   * This includes:
   *   - Item and Dragoon magic backgrounds, scroll arrows, and text
   *   - Item and Dragoon magic description background and text
   *   - Dragoon magic MP cost background and normal text (excluding the number value) */
  @Method(0x800f5c94L)
  public void drawItemMenuElements() {
    final SpellAndItemMenuA4 menu = spellAndItemMenu_800c6b60;
    menu.init();
    menu.transforms.identity();

    if(menu.menuState_00 != 0 && (menu._02 & 0x1) != 0) {
      if((menu._02 & 0x2) != 0) {
        //LAB_800f5ee8
        //Item menu
        final int a2 = menu._10 + 6;
        final int a3 = menu._12 + 17;

        if(this.battleUiItemSpellList == null) {
          this.battleUiItemSpellList = new UiBox("Battle UI Item/Spell List", menu.x_04 - a2 / 2, menu.y_06 - a3, a2, a3);
        }

        this.battleUiItemSpellList.render(Config.changeBattleRgb() ? Config.getBattleRgb() : Config.defaultUiColour);

        this.renderList(menu.menuType_0a);

        if((menu._02 & 0x8) != 0) {
          //LAB_800f5d78
          //LAB_800f5d90
          menu.transforms.transfer.set(menu.textX_18 - 16, menu._1a + menu.listScroll_24 * 14 + 2, 124.0f);
          RENDERER.queueOrthoOverlayModel(menu.unknownObj1[menu._84], menu.transforms);

          final int s0;
          if(menu.menuType_0a != 0) {
            s0 = 0;
          } else {
            s0 = 26;
          }

          //LAB_800f5e00
          final int s1;
          if((menu._02 & 0x100) != 0) {
            s1 = 2;
          } else {
            s1 = 0;
          }

          //LAB_800f5e18
          final int t0;
          if((menu._02 & 0x200) != 0) {
            t0 = -2;
          } else {
            t0 = 0;
          }

          //LAB_800f5e24
          if(menu.listIndex_1e > 0) {
            menu.transforms.transfer.set(menu.x_04 + s0 + 56, menu.y_06 + t0 - 100, 124.0f);
            RENDERER.queueOrthoOverlayModel(menu.upArrow, menu.transforms);
          }

          //LAB_800f5e7c
          if(menu.listIndex_1e + 6 < menu.count_22 - 1) {
            menu.transforms.transfer.set(menu.x_04 + s0 + 56, menu.y_06 + s1 - 7, 124.0f);
            RENDERER.queueOrthoOverlayModel(menu.downArrow, menu.transforms);
          }
        }
      }

      //LAB_800f5f50
      if((menu._02 & 0x40) != 0) {
        final int textType;
        if(menu.menuType_0a == 0) { // Item
          //LAB_800f5f8c
          textType = 4;
        } else if(menu.menuType_0a == 1) { // Spell
          //LAB_800f5f94
          textType = 5;
          if((menu._02 & 0x2) != 0) {
            final BattleEntity27c bent = setActiveCharacterSpell(menu.itemOrSpellId_1c);
            this.hud.addFloatingNumber(0, 1, 0, bent.spell_94.mp_06, 280, 135, 0, 1);

            menu.transforms.transfer.set(236 - centreScreenX_1f8003dc, 130 - centreScreenY_1f8003de, 124.0f);
            RENDERER.queueOrthoOverlayModel(menu.unknownObj2, menu.transforms);

            if(this.battleUiSpellList == null) {
              this.battleUiSpellList = new UiBox("Battle UI Spell List", 236, 130, 64, 14);
            }

            this.battleUiSpellList.render(Config.changeBattleRgb() ? Config.getBattleRgb() : Config.defaultUiColour);
          }
        } else {
          throw new RuntimeException("Undefined s1");
        }

        //LAB_800f604c
        //LAB_800f6050
        //Selected item description
        if(this.battleUiItemDescription == null) {
          this.battleUiItemDescription = new UiBox("Battle UI Item Description", 44, 156, 232, 14);
        }

        this.battleUiItemDescription.render(Config.changeBattleRgb() ? Config.getBattleRgb() : Config.defaultUiColour);
        this.renderText(textType, menu.itemOrSpellId_1c, 160, 163);
      }
    }

    //LAB_800f6088
  }

  @Method(0x800f60acL)
  public void clear() {
    this.state_00 = 0;
    this.highlightState_02 = 0;
    this.charIndex_04 = 0xff;
    this.x_06 = 0;
    this.y_08 = 0;
    this.xShiftOffset_0a = 0;
    this.iconCount_0e = 0;
    this.selectedIcon_22 = 0;
    this.currentIconStateTick_24 = 0;
    this.iconStateIndex_26 = 0;
    this.highlightX0_28 = 0;
    this.highlightY_2a = 0;
    this.colour_2c = 0;

    //LAB_800f60fc
    for(int i = 0; i < 9; i++) {
      this.iconFlags_10[i] = -1;
    }

    //LAB_800f611c
    this.countHighlightMovementStep_30 = 0;
    this.highlightMovementDistance_34 = 0;
    this.currentHighlightMovementStep_38 = 0;
    this.highlightX1_3c = 0;
    this.renderSelectedIconText_40 = false;
    this.cameraPositionSwitchTicksRemaining_44 = 0;
    this.target_48 = 0;
    this.displayTargetArrowAndName_4c = false;
    this.targetType_50 = 0;
    this.combatantIndex_54 = 0;
  }

  @Method(0x800f6134L)
  public void initializeMenuIcons(final ScriptState<? extends BattleEntity27c> bentState, final int displayedIconsBitset, final int disabledIconsBitset) {
    this.initIconObjs();

    this.state_00 = 1;
    this.highlightState_02 = 2;
    this.x_06 = 160;
    this.y_08 = 172;
    this.selectedIcon_22 = 0;
    this.currentIconStateTick_24 = 0;
    this.iconStateIndex_26 = 0;
    this.highlightX0_28 = 0;
    this.highlightY_2a = 0;
    this.colour_2c = 128;

    //LAB_800f61d8
    for(int i = 0; i < 9; i++) {
      this.iconFlags_10[i] = -1;
    }

    //LAB_800f61f8
    this.countHighlightMovementStep_30 = 0;
    this.highlightMovementDistance_34 = 0;
    this.currentHighlightMovementStep_38 = 0;
    this.highlightX1_3c = 0;
    this.renderSelectedIconText_40 = false;
    this.cameraPositionSwitchTicksRemaining_44 = 0;
    this.target_48 = 0;
    this.displayTargetArrowAndName_4c = false;
    this.targetType_50 = 0;
    this.combatantIndex_54 = 0;

    //LAB_800f6224
    //LAB_800f6234
    int bentIndex;
    for(bentIndex = 0; bentIndex < charCount_800c677c.get(); bentIndex++) {
      if(battleState_8006e398.charBents_e40[bentIndex] == bentState) {
        break;
      }
    }

    //LAB_800f6254
    this.iconCount_0e = 0;
    this.charIndex_04 = (short)battleState_8006e398.charBents_e40[bentIndex].innerStruct_00.charId_272;

    //LAB_800f62a4
    for(int i = 0, used = 0; i < 8; i++) {
      if((displayedIconsBitset & 0x1 << i) != 0) {
        this.iconFlags_10[used++] = iconFlags_800c7194[i];
        this.iconCount_0e++;
      }
      //LAB_800f62d0
    }

    this.xShiftOffset_0a = (short)((this.iconCount_0e * 19 - 3) / 2);
    this.setDisabledIcons(disabledIconsBitset);
  }

  /** Handles the various combat menu actions and then renders the menu:
   * <ol>
   *   <li>0 -> Set up camera positions</li>
   *   <li>1 -> Check for and handle input</li>
   *   <li>2 -> Cycle selector to adjacent icons</li>
   *   <li>3 -> Wrap selector to other end of menu</li>
   *   <li>4 -> Count down camera movement ticks</li>
   * </ol>
   */
  @Method(0x800f6330L)
  public int tickAndRender() {
    if(this.state_00 == 0) {
      return 0;
    }

    int selectedAction = 0;

    switch(this.state_00 - 1) {
      case 0 -> {  // Set up camera position list at battle start or camera reset (like dragoon or after trying to run)
        this.state_00 = 2;
        this.highlightX0_28 = (short)(this.x_06 - this.xShiftOffset_0a + this.selectedIcon_22 * 19 - 4);
        this.highlightY_2a = (short)(this.y_08 - 22);

        //LAB_800f63e8
        this.countHighlightMovementStep_30 = 0;
        this.highlightMovementDistance_34 = 0;
        this.currentHighlightMovementStep_38 = 0;
        this.highlightX1_3c = 0;
        this.renderSelectedIconText_40 = false;
        this.cameraPositionSwitchTicksRemaining_44 = 0;
        this.target_48 = 0;
        this.displayTargetArrowAndName_4c = false;
        this.targetType_50 = 0;
        this.combatantIndex_54 = 0;

        this._800c697c = 0;
        currentCameraPositionIndicesIndicesIndex_800c6ba1.set(0);
        countCameraPositionIndicesIndices_800c6ba0.set(0);

        //LAB_800f6424
        final int[] previousIndicesList = new int[4];
        for(int i = 0; i < 4; i++) {
          previousIndicesList[i] = 0xff;
          cameraPositionIndicesIndices_800c6c30.get(i).set(0);
        }

        //LAB_800f6458
        int cameraPositionIndicesIndex;
        boolean addCameraPositionIndex;
        int cameraPositionIndex;
        for(cameraPositionIndicesIndex = 0; cameraPositionIndicesIndex < 4; cameraPositionIndicesIndex++) {
          addCameraPositionIndex = true;
          cameraPositionIndex = currentStageData_800c6718.cameraPosIndices_18[cameraPositionIndicesIndex];

          //LAB_800f646c
          for(int i = 0; i < 4; i++) { // don't add duplicate indices
            if(previousIndicesList[i] == cameraPositionIndex) {
              addCameraPositionIndex = false;
              break;
            }
            //LAB_800f6480
          }

          if(addCameraPositionIndex) {
            previousIndicesList[countCameraPositionIndicesIndices_800c6ba0.get()] = currentStageData_800c6718.cameraPosIndices_18[cameraPositionIndicesIndex];
            cameraPositionIndicesIndices_800c6c30.get(countCameraPositionIndicesIndices_800c6ba0.get()).set(cameraPositionIndicesIndex);

            if(currentCameraPositionIndicesIndex_800c66b0.get() == cameraPositionIndicesIndex) {
              currentCameraPositionIndicesIndicesIndex_800c6ba1.set(countCameraPositionIndicesIndices_800c6ba0.get());
            }

            //LAB_800f64dc
            countCameraPositionIndicesIndices_800c6ba0.add(1);
          }
          //LAB_800f64ec
        }
      }

      case 1 -> {  // Checking for input
        final int countCameraPositionIndicesIndices = countCameraPositionIndicesIndices_800c6ba0.get();
        this.renderSelectedIconText_40 = false;
        this.cameraPositionSwitchTicksRemaining_44 = 0;

        // Input for changing camera angles
        if(countCameraPositionIndicesIndices >= 2 && (input_800bee90 & 0x2) != 0) {
          currentCameraPositionIndicesIndicesIndex_800c6ba1.add(1);
          if(currentCameraPositionIndicesIndicesIndex_800c6ba1.get() >= countCameraPositionIndicesIndices) {
            currentCameraPositionIndicesIndicesIndex_800c6ba1.set(0);
          }

          //LAB_800f6560
          _800c6748.set(33);
          this.state_00 = 5;
          currentCameraPositionIndicesIndex_800c66b0.set(cameraPositionIndicesIndices_800c6c30.get(currentCameraPositionIndicesIndicesIndex_800c6ba1.get()).get());
          this.cameraPositionSwitchTicksRemaining_44 = 60 / vsyncMode_8007a3b8 + 2;
          this.toggleHighlight(false);
          break;
        }

        // Input for cycling right on menu bar
        //LAB_800f65b8
        if((input_800bee90 & 0x2000) != 0) {
          playSound(0, 1, 0, 0, (short)0, (short)0);

          if(this.selectedIcon_22 < this.iconCount_0e - 1) {
            //LAB_800f6640
            this.selectedIcon_22++;
            this.state_00 = 3;

            //LAB_800f664c
            this.countHighlightMovementStep_30 = 3;
            this.highlightMovementDistance_34 = 19;
            this.currentHighlightMovementStep_38 = 0;
            this.iconStateIndex_26 = 0;
            break;
          }

          this.state_00 = 4;
          this.highlightState_02 |= 1;
          this.selectedIcon_22 = 0;
          this.iconStateIndex_26 = 0;
          this.countHighlightMovementStep_30 = 3;
          this.highlightMovementDistance_34 = 19;
          this.currentHighlightMovementStep_38 = 0;
          this.highlightX1_3c = this.x_06 - this.xShiftOffset_0a - 23;
          break;
        }

        // Input for cycling left on menu bar
        //LAB_800f6664
        if((input_800bee90 & 0x8000) != 0) {
          playSound(0, 1, 0, 0, (short)0, (short)0);

          if(this.selectedIcon_22 != 0) {
            //LAB_800f66f0
            this.selectedIcon_22--;
            this.state_00 = 3;

            //LAB_800f66fc
            this.countHighlightMovementStep_30 = 3;
            this.highlightMovementDistance_34 = -19;

            //LAB_800f6710
            this.currentHighlightMovementStep_38 = 0;
            this.iconStateIndex_26 = 0;
            break;
          }

          this.state_00 = 4;
          this.highlightState_02 |= 1;
          this.selectedIcon_22 = (short)(this.iconCount_0e - 1);
          this.highlightX1_3c = this.x_06 - this.xShiftOffset_0a + this.iconCount_0e * 19 - 4;
          this.countHighlightMovementStep_30 = 3;
          this.highlightMovementDistance_34 = -19;
          this.currentHighlightMovementStep_38 = 0;
          this.iconStateIndex_26 = 0;
          break;
        }

        // Input for pressing X on menu bar
        //LAB_800f671c
        if((press_800bee94 & 0x20) != 0) {
          int selectedIconFlag = this.iconFlags_10[this.selectedIcon_22];
          if((selectedIconFlag & 0x80) != 0) {
            playSound(0, 3, 0, 0, (short)0, (short)0);
          } else {
            selectedIconFlag = selectedIconFlag & 0xf;
            if(selectedIconFlag == 0x5) {
              prepareItemList();

              if(combatItems_800c6988.isEmpty()) {
                playSound(0, 3, 0, 0, (short)0, (short)0);
              } else {
                playSound(0, 2, 0, 0, (short)0, (short)0);
                selectedAction = this.iconFlags_10[this.selectedIcon_22] & 0xf;
              }
              //LAB_800f6790
            } else if(selectedIconFlag == 0x3L) {
              //LAB_800f67b8
              int charSlot;
              for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
                if(dragoonSpells_800c6960.get(charSlot).charIndex_00.get() == this.charIndex_04) {
                  break;
                }
              }

              //LAB_800f67d8
              //LAB_800f67f4
              int spellIndex;
              for(spellIndex = 0; spellIndex < 8; spellIndex++) {
                if(dragoonSpells_800c6960.get(charSlot).spellIndex_01.get(spellIndex).get() != -1) {
                  break;
                }
              }

              //LAB_800f681c
              if(spellIndex == 8) {
                playSound(0, 3, 0, 0, (short)0, (short)0);
              } else {
                playSound(0, 2, 0, 0, (short)0, (short)0);
                selectedAction = this.iconFlags_10[this.selectedIcon_22] & 0xf;
              }
            } else {
              //LAB_800f6858
              //LAB_800f6860
              playSound(0, 2, 0, 0, (short)0, (short)0);
              selectedAction = this.iconFlags_10[this.selectedIcon_22] & 0xf;
            }
          }
          //LAB_800f6898
          // Input for pressing circle on menu bar
        } else if((press_800bee94 & 0x40) != 0) {
          //LAB_800f68a4
          //LAB_800f68bc
          playSound(0, 3, 0, 0, (short)0, (short)0);
        }

        //LAB_800f68c4
        //LAB_800f68c8
        this.renderSelectedIconText_40 = true;
      }

      case 2 -> {  // Cycle to adjacent menu bar icon
        this.currentHighlightMovementStep_38++;
        this.highlightX0_28 += (short)(this.highlightMovementDistance_34 / this.countHighlightMovementStep_30);

        if(this.currentHighlightMovementStep_38 >= this.countHighlightMovementStep_30) {
          this.state_00 = 2;
          this.countHighlightMovementStep_30 = 0;
          this.highlightMovementDistance_34 = 0;
          this.currentHighlightMovementStep_38 = 0;
          this.highlightX0_28 = (short)(this.x_06 - this.xShiftOffset_0a + this.selectedIcon_22 * 19 - 4);
          this.highlightY_2a = (short)(this.y_08 - 22);
        }
      }

      case 3 -> {  // Wrap menu bar icon
        this.currentHighlightMovementStep_38++;
        this.highlightX0_28 += (short)(this.highlightMovementDistance_34 / this.countHighlightMovementStep_30);
        this.highlightX1_3c += this.highlightMovementDistance_34 / this.countHighlightMovementStep_30;
        this.colour_2c += (short)(0x80 / this.countHighlightMovementStep_30);

        if(this.currentHighlightMovementStep_38 >= this.countHighlightMovementStep_30) {
          this.state_00 = 2;
          this.colour_2c = 0x80;
          this.currentHighlightMovementStep_38 = 0;
          this.highlightMovementDistance_34 = 0;
          this.countHighlightMovementStep_30 = 0;
          this.highlightX0_28 = (short)(this.x_06 - this.xShiftOffset_0a + this.selectedIcon_22 * 19 - 4);
          this.highlightY_2a = (short)(this.y_08 - 22);
          this.highlightState_02 &= 0xfffe;
        }
      }

      case 4 -> {  // Seems to be related to switching camera views
        this.cameraPositionSwitchTicksRemaining_44--;
        if(this.cameraPositionSwitchTicksRemaining_44 == 1) {
          this.toggleHighlight(true);
          this.state_00 = 2;
        }
      }
    }

    //LAB_800f6a88
    //LAB_800f6a8c
    this.currentIconStateTick_24++;
    if(this.currentIconStateTick_24 >= 4) {
      this.currentIconStateTick_24 = 0;
      this.iconStateIndex_26++;
      if(this.iconStateIndex_26 >= 4) {
        this.iconStateIndex_26 = 0;
      }
    }

    //LAB_800f6ae0
    battleMenu_800c6c34.renderActionMenu();

    //LAB_800f6aec
    return selectedAction;
  }

  @Method(0x800f6b04L)
  public void renderActionMenu() {
    if(this.state_00 != 0 && (this.highlightState_02 & 0x2) != 0) {
      //LAB_800f704c
      final int variableW = this.iconCount_0e * 19 + 1;
      int x = this.x_06 - variableW / 2;
      int y = this.y_08 - 10;
      this.transforms.scaling(variableW, 1.0f, 1.0f);
      this.transforms.transfer.set(x, y, 124.0f);
      RENDERER.queueOrthoOverlayModel(this.actionMenuBackground[0], this.transforms);

      final int[][] battleMenuBaseCoords = new int[4][2];

      battleMenuBaseCoords[0][0] = x;
      battleMenuBaseCoords[2][0] = x;
      x = x + variableW;
      battleMenuBaseCoords[1][0] = x;
      battleMenuBaseCoords[3][0] = x;
      battleMenuBaseCoords[0][1] = y;
      battleMenuBaseCoords[1][1] = y;
      y = this.y_08 - 8;
      battleMenuBaseCoords[2][1] = y;
      battleMenuBaseCoords[3][1] = y;

      //LAB_800f710c
      for(int i = 0; i < 8; i++) {
        final BattleMenuBackgroundDisplayMetrics0c displayMetrics = battleMenuBackgroundDisplayMetrics_800fb614[i];
        x = battleMenuBaseCoords[displayMetrics.vertexBaseOffsetIndex_00][0] + displayMetrics.vertexXMod_02;
        y = battleMenuBaseCoords[displayMetrics.vertexBaseOffsetIndex_00][1] + displayMetrics.vertexYMod_04;

        final int w;
        if(displayMetrics.w_06 != 0) {
          w = displayMetrics.w_06;
        } else {
          w = variableW;
        }

        //LAB_800f7158
        final int h;
        if(displayMetrics.h_08 == 0) {
          h = 2;
        } else {
          h = displayMetrics.h_08;
        }

        //LAB_800f716c
        this.transforms.scaling(w, h, 1.0f);
        this.transforms.transfer.set(x, y, 124.0f);
        RENDERER.queueOrthoOverlayModel(this.actionMenuBackground[i + 1], this.transforms);
      }

      this.transforms.identity();

      //LAB_800f6fc8
      // Draw red glow underneath selected menu item
      this.transforms.transfer.set(this.highlightX0_28, this.highlightY_2a, 124.0f);
      RENDERER.queueOrthoOverlayModel(this.highlight, this.transforms)
        .monochrome(this.colour_2c / 255.0f);

      if((this.highlightState_02 & 0x1) != 0) {
        this.transforms.transfer.set(this.highlightX1_3c, this.highlightY_2a, 124.0f);
        RENDERER.queueOrthoOverlayModel(this.highlight, this.transforms)
          .monochrome(Math.max(0, (0x80 - this.colour_2c) / 255.0f));
      }

      //LAB_800f6c48
      for(int iconIndex = 0; iconIndex < this.iconCount_0e; iconIndex++) {
        final int iconId = (this.iconFlags_10[iconIndex] & 0xf) - 1;
        final int iconState;
        if(this.selectedIcon_22 == iconIndex) {
          iconState = battleMenuIconStates_800c71e4[this.iconStateIndex_26];
        } else {
          //LAB_800f6c88
          iconState = 0;
        }

        //LAB_800f6c90
        final int menuElementBaseX = this.x_06 - this.xShiftOffset_0a + iconIndex * 19;
        final int menuElementBaseY = this.y_08 - battleMenuIconHeights_800fb6bc[iconId][iconState];

        if(this.selectedIcon_22 == iconIndex && this.renderSelectedIconText_40) {
          // Selected combat menu icon text
          this.transforms.transfer.set(menuElementBaseX, this.y_08, 124.0f);
          RENDERER.queueOrthoOverlayModel(this.actionIconTextObj[iconId], this.transforms);
        }

        // Combat menu icons
        //LAB_800f6d70
        this.transforms.transfer.set(menuElementBaseX, menuElementBaseY, 124.0f);

        if((this.iconFlags_10[iconIndex] & 0xf) != 0x2) {
          RENDERER.queueOrthoOverlayModel(this.actionIconObj[iconId][iconState], this.transforms);
        } else if(this.charIndex_04 != 0 || (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 == 0) {
          RENDERER.queueOrthoOverlayModel(this.dragoonIconObj[this.charIndex_04][iconState], this.transforms);
        } else {
          RENDERER.queueOrthoOverlayModel(this.dragoonIconObj[9][iconState], this.transforms);

          if(iconState != 0) {
            // Divine dragoon spirit overlay
            //LAB_800f6de0
            RENDERER.queueOrthoOverlayModel(this.divineSpiritOverlay[iconState - 1], this.transforms);
          }
        }

        if((this.iconFlags_10[iconIndex] & 0x80) != 0) {
          this.transforms.transfer.set(menuElementBaseX, this.y_08 - 16, 124.0f);
          RENDERER.queueOrthoOverlayModel(this.actionDisabledObj, this.transforms);
        }

        //LAB_800f6fa4
      }
    }
    //LAB_800f71e0
  }

  @Method(0x800f7210L)
  private Obj buildBattleMenuSelectionHighlight(final String name, final BattleMenuHighlightMetrics12 highlightMetrics, final int clutOffset, @Nullable final Translucency transparencyMode, final float colour) {
    //LAB_800f7294
    final QuadBuilder builder = new QuadBuilder(name)
      .monochrome(colour)
      .pos(highlightMetrics.xBase_00, highlightMetrics.yBase_02, 0.0f)
      .posSize(highlightMetrics.w_04, highlightMetrics.h_06);

    final int uvShiftType = highlightMetrics.uvShiftType_10;
    if(uvShiftType == 0) {
      //LAB_800f7360
      builder
        .uv(highlightMetrics.u_08, highlightMetrics.v_0a)
        .uvSize(highlightMetrics.uvW_0c, highlightMetrics.uvH_0e);
    } else if(uvShiftType == 1) {
      //LAB_800f738c
      builder
        .uv(highlightMetrics.u_08, highlightMetrics.v_0a - 1)
        .uvSize(highlightMetrics.uvW_0c, highlightMetrics.uvH_0e);
      //LAB_800f7344
    } else if(uvShiftType == 2) {
      //LAB_800f73b8
      builder
        .uv(highlightMetrics.u_08 - 1, highlightMetrics.v_0a)
        .uvSize(highlightMetrics.uvW_0c, highlightMetrics.uvH_0e);
    } else if(uvShiftType == 3) {
      //LAB_800f740c
      builder
        .uv(highlightMetrics.u_08 - 1, highlightMetrics.v_0a - 1)
        .uvSize(highlightMetrics.uvW_0c, highlightMetrics.uvH_0e);
    }

    //LAB_800f745c
    //LAB_800f7460
    //LAB_800f746c
    final int clutX = 704 + clutOffset & 0x3f0;
    final int clutY = 496 + clutOffset % 16;

    builder
      .bpp(Bpp.BITS_4)
      .clut(clutX, clutY)
      .vramPos(704, 256);

    if(transparencyMode != null) {
      builder.translucency(transparencyMode);
    }

    return builder.build();
  }

  /**
   * @param targetType 0: chars, 1: monsters, 2: all
   */
  @Method(0x800f7768L)
  public int handleTargeting(final int targetType, final boolean targetAll) {
    final int count;
    short t3 = 1;

    if(targetType == 1) {
      this.displayTargetArrowAndName_4c = true;
      //LAB_800f77d4
      count = aliveMonsterCount_800c6758.get();

      //LAB_800f77e8
      this._800c697c = this._800c697e;
    } else {
      this.displayTargetArrowAndName_4c = true;
      if(targetType == 0) {
        this._800c697c = this._800c6980;
        count = charCount_800c677c.get();
      } else {
        //LAB_800f77f0
        count = aliveBentCount_800c669c.get();
      }
    }

    //LAB_800f77f4
    if((press_800bee94 & 0x3000) != 0) {
      this._800c697c++;
      if(this._800c697c >= count) {
        this._800c697c = 0;
      }
    }

    //LAB_800f7830
    if((press_800bee94 & 0xc000) != 0) {
      this._800c697c--;
      if(this._800c697c < 0) {
        this._800c697c = count - 1;
      }
      t3 = -1;
    }

    //LAB_800f786c
    //LAB_800f7880
    if(this._800c697c < 0 || this._800c697c >= count) {
      //LAB_800f78a0
      this._800c697c = 0;
      t3 = 1;
    }

    //LAB_800f78ac
    //LAB_800f78d4
    int v1;
    ScriptState<BattleEntity27c> target = null;
    for(v1 = 0; v1 < count; v1++) {
      target = targetBents_800c71f0[targetType][this._800c697c];

      if(target != null && (target.storage_44[7] & 0x4000) == 0) {
        break;
      }

      this._800c697c += t3;

      if(this._800c697c >= count) {
        this._800c697c = 0;
      }

      //LAB_800f792c
      if(this._800c697c < 0) {
        this._800c697c = count - 1;
      }

      //LAB_800f7948
    }

    //LAB_800f7960
    if(v1 == count) {
      target = targetBents_800c71f0[targetType][this._800c697c];
      this._800c697c = 0;
    }

    //LAB_800f7998
    this.targetType_50 = targetType;
    if(!targetAll) {
      this.combatantIndex_54 = this._800c697c;
    } else {
      //LAB_800f79b4
      this.combatantIndex_54 = -1;
    }

    //LAB_800f79bc
    this.target_48 = target.index;

    if(targetType == 1) {
      //LAB_800f79fc
      this._800c697e = this._800c697c;
    } else if(targetType == 0) {
      this._800c6980 = this._800c697c;
    }

    //LAB_800f7a0c
    //LAB_800f7a10
    int ret = 0;
    if((press_800bee94 & 0x20) != 0) { // Cross
      ret = 1;
      this._800c697c = 0;
      this.displayTargetArrowAndName_4c = false;
    }

    //LAB_800f7a38
    if((press_800bee94 & 0x40) != 0) { // Circle
      ret = -1;
      this._800c697c = 0;
      this.target_48 = -1;
      this.displayTargetArrowAndName_4c = false;
    }

    //LAB_800f7a68
    return ret;
  }

  /**
   * @param textType <ol start="0">
   *                   <li>Player names</li>
   *                   <li>Player names</li>
   *                   <li>Combat item names</li>
   *                   <li>Dragoon spells</li>
   *                   <li>Item descriptions</li>
   *                   <li>Spell descriptions</li>
   *                 </ol>
   */
  @Method(0x800f8ac4L)
  private void renderText(final int textType, final int textIndex, final int x, final int y) {
    final LodString str;
    if(textType == 4) {
      str = new LodString(itemStats_8004f2ac[textIndex].combatDescription);
    } else if(textType == 5) {
      str = new LodString(spellStats_800fa0b8[textIndex].combatDescription);
    } else {
      throw new IllegalArgumentException("Only supports textType 4/5");
    }

    final BattleDescriptionEvent event = EVENTS.postEvent(new BattleDescriptionEvent(textType, textIndex, str));
    Scus94491BpeSegment_8002.renderText(event.string, x - textWidth(event.string) / 2, y - 6, TextColour.WHITE, 0);
  }

  @Method(0x800f8b74L)
  public void setDisabledIcons(final int disabledIconBitset) {
    //LAB_800f8bd8
    for(int i = 0; i < 8; i++) {
      if((disabledIconBitset & 0x1 << i) != 0) {
        //LAB_800f8bf4
        for(int icon = 0; icon < 8; icon++) {
          if((this.iconFlags_10[icon] & 0xf) == iconFlags_800c7194[i]) {
            this.iconFlags_10[icon] |= 0x80;
            break;
          }
        }
      }
    }
  }

  @Method(0x800f8c38L)
  public void toggleHighlight(final boolean render) {
    if(this.state_00 != 0) {
      //LAB_800f8c78
      if(!render || this.cameraPositionSwitchTicksRemaining_44 != 0) {
        //LAB_800f8c64
        this.highlightState_02 &= 0xfffd;
      } else {
        this.highlightState_02 |= 0x2;
      }
    }
    //LAB_800f8c98
  }
}
