package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.sound.PatchList;
import legend.game.sound.VolumeRamp;

public class SshdFile implements MemoryRef {
  public static final long MAGIC = 0x6468_5353L; //SShd

  private final Value ref;

  public final IntRef mySize_00;
  public final IntRef soundBankSize_04;

  public final UnsignedIntRef magic_0c;
  /**
   * <ul>
   *   <li>0 - Same type as 4?</li>
   *   <li>1 - {@link VolumeRamp}</li>
   *   <li>3 - {@link PatchList} (only used if ptr 4 is also set?)</li>
   *   <li>4 - Embedded SSSQ file? Has 24 entries instead of 16. Most header information is 0.</li>
   * </ul>
   */
  public final ArrayRef<IntRef> ptrs_10;

  public SshdFile(final Value ref) {
    this.ref = ref;

    this.mySize_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.soundBankSize_04 = ref.offset(4, 0x04L).cast(IntRef::new);

    this.magic_0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this.ptrs_10 = ref.offset(4, 0x10L).cast(ArrayRef.of(IntRef.class, 0x1c, 4, IntRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
