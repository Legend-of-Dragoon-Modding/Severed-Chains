package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.game.types.McqHeader;
import legend.game.types.MrgFile;

/** 0x18cb0 bytes */
public class BattleStruct18cb0 implements MemoryRef {
  private final Value ref;

  /** This reference is only valid while it's loading */
  public final Pointer<MrgFile> stageMrg_638;
  public final MrgFile stageTmdMrg_63c;

  public final BattleStage stage_963c;
  public final McqHeader stageMcq_9cb0;

  public final Value _9ce8;

  public BattleStruct18cb0(final Value ref) {
    this.ref = ref;

    this.stageMrg_638 = ref.offset(4, 0x638L).cast(Pointer.deferred(4, MrgFile::new));
    this.stageTmdMrg_63c = ref.offset(4, 0x63cL).cast(MrgFile::new);

    this.stage_963c = ref.offset(4, 0x963cL).cast(BattleStage::new);

    this.stageMcq_9cb0 = ref.offset(4, 0x9cb0L).cast(McqHeader::new);

    this._9ce8 = ref.offset(4, 0x9ce8L);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
