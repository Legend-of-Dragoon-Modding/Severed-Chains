package legend.game.sound;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class ReverbConfig implements MemoryRef {
  private final Value ref;

  public final ShortRef dApf1;
  public final ShortRef dApf2;
  public final ShortRef vIir;
  public final ShortRef vComb1;
  public final ShortRef vComb2;
  public final ShortRef vComb3;
  public final ShortRef vComb4;
  public final ShortRef vWall;
  public final ShortRef vApf1;
  public final ShortRef vApf2;
  public final ShortRef mLSame;
  public final ShortRef mRSame;
  public final ShortRef mLComb1;
  public final ShortRef mRComb1;
  public final ShortRef mLComb2;
  public final ShortRef mRComb2;
  public final ShortRef dLSame;
  public final ShortRef dRSame;
  public final ShortRef mLDiff;
  public final ShortRef mRDiff;
  public final ShortRef mLComb3;
  public final ShortRef mRComb3;
  public final ShortRef mLComb4;
  public final ShortRef mRComb4;
  public final ShortRef dLDiff;
  public final ShortRef dRDiff;
  public final ShortRef mLApf1;
  public final ShortRef mRApf1;
  public final ShortRef mLApf2;
  public final ShortRef mRApf2;
  public final ShortRef vLIn;
  public final ShortRef vRIn;

  public ReverbConfig(final Value ref) {
    this.ref = ref;

    this.dApf1 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this.dApf2 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this.vIir = ref.offset(2, 0x04L).cast(ShortRef::new);
    this.vComb1 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this.vComb2 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this.vComb3 = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this.vComb4 = ref.offset(2, 0x0cL).cast(ShortRef::new);
    this.vWall = ref.offset(2, 0x0eL).cast(ShortRef::new);
    this.vApf1 = ref.offset(2, 0x10L).cast(ShortRef::new);
    this.vApf2 = ref.offset(2, 0x12L).cast(ShortRef::new);
    this.mLSame = ref.offset(2, 0x14L).cast(ShortRef::new);
    this.mRSame = ref.offset(2, 0x16L).cast(ShortRef::new);
    this.mLComb1 = ref.offset(2, 0x18L).cast(ShortRef::new);
    this.mRComb1 = ref.offset(2, 0x1aL).cast(ShortRef::new);
    this.mLComb2 = ref.offset(2, 0x1cL).cast(ShortRef::new);
    this.mRComb2 = ref.offset(2, 0x1eL).cast(ShortRef::new);
    this.dLSame = ref.offset(2, 0x20L).cast(ShortRef::new);
    this.dRSame = ref.offset(2, 0x22L).cast(ShortRef::new);
    this.mLDiff = ref.offset(2, 0x24L).cast(ShortRef::new);
    this.mRDiff = ref.offset(2, 0x26L).cast(ShortRef::new);
    this.mLComb3 = ref.offset(2, 0x28L).cast(ShortRef::new);
    this.mRComb3 = ref.offset(2, 0x2aL).cast(ShortRef::new);
    this.mLComb4 = ref.offset(2, 0x2cL).cast(ShortRef::new);
    this.mRComb4 = ref.offset(2, 0x2eL).cast(ShortRef::new);
    this.dLDiff = ref.offset(2, 0x30L).cast(ShortRef::new);
    this.dRDiff = ref.offset(2, 0x32L).cast(ShortRef::new);
    this.mLApf1 = ref.offset(2, 0x34L).cast(ShortRef::new);
    this.mRApf1 = ref.offset(2, 0x36L).cast(ShortRef::new);
    this.mLApf2 = ref.offset(2, 0x38L).cast(ShortRef::new);
    this.mRApf2 = ref.offset(2, 0x3aL).cast(ShortRef::new);
    this.vLIn = ref.offset(2, 0x3cL).cast(ShortRef::new);
    this.vRIn = ref.offset(2, 0x3eL).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
