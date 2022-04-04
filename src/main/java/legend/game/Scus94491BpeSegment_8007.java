package legend.game;

import legend.core.memory.Value;

import static legend.core.Hardware.MEMORY;

public final class Scus94491BpeSegment_8007 {
  private Scus94491BpeSegment_8007() { }

  /** Only set for an instant after buttons are pressed */
  public static final Value joypadPress_8007a398 = MEMORY.ref(4, 0x8007a398L);
  /** Remains set for the duration of the button press */
  public static final Value joypadInput_8007a39c = MEMORY.ref(4, 0x8007a39cL);
  /** Only set for an instant after buttons are pressed, but repeats while button is held */
  public static final Value joypadRepeat_8007a3a0 = MEMORY.ref(4, 0x8007a3a0L);

  public static final Value _8007a3a8 = MEMORY.ref(4, 0x8007a3a8L);
  public static final Value _8007a3ac = MEMORY.ref(4, 0x8007a3acL);

  /** Number of vsyncs to wait for between frames (0 means 1) */
  public static final Value vsyncMode_8007a3b8 = MEMORY.ref(4, 0x8007a3b8L);

  public static final Value _8007a3c0 = MEMORY.ref(4, 0x8007a3c0L);
}
