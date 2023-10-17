package legend.game;

import legend.core.memory.types.IntRef;

import static legend.core.GameEngine.MEMORY;

public final class Scus94491BpeSegment_8007 {
  private Scus94491BpeSegment_8007() { }

  public static final IntRef clearRed_8007a3a8 = MEMORY.ref(4, 0x8007a3a8L, IntRef::new);

  public static final IntRef shopId_8007a3b4 = MEMORY.ref(4, 0x8007a3b4L).cast(IntRef::new);
  /** 60 FPS divisor (e.g. 2 means 30 FPS) */
  public static int vsyncMode_8007a3b8 = 1;
}
