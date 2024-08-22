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

  public static final BattleMenuIconMetrics08[] battleMenuIconMetrics_800fb674 = {
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
   *   <li>Lower 4 bits are selected action - defend, transform, d-magic, attack, item, run, special, ?, d-attack</li>
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
  public Obj menuObj;
  public int actionDisabledObjOffset;
  public int divineSpiritObjOffset;
  public int actionIconObjOffset;
  public int actionIconTextObjOffset;
  public int actionDragoonIconObjOffset;
  public int actionMenuBackgroundObjOffset;
  public int highlightObjOffset;
  public int targetArrowsObjOffset;

  public int targetedSlot_800c697c;
  public int targetedMonsterSlot_800c697e;
  public int targetedPlayerSlot_800c6980;

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

    this.targetedMonsterSlot_800c697e = 0;
    this.targetedPlayerSlot_800c6980 = 0;
  }

  public void initIconObjs() {
    if(this.menuObj == null) {
      final QuadBuilder builder = new QuadBuilder("Battle menu");

      // "X" icon over attack icon if attack is disabled
      this.actionDisabledObjOffset = builder.currentQuadIndex() * 4;
      this.hud.buildBattleMenuElement(builder, 0, 0, 96, 112, 16, 16, 0x19);

      // Divine dragoon spirit overlay
      this.divineSpiritObjOffset = builder.currentQuadIndex() * 4;
      for(int i = 0; i < 2; i++) {
        this.hud.buildBattleMenuElement(builder, 4, 0, 80 + i * 8, 112, 8, 16, 0x98);
      }

      // Combat menu icons
      this.actionIconObjOffset = builder.currentQuadIndex() * 4;
      for(int iconId = 0; iconId < 9; iconId++) {
        final BattleMenuIconMetrics08 iconMetrics = battleMenuIconMetrics_800fb674[iconId];

        final int iconClutOffset = iconMetrics.clutOffset_04;

        for(int iconState = 0; iconState < 3; iconState++) {
          final int vOffset = battleMenuIconVOffsets_800fb6f4[iconId][iconState];
          final int iconH = battleMenuIconHeights_800fb6bc[iconId][iconState];

          this.hud.buildBattleMenuElement(builder, 0, 0, iconMetrics.u_00, iconMetrics.v_02 + vOffset & 0xff, 16, iconH, iconClutOffset);
        }
      }

      // Selected combat menu icon text
      this.actionIconTextObjOffset = builder.currentQuadIndex() * 4;
      for(int iconId = 0; iconId < 9; iconId++) {
        final BattleMenuTextMetrics08 textMetrics = battleMenuTextMetrics_800fb72c[iconId];
        this.hud.buildBattleMenuElement(builder, -textMetrics.w_04 / 2 + 8, -24, textMetrics.u_00, textMetrics.v_02, textMetrics.w_04, 8, textMetrics.clutOffset_06);
      }

      // Combat menu dragoon icons
      this.actionDragoonIconObjOffset = builder.currentQuadIndex() * 4;
      for(int spiritId = 0; spiritId < 10; spiritId++) {
        final BattleMenuIconMetrics08 iconMetrics = battleMenuIconMetrics_800fb674[1];

        final int iconClutOffset = dragoonSpiritIconClutOffsets_800c71d0[spiritId];

        for(int iconState = 0; iconState < 3; iconState++) {
          final int vOffset = battleMenuIconVOffsets_800fb6f4[1][iconState];
          final int iconH = battleMenuIconHeights_800fb6bc[1][iconState];

          this.hud.buildBattleMenuElement(builder, 0, 0, iconMetrics.u_00, iconMetrics.v_02 + vOffset & 0xff, 16, iconH, iconClutOffset);
        }
      }

      this.actionMenuBackgroundObjOffset = builder.currentQuadIndex() * 4;
      this.hud.buildBattleMenuBackground(builder, battleMenuBackgroundMetrics_800fb5dc[0], 0, 0, 1, 2, 0x2b, battleMenuBackgroundMetrics_800fb5dc[0].uvShiftType_04);

      for(int i = 1; i < 9; i++) {
        this.hud.buildBattleMenuBackground(builder, battleMenuBackgroundMetrics_800fb5dc[i], 0, 0, 1, 1, 0x2b, battleMenuBackgroundMetrics_800fb5dc[i].uvShiftType_04);
      }

      // Red glow underneath selected menu item
      this.highlightObjOffset = builder.currentQuadIndex() * 4;
      this.buildBattleMenuSelectionHighlight(builder, battleMenuHighlightMetrics_800c71bc, 0xc, 1.0f);

      this.targetArrowsObjOffset = builder.currentQuadIndex() * 4;
      for(int i = 0; i < 3; i++) {
        builder
          .add()
          .bpp(Bpp.BITS_4)
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
      }

      this.menuObj = builder.build();
    }
  }

  @Method(0x800f7210L)
  private void buildBattleMenuSelectionHighlight(final QuadBuilder builder, final BattleMenuHighlightMetrics12 highlightMetrics, final int clutOffset, final float colour) {
    //LAB_800f7294
    builder
      .add()
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
  }

  public void delete() {
    if(this.menuObj != null) {
      this.menuObj.delete();
      this.menuObj = null;
    }
  }

  public boolean isIconEnabled(final int value) {
    return Arrays.stream(this.iconFlags_10, 0, this.iconFlags_10.length)
      .anyMatch(i -> i == value);
  }

  public int retrieveIconEnabled(final int... values) {
    return Arrays.stream(this.iconFlags_10, 0, this.iconFlags_10.length)
      .filter(i -> Arrays.stream(values).anyMatch(value -> value == i))
      .findFirst()
      .orElse(0);
  }
}
