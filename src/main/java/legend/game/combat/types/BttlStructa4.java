package legend.game.combat.types;

import legend.core.memory.types.IntRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.inventory.Item;

/** Used for rendering the spell menu, item menu, maybe other stuff? */
public class BttlStructa4 {
  public final ShortRef _00;
  public final UnsignedShortRef _02;
  public final UnsignedShortRef x_04;
  public final UnsignedShortRef y_06;
  public final ShortRef charIndex_08;
  /** 0 = item, 1 = spell */
  public final ShortRef attackType_0a;
  public final UnsignedShortRef _0c;
  public final UnsignedShortRef _0e;
  public final UnsignedShortRef _10;
  public final UnsignedShortRef _12;
  public final UnsignedShortRef _14;
  public final UnsignedShortRef _16;
  public final ShortRef _18;
  public final ShortRef _1a;
  /** short itemId */
  public Item item_1c;
  public int spellIndex_1c;
  public final ShortRef _1e;
  public final ShortRef _20;
  public final ShortRef count_22;
  public final ShortRef _24;
  public final ShortRef _26;
  public final ShortRef _28;
  public final ShortRef _2a;
  public final IntRef _2c;
  public final ShortRef _30;

  public final IntRef _7c;
  public final IntRef _80;
  public final IntRef _84;
  public final IntRef _88;
  public final IntRef _8c;
  public final IntRef _90;
  public final IntRef _94;
  public final IntRef _98;
  public final IntRef _9c;
  public final IntRef _a0;

  public BttlStructa4() {
    this._00 = new ShortRef();
    this._02 = new UnsignedShortRef();
    this.x_04 = new UnsignedShortRef();
    this.y_06 = new UnsignedShortRef();
    this.charIndex_08 = new ShortRef();
    this.attackType_0a = new ShortRef();
    this._0c = new UnsignedShortRef();
    this._0e = new UnsignedShortRef();
    this._10 = new UnsignedShortRef();
    this._12 = new UnsignedShortRef();
    this._14 = new UnsignedShortRef();
    this._16 = new UnsignedShortRef();
    this._18 = new ShortRef();
    this._1a = new ShortRef();

    this._1e = new ShortRef();
    this._20 = new ShortRef();
    this.count_22 = new ShortRef();
    this._24 = new ShortRef();
    this._26 = new ShortRef();
    this._28 = new ShortRef();
    this._2a = new ShortRef();
    this._2c = new IntRef();
    this._30 = new ShortRef();

    this._7c = new IntRef();
    this._80 = new IntRef();
    this._84 = new IntRef();
    this._88 = new IntRef();
    this._8c = new IntRef();
    this._90 = new IntRef();
    this._94 = new IntRef();
    this._98 = new IntRef();
    this._9c = new IntRef();
    this._a0 = new IntRef();
  }
}
