package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

/** 0x18cb0 bytes */
public class BattleStruct implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _638; //TODO pointer
  public final UnsignedIntRef _63c; //TODO pointer

  public final BattleRenderStruct render_963c;
  //TODO don't know where this struct ends

  //TODO 0x2c-bytes long
  public final Value _9cb0;
  public final Value _9cdc;
  public final Value _9ce0;
  public final Value _9ce4;

  //TODO unknown size
  public final Value _d4b0;

  public BattleStruct(final Value ref) {
    this.ref = ref;

    this._638 = ref.offset(4, 0x638L).cast(UnsignedIntRef::new);
    this._63c = ref.offset(4, 0x63cL).cast(UnsignedIntRef::new);

    this.render_963c = ref.offset(4, 0x963cL).cast(BattleRenderStruct::new);

    this._9cb0 = ref.offset(4, 0x9cb0L);
    this._9cdc = ref.offset(4, 0x9cdcL);
    this._9ce0 = ref.offset(4, 0x9ce0L);
    this._9ce4 = ref.offset(4, 0x9ce4L);

    this._d4b0 = ref.offset(4, 0xd4b0L);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
