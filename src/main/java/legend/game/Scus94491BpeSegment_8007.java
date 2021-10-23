package legend.game;

import legend.core.memory.Value;

import static legend.core.Hardware.MEMORY;

public final class Scus94491BpeSegment_8007 {
  private Scus94491BpeSegment_8007() { }

  public static final Value _8007a398 = MEMORY.ref(4, 0x8007a398L);
  public static final Value _8007a39c = MEMORY.ref(4, 0x8007a39cL);
  public static final Value _8007a3a0 = MEMORY.ref(4, 0x8007a3a0L);

  public static final Value _8007a3a8 = MEMORY.ref(4, 0x8007a3a8L);
  public static final Value _8007a3ac = MEMORY.ref(4, 0x8007a3acL);

  /** Number of vsyncs to wait for between frames (0 means 1) */
  public static final Value vsyncMode_8007a3b8 = MEMORY.ref(4, 0x8007a3b8L);

  public static final Value _8007a3c0 = MEMORY.ref(4, 0x8007a3c0L);
}
