package legend.game.combat.ui;

import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.combat.environment.BattleMenuIconMetrics08;
import legend.game.combat.environment.BattleMenuTextMetrics08;
import legend.game.types.Translucency;

import static legend.game.combat.Bttl_800c.battleMenuBackgroundMetrics_800fb5dc;
import static legend.game.combat.Bttl_800c.battleMenuHighlightMetrics_800c71bc;
import static legend.game.combat.Bttl_800c.battleMenuIconHeights_800fb6bc;
import static legend.game.combat.Bttl_800c.battleMenuIconMetrics_800fb674;
import static legend.game.combat.Bttl_800c.battleMenuIconVOffsets_800fb6f4;
import static legend.game.combat.Bttl_800c.battleMenuTextMetrics_800fb72c;
import static legend.game.combat.Bttl_800c.dragoonSpiritIconClutOffsets_800c71d0;
import static legend.game.combat.Bttl_800f.buildBattleMenuBackground;
import static legend.game.combat.Bttl_800f.buildBattleMenuElement;
import static legend.game.combat.Bttl_800f.buildBattleMenuSelectionHighlight;

public class BattleMenuStruct58 {
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

  public void initIcons() {
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
      this.highlight = buildBattleMenuSelectionHighlight("Action UI Highlight", battleMenuHighlightMetrics_800c71bc, 0xc, Translucency.B_PLUS_F, 1.0f);

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
}
