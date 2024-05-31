package legend.game.combat.ui;

import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.environment.BattleItemMenuArrowUvMetrics06;
import legend.game.combat.environment.BattleMenuHighlightMetrics12;
import legend.game.combat.environment.BattleMenuIconMetrics08;
import legend.game.combat.environment.BattleMenuTextMetrics08;
import legend.game.types.Translucency;

import javax.annotation.Nullable;

import java.util.Arrays;

import static legend.game.combat.ui.BattleHud.battleMenuIconHeights_800fb6bc;

public class BattleMenuStruct58 {
  private static final BattleMenuHighlightMetrics12 battleMenuHighlightMetrics_800c71bc = new BattleMenuHighlightMetrics12(0, 0, 24, 24, 232, 120, 23, 23, 0);
  private static final int[] dragoonSpiritIconClutOffsets_800c71d0 = {152, 153, 154, 155, 156, 153, 157, 158, 154, 159};

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

  public short state_00;
  /** 0 = don't render, 1 = wrapping, 2 = render normally */
  public int highlightState_02;
  public PlayerBattleEntity player_04;
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

  @Method(0x800f60acL)
  public void clear() {
    this.state_00 = 0;
    this.highlightState_02 = 0;
    this.player_04 = null;
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

    this._800c697e = 0;
    this._800c6980 = 0;
  }

  public void initIconObjs() {
    if(this.actionDisabledObj == null) {
      // "X" icon over attack icon if attack is disabled
      this.actionDisabledObj = this.hud.buildBattleMenuElement("Action Icon Disabled", 0, 0, 96, 112, 16, 16, 0x19, null);

      // Divine dragoon spirit overlay
      for(int i = 0; i < 2; i++) {
        this.divineSpiritOverlay[i] = this.hud.buildBattleMenuElement("Action Icon Divine Dragoon Spirit Overlay", 4, 0, 80 + i * 8, 112, 8, 16, 0x98, Translucency.B_PLUS_F);
      }

      for(int iconId = 0; iconId < 9; iconId++) {
        final BattleMenuIconMetrics08 iconMetrics = battleMenuIconMetrics_800fb674[iconId];

        final int iconClutOffset = iconMetrics.clutOffset_04;

        for(int iconState = 0; iconState < 3; iconState++) {
          final int vOffset = battleMenuIconVOffsets_800fb6f4[iconId][iconState];
          final int iconH = battleMenuIconHeights_800fb6bc[iconId][iconState];

          // Combat menu icons
          this.actionIconObj[iconId][iconState] = this.hud.buildBattleMenuElement("Action Icon " + iconId + " state " + iconState, 0, 0, iconMetrics.u_00, iconMetrics.v_02 + vOffset & 0xff, 16, iconH, iconClutOffset, Translucency.of(iconMetrics.translucencyMode_06));
        }

        // Selected combat menu icon text
        final BattleMenuTextMetrics08 textMetrics = battleMenuTextMetrics_800fb72c[iconId];
        this.actionIconTextObj[iconId] = this.hud.buildBattleMenuElement("Action Icon Text " + iconId, -textMetrics.w_04 / 2 + 8, -24, textMetrics.u_00, textMetrics.v_02, textMetrics.w_04, 8, textMetrics.clutOffset_06, null);
      }

      for(int spiritId = 0; spiritId < 10; spiritId++) {
        final BattleMenuIconMetrics08 iconMetrics = battleMenuIconMetrics_800fb674[1];

        final int iconClutOffset = dragoonSpiritIconClutOffsets_800c71d0[spiritId];

        for(int iconState = 0; iconState < 3; iconState++) {
          final int vOffset = battleMenuIconVOffsets_800fb6f4[1][iconState];
          final int iconH = battleMenuIconHeights_800fb6bc[1][iconState];

          // Combat menu icons
          this.dragoonIconObj[spiritId][iconState] = this.hud.buildBattleMenuElement("Spirit Icon " + spiritId + " state " + iconState, 0, 0, iconMetrics.u_00, iconMetrics.v_02 + vOffset & 0xff, 16, iconH, iconClutOffset, Translucency.of(iconMetrics.translucencyMode_06));
        }
      }

      this.actionMenuBackground[0] = this.hud.buildBattleMenuBackground("Action Background 0", battleMenuBackgroundMetrics_800fb5dc[0], 0, 0, 1, 2, 0x2b, Translucency.B_PLUS_F, battleMenuBackgroundMetrics_800fb5dc[0].uvShiftType_04);

      for(int i = 1; i < 9; i++) {
        this.actionMenuBackground[i] = this.hud.buildBattleMenuBackground("Action Background " + i, battleMenuBackgroundMetrics_800fb5dc[i], 0, 0, 1, 1, 0x2b, Translucency.B_PLUS_F, battleMenuBackgroundMetrics_800fb5dc[i].uvShiftType_04);
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

  public boolean isIconEnabled(int value) {
    return Arrays.stream(this.iconFlags_10, 0, this.iconFlags_10.length)
      .anyMatch(i -> i == value);
  }

  public int retrieveIconEnabled(int... values) {
    return Arrays.stream(this.iconFlags_10, 0, this.iconFlags_10.length)
      .filter(i -> Arrays.stream(values).anyMatch(value -> value == i))
      .findFirst()
      .orElse(0);
  }
}
