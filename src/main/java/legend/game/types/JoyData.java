package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.ConsumerRef;
import legend.core.memory.types.FunctionRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class JoyData implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef int00;
  public final UnsignedIntRef int04;
  public final UnsignedIntRef int08;
  public final Pointer<ArrayRef<JoyData>> joyDataPtr0c;
  public final Pointer<JoyData> joyDataPtr10;
  public final Pointer<ConsumerRef<JoyData>> commandSender;
  public final Pointer<FunctionRef<JoyData, Integer>> responseHandler;
  // Unknown
  public final Pointer<ArrayRef<UnsignedByteRef>> commandParams_byteArrPtr20;
  public final ArrayRef<UnsignedByteRef> commandParams_byteArr24;
  public final Pointer<ArrayRef<UnsignedByteRef>> bytePtr28;
  public final Pointer<ArrayRef<UnsignedByteRef>> ptrCommandParams_bytePtr2c;
  public final Pointer<ArrayRef<UnsignedByteRef>> bytePtr30;
  public final ByteRef byte34;
  public final ByteRef byte35;
  public final UnsignedByteRef commandParamCount_byte36;
  public final UnsignedByteRef command_byte37;
  public final UnsignedByteRef byte38;
  public final ByteRef byte39;
  // Unknown
  public final Pointer<ArrayRef<UnsignedByteRef>> responseBufferPtr_bytePtr3c;
  public final Pointer<ArrayRef<ByteRef>> bytePtr40;
  public final ByteRef responseBufferIndex_byte44;
  public final UnsignedByteRef responseBytesSent_byte45;
  public final UnsignedByteRef byte46;
  public final UnsignedByteRef byte47;
  public final UnsignedByteRef byte48;
  public final UnsignedByteRef byte49;
  public final UnsignedByteRef byte4a;
  // Unknown
  public final IntRef counter_int4c;
  public final ByteRef byte50;
  public final ArrayRef<UnsignedByteRef> setAnalogState44Params_byteArr51;
  public final ByteRef byte53;
  // Unknown
  public final ArrayRef<UnsignedByteRef> byteArr57;
  public final ArrayRef<UnsignedByteRef> getJoypadState46Params_byteArr5d;
  // Unknown - is the previous array bigger than 6 bytes?
  public final UnsignedByteRef bytee3;
  public final UnsignedByteRef bytee4;
  // Unknown
  public final UnsignedShortRef shorte6;
  public final UnsignedByteRef controllerType_bytee8;
  public final UnsignedByteRef bytee9;
  public final UnsignedByteRef byteea;
  public final UnsignedByteRef byteeb;
  public final UnsignedShortRef shortec;
  public final UnsignedShortRef shortee;

  public JoyData(final Value ref) {
    this.ref = ref;

    this.int00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.int04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this.int08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.joyDataPtr0c = ref.offset(4, 0x0cL).cast(Pointer.deferred(4, ArrayRef.of(JoyData.class, 4, 240, JoyData::new)));
    this.joyDataPtr10 = ref.offset(4, 0x10L).cast(Pointer.of(4, JoyData::new));
    this.commandSender = ref.offset(4, 0x14L).cast(Pointer.of(4, ConsumerRef::new));
    this.responseHandler = ref.offset(4, 0x18L).cast(Pointer.of(4, FunctionRef::new));

    this.commandParams_byteArrPtr20 = ref.offset(4, 0x20L).cast(Pointer.of(8, ArrayRef.of(UnsignedByteRef.class, 8, 1, UnsignedByteRef::new)));
    this.commandParams_byteArr24 = ref.offset(1, 0x24L).cast(ArrayRef.of(UnsignedByteRef.class, 4, 1, UnsignedByteRef::new));
    this.bytePtr28 = ref.offset(4, 0x28L).cast(Pointer.deferred(4, ArrayRef.of(UnsignedByteRef.class, 4, 1, UnsignedByteRef::new)));
    this.ptrCommandParams_bytePtr2c = ref.offset(4, 0x2cL).cast(Pointer.deferred(4, ArrayRef.of(UnsignedByteRef.class, 4, 1, UnsignedByteRef::new)));
    this.bytePtr30 = ref.offset(4, 0x30L).cast(Pointer.deferred(4, ArrayRef.of(UnsignedByteRef.class, 4, 1, UnsignedByteRef::new)));
    this.byte34 = ref.offset(1, 0x34L).cast(ByteRef::new);
    this.byte35 = ref.offset(1, 0x35L).cast(ByteRef::new);
    this.commandParamCount_byte36 = ref.offset(1, 0x36L).cast(UnsignedByteRef::new);
    this.command_byte37 = ref.offset(1, 0x37L).cast(UnsignedByteRef::new);
    this.byte38 = ref.offset(1, 0x38L).cast(UnsignedByteRef::new);
    this.byte39 = ref.offset(1, 0x39L).cast(ByteRef::new);

    this.responseBufferPtr_bytePtr3c = ref.offset(4, 0x3cL).cast(Pointer.deferred(1, ArrayRef.of(UnsignedByteRef.class, 35, 1, UnsignedByteRef::new)));
    this.bytePtr40 = ref.offset(4, 0x40L).cast(Pointer.deferred(1,ArrayRef.of(ByteRef.class, 37, 1, ByteRef::new)));
    this.responseBufferIndex_byte44 = ref.offset(1, 0x44L).cast(ByteRef::new);
    this.responseBytesSent_byte45 = ref.offset(1, 0x45L).cast(UnsignedByteRef::new);
    this.byte46 = ref.offset(1, 0x46L).cast(UnsignedByteRef::new);
    this.byte47 = ref.offset(1, 0x47L).cast(UnsignedByteRef::new);
    this.byte48 = ref.offset(1, 0x48L).cast(UnsignedByteRef::new);
    this.byte49 = ref.offset(1, 0x49L).cast(UnsignedByteRef::new);
    this.byte4a = ref.offset(1, 0x4aL).cast(UnsignedByteRef::new);

    this.counter_int4c = ref.offset(4, 0x4cL).cast(IntRef::new);
    this.byte50 = ref.offset(1, 0x50L).cast(ByteRef::new);
    this.setAnalogState44Params_byteArr51 = ref.offset(2, 0x51L).cast(ArrayRef.of(UnsignedByteRef.class, 2, 1, UnsignedByteRef::new));
    this.byte53 = ref.offset(1, 0x53L).cast(ByteRef::new);

    this.byteArr57 = ref.offset(6, 0x57L).cast(ArrayRef.of(UnsignedByteRef.class, 6, 1, UnsignedByteRef::new));
    this.getJoypadState46Params_byteArr5d = ref.offset(6, 0x5dL).cast(ArrayRef.of(UnsignedByteRef.class, 6, 1, UnsignedByteRef::new));

    this.bytee3 = ref.offset(1, 0xe3L).cast(UnsignedByteRef::new);
    this.bytee4 = ref.offset(1, 0xe4L).cast(UnsignedByteRef::new);

    this.shorte6 = ref.offset(2, 0xe6L).cast(UnsignedShortRef::new);
    this.controllerType_bytee8 = ref.offset(1, 0xe8L).cast(UnsignedByteRef::new);
    this.bytee9 = ref.offset(1, 0xe9L).cast(UnsignedByteRef::new);
    this.byteea = ref.offset(1, 0xeaL).cast(UnsignedByteRef::new);
    this.byteeb = ref.offset(1, 0xebL).cast(UnsignedByteRef::new);
    this.shortec = ref.offset(2, 0xecL).cast(UnsignedShortRef::new);
    this.shortee = ref.offset(2, 0xeeL).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
