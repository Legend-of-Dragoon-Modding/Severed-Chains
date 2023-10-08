package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;

/** Same as AdditionHitProperties20 in BattlePreloadedEntities_18cb0, except fields are bytes */
public class AdditionHitProperties10 implements MemoryRef {
  public final Value ref;

  public final UnsignedByteRef flags_00;
  public final ByteRef totalFrames_01;
  public final ByteRef overlayHitFrameOffset_02;
  public final ByteRef totalSuccessFrames_03;
  public final ByteRef damageMultiplier_04;
  public final ByteRef spValue_05;
  public final ByteRef audioFile_06;
  public final ByteRef isFinalHit_07;
  public final ByteRef _08; // related to camera or voice? index into array?
  public final ByteRef _09; // related to camera or voice? index into array?
  public final ByteRef _0a; // related to camera? index into array?
  public final ByteRef hitDistanceFromTarget_0b;
  public final ByteRef framesToHitPosition_0c;
  public final ByteRef _0d; // always 32 (except for a few for Haschel), could be length of properties array
  public final ByteRef framesPostFailure_0e;
  public final ByteRef overlayStartingFrameOffset_0f;

  public AdditionHitProperties10(final Value ref) {
    this.ref = ref;

    this.flags_00 = ref.offset(1, 0x00).cast(UnsignedByteRef::new);
    this.totalFrames_01 = ref.offset(1, 0x01).cast(ByteRef::new);
    this.overlayHitFrameOffset_02 = ref.offset(1, 0x02).cast(ByteRef::new);
    this.totalSuccessFrames_03 = ref.offset(1, 0x03).cast(ByteRef::new);
    this.damageMultiplier_04 = ref.offset(1, 0x04).cast(ByteRef::new);
    this.spValue_05 = ref.offset(1, 0x05).cast(ByteRef::new);
    this.audioFile_06 = ref.offset(1, 0x06).cast(ByteRef::new);
    this.isFinalHit_07 = ref.offset(1, 0x07).cast(ByteRef::new);
    this._08 = ref.offset(1, 0x08).cast(ByteRef::new);
    this._09 = ref.offset(1, 0x09).cast(ByteRef::new);
    this._0a = ref.offset(1, 0x0a).cast(ByteRef::new);
    this.hitDistanceFromTarget_0b = ref.offset(1, 0x0b).cast(ByteRef::new);
    this.framesToHitPosition_0c = ref.offset(1, 0x0c).cast(ByteRef::new);
    this._0d = ref.offset(1, 0x0d).cast(ByteRef::new);
    this.framesPostFailure_0e = ref.offset(1, 0x0e).cast(ByteRef::new);
    this.overlayStartingFrameOffset_0f = ref.offset(1, 0x0f).cast(ByteRef::new);
  }
  
  public int get(final int index) {
    return switch(index) {
      case 0 -> this.flags_00.get();
      case 1 -> this.totalFrames_01.get();
      case 2 -> this.overlayHitFrameOffset_02.get();
      case 3 -> this.totalSuccessFrames_03.get();
      case 4 -> this.damageMultiplier_04.get();
      case 5 -> this.spValue_05.get();
      case 6 -> this.audioFile_06.get();
      case 7 -> this.isFinalHit_07.get();
      case 8 -> this._08.get();
      case 9 -> this._09.get();
      case 10 -> this._0a.get();
      case 11 -> this.hitDistanceFromTarget_0b.get();
      case 12 -> this.framesToHitPosition_0c.get();
      case 13 -> this._0d.get();
      case 14 -> this.framesPostFailure_0e.get();
      case 15 -> this.overlayStartingFrameOffset_0f.get();
      default -> throw new IllegalArgumentException("Invalid property index " + index);
    };
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
