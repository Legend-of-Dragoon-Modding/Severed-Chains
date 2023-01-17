package legend.game;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;

import static legend.core.GameEngine.MEMORY;

public final class Scus94491BpeSegment_8007 {
  private Scus94491BpeSegment_8007() { }

  /**
   * Only set for an instant after buttons are pressed
   * <ul>
   *   <li>0x01 - L2</li>
   *   <li>0x02 - R2</li>
   *   <li>0x04 - L1</li>
   *   <li>0x08 - R1</li>
   *   <li>0x10 - Triangle</li>
   *   <li>0x20 - Cross</li>
   *   <li>0x40 - Circle</li>
   *   <li>0x80 - Square</li>
   *   <li>0x1000 - Up</li>
   *   <li>0x2000 - Right</li>
   *   <li>0x4000 - Down</li>
   *   <li>0x8000 - Left</li>
   * </ul>
   */
  public static final Value joypadPress_8007a398 = MEMORY.ref(4, 0x8007a398L);
  /**
   * Remains set for the duration of the button press
   * <ul>
   *   <li>0x01 - L2</li>
   *   <li>0x02 - R2</li>
   *   <li>0x04 - L1</li>
   *   <li>0x08 - R1</li>
   *   <li>0x10 - Triangle</li>
   *   <li>0x20 - Cross</li>
   *   <li>0x40 - Circle</li>
   *   <li>0x80 - Square</li>
   *   <li>0x1000 - Up</li>
   *   <li>0x2000 - Right</li>
   *   <li>0x4000 - Down</li>
   *   <li>0x8000 - Left</li>
   * </ul>
   */
  public static final Value joypadInput_8007a39c = MEMORY.ref(4, 0x8007a39cL);
  /**
   * Only set for an instant after buttons are pressed, but repeats while button is held
   * <ul>
   *   <li>0x01 - L2</li>
   *   <li>0x02 - R2</li>
   *   <li>0x04 - L1</li>
   *   <li>0x08 - R1</li>
   *   <li>0x10 - Triangle</li>
   *   <li>0x20 - Cross</li>
   *   <li>0x40 - Circle</li>
   *   <li>0x80 - Square</li>
   *   <li>0x1000 - Up</li>
   *   <li>0x2000 - Right</li>
   *   <li>0x4000 - Down</li>
   *   <li>0x8000 - Left</li>
   * </ul>
   */
  public static final Value joypadRepeat_8007a3a0 = MEMORY.ref(4, 0x8007a3a0L);

  public static final IntRef _8007a3a8 = MEMORY.ref(4, 0x8007a3a8L, IntRef::new);

  public static final IntRef shopId_8007a3b4 = MEMORY.ref(4, 0x8007a3b4L).cast(IntRef::new);
  /** 60 FPS divisor (e.g. 2 means 30 FPS) */
  public static final IntRef vsyncMode_8007a3b8 = MEMORY.ref(4, 0x8007a3b8L, IntRef::new);
}
