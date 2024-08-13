package legend.game.scripting;

public enum OpType {
  YIELD(0, "yield"),
  REWIND(1, "rewind"),
  WAIT(2, "wait"),
  WAIT_CMP(3, "wait_cmp"),
  WAIT_CMP_0(4, "wait_cmp"),
  REWIND5(5, "rewind"),
  REWIND6(6, "rewind"),
  REWIND7(7, "rewind"),
  MOV(8, "mov"),
  SWAP_BROKEN(9, "swap_broken"),
  MEMCPY(10, "memcpy"),
  REWIND11(11, "rewind"),
  MOV_0(12, "mov"),
  REWIND13(13, "rewind"),
  REWIND14(14, "rewind"),
  REWIND15(15, "rewind"),
  AND(16, "and"),
  OR(17, "or"),
  XOR(18, "xor"),
  ANDOR(19, "andor"),
  NOT(20, "not"),
  SHL(21, "shl"),
  SHR(22, "shr"),
  ADD(24, "add"),
  SUB(25, "sub"),
  SUB_REV(26, "sub_rev"),
  INCR(27, "incr"),
  DECR(28, "decr"),
  NEG(29, "neg"),
  ABS(30, "abs"),
  MUL(32, "mul"),
  DIV(33, "div"),
  DIV_REV(34, "div_rev"),
  MOD(35, "mod"),
  MOD_REV(36, "mod_rev"),
  MUL_12(40, "mul_12"),
  DIV_12(41, "div_12"),
  DIV_12_REV(42, "div_12_rev"),
  MOD43(43, "mod"),
  MOD_REV44(44, "mod_rev"),
  SQRT(48, "sqrt"),
  RAND(49, "rand"),
  SIN_12(50, "sin_12"),
  COS_12(51, "cos_12"),
  ATAN2_12(52, "atan2_12"),
  CALL(56, "call"),
  JMP(64, "jmp"),
  JMP_CMP(65, "jmp_cmp"),
  JMP_CMP_0(66, "jmp_cmp"),
  WHILE(67, "while"),
  JMP_TABLE(68, "jmp_table"),
  GOSUB(72, "gosub"),
  RETURN(73, "return"),
  GOSUB_TABLE(74, "gosub_table"),
  DEALLOCATE(80, "deallocate"),
  DEALLOCATE82(82, "deallocate"),
  DEALLOCATE_OTHER(83, "deallocate_other"),
  FORK(86, "fork"),
  FORK_REENTER(87, "fork_reenter"),
  CONSUME(88, "consume"),
  NOOP_96(96, "debug96"),
  NOOP_97(97, "debug97"),
  NOOP_98(98, "debug98"),
  DEPTH(99, "depth"),
  ;

  public static OpType byOpcode(final int opcode) {
    for(final OpType op : OpType.values()) {
      if(op.opcode == opcode) {
        return op;
      }
    }

    return null;
  }

  public final int opcode;
  public final String name;

  OpType(final int opcode, final String name) {
    this.opcode = opcode;
    this.name = name;
  }

  public boolean hasHeaderParam() {
    return
      this == WAIT_CMP ||
      this == WAIT_CMP_0 ||
      this == CALL ||
      this == JMP_CMP ||
      this == JMP_CMP_0 ||
      this == NOOP_96;
  }
}
