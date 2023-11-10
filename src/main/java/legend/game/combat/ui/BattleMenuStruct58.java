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
  public final short[] iconFlags_10 = new short[9];
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
        final BattleMenuIconMetrics08 iconMetrics = battleMenuIconMetrics_800fb674.get(iconId);

        final int iconClutOffset = battleMenuIconMetrics_800fb674.get(iconId).clutOffset_04.get();

        for(int iconState = 0; iconState < 3; iconState++) {
          final int vOffset = battleMenuIconVOffsets_800fb6f4.get(iconId).get(iconState).get();
          final int iconH = battleMenuIconHeights_800fb6bc.get(iconId).get(iconState).get();

          // Combat menu icons
          this.actionIconObj[iconId][iconState] = buildBattleMenuElement("Action Icon " + iconId + " state " + iconState, 0, 0, iconMetrics.u_00.get(), iconMetrics.v_02.get() + vOffset & 0xff, 16, iconH, iconClutOffset, Translucency.of(iconMetrics.translucencyMode_06.get()));
        }

        // Selected combat menu icon text
        final BattleMenuTextMetrics08 textMetrics = battleMenuTextMetrics_800fb72c.get(iconId);
        this.actionIconTextObj[iconId] = buildBattleMenuElement("Action Icon Text " + iconId, -textMetrics.w_04.get() / 2 + 8, -24, textMetrics.u_00.get(), textMetrics.v_02.get(), textMetrics.w_04.get(), 8, textMetrics.clutOffset_06.get(), null);
      }

      for(int spiritId = 0; spiritId < 10; spiritId++) {
        final BattleMenuIconMetrics08 iconMetrics = battleMenuIconMetrics_800fb674.get(1);

        final int iconClutOffset = dragoonSpiritIconClutOffsets_800c71d0.get(spiritId).get();

        for(int iconState = 0; iconState < 3; iconState++) {
          final int vOffset = battleMenuIconVOffsets_800fb6f4.get(1).get(iconState).get();
          final int iconH = battleMenuIconHeights_800fb6bc.get(1).get(iconState).get();

          // Combat menu icons
          this.dragoonIconObj[spiritId][iconState] = buildBattleMenuElement("Spirit Icon " + spiritId + " state " + iconState, 0, 0, iconMetrics.u_00.get(), iconMetrics.v_02.get() + vOffset & 0xff, 16, iconH, iconClutOffset, Translucency.of(iconMetrics.translucencyMode_06.get()));
        }
      }

      this.actionMenuBackground[0] = buildBattleMenuBackground("Action Background 0", battleMenuBackgroundMetrics_800fb5dc.get(0), 0, 0, 1, 2, 0x2b, Translucency.B_PLUS_F, battleMenuBackgroundMetrics_800fb5dc.get(0).uvShiftType_04.get());

      for(int i = 1; i < 9; i++) {
        this.actionMenuBackground[i] = buildBattleMenuBackground("Action Background " + i, battleMenuBackgroundMetrics_800fb5dc.get(i), 0, 0, 1, 1, 0x2b, Translucency.B_PLUS_F, battleMenuBackgroundMetrics_800fb5dc.get(i).uvShiftType_04.get());
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
        this.actionIconObj[i][state].delete();
      }

      this.actionIconTextObj[i].delete();
      this.actionMenuBackground[i].delete();
    }

    for(int i = 0; i < this.dragoonIconObj.length; i++) {
      for(int state = 0; state < this.dragoonIconObj[i].length; state++) {
        this.dragoonIconObj[i][state].delete();
      }
    }

    this.actionDisabledObj.delete();

    for(int i = 0; i < 2; i++) {
      this.divineSpiritOverlay[i].delete();
    }

    this.highlight.delete();

    for(int i = 0; i < this.targetArrows.length; i++) {
      this.targetArrows[i].delete();
    }
  }
}
