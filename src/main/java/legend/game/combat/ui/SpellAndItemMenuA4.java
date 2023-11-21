package legend.game.combat.ui;

import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.game.types.Translucency;

import static legend.game.combat.Bttl_800c.battleItemMenuScrollArrowUvMetrics_800c7190;
import static legend.game.combat.Bttl_800f.buildBattleMenuBackground;
import static legend.game.combat.Bttl_800f.buildBattleMenuElement;

public class SpellAndItemMenuA4 {
  public short menuState_00;
  /** ushort */
  public int _02;
  /** ushort */
  public int x_04;
  /** ushort */
  public int y_06;
  public short charIndex_08;
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
  public int _10;
  /** ushort */
  public int _12;
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
  public int _84;
  public int _88;
  public int _8c;
  public int _90;
  public int _94;
  public int _98;
  public int _9c;
  public int _a0;

  public final MV transforms = new MV();
  public final Obj[] unknownObj1 = new Obj[8];
  public Obj unknownObj2;
  public Obj upArrow;
  public Obj downArrow;

  public void init() {
    if(this.unknownObj1[0] == null) {
      for(int i = 0; i < 8; i++) {
        this.unknownObj1[i] = buildBattleMenuElement("Unknown Spell/Item UI Obj 1 " + i, 0, 0, i % 4 * 16 + 192 & 0xf0, i / 4 * 8 + 32 & 0xf8, 15, 8, 0xd, Translucency.B_PLUS_F);
      }

      this.unknownObj2 = buildBattleMenuElement("Unknown Spell/Item UI Obj 2", 0, 0, 16, 128, 24, 16, 0x2c, null);

      this.upArrow = buildBattleMenuBackground("Spell/Item Up Arrow", battleItemMenuScrollArrowUvMetrics_800c7190, 0, 0, battleItemMenuScrollArrowUvMetrics_800c7190.w_02.get(), battleItemMenuScrollArrowUvMetrics_800c7190.h_03.get(), 0xd, null, (short)0);
      this.downArrow = buildBattleMenuBackground("Spell/Item Down Arrow", battleItemMenuScrollArrowUvMetrics_800c7190, 0, 0, battleItemMenuScrollArrowUvMetrics_800c7190.w_02.get(), battleItemMenuScrollArrowUvMetrics_800c7190.h_03.get(), 0xd, null, (short)1);
    }
  }

  public void delete() {
    if(this.unknownObj1[0] != null) {
      for(int i = 0; i < 8; i++) {
        if(this.unknownObj1[i] != null) {
          this.unknownObj1[i].delete();
          this.unknownObj1[i] = null;
        }
      }

      if(this.unknownObj2 != null) {
        this.unknownObj2.delete();
        this.unknownObj2 = null;
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
}
