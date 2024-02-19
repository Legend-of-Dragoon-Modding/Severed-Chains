package legend.game.combat.ui;

import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.environment.BattleMenuBackgroundUvMetrics04;
import legend.game.types.Translucency;

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
  /** ushort */
  public int _0c;
  /** ushort */
  public int _0e;
  /** ushort */
  public int width_10;
  /** ushort */
  public int height_12;
  /** ushort */
  public int _14;
  /** ushort */
  public int _16;
  public short textX_18;
  public short _1a;
  public short itemOrSpellId_1c;
  public short listIndex_1e;
  public short _20;
  public short count_22;
  public short listScroll_24;
  public short _26;
  public short _28;
  public short _2a;
  public int _2c;
  public short _30;

  public int _7c;
  public int _80;
  public int selectionArrowFrame_84;
  public int _88;
  public int _8c;
  public int _90;
  public int _94;
  public int _98;
  public int _9c;
  public int _a0;

  public final MV transforms = new MV();
  public final Obj[] arrowObj = new Obj[8];
  public Obj mpObj;
  public Obj upArrow;
  public Obj downArrow;

  private final BattleHud hud;

  public boolean itemTargetAll_800c69c8;

  public int itemTargetType_800c6b68;

  public SpellAndItemMenuA4(final BattleHud hud) {
    this.hud = hud;
  }

  public void init() {
    if(this.arrowObj[0] == null) {
      for(int i = 0; i < 8; i++) {
        this.arrowObj[i] = this.hud.buildBattleMenuElement("Spell/Item Selection Arrow " + i, 0, 0, i % 4 * 16 + 192 & 0xf0, i / 4 * 8 + 32 & 0xf8, 15, 8, 0xd, Translucency.B_PLUS_F);
      }

      this.mpObj = this.hud.buildBattleMenuElement("Spell/Item MP", 0, 0, 16, 128, 24, 16, 0x2c, null);

      this.upArrow = this.hud.buildBattleMenuBackground("Spell/Item Up Arrow", battleItemMenuScrollArrowUvMetrics_800c7190, 0, 0, battleItemMenuScrollArrowUvMetrics_800c7190.w_02, battleItemMenuScrollArrowUvMetrics_800c7190.h_03, 0xd, null, (short)0);
      this.downArrow = this.hud.buildBattleMenuBackground("Spell/Item Down Arrow", battleItemMenuScrollArrowUvMetrics_800c7190, 0, 0, battleItemMenuScrollArrowUvMetrics_800c7190.w_02, battleItemMenuScrollArrowUvMetrics_800c7190.h_03, 0xd, null, (short)1);
    }
  }

  public void delete() {
    if(this.arrowObj[0] != null) {
      for(int i = 0; i < 8; i++) {
        if(this.arrowObj[i] != null) {
          this.arrowObj[i].delete();
          this.arrowObj[i] = null;
        }
      }

      if(this.mpObj != null) {
        this.mpObj.delete();
        this.mpObj = null;
      }

      if(this.upArrow != null) {
        this.upArrow.delete();
        this.upArrow = null;
      }

      if(this.downArrow != null) {
        this.downArrow.delete();
        this.downArrow = null;
      }
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
    this._0c = 0;
    this._0e = 0;
    this.width_10 = 0;
    this.height_12 = 0;
    this._14 = 0;
    this._16 = 0x1000;
    this.textX_18 = 0;
    this._1a = 0;
    this.itemOrSpellId_1c = -1;
    this.count_22 = 0;
    this.listScroll_24 = 0;

    this.itemTargetAll_800c69c8 = false;
    this.itemTargetType_800c6b68 = 0;
  }
}
