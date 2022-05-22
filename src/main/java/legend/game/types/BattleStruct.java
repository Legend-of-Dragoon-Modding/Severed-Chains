package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;

/** 0x18cb0 bytes */
public class BattleStruct implements MemoryRef {
  private final Value ref;

  /** This reference is only valid while it's loading */
  public final Pointer<MrgFile> stageMrg_638;
  public final Pointer<MrgFile> stageTmdMrg_63c;

  public final BattleRenderStruct render_963c;
  //TODO don't know where this struct ends

  public final McqHeader stageMcq_9cb0;
  public final Value _9cdc;
  public final Value _9ce0;
  public final Value _9ce4;
  public final Value _9ce8;

  //TODO unknown size
  public final Value _d4b0;

  public BattleStruct(final Value ref) {
    this.ref = ref;

    this.stageMrg_638 = ref.offset(4, 0x638L).cast(Pointer.deferred(4, MrgFile::new));
    this.stageTmdMrg_63c = ref.offset(4, 0x63cL).cast(Pointer.deferred(4, MrgFile::new));

    this.render_963c = ref.offset(4, 0x963cL).cast(BattleRenderStruct::new);

    this.stageMcq_9cb0 = ref.offset(4, 0x9cb0L).cast(McqHeader::new);
    this._9cdc = ref.offset(4, 0x9cdcL);
    this._9ce0 = ref.offset(4, 0x9ce0L);
    this._9ce4 = ref.offset(4, 0x9ce4L);
    this._9ce8 = ref.offset(4, 0x9ce8L);

    this._d4b0 = ref.offset(4, 0xd4b0L);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
