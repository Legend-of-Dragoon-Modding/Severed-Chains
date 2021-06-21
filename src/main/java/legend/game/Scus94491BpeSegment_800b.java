package legend.game;

import legend.core.cdrom.CdlPacket;
import legend.core.cdrom.Response;
import legend.core.cdrom.SyncCode;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BiConsumerRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.CString;
import legend.core.memory.types.EnumRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.UnsignedIntRef;

import static legend.core.Hardware.MEMORY;
import static legend.core.LibDs.DSL_MAX_COMMAND;

public final class Scus94491BpeSegment_800b {
  private Scus94491BpeSegment_800b() { }

  public static final Value _800babc0 = MEMORY.ref(1, 0x800babc0L);

  public static final Value mono_800bb0a8 = MEMORY.ref(1, 0x800bb0a8L);
  public static final Value vibrationEnabled_800bb0a9 = MEMORY.ref(1, 0x800bb0a9L);

  public static final Value _800bb0fc = MEMORY.ref(4, 0x800bb0fcL);

  public static final Value _800bb104 = MEMORY.ref(4, 0x800bb104L);

  public static final Value loadingStage_800bb10c = MEMORY.ref(4, 0x800bb10cL);
  public static final Value _800bb110 = MEMORY.ref(2, 0x800bb110L);
  public static final Value _800bb112 = MEMORY.ref(2, 0x800bb112L);

  public static final ArrayRef<UnsignedIntRef> array_800bb198 = MEMORY.ref(4, 0x800bb198L, ArrayRef.of(UnsignedIntRef.class, 36, 4, UnsignedIntRef::new));

  public static final Value _800bb228 = MEMORY.ref(4, 0x800bb228L);

  public static final Value _800bb348 = MEMORY.ref(4, 0x800bb348L);

  public static final Value _800bc0c0 = MEMORY.ref(4, 0x800bc0c0L);

  public static final Value _800bc1c0 = MEMORY.ref(4, 0x800bc1c0L);

  public static final Value _800bd808 = MEMORY.ref(4, 0x800bd808L);

  public static final Value _800bda10 = MEMORY.ref(4, 0x800bda10L);

  public static final Value _800bda84 = MEMORY.ref(2, 0x800bda84L);
  public static final Value _800bda86 = MEMORY.ref(2, 0x800bda86L);
  public static final Value _800bda88 = MEMORY.ref(2, 0x800bda88L);

  public static final Value _800bdaad = MEMORY.ref(1, 0x800bdaadL);

  public static final Value _800bdadc = MEMORY.ref(1, 0x800bdadcL);

  public static final Value _800bdb90 = MEMORY.ref(4, 0x800bdb90L);

  public static final Value _800bdc24 = MEMORY.ref(4, 0x800bdc24L);

  public static final Value _800bdc40 = MEMORY.ref(4, 0x800bdc40L);

  public static final Value _800bdc5c = MEMORY.ref(4, 0x800bdc5cL);

  public static final Value _800bed60 = MEMORY.ref(2, 0x800bed60L);
  public static final Value _800bed62 = MEMORY.ref(2, 0x800bed62L);
  public static final Value _800bed64 = MEMORY.ref(2, 0x800bed64L);
  public static final Value _800bed66 = MEMORY.ref(2, 0x800bed66L);

  public static final Value _800bed6c = MEMORY.ref(2, 0x800bed6eL);
  public static final Value _800bed6e = MEMORY.ref(2, 0x800bed6cL);

  public static final Value _800beda8 = MEMORY.ref(2, 0x800beda8L);

  public static final Value _800bedb8 = MEMORY.ref(1, 0x800bedb8L);
  public static final Value _800bedb9 = MEMORY.ref(1, 0x800bedb9L);
  public static final Value _800bedba = MEMORY.ref(1, 0x800bedbaL);
  public static final Value _800bedbb = MEMORY.ref(1, 0x800bedbbL);
  public static final Value _800bedbc = MEMORY.ref(2, 0x800bedbcL);
  public static final Value _800bedbe = MEMORY.ref(1, 0x800bedbeL);

  public static final Value _800bede0 = MEMORY.ref(1, 0x800bede0L);
  public static final Value _800bede1 = MEMORY.ref(1, 0x800bede1L);
  public static final Value _800bede2 = MEMORY.ref(1, 0x800bede2L);
  public static final Value _800bede3 = MEMORY.ref(1, 0x800bede3L);

  public static final Value _800bede8 = MEMORY.ref(4, 0x800bede8L);
  public static final Value _800bedec = MEMORY.ref(1, 0x800bedecL);

  public static final Value _800bee48 = MEMORY.ref(1, 0x800bee48L);

  public static final Value _800bee4e = MEMORY.ref(1, 0x800bee4eL);

  public static final Value _800bee80 = MEMORY.ref(4, 0x800bee80L);

  public static final Value _800bee88 = MEMORY.ref(4, 0x800bee88L);
  public static final Value _800bee8c = MEMORY.ref(4, 0x800bee8cL);
  public static final Value _800bee90 = MEMORY.ref(4, 0x800bee90L);
  public static final Value _800bee94 = MEMORY.ref(4, 0x800bee94L);
  public static final Value _800bee98 = MEMORY.ref(4, 0x800bee98L);

  public static final Value _800beec4 = MEMORY.ref(4, 0x800beec4L);

  public static final Value _800bef44 = MEMORY.ref(4, 0x800bef44L);

  public static final Value _800befc4 = MEMORY.ref(4, 0x800befc4L);

  public static final Value _800bf044 = MEMORY.ref(1, 0x800bf044L);

  public static final Value _800bf064 = MEMORY.ref(4, 0x800bf064L);
  public static final Value _800bf068 = MEMORY.ref(2, 0x800bf068L);
  public static final Value _800bf06a = MEMORY.ref(2, 0x800bf06aL);
  public static final Value _800bf06c = MEMORY.ref(2, 0x800bf06cL);
  public static final Value _800bf06e = MEMORY.ref(2, 0x800bf06eL);

  public static final Value _800bf074 = MEMORY.ref(2, 0x800bf074L);
  public static final Value _800bf076 = MEMORY.ref(2, 0x800bf076L);

  public static final Value _800bf0a8 = MEMORY.ref(1, 0x800bf0a8L);
  public static final Value _800bf0ac = MEMORY.ref(4, 0x800bf0acL);

  public static final Value _800bf0b4 = MEMORY.ref(4, 0x800bf0b4L);

  public static final Value _800bf0cd = MEMORY.ref(1, 0x800bf0cdL);
  public static final Value _800bf0ce = MEMORY.ref(1, 0x800bf0ceL);
  public static final Value _800bf0cf = MEMORY.ref(1, 0x800bf0cfL);
  public static final Value _800bf0d0 = MEMORY.ref(1, 0x800bf0d0L);

  public static final Value _800bf0d8 = MEMORY.ref(4, 0x800bf0d8L);

  public static final Value _800bf160 = MEMORY.ref(4, 0x800bf160L);
  public static final Value _800bf164 = MEMORY.ref(4, 0x800bf164L);

  public static final Value _800bf170 = MEMORY.ref(4, 0x800bf170L);
  public static final Value _800bf174 = MEMORY.ref(4, 0x800bf174L);
  public static final Value _800bf178 = MEMORY.ref(4, 0x800bf178L);
  public static final Value _800bf17c = MEMORY.ref(4, 0x800bf17cL);
  public static final Value cardChannel_800bf180 = MEMORY.ref(4, 0x800bf180L);
  public static final Value _800bf184 = MEMORY.ref(4, 0x800bf184L);

  public static final Pointer<BiConsumerRef<Long, Long>> _800bf1b4 = MEMORY.ref(4, 0x800bf1b4L, Pointer.of(BiConsumerRef::new));
  public static final Value _800bf1b8 = MEMORY.ref(4, 0x800bf1b8L);
  public static final Value _800bf1bc = MEMORY.ref(4, 0x800bf1bcL);
  public static final Value _800bf1c0 = MEMORY.ref(4, 0x800bf1c0L);
  public static final Value _800bf1c4 = MEMORY.ref(4, 0x800bf1c4L);

  public static final Value _800bf200 = MEMORY.ref(4, 0x800bf200L);

  public static final Value _800bf23c = MEMORY.ref(4, 0x800bf23cL);

  public static final Value SwCARD_EvSpIOE_EventId_800bf250 = MEMORY.ref(4, 0x800bf250L);
  public static final Value SwCARD_EvSpERROR_EventId_800bf254 = MEMORY.ref(4, 0x800bf254L);
  public static final Value SwCARD_EvSpTIMOUT_EventId_800bf258 = MEMORY.ref(4, 0x800bf258L);
  public static final Value SwCARD_EvSpNEW_EventId_800bf25c = MEMORY.ref(4, 0x800bf25cL);
  public static final Value HwCARD_EvSpIOE_EventId_800bf260 = MEMORY.ref(4, 0x800bf260L);
  public static final Value HwCARD_EvSpERROR_EventId_800bf264 = MEMORY.ref(4, 0x800bf264L);
  public static final Value HwCARD_EvSpTIMOUT_EventId_800bf268 = MEMORY.ref(4, 0x800bf268L);
  public static final Value HwCARD_EvSpNEW_EventId_800bf26c = MEMORY.ref(4, 0x800bf26cL);
  public static final Value _800bf270 = MEMORY.ref(4, 0x800bf270L);
  public static final Value _800bf274 = MEMORY.ref(4, 0x800bf274L);
  public static final Value _800bf278 = MEMORY.ref(4, 0x800bf278L);
  public static final Value _800bf27c = MEMORY.ref(4, 0x800bf27cL);
  public static final Value _800bf280 = MEMORY.ref(4, 0x800bf280L);
  public static final Value _800bf284 = MEMORY.ref(4, 0x800bf284L);
  public static final Value _800bf288 = MEMORY.ref(4, 0x800bf288L);
  public static final Value _800bf28c = MEMORY.ref(4, 0x800bf28cL);

  public static final ArrayRef<ByteRef> cdromResponses_800bf5c0 = MEMORY.ref(8, 0x800bf5c0L, ArrayRef.of(ByteRef.class, 8, 1, ByteRef::new));
  public static final ArrayRef<ByteRef> cdromResponses_800bf5c8 = MEMORY.ref(8, 0x800bf5c8L, ArrayRef.of(ByteRef.class, 8, 1, ByteRef::new));
  public static final ArrayRef<ByteRef> cdromResponses_800bf5d0 = MEMORY.ref(8, 0x800bf5d0L, ArrayRef.of(ByteRef.class, 8, 1, ByteRef::new));

  public static final Pointer<CString> _800bf5e0 = MEMORY.ref(4, 0x800bf5e0L, Pointer.of(CString::new));

  public static final Value batch_800bf608 = MEMORY.ref(4, 0x800bf608L);
  public static final EnumRef<SyncCode> syncCode_800bf60c = MEMORY.ref(1, 0x800bf60cL, EnumRef.of(SyncCode.values()));
  public static final Value response_800bf60d = MEMORY.ref(1, 0x800bf60dL);

  public static final Value batch_800bf618 = MEMORY.ref(4, 0x800bf618L);
  public static final EnumRef<SyncCode> syncCode_800bf61c = MEMORY.ref(1, 0x800bf61cL, EnumRef.of(SyncCode.values()));
  public static final Value response_800bf61d = MEMORY.ref(1, 0x800bf61dL);

  public static final Value batch_800bf628 = MEMORY.ref(4, 0x800bf628L);
  public static final EnumRef<SyncCode> syncCode_800bf62c = MEMORY.ref(1, 0x800bf62cL, EnumRef.of(SyncCode.values()));
  public static final Value response_800bf62d = MEMORY.ref(1, 0x800bf62dL);

  public static final ArrayRef<CdlPacket> CdlPacket_800bf638 = MEMORY.ref(4, 0x800bf638L, ArrayRef.of(CdlPacket.class, DSL_MAX_COMMAND, 0x18, CdlPacket::new));

  public static final Value _800bf6f8 = MEMORY.ref(4, 0x800bf6f8L);
  public static final Value _800bf6fc = MEMORY.ref(4, 0x800bf6fcL);
  public static final Value cdlPacketIndex_800bf700 = MEMORY.ref(4, 0x800bf700L);

  public static final ArrayRef<Response> cdromResponseBuffer_800bf708 = MEMORY.ref(4, 0x800bf708L, ArrayRef.of(Response.class, DSL_MAX_COMMAND, 0x10, Response::new));

  public static final Value cdromResponseBufferIndex_800bf788 = MEMORY.ref(4, 0x800bf788L);

  public static final Pointer<RunnableRef> _800bf798 = MEMORY.ref(4, 0x800bf798L, Pointer.of(RunnableRef::new));
  public static final Pointer<BiConsumerRef<SyncCode, byte[]>> _800bf79c = MEMORY.ref(4, 0x800bf79cL, Pointer.of(BiConsumerRef::new));
  public static final Pointer<BiConsumerRef<SyncCode, byte[]>> cdromDmaInterruptSubCallbackPtr_800bf7a0 = MEMORY.ref(4, 0x800bf7a0L, Pointer.of(BiConsumerRef::new));
  public static final Pointer<BiConsumerRef<SyncCode, byte[]>> _800bf7a4 = MEMORY.ref(4, 0x800bf7a4L, Pointer.of(BiConsumerRef::new));
}
