package legend.game.combat.ui;

import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.environment.BattleMenuBackgroundUvMetrics04;

public class SpellAndItemMenuA4 {
  private static final BattleMenuBackgroundUvMetrics04 battleItemMenuScrollArrowUvMetrics_800c7190 = new BattleMenuBackgroundUvMetrics04(224, 8, 16, 8);

  public short menuState_00;
  /** ushort */
  public int flags_02;
  /** ushort */
  public int x_04;
  /** ushort */
  public int y_06;
  public PlayerBattleEntity player_08;
  /**
   * <ol start="0">
   *   <li>Items</li>
   *   <li>Spells</li>
   * </ol>
   */
  public short menuType_0a;
//  /** ushort */
//  public int _0c;
//  /** ushort */
//  public int _0e;
  /** ushort */
  public int width_10;
  /** ushort */
  public int height_12;
//  /** ushort */
//  public int _14;
//  /** ushort */
//  public int _16;
  public short textX_18;
  public short listStartY_1a;
  public short itemOrSpellId_1c;
  public short listScroll_1e;
  /** Decreases as you scroll down the list */
  public short listOffsetY_20;
  public short count_22;
  /** Selected item relative to listScroll */
  public short listIndex_24;
  /** Used to select the same item when the list is opened next time */
  public short lastListIndex_26;
  /** Used to select the same item when the list is opened next time */
  public short lastListScroll_28;
  /** Used to select the same item when the list is opened next time */
  public short lastListOffsetY_2a;
  public int lastOffsetFromStartOfListY_2c;
  /** Not actually set */
  public short lastSpellIndex_30;

  public int lastListOffset_7c;
  /** +5 for up, -5 for down*/
  public int scrollAmount_80;
  public int selectionArrowFrame_84;
//  public int _88;
//  public int _8c;
  /** Used to speed up scrolling after holding up or down for 3 items */
  public int scrollCounter_90;
  public int offsetFromStartOfListY_94;
//  public int _98;
//  public int _9c;
  /**
   * <ul>
   *   <li>0 - nothing selected</li>
   *   <li>1 - item or spell selected</li>
   *   <li>2 - menu unloading</li>
   * </ul>
   */
  public int selectionState_a0;

  public final MV transforms = new MV();
  public Obj menuObj;
  public int arrowObjOffset;
  public int mpObjOffset;
  public int upObjOffset;
  public int downObjOffset;

  private final BattleHud hud;

  public boolean itemTargetAll_800c69c8;

  public int itemTargetType_800c6b68;

  public SpellAndItemMenuA4(final BattleHud hud) {
    this.hud = hud;
  }

  public void init() {
    if(this.menuObj == null) {
      final QuadBuilder builder = new QuadBuilder("Spell/item menu");

      this.arrowObjOffset = builder.currentQuadIndex() * 4;
      for(int i = 0; i < 8; i++) {
        this.hud.buildBattleMenuElement(builder, 0, 0, i % 4 * 16 + 192 & 0xf0, i / 4 * 8 + 32 & 0xf8, 15, 8, 0xd);
      }

      this.mpObjOffset = builder.currentQuadIndex() * 4;
      this.hud.buildBattleMenuElement(builder, 0, 0, 16, 128, 24, 16, 0x2c);

      this.upObjOffset = builder.currentQuadIndex() * 4;
      this.hud.buildBattleMenuBackground(builder, battleItemMenuScrollArrowUvMetrics_800c7190, 0, 0, battleItemMenuScrollArrowUvMetrics_800c7190.w_02, battleItemMenuScrollArrowUvMetrics_800c7190.h_03, 0xd, (short)0);
      this.downObjOffset = builder.currentQuadIndex() * 4;
      this.hud.buildBattleMenuBackground(builder, battleItemMenuScrollArrowUvMetrics_800c7190, 0, 0, battleItemMenuScrollArrowUvMetrics_800c7190.w_02, battleItemMenuScrollArrowUvMetrics_800c7190.h_03, 0xd, (short)1);

      this.menuObj = builder.build();
    }
  }

  public void delete() {
    if(this.menuObj != null) {
      this.menuObj.delete();
      this.menuObj = null;
    }
  }

  @Method(0x800f4964L)
  public void clear() {
    this.menuState_00 = 0;
    this.flags_02 = 0;
    this.x_04 = 0;
    this.y_06 = 0;
    this.player_08 = null;
    this.menuType_0a = 0;
    this.width_10 = 0;
    this.height_12 = 0;
    this.textX_18 = 0;
    this.listStartY_1a = 0;
    this.itemOrSpellId_1c = -1;
    this.count_22 = 0;
    this.listIndex_24 = 0;

    this.itemTargetAll_800c69c8 = false;
    this.itemTargetType_800c6b68 = 0;
  }
}
