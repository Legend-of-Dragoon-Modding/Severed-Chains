package legend.core;

public enum ExceptionCause {
  INTERRUPT(0x0),
  LOAD_ADDRESS_ERROR(0x4),
  STORE_ADDRESS_ERROR(0x5),
  BUS_ERROR_FETCH(0x6),
  SYSCALL(0x8),
  BREAK(0x9),
  ILLEGAL_INSTR(0xa),
  COPROCESSOR_ERROR(0xb),
  OVERFLOW(0xc),
  ;

  public final int value;

  ExceptionCause(final int value) {
    this.value = value;
  }
}
