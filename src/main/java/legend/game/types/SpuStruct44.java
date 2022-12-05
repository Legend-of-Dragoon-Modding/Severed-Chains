package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class SpuStruct44 implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef voiceIndex_00;
  public final UnsignedByteRef channelIndex_01;

  public final UnsignedByteRef _03;
  public final UnsignedByteRef _04;

  public final Pointer<SshdFile> sshdPtr_08;
  public final UnsignedByteRef _0c;
  public final UnsignedByteRef _0d;

  /** May be -1 */
  public final ShortRef voiceIndex_10;
  public final UnsignedShortRef reverbModeLo_12;
  public final UnsignedShortRef reverbModeHi_14;
  public final UnsignedShortRef noiseModeLo_16;
  public final UnsignedShortRef noiseModeHi_18;

  public final UnsignedIntRef eventSpuIrq_1c;
  /** No longer used */
  public final BoolRef spuDmaTransferInProgress_20;
  public final UnsignedByteRef pitchShifted_22;
  public final UnsignedByteRef _23;
  public final UnsignedShortRef pitch_24;
  public final ShortRef pitchShiftVolLeft_26;
  public final ShortRef pitchShiftVolRight_28;
  public final UnsignedByteRef fadingIn_2a;
  public final UnsignedByteRef fadingOut_2b;
  public final UnsignedShortRef fadeTime_2c;
  public final UnsignedShortRef fadeInVol_2e;
  public final UnsignedShortRef fadeOutVolL_30;
  public final UnsignedShortRef fadeOutVolR_32;
  public final UnsignedShortRef reverbType_34;
  public final UnsignedShortRef mono_36;
  public final UnsignedByteRef hasCallback_38;
  public final UnsignedByteRef dmaIndex_39;
  public final UnsignedShortRef keyOnLo_3a;
  public final UnsignedShortRef keyOnHi_3c;
  public final UnsignedShortRef keyOffLo_3e;
  public final UnsignedShortRef keyOffHi_40;
  public final UnsignedShortRef _42;

  public SpuStruct44(final Value ref) {
    this.ref = ref;

    this.voiceIndex_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this.channelIndex_01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);

    this._03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    this._04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);

    this.sshdPtr_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, SshdFile::new));
    this._0c = ref.offset(1, 0x0cL).cast(UnsignedByteRef::new);
    this._0d = ref.offset(1, 0x0dL).cast(UnsignedByteRef::new);

    this.voiceIndex_10 = ref.offset(2, 0x10L).cast(ShortRef::new);
    this.reverbModeLo_12 = ref.offset(2, 0x12L).cast(UnsignedShortRef::new);
    this.reverbModeHi_14 = ref.offset(2, 0x14L).cast(UnsignedShortRef::new);
    this.noiseModeLo_16 = ref.offset(2, 0x16L).cast(UnsignedShortRef::new);
    this.noiseModeHi_18 = ref.offset(2, 0x18L).cast(UnsignedShortRef::new);

    this.eventSpuIrq_1c = ref.offset(4, 0x1cL).cast(UnsignedIntRef::new);
    this.spuDmaTransferInProgress_20 = ref.offset(2, 0x20L).cast(BoolRef::new);
    this.pitchShifted_22 = ref.offset(1, 0x22L).cast(UnsignedByteRef::new);
    this._23 = ref.offset(1, 0x23L).cast(UnsignedByteRef::new);
    this.pitch_24 = ref.offset(2, 0x24L).cast(UnsignedShortRef::new);
    this.pitchShiftVolLeft_26 = ref.offset(2, 0x26L).cast(ShortRef::new);
    this.pitchShiftVolRight_28 = ref.offset(2, 0x28L).cast(ShortRef::new);
    this.fadingIn_2a = ref.offset(1, 0x2aL).cast(UnsignedByteRef::new);
    this.fadingOut_2b = ref.offset(1, 0x2bL).cast(UnsignedByteRef::new);
    this.fadeTime_2c = ref.offset(2, 0x2cL).cast(UnsignedShortRef::new);
    this.fadeInVol_2e = ref.offset(2, 0x2eL).cast(UnsignedShortRef::new);
    this.fadeOutVolL_30 = ref.offset(2, 0x30L).cast(UnsignedShortRef::new);
    this.fadeOutVolR_32 = ref.offset(2, 0x32L).cast(UnsignedShortRef::new);
    this.reverbType_34 = ref.offset(2, 0x34L).cast(UnsignedShortRef::new);
    this.mono_36 = ref.offset(2, 0x36L).cast(UnsignedShortRef::new);
    this.hasCallback_38 = ref.offset(1, 0x38L).cast(UnsignedByteRef::new);
    this.dmaIndex_39 = ref.offset(1, 0x39L).cast(UnsignedByteRef::new);
    this.keyOnLo_3a = ref.offset(2, 0x3aL).cast(UnsignedShortRef::new);
    this.keyOnHi_3c = ref.offset(2, 0x3cL).cast(UnsignedShortRef::new);
    this.keyOffLo_3e = ref.offset(2, 0x3eL).cast(UnsignedShortRef::new);
    this.keyOffHi_40 = ref.offset(2, 0x40L).cast(UnsignedShortRef::new);
    this._42 = ref.offset(2, 0x42L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
