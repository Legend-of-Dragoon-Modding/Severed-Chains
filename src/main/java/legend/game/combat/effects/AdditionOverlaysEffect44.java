package legend.game.combat.effects;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class AdditionOverlaysEffect44 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final IntRef attackerScriptIndex_00;
  public final IntRef targetScriptIndex_04;

  public final VECTOR attackerStartingPosition_10; // Unused
  public final VECTOR distancePerFrame_20; // Unused

  public final UnsignedByteRef count_30;
  /** 0 = renders and ticks, 1 = skips render and tick, 2 = renders only; set by scriptAlterAdditionContinuationState */
  public final UnsignedByteRef pauseTickerAndRenderer_31;
  /** 0 = not complete, 1 = complete, either successful or failed */
  public final ByteRef additionComplete_32;

  public final ShortRef currentFrame_34;
  public final UnsignedShortRef unused_36;
  public final UnsignedByteRef numFramesToRenderCenterSquare_38;
  public final UnsignedByteRef lastCompletedHit_39;
  public final UnsignedByteRef autoCompleteType_3a; // 0 = no auto complete, 2 = WC and UW auto-complete

  public final Pointer<AdditionOverlaysHit20> lastCompletedHitOverlay_3c; // points to a specific hit set while ticking
  public final Pointer<UnboundedArrayRef<AdditionOverlaysHit20>> hitOverlays_40;

  public AdditionOverlaysEffect44(final Value ref) {
    this.ref = ref;

    this.attackerScriptIndex_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.targetScriptIndex_04 = ref.offset(4, 0x04L).cast(IntRef::new);

    this.attackerStartingPosition_10 = ref.offset(4, 0x10L).cast(VECTOR::new);
    this.distancePerFrame_20 = ref.offset(4, 0x20L).cast(VECTOR::new);

    this.count_30 = ref.offset(1, 0x30L).cast(UnsignedByteRef::new);
    this.pauseTickerAndRenderer_31 = ref.offset(1, 0x31L).cast(UnsignedByteRef::new);
    this.additionComplete_32 = ref.offset(1, 0x32L).cast(ByteRef::new);

    this.currentFrame_34 = ref.offset(2, 0x34L).cast(ShortRef::new);
    this.unused_36 = ref.offset(2, 0x36L).cast(UnsignedShortRef::new);
    this.numFramesToRenderCenterSquare_38 = ref.offset(1, 0x38L).cast(UnsignedByteRef::new);
    this.lastCompletedHit_39 = ref.offset(1, 0x39L).cast(UnsignedByteRef::new);
    this.autoCompleteType_3a = ref.offset(1, 0x3aL).cast(UnsignedByteRef::new);

    this.lastCompletedHitOverlay_3c = ref.offset(4, 0x3cL).cast(Pointer.deferred(4, AdditionOverlaysHit20::new));
    this.hitOverlays_40 = ref.offset(4, 0x40L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x20, AdditionOverlaysHit20::new, this.count_30::get)));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
